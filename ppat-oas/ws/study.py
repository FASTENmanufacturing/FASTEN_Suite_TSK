'''
Created on 24 de jun de 2019
Author:     1. Flavio Filho (Jun-Dec 2019)    
            2. Saymon G. Bandeira (Jun 2019-Today) 
            
Contact:    1. flaviofilho2908@gmail.com
            2. saymongb@gmail.com
Summary:    A single forecast study. This class works as a bridge
            between the REST API and the forecast models manager.
            
Note:       
Fix:
Status:        
'''

from ws.objects import StudyResults,Demand
import ws.models_manager as ms
import utils.util as ut
from ws.objects import StudyException

class ForecastStudy(object):
    
    def __init__(self, models,demands,forecast_Horizon,part,remoteStation,frequency):
        self.models = models
        self.demands = demands
        self.forecast_Horizon = forecast_Horizon
        self.part = part
        self.remoteStation = remoteStation
        self.frequency = frequency
        
    def runStudy(self):      
        
        try:
            
            self.validaForecastingStudy()
            
            listResult = []
            jsonListModels = []
                  
            model = ms.ModelsManager(self.demands,
                                     self.forecast_Horizon,
                                     self.models)
            
            model.fit()
            listResult = model.getModelResults()
               
            for m in listResult:
                
                m.part = self.part
                m.remoteStation = self.remoteStation
                jsonListModels.append(m.json())
        
            listStudyResults = StudyResults(self.demands,jsonListModels)
            
            return listStudyResults.json()
            
        except (StudyException,ValueError) as e:
            raise e
        except Exception as e:
            raise e
    
    def validaForecastingStudy(self):
        # Perform verifications to raise custom exceptions.
        
        for m in list(self.models):
            if(not m in ms.ModelsManager.getModelsNamesList()):
                raise StudyException("Invalid name in models list.")
        try:
            self.demands = ut.Utils.listDictToPandas(self.demands,self.frequency)
        except Exception as e:
            raise StudyException(e.args[0])
        try:
            Horizon = 2 + self.forecast_Horizon # non numeric values will raise an error here
        except Exception as e:
            raise ValueError("Invalid value for forecast horizon.")
                    
        if len(self.demands) < ms.ModelsManager.getMinLength():
            raise StudyException("Not enough data to perform analysis.") 
        
        if self.forecast_Horizon > ms.ModelsManager.getHorizonLimit():
            raise StudyException("The forecast horizon exceeds the upper limit.")
        
    def json(self):
        
        return{
            'models': self.models,
            'frequency': self.frequency,
            'demands': Demand.getJSONDemandList(self.demands),
            'horizon': self.forecast_Horizon,
            'part': self.part,
            'remoteStation': self.remoteStation
            }    