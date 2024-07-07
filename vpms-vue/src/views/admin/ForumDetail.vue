<template>
  <div class="main-contain">
    <div class="show-title">{{forum.title}}</div>
    <div class="show-content">
      <el-tag size="small" type="warning" v-for="(tag,id) in forum.tags.split(';')" :key="id">{{ tag }}</el-tag>
    </div>
    <div class="show-content">{{ forum.user.nickName }}<span class="space"></span>{{ forum.publishTime|timestampToTime }}</div>
    <div v-html="forum.content" class="introduction"></div>
    <div class="comment-list">
      <p style="color:#d77f57;margin: 50px 0 0 0; padding-top: 10px;">评论列表</p>
      <ul>
        <li class="item" v-for="(c,id) in commentList" :key="id">
          <p class="user">{{ c.user.nickName }} <span class="del-icon" @click="removeComment(c.forumCommentId)"><i class="el-icon-delete"></i></span></p>
          <p class="ctime">{{ c.publishTime|timestampToTime }}</p>
          <div class="content">{{ c.content }}</div>
        </li>
      </ul>
    </div>
    <div class="show-btn">
      <el-button size="small" type="info" icon="el-icon-refresh-left" @click="goBack()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { GetForumDetail,DeleteComment } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      formInline: {
        forumId: '',
      },
      editForm: {
        forumId: "",
        content: '',
      },
      title: "发布帖子",
      // rules表单验证
      rules: {
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      forum: {},
      commentList: []
    }
  },
 
  created() {
    this.getdata()
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata() {
      console.log('拉取数据！')
      const forumId = this.$route.query.fid
      console.log('获得帖子ID：',forumId)
      this.editForm.forumId=forumId
      let parameter = {forumId: forumId}
      GetForumDetail(parameter)
        .then(res => {
          this.loading = false
          if (res.success) {
            this.forum = res.data.forum,
            this.commentList = res.data.comments
          } else {
            this.$message({
              type: 'info',
              message: res.msg
            })
          }
        }).catch(err => {
          this.editFormVisible = false
          this.loading = false
          console.log(err)
          this.$message.error('查询失败，请稍后再试！')
        })
    },

    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },

    removeComment(cid){
      let msg = "确定要删除该评论吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.loading = true
        DeleteComment({commentId: cid}).then((res) => {
          this.loading = false
          if (res.success) {
            this.getdata(this.formInline);
            this.$message({
              type: "success",
              message: "删除成功！",
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
          this.$message.error("删除失败！");
        });
      })
    }
  }
}
</script>

<style>

.margin-top{
  width: 80%;
  margin: 0 auto;
}
.userRole {
  width: 100%;
}
.comm-item{
    width: 30%;
}
.court-list .image{
    width: 100%;
}
.green-txt{
  color: rgb(3, 143, 56);
  font-weight: bold;
}
.red-txt{
  color: red;
}
td.a-price{
  font-weight: bold;
  color: #f90;
}
.detail-pics{
  margin: 12px auto;
  width: 750px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
.detail-pics .el-image {
  display: inline-block;
  margin-right: 1%;
  width: 23% !important;
  height: auto !important;
  margin-bottom: 5px;
}
.show-content{
  padding-bottom: 15px;
  margin: 0;
  text-align: center;
  color: #d77f57;
  font-size: 14px;
}
.show-desc{
  padding-top: 15px;
  text-align: center;
  color: #666;
  font-size: 12px;
}
.show-title{
  margin: 15px 0;
  font-size: 20px;
  color: #1ea5a3;
  text-align: center;
}
.show-btn{
  text-align: center;
  margin: 10px 0 30px 0;
}
.el-tag{
  margin-right: 4px;
}
.introduction{
  width: 820px;
  margin: 0 auto;
  color: #666;
}
.main-contain{
  padding-bottom: 20px;
}
.introduction img{
  width: 680px;
  height: auto;
  margin-left: 70px;
}
.space{
  padding: 0 10px;
}
.comment-list{
  width: 820px;
  margin: 0 auto;
  color: #666;
}
.comment-list .item{
  padding: 10px 0;
  border-bottom: 1px solid #1ea5a3;
}
.comment-list .user{
  font-size: 16px;
  color: #1ea5a3;
  margin: 5px 0;
}
.comment-list .ctime{
  color: #888;
  margin: 5px 0;
  font-size: 14px;
}
.comment-list .content{
  font-size: 14px;
}
.comment-list .del-icon{
  cursor: pointer;
  padding: 15px;
}
.comment-list .del-icon:hover{
  color: #d77f57;
}
</style>