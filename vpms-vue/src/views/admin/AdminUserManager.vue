<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>公寓管理员管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.mobile"
          placeholder="输入手机号"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="search"
          >搜索</el-button
        >
        <el-button
          size="small"
          type="primary"
          icon="el-icon-plus"
          @click="handleEdit()"
          >添加</el-button
        >
      </el-form-item>
    </el-form>
    <!--列表 toyId toyName location floor price status-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
      <!-- <el-table-column align="center" prop="role" label="角色">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.role == 1">会员</el-tag>
          <el-tag v-if="scope.row.role == 2" type="warning">管理员</el-tag>
        </template>
      </el-table-column> -->
      <el-table-column align="center" prop="nickName" label="姓名">
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="手机号">
      </el-table-column>
      <el-table-column align="center" prop="role" label="状态">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status == 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="scope.row.status == 2" type="warning">已冻结</el-tag>
          <el-tag size="small" v-else-if="scope.row.status == -1" type="info">已拒绝</el-tag>
          <el-tag size="small" v-else-if="scope.row.status == 0">待审核</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column align="center" sortable prop="floor" label="上次登录">
        <template slot-scope="scope">
          <div>{{scope.row.lastLogin|timestampToTime}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="offShelf(scope.row, 1)" icon="el-icon-check" v-if="scope.row.status == 0">通过</el-button>
          <el-button size="mini" type="danger" @click="offShelf(scope.row, -1)" icon="el-icon-close" v-if="scope.row.status == 0">拒绝</el-button>
          <el-button size="mini" type="warning" @click="offShelf(scope.row, 2)" icon="el-icon-lock" v-if="scope.row.status == 1">冻结</el-button>
          <el-button size="mini" type="success" @click="offShelf(scope.row, 1)" icon="el-icon-check" v-if="scope.row.status == 2">解冻</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import axios from 'axios';
import { AdminManagerList, UpdateApartmentMgrStatus } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      title: "添加",
      editForm: {
        userId: '',
        pwd: '',
        realName: '',
        gender: '',
        mobile: '',
        role: 1,
        isLock: ''
      },
      formInline: {
        currentPage: 1,
        pageSize: 10,
        varLable: "",
        mobile: "",
      },
      listData: [], //用户数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
    };
  },
  // 注册组件
  components: {
    Pagination,
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    this.getdata(this.formInline)
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取用户列表数据
    getdata(parameter) {
      this.loading = true;
      AdminManagerList(parameter)
        .then((res) => {
          this.loading = false;
          if (res.success) {
            this.listData = res.data.records;
            this.pageparm.currentPage = res.data.currentPage;
            this.pageparm.pageSize = res.data.pageSize;
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: "info",
              message: res.msg,
            });
          }
        })
        .catch((err) => {
          this.editFormVisible = false;
          this.loading = false;
          this.$message.error("获取数据失败，请稍后再试！");
        });
    },
    // 分页插件事件
    callFather(parm) {
      console.log('分页插件事件', parm)
      this.formInline.currentPage=parm.currentPage
      this.formInline.pageSize=parm.pageSize
      this.getdata(this.formInline);
    },
    // 搜索事件
    search() {
      this.getdata(this.formInline);
    },

    // 用户状态操作
    offShelf(row, status) {
      let msg = "确定通过该用户申请吗？"
      if(status == 1&&row.status==2){
        msg = "确定要解冻该用户吗？"
      }else if(status == -1){
        msg = "确定拒绝该用户申请吗？"
      }else if(status == 2){
        msg = "确定要冻结该用户吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let req = {managerId: row.adminId, status: status}
          UpdateApartmentMgrStatus(req)
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "更新成功!",
                });
                this.getdata(this.formInline);
              } else {
                this.$message({
                  type: "info",
                  message: res.msg,
                });
              }
            })
            .catch((err) => {
              this.loading = false;
              this.$message.error("更新失败，请稍后再试！");
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    //显示编辑界面
    handleDetail: function (index, row) {
      this.detailVisible = true
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },
    closeDialog2() {
      this.detailVisible = false;
    },
  },
};
</script>

<style scoped>
.user-search {
  margin-top: 20px;
}
.userRole {
  width: 100%;
}
.avatar-uploader /deep/ .el-upload {
    border-radius: 6px;
    background-color: #fff;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #41a8a8;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #8c939d;
    width: 180px;
    height: 140px;
    line-height: 140px;
    text-align: center;
  }
  .showUploader /deep/ .el-upload-list{
    display: none;
  }
  .avatar {
    width: 100%;
    height: 140px;
    display: block;
  }
  .img-list{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
  .img-list .img-li{
    display: inline-block;
    margin-right: 1%;
    width: 32%;
    margin-bottom: 5px;
  }
  .demo-image{
    margin-top: 15px;
  }
</style>

 
 