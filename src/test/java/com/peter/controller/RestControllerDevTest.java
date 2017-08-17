/*
 *  TO IMPROVE: TEST ERROR HANDLING OF ALL POST CONTROLLERS
testSubscribeNewsNotifications_wrongInput
testSubscribeNewMoviesNotifications_wrongInput
testAddToPlayList_wrongInput
testRmoveFromPlayList_wrongInput
_testgetPlayListwrongInput
 */
package com.peter.controller;

import com.peter.bl.AppBL;
import com.peter.jwt.AuthorizationLogic;
import com.peter.model.httpbody.LoginVM;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.assertj.core.api.SoftAssertions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;



        
/**
 *
 * @author peter
 */
@RunWith(MockitoJUnitRunner.class)
public class RestControllerDevTest {
    
    @InjectMocks
    private final RestControllersDev restControllers;
    
    @Mock
    private AuthorizationLogic authorizationLogic;
    
    @Mock
    private AppBL mobileBL;
    
    private final MockHttpServletResponse response;
    
    private MockMvc mockMvc;
    
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJibGFja0BjYXQuY29tIiwiYXV0aCI6InVzZXIiLCJleHAiOjE0ODY3NTA3NTF9.i5VBMjij4pCj9zPFyvPmrSDz3Jxp_qm6vCR6T-O9D3-yzOVlWUiWeu-n6iP3N1qSrT3a7EhoFfU-5nKCKPyBhQ";
    
    public RestControllerDevTest() {
        restControllers= new RestControllersDev();
        response = new MockHttpServletResponse();
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restControllers).build();
    }

    /**
     * Test of badRequestSender method, of class RestControllersDev.
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    @Test
    public void testBadRequestSender() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("badRequestSender");
        MockBindingResult br = new MockBindingResult(); 
        Method badRequestSender = RestControllersDev.class.getDeclaredMethod("badRequestSender", BindingResult.class, HttpServletResponse.class);
        badRequestSender.setAccessible(true);
        badRequestSender.invoke(restControllers, br, response);
        SoftAssertions assertion = new SoftAssertions();
        assertion.assertThat(response.getErrorMessage()).isEqualTo("Error in object 'TestObjectError': codes []; arguments []; default message [there was an error]. ");
        assertion.assertThat(MockHttpServletResponse.SC_BAD_REQUEST).isEqualTo(response.getStatus());
        assertion.assertAll();
    }

    /**
     * Test of authorize method, of class RestControllersDev.
     * @throws java.lang.Exception
     */
    @Test
    public void testAuthorize() throws Exception {
        System.out.println("authorize");
        when(authorizationLogic.authorize(isA(LoginVM.class))).thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJibGFja0BjYXQuY29tIiwiYXV0aCI6InVzZXIiLCJleHAiOjE0ODY3NTA3NTF9.i5VBMjij4pCj9zPFyvPmrSDz3Jxp_qm6vCR6T-O9D3-yzOVlWUiWeu-n6iP3N1qSrT3a7EhoFfU-5nKCKPyBhQ");
        MvcResult result =
        mockMvc.perform(
                post("/authenticate")
                        .header("Content-Type", "application/json")
                        .content("{\"username\":\"black@cat.com\",\"password\":\"password\",\"rememberMe\":false}")
                        .accept("application/json")
                        .secure(true)
                    ).andReturn();
        SoftAssertions assertion = new SoftAssertions();
        assertion.assertThat(MockHttpServletResponse.SC_OK).isEqualTo(result.getResponse().getStatus());
        assertion.assertThat(result.getResponse().getHeader("Authorization")).isEqualTo(TOKEN);
        assertion.assertAll();
    }
    
    @Test
    public void testAuthorizeWrongMessageBody() throws Exception {
        System.out.println("authorizeWrongMessageBody");
        MvcResult result =
        mockMvc.perform(
                post("/authenticate")
                        .header("Content-Type", "application/json")
                        .content("{\"WRONG\":\"black@cat.com\",\"password\":\"password\",\"rememberMe\":false}")
                        .accept("application/json")
                        .secure(true)
                    ).andReturn();
        assertEquals(MockHttpServletResponse.SC_BAD_REQUEST,result.getResponse().getStatus());
    }
        
    @Test
    public void testAuthorizeMissingField() throws Exception {
        System.out.println("authorizeMissingField");
        MvcResult result =
        mockMvc.perform(
                post("/authenticate")
                        .header("Content-Type", "application/json")
                        .content("{\"password\":\"password\",\"rememberMe\":false}")
                        .accept("application/json")
                        .secure(true)
                    ).andReturn();
        assertEquals(MockHttpServletResponse.SC_BAD_REQUEST,result.getResponse().getStatus());
    }
    
     @Test
    public void testAuthorizeWrongCredentials() throws Exception {
        System.out.println("authorizeWrongCredentials");
        when(authorizationLogic.authorize(isA(LoginVM.class))).thenThrow(new Exception("Wrong credentials"));
        MvcResult result =
        mockMvc.perform(
                post("/authenticate")
                        .header("Content-Type", "application/json")
                        .content("{\"username\":\"wrong!!\",\"password\":\"wrong!!\",\"rememberMe\":false}")
                        .accept("application/json")
                        .secure(true)
                    ).andReturn();
        SoftAssertions assertion = new SoftAssertions();
        assertion.assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_UNAUTHORIZED);
        assertion.assertThat(result.getResponse().getErrorMessage()).isEqualTo("Wrong credentials");
        assertion.assertAll();
    }
    
    @Test
    public void testAuthorizeInactiveAccount() throws Exception {
        System.out.println("authorizeInactiveAccount");
        when(authorizationLogic.authorize(isA(LoginVM.class))).thenThrow(new Exception("The account is inactive"));
        MvcResult result =
        mockMvc.perform(
                post("/authenticate")
                        .header("Content-Type", "application/json")
                        .content("{\"username\":\"wrong!!\",\"password\":\"wrong!!\",\"rememberMe\":false}")
                        .accept("application/json")
                        .secure(true)
                    ).andReturn();
        SoftAssertions assertion = new SoftAssertions();
        assertion.assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_UNAUTHORIZED);
        assertion.assertThat(result.getResponse().getErrorMessage()).isEqualTo("The account is inactive");
        assertion.assertAll();
    }
    
//    @Test
//    public void testGetVersion() throws Exception {
//        System.out.println("testGetVersion");
//        when(mobileBL.doSomething(isA(String.class))).thenReturn(new MobileFeaturesError("invalid version"));
//        MvcResult result =
//        mockMvc.perform(get("/appFired")
//                        .param("appVersion", "5")
//                        .header("Content-Type", "application/json")
//                        .header("Authorization", TOKEN)
//                        .accept("application/json")
//                        .secure(true)
//                    ).andReturn();
//        SoftAssertions assertion = new SoftAssertions();
//        assertion.assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_OK);
//        assertion.assertThat(result.getResponse().getContentAsString().contains("invalid version")).isEqualTo(true);
//        assertion.assertAll();
//    }
//    
//    @Test
//    public void testGetVersionNoParameter() throws Exception {
//        System.out.println("testGetVersionNoParameter");
//        MvcResult result =
//        mockMvc.perform(get("/appFired")
//                        .header("Content-Type", "application/json")
//                        .header("Authorization", TOKEN)
//                        .accept("application/json")
//                        .secure(true)
//                    ).andReturn();
//        SoftAssertions assertion = new SoftAssertions();
//        assertion.assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_BAD_REQUEST);
//        assertion.assertAll();
//    }
    
    private static class MockBindingResult implements BindingResult {

        @Override
        public Object getTarget() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Map<String, Object> getModel() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getRawFieldValue(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public PropertyEditor findEditor(String string, Class<?> type) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public PropertyEditorRegistry getPropertyEditorRegistry() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addError(ObjectError oe) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String[] resolveMessageCodes(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String[] resolveMessageCodes(String string, String string1) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void recordSuppressedField(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String[] getSuppressedFields() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getObjectName() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setNestedPath(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getNestedPath() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void pushNestedPath(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void popNestedPath() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void reject(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void reject(String string, String string1) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void reject(String string, Object[] os, String string1) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void rejectValue(String string, String string1) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void rejectValue(String string, String string1, String string2) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void rejectValue(String string, String string1, Object[] os, String string2) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void addAllErrors(Errors errors) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasErrors() {
            return true;
        }

        @Override
        public int getErrorCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<ObjectError> getAllErrors() {
            ArrayList<ObjectError> er = new ArrayList <>();
            er.add(new ObjectError("TestObjectError", "there was an error"));
            return er;
        }

        @Override
        public boolean hasGlobalErrors() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getGlobalErrorCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<ObjectError> getGlobalErrors() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ObjectError getGlobalError() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasFieldErrors() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFieldErrorCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<FieldError> getFieldErrors() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public FieldError getFieldError() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasFieldErrors(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getFieldErrorCount(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<FieldError> getFieldErrors(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public FieldError getFieldError(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getFieldValue(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Class<?> getFieldType(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    
}
