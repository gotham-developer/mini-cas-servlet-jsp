package com.yash.minicas.service.checker;

import com.yash.minicas.exception.ApplicationNumberIncorrectException;
import com.yash.minicas.entity.LoanApplication;

public interface CheckerService {
    String approveLoan(String applicationNumber);

    String rejectLoan(String applicationNumber);

    LoanApplication searchLoanApplication(String applicationNumber) throws ApplicationNumberIncorrectException;
}