#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct  7 12:02:06 2019

@author: saymongb
"""

# coding=UTF-8
'''

Arquivo gerado para testes gerais
Pŕoximo: .

'''
# Fix import problems when running scripts
import sys,os,string
idx = os.getcwd().rfind('/')
sys.path.append(os.getcwd()[:idx])

import matplotlib.pyplot as plt
import pandas as pd
import math
import ws.models_manager as ms
import utils.util as ut
import datetime as dt
import ws.objects as obj
import statsmodels.stats.diagnostic as lj

# Data definition, global scope
file = 'DATA.xls'
#file = u'Demanda corrediça.xlsx'
path = '../dataset/'
imagePath = '../images/'
resultsPath = '../experiments/results/'
outputFileName = 'BENCHMARK_EVALUATION'
freq = 'M'
models = ['NAIVE','SES','HOLT','AR','CR','ANN','ELM','CFE','CFM']

#for i in [1]:
#    md = ms.ModelSelector([1,3,4,5,7,6,15,7,18,22,10,25,15,38,25,28,30,22,23],1,['AUTO'])
#    md.arimaOptimization()
#    break
# For spreadsheet output
writer = pd.ExcelWriter(resultsPath+outputFileName+'.xls',datetime_format='hh:mm:ss.000000')
cols = ['STATIONS','PARTS','LENGTH','MAX','MEAN','STD','ADI','CV','TOTAL-TIME',
        'SES-TN','SES-TEST','SES-RMSE','SES-MASE',
        'NAIVE-TN','NAIVE-TEST','NAIVE-RMSE','NAIVE-MASE',
        'HOLT-TN','HOLT-TEST','HOLT-RMSE','HOLT-MASE',
        'CR-TN','CR-TEST','CR-RMSE','CR-MASE',
        'AR-TN','AR-TEST','AR-RMSE','AR-MASE',
        'ANN-TN','ANN-TEST','ANN-RMSE','ANN-MASE',
        'ELM-TN','ELM-TEST','ELM-RMSE','ELM-MASE',
        'CFM-TN','CFM-TEST','CFM-RMSE','CFM-MASE',
        'CFE-TN','CFE-TEST','CFE-RMSE','CFE-MASE',
        'AUTO-TN','AUTO-TEST','AUTO-RMSE','AUTO-MASE']
frame = pd.DataFrame(columns = cols)
frameAux = pd.DataFrame(columns = cols)
frameTrials = pd.DataFrame(columns = ['SES-TN','SES-TEST',
        'NAIVE-TN','NAIVE-TEST',
        'HOLT-TN','HOLT-TEST',
        'CR-TN','CR-TEST',
        'AR-TN','AR-TEST',
        'ANN-TN','ANN-TEST',
        'ELM-TN','ELM-TEST',
        'CFM-TN','CFM-TEST',
        'CFE-TN','CFE-TEST',
        'AUTO-TN','AUTO-TEST'])

data = pd.read_excel(path+file)
grouped = data[['STATIONS','PARTS']].groupby(['STATIONS','PARTS'])

lengthCutOff = 20
ADIcutOff = 14
totalSeries = 730
processedSeries = 0
totalIntermittent = 0
roundPlaces = 3
processTimeAux = None
processTotalTimeAux = None
startTime = dt.datetime.now()

for trial in range (10):

    for station, part in grouped:
        
        # Step 1: create time series from the spreadsheet
        temp = data.loc[(data['STATIONS'] == station[0]) & (data['PARTS'] == station[1])]
        #temp = data.loc[(data['STATIONS'] == 'RIO DE JANEIRO') & (data['PARTS'] == 'BASE PLASTICA DE BOTOEIRA')]
        serie = pd.Series(temp['DEMANDS'].values,temp['DATE'])
        
        processTimeAux = dt.datetime.now()
        serie = serie.resample(freq).sum()
        processTimeAux = dt.datetime.now()- processTimeAux
        
        length = len(serie)
        adi = round(ut.Utils.getADI(serie),roundPlaces)
        
        # Step 2: filter by length and record features of series
        # if length >= sizeCutOff and adi < ADIcutOff:
        if length > lengthCutOff and adi/length < 0.35 :
            
            processedSeries+=1
            print('\n\n Remote Station:'+station[0]+', part:'+station[1]+'.')
            print('Progress:'+
                      str(round((processedSeries/totalSeries)*100,2))+'%.')
            
            # Features of each series
            max = serie.max()
            cv = round(ut.Utils.getCV(serie),roundPlaces)
            mean = round(serie.mean(),roundPlaces)
            std = round(serie.std(),roundPlaces)
            
            if adi > 1.32:
                totalIntermittent+=1
        
            # Step 3: fit models
            processTotalTimeAux = dt.datetime.now()
            md = ms.ModelsManager(data=serie,horizon=6,models=models)
            md.fit()
            processTotalTimeAux = dt.datetime.now() - processTotalTimeAux   
            automatic,value = md.getBestByMetric('MASE')
            
            '''plt.title(station[1]+' in '+station[0])
            plt.plot(serie,'black')
            plt.plot(automatic.trainingPrediction,'yellow')
            plt.plot(automatic.testPrediction,'blue')
            plt.plot(automatic.forecastDemand,'green')
            plt.xlabel('Time')
            plt.ylabel('Demand')
            plt.legend(['Real','Fitted values',automatic.model+'- Test',automatic.model+'- Forecast'])
            plt.gcf().autofmt_xdate()
            plt.savefig(imagePath+station[1].replace('/',' ')+
                        '_'+station[0]+'_'+automatic.model+'.png',dpi = 300)
            plt.close()'''
            
            # Step 4: get data
            line = {}
            lineAux = {}
            
            # Statistical Summary
            lineAux['STATIONS'] = station[0]
            lineAux['PARTS'] = station[1]
            lineAux['LENGTH'] = length
            lineAux['MAX'] = max
            lineAux['MEAN'] = mean 
            lineAux['STD'] = std
            lineAux['CV'] = cv
            lineAux['ADI'] = adi 
            lineAux['TOTAL-TIME'] = str(processTimeAux+processTotalTimeAux)
            line['TOTAL-TIME'] = processTimeAux+processTotalTimeAux
            
            autoTrainingTime = dt.datetime.now()
            autoTrainingTime = autoTrainingTime - autoTrainingTime
            autoTestTime = autoTrainingTime
            # Run time
            for m in md.runTimeList:
                
                lineAux[m.getModelName()+'-TN'] = str(m.getTrainingTime())
                lineAux[m.getModelName()+'-TEST'] = str(m.getTestTime())
                line[m.getModelName()+'-TN'] = m.getTrainingTime()
                line[m.getModelName()+'-TEST'] = m.getTestTime()
                
                if m.getModelName() not in ['CFE','CFM']:
                    autoTrainingTime += m.getTrainingTime()
                    autoTestTime += m.getTestTime()
                
                line['AUTO-TN'] = autoTrainingTime
                line['AUTO-TEST'] = autoTestTime
                lineAux['AUTO-TN'] = str(autoTrainingTime)
                lineAux['AUTO-TEST'] = str(autoTestTime)
            print(lineAux)
            # Accuracy
            for t in md.modelsResult:
                
                modelName = t.model
                rmse = obj.ForecastErro.getValueByMetricName(t.error,'RMSE','TEST')
                mase = obj.ForecastErro.getValueByMetricName(t.error,'MASE','TEST')
                line[modelName+'-RMSE'] = rmse
                line[modelName+'-MASE'] = mase
                lineAux[modelName+'-RMSE'] = rmse
                lineAux[modelName+'-MASE'] = mase
            
            line['AUTO-RMSE'] = obj.ForecastErro.getValueByMetricName(
                automatic.error,'RMSE','TEST')
            line['AUTO-MASE'] = obj.ForecastErro.getValueByMetricName(
                automatic.error,'MASE','TEST')
            lineAux['AUTO-RMSE'] = obj.ForecastErro.getValueByMetricName(
                automatic.error,'RMSE','TEST')
            lineAux['AUTO-MASE'] = obj.ForecastErro.getValueByMetricName(
                automatic.error,'MASE','TEST')
                
            # Step 5: write to spreadsheet
            frame = frame.append(line,ignore_index=True)
            frameAux = frameAux.append(lineAux,ignore_index=True)
    
    summarySheet = pd.DataFrame(columns=['MODEL-METHOD','TN-TIME','TEST-TIME','RMSE','MASE'])
    lineTrials = {}
    
    for m in models:
        
        trainingTime = str(frame[m+'-TN'].mean())[8:]
        testTime = str(frame[m+'-TEST'].mean())[8:]
        rmse = round(frame[m+'-RMSE'].mean(),roundPlaces)
        mase = round(frame[m+'-MASE'].mean(),roundPlaces)
        line = {'MODEL-METHOD':m,
                'TN-TIME':trainingTime,
                'TEST-TIME':testTime,
                'RMSE':rmse,
                'MASE':mase}    
        summarySheet = summarySheet.append(line,ignore_index=True)
        
        lineTrials[m+'-TN'] = frame[m+'-TN'].mean()
        lineTrials[m+'-TEST'] = frame[m+'-TEST'].mean()
        
    # Automatic strategy statistics 
    line = {'MODEL-METHOD':'AUTO',
                'TN-TIME':str(frame['AUTO-TN'].mean())[8:],
                'TEST-TIME':str(frame['AUTO-TEST'].mean())[8:],
                'RMSE':round(frame['AUTO-RMSE'].mean(),roundPlaces),
                'MASE':round(frame['AUTO-MASE'].mean(),roundPlaces)}
    
    lineTrials['AUTO-TN'] = frame['AUTO-TN'].mean()
    lineTrials['AUTO-TEST'] = frame['AUTO-TEST'].mean()
    frameTrials = frameTrials.append(lineTrials,ignore_index=True)
    
    summarySheet = summarySheet.append(line,ignore_index=True)
    
    line = {}
    line['MODEL-METHOD'] = 'TOTAL-TIME'
    line['TN-TIME'] = str(frame['TOTAL-TIME'].mean())[8:]
    summarySheet = summarySheet.append(line,ignore_index=True)
    
    # Save to disk
    frameAux.to_excel(excel_writer=writer,sheet_name='T'+str(trial)+'_'+'DATA',index=False)
    summarySheet.to_excel(excel_writer=writer,sheet_name='T'+str(trial)+'_'+'SUM',index=False)
    writer.save()

frameTrialsSum = pd.DataFrame(columns = ['MODEL','MEAN-TN','STD-TN','MEAN-TEST','STD-TEST'])
line = {}
for m in models:
    
    line = {'MODEL':m,
          'MEAN-TN': str(frameTrials[m+'-TN'].mean())[10:],
          'STD-TN': str(frameTrials[m+'-TN'].std())[10:],
          'MEAN-TEST': str(frameTrials[m+'-TEST'].mean())[10:],
          'STD-TEST': str(frameTrials[m+'-TEST'].std())[10:]}
        #std = frameTrials[c].std()
        #line[c]=str(mean)[10:]
        #line[c]=str(std)[10:]
    frameTrialsSum = frameTrialsSum.append(line,ignore_index=True)
    print(line)

line = {'MODEL':'AUTO',
          'MEAN-TN': str(frameTrials['AUTO-TN'].mean())[10:],
          'STD-TN': str(frameTrials['AUTO-TN'].std())[10:],
          'MEAN-TEST': str(frameTrials['AUTO-TEST'].mean())[10:],
          'STD-TEST': str(frameTrials['AUTO-TEST'].std())[10:]}
        #std = frameTrials[c].std()
        #line[c]=str(mean)[10:]
        #line[c]=str(std)[10:]
frameTrialsSum = frameTrialsSum.append(line,ignore_index=True)
print(line)
    
frameTrialsSum.to_excel(excel_writer=writer,sheet_name='TRIALS_'+'SUM',index=False)
writer.save()
total = dt.datetime.now() - startTime

print('Total of analyzed series:'+str(processedSeries))
print('Total of intermittent series:'+str(totalIntermittent))
print('Total Run time:'+str(total))
