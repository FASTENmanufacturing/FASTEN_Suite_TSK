/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;

/**
 *
 * @author narci
 */
public class ServiceLevelOverTime {
    public double time, serviceLevelValue;
    
    public void setServiceLevelOverTime(double t, double v){
        this.time = t;
        this.serviceLevelValue = v;
    }
    
    
    /*public ServiceLevelOverTime(double t, double v){
        this.time = t;
        this.serviceLevelValue = v;
    }*/
}
