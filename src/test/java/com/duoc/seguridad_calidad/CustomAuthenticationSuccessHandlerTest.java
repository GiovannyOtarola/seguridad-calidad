package com.duoc.seguridad_calidad;

import java.util.Arrays;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.duoc.seguridad_calidad.provider.CustomAuthenticationSuccessHandler;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomAuthenticationSuccessHandlerTest {

    private CustomAuthenticationSuccessHandler handler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        handler = new CustomAuthenticationSuccessHandler();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testOnAuthenticationSuccessAdminRole() throws Exception {
        // Given: Create an Authentication mock with the ROLE_ADMIN role
        authentication = mock(Authentication.class);
        GrantedAuthority adminRole = () -> "ROLE_ADMIN";
        
        // Use doAnswer to mock the getAuthorities method dynamically
        doAnswer(invocation -> Arrays.asList(adminRole)).when(authentication).getAuthorities();

        // When: Call the onAuthenticationSuccess method
        handler.onAuthenticationSuccess(request, response, authentication);

        // Then: Verify that the redirection is to /admin
        assertEquals("/admin", response.getRedirectedUrl());
    }

    @Test
    void testOnAuthenticationSuccessUserRole() throws Exception {
        // Given: Create an Authentication mock with the ROLE_USER role
        authentication = mock(Authentication.class);
        GrantedAuthority userRole = () -> "ROLE_USER";
        
        // Use doAnswer to mock the getAuthorities method dynamically
        doAnswer(invocation -> Arrays.asList(userRole)).when(authentication).getAuthorities();

        // When: Call the onAuthenticationSuccess method
        handler.onAuthenticationSuccess(request, response, authentication);

        // Then: Verify that the redirection is to /home
        assertEquals("/home", response.getRedirectedUrl());
    }

    @Test
    void testOnAuthenticationSuccessNoRole() throws Exception {
        // Given: Create an Authentication mock with no roles
        authentication = mock(Authentication.class);
        
        // Use doAnswer to mock the getAuthorities method to return an empty list
        doAnswer(invocation -> Arrays.asList()).when(authentication).getAuthorities();

        // When: Call the onAuthenticationSuccess method
        handler.onAuthenticationSuccess(request, response, authentication);

        // Then: Verify that the redirection is to /home (default ROLE_USER)
        assertEquals("/home", response.getRedirectedUrl());
    }
}