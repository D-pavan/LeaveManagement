package com.wavemaker.models;

import org.json.JSONObject;

import java.sql.Date;
import java.util.Objects;

public class Leave {
    private  int employeeId,leaveId;
    private String leaveType,reason,status;
    private Date fromDate,toDate,appliedDate;


    public Leave(int leaveId,int employeeId, String leaveType, Date fromDate, Date toDate,String reason, Date appliedDate, String status) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.reason = reason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.appliedDate = appliedDate;
        this.status=status;
        this.leaveId=leaveId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Date appliedDate) {
        this.appliedDate = appliedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leave that = (Leave) o;
        return getEmployeeId() == that.getEmployeeId() && getLeaveId() == that.getLeaveId() && Objects.equals(getLeaveType(), that.getLeaveType()) && Objects.equals(getFromDate(), that.getFromDate()) && Objects.equals(getToDate(), that.getToDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getLeaveType());
    }

    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("leaveId",leaveId);
        jsonObject.put("employeeId",employeeId);
        jsonObject.put("leaveType",leaveType);
        jsonObject.put("reason",reason);
        jsonObject.put("status",status);
        jsonObject.put("fromDate",fromDate);
        jsonObject.put("toDate",toDate);
        jsonObject.put("appliedDate",appliedDate);
        return  jsonObject.toString();
    }
}
