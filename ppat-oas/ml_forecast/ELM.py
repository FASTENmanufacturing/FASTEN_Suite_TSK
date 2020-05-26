# -*- coding: utf-8 -*-
"""
Created on Wed Dec 18 16:23:56 2019

@author: FlavioFilho
"""

import numpy as np
import elm
import statistics as st
from utils.Utils_neural_network import Utils_neural_network as Util_NN
from utils.util import Utils as ut
import datetime as dt
import ws.objects as obj

class ELM(object):
    def __init__(self,dataset,horizion, executions =100, proportion = 0.80, test_observations = 0):
        self.__index = dataset.index
        self.__dataset = np.array(dataset)
        self.__horizion = horizion
        self.__time_delay = 1
        self.__proportion = proportion
        self.__activation_function = ''
        self.__C = 1
        self.__test_observations = test_observations
        self.__qtd_executions = executions
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
        
        self._runTimeOpt = obj.ModelsRunTime('ELM-OPT')
        startTime = dt.datetime.now()
        self.__grid_search_optimization_function()
        self._runTimeOpt.setTrainingTime(dt.datetime.now()-startTime)
        self._runTimeOpt.setTestTime(self._runTimeOpt.getTrainingTime()) # remove this line after tests
        
        self.__train, self.__test = Util_NN.split_train_and_test(self.__dataset,proportion,self.__time_delay,self.__test_observations)
        
        # reshape dataset
        self.__trainX, self.__trainY = Util_NN.create_dataset(self.__train, self.__time_delay)
        
        # reshape dataset for elm
        self.__dataTrain = np.zeros(((len(self.__train)-self.__time_delay),(self.__time_delay+1)))
        self.__dataTrain[:,1:(self.__time_delay+1)] = self.__trainX
        self.__dataTrain[:,0] = self.__trainY
        
        # reshape dataset
        self.__testX, self.__testY = Util_NN.create_dataset(self.__test, self.__time_delay)  
        
        # reshape dataset for elm
        self.__dataTest = np.zeros(((len(self.__test)-self.__time_delay),(self.__time_delay+1)))
        self.__dataTest[:,1:(self.__time_delay+1)] = self.__testX
        self.__dataTest[:,0] = self.__testY
        
        if self.__test_observations == 0:
            self.__test_observations = len(self.__testY)
            
    def __creat_model(self,hidden_nodes = 10,function= "sigmoid", C = 1):
    
        params = [function, C, hidden_nodes, False]
        model = elm.ELMRandom(params)
        
        return model
    
    def __forecating(self, horizion, time_delay,dataset, model):
        predictionsX = np.zeros((horizion, time_delay+1))
        predictionsY = np.zeros((horizion))
        
        Home_position = len(dataset)-(time_delay)
        Final_position = len(dataset)
        predictionsX[0,1:(time_delay+1)] = dataset[Home_position:Final_position]
        aux = predictionsX[0].reshape((1, time_delay+1))
        
        for h in range(horizion):
            
            r = model.test(aux)
            predictionsY[h] = r.predicted_targets
        
            if h < horizion-1:
                for t in range(time_delay-1):
                    predictionsX[h+1][t+1] = predictionsX[h][t+2] 
                predictionsX[h+1][time_delay] = predictionsY[h]
                aux = predictionsX[h+1].reshape((1, time_delay+1))
                
        return predictionsX, predictionsY
    
    def __combination_executions(self, dataset, train, test, train_aux, executions = 100, neurons = 10,c = 1, function = 'sigmoid', time_delay = 1):
        
        executions_trainPredict = []
        executions_testPredict_one_step = []
        executions_testPredict_mult_step = []
        executions_predict_horizon = []
        
        median_trainPredict = []
        median_testPredict_one_step = []
        median_testPredict_mult_step = []
        median_predict_horizon = []
        
        startTimeTraining = dt.datetime.now()
        trainTimeCum = startTimeTraining - startTimeTraining #due to cumulative sum in the loop
        startTimeTest = startTimeTraining
        testTimeCum = trainTimeCum
    
        for t in range(0,executions):
            
            #create elm model    
            self.__model = self.__creat_model(neurons,function,c)
       
            #train elm model 
            startTimeTraining = dt.datetime.now()
            tr_result = self.__model.train(train)
            trainTimeCum += dt.datetime.now() - startTimeTraining
            
            startTimeTest = dt.datetime.now()     
            te_result = self.__model.test(test)
            testTimeCum += dt.datetime.now() - startTimeTest
            
            trainPredict = tr_result.predicted_targets
            testPredict = te_result.predicted_targets
        
            predictionsX, predictionsY = self.__forecating(self.__horizion,time_delay,dataset,self.__model)
            test_X, test_Y = self.__forecating(len(test),time_delay,train_aux,self.__model)

            executions_trainPredict.append(trainPredict)
            executions_testPredict_one_step.append(testPredict)
            executions_testPredict_mult_step.append(test_Y)
            executions_predict_horizon.append(predictionsY)
            
        #make the median combination of the predictions 
        startTimeTraining = dt.datetime.now()
        for x in range(len(trainPredict)):
            c = np.array(executions_trainPredict)
            c = c[:,x]
            c = st.median(c)
            median_trainPredict.append(c)
         
        trainTimeCum += dt.datetime.now() - startTimeTraining
        self._runTime = obj.ModelsRunTime('ELM')
        self._runTime.setTrainingTime(trainTimeCum) 
             
        for x in range(len(testPredict)):
            c = np.array(executions_testPredict_one_step)
            c = c[:,x]
            c = st.median(c)
            median_testPredict_one_step.append(c)           
        
        
        startTimeTest = dt.datetime.now()   
        for x in range(len(test_Y)):
            c = np.array(executions_testPredict_mult_step)
            c = c[:,x]
            c = st.median(c)
            median_testPredict_mult_step.append(c)
        testTimeCum += dt.datetime.now() - startTimeTest 
        self._runTime.setTestTime(testTimeCum)
            
        for x in range(len(predictionsY)):
            c = np.array(executions_predict_horizon)
            c = c[:,x]
            c = st.median(c)
            median_predict_horizon.append(c)
            
        return median_trainPredict, median_testPredict_one_step, median_testPredict_mult_step, median_predict_horizon
    
    def __grid_search_optimization_function(self):
        for lag in range(1,6):
            # split into train_aux and test_aux sets
            train_aux, test_aux = Util_NN.split_train_and_test(self.__train,self.__proportion,lag)
            
            # reshape dataset
            trainX_aux, trainY_aux = Util_NN.create_dataset(train_aux, lag)
            
            # reshape dataset for elm
            dataTrain = np.zeros(((len(train_aux)-lag),(lag+1)))
            dataTrain[:,1:(lag+1)] = trainX_aux
            dataTrain[:,0] = trainY_aux
            
            # reshape dataset
            testX_aux, testY_aux = Util_NN.create_dataset(test_aux, lag) 
            
            # reshape dataset for elm
            dataTest = np.zeros(((len(test_aux)-lag),(lag+1)))
            dataTest[:,1:(lag+1)] = testX_aux
            dataTest[:,0] = testY_aux
            

            list_activation_function = ['sigmoid','multiquadric']
            vetor_C = [1,2**1,2**2,2**3,2**4,2**5,2**-1,2**-2,2**-3,2**-4,2**-5]
            nodes = int((2*lag+1))
            for f in list_activation_function:
                for c in vetor_C:
                    error_median = 0
                    error = []
                    for fold in range(1):
                        median_trainPredict, median_testPredict_one_step, median_testPredict_mult_step, median_predict_horizon = self.__combination_executions(self.__train,dataTrain,dataTest,train_aux,self.__qtd_executions,nodes,c,f,lag)
                                   
                        # Estimate model performance
                        testScoreRMSE = ut.rmse(testY_aux, median_testPredict_one_step)
                        
                        
                        error.append(testScoreRMSE) 
                        
                    error_median = st.median(error)   
                    if self.__best_error > error_median:
                        self.__best_neuron_amount = nodes
                        self.__best_error = error_median
                        self.__C = c
                        self.__activation_function = f
                        self.__time_delay = lag
                        
                        
    def best_neuron_amount(self):
        return self.__best_neuron_amount, self.__best_error
    
    def best_C(self):
        return self.__C
    
    def best_activation_function(self):
        return self.__activation_function
    
    def time_delay(self):
        return self.__time_delay
    
    def predictions(self, executions = 100):
        
        median_trainPredict, median_testPredict_one_step, median_testPredict_mult_step, median_predict_horizon = self.__combination_executions(self.__dataset,self.__dataTrain, self.__dataTest, self.__train,executions,self.__best_neuron_amount, self.__C, self.__activation_function,self.__time_delay )
           
        
        # Estimate model performance
        #self.trainScoreMASE, self.trainScoreRMSE, self.trainScoreMAPE, self.testScoreMASE_one_step, self.testScoreRMSE_one_step, self.testScoreMAPE_one_step = Util_NN.evaluate_all_errors(self.__trainY,
         #                                    median_trainPredict,
          #                                   self.__testY,
           #                                  median_testPredict_one_step,
            #                                 self.__train)
        
        
        # Estimate model performance
        self.trainScoreMASE, self.trainScoreRMSE, self.trainScoreMAPE, self.testScoreMASE_mult_setp, self.testScoreRMSE_mult_setp, self.testScoreMAPE_mult_setp = Util_NN.evaluate_all_errors(self.__trainY,
                                             median_trainPredict,
                                             self.__testY,
                                             median_testPredict_mult_step,
                                             self.__train)             
        
        # generate predictions for training
        self.trainPredict = median_trainPredict
        self.testPredict = median_testPredict_mult_step
        self.predict_horizon = median_predict_horizon
        
        #print_chart
        #trainPredictPlot, testPredictPlot, previsoesPlot = Util_NN.print_chart(self.__dataset,self.trainPredict,self.testPredict,self.predict_horizon,self.__horizion,self.__time_delay)
        
        self.trainPredict_Result , self.testPredict_Result, self.predict_horizon_Result = Util_NN.treat_output(self.__dataset,self.__index,self.trainPredict,self.testPredict,self.predict_horizon,self.__horizion,self.__time_delay)
    
    def getRunTimeOpt(self):
        return self._runTimeOpt
    
    def getRunTime(self):
        return self._runTime       