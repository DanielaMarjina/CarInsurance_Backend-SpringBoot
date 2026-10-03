package com.danielamarjina.carinsurance.config;

import com.danielamarjina.carinsurance.service.CustomUserDetailsService;
import com.danielamarjina.carinsurance.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class JWTAuthenticationFilterTest {
    @Mock
    private JWTService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        mocks.close();
    }

    @Test
    void shouldContinueFilterChainWhenAuthHeaderIsMissing() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        jwtAuthenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        verify(jwtService, never()).extractEmail(any());
    }

    @Test
    void shouldContinueFilterChainWhenAuthHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "abc");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        jwtAuthenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        verify(filterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        verify(jwtService, never()).extractEmail(any());
    }

    @Test
    void shouldAuthenticateUserWhenTokenIsValid() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("john@test.com")
                .password("encoded-password")
                .authorities("ROLE_EMPLOYEE")
                .build();
        when(jwtService.extractEmail("valid-token"))
                .thenReturn("john@test.com");
        when(userDetailsService.loadUserByUsername("john@test.com"))
                .thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-token", userDetails))
                .thenReturn(true);
        jwtAuthenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("john@test.com", authentication.getName());
        assertTrue(authentication.getAuthorities().stream().
                anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_EMPLOYEE")));
        verify(filterChain).doFilter(mockHttpServletRequest,mockHttpServletResponse);
    }

    @Test
    void shouldNotAuthenticateUserWhenTokenIsNotValid() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("john@test.com")
                .password("encoded-password")
                .authorities("ROLE_EMPLOYEE")
                .build();
        when(jwtService.extractEmail("invalid-token"))
                .thenReturn("john@test.com");
        when(userDetailsService.loadUserByUsername("john@test.com"))
                .thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid-token", userDetails))
                .thenReturn(false);
        jwtAuthenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        verify(filterChain).doFilter(mockHttpServletRequest,mockHttpServletResponse);
    }
}