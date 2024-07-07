package com.vpms.controller.admin.expert;

import com.alibaba.fastjson.JSONObject;
import com.vpms.author.AuthRequired;
import com.vpms.controller.vo.JsonResult;
import com.vpms.dao.mapper.AdminMapper;
import com.vpms.dao.mapper.AnswerMapper;
import com.vpms.dao.mapper.QuestionMapper;
import com.vpms.dao.mapper.UserMapper;
import com.vpms.dao.model.Admin;
import com.vpms.dao.model.Answer;
import com.vpms.dao.model.Question;
import com.vpms.service.QuestionService;
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

/**
 * 教师：问题管理
 */
@RequestMapping("/expert/question")
@RestController
public class ExpertQuestionController {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private UserMapper userMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询问题列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult toyList(HttpServletRequest request, PageView<Question> page, String kw) {
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Question> list = questionMapper.selectQuestions(kw, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(questionMapper.selectQuestionsCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 查询问题详情
     * @param qid
     * @return
     */
    @RequestMapping("detail")
    public JsonResult detail(Integer qid) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        JSONObject jo = new JSONObject();
        result.setData(jo);
        List<Answer> answers = answerMapper.selectAnswerByQuestion(qid);
        Question question = questionMapper.selectQuestionById(qid);
        question.setSearchViewCount(question.getSearchViewCount()+1);
        questionMapper.updateByPrimaryKeySelective(question);
        question.setUser(userMapper.selectByPrimaryKey(question.getUserId()));
        jo.put("question", question);
        jo.put("answers", answers);
        return result;
    }

    /**
     * 删除问题
     * @param qid
     * @return
     */
    @RequestMapping("delete")
    public JsonResult delete(Integer qid) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
//        Question question = questionMapper.selectByPrimaryKey(qid);
//        question.setStatus(QuestionService.STATUS_DISABLE);
//        questionMapper.updateByPrimaryKeySelective(question);
//        result.setData(question);
        questionMapper.deleteByPrimaryKey(qid);//删除问题
        Answer answer = new Answer();
        answer.setQuestionId(qid);
        answerMapper.delete(answer);//删除回答记录
        return result;
    }

    /**
     * 回答问题
     * @param answer
     * @return
     */
    @RequestMapping("answer")
    @AuthRequired
    public JsonResult saveOrUpdateQuestion(Answer answer, HttpServletRequest request) {
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        JsonResult result = new JsonResult(true, "0", "保存成功");
        try {
            //将专家的答案插入数据库中
            answer.setAdminId(expert.getAdminId());
            answer.setAnswerTime(new Date());
            answerMapper.insertSelective(answer);
            //将问题的状态更新为回答中
            Question question = questionMapper.selectByPrimaryKey(answer.getQuestionId());
            if(question.getStatus() == QuestionService.STATUS_WAIT_ANSWER){
                question.setStatus(QuestionService.STATUS_WAIT_FINISH);
                questionMapper.updateByPrimaryKeySelective(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

}
