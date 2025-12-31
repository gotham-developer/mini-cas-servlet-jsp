package com.yash.minicas.dao;

import com.yash.minicas.entity.Customer;
import com.yash.minicas.entity.Education;

import java.util.Set;

public interface CustomerDAO {
    public int create(Customer customer);

    public Customer read(int customerId);

    public Customer read(String customerName, String contactNumber);

//    public void update(Customer customer) {
//        String sql = "UPDATE APEX_YASH_CUSTOMER_SERVLET SET NAME = ?, GENDER = ?, DATE_OF_BIRTH = ?, CONTACT_NUMBER = ?, SALUTATION = ?, NATIONALITY = ?, ADDRESS = ?, QUALIFICATION_TYPE = ? WHERE ID = ?";
//        try (Connection connection = DBConnectionManager.getConnection()) {
//            if (connection == null) {
//                LoggerUtility.logger.warn(NO_CONNECTION_MESSAGE);
//                return;
//            }
//
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, customer.getCustomerName());
//                preparedStatement.setString(2, customer.getGender());
//                preparedStatement.setDate(3, Date.valueOf(customer.getDateOfBirth()));
//                preparedStatement.setString(4, customer.getContactNumber());
//                preparedStatement.setString(5, customer.getSalutation());
//                preparedStatement.setString(6, customer.getNationality());
//                preparedStatement.setString(7, customer.getAddress());
//                preparedStatement.setString(8, customer.getQualificationType());
//                preparedStatement.setInt(9, customer.getCustomerId());
//                preparedStatement.executeUpdate();
//
//                LoggerUtility.logger.info("Customer updated successfully in the DB.");
//            }
//        } catch (SQLException e) {
//            LoggerUtility.logger.error("SQL Exception occurred while updating customer: {}", e.getMessage());
//        }
//    }

    public void delete(int id);

    public int createEducation(Set<Education> educationSet);
}
