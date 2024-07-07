<template>
  <div class="main-contain">
    <div class="top-nav">
      <div class="page-title" style="float: left;width: 300px;">
        <span>蔬菜作物类型管理</span>
        <!-- <el-button size="small" icon="el-icon-plus" class="orange-button" style="float: right;" @click="handleEdit()">添加作物类型</el-button> -->
      </div>
      <!-- 搜索筛选 -->
      <el-form :inline="true" :model="formInline" class="user-search" style="float: right; margin: 0;">
        <el-form-item label="搜索：">
          <el-input
            size="small"
            v-model="formInline.kw"
            placeholder="输入蔬菜名称"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            size="small"
            class="green-button"
            icon="el-icon-search"
            @click="search"
            >搜索</el-button
          >
          <el-button
              size="small"
              class="orange-button"
              icon="el-icon-plus"
              @click="handleEdit()"
          >添加</el-button>
        </el-form-item>
      </el-form>
    </div>
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
          <el-avatar shape="square" size="large" :src="scope.row.picList[0]" v-if="scope.row.picList"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="category" label="类别">
        <template slot-scope="scope">
          <el-tag size="small" type="success">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="cropsName" label="蔬菜名称">
        <template slot-scope="scope">
          <router-link :to="'item?tid='+scope.row.cropsId">
            <strong class="tname">{{ scope.row.cropsName }}</strong>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="period" label="成熟周期" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="production" label="预估产量" show-overflow-tooltip>
      </el-table-column>
      <!-- <el-table-column align="center" label="价格" width="100">
        <template slot-scope="scope">
          <strong style="color: #F56C6C;">{{ scope.row.price|amountString }}</strong>
        </template>
      </el-table-column> -->
      <!-- <el-table-column align="center" prop="detail" label="详情" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 1" style="color:#67C23A;">正常</span>
          <span v-else-if="scope.row.status == -1" style="color:#666666;">无效</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" sortable prop="floor" label="最后更新">
        <template slot-scope="scope">
          <div>{{scope.row.lastUpdate|timestampToTime}}</div>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" width="380">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <el-button size="mini" @click="handlePlant(scope.row)" icon="el-icon-plus">种植</el-button>
          <el-button
            size="mini"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
            >修改</el-button
          >
          <el-button size="mini" class="orange-button" @click="offShelf(scope.$index, scope.row, -1)" v-if="scope.row.status == 1"  icon="el-icon-download">移除</el-button>
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
        <el-form-item label="蔬菜类别" prop="category">
          <el-select v-model="editForm.category" placeholder="请选择" size="small">
            <el-option label="叶菜类" value="叶菜类"></el-option>
            <el-option label="根菜类" value="根菜类"></el-option>
            <el-option label="茎菜类" value="茎菜类"></el-option>
            <el-option label="花菜类" value="花菜类"></el-option>
            <el-option label="茄果类" value="茄果类"></el-option>
            <el-option label="豆荚类" value="豆荚类"></el-option>
            <el-option label="食用菌类" value="食用菌类"></el-option>
            <el-option label="其它" value="其它"></el-option>
            <!-- '根菜类', '茎菜类', '叶菜类', '花菜类', '茄果类', '豆荚类', '食用菌类' -->
          </el-select>
        </el-form-item>
        <el-form-item label="蔬菜名称" prop="cropsName">
          <el-input
            size="small"
            v-model="editForm.cropsName"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="成熟周期" prop="period">
          <el-input
            size="small"
            v-model="editForm.period"
            placeholder="如：180天"
          ></el-input>
        </el-form-item>
        <el-form-item label="预估产量" prop="production">
          <el-input
            size="small"
            v-model="editForm.production"
            auto-complete="off"
            placeholder="如：80KG"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="价格(元/每棵)" prop="price">
          <el-input
            size="small"
            v-model="editForm.price"
            auto-complete="off"
          ></el-input>
        </el-form-item> -->
        <el-form-item label="详细描述" prop="detail" id='quillEditorQiniu'>
          <el-input type="textarea" v-model="editForm.detail" :rows="5"></el-input>
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

    <el-dialog
      title="种植蔬菜"
      :visible.sync="editPlantVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="plantForm"
        :rules="rules"
        ref="plantForm"
      >
        <el-form-item label="蔬菜名称">
          <el-input
            v-if="currTree.cropsName"
            size="small"
            disabled
            :value="currTree.cropsName"
          ></el-input>
        </el-form-item>
        <el-form-item label="种植编号" prop="cropsNo">
          <el-input
            size="small"
            v-model="plantForm.cropsNo"
          ></el-input>
        </el-form-item>
        <el-form-item label="种植区域" prop="plantLocation">
          <el-input
            size="small"
            v-model="plantForm.plantLocation"
            auto-complete="off"
            placeholder="如：南区平地"
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
          @click="submitPlantForm('plantForm')"
          v-loading.fullscreen.lock="fullscreenLoading"
          >保存</el-button
        >
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="蔬菜信息详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item label="类型">
        <el-tag size="small">{{ currDetailData.category }}</el-tag>
      </el-descriptions-item>
        <el-descriptions-item label="蔬菜名称" labelStyle="width: 120px;">{{ currDetailData.cropsName }}</el-descriptions-item>
        <el-descriptions-item label="成熟周期">{{ currDetailData.period }}</el-descriptions-item>
        <el-descriptions-item label="预估产量">{{ currDetailData.production }}</el-descriptions-item>
        <el-descriptions-item label="详细描述" span="2">{{ currDetailData.detail }}</el-descriptions-item>
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
import { CropsList, UpdateCrops, UpdateTreeStatus,PlantTree } from '@/api/api.js'

export default {
  data() {
    return {
      nshow: true, //switch开启
      fshow: false, //switch关闭
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
      title: "添加蔬菜蔬菜",
      editForm: {
        cropsId: '',
        cropsName: '',
        category: '',
        status: 1,
        price: '',
        period: '',
        production: '',
        pics: '',
        createTime: '',
        adoptedCount: 0,
        isRecommend: 0,
        plantArea: '',
        detail: '',
      },
      saveOrUpdate: 1,
      files: {},
      photos: [],
      // rules表单验证
      rules: {
        cropsName: [{ required: true, message: "请输入名称", trigger: "blur" }],
        category: [{ required: true, message: "请选择类型", trigger: "blur" }],
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
      editPlantVisible: false,
      currTree: {},
      plantForm: {
        cropsId: '',
        cropsNo: '',
        plantLocation: '',
        status: 1
      }
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
    // 获取蔬菜列表
    getdata(parameter) {
      this.loading = true;
      CropsList(parameter)
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
        this.title = "修改蔬菜信息";
        this.saveOrUpdate=2
        this.editForm.cropsId=row.cropsId
        this.editForm.cropsName=row.cropsName
        this.editForm.category=row.category
        this.editForm.status=row.status
        this.editForm.price=row.price
        this.editForm.period=row.period
        this.editForm.production=row.production
        this.editForm.pics=row.pics
        this.editForm.adoptedCount=row.adoptedCount
        this.editForm.isRecommend=row.isRecommend
        this.editForm.plantArea=row.plantArea
        this.editForm.detail=row.detail
        this.photos=row.pics.split(",")
      } else {
        this.title = "添加蔬菜";
        this.editForm.cropsName=''
        this.editForm.category=''
        this.editForm.status=1
        this.editForm.price=''
        this.editForm.period=''
        this.editForm.production=''
        this.editForm.pics=''
        this.editForm.adoptedCount=0
        this.editForm.isRecommend=0
        this.editForm.plantArea=''
        this.editForm.detail=''
        this.photos=[]
      }
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          this.editForm.pics = this.photos.join(",")
          UpdateCrops(this.editForm)
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
    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要注销该蔬菜吗？注销后无法再进行农事作业记录，但历史记录会保留。"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          UpdateTreeStatus({cropsId: row.cropsId, status: status})
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
    //显示详情界面
    handleDetail: function (index, row) {
      this.roomDescVisible = true
      this.currDetailData = row
      this.currDetailPics = row.pics.split(",")
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
      this.editPlantVisible=false
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

    //种植表单
    handlePlant: function (row) {
      this.editPlantVisible = true;
      this.currTree=row
      this.plantForm.cropsId=row.cropsId
    },

    // 提交种植表单
    submitPlantForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          PlantTree(this.plantForm)
            .then((res) => {
              this.fullscreenLoading = false
              this.editFormVisible = false
              this.loading = false
              this.editPlantVisible = false
              if (res.success) {
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
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
.top-nav{
  padding: 10px 0;
}
.top-nav .page-title{
  width: 100%;
  margin: 0;
  line-height: 36px;
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
 .main-contain a{
  text-decoration: none;
 } 
</style>

 
 