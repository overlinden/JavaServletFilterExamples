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
 *      <filter-name>UserAgentFilter</filter-name>
 *      <filter-class>de.wpsverlinden.javaservletfilterexamples.request.UserAgentFilter</filter-class>
 *      <init-param>
 *          <param-name>allowed</param-name>
 *          <param-value>.*Firefox.*</param-value>
 *      </init-param>
 *  </filter>
 * ...
 */
public class UserAgentFilter implements Filter {

    private String allowed;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowed = filterConfig.getInitParameter("allowed");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        if (!req.getHeader("User-Agent").matches(allowed)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Your user agent was rejected");
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
