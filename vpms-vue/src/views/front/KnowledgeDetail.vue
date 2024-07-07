<template>
  <div class="main-contain" v-if="currDetailData">
    <!-- <p class="page-title">{{ currDetailData.title }}</p> -->
    <div class="show-title">{{currDetailData.title}}</div>
    <div class="auth">
      <p class="student">
        {{ currDetailData.expert.workDept }}
        <span>{{ currDetailData.expert.realName}}</span>
      </p>
      <p class="ptime">
        {{currDetailData.publishDate|timestampToDayHM}}
      </p>
    </div>
    <div v-html="currDetailData.detail" class="introduction" v-if="currDetailData!=null"></div>
    
    <div class="show-btn" style="margin-top: 35px;">
      <el-button size="small" type="info" icon="el-icon-refresh-left" @click="goBack()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { GetKnowledgeDetail } from '@/api/api.js'

export default {
  data() {
    return {
      loading: false,
      question: {},
      editFormVisible: false,
      editFormVisible2: false,
      currDetailData: null,
      currUser: {},
    }
  },
 
  created() {
    this.getdata()
  },

  computed:{
    loginUser(){
      console.log('当前登录???：',this.$store.state.loginUser)
      return this.$store.state.currUser
    }
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata() {
      console.log('拉取数据！', this.$route.query)
      const kid = this.$route.query.kid
      let parameter = {kid: kid}
      GetKnowledgeDetail(parameter)
        .then(res => {
          this.currDetailData = res.data
        })
    },
    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },
    
  }
}
</script>

<style scoped >
.show-title{
  margin: 15px 0;
  font-size: 22px;
  color: #666;
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
.student{
  font-size: 14px;
  margin: 0;
}
.student span{
  color: #d77f57;
}
</style>