// main {
//  thisOplModel.generate();
//
//  var locationAllocation = thisOplModel;
//  var varyMaxH = locationAllocation.max_H;
//
//  var best;
//  var curr = Infinity;
//  var basis = new IloOplCplexBasis();
//  var ofile = new IloOplOutputFile("location_allocation_main.txt");
//
//  while ( 1 ) {
//    
//  	best = curr;
//  	d1 = new Date();
//  	remoteStationsEscolhidasList="";
//  	if ( cplex.solve() ) {
//  		curr = cplex.getObjValue();
//  		d2 = new Date();
//  		timeInMilis = d2-d1;
//  		for(var rs in locationAllocation.R){
//			if(locationAllocation.x[rs]==1){
//				remoteStationsEscolhidasList += rs + "; ";
//			}
//		}
//	 	writeln("max_H = ", varyMaxH, " is ", curr, " tempo de execucao = ", timeInMilis, " cidades escolhidas = ", remoteStationsEscolhidasList);
//		writeln();
//		ofile.writeln("max_H = ", varyMaxH, " is ", curr, " tempo de execucao = ", timeInMilis, " cidades escolhidas = ", remoteStationsEscolhidasList);        
//  	} else {
//    	writeln("No solution!");
//  	}
//  
//  	//if ( best==curr ) break;
//  	
//  	if(varyMaxH==49) break;
//
//    // prepare next iteration
//    var def = locationAllocation.modelDefinition;
//    var data = locationAllocation.dataElements;
//      
//    if ( locationAllocation!=thisOplModel ) {
//      locationAllocation.end();
//    }
//
//    locationAllocation = new IloOplModel(def,cplex);
//    varyMaxH++;
//    data.max_H = varyMaxH;
//    locationAllocation.addDataSource(data);
//    locationAllocation.generate();
//
//  }
//
//}
 
 //Parametros globais
float FC_R = ...;//custo anual da remote station
float cc = ...;//custo anual de posse do estoque fracao do custo capital (custo de capital representa uma taxa minima que uma empresa deve superar antes de poder gerar valor)
float sc = ...;//stockout custo
float timeImportance=...;
float costImportance=...;
float max_H = ...;//maximo de horas para entrega das peças

//Impressora
tuple ImpressoraType
{
	key int rowNum; 
	string marca;
	float capacidadeAnual;
	float custoFixoAnual;
	int numeroMaximo;
};
{ImpressoraType} ImpressoraExcelSource = ...;
{ImpressoraType} ImpressoraExcel = {impressora | impressora in ImpressoraExcelSource : impressora.rowNum !=0};

float FC_P=0;
float C_P =0;
int max_P =0;
execute{
	var line = ImpressoraExcel.get(1);
	FC_P = line.custoFixoAnual;//custo da impressora
	C_P = line.capacidadeAnual;//capacidade da impressora
	max_P = line.numeroMaximo;//maximo de impressoras 3D por remote stations
}

//Parametros do resultado do pmedian
{string} PExcelSource=...;// parts
{string} P= {part | part in PExcelSource : part!=""};

tuple RemoteType
{
	string remote;//remote station
};
{RemoteType} RExcelSource=...;// remote stations
{string} R = {remoteStation.remote | remoteStation in RExcelSource : remoteStation.remote !=""};//remotes stations


tuple RemotePascalCaseType
{
	key string remote;//remote station
	string PascalCase; //pascal case usado para construir a planilha de simulacao
};
{RemotePascalCaseType} RPascalCaseExcelSource=...;
{RemotePascalCaseType} RPascalCase={remoteStationPascalCase | remoteStationPascalCase in RPascalCaseExcelSource : remoteStationPascalCase.remote !=""};//remotes stations


// #################################################### SUPPLIER ###################################

//lendo supplier
tuple SupplierType
{
	string Supplier;
	string Cidade;
};

{SupplierType} SupplierExcelSource= ...;
{SupplierType} SupplierExcel= {supplier | supplier in SupplierExcelSource : supplier.Supplier!=""};

{string} S;// suppliers
string citySupplier[S];//city of supplier
execute{
	for(var line in SupplierExcel){
		S.add(line.Supplier);
	}
	for(var s in S){
		for(var line in SupplierExcel){
		 	if(line.Supplier==s){
				citySupplier[s]=line.Cidade;
		 	}		
		}	
	}
}

// #################################################### pmedian DEMAND ###################################
//lendo clients das remotes do pmedian
//lendo supplier
tuple RemoteStationsFiliaisType
{
	string remote;
	string filial;
};

{RemoteStationsFiliaisType} RemoteStationsFiliaisExcelSource= ...;
{RemoteStationsFiliaisType} RemoteStationsFiliaisExcel= {line | line in RemoteStationsFiliaisExcelSource : line.remote!=""};

//lendo DemandaCalc do pmedian
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

{RemoteStationPartType} DemandCalcExcelSource= ...;
{RemoteStationPartType} DemandCalcExcel=  {line | line in DemandCalcExcelSource : line.Cidade!=""};


int DemandaCalc[R][P];
float QtdDemandaOrdensCalc[R][P];
float AvgDemandaCalc[R][P];
float StDesvDemandaCalc[R][P];
float LTNFCalc[R][P];
float QtdLTNFCalc[R][P];
float AvgLTNFCalc[R][P];
float StDesvLTNFCalc[R][P];

execute{	
	
	
	for(var r in R){
		for(var p in P){
			for(var line in DemandCalcExcel){
				if(line.Cidade==r && line.Peca ==p){
					DemandaCalc[r][p] = line.Demanda;
					QtdDemandaOrdensCalc[r][p]= line.QuantidadeOrdens;
					AvgDemandaCalc[r][p]=line.DemandaAvg;
					StDesvDemandaCalc[r][p]=line.DemandaStDesv;
					LTNFCalc[r][p]=line.NF_LT;
					QtdLTNFCalc[r][p]=line.QuantidadeOrdensNF;
					AvgLTNFCalc[r][p]=line.NF_LTAvg;
					StDesvLTNFCalc[r][p]=line.NF_LTStDesv;
				}
			}
 		}
 	}		
}



// #################################################### PART ###################################

//lendo ProcessingPartCost
tuple ProcessingPartCostInfo
{
	key int rownum;
	string sram;
	string Peca;
	float Cost;
	float ProductionTime;
};

{ProcessingPartCostInfo} ProcessingPartCostExcelSource= ...; 
{ProcessingPartCostInfo} ProcessingPartCostExcel= {line | line in ProcessingPartCostExcelSource : line.rownum!=0};

//lendo SupplyPartCost
tuple SuppliedPartCostInfo
{
	string Supplier;
	string Peca;
	float Cost;
};

{SuppliedPartCostInfo} SuppliedPartCostExcelSource= ...; 
{SuppliedPartCostInfo} SuppliedPartCostExcel = {line | line in SuppliedPartCostExcelSource : line.Supplier!=""};

//Part
float uc[R][P];//custo unitario da peca
float pt[R][P];//processing time of part (hours)
float cp[R][P]; //capacidade de producao da impressora por parte (hours by year)

float price[S][P];//custo de fornecimento da peca 

execute{	
	
	for(var r in R){
		for(var p in P){
			for(var line in ProcessingPartCostExcel){
				if(line.sram=="MKB_DBT_01"){		
					if(line.Peca ==p){
						uc[r][p] = line.Cost;
						pt[r][p] = line.ProductionTime;
						cp[r][p] = C_P/pt[r][p]; // = Annual capacity of a 3D printer (in hours) / 3D Printer processing time (hours)
					}
  				}				
			}
 		}
 	}
 	
 	
 	for(var s in S){
		for(var p in P){
			for(var tupla in SuppliedPartCostExcel){
				if(tupla.Supplier==s && tupla.Peca==p){
					price[s][p] = tupla.Cost;
				}
			}
 		}
 	}		
}





// ############################################# DELIVERY ########################################

tuple DistanceType
{
	string 	CidadeOrigem;
	string 	CidadeDestino;
	float 	Distance;
	string 	DistanceReadable;
};
{DistanceType} DistanceExcelSource= ...;
{DistanceType} DistanceExcel= {line | line in DistanceExcelSource : line.CidadeOrigem!=""};

tuple TimeType
{
	string 	CidadeOrigem;
	string 	CidadeDestino;
	float 	Duration;
	string 	DurationReadable;
};
{TimeType} TimeExcelSource= ...;
{TimeType} TimeExcel= {line | line in TimeExcelSource : line.CidadeOrigem!=""};


//tuple SedexType
//{
//	string CidadeOrigem;
//	string CEPOrigem;
//	string CidadeDestino;
//	string CEPDestino;
//	float ValorSEDEX;
//	float ValorPagSeguro;
//};
//{SedexType} SedexExcelSource= ...;
//{SedexType} SedexExcel=  {line | line in SedexExcelSource : line.CidadeOrigem!=""};

float dcUber[R][R];//custo de entrega da peca de remote para remote
//float dcSedex[R][R];//custo de entrega da peca de remote para remote
//float dcPagSeguro[R][R];//custo de entrega da peca de remote para remote
float dt[R][R];//tempo de entrega da peca de remote para remote

float sdc[S][R]; //custo de entrega da peca a partir de um fornecedor
float st[S][R]; //tempo de entrega da peca a partir de um fornecedor

//zscore
//{float} percentage =...;
//float zscore[percentage] = ...;




execute{

	for(var sender in R){
		for(var receiver in R){	
			for(var tupleDistance in DistanceExcel){
				if(tupleDistance.CidadeOrigem==sender && tupleDistance.CidadeDestino==receiver){	
					dcUber[sender][receiver] = (tupleDistance.Distance/1000)*1.43; //UberX: R$ 1,43 o km e R$ 0,35 por minuto
	  			}
   			}
		}		
	}
	
//	for(var sender in R){
//		for(var receiver in R){	
//			for(var tupleSedex in SedexExcel){
//				if(tupleSedex.CidadeOrigem==sender && tupleSedex.CidadeDestino==receiver){	
//					dcSedex[sender][receiver] = tupleSedex.ValorSEDEX;
//					dcPagSeguro[sender][receiver] = tupleSedex.ValorPagSeguro;
//	  			}
//   			}
//		}		
//	}
	
	for(var sender in R){
		for(var receiver in R){	
			for(var tupleTime in TimeExcel){
				if(tupleTime.CidadeOrigem==sender && tupleTime.CidadeDestino==receiver){	
					dt[sender][receiver] = tupleTime.Duration/(60*60);//horas
	  			}
   			}
		}		
	}
	
	for(var s in S){
		for(var r in R){	
			for(var tDistance in DistanceExcel){
				if(tDistance.CidadeOrigem==citySupplier[s] && tDistance.CidadeDestino==r){	
					sdc[s][r] = 100000;//(tDistance.Distance/1000)*1.43; //UberX: R$ 1,43 o km e R$ 0,35 por minuto
	  			}
   			}
		}		
	}

	for(var s in S){
		for(var r in R){	
			for(var tTime in TimeExcel){
				if(tTime.CidadeOrigem==citySupplier[s] && tTime.CidadeDestino==r){	
					st[s][r] = 500;//(tTime.Duration/(60*60))*20;//horas
	  			}
   			}
		}		
	}
	
	

}


// ############################################# INTERNAL ORDER COST ########################################
tuple InternalOrderCostInfo
{
	string Cidade;
	string Peca;
	float Cost;
	float IssueTime;
};
{InternalOrderCostInfo} InternalOrderCostExcelSource= ...; 
{InternalOrderCostInfo} InternalOrderCostExcel= {line | line in InternalOrderCostExcelSource : line.Cidade!=""};
	
//Order cost
float ioc[R][P];//custo da ordem interna de remote para remote
float ioct[R][P];//tempo NF da ordem interna de remote para remote (em horas)
execute{
	for(var r in R){
		for(var p in P){
			for(var line in InternalOrderCostExcel){
				if(line.Cidade==r && line.Peca ==p){
					ioc[r][p] = line.Cost;
				}
			}
 		}
 	}
 	
 	for(var r in R){
		for(var p in P){
			ioct[r][p] = 0;//AvgLTNFCalc[r][p]*8;//horas  LTNFCalc DADos inconsistentes
 		}
 	}
}




//Total cost pre-calculate
float rtc[R][R][P]; //total remote to remote cost 
float rtt[R][R][P]; //total remote to remote time 
float normRemoteCost[R][R][P];

float stc[S][R][P]; //total supllier to remote cost
float stt[S][R][P]; //total supllier to remote time
float normSupplierCost[S][R][P];

tuple TotalRemoteStationCostType
{
	string sender;
	string receiver;
	string part;
	float cost;
	float time;
	float percent;
}

tuple TotalSupplierCostType
{
	string supplier;
	string receiver;
	string part;
	float cost;
	float time;
	float percent;
}

{TotalRemoteStationCostType} TotalRemoteStationCostExcel;
{TotalSupplierCostType} TotalSupplierCostExcel;

float rtcMax=0; //184.11 demand //7276.5 demand 32000 R$
float rttMax=0; //73.93  h
float rtcMaxTotalFixo=191.41;
float rttMaxTotalFixo=84.775;
float stcMax=0;
float sttMax=0;
float stcMaxTotalFixo=7063;//247413.865151515;
float sttMaxTotalFixo=500;


execute
{
	
	//montando custo total de producao
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				rtc[sender][receiver][product] = uc[sender][product] + dcUber[sender][receiver] + ioc[sender][product];//R$
				rtt[sender][receiver][product] = pt[sender][product] + dt[sender][receiver] + ioct[sender][product];//horas
				if(rtc[sender][receiver][product]>rtcMax){
					rtcMax=rtc[sender][receiver][product];				
				}
				if(rtt[sender][receiver][product]>rttMax){
					rttMax=rtt[sender][receiver][product];				
				}
 			}			
		}	
	}
	
	//montando custo total de fornecimento
	for(var supplier in S){
		for(var receiver in R){
			for(var product in P){
				stc[supplier][receiver][product] = price[supplier][product] + sdc[supplier][receiver];//R$
				stt[supplier][receiver][product] = 0					    + st[supplier][receiver];//horas
				if(stc[supplier][receiver][product]>stcMax){
					stcMax=stc[supplier][receiver][product];				
				}
				if(stt[supplier][receiver][product]>sttMax){
					sttMax=stt[supplier][receiver][product];				
				}
 			}			
		}	
	}
	
	
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				normRemoteCost[sender][receiver][product]=	rtc[sender][receiver][product]/rtcMaxTotalFixo*costImportance+rtt[sender][receiver][product]/rttMaxTotalFixo*timeImportance;
				TotalRemoteStationCostExcel.add(sender,receiver, product, rtc[sender][receiver][product], rtt[sender][receiver][product], normRemoteCost[sender][receiver][product]);
			}
		}			
	}
	
	for(var supplier in S){
		for(var receiver in R){
			for(var product in P){
				normSupplierCost[supplier][receiver][product]=stc[supplier][receiver][product]/stcMaxTotalFixo*costImportance+stt[supplier][receiver][product]/sttMaxTotalFixo*timeImportance;
				TotalSupplierCostExcel.add(supplier,receiver, product, stc[supplier][receiver][product], stt[supplier][receiver][product],normSupplierCost[supplier][receiver][product]);		
			}			
		}	
	}
}


// ############################################# CALC Quantidade OTIMA ESTOQUE ########################################

//LT
float QtdLTCalc[R][P]; // quantidade de pedidos da part p para a remote r
float LTCalc[R][P];// tot leading time in sender
float AvgLTCalc[R][P]; //media de tempo total de sender para a part
float StDesvAuxLTCalc[R][P];
float StDesvLTCalc[R][P];//desvio padrao de sender para a part

execute{
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				//se houver demanda do produto no recebedor, entao há um pedido
				if(DemandaCalc[receiver][product]>0){
					QtdLTCalc[sender][product]+=1;	
					LTCalc[sender][product]+=rtt[sender][receiver][product];
				}			
 			}			
		}	
	}
	
	for(var sender in R){
		for(var product in P){
			if(QtdLTCalc[sender][product]!=0){
				AvgLTCalc[sender][product]=LTCalc[sender][product]/QtdLTCalc[sender][product];	
			}else{
				AvgLTCalc[sender][product]=0;
			}			
		}	
	}
	
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				StDesvAuxLTCalc[sender][product]+=Opl.pow((rtt[sender][receiver][product]-AvgLTCalc[sender][product]),2);
   			}								
		}	
	}

	for(var sender in R){
		for(var product in P){
			if(QtdLTCalc[sender][product]!=0){
				StDesvLTCalc[sender][product]=Opl.sqrt(StDesvAuxLTCalc[sender][product]/QtdLTCalc[sender][product]);	
			}else{
				StDesvLTCalc[sender][product]=0;
			}			
		}	
	}
}


//Demand
int D[R][P] = DemandaCalc;//para os 3 anos

//Standard deviation in review time
//Stdev
float stdev[R][P]=StDesvDemandaCalc;//para os 3 anos

//AvgDemand
float avgdemand[R][P]=AvgDemandaCalc;


//stdesvLT
float stdesvLT[R][P]=StDesvLTCalc;

//AvgLT
float avglt[R][P]=AvgLTCalc;

//pre-calculate Qwr quantidade economica
//filtrando receiver/product que tenha demanda nula portanto Qw nulo
tuple rrpType
{
	string sender;//remote
	string receiver;//remote
	string product;//part
}

tuple srpType
{
	string supplier;//remote
	string receiver;//remote
	string product;//part
}

tuple rpType
{
	string receiver;//remote
	string product;//part
}

{rrpType} RRP;
{srpType} SRP;

{rpType} RPWithDemand;
{rrpType} RRPWithDemand;
int HasDemand[R][P];
execute{

	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				RRP.add(sender, receiver, product);
 			}			
		}	
	}
	
	for(var supplier in S){
		for(var receiver in R){
			for(var product in P){
				SRP.add(supplier, receiver, product);				
 			}			
		}	
	}
	
	for(var receiver in R){
		for(var product in P){
			if(D[receiver][product]!=0){
				RPWithDemand.add(receiver, product);	
				HasDemand[receiver][product]=1;	
			}		
		}			
	}	
	
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				if(D[receiver][product]!=0){
					RRPWithDemand.add(sender, receiver, product);
				}			
 			}			
		}	
	}
	
}

float Qw[R][R][P];
float Qws[S][R][P];

//filtro de Qw zero
{rrpType} Qw_nonZero;
{srpType} Qws_nonZero;
execute
{

	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				Qw[sender][receiver][product] = Opl.sqrt((2*uc[sender][product] * D[receiver][product] 			+ 	dcUber[sender][receiver] * QtdDemandaOrdensCalc[receiver][product])/(cc*uc[sender][product]));
				if(Qw[sender][receiver][product]!=0){
					Qw_nonZero.add(sender, receiver, product);
   				}									
 			}			
		}	
	}
	
	for(var supplier in S){
		for(var receiver in R){
			for(var product in P){
				Qws[supplier][receiver][product] = Opl.sqrt((2*price[supplier][product] * D[receiver][product]		+	sdc[supplier][receiver] * QtdDemandaOrdensCalc[receiver][product])/(cc*price[supplier][product]));
				if(Qws[supplier][receiver][product]!=0){
					Qws_nonZero.add(supplier, receiver, product);
   				}
 			}			
		}	
	}
	
}



//Variables
dvar boolean 	x[R];//binario se existira a remote
dvar boolean 	y[R][R][P];//binario de rota de remote a remote
dvar boolean	z[S][R][P];//binario de rota de fornecedor para remote
dvar int+ 		n[R];//numero de impressoras
//dvar int+		estoque[R][P];//quantidade de pecas em estoque por remote
//dvar boolean 	aux[R][P][percentage];//binario para determinar porcentagem de sla


//Objective
minimize 
  			//custo de uma fabrica e impressoras
  				sum( r in R ) x[r] * (FC_R + n[r]*FC_P)
    		
    		//custo de fabricacao
    		+	sum(sender in R, receiver in R, product in P) y[sender][receiver][product] * (		uc[sender][product] * D[receiver][product] 			+ 	dcUber[sender][receiver] * QtdDemandaOrdensCalc[receiver][product] + ioc[sender][product]*QtdDemandaOrdensCalc[receiver][product])
			
			//custo de fornecimento externo
    		+	sum(supplier in S, receiver in R, product in P) z[supplier][receiver][product] * (	price[supplier][product] * D[receiver][product]		+	sdc[supplier][receiver] * QtdDemandaOrdensCalc[receiver][product] )
    		
    		//estoque
    		//+	sum(sender in R, receiver in R, product in P) (dc[sender][receiver] * (D[receiver][product]/estoque[sender][product]) + cc*uc[sender][product]*estoque[sender][product]/2)
    		
    		
    		//+	sum(rrp in Qw_nonZero) y[rrp.sender][rrp.receiver][rrp.product] * (dc[rrp.sender][rrp.receiver]*D[rrp.receiver][rrp.product])/(Qw[rrp.sender][rrp.receiver][rrp.product] + cc * uc[rrp.sender][rrp.product]*(Qw[rrp.sender][rrp.product][rrp.receiver]/2) * (1-(D[rrp.receiver][rrp.product]/cp[rrp.sender][rrp.product])))// *n[rrp.sender]
    		//+	sum(spr in Qws_nonZero) z[spr.supplier][spr.receiver][spr.product] * ((sdc[spr.supplier][spr.receiver] * D[spr.receiver][spr.product])/(Qws[spr.supplier][spr.product][spr.receiver]+ cc * price[spr.supplier][spr.product]* (Qws[spr.supplier][spr.receiver][spr.product]/2)))
    		//+	sum( sender in R, product in P, percent in percentage )( ((aux[sender][product][percent] * zscore[percent] *  sqrt(avglt[sender][product]) * stdev[sender][product])
    			//													  + (aux[sender][product][percent] * zscore[percent] *  stdesvLT[sender][product] * avgdemand[sender][product]))* cc * uc[product] 
    				//												  )
			//+ sum(rpr in Qw_nonZero, percent in percentage) ((100-percent) *D[rpr.receiver][rpr.product]*sc)/Qw[rpr.sender][rpr.product][rpr.receiver]    																  
    		;
    		
    		//TODO pos processamento com z e y sugerindo os qw em estoque para cada produto

//Constraints
subject to 
{
	
	//p-median =7
	//sum(sender in R)  x[sender] ==7; 	
	
	//restringe o maximo de impressoras em uma remote (ou o problema não é convexo)
	forall(sender in R){
		n[sender] <= max_P;
	}
	
	//cada rota deve demorar no maximo X horas de viagem
	forall(sender, receiver in R, product in P){
		y[sender][receiver][product]*rtt[sender][receiver][product] <= max_H;// horas
	}
	
	//cada rota deve demorar no maximo X horas de viagem
	forall(supplier in S, receiver in R, product in P){
		z[supplier][receiver][product]*stt[supplier][receiver][product] <= max_H;// dias em horas
	}
	
	//so existe impressora se existir a respectiva remote station as facility
	forall(sender in R){
 		n[sender] <=  x[sender]*max_P;
 	}
	
	//so existe link se existir a remote station as facility
 	forall(sender, receiver in R, product in P){
 		y[sender][receiver][product] <=  x[sender];
 	}
 	
 	//###### ALTERACAO JOAO ####
 	//se existir a remote as facility entao alguma das remote as receiver ira receber o produto
 	//###### DE: ####
 	//forall(sender, receiver in R, product in P){
 	//	sum(receiver in R) y[sender][receiver][product] >=  x[sender];
 	//}
 	
 	//###### PARA: ####
 	forall(sender in R){
 		sum(receiver in R, product in P) y[sender][receiver][product] >=  x[sender];
 	}
 	
 	//so existe rota se houver demanda
 	forall(sender, receiver in R, product in P){
 		y[sender][receiver][product] <=  HasDemand[receiver][product];
 	}
 	
 	forall(supplier in S, receiver in R, product in P){
 		z[supplier][receiver][product] <=  HasDemand[receiver][product];
 	}
 	
 	//tem que atender a demanda 
 	forall( receiver in R, product in P){
 		sum(sender in R)(y[sender][receiver][product])+sum(supplier in S)(z[supplier][receiver][product]) >=  HasDemand[receiver][product];
 	}
 	
 	
	
	//###### ALTERACAO JOAO ####
	//restringe capacidade
	//###### DE: ####
 	//forall(sender in R, product in P)
 	//{
 	//	sum(receiver in R) y[sender][receiver][product]*D[receiver][product] <= n[sender] * C_P/pt[sender][product];
 	//}
 	//###### PARA: ####
 	forall(sender in R)
 	{
 		sum(receiver in R, product in P) y[sender][receiver][product]*D[receiver][product]*pt[sender][product] <= n[sender] * C_P*0.5;//0.5 eh fator para evitar que a maquina fique muito ocupada, e quando chega um novo pedido jah tem muitas coisas no buffer
 	}
 
 //se houver impressora deve ser produtor	
 	forall(sender in R){
 	 	n[sender]>=x[sender];
 	}
 	
 	
 	//somente um sender para cada demanda
 	//forall(receiver in R, product in P)
 	//{
 	//	sum(sender in R)(y[sender][receiver][product])== 1;
 	//}
 	
 	
 	//somente um sender para cada demanda
 	forall(rp in RPWithDemand)
 	{
 		sum(sender in R)(y[sender][rp.receiver][rp.product])+ sum(supplier in S)(z[supplier][rp.receiver][rp.product])== 1;
 	}
 	
	//restringe fornecimento ou producao
 	//forall(receiver in R, product in P)
 	//{
 	//	sum(sender in R)(y[sender][receiver][product]) + sum(supplier in S)(z[supplier][receiver][product]) == 1;
 	//}
 	
 	//forall(rp in RPWithDemand)
 	//{
 	//	sum(sender in R)(y[sender][rp.receiver][rp.product]) + sum(supplier in S)(z[supplier][rp.receiver][rp.product]) == 1;
 	//}
 	
 	//restringe a escolha de um e apenas um valor de sla para cada combinacao sender product
 	//forall(sender in R,product in P){
 	//	sum(percent in percentage) aux[sender][product][percent] <= 1;//igual a 1
 	//}
 	
}



tuple RotasType
{
	string fromRemote;
	string toRemote;
	string part;
	int demanda;
	float costFromRemote;
	float leadingTime;
}

{RotasType} RotasExcel;

tuple InternalSupplyType
{
	string fromRemote;
	string toRemote;
	string part;
}

{InternalSupplyType} InternalSupplyExcel;

tuple ExternalSupplyType
{
	string fromSupplier;
	string toRemote;
	string part;
}

{ExternalSupplyType} ExternalSupplyExcel;


tuple FornecimentoType
{
	string fromSupplier;
	string toRemote;
	string part;
	int demanda;
	float costFromSupplier;
	float leadingTime;
}

{FornecimentoType} FornecimentoExcel;

tuple ProductionType
{
	string fromRemote;
	string part;
}

{ProductionType} ProductionExcel;

execute{
		
		for(var sender in R){
			for(var receiver in R){
				for(var product in P){
					if(y[sender][receiver][product]==1){
						RotasExcel.add(sender,receiver,product, D[receiver][product], rtc[sender][receiver][product],rtt[sender][receiver][product]);
						InternalSupplyExcel.add(sender,receiver,product);
						ProductionExcel.add(sender,product);					
					}			
 				}
			}	
		}
		
		
		for(var supplier in S){
			for(var receiver in R){
				for(var product in P){
					if(z[supplier][receiver][product]==1){
						FornecimentoExcel.add(supplier,receiver,product, D[receiver][product], stc[supplier][receiver][product],stt[supplier][receiver][product]);					
						ExternalSupplyExcel.add(supplier,receiver,product);	
					}			
 				}
			}	
		}
}

tuple PrintersAllocatedType
{
	string Remote;
	int qtd;
}

{PrintersAllocatedType} PrintersAllocatedExcel;
execute{
	for(var i in n){
		if(n[i]>=1){
			PrintersAllocatedExcel.add(i,n[i]);
		}	
	}
}


//montando as matrizes de adjacencia por produto
/*execute
{

	for(var product in P){
		
		writeln(" Matriz de adjacencia para o produto: " + product);
		
		for(var sender in R){
			for(var receiver in R){
				write(y[sender][receiver][product]+ "\t ");			
 			}
 			writeln("");			
		}	
	}
	
	
	writeln(" \n\n\n\n\n\n FORNECIMENTO \n\n\n\n\n\n");
	
	
	//montando custo total de fornecimento
	for(var product in P){
		writeln(" Matriz de adjacencia para o produto: " + product);	
	
		for(var supplier in S){
			for(var receiver in R){
				write(z[supplier][receiver][product]+ ", ");				
 			}			
			writeln("");
		}
	}
} 	*/



//############################### SIMULACAO ##############################################

//indexacao das InitStatistics
tuple InitStatisticsType
{
	key string PartType;
	int PartID;
	string TSPartSL;
	string InventoryLevel;
	string StatStatInvPart;
	string TSProducePart;
	string TSProducePartLT;
	string TSIntPartCost;
	string TSSupPartCost;
	string StatStatIntCostAux;
	string StatStatIntPartCost;
	string StatStatIntCostSupplierAux;
	string StatStatIntPartSupplierCost;
	string StatStatSupCostAux;
	string StatStatSupPartCost;	
};

{InitStatisticsType} InitStatisticsExcel;
tuple letra {key int id; string caractere;};
{letra} letras = {	<0,"A">,<1,"B">,<2,"C">,<3,"C">,<4,"E">,<5,"F">,<6,"G">,<7,"H">,<8,"I">,<9,"J">,<10,"K">,<11,"L">,<12,"M">,
					<13,"N">,<14,"O">,<15,"P">,<16,"Q">,<17,"R">,<18,"S">,<19,"T">,<20,"U">,<21,"V">,<22,"W">,<23,"X">,<24,"Y">,<25,"Z">};
execute{
	var ID=0;
	for(var product in P){
		InitStatisticsExcel.add(
			product,
			ID,
			"TSServiceLevelPart"+letras.get(ID).caractere,
			"StaInventory["+(ID+1)+"]",
			"StatStatInvPart"+letras.get(ID).caractere,
			"TSProdPart"+letras.get(ID).caractere,
			"TSProdPartLT"+letras.get(ID).caractere,
			"TSIntCostPart"+letras.get(ID).caractere,
			"TSSupCostPart"+letras.get(ID).caractere,
			"IntPartsCostAux["+(ID+1)+"]",
			"StatStatIntCostPart"+letras.get(ID).caractere,
			"IntPartsCostSupplierAux["+(ID+1)+"]",
			"StatStatIntCostSupplierPart"+letras.get(ID).caractere,
			"SupPartsCostAux["+(ID+1)+"]",
			"StatStatSupCostPart"+letras.get(ID).caractere
		);
		ID++;
	}
}

//indexacao das parts
tuple TablePartsType
{
	key string PartType;
	int ID;
};

{TablePartsType} TablePartsExcel;

execute{
	var ID=0;
	for(var product in P){
		TablePartsExcel.add(product,ID);
		ID++;
	}
}

//indexacao dos fornecedores internos e externos
tuple TableSupplierType
{
	key string SupplierType;
	int ID;
};

{TableSupplierType} TableSupplierExcel;
{string} TableSupplierAux;

execute{
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				if(y[sender][receiver][product]==1){
					TableSupplierAux.add(sender);
				}			
			}
		}	
	}
	
	for(var supplier in S){
		for(var receiver in R){
			for(var product in P){
				if(z[supplier][receiver][product]==1){
					TableSupplierAux.add(supplier);										
				}			
			}
		}	
	}
	
	var ID=0;
	for(var tuple in TableSupplierAux){
		TableSupplierExcel.add(tuple,ID);
		ID++;
	}
}


//tabela de fornecimento seguindo modelo de simulacao
tuple SuppliersType
{
	string Station;
	string PartType;
	int PartID;
	string SupplierName;
	int SupplierID;
	string SupplierOrderInputNode;
};

{SuppliersType} SuppliersExcel;

execute{
		
		for(var sender in R){
			for(var receiver in R){
				for(var product in P){
					if(y[sender][receiver][product]==1){
						var SupplierOrderInputNode =""; 					
						if(sender==receiver){
							SupplierOrderInputNode = "Input@"+sender;
							for(var product in P){
								SuppliersExcel.add(receiver,product,TablePartsExcel.get(product).ID, sender, TableSupplierExcel.get(sender).ID, SupplierOrderInputNode);
      						}								
						}else{
							SupplierOrderInputNode ="OrderInput@"+sender;
							SuppliersExcel.add(receiver,product,TablePartsExcel.get(product).ID, sender, TableSupplierExcel.get(sender).ID, SupplierOrderInputNode);
						}
					}			
 				}
			}	
		}
}


//calculando media de estoque para iniciar as remotes
float Qwm[R][P];
float totQwm[R][P];
int qtdQwm[R][P];

execute{
		
		for(var sender in R){
			for(var receiver in R){
				for(var product in P){
					if(y[sender][receiver][product]==1){
						totQwm[sender][product]+=Qw[sender][receiver][product];
						qtdQwm[sender][product]+=1;				
					}			
 				}
			}	
		}

		for(var sender in R){
			for(var product in P){
				if(qtdQwm[sender][product]!=0){			
					Qwm[sender][product]=totQwm[sender][product]/qtdQwm[sender][product];
   				}					
			}
		}	

}


tuple InitStationsType
{
	string Stations;
	string PartType;
	int PartID;
	int InitialInventory;
	int ReOrderPt;
	int OrderUptoQty;
	int Produce;
	float ProductionTime;
	int ProductionCapacity;
	int ProdParallelQty;
	float UnitProdCost;
};

{InitStationsType} InitStationsExcel;

execute{

	for(var remote in R){
		for(var product in P){	
			if(x[remote]==1)
				InitStationsExcel.add(remote,product,TablePartsExcel.get(product).ID, Opl.round(Qwm[remote][product]), Opl.round(Qwm[remote][product]), Opl.round(Qwm[remote][product]), x[remote],(pt[remote][product]*60),n[remote],n[remote],uc[remote][product]);				
 		}			
	}
	
	for(var remote in R){
		for(var product in P){	
			if(x[remote]==0)
				InitStationsExcel.add(remote,product,TablePartsExcel.get(product).ID, 0,0,0, HasDemand[remote][product]-1,0,0,0,0);				
 		}			
	}
}

tuple RemoteStationsType{
	string Stations;
	int ReviewTime;
	float OpenCost;
}

{RemoteStationsType} RemoteStationsExcel;

execute{
	for(var remote in R){
		if(x[remote]==1)
			RemoteStationsExcel.add(remote, 7,FC_R);
	}
}

tuple StationProdCapType{
	string Station;
	int DayTime;
	int ProductionCapacity;
	int NumPrinters;
}

{StationProdCapType} StationProdCapExcel;
execute{
	for(var remote in R){
		StationProdCapExcel.add(remote, 0,n[remote],n[remote]);
	}
}

tuple TransportTimesType{
	key string PathName;
	int PathID;
	float TransportTime;
}

{TransportTimesType} TransportTimesExcel;

tuple TransportTimesTempType{
	string PathName;
	float TransportTime;
}

{TransportTimesTempType} TransportTimesTemp;
execute{
		for(var sender in R){
			for(var receiver in R){
				for(var product in P){
					if(y[sender][receiver][product]==1){
						if(sender!=receiver){
						var pathName = 	"TP"+sender+"to"+receiver;
						TransportTimesTemp.add(pathName,dt[sender][receiver]);					
						}					
					}			
 				}
			}	
		}
		
		for(var linha in RemoteStationsFiliaisExcel){
			for(var tupleTime in TimeExcel){
				if(tupleTime.CidadeOrigem==linha.remote && tupleTime.CidadeDestino==linha.filial){
					if(linha.remote!=linha.filial){				
						var pathNameClient = 	"TP"+linha.remote+"toC"+linha.filial;		
						TransportTimesTemp.add(pathNameClient,tupleTime.Duration/(60*60));
    				} else {
    					for(var product in P){    				
	    					if(y[linha.remote][linha.filial][product]==0){    				
	    						var pathNameClient = 	"TP"+linha.remote+"toSnk"+RPascalCase.get(linha.filial).PascalCase;    				
	    						TransportTimesTemp.add(pathNameClient,0.000000001);
	       					}
          				}       					    					
    				}				
	  			}
   			}
		}
		
		var cont=0;
		for(var temp in TransportTimesTemp){
			TransportTimesExcel.add(temp.PathName,cont,temp.TransportTime);
			cont++;
		}
}

tuple TransfCostsType{
 	string Stations;
	int PartID;
	float ClientsTransfCost;
	float IntTransfCost;
	float SupTransfCost;
}

{TransfCostsType} TransfCostsExcel;

float totCostRemotePart[R][P];
int qtdCostRemotePart[R][P];//?

execute{
	for(var sender in R){
		for(var receiver in R){
			for(var product in P){
				if(y[sender][receiver][product]==1){
					totCostRemotePart[sender][product]+=rtc[sender][receiver][product];
					qtdCostRemotePart[sender][product]+=1;
				}			
			}
		}	
	}
	
	for(var sender in R){
		for(var product in P){
			if(qtdCostRemotePart[sender][product]>0){
				TransfCostsExcel.add(sender, TablePartsExcel.get(product).ID,(totCostRemotePart[sender][product]/qtdCostRemotePart[sender][product]),0,0);
			}		
		}
	}		
}

tuple SomeCostType{
	float PrintersCost;
	float StockoutCost;
}

{SomeCostType} SomeCostExcel;
execute{
	SomeCostExcel.add(FC_P,sc);
}

tuple InvHoldingCostType{
	float InvHoldingCost;
}

{InvHoldingCostType} InvHoldingCostExcel;
execute{
	InvHoldingCostExcel.add(cc);
}

