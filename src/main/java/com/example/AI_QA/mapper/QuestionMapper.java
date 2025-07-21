package com.example.AI_QA.mapper;

import com.example.AI_QA.entity.Question;
import com.example.AI_QA.vo.QuestionAnswerVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question (user_id, content, status, create_time, update_time) " +
            "VALUES (#{userId}, #{content}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Question question);

    @Update("UPDATE question SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT q.id AS questionId, q.content AS questionContent, a.answer_content AS answerContent, q.create_time AS createTime " +
            "FROM question q LEFT JOIN answer a ON q.id = a.question_id " +
            "WHERE q.user_id = #{userId} " +
            "ORDER BY q.create_time DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<QuestionAnswerVO> findUserQA(@Param("userId") Long userId,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM question WHERE user_id = #{userId}")
    int countUserQA(@Param("userId") Long userId);

}
