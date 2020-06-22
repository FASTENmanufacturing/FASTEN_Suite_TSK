package com.fasten.wp4.optimizator.tactical.cplex.versions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


public class PMedianPartsPrintersSimpleTskMatrix {

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

	private int sramCapacity; 		//capacity of work of a SRAM in seconds/day
	private int horizon;			//the time to response for this demands request in days

	public PMedianPartsPrintersSimpleTskMatrix(TacticalOptimization tacticalOptimization, int[] distributionCenters,
			String[] distributionCentersNames, int[] remoteStations, String[] remoteStationsNames, int[] parts,
			String[] partsNames, double[] processingTime, int[][] numberOfOrder, int[][] demandByPart, int[][] matrix,
			int sramCapacity) {
		super();
		this.tacticalOptimization = tacticalOptimization;			//the tactical Optimization study configured
		this.distributionCenters = distributionCenters;				//the clients ids
		this.distributionCentersNames = distributionCentersNames;	//the clients names
		this.remoteStations = remoteStations;						//the warehouses ids
		this.remoteStationsNames = remoteStationsNames;				//the warehouses names
		this.parts = parts;											//the spare parts ids
		this.partsNames = partsNames;								//the spare parts names
		this.processingTime = processingTime;						//the average time of processing a part in seconds
		this.numberOfOrder = numberOfOrder;							//the number of orders by client and part <SAO PAULO, Button, 2>, means that SAO PAULO asked for button twice
		this.demandByPart = demandByPart;							//the quantity of parts by client and part <SAO PAULO, Button, 14>, means that SAO PAULO asked 14 buttons
		this.matrix = matrix;										//the matrix of travel times in seconds from a client to a warehouse
		this.sramCapacity=sramCapacity;								//the capacity of work for the SRAM in seconds by day
		//horizon as original study configuration
		this.horizon = Math.toIntExact(ChronoUnit.DAYS.between(Instant.ofEpochMilli(tacticalOptimization.getInitialDate().getTime()), Instant.ofEpochMilli(tacticalOptimization.getEndDate().getTime())));
	}

	public PMedianPartsPrintersSimpleTskMatrix(TacticalOptimization tacticalOptimization, int[] distributionCenters,
			String[] distributionCentersNames, int[] remoteStations, String[] remoteStationsNames, int[] parts,
			String[] partsNames, double[] processingTime, int[][] numberOfOrder, int[][] demandByPart, int[][] matrix,
			int sramCapacity, int horizon) {
		super();
		this.tacticalOptimization = tacticalOptimization;			//the tactical Optimization study configured
		this.distributionCenters = distributionCenters;				//the clients ids
		this.distributionCentersNames = distributionCentersNames;	//the clients names
		this.remoteStations = remoteStations;						//the warehouses ids
		this.remoteStationsNames = remoteStationsNames;				//the warehouses names
		this.parts = parts;											//the spare parts ids
		this.partsNames = partsNames;								//the spare parts names
		this.processingTime = processingTime;						//the average time of processing a part in seconds
		this.numberOfOrder = numberOfOrder;							//the number of orders by client and part <SAO PAULO, Button, 2>, means that SAO PAULO asked for button twice
		this.demandByPart = demandByPart;							//the quantity of parts by client and part <SAO PAULO, Button, 14>, means that SAO PAULO asked 14 buttons
		this.matrix = matrix;										//the matrix of travel times in seconds from a client to a warehouse
		this.sramCapacity=sramCapacity;								//the capacity of work for the SRAM in seconds by day
		this.horizon = horizon;										//the horizon to accomplish demands range
	}

	//the best cost benefit varying p from one to one on each warehouse candidates
	public HashMap<String, Object> costBenefit(){

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		//first solution avaiable
		HashMap<String, Object> costBenefit = new HashMap<String,Object>();
		Double maxResult = null;		//the worst optimal solution will occur at the first feasible solution
		Integer firstSolutionP=null;	//the fist feasible P

		//vary p to get first solution
		for (int p = 1; p < remoteStations.length; p++) {
			HashMap<String,Object> solution = solve(p);
			CplexResult result = (CplexResult) solution.get("cplexResult");
			if(result.getObjectiveStatus().equals(Status.Optimal)) {
				maxResult=result.getObjectiveValue();
				firstSolutionP=p+1;
				break;
			}
		}

		//vary p from first solution to all range of warehouses candidates
		for (int p = firstSolutionP; p < remoteStations.length; p++) {
			HashMap<String,Object> solution = solve(p);
			CplexResult result = (CplexResult) solution.get("cplexResult");
			double pPercent = ((double)p/(remoteStations.length-1))*100;
			double objectivePercent = (result.getObjectiveValue()/maxResult)*100;
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug(" percent p: "+df.format(pPercent)+"% percent obj: "+df.format(objectivePercent)+"%");
			}
			if(objectivePercent<pPercent) {
				costBenefit=solution;
				break;
			}
		}

		return costBenefit;
	}

	/**
	 * Calculates the Cplex result and convert it into two readable objects one with Cplex Sensitive analysis (CplexResult) 
	 * and other applied to the problem (TacticalOptimizationResult)
	 * 
	 * Observations: 
	 * 1) All variables is calculated where there are demand so numberOfOrder and demandByPart must be different of zero 
	 * or the optimizer will always chose the route where zero is set
	 * 
	 * 2) The SRAMs are double becouse they must not influence in the travel costs and if they are int the processing part would be negative
	 * 
	 * @param p - number of warehouses
	 * @return the combination of CplexResult and TacticalOptimizationResults
	 */
	@SuppressWarnings("serial")
	public HashMap<String, Object> solve(int p) {

		//name as original pmedian sample
		int[] costumers = distributionCenters;
		String[] costumersNames = distributionCentersNames;
		int[] warehouses =  remoteStations;
		String[] warehousesNames =  remoteStationsNames;

		int n = costumers.length; 	//costumers
		int m = warehouses.length; 	//warehouses
		int k = parts.length;		//parts

		int[][] time = matrix;		//changed original name distance to time because our logic is to optimize time
		
		int totalTimeToExecuteOrders = sramCapacity * horizon; //the total time to meet the demand analyzed


		//first part of objective function, minimized by the location of the warehouses
		double[][][] travelCost = new double [n][m][k];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				for (int s = 0; s < k; s++) {
					if(numberOfOrder[c][s]!=0) {
						//the cost of travel is affected by the quantity of travels to accomplish the number of orders by customer
						travelCost[c][w][s] = numberOfOrder[c][s]*time[c][w];		
					}
				}
			}
		}

		//the second part of objective function, minimized by the number of SRAMs allocated at each chosen location
		double[][][] processingCost = new double [n][m][k];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				for (int s = 0; s < k; s++) {
					if(demandByPart[c][s]!=0) {
						//the cost of processing is proportional to the quantity of parts
						processingCost[c][w][s] = processingTime[s]*demandByPart[c][s];
					}
				}
			}
		}
		
		//the total cost of this Supply Chain used only for log
		//because the processing cost became zero when the optimizer allocate SRAMs 
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

		//the maximum of SRAMs is the total SRAMs requested to process all parts in the demand range (at horizon time)
		double maxSrams = 0.0;
		for (int c = 0; c < n; c++) {
			double sumByCustomerPart =0.0;
			for (int s = 0; s < k; s++) {
				if(demandByPart[c][s]!=0) {
					//accumulate the time of processing the part times quantity of parts by each customer
					sumByCustomerPart += processingTime[s]*demandByPart[c][s];
				}
			}
			//accumulate each proportion of SRAM by customer
			maxSrams+=sumByCustomerPart/totalTimeToExecuteOrders;
		}

		//the proportion of SRAM requested by customer and part to be meet
		double[][] srams = new double [n][k];
		for (int c = 0; c < n; c++) {
			for (int s = 0; s < k; s++) {
				if(demandByPart[c][s]!=0) {
					srams[c][s]=(processingTime[s]*demandByPart[c][s])/totalTimeToExecuteOrders;
				}
			}
		}

		//login variables and derived variables 
		if(LOGGER.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("p				= "+p+"\n");
			sb.append("sramCapacity		= "+sramCapacity+"\n");
			sb.append("horizon			= "+horizon+"\n");
			sb.append("totalTimeToExecuteOrders			= "+totalTimeToExecuteOrders+"\n");
			sb.append("maxSrams			= "+maxSrams+"\n");
			sb.append("costumers		= \n"+PrettyPrintMatrix.printStr(costumers)				+"\n");
			sb.append("costumersNames	= \n"+PrettyPrintMatrix.printStr(costumersNames)		+"\n");
			sb.append("warehouses		= \n"+PrettyPrintMatrix.printStr(warehouses)			+"\n");
			sb.append("warehousesNames	= \n"+PrettyPrintMatrix.printStr(warehousesNames)		+"\n");
			sb.append("parts			= \n"+PrettyPrintMatrix.printStr(parts)					+"\n");
			sb.append("partsNames		= \n"+PrettyPrintMatrix.printStr(partsNames)			+"\n");
			sb.append("processingTime	= \n"+PrettyPrintMatrix.printStr(processingTime)		+"\n");
			sb.append("numberOfOrder	= \n"+PrettyPrintMatrix.printStr(numberOfOrder)			+"\n");
			sb.append("demandByPart		= \n"+PrettyPrintMatrix.printStr(demandByPart)			+"\n");
			sb.append("matrix|time(s)	= \n"+PrettyPrintMatrix.printStr(time)					+"\n");
			sb.append("travelCost		= \n"+PrettyPrintMatrix.printStr(travelCost)			+"\n");
			sb.append("processingCost	= \n"+PrettyPrintMatrix.printStr(processingCost)		+"\n");
			sb.append("totalCost		= \n"+PrettyPrintMatrix.printStr(totalCost)				+"\n");
			sb.append("srams			= \n"+PrettyPrintMatrix.printStr(srams)					+"\n");
			LOGGER.debug(sb.toString());
		}

		IloCplex cplex = null;

		try {
			// define new model
			cplex  = new IloCplex();
			//cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			//variables
			//openWarehouse[w], boolean that flags where to put the warehouse 
			IloNumVar[] openWarehouse = new IloNumVar[m];
			for (int w = 0 ; w < m; w++) {
				openWarehouse[w] = cplex.boolVar("openWarehouse"+"_"+warehouses[w]);
			}

			//shipToCustomer[c][w][s], boolean that flags who(w) route the spare part(s) to which customer(c)
			//since the p-median attach customers to warehouses the logic is reverse, which customer(c) is attached to the warehouse (w) to receive part (s) 
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

			//sramsInWarehouse[w], double that indicates the minimum SRAMs that must be allocated to the warehouse to meet demand
			IloNumVar[] sramsInWarehouse = new IloNumVar[m];
			for (int w = 0 ; w < m; w++) {
				sramsInWarehouse[w] = cplex.numVar(0,Integer.MAX_VALUE, "nSram"+"_"+warehouses[w]);
			}

			
			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							objective.addTerm(travelCost[c][w][s],shipToCustomer[c][w][s]);
						}
					}
				}
			}
			cplex.addMinimize(objective);

			// constraints
			//------------------------ shipConstraint ------------------------
			//the sum of each part for each customer must be equal to 1 per warehouse (ie <Sao Paulo, Botao> * W1 + <Sao Paulo, Botao> * W2 ... = 1
			//only one warehouse sent that spare part for that customer (transportation x allocation problem)
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

			//------------------------ openConstraint ------------------------
			//the sum of each warehouse boolean must be equal to p
			IloLinearNumExpr openConstraintExpression = cplex.linearNumExpr();
			for (int w=0; w<m; w++) {
				openConstraintExpression.addTerm(1.0, openWarehouse[w]);
			}
			IloRange openConstraint = cplex.addEq(openConstraintExpression, p);
			openConstraint.setName("openConstraint");

			//------------------------ shipOpenConstraint ------------------------
			//only allow ship from open warehouses
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

			//------------------------ capacityConstraints ------------------------
			//the amount of SRAMs allocated must be greater than or equal to the location's need
			IloConstraint[] capacityConstraints = new IloConstraint[m];
			IloLinearNumExpr[] capacityConstraintsExpression = new IloLinearNumExpr[m];
			for (int w=0; w<m; w++) {
				capacityConstraintsExpression[w] = cplex.linearNumExpr();
				capacityConstraintsExpression[w].addTerm(1.0, sramsInWarehouse[w]);
				for (int c = 0; c < n; c++) {
					for (int s = 0; s < k; s++) {
						if( (numberOfOrder[c][s]!=0) && (demandByPart[c][s]!=0) ) {
							capacityConstraintsExpression[w].addTerm(-1.0*srams[c][s], shipToCustomer[c][w][s]);
						}
					}
				}
				IloRange capacityConstraint = cplex.addEq(capacityConstraintsExpression[w], 0.0);
				capacityConstraint.setName("maxSramsConstraint"+"_"+warehouses[w]);
				capacityConstraints[w]=capacityConstraint;
			}

			// solve model
			if (cplex.solve()) {
				

				//variables
				if(LOGGER.isInfoEnabled()) {

					StringBuilder sb = new StringBuilder();
					sb.append("\n"+"Solution status	= "+cplex.getStatus()+"\n");
					sb.append("obj 				= "+cplex.getObjValue() );
					LOGGER.info(sb.toString());
				}
				
				if(LOGGER.isDebugEnabled()) {
					
					String[] openWarehouseLog = new String[m];
					for (int w = 0 ; w < m; w++) {
						openWarehouseLog[w]=(cplex.getValue(openWarehouse[w])+"").replace("-","");
					}
					

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

					String[] sramsInWarehouseLog = new String[m];
					for (int w = 0 ; w < m; w++) {
						sramsInWarehouseLog[w]=(cplex.getValue(sramsInWarehouse[w])+"").replace("-","");
					}
					
					StringBuilder sb = new StringBuilder();
					sb.append("openWarehouse 	= \n"+PrettyPrintMatrix.printStr(openWarehouseLog)		+"\n");
					sb.append("shipToCustomer	= \n"+PrettyPrintMatrix.printStr(shipToCustomerLog)		+"\n");
					sb.append("sramsInWarehouse	= \n"+PrettyPrintMatrix.printStr(sramsInWarehouseLog)	+"\n");
					LOGGER.debug(sb.toString());
				}

				//for sensibility analysis
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
					
					String[] capacityConstraintsLog = new String[m];
					for (int w=0; w<m; w++) {
						capacityConstraintsLog[w]=((IloRange) capacityConstraints[w]).toString().replace("IloRange ","")+" -> slack = "+cplex.getSlack((IloRange) capacityConstraints[w]);
					}
					LOGGER.trace("capacityConstraints = \n"+PrettyPrintMatrix.printStr(capacityConstraintsLog));
					
				}

				
				//Creating results into objects

				//Generic
				CplexResult result = new CplexResult();

				//Applied to the problem
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
						printer.setNumberOfSRAMs(new BigDecimal(cplex.getValue(sramsInWarehouse[w])).setScale(0,RoundingMode.CEILING).intValue());
						printer.setTacticalOptimizationResult(tacticalOptimizationResult);
						printers.add(printer);
					}
					tacticalOptimizationResult.setPrinters(printers);
				}
				
				for (int w = 0 ; w < m; w++) {
					Variable var = new Variable();
					var.setName(sramsInWarehouse[w].getName());
					Double value= cplex.getValue(sramsInWarehouse[w]);
					var.setValue(value);
					variables.add(var);
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


				//no dual value for MIPs
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
				
				for (int w = 0; w < m; w++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) capacityConstraints[w]).toString());
					constraint.setSlack(cplex.getSlack((IloRange) capacityConstraints[w]));
					constraints.add(constraint);
				}
				
				result.setConstraints(constraints);
				
				return new HashMap<String,Object>(){{
					put("cplexResult",result);
					put("tacticalOptimizationResult",tacticalOptimizationResult);
				}};
				
			} else {
				LOGGER.info("Problem not solved");
				CplexResult unsolved = new CplexResult();
				unsolved.setObjectiveStatus(Status.valueOf(cplex.getStatus().toString()));
				return new HashMap<String,Object>(){{
					put("cplexResult",unsolved);
					put("tacticalOptimizationResult",null);
				}};
			}

		}catch(IloException e) {
			CplexResult error = new CplexResult();
			error.setObjectiveStatus(Status.Error);
			return new HashMap<String,Object>(){{
				put("cplexResult",error);
				put("tacticalOptimizationResult",null);
			}};
		} finally {
			if(cplex!=null) cplex.end();
		}


	}


}
