<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '#' }">营销信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="请输入关键字"
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
        >发布</el-button>
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
          <el-avatar shape="square" size="large" :src="scope.row.pic.split(',')[0]"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="productName" label="商品名称">
        <template slot-scope="scope">
          <strong class="tname">{{ scope.row.productName }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="quality" label="品质" width="100">
        <template slot-scope="scope">
          <el-tag size="small" type="success" v-if="scope.row.quality=='优'">{{ scope.row.quality }}</el-tag>
          <el-tag size="small" type="primary" v-if="scope.row.quality=='良'">{{ scope.row.quality }}</el-tag>
          <el-tag size="small" type="warning" v-if="scope.row.quality=='一般'">{{ scope.row.quality }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="quantity" label="数量" width="120">
      </el-table-column>
      <el-table-column align="center" label="价格" width="110">
        <template slot-scope="scope">
          ￥<strong style="color: #F56C6C;">{{ scope.row.price|amountString }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="publishMobile" label="发布人信息" width="120">
        <template slot-scope="scope">
          <div>{{ scope.row.publishUser.farmName }}</div>
          <div>{{ scope.row.publishMobile }}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="shortDesc" label="详情" show-overflow-tooltip min-width="320">
      </el-table-column>
      <!-- <el-table-column align="center" prop="shortDesc" label="详情" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 0" style="color:#67C23A;">待售</span>
          <span v-else-if="scope.row.status == 1" style="color:#666666;">已预定</span>
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
          <el-button size="mini" type="success" v-if="scope.row.status == 0 && loginUser.userId!=scope.row.userId" 
            @click="handleBook(scope.row)" icon="el-icon-shopping-cart-2">预定
          </el-button>
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
        <el-form-item label="商品名称" prop="productName">
          <el-input
            size="small"
            v-model="editForm.productName"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="品质" prop="quality">
          <el-select v-model="editForm.quality" placeholder="请选择" size="small">
            <el-option label="优" value="优"></el-option>
            <el-option label="良" value="良"></el-option>
            <el-option label="一般" value="一般"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input
            size="small"
            v-model="editForm.quantity"
            auto-complete="off"
            placeholder="如：80KG"
          ></el-input>
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input
            size="small"
            v-model="editForm.price"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="详细描述" prop="shortDesc" id='quillEditorQiniu'>
          <el-input type="textarea" v-model="editForm.shortDesc" :rows="5"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="publishMobile">
          <el-input
            size="small"
            v-model="editForm.publishMobile"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="展示图片" prop="role">
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

    <!-- 预定界面 -->
    <el-dialog
      title="预定商品"
      :visible.sync="bookVisible"
      width="35%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
      >
        <el-form-item label="商品名称" prop="productName">
          <el-input
            disabled
            size="small"
            v-if="currDetailData.productName"
            :value="currDetailData.productName"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="bookMobile">
          <el-input
            size="small"
            v-model="bookMobile"
            placeholder="请输入您的联系电话"
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
          @click="submitBookForm()"
          v-loading.fullscreen.lock="fullscreenLoading"
          >提交</el-button
        >
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="营销信息详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item label="商品名称" labelStyle="width: 120px;">{{ currDetailData.productName }}</el-descriptions-item>
      <el-descriptions-item label="发布时间" labelStyle="width: 120px;">{{ currDetailData.createTime|timestampToDayHM }}</el-descriptions-item>
      <el-descriptions-item label="品质">{{ currDetailData.quality }}</el-descriptions-item>
      <el-descriptions-item label="数量">{{ currDetailData.quantity }}</el-descriptions-item>
      <el-descriptions-item label="价格">￥<span class="price">{{ currDetailData.price|amountString }}</span></el-descriptions-item>
      <el-descriptions-item label="状态">
        <span v-if="currDetailData.status == 0" style="color:#67C23A;">待售</span>
        <span v-else-if="currDetailData.status == 1" style="color:#666666;">已预定</span>
      </el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ currDetailData.publishMobile }}</el-descriptions-item>
      <el-descriptions-item label=""></el-descriptions-item>
      <el-descriptions-item label="详细描述" span="2">{{ currDetailData.shortDesc }}</el-descriptions-item>
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
import * as Quill from 'quill'
import { MarketInfoList, SaveMarketInfo, DeleteMarketInfo, BookMarketInfo } from '@/api/api.js'

export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
      bookVisible: false,
      title: "发布营销信息",
      editForm: {
        marketInfoId: '',
        productName: '',
        status: 1,
        price: '',
        quality: '',
        quantity: '',
        pic: '',
        status: 0,
        publishMobile: '',
        shortDesc: '',
      },
      saveOrUpdate: 1,
      files: {},
      photos: [],
      // rules表单验证
      rules: {
        productName: [{ required: true, message: "请输入名称", trigger: "blur" }],
        price: [{ required: true, message: "请输入价格", trigger: "blur" }],
        bookMobile: [{ required: true, message: "请输入", trigger: "blur" }],
        quantity: [{ required: true, message: "请输入", trigger: "blur" }],
        quality: [{ required: true, message: "请选择", trigger: "blur" }],
        publishMobile: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
      },
      formInline: {
        page: 1,
        limit: 10,
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
      fullscreenLoading: false,
      bookMobile: '',
      loginUser: null
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

    this.loginUser = JSON.parse(localStorage.getItem('AdminInfo'))
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取果树列表
    getdata(parameter) {
      this.loading = true;
      MarketInfoList(parameter)
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
      this.editFormVisible = true;
      if (row != undefined && row != "undefined") {
        this.title = "修改营销信息";
        this.saveOrUpdate=2
        this.editForm.marketInfoId=row.marketInfoId
        this.editForm.productName=row.productName
        this.editForm.status=row.status
        this.editForm.price=row.price
        this.editForm.quality=row.quality
        this.editForm.quantity=row.quantity
        this.editForm.pic=row.pic
        this.editForm.status=row.status
        this.editForm.publishMobile=row.publishMobile
        this.editForm.shortDesc=row.shortDesc
        this.photos=row.pic.split(",")
      } else {
        this.title = "发布营销信息";
        this.editForm.productName=''
        this.editForm.status=1
        this.editForm.price=''
        this.editForm.quality=''
        this.editForm.quantity=''
        this.editForm.pic=''
        this.editForm.status=0
        this.editForm.publishMobile=''
        this.editForm.shortDesc=''
        this.photos=[]
      }
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          this.editForm.pic = this.photos.join(",")
          SaveMarketInfo(this.editForm)
            .then((res) => {
              this.fullscreenLoading = false
              this.editFormVisible = false
              this.loading = false
              this.editForm.plantArea=''
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

    //提交预定表单
    submitBookForm(){
      if(this.bookMobile==''){
        this.$message.error("请输入联系电话！");
        return 
      }
      let req = {
        marketInfoId: this.currDetailData.marketInfoId, bookMobile: this.bookMobile
      }
      BookMarketInfo(req)
        .then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "预定成功!",
            });
            this.getdata(this.formInline);
            this.bookVisible=false
          } else {
            this.$message({
              type: "info",
              message: res.msg,
            });
          }
        })
    },

    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要删除该营销信息吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        DeleteMarketInfo({mid: row.marketInfoId})
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
        })
    },
    //显示详情界面
    handleDetail: function (index, row) {
      this.roomDescVisible = true
      this.currDetailData = row
      this.currDetailPics = row.pic.split(",")
    },
    // 显示预定对话框
    handleBook: function (row) {
      this.bookVisible = true
      this.bookMobile==''
      this.currDetailData = row
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
      this.bookVisible=false
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
.price{
  color: coral;
  font-weight: bold;
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
    width: 180px;
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
  .tname{
    color: #41a8a8;
    font-size: 14px;
  }
</style>

 
 