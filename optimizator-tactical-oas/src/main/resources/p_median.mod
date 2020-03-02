//usar Rotas para alimentar grafo em http://graphonline.ru/en/
//p median model from https://www.ibm.com/support/knowledgecenter/SSSA5P_12.5.1/ilog.odms.ide.help/OPL_Studio/oplquickstart/topics/opl_quickstart_pmedian.html


//reading Excel
tuple DemandType
{
	key int id;
	int Filial;
	string Cidade;
	string Estado;
	int DataPedido;
	int DataEmissaoNF;
	string Peca;
	int Demanda;
	float IdadeDoElevador;
};
{DemandType} DemandExcelSource = ...;
{DemandType} DemandExcel = {demand | demand in DemandExcelSource : demand.id !=0};

tuple DistanceType
{
	string 	CidadeOrigem;
	string 	CidadeDestino;
	float 	Distance;
	string 	DistanceReadable;
};
{DistanceType} DistanceExcelSource= ...;
{DistanceType} DistanceExcel = {distance | distance in DistanceExcelSource : distance.CidadeOrigem !=""};

tuple TimeType
{
	string 	CidadeOrigem;
	string 	CidadeDestino;
	float 	Duration;
	string 	DurationReadable;
};
{TimeType} TimeExcelSource= ...;
{TimeType} TimeExcel = {time | time in TimeExcelSource : time.CidadeOrigem !=""};


//Data
int P = ...;
float pesoTempo=...;
float pesoDistancia=...;

{string} Filiais;
{string} RemoteStations;
{string} Pecas;
int Demand[Filiais];

float Distance[Filiais][RemoteStations];
float Duration[Filiais][RemoteStations];

float NormDistance[Filiais][RemoteStations];//Normaliza a distancia
float NormDuration[Filiais][RemoteStations];//Normaliza a duracao

float CustoTotal[Filiais][RemoteStations];

//preprocessing data
execute{

	for(var line in DemandExcel){
		Filiais.add(line.Cidade);
		RemoteStations.add(line.Cidade);
		Pecas.add(line.Peca);
	}
	
	for(var filial in Filiais){
		for(var line in DemandExcel){
			if(line.Cidade==filial){
				Demand[filial]+= line.Demanda;					
			}		
		}		
	}
	
	var maxDistance=0;
	for(var tuple in DistanceExcel){
		Distance[tuple.CidadeOrigem][tuple.CidadeDestino] = tuple.Distance;
		if(tuple.Distance>maxDistance){
			maxDistance=tuple.Distance;		
		}
	}		


	var maxDuration = 0;
	for(var tupleTime in TimeExcel){
		Duration[tupleTime.CidadeOrigem][tupleTime.CidadeDestino] = tupleTime.Duration;
		if(tupleTime.Duration>maxDuration){
			maxDuration=tupleTime.Duration;		
		}
	}		
	
	for(var tuple in DistanceExcel){
		NormDistance[tuple.CidadeOrigem][tuple.CidadeDestino] = tuple.Distance/maxDistance;
	}
	
	for(var tupleTime in TimeExcel){
		NormDuration[tupleTime.CidadeOrigem][tupleTime.CidadeDestino] = tupleTime.Duration/maxDuration;
	}
	
	for(var tuple in DistanceExcel){
		CustoTotal[tuple.CidadeOrigem][tuple.CidadeDestino] = pesoDistancia*NormDistance[tuple.CidadeOrigem][tuple.CidadeDestino] + pesoTempo*NormDuration[tuple.CidadeOrigem][tuple.CidadeDestino];
	}
}



//Variables
dvar boolean AbrirRemoteStation[RemoteStations];
dvar boolean Rotas[Filiais][RemoteStations];

//Objective
minimize 
  sum( f in Filiais , rs in RemoteStations ) 
    //Demand[f]*Distance[f][rs]*Rotas[f][rs];
    //Demand[f]*Duration[f][rs]*Rotas[f][rs];
    Demand[f]*CustoTotal[f][rs]*Rotas[f][rs];

//Constraints
subject to {
  forall( f in Filiais )
    filialRecebeDeApenasUmaRemoteStation:
      sum( rs in RemoteStations ) 
        Rotas[f][rs] == 1;

  qtdRemotesStation:
    sum( rs in RemoteStations ) 
      AbrirRemoteStation[rs] == P;

  forall( f in Filiais , rs in RemoteStations )
    paraHaverRotaDeveExistirRemoteStation:
      Rotas[f][rs] <= AbrirRemoteStation[rs];
}



//posprocessing results
{string} RemoteStationsEscolhidas;
{string} ResultSet[RemoteStationsEscolhidas];
string Resultado[RemoteStationsEscolhidas];
tuple RemoteStationsType
{
	string RemoteStation;
	string Filial;
};
{RemoteStationsType} RemoteStationsExcel;

execute{


	for(var rs in RemoteStations){
		if(AbrirRemoteStation[rs]==1){
			RemoteStationsEscolhidas.add(rs);
		}
	}		
	
	for(var remoteStationsEscolhida in RemoteStationsEscolhidas){
		for(var f in Filiais){
			if(Rotas[f][remoteStationsEscolhida]==1){
				Resultado[remoteStationsEscolhida]+=f+", ";
				ResultSet[remoteStationsEscolhida].add(f);
				RemoteStationsExcel.add(remoteStationsEscolhida,f);
			}
		}	
	}
	
}


//calculando dados sobre o agrupamento das demandas para a Remote Station
int DemandaNF_LT[DemandExcel];

tuple DemandOrdenadaType
{
	key int id;
	int Filial;
	string Cidade;
	string Estado;
	int DataPedido;
	int DataEmissaoNF;
	string Peca;
	int Demanda;
	float IdadeDoElevador;
	int LT;
};
{DemandOrdenadaType} DemandOrdenadaExcel;

execute{

	//calculando LT NF
	for(var linha in DemandExcel){
		if(linha.DataEmissaoNF!=0 & linha.DataPedido!=0){	
			DemandaNF_LT[linha] = linha.DataEmissaoNF-linha.DataPedido;
		}	
	}
	
	//ordenando a demanda por agrupamentos de RemoteStations escolhidas e filiais
	for(var item in RemoteStationsExcel){
		for(var linha in DemandExcel){
			if(linha.Cidade==item.Filial)
				DemandOrdenadaExcel.add(linha,DemandaNF_LT[linha]);
		}			
	}
}


float DemandaCalc[RemoteStationsEscolhidas][Pecas];
float DemandaYearCalc[RemoteStationsEscolhidas][Pecas];
float QtdDemandaOrdensCalc[RemoteStationsEscolhidas][Pecas];
float AvgDemandaCalc[RemoteStationsEscolhidas][Pecas];
float AvgDemandaYearCalc[RemoteStationsEscolhidas][Pecas];

//Leading Time to emiting Nota Fiscal - NF
float LTNFCalc[RemoteStationsEscolhidas][Pecas];
float LTNFYearCalc[RemoteStationsEscolhidas][Pecas];
float QtdLTNFCalc[RemoteStationsEscolhidas][Pecas];
float AvgLTNFCalc[RemoteStationsEscolhidas][Pecas];
float AvgLTNFYearCalc[RemoteStationsEscolhidas][Pecas];

execute{	
	for(var linha in DemandOrdenadaExcel){
		for(var remoteStationEscolhida in RemoteStationsEscolhidas){
			if(ResultSet[remoteStationEscolhida].contains(linha.Cidade)){
				for(var peca in Pecas){
					if(linha.Peca==peca){
						DemandaCalc[remoteStationEscolhida][peca]+=linha.Demanda;
						QtdDemandaOrdensCalc[remoteStationEscolhida][peca]+=1;

						LTNFCalc[remoteStationEscolhida][peca]+=linha.LT;
						QtdLTNFCalc[remoteStationEscolhida][peca]+=1;
					}					
				}										
			}
		}
	}
	
	for(var r in RemoteStationsEscolhidas){
		for(var p in Pecas){
			
			if(QtdDemandaOrdensCalc[r][p]!=0){
				AvgDemandaCalc[r][p]=DemandaCalc[r][p]/QtdDemandaOrdensCalc[r][p];
			}else{
				AvgDemandaCalc[r][p]=0;
			}
			
			if(QtdLTNFCalc[r][p]!=0){
				AvgLTNFCalc[r][p]=LTNFCalc[r][p]/QtdLTNFCalc[r][p];
			}else{
				AvgLTNFCalc[r][p]=0;
			}		
			
			DemandaYearCalc[r][p]=DemandaCalc[r][p]/264;
			AvgDemandaYearCalc[r][p]=AvgDemandaCalc[r][p]/264;
			
			LTNFYearCalc[r][p]=LTNFCalc[r][p]/264;
			AvgLTNFYearCalc[r][p]=AvgLTNFCalc[r][p]/264;
			
		}	
	}
}

//StDesv Demanda
float StDesvAuxDemandaCalc[RemoteStationsEscolhidas][Pecas];
float StDesvDemandaCalc[RemoteStationsEscolhidas][Pecas];
float StDesvDemandaYearCalc[RemoteStationsEscolhidas][Pecas];


//StDesv Leading Time to emiting Nota Fiscal - NF
float StDesvAuxLTNFCalc[RemoteStationsEscolhidas][Pecas];
float StDesvLTNFCalc[RemoteStationsEscolhidas][Pecas];
float StDesvLTNFYearCalc[RemoteStationsEscolhidas][Pecas];


execute{	
	for(var linha in DemandOrdenadaExcel){
		for(var remoteStationEscolhida in RemoteStationsEscolhidas){
			if(ResultSet[remoteStationEscolhida].contains(linha.Cidade)){
				for(var peca in Pecas){
					if(linha.Peca==peca){
						StDesvAuxDemandaCalc[remoteStationEscolhida][peca]+=Opl.pow((linha.Demanda-AvgDemandaCalc[remoteStationEscolhida][peca]),2);
						StDesvAuxLTNFCalc[remoteStationEscolhida][peca]+=Opl.pow((linha.LT-AvgLTNFCalc[remoteStationEscolhida][peca]),2);
					}					
				}										
			}
		}
	}
	
	for(var r in RemoteStationsEscolhidas){
		for(var p in Pecas){
			//demanda	
			if(QtdDemandaOrdensCalc[r][p]!=0){
				StDesvDemandaCalc[r][p]=Opl.sqrt(StDesvAuxDemandaCalc[r][p]/QtdDemandaOrdensCalc[r][p]);
 			}else{
 				StDesvDemandaCalc[r][p]=0;
 			}		
 			
 			StDesvDemandaYearCalc[r][p]=StDesvDemandaCalc[r][p]/264;
 			
 			//LT NF
 			if(QtdLTNFCalc[r][p]!=0){
				StDesvLTNFCalc[r][p]=Opl.sqrt(StDesvAuxLTNFCalc[r][p]/QtdLTNFCalc[r][p]);
 			}else{
 				StDesvLTNFCalc[r][p]=0;
 			}		
 			
 			StDesvLTNFYearCalc[r][p]=StDesvLTNFCalc[r][p]/264;
 			
		}	
	}
}


//criando saida excel
tuple RemoteStationPartType
{
	string Cidade;
	string Peca;
	
	int Demanda;
	float DemandaYear;
	int QuantidadeOrdens;
	float DemandaAvg;
	float DemandaAvgYear;
	float DemandaStDesv;
	float DemandaStDesvYear;
	
	int NF_LT;
	float NF_LTYear;
	int QuantidadeOrdensNF;
	float NF_LTAvg;
	float NF_LTAvgYear;
	float NF_LTStDesv;
	float NF_LTStDesvYear;
		
};

{RemoteStationPartType} DemandCalcExcel;

execute{	
	
	
	for(var r in RemoteStationsEscolhidas){
		for(var p in Pecas){
			DemandCalcExcel.add(
				r,
				p,
				
				DemandaCalc[r][p],
			 	DemandaYearCalc[r][p],
			 	QtdDemandaOrdensCalc[r][p],
				AvgDemandaCalc[r][p],
				AvgDemandaYearCalc[r][p],
				StDesvDemandaCalc[r][p],
				StDesvDemandaYearCalc[r][p],
			
				LTNFCalc[r][p],
				LTNFYearCalc[r][p],
				QtdLTNFCalc[r][p],
				AvgLTNFCalc[r][p],
				AvgLTNFYearCalc[r][p],
				StDesvLTNFCalc[r][p],
				StDesvLTNFYearCalc[r][p]
				
				);
		}	
	}
}

				
