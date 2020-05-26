#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Nov  4 22:30:13 2019
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Code to perform the AUTO strategy accuracy checks.
Note:       

Fix: 
Status: To verify necessary changes for the new experimental design.       
Next:
       
"""
# Imports
import os,sys
sys.path.append(os.getcwd()[0:-11]) # run into tests
import pandas as pd

def improvedScore(dataFrame):
    
    newFrame = pd.Series()
    
    for i in range(len(dataFrame)):
        best = dataFrame.iloc[[i]]['BEST'].values
        best = dataFrame.iloc[[i]][best].values[0]
        newFrame = newFrame.append(pd.Series(best))
       
    return newFrame.mean(),newFrame.std()    

# Data source, directory data
dataFile = 'selection_M3_FULL.xls'
path = '../Results/'
sheetName= ['MMASE','MRMSE']
imagePath = '../Images/'

for sheet in sheetName:
    print()
    print(sheet)
    data = pd.read_excel(path+dataFile,None)
    data = data.pop(sheet)
    mean,std = improvedScore(data)
    print('Mean:'+str(mean)+',std:'+str(std))