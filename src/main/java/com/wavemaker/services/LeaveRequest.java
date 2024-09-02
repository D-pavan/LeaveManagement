package com.wavemaker.services;

import com.wavemaker.models.Leave;
import com.wavemaker.models.LeaveDTO;
import com.wavemaker.models.User;

import java.util.List;

public interface LeaveRequest {
    void applyLeave(Leave leave);

    void updateLeave(int employeeId, String status);

    List<LeaveDTO> getLeaveRequestsForManger(int managerId);

    List<Leave> getAppliedLeavesForEmployee(int employeeId);


    List<Leave> getFilteredLeaves(int empId, String status);

    int getAppliedLeavesCount(int empId, String leaveType);

    int getAnnualLeaves(String leaveType);

    int validateUser(User user);

    List<LeaveDTO> getLeavesApprovedOrRejected(int managerId);
}