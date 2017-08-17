/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.controller;

import com.peter.bl.AppBL;
import com.peter.jwt.AuthorizationLogic;
import com.peter.jwt.JWTConfigurer;
import com.peter.model.httpbody.AbstractMessage;
import com.peter.model.httpbody.LoginVM;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author peter
 */
 @CrossOrigin(origins={"https://www.peter.com"}, allowedHeaders={"Content-Type", "Accept","Authorization"},
        exposedHeaders={"Authorization"},
        methods={RequestMethod.DELETE,RequestMethod.GET,RequestMethod.OPTIONS,RequestMethod.POST}, 
        allowCredentials="false", maxAge = 4800)
 @RestController
@Profile({"production", "default"})
@ComponentScan("com.peter.bl")
@ComponentScan("com.peter.jwt")
public class RestControllersProduction {
    
    @Autowired
    private AuthorizationLogic authorizationLogic;
    
    @Autowired
    private AppBL mobileBL;
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllException(Exception ex) {
        return "There was a problem with your request";
    }
    
    private void badRequestSender(BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new Exception();
        }
    }
    
    @PostMapping("/authenticate")
    public void authorize(@Valid @RequestBody LoginVM loginVM, BindingResult result, HttpServletResponse response) throws Exception {
        if (result.hasErrors()) {
            badRequestSender(result);
            return;
        }
        try{
        String jwt = authorizationLogic.authorize(loginVM);
        response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        ResponseEntity.ok();
        } catch(Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @RequestMapping(value = "/appFired", method = RequestMethod.GET)
    public @ResponseBody AbstractMessage getVersion(HttpServletResponse response,@RequestParam(value = "appVersion", required = true) String appVersion) throws IOException, Exception {
        try {
            return mobileBL.doSomething(appVersion);
        } catch (Exception dae) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}