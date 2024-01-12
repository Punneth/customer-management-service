package com.albanero.service.impl;

import com.albanero.constants.ErrorCode;
import com.albanero.dao.UserDao;
import com.albanero.exceptions.ValidationException;
import com.albanero.pojo.User;
import com.albanero.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserDao userDao;

    @Override
    public void addUser(User user) {
        if (user == null) {
            throw new ValidationException(ErrorCode.USER_VALIDATION_FAILED.getErrorMessage(),
                    ErrorCode.USER_VALIDATION_FAILED.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.addUser(user);
    }
}
