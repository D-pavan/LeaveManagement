package com.wavemaker.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {"/dashboard.html", "/logout", "/MyLeaves", "/TeamLeaves", "/appliedLeaves", "/annualLeaves" ,"/employee","/pendingLeaves","/teams"})
public class AuthenticationFilterServlet implements Filter {
    Logger logger = LoggerFactory.getLogger(AuthenticationFilterServlet.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.debug("Attempt to filter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(false);
        String uri = httpServletRequest.getRequestURI();
        boolean loggedIn = httpSession != null && httpSession.getAttribute("email") != null;
        if (!loggedIn) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index.html");
            logger.info("Redirected");
            logger.info("url {}", uri);
        } else {
            chain.doFilter(request, response);
            logger.info("Filter passed");
        }

    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }


}
