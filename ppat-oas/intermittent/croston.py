#!/usr/bin/env python3
# -*- coding: utf-8 -*-
'''
Created on Sat Sep 21 13:51:01 2019
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Croston (1972) method implementation and it's variants (SBA,SBJ,TSB)
Note:
    1. Implement Croston, SBA,SBJ,TSB
    2. Implement parameter optimization
    3. Keep the implementation standards of StatsModels.
    4. Keep all values fitted (include when no demand occurs)
    alpha parameter: default value is conform to R. H. Teunter & L Duncan (2009),
                     Forecasting intermittent demand: a comparative study,
                     Journal of the Operational Research Society,
                     DOI: 10.1057/palgrave.jors.2602569
                     
Fix:    
Status: .
'''

import pandas as pd
import numpy as np
import statsmodels.tsa.holtwinters as ts
import statsmodels.tools.eval_measures as ms
import sys

class Croston:
    
    def __init__(self,data,h=1,alpha = 0.15,variant ='CR',init='mean',use_brute=True):
        '''
        Parameters
        ----------
        
        data: must be a Series (Pandas) object.
        alpha: smooth parameter, same as exponential smoothing
        use_brute: Grid search optimization in range [0.1 , 0.3].
        variant: original Croston method (CR),Syntetos and Boylan Aproximation (SBA)

        '''
        self.data = data
        self.index = data.index
        self.h = h
        self.variant = variant
        self.initValue = None
        self.init = init
        self.alpha = alpha
        self.optimizationSteps = 100
        self.fittedDemands = None
        self.fittedIntervals = None
        self.fittedForecasts = None # in-sample forecasts
        self.intervals = None
        self.use_brute = use_brute
        self.demandValues = None
        self.fcast = None # same for all h.
        
    def fit(self):
        '''
        Initialize first demand and first interval
        '''
        
        self.adjustIndex()
        self.initialize()
        
        if self.use_brute:
      
            self.optimize()
            
        else:
            
            self.decompose()
            self.adjustIndex(False)
            self.setInSampleValues()
           
    def adjustIndex(self,remove=True):
        '''
        Drop index for calculations or reset to original for forecasts
        '''
        
        if remove: 
            self.data = self.data.reset_index(drop=True)
            self.data.index = self.data.index+1 # avoid index with 0 value.
        else: 
            self.data.index = self.index
        
    def decompose(self):
        '''
           Croston's decomposition 
        '''
        z = ts.SimpleExpSmoothing(self.demandValues.to_numpy())
        p = ts.ExponentialSmoothing(self.intervals.to_numpy())
        
        # Demand level
        z = z.fit(smoothing_level=self.alpha,
                  initial_level = self.demandValues[0],
                  optimized=False)
        
        # Intervals
        p = p.fit(smoothing_level=self.alpha,
                  initial_level = self.intervals[0],
                  optimized=False )
        
        self.fittedvalues = z.fittedfcast
        self.fittedIntervals = p.fittedfcast
        self.fittedForecasts = self.fittedvalues/self.fittedIntervals
        self.fcast = z.forecast(1)/p.forecast(1)
                
    def forecast(self,h=1):
        
        if self.fittedForecasts is None:
            self.fit()
        
        if h != 1:
            self.h = h
        
        # Create new series
        array = np.zeros(self.h)
        array[:] = self.fcast
        index = pd.date_range(start= self.index[-1],
                              freq=self.index.freq,
                              periods = self.h+1)
         
        return pd.Series(array,index[1:])
    
    def initialize(self):
        '''
            Set demands and intervals from raw data.
        '''
        
        self.demandValues = self.data[self.data>0] #non-zero demands
        indexSeries = pd.Series(self.demandValues.index)
        self.demandValues = self.demandValues.reset_index(drop=True)
        self.intervals = indexSeries.diff()
        self.intervals[0] = indexSeries[0]
        
    
    def setInSampleValues(self):
        
        k=0
        size = len(self.data)
        fittedValues = np.zeros(size)
        
        for i in range(size):
           
            if self.data[i]>0:
                fittedValues[i] = self.fittedForecasts[k]
                k+=1
            else:
                fittedValues[i] = self.fittedForecasts[k]
        self.fittedForecasts = pd.Series(fittedValues,self.index)
        
    
    def optimize(self):
       '''
           This method performs a grid search to find 'optimal' value
           based on the RMSE.
       '''
       lowerLimit = 0.1
       upperLimit = 0.3
       minRMSE = sys.float_info.max
       currentRMSE = None
       finalAlpha = 0.1
      
       for alpha in np.linspace(lowerLimit,upperLimit,self.optimizationSteps): 
           
           self.alpha = alpha
           self.decompose()
           self.adjustIndex(False)
           self.setInSampleValues()
           
           currentRMSE = ms.rmse(self.data.values, self.fittedForecasts.values)
           
           self.adjustIndex(True)    
           
           if currentRMSE < minRMSE:
               minRMSE = currentRMSE
               finalAlpha = alpha
       
       self.alpha = finalAlpha