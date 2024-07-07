<template>
  <div class="main-contain">
    <div class="search-div" style="padding-left: 60px;">
      <input type="text" class="search-ipt" placeholder="输入关键字" v-model="formInline.kw">
      <div class="search-btn" @click="onSearch()"><i class="el-icon-search search-button"></i>搜索</div>
    </div>
    <!--列表-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
      <el-table-column align="center" prop="publishDate" label="提问时间" width="150">
        <template slot-scope="scope">
          {{ scope.row.createTime|timestampToDayHM }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="title" label="标题" show-overflow-tooltip>
      </el-table-column>
      <!-- <el-table-column align="center" prop="teacher.realName" label="提问人" show-overflow-tooltip>
        <template slot-scope="scope">
          <span class="cspan">{{ scope.row.user.majorClass.remark+scope.row.user.majorClass.className }}</span>
          <span class="tspan">{{ scope.row.user.nickName }}</span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" prop="teacher.realName" label="关键字" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.keyWord!=null" class="mtags">
            <el-tag type="warning" size="small" v-for="(itemChild,indexChild) in scope.row.keyWord.split(';')" :key="indexChild">{{ itemChild }}</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="role" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0" type="warning" size="small">待回答</el-tag>
          <el-tag v-else-if="scope.row.status == 1" type="primary" size="small">回答中</el-tag>
          <el-tag v-else-if="scope.row.status == 2" type="info" size="small">已解决</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="toDetailPage(scope.row)" icon="el-icon-document">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
  </div>
</template>

<script>
import { GetMyQuestions } from '@/api/api.js'
import Pagination from '../../components/Pagination'

export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      query: {},
      scenicId: '',
      formInline: {
        page: 1,
        limit: 10,
        kw: '',
      },
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
      GetMyQuestions(parameter)
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

    //显示详情页面
    toDetailPage(row) {
      // this.detailVisible = true;
      // this.currDetailData=row
      this.$router.push({ path: '/question/detail?qid='+row.questionId })
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
.cspan, .ktitle{
  color: #5d7db3;
}
.tspan{
  color: #40a9a8;
  font-weight: bold;
}
.el-tag{
  margin-right: 8px;
}
</style>