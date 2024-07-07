package com.vpms.controller.user;

import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.AdminMapper;
import com.vpms.dao.mapper.KnowledgeMapper;
import com.vpms.dao.mapper.UserMapper;
import com.vpms.dao.model.Knowledge;
import com.vpms.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 知识库相关接口
 */
@RestController
public class KnowledgeController extends ApiBaseController {
	
	@Autowired
	private KnowledgeMapper knowledgeMapper;
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AdminMapper adminMapper;

	/**
	 * 查询知识库列表
	 * @param pageView
	 * @param kw
	 * @return
	 */
	@RequestMapping("knowledge/list")
	public JsonResult knowledgeList(PageView<Knowledge> pageView, String kw) {
		//查询知识库，如果输入了关键字则模糊查询
		List<Knowledge> list = knowledgeMapper.searchKnowledge(kw, pageView.getFirstResult(), pageView.getMaxresult());
		for(Knowledge knowledge:list){
			knowledge.setExpert(adminMapper.selectByPrimaryKey(knowledge.getUserId()));
		}
		pageView.setRecords(list);
		pageView.setTotalrecord(knowledgeMapper.searchKnowledgeCount(kw));//总记录数
		return JsonResult.success(pageView);
	}

	/**
	 * 查询条目详情
	 * @param kid
	 * @return
	 */
	@RequestMapping("knowledge/detail")
	public JsonResult detail(@RequestParam Integer kid) {
		JsonResult result = new JsonResult(true, "0", "查询成功");
		Knowledge knowledge = knowledgeMapper.selectByPrimaryKey(kid);
		knowledge.setViewCount(knowledge.getViewCount()+1);//累计浏览数量
		knowledge.setExpert(adminMapper.selectByPrimaryKey(knowledge.getUserId()));
		knowledgeMapper.updateByPrimaryKeySelective(knowledge);
		result.setData(knowledge);
		return result;
	}

}
