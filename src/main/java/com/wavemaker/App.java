package com.wavemaker;

import com.wavemaker.models.Employee;
import com.wavemaker.models.EmployeeDTO;

import java.sql.Date;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World");


    EmployeeDTO employeeDTO = new EmployeeDTO(1, "name", new Date(System.currentTimeMillis()), "a", "32032", 9, "male", "hello", "manager@mail");
        System.out.println(employeeDTO);
    }
}
