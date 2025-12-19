package org.example.videoshareplatform01.dao;

import org.example.videoshareplatform01.model.User;

public interface UserDao {
    void save(User user);
    User findByUsername(String username);
    User findById(int id);
    User findByUsernameAndPassword(String username, String password);
}