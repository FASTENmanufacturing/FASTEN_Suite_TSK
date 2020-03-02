package com.fasten.wp4.optimizator.tactical.business;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasten.wp4.optimizator.tactical.model.InternalSupply;
import com.fasten.wp4.optimizator.tactical.model.Part;
import com.fasten.wp4.optimizator.tactical.model.Production;
import com.fasten.wp4.optimizator.tactical.model.RemoteStation;
import com.fasten.wp4.optimizator.tactical.model.Route;
import com.fasten.wp4.optimizator.tactical.model.SRAMsAllocated;
import com.fasten.wp4.optimizator.tactical.model.TacticalOptimizationResult;

public class ExcelReader {

	public static void main(String args[]) throws IOException {
		String requestId="ac817831-1bcb-4b56-8339-d15257c83ebf";
		TacticalOptimizationResult result = ExcelReader.toPojo(Paths.get("studies",requestId,"TK_Data_v9.xlsx"));
		System.out.println(result);
	}

	public static TacticalOptimizationResult toPojo(Path path) throws IOException{

		Workbook wb = new XSSFWorkbook(new FileInputStream (path.toAbsolutePath().toFile()));

		int auxRow=0;
		int auxColumn=0;
		String sheetName=null;
		TacticalOptimizationResult tacticalOptimizationResult = new TacticalOptimizationResult();

		for (int i=0; i<wb.getNumberOfSheets(); i++) { 

			Sheet sheet = wb.getSheetAt(i);
			sheetName=wb.getSheetName(i);

			if(sheetName.contains("Rotas")){
				List<Route> routes = new ArrayList<Route>();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if(auxRow>=0){
						Iterator<Cell> cellIterator = row.cellIterator();
						Route route = new Route();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if(auxColumn==0){
								RemoteStation remoteStationOrigin = new RemoteStation();
								remoteStationOrigin.setName(cell.getStringCellValue());
								route.setRemoteStationOrigin(remoteStationOrigin);
							}else if(auxColumn==1){
								RemoteStation remoteStationDestination = new RemoteStation();
								remoteStationDestination.setName(cell.getStringCellValue());
								route.setRemoteStationDestination(remoteStationDestination);
							}else if(auxColumn==2){
								Part part = new Part();
								part.setName(cell.getStringCellValue());
								route.setPart(part);
							}else if(auxColumn==3){
								route.setQuantity(new BigDecimal(cell.getNumericCellValue()).setScale(0,RoundingMode.HALF_UP).intValue());
							}else if(auxColumn==4){
								route.setCost(new BigDecimal(cell.getNumericCellValue()).setScale(2,RoundingMode.HALF_UP));
							}else if(auxColumn==5){
								route.setLeadTime(new BigDecimal(cell.getNumericCellValue()).setScale(14,RoundingMode.HALF_UP).doubleValue());
							}
							auxColumn++;
						}
						auxColumn=0;
						routes.add(route); 
					}
					auxRow++;
				}
				tacticalOptimizationResult.setRoutes(routes);
			} else if (sheetName.contains("PrintersAllocated")) {
				List<SRAMsAllocated> sramsAllocated = new ArrayList<SRAMsAllocated>();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if(auxRow>=0){
						Iterator<Cell> cellIterator = row.cellIterator();
						SRAMsAllocated sramAllocated = new SRAMsAllocated();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if(auxColumn==0){
								RemoteStation remoteStation = new RemoteStation();
								remoteStation.setName(cell.getStringCellValue());
								sramAllocated.setRemoteStation(remoteStation);
							}else if(auxColumn==1){
								sramAllocated.setNumberOfSRAMs(new BigDecimal(cell.getNumericCellValue()).setScale(0,RoundingMode.HALF_UP).intValue());
							}
							auxColumn++;
						}
						auxColumn=0;
						sramsAllocated.add(sramAllocated); 
					}
					auxRow++;
				}
				tacticalOptimizationResult.setPrinters(sramsAllocated);
			} else if (sheetName.contains("InternalSupply")) {
				List<InternalSupply> internalSupplies = new ArrayList<InternalSupply>();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if(auxRow>=0){
						Iterator<Cell> cellIterator = row.cellIterator();
						InternalSupply internalSupply = new InternalSupply();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if(auxColumn==0){
								RemoteStation remoteStationOrigin = new RemoteStation();
								remoteStationOrigin.setName(cell.getStringCellValue());
								internalSupply.setRemoteStationOrigin(remoteStationOrigin);
							}else if(auxColumn==1){
								RemoteStation remoteStationDestination = new RemoteStation();
								remoteStationDestination.setName(cell.getStringCellValue());
								internalSupply.setRemoteStationDestination(remoteStationDestination);
							}else if(auxColumn==2){
								Part part = new Part();
								part.setName(cell.getStringCellValue());
								internalSupply.setPart(part);
							}
							auxColumn++;
						}
						auxColumn=0;
						internalSupplies.add(internalSupply); 
					}
					auxRow++;
				}
				tacticalOptimizationResult.setInternalSuppliers(internalSupplies);
			} else if (sheetName.contains("Production")) {
				List<Production> productions = new ArrayList<Production>();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if(auxRow>=0){
						Iterator<Cell> cellIterator = row.cellIterator();
						Production production = new Production();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if(auxColumn==0){
								RemoteStation remoteStation = new RemoteStation();
								remoteStation.setName(cell.getStringCellValue());
								production.setRemoteStation(remoteStation);
							}else if(auxColumn==1){
								Part part = new Part();
								part.setName(cell.getStringCellValue());
								production.setPart(part);
							}
							auxColumn++;
						}
						auxColumn=0;
						productions.add(production); 
					}
					auxRow++;
				}
				tacticalOptimizationResult.setProductions(productions);
			}
		}
		wb.close();
		return tacticalOptimizationResult;
	}
}