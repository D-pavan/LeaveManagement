package com.wavemaker.services.impl;

import com.wavemaker.exceptions.EmployeeNotFoundException;
import com.wavemaker.models.Employee;
import com.wavemaker.repository.EmployeeRepository;
import com.wavemaker.repository.impl.EmployeeRepositoryImpl;
import com.wavemaker.services.EmployeeService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private static EmployeeServiceImpl employeeServiceImpl;
    private final EmployeeRepository employeeRepository;

    private EmployeeServiceImpl() {
        employeeRepository = EmployeeRepositoryImpl.getInstance();
    }

    public static EmployeeServiceImpl getInstance() {
        if (employeeServiceImpl == null) {
            employeeServiceImpl = new EmployeeServiceImpl();
        }
        return employeeServiceImpl;
    }

    @Override
    public Employee getEmployee(int empId) throws EmployeeNotFoundException {
        return employeeRepository.getEmployee(empId);
    }

    @Override
    public void updateEmployee(int empId, Employee employee) throws EmployeeNotFoundException {
        employeeRepository.updateEmployee(empId, employee);
    }

    @Override
    public List<Employee> getTeamMembers(int managerId) {
        return employeeRepository.getTeamMembers(managerId);
    }
}
