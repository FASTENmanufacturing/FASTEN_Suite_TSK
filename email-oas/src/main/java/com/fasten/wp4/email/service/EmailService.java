package com.fasten.wp4.email.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
//Implementation of sending Email with Spring 
public class EmailService{
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private JavaMailSenderImpl mailSender;
	
	public void sendWithSimpleMessage(String to, String subject, String content) throws MailException {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content);
			mailSender.send(message);
		} catch (MailException e) {
			LOGGER.error("MailError", e);
			throw e;
		}
	}

	public void sendWithMimeMessagePreparator(String to, String subject, String content) throws MailException{

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
				mimeMessage.setFrom(	new InternetAddress(mailSender.getUsername()));
				mimeMessage.setSubject(subject);
				mimeMessage.setText(content);
			}

		};

		try {
			mailSender.send(preparator);
		} catch (MailException e) {
			LOGGER.error("MailError", e);
			throw e;
		}
	}


	public void sendWithMimeMessageHelper(String to, String subject, String content, Boolean isHTML) throws MailException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,isHTML);
			mailSender.send(message);

		} catch (MailException | MessagingException e) {
			LOGGER.error("MailError", e);
			throw e;
		}

	}

	public void sendWithFreemarker(String to, String subject, String content) throws MessagingException, IOException,TemplateException {

		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());



			Template template = freemarkerConfig.getTemplate("single-column/build.ftl");
			Map<String, Object> model = new HashMap<String, Object>();
			
			model.put("subtitle", subject);
			model.put("content", content);
			model.put("system", "FPSOT - FASTEN Predictive Simulator-Optimizer Tool");
			model.put("systemUrl", "http://www.fastenmanufacturing.eu");
			model.put("companyName", "INESC TEC"); 
			model.put("companyAddress", "Flexible and Autonomous Manufacturing Systems for Custom-Designed Products. "
					+ "This project has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement 777096");
			model.put("companyUrl", "https://www.inesctec.pt");
			
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

			helper.setTo(to);
			helper.setText(html, true);
			helper.addInline("logo", new ClassPathResource("logo.png"));
			helper.setSubject(subject);
			helper.setFrom(mailSender.getUsername());

			mailSender.send(message);
		} catch (MessagingException | IOException | TemplateException e) {
			LOGGER.error("MailError", e);
			throw e;
		}

	}




}
