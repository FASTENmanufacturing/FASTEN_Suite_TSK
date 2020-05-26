# Fix import problems when running scripts
import sys,os,string
idx = os.getcwd().rfind('/')
sys.path.append(os.getcwd()[:idx])

import ml_forecast.ANN as ann
from ml.Arquivo import Arquivo as ar
import pandas as pd
import matplotlib.pyplot as plt
import statistics as st
import math

# Data definition, global scope
file = u'Demanda corrediça.xlsx'
path = '../dataset/'
imagePath = 'Images/'
freq = 'W'
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
#unidade = 'Porto Velho'
#unidade = 'Rio de Janeiro'
#unidade = 'BALNEARIO CAMBORIU'
#unidade = 'Salvador'
#unidade = 'Brasília'
unidade = 'São Paulo'

filiais = pd.read_excel(path+file,'2017')
filiais = filiais['Unidade'].str.replace("  +","") #remover espaços
filiais = filiais.unique()
filiais = filiais[28:len(filiais)]
filiais = ['São Paulo']

for unidade in filiais:
    print(unidade)
    
    serie = readCorredica(unidade)
    
    plt.plot(serie)
    plt.title('Corrediça em '+unidade)
    plt.xlabel('Time')
    plt.ylabel('Demand')
    plt.gcf().autofmt_xdate()
    plt.show()
    
    
    
    resultado = []
    
    unidadeAux = ar.removerCaracteresEspeciais(unidade).replace(' ','')
    nomePasta = 'ML\\'+unidadeAux
    ar.criarPasta(nomePasta)
    
    resultado.append('Teste da unidade: '+unidade+'\n' )
    resultado.append('Nomeclaturas: M(Média), D(Desvio Padrão), N_O(Número de Neurônio da camada Oculta) \n' )
    resultado.append('\n' )
    
    
    
    estudos = []
    
    estudo1 = {'estudo': 'Estudo 1','activation_function': 'RELU/LINEAR','stopping_criterion': 'iterations','max_iterations': 200,'batch': 32,'optimizer': 'adam'}
    estudo2 = {'estudo': 'Estudo 2','activation_function': 'RELU/LINEAR','stopping_criterion': 'iterations','max_iterations': 400,'batch': 32,'optimizer': 'adam'}
    estudo3 = {'estudo': 'Estudo 3','activation_function': 'RELU/LINEAR','stopping_criterion': 'Early_stopping','max_iterations': 400,'batch': 32,'optimizer': 'adam'}
    estudo4 = {'estudo': 'Estudo 4','activation_function': 'SIGMOID/LINEAR','stopping_criterion': 'iterations','max_iterations': 200,'batch': 32,'optimizer': 'adam'}
    estudo5 = {'estudo': 'Estudo 5','activation_function': 'SIGMOID/LINEAR','stopping_criterion': 'iterations','max_iterations': 400,'batch': 32,'optimizer': 'adam'}
    estudo6 = {'estudo': 'Estudo 6','activation_function': 'SIGMOID/LINEAR','stopping_criterion': 'Early_stopping','max_iterations': 400,'batch': 32,'optimizer': 'adam'}
    
    
    estudos.append(estudo1)
    estudos.append(estudo2)
    estudos.append(estudo3)
    estudos.append(estudo4)
    estudos.append(estudo5)
    estudos.append(estudo6)
    
    for estudo in estudos:
        resultado.append('\n' )
        resultado.append('\n' )
        resultado.append(estudo['estudo']+'\n' )
        resultado.append('activation_function: '+estudo['activation_function']+'\n' )
        resultado.append('stopping_criterion: '+estudo['stopping_criterion']+'\n' )
        resultado.append('max_iterations: '+str(estudo['max_iterations'])+'\n' )
        resultado.append('batch: '+str(estudo['batch'])+'\n' )
        resultado.append('optimizer: '+estudo['optimizer']+'\n' )
        
        print(estudo['estudo'])
        print(estudo['activation_function'])
        print(estudo['stopping_criterion'])
        print(estudo['max_iterations'])
        print(estudo['batch'])
        print(estudo['optimizer'])
        print()
        
        resultado.append('\n' )
    
    
    
    
        resultado.append('Tabela da média dos 20 treinamentos:\n' )
    
        for atraso in range(1,6): 
        
    
            print('ATRASO ',atraso)
            model = ann.ANN(serie,
                atraso,
                12,
                estudo['activation_function'],
                estudo['stopping_criterion'],
                estudo['max_iterations'],
                estudo['batch'],
                0.80,
                estudo['optimizer'])
            qtd_neuronios, erro = model.best_neuron_amount()
    
           
            list_trainScoreMSE = []
            list_trainScoreRMSE = []
            list_testScoreMSE = []
            list_testScoreRMSE = []
    
            i = 0
            while(i != 20):
            
                model.fit()
                print('TEINAMENTO ',i)
                list_trainScoreMSE.append(model.trainScoreMSE)
                list_trainScoreRMSE.append(model.trainScoreRMSE)
                list_testScoreMSE.append(model.testScoreMSE)
                list_testScoreRMSE.append(model.testScoreRMSE)
            
            
                i = i + 1
        
        
            mean_trainScoreMSE = st.mean(list_trainScoreMSE)
            mean_trainScoreRMSE = st.mean(list_trainScoreRMSE)
            mean_testScoreMSE = st.mean(list_testScoreMSE)
            mean_testScoreRMSE = st.mean(list_testScoreRMSE)
        
            dev_trainScoreMSE = st.pstdev(list_trainScoreMSE)
            dev_trainScoreRMSE = st.pstdev(list_trainScoreRMSE)
            dev_testScoreMSE = st.pstdev(list_testScoreMSE)
            dev_testScoreRMSE = st.pstdev(list_testScoreRMSE)
            
            resultado.append('ATRASO  N_O      M(trainScoreMSE)     D(trainScoreMSE)     M(trainScoreRMSE)    D(trainScoreRMSE)    M(testScoreMSE)      D(testScoreMSE)      M(testScoreRMSE)     D(testScoreRMSE)       \n' )
            resultado.append(''+str(atraso)+'       '+ar.ajustarNumero2(qtd_neuronios)+'       '+ar.ajustarNumero(round(mean_trainScoreMSE,4))+''+ar.ajustarNumero(round(dev_trainScoreMSE,4))+''+ar.ajustarNumero(round(mean_trainScoreRMSE,4))+''+ar.ajustarNumero(round(dev_trainScoreRMSE,4))+''+ar.ajustarNumero(round(mean_testScoreMSE,4))+''+ar.ajustarNumero(round(dev_testScoreMSE,4))+''+ar.ajustarNumero(round(mean_testScoreRMSE,4))+''+ar.ajustarNumero(round(dev_testScoreRMSE,4))+'\n' )

    nomeArquivo = nomePasta+'\\'+unidadeAux+'.txt'
    ar.EscreverUmArquico(resultado,nomeArquivo)
    
    
    
    
    
    
    
    
    
