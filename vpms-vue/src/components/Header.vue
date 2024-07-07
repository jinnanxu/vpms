<template>
  <header class="zui-header">
    <!--顶部LOGO与搜索栏-->
    <div class="top-bar">
      <div class="logo">
      </div>
      <!--导航菜单-->
      <ul class="menu-list">
        <li class="menu-li" v-for="(menu, id) in menuList" :key="id" @click="menu.action">
          <span>{{menu.text}}</span>
          <ul v-if="menu.hasChild" class="sub-menu">
            <li class="sub-item" v-for="(subMenu, id) in menu.children" :key="id" @click="subMenu.action">{{subMenu.text}}</li>
          </ul>
        </li>
      </ul>
      <ul class="menu-list" v-show="loginUser">
        <li class="menu-li" v-for="(menu, id) in userMenu" :key="id" @click="menu.action">
          <span>{{menu.text}}</span>
          <ul v-if="menu.hasChild" class="sub-menu">
            <li class="sub-item" v-for="(subMenu, id) in menu.children" :key="id" @click="subMenu.action">{{subMenu.text}}</li>
          </ul>
        </li>
      </ul>
      <!--右侧用户-->
      <div class="user-login">
        <ul v-if="!loginUser">
          <li @click="login()">登录</li>
          <li @click="reg()">注册</li>
        </ul>
        <!--登录后-->
        <ul v-else>
          <li @click="profile()">{{ loginUser.nickName }}</li>
          <li @click="logout()">退出</li>
        </ul>
      </div>
      <!-- <div class="search-bar">
        <input type="text" class="search-ipt" placeholder="搜索">
        <i class="el-icon-search search-button" @click="onSearch()"></i>
      </div> -->
    </div>
  </header>
</template>
<script>
  import './less/header.less'
  import Logo from './Logo'
  export default {
    props: {
      logoType: {
        default: 'zhichou'
      }
    },
    components: {Logo},
    data () {
      return {
        menuList: [ // 设置下拉列表数据和对应的点击事件
          // {id: 1, text: '首 页', hasChild: false, action: this.toQuestion},
          {id: 2, text: '田间作业', hasChild: false, action: this.index},
          {id: 3, text: '知识库', hasChild: false, action: this.knowledeg},
          {id: 4, text: '技术问答', hasChild: false, action: this.toQuestion},
        ],
        //登录后出现的菜单
        userMenu: [
          {id: 6, text: '种植管理', hasChild: false, action: this.myPlant},
          {id: 7, text: '作物管理', hasChild: false, action: this.myCrops},
          // {id: 9, text: '个人信息', hasChild: true, action: this.profile, children:[]},
        ],
        currUser: {}
      }
    },
    created() {
      this.loadLoginUser()
    },
    methods: {
      onButtonClick: function (e) {},
      void: function () {
        return;
      },
      openMenu: function () {
       this.$refs.droplist.show()
      },
      index: function () {
       this.$router.push({path:'/index'})
      },
      login: function () {
       this.$router.push({path:'/login'})
      },
      reg: function () {
       this.$router.push({path:'/reg'})
      },
      profile: function () {
       this.$router.push({path:'/profile'})
      },
      toQuestion: function () {
       this.$router.push({path:'/question/list'})
      },
      knowledeg: function () {
       this.$router.push({path:'/knowledge/list'})
      },
      myQuestions: function () {
       this.$router.push({path:'/my/questions'})
      },
      myCrops: function () {
       this.$router.push({path:'/crops/list'})
      },
      myPlant: function () {
        this.$router.push({path:'/crops/item'})
      },
      logout: function(){
        this.$confirm('退出登录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.commit('logout', {userId: null})
          console.log('登出后：',this.$store.state.loginUser)
          localStorage.removeItem('LoginUser')
          localStorage.removeItem('LoginUserRole')
          localStorage.removeItem('AuthToken')
          this.$router.push({path:'/index'})
          window.location.reload()
        })
      },
      //搜索
      onSearch: function(){
        this.$router.push({path:'/scenic', query: {kw: '关键字'}})
      },
      loadLoginUser: function(){
        this.currUser = localStorage.getItem('LoginUser')
      }
    },
    computed:{
      loginUser(){
        let lastLogin = parseFloat(localStorage.getItem('loginTime'))
        let now = (new Date()).getTime()
        console.log('当前登录，判断前：',this.$store.state.currUser)
        if(localStorage.getItem('loginTime')!='' && (now - lastLogin > 86400000)){
          localStorage.removeItem('LoginUser')
          return null
        }else if(localStorage.getItem('LoginUser')!=null){
          let currUser = JSON.parse(localStorage.getItem('LoginUser'))
          return currUser
        }else if(!this.$store.state.currUser.nickName){
          // if(localStorage.getItem('LoginUser')=='undefined'){return null}
          // let currUser = JSON.parse(localStorage.getItem('LoginUser'))
          // this.$store.commit('loginUser', currUser)
          // return currUser
          return null
        }else{
          console.log('当前登录???：',this.$store.state.loginUser)
          return this.$store.state.currUser
        }
      }
    }
  }
</script>
<style scoped>
body, html{
  padding: 0;
  margin: 0;
}
.h-content{
  width: 100%;
}
.h-content{
  max-width: 820px;
  margin: 0 auto;
}
.menu-list{
  list-style: none;
  padding: 0;
  margin: 0;
  display: inline-block;
}
.menu-list .menu-li{
  display: inline-block;
  padding: 5px 20px;
  line-height: 45px;
  font-size: 16px;
  position: relative;
  color: #fff;
}
.menu-list .menu-li:hover{
  background-color: #fff;
  color: #333;
  cursor: pointer;
}
.menu-list .menu-logo{
  width: 160px;
  height: 45px;
}
.zui-header{
  background-color: #5d7db3;
  height: auto;
}
.zui-header .zui-logo{
  width: 280px;
  height: 55px;
  vertical-align: middle;
  float: left;
  margin: 4px 0 0;
}
.sub-menu{
  display: none;
  list-style: none;
  position: absolute;
  top: 50px;
  background-color: #fff;
  color: #006dd9;
  width: 122px;
  left: 0;
  padding: 0;
  z-index: 99999;
}
.sub-menu .sub-item:hover{
  background-color: #3f68b0;
  color: #fff;
}
.sub-menu .sub-item{
  padding: 0 20px;
  line-height: 50px;
  font-size: 16px;
  border-top: none;
}
.menu-list .menu-li:hover .sub-menu{
  display: block;
}
.top-bar{
  max-width: 1024px;
  margin: 0 auto;
}
.top-bar .logo{
  background-image: url('../assets/img/head-logo.png');
  background-size: contain;
  background-position: center;
  height: 55px;
  width: 185px;
  background-repeat: no-repeat;
  float: left;
}
.top-bar .search-bar{
  width: 350px;
  float: right;
  position: relative;
}
.search-bar .search-ipt{
  height: 35px;
  width: 280px;
  border-radius: 25px;
  margin-top: 10px;
  border: 1px solid #3f68b0;
  padding: 0 12px;
}
.search-bar .search-button{
  position: absolute;
  right: 48px;
  top: 12px;
  background: #e2a388;
  color: #fff;
  font-weight: bold;
  width: 32px;
  height: 32px;
  border-radius: 16px;
  text-align: center;
  line-height: 32px;
  cursor: pointer;
}
.top-bar .user-login{
  float: right;
  /* width: 200px; */
  margin-top: 10px;
  font-size: 14px;
}
.user-login li{
  display: inline-block;
  min-width: 45px;
  text-align: center;
  cursor: pointer;
  line-height: 35px;
  height: 35px;
  margin: 0 5px;
  font-size: 14px;
  color: #fff;
}
.user-login li:hover{
  color: #e2a388;
}
</style>
