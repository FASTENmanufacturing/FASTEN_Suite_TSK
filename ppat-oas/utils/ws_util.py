'''
Created on  21 de jan de 2020
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    Auxiliary functions to support the Web Service operations.
Note:       

Fix: .
Status: On going.        
'''
from ws import study as st
import utils.util as ut
import json
import datetime as dt
from ws.objects import ErrorJson,StudyException

class WebServiceTools:

    @staticmethod
    def decodeForecastStudyList(request):
        
        # Build and returns a list of ForecastStudy.
        forecastStudyList = []
        forecastStudy = None
        startDate = None
        endDate = None
        error = False

        startDate = request.json['startDate']
        endDate = request.json['endDate']
        
        for m in request.json['StudyList']:
            
            forecastStudy = st.ForecastStudy(
                models = ['AUTO'] ,
                demands = m['demands'],
                forecast_Horizon = m['horizon'],
                part = m['part'],
                remoteStation = m['remoteStation'],
                frequency = m['frequency'])
            
            forecastStudyList.append(forecastStudy)   
            
        return forecastStudyList
    
    @staticmethod
    def createResultList(forecastStudyList,request):
        # Runs and builds a list of the required studies.
        
        tempList = []
        forecastStudy = None
        error = False
        
        for study in forecastStudyList:
            
            # This point can be slow.
            res = study.runStudy()
             
            forecastStudy = st.ForecastStudy(
                models = [res['modelsResults'][0]['model']] ,
                demands = res['modelsResults'][0]['forecastDemand'],
                forecast_Horizon = study.forecast_Horizon,
                part = res['modelsResults'][0]['part'],
                remoteStation = res['modelsResults'][0]['remoteStation'],
                frequency = study.frequency
                )
            
            tempList.append(forecastStudy)
                
        startDate = request.json['startDate']
        endDate = request.json['endDate']
        
        return {
            'startDate': startDate,
            'endDate': endDate,
            'StudyList': tempList
            }        
         
    @staticmethod
    def decode(request):
        
        data = {}
        
        try:
            data['demands'] = request.json['demands'] 
        except Exception:
            raise ErrorJson("Error decoding demands, check your json.")
        try:
            data['models'] = request.json['models']
        except Exception:
            raise ErrorJson("Error decoding models, check your json.")  
        try:
           data['horizon'] = request.json['horizon']
        except Exception:
            raise ErrorJson("Error decoding horizon, check your json.")
        try:
            data['part'] = request.json['part']
        except Exception:
            raise ErrorJson("Error decoding part, check your json.")
        try:
            data['remoteStation'] = request.json['remoteStation']
        except Exception:
            raise ErrorJson("Error decoding remoteStation, check your json.")
        try:
            data['frequency'] = request.json['frequency']
        except Exception:
            raise ErrorJson('Error decoding frequency, check your json.')
         
        return data 
       