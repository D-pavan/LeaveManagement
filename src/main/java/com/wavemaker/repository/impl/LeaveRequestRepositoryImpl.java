package com.wavemaker.repository.impl;

import com.wavemaker.models.Leave;
import com.wavemaker.models.LeaveDTO;
import com.wavemaker.models.User;
import com.wavemaker.util.DatabaseConnector;
import com.wavemaker.repository.LeaveRequestRepository;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {
    private static LeaveRequestRepositoryImpl leaveRequestRepositoryImplementation;
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(LeaveRequestRepositoryImpl.class);

    private LeaveRequestRepositoryImpl() {
        connection = DatabaseConnector.getInstance().getConnection();
    }

    public static LeaveRequestRepositoryImpl getInstance() {
        if (leaveRequestRepositoryImplementation == null) {
            leaveRequestRepositoryImplementation = new LeaveRequestRepositoryImpl();
        }
        return leaveRequestRepositoryImplementation;
    }

    @Override
    public void applyLeave(Leave leave) {
        logger.debug("Attempt to add leave for employee with id {}", leave.getEmployeeId());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.INSERT_LEAVE);
            preparedStatement.setInt(1, leave.getEmployeeId());
            preparedStatement.setString(2, leave.getLeaveType());
            preparedStatement.setDate(3, leave.getFromDate());
            preparedStatement.setDate(4, leave.getToDate());
            preparedStatement.setString(5, leave.getReason());
            preparedStatement.setDate(6, leave.getAppliedDate());
            preparedStatement.setString(7, leave.getStatus());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                logger.info("Leave Request added successfully for employee with id {}", leave.getEmployeeId());
            preparedStatement.close();

        } catch (SQLException e) {
            logger.error("Exception while adding Leave Request for employee with id {}", leave.getEmployeeId(), e);
        }
    }

    @Override
    public void updateLeave(int leaveId, String status) {
        logger.debug("Attempt to update leave for employee with id {}", leaveId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.UPDATE_LEAVE);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, leaveId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Leave updated successfully for employee with id {}", leaveId);
            }
        } catch (SQLException e) {
            logger.error("Exception while updating leave for employee with id {}", leaveId, e);
        }
    }

    @Override
    public List<Leave> getAppliedLeavesForEmployee(int employeeId) {
        logger.debug("Attempt to get leaves for employees with id {}", employeeId);
        List<Leave> leaves = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.MY_LEAVES);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                leaves.add(new Leave(resultSet.getInt(8), resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getDate(3), resultSet.getDate(4),
                        resultSet.getString(5), resultSet.getDate(6), resultSet.getString(7)));
            }
            resultSet.close();
            preparedStatement.close();
            logger.info("Leaves fetched successfully for employee with id {}", employeeId);
        } catch (SQLException e) {
            logger.error("Exception while get Leaves for employee applied with id : {}", employeeId);
        }
        return leaves;
    }

    @Override
    public List<LeaveDTO> getLeaveRequestsForManger(int managerId) {
        logger.debug("Attempt fetch leaves to be approved by manager");
        List<LeaveDTO> leaves = new ArrayList<>();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(Queries.PENDING_TEAM_LEAVES);
            preparedStatement.setInt(1, managerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                leaves.add(new LeaveDTO(resultSet.getInt(8), resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getDate(3),
                        resultSet.getDate(4), resultSet.getString(5),
                        resultSet.getDate(6), resultSet.getString(7),
                        resultSet.getString(9), resultSet.getDate(11),
                        resultSet.getString(10), resultSet.getString(12),
                        -1, -1));
                int lastLeave = leaves.size() - 1;
                leaves.get(lastLeave).setLeavesTaken(getAppliedLeavesCount(leaves.get(lastLeave).getEmployeeId(),
                        leaves.get(lastLeave).getLeaveType()));

                leaves.get(lastLeave).setAvailableLeaves(getAnnualLeaves(leaves.get(lastLeave).getLeaveType()) - leaves.get(lastLeave).
                        getLeavesTaken());

            }
            logger.info("Leaves fetched successfully for manager with id {}", managerId);
        } catch (SQLException e) {
            logger.error("Exception while fetching leaves for manager with id{}", managerId, e);
        }

        return leaves;
    }

    @Override
    public List<LeaveDTO> getLeavesApprovedOrRejected(int managerId) {
        logger.debug("Attempt fetch leaves  approved or rejected by manager");
        List<LeaveDTO> leaves = new ArrayList<>();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(Queries.TEAM_LEAVES);
            preparedStatement.setInt(1, managerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                leaves.add(new LeaveDTO(resultSet.getInt(8), resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getDate(3),
                        resultSet.getDate(4), resultSet.getString(5),
                        resultSet.getDate(6), resultSet.getString(7),
                        resultSet.getString(9), resultSet.getDate(11),
                        resultSet.getString(10), resultSet.getString(12),
                        -1, -1));
                int lastLeave = leaves.size() - 1;
                leaves.get(lastLeave).setLeavesTaken(getAppliedLeavesCount(leaves.get(lastLeave).getEmployeeId(),
                        leaves.get(lastLeave).getLeaveType()));

                leaves.get(lastLeave).setAvailableLeaves(getAnnualLeaves(leaves.get(lastLeave).getLeaveType()) - leaves.get(lastLeave).
                        getLeavesTaken());

            }
            logger.info("Approved or Rejected leaves fetched successfully for manager with id {}", managerId);
        } catch (SQLException e) {
            logger.error("Exception while fetching leaves approved or rejected for manager with id{}", managerId, e);
        }

        return leaves;
    }

    @Override
    public int validateUser(User user) {
        int id = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.VALIDATE_USER);
            preparedStatement.setString(1, user.getEmailId());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement = connection.prepareStatement(Queries.GET_EMPLOYEE_ID);
                preparedStatement.setString(1, resultSet.getString(1));
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }


    @Override
    public int getAnnualLeaves(String leaveType) {
        int count = 0;
        logger.debug("Fetching annuals leaves for type {},", leaveType);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_ANNUAL_LEAVES);
            preparedStatement.setString(1, leaveType);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count > 0) {
                logger.info("Annual leaves fetched successfully for leave type {}", leaveType);
            }
        } catch (SQLException e) {
            logger.error("Exception while fetching annuals leaves for leave type : {}", leaveType, e);
        }
        return count;
    }

    @Override
    public int getAppliedLeavesCount(int empId, String leaveType) {
        int count = 0;
        logger.debug("Attempt to get applied leaves count for employee with id {}", empId);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_APPLIED_LEAVES_COUNT);
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, leaveType);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            logger.info("Applied leaves fetched successfully from database ");
        } catch (SQLException e) {
            logger.error("Exception while getting applied leaves count for employee with id {}", empId, e);
        }
        return count;
    }

    @Override
    public List<Leave> getFilteredLeaves(int employeeId, String status) {
        logger.debug("Attempt to get filtered leaves for employees with id {}", employeeId);
        List<Leave> leaves = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.GET_FILTERED_LEAVES);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                leaves.add(new Leave(resultSet.getInt(8), resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getDate(3), resultSet.getDate(4),
                        resultSet.getString(5), resultSet.getDate(6), resultSet.getString(7)));
            }
            resultSet.close();
            preparedStatement.close();
            logger.info("Filtered Leaves fetched successfully for employee with id {}", employeeId);
        } catch (SQLException e) {
            logger.error("Exception while getting filtered Leaves for employee applied with id : {}", employeeId);
        }
        return leaves;
    }
}
