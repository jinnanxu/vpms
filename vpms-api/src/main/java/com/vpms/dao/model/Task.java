package com.vpms.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Task {
    @Id
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 作业时间
     */
    @Column(name = "task_time")
    private Date taskTime;

    /**
     * 作物类别ID
     */
    @Column(name = "crops_id")
    private Integer cropsId;

    @Transient
    private Crops crops;

    /**
     * 作物单位ID
     */
    @Column(name = "crops_item_id")
    private Integer cropsItemId;

    @Transient
    private CropsItem cropsItem;

    /**
     * 物候期："发芽期","出苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期"
     */
    private String period;

    /**
     * 作业类型："巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","清园","补苗","其他"
     */
    @Column(name = "task_type")
    private String taskType;

    private String advice;

    @Column(name = "advice_expert")
    private String adviceExpert;

    /**
     * 详情
     */
    @Column(name = "task_detail")
    private String taskDetail;

    /**
     * 图片
     */
    private String pics;

    @Transient
    private List<String> picList;

    /**
     * 天气
     */
    private String weather;

    /**
     * 气温
     */
    private String temp;

    /**
     * 操作人
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * @return task_id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取作业时间
     *
     * @return task_time - 作业时间
     */
    public Date getTaskTime() {
        return taskTime;
    }

    /**
     * 设置作业时间
     *
     * @param taskTime 作业时间
     */
    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
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
     * 获取作物单位ID
     *
     * @return crops_item_id - 作物单位ID
     */
    public Integer getCropsItemId() {
        return cropsItemId;
    }

    /**
     * 设置作物单位ID
     *
     * @param cropsItemId 作物单位ID
     */
    public void setCropsItemId(Integer cropsItemId) {
        this.cropsItemId = cropsItemId;
    }

    /**
     * 获取物候期："发芽期","出苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期"
     *
     * @return period - 物候期："发芽期","出苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期"
     */
    public String getPeriod() {
        return period;
    }

    /**
     * 设置物候期："发芽期","出苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期"
     *
     * @param period 物候期："发芽期","出苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期"
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    /**
     * 获取作业类型："巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","清园","补苗","其他"
     *
     * @return task_type - 作业类型："巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","清园","补苗","其他"
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * 设置作业类型："巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","清园","补苗","其他"
     *
     * @param taskType 作业类型："巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","清园","补苗","其他"
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType == null ? null : taskType.trim();
    }

    /**
     * 获取详情
     *
     * @return task_detail - 详情
     */
    public String getTaskDetail() {
        return taskDetail;
    }

    /**
     * 设置详情
     *
     * @param taskDetail 详情
     */
    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail == null ? null : taskDetail.trim();
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
     * 获取天气
     *
     * @return weather - 天气
     */
    public String getWeather() {
        return weather;
    }

    /**
     * 设置天气
     *
     * @param weather 天气
     */
    public void setWeather(String weather) {
        this.weather = weather == null ? null : weather.trim();
    }

    /**
     * 获取气温
     *
     * @return temp - 气温
     */
    public String getTemp() {
        return temp;
    }

    /**
     * 设置气温
     *
     * @param temp 气温
     */
    public void setTemp(String temp) {
        this.temp = temp == null ? null : temp.trim();
    }

    /**
     * 获取操作人
     *
     * @return user_id - 操作人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置操作人
     *
     * @param userId 操作人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
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

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getAdviceExpert() {
        return adviceExpert;
    }

    public void setAdviceExpert(String adviceExpert) {
        this.adviceExpert = adviceExpert;
    }
}