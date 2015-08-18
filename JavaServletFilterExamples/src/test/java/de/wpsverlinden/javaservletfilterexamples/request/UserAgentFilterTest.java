package de.wpsverlinden.javaservletfilterexamples.request;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
public class UserAgentFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    UserAgentFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public UserAgentFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn(".*Firefox.*");
        
        filter = new UserAgentFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidUserAgent() throws IOException, ServletException {
        when(httpServletRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidUserAgent() throws IOException, ServletException {
        when(httpServletRequest.getHeader("User-Agent")).thenReturn("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Your user agent was rejected");
    }
}
