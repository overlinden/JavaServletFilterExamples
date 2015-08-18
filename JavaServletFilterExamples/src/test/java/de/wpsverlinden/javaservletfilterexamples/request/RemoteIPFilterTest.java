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
public class RemoteIPFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    RemoteIPFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public RemoteIPFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn("192.168.1.0/24");
        
        filter = new RemoteIPFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidRemoteIP() throws IOException, ServletException {
        when(httpServletRequest.getRemoteAddr()).thenReturn("192.168.1.20");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidRemoteIP() throws IOException, ServletException {
        when(httpServletRequest.getRemoteAddr()).thenReturn("192.168.2.20");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Your source IP address was rejected");
    }
}
