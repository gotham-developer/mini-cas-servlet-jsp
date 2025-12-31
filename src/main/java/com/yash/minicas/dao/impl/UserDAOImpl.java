package com.yash.minicas.dao.impl;

import com.yash.minicas.dao.UserDAO;
import com.yash.minicas.entity.Role;
import com.yash.minicas.entity.User;
import com.yash.minicas.util.DBConnectionManager;
import com.yash.minicas.util.LoggerUtility;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerUtility.getLogger(UserDAOImpl.class);

    public User validateUser(String username, String password, Role role) {
        String sql = "SELECT ID FROM YASH_MINI_CAS_USER WHERE USERNAME = ? AND PASSWORD = ? AND ROLE = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role.name());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("ID");
                        return User.UserBuilder.anUser().withId(id).withUsername(username).withPassword(password).withRole(role).build();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while validating user: {}", e.getMessage());
        }

        return null;
    }

    public User read(String username) {
        String sql = "SELECT ID, USERNAME, PASSWORD, ROLE FROM YASH_MINI_CAS_USER WHERE USERNAME = ?";

        try (Connection connection = DBConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int    id       = resultSet.getInt("ID");
                        String password = resultSet.getString("PASSWORD");
                        Role   role     = Role.valueOf(resultSet.getString("ROLE"));
                        return User.UserBuilder.anUser().withId(id).withUsername(username).withPassword(password).withRole(role).build();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Exception occurred while reading user: {}", e.getMessage());
        }

        return null;
    }
}