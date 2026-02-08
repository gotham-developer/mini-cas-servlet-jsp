package com.yash.minicas.service.impl;

import com.yash.minicas.dao.UserDAO;
import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import com.yash.minicas.service.AuthService;

public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User validateUser(String username, String password, Role role) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || role == null) {
            return null;
        }

        return userDAO.validateUser(username, password, role);
    }

    public User fetchUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        return userDAO.read(username);
    }
}
