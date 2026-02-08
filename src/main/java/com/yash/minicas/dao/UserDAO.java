package com.yash.minicas.dao;

import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;

public interface UserDAO {
    public User validateUser(String username, String password, Role role);

    public User read(String username);
}
