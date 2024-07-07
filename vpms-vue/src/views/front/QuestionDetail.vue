<template>
  <div class="main-contain" v-if="currDetailData">
    <!-- <p class="page-title">{{ currDetailData.title }}</p> -->
    <div class="show-title">{{currDetailData.question.title}}</div>
    <div class="auth">
      <p class="ptime">
        <el-tag type="primary" size="mini" v-if="currDetailData.question.status==0">待解答</el-tag>
        <el-tag type="warning" size="mini" v-else-if="currDetailData.question.status==1">回答中</el-tag>
        <el-tag type="success" size="mini" v-else-if="currDetailData.question.status==2">已解决</el-tag>
        {{currDetailData.question.createTime|timestampToDayHM}}
      </p>
    </div>
    <div class="info-item">
      <span class="meta">关键词：</span>
      <span class="value">{{ currDetailData.question.keyWord }}</span>
    </div>
    <div class="info-item">
      <span class="meta">浏览量：</span>
      <span class="value">{{ currDetailData.question.searchViewCount }}</span>
    </div>
    <div class="info-item student-info">
      <p class="student">
        <span class="meta">提问人：</span>
        <span class="avatar">
          <el-avatar shape="square" size="small" :src="currDetailData.question.user.pic"></el-avatar>
        </span>
        <span class="student">{{ currDetailData.question.user.nickName }}</span><span class="ptime" v-if="currDetailData.question.user.farmName!=null">[{{ currDetailData.question.user.farmName }}]</span>
      </p>
    </div>
    <div v-html="currDetailData.question.content" class="introduction"></div>
    <div class="antitle">
      <span class="meta">答疑记录：</span>
    </div>
    <!--回答与追问-->
    <div v-for="(aitem,id) in currDetailData.answers" :key="id" class="answers">
      <div class="info-item">
        <span class="meta" v-if="aitem.answerType==1">专家回复：</span>
      </div>
      <div class="info-item" style="line-height: 38px;">
        <span class="meta teacher" v-if="aitem.answerType==1">{{ aitem.expert.realName }} </span>
        <span class="meta student" v-else-if="aitem.answerType==2">
          <el-avatar shape="square" size="medium" :src="aitem.user.pic"></el-avatar>{{ aitem.user.nickName }} 
        </span>
        <span class="ptime"> {{aitem.answerTime|timestampToDayHM}} </span><span class="meta" v-if="aitem.answerType==2"> 追问：</span>
      </div>
      <div v-html="aitem.content" class="introduction"></div>
      <span class="floor">{{ id+1 }}</span>
    </div>
    <div class="show-btn" style="margin-top: 35px;" v-if="loginUser!=null && currDetailData.question.status==1 && currDetailData.question.user.userId==loginUser.userId">
      <p class="qtip">
        <i class="el-icon-warning"></i>
        专家已回答，请在7天内结束问题并给予评价！
      </p>
    </div>
    <div class="show-btn" style="margin-top: 35px;" v-if="loginUser!=null">
      <el-button size="small" type="primary" @click="handleEdit(question)" icon="el-icon-edit" v-if="currDetailData.question.status==1 && currDetailData.question.user.userId==loginUser.userId">追问</el-button>
      <el-button size="small" type="success" @click="handleFinish(question)" icon="el-icon-check" v-if="currDetailData.question.status==1 && currDetailData.question.user.userId==loginUser.userId">结束</el-button>
      <el-button size="small" type="info" icon="el-icon-refresh-left" @click="goBack()">返回</el-button>
    </div>
    <!-- 编辑界面 -->
    <el-dialog
      title="追问"
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
        <el-form-item label="内容正文" prop="content" id='quillEditorQiniu'>
          <quill-editor class="editor" v-model="editForm.content" ref="customQuillEditor" :options="editorOption" >
          </quill-editor>
          <!-- 基于elementUi的上传组件 el-upload-->
          <el-upload
              class="avatar-uploader"
              :action="uploadImgUrl"
              :accept="'image/*'"
              :show-file-list="false"
              :on-success="uploadEditorSuccess"
              :on-error="uploadEditorError"
              :before-upload="beforeEditorUpload">
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          class="title"
          @click="submitForm('editForm')"
          >提交</el-button
        >
      </div>
    </el-dialog>

    <!-- 完结问题界面 -->
    <el-dialog
      title="关闭问题"
      :visible.sync="editFormVisible2"
      width="50%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-row :gutter="16">
        <el-col :span="8" class="profile-item meta"><div class="grid-content bg-purple">您对本次答疑的满意度：</div></el-col>
        <el-col :span="6" class="profile-item">
          <el-rate v-model="editForm2.rate"></el-rate>
        </el-col>
      </el-row>
      <el-row :gutter="16" style="margin: 50px 0 20px 0;">
        <el-col :span="8" style="color: #fff;">.</el-col>
        <el-col :span="8">
          <el-button size="small" @click="closeDialog">取消</el-button>
          <el-button
            size="small"
            type="primary"
            :loading="loading"
            @click="submitFinish()"
            >提交</el-button
          >
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { GetQuestionDetail,StudentSaveAnswer,FinishQuestion } from '@/api/api.js'

const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],        // toggled buttons

  [{'list': 'ordered'}, {'list': 'bullet'}],
  [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
  [{'direction': 'rtl'}],                         // text direction

  [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
  [{'header': [1, 2, 3, 4, 5, 6, false]}],

  [{'color': []}, {'background': []}],          // dropdown with defaults from theme
  [{'font': []}],
  [{'align': []}],
  ['link', 'image'],
  ['clean']                                         // remove formatting button
];

export default {
  data() {
    return {
      loading: false,
      question: {},
      editFormVisible: false,
      editFormVisible2: false,
      currDetailData: null,
      currUser: {},
      uploadImgUrl:"/vpms-server/upload2",
      uploadUrlPath:"没有文件上传",
      quillUpdateImg:false,
      editForm: {
        questionId: '',
        answerType: 2,
        content: '',
      },
      editForm2:{
        questionId: '',
        rate: 0,
      },
      rules: {
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      editorOption:{
        placeholder:'请输入正文',
        modules: {
          toolbar: {
            container: toolbarOptions,  // 工具栏
            handlers: {
              'image': function (value) {
                if (value) {
                  document.querySelector('#quillEditorQiniu .avatar-uploader input').click()
                } else {
                  this.quill.format('image', false);
                }
              },
            }
          }
        }
      },
    }
  },
 
  created() {
    this.getdata()
  },

  computed:{
    loginUser(){
        let lastLogin = parseFloat(localStorage.getItem('loginTime'))
        let now = (new Date()).getTime()
        console.log('当前登录，判断前：',this.$store.state.currUser)
        if(now - lastLogin > 86400000){
          localStorage.removeItem('LoginUser')
          return null
        }else if(localStorage.getItem('LoginUser')!=null){
          let currUser = JSON.parse(localStorage.getItem('LoginUser'))
          return currUser
        }else if(!this.$store.state.currUser.nickName){
          return null
        }else{
          console.log('当前登录???：',this.$store.state.loginUser)
          return this.$store.state.currUser
        }
      }
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata() {
      console.log('拉取数据！', this.$route.query)
      const questionId = this.$route.query.qid
      this.editForm.questionId = this.$route.query.qid
      this.editForm2.questionId = this.$route.query.qid
      let parameter = {questionId: questionId}
      GetQuestionDetail(parameter)
        .then(res => {
          this.loading = false
          if (res.success) {
            this.currDetailData = res.data
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
          this.$message.error('详情查询失败，请稍后再试！')
        })
    },
    
    // 分页插件事件
    callFather(parm) {
      this.formInline.page = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },
    
    //显示编辑界面
    handleEdit: function (row) {
      console.log('显示编辑界面',row)
      this.editFormVisible = true
      this.editForm.content = ''
    },

    handleFinish: function (row) {
      this.editFormVisible2 = true
      this.editForm.rate = 0
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (!valid) {
          return false;
        }
        // 调用保存接口
        StudentSaveAnswer(this.editForm)
          .then((res) => {
            this.editFormVisible = false;
            this.loading = false;
            if (res.success) {
              this.$message({
                type: "success",
                message: "提交成功！",
              });
            } else {
              this.$message({
                type: "info",
                message: res.msg,
              });
            }
            this.getdata(this.formInline)
          })
      });
    },

    //结束问题
    submitFinish(){
      if(this.editForm2.rate==0){
        this.$message({
            type: "error",
            message: '请评分后提交！',
          });
        return 
      }
      FinishQuestion(this.editForm2).then((res) => {
        this.editFormVisible2 = false;
        if (res.success) {
          this.$message({
            type: "success",
            message: "问题已完结！",
          });
        } else {
          this.$message({
            type: "info",
            message: res.msg,
          });
        }
        this.getdata(this.formInline)
      })
    },

    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
      this.editFormVisible2 = false;
    },

    // 富文本编辑器：上传图片成功
    uploadEditorSuccess(res, file) {
      console.log("上传成功")
      // this.$emit('upload',res, file)
      console.log(res, file);
      //拼接出上传的图片在服务器的完整地址
      let imgUrl=res.data[0];
      let type=imgUrl.substring(imgUrl.lastIndexOf(".")+1);
      console.log(type);
      // 获取富文本组件实例
      let quill = this.$refs.customQuillEditor.quill;
      // 获取光标所在位置
      let length = quill.getSelection().index;
      // 插入图片 res.info为服务器返回的图片地址
      quill.insertEmbed(length, 'image', imgUrl)
      // 调整光标到最后
      quill.setSelection(length + 1)
      //取消上传动画
      this.quillUpdateImg = false;
    },
    // 上传(文件)图片失败
    uploadEditorError(res, file) {
      console.log(res);
      console.log(file);
      //页面提示
      this.$message.error('上传图片失败')
      //取消上传动画
      this.quillUpdateImg = false;
    },
    //上传图片之前async
    beforeEditorUpload(res, file){
      //显示上传动画
      this.quillUpdateImg = true;
      console.log(res);
    },
    //上传组件返回的结果
    uploadResult:function (res){
      this.uploadUrlPath=res;
    },
  }
}
</script>

<style scoped >
.detail-pics{
  margin: 12px auto;
  width: 750px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
}
.detail-pics .el-image {
  display: inline-block;
  margin-right: 1%;
  width: 23% !important;
  height: auto !important;
  margin-bottom: 5px;
}
.show-title{
  margin: 15px 0;
  font-size: 22px;
  color: #5d7db3;
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
  padding: 10px;
  min-height: 60px;
  color: #666;
  font-size: 14px;
  overflow: auto;
  width: 860px;
  margin: 0 auto;
  box-sizing: border-box;
}
.main-contain{
  padding-bottom: 20px;
}
.introduction img{
  width: 680px;
  height: auto;
  padding-left: 100px;
}
.auth{
  text-align: center;
  color: #666;
}
.ptime{
  font-size: 12px;
  margin: 8px 0;
}
.pic-title{
  font-size: 16px;
  width: 820px;
  margin: 10px auto;
  color: #888;
  padding: 15px 20px;
  border-top: 1px solid #ccc;
}
.info-item .student{
  color: #888;
}
.student{
  font-size: 14px;
  margin: 0;
}
.student span{
  color: #d77f57;
}
.info-item .student{
  padding-left: 6px;
}
.answers .info-item{
  padding-left: 10px;
}
.info-item{
  margin: 6px 0;
  padding-left: 100px;
}
.info-item .meta{
  color: #666;
  font-size: 14px;
}
.info-item .value{
  color: #5d7db3;
}
.answers{
  border: #b6cad7 1px solid;
  width: 860px;
  margin: 10px auto;
  position: relative;
}
.answers .floor{
  position: absolute;
  right: 10px;
  top: 10px;
  color: rgba(93, 125, 179, .4);
  font-size: 26px;
}
.answers .teacher{
  color: #5d7db3;
  font-weight: bold;
}
.answers .ptime{
  color: #888;
  font-weight: normal;
  padding-left: 8px;
}
.quill-editor {
  height: 250px;
  margin-bottom: 100px;
}
.el-icon-warning{
  color: #d77f57;
  font-size: 16px;
}
.qtip{
  font-size: 14px;
  color: #d77f57;
}
.antitle{
  width: 860px;
  margin: 15px auto 0 auto;
  border-top: #5d7db3 dashed 1px;
  font-size: 14px;
  color: #888;
  padding-top: 15px;;
}
/deep/ .introduction p img{
  width: 65%;
}
.student-info p.student{
  padding: 5px 0;
  line-height: 28px;
}
.student-info p .meta, .student-info p .avatar{
  float: left;
}
.student-info p .cname{
  padding: 0 4px;
  color: #40a9a8;
}
</style>