package com.yash.minicas.service.maker;

import com.yash.minicas.entity.Customer;

public interface MakerService {
    String addLoanApplication(double loanAmount, double rate, int tenure, String productType, String product,
                              String scheme, String tenureIn, Customer customer);

    Customer searchCustomer(int customerId);

//    LoanApplication modifyLoanApplication(String applicationNumber, LoanStatus loanStatus);

    String deleteLoanApplication(String applicationNumber);

    String modifyLoanApplication(String applicationNumber);
}
