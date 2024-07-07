package com.vpms.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Question {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "question_id")
    private Integer questionId;

    @Transient
    private String headPic;

    /**
     * 提问人的ID
     */
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private User user;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 关键字
     */
    @Column(name = "key_word")
    private String keyWord;

    /**
     * 提问时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 状态
     */
    private Integer status;

    private Double rate;

    /**
     * 问题详情
     */
    private String content;

    @Column(name = "search_view_count")
    private Integer searchViewCount;

    /**
     * 获取物理ID
     *
     * @return question_id - 物理ID
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * 设置物理ID
     *
     * @param questionId 物理ID
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * 获取提问学生的ID
     *
     * @return user_id - 提问学生的ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置提问学生的ID
     *
     * @param userId 提问学生的ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取问题标题
     *
     * @return title - 问题标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置问题标题
     *
     * @param title 问题标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取关键字
     *
     * @return key_word - 关键字
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * 设置关键字
     *
     * @param keyWord 关键字
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    /**
     * 获取提问时间
     *
     * @return create_time - 提问时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置提问时间
     *
     * @param createTime 提问时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取问题详情
     *
     * @return content - 问题详情
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置问题详情
     *
     * @param content 问题详情
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getSearchViewCount() {
        return searchViewCount;
    }

    public void setSearchViewCount(Integer searchViewCount) {
        this.searchViewCount = searchViewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}