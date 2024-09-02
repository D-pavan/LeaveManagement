package com.wavemaker.repository.impl;

import com.wavemaker.exceptions.EmployeeNotFoundException;
import com.wavemaker.models.Employee;
import com.wavemaker.models.EmployeeDTO;
import com.wavemaker.repository.EmployeeRepository;
import com.wavemaker.util.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static EmployeeRepositoryImpl employeeRepositoryImpl;
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    private EmployeeRepositoryImpl() {
        connection = DatabaseConnector.getInstance().getConnection();
    }

    public static synchronized EmployeeRepositoryImpl getInstance() {
        if (employeeRepositoryImpl == null) {
            employeeRepositoryImpl = new EmployeeRepositoryImpl();
        }
        return employeeRepositoryImpl;
    }

    @Override
    public Employee getEmployee(int empId) throws EmployeeNotFoundException {
        logger.debug("Attempt fetch an  employee with id {}", empId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_EMPLOYEE);
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                logger.info("Employee fetched successfully");
                return new EmployeeDTO(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getDate(3), resultSet.getString(4), resultSet.getString(5),
                        resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8),
                        resultSet.getString(9));
            } else throw new EmployeeNotFoundException("No Employee found with given ID :" + empId);
        } catch (SQLException e) {
            logger.error("Exception while fetching employee", e);
        }
        return null;
    }

    @Override
    public void updateEmployee(int empId, Employee employee) throws EmployeeNotFoundException {
        logger.debug("Attempt update employee in database");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.UPDATE_EMPLOYEE);
            preparedStatement.setString(1, employee.getEmployeeName());
            preparedStatement.setDate(2, employee.getDateOfBirth());
            preparedStatement.setString(3, employee.getPhoneNumber());
            preparedStatement.setString(4, employee.getGender());
            preparedStatement.setInt(5, empId);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                logger.info("Employee updated");
            } else {
                logger.info("Employee Not found with given id {}", empId);
                throw new EmployeeNotFoundException("No employee found with given Id : " + empId);
            }
        } catch (Exception e) {
            logger.error("Exception while updating employee", e);
        }

    }

    @Override
    public List<Employee> getTeamMembers(int managerId) {
        List<Employee> employees = new ArrayList<>();
        logger.debug("Attempt to get team members");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_TEAM_MEMBERS);
            preparedStatement.setInt(1, managerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getDate(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7)));
            }
            logger.info("Team members fetched from database");
        } catch (SQLException e) {
            logger.error("Exception while fetching team members", e);
        }
        return employees;
    }
}
