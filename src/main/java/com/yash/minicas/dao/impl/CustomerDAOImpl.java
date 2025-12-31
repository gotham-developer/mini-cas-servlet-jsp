package com.yash.minicas.dao.impl;

import com.yash.minicas.dao.CustomerDAO;
import com.yash.minicas.entity.Customer;
import com.yash.minicas.entity.Education;
import com.yash.minicas.util.DBConnectionManager;
import com.yash.minicas.util.LoggerUtility;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = LoggerUtility.getLogger(CustomerDAOImpl.class);

    public int create(Customer customer) {
        String sql = "INSERT INTO YASH_MINI_CAS_CUSTOMER (NAME, SALUTATION, GENDER, DATE_OF_BIRTH, CONTACT_NUMBER, NATIONALITY, FLAT_PLOT_NUMBER, ADDRESS_LINE2, CITY, STATE, COUNTRY, PINCODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"ID"})) {
                preparedStatement.setString(1, customer.getCustomerName());
                preparedStatement.setString(2, customer.getSalutation());
                preparedStatement.setString(3, customer.getGender());
                preparedStatement.setDate(4, Date.valueOf(customer.getDateOfBirth()));
                preparedStatement.setString(5, customer.getContactNumber());
                preparedStatement.setString(6, customer.getNationality());
                preparedStatement.setString(7, customer.getFlatPlotNumber());
                preparedStatement.setString(8, customer.getAddressLine2());
                preparedStatement.setString(9, customer.getCity());
                preparedStatement.setString(10, customer.getState());
                preparedStatement.setString(11, customer.getCountry());
                preparedStatement.setString(12, customer.getPinCode());
                preparedStatement.executeUpdate();

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        logger.info("Customer stored successfully in the DB");
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while storing customer: {}", e.getMessage());
        }

        return -1;
    }

    public Customer read(int customerId) {
        String custSQL = "SELECT NAME, SALUTATION, GENDER, DATE_OF_BIRTH, CONTACT_NUMBER, NATIONALITY, FLAT_PLOT_NUMBER, ADDRESS_LINE2, CITY, STATE, COUNTRY, PINCODE FROM YASH_MINI_CAS_CUSTOMER WHERE ID = ?";
        String eduSQL  = "SELECT QUALIFICATION_TYPE, CLASSIFICATION, SPECIALIZATION, BOARD_UNIVERSITY, YEAR_OF_PASSING, IS_HIGHEST_DEGREE FROM YASH_MINI_CAS_EDUCATION WHERE CUSTOMER_ID = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement psCust = connection.prepareStatement(custSQL)) {
                psCust.setInt(1, customerId);
                ResultSet resultSet = psCust.executeQuery();

                if (!resultSet.next()) {
                    logger.warn("Customer not found");
                    return null;
                }

                logger.info("Customer found");
                Customer customer = Customer.CustomerBuilder.aCustomer(resultSet.getString("NAME"))
                                                            .withGender(resultSet.getString("GENDER"))
                                                            .withDateOfBirth(resultSet.getDate("DATE_OF_BIRTH").toLocalDate())
                                                            .withNationality(resultSet.getString("NATIONALITY"))
                                                            .withFlatPlotNumber(resultSet.getString("FLAT_PLOT_NUMBER"))
                                                            .withCity(resultSet.getString("CITY"))
                                                            .withState(resultSet.getString("STATE"))
                                                            .withCountry(resultSet.getString("COUNTRY"))
                                                            .withPinCode(resultSet.getString("PINCODE"))
                                                            .withCustomerId(customerId)
                                                            .withContactNumber(resultSet.getString("CONTACT_NUMBER"))
                                                            .withSalutation(resultSet.getString("SALUTATION"))
                                                            .withAddressLine2(resultSet.getString("ADDRESS_LINE2"))
                                                            .build();

                Set<Education> educationSet = new HashSet<>();
                try (PreparedStatement psEdu = connection.prepareStatement(eduSQL)) {
                    psEdu.setInt(1, customerId);
                    ResultSet rsEdu = psEdu.executeQuery();

                    while (rsEdu.next()) {
                        Education edu = Education.EducationBuilder.anEducation(rsEdu.getString("QUALIFICATION_TYPE"))
                                                                  .withClassification(rsEdu.getString("CLASSIFICATION"))
                                                                  .withSpecialization(rsEdu.getString("SPECIALIZATION"))
                                                                  .withBoardUniversity(rsEdu.getString("BOARD_UNIVERSITY"))
                                                                  .withYearOfPassing(rsEdu.getInt("YEAR_OF_PASSING"))
                                                                  .withIsHighestDegree(rsEdu.getString("IS_HIGHEST_DEGREE"))
                                                                  .build();
                        educationSet.add(edu);
                    }
                }

                customer.setEducationSet(educationSet);
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while reading customer: {}", e.getMessage());
        }
        return null;
    }

    public Customer read(String customerName, String contactNumber) {
        String sql = "SELECT ID, GENDER, DATE_OF_BIRTH, NATIONALITY, FLAT_PLOT_NUMBER, CITY, STATE, COUNTRY, PINCODE FROM YASH_MINI_CAS_CUSTOMER WHERE NAME = ? AND CONTACT_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, customerName);
                preparedStatement.setString(2, contactNumber);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    logger.warn("Customer not found");
                    return null;
                }

                logger.info("Customer found");
                return Customer.CustomerBuilder.aCustomer(resultSet.getString("NAME"))
                                               .withGender(resultSet.getString("GENDER"))
                                               .withDateOfBirth(resultSet.getDate("DATE_OF_BIRTH").toLocalDate())
                                               .withNationality(resultSet.getString("NATIONALITY"))
                                               .withFlatPlotNumber(resultSet.getString("FLAT_PLOT_NUMBER"))
                                               .withCity(resultSet.getString("CITY"))
                                               .withState(resultSet.getString("STATE"))
                                               .withCountry(resultSet.getString("COUNTRY"))
                                               .withPinCode(resultSet.getString("PINCODE"))
                                               .withCustomerId(resultSet.getInt("ID"))
                                               .build();
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while reading customer: {}", e.getMessage());
        }
        return null;
    }

    public void update(Customer customer) {
        String sql = "UPDATE YASH_MINI_CAS_CUSTOMER SET NAME = ?, GENDER = ?, DATE_OF_BIRTH = ?, CONTACT_NUMBER = ?, SALUTATION = ?, NATIONALITY = ?, ADDRESS = ?, QUALIFICATION_TYPE = ? WHERE ID = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, customer.getCustomerName());
                preparedStatement.setString(2, customer.getGender());
                preparedStatement.setDate(3, Date.valueOf(customer.getDateOfBirth()));
                preparedStatement.setString(4, customer.getContactNumber());
                preparedStatement.setString(5, customer.getSalutation());
                preparedStatement.setString(6, customer.getNationality());
                preparedStatement.setInt(9, customer.getCustomerId());
                preparedStatement.executeUpdate();

                logger.info("Customer updated successfully in the DB.");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while updating customer: {}", e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM YASH_MINI_CAS_CUSTOMER WHERE ID = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

                logger.info("Customer deleted successfully from the DB.");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while deleting customer: {}", e.getMessage());
        }
    }

    public int createEducation(Set<Education> educationSet) {
        String sql = "INSERT INTO YASH_MINI_CAS_EDUCATION (CUSTOMER_ID, QUALIFICATION_TYPE, CLASSIFICATION, SPECIALIZATION, BOARD_UNIVERSITY, YEAR_OF_PASSING, IS_HIGHEST_DEGREE) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Education education : educationSet) {
                    preparedStatement.setInt(1, education.getCustomerId());
                    preparedStatement.setString(2, education.getQualificationType());
                    preparedStatement.setString(3, education.getClassification());
                    preparedStatement.setString(4, education.getSpecialization());
                    preparedStatement.setString(5, education.getBoardUniversity());
                    preparedStatement.setInt(6, education.getYearOfPassing());
                    preparedStatement.setString(7, education.getIsHighestDegree());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while storing education: {}", e.getMessage());
        }

        return -1;
    }

}
