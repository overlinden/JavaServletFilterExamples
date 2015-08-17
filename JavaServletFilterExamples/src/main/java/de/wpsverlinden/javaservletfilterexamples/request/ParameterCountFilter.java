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
 *      <filter-name>ParameterCountFilter</filter-name>
 *      <filter-class>de.wpsverlinden.javaservletfilterexamples.request.ParameterCountFilterTest</filter-class>
 *      <init-param>
 *          <param-name>min</param-name>
 *          <param-value>1</param-value>
 *      </init-param>
 *      <init-param>
 *          <param-name>max</param-name>
 *          <param-value>5</param-value>
 *      </init-param>
 *  </filter>
 * ...
 */
public class ParameterCountFilter implements Filter {

    private int min;
    private int max;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        min = Integer.parseInt(filterConfig.getInitParameter("min"));
        max = Integer.parseInt(filterConfig.getInitParameter("max"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        Enumeration<String> parameterNames = req.getParameterNames();
        int c = 0;
        while (parameterNames.hasMoreElements()) {
            c++;
            parameterNames.nextElement();
        }
        
        if (c < min || c > max) {
            res.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Request parameters outside of allowed range [" + min + "-" + max + "]");
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
