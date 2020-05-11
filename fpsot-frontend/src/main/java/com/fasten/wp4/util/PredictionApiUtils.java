package com.fasten.wp4.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.DemandProjected;
import com.fasten.wp4.database.client.model.DemandProjected.DemandSubtypeEnum;
import com.fasten.wp4.database.client.model.DemandProjectionStudy;
import com.fasten.wp4.database.client.model.ErrorProjected;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.predictive.client.model.DemandData;
import com.fasten.wp4.predictive.client.model.ForecastErro;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;
import com.fasten.wp4.predictive.client.model.ModelsResult;
import com.fasten.wp4.predictive.client.model.StudyResults;

public class PredictionApiUtils {
	
	
//	#############################################
//	################ INPUT #####################
//	#############################################
	public static ForecastingStudy convertInput(Prediction prediction, List<Demand> realDemand) throws ApiException {
		ForecastingStudy forecastingStudy = new ForecastingStudy();
		forecastingStudy.setDemands(convert(realDemand));
		forecastingStudy.setHorizon(prediction.getHorizon());
		forecastingStudy.setModels(convertToForecastingStudyModel(prediction));
		forecastingStudy.setPart((prediction.getPart()!=null && prediction.getPart().getName()!=null)?prediction.getPart().getName():"All");
		forecastingStudy.setRemoteStation((prediction.getRemoteStation()!=null && prediction.getRemoteStation().getName()!=null)?prediction.getRemoteStation().getName():"All");
		forecastingStudy.setFrequency(convert(prediction.getGranularity()));
		return forecastingStudy;
	}
	
	private static List<ForecastingStudy.ModelsEnum> convertToForecastingStudyModel(Prediction prediciton){
		List<ForecastingStudy.ModelsEnum> models = new ArrayList<ForecastingStudy.ModelsEnum>();
		if(Prediction.ModelSelectionEnum.MANUAL.equals(prediciton.getModelSelection())) {
			for(Prediction.ModelsEnum selectedModel : prediciton.getModels()) {
				models.add(Prediction.ModelsEnum.AR.equals(selectedModel)? ForecastingStudy.ModelsEnum.AR :
							(Prediction.ModelsEnum.CF1.equals(selectedModel))? ForecastingStudy.ModelsEnum.CF1 :
								(Prediction.ModelsEnum.HOLT.equals(selectedModel))? ForecastingStudy.ModelsEnum.HOLT :
									(Prediction.ModelsEnum.NAIVE.equals(selectedModel))? ForecastingStudy.ModelsEnum.NAIVE :
										(Prediction.ModelsEnum.SES.equals(selectedModel))? ForecastingStudy.ModelsEnum.SES :
											(Prediction.ModelsEnum.CR.equals(selectedModel))? ForecastingStudy.ModelsEnum.CR :
												(Prediction.ModelsEnum.ANN.equals(selectedModel))? ForecastingStudy.ModelsEnum.ANN :
													(Prediction.ModelsEnum.ELM.equals(selectedModel))? ForecastingStudy.ModelsEnum.ELM :
												null);
			}
		}else if(Prediction.ModelSelectionEnum.AUTOMATIC.equals(prediciton.getModelSelection())) {
			models.add(ForecastingStudy.ModelsEnum.AUTO);
		}else {
			return null;
		}
		return models;
	}
	
	private static ForecastingStudy.FrequencyEnum convert(Prediction.GranularityEnum selectedGranularity){
		return (Prediction.GranularityEnum.ANNUAL.equals(selectedGranularity))? ForecastingStudy.FrequencyEnum.A :
			(Prediction.GranularityEnum.MONTHLY.equals(selectedGranularity))? ForecastingStudy.FrequencyEnum.M :
				(Prediction.GranularityEnum.WEEKLY.equals(selectedGranularity))? ForecastingStudy.FrequencyEnum.W :
					(Prediction.GranularityEnum.DAILY.equals(selectedGranularity))? ForecastingStudy.FrequencyEnum.D :
						null;
	}
	
	private static List<DemandData> convert(List<Demand> realDemand){
		return realDemand.stream().map(d->new DemandData().demandDate(new SimpleDateFormat("dd/MM/yyyy").format(d.getOrderDate())).demandValue(d.getQuantity())).collect(Collectors.toList());
	}
	
//	#############################################
//	################ OUTPUT #####################
//	#############################################
	public static DemandProjectionStudy convertOutput(Prediction prediction, List<Demand> realDemand, StudyResults resultados) throws ApiException {
		
		DemandProjectionStudy demandProjectionStudy = new DemandProjectionStudy();
		demandProjectionStudy.setStudy(prediction);
		demandProjectionStudy.setPrevisionDate(new Date());
		demandProjectionStudy.setRealDemandConsideredStart(getMinRealDate(realDemand));
		demandProjectionStudy.setRealDemandConsideredEnd(getMaxRealDate(realDemand));
		demandProjectionStudy.setProcessedDemandConsideredStart(getMinProcessedDate(resultados.getProcessedDemand()));
		demandProjectionStudy.setProcessedDemandConsideredEnd(getMaxProcessedDate(resultados.getProcessedDemand()));
		demandProjectionStudy.setDemandProjecteds(new ArrayList<DemandProjected>());
		demandProjectionStudy.setErrors(new ArrayList<ErrorProjected>());
		
		for (ModelsResult modelsResult : resultados.getModelsResults()) {
			
			demandProjectionStudy.setPrevisionPeriodConsideredStart( 
					(demandProjectionStudy.getPrevisionPeriodConsideredStart()==null 
						|| getMinForecastDate(modelsResult.getForecastDemand()).before(demandProjectionStudy.getPrevisionPeriodConsideredStart()))? 
								getMinForecastDate(modelsResult.getForecastDemand()): 
									demandProjectionStudy.getPrevisionPeriodConsideredStart());
			
			demandProjectionStudy.setPrevisionPeriodConsideredEnd( 
					(demandProjectionStudy.getPrevisionPeriodConsideredEnd()==null 
					    || getMaxForecastDate(modelsResult.getForecastDemand()).after(demandProjectionStudy.getPrevisionPeriodConsideredEnd()))? 
					    		getMaxForecastDate(modelsResult.getForecastDemand()): 
					    			demandProjectionStudy.getPrevisionPeriodConsideredEnd());
			
			DemandSubtypeEnum demandSubtype = convertToDemandSubtype(modelsResult.getModel());
			
			demandProjectionStudy.getDemandProjecteds().addAll(Stream.of(
					getDemandProjectedList(prediction, resultados.getProcessedDemand(), DemandProjected.DemandTypeEnum.PROCESSED, demandSubtype),
					getDemandProjectedList(prediction, modelsResult.getTrainingPrediction(), DemandProjected.DemandTypeEnum.TRAIN, demandSubtype),
					getDemandProjectedList(prediction, modelsResult.getTestPrediction(), DemandProjected.DemandTypeEnum.TEST, demandSubtype),
					getDemandProjectedList(prediction, modelsResult.getForecastDemand(), DemandProjected.DemandTypeEnum.FORECAST, demandSubtype)
			).flatMap(Collection::stream).collect(Collectors.toList()));
			
			demandProjectionStudy.getErrors().addAll(getErrorProjectedList(modelsResult));
		}
		
		return demandProjectionStudy;
		
	}
	
	private static DemandProjected.DemandSubtypeEnum convertToDemandSubtype(ModelsResult.ModelEnum selectedModel){
		return (ModelsResult.ModelEnum.AR.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.AR :
			(ModelsResult.ModelEnum.CF1.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.CF1 :
				(ModelsResult.ModelEnum.HOLT.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.HOLT :
					(ModelsResult.ModelEnum.NAIVE.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.NAIVE :
						(ModelsResult.ModelEnum.SES.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.SES :
							(ModelsResult.ModelEnum.CR.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.CR :
								(ModelsResult.ModelEnum.ANN.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.ANN :
									(ModelsResult.ModelEnum.ELM.equals(selectedModel))? DemandProjected.DemandSubtypeEnum.ELM :
								null;
	}
	
	private static ErrorProjected.ErrorTypeEnum convertToErrorType(ForecastErro.ErrorTypeEnum errorType) {
		return (ForecastErro.ErrorTypeEnum.TEST.equals(errorType))? ErrorProjected.ErrorTypeEnum.TEST :
			(ForecastErro.ErrorTypeEnum.TRAIN.equals(errorType))? ErrorProjected.ErrorTypeEnum.TRAIN :
					null;
	}
	
	private static ErrorProjected.ErrorSubtypeEnum convertToErrorSubtype(ModelsResult.ModelEnum selectedModel){
		return (ModelsResult.ModelEnum.AR.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.AR :
			(ModelsResult.ModelEnum.CF1.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.CF1 :
				(ModelsResult.ModelEnum.HOLT.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.HOLT :
					(ModelsResult.ModelEnum.NAIVE.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.NAIVE :
						(ModelsResult.ModelEnum.SES.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.SES :
							(ModelsResult.ModelEnum.CR.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.CR :
								(ModelsResult.ModelEnum.ANN.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.ANN :
									(ModelsResult.ModelEnum.ELM.equals(selectedModel))? ErrorProjected.ErrorSubtypeEnum.ELM :
								null;
	}
	
	private static ErrorProjected.ErrorMethodEnum convertToErrorMethod(ForecastErro.NameEnum errorMethod) {
		return (ForecastErro.NameEnum.RMSE.equals(errorMethod))? ErrorProjected.ErrorMethodEnum.RMSE :
			(ForecastErro.NameEnum.MAPE.equals(errorMethod))? ErrorProjected.ErrorMethodEnum.MAPE :
				(ForecastErro.NameEnum.MASE.equals(errorMethod))? ErrorProjected.ErrorMethodEnum.MASE :
				null;
	}
	
	private static Date getMaxProcessedDate(List<DemandData> processedDemand) {
		return processedDemand.stream().map(DemandData::getDemandDate).map(s ->{try { return new SimpleDateFormat("dd/MM/yyyy").parse(s); } catch (ParseException pe) {return null;}}).max(Date::compareTo).get();
	}
	
	private static Date getMinProcessedDate(List<DemandData> processedDemand) {
		return processedDemand.stream().map(DemandData::getDemandDate).map(s ->{try {return new SimpleDateFormat("dd/MM/yyyy").parse(s);} catch (ParseException pe) {return null;}}).min(Date::compareTo).get();
	}
	
	private static Date getMaxRealDate(List<Demand> realDemand) {
		return realDemand.stream().map(Demand::getOrderDate).max(Date::compareTo).get();
	}
	
	private static Date getMinRealDate(List<Demand> realDemand) {
		return realDemand.stream().map(Demand::getOrderDate).min(Date::compareTo).get();
	}
	
	private static Date getMaxForecastDate(List<DemandData> forecastDemand) {
		return forecastDemand.stream().map(DemandData::getDemandDate).map(s ->{try { return new SimpleDateFormat("dd/MM/yyyy").parse(s); } catch (ParseException pe) {return null;}}).max(Date::compareTo).get();
	}
	
	private static Date getMinForecastDate(List<DemandData> forecastDemand) {
		return forecastDemand.stream().map(DemandData::getDemandDate).map(s ->{try {return new SimpleDateFormat("dd/MM/yyyy").parse(s);} catch (ParseException pe) {return null;}}).min(Date::compareTo).get();
	}
	
	private static List<DemandProjected> getDemandProjectedList(Prediction prediction, List<DemandData> demandsResult, DemandProjected.DemandTypeEnum type, DemandProjected.DemandSubtypeEnum demandSubtypeEnum){
		List<DemandProjected> demandProjecteds = new ArrayList<DemandProjected>();
		for (DemandData demandData : demandsResult) {
			DemandProjected d = new DemandProjected();
			d.setDemandType(type);
			d.setDemandSubtype(demandSubtypeEnum);
			d.setPart(prediction.getPart());
			d.setRemoteStation(prediction.getRemoteStation());
			try {d.setProjectedOrderDate(new SimpleDateFormat("dd/MM/yyyy").parse(demandData.getDemandDate()));} catch (ParseException e) {}
			d.setQuantity(demandData.getDemandValue());
			demandProjecteds.add(d);
		}
		return demandProjecteds;
	}
	
	private static List<ErrorProjected> getErrorProjectedList(ModelsResult modelsResult){
		ErrorProjected.ErrorSubtypeEnum errorSubtype = convertToErrorSubtype(modelsResult.getModel());
		List<ErrorProjected> errorsProjecteds = new ArrayList<ErrorProjected>();
		for (ForecastErro forecastError : modelsResult.getError()) {
			ErrorProjected e = new ErrorProjected();
			e.setErrorType(convertToErrorType(forecastError.getErrorType()));
			e.setErrorSubtype(errorSubtype);
			e.setErrorMethod(convertToErrorMethod(forecastError.getName()));
			e.setValue(forecastError.getValue());
			errorsProjecteds.add(e);
		}
		return errorsProjecteds;
	}

	

	
}