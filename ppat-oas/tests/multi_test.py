# coding=UTF-8
'''

Test script for multi method.

'''
# Fix import problems when running scripts
import sys,os,string
idx = os.getcwd().rfind('/')
sys.path.append(os.getcwd()[:idx])

import matplotlib.pyplot as plt
import pandas as pd
import requests
import json
import datetime as dt
import ws.objects as obj
import utils.util as ut
import numpy as np
import intermittent.croston as cr

# Data definition, global scope
file = u'Demanda corrediça.xlsx'
path = '../dataset/'
imagePath = '../images/'
frequency = 'M'#2W, 3W,D,W
modelo = ['AUTO']#['AR','HOLT','SES','NAIVE','CF1','CR','ANN','ELM']
sheet_names = ['2017','2018','2019']

# set this to localhost
url = "http://localhost:8083/forecast/multi/"
# or this to test the production environment
#url = "http://200.131.17.17:11009/forecast/single/"
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

filiais = pd.read_excel(path+file,'2017')
filiais = filiais['Unidade'].str.replace("  +","") #remover espaços
filiais = filiais.unique() 
filiais = ['São Paulo','Rio de Janeiro']
filiais.sort()

dados = pd.read_excel(path+file,None)

studyList = []

data = {'startDate': '01/01/2017',
        'endDate':'31/12/2019',
        'StudyList': studyList
         }
startTime = dt.datetime.now()
totalTime = None

# force error on the application.
'''studyList.append({'models': modelo,
            'frequency':frequency,
            'demands': [{
                'demandValue': -1,
                'demandDate': "31/12/2020"
                }],
            'horizon': 6,
            'part': 'Corrediça',
            'remoteStation': 'Macapá'
    })'''

for unidade in filiais:
 
    serie = ut.Utils.readCorredica(unidade,dados,sheet_names)
    
    studyData = {'models': modelo,
            'frequency':frequency,
            'demands': obj.Demand.getJSONDemandList(serie),
            'horizon': 6,
            'part': 'Corrediça',
            'remoteStation': unidade
    }
    
    studyList.append(studyData)
    
# Send request
r = requests.post(url, data=json.dumps(data), headers=headers)
print(r.text)
studyJson=r.json()

'''for m in studyJson['StudyList']:
    
    demands = ut.Utils.listDictToPandas(m['demands'],frequency)
    
    plt.title('Slide piece in '+m['remoteStation'])
    plt.plot(demands,'black')
    plt.xlabel('Time')
    plt.ylabel('Demand')
    plt.legend(['Forecast'+m['models'][0]])
    plt.gcf().autofmt_xdate()
    #plt.savefig(imagePath+m['remoteStation']+m['models'][0]+'- corredica.png',dpi = 300)
    plt.show()
    
totalTime = dt.datetime.now() - startTime
print()
print('Time to run tests:')
print(totalTime)
print()'''
