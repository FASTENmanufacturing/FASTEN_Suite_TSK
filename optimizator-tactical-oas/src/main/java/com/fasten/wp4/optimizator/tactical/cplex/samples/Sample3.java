package com.fasten.wp4.optimizator.tactical.cplex.samples;

import com.fasten.wp4.optimizator.tactical.util.MathUtils;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class Sample3 {

	public void run(int n) {


		IloCplex cplex = null;

		try {

			//random data
			double[] xPos = new double[n];
			double[] yPos = new double[n];
			for (int i = 0; i < n; i ++){
				xPos[i] = Math.random()*100;
				yPos[i] = Math.random()*100;
			}

			double[][] c = new double[n][n];
			for (int i = 0 ; i < n; i++){
				for (int j = 0 ; j < n; j++) {
					c[i][j] = Math.sqrt(Math.pow(xPos[i]-xPos[j], 2)+ Math.pow(yPos[i]-yPos[j],2));
				}
			}

			//model

			cplex = new IloCplex();

			//variables
			IloNumVar[][] x = new IloNumVar[n][n];

			for(int i = 0; i < n; i++){
				for (int j = 0 ; j < n; j++) {
					x[i][j] = cplex.boolVar("x"+i+""+j);
				}
			}

			IloNumVar[] u = new IloNumVar[n];
			for(int i = 0; i < n; i++){
				u[i] = cplex.numVar(0, Double.MAX_VALUE, "u"+i);
			}

			//Objective
			IloLinearNumExpr obj = cplex.linearNumExpr();
			for(int i =0 ; i <n ; i++){
				for (int j = 0; j< n ;j++){
					if(j != i){
						obj.addTerm(c[i][j], x[i][j]);
					}
				}
			}
			cplex.addMinimize(obj);

			//constraints
			IloConstraint[] outfluxContraints = new IloConstraint[n];
			for(int j = 0; j < n; j++){
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for(int i = 0; i< n ; i++){
					if(i!=j){
						expr.addTerm(1.0, x[i][j]);
					}
				}
				IloRange outfluxContraint = cplex.addEq(expr, 1.0);
				outfluxContraints[j]=outfluxContraint;
			}

			IloConstraint[] influxContraints = new IloConstraint[n];
			for(int i = 0; i < n; i++){
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for(int j = 0; j< n ; j++){
					if(j!=i){
						expr.addTerm(1.0, x[i][j]);
					}
				}
				IloRange influxContraint = cplex.addEq(expr, 1.0);
				influxContraints[i]=influxContraint;
			}

			IloConstraint[][] subnetContraints = new IloConstraint[n][n];
			for(int i = 1; i < n; i++){
				for(int j = 1; j < n; j++){
					if(i != j){
						IloLinearNumExpr expr = cplex.linearNumExpr();
						expr.addTerm(1.0, u[i]);
						expr.addTerm(-1.0, u[j]);
						expr.addTerm(n-1, x[i][j]);
						IloRange subnetContraint = cplex.addLe(expr, n-2);
						subnetContraints[i][j]=subnetContraint;
					}
				}
			}

			//solve mode
			if(cplex.solve()){

				//solution
				System.out.println("\nSolution status = "+ cplex.getStatus()+"\n");
				System.out.println("obj = " + cplex.getObjValue());

				//variables
				System.out.println("x   = [");
				for(int i = 0; i<x.length; i++){
					for(int j = 0 ; j < x[i].length; j ++){
						if(i != j)
							System.out.print(cplex.getValue(x[i][j])+ "\t");
						else
							System.out.print("-\t");
					}
					System.out.print("\n\r");
				}
				System.out.println("]\n");

//				System.out.println("u   = [");
//				for(int i = 0; i < n; i++){
//					System.out.print(cplex.getValue(u[i])+ "\t");
//				}
//				System.out.println("]\n");
				
				//constraints (sensibility analysis)
				System.out.println("------ outflux Constraints -----");
				for(int j = 0; j < n; j++){
					System.out.print(((IloRange) outfluxContraints[j])+" -> "+"\t");
					System.out.print("slack constraint "+(j+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) outfluxContraints[j]),2)+"\n");
//					System.out.print("dual  constraint "+(j+1)+" = "+MathUtils.round(cplex.getDual((IloRange) outfluxContraints[j]),2)+"\n");
				}
				System.out.println("------ influx Constraints -----");
				for(int i = 0; i < n; i++){
					System.out.print(((IloRange) influxContraints[i])+" -> "+"\t");
					System.out.print("slack constraint "+(i+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) influxContraints[i]),2)+"\n");
//					System.out.print("dual  constraint "+(i+1)+" = "+MathUtils.round(cplex.getDual((IloRange) influxContraints[i]),2)+"\n");
				}
				System.out.println("------ subnet Constraints -----");
				for(int i = 1; i < n; i++){
					for(int j = 1; j < n; j++){
						if(i != j){
							System.out.print(((IloRange) subnetContraints[i][j])+" -> "+"\t");
							System.out.print("slack constraint "+(i+1)+" = "+MathUtils.round(cplex.getSlack((IloRange) subnetContraints[i][j]),2)+"\n");
//							System.out.print("dual  constraint "+(i+1)+" = "+MathUtils.round(cplex.getDual((IloRange) subnetContraints[i][j]),2)+"\n");
						}
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
