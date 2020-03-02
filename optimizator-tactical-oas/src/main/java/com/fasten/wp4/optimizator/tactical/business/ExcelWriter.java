package com.fasten.wp4.optimizator.tactical.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasten.wp4.optimizator.tactical.model.Delivery;
import com.fasten.wp4.optimizator.tactical.model.Demand;
import com.fasten.wp4.optimizator.tactical.model.Part;
import com.fasten.wp4.optimizator.tactical.model.ProcessingPart;
import com.fasten.wp4.optimizator.tactical.model.RemoteStation;
import com.fasten.wp4.optimizator.tactical.model.SRAM;
import com.fasten.wp4.optimizator.tactical.model.TacticalOptimization;
import com.google.common.base.CaseFormat;


public class ExcelWriter {


	//create Demand xls fileOs
	@SuppressWarnings("unused")
	public static void writeDemandExcell(String requestId,TacticalOptimization study, List<Demand> demands, List<RemoteStation> remoteStations, List<Delivery> deliveries, List<Part> parts) throws IOException {
		String[] columnsConfig = {"Max locations", "Importancia do tempo", "Importancia da distancia"};
		String[] columns = {"Filial", "Cidade", "Estado", "Data Pedido", "Data Emissão NF","Peça", "Demanda", "Idade do Elev."};
		String[] columnsPascalCase = {"Remote Stations","PascalCase"};
		String[] columnsTotalCost = {"Cidade Origem", "Cidade Destino","Duration","Duration","NormDistance","DistanceNorm","totCostNorm"};
		String[] columnsResultado = {"Remote Stations","Filiais"};
		String[] columnsRemoteStations = {"Remote Stations"};
		String[] columnsRemoteStationsFiliais = {"Remote Stations","Filial"};
		String[] columnsPecas = {"Parts"};
		String[] columnsFiliais = {"Filiais"};

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheetConfig = workbook.createSheet("Config");
		Sheet sheetDemand = workbook.createSheet("Demand");
		Sheet sheetPascalCase = workbook.createSheet("PascalCase");
		Sheet sheetTotalCost = workbook.createSheet("TotalCost");
		Sheet sheetResultado = workbook.createSheet("Resultado");
		Sheet sheetRemoteStations = workbook.createSheet("RemoteStations");
		Sheet sheetRemoteStationsFiliais = workbook.createSheet("RemoteStationsFiliais");
		Sheet sheetPecas = workbook.createSheet("Pecas");
		Sheet sheetFiliais = workbook.createSheet("Filiais");
		Sheet sheetDemandGroup = workbook.createSheet("DemandGroup");
		Sheet sheetDemandCalc = workbook.createSheet("DemandCalc");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(false);
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

		// Create a Row
		Row headerRowConfig = sheetConfig.createRow(1);
		Row headerRow = sheetDemand.createRow(1);
		Row headerRowPascalCase = sheetPascalCase.createRow(0);
		Row headerRowTotalCost = sheetTotalCost.createRow(0);
		Row headerRowResultado = sheetResultado.createRow(0);
		Row headerRowRemoteStations = sheetRemoteStations.createRow(0);
		Row headerRowRemoteStationsFiliais = sheetRemoteStationsFiliais.createRow(0);
		Row headerRowPecas = sheetPecas.createRow(0);
		Row headerRowFiliais = sheetFiliais.createRow(0);

		// Create cells
		for(int i = 0; i < columnsConfig.length; i++) {
			int index = i+1;
			Cell cell = headerRowConfig.createCell(index);
			cell.setCellValue(columnsConfig[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columns.length; i++) {
			int index = i+1;
			Cell cell = headerRow.createCell(index);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsPascalCase.length;i++) {
			int index = i+1;
			Cell cell = headerRowPascalCase.createCell(index);
			cell.setCellValue(columnsPascalCase[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsTotalCost.length;i++) {
			int index = i+1;
			Cell cell = headerRowTotalCost.createCell(index);
			cell.setCellValue(columnsTotalCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsResultado.length;i++) {
			int index = i+1;
			Cell cell = headerRowResultado.createCell(index);
			cell.setCellValue(columnsResultado[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsRemoteStations.length;i++) {
			int index = i+1;
			Cell cell = headerRowRemoteStations.createCell(index);
			cell.setCellValue(columnsRemoteStations[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsRemoteStationsFiliais.length;i++) {
			int index = i+1;
			Cell cell = headerRowRemoteStationsFiliais.createCell(index);
			cell.setCellValue(columnsRemoteStationsFiliais[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsPecas.length;i++) {
			int index = i+1;
			Cell cell = headerRowPecas.createCell(index);
			cell.setCellValue(columnsPecas[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i<columnsFiliais.length;i++) {
			int index = i+1;
			Cell cell = headerRowFiliais.createCell(index);
			cell.setCellValue(columnsFiliais[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat( 14 )));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		// Create Cell Style for formatting percent
		CellStyle percentCellStyle = workbook.createCellStyle();
		percentCellStyle.setDataFormat(workbook.createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat( 10 )));

		// Create Other rows and cells with config data
		config:{
			int rowNumConfig = 2;
			Row row = sheetConfig.createRow(rowNumConfig++);
			if(study.getMaximumLocations()!=null)
				row.createCell(1).setCellValue(study.getMaximumLocations());
			else
				row.createCell(1).setCellValue(remoteStations.size());
			if(study.getDistanceWeight()!=null)
				row.createCell(2).setCellValue(study.getDistanceWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
			else
				row.createCell(2).setCellValue(0.3d);
			if(study.getTimeWeight()!=null)
				row.createCell(3).setCellValue(study.getTimeWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
			else
				row.createCell(3).setCellValue(0.7d);
		}
		
		// Create Other rows and cells with demand data
		int rowNum = 2;
		for(Demand demand: demands) {
			Row row = sheetDemand.createRow(rowNum++);
			row.createCell(0).setCellValue(rowNum-2);
			if(!demand.getRemoteStation().getCode().isEmpty())
				row.createCell(1).setCellValue(Long.parseLong(demand.getRemoteStation().getCode()));
			else
				row.createCell(1).setCellValue(0);
			row.createCell(2).setCellValue(demand.getRemoteStation().getName().replaceAll("\\s",""));
			row.createCell(3).setCellValue(demand.getRemoteStation().getState().getStateAcronym());

			Cell orderDate = row.createCell(4);
			if(demand.getOrderDate()!=null) {
				orderDate.setCellValue(getDateWithoutTime(demand.getOrderDate()));
			}else {
				orderDate.setCellValue(getDateWithoutTime(new Date()));
			}
			orderDate.setCellStyle(dateCellStyle);

			Cell deliveryDate = row.createCell(5);
			if(demand.getDeliveryDate()!=null) {
				deliveryDate.setCellValue(getDateWithoutTime(demand.getDeliveryDate()));
			}else {
				deliveryDate.setCellValue(getDateWithoutTime(new Date()));
			}

			deliveryDate.setCellStyle(dateCellStyle);

			row.createCell(6).setCellValue(demand.getPart().getName().replaceAll("\\s",""));
			row.createCell(7).setCellValue(demand.getQuantity());
			row.createCell(8).setCellValue("");//TODO
		}

		// Create Other rows and cells with pascal case data
		int rowNumPascalCase = 1;
		for(RemoteStation remoteStation: remoteStations) {
			Row row = sheetPascalCase.createRow(rowNumPascalCase++);
			String name = Normalizer.normalize(remoteStation.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","");
			String pascalName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, Normalizer.normalize(remoteStation.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","_").toUpperCase());
			row.createCell(1).setCellValue(name);
			row.createCell(2).setCellValue(pascalName);
		}

		// Create Other rows and cells with total cost data
		Integer maxTime = deliveries.stream().mapToInt(d -> d.getTime()).max().orElseThrow(NoSuchElementException::new);
		Integer maxDistance = deliveries.stream().mapToInt(d -> d.getDistance()).max().orElseThrow(NoSuchElementException::new);

		int rowNumTotalCost = 1;
		for(Delivery delivery: deliveries) {
			Row row = sheetTotalCost.createRow(rowNumTotalCost++);
			String cidadeOrigem = Normalizer.normalize(delivery.getOrigin().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(1).setCellValue(cidadeOrigem);
			String cidadeDestino = Normalizer.normalize(delivery.getDestination().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(2).setCellValue(cidadeDestino);
			row.createCell(3).setCellValue(delivery.getTime());

			BigDecimal durationAbsoluteNorm = new BigDecimal(delivery.getTime()).divide(new BigDecimal(maxTime), 6, RoundingMode.HALF_UP);
			Cell durationNorm = row.createCell(4);
			durationNorm.setCellValue(durationAbsoluteNorm.doubleValue());
			durationNorm.setCellStyle(percentCellStyle);

			row.createCell(5).setCellValue(delivery.getDistance());

			BigDecimal distanceAbsoluteNorm = new BigDecimal(delivery.getDistance()).divide(new BigDecimal(maxDistance), 6, RoundingMode.HALF_UP);
			Cell distanceNorm = row.createCell(6);
			distanceNorm.setCellValue(distanceAbsoluteNorm.doubleValue());
			distanceNorm.setCellStyle(percentCellStyle);

			BigDecimal timeWeight = new BigDecimal(0.7d);
			if(study.getTimeWeight()!=null)
				timeWeight = study.getTimeWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

			BigDecimal distanceWeight = new BigDecimal(0.3d);
			if(study.getDistanceWeight()!=null)
				distanceWeight = study.getDistanceWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

			BigDecimal totCostAbsoluteDuration = durationAbsoluteNorm.multiply(timeWeight); 
			BigDecimal totCostAbsoluteDistance = distanceAbsoluteNorm.multiply(distanceWeight); 
			BigDecimal totCostAbsoluteNorm = totCostAbsoluteDuration.add(totCostAbsoluteDistance);
			Cell totCostNorm = row.createCell(7);
			totCostNorm.setCellValue(totCostAbsoluteNorm.doubleValue());
			totCostNorm.setCellStyle(percentCellStyle);
		}

		// Dont Create Other rows and cells with result data <--- CPLEX does this

		// Create Other rows and cells with Remote stations case data
		//TODO verify if the remote in this sheet is from results, if so, let CPLEX write it.
		int rowNumRemoteStations = 1;
		for(RemoteStation remoteStation: remoteStations) {
			Row row = sheetRemoteStations.createRow(rowNumRemoteStations++);
			String name = Normalizer.normalize(remoteStation.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(1).setCellValue(name);
		}

		// Create Other rows and cells with pecas case data
		int rowNumPecas = 1;
		for(Part part: parts) {
			Row row = sheetPecas.createRow(rowNumPecas++);
			String name = Normalizer.normalize(part.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","");
			row.createCell(1).setCellValue(name);
		}

		// Resize all columns to fit the content size
		for(int i = 1; i <= columnsConfig.length; i++) {
			sheetConfig.autoSizeColumn(i);
		}
		for(int i = 1; i <= columns.length; i++) {
			sheetDemand.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsPascalCase.length; i++) {
			sheetPascalCase.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsTotalCost.length; i++) {
			sheetTotalCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsResultado.length; i++) {
			sheetResultado.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsRemoteStations.length; i++) {
			sheetRemoteStations.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsRemoteStationsFiliais.length; i++) {
			sheetRemoteStationsFiliais.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsPecas.length; i++) {
			sheetPecas.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsFiliais.length; i++) {
			sheetFiliais.autoSizeColumn(i);
		}

		Path file = Paths.get("studies",requestId,"Demanda Fasten.xlsx");
		FileOutputStream fileOut = new FileOutputStream(file.toAbsolutePath().toFile().getPath());
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	public static void writeTKExcell(String requestId,TacticalOptimization study, List<RemoteStation> remoteStations, List<Part> parts, List<SRAM> srams, List<ProcessingPart> processingParts, Double minLeadTime) throws IOException {
		String[] columnsConfig = {"Custo fixo anual", "Custo capital","Custo de escassez","Importancia do tempo","Importancia do custo","Limite de horas para entrega de peça"};
		String[] columnsImpressoras = {"Marca", "Capacidade Anual (horas)", "Custo fixo anual (R$)", "Número máximo"};
		String[] columnsFornecedores = {"Fornecedores","Cidade"};
		String[] columnsProcessingPartCost = {"Remote Station","Part","Cost (R$)","Processing Time (horas)"};
		String[] columnsSuppliedPartCost = {"Supplier","Part","Cost (R$)"};
		String[] columnsInternalOrderCost = {"Remote Station","Part","Cost (R$)"};
		String[] columnsTotalRemoteStationCost = {"Remote Station","Remote Station","Part","Cost (R$)","Time (hours)","%"};
		String[] columnsTotalSupplierCost = {"Supplier","Remote Station","Part","Cost (R$)","Time(hours)","%"};

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheetConfig = workbook.createSheet("Config");
		Sheet sheetImpressoras= workbook.createSheet("Impressoras");
		Sheet sheetFornecedores= workbook.createSheet("Fornecedores");
		Sheet sheetProcessingPartCost= workbook.createSheet("ProcessingPartCost");
		Sheet sheetSuppliedPartCost= workbook.createSheet("SuppliedPartCost");
		Sheet sheetInternalOrderCost= workbook.createSheet("InternalOrderCost");

		//empty results sheet that will be fullfiled by CPLEX
		Sheet sheetTotalRemoteStationCost= workbook.createSheet("TotalRemoteStationCost");
		Sheet sheetTotalSupplierCost= workbook.createSheet("TotalSupplierCost");
		Sheet sheetRotas= workbook.createSheet("Rotas");
		Sheet sheetPrintersAllocated= workbook.createSheet("PrintersAllocated");
		Sheet sheetInternalSupply= workbook.createSheet("InternalSupply");
		Sheet sheetFornecimento= workbook.createSheet("Fornecimento");
		Sheet sheetExternalSupply= workbook.createSheet("ExternalSupply");
		Sheet sheetProduction= workbook.createSheet("Production");


		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(false);
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

		// Create a Row
		Row headerRowConfig = sheetConfig.createRow(1);
		Row headerRowImpressoras = sheetImpressoras.createRow(1);
		Row headerRowFornecedores = sheetFornecedores.createRow(0);
		Row headerRowProcessingPartCost = sheetProcessingPartCost.createRow(0);
		Row headerRowSuppliedPartCost = sheetSuppliedPartCost.createRow(0);
		Row headerRowInternalOrderCost = sheetInternalOrderCost.createRow(0);
		Row headerRowTotalRemoteStationCost = sheetTotalRemoteStationCost.createRow(0);
		Row headerRowTotalSupplierCost = sheetTotalSupplierCost.createRow(1);

		// Create cells
		for(int i = 0; i < columnsConfig.length; i++) {
			int index = i+1;
			Cell cell = headerRowConfig.createCell(index);
			cell.setCellValue(columnsConfig[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsImpressoras.length; i++) {
			int index = i+1;
			Cell cell = headerRowImpressoras.createCell(index);
			cell.setCellValue(columnsImpressoras[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsFornecedores.length; i++) {
			int index = i+1;
			Cell cell = headerRowFornecedores.createCell(index);
			cell.setCellValue(columnsFornecedores[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsProcessingPartCost.length; i++) {
			int index = i+1;
			Cell cell = headerRowProcessingPartCost.createCell(index);
			cell.setCellValue(columnsProcessingPartCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsSuppliedPartCost.length; i++) {
			int index = i+1;
			Cell cell = headerRowSuppliedPartCost.createCell(index);
			cell.setCellValue(columnsSuppliedPartCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsInternalOrderCost.length; i++) {
			int index = i+1;
			Cell cell = headerRowInternalOrderCost.createCell(index);
			cell.setCellValue(columnsInternalOrderCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsTotalRemoteStationCost.length; i++) {
			int index = i+1;
			Cell cell = headerRowTotalRemoteStationCost.createCell(index);
			cell.setCellValue(columnsTotalRemoteStationCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsTotalSupplierCost.length; i++) {
			int index = i+1;
			Cell cell = headerRowTotalSupplierCost.createCell(index);
			cell.setCellValue(columnsTotalSupplierCost[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

		// Create Cell Style for formatting percent
		CellStyle percentCellStyle = workbook.createCellStyle();
		percentCellStyle.setDataFormat(workbook.createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat( 10 )));

		// Create Other rows and cells with Config data
		int rowConfig = 2;
		config: {
			Row row = sheetConfig.createRow(rowConfig++);
			row.createCell(1).setCellValue(study.getProductionCenterCost().doubleValue());
			if(study.getCapitalCost()!=null && study.getCapitalCost().compareTo(BigDecimal.ZERO)!=0)
				row.createCell(2).setCellValue(study.getCapitalCost().doubleValue());
			else
				row.createCell(2).setCellValue(0.2d);
			if(study.getStockoutCost()!=null && study.getStockoutCost().compareTo(BigDecimal.ZERO)!=0)
				row.createCell(3).setCellValue(study.getStockoutCost().doubleValue());
			else
				row.createCell(3).setCellValue(400.0d);
			if(study.getTimeWeight()!=null)
				row.createCell(4).setCellValue(study.getTimeWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
			else
				row.createCell(4).setCellValue(0.7d);
			if(study.getDistanceWeight()!=null)
				row.createCell(5).setCellValue(study.getDistanceWeight().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
			else
				row.createCell(5).setCellValue(0.3d);
			if(study.getLeadTimeLimit()!=null)
				row.createCell(6).setCellValue(study.getLeadTimeLimit().doubleValue());
			else
				row.createCell(6).setCellValue(minLeadTime);
		}

		// Create Other rows and cells with Impressoras data
		int rowNumImpressoras = 2;
		for(SRAM sram: srams) {
			Row row = sheetImpressoras.createRow(rowNumImpressoras++);
			row.createCell(0).setCellValue(rowNumImpressoras-2);
			row.createCell(1).setCellValue(sram.getCode());
			//TODO define if it comes from sram or by study
			row.createCell(2).setCellValue(sram.getCapacity());
			row.createCell(3).setCellValue(sram.getFixedCost().doubleValue());
			row.createCell(4).setCellValue(study.getMaximumSrams());
		}

		// Create Other rows and cells with Fornecedores data
		int rowNumFornecedores = 1;
		fornecedores: {
			Row row = sheetFornecedores.createRow(rowNumFornecedores++);
			row.createCell(1).setCellValue("Supplier1");
			row.createCell(2).setCellValue("MANAUS");
		}

		// Create Other rows and cells with Processing Part Cost data
		int rowProcessingPartCost = 1;
		for(ProcessingPart processingPart: processingParts) {
			Row row = sheetProcessingPartCost.createRow(rowProcessingPartCost++);
			row.createCell(0).setCellValue(rowProcessingPartCost-1);
			row.createCell(1).setCellValue(processingPart.getSRAM().getCode());
			row.createCell(2).setCellValue(processingPart.getPart().getName().replaceAll("\\s",""));
			row.createCell(3).setCellValue(processingPart.getCost().doubleValue());
			double totalProcessingTime = (processingPart.getAvgProducingTime()+processingPart.getStdProducingTime()+processingPart.getAvgSetupTime()+processingPart.getStdSetupTime())/3600;
			row.createCell(4).setCellValue(totalProcessingTime);
		}
		Double maxCostProcessingPart = processingParts.stream().mapToDouble(pp -> pp.getCost().doubleValue()).max().orElseThrow(NoSuchElementException::new);

		// Create Other rows and cells with Supplied Part Cost data
		int rowSuppliedPartCost = 1;
		for(Part part: parts) {
			Row row = sheetSuppliedPartCost.createRow(rowSuppliedPartCost++);
			row.createCell(1).setCellValue("Supplier1");
			row.createCell(2).setCellValue(part.getName().replaceAll("\\s",""));
			row.createCell(3).setCellValue(maxCostProcessingPart*100);
		}

		// Create Other rows and cells with InternalOrderCost data
		int rowNumInternalOrderCost = 1;
		for(RemoteStation remoteStation: remoteStations) {
			for(Part part: parts) {
				Row row = sheetInternalOrderCost.createRow(rowNumInternalOrderCost++);
				row.createCell(1).setCellValue(Normalizer.normalize(remoteStation.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase());
				row.createCell(2).setCellValue(part.getName().replaceAll("\\s",""));
				row.createCell(3).setCellValue(0.0d);
			}
		}

		// Resize all columns to fit the content size
		for(int i = 1; i <= columnsConfig.length; i++) {
			sheetConfig.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsImpressoras.length; i++) {
			sheetImpressoras.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsFornecedores.length; i++) {
			sheetFornecedores.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsProcessingPartCost.length; i++) {
			sheetProcessingPartCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsSuppliedPartCost.length; i++) {
			sheetSuppliedPartCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsInternalOrderCost.length; i++) {
			sheetInternalOrderCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsTotalRemoteStationCost.length; i++) {
			sheetTotalRemoteStationCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsTotalSupplierCost.length; i++) {
			sheetTotalSupplierCost.autoSizeColumn(i);
		}

		Path file = Paths.get("studies",requestId,"TK_Data_v9.xlsx");
		FileOutputStream fileOut = new FileOutputStream(file.toAbsolutePath().toFile().getPath());
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	public static void writeCostsExcell(String requestId, List<Delivery> deliveries) throws IOException {
		String[] columnsDistanceCost = {"Origem", "Destino", "Distancia", "Distancia (legivel)"};
		String[] columnsTimeCost = {"Origem", "Destino", "Tempo da viagem (segundos)", "Tempo da viagem (legivel)"};

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheetDistanceCost = workbook.createSheet("DistanceCost");
		Sheet sheetTimeCost = workbook.createSheet("TimeCost");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(false);
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

		// Create a Row
		Row headerRowDistanceCost = sheetDistanceCost.createRow(0);
		Row headerRowTimeCost = sheetTimeCost.createRow(0);

		// Create cells
		for(int i = 0; i < columnsDistanceCost.length; i++) {
			Cell cell = headerRowDistanceCost.createCell(i);
			cell.setCellValue(columnsDistanceCost[i]);
			cell.setCellStyle(headerCellStyle);
		}
		for(int i = 0; i < columnsTimeCost.length; i++) {
			Cell cell = headerRowTimeCost.createCell(i);
			cell.setCellValue(columnsTimeCost[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Other rows and cells with Distance Cost data
		int rowNumDistanceCost = 1;
		for(Delivery delivery: deliveries) {
			Row row = sheetDistanceCost.createRow(rowNumDistanceCost++);
			String cidadeOrigem = Normalizer.normalize(delivery.getOrigin().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(0).setCellValue(cidadeOrigem);
			String cidadeDestino = Normalizer.normalize(delivery.getDestination().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(1).setCellValue(cidadeDestino);
			row.createCell(2).setCellValue(delivery.getDistance());
			row.createCell(3).setCellValue(delivery.getDistance()/1000+ " Km");
		}

		// Create Other rows and cells with Time Cost data
		int rowNumTimeCost = 1;
		for(Delivery delivery: deliveries) {
			Row row = sheetTimeCost.createRow(rowNumTimeCost++);
			String cidadeOrigem = Normalizer.normalize(delivery.getOrigin().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(0).setCellValue(cidadeOrigem);
			String cidadeDestino = Normalizer.normalize(delivery.getDestination().getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("\\s","").toUpperCase();
			row.createCell(1).setCellValue(cidadeDestino);
			row.createCell(2).setCellValue(delivery.getTime());
			row.createCell(3).setCellValue(convertToDaysHoursMinutes(delivery.getTime()));
		}

		// Resize all columns to fit the content size
		for(int i = 1; i <= columnsDistanceCost.length; i++) {
			sheetDistanceCost.autoSizeColumn(i);
		}
		for(int i = 1; i <= columnsTimeCost.length; i++) {
			sheetTimeCost.autoSizeColumn(i);
		}

		Path file = Paths.get("studies",requestId,"Costs.xlsx");
		FileOutputStream fileOut = new FileOutputStream(file.toAbsolutePath().toFile().getPath());
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	public static String convertToDaysHoursMinutes(long seconds) {

		int day = (int)TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60); 
		long seg = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds)* 60);

		String result = "";

		if (day != 0){
			result += day;
			if (day == 1){
				result += " dia ";
			}else{
				result += " dias ";
			}
		}

		if (hours != 0){
			result += hours;

			if (hours == 1){
				result += " hr ";
			}
			else{
				result += " hrs ";
			}
		}

		if (minute != 0){
			result += minute + " min";;
		}

		if (seg != 0){
			result += " " + seg + " s";
		}
		
		if (result.isEmpty()) {
			result +="0s";
		}

		return result;
	}

	public static LocalDate getDateWithoutTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String timeStr=dateFormat.format(date);
		String yearStr = timeStr.substring(0, 4);
		String monthStr = timeStr.substring(5, 7);
		String dayStr = timeStr.substring(8, 10);
		int year = Integer.parseInt(yearStr);
		int month = Integer.parseInt(monthStr);
		int day = Integer.parseInt(dayStr);
		LocalDate localDate = LocalDate.of(year, month, day);
		long noFoDays = localDate.toEpochDay();
		LocalDate newDate = LocalDate.ofEpochDay(noFoDays);
		return newDate;
	}

}
