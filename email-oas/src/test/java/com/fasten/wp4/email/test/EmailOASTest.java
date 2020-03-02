package com.fasten.wp4.email.test;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasten.wp4.email.model.Email;
import com.fasten.wp4.email.service.EmailService;

import freemarker.template.TemplateException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailOASTest {

	@Autowired
	public EmailService emailService; 
	
	@Test
	public void sendMail() throws MessagingException, IOException, TemplateException {
		Email email = new Email();
		email.setContent("Test of content");
		email.setHtml(false);
		email.setSubject("Test of subject");
		email.setTo("filipe.marinho.brito@gmail.com");

		if(email.getHtml()) {
			emailService.sendWithFreemarker(email.getTo(), email.getSubject(), email.getContent());
		}else {
			emailService.sendWithSimpleMessage(email.getTo(), email.getSubject(), email.getContent());
		}
	}

}
