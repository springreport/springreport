import { createApp } from 'vue';
// permission 权限文件
import './config/permission';

// element
import 'element-plus/theme-chalk/display.css';
import '@/assets/index.css'
import App from './App.vue';
const app = createApp(App);

// layout components
import layoutComp from './layouts/components/export';
layoutComp(app);

// router
import router from './router/index';
app.use(router);

// vuex
import store from '@/store';
app.use(store);

//报表大屏用组件start
import cusTable from './components/table/custable';
app.use(cusTable);
import searchForm from './components/searchform/searchForm';
app.use(searchForm);
import reportForm from './components/reportForm/reportForm';
app.use(reportForm);
import taskReportForm from './components/taskReportForm/taskReportForm'
app.use(taskReportForm)
import modal from './components/modal/modal';
app.use(modal);
import upload from './components/upload/upload'
app.use(upload)
import fileUpload from './components/upload/fileUpload'
app.use(fileUpload)
import validate from './components/validate/validate'
app.use(validate);
import commonUtil from './components/common/common'
app.config.globalProperties.commonUtil = commonUtil;
import selectUtil from './components/common/selectValue'
app.config.globalProperties.selectUtil = selectUtil;
import commonConstants from './components/common/constants'
app.config.globalProperties.commonConstants = commonConstants;
import apis from './components/common/api'
app.config.globalProperties.apis = apis;
import * as echarts from 'echarts';
app.config.globalProperties.$echarts = echarts
//大屏部分相关的引入 start
// import './icons'
import screenConstants from './components/common/screenConstants'
app.config.globalProperties.screenConstants = screenConstants;
import componentForm from './components/screen/componentForm/componentForm';
app.use(componentForm);
import Vue3DraggableResizable from 'vue3-draggable-resizable'
//default styles
import 'vue3-draggable-resizable/dist/Vue3DraggableResizable.css'
app.use(Vue3DraggableResizable)

import draggables from './components/screen/draggables'
app.use(draggables)

import '@imengyu/vue3-context-menu/lib/vue3-context-menu.css'
import ContextMenu from '@imengyu/vue3-context-menu'
app.use(ContextMenu);
import vue3SeamlessScroll from 'vue3-seamless-scroll';
app.use(vue3SeamlessScroll)

import DataVVue3 from '@kjgl77/datav-vue3'
app.use(DataVVue3)

//大屏部分相关的引入 end
// 引入自定义指令
import directive from './components/common/directive';
// 注册自定义指令
app.use(directive);
//报表大屏用组件end
// 按需注册方式
// import elementPlus from './plugin/el-comp';
// 注册 elementPlus组件/插件
// elementPlus(app);
// // 完整引入
// 注册字节跳动图标
import iconPark from './plugin/icon-park';

iconPark(app);
import vant from 'vant';
app.use(vant)
import 'vant/lib/index.css';
import { Tab, Tabs, NavBar, Pagination, Empty ,Image as VanImage ,Popup } from "vant";
app.use(Tab);
app.use(Tabs);
app.use(NavBar);
app.use(Pagination);
app.use(Empty);
app.use(VanImage);
app.use(Popup);
// var env = import.meta.env.VITE_BASE_NODE_ENV
// var url = "";
// if(env == "development"){
//   url = 'http://192.168.0.105:9696/web-apps/apps/api/documents/api.js';
// }else{
//   url = 'http://192.168.0.105:9696/web-apps/apps/api/documents/api.js';
// }
// var script = document.createElement('script');
// script.src = url;
// document.body.appendChild(script);
app.mount('#app');
//自定义标签指令-按钮是否有权限展示start
app.directive('has', {
  mounted: function (el, binding) {
      if (!permissionJudge(binding.value)) {
        el.parentNode.removeChild(el);
      }
  
      function permissionJudge(value) {
        if(value =="ignore")
        {
          return true;
        }
        // 此处store.getters.getMenuBtnList代表vuex中储存的按钮菜单数据
        let apis = localStorage.getItem("apiList");
        let list = apis.split(",")
        if(list != null && list.length > 0)
        {
          for (let item of list) {
            if (item === value) {
              return true;
            }
          }
        }
        
        return false;
      }
    }
  });
  //自定义标签指令-按钮是否有权限展示end