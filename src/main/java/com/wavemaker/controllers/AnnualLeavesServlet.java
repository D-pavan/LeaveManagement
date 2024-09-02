package com.wavemaker.controllers;

import com.wavemaker.services.LeaveRequest;
import com.wavemaker.services.impl.LeaveRequestServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = "/annualLeaves")
public class AnnualLeavesServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(AnnualLeavesServlet.class);
    private final LeaveRequest leaveRequest = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to fetch annual leaves through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sick", leaveRequest.getAnnualLeaves("SICK"));
                jsonObject.put("personalTimeOff", leaveRequest.getAnnualLeaves("PERSONAL TIME OFF"));
                jsonObject.put("lossOfPay", leaveRequest.getAnnualLeaves("LOSS OF PAY"));
                jsonObject.put("maternity", leaveRequest.getAnnualLeaves("MATERNITY"));
                jsonObject.put("paternity", leaveRequest.getAnnualLeaves("PATERNITY"));
                resp.getWriter().write(jsonObject.toString());

                logger.info("Annual leaves fetched through api call");
            } catch (JSONException e) {
                logger.error("Exception while putting into json object ");
            }
        } else {
            resp.getWriter().write("{\"message\":\"not logged in\"}");
            logger.info("not logged in");
        }
    }
}
