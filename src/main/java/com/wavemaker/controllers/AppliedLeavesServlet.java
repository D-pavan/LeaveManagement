package com.wavemaker.controllers;

import com.wavemaker.services.LeaveRequest;
import com.wavemaker.services.impl.LeaveRequestServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/appliedLeaves")
public class AppliedLeavesServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(AppliedLeavesServlet.class);
    private final LeaveRequest leaveRequest = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to get Applied leaves through api call ");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            int empId = (int) httpSession.getAttribute("empId");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sick", leaveRequest.getAppliedLeavesCount(empId, "SICK"));
                jsonObject.put("personalTimeOff", leaveRequest.getAppliedLeavesCount(empId, "PERSONAL TIME OFF"));
                jsonObject.put("lossOfPay", leaveRequest.getAppliedLeavesCount(empId, "LOSS OF PAY"));
                jsonObject.put("maternity", leaveRequest.getAppliedLeavesCount(empId, "MATERNITY"));
                jsonObject.put("paternity", leaveRequest.getAppliedLeavesCount(empId, "PATERNITY"));
                resp.getWriter().write(jsonObject.toString());
                logger.info("Fetched applied leaves successfully");
            } catch (Exception e) {
                logger.info("Exception while putting into JSONObject", e);
            }

        } else {
            resp.getWriter().write("{\"message\":\"not logged in\"}");
            logger.info("not logged in");
        }
    }
}
