package com.example.AI_QA.mapper;

import com.example.AI_QA.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question(user_id, content, status) VALUES(#{userId}, #{content}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Update("UPDATE question SET status = #{status} where user_id = #{userId}")
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);

    @Select("SELECT * FROM question WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Question> findByUserId(Long userId);

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question findById(Long id);
}
