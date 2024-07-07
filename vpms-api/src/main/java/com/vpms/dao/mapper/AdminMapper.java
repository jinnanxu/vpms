package com.vpms.dao.mapper;

import com.vpms.dao.model.Admin;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminMapper extends Mapper<Admin> {

    List<Admin> selectExpertList(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectExpertListCount(@Param("kw") String kw);
}