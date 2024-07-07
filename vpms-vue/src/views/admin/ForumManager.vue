<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>交流帖子管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="输入标题"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="onSearch"
          >搜索</el-button
        >
      </el-form-item>
    </el-form>
    <!--列表 noticeId hotelName location floor price status-->
    <el-table size="small" :data="listData" highlight-current-row v-loading="loading" border element-loading-text="拼命加载中" style="width: 100%;">
      <el-table-column align="center" prop="user.nickName" label="作者">
        <template slot-scope="scope">
          <div class="avatar-div">
            <el-avatar shape="square" size="medium" fit="fit" :src="scope.row.user.pic==null?'http://118.25.89.125:18080/images/20230309132054typ1q.jpg':scope.row.user.pic"></el-avatar>
            <p>{{ scope.row.user.nickName }}</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="tag" label="标签" width="160">
        <template slot-scope="scope">
          <el-tag size="small" type="warning" v-for="(tag,id) in scope.row.tags.split(';')" :key="id">{{ tag }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="title" label="标题" show-overflow-tooltip width="380">
      </el-table-column>
      <!-- <el-table-column align="center" prop="content" label="详情" show-overflow-tooltip>
      </el-table-column> -->
      <el-table-column align="center" prop="createTime" label="发帖时间" width="160">
        <template slot-scope="scope">
          <div>{{scope.row.publishTime|timestampToDayHM}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="查看" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="toDetailPage(scope.row.froumId)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
  </div>
</template>

<script>
import { AdminForumList } from '@/api/api.js'
import Pagination from '../../components/Pagination'

export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      query: {},
      scenicId: '',
      formInline: {
        page: 1,
        limit: 10,
        kw: '',
        token: localStorage.getItem('logintoken')
      },
      listData: [], //列表数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
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
      AdminForumList(parameter)
        .then(res => {
          this.loading = false
          if (res.success) {
            this.listData = res.data.records
            this.pageparm.currentPage = res.data.currentPage
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
    //详情
    toDetailPage(fid) {
      this.$router.push({ path: '/manager/forum/commentmgr?fid='+fid })
    },

    onSearch(){
      this.getdata(this.formInline)
    },

  }
}
</script>

<style>
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
    border-color: #1ea5a3;
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
  .quill-editor {
    height: 250px;
    margin-bottom: 100px;
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
</style>

 
 