<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '#' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>农事作业管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="蔬菜作物名称"
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
      </el-form-item>
    </el-form>
    <!--列表 hotelId roomNo location floor price status-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
      <el-table-column align="center" label="图片" width="80">
        <template slot-scope="scope">
          <el-avatar shape="square" size="medium" :src="scope.row.picList[0]" v-if="scope.row.picList"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column align="center" label="作业时间" width="142">
        <template slot-scope="scope">
          <div>{{scope.row.taskTime|timestampToTime}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="作物/编号">
        <template slot-scope="scope">
          <div>{{scope.row.crops.cropsName}}({{ scope.row.cropsItem.cropsNo }})</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="taskType" label="作业类别" width="100">
      </el-table-column>
      <el-table-column align="center" prop="period" label="物候期" width="130">
      </el-table-column>
      <el-table-column align="center" prop="taskDetail" label="作业详情" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" label="天气情况" width="100">
        <template slot-scope="scope">
          <span style="color: #67C23A;">{{ scope.row.weather }}</span>
          <span style="color: #E6A23C;" v-if="scope.row.temp!=null">{{ scope.row.temp }}°C</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" sortable prop="floor" label="最后更新">
        <template slot-scope="scope">
          <div>{{scope.row.lastUpdate|timestampToTime}}</div>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <el-button
            v-if="scope.row.advice==null"
            size="mini"
            type="success"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
            >建议</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>
    <!-- 编辑界面 -->
    <el-dialog
      :title="title"
      :visible.sync="editFormVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="建议内容" prop="advice">
          <el-input
            size="small"
            v-model="editForm.advice"
            auto-complete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitForm('editForm')"
          v-loading.fullscreen.lock="fullscreenLoading"
          >提交</el-button
        >
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="农事作业详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="roomDescVisible">
        <el-descriptions-item label="作物类型" labelStyle="width: 120px;">
           {{currDetailData.crops.category}}
        </el-descriptions-item>
        <el-descriptions-item label="具体品种" labelStyle="width: 120px;">
           {{currDetailData.crops.cropsName}}
        </el-descriptions-item>
        <el-descriptions-item label="作物编号">
          {{currDetailData.cropsItem.cropsNo}}
        </el-descriptions-item>
        <el-descriptions-item label="种植区域">
          {{currDetailData.cropsItem.plantLocation}}
        </el-descriptions-item>
        <el-descriptions-item label="任务类别">
          <el-tag size="small" type="success">{{ currDetailData.taskType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="物候期">
          <el-tag size="small" type="primary">{{ currDetailData.period }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="天气">{{ currDetailData.weather }}</el-descriptions-item>
        <el-descriptions-item label="气温">{{ currDetailData.temp }}°C</el-descriptions-item>
        <el-descriptions-item label="详细描述" span="2">{{ currDetailData.taskDetail }}</el-descriptions-item>
        <el-descriptions-item label="专家建议" span="2" v-if="currDetailData.advice!=null">
          <div>{{ currDetailData.adviceExpert }}</div>
          <div>{{ currDetailData.advice }}</div>
        </el-descriptions-item>
    </el-descriptions>
    <!-- <div v-html="currDetailData.roomDesc" class="introduction"></div> -->
    <div class="demo-image">
      <div class="block" v-for="fit in currDetailPics" :key="fit">
        <a :href="fit" target="_blank">
        <el-image
          style="width: 100px; height: 100px"
          :src="fit"
          fit="cover"></el-image>
        </a>
      </div>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import { fileUploader } from '@/api/upload.js'
import { ExpertTaskList, SaveTaskAdvice, DeleteTask } from '@/api/api.js'

export default {
  data() {
    return {
      nshow: true, //switch开启
      fshow: false, //switch关闭
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
      title: "",
      editForm: {
        taskId: '',
        advice: '',
      },
      saveOrUpdate: 1,
      files: {},
      photos: [],
      // rules表单验证
      rules: {
        advice: [{ required: true, message: "请输入建议内容", trigger: "blur" }],
      },
      formInline: {
        kw: "",
        currentpage: 1,
        pageSize: 10,
      },
      listData: [],
      // 分页参数
      pageparm: {
        currentpage: 1,
        pageSize: 10,
        total: 10,
      },
      currDetailData: {},
      currDetailPics: [],
      fullscreenLoading: false
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
    // 获取农事作业列表
    getdata(parameter) {
      this.loading = true;
      ExpertTaskList(parameter).then((res) => {
        this.loading = false;
        if (res.success) {
          this.listData = res.data.records;
          this.pageparm.currentpage = res.data.currentPage;
          this.pageparm.pageSize = res.data.pageSize;
          this.pageparm.total = res.data.totalrecord;
        } else {
          this.$message({
            type: "info",
            message: res.msg,
          });
        }
      }).catch((err) => {
        this.editFormVisible = false;
        this.loading = false;
        this.$message.error("获取数据失败，请稍后再试！");
      });
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage;
      this.formInline.pageSize = parm.pageSize;
      this.formInline.total = parm.total
      this.getdata(this.formInline);
    },
    // 搜索事件
    search() {
      console.log('搜索：', this.formInline)
      this.getdata(this.formInline)
    },
    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true
      this.title = "发送建议";
      this.editForm.taskId=row.taskId
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          SaveTaskAdvice(this.editForm)
            .then((res) => {
              this.fullscreenLoading = false
              this.editFormVisible = false
              this.loading = false
              this.editForm.advice=''
              if (res.success) {
                this.getdata(this.formInline);
                this.$message({
                  type: "success",
                  message: "提交成功！",
                });
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
              this.$message.error("保存失败，请稍后再试！");
            });
        } else {
          return false;
        }
      });
    },
    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要删除该作业记录吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        DeleteTask({taskId: row.taskId})
          .then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: "操作成功!",
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
            this.$message.error("操作失败，请稍后再试！");
          });
        })
        .catch((err) => {
          console.error(err)
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    //显示编辑界面
    handleDetail: function (index, row) {
      this.roomDescVisible = true
      console.log('详情对象：', row)
      this.currDetailData = row
      this.currDetailPics = row.pics.split(",")
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },
    closeDialog2() {
      this.roomDescVisible = false;
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
.user-search .el-form--inline .el-form-item{
  vertical-align: baseline;
}
.user-search .el-form-item{
  margin-bottom: 10px;
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
    width: 180px;
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
  .quill-editor {
    height: 250px;
    margin-bottom: 100px;
  }
  .introduction, .demo-image{
    width: 80%;
    margin: 15px auto;
  }
  .el-image{
    cursor: zoom-in;
  }
  .block{
    display: inline-block;
    width: 100px;
    margin: 10px;
  }
</style>

 
 