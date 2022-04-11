package com.github.redxiiikk.learn.springbootspock.service.impl;

import com.github.redxiiikk.learn.springbootspock.mapper.UserMapper;
import com.github.redxiiikk.learn.springbootspock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<String> names() {
        return userMapper.names();
    }
}
