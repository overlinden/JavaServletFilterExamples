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
public class EncodingFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    EncodingFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public EncodingFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn(".*gzip.*");
        
        filter = new EncodingFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidEncoding() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Accept-Encoding")).thenReturn("gzip, deflate");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidUserAgent() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Accept-Encoding")).thenReturn("");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Your encoding was rejected");
    }
}
