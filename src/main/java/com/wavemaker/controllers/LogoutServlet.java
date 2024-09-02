package com.wavemaker.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Attempt to logout");
        try {
            HttpSession httpSession = req.getSession(false);
            resp.setContentType("application/json");
            if (httpSession != null) {
                httpSession.invalidate();
                resp.getWriter().write("{\"message\" : \"logged out\"}");
            } else {
                resp.getWriter().write("{\"message\" : \"not logged in\"}");
            }
            logger.info("Logout Successful");
        } catch (Exception e) {
            logger.error("Exception while logging out ", e);
        }

    }
}
