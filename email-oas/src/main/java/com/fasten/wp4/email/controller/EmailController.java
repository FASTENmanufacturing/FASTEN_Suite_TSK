package com.fasten.wp4.email.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.email.model.Email;
import com.fasten.wp4.email.service.EmailService;

import freemarker.template.TemplateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

// https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#mail
// would create a thread safe "copy" if used a template message and customize it
@CrossOrigin
@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	//send with simple message
	@ApiOperation(value = "Send simple message", notes = "Uses SimpleMailMessage from java", authorizations = {@Authorization(value="basicAuth")})
	@RequestMapping(value = "/sendWithSimpleMessage", method = RequestMethod.POST)
	public ResponseEntity<Void> sendWithSimpleMessage(@RequestBody(required=true) @Valid Email email ) throws MessagingException, IOException, TemplateException {
		emailService.sendWithSimpleMessage(email.getTo(), email.getSubject(), email.getContent());
		return ResponseEntity.ok().build();
	}

	//send with mime message preparator
	@ApiOperation(value = "Send mime message", notes = "Uses MimeMessagePreparator from java", authorizations = {@Authorization(value="basicAuth")})
	@RequestMapping(value = "/sendWithMimeMessagePreparator", method = RequestMethod.POST)
	public ResponseEntity<Void> sendWithMimeMessagePreparator(@RequestBody(required=true) @Valid Email email ) throws MessagingException, IOException, TemplateException {
		emailService.sendWithMimeMessagePreparator(email.getTo(), email.getSubject(), email.getContent());
		return ResponseEntity.ok().build();
	}

	//send with mime message messager helper (easy)
	@ApiOperation(value = "Send mime message", notes = "Uses MimeMessageHelper from java", authorizations = {@Authorization(value="basicAuth")})
	@RequestMapping(value = "/sendWithMimeMessageHelper", method = RequestMethod.POST)
	public ResponseEntity<Void> sendWithMimeMessageHelper(@RequestBody(required=true) @Valid Email email ) throws MessagingException, IOException, TemplateException {
		emailService.sendWithMimeMessageHelper(email.getTo(), email.getSubject(), email.getContent(), (email.getHtml()!=null)?email.getHtml():false);
		return ResponseEntity.ok().build();
	}

	//send with Freemarker
	@ApiOperation(value = "Send templated message", notes = "Uses MimeMessageHelper and Template form Freemarker", authorizations = {@Authorization(value="basicAuth")})
	@RequestMapping(value = "/sendWithFreemarker", method = RequestMethod.POST)
	public ResponseEntity<Void> sendWithFreemarker(@RequestBody(required=true) @Valid Email email) throws MessagingException, IOException, TemplateException {
		emailService.sendWithFreemarker(email.getTo(), email.getSubject(), email.getContent());
		return ResponseEntity.ok().build();
	}

}
