package com.vpms.dao.mapper;

import com.vpms.dao.model.Knowledge;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface KnowledgeMapper extends Mapper<Knowledge> {

    List<Knowledge> selectExpertKnowledge(@Param("kw") String kw, @Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectExpertKnowledgeCount(@Param("kw") String kw, @Param("userId") Integer userId);

    List<Knowledge> searchKnowledge(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer searchKnowledgeCount(@Param("kw") String kw);
}