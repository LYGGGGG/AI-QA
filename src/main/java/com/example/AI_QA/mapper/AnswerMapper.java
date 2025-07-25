package com.example.AI_QA.mapper;

import com.example.AI_QA.entity.Answer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AnswerMapper {

    @Insert("INSERT INTO answer (question_id, answer_content, create_time, model_used) " +
            "VALUES (#{questionId}, #{answerContent}, NOW(), #{modelUsed})")
    void insert(Answer answer);

    @Select("SELECT * FROM answer WHERE question_id = #{questionId}")
    Answer findByQuestionId(@Param("questionId") Long questionId);
}
