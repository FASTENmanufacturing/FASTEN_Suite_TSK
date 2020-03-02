package com.fasten.wp4.email.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasten.wp4.email.validation.ValidEmail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

// Email model to validate JSON
@ApiModel(description = "Email model")
public class Email {
    
	@ValidEmail
    @NotNull
    @Size(min = 1, message = "Please, set an email address to send the message to it")
	@ApiModelProperty(notes = "Destinatary validated email", example="filipe.marinho.brito@gmail.com")
    private String to;
	
	@ApiModelProperty(notes = "The message subject", example="Sent from Swagger")
    private String subject;
    
	@ApiModelProperty(notes = "The message body", example="Test mail from swagger")
    private String content;
    
	@ApiModelProperty(notes = "Flag if message is formated in html or in simple text", example="true")
    private Boolean html;

    public Email() {
	}
	
	public Email(String to, String subject, String content) {
		super();
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
	
	public Email(String to, String subject, String content, Boolean html) {
		super();
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.html = html;
	}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

}
