<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>农业专家管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
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
          type="success"
          icon="el-icon-plus"
          @click="handleEdit()"
          >添加农业专家</el-button
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
      <!-- <el-table-column align="center" prop="pic" label="头像" width="80">
        <template slot-scope="scope">
          <el-avatar shape="square" size="medium" :src="scope.row.pic"></el-avatar>
        </template>
      </el-table-column> -->
      <el-table-column align="center" prop="workDept" label="工作单位">
        <template slot-scope="scope">
          {{ scope.row.workDept }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="realName" label="姓名">
      </el-table-column>
      <el-table-column align="center" prop="title" label="职称">
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="手机号">
      </el-table-column>
      <el-table-column align="center" prop="rate" label="评分">
        <template slot-scope="scope">
          <el-rate
            v-model="scope.row.rate"
            disabled
            show-score>
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="role" label="状态" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">正常</el-tag>
          <el-tag v-else type="warning" size="small">已注销</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope" v-if="scope.row.status == 1">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
            >修改</el-button
          >
          <el-button size="mini" type="danger" @click="offShelf(scope.$index, scope.row, 2)" icon="el-icon-delete">注销</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>
    <!--编辑表单-->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="editFormVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="工作单位" prop="workDept">
          <el-input
            size="small"
            v-model="editForm.workDept"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input
            size="small"
            v-model="editForm.realName"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="性别" prop="gender">
          <el-radio v-model="editForm.gender" label="男">男</el-radio>
          <el-radio v-model="editForm.gender" label="女">女</el-radio>
        </el-form-item> -->
        <el-form-item label="职称" prop="title">
        <el-input
          size="small"
          v-model="editForm.title"
          auto-complete="off"
          placeholder=""
        ></el-input>
      </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input
            size="small"
            v-model="editForm.mobile"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="登录密码" prop="pwd">
          <el-input
            size="small"
            v-model="editForm.pwd"
            auto-complete="off"
            placeholder=""
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
          >保存</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import { AdminGetExpertList, AdminUpdateExpert, AdminDeleteExpert } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        adminId: '',
        pwd: '',
        realName: '',
        workDept: '',
        mobile: '',
        title: '',
        rate: 0,
        role: 2,
        status: 1,
      },
      // rules表单验证 
      rules: {
        mobile: [
          { required: false, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        collegeId: [{ required: true, message: '请输选择学院', trigger: 'blur' }],
        pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        kw: "",
      },
      listData: [], //用户数据
      collegeList: [],
      // 分页参数
      pageparm: {
        currentpage: 1,
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
      AdminGetExpertList(parameter)
        .then((res) => {
          this.loading = false;
          if (res.success) {
            this.listData = res.data.records;
            this.pageparm.currentpage = res.data.currentpage;
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
      this.formInline.currentpage=parm.currentPage
      this.formInline.pageSize=parm.pageSize
      this.getdata(this.formInline);
    },
    // 搜索事件
    search() {
      this.getdata(this.formInline);
    },

    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
      // 默认新增
      if (row != undefined && row != "undefined") {
        // 更新
        this.dialogTitle = '修改用户信息'
        this.editForm.adminId = row.adminId;
        this.editForm.pwd = row.pwd;
        this.editForm.realName = row.realName;
        this.editForm.gender = row.gender;
        this.editForm.mobile = row.mobile;
        this.editForm.status = row.status;
        this.editForm.title = row.title;
        this.editForm.rate = row.rate;
        this.editForm.workDept = row.workDept;
      }else{
        this.editForm.pwd = ''
        this.editForm.realName = ''
        this.editForm.gender = ''
        this.editForm.mobile = ''
        this.editForm.status = 1
        this.editForm.title = ''
        this.editForm.rate = 0
        this.editForm.workDept = ''
      } 
    },
    // 提交修改用户表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          AdminUpdateExpert(this.editForm)
            .then((res) => {
              this.editFormVisible = false;
              this.loading = false;
              if (res.success) {
                this.getdata(this.formInline);
                this.$message({
                  type: "success",
                  message: "更新成功！",
                });
                this.photos=[]
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
    // 用户冻结与解冻
    offShelf(index, row, status) {
      let msg = "确定要注销该用户吗？"
      // if(status == 1){
      //   msg = "确定要解冻该用户吗？"
      // }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          AdminDeleteExpert({expertId: row.adminId})
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
    border-color: #5d7db3;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #8c939d;
    width: 32%;
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

 
 