<template>
  <div class="main-contain" style="width: 1024px;">
    
  </div>
</template>

<script>
import { GetIndexData } from '@/api/api.js'
export default {
  data() {
    return {
      kw: '',
      swipeImages: [],
    }
  },
  // 注册组件
  components: {
    
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    this.getdata()
  },

  mounted() {
   
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取首页信息数据
    getdata(parameter) {
      console.log('开始获取首页资源列表！')
      this.loading = true
      GetIndexData({})
        .then(res => {
          if (res.success) {
            this.studyRoom = res.data.shop
            this.initMap()
          } else {}
        }).catch(err => {
          this.loading = false
          this.$message.error('系统繁忙，请稍后再试！')
      })
    },
    
    //跳到详情页面
    toDetailPage(sid) {
      this.$router.push({ path: '/product/detail?pid='+sid })
    },

  }
}
</script>

<style scoped>
#mapContainer{
  width: 1024px;
  height: 380px;
}

</style>