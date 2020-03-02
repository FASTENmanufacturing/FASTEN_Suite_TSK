/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;

import java.util.ArrayList;

/**
 *
 * @author narci
 */
public class TallyStatistics {
    
    //As tallys vao estar como double, mas quando nao tem valor no Excel aparecem como NaN, e preciso analisar isso;
    public double TSPrintersTimeIdle, TSNumSupplierOrders, TSProductionTime, TSEntInternalOrders, TSEntSupplierOrders, TSNumInternalOrders,
                  TSNumStockouts, TSWaitingNodeStation, TSJoinSameNumOrder, TSServiceLevel;
    
    private ServiceLevelOverTime servicelevelovertimeAux = new ServiceLevelOverTime();
    public ArrayList<ServiceLevelOverTime> servicelevelovertime = new ArrayList<>();
    
    public void setServiceLevelValueOverTime (double t, double v){
        this.servicelevelovertimeAux.setServiceLevelOverTime(t, v);
        this.servicelevelovertime.add(servicelevelovertimeAux);
    }
    
}
