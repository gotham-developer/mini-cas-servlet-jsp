package com.yash.minicas.util.factory;

import com.yash.minicas.dao.CustomerDAO;
import com.yash.minicas.dao.LoanApplicationDAO;
import com.yash.minicas.dao.UserDAO;
import com.yash.minicas.dao.impl.CustomerDAOImpl;
import com.yash.minicas.dao.impl.LoanApplicationDAOImpl;
import com.yash.minicas.dao.impl.UserDAOImpl;

public final class DAOFactory {

    private DAOFactory() { }

    public static UserDAO getUserDAO() {
        return UserDAOHolder.INSTANCE;
    }

    public static CustomerDAO getCustomerDAO() {
        return CustomerDAOHolder.INSTANCE;
    }

    public static LoanApplicationDAO getLoanApplicationDAO() {
        return LoanApplicationDAOHolder.INSTANCE;
    }

    private static class UserDAOHolder {
        private static final UserDAO INSTANCE = new UserDAOImpl();
    }

    private static class CustomerDAOHolder {
        private static final CustomerDAO INSTANCE = new CustomerDAOImpl();
    }

    private static class LoanApplicationDAOHolder {
        private static final LoanApplicationDAO INSTANCE = new LoanApplicationDAOImpl();
    }
}

