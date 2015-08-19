package de.wpsverlinden.javaservletfilterexamples.response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Oliver Verlinden
 * 
 * Example web.xml configuration:
 * ...
 * <filter>
 *      <filter-name>StatusFilter</filter-name>
 *      <filter-class>de.wpsverlinden.javaservletfilterexamples.request.StatusFilter</filter-class>
 *      <init-param>
 *          <param-name>allowed</param-name>
 *          <param-value>200,220</param-value>
 *      </init-param>
 *  </filter>
 * ...
 */
public class StatusFilter implements Filter {

    private List<String> allowed;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowed = Arrays.asList(filterConfig.getInitParameter("allowed").split(","));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        chain.doFilter(request, response);
        
        if (!allowed.contains(String.valueOf(res.getStatus()))) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Response dropped because of not allowed status code '" + res.getStatus() + "'");
        }
    }

    @Override
    public void destroy() {
    }
}
