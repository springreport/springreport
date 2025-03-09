import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router';
import Layout from '@/layouts/index.vue';
import axios from 'axios';
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
    },
    hidden: true,
  },
  {
    path: '/401',
    name: '401',
    component: () => import('@/views/errorPage/401.vue'),
    hidden: true,
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/errorPage/404.vue'),
    hidden: true,
  },
  {
    path: '/luckyReportDesign',
    name: 'luckyReportDesign',
    component: () => import('@/views/luckyreport/luckyReportDesign.vue'),
    meta: {
      title: '报表设计',
    },
  },
  {
    path: '/luckyReportFroms',
    name: 'luckyReportFroms',
    component: () => import('@/views/luckyreport/luckyReportFroms.vue'),
    meta: {
      title: '填报报表设计',
    },
  },
  {
    path: '/luckyReportFromsPreview',
    name: 'luckyReportFromsPreview',
    component: () => import('@/views/luckyreport/luckyReportFromsPreview.vue'),
    meta: {
      title: '填报报表预览',
    },
  },
  {
    path: '/onlineReport',
    name: 'onlineReport',
    component: () => import('@/views/luckyreport/onlineReport.vue'),
    meta: {
      title: '协同编辑',
    },
  },
  {
    path: '/h5ReportPreview',
    name: 'h5ReportPreview',
    component: () => import('@/views/h5Report/index.vue'),
    meta: {
      title: '报表查看',
    },
  },
  {
    path: '/luckyReportPreview',
    name: 'luckyReportPreview',
    component: () => import('@/views/luckyreport/luckyReportPreview.vue'),
    meta: {
      title: 'Excel报表查看',
    },
  },
  {
    path: '/coedit',
    name: 'coedit',
    component: () => import('@/views/coedit/coedit.vue'),
    meta: {
      title: '协同编辑',
    }
  },
  {
    path: '/docDesign',
    name: 'docDesign',
    component: () => import('@/views/editor/docDesign.vue'),
    meta: {
      title: 'doc设计',
    },
  },
  {
    path: '/docPreview',
    name: 'docPreview',
    component: () => import('@/views/editor/docPreview.vue'),
    meta: {
      title: 'doc预览',
    },
  },
  {
    path: '/screenDesign',
    name: 'screenDesign',
    component: () => import('@/views/screen/screendesign/screenDesign.vue'),
    meta: {
      title: '大屏设计',
    },
  },
  {
    path: '/screenView',
    name: 'screenView',
    component: () => import('@/views/screen/screendesign/screenView.vue'),
    meta: {
      title: '大屏查看',
    },
  },
  {
    path: "/attachment",
    name: "attachment",
    component: () => import("@/views/attachment/attachment.vue"),
    meta: {
      title: "附件查看",
    },
  },
  // {
  //   path: '/slideDesign',
  //   name: 'slideDesign',
  //   component: () => import('@/views/slide/slideDesign.vue'),
  //   meta: {
  //     title: 'PPT设计',
  //   },
  // },
  // {
  //   path: '/slidePreview',
  //   name: 'slidePreview',
  //   component: () => import('@/views/slide/slidePreview.vue'),
  //   meta: {
  //     title: 'PPT预览',
  //   },
  // },
   {
    path: '/onlyOfficeDocDesign',
    name: 'onlyOfficeDocDesign',
    component: () => import('@/views/onlyoffice/docDesign.vue'),
    meta: {
      title: 'doc设计',
    },
  },
  {
    path: '/onlyOfficeDocPreview',
    name: 'onlyOfficeDocPreview',
    component: () => import('@/views/onlyoffice/docPreview.vue'),
    meta: {
      title: 'doc预览',
    },
  },
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    name: 'Root',
    children: [
      {
        path: '/index',
        name: 'Index',
        component: () => import('../views/index/index.vue'),
        meta: {
          title: '首页',
          icon: 'icon-home',
          affix: true,
          noKeepAlive: true,
        },
      },
      {
        path: '/tempRefresh',
        name: 'tempRefresh',
        component: () => import('@/views/refresh/tempRefresh.vue'),
        meta: {
          title: '刷新',
          icon: 'icon-like',
        },
      },
      {
        path: '/iconPark',
        name: 'IconPark',
        component: () => import('@/views/icon/index.vue'),
        meta: {
          title: '图标',
          icon: 'icon-like',
        },
      },
      {
        path: '/sysUser',
        name: 'sysUser',
        component: () => import('@/views/sysuser/SysUser.vue'),
        meta: {
          title: '用户管理',
          icon: 'icon-peoples',
        },
      },
      {
        path: '/sysRole',
        name: 'sysRole',
        component: () => import('@/views/sysrole/SysRole.vue'),
        meta: {
          title: '角色管理',
          icon: 'icon-user',
        },
      },
      {
        path: '/sysMenu',
        name: 'sysMenu',
        component: () => import('@/views/sysmenu/SysMenu.vue'),
        meta: {
          title: '菜单管理',
          icon: 'icon-music-list',
        },
      },
      {
        path: '/sysApi',
        name: 'sysApi',
        component: () => import('@/views/sysapi/SysApi.vue'),
        meta: {
          title: '菜单功能',
          icon: 'el-icon-menu',
        },
      },
      {
        path: '/sysDept',
        name: 'sysDept',
        component: () => import('@/views/sysdept/SysDept.vue'),
        meta: {
          title: '部门管理',
          icon: 'icon-network-tree',
        },
      },
      {
        path: '/sysPost',
        name: 'sysPost',
        component: () => import('@/views/syspost/SysPost.vue'),
        meta: {
          title: '岗位管理',
          icon: 'icon-user-positioning',
        },
      },
      {
        path: '/sysMerchantAuthTemplate',
        name: 'sysMerchantAuthTemplate',
        component: () => import('@/views/sysmerchantauthtemplate/SysMerchantAuthTemplate.vue'),
        meta: {
          title: '权限模板',
          icon: 'icon-permissions',
        },
      },
      {
        path: '/sysMerchant',
        name: 'sysMerchant',
        component: () => import('@/views/sysmerchant/SysMerchant.vue'),
        meta: {
          title: '租户管理',
          icon: 'icon-user-business',
        },
      },
      {
        path: '/reportType',
        name: 'reportType',
        component: () => import('@/views/reporttype/ReportType.vue'),
        meta: {
          title: '报表类型管理',
          icon: 'icon-table-report',
        },
      },
      {
        path: '/reportDatasource',
        name: 'reportDatasource',
        component: () => import('@/views/reportdatasource/ReportDatasource.vue'),
        meta: {
          title: '数据源管理',
          icon: 'icon-data',
        },
      },
      {
        path: '/reportDatasourceDictType',
        name: 'reportDatasourceDictType',
        component: () => import('@/views/reportdatasourcedicttype/ReportDatasourceDictType.vue'),
        meta: {
          title: '数据字典类型',
          icon: 'icon-data',
        },
      },
      {
        path: '/reportDatasourceDictData',
        name: 'reportDatasourceDictData',
        component: () => import('@/views/reportdatasourcedictdata/ReportDatasourceDictData.vue'),
        meta: {
          title: '数据字典值',
          icon: 'icon-data',
        },
      },
      {
        path: '/onlineTpl',
        name: 'onlineTpl',
        component: () => import('@/views/onlinetpl/OnlineTpl.vue'),
        meta: {
          title: 'Excel协同文档',
          icon: 'icon-excel',
        },
      },
      {
        path: '/reportTpl',
        name: 'reportTpl',
        component: () => import('@/views/reporttpl/ReportTpl.vue'),
        meta: {
          title: 'Excel报表',
          icon: 'icon-table',
        },
      },
      {
        path: '/viewReport',
        name: 'viewReport',
        component: () => import('@/views/viewReport/viewReport.vue'),
        meta: {
          title: 'Excel报表查看',
          icon: 'icon-eyes',
        },
      },
      {
        path: '/reportTask',
        name: 'reportTask',
        component: () => import('@/views/qrtzreportdetail/QrtzReportDetail.vue'),
        meta: {
          title: '报表定时任务',
          icon: 'icon-timer',
        },
      },
      {
        path: '/docTpl',
        name: 'docTpl',
        component: () => import('@/views/doctpl/DocTpl.vue'),
        meta: {
          title: 'Word报表',
          icon: 'icon-word',
        },
      },
      {
        path: '/screenTpl',
        name: 'screenTpl',
        component: () => import('@/views/screen/screentpl/ScreenTpl.vue'),
        meta: {
          title: '大屏模板管理',
          icon: 'icon-word',
        },
      },
      // {
      //   path: '/slideTpl',
      //   name: 'slideTpl',
      //   component: () => import('@/views/slidetpl/SlideTpl.vue'),
      //   meta: {
      //     title: 'PPT模板管理',
      //     icon: 'icon-word',
      //   },
      // },
      {
        path: "/doconlyoffice",
        name: "doconlyoffice",
        component: () => import("@/views/doconlyoffice/DocOnlyOffice.vue"),
        meta: {
          title: "Word报表(onlyoffice)",
        },
      },
    ],
  },
  {
    path: '/errorPage',
    name: 'ErrorPage',
    component: Layout,
    meta: {
      title: '错误页面',
      icon: 'icon-link-cloud-faild',
    },
    children: [
      {
        path: '/404Page',
        name: '404Page',
        component: () => import('@/views/errorPage/404.vue'),
        meta: {
          title: '404',
          icon: 'icon-link-cloud-faild',
        },
      },
      {
        path: '/401Page',
        name: '401Page',
        component: () => import('@/views/errorPage/401.vue'),
        meta: {
          title: '401',
          icon: 'icon-link-interrupt',
        },
      },
    ],
  },
];

export const constantThirdPartyRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
    },
    hidden: true,
  },
  {
    path: '/401',
    name: '401',
    component: () => import('@/views/errorPage/401.vue'),
    hidden: true,
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/errorPage/404.vue'),
    hidden: true,
  },
  {
    path: '/luckyReportDesign',
    name: 'luckyReportDesign',
    component: () => import('@/views/luckyreport/luckyReportDesign.vue'),
    meta: {
      title: '报表设计',
    },
  },
  {
    path: '/luckyReportFroms',
    name: 'luckyReportFroms',
    component: () => import('@/views/luckyreport/luckyReportFroms.vue'),
    meta: {
      title: '填报报表设计',
    },
  },
  {
    path: '/luckyReportFromsPreview',
    name: 'luckyReportFromsPreview',
    component: () => import('@/views/luckyreport/luckyReportFromsPreview.vue'),
    meta: {
      title: '填报报表预览',
    },
  },
  {
    path: '/onlineReport',
    name: 'onlineReport',
    component: () => import('@/views/luckyreport/onlineReport.vue'),
    meta: {
      title: '协同编辑',
    },
  },
  {
    path: '/h5ReportPreview',
    name: 'h5ReportPreview',
    component: () => import('@/views/h5Report/index.vue'),
    meta: {
      title: '报表查看',
    },
  },
  {
    path: '/luckyReportPreview',
    name: 'luckyReportPreview',
    component: () => import('@/views/luckyreport/luckyReportPreview.vue'),
    meta: {
      title: 'Excel报表查看',
    },
  },
  {
    path: '/coedit',
    name: 'coedit',
    component: () => import('@/views/coedit/coedit.vue'),
    meta: {
      title: '协同编辑',
    }
  },
  {
    path: '/docDesign',
    name: 'docDesign',
    component: () => import('@/views/editor/docDesign.vue'),
    meta: {
      title: 'doc设计',
    },
  },
  {
    path: '/docPreview',
    name: 'docPreview',
    component: () => import('@/views/editor/docPreview.vue'),
    meta: {
      title: 'doc预览',
    },
  },
  {
    path: '/screenDesign',
    name: 'screenDesign',
    component: () => import('@/views/screen/screendesign/screenDesign.vue'),
    meta: {
      title: '大屏设计',
    },
  },
  {
    path: '/screenView',
    name: 'screenView',
    component: () => import('@/views/screen/screendesign/screenView.vue'),
    meta: {
      title: '大屏查看',
    },
  },
  {
    path: "/attachment",
    name: "attachment",
    component: () => import("@/views/attachment/attachment.vue"),
    meta: {
      title: "附件查看",
    },
  },
  // {
  //   path: '/slideDesign',
  //   name: 'slideDesign',
  //   component: () => import('@/views/slide/slideDesign.vue'),
  //   meta: {
  //     title: 'PPT设计',
  //   },
  // },
  // {
  //   path: '/slidePreview',
  //   name: 'slidePreview',
  //   component: () => import('@/views/slide/slidePreview.vue'),
  //   meta: {
  //     title: 'PPT预览',
  //   },
  // },
  // {
  //   path: '/',
  //   component: Layout,
  //   redirect: '/index',
  //   name: 'Root',
  //   children: [
  //   ],
  // },
  
  {
    path: '/index',
    name: 'Index',
    component: () => import('../views/index/index.vue'),
    meta: {
      title: '首页',
      icon: 'icon-home',
      affix: true,
      noKeepAlive: true,
    },
  },
  {
    path: '/tempRefresh',
    name: 'tempRefresh',
    component: () => import('@/views/refresh/tempRefresh.vue'),
    meta: {
      title: '刷新',
      icon: 'icon-like',
    },
  },
  {
    path: '/iconPark',
    name: 'IconPark',
    component: () => import('@/views/icon/index.vue'),
    meta: {
      title: '图标',
      icon: 'icon-like',
    },
  },
  {
    path: '/sysUser',
    name: 'sysUser',
    component: () => import('@/views/sysuser/SysUser.vue'),
    meta: {
      title: '用户管理',
      icon: 'icon-peoples',
    },
  },
  {
    path: '/sysRole',
    name: 'sysRole',
    component: () => import('@/views/sysrole/SysRole.vue'),
    meta: {
      title: '角色管理',
      icon: 'icon-user',
    },
  },
  {
    path: '/sysMenu',
    name: 'sysMenu',
    component: () => import('@/views/sysmenu/SysMenu.vue'),
    meta: {
      title: '菜单管理',
      icon: 'icon-music-list',
    },
  },
  {
    path: '/sysApi',
    name: 'sysApi',
    component: () => import('@/views/sysapi/SysApi.vue'),
    meta: {
      title: '菜单功能',
      icon: 'el-icon-menu',
    },
  },
  {
    path: '/sysDept',
    name: 'sysDept',
    component: () => import('@/views/sysdept/SysDept.vue'),
    meta: {
      title: '部门管理',
      icon: 'icon-network-tree',
    },
  },
  {
    path: '/sysPost',
    name: 'sysPost',
    component: () => import('@/views/syspost/SysPost.vue'),
    meta: {
      title: '岗位管理',
      icon: 'icon-user-positioning',
    },
  },
  {
    path: '/sysMerchantAuthTemplate',
    name: 'sysMerchantAuthTemplate',
    component: () => import('@/views/sysmerchantauthtemplate/SysMerchantAuthTemplate.vue'),
    meta: {
      title: '权限模板',
      icon: 'icon-permissions',
    },
  },
  {
    path: '/sysMerchant',
    name: 'sysMerchant',
    component: () => import('@/views/sysmerchant/SysMerchant.vue'),
    meta: {
      title: '租户管理',
      icon: 'icon-user-business',
    },
  },
  {
    path: '/reportType',
    name: 'reportType',
    component: () => import('@/views/reporttype/ReportType.vue'),
    meta: {
      title: '报表类型管理',
      icon: 'icon-table-report',
    },
  },
  {
    path: '/reportDatasource',
    name: 'reportDatasource',
    component: () => import('@/views/reportdatasource/ReportDatasource.vue'),
    meta: {
      title: '数据源管理',
      icon: 'icon-data',
    },
  },
  {
    path: '/reportDatasourceDictType',
    name: 'reportDatasourceDictType',
    component: () => import('@/views/reportdatasourcedicttype/ReportDatasourceDictType.vue'),
    meta: {
      title: '数据字典类型',
      icon: 'icon-data',
    },
  },
  {
    path: '/reportDatasourceDictData',
    name: 'reportDatasourceDictData',
    component: () => import('@/views/reportdatasourcedictdata/ReportDatasourceDictData.vue'),
    meta: {
      title: '数据字典值',
      icon: 'icon-data',
    },
  },
  {
    path: '/onlineTpl',
    name: 'onlineTpl',
    component: () => import('@/views/onlinetpl/OnlineTpl.vue'),
    meta: {
      title: 'Excel协同文档',
      icon: 'icon-excel',
    },
  },
  {
    path: '/reportTpl',
    name: 'reportTpl',
    component: () => import('@/views/reporttpl/ReportTpl.vue'),
    meta: {
      title: 'Excel报表',
      icon: 'icon-table',
    },
  },
  {
    path: '/viewReport',
    name: 'viewReport',
    component: () => import('@/views/viewReport/viewReport.vue'),
    meta: {
      title: 'Excel报表查看',
      icon: 'icon-eyes',
    },
  },
  {
    path: '/reportTask',
    name: 'reportTask',
    component: () => import('@/views/qrtzreportdetail/QrtzReportDetail.vue'),
    meta: {
      title: '报表定时任务',
      icon: 'icon-timer',
    },
  },
  {
    path: '/docTpl',
    name: 'docTpl',
    component: () => import('@/views/doctpl/DocTpl.vue'),
    meta: {
      title: 'Word报表',
      icon: 'icon-word',
    },
  },
  {
    path: '/screenTpl',
    name: 'screenTpl',
    component: () => import('@/views/screen/screentpl/ScreenTpl.vue'),
    meta: {
      title: '大屏模板管理',
      icon: 'icon-word',
    },
  },
  // {
  //   path: '/slideTpl',
  //   name: 'slideTpl',
  //   component: () => import('@/views/slidetpl/SlideTpl.vue'),
  //   meta: {
  //     title: 'PPT模板管理',
  //     icon: 'icon-word',
  //   },
  // },
  {
    path: '/errorPage',
    name: 'ErrorPage',
    component: Layout,
    meta: {
      title: '错误页面',
      icon: 'icon-link-cloud-faild',
    },
    children: [
      {
        path: '/404Page',
        name: '404Page',
        component: () => import('@/views/errorPage/404.vue'),
        meta: {
          title: '404',
          icon: 'icon-link-cloud-faild',
        },
      },
      {
        path: '/401Page',
        name: '401Page',
        component: () => import('@/views/errorPage/401.vue'),
        meta: {
          title: '401',
          icon: 'icon-link-interrupt',
        },
      },
    ],
  },
];

export const asyncRoutes = [

];

const router = createRouter({
  history: createWebHistory("/SpringReport-vue3"),
  routes: constantRoutes,
  // routes:constantThirdPartyRoutes,
});

// reset router
export function resetRouter() {
  router.getRoutes().forEach((route) => {
    const { name } = route;
    if (name) {
      router.hasRoute(name) && router.removeRoute(name);
    }
  });
}
axios.defaults.timeout = 300000; // 毫秒

axios.defaults.baseURL = "/SpringReport";
export default router;
