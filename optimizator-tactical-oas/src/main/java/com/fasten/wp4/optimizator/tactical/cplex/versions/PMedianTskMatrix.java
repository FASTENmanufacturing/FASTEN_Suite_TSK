package com.fasten.wp4.optimizator.tactical.cplex.versions;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
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

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


public class PMedianTskMatrix {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private TacticalOptimization tacticalOptimization;
	private int[] distributionCenters;
	private String[] distributionCentersNames;
	private int[] remoteStations;
	private String[] remoteStationsNames;
	private int[] demandOrders;
	private int[][] matrix;
	
	public PMedianTskMatrix(TacticalOptimization tacticalOptimization, int[] distributionCenters,
			String[] distributionCentersNames, int[] remoteStations, String[] remoteStationsNames, int[] demandOrders,
			int[][] matrix) {
		super();
		this.tacticalOptimization = tacticalOptimization;
		this.distributionCenters = distributionCenters;
		this.distributionCentersNames = distributionCentersNames;
		this.remoteStations = remoteStations;
		this.remoteStationsNames = remoteStationsNames;
		this.demandOrders = demandOrders;
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
		String[] costumersName = distributionCentersNames;
		int[] warehouses =  remoteStations;
		String[] warehousesName =  remoteStationsNames;
		
		int n = costumers.length; //costumers
		int m = warehouses.length; //warehouses

		int[] demand =  demandOrders;
		int[][] time = matrix;
		
		double[][]cost = new double [n][m];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				cost[c][w] = demand[c]*time[c][w];
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("cost   = [\n");
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				sb.append(cost[c][w] + "\t");
			}
			sb.append("\n\r");
		}
		sb.append("]");
		LOGGER.debug(sb.toString());

		
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

			IloNumVar[][] shipToCustomer = new IloNumVar[n][m];
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					shipToCustomer[c][w] = cplex.boolVar("shipToCustomer"+"_"+costumers[c]+"_"+warehouses[w]);
				}
			}
			
			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					objective.addTerm(demand[c]*time[c][w],shipToCustomer[c][w]);
				}
			}
			cplex.addMinimize(objective);

			// constraints
			IloConstraint[] shipConstraints = new IloConstraint[n];
			IloLinearNumExpr[] shipConstraintExpression = new IloLinearNumExpr[n];
			for (int c=0; c<n; c++) {
				shipConstraintExpression[c] = cplex.linearNumExpr();
				for (int w=0; w<m; w++) {
					shipConstraintExpression[c].addTerm(1.0, shipToCustomer[c][w]);
				}
				IloRange shipConstraint = cplex.addEq(shipConstraintExpression[c], 1);
				shipConstraint.setName("shipConstraint"+"_"+costumers[c]);
				shipConstraints[c]=shipConstraint;
			}
			
			IloLinearNumExpr openConstraintExpression = cplex.linearNumExpr();
			for (int w=0; w<m; w++) {
				openConstraintExpression.addTerm(1.0, openWarehouse[w]);
			}
			IloRange openConstraint = cplex.addEq(openConstraintExpression, p);
			openConstraint.setName("openConstraint");
			
			IloConstraint[][] shipOpenConstraints = new IloConstraint[n][m];
			IloLinearNumExpr[][] shipOpenExpression = new IloLinearNumExpr[n][m];
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					shipOpenExpression[c][w] = cplex.linearNumExpr();
					shipOpenExpression[c][w].addTerm(1.0, shipToCustomer[c][w]);
					IloConstraint shipOpenConstraint = cplex.addLe(shipOpenExpression[c][w], openWarehouse[w]);
					shipOpenConstraint.setName("shipOpenConstraint"+"_"+costumers[c]+"_"+warehouses[w]);
					shipOpenConstraints[c][w]=shipOpenConstraint;
				}
			}

			// solve model
			if (cplex.solve()) {

				LOGGER.info("\nSolution status = "+ cplex.getStatus()+"\n");
				LOGGER.info("obj = "+cplex.getObjValue());

				sb.setLength(0);
				sb.append("openWarehouse   = [\n");
				for (int w = 0 ; w < m; w++) {
					sb.append((cplex.getValue(openWarehouse[w]) + "\t").replace("-",""));
				}
				sb.append("]");
				LOGGER.info(sb.toString());
				
				sb.setLength(0);
				sb.append("shipToCustomer   = [\n");
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						sb.append((cplex.getValue(shipToCustomer[c][w]) + "\t").replace("-",""));
					}
					sb.append("\n\r");
				}
				sb.append("]");
				LOGGER.info(sb.toString());
				
				sb.setLength(0);
				LOGGER.debug("------ ship Constraints -----");
				for (int c=0;c<n;c++) {
					sb.append(((IloRange) shipConstraints[c])+" -> "+"\t");
					sb.append("slack = "+cplex.getSlack((IloRange) shipConstraints[c])+"\n");
				}
				LOGGER.debug(sb.toString());
				
				sb.setLength(0);
				LOGGER.debug("------ open Constraint -----");
				sb.append(((IloRange) openConstraint)+" -> "+"\t");
				sb.append("slack = "+cplex.getSlack((IloRange) openConstraint)+"\n");
				LOGGER.debug(sb.toString());
				
				sb.setLength(0);
				LOGGER.debug("------ ship Open Constraints -----");
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						sb.append(((IloRange) shipOpenConstraints[c][w])+" -> "+"\t");
						sb.append("slack = "+cplex.getSlack((IloRange) shipOpenConstraints[c][w])+"\n");
					}
				}
				LOGGER.debug(sb.toString());
				
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
						printer.setRemoteStation(warehousesName[w]);
						printer.setNumberOfSRAMs(1);
						printer.setTacticalOptimizationResult(tacticalOptimizationResult);
						printers.add(printer);
					}
					tacticalOptimizationResult.setPrinters(printers);
					
				}
				
				
				List<Route> routes = new ArrayList<Route>();
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						Variable var = new Variable();
						var.setName(shipToCustomer[c][w].getName());
						Double value = cplex.getValue(shipToCustomer[c][w]);
						var.setValue(value);
						variables.add(var);
						
						if(value.compareTo(1d)==0) {
							Route route = new Route();
							route.setDistributionCenter(costumersName[c]);
							route.setRemoteStation(warehousesName[w]);
							route.setTacticalOptimizationResult(tacticalOptimizationResult);
							route.setTimeOfTravel(new Double(time[c][w]));
							route.setPart(" - ");
							routes.add(route);
						}
						tacticalOptimizationResult.setRoutes(routes);
						
					}
				}
				result.setVariables(variables);
				

				List<Constraint> constraints = new ArrayList<Constraint>();
				for (int c=0; c<n; c++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) shipConstraints[c]).toString());
					//constraint.setDual(cplex.getDual((IloRange) shipConstraints[c]));
					constraint.setSlack(cplex.getSlack((IloRange) shipConstraints[c]));
					constraints.add(constraint);
				}
				
				Constraint openContraintResult = new Constraint();
				openContraintResult.setFunction((openConstraint).toString());
				//constraint.setDual(cplex.getDual((IloRange) openConstraint));
				openContraintResult.setSlack(cplex.getSlack((IloRange) openConstraint));
				constraints.add(openContraintResult);
				
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						Constraint constraint = new Constraint();
						constraint.setFunction(((IloRange) shipOpenConstraints[c][w]).toString());
						//constraint.setDual(cplex.getDual((IloRange) shipOpenConstraints[c][w]));
						constraint.setSlack(cplex.getSlack((IloRange) shipOpenConstraints[c][w]));
						constraints.add(constraint);
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
