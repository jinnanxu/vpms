<template>
  <div class="main-contain">
    <!-- 搜索筛选 -->
    <div class="top-nav">
      <p class="page-title" style="float: left;">我的订单</p><!--{{ needInfo.title }}-->
    </div>
    <!-- <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input size="small" v-model="formInline.kw" placeholder="输入标题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" icon="el-icon-search" @click="search" class="black-button">搜索</el-button>
      </el-form-item>
    </el-form> -->
    <!--列表-->
    <el-table size="small" :data="listData" v-loading="loading" border element-loading-text="拼命加载中">
      <!-- <el-table-column align="center" label="订单编号" prop="orderNo"></el-table-column> -->
      <el-table-column align="center" prop="expiration" label="预约到店时间" width="130">
        <template slot-scope="scope">
          <div>{{scope.row.bookTime|timestampToDayHM}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="seat.seatName" label="预约座位">
      </el-table-column>
      <el-table-column align="center" prop="bookHours" label="使用时长" width="80">
        <template slot-scope="scope">{{ scope.row.bookHours }}小时</template>
      </el-table-column>
      <el-table-column align="center" label="订单金额" width="90">
        <template slot-scope="scope">
          ￥<strong style="color: #F56C6C;">{{ scope.row.totalAmount|amountString }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="payType" label="支付方式" width="75">
        <template slot-scope="scope">
          <!--1-在线在线支付；2-使用自习卡；3-到店现金；-->
          <span v-if="scope.row.payType == 1" size="small">在线支付</span>
          <span type="success" v-if="scope.row.payType == 2" size="small">自习卡</span>
          <span type="success" v-if="scope.row.payType == 3" size="small">到店现金</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="remark" label="客户备注" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <!--0-待支付；1-待使用；2-已使用；-1已取消-->
          <span v-if="scope.row.orderStatus == 0" style="color:#F56C6C;">待支付</span>
          <span v-if="scope.row.orderStatus == 1" style="color:#67C23A;">待使用</span>
          <span v-if="scope.row.orderStatus == 2 && scope.row.leaveTime!=null" style="color:#409eff;">已使用</span>
          <span v-if="scope.row.orderStatus == 2 && scope.row.leaveTime==null" style="color:#409eff;">使用中</span>
          <span v-else-if="scope.row.orderStatus == -1" style="color:#999;">已取消</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="使用信息" width="180">
        <template slot-scope="scope" v-if="scope.row.orderStatus==2">
          <div>
            到店：<span style="color: #67C23A;">{{scope.row.checkInTime|timestampToDayHM}}</span>
          </div>
          <div>
            离店：<span style="color: #F56C6C;">{{scope.row.leaveTime|timestampToDayHM}}</span>
          </div>
          <div v-if="scope.row.surcharge>0">
            超时费用：<span style="color: #F56C6C;">{{ scope.row.surcharge|amountString }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" v-if="scope.row.orderStatus==0" class="green-button" @click="onShowPay(scope.row)">支付</el-button>
          <el-button size="mini" v-if="scope.row.orderStatus==1||scope.row.orderStatus==0" class="orange-button" @click="onCancel(scope.row)">取消</el-button>
          <el-button size="mini" class="detail-button" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
    
    <!--支付对话框-->
    <el-dialog
      title="订单支付"
      :visible.sync="payVisible"
      width="45%"
      :close-on-click-modal=false
      :show-close="false"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="orderInfo!=null">
      <el-descriptions-item label="订单编号">{{orderInfo.orderNo}}</el-descriptions-item>
      <el-descriptions-item label="订单金额">
        ￥
        <span style="font-size: 22px; color: #d4571d;">
          {{ orderInfo.totalAmount|amountString }}
        </span>
      </el-descriptions-item>
    </el-descriptions>
    <div class="pay-type">
      <el-form label-width="80px">
        <el-form-item label="支付方式" prop="payType">
          <el-select v-model="payType" clearable placeholder="请选择" size="small">
            <el-option
              label="在线支付"
              :value="1">
            </el-option>
            <el-option
              label="使用自习卡"
              :value="2">
            </el-option>
            <el-option
              label="到店现金"
              :value="3">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="自习卡" prop="studyCardId" v-if="payType==2">
          <el-select v-model="studyCardId" placeholder="请选择" size="small">
            <el-option v-for="(item,idx) in myStudyCards" :key="idx"
              :label="item.cardName"
              :value="item.studyCardId">
            </el-option>
          </el-select>
          <span style="text-align: center;font-size: 12px;color: #999;">前往门店时请携带自习卡以便核销使用。</span>
        </el-form-item>
      </el-form>
    </div>
    <div class="pay-btn">
      <div class="qr-code" v-if="payType==1"><!--支付方式：1-在线支付；2-使用自习卡；3-到店现金；-->
        <img src="http://118.25.89.125:18080/images/pay.png" alt="" style="width: 200px;height: 200px;">
        <p style="text-align: center;font-size: 12px;color: #999;">扫码支付后请点击“确认”按钮</p>
      </div>
      <div class="study-card" v-if="payType==2">
        
      </div>
      <el-button size="small" icon="el-icon-wallet" class="green-button" @click="onPay()">确定</el-button>
      <el-button size="small" icon="el-icon-close" class="orange-button" @click="closeDialog2()">取消</el-button>
    </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="订单详情"
      :visible.sync="orderDetailVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="currDetailData!=null">
      <el-descriptions-item label="预约人" labelStyle="width: 160px;">{{ currDetailData.contact }}</el-descriptions-item>
      <el-descriptions-item label="联系方式" labelStyle="width: 160px;">{{ currDetailData.mobile }}</el-descriptions-item>
      <!-- <el-descriptions-item label="标签">
        <el-tag size="small">{{ currDetailData.tags }}</el-tag>
      </el-descriptions-item> -->
      <el-descriptions-item label="预约座位" labelStyle="width: 160px;">{{ currDetailData.seat.seatName }}</el-descriptions-item>
      <el-descriptions-item label="预约时间" labelStyle="width: 160px;">{{ currDetailData.bookTime|timestampToDayHM}}</el-descriptions-item>
      <el-descriptions-item label="订单金额" labelStyle="width: 160px;">
        ￥<strong style="color: #F56C6C;">{{ currDetailData.totalAmount|amountString }}</strong> 
      </el-descriptions-item>
      <el-descriptions-item label="核销码" labelStyle="width: 160px;">
        <strong style="color: #5d7db3; font-size: 18px;">{{ currDetailData.serviceCode }}</strong> 
      </el-descriptions-item>
      <el-descriptions-item label="预约时长" labelStyle="width: 160px;">{{ currDetailData.bookHours }}小时</el-descriptions-item>
      <el-descriptions-item label="状态" labelStyle="width: 160px;">
        <span v-if="currDetailData.orderStatus == 0" style="color:#F56C6C;">待支付</span>
        <span v-if="currDetailData.orderStatus == 1" style="color:#67C23A;">待使用</span>
        <span v-if="currDetailData.orderStatus == 2 && currDetailData.leaveTime!=null" style="color:#409eff;">已使用</span>
        <span v-if="currDetailData.orderStatus == 2 && currDetailData.leaveTime==null" style="color:#409eff;">使用中</span>
        <span v-else-if="currDetailData.orderStatus == -1" style="color:#999;">已取消</span>
      </el-descriptions-item>
      <el-descriptions-item label="到店时间">{{ currDetailData.checkInTime|timestampToDayHM}}</el-descriptions-item>
      <el-descriptions-item label="离店时间">{{ currDetailData.leaveTime|timestampToDayHM}}</el-descriptions-item>
      <el-descriptions-item label="超时费用" v-if="currDetailData.surcharge>0">￥{{ currDetailData.surcharge|amountString }}</el-descriptions-item>
      <el-descriptions-item label="用户备注" :span="2">{{ currDetailData.remark }}</el-descriptions-item>
      <el-descriptions-item label="温馨提示" :span="2" v-if="currDetailData.shop.bookTips!=null">{{ currDetailData.shop.bookTips }}</el-descriptions-item>
    </el-descriptions>
    <!--支付记录-->
    <el-descriptions class="margin-top" :column="2" border v-if="currDetailData!=null">
      <el-descriptions-item label="支付方式" labelStyle="width: 160px;">
        <span v-if="currDetailData.payType == 1" size="small">在线支付</span>
        <span type="success" v-if="currDetailData.payType == 2" size="small">自习卡</span>
        <span type="success" v-if="currDetailData.payType == 3" size="small">到店现金</span>
      </el-descriptions-item>
      <el-descriptions-item label="自习卡号" labelStyle="width: 160px;" v-if="currDetailData.payType == 2">
        {{ currDetailData.pay.billNo }}
      </el-descriptions-item>
      <el-descriptions-item label="流水号" labelStyle="width: 160px;" v-if="currDetailData.payType == 1">
        {{ currDetailData.pay.billNo }}
      </el-descriptions-item>
      <el-descriptions-item label="支付结果" labelStyle="width: 160px;" v-if="currDetailData.payType == 1">
        <span v-if="currDetailData.pay.payStatus==0">待支付</span>
        <span v-else>已支付</span>
      </el-descriptions-item>
      <el-descriptions-item label="核销结果" labelStyle="width: 160px;" v-if="currDetailData.payType == 1">
        <span v-if="currDetailData.pay.payStatus==0">待到店</span>
        <span v-else>已核销</span>
      </el-descriptions-item>
    </el-descriptions>
    <div class="pay-btn" style="margin: 20px 0;">
      <el-button size="small" icon="el-icon-close" class="orange-button" @click="closeDialog3()">关闭</el-button>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import { MyOrders, CancelOrder,PayOrder, LoadMyCoupons, OrderDetail } from '@/api/api.js'
import Pagination from '../../components/Pagination'
export default {
  data() {
    return {
      editForm: {
        name: '',
      },
      listData: [], //用户数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
      formInline: {
        currentpage: 1,
        limit: 10,
      },
      myStudyCards: [],
      studyCardId: '',
      contentVisible: false, //详情对话框显示与隐藏
      currDetailData: null,
      payVisible: false,
      orderInfo: null,
      detailVisible: false,
      orderDetailVisible: false,
      payType: 1
    }
  },
  // 注册组件
  components: {
    Pagination
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    const cId = this.$route.query.nid
    this.getdata(this.formInline)
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取列表
    getdata(parameter) {
      this.loading = true
      MyOrders(parameter)
        .then(res => {
          this.loading = false
          console.log('订单列表', res)
          if (res.success) {
            this.listData = res.data.records
            this.pageparm.currentPage = res.data.currentpage
            this.pageparm.pageSize = res.data.pageSize
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: 'info',
              message: res.msg
            })
          }
        })
        .catch(err => {
          this.loading = false
          this.$message.error('数据拉取失败，请稍后再试！')
        })
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },
    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

    onShowPay(row){
      LoadMyCoupons({}).then(res => {
        this.myStudyCards = res.data.records
        console.log(res.data)
      })
      this.payVisible = true
      this.orderInfo=row
    },

    //支付完成
    onPay: function(){
      let req={
        orderId: this.orderInfo.orderId,
        payType: this.payType
      }
      PayOrder(req).then((res) => {
        if (res.code==0) {
          this.$message({
            type: 'success',
            message: '支付成功'
          })
          this.payVisible=false
          this.getdata()
        } else {
          this.$message.error(res.msg);
        }
      }).catch((err) => {
        this.$message.error("支付失败！");
      });
      
    },

    // 取消订单
    onCancel(order){
      this.$confirm('确认要取消该订单吗？', '信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let req={
          orderId: order.orderId,
        }
        CancelOrder(req).then((res) => {
          if (res.code==0) {
            this.$message({
              type: 'success',
              message: '订单已取消'
            })
            this.getdata()
          } else {
            this.$message.error(res.msg);
          }
        }).catch((err) => {
          this.$message.error("取消失败！");
        });
      })
    },

    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false
      this.editForm.warehouseId = null
    },
    //显示详情界面
    handleDetail: function (row) {
      console.log(row)
      //从接口读取详情信息
      this.orderDetailVisible = true
      let req = {orderId: row.orderId}
      OrderDetail(req).then((res) => {
        if (res.code==0) {
          this.currDetailData = res.data
        }
      })
      //console.log('显示详情：',this.currDetailData)
    },
    closeDialog2() {
      this.payVisible = false;
    },
    closeDialog3() {
      this.orderDetailVisible = false;
    },
  }
}
</script>

<style scoped>
.main-contain .el-button{
  margin-left: 2px;
}
.dtitle{
  font-size: 16px;
  margin: 10px 0;
  color: #666;
}
.introduction{
  margin: 15px 0;
}
.price{
  color: #d77f57;
}
.show-btn{
  text-align: center;
  margin-top: 100px;
  clear: both;
}
.opname{
  vertical-align: top;
}
.qr-code, .pay-btn{
  text-align: center;
}
.qr-code{
  margin: 12px 0;
}
.main-contain .detail-button{
  background-color: #8cb9be;
  color: #fff;
}
.main-contain .detail-button:hover{
  background-color: #91b5a9;
}
.pay-type{
  margin: 15px 0;
}
</style>