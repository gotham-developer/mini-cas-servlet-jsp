package com.yash.minicas.service;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;

public interface AuthService {
    User validateUser(String username, String password, Role role);

    User fetchUserByUsername(String username);
}
