#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Nov  4 22:30:13 2019
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Code to run performance evaluation and save results.
Note:       a) Save results in a ".xlsx" file.
            b) Tests with all models in pool.

Fix: 
Status: Validating         
Next:
       
"""

# Imports
import os,sys
sys.path.append(os.getcwd()[0:-11]) # run into tests
import matplotlib.pyplot as plt
import pandas as pd
import objects as obj
import utils.util as ut
import numpy as np
import datetime as dt
import math
import modelSeletor as ms
import warnings
import re # REGEX Operation

warnings.filterwarnings("ignore")

# Data source, directory data
#dataFile = u'Demanda corrediça.xlsx'
dataFile = 'M3C.xls'
path = '../Dataset/'
imagePath = '../Images/Error/'
resultsPath = '../Results/'
outputFileName = None
m3 = dataFile=='M3C.xls'

# Experiment configuration
metrics= ['MASE']#,'RMSE','MAPE']
horizon = 1
frequency = ['M','W','D']
modelsList = ['NAIVE','SES','HOLT','AR','CR','CF1']
proportionList = [60,20,20]
combinationType= ['errorBased','equal']

colors = np.random.rand(len(modelsList),3) # to plot
data = pd.read_excel(path+dataFile,None)

if not m3:
    
    # Specific corrediça spreadsheet operations
    outputFileName = 'performance_corredica'
    sheet_names = ['2017','2018','2019'] # For corrediça spreadsheet
    filiais = pd.read_excel(path+dataFile,'2017')
    filiais = filiais['Unidade'].str.replace("  +","") #remove space
    names = filiais.unique()
    names.sort()
    #names = ['Porto Velho','São Paulo'] # coment this line for executions
    
else:
    
    # Specific to M3-Competition monthly data
    # Extract series conform to https://doi.org/10.1016/j.jbusres.2015.03.028
    outputFileName = 'performance_M3'
    frequency = ['M']
    data = data.pop('M3Month')
    #data = data[data['N']>=126]
    names = data['Series'].unique()
    names.sort()
    names = ['N2801','N1675','N2750','N1403','N1711','N1527','N1514']#,'N1404'] # coment this line for executions

# To compute time of executions
startTime = dt.datetime.now()
totalTime = None

# Excel frame structure, changes os this data may affects buildSpreadSheet's
# behavior on Utils class.
cols = ['Series Name','SES','HOLT','NAIVE',
        'AR','CR','CF-Mean','CF-Error',
        'Model','Mean','Std.']
frame = pd.DataFrame(columns = cols)
# To save on disk
writer = pd.ExcelWriter(resultsPath+outputFileName+'.xls')

for metric in metrics:
    
    for freq in frequency:
        
        frame = pd.DataFrame(columns = cols)
        
        for serieName in names:
            
            print()
            print('Series:'+serieName)
        
            if not m3:
                # Get data
                serie = ut.Utils.readCorredica(serieName,data,sheet_names)
                # Proccess series on same frequency
                sampleObj = serie.resample(freq)
                newSeries  = sampleObj.sum()
            else:
                newSeries = ut.Utils.buildM3DataFrame(data,serieName)
                
            # Set intervals for evaluation
            # 1--------T1--------T2--------->T
            T1 = int(len(newSeries)*(proportionList[0]/100))
            T2 = int(len(newSeries)*((proportionList[0]+proportionList[1])/100))
           
            # Fit model on validation data and get best
            selector = ms.ModelSelector(data=newSeries,
                                        models=modelsList,
                                        horizon = 18,
                                        start=len(newSeries)-18,
                                        combType=combinationType[0],
                                        combMetric=metric)
            selector.fit()
            # Add to DataFrame
           
            model = selector.getModelByName('CF-Error')
            selector.removeModel('CF-Error')
            selector.combType = 'equal'
            selector.combinationFit()   
            selector.modelsResult.append(model)
            
            '''line = {}
            for m in selector.modelsResult:
                
                errorValue = obj.ForecastErro.getValueByMetricName(m.error,metric)
                line[m.model] = errorValue
                
            line['Series Name'] = serieName
            
            frame = frame.append(line,ignore_index=True)'''
        
            '''legends = []
            for m in frame.columns[1:8]:
                
                modelName = m
                meanError = frame[m].mean()
                std = frame[m].std()
                frame = frame.append({'Model':modelName,
                                                    'Mean':meanError,
                                                    'Std.':std},
                                                  ignore_index=True)
                legends.append(modelName)
                plt.title('Error -'+metric)
                plt.xlabel('Series no.')
                plt.ylabel('Value')
                plt.plot(frame[m].values)'''
                
            m1 = selector.modelsResult[2]
            plt.title('Series no. '+serieName)
            plt.plot(newSeries,'black')
            plt.plot(m1.trainingPrediction.append(m1.testPrediction),'blue')
            #plt.plot(m1.testPrediction,'blue')
            plt.plot(m1.forecastDemand,'blue')
            m2 = selector.modelsResult[0]
            plt.plot(m2.trainingPrediction.append(m2.testPrediction),'green')
            #plt.plot(m2.testPrediction,'green')
            plt.plot(m2.forecastDemand,'green')
            m3 = selector.modelsResult[6]
            plt.plot(m3.trainingPrediction.append(m3.testPrediction),'brown')
            #plt.plot(m3.testPrediction,'silver')
            plt.plot(m3.forecastDemand,'brown')
            
            
            plt.gcf().autofmt_xdate()
    
    
    
            plt.legend(['Real',
                        m1.model,
                        m2.model,
                        m3.model])
            plt.savefig(imagePath+metric+freq+outputFileName+serieName+'.png',dpi = 800)
            plt.close()
            #frame.to_excel(excel_writer=writer,sheet_name=freq+metric,index=False)
    
#writer.save()

#old
'''for freq in frequency:
    print(freq)
    
    for prop in proportionList:
        
        proportionText = str(prop)
        proportionText = re.sub('[\,\[\] ]','',proportionText)
        frame = pd.DataFrame(columns=cols)
        
        print(proportionText)
        
        for serieName in names:
            
            print()
            print('Series:'+serieName)
            
            if not m3:
                # Get data
                serie = ut.Utils.readCorredica(serieName,data,sheet_names)
                # Proccess series on same frequency
                sampleObj = serie.resample(freq)
                newSeries  = sampleObj.sum()
            else:
                newSeries = ut.Utils.buildM3DataFrame(data,serieName,30)
                
            # Set intervals for evaluation
            # --------T1--------T2--------->T
            T1 = int(len(newSeries)*(prop[0]/100))
            T2 = int(len(newSeries)*((prop[0]+prop[1])/100))
            print(T1)
            print(T2)
            print(len(newSeries))
            # Fit model on validation data and get best
            selector = ms.ModelSelector(data=newSeries[:T2],
                                        models=modelsList,
                                        start=T1,
                                        combType=combinationType,
                                        combMetric=metric)
            selector.fit()
            bestValidation,valueValidation = selector.getBestByMetric(metric)
            
            combinationModel = selector.getModelByName('CF1')
            plt.plot(newSeries[T1:T2],'black')
            conjuntoDeTeste = combinationModel.testPrediction
            plt.plot(conjuntoDeTeste,'blue')
            plt.plot(bestValidation.testPrediction,'red')
            plt.title('Corrediça em '+serieName)
            plt.xlabel('Time')
            plt.ylabel('Demand')
            plt.gcf().autofmt_xdate()
            plt.savefig(imagePath+serieName+freq+proportionText+
                        '- corredica.png',dpi = 800)
            plt.close()
            
            #Get metric values on test data
            selector = ms.ModelSelector(data=newSeries,
                                        models=modelsList,
                                        start=T2,
                                        combType=combinationType,
                                        combMetric=metric)
            selector.fit()
            bestTest,valueTest = selector.getBestByMetric(metric)
            
            # Add to DataFrame
            line = {
                    'Series Name':serieName,
                    'Size':len(newSeries),
                    'Validation': bestValidation.model,
                    'Error':valueValidation,
                    'Test': bestTest.model,
                    'Error Test': valueTest,
                    }
            
            frame = frame.append(line,ignore_index=True)
        
        # Build xlsx
        ut.Utils.buildSpreadSheet(proportionText,frame,freq,writer)
        
writer.save()'''
totalTime = dt.datetime.now() - startTime
print()
print('Time to run tests:')
print(totalTime)
print()