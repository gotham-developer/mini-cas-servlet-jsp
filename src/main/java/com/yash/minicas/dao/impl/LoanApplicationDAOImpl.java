package com.yash.minicas.dao.impl;

import com.yash.minicas.dao.LoanApplicationDAO;
import com.yash.minicas.entity.Customer;
import com.yash.minicas.entity.LoanApplication;
import com.yash.minicas.entity.LoanStatus;
import com.yash.minicas.entity.WorkflowStatus;
import com.yash.minicas.util.DBConnectionManager;
import com.yash.minicas.util.LoggerUtility;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanApplicationDAOImpl implements LoanApplicationDAO {
    private static final Logger logger = LoggerUtility.getLogger(LoanApplicationDAOImpl.class);

    private static final String ID                 = "ID";
    private static final String APPLICATION_NUMBER = "APPLICATION_NUMBER";
    private static final String LOAN_AMOUNT        = "LOAN_AMOUNT";
    private static final String TENURE             = "TENURE";
    private static final String TENURE_IN          = "TENURE_IN";
    private static final String PRODUCT_TYPE       = "PRODUCT_TYPE";
    private static final String PRODUCT            = "PRODUCT";
    private static final String SCHEME             = "SCHEME";
    private static final String RATE               = "RATE";
    private static final String LOAN_STATUS        = "LOAN_STATUS";
    private static final String WORKFLOW_STATUS    = "WORKFLOW_STATUS";
    private static final String CREATION_DATE      = "CREATION_DATE";
    private static final String CUSTOMER_ID        = "CUSTOMER_ID";
    private static final String NAME               = "NAME";
    private static final String CONTACT_NUMBER     = "CONTACT_NUMBER";

    public int createInMasterTable(LoanApplication loanApplication) {
        String sql = "INSERT INTO YASH_MINI_CAS_LOAN_APPLICATION_MASTER (ID, APPLICATION_NUMBER, LOAN_AMOUNT, TENURE, TENURE_IN, PRODUCT_TYPE, PRODUCT, SCHEME, RATE, LOAN_STATUS, WORKFLOW_STATUS, CUSTOMER_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, loanApplication.getApplicationId());
                preparedStatement.setString(2, loanApplication.getApplicationNumber());
                preparedStatement.setFloat(3, (float) loanApplication.getLoanAmount());
                preparedStatement.setInt(4, loanApplication.getTenure());
                preparedStatement.setString(5, loanApplication.getTenureIn());
                preparedStatement.setString(6, loanApplication.getProductType());
                preparedStatement.setString(7, loanApplication.getProduct());
                preparedStatement.setString(8, loanApplication.getScheme());
                preparedStatement.setFloat(9, (float) loanApplication.getRate());
                preparedStatement.setString(10, loanApplication.getLoanStatus().name());
                preparedStatement.setString(11, loanApplication.getWorkflowStatus().name());
                preparedStatement.setInt(12, loanApplication.getCustomer().getCustomerId());
                preparedStatement.executeUpdate();

                logger.info("Loan Application Stored Successfully In MASTER Table");
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return -1;
    }

    public LoanApplication readFromMasterTable(String applicationNumber) {
        String sql = "SELECT ID, LOAN_AMOUNT, TENURE, TENURE_IN, PRODUCT_TYPE, PRODUCT, SCHEME, RATE, LOAN_STATUS, WORKFLOW_STATUS, CUSTOMER_ID, CREATION_DATE FROM YASH_MINI_CAS_LOAN_APPLICATION_MASTER WHERE APPLICATION_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, applicationNumber);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    logger.warn("Loan application not found");
                    return null;
                }

                int            id             = resultSet.getInt(ID);
                LocalDate      creationDate   = resultSet.getDate(CREATION_DATE).toLocalDate();
                double         loanAmount     = resultSet.getFloat(LOAN_AMOUNT);
                int            tenure         = resultSet.getInt(TENURE);
                String         tenureIn       = resultSet.getString(TENURE_IN);
                String         productType    = resultSet.getString(PRODUCT_TYPE);
                String         product        = resultSet.getString(PRODUCT);
                String         scheme         = resultSet.getString(SCHEME);
                double         rate           = resultSet.getDouble(RATE);
                LoanStatus     loanStatus     = LoanStatus.valueOf(resultSet.getString(LOAN_STATUS));
                WorkflowStatus workflowStatus = WorkflowStatus.valueOf(resultSet.getString(WORKFLOW_STATUS));
                int            customerId     = resultSet.getInt(CUSTOMER_ID);

                logger.warn("Loan Application Found In MASTER Table");
                LoanApplication loanApplication = LoanApplication.LoanApplicationBuilder.aLoanApplication()
                                                                                        .withLoanAmount(loanAmount)
                                                                                        .withTenure(tenure)
                                                                                        .withProductType(productType)
                                                                                        .withProduct(product)
                                                                                        .withScheme(scheme)
                                                                                        .withRate(rate)
                                                                                        .withApplicationId(id)
                                                                                        .withApplicationNumber(applicationNumber)
                                                                                        .withApplicationCreationDate(creationDate)
                                                                                        .withTenureIn(tenureIn)
                                                                                        .withLoanStatus(loanStatus)
                                                                                        .withWorkflowStatus(workflowStatus)
                                                                                        .build();
                loanApplication.getCustomer().setCustomerId(customerId);
                return loanApplication;
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return null;
    }

    public void updateInMasterTable(LoanApplication loanApplication) {
        String sql = "UPDATE YASH_MINI_CAS_LOAN_APPLICATION_MASTER SET LOAN_STATUS = ?, WORKFLOW_STATUS = ? WHERE APPLICATION_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, loanApplication.getLoanStatus().name());
                preparedStatement.setString(2, loanApplication.getWorkflowStatus().name());
                preparedStatement.setString(3, loanApplication.getApplicationNumber());
                preparedStatement.executeUpdate();

                logger.info("Loan Application Updated Successfully In MASTER Table");
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public void deleteFromMasterTable(String applicationNumber) {
        String sql = "DELETE FROM YASH_MINI_CAS_LOAN_APPLICATION_MASTER WHERE APPLICATION_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, applicationNumber);
                preparedStatement.executeUpdate();

                logger.info("Loan application deleted successfully from the DB.");
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public int createInTempTable(LoanApplication loanApplication) {
        String sql = "INSERT INTO YASH_MINI_CAS_LOAN_APPLICATION_TEMP (APPLICATION_NUMBER, LOAN_AMOUNT, TENURE, TENURE_IN, PRODUCT_TYPE, PRODUCT, SCHEME, RATE, LOAN_STATUS, WORKFLOW_STATUS, CUSTOMER_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"ID"})) {
                preparedStatement.setString(1, loanApplication.getApplicationNumber());
                preparedStatement.setFloat(2, (float) loanApplication.getLoanAmount());
                preparedStatement.setInt(3, loanApplication.getTenure());
                preparedStatement.setString(4, loanApplication.getTenureIn());
                preparedStatement.setString(5, loanApplication.getProductType());
                preparedStatement.setString(6, loanApplication.getProduct());
                preparedStatement.setString(7, loanApplication.getScheme());
                preparedStatement.setFloat(8, (float) loanApplication.getRate());
                preparedStatement.setString(9, loanApplication.getLoanStatus().name());
                preparedStatement.setString(10, loanApplication.getWorkflowStatus().name());
                preparedStatement.setInt(11, loanApplication.getCustomer().getCustomerId());
                preparedStatement.executeUpdate();

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        logger.info("Loan Application Stored Successfully In TEMP Table");
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return -1;
    }

    public LoanApplication readFromTempTable(String applicationNumber) {
        String sql = "SELECT ID, LOAN_AMOUNT, TENURE, TENURE_IN, PRODUCT_TYPE, PRODUCT, SCHEME, RATE, LOAN_STATUS, WORKFLOW_STATUS, CUSTOMER_ID, CREATION_DATE FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP WHERE APPLICATION_NUMBER = ?";

        ResultSet resultSet;
        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, applicationNumber);
                resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    logger.warn("Loan application not found");
                    return null;
                }

                int            id             = resultSet.getInt(ID);
                LocalDate      creationDate   = resultSet.getDate(CREATION_DATE).toLocalDate();
                double         loanAmount     = resultSet.getFloat(LOAN_AMOUNT);
                int            tenure         = resultSet.getInt(TENURE);
                String         tenureIn       = resultSet.getString(TENURE_IN);
                String         productType    = resultSet.getString(PRODUCT_TYPE);
                String         product        = resultSet.getString(PRODUCT);
                String         scheme         = resultSet.getString(SCHEME);
                double         rate           = resultSet.getDouble(RATE);
                LoanStatus     loanStatus     = LoanStatus.valueOf(resultSet.getString(LOAN_STATUS));
                WorkflowStatus workflowStatus = WorkflowStatus.valueOf(resultSet.getString(WORKFLOW_STATUS));
                int            customerId     = resultSet.getInt(CUSTOMER_ID);

                logger.warn("Loan Application Found In TEMP Table");
                LoanApplication loanApplication = LoanApplication.LoanApplicationBuilder.aLoanApplication()
                                                                                        .withLoanAmount(loanAmount)
                                                                                        .withTenure(tenure)
                                                                                        .withProductType(productType)
                                                                                        .withProduct(product)
                                                                                        .withScheme(scheme)
                                                                                        .withRate(rate)
                                                                                        .withApplicationId(id)
                                                                                        .withApplicationNumber(applicationNumber)
                                                                                        .withApplicationCreationDate(creationDate)
                                                                                        .withTenureIn(tenureIn)
                                                                                        .withLoanStatus(loanStatus)
                                                                                        .withWorkflowStatus(workflowStatus)
                                                                                        .build();
                loanApplication.getCustomer().setCustomerId(customerId);
                return loanApplication;
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return null;
    }

    public void updateInTempTable(LoanApplication loanApplication) {
        String sql = "UPDATE YASH_MINI_CAS_LOAN_APPLICATION_TEMP SET LOAN_STATUS = ?, WORKFLOW_STATUS = ? WHERE APPLICATION_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, loanApplication.getLoanStatus().name());
                preparedStatement.setString(2, loanApplication.getWorkflowStatus().name());
                preparedStatement.setString(3, loanApplication.getApplicationNumber());
                preparedStatement.executeUpdate();

                logger.info("Loan application updated successfully in the DB.");
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public void deleteFromTempTable(String applicationNumber) {
        String sql = "DELETE FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP WHERE APPLICATION_NUMBER = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, applicationNumber);
                preparedStatement.executeUpdate();

                logger.info("Loan Application Deleted Successfully From TEMP Table");
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public List<LoanApplication> getLoansByMaker() {
        String sql = "SELECT t.APPLICATION_NUMBER, c.NAME, c.CONTACT_NUMBER, t.PRODUCT_TYPE, t.PRODUCT, t.LOAN_AMOUNT, t.LOAN_STATUS, t.WORKFLOW_STATUS, t.CREATION_DATE FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP t JOIN YASH_MINI_CAS_CUSTOMER c ON t.CUSTOMER_ID = c.ID UNION ALL SELECT m.APPLICATION_NUMBER, c.NAME, c.CONTACT_NUMBER, m.PRODUCT_TYPE, m.PRODUCT, m.LOAN_AMOUNT, m.LOAN_STATUS, m.WORKFLOW_STATUS, m.CREATION_DATE FROM YASH_MINI_CAS_LOAN_APPLICATION_MASTER m JOIN YASH_MINI_CAS_CUSTOMER c ON m.CUSTOMER_ID = c.ID WHERE NOT EXISTS (SELECT 1 FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP t WHERE t.APPLICATION_NUMBER = m.APPLICATION_NUMBER)";

        List<LoanApplication> loanApplicationList = new ArrayList<>();

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String         applicationNumber = resultSet.getString(APPLICATION_NUMBER);
                    String         productType       = resultSet.getString(PRODUCT_TYPE);
                    String         product           = resultSet.getString(PRODUCT);
                    double         loanAmount        = resultSet.getFloat(LOAN_AMOUNT);
                    LoanStatus     loanStatus        = LoanStatus.valueOf(resultSet.getString(LOAN_STATUS));
                    WorkflowStatus workflowStatus    = WorkflowStatus.valueOf(resultSet.getString(WORKFLOW_STATUS));
                    LocalDate      creationDate      = resultSet.getDate(CREATION_DATE).toLocalDate();
                    String         customerName      = resultSet.getString(NAME);
                    String         contactNumber     = resultSet.getString(CONTACT_NUMBER);

                    Customer customer = Customer.CustomerBuilder.aCustomer(customerName).withContactNumber(contactNumber).build();
                    LoanApplication loanApplication = LoanApplication.LoanApplicationBuilder.aLoanApplication()
                                                                                            .withLoanAmount(loanAmount)
                                                                                            .withProductType(productType)
                                                                                            .withProduct(product)
                                                                                            .withApplicationNumber(applicationNumber)
                                                                                            .withApplicationCreationDate(creationDate)
                                                                                            .withLoanStatus(loanStatus)
                                                                                            .withWorkflowStatus(workflowStatus)
                                                                                            .withCustomer(customer)
                                                                                            .build();
                    loanApplicationList.add(loanApplication);
                }
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return loanApplicationList;
    }

    public List<LoanApplication> getLoansForChecker() {
        String sql = "SELECT t.APPLICATION_NUMBER, c.NAME, c.CONTACT_NUMBER, t.PRODUCT_TYPE, t.PRODUCT, t.LOAN_AMOUNT, t.LOAN_STATUS, t.WORKFLOW_STATUS, t.CREATION_DATE FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP t JOIN YASH_MINI_CAS_CUSTOMER c ON t.CUSTOMER_ID = c.ID WHERE t.WORKFLOW_STATUS IN ('NEW','PENDING_MODIFICATION','PENDING_DELETION')";

        List<LoanApplication> loanApplicationList = new ArrayList<>();

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String         applicationNumber = resultSet.getString(APPLICATION_NUMBER);
                    String         productType       = resultSet.getString(PRODUCT_TYPE);
                    String         product           = resultSet.getString(PRODUCT);
                    double         loanAmount        = resultSet.getFloat(LOAN_AMOUNT);
                    LoanStatus     loanStatus        = LoanStatus.valueOf(resultSet.getString(LOAN_STATUS));
                    WorkflowStatus workflowStatus    = WorkflowStatus.valueOf(resultSet.getString(WORKFLOW_STATUS));
                    LocalDate      creationDate      = resultSet.getDate(CREATION_DATE).toLocalDate();
                    String         customerName      = resultSet.getString(NAME);
                    String         contactNumber     = resultSet.getString(CONTACT_NUMBER);

                    LoanApplication loanApplication = LoanApplication.LoanApplicationBuilder.aLoanApplication()
                                                                                            .withLoanAmount(loanAmount)
                                                                                            .withProductType(productType)
                                                                                            .withProduct(product)
                                                                                            .withApplicationNumber(applicationNumber)
                                                                                            .withApplicationCreationDate(creationDate)
                                                                                            .withLoanStatus(loanStatus)
                                                                                            .withWorkflowStatus(workflowStatus)
                                                                                            .build();
                    loanApplication.getCustomer().setCustomerName(customerName);
                    loanApplication.getCustomer().setContactNumber(contactNumber);
                    loanApplicationList.add(loanApplication);
                }
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return loanApplicationList;
    }

    @Override
    public int countLoansByCustomer(int customerId) {
        String sql = "SELECT (SELECT COUNT(*) FROM YASH_MINI_CAS_LOAN_APPLICATION_MASTER WHERE CUSTOMER_ID = ?) + (SELECT COUNT(*) FROM YASH_MINI_CAS_LOAN_APPLICATION_TEMP WHERE CUSTOMER_ID = ?) AS TOTAL FROM DUAL";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.setInt(2, customerId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("TOTAL");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("{}", e.getMessage());
        }

        return 0;
    }
}
