package com.vpms.dao.mapper;

import com.vpms.dao.model.CropsStatus;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CropsStatusMapper extends Mapper<CropsStatus> {

    List<CropsStatus> selectCropsStatusList(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end, @Param("kw") String kw);

    Integer selectCropsStatusCount(@Param("userId") Integer userId, @Param("kw") String kw);

    List<CropsStatus> selectLastCropsStatus(@Param("orderId") Integer orderId);

}