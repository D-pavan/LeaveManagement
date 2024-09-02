package com.wavemaker.controllers;

import com.wavemaker.models.Leave;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(urlPatterns = "/MyLeaves")
public class MyLeaveServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(MyLeaveServlet.class);
    private final LeaveRequest leaveRequest = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to get Applied Leaves");
        resp.setContentType("application/json");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                StringBuilder stringBuilder = new StringBuilder();
                int empId = (int) httpSession.getAttribute("empId");
                String status = req.getParameter("status");
                if (status != null && !status.isEmpty()) {
                    stringBuilder.append("{");
                    List<Leave> leaves = leaveRequest.getFilteredLeaves(empId, status);
                    int n = leaves.size();
                    for (Leave leave : leaves) {
                        stringBuilder.append("\"").append(leave.getLeaveId()).append("\" :").append(leave);
                        if (n - 1 > 0) stringBuilder.append(",");
                        n--;
                    }
                    stringBuilder.append("}");
                } else {
                    stringBuilder.append("{");
                    List<Leave> leaves = leaveRequest.getAppliedLeavesForEmployee(empId);
                    int n = leaves.size();
                    for (Leave leave : leaves) {
                        stringBuilder.append("\"").append(leave.getLeaveId()).append("\" :").append(leave);
                        if (n - 1 > 0) stringBuilder.append(",");
                        n--;
                    }
                    stringBuilder.append("}");
                }
                resp.getWriter().write(stringBuilder.toString());
                logger.info("Applied Leaves fetched successfully for employee with Id :{}", empId);

            } else {
                resp.getWriter().write("{\"message\":\"Please login\"}");
                logger.info("Not Logged in");
            }
        } catch (Exception e) {
            logger.error("Exception while fetching applied leaves through api call ", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to apply leave through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            int empId = (int) httpSession.getAttribute("empId");
            try {

                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = req.getReader();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                Leave leave = getLeave(stringBuilder);
                leave.setEmployeeId(empId);
                leaveRequest.applyLeave(leave);
                resp.getWriter().write(leave.toString());
                logger.info("Leave applied successfully for employee with id {}", empId);
            } catch (Exception e) {
                logger.error("Exception while apply leave through api for employee with id : {}", empId, e);
            }
        } else {
            resp.getWriter().write("{\"message\":\"Please login\"}");
            logger.info("Not logged in");
        }
    }

    public Leave getLeave(StringBuilder stringBuilder) {
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date fromDateUtil = dateFormat.parse(jsonObject.getString("fromDate"));
            java.util.Date toDateUtil = dateFormat.parse(jsonObject.getString("toDate"));
            java.util.Date appliedDateUtil = dateFormat.parse(jsonObject.getString("appliedDate"));
            Date fromDate = new Date(fromDateUtil.getTime());
            Date toDate = new Date(toDateUtil.getTime());
            Date appliedDate = new Date(appliedDateUtil.getTime());
            int leaveId = jsonObject.getInt("leaveId");
            int employeeId = jsonObject.getInt("employeeId");
            String reason = jsonObject.getString("reason");
            String status = jsonObject.getString("status");
            String leaveType = jsonObject.getString("leaveType");

            return new Leave(leaveId, employeeId, leaveType, fromDate, toDate, reason, appliedDate, status);
        } catch (ParseException e) {
            logger.error("Exception while parsing", e);
        }
        return null;
    }

}
