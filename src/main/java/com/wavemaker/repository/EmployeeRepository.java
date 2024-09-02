package com.wavemaker.repository;

import com.wavemaker.exceptions.EmployeeNotFoundException;
import com.wavemaker.models.Employee;

import java.util.List;

public interface EmployeeRepository {
    Employee getEmployee(int empId) throws EmployeeNotFoundException;

    void updateEmployee(int empId, Employee employee) throws EmployeeNotFoundException;

    List<Employee> getTeamMembers(int managerId);

}
