package com.vpms.controller.admin.expert;

import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.AdminMapper;
import com.vpms.dao.mapper.KnowledgeMapper;
import com.vpms.dao.model.Admin;
import com.vpms.dao.model.Knowledge;
import com.vpms.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/expert/knowledge")
@RestController
public class ExpertKnowledgeController {

    @Autowired
    private KnowledgeMapper knowledgeMapper;
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
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult dataList(PageView<Knowledge> page, String kw, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        // 查询所有专家发布的知识库，在前端控制是否可以修改与删除
        List<Knowledge> list = knowledgeMapper.selectExpertKnowledge(kw, null, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(knowledgeMapper.selectExpertKnowledgeCount(kw, null));
        result.setData(page);
        return result;
    }

    /**
     * 发布知识库
     * @param knowledgeBase
     * @param request
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateMeta(Knowledge knowledgeBase, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        if(request.getAttribute("loginAdmin")==null){
            return JsonResult.error(500, "登录超时，请重新登录");
        }
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        try {
            if(knowledgeBase.getKnowledgeId() == null){
                knowledgeBase.setUserId(expert.getAdminId());
                knowledgeBase.setPublishDate(new Date());
                knowledgeMapper.insertSelective(knowledgeBase);
            }else{
                knowledgeMapper.updateByPrimaryKeySelective(knowledgeBase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除
     * @param kid
     * @return
     */
    @RequestMapping("delete")
    @AuthRequired
    public JsonResult deleteKnowledge(Integer kid) {
        JsonResult result = new JsonResult(true, "0", "删除成功");
        knowledgeMapper.deleteByPrimaryKey(kid);
        return result;
    }
}
