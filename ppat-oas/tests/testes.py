import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

file = '/home/saymongb/Downloads/selection_full_data.xls'
data = pd.read_excel(file,'T0_RMSE')
series = data[['REMOTE-STATION','PART']]
result = pd.Series(np.zeros(10),['NAIVE','SES','HOLT','AR','CR','ANN','ELM','CFE','CFM','AUTO'])
finalFrame = pd.DataFrame()

for row in series.iterrows():
    
    part = row[1][1]
    station = row[1][0]
    dfTemp = pd.DataFrame()
    
    for i in range(10):
        
        sheet = 'T'+str(i)+'_MASE'
        print(sheet+'\n')
        print('Estação:'+station+' - Peça: '+part)
        data = pd.read_excel(file,sheet)
        line = data[(data['REMOTE-STATION'] == station) & (data['PART'] == part)]
        dfTemp = dfTemp.append(line)
    print(dfTemp.mean())
    print(dfTemp.mean().idxmin())
    result[dfTemp.mean().idxmin()]+=1
    

print('\n Resultado: \n')
print(result)
print(result.sum()) 