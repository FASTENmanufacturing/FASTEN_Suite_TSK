package com.fasten.wp4.optimizator.tactical.simulator;
import java.lang.String;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author narci
 */
public class Location {
    
    private String location;
    private String isProductionCenter = "False";
    private Orders orders = new Orders();
    private Costs costs = new Costs();
    private StateStatistics statestatistics = new StateStatistics();
    private TallyStatistics tallystatistics = new TallyStatistics();
    
    
    public void setLocation (String location){
        this.location = location;
    }
    
    public void setNumOrdersLeft (double numOrdersLeft){
        this.orders.numOrdersLeft = numOrdersLeft;
    }
    
    public void setCosts (int caseNum, Double cost){
        switch (caseNum) {
            case 1:  this.costs.StockoutCost = cost;
                     break;
            case 2:  this.costs.PrintersCost = cost;
                     if(cost>0){
                         this.isProductionCenter = "True";
                     }
                     break;
            case 3:  this.costs.SuppliersCost = cost;
                     break;
            case 4:  this.costs.InternalCost = cost;
                     break;
            case 5:  this.costs.ClientsTransCost = cost;
                     break;
            case 6:  this.costs.SupTransfCost = cost;
                     break;
            case 7:  this.costs.IntTransfCost = cost;
                     break;
            case 8:  this.costs.InvHoldingCost = cost;
                     break;
            case 9:  this.costs.TotalStationCost = cost;
                     break;
        }
    }
    
    public String getLocation(){
        return this.location;
    }
    
    public double getNumOrdersLeft(){
        //return this.numOrdersLeft;
        return this.orders.numOrdersLeft;
    }
    
    public double getCosts (int caseNum){
        switch (caseNum) {
            case 1:  return this.costs.StockoutCost;
            case 2:  return this.costs.PrintersCost;
            case 3:  return this.costs.SuppliersCost;
            case 4:  return this.costs.InternalCost;
            case 5:  return this.costs.ClientsTransCost;
            case 6:  return this.costs.SupTransfCost;
            case 7:  return this.costs.IntTransfCost;
            case 8:  return this.costs.InvHoldingCost;
            case 9:  return this.costs.TotalStationCost;
        }
        return 0;
    }
    
    public String getIsProductionCenter(){
        return this.isProductionCenter;
    }
     
    public void setStateStatistics (double StatStatWaitingNodeStation, double StatStatJoinSameNumOrder, double StatStatProduction){
        this.statestatistics.StatStatWaitingNodeStation = StatStatWaitingNodeStation;
        this.statestatistics.StatStatJoinSameNumOrder = StatStatJoinSameNumOrder;
        this.statestatistics.StatStatProduction = StatStatProduction;
    } 
    
    public double getStatStatWaitingNodeStation(){
        return this.statestatistics.StatStatWaitingNodeStation;
    }
    
    public double getStatStatJoinSameNumOrder(){
        return this.statestatistics.StatStatJoinSameNumOrder;
    }
    
    public double getStatStatProduction(){
        return this.statestatistics.StatStatProduction;
    }
    
    public void setTallyStatistics (double TSPrintersTimeIdle, double TSNumSupplierOrders, double TSProductionTime, double TSEntInternalOrders,
                                    double TSEntSupplierOrders, double TSNumInternalOrders, double TSNumStockouts, double TSWaitingNodeStation,
                                    double TSJoinSameNumOrder, double TSServiceLevel){
        
        this.tallystatistics.TSPrintersTimeIdle = TSPrintersTimeIdle;
        this.tallystatistics.TSNumSupplierOrders = TSNumSupplierOrders;
        this.tallystatistics.TSProductionTime = TSProductionTime;
        this.tallystatistics.TSEntInternalOrders = TSEntInternalOrders;
        this.tallystatistics.TSEntSupplierOrders = TSEntSupplierOrders;
        this.tallystatistics.TSNumInternalOrders = TSNumInternalOrders;
        this.tallystatistics.TSNumStockouts = TSNumStockouts;
        this.tallystatistics.TSWaitingNodeStation = TSWaitingNodeStation;
        this.tallystatistics.TSJoinSameNumOrder = TSJoinSameNumOrder;
        this.tallystatistics.TSServiceLevel = TSServiceLevel;
    }
    
    public double getTSPrintersTimeIdle(){
        return this.tallystatistics.TSPrintersTimeIdle;
    }
    
    public double getTSNumSupplierOrders(){
        return this.tallystatistics.TSNumSupplierOrders;
    }
    
    public double getTSProductionTime(){
        return this.tallystatistics.TSProductionTime;
    }
    
    public double getTSEntInternalOrders(){
        return this.tallystatistics.TSEntInternalOrders;
    }
    
    public double getTSEntSupplierOrders(){
        return this.tallystatistics.TSEntSupplierOrders;
    }
    
    public double getTSNumInternalOrders(){
        return this.tallystatistics.TSNumInternalOrders;
    }
    
    public double getTSNumStockouts(){
        return this.tallystatistics.TSNumStockouts;
    }
    
    public double getTSWaitingNodeStation(){
        return this.tallystatistics.TSWaitingNodeStation;
    }
    
    public double getTSJoinSameNumOrder(){
        return this.tallystatistics.TSJoinSameNumOrder;
    }
    
    public double getTSServiceLevel(){
        return this.tallystatistics.TSServiceLevel;
    }
    
}
