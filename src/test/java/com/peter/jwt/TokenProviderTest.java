/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.jwt;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import io.jsonwebtoken.*;
import java.util.ArrayList;
import java.util.Collection;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.api.SoftAssertions;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author peter
 */
@RunWith(MockitoJUnitRunner.class)
public class TokenProviderTest {

    @InjectMocks
    private final TokenProvider tokenProvider;

    @Mock
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private static final String key = "*$twm.`Y%jK?eDzBL9dnqU[aongCOOLS`[EDp2qsy#9G9]M6r4019g#Fp]jp<tk";

    public TokenProviderTest() {
        tokenProvider = new TokenProvider();
    }

    @Before
    public void setUp() {
        tokenProvider.init();
    }

    @Test
    public void createTokenTest() {
        System.out.println("createTokenTest");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new User("test@testington.com", "London", authorities), "", null));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "pass");

        Jwt<Header, Claims> res = Jwts.parser().setSigningKey(key).parse(tokenProvider.createToken(authenticationToken, Boolean.TRUE));
        SoftAssertions assertion = new SoftAssertions();
        assertion.assertThat(res.getBody().getSubject().equals("test@testington.com"));
        assertion.assertThat(res.getBody().getIssuer().equals("www.peter.com"));
        assertion.assertAll();
    }
    
    @Test
    public void getAuthenticationTest() throws Exception {
        System.out.println("getAuthenticationTest");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        when(inMemoryUserDetailsManager.loadUserByUsername(isA(String.class))).thenReturn(new User("test@testington.com", "London", authorities));
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) tokenProvider.getAuthentication("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3d3cucGV0ZXIuY29tIiwic3ViIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsIm5pY2tuYW1lIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsImF1ZCI6InVzZXIiLCJleHAiOjE1MDQ2MjIzODMsImlhdCI6MTUwMjQ3NDg5OX0.rndaJZm03mjsPIMpjiX6JKAIDPsKyEV2l5CEnJE6Es2FJOWH8XUhiG2R4VQ1uqkjPMesdlKEh7r6dgS-BhsddA");
        assertTrue(((User)authenticationToken.getPrincipal()).getPassword().equals("London"));
    }
    
    @Test(expected=Exception.class)
    public void getAuthenticationTestInactiveUser() throws Exception {
        System.out.println("getAuthenticationTestInactiveUser");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        when(inMemoryUserDetailsManager.loadUserByUsername(isA(String.class))).thenReturn(new User("test@testington.com", "London", false, false, false, false, authorities));
        tokenProvider.getAuthentication("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3d3cucGV0ZXIuY29tIiwic3ViIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsIm5pY2tuYW1lIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsImF1ZCI6InVzZXIiLCJleHAiOjE1MDQ2MjIzODMsImlhdCI6MTUwMjQ3NDg5OX0.rndaJZm03mjsPIMpjiX6JKAIDPsKyEV2l5CEnJE6Es2FJOWH8XUhiG2R4VQ1uqkjPMesdlKEh7r6dgS-BhsddA");
    }
    
    @Test
    public void validateTokenTest() throws Exception {
        System.out.println("validateTokenTest");
        assertTrue(tokenProvider.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3d3cucGV0ZXIuY29tIiwic3ViIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsIm5pY2tuYW1lIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsImF1ZCI6InVzZXIiLCJleHAiOjE1MDQ2MjIzODMsImlhdCI6MTUwMjQ3NDg5OX0.rndaJZm03mjsPIMpjiX6JKAIDPsKyEV2l5CEnJE6Es2FJOWH8XUhiG2R4VQ1uqkjPMesdlKEh7r6dgS-BhsddA"));
    }
    
    @Test
    public void validateTokenTestWrongToken() throws Exception {
        System.out.println("validateTokenTestWrongToken");
        assertFalse(tokenProvider.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3d3cucGV0ZXIuY29tIiwic3ViIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsIm5pY2tuYW1lIjoidGVzdEB0ZXN0aW5ndG9uLmNvbSIsImF1ZCI6InVzZXIiLCJleHAiOjE1MDQ2MjIzODMsImlhdCI6MTUwMjQ3NDg5OX0.rndaJZm03mjs098pjiX6JKAIDPsKyEV2l5CEnJE6Es2FJOWH8XUhiG2R4VQ1uqkjPMesd4KEhQr6dgS-BhsddA"));
    }
}
