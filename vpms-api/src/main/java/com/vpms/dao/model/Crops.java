package com.vpms.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Crops {
    @Id
    @Column(name = "crops_id")
    private Integer cropsId;

    /**
     * 作物名称
     */
    @Column(name = "crops_name")
    private String cropsName;

    /**
     * 作物类型：叶菜、瓜类、根茎类、其它
     */
    private String category;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 成熟周期（天）
     */
    private String period;

    /**
     * 预估产量
     */
    private String production;

    /**
     * 图片
     */
    private String pics;

    @Transient
    private List<String> picList;

    /**
     * 录入时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 详情
     */
    private String detail;

    /**
     * @return crops_id
     */
    public Integer getCropsId() {
        return cropsId;
    }

    /**
     * @param cropsId
     */
    public void setCropsId(Integer cropsId) {
        this.cropsId = cropsId;
    }

    /**
     * 获取作物名称
     *
     * @return crops_name - 作物名称
     */
    public String getCropsName() {
        return cropsName;
    }

    /**
     * 设置作物名称
     *
     * @param cropsName 作物名称
     */
    public void setCropsName(String cropsName) {
        this.cropsName = cropsName == null ? null : cropsName.trim();
    }

    /**
     * 获取作物类型：苹果梨、柑橘柚、热带水果、葡萄莓、其它
     *
     * @return category - 作物类型：苹果梨、柑橘柚、热带水果、葡萄莓、其它
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置作物类型：苹果梨、柑橘柚、热带水果、葡萄莓、其它
     *
     * @param category 作物类型：苹果梨、柑橘柚、热带水果、葡萄莓、其它
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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
     * 获取果实成熟周期（天）
     *
     * @return period - 果实成熟周期（天）
     */
    public String getPeriod() {
        return period;
    }

    /**
     * 设置果实成熟周期（天）
     *
     * @param period 果实成熟周期（天）
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    /**
     * 获取预估产量
     *
     * @return production - 预估产量
     */
    public String getProduction() {
        return production;
    }

    /**
     * 设置预估产量
     *
     * @param production 预估产量
     */
    public void setProduction(String production) {
        this.production = production == null ? null : production.trim();
    }

    /**
     * 获取图片
     *
     * @return pics - 图片
     */
    public String getPics() {
        return pics;
    }

    /**
     * 设置图片
     *
     * @param pics 图片
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * 获取录入时间
     *
     * @return create_time - 录入时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置录入时间
     *
     * @param createTime 录入时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取所属用户
     *
     * @return user_id - 所属用户
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置所属用户
     *
     * @param userId 所属用户
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取详情
     *
     * @return detail - 详情
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置详情
     *
     * @param detail 详情
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}