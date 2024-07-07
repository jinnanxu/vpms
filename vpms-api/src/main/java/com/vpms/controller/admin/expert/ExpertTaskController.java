package com.vpms.controller.admin.expert;

import com.alibaba.fastjson.JSONObject;
import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.*;
import com.vpms.dao.model.*;
import com.vpms.service.FarmTaskService;
import com.vpms.service.QuestionService;
import com.vpms.utils.DateUtils;
import com.vpms.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教师：问题管理
 */
@RequestMapping("/expert/task")
@RestController
public class ExpertTaskController {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CropsItemMapper cropsItemMapper;

    @Autowired
    private FarmTaskService farmTaskService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("list")
    @AuthRequired
    public JsonResult treeList(HttpServletRequest request, PageView<Task> page, @RequestParam(required = false) String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        List<Task> list = taskMapper.selectAllTaskList(page.getFirstResult(), page.getMaxresult(), kw);
        for(Task task:list){
            if(StringUtils.isNotBlank(task.getPics())){
                List<String> picList = new ArrayList<>();
                String[] pics = task.getPics().split(",");
                for(String pic:pics){
                    picList.add(pic);
                }
                task.setPicList(picList);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(taskMapper.selectAllTaskListCount(kw));
        result.setData(page);
        return result;
    }

    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateTask(HttpServletRequest request, String advice, Integer taskId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        try {
            Task task = taskMapper.selectByPrimaryKey(taskId);
            task.setAdvice(advice);
            task.setAdviceExpert(expert.getWorkDept()+"："+expert.getRealName()+" "+ DateUtils.formatDateAndTime(new Date()));
            taskMapper.updateByPrimaryKeySelective(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}
