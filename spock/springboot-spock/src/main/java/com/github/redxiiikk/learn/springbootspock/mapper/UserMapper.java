package com.github.redxiiikk.learn.springbootspock.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select name from user")
    List<String> names();
}
