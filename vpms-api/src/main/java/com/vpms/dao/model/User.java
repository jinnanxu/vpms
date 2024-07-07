package com.vpms.dao.model;

import java.util.Date;
import javax.persistence.*;

public class User {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login")
    private Date lastLogin;

    /**
     * 头像
     */
    private String pic;

    private Integer status;

    /**
     * 农场名称
     */
    @Column(name = "farm_name")
    private String farmName;

    /**
     * 获取物理ID
     *
     * @return user_id - 物理ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置物理ID
     *
     * @param userId 物理ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取密码
     *
     * @return pwd - 密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置密码
     *
     * @param pwd 密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取上次登录时间
     *
     * @return last_login - 上次登录时间
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLogin 上次登录时间
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 获取头像
     *
     * @return pic - 头像
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置头像
     *
     * @param pic 头像
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 获取农场名称
     *
     * @return farm_name - 农场名称
     */
    public String getFarmName() {
        return farmName;
    }

    /**
     * 设置农场名称
     *
     * @param farmName 农场名称
     */
    public void setFarmName(String farmName) {
        this.farmName = farmName == null ? null : farmName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}