package com.fasten.wp4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.fiware.controller.OrionController;
import com.fasten.wp4.orion.client.api.SubscriptionsApi;
import com.fasten.wp4.orion.client.invoker.ApiException;
import com.fasten.wp4.orion.client.model.QueryPattern;
import com.fasten.wp4.orion.client.model.Subscription;
import com.fasten.wp4.orion.client.model.Subscription.StatusEnum;
import com.fasten.wp4.orion.client.model.SubscriptionNotification;
import com.fasten.wp4.orion.client.model.SubscriptionNotification.AttrsFormatEnum;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import com.fasten.wp4.orion.client.model.SubscriptionNotificationHttp;
import com.fasten.wp4.orion.client.model.SubscriptionSubject;
import com.fasten.wp4.orion.client.model.SubscriptionSubjectConditions;

@Component
public class AfterApplicationStartup {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	SubscriptionsApi subscriptionsApi;

	@Value("${iot-fiware-oas.url}")
	private String url;

	@Value("${spring.application.name}")
	private String springApplicationName;

	@EventListener(ApplicationReadyEvent.class)
	public void doAfterStartup() throws ApiException {

		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		
		//lack of name patter, wp5 responsability 
		verifyAndCreateSubscription("printer3d");
		verifyAndCreateSubscription("3Dprinter_1_extruder");

	}

	private void verifyAndCreateSubscription(String type) throws ApiException {
		
		LOGGER.info("Verifying if exists subscription to type " +type);

		String webhookPath=url+OrionController.SRAM_PATH+"/"+type;
		String subscriptionIdentifyer = springApplicationName+" "+webhookPath;
		List<Subscription> subscriptions = subscriptionsApi.retrieveSubscriptions(null,null,null);
		Subscription existentSubscription = subscriptions.parallelStream().filter(s -> s.getDescription().contains(subscriptionIdentifyer)).findAny().orElse(null);

		if(existentSubscription==null) {
			LOGGER.info("Did not found "+subscriptionIdentifyer);

			Subscription subscription = new Subscription();
			subscription.setDescription("Created by "+subscriptionIdentifyer);
			subscription.setStatus(StatusEnum.ACTIVE);

			SubscriptionSubject subject = new SubscriptionSubject();
			List<QueryPattern> entities = new ArrayList<QueryPattern>();
			QueryPattern entity = new QueryPattern();
			entity.setIdPattern(".*");
			entity.setType(type);
			entities.add(entity);
			subject.setEntities(entities);
			SubscriptionSubjectConditions condition = new SubscriptionSubjectConditions();
			condition.setAttrs(new ArrayList<String>());
			subject.setConditions(condition);
			subscription.setSubject(subject);

			SubscriptionNotification notification = new SubscriptionNotification();
			notification.setAttrs(new ArrayList<String>());
			notification.setAttrsFormat(AttrsFormatEnum.KEYVALUES);
			notification.setOnlyChangedAttrs(false);
			notification.setMetadata(Arrays.asList("dateCreated","dateModified","TimeInstant","timestamp"));
			SubscriptionNotificationHttp http = new SubscriptionNotificationHttp();
			http.setUrl(webhookPath);
			notification.setHttp(http);
			subscription.setNotification(notification);

			subscription.setThrottling(1);
			subscriptionsApi.createANewSubscription(subscription);
			LOGGER.info("Created subscription "+subscription);
		}else {
			LOGGER.info("Subscription "+subscriptionIdentifyer+" alredy exists.");
		}
	}

}
