//api.js
import service from './request'

//农业专家注册
export const userRegist = data => {
    console.log('触发封装的Axios方法！')
    return service({
        url: '/vpms-server/admin/user/expert/save',
        method: 'post',
        data
    })
};

//后台用户登录
export const AdminLogin = data => {
    return service({
        url: '/vpms-server/admin/login',
        method: 'post',
        data
    })
};

//更新个人信息
export const updateProfile = data => {
    return service({
        url: '/vpms-server/admin/updateProfile',
        method: 'post',
        data
    })
};

//管理员更新用户信息
export const AdminUpdateUser = data => {
    return service({
        url: '/vpms-server/admin/user/save',
        method: 'post',
        data
    })
};

export const AdminUpdateUser2 = data => {
    return service({
        url: '/vpms-server/admin/user/update/status',
        method: 'post',
        data
    })
};

//管理员获取前台用户列表
export const AdminUserList = data => {
    return service({
        url: '/vpms-server/admin/user/list',
        method: 'post',
        data
    })
};

//管理员获取农业专家用户列表
export const AdminManagerList = data => {
    return service({
        url: '/vpms-server/admin/user/expert/list',
        method: 'post',
        data
    })
};

//更新农业专家状态
export const UpdateApartmentMgrStatus = data => {
    return service({
        url: '/vpms-server/admin/user/expert/update',
        method: 'post',
        data
    })
};

/********管理员：基础数据管理****** */
export const AdminGetCollegeList = data => {
    return service({
        url: '/vpms-server/admin/data/college/list',
        method: 'post',
        data
    })
};
export const AdminUpdateCollege = data => {
    return service({
        url: '/vpms-server/admin/data/college/save',
        method: 'post',
        data
    })
};
export const AdminDeleteCollege = data => {
    return service({
        url: '/vpms-server/admin/data/college/delete',
        method: 'post',
        data
    })
};
export const AdminGetClassList = data => {
    return service({
        url: '/vpms-server/admin/data/major/list',
        method: 'post',
        data
    })
};
export const AdminUpdateClass = data => {
    return service({
        url: '/vpms-server/admin/data/major/save',
        method: 'post',
        data
    })
};
export const AdminDeleteClass = data => {
    return service({
        url: '/vpms-server/admin/data/major/delete',
        method: 'post',
        data
    })
};
export const AdminGetExpertList = data => {
    return service({
        url: '/vpms-server/admin/user/expert/list',
        method: 'post',
        data
    })
};
export const AdminUpdateExpert = data => {
    return service({
        url: '/vpms-server/admin/user/expert/save',
        method: 'post',
        data
    })
};
export const AdminDeleteExpert = data => {
    return service({
        url: '/vpms-server/admin/user/expert/delete',
        method: 'post',
        data
    })
};
export const AdminGetStudentList = data => {
    return service({
        url: '/vpms-server/admin/user/list',
        method: 'post',
        data
    })
};
export const AdminUpdateStudent = data => {
    return service({
        url: '/vpms-server/admin/user/save',
        method: 'post',
        data
    })
};
export const AdminDeleteStudent = data => {
    return service({
        url: '/vpms-server/admin/user/delete',
        method: 'post',
        data
    })
};
/********管理员：基础数据管理****** */

/**********农业专家：知识库管理********* */
export const AdminGetKnowledgeList = data => {
    return service({
        url: '/vpms-server/expert/knowledge/list',
        method: 'post',
        data
    })
};
export const AdminUpdateKnowledge = data => {
    return service({
        url: '/vpms-server/expert/knowledge/save',
        method: 'post',
        data
    })
};
export const AdminDeleteKnowledge = data => {
    return service({
        url: '/vpms-server/expert/knowledge/delete',
        method: 'post',
        data
    })
};
/**********农业专家：知识库管理END********* */

/**********公告管理********* */
export const AdminGetNoticeList = data => {
    return service({
        url: '/vpms-server/admin/notice/list',
        method: 'post',
        data
    })
};
export const AdminUpdateNotice = data => {
    return service({
        url: '/vpms-server/admin/notice/save',
        method: 'post',
        data
    })
};
export const AdminDeleteNotice = data => {
    return service({
        url: '/vpms-server/admin/notice/delete',
        method: 'post',
        data
    })
};
/**********公告管理END********* */

/**********农业专家：问题管理********* */
export const AdminGetQuestionList = data => {
    return service({
        url: '/vpms-server/expert/question/list',
        method: 'post',
        data
    })
};
export const AdminGetQuestionDetail = data => {
    return service({
        url: '/vpms-server/expert/question/detail',
        method: 'post',
        data
    })
};
export const AdminUpdateQuestion = data => {
    return service({
        url: '/vpms-server/expert/question/save',
        method: 'post',
        data
    })
};
export const AdminDeleteQuestion = data => {
    return service({
        url: '/vpms-server/expert/question/delete',
        method: 'post',
        data
    })
};
export const ExpertSaveAnswer = data => {
    return service({
        url: '/vpms-server/expert/question/answer',
        method: 'post',
        data
    })
};
/**********农业专家：问题管理END********* */

// 查询所有作物
export const LoadAllCrops = data => {
    return service({
        url: '/vpms-server/expert/crops/list/all',
        method: 'post',
        data
    })
};

export const FrontUserLogin = data => {
    return service({
        url: '/vpms-server/user/login',
        method: 'post',
        data
    })
};

export const FrontUserRegist = data => {
    return service({
        url: '/vpms-server/user/register',
        method: 'post',
        data
    })
};

export const FrontUpdateProfile = data => {
    return service({
        url: '/vpms-server/user/updateUser',
        method: 'post',
        data
    })
};

export const GetQuestionList = data => {
    return service({
        url: '/vpms-server/question/list',
        method: 'post',
        data
    })
};

export const GetQuestionDetail = data => {
    return service({
        url: '/vpms-server/question/detail',
        method: 'post',
        data
    })
};

export const SaveQuestion = data => {
    return service({
        url: '/vpms-server/question/save',
        method: 'post',
        data
    })
};

export const StudentSaveAnswer = data => {
    return service({
        url: '/vpms-server/question/answer/save',
        method: 'post',
        data
    })
};

export const FinishQuestion = data => {
    return service({
        url: '/vpms-server/question/finish',
        method: 'post',
        data
    })
};

export const GetKnowledgeList = data => {
    return service({
        url: '/vpms-server/knowledge/list',
        method: 'post',
        data
    })
};

export const GetKnowledgeDetail = data => {
    return service({
        url: '/vpms-server/knowledge/detail',
        method: 'post',
        data
    })
};

export const GetNoticeList = data => {
    return service({
        url: '/vpms-server/notice/list',
        method: 'post',
        data
    })
};

export const GetNoticeDetail = data => {
    return service({
        url: '/vpms-server/notice/detail',
        method: 'post',
        data
    })
};

export const GetMyQuestions = data => {
    return service({
        url: '/vpms-server/question/my/list',
        method: 'post',
        data
    })
};

//新增或修改农事作业
export const SaveTask = data => {
    return service({
        url: '/vpms-server/farmer/task/save',
        method: 'post',
        data
    })
};


//农事作业列表
export const TaskList = data => {
    return service({
        url: '/vpms-server/farmer/task/list',
        method: 'post',
        data
    })
};

//删除农事作业
export const DeleteTask = data => {
    return service({
        url: '/vpms-server/farmer/task/delete',
        method: 'post',
        data
    })
};

// 查询作物item
export const LoadTreeItems = data => {
    return service({
        url: '/vpms-server/farmer/task/load/items',
        method: 'post',
        data
    })
};

// 查询我的所有作物
export const LoadAllMyCrops = data => {
    return service({
        url: '/vpms-server/crops/list/all',
        method: 'post',
        data
    })
};

// 种植新作物
export const PlantTree = data => {
    return service({
        url: '/vpms-server/crops/plant',
        method: 'post',
        data
    })
};

export const PlantCropsList = data => {
    return service({
        url: '/vpms-server/crops/plant/list',
        method: 'post',
        data
    })
};

export const RemovePlant = data => {
    return service({
        url: '/vpms-server/crops/plant/remove',
        method: 'post',
        data
    })
};

//蔬菜列表
export const CropsList = data => {
    return service({
        url: '/vpms-server/crops/list',
        method: 'post',
        data
    })
};

//新增或修改蔬菜信息
export const UpdateCrops = data => {
    return service({
        url: '/vpms-server/crops/save',
        method: 'post',
        data
    })
};

//农事作业列表
export const ExpertTaskList = data => {
    return service({
        url: '/vpms-server/expert/task/list',
        method: 'post',
        data
    })
};

export const SaveTaskAdvice = data => {
    return service({
        url: '/vpms-server/expert/task/save',
        method: 'post',
        data
    })
};