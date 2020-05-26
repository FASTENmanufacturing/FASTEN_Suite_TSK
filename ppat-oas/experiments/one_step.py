#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Dec 14 10:20:27 2019

@author: saymongb
"""

import os,sys
sys.path.append(os.getcwd()[0:-11])
import statsmodels.tsa.holtwinters as ts
import statsmodels.tsa.ar_model as ar
import pandas as pd
import matplotlib.pyplot as plt
import modelSeletor as ms
import numpy as np

data = [1.0,4,6,5,7,8,5,7,10,20,21,19,13,15,16,20,18,17,21,20]
index = pd.date_range(start='01/01/2019',periods=len(data),freq='M')
series = pd.Series(data,index)
start = int(len(series.values)*0.8)
print('Série real:')
print(series)
print()

seletor = ms.ModelSelector(series,1,['AR'],80,stepType='multi')
seletor.fit()
print('Resultados do seletor:')
print(seletor.modelsResult[0].trainingPrediction)

print(seletor.modelsResult[0].testPrediction)
print(seletor.modelsResult[0].error[0].value)

print('Ajuste do AR:')
AR = ar.AR(series[:start])
AR = AR.fit()
trainingFit = pd.Series(AR.fittedvalues)
testPredictions = pd.Series(AR.predict(start=start,end=len(series)-1,dynamic=False))
print(AR.fittedvalues)

AR2 = ar.AR(series)
AR2 = AR2.fit(maxlag = AR.k_ar)
AR2.k_ar = AR.k_ar
AR2.k_tren = AR.k_trend
AR2.params = AR.params
teste2 = ms.ModelSelector.oneStepARPrediction(series,AR2.params,start,len(series)-start)

print('Teste')
print(testPredictions)
print('Test2')
print(teste2)