package com.example.AI_QA.mapper;

import com.example.AI_QA.entity.Question;
import com.example.AI_QA.vo.QuestionAnswerVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question(user_id, content, status, tag, starred, note, summary, create_time, update_time) " +
            "VALUES(#{userId}, #{content}, #{status}, #{tag}, #{starred}, #{note}, #{summary}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Update("UPDATE question SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT * FROM question WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Question> findByUserId(Long userId);

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question findById(Long id);

    // 分页查询某用户的问答记录
    @Select("""
            <script>
            SELECT q.id AS questionId,
                   q.content AS questionContent,
                   q.tag,
                   q.starred,
                   q.note,
                   q.summary,
                   a.answer_content AS answerContent,
                   q.create_time AS createTime
            FROM question q LEFT JOIN answer a ON q.id = a.question_id
            WHERE q.user_id = #{userId}
            <if test='keyword != null and keyword != ""'>
              AND (q.content LIKE CONCAT('%',#{keyword},'%') OR a.answer_content LIKE CONCAT('%',#{keyword},'%'))
            </if>
            <if test='tag != null and tag != ""'>
              AND q.tag = #{tag}
            </if>
            <if test='startDate != null and startDate != ""'>
              AND q.create_time >= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
              AND q.create_time &lt;= #{endDate}
            </if>
            ORDER BY q.create_time DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<QuestionAnswerVO> findUserQA(@Param("userId") Long userId,
                                      @Param("keyword") String keyword,
                                      @Param("tag") String tag,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);

    /// 查询该用户的问答总数（支持筛选）
    @Select("""
            <script>
            SELECT COUNT(*)
            FROM question q LEFT JOIN answer a ON q.id = a.question_id
            WHERE q.user_id = #{userId}
            <if test='keyword != null and keyword != ""'>
              AND (q.content LIKE CONCAT('%',#{keyword},'%') OR a.answer_content LIKE CONCAT('%',#{keyword},'%'))
            </if>
            <if test='tag != null and tag != ""'>
              AND q.tag = #{tag}
            </if>
            <if test='startDate != null and startDate != ""'>
              AND q.create_time >= #{startDate}
            </if>
            <if test='endDate != null and endDate != ""'>
              AND q.create_time &lt;= #{endDate}
            </if>
            </script>
            """)
    int countUserQA(@Param("userId") Long userId,
                    @Param("keyword") String keyword,
                    @Param("tag") String tag,
                    @Param("startDate") String startDate,
                    @Param("endDate") String endDate);

    @Select("SELECT q.id AS questionId, q.content AS questionContent, q.tag, q.starred, q.note, q.summary, a.answer_content AS answerContent, q.create_time AS createTime FROM question q LEFT JOIN answer a ON q.id = a.question_id WHERE q.id = #{id}")
    QuestionAnswerVO findQAById(@Param("id") Long id);

    @Update("UPDATE question SET starred = #{starred} WHERE id = #{id}")
    void updateStarred(@Param("id") Long id, @Param("starred") boolean starred);

    @Update("UPDATE question SET note = #{note} WHERE id = #{id}")
    void updateNote(@Param("id") Long id, @Param("note") String note);

    @Update("UPDATE question SET summary = #{summary} WHERE id = #{id}")
    void updateSummary(@Param("id") Long id, @Param("summary") String summary);

    @Delete("""
            <script>
            DELETE FROM question WHERE id IN
            <foreach collection='ids' item='id' open='(' separator=',' close=')'>
            #{id}
            </foreach>
            </script>
            """)
    void deleteQuestions(@Param("ids") List<Long> ids);
}
