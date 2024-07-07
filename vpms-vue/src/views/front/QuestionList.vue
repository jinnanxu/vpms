<!--前台：答疑列表页面-->
<template>
  <div class="main-contain" style="width: 880px;">
    <div class="top-nav">
      <div class="search-div">
        <input type="text" class="search-ipt" placeholder="提问前先搜一搜" v-model="formInline.kw">
        <div class="search-btn" @click="onSearch()"><i class="el-icon-search search-button"></i>搜索</div>
        <div class="edit-btn" @click="handleEdit()"><i class="el-icon-edit"></i>提问</div>
        <div class="myquestion">
          <router-link :to="{path: '/my/questions'}">
            <span>我的提问&gt;</span>
          </router-link>
        </div>
      </div>
    </div>
    <ul class="info-list" >
      <li class="item2" v-for="(item,id) in listData" :key="id">
        <img :src="item.headPic" alt="" @click="toDetailPage(item.questionId)" v-if="item.headPic!=null&&item.headPic!=''" class="img">
        <div v-else class="default-img img"></div>
        <div class="content" @click="toDetailPage(item.questionId)">
          <p class="title">{{ item.title }}</p>
          <div class="mtags">
            <span v-if="item.keyWord!=null" class="mtags">
              <el-tag type="warning" size="small" v-for="(itemChild,indexChild) in item.keyWord.split(';')" :key="indexChild">{{ itemChild }}</el-tag>
            </span>
            <span class="view">
              <i class="el-icon-view"></i>
              {{ item.searchViewCount }}
            </span>
          </div>
          <span class="student">{{ item.user.nickName }}</span><span class="ptime" v-if="item.user.farmName!=null">[{{ item.user.farmName }}]</span>
          <span class="ptime">{{item.createTime|timestampToDayHM}}</span>
          <div class="pcontent">{{ item.content }}</div>
        </div>
        <div class="status-tag tag-success" v-if="item.status==1">回答中</div>
        <div class="status-tag tag-wating" v-else-if="item.status==0">待回答</div>
        <div class="status-tag tag-finish" v-else-if="item.status==2">已解决</div>
        <div class="clear"></div>
      </li>
    </ul>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>

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
        <el-form-item label="问题标题" prop="title">
          <el-input
            size="small"
            v-model="editForm.title"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="关键字" prop="keyWord">
          <el-input
            size="small"
            v-model="editForm.keyWord"
            auto-complete="off"
            placeholder="如有多个请以分别隔开，如：数据库;SQL"
          ></el-input>
        </el-form-item>
        <el-form-item label="问题正文" prop="content" id='quillEditorQiniu'>
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
      <div slot="footer" class="dialog-footer" style="margin-top: 20px;">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitForm('editForm')"
          >发布</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { GetQuestionList, SaveQuestion, FindAvCollege } from '@/api/api.js'
import Pagination from '../../components/Pagination'
import { fileUploader } from '@/api/upload.js'
import { Loading } from 'element-ui'

const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
  ['blockquote', 'code-block'],

  [{'header': 1}, {'header': 2}],               // custom button values
  [{'list': 'ordered'}, {'list': 'bullet'}],
  [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
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
      query: {},
      formInline: {
        kw: '',
        currentPage: 1,
        pageSize: 10,
      },
      listData: [], //列表数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
      loading: false,
      editForm: {
        title: '',
        keyWord: '',
        content: '',
        status: 0,
        rate: 0,
        searchViewCount: 0
      },
      title: "发布提问",
      // rules表单验证
      rules: {
        title: [{ required: true, message: "请输入", trigger: "blur" }],
        content: [{ required: true, message: "请输入", trigger: "blur" }],
      },
      files: {},
      photos: [],
      editFormVisible: false,
      collegeList: [],
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
      let loadingInstance = Loading.service({target: '.info-list', text: '正在加载...'})
      GetQuestionList(parameter)
        .then(res => {
          loadingInstance.close()
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
      this.formInline.currentPage = parm.currentPage
      this.formInline.pageSize = parm.pageSize
      this.getdata(this.formInline)
    },
    
    //进入某个详情页面
    toDetailPage(sid) {
      this.$router.push({ path: '/question/detail?qid='+sid })
    },
    
    onSearch(){
      this.getdata(this.formInline)
    },

    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
      this.title = "发布问题";
      this.editForm.title = ""
      this.editForm.keyWord = ""
      this.editForm.content = ""
      this.photos=[]
      FindAvCollege({}).then((res) => {
        this.collegeList=res.data
      })
    },
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          SaveQuestion(this.editForm)
            .then((res) => {
              this.editFormVisible = false;
              this.loading = false;
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
            }).catch((err) => {
              this.editFormVisible = false;
              this.loading = false;
              this.$message.error("保存失败，请稍后再试！");
            });
        } else {
          return false;
        }
      });
    },

    onSearch(){
      this.getdata(this.formInline)
    },

    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false
    },
    closeDialog2() {
      this.detailVisible = false;
    },
    handlePreview(file) {
      console.log(file);
    },
    handleRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    uploadSectionFile(file) {
      console.log('选定图片！')
      const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
      const isJPG = typeArr.indexOf(file.raw.type) !== -1;
      const isLt3M = file.size / 1024 / 1024 < 3;
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
      this.files = file.raw;
      // FormData 对象
      var formData = new FormData();
      // 文件对象
      formData.append("myFile", file.raw)
      console.log('上传图片表单：', formData)
      let loadingInstance = Loading.service({target: '.info-list', text: '正在上传图片...'})
      fileUploader(formData).then((res) => {
        loadingInstance.close()
        if (res.data.errno==0) {
          console.log('图片上传成功！', res.data)
          this.photos.push(res.data.data);
          if(this.editForm.pics!=""){
            this.editForm.pics = this.editForm.pics+","+res.data.data
          }else{
            this.editForm.pics = res.data.data
          }
          console.log(this.editForm.pics)
        } else {
          this.$message.error(res.data.message);
        }
      })
    },
    // 上传图片成功
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
      //  const res1 = await uploadImage()
      // console.log(res1,'=====');
      // this.$emit('before',res, file)
      console.log(res);
      console.log(file);
    },
    //上传组件返回的结果
    uploadResult:function (res){
      this.uploadUrlPath=res;
    },
  }
}
</script>

<style scoped>
.top-nav{
  height: 45px;
  margin-top: 10px;
}
.el-tag{
  margin-right: 4px;
}
.info-list{
  margin-top: 8px;
}
.info-list .item2{
  overflow: hidden;
  clear: both;
  margin: 0 auto 20px auto;
  border: 1px solid #ccc;
  padding: 10px 5px;
  box-shadow: 2px 2px 8px #ccc;
  position: relative;
}
.info-list .status-tag{
  position: absolute;
  top: -22px;
  right: -55px;
  rotate: 45deg;
  color: #fff;
  width: 120px;
  height: 60px;
  line-height: 90px;
  text-align: center;
  font-size: 12px;
}
.info-list .tag-success{
  background-color: #5d7db3;
}
.info-list .tag-finish{
  background-color: #40a9a8;
}
.info-list .tag-wating{
  background-color: #d77f57;
}
.info-list .item2 .img{
  width: 160px;
  height: 120px;
  float: left;
}
.info-list .item2 .content{
  display: inline-block;
  width: 660px;
  padding-left: 15px;
}
.item2 .content .title{
  margin: 0 0 8px 0;
  font-size: 18px;
  display: inline-block;
  padding-left: 5px;
  color: #666;
}
.info-list .item2 .content:hover .title{
  color: #5d7db3;
}
.info-list .item2 .content:hover{
  cursor: pointer;
}
.info-list .item2 img:hover{
  cursor: pointer;
}
.content .desc{
  margin: 8px 0;
  font-size: 14px;
  color: #666;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
.clear{
  clear: both;
}
.ptime{
  color: #666;
  font-size: 14px;
}
.area-list .aitem{
  padding: 4px 12px;
  border: 1px solid #262e34;
  color: #262e34;
  border-radius: 3px;
  display: inline-block;
  margin: 0 6px;
  font-size: 14px;
  cursor: pointer;
}
.area-list li.active{
  background-color: #262e34;
  color: #fff;
}
.area-list .meta{
  color: #666;
  padding: 2px 8px;
  display: inline-block;
}
.area-list{
  margin: 12px 0;
  font-size: 14px;
  float: left;
}
.search-div{
  margin: 8px 0;
}
.search-div .search-ipt{
  height: 35px;
  line-height: 35px;
  width: 220px;
  border-radius: 5px;
  border: 1px solid #808191;
  padding: 0 12px;
}
.search-div .search-btn, .edit-btn{
  display: inline-block;
  background-color: #5d7db3;
  color: #fff;
  height: 35px;
  line-height: 35px;
  cursor: pointer;
  border-radius: 3px;
  font-size: 14px;
  padding: 0 20px 0 15px;
  text-align: center;
}
.search-div .edit-btn{
  background-color: #d77f57;
}
.search-div .edit-btn:HOVER, .search-div .search-btn:hover{
  background-color: #84709b;
}
.search-div .search-button, .edit-btn i{
  padding-right: 8px;
  font-size: 18px;
}
.info-list .pcontent{
  overflow: hidden; /* 隐藏超出容器范围的内容 */
  text-overflow: ellipsis; /* 当文字溢出时显示省略号 */
  display: -webkit-box; /* 必须指定display为-webkit-box才能生效 */
  -webkit-line-clamp: 2; /* 限制最大显示行数为3行 */
  -webkit-box-orient: vertical; /* 垂直布局 */
  width: 630px;
  font-size: 14px;
  height: 42px;
  line-height: 22px;
  padding-top: 8px;
  color: #666;
}
.editor{
  height: 350px;
}
/deep/ .el-dialog__body{
  padding-top: 5px;
}
.default-img{
  background-image: url('../../assets/img/default.png');
  background-size: cover;
}
.mtags{
  margin-bottom: 8px;
}
.item2 .student{
  font-size: 14px;
  color: #5d7db3;
}
.mtags .view{
  color: #5d7db3;
  font-size: 14px;
}
.mtags .view i{
  font-size: 16px;
}
.myquestion{
  float: right;
  padding-top: 8px;
  color: #d77f57;
}
.myquestion a{
  color: #d77f57;
  text-decoration: none;
}
</style>