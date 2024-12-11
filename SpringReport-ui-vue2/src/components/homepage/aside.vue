<!--
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-02-09 07:58:05
 * @LastEditors: caiyang
 * @LastEditTime: 2021-06-18 15:20:45
-->
<template>
  <!-- 没用el-aside因为自带300宽度 -->
  <div class="_aside">
    <el-menu
      :default-active="onRoutes"
      :collapse="navShow"
      class="el-menu-vertical-demo menu"
      unique-opened
      text-color="rgba(0, 0, 0, 0.9)"
      router
    >
      <template v-for="(menu_one, i) in menuData">
        <el-submenu :key="i" :index="menu_one.path">
          <template slot="title">
            <i :class="menu_one.icon" />
            <span>{{ menu_one.title }}</span>
          </template>

          <el-menu-item
            v-for="(menu_two, i) in menu_one.subs"
            :key="i"
            :index="menu_two.path"
          >
            <!-- <i :class="menu_two.icon" /> -->
            <span>{{ menu_two.title }}</span>
          </el-menu-item>
        </el-submenu>
      </template>
    </el-menu>
    <div class="collapse-btn df-c" @click="navChangeShow()">
      <img v-show="navShow" src="@/static/img/menu-fold.png" height="16">
      <img v-show="!navShow" src="@/static/img/menu-unfold.png" height="16">
    </div>
  </div>
</template>
<style lang="scss" scoped>
._aside {
  position: relative;
  .menu {
    height: 100%;
  }
  .menu:not(.el-menu--collapse) {
    //设置才可以有折叠特效
    width: 232px;
    padding: 0 8px;
    box-sizing: border-box;
  }
  .collapse-btn {
    position: absolute;
    bottom: 0;
    left: 0;
    background: #ffffff;
    box-shadow: inset 0px 1px 0px #f0f0f0;
    width: 100%;
    height: 56px;
    z-index: 100;
    padding-left: 24px;
    box-sizing: border-box;
    img {
      cursor: pointer;
      transition: all 0.3s;
      &:hover {
        background-color: rgba(0, 0, 0, 0.1);
      }
    }
  }
}
</style>

<script>
import bus from '../common/bus'
// import commonUtil from '../common/common'
export default {
  data() {
    return {
      navShow: false, // 导航是否折叠
      menuData: [
        //  {path: "report", icon:"el-icon-pie-chart",title:"报表",subs:[
        //    {path: "reportType", icon:"el-icon-s-grid",title:"报表类型管理"},
        //    {path: "reportDatasource", icon:"el-icon-s-data",title:"报表数据库管理"},
        //    {path: "reportTpl", icon:"el-icon-pie-chart",title:"报表管理"},
        //  ]},
        //  {path: "system", icon:"el-icon-s-tools",title:"系统管理",subs:[
        //    {path: "sysUser", icon:"el-icon-user-solid",title:"用户管理"},
        //    {path: "sysRole", icon:"el-icon-s-custom",title:"角色管理"},
        //    {path: "sysMenu", icon:"el-icon-menu",title:"菜单管理"},
        //  ]},
      ]
    }
  },
  computed: {
    onRoutes() {
      // 监听路由,设置默认激活项目
      return this.$route.path.replace('/', '')
    }
  },

  mounted() {
    this.getMenus()
    this.checkScreenWidth()
    const that = this
    window.addEventListener('resize', this.debounce(function() {
      that.checkScreenWidth()
    }, 300))
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkScreenWidth)
  },
  methods: {
    debounce(fn, wait) {
      let timer
      return function() {
        const _this = this
        const args = arguments
        if (timer) {
          clearTimeout(timer)
        }
        timer = setTimeout(function() {
          fn.apply(_this, args)
        }, wait)
      }
    },
    navChangeShow() {
      // 切换左侧导航展示/折叠
      this.navShow = !this.navShow
      bus.$emit('navShowChange', this.navShow)
    },
    checkScreenWidth() {
      this.navShow = window.innerWidth < 1300
      bus.$emit('navShowChange', this.navShow)
    },
    getMenus() {
      var obj = {
        url: this.apis.index.getIndexMenuApi,
        params: {}
      }
      this.commonUtil.doPost(obj).then((response) => {
        this.menuData = response.responseData
      })
    }
  }
}
</script>
