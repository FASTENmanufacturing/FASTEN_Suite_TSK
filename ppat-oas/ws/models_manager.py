'''
Created on 24 de jun de 2019
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Framework to perform model selection and get evaluation metrics
Note:       Implement first item level selection, include ARIMA models.
            Works with Pandas.Series
            Detail parameters data!
            Models in pool: Simple Exponential Smoothing (SES), 
                            Naive Forecast (NAIVE),
                            Autoregressive (AR), 
                            SES Holt modification (HOLT),
                            Croston Method (CR),
                            Combination Forecast mean based (CFM),
                            Combination Forecast error based (CFE),
                            Automatic selection procedure based on RMSE (AUTO),
                            Artificial Neural Network trained by Adam algorithm (ANN),
                            Artificial Neural Network trained by ELM algorithm (ELM)

Fix: 
Status:      
'''
# Disable warnings about to AVX/FMA (CPU features)
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import statsmodels.tsa.holtwinters as ts
import statsmodels.tools.eval_measures as ms
import statsmodels.tsa.ar_model as ar
import ws.objects as obj
import utils.util as ut
import numpy as np
import pandas as pd
import sys
import intermittent.croston as cr
from ml_forecast.ANN import ANN as ann
from ml_forecast.ELM import ELM as elm
from jinja2 import runtime
from statsmodels.tsa.arima_model import ARIMA as arima
import datetime as dt

class ModelsManager:
    
    # Class attributes
    _metrics = ['RMSE','MAPE','MASE']
    _standardModels = ['SES','NAIVE','AR','HOLT','CR','CFM','CFE','ANN','ELM']
    _horizonLimit = 12
    _decimalPlaces= 4
    _minLength = 20
    
    def __init__(self,data,horizon=1,models=['AUTO'], prop=80,start=None,
                 combType='errorBased', combMetric='RMSE'):
        '''Parameters
           ----------
           
            data: a Pandas.Series representing a historic of an item (time-series)
            horizon: number of forecast required
            start: index to initial forecast on training set
            prop: proportion of training/test-sample to fit a model, default 80-20.
            combType: type of combination ['equal','errorBased','trimmed'] method to be used
            combMetric: metric used to compute the weights of each method
        '''
        # set train and test data
        if start:
            self.start = start
        else:    
            self.start = int(len(data)*(prop/100))
         
        self.data = data
        self.trainData = data[:self.start]
        self.testData = data[self.start:]
         
        self.modelsName = models
        self.fittedModel = None # used as auxiliary variable
        self.fittedModelFinal = None # used as auxiliary variable
        self.horizon = horizon
        self.prop = prop
        
        # Params for the combination forecast approach
        self.weights = None
        self.numModels = None
        self.noCount = 0 # number of models not disregarded to combination
        self.combType = combType
        self.combMetric = combMetric
        self.intervalUC = 'TRAIN' #interval under consideration for weights assignment
        
        self.modelsResult = []
        self.runTimeList = []
        self.initialize()
     
    def initialize(self):
        '''
            Purpose: adjust parameters and set fields to avoid internal errors.
        '''
        
        self.numModels = len(self.modelsName)
        
        # Rule: if required, the last models to be trained
        # will be in this order - CFM,CFE,AUTO. 
        if 'CFE' in self.modelsName:
            self.noCount+=1
            self.modelsName.remove('CFE')
            self.modelsName.append('CFE')
        
        if 'CFM' in self.modelsName:
            self.noCount+=1
            self.modelsName.remove('CFM')
            self.modelsName.append('CFM')
        
        self.numModels = self.numModels - self.noCount
            
        if 'AUTO' in self.modelsName:
            self.modelsName.remove('AUTO')
            self.modelsName.append('AUTO')         
        
    def fit(self):
        '''Fit each mode and:
            a) set training/test predictions
            b) set model error(training/test) values
            c) set forecasts
        '''
        for i in range(len(self.modelsName)):

            if not self.isFitted(self.modelsName[i]):
            
                if self.modelsName[i] in ['SES','NAIVE','HOLT']:
                   
                    self.exponentialFit(self.modelsName[i])
                    
                elif self.modelsName[i] == 'CR':
                    
                    self.crostonFit()
                    
                elif self.modelsName[i] == 'AR':
                    
                    self.ARFit()
                    
                elif self.modelsName[i] == 'AUTO':
                    
                    self.autoFit()
                    
                elif self.modelsName[i] in ['CFE','CFM']:
                    
                    self.combinationFit(self.modelsName[i])
                    
                elif self.modelsName[i] in ['ANN','ELM']:
                    
                    self.RN_Fit(self.modelsName[i])
                
    def exponentialFit(self,name):
        ''' Parameters
            ----------
            
            name: name of model
        '''    
        modelName = name
        errorObjs = []
        runTimeObj = obj.ModelsRunTime(name)
        startTime = None
        totalTime = None
        
        # Step 1: fit selected model
        if name == 'NAIVE':
             # for evaluation
             startTime = dt.datetime.now()
             self.fittedModel = ts.ExponentialSmoothing(self.trainData)
             self.fittedModel = self.fittedModel.fit(smoothing_level=1)
             runTimeObj.setTrainingTime(dt.datetime.now()-startTime)
             
             # for real forecasts
             self.fittedModelFinal = ts.ExponentialSmoothing(self.data)
             self.fittedModelFinal = self.fittedModelFinal.fit(smoothing_level=1)
             
        elif name == 'SES':
            # for evaluation
             startTime = dt.datetime.now()
             self.fittedModel = ts.SimpleExpSmoothing(self.trainData)
             self.fittedModel = self.fittedModel.fit(optimized = True,
                                                   use_brute = True) #grid search
             runTimeObj.setTrainingTime(dt.datetime.now()-startTime)
             
            # for real forecasts
             self.fittedModelFinal = ts.SimpleExpSmoothing(self.data)
             self.fittedModelFinal = self.fittedModelFinal.fit(optimized = True,
                                                  use_brute = True) #grid search
        elif name == 'HOLT':
            # Holt's linear trend method 
            # for evaluation
             startTime = dt.datetime.now()
             self.fittedModel = ts.Holt(self.trainData)
             self.fittedModel = self.fittedModel.fit(optimized = True,
                                                   use_brute = True) #grid search
             runTimeObj.setTrainingTime(dt.datetime.now()-startTime)
            # for real forecasts
             self.fittedModelFinal = ts.Holt(self.data)
             self.fittedModelFinal = self.fittedModelFinal.fit(optimized = True,
                                                  use_brute = True) #grid search
         
        # Step 2: get fitted values for training, test and forecasts
        trainingFit = pd.Series(np.ceil(self.fittedModel.fittedvalues))
        startTime = dt.datetime.now()
        testPredictions = pd.Series(np.ceil(self.fittedModel.forecast(len(self.testData))))
        totalTime = dt.datetime.now() - startTime
        
        forecasts = pd.Series(np.ceil(self.fittedModelFinal.forecast(self.horizon)))
               
        # Step 3: set error
        errorObjs = self.setErrorData(trainingFit,testPredictions,runTimeObj)
        
        runTimeObj.setTestTime(runTimeObj.getTestTime()+totalTime)
        self.runTimeList.append(runTimeObj)
        # Add to ModelsResult list
        self.setModelResults(modelName,errorObjs,trainingFit,
                            testPredictions,forecasts)
        
    def RN_Fit(self,name):
        ''' Parameters
            ----------
            
            name: name of model
        '''    
        modelName = name
        errorObjs = []
        runTimeOpt = None
        runTime = None
        totalTime = None
        
        if self.start ==  None:
            amountTestObs = 0
        else:
            amountTestObs = len(self.data) - self.start
        
        # Step 1: fit selected model
        if name == 'ANN':
             # for evaluation

             configuration = {'activation_function': 'RELU/LINEAR',
                              'stopping_criterion': 'iterations',
                              'max_iterations': 400,
                              'batch': 32,
                              'optimizer': 'adam'}
             model = ann(self.data,
                         self.horizon,
                         configuration['activation_function'],
                         configuration['stopping_criterion'] ,
                         configuration['max_iterations'],
                         configuration['batch'],
                         (self.prop/100),
                         configuration['optimizer'],
                         #0)
                         amountTestObs)
             model.predictions(20)
             
             runTimeOpt = model.getRunTimeOpt()
             runTime = model.getRunTime()
             
        elif name == 'ELM':
            # for evaluation
             model = elm(self.data,self.horizon,10,(self.prop/100), amountTestObs)
             model.predictions(20)
            
             runTimeOpt = model.getRunTimeOpt()
             runTime = model.getRunTime()
        ''' Step 2: get fitted values for training, test and forecasts
        '''
        
        trainingFit = model.trainPredict_Result
        testPredictions = model.testPredict_Result
        forecasts = model.predict_horizon_Result
        
        # Step 3: set error
        #errorObjs = []
        totalTime = runTime.getTestTime()
        errorObjs = self.setErrorData(trainingFit,testPredictions,runTime)
        runTime.setTestTime(runTime.getTestTime()+totalTime)         
               
        runTime.setTrainingTime(
            runTime.getTrainingTime() +
            runTimeOpt.getTrainingTime()
            )
        self.runTimeList.append(runTime)
        
        # Add to ModelsResult list
        self.setModelResults(modelName,errorObjs,trainingFit,
                            testPredictions,forecasts)
        
    def crostonFit(self):
        
        modelName = 'CR'
        errorObjs = []
        runTimeObj = obj.ModelsRunTime(modelName)
        startTime = None
        totalTime = None
        
        # Step 1: fit selected model
        startTime = dt.datetime.now()
        self.fittedModel = cr.Croston(self.trainData)
        self.fittedModel.fit()
        runTimeObj.setTrainingTime(dt.datetime.now()-startTime)
        
        self.fittedModelFinal = cr.Croston(self.data)
        self.fittedModelFinal.fit()
        
        # Step 2: get fitted values for training, test and forecasts
        trainingFit = pd.Series(np.ceil(self.fittedModel.fittedForecasts))
        startTime = dt.datetime.now()
        testPredictions = pd.Series(np.ceil(self.fittedModel.forecast(len(self.testData))))
        totalTime = dt.datetime.now() - startTime
        forecasts = pd.Series(np.ceil(self.fittedModelFinal.forecast(self.horizon)))
               
        # Step 3: set error
        errorObjs = self.setErrorData(trainingFit,testPredictions,runTimeObj)
        runTimeObj.setTestTime(runTimeObj.getTestTime()+totalTime)
        self.runTimeList.append(runTimeObj)
        
        # Add to ModelsResult list
        self.setModelResults(modelName,errorObjs,trainingFit,
                            testPredictions,forecasts)
        
    def ARFit(self):
        ''' Fits a autoregressive model.
        '''    
        modelName = 'AR'
        errorObjs = []
        runTimeObj = obj.ModelsRunTime(modelName)
        startTime = None
        totalTime = None
        
        # Step 1: set training and test values
        
        startTime = dt.datetime.now()
        self.fittedModel = ar.AR(self.trainData)
        self.fittedModel = self.fittedModel.fit()
        runTimeObj.setTrainingTime(dt.datetime.now()-startTime)
        
        trainingFit = pd.Series(np.ceil(self.fittedModel.fittedvalues))
        
        startTime = dt.datetime.now()
        testPredictions = pd.Series(np.ceil(self.fittedModel.predict(
                start=len(self.trainData),
                end=len(self.trainData)+len(self.testData)-1,
                dynamic=False)))
        totalTime = dt.datetime.now() - startTime
        
        # Step 2: Training again with all data for accurate forecasts
        self.fittedModelFinal = ar.AR(self.data)
        self.fittedModelFinal = self.fittedModelFinal.fit()
        forecasts = pd.Series(np.ceil(self.fittedModelFinal.predict(
                start=len(self.data),
                end=len(self.data)+self.horizon-1,
                dynamic=False)))
        
        '''Step 3: set error
            for AR, the size of trainData will be different from
            fitted values at model. Fill initial trainingPredictions
            with same data as real. This will no affect the evaluation
            metrics.
        '''
        
        errorObjs = self.setErrorData(trainingFit,testPredictions,runTimeObj)
        runTimeObj.setTestTime(runTimeObj.getTestTime()+totalTime)
        self.runTimeList.append(runTimeObj)
        # Add to ModelsResult list
        self.setModelResults(modelName,errorObjs,trainingFit,
                            testPredictions,forecasts)
        
    def fitAllModels(self):
        '''
            The verification in isFitted is necessary due to the order on which
            the models are passed (random).
        '''
        
        if not self.isFitted('SES'):
            self.exponentialFit('SES')
        
        if not self.isFitted('NAIVE'):
            self.exponentialFit('NAIVE')
        
        if not self.isFitted('HOLT'):
            self.exponentialFit('HOLT')
       
        if not self.isFitted('CR'):
            self.crostonFit()
        
        if not self.isFitted('AR'):
            self.ARFit()
       
        if not self.isFitted('ANN'):
            self.RN_Fit('ANN')
        
        if not self.isFitted('ELM'):
            self.RN_Fit('ELM')
        
        # Must be the last model to train.
        if not self.isFitted('CFM'):
            self.combinationFit('CFM')
            
        if not self.isFitted('CFE'):
            self.combinationFit('CFE')
        
    def autoFit(self):
        ''' Automatic selection based on minimal MASE.
            
            Parameters:
            -----------
            
        '''
       
        self.modelsName = self._standardModels
        self.initialize()
       
        self.fitAllModels()
        
        final,value = self.getBestByMetric('MASE')
        
        self.modelsResult.clear()
        self.modelsResult.append(final)
    
    def getBestByMetric(self,metricName):
        
        minError = sys.float_info.max
        finalModel = None
        
        for m in self.modelsResult:
            
            errorValue = obj.ForecastErro.getValueByMetricName(m.error,metricName,'TEST')
            
            if errorValue < minError:
                    minError = errorValue
                    finalModel = m
                    
        return finalModel,minError            
                    
    def getModelByName(self,name):
        
        for m in self.modelsResult:
            if m.model == name:
                return m
        return 'Invalid'    
            
    def combinationFit(self,model):
        '''
            Linear combination of models. Some matrix algebra is necessary here. 
            Using NumPy arrays for calculations.
            
            Variables
            ---------
            
            self.weights: a 1xN vector of weights of each model in pool. The weights
                can be equal for all models or assigned conform to some criteria.
                
            traningMatrix,testMatrix,forecastMatrix: Each column is a point on
                time and each row, the forecasts made by one model. The 
                combinations are performed over the columns.
        '''
        runTime = obj.ModelsRunTime(model)
        startTimeTraining = dt.datetime.now()
        totalTimeTraining = startTimeTraining - startTimeTraining
        startTimeTest = startTimeTraining
        totalTimeTest = totalTimeTraining
        
        # Step 1: set weights
        if model == 'CFM':
            coefVector = np.zeros(self.numModels)
            coefVector [:] = 1/self.numModels
            weights = np.array(coefVector)
        else:
            weights = self.setWeights()
          
        trainIndexes = self.getMinTrainIndex()
        
        # Step 2: build matrix of training/test/forecast values
        traningMatrix = np.array([])
        testMatrix = np.array([])
        forecastMatrix = np.array([])
        
        sizeTraining = len(trainIndexes)
        sizeTest = len(self.testData)
        
        # Same indexes for all models, including combination.
        trainingIdx = trainIndexes
        testIdx = self.testData.index
        forecastIdx = self.modelsResult[0].forecastDemand.index
        
        for m in self.modelsResult:
            
            if m.model not in ['CFE','CFM']:
                
                startTimeTraining = dt.datetime.now()
                traningMatrix = np.append(traningMatrix,
                                      m.trainingPrediction[trainIndexes].values)
                totalTimeTraining += dt.datetime.now() - startTimeTraining
                
                startTimeTest = dt.datetime.now()
                testMatrix = np.append(testMatrix,
                                   m.testPrediction.values)
                totalTimeTest += dt.datetime.now() - startTimeTest
                
                forecastMatrix = np.append(forecastMatrix,
                                       m.forecastDemand.values)
        
        # Reshape to get matrix
        startTimeTraining = dt.datetime.now()
        traningMatrix = np.reshape(traningMatrix,[self.numModels, sizeTraining])
        totalTimeTraining += dt.datetime.now() - startTimeTraining
        startTimeTest = dt.datetime.now()
        testMatrix = np.reshape(testMatrix,[self.numModels, sizeTest])
        totalTimeTest += dt.datetime.now() - startTimeTest
        forecastMatrix = np.reshape(forecastMatrix,[self.numModels, self.horizon])
        
        # Step 3: compute forecasts by matrix multiplication
        
        if self.combType == 'trimmed':
            # Exclude min and max from each predictions (lines)
            # This approach is not used anymore.
            trainingFit = ModelsManager.makeTrimmedMean(traningMatrix)
            testPredictions = ModelsManager.makeTrimmedMean(testMatrix)
            forecasts = ModelsManager.makeTrimmedMean(forecastMatrix)
            
        else: 
        
            startTimeTraining = dt.datetime.now()
            trainingFit = np.matmul(weights,traningMatrix)
            totalTimeTraining += dt.datetime.now() - startTimeTraining
            startTimeTest = dt.datetime.now()
            testPredictions = np.matmul(weights,testMatrix)
            totalTimeTest += dt.datetime.now() - startTimeTest
            forecasts = np.matmul(weights,forecastMatrix)
        
        trainingFit = pd.Series(np.ceil(trainingFit),trainingIdx)
        testPredictions = pd.Series(np.ceil(testPredictions),testIdx)
        forecasts = pd.Series(np.ceil(forecasts),forecastIdx)     
        
        errorObjs = self.setErrorData(trainingFit,testPredictions,runTime)
        totalTimeTest+= runTime.getTestTime()
        
        for t in self.runTimeList:
            
            if t.getModelName() not in ['ANN-OPT','ELM-OPT','CFM','CFE']:
            
                totalTimeTest += t.getTestTime()
                totalTimeTraining += t.getTrainingTime()    
        
        runTime.setTestTime(totalTimeTest)
        runTime.setTrainingTime(totalTimeTraining)
        
        self.runTimeList.append(runTime)
        
        # Add to ModelsResult list        
        self.setModelResults(model,errorObjs,trainingFit,
                            testPredictions,forecasts)
    
    def arimaOptimization(self):
        # Performs a grid search to find "optimal" parameters
        # for a ARIMA(p,d,q)
        
        p_values = range(4)
        d_values = range(3)
        q_values = range(4)
        
        for p in p_values:
            for d in d_values:
                for q in q_values:
                    
                    try:
                        model = arima(self.trainData,(p,d,q))
                        model = model.fit()
                    except:
                        print('Unable to fit a ARIMA('+
                              str(p)+','+
                              str(d)+','+
                              str(q)+').')                     
        
    def isFitted(self,modelName):
        
        for m in self.modelsResult:
            
            if m.model == modelName:
                return True
        return False
 
    @staticmethod
    def makeTrimmedMean(predictionsMatrix):
        '''
            Removes the upper and lower output of the predictors and
            returns a mean of remaining values.
            
            Actually working removing 2 predictions (one upper, one lower)
            
            Parameter
            ---------
            predictionsMatrix: a mxn numpy matrix.
                        
        '''
        # Step 1: sort columns
        predictionsMatrix = np.sort(predictionsMatrix,axis=0)
        
        # Step 2: remove first and last rows
        predictionsMatrix = np.delete(predictionsMatrix,
                                      [0, len(predictionsMatrix)-1],0)
        
        # Step 3: return the mean over lines
        final = np.mean(predictionsMatrix,0)
        
        return final
    
    def setWeights(self):
        # For based error strategy
        
        coefVector = np.zeros(self.numModels)
        metricValues = []
        sum = 0
        coefVector = []
        
        if self.weights is None:
            
            for m in self.modelsResult:
                
                if m.model not in ['CFE','CFM']:
                    val = obj.ForecastErro.getValueByMetricName(
                        m.error,self.combMetric,self.intervalUC)
                    metricValues.append(val)
                    sum = sum + val
                
            for m in metricValues:
                
                coefVector.append(1-(m/sum))    
            
            coefVector = np.array(coefVector)
            coefVector[:] = coefVector[:]/coefVector.sum() 
            self.weights = coefVector
        
        return self.weights
        
    # Build a ForecastErro list
    def setErrorData(self,trainingFit,testPredictions,runTimeObj=None):
        
        auxList = []
        trainingIdx = trainingFit.index
        startTimeAux = None
        totalTimeCum = None
        
        # Root Mean Squared Error - RMSE
        trainingErrorRMSE = round(ms.rmse(self.trainData[trainingIdx],
                                          trainingFit),
                                  ModelsManager._decimalPlaces)
        
        startTimeAux = dt.datetime.now()
        testErrorRMSE = round(ms.rmse(self.testData,testPredictions),
                              ModelsManager._decimalPlaces)
        totalTimeCum = dt.datetime.now() - startTimeAux
        
        auxList.append(obj.ForecastErro('RMSE',testErrorRMSE,'TEST'))
        auxList.append(obj.ForecastErro('RMSE',trainingErrorRMSE,'TRAIN'))
        
        #MAPE only all values > 0
        if 0 not in self.data.values:
             
            trainingErrorMAPE = round(
                ut.Utils.mape(
                    self.trainData[trainingIdx],
                    trainingFit),
                ModelsManager._decimalPlaces)
            
            startTimeAux = dt.datetime.now()
            testErrorMape = round(ut.Utils.mape(self.testData,testPredictions),
                                  ModelsManager._decimalPlaces)
            totalTimeCum += dt.datetime.now() - startTimeAux
            
            auxList.append(
                    obj.ForecastErro('MAPE',trainingErrorMAPE,'TRAIN'))
            auxList.append(
                    obj.ForecastErro('MAPE',testErrorMape,'TEST'))        
        
        # Mean Absolute Scaled Error
        trainingErrorMASE = round(
            ut.Utils.mase(
                self.trainData.to_numpy(),
                self.trainData[trainingIdx].to_numpy(),
                trainingFit.to_numpy()),
            ModelsManager._decimalPlaces)
        
        startTimeAux = dt.datetime.now()
        testErrorMASE = round(ut.Utils.mase(self.trainData.to_numpy(),
                                            self.testData.to_numpy(),
                                            testPredictions.to_numpy()),
                                  ModelsManager._decimalPlaces)
        totalTimeCum += dt.datetime.now() - startTimeAux
        
        runTimeObj.setTestTime(totalTimeCum)
        
        auxList.append(
                obj.ForecastErro('MASE',trainingErrorMASE,'TRAIN'))
        auxList.append(
                obj.ForecastErro('MASE',testErrorMASE,'TEST')) 
        
        return auxList
    
    # Build ModelResult object
    def setModelResults(self,model,error,trainingPrediction,
                       testPrediction,forecastDemand):
        
        # Replace negative values
        trainingPrediction[trainingPrediction<0] = 0
        testPrediction[testPrediction<0] = 0
        forecastDemand[forecastDemand<0] = 0
        
        self.modelsResult.append(
                obj.ModelsResult(model,error,trainingPrediction,
                       testPrediction,forecastDemand)
                )
    # Getters and Setters                
    
    def getMinTrainIndex(self):
        '''Returns the indexes of training set based on the model
         with a lower number of fitted values in this set.
         This method is necessary due to the computation of the weights
        for the Combination Forecast (CF) strategy.'''
        
        indexSize = sys.float_info.max
        referenceIndex = []
        
        for m in self.modelsResult:
            
            size = len(m.trainingPrediction.index)
            
            if size < indexSize:
                indexSize = size
                referenceIndex = m.trainingPrediction.index
         
        return referenceIndex 
    
    def getModelResults(self):
        return self.modelsResult
    
    def removeModel(self,name):
        
        model = self.getModelByName(name)
        self.modelsResult.remove(model)
    
    @staticmethod
    def getModelsNamesList():
        # AUTO must be appended here due the recursion in autofit method.
        a = ModelsManager._standardModels.copy()
        a.append('AUTO')
        return a
    
    @staticmethod
    def getMetricsNames():
        return ModelsManager._metrics     
    
    @staticmethod
    def getMinLength():
        return ModelsManager._minLength 

    @staticmethod
    def getHorizonLimit():
        return ModelsManager._horizonLimit	
