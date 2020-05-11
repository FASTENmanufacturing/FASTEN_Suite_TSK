package com.fasten.wp4.iot.kafka.consumer;

import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasten.wp4.database.model.Part;
import com.fasten.wp4.database.model.ProcessingPart;
import com.fasten.wp4.database.model.SRAM;
import com.fasten.wp4.database.model.SRAMCapabilities;
import com.fasten.wp4.database.model.SRAMEnviromentalInfo;
import com.fasten.wp4.database.model.SRAMProcessStatus;
import com.fasten.wp4.database.model.SRAMStatus;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.repository.SRAMRepository;
import com.fasten.wp4.iot.kafka.model.TopicSRAM;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SRAMEventConsumer {//implements ConsumerSeekAware{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private SRAMRepository sramRepository;

	@Autowired
	private PartRepository partRepository;

	@Autowired
	private ProcessingPartRepository processingPartRepository;

	@KafkaListener(id="status-topic_listner",topics = "status-topic")
	public void receiveStatusTopic(ConsumerRecord<Object, String> record, @Payload(required=false) String json, @SuppressWarnings("rawtypes") @Headers Map headers, Acknowledgment ack) throws Exception {
		LOGGER.info("status-topic, offset:" +record.offset());
		TopicSRAM topicSRAM = mapper.readValue(json,new TypeReference<TopicSRAM>() {});
//		LOGGER.info("Importing producing resources info - SRAMS Models/Capacities");
		importar(topicSRAM);
//		LOGGER.info("End of Import data");
		ack.acknowledge();
	}

	@KafkaListener(id="status-topic2_listner", topics = "status-topic2")
	public void receiveStatusTopic2(ConsumerRecord<Object, String> requestObj, @Payload(required=false) String json, @SuppressWarnings("rawtypes") @Headers Map headers, Acknowledgment ack) throws Exception {
		ConsumerRecord<Object, String> record = (ConsumerRecord<Object, String>) requestObj;
		LOGGER.info("status-topic2, offset:" +record.offset());
		TopicSRAM topicSRAM = mapper.readValue(json,new TypeReference<TopicSRAM>() {});
//			LOGGER.info("Importing producing resources info - SRAMS Models/Capacities");
		importar(topicSRAM);
//			LOGGER.info("End of Import data");
		ack.acknowledge();
	}

	@Transactional
	private void importar(TopicSRAM topicSRAM) {

		Optional<SRAM> sramOpt = sramRepository.findByCode(topicSRAM.getCode());
		SRAM sram=null;
		if(!sramOpt.isPresent()) {
			sram=new SRAM();
		}else {
			sram=sramOpt.get();
		}

		sram.setCode(topicSRAM.getCode());
		sram.setPrintTime(topicSRAM.getProcessStatus().getProgress().getPrintTime());
		sram.setPrintTimeLeft(topicSRAM.getProcessStatus().getProgress().getPrintTimeLeft());
		sram.setPrintTimeLeftOrigin(topicSRAM.getProcessStatus().getProgress().getPrintTimeLeftOrigin());
		sram.setProgressCompletion(topicSRAM.getProcessStatus().getProgress().getCompletion());

		SRAMEnviromentalInfo sramEnviromentalInfo = new SRAMEnviromentalInfo();
		sramEnviromentalInfo.setHumidity(topicSRAM.getEnviromentalInfo().getHumidity());
		sramEnviromentalInfo.setTemperature(topicSRAM.getEnviromentalInfo().getTemperature());
		sram.setEnviromentalInfo(sramEnviromentalInfo);

		if(topicSRAM.getProcessStatus()!=null &&
				topicSRAM.getProcessStatus().getJob()!=null &&
				topicSRAM.getProcessStatus().getJob().getFilament()!=null &&
				(topicSRAM.getProcessStatus().getJob().getFilament().getVolume()!=null || topicSRAM.getProcessStatus().getJob().getFilament().getLength()!=null)) {
			SRAMCapabilities sramCapabilities = new SRAMCapabilities();
			if(topicSRAM.getProcessStatus().getJob().getFilament().getVolume()!=null ) {
				sramCapabilities.setVolume(topicSRAM.getProcessStatus().getJob().getFilament().getVolume());
			}
			if(topicSRAM.getProcessStatus().getJob().getFilament().getLength()!=null) {
				sramCapabilities.setLenght(topicSRAM.getProcessStatus().getJob().getFilament().getLength());
			}
			sram.setCapabilities(sramCapabilities);
		}

		if(topicSRAM.getProcessStatus().getJob().getFile().getName()!=null) {
			Optional<Part> partOptional = partRepository.findByLayout(topicSRAM.getProcessStatus().getJob().getFile().getName());
			if(partOptional.isPresent()) {
				Part part = partOptional.get();
				Optional<ProcessingPart> processingPartOptional = processingPartRepository.findByPartCodeAndSRAMCode(part.getCode(), sram.getCode());
				ProcessingPart processingPart=null;
				if(processingPartOptional.isPresent()) {
					processingPart=processingPartOptional.get();
					processingPart.setAveragePrintTime(topicSRAM.getProcessStatus().getJob().getAveragePrintTime());
					processingPart.setEstimatedPrintTime(topicSRAM.getProcessStatus().getJob().getEstimatedPrintTime());
					processingPart.setLastPrintTime(topicSRAM.getProcessStatus().getJob().getLastPrintTime());
					processingPart.setSRAM(sram);
					processingPart.setPart(part);
				}else {
					processingPart= new ProcessingPart();
					processingPart.setAveragePrintTime(topicSRAM.getProcessStatus().getJob().getAveragePrintTime());
					processingPart.setEstimatedPrintTime(topicSRAM.getProcessStatus().getJob().getEstimatedPrintTime());
					processingPart.setLastPrintTime(topicSRAM.getProcessStatus().getJob().getLastPrintTime());
					processingPart.setSRAM(sram);
					processingPart.setPart(part);
				}
				processingPartRepository.save(processingPart);
			}
		}

		//From http://docs.octoprint.org/en/master/api/job.html
		//"Operational", "Printing", "Pausing", "Paused", "Cancelling", "Error", "Offline", "Finishing", ""

		switch(topicSRAM.getProcessStatus().getState()) {
		case "Operational":
			sram.setProcessStatus(SRAMProcessStatus.Standby);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Printing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Pausing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Paused":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Cancelling":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Finishing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Error":
			sram.setProcessStatus(SRAMProcessStatus.Error);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Offline":
			sram.setProcessStatus(SRAMProcessStatus.NoInfo);
			sram.setStatus(SRAMStatus.Offline);
			break;
		}

		//Temp: Não pode ser menor que 30 graus
		//Umidade: Entre 40-50 %
		if(!(sramEnviromentalInfo.getTemperature().compareTo(30d)>=0 && 
				(sramEnviromentalInfo.getHumidity().compareTo(40d)>=0 && sramEnviromentalInfo.getHumidity().compareTo(50d)<=0)
				)) {
			sram.setProcessStatus(SRAMProcessStatus.NotReady);
		}

		sramRepository.save(sram);
	}

//	@Override
//	public void registerSeekCallback(ConsumerSeekCallback callback) {}
//
//	@Override
//	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//		assignments.keySet().stream()
//		.filter(partition -> "status-topic".equals(partition.topic())||"status-topic2".equals(partition.topic()))
//		.forEach(partition -> callback.seekToBeginning(partition.topic(), partition.partition()));
//	}
//
//	@Override
//	public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {}

}
