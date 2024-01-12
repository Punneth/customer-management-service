package com.albanero.dao.impl;

import com.albanero.dao.UserDao;
import com.albanero.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void addUser(User user) {
        try {
            namedJdbcTemplate.update(addUser(), new BeanPropertySqlParameterSource(user));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public User getUserByName(String name) {

        User user = null;
        try {

            user = namedJdbcTemplate.queryForObject(getUserByName(),
                    new BeanPropertySqlParameterSource(User.builder().userName(name).build()),
                    new BeanPropertyRowMapper<>(User.class));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return user;
    }

    private String getUserByName() {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM USER ");
        queryBuilder.append("WHERE userName =:userName");
        return queryBuilder.toString();
    }

    private String addUser() {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO USER(userName, password, email, roles) ");
        queryBuilder.append("VALUES(:userName, :password, :email, :roles)");
        return queryBuilder.toString();
    }
}
