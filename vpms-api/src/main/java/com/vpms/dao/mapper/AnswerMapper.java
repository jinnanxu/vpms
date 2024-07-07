package com.vpms.dao.mapper;

import com.vpms.dao.model.Answer;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AnswerMapper extends Mapper<Answer> {

    List<Answer> selectAnswerByQuestion(@Param("questionId") Integer questionId);

    Integer selectAnswerCount(@Param("teacherId") Integer teacherId);

    /**
     * 查询回答过某个问题的所有专家ID
     * @param questionId
     * @return
     */
    List<Integer> selectAnswerExpertIds(@Param("questionId") Integer questionId);
}