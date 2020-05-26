# -*- coding: utf-8 -*-
"""
Created on Wed Dec 18 11:38:23 2019

@author: FlavioFilho
"""

import numpy as np
from keras.models import Sequential
from keras.layers import Dense
from keras.callbacks import EarlyStopping, ReduceLROnPlateau
from keras import backend as K
import statistics as st
from utils.Utils_neural_network import Utils_neural_network as Util_NN
from utils.util import Utils as ut
import datetime as dt
import ws.objects as obj


class ANN(object):
    
    def __init__(self,
                 dataset,
                 horizion,
                 activation_function,
                 stopping_criterion = 'iterations',
                 max_iterations= 400,
                 batch= 32,
                 proportion = 0.80,
                 optimizer = 'adam',
                 test_observations = 0):
        
        self.__index = dataset.index
        self.__dataset = np.array(dataset)
        self.__max_iterations = max_iterations
        self.__batch = batch
        self.__horizion = horizion
        self.__time_delay = 1
        self.__stopping_criterion = stopping_criterion
        self.__proportion = proportion
        self.__optimizer = optimizer
        self.__activation_function = activation_function
        self.__test_observations = test_observations
        self.trainScoreMASE = 0
        self.trainScoreRMSE = 0
        self.trainScoreMAPE = 0
        self.testScoreMASE_one_step = 0
        self.testScoreRMSE_one_step = 0
        self.testScoreMAPE_one_step = 0
        self.testScoreMASE_mult_setp = 0
        self.testScoreRMSE_mult_setp = 0
        self.testScoreMAPE_mult_setp = 0
        self.trainPredict = []
        self.testPredict = []
        self.predict_horizon = []
        self.trainPredict_Result = []
        self.testPredict_Result = []
        self.predict_horizon_Result = []
        self._runTimeOpt = None
        self._runTime = None

        # split into train and test sets
        self.__train, self.__test = Util_NN.split_train_and_test(self.__dataset,proportion,1,self.__test_observations)
        self.__best_neuron_amount = 1
        self.__best_error = 1000000000
        
        self._runTimeOpt = obj.ModelsRunTime('ANN-OPT')
        startTime = dt.datetime.now()
        self.__grid_search_optimization_function()
        self._runTimeOpt.setTrainingTime(dt.datetime.now()-startTime)
        self._runTimeOpt.setTestTime(self._runTimeOpt.getTrainingTime()) # remove this line after tests
        
        self.__train, self.__test = Util_NN.split_train_and_test(self.__dataset,proportion,self.__time_delay,self.__test_observations)
        
        # reshape the dataset
        self.__trainX, self.__trainY = Util_NN.create_dataset(self.__train, self.__time_delay)
        self.__testX, self.__testY = Util_NN.create_dataset(self.__test, self.__time_delay)
        
        if self.__test_observations == 0:
            self.__test_observations = len(self.__testY)
            
    def __creat_model(self,time_delay,hidden_nodes,optimization_algorithm,activation_function):
        K.clear_session()
        model = Sequential()
        if activation_function == 'RELU/LINEAR':
            model.add(Dense(hidden_nodes, input_dim=time_delay, activation='relu'))
            model.add(Dense(1,activation='linear'))        
        else:
            model.add(Dense(hidden_nodes, input_dim=time_delay, activation='sigmoid'))
            model.add(Dense(1, activation='linear'))
        model.compile(loss='mean_squared_error', optimizer= optimization_algorithm)
        return model
    
    def best_neuron_amount(self):
        return self.__best_neuron_amount, self.__best_error
    
    def time_delay(self):
        return self.__time_delay, self.__best_error


    def __grid_search_optimization_function(self):
        
        
        for lag in range(1,6): # REVERTO TO range(1,6)
            
            # split into train_aux and test_aux sets
            train_aux, test_aux = Util_NN.split_train_and_test(self.__train,self.__proportion,lag)
            
            # reshape dataset
            trainX_aux, trainY_aux = Util_NN.create_dataset(train_aux, lag)
            testX_aux, testY_aux = Util_NN.create_dataset(test_aux, lag)
            
            error_median = 0
            error = []
            for fold in range(10):
            
                # calculate amount of neurons in hidden layer
                nodes = int((2*lag+1))
                
                # train neural network model  with chosen stopping criterion
                self.__training_model(lag,nodes,trainX_aux,trainY_aux)
                
                #trainPredict = Util_NN.convert_column_in_row( self.__model.predict(trainX_aux))
                testPredict = Util_NN.convert_column_in_row(self.__model.predict(testX_aux))
                
                test_predict_X_aux, test_predict_Y_aux = self.__forecating(len(testY_aux),lag,train_aux,self.__model)
                
                #testa com horizonte tempo maior que 1
                #trainScoreMASE, trainScoreRMSE, trainScoreMAPE, testScoreMASE, testScoreRMSE, testScoreMAPE = estimator_Erro(trainY_aux,trainPredict,testY_aux,test_Y_aux,train_aux )
                
                testScoreRMSE = ut.rmse(testY_aux, testPredict)
                #testScoreRMSE = RMSE(testY_aux, test_predict_Y_aux)
                
                error.append(testScoreRMSE)
            
            error_median = st.median(error)
            if self.__best_error > error_median:
                self.__best_neuron_amount = nodes
                self.__best_error = error_median
                self.__time_delay = lag
                
    def __forecating(self,horizion, time_delay,dataset, model):
        predictionsX = np.zeros((horizion, time_delay))
        predictionsY = np.zeros((horizion))
        
        Home_position = len(dataset)-(time_delay)
        Final_position = len(dataset)
        predictionsX[0] = dataset[Home_position:Final_position]
        aux = predictionsX[0].reshape((1, time_delay))
        
        for h in range(horizion):
            predictionsY[h] = model.predict(aux)
        
            if h < horizion-1:
                for t in range(time_delay-1):
                    predictionsX[h+1][t] = predictionsX[h][t+1] 
                predictionsX[h+1][time_delay-1] = predictionsY[h]
                aux = predictionsX[h+1].reshape((1, time_delay))
                
        return predictionsX, predictionsY
    
    def __training_model(self, delay , nodes_hidden, Train__X, Train__Y):
        self.__model = self.__creat_model(delay,nodes_hidden,self.__optimizer,self.__activation_function)
                
        if self.__stopping_criterion == 'iterations':
            # training model
            self.__model.fit(Train__X, Train__Y, epochs=self.__max_iterations, batch_size=self.__batch, verbose=0)
        else:
            # neural network training will stop when it stops improving after 10 iterations
            es = EarlyStopping(monitor = 'loss', min_delta = 1e-10, patience = 10, verbose = 1)
            # will decrease neural network learning rate after stop improving 5 iterations
            rlr = ReduceLROnPlateau(monitor = 'loss', factor = 0.2, patience = 5, verbose = 1)
            # training model
            self.__model.fit(Train__X, Train__Y, epochs=self.__max_iterations, batch_size=self.__batch, verbose=2,callbacks = [es,rlr])
                
    
    def predictions(self, executions = 20):
        list_train = []
        #list_test_one_step = []
        list_test_mult_step = []
        list_predict_horizon = []
            
        median_trainPredict = []
        #median_testPredict_one_step = []
        median_testPredict_mult_step = []
        median_predict_horizon = []
        
        startTimeTraining = dt.datetime.now()
        trainTimeCum = startTimeTraining - startTimeTraining #due to cumulative sum in the loop
        startTimeTest = startTimeTraining
        testTimeCum = trainTimeCum
        
        for r in range(executions):
            
            startTimeTraining = dt.datetime.now()
            # train neural network model  with chosen stopping criterion
            self.__training_model(self.__time_delay,self.__best_neuron_amount,self.__trainX,self.__trainY)
               
            # generate predictions for training
            self.trainPredict = Util_NN.convert_column_in_row(self.__model.predict(self.__trainX))
            #self.testPredict = Util_NN.convert_column_in_row(self.__model.predict(self.__testX))
            trainTimeCum += dt.datetime.now() - startTimeTraining
            
            startTimeTest = dt.datetime.now()    
            test_X, test_Y = self.__forecating(self.__test_observations,self.__time_delay,self.__train,self.__model)
            testTimeCum += dt.datetime.now() - startTimeTest
            
            horizon_X, horizon_Y = self.__forecating(self.__horizion,self.__time_delay,self.__dataset,self.__model)
            
            predict_horizon = horizon_Y
            testPredict_mult_step = test_Y
            
            list_train.append(self.trainPredict)
            #list_test_one_step.append(self.testPredict)
            list_test_mult_step.append(testPredict_mult_step)
            list_predict_horizon.append(predict_horizon)
        
        startTimeTraining = dt.datetime.now()        
        #make the median combination of the predictions    
        for x in range(len(self.trainPredict)):
            c = np.array(list_train)
            c = c[:,x]
            c = st.median(c)
            median_trainPredict.append(c)
        
        trainTimeCum += dt.datetime.now() - startTimeTraining
        self._runTime = obj.ModelsRunTime('ANN')
        self._runTime.setTrainingTime(trainTimeCum)
            
        #for x in range(len(self.testPredict)):
            #c = np.array(list_test_one_step)
            #c = c[:,x]
            #c = st.median(c)
            #median_testPredict_one_step.append(c)
        
        startTimeTest = dt.datetime.now()
        for x in range(len(testPredict_mult_step)):
            c = np.array(list_test_mult_step)
            c = c[:,x]
            c = st.median(c)
            median_testPredict_mult_step.append(c)
        testTimeCum += dt.datetime.now() - startTimeTest
        self._runTime.setTestTime(testTimeCum)
            
        for x in range(len(predict_horizon)):
            c = np.array(list_predict_horizon)
            c = c[:,x]
            c = st.median(c)
            median_predict_horizon.append(c)
                
        self.trainPredict = median_trainPredict
        #self.testPredict = median_testPredict_one_step
        self.testPredict = median_testPredict_mult_step
        testPredict_mult_step = median_testPredict_mult_step
        self.predict_horizon = median_predict_horizon
                
        #self.trainScoreMASE, self.trainScoreRMSE, self.trainScoreMAPE, self.testScoreMASE_one_step, self.testScoreRMSE_one_step, self.testScoreMAPE_one_step = Util_NN.evaluate_all_errors(self.__trainY,self.trainPredict,self.__testY,self.testPredict,self.__train)
        
        
        self.trainScoreMASE, self.trainScoreRMSE, self.trainScoreMAPE, self.testScoreMASE_mult_setp, self.testScoreRMSE_mult_setp, self.testScoreMAPE_mult_setp = Util_NN.evaluate_all_errors(self.__trainY,
                                            self.trainPredict,
                                            self.__testY,
                                            testPredict_mult_step,
                                            self.__train)
            
        #print_chart
        #trainPredictPlot, testPredictPlot, previsoesPlot = Util_NN.print_chart(self.__dataset,self.trainPredict,self.testPredict,self.predict_horizon,self.__horizion,self.__time_delay)
        
        self.trainPredict_Result , self.testPredict_Result, self.predict_horizon_Result = Util_NN.treat_output(self.__dataset,self.__index,self.trainPredict,self.testPredict,self.predict_horizon,self.__horizion,self.__time_delay)
     
    def getRunTimeOpt(self):
        return self._runTimeOpt
    
    def getRunTime(self):
        return self._runTime
      