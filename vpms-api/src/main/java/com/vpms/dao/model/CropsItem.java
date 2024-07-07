package com.vpms.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "crops_item")
public class CropsItem {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "crops_item_id")
    private Integer cropsItemId;

    /**
     * 作物类型ID
     */
    @Column(name = "crops_id")
    private Integer cropsId;

    @Transient
    private Crops crops;

    /**
     * 作物编号
     */
    @Column(name = "crops_no")
    private String cropsNo;

    /**
     * 种植时间
     */
    @Column(name = "plant_time")
    private Date plantTime;

    /**
     * 种植位置
     */
    @Column(name = "plant_location")
    private String plantLocation;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 获取物理主键
     *
     * @return crops_item_id - 物理主键
     */
    public Integer getCropsItemId() {
        return cropsItemId;
    }

    /**
     * 设置物理主键
     *
     * @param cropsItemId 物理主键
     */
    public void setCropsItemId(Integer cropsItemId) {
        this.cropsItemId = cropsItemId;
    }

    /**
     * 获取作物类型ID
     *
     * @return crops_id - 作物类型ID
     */
    public Integer getCropsId() {
        return cropsId;
    }

    /**
     * 设置作物类型ID
     *
     * @param cropsId 作物类型ID
     */
    public void setCropsId(Integer cropsId) {
        this.cropsId = cropsId;
    }

    /**
     * 获取作物编号
     *
     * @return crops_no - 作物编号
     */
    public String getCropsNo() {
        return cropsNo;
    }

    /**
     * 设置作物编号
     *
     * @param cropsNo 作物编号
     */
    public void setCropsNo(String cropsNo) {
        this.cropsNo = cropsNo == null ? null : cropsNo.trim();
    }

    /**
     * 获取种植时间
     *
     * @return plant_time - 种植时间
     */
    public Date getPlantTime() {
        return plantTime;
    }

    /**
     * 设置种植时间
     *
     * @param plantTime 种植时间
     */
    public void setPlantTime(Date plantTime) {
        this.plantTime = plantTime;
    }

    /**
     * 获取种植位置
     *
     * @return plant_location - 种植位置
     */
    public String getPlantLocation() {
        return plantLocation;
    }

    /**
     * 设置种植位置
     *
     * @param plantLocation 种植位置
     */
    public void setPlantLocation(String plantLocation) {
        this.plantLocation = plantLocation == null ? null : plantLocation.trim();
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

    public Crops getCrops() {
        return crops;
    }

    public void setCrops(Crops crops) {
        this.crops = crops;
    }
}