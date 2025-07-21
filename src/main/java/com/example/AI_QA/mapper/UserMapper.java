package com.example.AI_QA.mapper;

import com.example.AI_QA.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user (username, password, email, role, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{email}, #{role}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET password = #{password}, email = #{email}, role = #{role}, update_time = NOW() WHERE id = #{id}")
    int update(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT * FROM user ORDER BY create_time DESC")
    List<User> findAll();
}
