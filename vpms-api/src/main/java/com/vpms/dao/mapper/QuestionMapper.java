package com.vpms.dao.mapper;

import com.vpms.dao.model.Question;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface QuestionMapper extends Mapper<Question> {

    List<Question> selectQuestions(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectQuestionsCount(@Param("kw") String kw);

    Question selectQuestionById(@Param("questionId") Integer questionId);

    /**
     * 查询某个用户提出的问题
     * @param kw
     * @param userId
     * @param start
     * @param end
     * @return
     */
    List<Question> selectMyQuestions(@Param("kw") String kw, @Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyQuestionsCount(@Param("kw") String kw, @Param("userId") Integer userId);

    /**
     * 查询未关闭的问题
     * @return
     */
    List<Question> selectNotFinishQuestion();

    /**
     * 扫描超过1天还未回答的问题
     * @return
     */
    List<Question> selectWaitAnswerQuestion();
}