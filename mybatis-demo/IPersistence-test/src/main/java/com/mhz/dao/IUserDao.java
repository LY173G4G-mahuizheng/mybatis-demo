package com.mhz.dao;

import com.mhz.pojo.User;

import java.util.List;

public interface IUserDao {
    public List<User> selectList();

    public User selectOne(User user);
}
