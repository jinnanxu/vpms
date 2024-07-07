package com.vpms.service.impl;

import com.vpms.dao.mapper.TaskMapper;
import com.vpms.dao.model.Task;
import com.vpms.service.FarmTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FarmTaskServiceImpl implements FarmTaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void saveFarmTask(Task task) {
        task.setTaskTime(new Date());
        taskMapper.insertSelective(task);
    }
}
