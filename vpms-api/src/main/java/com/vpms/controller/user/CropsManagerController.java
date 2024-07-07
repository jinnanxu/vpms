package com.vpms.controller.user;

import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.CropsItemMapper;
import com.vpms.dao.mapper.CropsMapper;
import com.vpms.dao.model.Crops;
import com.vpms.dao.model.CropsItem;
import com.vpms.dao.model.User;
import com.vpms.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 后台管理：作物信息管理接口
 */
@RequestMapping("/crops")
@RestController
public class CropsManagerController extends ApiBaseController{

    @Autowired
    private CropsMapper cropsMapper;
    @Autowired
    private CropsItemMapper cropsItemMapper;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 我的蔬菜作物列表
     * @param request
     * @param reqMap
     * @return
     */
    @RequestMapping("list")
    public JsonResult cropsList(HttpServletRequest request, Map<String, Object> reqMap) {
        PageView<Crops> pageView = new PageView<>();
        User user = getCurrUser(request);
        pageView = pageView.startPage(reqMap);
        Example example = new Example(Crops.class);
        Example.Criteria criteria = example.createCriteria();
        // 如果要查询
        if(StringUtils.isNotBlank(request.getParameter("kw"))){
            //关键字模糊查询
            criteria.andLike("cropsName", "%"+request.getParameter("kw")+"%");
        }
        criteria.andEqualTo("userId", user.getUserId());
        List<Crops> list = cropsMapper.selectByExampleAndRowBounds(example, new RowBounds(pageView.getFirstResult(), pageView.getMaxresult()));
        // 切割图片、标签
        for (Crops item : list) {
            if(StringUtils.isNotBlank(item.getPics())) {
                String[] url = item.getPics().split(",");
                List<String> urls = new ArrayList<>();
                for (int i=0; i<url.length; i++) {
                    urls.add(url[i]);
                }
                item.setPicList(urls);
            }
        }
        pageView.setRecords(list);
        pageView.setTotalrecord(cropsMapper.selectCountByExample(example));//总记录数
        return JsonResult.success(pageView);
    }

    /**
     * 查询所有作物
     * @param request
     * @return
     */
    @RequestMapping("list/all")
    @AuthRequired
    public JsonResult allCropsList(HttpServletRequest request) {
        PageView<Crops> pageView = new PageView<>();
        User user = getCurrUser(request);
        Example example = new Example(Crops.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());
        criteria.andEqualTo("status", 1);//仅查询正常的作物
        List<Crops> list = cropsMapper.selectByExampleAndRowBounds(example, new RowBounds(pageView.getFirstResult(), pageView.getMaxresult()));
        return JsonResult.success(list);
    }

    /**
     * 新增或更新作物信息
     * @param crops
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateCrops(HttpServletRequest request, Crops crops) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(crops.getCropsId()==null){
                //如果没有ID，说明数据库里还没有这条记录，插入新增
                User user = getCurrUser(request);
                crops.setUserId(user.getUserId());
                crops.setCreateTime(new Date());
                cropsMapper.insertSelective(crops);
            }else{
                //如果已经有ID，说明是更新操作
                cropsMapper.updateByPrimaryKeySelective(crops);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 更新作物状态
     * @param cropsId
     * @param status
     * @return
     */
    @RequestMapping("updateStatus")
    public JsonResult updateStatus(Integer cropsId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            Crops crops = cropsMapper.selectByPrimaryKey(cropsId);
            crops.setStatus(status);
            cropsMapper.updateByPrimaryKeySelective(crops);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 种植作物
     * @param request
     * @param cropsItem
     * @return
     */
    @RequestMapping("plant")
    @AuthRequired
    public JsonResult saveOrUpdateCropsItem(HttpServletRequest request, CropsItem cropsItem) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(cropsItem.getCropsItemId()==null){
                //如果没有ID，说明数据库里还没有这条记录，插入新增
                cropsItem.setPlantTime(new Date());
                cropsItemMapper.insertSelective(cropsItem);
            }else{
                //如果已经有ID，说明是更新操作
                cropsItemMapper.updateByPrimaryKeySelective(cropsItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询某个品种的种植列表
     * @param request
     * @param cropsId
     * @return
     */
    @RequestMapping("plant/list")
    @AuthRequired
    public JsonResult findCropsItems(HttpServletRequest request, Integer cropsId, PageView<CropsItem> pageView) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        User user = getCurrUser(request);
        List<CropsItem> list = cropsItemMapper.selectCropsItemByCropsId(cropsId, user.getUserId(), pageView.getFirstResult(), pageView.getMaxresult());
        pageView.setRecords(list);
        pageView.setTotalrecord(cropsItemMapper.selectCropsItemCountByCropsId(cropsId, user.getUserId()));
        result.setData(pageView);
        return result;
    }

    /**
     * 移除种植作物
     * @param cropsItemId
     * @return
     */
    @RequestMapping("plant/remove")
    @AuthRequired
    public JsonResult removeCropsItems(Integer cropsItemId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        CropsItem cropsItem = cropsItemMapper.selectByPrimaryKey(cropsItemId);
        cropsItem.setStatus(-1);
        cropsItemMapper.updateByPrimaryKeySelective(cropsItem);
        return result;
    }
}
