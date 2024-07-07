<template>
  <div class="main-contain" style="width: 1050px;">
    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>田间作业记录</span>
        <el-button size="small" icon="el-icon-plus" class="orange-button" style="float: right;" @click="handleEdit()">添加作业记录</el-button>
      </div>
    </div>
    <!--列表-->   
    <div id="tour" class="index-section" style="margin: 0 auto;">
      <figure class="product" v-for="(item,id) in listData" :key="id">
        <img src="http://118.25.89.125:18080/images/default.jpg" v-if="item.picList==null||item.picList.length==0" @click="toDetailPage(item.cropsId)">
        <a :href="item.picList[0]" target="_blank" v-else>
          <img :src="item.picList[0]">
        </a>
        <figcaption>
          <p class="title" @click="toDetailPage(item.cropsId)">
            <strong>{{ item.taskType }}</strong>
          </p>
          <p class="artist desc">
            <span title="作物类型">{{ item.crops.cropsName }}</span>
            <span class="mpd">{{ item.period }}</span>
          </p>
          <p class="profile" @click="toDetailPage(item.cropsId)" :title="item.taskDetail">{{ item.taskDetail }}</p>
          <div class="buy-div">
            <span class="task-time">{{ item.taskTime|timestampToDayHM }}</span>
            <span class="weather">
              <!--["晴", "多云", "阴", "小雨", "中雨", "大雨", "暴雨", "雪"]-->
              <i class="el-icon-sunny wicon" v-if="item.weather=='晴'" :title="item.weather"></i>
              <i class="el-icon-cloudy-and-sunny wicon" v-else-if="item.weather=='多云'" :title="item.weather"></i>
              <i class="el-icon-partly-cloudy wicon" v-else-if="item.weather=='阴'" :title="item.weather"></i>
              <i class="el-icon-light-rain wicon" v-else-if="item.weather=='小雨'" :title="item.weather"></i>
              <i class="el-icon-heavy-rain wicon" v-else-if="item.weather=='中雨'" :title="item.weather"></i>
              <i class="el-icon-heavy-rain wicon" v-else-if="item.weather=='大雨'" :title="item.weather"></i>
              <i class="el-icon-heavy-rain wicon" v-else-if="item.weather=='暴雨'" :title="item.weather"></i>
              <i class="el-icon-orange wicon" v-else-if="item.weather=='雪'" :title="item.weather"></i>
              <span v-else>{{ item.weather }}</span>
              {{ item.temp }}°C
            </span>
            <!-- <el-button size="mini" icon="el-icon-document" @click="onBookSeat(item)" class="book-btn">详情</el-button> -->
          </div>
          <div class="tips">
            <span v-if="item.advice==null" class="none">暂无专家建议</span>
            <span v-else class="advice" @click="showAdvice(item)"><i class="el-icon-message-solid alert"></i>查看专家建议</span>
          </div>
          <div class="del-icon" @click="removeTask(item)">
            <el-button type="danger" icon="el-icon-delete" circle></el-button>
          </div>
        </figcaption>
      </figure>
    </div>
    <!-- 搜索筛选 -->
    <!-- <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="农作物名称"
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
    </el-form> -->
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
        <el-form-item label="蔬菜作物类型" prop="cropsId">
            <el-select v-model="editForm.cropsId" placeholder="请选择" size="small" @change="onChangeTree">
              <el-option v-for="(item,idx) in allTreeList" :key="idx"
                :label="item.cropsName"
                :value="item.cropsId">
              </el-option>
            </el-select>
          </el-form-item>
        <el-form-item label="种植编号" prop="cropsItemId">
          <el-select v-model="editForm.cropsItemId" size="small" filterable placeholder="请选择">
            <el-option
              v-for="item in cropsItemList"
              :key="item.cropsItemId"
              :label="item.cropsNo+'('+item.plantLocation+')'"
              :value="item.cropsItemId">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="作业类型" prop="taskType">
          <el-select v-model="editForm.taskType" size="small" placeholder="请选择">
            <el-option
              v-for="(item,index) in taskType"
              :key="index"
              :label="item"
              :value="item">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="物候期" prop="period">
          <el-select v-model="editForm.period" size="small" placeholder="请选择">
            <el-option
              v-for="(item,index) in periodList"
              :key="index"
              :label="item"
              :value="item">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="天气" prop="weather">
          <el-select v-model="editForm.weather" size="small" placeholder="请选择">
            <el-option
              v-for="(item,index) in weatherList"
              :key="index"
              :label="item"
              :value="item">
            </el-option>            
          </el-select>
        </el-form-item>
        <el-form-item label="气温（°C）" prop="temp">
          <el-input
            size="small"
            v-model="editForm.temp"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="作业详情" prop="taskDetail" id='quillEditorQiniu'>
          <el-input type="textarea" v-model="editForm.taskDetail" :rows="5" placeholder="如：施用复合肥10公斤、喷洒1:2000多效唑10公斤…"></el-input>
        </el-form-item>
        <el-form-item label="工作图片" prop="role">
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
      title="农事作业详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="roomDescVisible">
        <el-descriptions-item label="农作物类型" labelStyle="width: 120px;">
           {{currDetailData.crops.category}}
        </el-descriptions-item>
        <el-descriptions-item label="具体品种" labelStyle="width: 120px;">
           {{currDetailData.crops.cropsName}}
        </el-descriptions-item>
        <el-descriptions-item label="农作物编号">
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
    <el-dialog
      :visible.sync="adviceVisible"
      width="50%"
      :close-on-click-modal=true
      @click="closeDialog"
    >
      <h3>专家建议</h3>
      <div style="color: #5d7db3; padding: 10px 0;">{{ currTask.adviceExpert }}</div>
      <div style="margin-bottom: 35px;">{{ currTask.advice }}</div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import { fileUploader } from '@/api/upload.js'
import { LoadAllMyCrops, TaskList, SaveTask, DeleteTask,LoadTreeItems } from '@/api/api.js'

export default {
  data() {
    return {
      nshow: true, //switch开启
      fshow: false, //switch关闭
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
      adviceVisible: false,
      title: "",
      editForm: {
        taskId: '',
        cropsId: '',
        cropsItemId: '',
        period: '',
        taskType: '',
        taskDetail: '',
        weather: '',
        temp: '',
      },
      saveOrUpdate: 1,
      files: {},
      photos: [],
      // rules表单验证
      rules: {
        cropsId: [{ required: true, message: "请选择", trigger: "blur" }],
        cropsItemId: [{ required: true, message: "请选择", trigger: "blur" }],
        temp: [{ required: true, message: "请输入气温", trigger: "blur" }],
        period: [{ required: true, message: "请选择物候期", trigger: "blur" }],
        taskType: [{ required: true, message: "请选择作业类型", trigger: "blur" }],
        weather: [{ required: true, message: "请输入天气", trigger: "blur" }],
        bathInsertAdoptOrderIds: [{ required: true, message: "请选择认养订单", trigger: "blur" }],
      },
      formInline: {
        kw: "",
        currentpage: 1,
        pageSize: 8,
      },
      listData: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 8,
        total: 8,
      },
      currDetailData: {},
      currDetailPics: [],
      allTreeList: [],
      cropsItemList: [],
      taskType: [
      "巡园","灌水","施肥","播种","幼苗栽植","植株调整","中耕","培土","采收","清园","补苗","其他"
      ],
      periodList: ["发芽期","幼苗期","分蘖期","越冬期","返青期","拔节期","孕穗期","抽穗期","开花期","成熟期","其它"],
      weatherList: ["晴", "多云", "阴", "小雨", "中雨", "大雨", "暴雨", "雪"],
      fullscreenLoading: false,
      currTask: {}
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
    // 查询当前用户的所有农作物
    LoadAllMyCrops(){
      LoadAllMyCrops().then((res) => {
        this.allTreeList=res.data
        console.log('读取所有农作物',res)
      })
    },

    // 选择农作物下拉框事件
    onChangeTree(val){
      console.log('选中！',val)
      this.editForm.cropsItemId=''
      this.editForm.cropsId=val
      let req = {
        cropsId: val
      }
      //获取某类农作物的所有实例
      LoadTreeItems(req).then((res) => {
        this.cropsItemList=res.data
        console.log('读取所有农作物Item',res)
      })
    },

    // 获取农事作业列表
    getdata(parameter) {
      this.loading = true;
      TaskList(parameter)
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
      this.LoadAllMyCrops()
      this.editFormVisible = true
      if (row != undefined && row != "undefined") {
        this.title = "修改作业信息";
        this.editForm.taskId=row.taskId
        this.editForm.period=row.period
        this.editForm.taskType=row.taskType
        this.editForm.taskDetail=row.taskDetail
        this.editForm.weather=row.weather
        this.editForm.temp=row.temp
        this.editForm.bathInsertAdoptOrderIds=[]
        this.photos=row.pics.split(",")
      } else {
        this.title = "添加农事作业记录";
        this.editForm.period=''
        this.editForm.taskType=''
        this.editForm.taskDetail=''
        this.editForm.weather=''
        this.editForm.temp=''
        this.editForm.bathInsertAdoptOrderIds=[]
        this.photos=[]
      }
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true
          this.editForm.pics = this.photos.join(",")
          SaveTask(this.editForm)
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

    // 显示专家建议
    showAdvice: function (row) {
      this.adviceVisible = true
      this.currTask=row
    },

    // 改变状态
    removeTask(row) {
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
      this.editFormVisible = false
      this.adviceVisible = false
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
.top-nav .page-title{
  width: 100%;
  margin-bottom: 0;
  line-height: 36px;
}
.index-section .product .artist{
  margin: 0;
  color: #41a8a8;
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
  .introduction, .demo-image{
    width: 80%;
    margin: 15px auto;
  }
  .block{
    display: inline-block;
    width: 100px;
    margin: 10px;
  }
  .product .buy-div .task-time{
    font-size: 14px;
    letter-spacing: 0;
  }
  .product .buy-div .weather{
    float: right;
    color: #d87f57;
  }
  .product .buy-div .wicon{
    font-size: 18px;
  }
  .el-button [class*=el-icon-]+span{
    font-size: 14px;
  }
  .product .del-icon{
    visibility: hidden;
    position: absolute;
    right: 10px;
    top: 10px;
  }
  .product{
    position: relative;
  }
  .product:HOVER .del-icon{
    visibility: visible;
  }
  .tips{
    font-size: 14px;
    padding-top: 5px;
  }
  .tips .none{
    color: #999;
  }
  .tips .alert{
    color: #d87f57;
    font-size: 16px;
    padding-right: 5px;
  }
  .tips .advice{
    color: #5d7db3;
  }
  .tips .advice:HOVER{
    color: #d87f57;
    cursor: pointer;
  }
</style>

 
 