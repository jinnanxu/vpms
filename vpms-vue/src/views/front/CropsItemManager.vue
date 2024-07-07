<template>
  <div class="main-contain">
    <div class="top-nav">
      <div class="page-title" style="float: left;width: 300px;">
        <span>蔬菜作物种植管理</span>
        <!-- <el-button size="small" icon="el-icon-plus" class="orange-button" style="float: right;" @click="handleEdit()">添加作物类型</el-button> -->
      </div>
      <!-- 搜索筛选 -->
      <el-form :inline="true" :model="formInline" class="user-search" style="float: right;">
        <el-form-item label="筛选：">
          <el-select v-model="formInline.cropsId" placeholder="请选择" size="small" @change="onChangeTree" clearable>
            <el-option v-for="(item,idx) in allCrops" :key="idx"
              :label="item.cropsName"
              :value="item.cropsId">
            </el-option>
          </el-select>
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
      <el-table-column align="center" prop="crops.cropsName" label="蔬菜作物" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="cropsNo" label="种植编号" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="plantLocation" label="种植区域" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" sortable prop="floor" label="种植时间">
        <template slot-scope="scope">
          <div>{{scope.row.plantTime|timestampToDate}}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="价格" width="100">
        <template slot-scope="scope">
          <strong style="color: #F56C6C;">{{ scope.row.price|amountString }}</strong>
        </template>
      </el-table-column> -->
      <!-- <el-table-column align="center" prop="detail" label="详情" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <strong v-if="scope.row.status == 1" style="color:#67C23A;">正常</strong>
          <strong v-else-if="scope.row.status == -1" style="color:#666666;">无效</strong>
        </template>
      </el-table-column>
      
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button size="mini" class="orange-button" @click="offShelf(scope.$index, scope.row, -1)" v-if="scope.row.status == 1"  icon="el-icon-download">移除</el-button>
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
import { fileUploader } from '@/api/upload.js'
import * as Quill from 'quill'
import { PlantCropsList, RemovePlant, UpdateTreeStatus,PlantTree,LoadAllMyCrops } from '@/api/api.js'

export default {
  data() {
    return {
      nshow: true, //switch开启
      fshow: false, //switch关闭
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
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
        cropsId: "",
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
      allCrops: [],
      fullscreenLoading: false,
      editPlantVisible: false,
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
    this.loadAllMyCrops()
    let cropsId = parseInt(this.$route.query.tid)
    if(!cropsId){
      cropsId=''
    }
    this.formInline.cropsId=cropsId
    this.getdata(this.formInline)
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取果树列表
    getdata(parameter) {
      this.loading = true;
      PlantCropsList(parameter)
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
    },
    // 选择果树下拉框事件
    onChangeTree(val){
      console.log('选中！',val)
      this.formInline.cropsId = val
      //获取某类果树的所有实例
      PlantCropsList(this.formInline)
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
    
    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要移除该果树种植记录吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        RemovePlant({cropsItemId: row.cropsItemId})
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
          }).catch((err) => {
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
    // 查询当前用户的所有作物
    loadAllMyCrops(){
      LoadAllMyCrops().then((res) => {
        this.allCrops=res.data
        console.log('读取所有作物',res)
      })
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

    //种植表单
    handlePlant: function (row) {
      this.editPlantVisible = true;
      this.currTree=row
      this.plantForm.cropsId=row.cropsId
    },
  },
};
</script>

<style scoped>
.user-search {
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
</style>

 
 