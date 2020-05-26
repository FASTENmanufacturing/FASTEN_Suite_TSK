# Fix import problems when running scripts
import sys,os,string
idx = os.getcwd().rfind('/')
sys.path.append(os.getcwd()[:idx])

import matplotlib.pyplot as plt
import pandas as pd
import math
import ws.models_manager as ms
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import Dense, Dropout
from keras.callbacks import EarlyStopping, ReduceLROnPlateau, ModelCheckpoint
from sklearn.metrics import mean_squared_error


# Data definition, global scope
file = u'Demanda corrediça.xlsx'
path = '../dataset/'
imagePath = 'Images/'
filial = u'São Paulo'
freq = 'M'
modelo = ['HOLT']
sheet_names = ['2017','2018','2019']
prop=80

def readCorredica(filial):
    #pasta de trabalho
    temp = pd.Series()
    
    for sheet in sheet_names:
        
        spr = pd.read_excel(path+file,sheet)
        spr['Unidade'] = spr['Unidade'].str.replace("  +","") #remover espaços
        newSpr = spr[spr['Unidade'] == filial] # apenas da filial especificada
        timeSeries = pd.Series(data=newSpr['Quantidade'].values,
                          index=newSpr['Data'].values)
        temp = temp.append(timeSeries)
        
    sample =  temp.resample(freq)
    timeSeries = sample.sum()    
    
    return timeSeries

unidade = 'Rio de Janeiro'
#unidade = 'São Paulo'
serie = readCorredica(unidade)

plt.plot(serie)
plt.title('Corrediça em '+unidade)
plt.xlabel('Time')
plt.ylabel('Demand')
plt.gcf().autofmt_xdate()
plt.show()


# convert an array of values into a dataset matrix
def create_dataset(dataset, time_delay=1):
	dataX, dataY = [], []
	for i in range(len(dataset)-time_delay):
		a = dataset[i:(i+time_delay),0]
		dataX.append(a)
		dataY.append(dataset[i + time_delay])
	return np.array(dataX), np.array(dataY)


def split_train_and_test(dataset,percentage=0.80, time_delay = 1):
    dataset.reshape(( len(dataset),1))
    train_size = int(len(dataset) * percentage)
    train, test = dataset[0:train_size], dataset[(train_size-time_delay):len(dataset)]
    return train, test

def creat_model(time_delay):
    
    model = Sequential()
    
    model.add(Dense(3, input_dim=time_delay, activation='relu'))
    model.add(Dropout(0.1))
    #model.add(Dense(8, activation='relu'))tanh
    model.add(Dense(8, activation='relu'))
    model.add(Dropout(0.1))
    model.add(Dense(1))
    model.compile(loss='mean_squared_error', optimizer='adam')
    return model

def estimator_Error(model,trainX,trainY,testX, testY):
    trainScore = model.evaluate(trainX, trainY, verbose=0)
    print('Train Score: %.2f MSE (%.2f RMSE)' % (trainScore, math.sqrt(trainScore)))
    testScore = model.evaluate(testX, testY, verbose=0)
    print('Test Score: %.2f MSE (%.2f RMSE)' % (testScore, math.sqrt(testScore)))
    
def forecating(horizion, time_delay,dataset, model):
    previsoesX = np.zeros((horizion, time_delay))
    previsoesY = np.zeros((horizion))
    
    posicaoInicialDosUltimosTres = len(dataset)-(time_delay)
    posicaoFinalDosUltimosTres = len(dataset)
    previsoesX[0] = dataset[posicaoInicialDosUltimosTres:posicaoFinalDosUltimosTres,0]
    aux = previsoesX[0].reshape((1, time_delay))
    
    for h in range(horizion):
        previsoesY[h] = model.predict(aux)
    
        if h < horizion-1:
            #print(model.predict(aux))
            for t in range(time_delay-1):
                previsoesX[h+1][t] = previsoesX[h][t+1] 
            previsoesX[h+1][time_delay-1] = previsoesY[h]
            aux = previsoesX[h+1].reshape((1, time_delay))
            
    return previsoesX, previsoesY

def imprimir_Grafico(dataset,trainPredict, testPredict, previsoes, horizion,time_delay):
    # shift train predictions for plotting
    trainPredictPlot = np.zeros((len(dataset), 1))
    trainPredictPlot[:, :] = np.nan
    trainPredictPlot[time_delay:len(trainPredict)+time_delay, :] = trainPredict
    # shift test predictions for plotting
    testPredictPlot = np.zeros((len(dataset), 1))
    testPredictPlot[:, :] = np.nan
    testPredictPlot[len(trainPredict)+(time_delay):len(dataset), :] = testPredict
    #correção da previsão para plotar
    previsoesPlot = np.zeros((horizion+len(dataset), 1))
    previsoesPlot[:, :] = np.nan
    previsoesPlot[(len(dataset)):(horizion+len(dataset))] = previsoes.reshape((horizion, 1))
    # plot baseline and predictions
    plt.plot(dataset)
    plt.plot(trainPredictPlot)
    plt.plot(testPredictPlot)
    plt.plot(previsoesPlot)
    plt.show()
    
    return trainPredictPlot, testPredictPlot, previsoesPlot

#Normalizar base

#transformar serie para estrutura numpy
#dataset = np.array(serie)
dataset = np.array(serie)
normalizador = MinMaxScaler()
dataset_normalizada = normalizador.fit_transform(dataset.reshape(-1, 1))

plt.plot(dataset_normalizada)
plt.show()
time_delay = 3
# split into train and test sets
train, test = split_train_and_test(dataset_normalizada,0.80,time_delay)
# reshape dataset
trainX, trainY = create_dataset(train, time_delay)
testX, testY = create_dataset(test, time_delay)
# create and fit Multilayer Perceptron model
model = creat_model(time_delay)
# treinando modelo
model.fit(trainX, trainY, epochs=500, batch_size=32, verbose=2)
# Estimate model performance
estimator_Error(model,trainX,trainY,testX,testY)
# generate predictions for training
trainPredict = model.predict(trainX)
testPredict = model.predict(testX)
#gerar previsões futuras
horizion = 12
previsoesX, previsoesY = forecating(horizion,time_delay,dataset_normalizada,model)
#imprimir resultado
datasetFinal = normalizador.inverse_transform(dataset_normalizada)
trainYFinal = normalizador.inverse_transform(trainY)
testYFinal = normalizador.inverse_transform(testY)
trainPredictFinal = normalizador.inverse_transform(trainPredict)
testPredictFinal = normalizador.inverse_transform(testPredict)
previsoesYFinal = normalizador.inverse_transform(previsoesY.reshape(1, -1))

errotrain = mean_squared_error(trainYFinal, trainPredictFinal)
errotest = mean_squared_error(testYFinal, testPredictFinal)
print("train MSE: ",errotrain)
print("train RMSE: ",math.sqrt(errotrain))
print("test MSE: ",errotest)
print("test RMSE: ",math.sqrt(errotest))



trainPredictPlot, testPredictPlot, previsoesPlot = imprimir_Grafico(dataset_normalizada,trainPredict,testPredict,previsoesY,horizion,time_delay)

trainPredictPlot, testPredictPlot, previsoesPlot = imprimir_Grafico(datasetFinal,trainPredictFinal,testPredictFinal,previsoesYFinal,horizion,time_delay)


