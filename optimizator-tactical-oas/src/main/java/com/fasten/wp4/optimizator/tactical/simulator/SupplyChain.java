/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;

import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author narci
 */
public class SupplyChain {
	private String supplychain_id = "TSK Brasil";
	private ArrayList<Location> locations = new ArrayList<>();
    private Double TotalOpenStationCost, TotalSupplyChainCost;
    
    public SupplyChain() {
    	
    }
    
    public <Locations>ArrayList getLocations(){
        return this.locations;
    }
    
    public void setLocations(ArrayList l){
        this.locations=l;
    }
    
    public double getTotalOpenStationCost(){
        return this.TotalOpenStationCost;
    }
    
    public void setTotalOpenStationCost( double tosc){
        this.TotalOpenStationCost = tosc;
    }
    
     public double getTotalSupplyChainCost(){
        return this.TotalSupplyChainCost;
    }
    
    public void setTotalSupplyChainCost( double tscc){
        this.TotalSupplyChainCost = tscc;
    }
}
