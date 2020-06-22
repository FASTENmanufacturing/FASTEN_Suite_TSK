package com.fasten.wp4.optimizator.tactical.cplex.samples;

import java.util.ArrayList;
import java.util.List;

import com.fasten.wp4.optimizator.tactical.model.Constraint;
import com.fasten.wp4.optimizator.tactical.model.CplexResult;
import com.fasten.wp4.optimizator.tactical.model.Status;
import com.fasten.wp4.optimizator.tactical.model.Variable;
import com.fasten.wp4.optimizator.tactical.util.MathUtils;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


public class Sample2 {

	public CplexResult run() {

		int n = 4; //cargos
		int m = 3; //compartments
		double[] p = {310.0, 380.0, 350.0, 285.0}; //profit
		double[] v = {480.0, 650.0, 580.0, 390.0}; //volume per ton of cargo
		double[] a = {18.0, 15.0, 23.0, 12.0}; //available weight
		double[] c = {10.0, 16.0, 8.0}; //capacity of compartment
		double[] V = {6800.0, 8700.0, 5300.0}; //volume capacity of 

		IloCplex cplex = null;

		try {
			// define new model
			cplex  = new IloCplex();
			//cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			// variables
			IloNumVar[][] x = new IloNumVar[n][m];
			for (int i=0; i<n; i++) {
				for (int j=0; j<m; j++) {
					x[i][j] = cplex.numVar(0, Double.MAX_VALUE, "x"+"_"+i+"_"+j);
				}
			}
			IloNumVar y = cplex.numVar(0,Double.MAX_VALUE, "y");

			//objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i=0; i<n; i++) {
				for (int j=0; j<m; j++) {
					objective.addTerm(p[i],x[i][j]);
				}
			}
			cplex.addMaximize(objective);

			// constraints
			IloConstraint[] avaiableWeightConstraints = new IloConstraint[n];
			IloLinearNumExpr[] avaiableWeight = new IloLinearNumExpr[n];
			for (int i=0; i<n; i++) {
				avaiableWeight[i] = cplex.linearNumExpr();
				for (int j=0; j<m; j++) {
					avaiableWeight[i].addTerm(1.0, x[i][j]);
				}
				IloRange avaiableWeightConstraint = cplex.addLe(avaiableWeight[i], a[i]);
				avaiableWeightConstraint.setName("avaiableWeightConstraint"+i);
				avaiableWeightConstraints[i]=avaiableWeightConstraint;
			}

			IloConstraint[] usedWeightCapacityConstraints = new IloConstraint[m];
			IloLinearNumExpr[] usedWeightCapacity = new IloLinearNumExpr[m];
			for (int j=0; j<m; j++) {
				usedWeightCapacity[j] = cplex.linearNumExpr();
				for (int i=0; i<n; i++) {
					usedWeightCapacity[j].addTerm(1.0, x[i][j]);
				}
				IloRange usedWeightCapacityConstraint = cplex.addLe(usedWeightCapacity[j], c[j]);
				usedWeightCapacityConstraint.setName("usedWeightCapacityConstraint"+j);
				usedWeightCapacityConstraints[j]=usedWeightCapacityConstraint;
			}

			IloConstraint[] usedVolumeCapacityConstraints = new IloConstraint[m];
			IloLinearNumExpr[] usedVolumeCapacity = new IloLinearNumExpr[m];
			for (int j=0; j<m; j++) {
				usedVolumeCapacity[j] = cplex.linearNumExpr();
				for (int i=0; i<n; i++) {
					usedVolumeCapacity[j].addTerm(v[i],x[i][j]);
				}
				IloRange usedVolumeCapacityConstraint = cplex.addLe(usedVolumeCapacity[j],V[j]);
				usedVolumeCapacityConstraint.setName("usedVolumeCapacityConstraint"+j);
				usedVolumeCapacityConstraints[j]=usedVolumeCapacityConstraint;
			}

			IloConstraint[] proportionBalanceConstraints = new IloConstraint[m];
			IloLinearNumExpr[] proportionBalance = new IloLinearNumExpr[m];
			for (int j=0; j<m; j++) {
				proportionBalance[j] = cplex.linearNumExpr();
				for (int i=0; i<n; i++) {
					proportionBalance[j].addTerm(1.0,x[i][j]);
				}
				IloNumExpr prod = cplex.prod(1/c[j], proportionBalance[j]);
				IloConstraint proportionBalanceConstraint = cplex.addEq(prod, y);
				proportionBalanceConstraint.setName("proportionBalanceConstraint"+j);
				proportionBalanceConstraints[j]=proportionBalanceConstraint;
			}


			// solve model
			if (cplex.solve()) {
				System.out.println("\nSolution status = "+ cplex.getStatus()+"\n");
				System.out.println("obj = "+cplex.getObjValue());
				System.out.println("x   = [");
				for (int i=0; i<n; i++) {
					for (int j=0; j<m; j++) {
						System.out.print(MathUtils.round(cplex.getValue(x[i][j]),2) + "\t");
					}
					System.out.print("\n\r");
				}
				System.out.println("y   = "+cplex.getValue(y));
				System.out.println("------ avaiable Weight Constraints -----");
				for (int i=0;i<avaiableWeightConstraints.length;i++) {
					System.out.print(((IloRange) avaiableWeightConstraints[i])+" -> "+"\t");
					System.out.print("slack constraint "+(i+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) avaiableWeightConstraints[i]),2)+"\t");
					System.out.print("dual  constraint "+(i+1)+" = "+MathUtils.round(cplex.getDual((IloRange) avaiableWeightConstraints[i]),2)+"\n");
				}
				System.out.println("------ used Weight Capacity Constraints -----");
				for (int j=0;j<usedWeightCapacityConstraints.length;j++) {
					System.out.print(((IloRange) usedWeightCapacityConstraints[j])+" -> "+"\t");
					System.out.print("slack constraint "+(j+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) usedWeightCapacityConstraints[j]),2)+"\t");
					System.out.print("dual  constraint "+(j+1)+" = "+MathUtils.round(cplex.getDual((IloRange) usedWeightCapacityConstraints[j]),2)+"\n");
				}
				System.out.println("------ used Volume Capacity Constraints -----");
				for (int j=0;j<usedVolumeCapacityConstraints.length;j++) {
					System.out.print(((IloRange) usedVolumeCapacityConstraints[j])+" -> "+"\t");
					System.out.print("slack constraint "+(j+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) usedVolumeCapacityConstraints[j]),2)+"\t");
					System.out.print("dual  constraint "+(j+1)+" = "+MathUtils.round(cplex.getDual((IloRange) usedVolumeCapacityConstraints[j]),2)+"\n");
				}
				System.out.println("------ proportion Balance Constraints -----");
				for (int j=0;j<proportionBalanceConstraints.length;j++) {
					System.out.print(((IloRange) proportionBalanceConstraints[j])+" -> "+"\t\t\t");
					System.out.print("slack constraint "+(j+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) proportionBalanceConstraints[j]),2)+"\t");	
					System.out.print("dual  constraint "+(j+1)+" = "+MathUtils.round(cplex.getDual((IloRange) proportionBalanceConstraints[j]),2)+"\n");
				}
				
				
				CplexResult result = new CplexResult();
				
				result.setObjectiveStatus(Status.valueOf(cplex.getStatus().toString()));
				result.setObjectiveValue(cplex.getObjValue());
				List<Variable> variables = new ArrayList<Variable>();
				for (int i=0; i<n; i++) {
					for (int j=0; j<m; j++) {
						Variable varX = new Variable();
						varX.setName(x[i][j].getName());
						varX.setValue(cplex.getValue(x[i][j]));
						variables.add(varX);
					}
				}
				Variable varY = new Variable();
				varY.setName(y.getName());
				varY.setValue(cplex.getValue(y));
				variables.add(varY);
				
				result.setVariables(variables);
				
				List<Constraint> constraints = new ArrayList<Constraint>();
				for (int i=0;i<avaiableWeightConstraints.length;i++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) avaiableWeightConstraints[i]).toString());
					constraint.setDual(cplex.getSlack((IloRange) avaiableWeightConstraints[i]));
					constraint.setSlack(cplex.getDual((IloRange) avaiableWeightConstraints[i]));
					constraints.add(constraint);
				}
				for (int j=0;j<usedWeightCapacityConstraints.length;j++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) usedWeightCapacityConstraints[j]).toString());
					constraint.setDual(cplex.getSlack((IloRange) usedWeightCapacityConstraints[j]));
					constraint.setSlack(cplex.getDual((IloRange) usedWeightCapacityConstraints[j]));
					constraints.add(constraint);
				}
				for (int j=0;j<usedVolumeCapacityConstraints.length;j++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) usedVolumeCapacityConstraints[j]).toString());
					constraint.setDual(cplex.getSlack((IloRange) usedVolumeCapacityConstraints[j]));
					constraint.setSlack(cplex.getDual((IloRange) usedVolumeCapacityConstraints[j]));
					constraints.add(constraint);
				}
				for (int j=0;j<proportionBalanceConstraints.length;j++) {
					Constraint constraint = new Constraint();
					constraint.setFunction(((IloRange) proportionBalanceConstraints[j]).toString());
					constraint.setDual(cplex.getSlack((IloRange) proportionBalanceConstraints[j]));
					constraint.setSlack(cplex.getDual((IloRange) proportionBalanceConstraints[j]));
					constraints.add(constraint);
				}
				result.setConstraints(constraints);
				
				return result;
				
			} else {
				System.out.println("problem not solved");
			}

		} catch (IloException exc) {
			exc.printStackTrace();
		} finally {
			if(cplex!=null) cplex.end();
		}
		
		CplexResult error = new CplexResult();
		error.setObjectiveStatus(Status.Error);
		return error;
	}
	
	
	
	

	
	
	
	
	
}
