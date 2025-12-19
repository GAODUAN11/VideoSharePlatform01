package org.example.videoshareplatform01.service;

import org.example.videoshareplatform01.dao.UserDao;
import org.example.videoshareplatform01.dao.UserDaoImpl;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.util.PasswordUtil;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public boolean registerUser(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userDao.findByUsername(username) != null) {
            return false;
        }

        // 创建新用户
        User user = new User(username, PasswordUtil.hashPassword(password), email);
        userDao.save(user);
        return true;
    }

    public User loginUser(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserById(int id) {
        return userDao.findById(id);
    }
}