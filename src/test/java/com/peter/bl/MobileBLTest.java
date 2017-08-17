/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.bl;

import com.peter.model.httpbody.AbstractMessage;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author peter
 */
public class MobileBLTest {

    @InjectMocks
    private final AppBL mobileBL;

    public MobileBLTest() {
        mobileBL = new AppBL();
    }

    @Before
    public void setUp() {
    }
    
    @Test
    public void doSomething() {
        System.out.println("appFiresRightVersionRightUser");
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("user"));
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new User("test@testington.com", "London", authorities), "", null));
        AbstractMessage res = mobileBL.doSomething("6");
        assertTrue(res == null);
    }
    

}
