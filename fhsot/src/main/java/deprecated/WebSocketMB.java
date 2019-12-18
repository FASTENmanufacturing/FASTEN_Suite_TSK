package deprecated;
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.github.adminfaces.starter.bean;
//
//import java.io.Serializable;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.push.Push;
//import javax.faces.push.PushContext;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.github.adminfaces.starter.infra.i18n.LocaleMB;
//import com.google.gson.JsonObject;
//
//@ApplicationScoped
//@Named
//public class WebSocketMB implements Serializable {
//    
//    private Logger LOGGER = LoggerFactory.getLogger(getClass());
//    
//    @Inject
//    @Push
//    PushContext kafkaOutputChannel;
//    
//    @Inject
//    LocaleMB localeMB;
//    
//    public void init() {
//    	localeMB.updateViewLocale();
//    	FacesContext context = FacesContext.getCurrentInstance();
//    	String title = context.getApplication().evaluateExpressionGet(context, "#{adm['kafka.returned.title']}",String.class);
//    	String message = context.getApplication().evaluateExpressionGet(context, "#{adm['kafka.returned.message']}",String.class);
//    	int i=0;
//    	do {
//    	setTimeout(()->{
//    		sendMessage(title, message);}, (1000* (Math.random()*10)));
//    	i++;
//    	}while(i<=3);
//    }
//    
//    public void sendMessage(String title, String message) {
//    	LOGGER.info("send push message");
//    	JsonObject json = new JsonObject();
//    	json.addProperty("title", title);
//    	json.addProperty("message", message);
//    	kafkaOutputChannel.send(json.toString());
//    }
//    
//    public static void setTimeout(Runnable runnable, double delay){
//        new Thread(() -> {
//            try {
//                Thread.sleep((int)delay);
//                runnable.run();
//            }
//            catch (Exception e){
//                System.err.println(e);
//            }
//        }).start();
//    }
//    
//}
