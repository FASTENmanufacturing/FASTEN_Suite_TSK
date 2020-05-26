'''
Created on Wed Feb  19 23:06:00 2020
Author:     Saymon G. Bandeira
Contact:    saymongb@gmail.com
Summary:    This script was created to merge three Microsoft Excel
            files sent by ThyssenKrupp colleagues with the 
            historical demands of some spare parts.
Note:       
Fix: 
Status: Finished.       
Next: N/A.
'''

import numpy as np
import pandas as pd

fileCorredica = u'Demanda corrediça.xlsx'
corredicaSheets = ['2017','2018','2019']
fileElevadores = u'demandas_elevadores.xlsx'
filePlastica = u'Demanda peças plastica.xlsx'
path = '../dataset/'

cols = ['STATIONS','DATE','PARTS','DEMANDS']
data = pd.DataFrame(columns = cols)
dataTemp = pd.DataFrame(columns = cols)

# Elevator datasheet
temp = pd.read_excel(path+fileElevadores)
dataTemp[cols] = temp[['Cidade','Data Emissão NF','Peça','Demanda']]
data = data.append(dataTemp,ignore_index=True)

# Peça plástica datasheet
temp = pd.read_excel(path+filePlastica)
dataTemp = pd.DataFrame(columns = cols)
dataTemp[cols] = temp[['Unidade','Data','Descrição Material','Quantidade']]
data = data.append(dataTemp,ignore_index=True)

# Corrediça datasheet
temp = pd.read_excel(path+fileCorredica,None)
for sheet in corredicaSheets:
    tempAux = temp[sheet]
    tempAux['Unidade'] = tempAux['Unidade'].str.replace("  +","") #remove extra space at end of the string
    dataTemp = pd.DataFrame(columns = cols)
    dataTemp[cols] = tempAux[['Unidade','Data','Descrição Material','Quantidade']]
    data = data.append(dataTemp,ignore_index=True)

# Treat inconsistencies
data['STATIONS'] = data['STATIONS'].str.replace("Aracajú","ARACAJU")
data['STATIONS'] = data['STATIONS'].str.replace("BALNEÁRIO CAMBURIÚ","BALNEARIO CAMBORIU")
data['STATIONS'] = data['STATIONS'].str.replace("BARUERI / OSASCO","BARUERI")
data['STATIONS'] = data['STATIONS'].str.replace("POA - CENTRO SUL","POA - Central SUL")
data['STATIONS'] = data['STATIONS'].str.replace("NITERÓI","NITEROI")
data['STATIONS'] = data['STATIONS'].str.replace("SÃO LUIS","São Luiz do Maranhão")
data['STATIONS'] = data['STATIONS'].str.replace("MACEIÓ","MACEIO")
data['STATIONS'] = data['STATIONS'].str.replace("Sâo José dos Campos ","SÃO JOSÉ DOS CAMPOS")
data['STATIONS'] = data['STATIONS'].str.upper()
data['PARTS'] = data['PARTS'].str.upper()

#write data to disk
writer = pd.ExcelWriter(path+'DATA'+'.xls')
data.to_excel(excel_writer=writer,sheet_name='demands',index=False)  
writer.save()

# Summary of the data generated on the process
parts = data.groupby(['PARTS'],as_index=False).count()[['PARTS','DEMANDS']]
parts.columns = ['PARTS','ORDERS']
ordersByRemoteStations = data.groupby(by=['STATIONS','PARTS'],
                                      as_index=False).count()[['STATIONS','PARTS','DEMANDS']]
ordersByRemoteStations.columns = ['STATIONS','PARTS','ORDERS']
ordersFrame = ordersByRemoteStations.groupby(['STATIONS'],as_index=False).sum()
partsFrame = ordersByRemoteStations.groupby(['STATIONS'],as_index=False).count()
ordersFrame = ordersFrame.join(partsFrame[['PARTS']])

writer_ts = pd.ExcelWriter(path+'DATA-SUMMARY'+'.xls')

ordersByRemoteStations.to_excel(excel_writer=writer_ts,sheet_name='ORDERS',index=False)
parts.to_excel(excel_writer=writer_ts,sheet_name='PARTS',index=False) 
ordersFrame.to_excel(excel_writer=writer_ts,sheet_name='STATIONS',index=False)

writer_ts.save()