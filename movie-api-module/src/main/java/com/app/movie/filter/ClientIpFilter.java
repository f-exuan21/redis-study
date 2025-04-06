package com.app.movie.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class ClientIpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {

            HttpServletRequest request = (HttpServletRequest) servletRequest;

            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            } else {
                ip = ip.split(",")[0];
            }

            ClientIpHolder.setClientIp(ip);

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            ClientIpHolder.removeClientIp();
        }


    }
}
