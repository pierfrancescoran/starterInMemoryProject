/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.jwt;

import com.peter.model.httpbody.LoginVM;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

/**
 *
 * @author peter
 */
@ComponentScan("com.peter.config")
@Component
public class AuthorizationLogic {
    
    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;
    
    public String authorize(LoginVM loginVM) throws Exception{
        User customer = (User)inMemoryUserDetailsManager.loadUserByUsername(loginVM.getUsername());
        if(!customer.isEnabled()){
            throw new Exception("The account is inactive");
        }
        if (customer.getPassword().equals(loginVM.getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

            try {
                org.springframework.security.core.Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
                return tokenProvider.createToken(authentication, rememberMe);
            } catch (AuthenticationException exception) {
//                new ResponseEntity<>(Collections.singletonMap("AuthenticationException", exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
                throw new BadCredentialsException("Wrong credentials");
            }
        }
        throw new BadCredentialsException("Wrong credentials");
    }
    
}
