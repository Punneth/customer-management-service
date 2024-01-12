package com.albanero.dao;

import com.albanero.pojo.User;

public interface UserDao {
    void addUser(User user);

    User getUserByName(String name);
}
