/**
* 后台管理左边菜单
*/ 
<template>
  <div class="menulist">
    <el-menu :collapse="collapsed" collapse-transition router :default-active="$route.path"
      v-if="user.role==1"
     unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
      <div class="logobox">
        <div>蔬菜农场管理系统</div>
        <!-- <img class="logoimg" src="@/assets/img/logo.png" alt=""> -->
      </div>
      <el-submenu v-for="menu in adminMenu.adminMenus" :key="menu.menuid" :index="menu.menuname">
        <template slot="title">
          <font-awesome-icon :icon="['fas', menu.icon]"></font-awesome-icon>
          <span>{{menu.menuname}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="'/'+chmenu.url" :key="chmenu.menuid">
            <font-awesome-icon :icon="['fas', chmenu.icon]"></font-awesome-icon>
            <span>{{chmenu.menuname}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
    <el-menu v-if="user.role==2" :collapse="collapsed" collapse-transition router :default-active="$route.path" unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
      <div class="logobox">
        <div>蔬菜农场管理系统</div>
      </div>
      <!-- <el-menu-item :index="'/manager/index'">
        <font-awesome-icon :icon="['fas', 'tachometer-alt']"></font-awesome-icon>
        <span>控制台</span>
      </el-menu-item> -->
      <el-submenu v-for="menu in adminMenu.managerMenus" :key="menu.menuid" :index="menu.menuname">
        <template slot="title">
          <font-awesome-icon :icon="['fas', menu.icon]"></font-awesome-icon>
          <span>{{menu.menuname}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="'/'+chmenu.url" :key="chmenu.menuid">
            <font-awesome-icon :icon="['fas', chmenu.icon]"></font-awesome-icon>
            <span>{{chmenu.menuname}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>
<script>
export default {
  name: 'leftnav',
  data() {
    return {
      collapsed: false,
      adminMenu: [],
      user: {}
    }
  },
  // 创建完毕状态(里面是操作)
  created() {
    this.user = JSON.parse(localStorage.getItem('AdminInfo'))
    // if(this.user==null){
    //   this.user = {userRole:1, realName: '开发模式'}
    // }
    // if(this.user.role==2){
    //   this.userRole = '管理员'
    // }
    this.adminMenu = {
      success: true,
      adminMenus: [
        {
          menuid: 1,
          icon: 'users',
          menuname: '用户管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 11,
              icon: 'tree',
              menuname: '农户管理',
              hasThird: 'N',
              url: 'admin/usermgr',
              menus: null
            },
            {
              menuid: 12,
              icon: 'vcard',
              menuname: '农业专家管理',
              hasThird: 'N',
              url: 'admin/expert/mgr',
              menus: null
            }
          ]
        },
        {
          menuid: 2,
          icon: 'question',
          menuname: '内容管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 21,
              icon: 'list',
              menuname: '问题列表',
              hasThird: 'N',
              url: 'admin/question/list',
              menus: null
            },{
              menuid: 22,
              icon: 'database',
              menuname: '知识库',
              hasThird: 'N',
              url: 'admin/knowledge/list',
              menus: null
            }
          ]
        }, {
          menuid: 5,
          icon: 'cogs',
          menuname: '个人中心',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 51,
              icon: 'info-circle',
              menuname: '个人信息',
              hasThird: 'N',
              url: 'expert/profile',
              menus: null
            }
          ]
        }
      ],
      // 管理员菜单
      managerMenus: [
        // {
        //   menuid: 1,
        //   icon: 'home',
        //   menuname: '首页',
        //   hasThird: null,
        //   url: null,
        //   menus: [
        //     {
        //       menuid: 12,
        //       icon: 'tachometer-alt',
        //       menuname: '仪表盘',
        //       hasThird: 'N',
        //       url: 'manager/index',
        //       menus: null
        //     }
        //   ]
        // },
        {
          menuid: 3,
          icon: 'sliders-h',
          menuname: '农业技术内容',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 32,
              icon: 'question',
              menuname: '技术答疑',
              hasThird: 'N',
              url: 'expert/question/list',
              menus: null
            },{
              menuid: 33,
              icon: 'tools',
              menuname: '田间作业记录',
              hasThird: 'N',
              url: 'expert/task/list',
              menus: null
            }
          ]
        },{
          menuid: 4,
          icon: 'chart-line',
          menuname: '知识库管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 41,
              icon: 'list-alt',
              menuname: '知识库列表',
              hasThird: 'N',
              url: 'expert/knowlege/list',
              menus: null
            }
          ]
        }, {
          menuid: 71,
          icon: 'cogs',
          menuname: '个人中心',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 73,
              icon: 'info-circle',
              menuname: '个人信息',
              hasThird: 'N',
              url: 'expert/profile',
              menus: null
            }
          ]
        }
      ],
      msg: 'success'
    }
    
  }
}
</script>
<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 240px;
  min-height: 400px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
  text-align: left;
}
.el-menu-item-group__title {
  padding: 0px;
}
.el-menu-bg {
  background-color: #1f2d3d !important;
}
.el-menu {
  border: none;
}
.logobox {
  height: 40px;
  line-height: 40px;
  color: #fff;
  font-size: 20px;
  text-align: center;
  padding: 10px 0px;
}
.logoimg {
  height: 40px;
}
</style>