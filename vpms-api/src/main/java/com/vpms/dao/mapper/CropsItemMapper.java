package com.vpms.dao.mapper;

import com.vpms.dao.model.CropsItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CropsItemMapper extends Mapper<CropsItem> {

    List<CropsItem> selectCropsItemByCropsId(@Param("cropsId") Integer cropsId, @Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectCropsItemCountByCropsId(@Param("cropsId") Integer cropsId, @Param("userId") Integer userId);
}