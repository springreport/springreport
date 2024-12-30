import Vue from "vue";
import Router from "vue-router";
import axios from "axios";
import commonUtil from "../components/common/common";

// 主题样式: 深色
import { setDarkTheme } from "../components/common/configColor";

Vue.use(Router);

const router = new Router({
  base: commonUtil.baseUrl,
  routes: [
    {
      path: "/",
      redirect: "/login",
    },
    {
      name: "login",
      path: "/login",
      component: () => import("@/components/login.vue"),
      meta: { title: "登录页" },
    },
    {
      name: "h5ReportPreview",
      path: "/h5ReportPreview",
      component: () => import("@/components/pages/h5Report"),
      meta: { title: "报表查看" },
    },
    {
      path: "/home",
      component: () => import("@/components/homepage/homepage.vue"),
      meta: {
        title: "母版页",
      },
      children: [
        {
          path: "/index",
          name: "系统首页",
          component: () => import("@/components/homepage/shouye"),
          meta: {
            title: "系统首页",
            hideclose: true,
          },
        },
        {
          path: "/reportType",
          name: "reportType",
          component: () =>
            import("@/components/pages/reporttype/ReportType.vue"),
          meta: {
            title: "报表类型管理",
          },
        },
        {
          path: "/reportDatasource",
          name: "reportDatasource",
          component: () =>
            import("@/components/pages/reportdatasource/ReportDatasource.vue"),
          meta: {
            title: "数据源管理",
          },
        },
        {
          path: "/reportTpl",
          name: "reportTpl",
          component: () => import("@/components/pages/reporttpl/ReportTpl.vue"),
          meta: {
            title: "Excel报表",
          },
        },
        {
          path: "/sysUser",
          name: "sysUser",
          component: () => import("@/components/pages/sysuser/SysUser.vue"),
          meta: {
            title: "用户管理",
          },
        },
        {
          path: "/sysRole",
          name: "sysRole",
          component: () => import("@/components/pages/sysrole/SysRole.vue"),
          meta: {
            title: "角色管理",
          },
        },
        {
          path: "/sysDept",
          name: "sysDept",
          component: () => import("@/components/pages/sysdept/SysDept.vue"),
          meta: {
            title: "部门管理",
          },
        },
        {
          path: "/sysPost",
          name: "sysPost",
          component: () => import("@/components/pages/syspost/SysPost.vue"),
          meta: {
            title: "岗位管理",
          },
        },
        {
          path: "/sysMerchantAuthTemplate",
          name: "sysMerchantAuthTemplate",
          component: () => import("@/components/pages/sysmerchantauthtemplate/SysMerchantAuthTemplate.vue"),
          meta: {
            title: "权限模板",
          },
        },
        {
          path: "/sysMerchant",
          name: "sysMerchant",
          component: () => import("@/components/pages/sysmerchant/SysMerchant.vue"),
          meta: {
            title: "租户管理",
          },
        },
        {
          path: "/sysMenu",
          name: "sysMenu",
          component: () => import("@/components/pages/sysmenu/SysMenu.vue"),
          meta: {
            title: "菜单管理",
          },
        },
        {
          path: "/sysApi",
          name: "sysApi",
          component: () => import("@/components/pages/sysapi/SysApi.vue"),
          meta: {
            title: "菜单功能",
          },
        },
        {
          path: "/viewReport",
          name: "报表查看",
          component: () =>
            import("@/components/pages/viewReport/viewReport.vue"),
          meta: {
            title: "Excel报表查看",
          },
        },
        {
          path: "/reportDatasourceDictType",
          name: "reportDatasourceDictType",
          component: () =>
            import(
              "@/components/pages/reportdatasourcedicttype/ReportDatasourceDictType.vue"
            ),
          meta: {
            title: "数据字典类型",
          },
        },
        {
          path: "/reportDatasourceDictData",
          name: "reportDatasourceDictData",
          component: () =>
            import(
              "@/components/pages/reportdatasourcedictdata/ReportDatasourceDictData.vue"
            ),
          meta: {
            title: "数据字典值",
          },
        },
        {
          path: "/onlineTpl",
          name: "onlineTpl",
          component: () => import("@/components/pages/onlinetpl/OnlineTpl.vue"),
          meta: {
            title: "Excel协同文档",
          },
        },
        {
          path: "/reportTask",
          name: "reportTask",
          component: () => import("@/components/pages/qrtzreportdetail/QrtzReportDetail.vue"),
          meta: {
            title: "报表定时任务",
          },
        },
        {
          path: "/tempRefresh",
          name: "tempRefresh",
          component: () => import("@/components/pages/refresh/tempRefresh.vue"),
          meta: {
            title: "刷新页面",
          },
        },
        {
          path: "/docTpl",
          name: "docTpl",
          component: () => import("@/components/pages/doctpl/DocTpl.vue"),
          meta: {
            title: "word报表",
          },
        },
        {
          path: "/screenTpl",
          name: "screenTpl",
          component: () => import("@/components/pages/screen/screentpl/ScreenTpl.vue"),
          meta: {
            title: "大屏模板管理",
          },
        },
        {
          path: "/slideTpl",
          name: "slideTpl",
          component: () => import("@/components/pages/slidetpl/SlideTpl.vue"),
          meta: {
            title: "PPT模板管理",
          },
        },
      ],
    },
    {
      name: "404",
      path: "/404",
      component: () => import("@/components/homepage/404.vue"),
      meta: { title: "路由不存在" },
    },
    {
      name: "403",
      path: "/403",
      component: () => import("@/components/homepage/403.vue"),
      meta: { title: "资源不可访问" },
    },
    {
      path: "/luckyReportDesign",
      name: "luckyReportDesign",
      component: () =>
        import("@/components/pages/luckyreport/luckyReportDesign.vue"),
      meta: {
        title: "报表设计",
      },
    },
    {
      path: "/luckyReportFroms",
      name: "luckyReportFroms",
      component: () =>
        import("@/components/pages/luckyreport/luckyReportFroms.vue"),
      meta: {
        title: "填报报表设计",
      },
    },
    {
      path: "/luckyReportPreview",
      name: "luckyReportPreview",
      component: () =>
        import("@/components/pages/luckyreport/luckyReportPreview.vue"),
      meta: {
        title: "填报报表预览",
      },
    },
    {
      path: "/luckyReportFromsPreview",
      name: "luckyReportFromsPreview",
      component: () =>
        import("@/components/pages/luckyreport/luckyReportFromsPreview.vue"),
      meta: {
        title: "填报报表预览",
      },
    },
    {
      path: "/onlineReport",
      name: "onlineReport",
      component: () =>
        import("@/components/pages/luckyreport/onlineReport.vue"),
      meta: {
        title: "协同编辑",
      },
    },
    {
      path: "/coedit",
      name: "coedit",
      component: () =>
        import("@/components/pages/coedit/coedit.vue"),
      meta: {
        title: "协同编辑",
      },
    },
    {
      path: "/docDesign",
      name: "docDesign",
      component: () => import("@/components/pages/editor/docDesign.vue"),
      meta: {
        title: "doc设计",
      },
    },
    {
      path: "/docPreview",
      name: "docPreview",
      component: () => import("@/components/pages/editor/docPreview.vue"),
      meta: {
        title: "doc预览",
      },
    },
    {
      path: "/screenDesign",
      name: "screenDesign",
      component: () => import("@/components/pages/screen/screendesign/screenDesign.vue"),
      meta: {
        title: "大屏设计",
      },
    },
    {
      path: "/screenView",
      name: "screenView",
      component: () => import("@/components/pages/screen/screendesign/screenView.vue"),
      meta: {
        title: "大屏查看",
      },
    },
    {
      path: "/attachment",
      name: "attachment",
      component: () => import("@/components/pages/attachment/attachment.vue"),
      meta: {
        title: "附件查看",
      },
    },
    {
      path: "/slideDesign",
      name: "slideDesign",
      component: () => import("@/components/pages/slide/slideDesign.vue"),
      meta: {
        title: "PPT设计",
      },
    },
    {
      path: "/slidePreview",
      name: "slidePreview",
      component: () => import("@/components/pages/slide/slidePreview.vue"),
      meta: {
        title: "PPT预览",
      },
    },
    {
      path: "*",
      redirect: "/404",
    },
  ],
  mode: "history",
});
/**
 * 全局路由守卫
 */

const rightPathList = ["/login", "/404", "/403"]; // 直接可以进入的页面

router.beforeEach((to, from, next) => {
  // debugger
  console.log("跳转到:", to.fullPath);

  // liren 大屏设计页面 黑色主题
  if (to.path === "/screenDesign" || to.path === "/screenView") {
    setDarkTheme();
  }

  var token = localStorage.getItem("token");
  // if (!token && to.path != '/login') {//登陆认证校验,没登录则跳转/login
  //   next({ path: '/login' })
  // }
  // else {//权限认证
  if (rightPathList.includes(to.path)) {
    next();
  } else {
    next();
  }
  // else {
  //   next('403');
  // }

  // }
});
/**
 * 请求拦截器,添加请求头token
 */
axios.interceptors.request.use(
  (config) => {
    console.log(">>>请求url:", config.url);
    var headers = config.headers;
    if (localStorage.getItem("token")) {
      headers.token = localStorage.getItem("token");
    }
    return config;
  },
  (error) => {
    console.log(">>>请求异常:", error.message);
    return Promise.reject(error);
  }
);
// 接口请求超时设置
axios.defaults.timeout = 300000; // 毫秒

axios.defaults.baseURL = commonUtil.baseUrl;
/**
 * 应答拦截器,添加请求头token
 */
axios.interceptors.response.use(
  function (response) {
    // Do something with response data
    console.log("<<<请求成功");
    return response;
  },
  (error) => {
    // Do something with response error
    console.log("<<<异常信息:", error.message);
    return Promise.reject(error);
  }
);

// 获取当前路由是否有权限访问
function hasPermit(to) {
  var hasPermit = false;
  if (to.meta && to.meta.role) {
    var ruleList = getRuleList();
    hasPermit = ruleList.some((rule) => {
      return to.meta.role.includes(rule);
    });
  }
  return hasPermit;
}
// 获取登陆的角色集合
function getRuleList() {
  var ruleList = []; // 角色数组
  var strRule = localStorage.getItem("position");
  if (strRule.indexOf("|") != -1) {
    ruleList = strRule.split("|");
  } else {
    ruleList.push(strRule);
  }
  return ruleList;
}

export default router;
