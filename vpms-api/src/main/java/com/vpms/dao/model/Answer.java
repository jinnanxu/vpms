package com.vpms.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Answer {
    /**
     * 物理主键 
     */
    @Id
    @Column(name = "answer_id")
    private Integer answerId;

    /**
     * 对应的问题
     */
    @Column(name = "question_id")
    private Integer questionId;

    /**
     * 专家ID
     */
    @Column(name = "admin_id")
    private Integer adminId;

    @Transient
    private Admin expert;

    /**
     * 农民ID
     */
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private User user;

    /**
     * 类型：1-专家回复；2-农民追问
     */
    @Column(name = "answer_type")
    private Integer answerType;

    /**
     * 发布时间
     */
    @Column(name = "answer_time")
    private Date answerTime;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 获取物理主键 
     *
     * @return answer_id - 物理主键 
     */
    public Integer getAnswerId() {
        return answerId;
    }

    /**
     * 设置物理主键 
     *
     * @param answerId 物理主键 
     */
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    /**
     * 获取对应的问题
     *
     * @return question_id - 对应的问题
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * 设置对应的问题
     *
     * @param questionId 对应的问题
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * 获取专家ID
     *
     * @return admin_id - 专家ID
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置专家ID
     *
     * @param adminId 专家ID
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取农民ID
     *
     * @return user_id - 农民ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置农民ID
     *
     * @param userId 农民ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取类型：1-专家回复；2-农民追问
     *
     * @return answer_type - 类型：1-专家回复；2-农民追问
     */
    public Integer getAnswerType() {
        return answerType;
    }

    /**
     * 设置类型：1-专家回复；2-农民追问
     *
     * @param answerType 类型：1-专家回复；2-农民追问
     */
    public void setAnswerType(Integer answerType) {
        this.answerType = answerType;
    }

    /**
     * 获取发布时间
     *
     * @return answer_time - 发布时间
     */
    public Date getAnswerTime() {
        return answerTime;
    }

    /**
     * 设置发布时间
     *
     * @param answerTime 发布时间
     */
    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    /**
     * 获取正文内容
     *
     * @return content - 正文内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置正文内容
     *
     * @param content 正文内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public User getUser() {
        return user;
    }

    public Admin getExpert() {
        return expert;
    }

    public void setExpert(Admin expert) {
        this.expert = expert;
    }

    public void setUser(User user) {
        this.user = user;
    }
}