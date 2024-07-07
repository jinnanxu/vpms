package com.vpms.controller.user;

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
import com.vpms.dao.model.User;
import com.vpms.service.QuestionService;
import com.vpms.utils.HtmlFilterUtil;
import com.vpms.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 前台：答疑API
 * @author xujinnan
 *
 */
@RequestMapping("/question")
@RestController
public class QuestionApiController extends ApiBaseController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerMapper answerMapper;
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
     * 问题列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult forumList(PageView<Question> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Question> list = questionMapper.selectQuestions(kw, page.getFirstResult(), page.getMaxresult());
        for(Question q:list){
            //更新搜索量
            if(StringUtils.isNotBlank(kw)){
                q.setSearchViewCount(q.getSearchViewCount()+1);
                questionMapper.updateByPrimaryKeySelective(q);
            }
            q.setHeadPic(HtmlFilterUtil.findImageUrl(q.getContent()));
            q.setContent(HtmlFilterUtil.Html2Text(q.getContent()));
        }
        page.setRecords(list);
        page.setTotalrecord(questionMapper.selectQuestionsCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 查询我的问题列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("my/list")
    @AuthRequired
    public JsonResult myQuestionList(HttpServletRequest request, PageView<Question> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        User user = getCurrUser(request);
        List<Question> list = questionMapper.selectMyQuestions(kw, user.getUserId(), page.getFirstResult(), page.getMaxresult());
        for(Question q:list){
            q.setHeadPic(HtmlFilterUtil.findImageUrl(q.getContent()));
            q.setContent(HtmlFilterUtil.Html2Text(q.getContent()));
            //更新搜索量
            if(StringUtils.isNotBlank(kw)){
                q.setSearchViewCount(q.getSearchViewCount()+1);
                questionMapper.updateByPrimaryKeySelective(q);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(questionMapper.selectMyQuestionsCount(kw, user.getUserId()));
        result.setData(page);
        return result;
    }

    /**
     * 问题详情，要把所有的回答、追问都查出来
     * @param questionId
     * @return
     */
    @RequestMapping("detail")
    public JsonResult forumDetail(Integer questionId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        JSONObject jo = new JSONObject();
        result.setData(jo);
        List<Answer> answers = answerMapper.selectAnswerByQuestion(questionId);
        Question question = questionMapper.selectQuestionById(questionId);
        question.setSearchViewCount(question.getSearchViewCount()+1);
        questionMapper.updateByPrimaryKeySelective(question);
        question.setUser(userMapper.selectByPrimaryKey(question.getUserId()));
        jo.put("question", question);
        jo.put("answers", answers);
        return result;
    }

    /**
     * 提问，将数据插入数据库
     * @param request
     * @param question
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveQeustion(HttpServletRequest request, Question question) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        User user = getCurrUser(request);
        try {
            question.setCreateTime(new Date());
            question.setUserId(user.getUserId());//设置提问学生的ID
            questionMapper.insertSelective(question);//写入数据库
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 追问
     * @param request
     * @param answer
     * @return
     */
    @RequestMapping("answer/save")
    public JsonResult saveAnswer(HttpServletRequest request, Answer answer) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        User user = getCurrUser(request);
        try {
            answer.setAnswerTime(new Date());
            answer.setUserId(user.getUserId());
            answerMapper.insertSelective(answer);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 完结问题
     * @param request
     * @param questionId
     * @param rate
     * @return
     */
    @RequestMapping("finish")
    public JsonResult finish(HttpServletRequest request, Integer questionId, Double rate) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        User user = getCurrUser(request);
        try {
            Question q = questionMapper.selectByPrimaryKey(questionId);
            q.setStatus(QuestionService.STATUS_FINISH);//修改问题状态
            q.setRate(rate);//评分
            questionMapper.updateByPrimaryKeySelective(q);//更新数据库记录
            //更新专家的评分：查询专家回答的总问题数及专家当前的评分，再重新计算
            List<Integer> teacherIds = answerMapper.selectAnswerExpertIds(questionId);
            for(Integer tid:teacherIds){
                Admin t = adminMapper.selectByPrimaryKey(tid);
                //查询该专家一共回答过多少次问题
                int count = t.getAnswerTimes();
                //回答总次数乘以评分，再加上新评分后，除以总数+1
                if(t.getRate()==null || t.getRate() == 0){
                    //第一次回答，直接写入评分
                    t.setRate(rate);
                }else{
                    //已有评分，则根据专家的累计回答次数，进行平均分计算
                    double total = count * t.getRate(); //当前评分乘以总回答次数
                    double newRate = (total+rate)/(count+1);//新的总分除以新的总次数
                    BigDecimal bd = new BigDecimal(newRate);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP); //保留一位小数
                    t.setRate(bd.doubleValue());//新评分
                }
                t.setAnswerTimes(t.getAnswerTimes()+1);//累加总回答数
                adminMapper.updateByPrimaryKeySelective(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }
}
