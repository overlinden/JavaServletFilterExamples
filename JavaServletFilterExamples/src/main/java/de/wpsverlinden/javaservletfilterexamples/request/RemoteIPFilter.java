package de.wpsverlinden.javaservletfilterexamples.request;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
 * Example web.xml configuration: ...
 * <filter>
 * <filter-name>RemoteIPFilter</filter-name>
 * <filter-class>de.wpsverlinden.javaservletfilterexamples.request.RemoteIPFilter</filter-class>
 * <init-param>
 * <param-name>allowed</param-name>
 * <param-value>192.168.1.0/24</param-value>
 * </init-param>
 * </filter>
 * ...
 */
public class RemoteIPFilter implements Filter {

    private String allowed;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowed = filterConfig.getInitParameter("allowed");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!netMatch(allowed, req.getRemoteAddr())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Your source IP address was rejected");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean netMatch(String subnet, String hostIP) {

        String[] parts = subnet.split("/");
        String subnetIP = parts[0];
        int prefix;
        byte[] b;
        byte[] b1;

        if (parts.length < 2) {
            prefix = 0;
        } else {
            prefix = Integer.parseInt(parts[1]);
        }

        try {
            Inet4Address a = (Inet4Address) InetAddress.getByName(subnetIP);
            Inet4Address a1 = (Inet4Address) InetAddress.getByName(hostIP);
            
            b = a.getAddress();
            int ipInt = ((b[0] & 0xFF) << 24)
                    | ((b[1] & 0xFF) << 16)
                    | ((b[2] & 0xFF) << 8)
                    | ((b[3] & 0xFF) << 0);

            b1 = a1.getAddress();
            int ipInt1 = ((b1[0] & 0xFF) << 24)
                    | ((b1[1] & 0xFF) << 16)
                    | ((b1[2] & 0xFF) << 8)
                    | ((b1[3] & 0xFF) << 0);

            int mask = ~((1 << (32 - prefix)) - 1);

            return (ipInt & mask) == (ipInt1 & mask);
        } catch (UnknownHostException e) {
            return false;
        }
    }

}
