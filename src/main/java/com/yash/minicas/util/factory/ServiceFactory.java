package com.yash.minicas.util.factory;

import com.yash.minicas.service.AuthService;
import com.yash.minicas.service.CASService;
import com.yash.minicas.service.impl.AuthServiceImpl;
import com.yash.minicas.service.impl.CASServiceImpl;

public final class ServiceFactory {

    private ServiceFactory() { }

    public static CASService getCasService() {
        return CASServiceHolder.INSTANCE;
    }

    public static AuthService getAuthService() {
        return AuthServiceHolder.INSTANCE;
    }

    private static class CASServiceHolder {
        private static final CASService INSTANCE = new CASServiceImpl(DAOFactory.getCustomerDAO(), DAOFactory.getLoanApplicationDAO());
    }

    private static class AuthServiceHolder {
        private static final AuthService INSTANCE = new AuthServiceImpl(DAOFactory.getUserDAO());
    }
}
