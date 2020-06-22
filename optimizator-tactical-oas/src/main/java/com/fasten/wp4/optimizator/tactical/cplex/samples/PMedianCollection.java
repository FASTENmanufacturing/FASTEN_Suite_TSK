package com.fasten.wp4.optimizator.tactical.cplex.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasten.wp4.optimizator.tactical.model.Constraint;
import com.fasten.wp4.optimizator.tactical.model.CplexResult;
import com.fasten.wp4.optimizator.tactical.model.Status;
import com.fasten.wp4.optimizator.tactical.model.Variable;

import ilog.concert.IloConstraint;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


@SuppressWarnings("serial")
public class PMedianCollection {
	
	private HashMap<String, String> tuple(String c, String w){
		return new HashMap<String, String>(){{ put(c,w);}};
	}
	
	public CplexResult run() {

		int p = 2;

		List<String> costumers = Arrays.asList("Albert", "Bob", "Chris", "Daniel");
		List<String> warehouses = Arrays.asList("Santa Clara", "San Jose", "Berkeley");

		HashMap<String, Double> demand = new HashMap<String, Double>(){{
			put(costumers.get(0), 	100d);
			put(costumers.get(1), 	80d);
			put(costumers.get(2), 	80d);
			put(costumers.get(3), 	70d);
		}};

		HashMap<HashMap<String,String>,Double> distance = new HashMap<HashMap<String,String>,Double>(){{
			put(tuple(costumers.get(0),warehouses.get(0)),	2d);
			put(tuple(costumers.get(0),warehouses.get(1)),	10d);
			put(tuple(costumers.get(0),warehouses.get(2)),	50d);
			put(tuple(costumers.get(1),warehouses.get(0)),	2d);
			put(tuple(costumers.get(1),warehouses.get(1)),	10d);
			put(tuple(costumers.get(1),warehouses.get(2)),	52d);
			put(tuple(costumers.get(2),warehouses.get(0)),	50d);
			put(tuple(costumers.get(2),warehouses.get(1)),	600d);
			put(tuple(costumers.get(2),warehouses.get(2)),	3d);
			put(tuple(costumers.get(3),warehouses.get(0)),	40d);
			put(tuple(costumers.get(3),warehouses.get(1)),	60d);
			put(tuple(costumers.get(3),warehouses.get(2)),	1d);

		}};

		HashMap<HashMap<String,String>,Double> cost = new HashMap<HashMap<String,String>,Double>();
		costumers.forEach(c -> {
			warehouses.forEach(w-> {
				cost.put(tuple(c,w),	demand.get(c) * distance.get(tuple(c,w)));
			});
		});

			
		IloCplex cplex=new IloCplex();
		try {
		
			// define new model
			//cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			//variables
			HashMap<String,IloNumVar> openWarehouse = new HashMap<String,IloNumVar>();
			warehouses.forEach(w-> {
					openWarehouse.put(w,cplex.boolVar("openWarehouse"+"_"+w));
			});

			HashMap<HashMap<String,String>,IloNumVar> shipToCustomer = new HashMap<HashMap<String,String>,IloNumVar>();
			costumers.forEach(c -> {
				warehouses.forEach(w-> {
						shipToCustomer.put(tuple(c,w),	cplex.boolVar("shipToCustomer"+"_"+c+"_"+w));
				});
			});


			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			costumers.forEach(c -> {
				warehouses.forEach(w-> {
						objective.addTerm(/*demand.get(c) * distance.get(tuple(c,w))*/cost.get(tuple(c,w)),shipToCustomer.get(tuple(c,w)));
				});
			});
			cplex.addMinimize(objective);

			// constraints
			HashMap<String, IloConstraint> shipConstraints = new HashMap<String, IloConstraint>();
			HashMap<String, IloLinearNumExpr> shipConstraintExpression = new HashMap<String, IloLinearNumExpr>();
			costumers.forEach(c -> {
					shipConstraintExpression.put(c, cplex.linearNumExpr());
					warehouses.forEach(w-> {
							shipConstraintExpression.get(c).addTerm(1.0, shipToCustomer.get(tuple(c,w)));
					});
					IloRange shipConstraint = cplex.addEq(shipConstraintExpression.get(c), 1);
					shipConstraint.setName("shipConstraint"+"_"+c);
					shipConstraints.put(c,shipConstraint);
			});

			IloLinearNumExpr openConstraintExpression = cplex.linearNumExpr();
			warehouses.forEach(w-> {
					openConstraintExpression.addTerm(1.0, openWarehouse.get(w));
			});
			IloRange openConstraint = cplex.addEq(openConstraintExpression, p);
			openConstraint.setName("openConstraint");

			HashMap<HashMap<String,String>,IloConstraint> shipOpenConstraints = new HashMap<HashMap<String,String>,IloConstraint>();
			HashMap<HashMap<String,String>,IloLinearNumExpr> shipOpenExpression = new HashMap<HashMap<String,String>,IloLinearNumExpr>();
			costumers.forEach(c -> {
				warehouses.forEach(w-> {
						shipOpenExpression.put(tuple(c,w),cplex.linearNumExpr());
						shipOpenExpression.get(tuple(c,w)).addTerm(1.0, shipToCustomer.get(tuple(c,w)));
						IloConstraint shipOpenConstraint = cplex.addLe(shipOpenExpression.get(tuple(c,w)), openWarehouse.get(w));
						shipOpenConstraint.setName("shipOpenConstraint_"+c+"_"+w);
						shipOpenConstraints.put(tuple(c,w),shipOpenConstraint);
				});
			});

			// solve model
			if (cplex.solve()) {

				System.out.println("\nSolution status = "+ cplex.getStatus()+"\n");
				System.out.println("obj = "+cplex.getObjValue());

				System.out.println("openWarehouse   = [");
				warehouses.forEach(w-> {
						System.out.print(cplex.getValue(openWarehouse.get(w)) + "\t");
				});
				System.out.println("]");


				System.out.println("shipToCustomer   = [");
				costumers.forEach(c -> {
					warehouses.forEach(w-> {
							System.out.print(cplex.getValue(shipToCustomer.get(tuple(c,w))) + "\t");
					});
					System.out.print("\n\r");
				});
				System.out.println("]");

				System.out.println("------ ship Constraints -----");
				shipConstraints.forEach((c,shipConstraint) ->{
					System.out.print(((IloRange) shipConstraint)+" -> "+"\t");
						System.out.print("slack constraint "+c+" = "+cplex.getSlack((IloRange) shipConstraint)+"\n");
					//					System.out.print("dual  constraint "+c+" = "+innerCplex.getDual((IloRange) shipConstraint)+"\n");
				});

				System.out.println("------ open Constraint -----");
				System.out.print(((IloRange) openConstraint)+" -> "+"\t");
				System.out.print("slack constraint  = "+cplex.getSlack((IloRange) openConstraint)+"\n");
				//				System.out.print("dual  constraint  = "+innerCplex.getDual((IloRange) openConstraint)+"\n");

				System.out.println("------ ship Open Constraints -----");
				shipOpenConstraints.forEach((cw,shipOpenConstraint)->{
					cw.forEach((c,w) ->{
						System.out.print(((IloRange) shipOpenConstraint)+" -> "+"\t");
							System.out.print("slack constraint "+c+"_"+w+" = "+cplex.getSlack((IloRange) shipOpenConstraint)+"\n");
//							System.out.print("dual  constraint "+c+"_"+w+" = "+innerCplex.getDual((IloRange) shipOpenConstraint)+"\n");
					});
				});

				CplexResult result = new CplexResult();

				result.setObjectiveStatus(Status.valueOf(cplex.getStatus().toString()));
				result.setObjectiveValue(cplex.getObjValue());
				List<Variable> variables = new ArrayList<Variable>();
				warehouses.forEach(w-> {
					Variable var = new Variable();
					var.setName(openWarehouse.get(w).getName());
					var.setValue(cplex.getValue(openWarehouse.get(w)));
					variables.add(var);
				});
				costumers.forEach(c -> {
					warehouses.forEach(w-> {
						Variable var = new Variable();
						var.setName(shipToCustomer.get(tuple(c,w)).getName());
						var.setValue(cplex.getValue(shipToCustomer.get(tuple(c,w))));
						variables.add(var);
					});
				});

				result.setVariables(variables);

				List<Constraint> constraints = new ArrayList<Constraint>();
				costumers.forEach(c -> {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) shipConstraints.get(c)).toString());
					constraint.setSlack(cplex.getSlack((IloRange) shipConstraints.get(c)));
					constraints.add(constraint);
				});
				shipConstraints.forEach((c,shipConstraint) ->{
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) shipConstraint).toString());
					constraint.setSlack(cplex.getSlack((IloRange) shipConstraint));
					constraints.add(constraint);
				});

				Constraint constr = new Constraint();
				constr.setFunction((openConstraint).toString());
				constr.setSlack(cplex.getSlack((IloRange) openConstraint));
				constraints.add(constr);

				shipOpenConstraints.forEach((cw,shipOpenConstraint)->{
					cw.forEach((c,w) ->{
						Constraint constraint = new Constraint();
						constraint.setFunction(((IloRange) shipOpenConstraint).toString());
						//constraint.setDual(innerCplex.getDual((IloRange) shipOpenConstraint));
						constraint.setSlack(cplex.getSlack((IloRange) shipOpenConstraint));
						constraints.add(constraint);
					});
				});

				result.setConstraints(constraints);

				return result;

			} else {
				System.out.println("problem not solved");
			}
			
		} finally {
			if(cplex!=null) cplex.end();
		}

		CplexResult error = new CplexResult();
		error.setObjectiveStatus(Status.Error);
		return error;
	}

}
