import { createApp } from 'vue';
// permission 权限文件
import './config/permission';

// element
import 'element-plus/theme-chalk/display.css';
import App from './App.vue';
const app = createApp(App);

import { VueClipboard } from '@soerenmartius/vue3-clipboard';
app.use(VueClipboard);

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
import componentForm from './components/componentForm/componentForm';
app.use(componentForm);
import Vue3DraggableResizable from 'vue3-draggable-resizable'
//default styles
import 'vue3-draggable-resizable/dist/Vue3DraggableResizable.css'
app.use(Vue3DraggableResizable)
import * as echarts from 'echarts';
import 'echarts-gl';
app.config.globalProperties.$echarts = echarts
import '@/assets/index.css'
//echarts主题
import  'echarts/theme/azul.js'
import  'echarts/theme/bee-inspired.js'
import  'echarts/theme/blue.js'
import  'echarts/theme/caravan.js'
import  'echarts/theme/carp.js'
import  'echarts/theme/cool.js'
import  'echarts/theme/dark-blue.js'
import  'echarts/theme/dark-bold.js'
import  'echarts/theme/dark-digerati.js'
import  'echarts/theme/dark-fresh-cut.js'
import  'echarts/theme/dark-mushroom.js'
import  'echarts/theme/eduardo.js'
import  'echarts/theme/forest.js'
import  'echarts/theme/fresh-cut.js'
import  'echarts/theme/fruit.js'
import  'echarts/theme/gray.js'
import  'echarts/theme/green.js'
import  'echarts/theme/helianthus.js'
import  'echarts/theme/infographic.js'
import  'echarts/theme/inspired.js'
import  'echarts/theme/jazz.js'
import  'echarts/theme/london.js'
import  'echarts/theme/macarons2.js'
import  'echarts/theme/mint.js'
import  'echarts/theme/red.js'
import  'echarts/theme/red-velvet.js'
import  'echarts/theme/roma.js'
import  'echarts/theme/royal.js'
import  'echarts/theme/sakura.js'
import  'echarts/theme/shine.js'
import  'echarts/theme/tech-blue.js'
import  'echarts/theme/vintage.js'

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