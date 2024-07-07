package com.vpms.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Knowledge {
    @Id
    @Column(name = "knowledge_id")
    private Integer knowledgeId;

    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private Admin expert;

    @Column(name = "publish_date")
    private Date publishDate;

    @Column(name = "tags")
    private String tags;

    @Column(name = "view_count")
    private Integer viewCount;

    private String title;

    private String pics;

    private String detail;

    /**
     * @return knowledge_id
     */
    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    /**
     * @param knowledgeId
     */
    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return publish_date
     */
    public Date getPublishDate() {
        return publishDate;
    }

    /**
     * @param publishDate
     */
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * @return pics
     */
    public String getPics() {
        return pics;
    }

    /**
     * @param pics
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * @return detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Admin getExpert() {
        return expert;
    }

    public void setExpert(Admin expert) {
        this.expert = expert;
    }
}