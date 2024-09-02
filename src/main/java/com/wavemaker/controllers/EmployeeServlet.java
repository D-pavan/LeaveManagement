package com.wavemaker.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavemaker.exceptions.EmployeeNotFoundException;
import com.wavemaker.models.Employee;
import com.wavemaker.services.EmployeeService;
import com.wavemaker.services.impl.EmployeeServiceImpl;
import com.wavemaker.util.DateSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/employee", "/teams"})
public class EmployeeServlet extends HttpServlet {
    private final EmployeeService employeeService = EmployeeServiceImpl.getInstance();
    private final Logger logger = LoggerFactory.getLogger(EmployeeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Attempt to fetch employee through api call");
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            String url = req.getRequestURI();
            int empId = (int) httpSession.getAttribute("empId");
            if (url.endsWith("/employee")) {
                try {

                    Employee employee = employeeService.getEmployee(empId);
                    resp.getWriter().write(employee.toString());
                    logger.info("Employee fetched through api call");

                } catch (EmployeeNotFoundException e) {
                    logger.error("Exception while fetching employee through api call", e);
                }
            } else {
                List<Employee> teamMembers = employeeService.getTeamMembers(empId);
                StringBuilder stringBuilder = new StringBuilder("{");
                int n = teamMembers.size();
                for (Employee employee : teamMembers) {
                    stringBuilder.append("\"").append(employee.getEmployeeId()).append("\":").append(employee);
                    if (n - 1 > 0) stringBuilder.append(",");
                    n--;
                }
                stringBuilder.append("}");
                resp.getWriter().write(stringBuilder.toString());
            }
        } else {
            resp.getWriter().write("{\"message\":\"Not logged in\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
            Gson gson = gsonBuilder.create();
            try {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                Employee employee = gson.fromJson(stringBuilder.toString(), Employee.class);
                employeeService.updateEmployee(employee.getEmployeeId(), employee);
                logger.info("Employee updated through api call");
            } catch (EmployeeNotFoundException e) {
                logger.info("Exception while updating employee ", e);
            } catch (Exception e) {
                logger.info("Exception while updating employee through api call", e);
            }
        } else {
            resp.getWriter().write("{\"message\":\"Not logged in\"}");

        }
    }
}
