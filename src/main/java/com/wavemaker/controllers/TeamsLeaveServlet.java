package com.wavemaker.controllers;

import com.wavemaker.models.LeaveDTO;
import com.wavemaker.services.LeaveRequest;
import com.wavemaker.services.impl.LeaveRequestServiceImpl;
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
import java.util.List;

@WebServlet(urlPatterns = {"/TeamLeaves", "/pendingLeaves"})
public class TeamsLeaveServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(TeamsLeaveServlet.class);
    private final LeaveRequest leaveRequest = LeaveRequestServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to get Team Leaves");
        resp.setContentType("application/json");
        try {
            HttpSession httpSession = req.getSession(false);
            if (httpSession != null) {
                String url = req.getRequestURI();
                if (url.endsWith("/pendingLeaves")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int empId = (int) httpSession.getAttribute("empId");
                    stringBuilder.append("{");
                    List<LeaveDTO> leaves = leaveRequest.getLeaveRequestsForManger(empId);
                    int n = leaves.size();
                    for (LeaveDTO leave : leaves) {
                        stringBuilder.append("\"").append(leave.getLeaveId()).append("\":").append(leave);
                        if (n - 1 > 0) stringBuilder.append(",");
                        n--;
                    }
                    stringBuilder.append("}");
                    resp.getWriter().write(stringBuilder.toString());
                    logger.info("Teams leave fetched successfully for employee with id {}", empId);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    int empId = (int) httpSession.getAttribute("empId");
                    stringBuilder.append("{");
                    List<LeaveDTO> leaves = leaveRequest.getLeavesApprovedOrRejected(empId);
                    int n = leaves.size();
                    for (LeaveDTO leave : leaves) {
                        stringBuilder.append("\"").append(leave.getLeaveId()).append("\":").append(leave);
                        if (n - 1 > 0) stringBuilder.append(",");
                        n--;
                    }
                    stringBuilder.append("}");
                    resp.getWriter().write(stringBuilder.toString());
                    logger.info("Approved or Rejected leave fetched successfully for employee with id {}", empId);
                }
            } else {
                resp.getWriter().write("{\"message\":\"Please login\"}");
                logger.info("Not logged in");
            }
        } catch (Exception e) {
            logger.error("Exception while fetching team leaves or approved or rejected leaves", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("Attempt to update status through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = req.getReader();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                String status = jsonObject.getString("status");
                int leaveId = jsonObject.getInt("leaveId");
                leaveRequest.updateLeave(leaveId, status);
                logger.info("Status update through api call successfully");
            } catch (Exception e) {
                logger.error("Exception while updating status through api call", e);
            }
        } else {
            resp.getWriter().write("{\"message\":\"Please login\"}");
        }
    }

}
