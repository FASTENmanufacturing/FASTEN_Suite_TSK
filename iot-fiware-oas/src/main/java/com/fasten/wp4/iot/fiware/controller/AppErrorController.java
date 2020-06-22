package com.fasten.wp4.iot.fiware.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import springfox.documentation.annotations.ApiIgnore;


// Same as BasicErrorController replaces default /error mapping to JSON, ErrorPage 404 and Throwable.class maps to here.
@RestController
public class AppErrorController implements ErrorController{

	@Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }
    
    //Done inspecting BasicErrorController
    @RequestMapping(value = "/error")
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> body = this.errorAttributes.getErrorAttributes(webRequest,false); 
        
        HttpStatus status = null;
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        
        if (statusCode != null) {try {status = HttpStatus.valueOf(statusCode);}catch (Exception ex) {}} else{status = HttpStatus.INTERNAL_SERVER_ERROR;}
        
        return new ResponseEntity<>(body, status);
    }
    
}