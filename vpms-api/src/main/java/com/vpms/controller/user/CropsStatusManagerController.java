package com.vpms.controller.user;

import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.CropsMapper;
import com.vpms.dao.mapper.CropsStatusMapper;
import com.vpms.dao.model.CropsStatus;
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
 * 后台管理：果树商品接口
 */
@RequestMapping("/admin/status")
@RestController
public class CropsStatusManagerController extends ApiBaseController{

    @Autowired
    private CropsMapper cropsMapper;
    @Autowired
    private CropsStatusMapper cropsStatusMapper;
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
    public JsonResult treeList(HttpServletRequest request, PageView<CropsStatus> page, @RequestParam(required = false) String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Task.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("taskTime").desc();//根据上架时间倒序
        User user = getCurrUser(request);
        criteria.andEqualTo("userId", user.getUserId());
        List<CropsStatus> list = cropsStatusMapper.selectCropsStatusList(user.getUserId(), page.getFirstResult(), page.getMaxresult(), kw);
        for(CropsStatus ts:list){
            if(StringUtils.isNotBlank(ts.getPics())){
                List<String> picList = new ArrayList<>();
                String[] pics = ts.getPics().split(",");
                for(String pic:pics){
                    picList.add(pic);
                }
                ts.setPicList(picList);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(cropsStatusMapper.selectCropsStatusCount(user.getUserId(), kw));
        result.setData(page);
        return result;
    }

    /**
     * 新增或更新果树信息
     * @param status
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateCrops(HttpServletRequest request, CropsStatus status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(status.getCropsStatusId()==null){
                User user = getCurrUser(request);
                status.setUserId(user.getUserId());
                status.setUpdateTime(new Date());
                cropsStatusMapper.insertSelective(status);
            }else{
                //如果已经有ID，说明是更新操作
                status.setUpdateTime(new Date());
                cropsStatusMapper.updateByPrimaryKeySelective(status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 更新果树状态
     * @param statusId
     * @return
     */
    @RequestMapping("delete")
    public JsonResult deleteStatus(Integer statusId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            cropsStatusMapper.deleteByPrimaryKey(statusId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("删除失败");
            result.setSuccess(false);
        }
        return result;
    }

}
