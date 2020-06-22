package com.fasten.wp4.optimizator.tactical.cplex.versions;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasten.wp4.database.model.Route;
import com.fasten.wp4.database.model.SRAMsAllocated;
import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.optimizator.tactical.model.Constraint;
import com.fasten.wp4.optimizator.tactical.model.CplexResult;
import com.fasten.wp4.optimizator.tactical.model.Status;
import com.fasten.wp4.optimizator.tactical.model.Variable;
import com.fasten.wp4.optimizator.tactical.util.PrettyPrintMatrix;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


public class PMedianPartsTskMatrix {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private TacticalOptimization tacticalOptimization;

	private int[] distributionCenters;
	private String[] distributionCentersNames;
	private int[] remoteStations;
	private String[] remoteStationsNames;
	private int[] parts;
	private String[] partsNames;

	private double[] processingTime;
	private int[][] numberOfOrder;	//quantity by customer
	private int[][] demandByPart;	//by part
	private int[][] matrix;

	public PMedianPartsTskMatrix(TacticalOptimization tacticalOptimization, int[] distributionCenters,
			String[] distributionCentersNames, int[] remoteStations, String[] remoteStationsNames, int[] parts,
			String[] partsNames, double[] processingTime, int[][] numberOfOrder, int[][] demandByPart, int[][] matrix) {
		super();
		this.tacticalOptimization = tacticalOptimization;
		this.distributionCenters = distributionCenters;
		this.distributionCentersNames = distributionCentersNames;
		this.remoteStations = remoteStations;
		this.remoteStationsNames = remoteStationsNames;
		this.parts = parts;
		this.partsNames = partsNames;
		this.processingTime = processingTime;
		this.numberOfOrder = numberOfOrder;
		this.demandByPart = demandByPart;
		this.matrix = matrix;
	}

	public Entry<CplexResult, TacticalOptimizationResult> costBenefit(){

		//primeira solucao
		Map.Entry<CplexResult,TacticalOptimizationResult> costBenefit = null;
		Double maxResult = null;
		Integer firstSolutionP=null;
		for (int p = 1; p < remoteStations.length; p++) {
			Map.Entry<CplexResult,TacticalOptimizationResult> solution = solve(p);
			if(solution.getKey().getObjectiveStatus().equals(Status.Optimal)) {
				maxResult=solution.getKey().getObjectiveValue();
				firstSolutionP=p;
				break;
			}
		}

		//custo beneficio
		for (int p = firstSolutionP; p < remoteStations.length; p++) {
			Map.Entry<CplexResult,TacticalOptimizationResult> solution = solve(p);
			double pPercent = ((double)p/(remoteStations.length-1))*100;
			double objectivePercent = (solution.getKey().getObjectiveValue()/maxResult)*100;
			System.out.println(" percent p: "+pPercent+" percent obj: "+objectivePercent);
			if(objectivePercent<pPercent) {
				costBenefit=solution;
				break;
			}
		}

		return costBenefit;
	}

	private Map.Entry<CplexResult,TacticalOptimizationResult> solve(int p) {

		int[] costumers = distributionCenters;
		String[] costumersNames = distributionCentersNames;
		int[] warehouses =  remoteStations;
		String[] warehousesNames =  remoteStationsNames;

		int n = costumers.length; //costumers
		int m = warehouses.length; //warehouses
		int k = parts.length;//parts

		int[][] time = matrix;



		double[][][] travelCost = new double [n][m][k];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				for (int s = 0; s < k; s++) {
					if(numberOfOrder[c][s]!=0) {
						travelCost[c][w][s] = numberOfOrder[c][s]*time[c][w];
					}
				}
			}
		}

		double[][][] processingCost = new double [n][m][k];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				for (int s = 0; s < k; s++) {
					if(demandByPart[c][s]!=0) {
						processingCost[c][w][s] = processingTime[s]*demandByPart[c][s];
					}
				}
			}
		}

		double[][][] totalCost = new double [n][m][k];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				for (int s = 0; s < k; s++) {
					if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
						totalCost[c][w][s] = travelCost[c][w][s]+processingCost[c][w][s];
					}
				}
			}
		}

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("costumers			= \n"+PrettyPrintMatrix.printStr(costumers));
			LOGGER.debug("costumersNames	= \n"+PrettyPrintMatrix.printStr(costumersNames));
			LOGGER.debug("warehouses		= \n"+PrettyPrintMatrix.printStr(warehouses));
			LOGGER.debug("warehousesNames	= \n"+PrettyPrintMatrix.printStr(warehousesNames));
			LOGGER.debug("time				= \n"+PrettyPrintMatrix.printStr(time));
			LOGGER.debug("numberOfOrder		= \n"+PrettyPrintMatrix.printStr(numberOfOrder));
			LOGGER.debug("travelCost		= \n"+PrettyPrintMatrix.printStr(travelCost));
			LOGGER.debug("processingTime	= \n"+PrettyPrintMatrix.printStr(processingTime));
			LOGGER.debug("demandByPart		= \n"+PrettyPrintMatrix.printStr(demandByPart));
			LOGGER.debug("processingCost	= \n"+PrettyPrintMatrix.printStr(processingCost));
			LOGGER.debug("totalCost			= \n"+PrettyPrintMatrix.printStr(totalCost));
		}

		IloCplex cplex = null;

		try {
			// define new model
			cplex  = new IloCplex();
			//cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			//variables
			IloNumVar[] openWarehouse = new IloNumVar[m];
			for (int w = 0 ; w < m; w++) {
				openWarehouse[w] = cplex.boolVar("openWarehouse"+"_"+warehouses[w]);
			}

			IloNumVar[][][] shipToCustomer = new IloNumVar[n][m][k];
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							shipToCustomer[c][w][s] = cplex.boolVar("shipToCustomer"+"_"+costumers[c]+"_"+warehouses[w]+"_"+parts[s]);
						}
					}
				}
			}

			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							objective.addTerm(travelCost[c][w][s],shipToCustomer[c][w][s]);
							objective.addTerm(processingCost[c][w][s],shipToCustomer[c][w][s]);
						}
					}
				}
			}
			cplex.addMinimize(objective);

			// constraints
			//o somatorio de vetor parte dentro da coluna tem q ser igual a 1
			//			IloConstraint[][] shipConstraints = new IloConstraint[n][k];
			//			IloLinearNumExpr[][] shipConstraintExpression = new IloLinearNumExpr[n][k];
			//			for (int c=0; c<n; c++) {
			//				for (int s = 0; s < k; s++) {
			//					shipConstraintExpression[c][s] = cplex.linearNumExpr();
			//					for (int w=0; w<m; w++) {
			//						shipConstraintExpression[c][s].addTerm(1.0, shipToCustomer[c][w][s]);
			//					}
			//					IloRange shipConstraint = cplex.addEq(shipConstraintExpression[c][s], 1);
			//					shipConstraint.setName("shipConstraint"+"_"+costumers[c]+"_"+parts[s]);
			//					shipConstraints[c][s]=shipConstraint;
			//				}
			//			}

			// constraints
			//o somatorio da linha inteira tem q ser igual a 1
			//			IloConstraint[] shipConstraints = new IloConstraint[n];
			//			IloLinearNumExpr[] shipConstraintExpression = new IloLinearNumExpr[n];
			//			for (int c=0; c<n; c++) {
			//				shipConstraintExpression[c] = cplex.linearNumExpr();
			//				for (int s = 0; s < k; s++) {
			//					if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
			//						for (int w=0; w<m; w++) {
			//							shipConstraintExpression[c].addTerm(1.0, shipToCustomer[c][w][s]);
			//						}
			//					}
			//				}
			//				IloRange shipConstraint = cplex.addEq(shipConstraintExpression[c], 1);
			//				shipConstraint.setName("shipConstraint"+"_"+costumers[c]);
			//				shipConstraints[c]=shipConstraint;
			//			}

			// constraints
			//o somatorio de cada parte para cada customer igual a 1 por warehouse (ou seja <Sao Paulo, Botao>*W1 + <Sao Paulo, Botao>*W2 ... =1
			IloConstraint[][] shipConstraints = new IloConstraint[n][k];
			IloLinearNumExpr[][] shipConstraintExpression = new IloLinearNumExpr[n][k];
			for (int c=0; c<n; c++) {
				for (int s = 0; s < k; s++) {
					if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
						shipConstraintExpression[c][s] = cplex.linearNumExpr();
						for (int w=0; w<m; w++) {
							shipConstraintExpression[c][s].addTerm(1.0, shipToCustomer[c][w][s]);
						}
						IloRange shipConstraint = cplex.addEq(shipConstraintExpression[c][s], 1);
						shipConstraint.setName("shipConstraint"+"_"+costumers[c]+"_"+parts[s]);
						shipConstraints[c][s]=shipConstraint;
					}
				}
			}


			IloLinearNumExpr openConstraintExpression = cplex.linearNumExpr();
			for (int w=0; w<m; w++) {
				openConstraintExpression.addTerm(1.0, openWarehouse[w]);
			}
			IloRange openConstraint = cplex.addEq(openConstraintExpression, p);
			openConstraint.setName("openConstraint");

			IloConstraint[][][] shipOpenConstraints = new IloConstraint[n][m][k];
			IloLinearNumExpr[][][] shipOpenExpression = new IloLinearNumExpr[n][m][k];
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							shipOpenExpression[c][w][s] = cplex.linearNumExpr();
							shipOpenExpression[c][w][s].addTerm(1.0, shipToCustomer[c][w][s]);
							IloConstraint shipOpenConstraint = cplex.addLe(shipOpenExpression[c][w][s], openWarehouse[w]);
							shipOpenConstraint.setName("shipOpenConstraint"+"_"+costumers[c]+"_"+warehouses[w]+"_"+parts[s]);
							shipOpenConstraints[c][w][s]=shipOpenConstraint;
						}
					}
				}
			}


			// solve model
			if (cplex.solve()) {

				//variables
				if(LOGGER.isDebugEnabled()) {
					LOGGER.info("\nSolution status = "+ cplex.getStatus()+"\n");
					LOGGER.info("obj = "+cplex.getObjValue());

					String[] openWarehouseLog = new String[m];
					for (int w = 0 ; w < m; w++) {
						openWarehouseLog[w]=(cplex.getValue(openWarehouse[w])+"").replace("-","");
					}
					LOGGER.debug("openWarehouse = \n"+PrettyPrintMatrix.printStr(openWarehouseLog));

					String[][][] shipToCustomerLog = new String[n][m][k];
					for (int c = 0; c < n; c++) {
						for (int w = 0; w < m; w++) {
							for (int s = 0; s < k; s++) {
								if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
									shipToCustomerLog[c][w][s]=(cplex.getValue(shipToCustomer[c][w][s])+"").replace("-","");
								}else {
									shipToCustomerLog[c][w][s]="-";
								}
							}
						}
					}
					LOGGER.debug("shipToCustomer = \n"+PrettyPrintMatrix.printStr(shipToCustomerLog));
				}

				if(LOGGER.isTraceEnabled()) {
					//constraints
					String[][] shipConstraintsLog = new String[n][k];
					for (int c=0;c<n;c++) {
						for (int s = 0; s < k; s++) {
							if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
								shipConstraintsLog[c][s]=((IloRange) shipConstraints[c][s]).toString().replace("IloRange ","")+" -> slack = "+cplex.getSlack((IloRange) shipConstraints[c][s]);
							}else {
								shipConstraintsLog[c][s]="No shipConstraint"+"_"+costumers[c]+"_"+parts[s];
							}
						}
					}
					LOGGER.trace("shipToCustomer = \n"+PrettyPrintMatrix.printStr(shipConstraintsLog));

					String[] openConstraintLog = new String[]{((IloRange) openConstraint).toString().replace("IloRange ","")+" -> slack = "+cplex.getSlack((IloRange) openConstraint)};
					LOGGER.trace("openConstraint = \n"+PrettyPrintMatrix.printStr(openConstraintLog));


					String[][][] shipOpenConstraintsLog = new String[n][m][k];
					for (int c = 0; c < n; c++) {
						for (int w = 0; w < m; w++) {
							for (int s = 0; s < k; s++) {
								if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
									shipOpenConstraintsLog[c][w][s]=((IloRange) shipOpenConstraints[c][w][s]).toString().replace("IloRange ","")+" -> slack = "+cplex.getSlack((IloRange) shipOpenConstraints[c][w][s]);
								}else {
									shipOpenConstraintsLog[c][w][s]="No shipOpenConstraint"+"_"+costumers[c]+"_"+warehouses[w]+"_"+parts[s];
								}
							}
						}
					}
					LOGGER.trace("shipOpenConstraints = \n"+PrettyPrintMatrix.printStr(shipOpenConstraintsLog));
				}
				//criando resultados

				//generico
				CplexResult result = new CplexResult();

				//aplicado ao problema
				TacticalOptimizationResult tacticalOptimizationResult = new TacticalOptimizationResult();
				tacticalOptimizationResult.setStudy(tacticalOptimization);


				result.setObjectiveStatus(Status.valueOf(cplex.getStatus().toString()));
				result.setObjectiveValue(cplex.getObjValue());
				List<Variable> variables = new ArrayList<Variable>();
				List<SRAMsAllocated> printers = new ArrayList<SRAMsAllocated>();
				for (int w = 0 ; w < m; w++) {
					Variable var = new Variable();
					var.setName(openWarehouse[w].getName());
					Double value= cplex.getValue(openWarehouse[w]);
					var.setValue(value);
					variables.add(var);

					if(value.compareTo(1d)==0) {
						SRAMsAllocated printer = new SRAMsAllocated();
						printer.setRemoteStation(warehousesNames[w]);
						printer.setNumberOfSRAMs(1);
						printer.setTacticalOptimizationResult(tacticalOptimizationResult);
						printers.add(printer);
					}
					tacticalOptimizationResult.setPrinters(printers);
				}


				List<Route> routes = new ArrayList<Route>();
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						for (int s = 0; s < k; s++) {
							if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
								Variable var = new Variable();
								var.setName(shipToCustomer[c][w][s].getName());
								Double value = cplex.getValue(shipToCustomer[c][w][s]);
								var.setValue(value);
								variables.add(var);

								if(value.compareTo(1d)==0) {
									Route route = new Route();
									route.setDistributionCenter(costumersNames[c]);
									route.setRemoteStation(warehousesNames[w]);
									route.setTimeOfTravel(new Double(time[c][w]));
									route.setTimesTraveled(new Integer(numberOfOrder[c][s]));
									route.setTotalTravelTime(travelCost[c][w][s]);
									route.setTotalProcessingTime(processingCost[c][w][s]);
									route.setProcessingTime(processingTime[s]);
									route.setPart(partsNames[s]);
									route.setQuantityOfParts(demandByPart[c][s]);
									route.setTacticalOptimizationResult(tacticalOptimizationResult);
									routes.add(route);
								}
								tacticalOptimizationResult.setRoutes(routes);
							}
						}
					}
				}
				result.setVariables(variables);


				List<Constraint> constraints = new ArrayList<Constraint>();
				for (int c=0; c<n; c++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							Constraint constraint = new Constraint();
							constraint.setFunction(((IloRange) shipConstraints[c][s]).toString());
							//constraint.setDual(cplex.getDual((IloRange) shipConstraints[c][s]));
							constraint.setSlack(cplex.getSlack((IloRange) shipConstraints[c][s]));
							constraints.add(constraint);
						}
					}
				}


				Constraint openContraintResult = new Constraint();
				openContraintResult.setFunction((openConstraint).toString());
				//constraint.setDual(cplex.getDual((IloRange) openConstraint));
				openContraintResult.setSlack(cplex.getSlack((IloRange) openConstraint));
				constraints.add(openContraintResult);

				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						for (int s = 0; s < k; s++) {
							if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
								Constraint constraint = new Constraint();
								constraint.setFunction(((IloRange) shipOpenConstraints[c][w][s]).toString());
								//constraint.setDual(cplex.getDual((IloRange) shipOpenConstraints[c][w][s]));
								constraint.setSlack(cplex.getSlack((IloRange) shipOpenConstraints[c][w][s]));
								constraints.add(constraint);
							}
						}
					}
				}

				result.setConstraints(constraints);
				return new AbstractMap.SimpleEntry<CplexResult, TacticalOptimizationResult>(result,tacticalOptimizationResult);

			} else {
				LOGGER.info("Problem not solved");
				CplexResult unsolved = new CplexResult();
				unsolved.setObjectiveStatus(Status.valueOf(cplex.getStatus().toString()));
				return new AbstractMap.SimpleEntry<CplexResult, TacticalOptimizationResult>(unsolved,null);
			}

		}catch(IloException e) {
			CplexResult error = new CplexResult();
			error.setObjectiveStatus(Status.Error);
			return new AbstractMap.SimpleEntry<CplexResult, TacticalOptimizationResult>(error,null);
		} finally {
			if(cplex!=null) cplex.end();
		}


	}


}
