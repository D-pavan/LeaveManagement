package com.wavemaker.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavemaker.util.DateSerializer;

import java.sql.Date;
import java.util.Objects;

public class LeaveDTO extends Leave {
    private String name;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private int leavesTaken;
    private int availableLeaves;

    public LeaveDTO(int leaveId, int employeeId, String leaveType, Date fromDate, Date toDate,
                    String reason, Date appliedDate, String status, String name, Date dateOfBirth, String email,
                    String phoneNumber, int leavesTaken, int availableLeaves) {
        super(leaveId, employeeId, leaveType, fromDate, toDate, reason, appliedDate, status);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.leavesTaken = leavesTaken;
        this.availableLeaves = availableLeaves;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvailableLeaves() {
        return availableLeaves;
    }

    public void setAvailableLeaves(int availableLeaves) {
        this.availableLeaves = availableLeaves;
    }

    public int getLeavesTaken() {
        return leavesTaken;
    }

    public void setLeavesTaken(int leavesTaken) {
        this.leavesTaken = leavesTaken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LeaveDTO leaveDTO = (LeaveDTO) o;
        return Objects.equals(getName(), leaveDTO.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
