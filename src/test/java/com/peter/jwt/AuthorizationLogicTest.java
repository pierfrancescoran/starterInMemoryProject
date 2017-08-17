/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.jwt;

import com.peter.model.httpbody.LoginVM;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @author peter
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationLogicTest {

    @InjectMocks
    private final AuthorizationLogic authorizationLogic;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public AuthorizationLogicTest() {
        authorizationLogic = new AuthorizationLogic();
    }

    @Before
    public void setUp() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void authorizeTest() throws Exception {
        System.out.println("authorizeTest");
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("test@testington.com");
        loginVM.setPassword("London");
        loginVM.setRememberMe(Boolean.TRUE);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        when(inMemoryUserDetailsManager.loadUserByUsername(isA(String.class))).thenReturn(new User("test@testington.com", "London", authorities));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class))).thenReturn(authenticationToken);
        when(tokenProvider.createToken(authenticationToken, loginVM.isRememberMe())).thenReturn("token");
        String token = authorizationLogic.authorize(loginVM);
        assertFalse(token.isEmpty());
    }
    
    @Test(expected=Exception.class)
    public void authorizeTestNotEnabledUser() throws Exception {
        System.out.println("authorizeTestNotEnabledUser");
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("test@testington.com");
        loginVM.setPassword("London");
        loginVM.setRememberMe(Boolean.TRUE);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        when(inMemoryUserDetailsManager.loadUserByUsername(isA(String.class))).thenReturn(new User("test@testington.com", "London", false, false, false, false, authorities));
        authorizationLogic.authorize(loginVM);
    }
    
    @Test(expected=Exception.class)
    public void authorizeTestWrongPassword() throws Exception {
        System.out.println("authorizeTestWrongPassword");
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("test@testington.com");
        loginVM.setPassword("London");
        loginVM.setRememberMe(Boolean.TRUE);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        when(inMemoryUserDetailsManager.loadUserByUsername(isA(String.class))).thenReturn(new User("test@testington.com", "wrong", authorities));
        authorizationLogic.authorize(loginVM);
    }
}
