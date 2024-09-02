package com.wavemaker.exceptions;

public class EmployeeNotFoundException extends  Exception{
    public EmployeeNotFoundException(String message){
        super(message);
    }
}
