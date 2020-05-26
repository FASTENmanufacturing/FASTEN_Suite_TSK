#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Dec  4 00:40:49 2019

@author: saymongb
"""
import pandas as pd
import numpy as np

dataFile = 'M3C.xls'
path = '../Dataset/'
data = pd.read_excel(path+dataFile,None)
data = data.pop('M3Month')
data = data[data['N']>=126]

idx = range(998)
samples = np.random.choice(idx,100,False)
newFile = data.iloc[samples]
newFile.to_excel('M3C_100.xls',index=False)