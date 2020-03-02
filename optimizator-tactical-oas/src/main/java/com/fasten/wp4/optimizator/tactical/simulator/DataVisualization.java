/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author narci
 */
public class DataVisualization {
    
    
    private ArrayList<Location> locationsaux = new ArrayList<>();
    DataTreatment dataTreatment = new DataTreatment ();
    
    public void getLocationsInfo(SupplyChain supplychain){
        if (supplychain.getLocations().isEmpty()){
            System.out.print("All orders were completed\n");
        }
        else{
            System.out.print("Some(s) Order(s) was/were not completed in:\n");
            locationsaux = supplychain.getLocations();
            
            for (Location lr: locationsaux){
                System.out.print( "Location: " + dataTreatment.getStringSpaced(lr.getLocation()) + " | Is Production Center? " + 
                      lr.getIsProductionCenter() + " | Number of orders unfinished: " + lr.getNumOrdersLeft() + "\n");
                
                System.out.print("Costs: | ");
                for (int i=1; i<10; i++){
                    System.out.print(lr.getCosts(i) + " | ");
                }
                System.out.print("\n");
                
                System.out.print("StateStatistics: " + lr.getStatStatWaitingNodeStation() + " | " + 
                                 lr.getStatStatJoinSameNumOrder() + " | " + lr.getStatStatProduction() + " | ");
                System.out.print("\n");
                
                System.out.print("TallyStatistics: " + lr.getTSPrintersTimeIdle() + " | " + lr.getTSNumSupplierOrders() + " | "
                                 + lr.getTSProductionTime() + " | " + lr.getTSEntInternalOrders() + " | " + lr.getTSEntSupplierOrders() + " | "
                                 + lr.getTSNumInternalOrders() + " | " + lr.getTSNumStockouts() + " | " + lr.getTSWaitingNodeStation() + " | "
                                 + lr.getTSJoinSameNumOrder() + " | " + lr.getTSServiceLevel() + " | ");
                System.out.print("\n");
                
            }
            System.out.print("TotalOpenStationCost " + supplychain.getTotalOpenStationCost() + "\n");
            System.out.print("TotalSupplyChainCost " + supplychain.getTotalSupplyChainCost() + "\n");            
        }
    }
}
