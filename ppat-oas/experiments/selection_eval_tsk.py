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
Next: Redo with full data from TSK.
       
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

#warnings.filterwarnings("ignore")   

# Data source, directory data
file = 'DATA.xls'
path = '../dataset/'
imagePath = '../images/'
resultsPath = '../experiments/results/'
outputFileName = None

# Experiment configuration
metrics= ['RMSE','MASE']
horizon = 1
freq = 'M'
modelsList = ['HOLT','NAIVE','SES','AR','CR','ANN','ELM','CFE','CFM']
proportionList = [60,20,20]
roundPlaces = 3

data = pd.read_excel(path+file)

# TSK full data
outputFileName = 'selection_full_data'
grouped = data[['STATIONS','PARTS']].groupby(['STATIONS','PARTS'])
lengthCutOff = 20
ADIcutOff = 14
totalSeries = 1460
processedSeries = 0
totalIntermittent = 0
    
# To save on disk
cols = ['REMOTE-STATION','PART','ANN','AR','AUTO','CFE','CFM','CR','ELM','HOLT','NAIVE','SES']
colsSummary = ['MODEL','MEAN','STD']
frame = pd.DataFrame(columns = cols)
writer = pd.ExcelWriter(resultsPath+outputFileName+'.xls')

# To compute time of executions
startTime = dt.datetime.now()
totalTime = None

for trial in range(10):

    for metric in metrics:
        
        frame = pd.DataFrame(columns = cols)
        
        for station, part in grouped:
            # Step 1: create time series from the spreadsheet
            temp = data.loc[(data['STATIONS'] == station[0]) & (data['PARTS'] == station[1])]
            #temp = data.loc[(data['STATIONS'] == 'MANAUS') & \
            #                 (data['PARTS'] == 'CORREDICA PADRONIZADA P/PORTA TKE')]
            serie = pd.Series(temp['DEMANDS'].values, temp['DATE'])
            serie = serie.resample(freq).sum()
            length = len(serie)
            adi = round(ut.Utils.getADI(serie), roundPlaces)
    
            # Step 2: filter by length and record features of series
            if length > lengthCutOff and adi / length < 0.35 :
                
                print('\nRemote Station:' + station[0] + ', part:' + station[1] + '.')
                
                # Set intervals for evaluation
                # 1--------T1--------T2--------->T
                T1 = int(len(serie)*(proportionList[0]/100))
                T2 = int(len(serie)*((proportionList[0]+proportionList[1])/100))
                
                # Fit model on validation data and get best
                validation = ms.ModelSelector(data=serie[:T2],
                                            models=modelsList,
                                            start=T1,
                                            combMetric=metric)
                
                test = ms.ModelSelector(data = serie,
                                            models=modelsList,
                                            start=T2,
                                            combMetric=metric)
                
                processedSeries+=1
                print('Progress:'+
                      str(round((processedSeries/totalSeries)*100,2))+'%.')
                print('Validation step ...')
                validation.fit()
                validation.intervalUC = 'TEST'
                validation.weights = None
                test.weights = validation.setWeights()
                
                print('Test step...')
                test.fit()
                
                automatic,value = validation.getBestByMetric(metric)
                automatic = test.getModelByName(automatic.model)
                
                # Build spreadsheet data
                line = {}
                line['REMOTE-STATION'] = station[0]
                line['PART'] = station[1]
                
                for m in test.getModelResults():
                    
                    line[m.model] = obj.ForecastErro.getValueByMetricName(
                        m.error,
                        metric,
                        'TEST')
                
                line['AUTO'] = obj.ForecastErro.getValueByMetricName(
                        automatic.error,
                        metric,
                        'TEST')
                           
                frame = frame.append(line,ignore_index=True)
                
        frame.to_excel(excel_writer=writer,
                       sheet_name='T'+str(trial)+'_'+metric,
                       index=False)
            
        # Summary of the experiment
        frameAux = pd.DataFrame(columns = colsSummary)
        line = {}
        for col in frame.columns[2:]:
            
            line['MODEL'] = col
            line['MEAN']  = round(frame[col].mean(),roundPlaces)
            line['STD'] = round(frame[col].std(),roundPlaces)
            frameAux = frameAux.append(line,ignore_index=True)
            
        frameAux.to_excel(excel_writer=writer,
                          sheet_name='T'+str(trial)+'_'+metric+'_SUM',
                          index=False)

    writer.save()

totalTime = dt.datetime.now() - startTime
print()
print('Time to run tests:')
print(totalTime)
print()   