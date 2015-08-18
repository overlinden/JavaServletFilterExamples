package de.wpsverlinden.javaservletfilterexamples.response;



import de.wpsverlinden.javaservletfilterexamples.request.*;
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
public class StatusFilterTest {

    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;
    StatusFilter filter;
    FilterChain filterChain;
    FilterConfig filterConfig;

    public StatusFilterTest() {
    }

    @Before
    public void setUp() throws Exception {
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        when(filterConfig.getInitParameter("allowed")).thenReturn("GET");
        
        filter = new StatusFilter();
        filter.init(filterConfig);
    }

    @Test
    public void filterAcceptsValidStatus() throws IOException, ServletException {
        when(httpServletResponse.getStatus()).thenReturn(200);
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
    
    @Test
    public void filterDeclinesInvalidStatus() throws IOException, ServletException {
        when(httpServletResponse.getStatus()).thenReturn(404);
        
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Response dropped because of not allowed status code '404'");
    }
}
