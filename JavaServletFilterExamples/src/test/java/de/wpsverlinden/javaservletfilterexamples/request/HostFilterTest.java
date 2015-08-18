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
public class HostFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    HostFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public HostFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn("test.wps-verlinden.de");
        
        filter = new HostFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidHost() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Host")).thenReturn("test.wps-verlinden.de");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidHost() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Host")).thenReturn("wps-verlinden.de");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "The host you requested was rejected");
    }
}
