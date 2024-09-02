package com.wavemaker.services.impl;

import com.wavemaker.models.Leave;
import com.wavemaker.models.LeaveDTO;
import com.wavemaker.models.User;
import com.wavemaker.repository.LeaveRequestRepository;
import com.wavemaker.repository.impl.LeaveRequestRepositoryImpl;
import com.wavemaker.services.LeaveRequest;

import java.util.List;

public class LeaveRequestServiceImpl implements LeaveRequest {
    private static LeaveRequestServiceImpl leaveRequestServiceImplementation;
    private final LeaveRequestRepository leaveRequestRepository;

    private LeaveRequestServiceImpl() {
        leaveRequestRepository = LeaveRequestRepositoryImpl.getInstance();
    }

    public static synchronized LeaveRequestServiceImpl getInstance() {
        if (leaveRequestServiceImplementation == null) {
            leaveRequestServiceImplementation = new LeaveRequestServiceImpl();
        }
        return leaveRequestServiceImplementation;

    }

    @Override
    public List<LeaveDTO> getLeaveRequestsForManger(int managerId) {
        return leaveRequestRepository.getLeaveRequestsForManger(managerId);
    }

    @Override
    public List<Leave> getAppliedLeavesForEmployee(int employeeId) {
        return leaveRequestRepository.getAppliedLeavesForEmployee(employeeId);
    }

    @Override
    public void applyLeave(Leave leave) {
        leaveRequestRepository.applyLeave(leave);
    }

    @Override
    public void updateLeave(int employeeId, String status) {
        leaveRequestRepository.updateLeave(employeeId, status);
    }

    @Override
    public List<LeaveDTO> getLeavesApprovedOrRejected(int managerId) {
        return leaveRequestRepository.getLeavesApprovedOrRejected(managerId);
    }

    @Override
    public int validateUser(User user) {
        return leaveRequestRepository.validateUser(user);
    }

    @Override
    public int getAnnualLeaves(String leaveType) {
        return leaveRequestRepository.getAnnualLeaves(leaveType);
    }

    @Override
    public int getAppliedLeavesCount(int empId, String leaveType) {
        return leaveRequestRepository.getAppliedLeavesCount(empId, leaveType);
    }

    @Override
    public List<Leave> getFilteredLeaves(int empId, String status) {
        return leaveRequestRepository.getFilteredLeaves(empId, status);
    }

}
