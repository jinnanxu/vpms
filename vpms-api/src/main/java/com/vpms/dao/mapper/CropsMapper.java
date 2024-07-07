package com.vpms.dao.mapper;

import com.vpms.dao.model.Crops;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface CropsMapper extends Mapper<Crops> {

    Crops selectCropsById(@Param("cropsId") Integer cropsId);
}