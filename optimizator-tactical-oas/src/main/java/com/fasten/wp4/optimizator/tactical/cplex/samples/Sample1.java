package com.fasten.wp4.optimizator.tactical.cplex.samples;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;


public class Sample1 {

	public void run() {


		try {
			IloCplex cplex = new IloCplex();

			// variables
			IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");

			// expressions
			IloLinearNumExpr objective = cplex.linearNumExpr();
			objective.addTerm(0.12, x);
			objective.addTerm(0.15, y);

			// define objective
			cplex.addMinimize(objective);

			// define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();

			//			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x),cplex.prod(60, y)), 300));
			IloLinearNumExpr exp1 = cplex.linearNumExpr();
			exp1.addTerm(60, x);
			exp1.addTerm(60,y);
			IloRange c1 = cplex.addGe(exp1, 300);
			constraints.add(c1);

			//			constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x),cplex.prod(6, y)), 36));
			IloLinearNumExpr exp2 = cplex.linearNumExpr();
			exp2.addTerm(12, x);
			exp2.addTerm(6,y);
			IloRange c2 = cplex.addGe(exp2, 36);
			constraints.add(c2);

			//			constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x),cplex.prod(30, y)), 90));
			IloLinearNumExpr exp3 = cplex.linearNumExpr();
			exp3.addTerm(10, x);
			exp3.addTerm(30,y);
			IloRange c3 = cplex.addGe(exp3, 90);
			constraints.add(c3);

			IloLinearNumExpr exp4 = cplex.linearNumExpr();
			exp4.addTerm(2, x);
			exp4.addTerm(-1,y);
			IloRange c4 = cplex.addEq(exp4, 0);
			constraints.add(c4);

			IloLinearNumExpr exp5 = cplex.linearNumExpr();
			exp5.addTerm(1,y);
			exp5.addTerm(-1,x);
			IloRange c5 = cplex.addLe(exp5,8);
			constraints.add(c5);

			// display option
			cplex.setParam(IloCplex.Param.Simplex.Display, 0);

			// solve
			if (cplex.solve()) {
				System.out.println("obj = "+cplex.getObjValue());
				System.out.println("x   = "+cplex.getValue(x));
				System.out.println("y   = "+cplex.getValue(y));
				for (int i=0;i<constraints.size();i++) {
					System.out.println("dual constraint "+(i+1)+"  = "+cplex.getDual(constraints.get(i)));
					System.out.println("slack constraint "+(i+1)+" = "+cplex.getSlack(constraints.get(i)));
				}
			}
			else {
				System.out.println("Model not solved");
			}

			cplex.end();

		}
		catch (IloException exc) {
			exc.printStackTrace();
		}

	}

}