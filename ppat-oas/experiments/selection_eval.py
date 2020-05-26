#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Nov  4 22:30:13 2019
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Code to run model selection experiment and save results.
Note:       a) Save results in a ".xlsx" file.
            b) Tests with all models in pool.

Fix: .
Status:.       
Next: Refazer com novos dados.
       
"""

# Imports
import os,sys
sys.path.append(os.getcwd()[0:-11]) # run into tests
import matplotlib.pyplot as plt
import pandas as pd
import ws.objects as obj
import utils.util as ut
import numpy as np
import datetime as dt
import math
import ws.modelSelector as ms
import warnings
import re # REGEX Operation

warnings.filterwarnings("ignore")

def improvedScore(dataFrame,benchmark):
    
    numOfImproved = 0 
    
    for i in range(len(dataFrame)):
        
        best = dataFrame.iloc[[i]]['BEST'].values
        best = dataFrame.iloc[[i]][best].values
        mean = dataFrame.iloc[[i]][benchmark].values[0]
       
        if best < mean:
            numOfImproved +=1
       
    return (numOfImproved/(len(dataFrame)))*100    

# Data source, directory data
dataFile = u'Demanda corrediça.xlsx'
#dataFile = 'M3C.xls'
path = '../dataset/'
imagePath = '../images/'
resultsPath = '../experiments/results/'
outputFileName = None
m3 = dataFile=='M3C.xls'

# Experiment configuration
metrics= ['RMSE','MASE']#,'MAPE']
horizon = 1
frequency = ['M']#,'W','D']
modelsList = ['NAIVE','SES','HOLT','AR','CR','CF1','ELM','ANN']
proportionList = [60,20,20]
combinationType= ['errorBased','equal']

data = pd.read_excel(path+dataFile,None)

if not m3:
    
    # Specific corrediça spreadsheet operations
    outputFileName = 'selection_corredica_teste'
    sheet_names = ['2017','2018','2019'] # For corrediça spreadsheet
    filiais = pd.read_excel(path+dataFile,'2017')
    filiais = filiais['Unidade'].str.replace("  +","") #remove space
    names = filiais.unique()
    names.sort()
    #names = ['São Paulo','Rio de Janeiro'] # comment this line for executions
    
else:
    
    # Specific to M3-Competition monthly data
    # Extract series conform to https://doi.org/10.1016/j.jbusres.2015.03.028
    outputFileName = 'selection_M3_FULL'
    frequency = ['M']
    data = data.pop('M3Month')
    #data = data[data['N']>=60]
    names = data['Series'].unique()
    names.sort()
    names = ['N2801','N1404','N1417','N1793','N1428','N1468','N1495'] # coment this line for executions

# To compute time of executions
startTime = dt.datetime.now()
totalTime = None

# Excel frame structure, changes on this data may affects buildSpreadSheet's
# behavior on Utils class.
cols = ['Series Name','AR','AUTO','CF1','CF1w','CR','HOLT','NAIVE','SES','ANN','ELM']
colsSummary = ['MODEL','MEAN','STD']
frame = pd.DataFrame(columns = cols)
# To save on disk
writer = pd.ExcelWriter(resultsPath+outputFileName+'.xls')
writerSummary = pd.ExcelWriter(resultsPath+outputFileName+'summary.xls')

for metric in metrics:
    
    for freq in frequency:
        
        frame = pd.DataFrame(columns = cols)
        frameSummary = pd.DataFrame(columns = colsSummary)
        
        for serieName in names:
            
            print()
            print('Series:'+serieName)
        
            if not m3:
                # Get data
                serie = ut.Utils.readCorredica(serieName,data,sheet_names)
                # Process series on same frequency
                sampleObj = serie.resample(freq)
                newSeries  = sampleObj.sum()
            else:
                newSeries = ut.Utils.buildM3DataFrame(data,serieName)
            
            # Set intervals for evaluation
            # 1--------T1--------T2--------->T
            T1 = int(len(newSeries)*(proportionList[0]/100))
            T2 = int(len(newSeries)*((proportionList[0]+proportionList[1])/100))
            
            #T1 = len(newSeries) - 36
            #T2 = len(newSeries) - 18
            
            # Fit model on validation data and get best
            validation = ms.ModelSelector(data=newSeries[:T2],
                                        models=modelsList,
                                        start=T1,
                                        combType=combinationType[0],
                                        combMetric=metric)
            
            test = ms.ModelSelector(data = newSeries,
                                        models=modelsList,
                                        start=T2,
                                        combType=combinationType[0],
                                        combMetric=metric)
            
            validation.fit()
            combinationByError = validation.getModelByName('CF1')
            validation.removeModel('CF1')
            
            # Adjust weights for the test interval             
            validation.intervalUC = 'TEST'
            validation.setWeights()
            test.weights = validation.weights
            
            # Fit combination strategy using equal weights        
            validation.combType = 'equal'
            validation.weights = None
            validation.combinationFit()   
            
            automaticChosen,value = validation.getBestByMetric(metric)
            combErrorValue = obj.ForecastErro.getValueByMetricName(combinationByError.error,metric)
            
            # Both error-based or equal have the same name.
            if combErrorValue < value:
                automaticChosen = combinationByError
                automaticChosen.model = 'CF1w'
                value = combErrorValue 
            
            test.fit()
            combinationByError = test.getModelByName('CF1')
            test.removeModel('CF1')
            combinationByError.model = 'CF1w'
            test.combType = 'equal'
            test.weights = None
            test.combinationFit()
            test.modelsResult.append(combinationByError)
            
            # Add to DataFrame
            line = {}
            
            line['Series Name'] = serieName
            
            for m in test.modelsResult:
                
                errorValue = obj.ForecastErro.getValueByMetricName(m.error,metric,'TEST')
                line[m.model] = errorValue
                
            line['AUTO'] = automaticChosen.model
            
            print(line)
             
            frame = frame.append(line,ignore_index=True)
        
        #improvedPct = improvedScore(frame,'NAIVE')
        #frame = frame.append({'% Improved':round(improvedPct,4)},ignore_index=True)
        
        #legends = []
        for m in frame.columns[1:11]:
            
            print(m)
            if m!='AUTO':
                modelName = m
                meanError = frame[m].mean()
                std = frame[m].std()
                print(std)
                frameSummary = frameSummary.append({'MODEL':modelName,
                                  'MEAN':meanError,
                                  'STD':std},
                                  ignore_index=True)
            
        frame.to_excel(excel_writer=writer,sheet_name=freq+metric,index=False)
        frameSummary.to_excel(excel_writer=writerSummary,sheet_name=freq+metric,index=False)
        
writer.save()
writerSummary.save()

totalTime = dt.datetime.now() - startTime
print()
print('Time to run tests:')
print(totalTime)
print()