package com.wavemaker.controllers;

import com.wavemaker.models.User;
import com.wavemaker.services.impl.LeaveRequestServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;


@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("login attempt by user");
        resp.setContentType("application/json");
        System.out.println("in login");
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = req.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            User user = new User(jsonObject.getString("emailId"), jsonObject.getString("password"));
            int employeeId = LeaveRequestServiceImpl.getInstance().validateUser(user);
            if (employeeId != -1) {
                HttpSession httpSession = req.getSession(true);
                httpSession.setAttribute("email", user.getEmailId());
                httpSession.setAttribute("empId", employeeId);
                jsonObject = new JSONObject();
                jsonObject.put("message", "true");
                resp.getWriter().write(jsonObject.toString());
                logger.info("Authorized user");
            } else {
                resp.getWriter().write("{\"message\" : \"Invalid User\"}");
                logger.info("Unauthorized user");
            }
        } catch (Exception e) {
            logger.error("Exception while verifying user ", e);
        }
    }
}
