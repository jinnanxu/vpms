package com.vpms.dao.mapper;

import com.vpms.dao.model.Task;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TaskMapper extends Mapper<Task> {
    List<Task> selectTaskList(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end, @Param("kw") String kw);

    Integer selectTaskCount(@Param("userId") Integer userId, @Param("kw") String kw);

    List<Task> selectAllTaskList(@Param("start") Integer start, @Param("end") Integer end, @Param("kw") String kw);

    Integer selectAllTaskListCount(@Param("kw") String kw);
}