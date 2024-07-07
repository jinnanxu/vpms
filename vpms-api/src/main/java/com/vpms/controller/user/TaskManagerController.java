package com.vpms.controller.user;

import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.CropsItemMapper;
import com.vpms.dao.mapper.CropsMapper;
import com.vpms.dao.mapper.TaskMapper;
import com.vpms.dao.model.CropsItem;
import com.vpms.dao.model.Task;
import com.vpms.dao.model.User;
import com.vpms.service.FarmTaskService;
import com.vpms.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台管理：蔬菜作物商品接口
 */
@RequestMapping("/farmer/task")
@RestController
public class TaskManagerController extends ApiBaseController{

    @Autowired
    private CropsMapper cropsMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CropsItemMapper cropsItemMapper;

    @Autowired
    private FarmTaskService farmTaskService;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult treeList(HttpServletRequest request, PageView<Task> page, @RequestParam(required = false) String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        User user = getCurrUser(request);
        page.setMaxresult(8);
        List<Task> list = taskMapper.selectTaskList(user.getUserId(), page.getFirstResult(), page.getMaxresult(), kw);
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
        page.setTotalrecord(taskMapper.selectTaskCount(user.getUserId(), kw));
        result.setData(page);
        return result;
    }

    /**
     * 查询蔬菜作物项目
     * @param cropsId
     * @return
     */
    @RequestMapping("load/items")
    @AuthRequired
    public JsonResult treeList(Integer cropsId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(CropsItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cropsId", cropsId);
        List<CropsItem> list = cropsItemMapper.selectByExample(example);
        result.setData(list);
        return result;
    }

    /**
     * 新增或更新作业信息
     * @param task
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateTask(HttpServletRequest request, Task task) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(task.getTaskId()==null){
                //如果没有ID，说明数据库里还没有这条记录，插入新增
                User user = getCurrUser(request);
                task.setUserId(user.getUserId());
                farmTaskService.saveFarmTask(task);
            }else{
                //如果已经有ID，说明是更新操作
                taskMapper.updateByPrimaryKeySelective(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("delete")
    public JsonResult updateStatus(Integer taskId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            taskMapper.deleteByPrimaryKey(taskId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("删除失败");
            result.setSuccess(false);
        }
        return result;
    }

}
