'''
Created on 24 de jun de 2019
Author:     1. Flavio Filho (Jun-Dec 2019)    
            2. Saymon G. Bandeira (Jun 2019-Today) 
            
Contact:    1. flaviofilho2908@gmail.com
            2. saymongb@gmail.com
Summary:    An auxiliary file containing a set of classes
            to represent input and output data from REST API
            to the ModelsManager class.
            
Note:       
Fix:
Status:        
'''

from flask_restplus import fields

class ModelsRunTime(object):
    
    # This class can compose a ModelsResult object in the future.
    
    def __init__(self,name=None,trainingTime=None,testTime=None):
        
        self._modelName = name
        self._trainingTime = trainingTime
        self._testTime = testTime
        
    def setTrainingTime(self,time):
        self._trainingTime = time
        
    def setTestTime(self,time):
        self._testTime = time
        
    def getTrainingTime(self):
        return self._trainingTime
    
    def getTestTime(self):
        return self._testTime
    
    def getModelName(self):
        return self._modelName            

class DistributedStudy(object):
    # It provides logic to multiple studies. Used in the tactical optimizer.
    
    def __init__(self,
                 ForecastStudyList,
                 startDate,
                 endDate):
        self.startDate = startDate
        self.endDate = endDate
        self.forecastStudy = ForecastStudyList
    

# Class to represent a single demand data
class Demand(object):
    
    def __init__(self,value,date):
        '''Params: value, a float value
                   date, a string representation of a date
        '''
        self.demandValue = value
        self.demandDate = date
        
    def json(self):
        return {
                "demandValue":self.demandValue,
                "demandDate":self.demandDate
                }
        
    @staticmethod
    def getJSONDemandList(demandList):
        # This method iterate over a Pandas.Series object
        
        temp = []
        values = demandList.values
        indexes = demandList.index.strftime('%d/%m/%Y')
        
        for value,index in zip(values,indexes):
            temp.append(Demand(float(value),index).json())
        return temp
    

# Class to handle the errors of the models.
class ForecastErro(object):
    
    def __init__(self, name, value,errorType='TEST'):
        
        self.name = name
        self.value = value
        self.errorType = errorType
    
    # Create return JSON data.
    def json(self):
        
        return {
                "name":self.name,
                "value":self.value,
                "errorType":self.errorType
                }
   
    @staticmethod
    def getJSONerroList(listError):
       temp = []
       for m in listError:
           temp.append(m.json())
       return temp    
   
    # Fixed to TEST set
    @staticmethod    
    def getValueByMetricName(errorList,metricName,type='TRAIN'):
        # Return the error for the Combination Strategy.
        for er in errorList:
            
            if er.errorType == type and er.name == metricName:
                return er.value 
    

# Stores the results of each model
class ModelsResult(object):
    
    def __init__(self, model,error,trainingPrediction,testPrediction,
                 forecastDemand,part=None,remoteStation=None):
        self.model = model
        self.part = part
        self.remoteStation = remoteStation
        self.error = error
        self.trainingPrediction = trainingPrediction
        self.testPrediction = testPrediction
        self.forecastDemand = forecastDemand
    
    # Create return JSON data.   
    def json(self): 

        return {
                "model":self.model,
                "part":self.part,
                "remoteStation":self.remoteStation,
                "trainingPrediction": Demand.getJSONDemandList(self.trainingPrediction),
                "testPrediction": Demand.getJSONDemandList(self.testPrediction), 
                "forecastDemand": Demand.getJSONDemandList(self.forecastDemand),
                "error": ForecastErro.getJSONerroList(self.error)
                }
        
class StudyResults(object):
    def __init__(self,processedDemand,modelsResults):
        self.processedDemand = processedDemand
        self.modelsResults = modelsResults
        
    def json(self):
            
        return{"processedDemand":Demand.getJSONDemandList(self.processedDemand),
                    "modelsResults":self.modelsResults
                    }
  
# This entity represents the models of data available in the API.
class documentationModel(object):
    
    def __init__(self,app):
        
        self.demandData = app.model('DemandData',
                                    {'demandValue': fields.Integer(required=True,
                                                                   description = 'The value of a single demand.',
                                                                   example=12),
                                     'demandDate': fields.String(required=True,
                                                                 description = 'A string representation of a date in the format DD/MM/YYYY.',
                                                                 example='01/12/2019')})
        

    
        
        self.modelForecastingStudy = app.model(
                'ForecastingStudy',
				  {'models': fields.List(fields.String(enum=['NAIVE','SES','AR','HOLT','CR','AUTO','CFM','CFE','ANN','ELM']),required = True, description="Names of the forecasting methods", example=['NAIVE','SES','AR','HOLT','CR','ANN','ELM','CFE']), 
                    'frequency': fields.String(enum=['M','W','D','A'],required = True, description="The frequency of series (M=Month,W=Week,D=Day and A=Year).", example='M'),
                    'demands': fields.List(fields.Nested(self.demandData),description="The historical (time-series) demand data.",required=True),
                    'horizon': fields.Integer(required = True,description="Number of periods to forecast", min=1, example=1),
                    'part':fields.String(description="Part name", example='Bico Dosador'),
                    'remoteStation':fields.String(description="Demand location", example='São Paulo'),},)

        self.modelErroMetric = app.model('ForecastErro', 
                                         {'name':fields.String(enum=['RMSE','MAPE','MASE'],required = True,description="Name of the  forecating erro",example='RMSE'),
                                          'value': fields.Float(required = True,description="Value of the  forecating erro",example=15.5),
                                          'errorType': fields.String(enum=['TEST','TRAIN'],required = True,description="The set name where metrics were calculated.",example='TRAIN')},)
    
        self.modelsResult = app.model('ModelsResult', 
				  {'model': fields.String(enum=['NAIVE','SES','AR','HOLT','CR','CFE','CFM','ANN','ELM'],required = True, description="Name of the forecasting method"),
                   'trainingPrediction': fields.List(fields.Nested(self.demandData),description="Predicted demands in trainning set."),
                    'testPrediction': fields.List(fields.Nested(self.demandData),description="Predicted demands in test set."),
                    'forecastDemand': fields.List(fields.Nested(self.demandData),description="Predicted demands for the horizon parameter."),
                    'error': fields.List(fields.Nested(self.modelErroMetric),description="Error of forecating"),
                    'part':fields.String(required = True,description="Part name", example='Bico Dosador'),
                    'remoteStation':fields.String(required = True,description="Demand location", example='São Paulo'),},)
        
        self.modelStudyResults = app.model('StudyResults', 
                                         {'processedDemand': fields.List(fields.Nested(self.demandData),description="Processed demand conform to specified granularity."),
                                          'modelsResults': fields.List(fields.Nested(self.modelsResult),description="List of model results")})
    
        self.forecastStudyList = app.model(
            'ForecastStudyList',
            {
            'startDate': fields.String(required=True,
                                        description = 'The initial date in the format DD/MM/YYYY.',
                                        example='01/12/2019'),
             'endDate': fields.String(required=True,
                                        description = 'The end date in the format DD/MM/YYYY.',
                                        example='01/12/2020'),
             'StudyList':fields.List(fields.Nested(self.modelForecastingStudy))                           
            }
            )
        
    def getmodelForecastingStudy(self):
        return self.modelForecastingStudy
    
    def getmodelsResult(self):
        return self.modelsResult
    
    def getmodelStudyResults(self):
        return self.modelStudyResults
    
    def getForecastStudyList(self):
        return self.forecastStudyList
    
# Customized exceptions.
class ErrorJson(BaseException):
   "Error decoding JSON, check your JSON."
   pass

class StudyException(BaseException):
    "Invalid study field."
    pass
