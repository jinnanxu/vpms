<template>
  <div class="main-contain">
    <div class="search-div">
      <input type="text" class="search-ipt" placeholder="搜索关键字" v-model="formInline.kw">
      <div class="search-btn" @click="onSearch()"><i class="el-icon-search search-button"></i>搜索</div>
    </div>
    <el-table size="small" :data="listData" highlight-current-row :show-header="false" v-loading="loading" border element-loading-text="拼命加载中" style="width: 100%;">
      <el-table-column align="center" prop="title" label="标题" show-overflow-tooltip width="320">
        <template slot-scope="scope">
          <strong class="ktitle">{{ scope.row.title }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="tags" label="标签" show-overflow-tooltip>
        <template slot-scope="scope" v-if="scope.row.tags!=''">
          <el-tag size="small" type="primary" v-for="(tag,id) in scope.row.tags.split(';')" :key="id">{{ tag }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="teacher.realName" label="作者" show-overflow-tooltip>
        <template slot-scope="scope">
          <span class="cspan">{{ scope.row.expert.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="createTime" label="发布时间" width="150">
        <template slot-scope="scope">
          <div>{{scope.row.publishDate|timestampToDayHM}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="viewCount" label="搜索/浏览量" width="80">
        <template slot-scope="scope">
          <i class="el-icon-view"></i>
          {{ scope.row.viewCount }}
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="content" label="详情" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" label="查看" width="150">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-document" @click="toDetailPage(scope.row)" class="green-button">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
    <!--详情对话框-->
    <el-dialog
      title="知识库条目详情"
      :visible.sync="detailVisible"
      width="60%"
      :close-on-click-modal=true
      @click="closeDialog2"
    >
      <el-descriptions class="margin-top" :column="2" border v-if="currDetailData!=null">
          <!-- <el-descriptions-item label="字段1" labelStyle="width: 160px;">{{ currDetailData.seatName }}</el-descriptions-item>
          <el-descriptions-item label="字段2" labelStyle="width: 160px;">{{ currDetailData.price|amountString }}</el-descriptions-item> -->
          <!-- <el-descriptions-item label="状态" labelStyle="width: 160px;">
            <span v-if="currDetailData.status == 1" style="color:#67C23A;">正常</span>
            <span v-else-if="currDetailData.status == -1" style="color:#F56C6C;">已下架</span>
          </el-descriptions-item> -->
      </el-descriptions>
      <div v-html="currDetailData.content" class="introduction" v-if="currDetailData!=null"></div>
    </el-dialog>
  </div>
</template>

<script>
import { GetKnowledgeList,SaveForum } from '@/api/api.js'
import Pagination from '../../components/Pagination'
import { fileUploader } from '@/api/upload.js'

export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      orderInfo: {},
      query: {},
      scenicId: '',
      formInline: {
        page: 1,
        limit: 10,
        kw: '',
      },
      editForm: {
        title: "",
        tags: '',
        content: '',
        status: 1,
      },
      title: "发布帖子",
      // rules表单验证
      rules: {
        title: [{ required: true, message: "请输入", trigger: "blur" }],
        content: [{ required: true, message: "请输入", trigger: "blur" }],
      },
      files: {},
      photos: [],
      photoObj: "",
      listData: [], //列表数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
      currDetailData: null
    }
  },
  // 注册组件
  components: {
    Pagination
  },
 
  created() {
    this.getdata(this.formInline)
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
     // 获取数据列表
     getdata(parameter) {
      console.log('开始获取列表！', parameter)
      this.loading = true
      GetKnowledgeList(parameter)
        .then(res => {
          this.loading = false
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
          this.editFormVisible = false
          this.loading = false
          this.$message.error('系统繁忙，请稍后再试！')
        })
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },

    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
    },

    //显示详情页面
    toDetailPage(row) {
      // this.detailVisible = true;
      // this.currDetailData=row
      this.$router.push({ path: '/knowledge/detail?kid='+row.knowledgeId })
    },

    closeDialog2(){
      this.detailVisible = false;
    },

    onSearch(){
      this.getdata(this.formInline)
    },

  }
}
</script>

<style scoped>
.search-div{
  margin: 12px 0;
}
.search-div .search-ipt{
  height: 35px;
  line-height: 35px;
  width: 220px;
  border-radius: 5px;
  border: 1px solid #808191;
  padding: 0 12px;
}
.search-div .search-btn{
  display: inline-block;
  background-color: #d77f57;
  color: #fff;
  height: 35px;
  line-height: 35px;
  cursor: pointer;
  border-radius: 3px;
  font-size: 14px;
  padding: 0 20px 0 15px;
  text-align: center;
}
.search-div .edit-btn:HOVER, .search-div .search-btn:hover{
  background-color: #84709b;
}
.search-div .search-button, .edit-btn i{
  padding-right: 8px;
  font-size: 18px;
}
.avatar-div{
  display: flex; /* 设置容器为flex布局 */
  align-items: center; /* 水平居中对齐项目 */
  justify-content: center; /* 垂直居中对齐项目 */
  height: 40px; /* 设置容器高度 */
}
.avatar-div p{
  padding-left: 10px;
}
.cspan{
  color: #d77f57;
}
.ktitle{
  color: #666;
}
.tspan{
  color: #40a9a8;
  font-weight: bold;
}
.el-tag{
  margin-right: 8px;
}
</style>