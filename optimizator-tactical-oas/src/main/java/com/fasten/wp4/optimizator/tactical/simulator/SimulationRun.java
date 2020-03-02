package com.fasten.wp4.optimizator.tactical.simulator;

import java.io.File;
import java.io.IOException;

public class SimulationRun {

	public SimulationRun() {
		
	}
	
	public void RunSimulationModel() throws IOException, InterruptedException {
		
		//Run simulation model experiment in background
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File("SimulationModel")); //set execution diretory to get dll for Simio
        pb.command("SimulationModel\\RunExperiments.exe"); //set exe to run
        Process p = pb.start(); //run
        
        p.waitFor(); //wait for run to end
        System.out.println("Simulation finished\n");
        
        /*//Process p = new ProcessBuilder("C:\\Users\\narci\\source\\repos\\SimulationBlackBox\\bin\\debug\\RunExperiments.exe").start();
        //Process p = Runtime.getRuntime().exec("C:\\Users\\narci\\source\\repos\\SimulationBlackBox\\bin\\debug\\RunExperiments.exe");
        
        //Run simulation experiment in background
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File("C:\\Users\\narci\\source\\repos\\SimulationBlackBox\\bin\\debug")); //set execution diretory to get dll for Simio
        pb.command("C:\\Users\\narci\\source\\repos\\SimulationBlackBox\\bin\\debug\\RunExperiments.exe"); //set exe to run
        Process p = pb.start(); //run
        
        p.waitFor(); //wait for run to end
        System.out.println("Simulation finished\n");*/
	}
}
