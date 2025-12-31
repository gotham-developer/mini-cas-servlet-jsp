package com.yash.minicas.service;

import com.yash.minicas.entity.LoanApplication;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.checker.CheckerService;
import com.yash.minicas.service.maker.MakerService;

import java.util.List;

public interface CASService extends MakerService, CheckerService {
    List<LoanApplication> fetchLoansForUser(User user);
}
