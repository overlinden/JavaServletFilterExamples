package de.wpsverlinden.javaservletfilterexamples.request;

import java.io.IOException;
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
public class MethodFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    MethodFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public MethodFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn("GET");
        
        filter = new MethodFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidEncoding() throws IOException, ServletException {
        when(httpServletRequest.getMethod()).thenReturn("GET");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidUserAgent() throws IOException, ServletException {
        when(httpServletRequest.getMethod()).thenReturn("PUT");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method 'PUT' not allowed");
    }
}
