package com.yash.minicas.dao;

import com.yash.minicas.entity.LoanApplication;

import java.util.List;

public interface LoanApplicationDAO {
    public int createInMasterTable(LoanApplication loanApplication);

    public LoanApplication readFromMasterTable(String applicationNumber);

    public void updateInMasterTable(LoanApplication loanApplication);

    public void deleteFromMasterTable(String applicationNumber);

    public int createInTempTable(LoanApplication loanApplication);

    public LoanApplication readFromTempTable(String applicationNumber);

    public void updateInTempTable(LoanApplication loanApplication);

    public void deleteFromTempTable(String applicationNumber);

    public List<LoanApplication> getLoansByMaker();

    public List<LoanApplication> getLoansForChecker();

    int countLoansByCustomer(int customerId);
}
