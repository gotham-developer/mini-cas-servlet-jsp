package com.yash.minicas.service.impl;

import com.yash.minicas.exception.ApplicationNumberIncorrectException;
import com.yash.minicas.dao.CustomerDAO;
import com.yash.minicas.dao.LoanApplicationDAO;
import com.yash.minicas.entity.*;
import com.yash.minicas.service.CASService;

import java.util.Collections;
import java.util.List;

public class CASServiceImpl implements CASService {
    private final CustomerDAO        customerDAO;
    private final LoanApplicationDAO loanApplicationDAO;

    public CASServiceImpl(CustomerDAO customerDAO, LoanApplicationDAO loanApplicationDAO) {
        this.customerDAO        = customerDAO;
        this.loanApplicationDAO = loanApplicationDAO;
    }

    @Override
    public String approveLoan(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return "";
        }

        LoanApplication loanApplicationTemp = loanApplicationDAO.readFromTempTable(applicationNumber);
        if (loanApplicationTemp == null) {
            return "";
        }

        switch (loanApplicationTemp.getWorkflowStatus()) {
            case WorkflowStatus.NEW -> {
                loanApplicationTemp.setLoanStatus(LoanStatus.APPROVED);
                loanApplicationTemp.setWorkflowStatus(WorkflowStatus.AUTHORIZED);
                loanApplicationDAO.createInMasterTable(loanApplicationTemp);
            }
            case WorkflowStatus.PENDING_DELETION -> loanApplicationDAO.deleteFromMasterTable(applicationNumber);
            case WorkflowStatus.PENDING_MODIFICATION -> {
                loanApplicationTemp.setLoanStatus(LoanStatus.APPROVED);
                loanApplicationTemp.setWorkflowStatus(WorkflowStatus.AUTHORIZED);
                loanApplicationDAO.updateInMasterTable(loanApplicationTemp);
            }
            default -> { return ""; }
        }

        loanApplicationDAO.deleteFromTempTable(applicationNumber);

        int customerId = loanApplicationTemp.getCustomer().getCustomerId();
        int count      = loanApplicationDAO.countLoansByCustomer(customerId);
        if (count == 0) {
            customerDAO.delete(customerId);
        }

        return "Approval done successfully.";
    }

    @Override
    public String rejectLoan(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return "";
        }

        LoanApplication loanApplicationTemp = loanApplicationDAO.readFromTempTable(applicationNumber);
        if (loanApplicationTemp == null) {
            return "";
        }

        switch (loanApplicationTemp.getWorkflowStatus()) {
            case WorkflowStatus.NEW -> {
                loanApplicationTemp.setLoanStatus(LoanStatus.REJECTED);
                loanApplicationTemp.setWorkflowStatus(WorkflowStatus.REJECTED_NEW);
            }
            case WorkflowStatus.PENDING_MODIFICATION -> loanApplicationTemp.setWorkflowStatus(
                    WorkflowStatus.REJECTED_MODIFICATION);
            case WorkflowStatus.PENDING_DELETION -> loanApplicationTemp.setWorkflowStatus(
                    WorkflowStatus.REJECTED_DELETION);
            default -> { return ""; }
        }

        loanApplicationDAO.updateInTempTable(loanApplicationTemp);
        return "Rejection done successfully.";
    }

    @Override
    public String addLoanApplication(double loanAmount, double rate, int tenure, String productType, String product,
                                     String scheme, String tenureIn, Customer customer) {
        if (loanAmount <= 0 || rate <= 0 || tenure <= 0 || customer == null || productType == null ||
            productType.isEmpty() || product == null || product.isEmpty() || scheme == null || scheme.isEmpty() ||
            tenureIn == null || tenureIn.isEmpty() || customer.getCustomerName() == null ||
            customer.getCustomerName().isEmpty() || customer.getContactNumber() == null) {
            return "";
        }

        LoanApplication loanApplication = LoanApplication.LoanApplicationBuilder.aLoanApplication()
                                                                                .withLoanAmount(loanAmount)
                                                                                .withTenure(tenure)
                                                                                .withProductType(productType)
                                                                                .withProduct(product)
                                                                                .withScheme(scheme)
                                                                                .withRate(rate)
                                                                                .withTenureIn(tenureIn)
                                                                                .withCustomer(customer)
                                                                                .build();

        int generatedCustomerId = saveCustomerToDB(customer);
        if (generatedCustomerId == -1) {
            return "";
        }

        int generatedLoanApplicationId = loanApplicationDAO.createInTempTable(loanApplication);
        if (generatedLoanApplicationId == -1) {
            customerDAO.delete(generatedCustomerId);
            return "";
        }

        return loanApplication.getApplicationNumber();
    }

    @Override
    public Customer searchCustomer(int customerId) {
        if (customerId < 0) {
            return null;
        }

        return customerDAO.read(customerId);
    }

    @Override
    public String deleteLoanApplication(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return "";
        }

        LoanApplication loanApplicationTemp   = loanApplicationDAO.readFromTempTable(applicationNumber);
        LoanApplication loanApplicationMaster = loanApplicationDAO.readFromMasterTable(applicationNumber);

        if (loanApplicationTemp != null) {
            switch (loanApplicationTemp.getWorkflowStatus()) {
                case WorkflowStatus.NEW, WorkflowStatus.PENDING_MODIFICATION, WorkflowStatus.REJECTED_NEW,
                     WorkflowStatus.REJECTED_MODIFICATION -> loanApplicationDAO.deleteFromTempTable(applicationNumber);
                case WorkflowStatus.REJECTED_DELETION -> {
                    loanApplicationTemp.setWorkflowStatus(WorkflowStatus.PENDING_DELETION);
                    loanApplicationDAO.updateInTempTable(loanApplicationTemp);
                }
                default -> { return ""; }
            }
        } else if (loanApplicationMaster != null) {
            loanApplicationMaster.setWorkflowStatus(WorkflowStatus.PENDING_DELETION);
            loanApplicationDAO.createInTempTable(loanApplicationMaster);
        } else {
            return "";
        }

        int customerId = (loanApplicationTemp == null ? loanApplicationMaster : loanApplicationTemp).getCustomer()
                                                                                                    .getCustomerId();

        int count = loanApplicationDAO.countLoansByCustomer(customerId);
        if (count == 0) {
            customerDAO.delete(customerId);
        }

        return "Deletion done successfully.";
    }

    @Override
    public String modifyLoanApplication(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return "";
        }

        LoanApplication loanApplicationTemp   = loanApplicationDAO.readFromTempTable(applicationNumber);
        LoanApplication loanApplicationMaster = loanApplicationDAO.readFromMasterTable(applicationNumber);

        if (loanApplicationTemp != null) {
            switch (loanApplicationTemp.getWorkflowStatus()) {
                case WorkflowStatus.NEW, WorkflowStatus.REJECTED_NEW -> {
                    loanApplicationTemp.setLoanStatus(LoanStatus.PENDING);
                    loanApplicationTemp.setWorkflowStatus(WorkflowStatus.NEW);
                    loanApplicationDAO.updateInTempTable(loanApplicationTemp);
                }
                case WorkflowStatus.AUTHORIZED, WorkflowStatus.REJECTED_MODIFICATION -> {
                    loanApplicationTemp.setWorkflowStatus(WorkflowStatus.PENDING_MODIFICATION);
                    loanApplicationDAO.updateInTempTable(loanApplicationTemp);
                }
                default -> { return ""; }
            }
        } else if (loanApplicationMaster != null) {
            loanApplicationMaster.setWorkflowStatus(WorkflowStatus.PENDING_MODIFICATION);
            loanApplicationDAO.createInTempTable(loanApplicationMaster);
        } else {
            return "";
        }

        return "Modification done successfully.";
    }

    public int saveCustomerToDB(Customer customer) {
        int generatedCustomerId = customerDAO.create(customer);
        if (generatedCustomerId == -1) {
            return -1;
        }

        customer.setCustomerId(generatedCustomerId);
        for (Education education : customer.getEducationSet()) {
            education.setCustomerId(generatedCustomerId);
        }
        customerDAO.createEducation(customer.getEducationSet());

        return generatedCustomerId;
    }

    @Override
    public LoanApplication searchLoanApplication(String applicationNumber) throws ApplicationNumberIncorrectException {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return null;
        }

        LoanApplication loanApplication = loanApplicationDAO.readFromTempTable(applicationNumber);
        if (loanApplication == null) {
            throw new ApplicationNumberIncorrectException("Application not found");
        }

        return loanApplication;
    }

/*    @Override
    public LoanApplication modifyLoanApplication(String applicationNumber, LoanStatus loanStatus) {
        if (applicationNumber == null || applicationNumber.isEmpty()) {
            return null;
        }

        LoanApplication loanApplication;
        if (loanStatus == LoanStatus.PENDING) {
            loanApplication = loanApplicationDAO.readFromTempTable(applicationNumber);
        } else {
            loanApplication = loanApplicationDAO.readFromMasterTable(applicationNumber);
        }

        if (loanApplication == null) {
            return null;
        }

        switch (loanApplication.getWorkflowStatus()) {
            case WorkflowStatus.NEW -> loanApplication.setLoanStatus(LoanStatus.PENDING);
            case WorkflowStatus.REJECTED_NEW -> {
                loanApplication.setLoanStatus(LoanStatus.PENDING);
                loanApplication.setWorkflowStatus(WorkflowStatus.NEW);
            }
            case WorkflowStatus.REJECTED_MODIFICATION, WorkflowStatus.AUTHORIZED ->
                    loanApplication.setWorkflowStatus(WorkflowStatus.PENDING_MODIFICATION);
            default -> { return null; }
        }

        return loanApplication;
    }*/

    public Customer searchCustomer(String customerName, String contactNumber) {
        if (customerName == null || customerName.isEmpty() || contactNumber == null || contactNumber.isEmpty()) {
            return null;
        }

        return customerDAO.read(customerName, contactNumber);
    }

    public List<LoanApplication> fetchLoansForUser(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        if (Role.MAKER == user.getRole()) {
            return loanApplicationDAO.getLoansByMaker();
        } else if (Role.CHECKER == user.getRole()) {
            return loanApplicationDAO.getLoansForChecker();
        } else {
            return Collections.emptyList();
        }
    }
}
