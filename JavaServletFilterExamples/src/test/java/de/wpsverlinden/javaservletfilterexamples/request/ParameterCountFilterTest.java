package de.wpsverlinden.javaservletfilterexamples.request;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Oliver Verlinden
 */
public class ParameterCountFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    ParameterCountFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public ParameterCountFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("min")).thenReturn("1");
        when(filterConfig.getInitParameter("max")).thenReturn("1");
        
        filter = new ParameterCountFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsCorrectNumberOfParameters() throws IOException, ServletException {
        when(httpServletRequest.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList("one")));
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesLesserThenMinimumParameters() throws IOException, ServletException {
        when(httpServletRequest.getParameterNames()).thenReturn(Collections.enumeration(Collections.EMPTY_LIST));
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Request parameters outside of allowed range [1-1]");
    }
    
    @Test
    public void filterDeclinesMoreThenMaximumParameters() throws IOException, ServletException {
        when(httpServletRequest.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList("ont", "two", "three")));
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Request parameters outside of allowed range [1-1]");
    }
}
