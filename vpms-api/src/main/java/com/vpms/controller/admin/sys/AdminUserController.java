package com.vpms.controller.admin.sys;

import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.AdminMapper;
import com.vpms.dao.mapper.UserMapper;
import com.vpms.dao.model.Admin;
import com.vpms.dao.model.User;
import com.vpms.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 后台接口：用户管理
 */
@RequestMapping("/admin/user")
@RestController
public class AdminUserController {
    
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询农民用户列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult userList(PageView<User> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<User> list = userMapper.selectStudentList(kw, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(userMapper.selectStudentListCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 注销农民用户
     * @param userId
     * @return
     */
    @RequestMapping("delete")
    public JsonResult delete(Integer userId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        User user = userMapper.selectByPrimaryKey(userId);
        user.setStatus(-1);
        userMapper.updateByPrimaryKeySelective(user);
        result.setData(user);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateApartment(User user) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(user.getUserId()==null){
                userMapper.insertSelective(user);
            }else{
                userMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询专家列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("expert/list")
    public JsonResult apartmentManagerList(PageView<Admin> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Admin> list = adminMapper.selectExpertList(kw, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(adminMapper.selectExpertListCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 农业专家详情
     * @param expertId
     * @return
     */
    @RequestMapping("expert/detail")
    public JsonResult apartmentManagerDetail(Integer expertId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Admin expert = adminMapper.selectByPrimaryKey(expertId);
        result.setData(expert);
        return result;
    }

    /**
     * 注销农业专家信息
     * @param expertId
     * @return
     */
    @RequestMapping("expert/delete")
    public JsonResult expertDelete(Integer expertId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Admin expert = adminMapper.selectByPrimaryKey(expertId);
        expert.setStatus(-1);//注销状态
        adminMapper.updateByPrimaryKeySelective(expert);
        result.setData(expert);
        return result;
    }

    /**
     * 农业专家注册
     * @param user
     * @param user
     * @return
     */
    @RequestMapping("expert/save")
    public JsonResult saveApartmentManager(Admin user) {
        JsonResult result = new JsonResult(true, "0", "注册成功");
        try {
            if(user.getAdminId()==null){
                Admin exist = new Admin();
                exist.setMobile(user.getMobile());
                int count = adminMapper.selectCount(exist);
                if(count > 0){
                    result.setMsg("该手机号已注册");
                    result.setSuccess(false);
                }else{
                    adminMapper.insertSelective(user);
                }
            }else{
                adminMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("注册失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("update/status")
    public JsonResult updateStatus(Integer userId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            user.setStatus(status);
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("失败");
            result.setSuccess(false);
        }
        return result;
    }
}
