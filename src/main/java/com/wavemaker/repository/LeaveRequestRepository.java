package com.wavemaker.repository;

import com.wavemaker.models.Leave;
import com.wavemaker.models.LeaveDTO;
import com.wavemaker.models.User;

import java.util.List;

public interface LeaveRequestRepository {
    void applyLeave(Leave leave);

    void updateLeave(int employeeId, String status);

    List<LeaveDTO> getLeaveRequestsForManger(int managerId);

    List<Leave> getAppliedLeavesForEmployee(int employeeId);

    List<LeaveDTO> getLeavesApprovedOrRejected(int managerId);

    int getAppliedLeavesCount(int empId, String leaveType);

    List<Leave> getFilteredLeaves(int employeeId, String status);

    int getAnnualLeaves(String leaveType);

    int validateUser(User user);


}
