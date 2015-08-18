package de.wpsverlinden.javaservletfilterexamples.request;

import java.io.IOException;
import java.util.Enumeration;
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
 *      <filter-name>HostFilter</filter-name>
 *      <filter-class>de.wpsverlinden.javaservletfilterexamples.request.HostFilter</filter-class>
 *      <init-param>
 *          <param-name>allowed</param-name>
 *          <param-value>test.wps-verlinden.de</param-value>
 *      </init-param>
 *  </filter>
 * ...
 */
public class HostFilter implements Filter {

    private String allowed;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowed = filterConfig.getInitParameter("allowed");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        if (!req.getHeader("Host").equals(allowed)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "The host you requested was rejected");
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
