import { Statistic } from 'element-ui'
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)
//前台
const HomeFramePage = () => import('../views/HomeFramePage')
const LoginPage = () => import('../views/user/Login')
const RegistPage = () => import('../views/user/Regist')
// const IndexPage = () => import('../views/front/IndexPage')
const Profile = () => import('../views/user/Profile')
const QuestionList = () => import('../views/front/QuestionList')
const QuestionDetail = () => import('../views/front/QuestionDetail')
const KnowledgeList = () => import('../views/front/KnowledgeList')
const KnowledgeDetail = () => import('../views/front/KnowledgeDetail')
const MyQuestions = () => import('../views/front/MyQuestions')
const CropsItemManager = () => import('../views/front/CropsItemManager')
const CropsManager = () => import('@/views/front/CropsManager')
const FarmTaskManager = () => import('@/views/front/TaskManager')

const AdminPage = () => import('@/views/admin/AdminFramwork')
const AdminLoginPage = () => import('../views/admin/AdminLogin')
const UserManager = () => import('@/views/admin/UserManager')
const ExpertManager = () => import('@/views/admin/ExpertManager')
const AdminProfile = () => import('@/views/admin/AdminProfile')
const AdminQuestionsManager = () => import('@/views/admin/AdminQuestionsManager')
const AdminQuestionsDetail = () => import('@/views/admin/AdminQuestionsDetail')
const AdminKnowledgeList = () => import('@/views/admin/KnowledgeManager')

const Dashboard = () => import('@/views/expert/Dashboard')
const KnowlegeManager = () =>  import('@/views/expert/KnowledgeManager')
const QuestionsManager = () =>  import('@/views/expert/QuestionsManager')
const ExpertQuestionDetail = () =>  import('@/views/expert/ExpertQuestionDetail')
const ExpertTaskManager = () =>  import('@/views/expert/ExpertTaskManager')

const router = new VueRouter({
  mode: 'history',
  base: '/vpms/', //增加前缀，这样可以在云服务器的nginx配置多个项目，涉及到nginx的配置 如果不要前台，则填“__dirname”,
  routes: [
    {
      path: "/",
      name: "登录页面",
      component: LoginPage
    },{
      //前台页面路由
      path: '/',
      component: HomeFramePage,
      children: [
        {
          path: "login",
          name: "登录页面",
          component: LoginPage
        }, {
          path: "reg",
          name: "注册页面",
          component: RegistPage
        }, {
          path: "index",
          name: "网站首页",
          component: FarmTaskManager
        },{
          path: "profile",
          name: "个人信息",
          component: Profile
        },{
          path: "question/list",
          name: "问题列表页",
          component: QuestionList
        },{
          path: "question/detail",
          name: "问题详情页",
          component: QuestionDetail
        },{
          path: "knowledge/list",
          name: "知识库列表",
          component: KnowledgeList
        },{
          path: "knowledge/detail",
          name: "知识库详情",
          component: KnowledgeDetail
        },{
          path: "crops/list",
          name: "蔬菜作物类型管理",
          component: CropsManager
        },{
          path: "my/questions",
          name: "我的提问",
          component: MyQuestions
        },{
          path: "crops/item",
          name: "种植信息",
          component: CropsItemManager
        },
        
      ]
    }, {
      path: "/admin/login",
      name: "管理员登录",
      component: AdminLoginPage
    }, {
      path: '/admin',
      component: AdminPage,
      children: [
        {
          path: "usermgr",
          name: "用户管理",
          component: UserManager
        },{
          path: "profile",
          name: "个人信息",
          component: AdminProfile
        },{
          path: "expert/mgr",
          name: "专家管理",
          component: ExpertManager
        },{
          path: "question/list",
          name: "答疑管理",
          component: AdminQuestionsManager
        },{
          path: "question/detail",
          name: "问题详情",
          component: AdminQuestionsDetail
        },{
          path: "question/detail",
          name: "问题详情",
          component: ExpertQuestionDetail
        },{
          path: "knowledge/list",
          name: "知识库列表",
          component: AdminKnowledgeList
        },
        
      ]
    }, {
      path: '/expert', // 农业专家管理员 
      component: AdminPage,
      children: [
        {
          path: "index",
          name: "管理主页",
          component: Dashboard
        },{
          path: "profile",
          name: "个人信息",
          component: AdminProfile
        },{
          path: "knowlege/list",
          name: "知识库管理",
          component: KnowlegeManager
        },{
          path: "question/list",
          name: "答疑管理",
          component: QuestionsManager
        },{
          path: "question/detail",
          name: "问题详情",
          component: ExpertQuestionDetail
        },{
          path: "task/list",
          name: "田间作业",
          component: ExpertTaskManager
        },
      ]
    },
  ]
});


export default router;
