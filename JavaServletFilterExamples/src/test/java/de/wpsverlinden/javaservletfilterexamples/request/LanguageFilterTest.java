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
public class LanguageFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    LanguageFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public LanguageFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn(".*en-US.*");
        
        filter = new LanguageFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidLanguage() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Accept-Language")).thenReturn("en-US");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidLanguage() throws IOException, ServletException {
        when(httpServletRequest.getHeader("Accept-Language")).thenReturn("de-DE");
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Your language was rejected");
    }
}
