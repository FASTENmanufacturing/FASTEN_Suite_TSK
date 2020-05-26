# -*- coding: utf-8 -*-
"""
Created on Wed Dec 18 11:13:00 2019

@author: FlavioFilho
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from utils.util import Utils as ut

class Utils_neural_network:

    # convert an array of values into a dataset matrix
    def create_dataset(dataset, time_delay=1):
    	dataX, dataY = [], []
    	for i in range(len(dataset)-time_delay):
    		a = dataset[i:(i+time_delay)]
    		dataX.append(a)
    		dataY.append(dataset[i + time_delay])
    	return np.array(dataX), np.array(dataY)
    
    # split time series into training and testing
    def split_train_and_test(dataset,percentage=0.80, time_delay = 1, test_observations = 0):
        dataset = np.array(dataset)
        dataset.reshape((len(dataset),1))
        if test_observations == 0:
            train_size = int(len(dataset) * percentage)
            train, test = dataset[0:train_size], dataset[(train_size-time_delay):len(dataset)]
            return train, test
        train_size = int(len(dataset) - test_observations)
        train, test = dataset[0:train_size], dataset[(train_size-time_delay):len(dataset)]
        return train, test
    
    # convert an array in the form of a column to a row
    def convert_column_in_row (vetor):
        t = vetor
        vetor_aux = []
        for aux in t:
            vetor_aux.append(float(aux))
        return vetor_aux
    
     # calculate training and testing errors for all metrics
    def evaluate_all_errors(trainY_true,trainY_prediction,testY_true, testY_prediction, train):
    
        trainScoreMAPE = None
        testScoreMAPE = None
        
        trainScoreRMSE = ut.rmse(trainY_true, trainY_prediction)
        trainScoreMASE = ut.mase(train,trainY_true,trainY_prediction)
        
        if 0 not in trainY_true:
            trainScoreMAPE = ut.mape(trainY_true, trainY_prediction)

        testScoreRMSE = ut.rmse(testY_true, testY_prediction)
        testScoreMASE = ut.mase(train,testY_true, testY_prediction)
        
        if 0 not in testY_true:
            testScoreMAPE = ut.mape(testY_true, testY_prediction)
        
        return trainScoreMASE, trainScoreRMSE, trainScoreMAPE, testScoreMASE, testScoreRMSE, testScoreMAPE  
    
   
    # treat the output of the training, testing and future forecast for API
    def treat_output(dataset,index,trainPredict, testPredict, predictions, horizion,time_delay):

        train_Index = pd.Series(np.ceil(trainPredict),index[time_delay:len(trainPredict)+time_delay])
        
        testPredict = np.array(testPredict)
        testPredict = testPredict.reshape((len(dataset)-len(trainPredict)-(time_delay)))
        test_Index = pd.Series(np.ceil(testPredict),index[len(trainPredict)+(time_delay):len(dataset)])
        
        index_h = pd.date_range(start= index[-1],
                              freq=index.freq,
                              periods = horizion+1)
        
        predictions = np.array(predictions)
        predictions = predictions.reshape((horizion))
        horizon_index = pd.Series(np.ceil(predictions),index_h[1:])
        
        return train_Index, test_Index, horizon_index
    
    def print_chart(dataset,trainPredict, testPredict, previsoes, horizion,time_delay):
        # shift train predictions for plotting
        trainPredictPlot = np.zeros((len(dataset)))
        trainPredictPlot[:] = np.nan
        trainPredictPlot[time_delay:len(trainPredict)+time_delay] = trainPredict
        # shift test predictions for plotting
        testPredictPlot = np.zeros((len(dataset)))
        testPredictPlot[:] = np.nan
        testPredict = np.array(testPredict)
        testPredictPlot[len(trainPredict)+(time_delay):len(dataset)] = testPredict.reshape((len(dataset)-len(trainPredict)-(time_delay)))
        #correção da previsão para plotar
        previsoesPlot = np.zeros((horizion+len(dataset)))
        previsoesPlot[:] = np.nan
        previsoes = np.array(previsoes)
        previsoesPlot[(len(dataset)):(horizion+len(dataset))] = previsoes.reshape((horizion))
        # plot baseline and predictions
        plt.plot(dataset)
        plt.plot(trainPredictPlot)
        plt.plot(testPredictPlot)
        plt.plot(previsoesPlot)
        plt.show()
        
        return trainPredictPlot, testPredictPlot, previsoesPlot
    
