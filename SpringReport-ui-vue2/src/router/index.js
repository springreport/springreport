import Vue from "vue";
import Router from "vue-router";
import axios from "axios";
import commonUtil from "../components/common/common";
import commonConstants from "../components/common/constants";

// 主题样式: 深色
import { setDarkTheme } from "../components/common/configColor";

Vue.use(Router);

const router = new Router({
  base: commonUtil.baseUrl,
  routes: getRouter(),
  mode: "history",
});

function getRouter(){
  let routers = commonConstants.routers;
  // let routers = commonConstants.thirdPartyRouters
  return routers;
};
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
