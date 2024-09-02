package com.wavemaker.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavemaker.util.DateSerializer;

import java.sql.Date;
import java.util.Objects;

public class EmployeeDTO extends Employee {
    public String managerName;
    public String managerEmail;

    public EmployeeDTO(int employeeId, String employeeName, Date dateOfBirth, String email, String phoneNumber,
                       int managerId, String gender, String managerName, String managerEmail) {
        super(employeeId, employeeName, dateOfBirth, email, phoneNumber, managerId, gender);
        this.managerName = managerName;
        this.managerEmail = managerEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(getManagerName(), that.getManagerName()) && Objects.equals(getManagerEmail(), that.getManagerEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getManagerName(), getManagerEmail());
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
