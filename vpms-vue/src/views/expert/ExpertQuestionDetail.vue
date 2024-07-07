<template>
  <div class="">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>农业答疑</el-breadcrumb-item>
      <el-breadcrumb-item>问题详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="main-contain">
      <div class="show-title">
        <span v-if="question.status==0">[待回答]</span>
        <span v-else-if="question.status==1">[回答中]</span>
        <span v-else-if="question.status==2">[已解决]</span>
        {{question.title}}
      </div>
      <div class="show-content">
        <span class="tspan">{{ question.user.nickName }}</span><span v-show="question.user.farmName!=null">({{ question.user.farmName }})</span>
        <span class="ptime"> {{ question.createTime|timestampToTime }}</span>
      </div>
      <div class="show-content">
        <el-tag size="small" type="warning" v-for="(tag,id) in question.keyWord.split(';')" :key="id">{{ tag }}</el-tag>
      </div>
      <p style="color: #86c8cb; margin-top: 0;">问题详情：</p>
      <div v-html="question.content" class="introduction"></div>
      <div class="comment-list" v-if="answers.length>0">
        <p style="color: #86c8cb;margin-bottom: 0;">答疑历史：</p>
        <ul>
          <li class="item" v-for="(c,id) in answers" :key="id">
            <p v-if="c.answerType==1" class="ctime">
              <!--专家的回答-->
              <span class="cspan">{{ c.expert.realName }}</span>[{{ c.expert.workDept }}]
              于 <span class="atime">{{ c.answerTime|timestampToTime }}</span> 回答
            </p>
            <p v-else-if="c.answerType==2" class="ctime">
              <!--学生的追问-->
              <span class="student">{{ c.user.nickName }}</span>
              于 <span>{{ c.answerTime|timestampToTime }}</span> 追问
            </p>
            <p class="ctime">{{ c.publishTime|timestampToTime }}</p>
            <div v-html="c.content" class="content"></div>
          </li>
        </ul>
      </div>
      <div class="show-btn" style="width: 860px;">
        <el-button size="small" type="success" @click="handleEdit(question)" icon="el-icon-edit" v-if="question.status==1||question.status==0">回答</el-button>
        <el-button size="small" type="info" icon="el-icon-refresh-left" @click="goBack()">返回</el-button>
      </div>
    </div>
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
  </div>
</template>

<script>
import { AdminGetQuestionDetail,ExpertSaveAnswer } from '@/api/api.js'

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
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      formInline: {
        questionId: '',
      },
      editForm: {
        questionId: '',
        answerType: 1,
        content: '',
      },
      title: "回答问题",
      // rules表单验证
      rules: {
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      question: {},
      answers: [],
      uploadImgUrl:"/vpms-server/upload2",
      uploadUrlPath:"没有文件上传",
      quillUpdateImg:false,
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

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata() {
      console.log('拉取数据！')
      const questionId = this.$route.query.qid
      console.log('获得问题ID：',questionId)
      this.editForm.questionId=questionId
      let parameter = {qid: questionId}
      AdminGetQuestionDetail(parameter)
        .then(res => {
          this.loading = false
          if (res.success) {
            this.question = res.data.question,
            this.answers = res.data.answers
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

    //显示编辑界面
    handleEdit: function (row) {
      console.log('显示编辑界面',row)
      this.editFormVisible = true;
      this.editForm.questionId = row.questionId
      this.editForm.content = ''
    },
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (!valid) {
          return false;
        }
        // 调用保存接口
        ExpertSaveAnswer(this.editForm)
          .then((res) => {
            this.editFormVisible = false;
            this.loading = false;
            if (res.success) {
              this.getdata(this.formInline);
              this.$message({
                type: "success",
                message: "回答成功！",
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

    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
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
  color: #666;
}
.ptime{
  color: #888;
}
.show-btn{
  text-align: center;
  margin-top: 50px;
}
.el-tag{
  margin-right: 4px;
}
.introduction{
  width: 860px;
  color: #666;
  font-size: 14px;
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
  width: 860px;
  color: #666;
}
.comment-list .item{
  padding: 10px 0;
  border-bottom: #5d7db3 1px solid;
}
.comment-list .user{
  font-size: 16px;
  color: #1ea5a3;
  margin: 5px 0;
}
.comment-list .ctime{
  color: #888;
  margin: 8px 0;
  font-size: 14px;
}
.comment-list .atime{
  color: #5d7db3;
}
.comment-list .content{
  font-size: 14px;
  overflow: auto;
}
.comment-list .del-icon{
  cursor: pointer;
  padding: 15px;
}
.comment-list .del-icon:hover{
  color: #d77f57;
}
.student{
  color: #40a9a8;
}
.cspan{
  color: #5d7db3;
  font-weight: bold;
}
.tspan{
  color: #40a9a8;
  font-weight: bold;
  padding-right: 10px;
}
</style>