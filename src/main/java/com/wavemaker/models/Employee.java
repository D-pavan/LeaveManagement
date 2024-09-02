package com.wavemaker.models;

import org.json.JSONObject;

import java.sql.Date;
import java.util.Objects;

public class Employee {
    private int employeeId;
    private String employeeName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String gender;
    private int managerId;

    public Employee(int employeeId, String employeeName, Date dateOfBirth, String email, String phoneNumber,
                    int managerId, String gender) {
        this.employeeName = employeeName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.managerId = managerId;
        this.gender = gender;
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getEmployeeName() == employee.getEmployeeName() && Objects.equals(getEmail(), employee.getEmail()) && Objects.equals(getPhoneNumber(), employee.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeName(), getEmail(), getPhoneNumber());
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("employeeId", employeeId);
        jsonObject.put("employeeName", employeeName);
        jsonObject.put("dateOfBirth", dateOfBirth != null ? dateOfBirth.toString() : null);
        jsonObject.put("email", email);
        jsonObject.put("phoneNumber", phoneNumber);
        jsonObject.put("managerId", managerId);
        jsonObject.put("gender", gender);

        return jsonObject.toString();
    }

}
