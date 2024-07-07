package com.vpms.dao.mapper;

import com.vpms.dao.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    List<User> selectStudentList(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectStudentListCount(@Param("kw") String kw);
}