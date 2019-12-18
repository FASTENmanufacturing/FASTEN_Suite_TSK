package com.github.adminfaces.starter.infra.async;

public enum AsyncCallStatus {
	Canceled{@Override public String toString() {return "Canceled";}},
	Ready{@Override public String toString() {return "Ready";}},
	Running{@Override public String toString() {return "Running";}},
	Executed{@Override public String toString() {return "Executed";}},
}
