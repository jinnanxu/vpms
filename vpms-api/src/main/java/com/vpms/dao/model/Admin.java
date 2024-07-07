package com.vpms.dao.model;

import javax.persistence.*;

public class Admin {
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    private String mobile;

    private String pwd;

    private Integer role;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "work_dept")
    private String workDept;

    private String title;

    private Integer status;

    private Double rate;

    @Column(name = "answer_times")
    private Integer answerTimes;

    /**
     * @return admin_id
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * @return pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * @return role
     */
    public Integer getRole() {
        return role;
    }

    /**
     * @param role
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * @return real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * @return work_dept
     */
    public String getWorkDept() {
        return workDept;
    }

    /**
     * @param workDept
     */
    public void setWorkDept(String workDept) {
        this.workDept = workDept == null ? null : workDept.trim();
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
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getAnswerTimes() {
        return answerTimes;
    }

    public void setAnswerTimes(Integer answerTimes) {
        this.answerTimes = answerTimes;
    }
}