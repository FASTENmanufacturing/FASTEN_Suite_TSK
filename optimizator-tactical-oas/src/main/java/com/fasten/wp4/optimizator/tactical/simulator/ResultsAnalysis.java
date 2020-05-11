/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.optimizator.tactical.simulator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


/**
 *
 * @author narci
 */
public class ResultsAnalysis {
    
    private SupplyChain supplychain = new SupplyChain();
    private DataVisualization dataVisualization = new DataVisualization();
    private ArrayList<Location> locationsResults = new ArrayList<Location>();
    private int auxrow=0, auxcolumn=0;
    private String sheetName, cellLocAux;
    
    private JSONInterface jsonInterface = new JSONInterface();
    
    
    // The cell values need to be round to 3 decimal places because the Java Object to JSON method from google reach its limit size
    public static double roundAvoid(double value, int places) {
	    double scale = Math.pow(10, places);
	    return Math.round(value * scale) / scale;
	}
    
    public void analyseExelFile (Workbook wb) throws IOException{
        
        for (int i=0; i<wb.getNumberOfSheets(); i++) { 
            Sheet sheet = wb.getSheetAt(i);
            sheetName=wb.getSheetName(i);
            //System.out.println(sheetName + "\n");
            
            if(sheetName.contains("Sinks")){
                Iterator<Row> rowIterator = sheet.rowIterator();
                
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if(auxrow>0){
                        Iterator<Cell> cellIterator = row.cellIterator();
                        //System.out.print(cell + "\t");

                        Location location = new Location();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            //System.out.print(cell + "\t");

                             if(auxcolumn==2){ 
                                location.setLocation(cell.getStringCellValue());
                                //System.out.print(roundAvoid(cell.getNumericCellValue(), 3) + "\n");
                            }

                            if(auxcolumn==4){
                                location.setNumOrdersLeft(roundAvoid(cell.getNumericCellValue(), 3));
                                //System.out.print(roundAvoid(cell.getNumericCellValue(), 3) + "\n");
                            }
                            auxcolumn++;
                        }
                        auxcolumn=0;
                        locationsResults.add(location); 
                    }
                    auxrow++;
                }
            }
            
            if(sheetName.contains("Costs")){
                for (Location lr: locationsResults){
                    auxrow =0;
                    //System.out.print(ou.getLocation() + "\n");
                    Iterator<Row> rowIterator = sheet.rowIterator();
                
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        if(auxrow>0 ){
                            Iterator<Cell> cellIterator = row.cellIterator();
                            
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                if(auxcolumn==0){
                                    cellLocAux=cell.getStringCellValue();
                                }
                                
                                if(auxrow==1 && auxcolumn==10){
                                    this.supplychain.setTotalOpenStationCost(roundAvoid(cell.getNumericCellValue(), 3));
                                }
                                
                                if(auxrow==1 && auxcolumn==11){
                                    this.supplychain.setTotalSupplyChainCost(roundAvoid(cell.getNumericCellValue(), 3));
                                }
                               
                                if(lr.getLocation().toUpperCase().contains(cellLocAux)){
                                    switch (auxcolumn) {
                                        case 0:
                                                 break;
                                        case 1:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 2:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 3:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 4:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 5:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 6:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 7:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 8:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                        case 9:  lr.setCosts(auxcolumn, roundAvoid(cell.getNumericCellValue(), 3));
                                                 break;
                                    }
                                }
                                auxcolumn++;
                            }
                            auxcolumn=0;
                        }
                        auxrow++;
                    }
                }    
            }
            
            //Adicionar aqui os "if" para as folhas das StateStatistics, TallyStatistics e ServiceLevel;
            
            if(sheetName.contains("StateStatistics")){
                for (Location lr: locationsResults){
                    auxrow =0;
                    //System.out.print(ou.getLocation() + "\n");
                    Iterator<Row> rowIterator = sheet.rowIterator();
                    double aux1=0, aux2=0, aux3=0;
                
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        if(auxrow>0 ){
                            Iterator<Cell> cellIterator = row.cellIterator();
                            
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                if(auxcolumn==0){
                                    cellLocAux=cell.getStringCellValue();
                                }
                               
                                if(lr.getLocation().toUpperCase().contains(cellLocAux)){
                                    //codigo aqui
                                    
                                    switch (auxcolumn) {
                                        case 1:  aux1=roundAvoid(cell.getNumericCellValue(), 3);
                                        case 2:  aux2=roundAvoid(cell.getNumericCellValue(), 3);
                                        case 3:  aux3=roundAvoid(cell.getNumericCellValue(), 3);
                                    }
                                }
                                auxcolumn++;
                            }
                            auxcolumn=0;
                            lr.setStateStatistics(aux1, aux2, aux3);
                        }
                        auxrow++;
                    }
                }    
            }
            
            if(sheetName.contains("TallyStatistics")){
                for (Location lr: locationsResults){
                    auxrow =0;
                    //System.out.print(ou.getLocation() + "\n");
                    Iterator<Row> rowIterator = sheet.rowIterator();
                    double aux1=0, aux2=0, aux3=0, aux4=0, aux5=0, aux6=0, aux7=0, aux8=0, aux9=0, aux10=0;
                
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        if(auxrow>0 ){
                            Iterator<Cell> cellIterator = row.cellIterator();
                            
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                if(auxcolumn==0){
                                    cellLocAux=cell.getStringCellValue();
                                }
                                
                                if(lr.getLocation().toUpperCase().contains(cellLocAux)){
                                    //codigo aqui - falta identificar os NaN e converter para double
                                    if(cell.getCellType() == CellType.STRING){
                                        switch (auxcolumn) {
                                            case 1:  aux1=-1;
                                            case 2:  aux2=-1;
                                            case 3:  aux3=-1;
                                            case 4:  aux4=-1;
                                            case 5:  aux5=-1;
                                            case 6:  aux6=-1;
                                            case 7:  aux7=-1;
                                            case 8:  aux8=-1;
                                            case 9:  aux9=-1;
                                            case 10: aux10=-1;
                                        }
                                    }
                                    
                                    else{
                                        switch (auxcolumn) {
                                            case 1:  aux1=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 2:  aux2=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 3:  aux3=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 4:  aux4=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 5:  aux5=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 6:  aux6=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 7:  aux7=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 8:  aux8=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 9:  aux9=roundAvoid(cell.getNumericCellValue(), 3);
                                            case 10: aux10=roundAvoid(cell.getNumericCellValue(), 3);
                                        }
                                    }
                                    
                                }
                                auxcolumn++;
                            }
                            auxcolumn=0;
                            lr.setTallyStatistics(aux1, aux2, aux3, aux4, aux5, aux6, aux7, aux8, aux9, aux10);
                        }
                        auxrow++;
                    }
                }    
            }
            
            // Ainda falta Service Level
        }
        this.supplychain.setLocations(locationsResults);
        this.dataVisualization.getLocationsInfo(this.supplychain);
        
        jsonInterface.writeSIMResultsJSONFile(this.supplychain);
    }
}