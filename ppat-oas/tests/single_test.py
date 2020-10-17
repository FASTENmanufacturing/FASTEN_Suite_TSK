# coding=UTF-8
'''
Test the single method using data extracted from TSK spreadsheet.
'''
# Fix import problems when running scripts
import sys,os,string
idx = os.getcwd().rfind('/')
sys.path.append(os.getcwd()[:idx])

import matplotlib.pyplot as plt
import pandas as pd
import requests
import json
import ws.objects as obj
import utils.util as ut
import numpy as np
import intermittent.croston as cr

# Data definition, global scope
file = u'Demanda corrediça.xlsx'
path = '../dataset/'
imagePath = '../images/'
frequency = 'M'#2W, 3W,D,W
modelsList = ['SES']#['AR','HOLT','SES','NAIVE','CF1','CR','ELM','ANN']
sheet_names = ['2017','2018','2019']

# set this to localhost
url = "http://localhost:8083/forecast/single/"
# or this to test the production environment
#url = "http://200.131.17.17:11009/forecast/single/"
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

filiais = pd.read_excel(path+file,'2017')
filiais = filiais['Unidade'].str.replace("  +","") # remove whitespace
filiais = filiais.unique() 
filiais = ['São Paulo']
filiais.sort()

dados = pd.read_excel(path+file,None)
df = pd.DataFrame()

for unidade in filiais:
 
    serie = ut.Utils.readCorredica(unidade,dados,sheet_names)
    
    data = {'models': modelsList,
            'frequency':'M',
            'demands': obj.Demand.getJSONDemandList(serie),
            'horizon': 6,
            'part': 'Corrediça',
            'remoteStation': unidade
    }
    
    # Send request
    r = requests.post(url, data=json.dumps(data), headers=headers)
    studyJson=r.json()

    print('Processing remote station:'+unidade)
    
    print(r.text)
    
    # Proccess series on same frequency
    '''sampleObj = serie.resample(frequency)
    newSeries  = sampleObj.sum()
    
    remoteStation =  studyJson['modelsResults'][0]['remoteStation']
    demands = ut.Utils.listDictToPandas(studyJson['processedDemand'],frequency)
    
    for m in studyJson['modelsResults']:
        
        testPreds = ut.Utils.listDictToPandas(m['testPrediction'],frequency)
        trainingPrediction = ut.Utils.listDictToPandas(m['trainingPrediction'],frequency)
        trainingPrediction = trainingPrediction.append(testPreds)
        forecasts = ut.Utils.listDictToPandas(m['forecastDemand'],frequency)
    
        plt.title('Slide piece in '+unidade)
        plt.plot(demands,'black')
        plt.plot(trainingPrediction,'blue')
        plt.plot(testPreds,'blue')
        plt.plot(forecasts,'green')
        plt.xlabel('Time')
        plt.ylabel('Demand')
        plt.legend(['Real','Fitted values',m['model']+'-Predictions'])
        plt.gcf().autofmt_xdate()
        #plt.savefig(imagePath+unidade+m['model']+'- corredica.png',dpi = 300)
        plt.show()'''
        