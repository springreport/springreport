/**
 * @author hujiangjun 1217437592@qq.com
 * @description 路由控制
 */
import router from '@/router';
import store from '@/store';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { getPageTitle } from '@/utils/index';
import { setting } from '@/config/setting';
import commonConstants from '../components/common/constants';
// 主题样式: 深色
import { setDarkTheme } from '../components/common/configColor'
const { authentication, loginInterception, progressBar, routesWhiteList, recordRoute } = setting;

NProgress.configure({
  easing: 'ease',
  speed: 500,
  trickleSpeed: 200,
  showSpinner: false,
});
router.beforeEach(async (to, from, next) => {
  if (progressBar) NProgress.start();
  var token = localStorage.getItem(commonConstants.sessionItem.authorization);
  if(!token){
    token = to.query.token
  }
  if(!loginInterception)
  {
    next();
  }else{
    if (to.path === '/screenDesign') {
      setDarkTheme()
    }
    if (to.path === '/login'){
      //登录页面，如果有token直接跳转到首页，没有登录跳转到登录页面
      if(token)
      {
        next({ path: '/' });
      }else{
        next();
      }
    }else if(routesWhiteList.indexOf(to.path) !== -1)
    {//白名单页面直接跳转
      next();
    }else{
      //登录直接跳转，未登录跳转到登录页面
      if(token)
      {
        next();
      }else{
        next({ path: '/login' });
      }
    }
  }
  
  document.title = getPageTitle(to.meta.title);
});
router.afterEach(() => {
  if (progressBar) NProgress.done();
});
