package com.fasten.wp4.optimizator.tactical.model;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloRange;

public class CplexResult {
		private Double objectiveValue;
		private Status objectiveStatus;
		private List<Constraint> constraints = new ArrayList<Constraint>();
		private List<Variable> variables = new ArrayList<Variable>();

		public Double getObjectiveValue() {
			return objectiveValue;
		}

		public void setObjectiveValue(Double objectiveValue) {
			this.objectiveValue = objectiveValue;
		}

		public Status getObjectiveStatus() {
			return objectiveStatus;
		}

		public void setObjectiveStatus(Status objectiveStatus) {
			this.objectiveStatus = objectiveStatus;
		}

		public List<Constraint> getConstraints() {
			return constraints;
		}

		public void setConstraints(List<Constraint> constraints) {
			this.constraints = constraints;
		}

		public List<Variable> getVariables() {
			return variables;
		}

		public void setVariables(List<Variable> variables) {
			this.variables = variables;
		}

}
