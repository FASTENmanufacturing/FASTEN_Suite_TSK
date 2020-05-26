'''
Created on 24 de jun de 2019

Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Utilities for handle multiple datasets files (xlsx,csv)
Note:       Works with Pandas.DataFrame object by applying methods 
            for time series (Panda.Series) extraction
'''

import pandas as pd
import numpy as np
import math
from sklearn.metrics import mean_squared_error

decimalPlaces=3 # Common to all methods

class Utils:
    
    def __init__(self,dataSetsPath,fileName):
        self.dataSetsPath = dataSetsPath
        self.dataFrameTable = None
        self.fileName = fileName
        self.fileToDataFrame()
        
    def getTable(self):
        return self.dataFrameTable
    
    def setTable(self,df):
        self.dataFrameTable = df

    def fileToDataFrame(self):
    
        fileType = self.fileName[self.fileName.rfind('.')+1:]
        
        if fileType in ('xlsx','xls'):
            self.dataFrameTable = pd.read_excel(self.dataSetsPath+self.fileName)
        elif fileType == 'csv':
            self.dataFrameTable = pd.read_csv(self.dataSetsPath+self.fileName) 
        else:
            raise ("Unexpected file type!")

    def getItemRows(self,itemName,columnName):
        # Returns a subset rows of DataFrame for a specified item (itemName)
    
        try:
            allItems = self.dataFrameTable[columnName]
            allItems = allItems[allItems == itemName].index
            allItems = self.dataFrameTable.iloc[allItems] 
            return allItems
        except TypeError:
            raise ('Must load file first.')
        
    @staticmethod
    def getTimeSeries(dataFrameTable,columnSort,columnValue):
        # Returns rows for a specific item, ordered by columnSort
        
        dataFrame = dataFrameTable.sort_values(by=[columnSort],kind='mergesort')
        return dataFrame[[columnSort,columnValue]]
    
    @staticmethod
    def getTimeSeriesData(dataFrame,timeFreq):
        
        # Step 1: DataFrame to Series and group 'duplicate' observations 
        timeSeries = pd.Series(dataFrame.iloc[:,1].array,dataFrame.iloc[:,0])
        timeSeries = timeSeries.groupby(timeSeries.index).sum()
        
        # Step 2: Create new time series with zero values to combine
        maxDate = dataFrame.iloc[:,0].max().replace(day=1)
        minDate = dataFrame.iloc[:,0].min().replace(day=1)
        newSerieIndex = pd.date_range(minDate,maxDate,freq=timeFreq)
        newSerieData = np.zeros(len(newSerieIndex))
        newSeries = pd.Series(newSerieData,newSerieIndex)
        
        # Step 3: Combine and group by date
        newSeries = newSeries.combine(timeSeries, max, fill_value=0)
        newSeries = newSeries.to_period(freq=timeFreq,copy=False)
        newSeries = newSeries.groupby(newSeries.index).sum()
         
        return newSeries
    
    '''
    Further information about formula, see:
        Constantino et al. Spare parts management for irregular demand items.
        Omega. V. 81, p. 57-66, 2018. ISSN 0305-0483.
    Valid only for getADI and getCV functions.
    if CV <= 0.49 and ADI <= 1.32 : smooth.
    if CV <= 0.49 and ADI > 1.32 : intermittent
    if CV > 0.49 and ADI <= 1.32: erratic
    if CV > 0.49 and ADI > 1.32: lumpy
    '''
    @staticmethod
    def getCV(timeSeries):
        
        mean = timeSeries[timeSeries > 0].mean()
        std = timeSeries[timeSeries > 0].std()
        return std/mean
    
    @staticmethod
    def getADI(timeSeriesProcessed):
        
        timeSeries = timeSeriesProcessed
        newIndex = pd.Index(range(1, len(timeSeries)+1))
        timeSeries = pd.Series(data=timeSeries.to_numpy(),index=newIndex)
        timeSeries = timeSeries[timeSeries>0]
        timeSeries = pd.Series(data=timeSeries.index)
        differences = timeSeries.diff()
        sumIntervals = differences.sum() 
        result = sumIntervals/(differences.size-1)     
        
        return result
    
    @staticmethod
    def getDemandProbability(timeSeries):
        
        nonZeros = timeSeries[timeSeries > 0]
        p = nonZeros.size/timeSeries.size
        return p
    
    @staticmethod
    def getTimeSeriesStats(timeSeriesProcessed):
        # Creates a table's row with statistical data about an item
        
        min = timeSeriesProcessed.min()
        max = timeSeriesProcessed.max()
        startDate = timeSeriesProcessed.keys()[0]
        std = round(timeSeriesProcessed[timeSeriesProcessed > 0].std(),
                    decimalPlaces)
        mean = round(timeSeriesProcessed[timeSeriesProcessed > 0].mean(),
                     decimalPlaces)
        ADI = round(Utils.getADI(timeSeriesProcessed),
                    decimalPlaces)
        CV = round(Utils.getCV(timeSeriesProcessed),
                   decimalPlaces)
        prob = round(Utils.getDemandProbability(timeSeriesProcessed),
                     decimalPlaces)
    
        data = {'items':None,'min':[min],'max':[max],'mean':[mean],
                'std':[std],'ADI':[ADI],'CV':[CV],'prob':[prob],
                'startDate':startDate}
        data = pd.DataFrame(data)
    
        return data
    
    @staticmethod
    def getSummaryTable(spreadSheet,columnName,columnOrder, \
                    timeFreq,columnValue):
        # Creates a statistical summary in a table format.
    
        data = pd.DataFrame()
    
        for item in spreadSheet.getTable()[columnName].unique():
            
            temp = spreadSheet.getItemRows(item,columnName)
            temp = spreadSheet.getTimeSeries(temp, columnOrder,columnValue)
            temp = spreadSheet.getTimeSeriesData(temp, timeFreq)
            row = Utils.getTimeSeriesStats(temp)
            row.iat[0,0] = item
            data = data.append(row)
            
        return data
    
    '''
    Further information about procedure, see:
        Willemain Thomas R. et all. A new approach to forecasting intermittent
        demand for service parts inventories.
        International Journal of Forecasting.
        V. 81, p. 57-66, 2018. ISSN 0305-0483.
    Description: bootstrap method for purposes of creates a life-like
                time series.
    '''
    @staticmethod
    def getBootStrapSeries(series,size,method=''):
        
        values = series[series>0] 
        Z = values.std()
        t=[]
        min = values.min()
        max = values.max()
        
        if method == 'simple': #with raplecement
            
            t = np.random.choice(values,size) 
            
        elif method == 'jitter': # Willemain approximation
            
            for i in range(size):
                
               x = np.random.choice(values)
               jittered = 1+int(x+Z*math.sqrt(x))
                    
               if jittered <= 0:
                   t.append(x)
               else:
                   t.append(jittered)
                     
        else:
            t = np.random.randint(min+1,max+1,size)
        
        return  pd.Series(t) 
    

    def getTSByName(self,itemName):
        # Specific to TSK spreadsheet.
        
        columnName = u'Peça'# u'text': unicode
        columnValue = 'Demanda'
        columnOrder = u'Data Emissão NF'
        s = self.getItemRows(itemName,columnName)
        s = Utils.getTimeSeries(s,columnOrder,columnValue)
        s = Utils.getTimeSeriesData(s,'M')
        return s
    
    @staticmethod
    def mape(y,y_hat):
       # Param: y = real value, y_hat = predicion.
       # Return: Mean Absolute Percentage Error
       # Note: problem if y has zero values
       
       return np.mean(np.abs((y - y_hat) / y)) * 100
   
    @staticmethod
    def mase(training_series, testing_series, prediction_series):
        """
        Computes the MEAN-ABSOLUTE SCALED ERROR forcast error for univariate time series prediction.
        
        See "Another look at measures of forecast accuracy", Rob J Hyndman
        
        parameters:
            training_series: the series used to train the model, 1d numpy array
            testing_series: the test series to predict, 1d numpy array or float
            prediction_series: the prediction of testing_series, 1d numpy array (same size as testing_series) or float
            absolute: "squares" to use sum of squares and root the result, "absolute" to use absolute values.
        
        """
        n = training_series.shape[0]
        d = np.abs(np.diff( training_series) ).sum()/(n-1)
        errors = np.abs(testing_series - prediction_series )
        
        return errors.mean()/d
    
      
    # RMSE calculation
    @staticmethod
    def rmse (training_series,prediction_series):
        ScoreMSE = mean_squared_error(training_series, prediction_series)
        ScoreRMSE = math.sqrt(ScoreMSE)
        
        return  ScoreRMSE
   
   
    @staticmethod
    def listDictToPandas(listOfDict,frequency='M'):
        
        demands = []
        dateString = []

        for m in listOfDict:
            demands.append(float(m['demandValue']))
            dateString.append(m['demandDate'])
        
        newSeries = pd.Series(demands,
                              pd.DatetimeIndex(data=dateString,dayfirst=True))
        
        sampleObj = newSeries.resample(frequency)
        newSeries  = sampleObj.sum()
        return newSeries        
      
    @staticmethod
    def readCorredica(filial,data,listOfsheet):
    # Now this function reads the file and returns a series as was read.
        temp = pd.Series()
        
        for sheet in listOfsheet:
            
            spr = data[sheet]
            spr['Unidade'] = spr['Unidade'].str.replace("  +","") #remove whitespace
            rows = spr[spr['Unidade'] == filial] # specified branch only
            values = rows['Quantidade'].values
            dates = pd.DatetimeIndex(data=rows['Data'].values,
                                   dayfirst=True)
            timeSeries = pd.Series(values,dates,'double')
            temp = temp.append(timeSeries)
        return temp #timeSeries
    
    @staticmethod
    def buildSpreadSheet(proportionText,frame,frequency,excelWriter):
        #
        
        correct = frame[frame['Validation'] == frame['Test']]
        hits = len(correct)
        validationScores = frame.groupby(['Validation'],as_index=False).count()
        validationScores = validationScores[['Validation','Series Name']]
        validationScores.columns = ['Model-V','Validation Acc. (%)']
        
        # Insert columns, validation first
        frame.insert(len(frame.columns),'-','')
        frame.insert(len(frame.columns),'Model-V',validationScores['Model-V'].T)
        frame.insert(len(frame.columns),
                     'Validation Acc. (%)',
                     round(validationScores['Validation Acc. (%)'].T/len(frame)*100,decimalPlaces))
        
        # Insert columns, test
        testScores = frame.groupby(['Test'],as_index=False).count()
        testScores = testScores[['Test','Series Name']]
        testScores.columns = ['Model-T','Test Acc. (%)']
        
        frame.insert(len(frame.columns),'.','')
        frame.insert(len(frame.columns),'Model-T',testScores['Model-T'].T)
        frame.insert(len(frame.columns),
                     'Test Acc. (%)',
                     round(testScores['Test Acc. (%)'].T/len(frame)*100,decimalPlaces))        
        
        frame.insert(len(frame.columns),
                     'Correct choose (%)',
                     pd.DataFrame([round(hits/len(frame)*100,decimalPlaces)])) 
        
        
        frame.to_excel(excel_writer=excelWriter,
                       sheet_name=frequency+proportionText,
                       index=False)
        
    @staticmethod
    def buildM3DataFrame(dataFrame,seriesName,size=None):
        '''
            Get monthly data from M3 data set and return a Pandas.Series.
        '''
        
        # Get specific row
        newDF = dataFrame[dataFrame['Series']==seriesName]
       
        # To initialize date
        
        startYear = str(newDF['Starting Year'].values[0])
        month = str(newDF['Starting Month'].values[0])
        if startYear == '0':
            startYear = '1995'
            month = '1'
        
        startDate = month+'/'+startYear
        if size==None:
            size = newDF['N'].values[0]
            
        # Create new series
        dates = pd.date_range(start=startDate,periods=size,freq='M') #index
        values = newDF.iloc[0,6:size+6].values #values
        ts = pd.Series(values,dates,'double')
        
        return ts
    
    @staticmethod
    def improvedScore(dataFrame,benchmark):
        # Used by selection_eval_tsk.py and selection_eval.py
        numOfImproved = 0 
        
        for i in range(len(dataFrame)):
            
            best = dataFrame.iloc[[i]]['BEST'].values
            best = dataFrame.iloc[[i]][best].values
            mean = dataFrame.iloc[[i]][benchmark].values[0]
           
            if best < mean:
                numOfImproved +=1
           
        return (numOfImproved/(len(dataFrame)))*100 
           
         