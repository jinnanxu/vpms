<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/admin/task/list' }">果树状态管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="果树名称"
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
        >添加</el-button>
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
      <el-table-column align="center" label="更新时间" width="160">
        <template slot-scope="scope">
          <div>{{scope.row.updateTime|timestampToTime}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="作物/订单编号">
        <template slot-scope="scope">
          <div>{{scope.row.tree.treeName}}({{scope.row.treeItem.treeNo}})</div>
        </template>
      </el-table-column>
        <!-- <el-table-column align="center" label="操作人" show-overflow-tooltip>
          <template slot-scope="scope">
            <div>{{ scope.row.user.nickName }}</div>
            <div>{{ scope.row.user.mobile }}</div>
          </template>
        </el-table-column> -->
      <el-table-column align="center" prop="currStatus" label="生长状态">
      </el-table-column>
      <el-table-column align="center" prop="remark" label="详情" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" label="图片" width="120">
        <template slot-scope="scope">
          <el-avatar shape="square" size="medium" :src="scope.row.picList[0]" v-if="scope.row.picList"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <!-- <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
            >修改</el-button
          > -->
          <el-button size="mini" type="danger" @click="offShelf(scope.$index, scope.row, -1)" icon="el-icon-delete">删除</el-button>
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
        label-width="150px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="作物类型" prop="treeId">
            <el-select v-model="editForm.treeId" placeholder="请选择" size="small" @change="onChangeTree">
              <el-option v-for="(item,idx) in allTreeList" :key="idx"
                :label="item.treeName"
                :value="item.treeId">
              </el-option>
            </el-select>
          </el-form-item>
        <el-form-item label="果树编号" prop="treeItemId">
          <el-select v-model="editForm.treeItemId" size="small" filterable placeholder="请选择">
            <el-option
              v-for="item in treeItemList"
              :key="item.treeItemId"
              :label="item.treeNo"
              :value="item.treeItemId">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="当前生长状态" prop="currStatus">
          <el-input
            size="small"
            v-model="editForm.currStatus"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="详情" prop="remark" id='quillEditorQiniu'>
          <el-input type="textarea" placeholder="选填" v-model="editForm.remark" :rows="5"></el-input>
        </el-form-item>
        <el-form-item label="实时图片" prop="role">
          <el-upload 
            class="avatar-uploader showUploader"
            ref="businessLicense"
            action
            multiple
            :auto-upload="false"
            :on-change="uploadSectionFile"
            :limit="6"
          >
            <ul class="img-list">
              <li v-for="(pic,i) in photos" :key="i" class="img-li">
                <img :src="pic" class="avatar" />
              </li>
              <li v-if="photos.length==0 || photos.length<6" class="el-icon-plus avatar-uploader-icon"></li>
            </ul>
          </el-upload>
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
          >保存</el-button
        >
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="果树生长状态详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="roomDescVisible">
        <el-descriptions-item label="作物类型/编号" labelStyle="width: 160px;">
          {{currDetailData.tree.treeName}}
        </el-descriptions-item>
        <el-descriptions-item label="果树编号" labelStyle="width: 160px;">
          {{currDetailData.treeItem.treeNo}}
        </el-descriptions-item>
        <el-descriptions-item label="状态信息">
          {{ currDetailData.currStatus }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
           {{currDetailData.updateTime|timestampToTime}}
        </el-descriptions-item>
        <el-descriptions-item label="备注" span="2">
          {{ currDetailData.remark }}
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
import { TreeStatusList, SaveTreeStatus, DeleteTreeStatus,LoadTreeItems,LoadAllMyCrops } from '@/api/api.js'

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
        treeStatusId: '',
        treeId: '',
        currStatus: '',
        treeItemId: '',
        remark: '',
      },
      saveOrUpdate: 1,
      files: {},
      photos: [],
      // rules表单验证
      rules: {
        currStatus: [{ required: true, message: "请输入生成状态", trigger: "blur" }],
        treeItemId: [{ required: true, message: "请选择果树", trigger: "blur" }],
        treeId: [{ required: true, message: "请选择作物", trigger: "blur" }],
      },
      formInline: {
        kw: "",
        currentPage: 1,
        pageSize: 10,
      },
      listData: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
      currDetailData: {},
      currDetailPics: [],
      allTreeList: [],
      treeItemList: [],
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
      TreeStatusList(parameter)
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
      this.formInline.currentPage = parm.currentPage;
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
      this.LoadAllMyCrops()
      this.editFormVisible = true;
      if (row != undefined && row != "undefined") {
        this.title = "修改信息";
        this.editForm.treeStatusId=row.treeStatusId
        this.editForm.currStatus=row.currStatus
        this.editForm.remark=row.remark
        this.photos=row.pics.split(",")
      } else {
        this.title = "录入果树状态";
        this.editForm.treeId=''
        this.editForm.currStatus=''
        this.editForm.remark=''
        this.editForm.treeItemId=''
        this.photos=[]
      }
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          this.editForm.pics = this.photos.join(",")
          SaveTreeStatus(this.editForm)
            .then((res) => {
              this.fullscreenLoading = false
              this.editFormVisible = false
              this.loading = false
              this.editForm.bathInsertAdoptOrderIds=[]
              if (res.success) {
                this.getdata(this.formInline);
                this.$message({
                  type: "success",
                  message: "保存成功！",
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
    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要删除该状态记录吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        DeleteTreeStatus({statusId: row.treeStatusId})
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
    // 图片上传
    uploadSectionFile(file) {
      console.log('选定图片！',file)
      const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
      const isJPG = typeArr.indexOf(file.raw.type) !== -1;
      const isLt3M = file.size / 1024 / 1024 < 6;
      if (!isJPG) {
        this.$message.error("只能是图片!");
        this.$refs.upload.clearFiles();
        this.files = null;
        return;
      }
      if (!isLt3M) {
        this.$message.error("上传图片大小不能超过 3MB!");
        this.$refs.upload.clearFiles();
        this.files = null;
        return;
      }
      // FormData 对象
      var formData = new FormData();
      // 文件对象
      formData.append("myFile", file.raw)
      console.log('上传图片表单：', formData)
      fileUploader(formData).then((res) => {
        if (res.data.errno==0) {
          //this.photoObj = res.data.data.objectId;
          console.log('图片上传成功！', res.data)
          this.photos.push(res.data.data);
        } else {
          this.$message.error(res.data.message);
        }
      })
    },
    // 查询当前用户的所有果树
    LoadAllMyCrops(){
      LoadAllMyCrops().then((res) => {
        this.allTreeList=res.data
        console.log('读取所有果树',res)
      })
    },

    // 选择果树下拉框事件
    onChangeTree(val){
      console.log('选中！',val)
      this.editForm.treeItemId=''
      this.editForm.treeId=val
      let req = {
        treeId: val
      }
      //获取某类果树的所有实例
      LoadTreeItems(req).then((res) => {
        this.treeItemList=res.data
        console.log('读取所有果树Item',res)
      })
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

 
 