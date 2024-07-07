package com.vpms.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "crops_status")
public class CropsStatus {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "crops_status_id")
    private Integer cropsStatusId;

    /**
     * 作物类别ID
     */
    @Column(name = "crops_id")
    private Integer cropsId;

    @Transient
    private Crops crops;

    /**
     * 作物实例ID
     */
    @Column(name = "crops_item_id")
    private Integer cropsItemId;

    @Transient
    private CropsItem cropsItem;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 当前状态
     */
    @Column(name = "curr_status")
    private String currStatus;

    /**
     * 状态更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 图片
     */
    private String pics;

    @Transient
    private List<String> picList;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取物理ID
     *
     * @return crops_status_id - 物理ID
     */
    public Integer getCropsStatusId() {
        return cropsStatusId;
    }

    /**
     * 设置物理ID
     *
     * @param cropsStatusId 物理ID
     */
    public void setCropsStatusId(Integer cropsStatusId) {
        this.cropsStatusId = cropsStatusId;
    }

    /**
     * 获取作物类别ID
     *
     * @return crops_id - 作物类别ID
     */
    public Integer getCropsId() {
        return cropsId;
    }

    /**
     * 设置作物类别ID
     *
     * @param cropsId 作物类别ID
     */
    public void setCropsId(Integer cropsId) {
        this.cropsId = cropsId;
    }

    /**
     * 获取作物实例ID
     *
     * @return crops_item_id - 作物实例ID
     */
    public Integer getCropsItemId() {
        return cropsItemId;
    }

    /**
     * 设置作物实例ID
     *
     * @param cropsItemId 作物实例ID
     */
    public void setCropsItemId(Integer cropsItemId) {
        this.cropsItemId = cropsItemId;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取当前状态
     *
     * @return curr_status - 当前状态
     */
    public String getCurrStatus() {
        return currStatus;
    }

    /**
     * 设置当前状态
     *
     * @param currStatus 当前状态
     */
    public void setCurrStatus(String currStatus) {
        this.currStatus = currStatus == null ? null : currStatus.trim();
    }

    /**
     * 获取状态更新时间
     *
     * @return update_time - 状态更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置状态更新时间
     *
     * @param updateTime 状态更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Crops getCrops() {
        return crops;
    }

    public void setCrops(Crops crops) {
        this.crops = crops;
    }

    public CropsItem getCropsItem() {
        return cropsItem;
    }

    public void setCropsItem(CropsItem cropsItem) {
        this.cropsItem = cropsItem;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}