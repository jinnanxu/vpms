<template>
  <div style="margin-top: 50px; max-width: 1280px;">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="ditem">
          <router-link :to="{path: '/manager/service/reset'}">
            <el-statistic title="待解答问题">
              <template slot="formatter">
                <i class="el-icon-s-claim" style="color: #E6A23C; font-size: 32px;"></i>
                <span class="number">5</span>
                <span class="unit">道</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="ditem">
          <router-link :to="{path: '/manager/service/fix'}">
            <el-statistic title="累计解答">
              <template slot="formatter">
                <i class="el-icon-s-flag" style="color: #67c23a; font-size: 32px;"></i>
                <span class="number">12</span>
                <span class="unit">道</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="ditem">
          <router-link :to="{path: '/manager/rooms'}">
            <el-statistic title="已创建知识库">
              <template slot="formatter">
                <i class="el-icon-s-management" style="color: #409eff; font-size: 32px;"></i>
                <span class="number">5</span>
                <span class="unit">条</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="opt-btns">
      <el-col :span="6">
        <el-button type="warning" icon="el-icon-s-claim" @click="checkIn">立即解答</el-button>
      </el-col>
      <el-col :span="6">
        <el-button type="success" icon="el-icon-s-claim" @click="checkIn">问题列表</el-button>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" icon="el-icon-edit" @click="checkOut">添加知识库</el-button>
      </el-col>
    </el-row>
    <!--签到核销-->
    <el-dialog
      title="预约签到"
      :visible.sync="editFormVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeCheckinDialog"
    >
      <el-form
        label-width="120px"
        :model="reqData"
        :rules="formRules"
      >
        <el-form-item label="预约码" prop="serviceCode">
          <el-input
            v-model="reqData.serviceCode"
            auto-complete="off"
            placeholder="请扫描二维码或手动输入预约码"
          ></el-input>
        </el-form-item>
        <el-form-item label="自习卡编号" prop="cardNo" v-if="showCardNo">
          <el-input
            v-model="reqData.cardNo"
            auto-complete="off"
            placeholder="请扫描二维码或手动输入自习卡号"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeCheckinDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitCheckIn()"
          >确定</el-button
        >
      </div>
    </el-dialog>
    <!--离店签退-->
    <el-dialog
      title="离店签退"
      :visible.sync="leaveVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeCheckinDialog"
    >
      <el-form
        label-width="120px"
        :model="reqData"
        :rules="formRules"
      >
        <el-form-item label="顾客手机号" prop="mobile">
          <el-input
            v-model="reqData.mobile"
            auto-complete="off"
            placeholder="请输入预约时的手机号"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeCheckoutDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          class="title"
          @click="onLeave()"
          >确定</el-button
        >
      </div>
    </el-dialog>
    <!--现场购票-->
    <el-dialog
      title="现场购票"
      :visible.sync="buyTicketVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeBuyDialog"
    >
      <el-form
        label-width="120px"
        :model="reqData"
        :rules="formRules"
      >
        <el-form-item label="座位" prop="seatId">
          <el-select v-model="reqData.seatId" filterable placeholder="请选择" style="width: 100%;" size="small">
            <el-option
              v-for="item in seatList"
              :key="item.seatId"
              :label="item.seatName"
              :value="item.seatId">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="时长" prop="buyHours">
          <el-input
            v-model="reqData.buyHours"
            size="small"
            type="number"
            placeholder="请输入购买时长（小时）"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeBuyDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitOrder()"
          >确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { GetDashboardInfo } from '@/api/api.js'
export default {
  data() {
    return {
      dashboard: {},
      editFormVisible: false,
      buyTicketVisible: false,
      leaveVisible: false,
      showCardNo: false,
      reqData:{
        serviceCode: '',
        seatId: '',
        buyHours: '',
        mobile: '',
        cardNo: ''
      },
      formRules: {
        serviceCode: [{ required: true, message: "请输入预约码", trigger: "blur" }],
        seatId: [{ required: true, message: "请选择座位", trigger: "blur" }],
        buyHours: [{ required: true, message: "请输入时长", trigger: "blur" }],
        mobile: [{ required: true, message: "请输入手机号", trigger: "blur" }],
      },
      seatList: []
    };
  },
    created() {
    this.getdata({})
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取控制台数据
    getdata(parameter) {
      this.loading = true;
      GetDashboardInfo(parameter)
        .then((res) => {
          this.loading = false;
          console.log(res)
          this.dashboard=res.data
        })
        .catch((err) => {
          this.loading = false;
          this.$message.error("获取数据失败，请稍后再试！");
        });
    },

  }
}
</script>
<style>
  .like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }
  .el-statistic .con .number{
    font-size: 32px;
    padding: 10px 4px;
    color: #666;
  }
  .remark{
    font-size: 14px; color: #909399;
  }
  .remark .tip{
    padding: 4px 25px;
  }
  .ditem{
    cursor: pointer;
  }
  .ditem a{
    text-decoration: none;
  }
  .opt-btns{
    margin-top: 50px;
    text-align: center;
  }
  .ditem .unit{
    color: #909399;
    font-size: 18px;
  }
</style>