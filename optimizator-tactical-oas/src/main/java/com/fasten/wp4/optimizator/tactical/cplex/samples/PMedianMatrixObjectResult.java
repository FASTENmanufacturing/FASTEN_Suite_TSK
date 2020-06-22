package com.fasten.wp4.optimizator.tactical.cplex.samples;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


public class PMedianMatrixObjectResult {
	
	public void solveMe() {

		int p = 2;

		int n = 4; //costumers
		int m = 3; //warehouses
		String[] costumers = {"Albert", "Bob", "Chris", "Daniel"};
		String[] warehouses = {"Santa Clara", "San Jose", "Berkeley"};
		double[] demand = {100.0,	80.0,	80.0,	70.0};
		double[][] distance = {	{2.0,	10.0,	50.0},
								{2.0,	10.0,	52.0}, 
								{50.0,	60.0,	3.0},
								{40.0,	60.0,	1.0}};
		
		double[][]cost = new double [n][m];
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				cost[c][w] = demand[c]*distance[c][w];
			}
		}
		System.out.println("cost   = [");
		for (int c = 0; c < n; c++) {
			for (int w = 0; w < m; w++) {
				System.out.print(cost[c][w] + "\t");
			}
			System.out.print("\n\r");
		}
		System.out.println("]");
		
		IloCplex cplex = null;

		try {
			// define new model
			cplex  = new IloCplex();
			//cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			//variables
			IloNumVar[] openWarehouse = new IloNumVar[m];
			for (int w = 0 ; w < m; w++) {
				openWarehouse[w] = cplex.boolVar("openWarehouse"+"_"+w);
			}

			IloNumVar[][] shipToCustomer = new IloNumVar[n][m];
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					shipToCustomer[c][w] = cplex.boolVar("shipToCustomer"+"_"+c+"_"+w);
				}
			}
			
			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int c = 0; c < n; c++) {
				for (int w = 0; w < m; w++) {
					objective.addTerm(cost[c][w],shipToCustomer[c][w]);
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
				shipConstraint.setName("shipConstraint"+c);
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
					shipOpenConstraint.setName("shipOpenConstraint"+c+w);
					shipOpenConstraints[c][w]=shipOpenConstraint;
				}
			}

			// solve model
			if (cplex.solve()) {

				System.out.println("\nSolution status = "+ cplex.getStatus()+"\n");
				System.out.println("obj = "+cplex.getObjValue());

				System.out.println("openWarehouse   = [");
				for (int w = 0 ; w < m; w++) {
					System.out.print(cplex.getValue(openWarehouse[w]) + "\t");
				}
				System.out.println("]");
				
				
				System.out.println("shipToCustomer   = [");
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						System.out.print(cplex.getValue(shipToCustomer[c][w]) + "\t");
					}
					System.out.print("\n\r");
				}
				System.out.println("]");
				
				System.out.println("------ ship Constraints -----");
				for (int c=0;c<shipConstraints.length;c++) {
					System.out.print(((IloRange) shipConstraints[c])+" -> "+"\t");
					System.out.print("slack constraint "+(c+1)+" = "+cplex.getSlack((IloRange) shipConstraints[c])+"\n");
				}
				
				System.out.println("------ open Constraint -----");
				System.out.print(((IloRange) openConstraint)+" -> "+"\t");
				System.out.print("slack constraint  = "+cplex.getSlack((IloRange) openConstraint)+"\n");
				
				System.out.println("------ ship Open Constraints -----");
				for (int c = 0; c < n; c++) {
					for (int w = 0; w < m; w++) {
						System.out.print(((IloRange) shipOpenConstraints[c][w])+" -> "+"\t");
						System.out.print("slack constraint "+(c+1)+""+(w+1)+" = "+cplex.getSlack((IloRange) shipOpenConstraints[c][w])+"\n");
					}
				}
				
				
			} else {
				System.out.println("problem not solved");
			}

		} catch (IloException exc) {
			exc.printStackTrace();
		} finally {
			if(cplex!=null) cplex.end();
		}
		
	}
	
	
}
