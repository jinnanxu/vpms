/**
* 头部菜单
*/ 
<template>
  <el-menu class="el-menu-demo" mode="horizontal" background-color="#334157" text-color="#fff" active-text-color="#fff">
    <!-- <el-button class="buttonimg">
      <img class="showimg" :src="collapsed?imgsq:imgshow" @click="toggle(collapsed)">
    </el-button> -->
    <el-submenu index="2" class="submenu">
      <template slot="title">{{userRoleStr}} {{userData.realName}}</template>
      <el-menu-item @click="profile()" index="2-2">个人中心</el-menu-item>
      <el-menu-item @click="exit()" index="2-3">退出</el-menu-item>
    </el-submenu>
  </el-menu>
</template>
<script>
export default {
  name: 'navcon',
  data() {
    return {
      collapsed: true,
      imgshow: require('@/assets/img/show.png'),
      imgsq: require('@/assets/img/sq.png'),
      userData: {},
      userRoleStr: '',
      userRole: 1
    }
  },
  // 创建完毕状态(里面是操作)
  created() {
    let userData = JSON.parse(localStorage.getItem('AdminInfo'))
    this.userRole = localStorage.getItem('AdminRole')
    if(this.userRole==1){//管理员
      this.userRoleStr = '系统管理员'
    }else if(this.userRole==2){//农业专家
      this.userRoleStr = '农业专家'
    }
    this.userData = userData
    console.log(this.userRoleStr)
    this.user=userData
  },
  methods: {
    // 退出登录
    exit() {
      this.$confirm('退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          localStorage.removeItem('AdminInfo')
          localStorage.removeItem('TeacherInfo')
          setTimeout(() => {
            this.$store.commit('logout', 'false')
            this.$router.push({ path: '/admin/login' })
            this.$message({
              type: 'success',
              message: '已退出登录!'
            })
          }, 300)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })
    },
    // 切换显示
    toggle(showtype) {
      this.collapsed = !showtype
      // this.$root.Bus.$emit('toggle', this.collapsed)
      if (this.collapsed) {
        this.showclass = 'asideshow'
      } else {
        setTimeout(() => {
          this.showclass = 'aside'
        }, 300)
      }
    },
    profile(){
      if(this.userRole==1){
        //管理员
        this.$router.push({ path: '/admin/profile' })
      }else{
        this.$router.push({ path: '/expert/profile' })
      }
    },
  }
}
</script>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
}
.submenu {
  float: right;
}
.buttonimg {
  height: 60px;
  background-color: transparent;
  border: none;
}
.showimg {
  width: 26px;
  height: 26px;
  position: absolute;
  top: 17px;
  left: 17px;
}
.showimg:active {
  border: none;
}
</style>