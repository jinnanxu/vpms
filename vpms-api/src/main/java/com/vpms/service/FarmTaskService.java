package com.vpms.service;

import com.vpms.dao.model.Task;

public interface FarmTaskService {

    /**
     * 插入农事作业记录
     * @param task
     */
    void saveFarmTask(Task task);

}
