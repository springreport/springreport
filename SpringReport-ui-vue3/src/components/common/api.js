/*
 * @Description: 请求接口用配置文件
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021年2月12日20:20:19
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-07-12 07:48:32
 */
const api = {};

//登录接口
api.login = {
  loginApi: '/api/login/doLogin',
  getMerchantModeApi: '/api/login/getMerchantMode',
  getMerchantListApi: '/api/sysMerchant/getMerchantList',
  getUserInfoByTokenApi: '/api/login/getUserInfoByToken',
};

//首页接口
api.index = {
  getIndexMenuApi: '/api/sysMenu/getIndexMenu', //获取首页菜单
  getIndexDataApi: '/api/index/getIndexData',
};

//报表类型controller对应的api
api.reportType = {
  listApi: '/api/reportType/getTableList', //获取表格数据api
  insertApi: '/api/reportType/insert', //新增用api
  updateApi: '/api/reportType/update', //更新用api
  getDetailApi: '/api/reportType/getDetail', //获取详情用api
  deleteOneApi: '/api/reportType/delete', //单条删除api
  deleteBatchApi: '/api/reportType/deletebatch', //批量删除api
  getReportTypeApi: '/api/reportType/getReportType', //获取报表类型api
  getReportTypeTreeApi: '/api/reportType/getReportTypeTree', //获取报表类型树api
};

//报表数据库controller对应的api
api.reportDatasource = {
  listApi: '/api/reportDatasource/getTableList', //获取表格数据api
  insertApi: '/api/reportDatasource/insert', //新增用api
  updateApi: '/api/reportDatasource/update', //更新用api
  getDetailApi: '/api/reportDatasource/getDetail', //获取详情用api
  deleteOneApi: '/api/reportDatasource/delete', //单条删除api
  deleteBatchApi: '/api/reportDatasource/deletebatch', //批量删除api
  getReportDatasourceApi: '/api/reportDatasource/getReportDatasource', //获取数据源
  connectionTestApi: '/api/reportDatasource/connectionTest', //连接测试
  getDatasourceSelectDataApi: '/api/reportDatasource/getDatasourceSelectData', //获取下拉框数据
  getDatabseTablesApi: '/api/reportDatasource/getDatabseTables', //获取数据源表信息
};
api.sysRoleReport = {
  // listApi:"/api/sysRoleReport/getReports",//获取表格数据api
};

//报表管理对应的api
api.reportTpl = {
  listApi: '/api/reportTpl/getTableList', //获取表格数据api
  insertApi: '/api/reportTpl/insert', //新增用api
  updateApi: '/api/reportTpl/update', //更新用api
  getDetailApi: '/api/reportTpl/getDetail', //获取详情用api
  deleteOneApi: '/api/reportTpl/delete', //单条删除api
  deleteBatchApi: '/api/reportTpl/deletebatch', //批量删除api
  validateDesignPwdApi: '/api/reportTpl/validateDesignPwd', //校验设计密码
  changeDesignPwdApi: '/api/reportTpl/changeDesignPwd', //修改密码
  getRoleReportsApi: '/api/reportTpl/getRoleReports', //获取角色报表
  doCopyReportApi: '/api/reportTpl/copyReport', //复制报表
  uploadExcelApi: '/api/reportTpl/uploadExcel', //上传excel模板
  getShareUrlApi: '/api/reportTpl/getShareUrl', //获取分享链接
  getShareDetailApi: '/api/reportTpl/getShareDetail', //获取详情用api(分享链接用)
  uploadReportTplApi: '/api/reportTpl/uploadReportTpl', //上传excel模板
  getAllTplsApi: '/api/reportTpl/getAllTpls', //获取表格数据api
  getTplAuthApi: '/api/reportTpl/getTplAuth', //获取授权范围
  getChildrenApi: '/api/reportTpl/getChildren',
};

//报表设计对应的api
api.reportDesign = {
  getReportTplDateSourceApi: '/api/reportTplDatasource/getReportTplDatasoure', //获取模板关联的数据源api
  deleteDataSetApi: '/api/reportTplDataset/delete', //删除数据集api
  execSqlApi: '/api/reportDatasource/execSql', //执行sql语句并解析
  getDataSetsApi: '/api/reportTplDataset/getTplDatasets', //获取报表模板数据集api
  addDataSetApi: '/api/reportTplDataset/addTplDataSets', //添加报表模板数据集api
  saveReportTplApi: '/api/reportTpl/saveTpl', //保存报表模板api
  saveLuckySheetTplApi: '/api/reportTpl/saveLuckySheetTpl', //保存报表模板api
  getTplSettingsApi: '/api/reportTpl/getTplSettings', //获取报表模板设置项api
  getLuckySheetTplSettingsApi: '/api/reportTpl/getLuckySheetTplSettings',
  getDataSetColumnsApi: '/api/reportTplDataset/getDatasetColumns', //获取数据集字段api
  saveReportFormsTplApi: '/api/reportTpl/saveReportFormsTpl', //保存填报报表模板api
  getReportFormsTplApi: '/api/reportTpl/getReportFormsTpl', //获取填报报表模板api
  getApiDefaultRequestResultApi: '/api/reportTplDataset/getApiDefaultRequestResult', //获取接口返回值
  getTableListApi: '/api/reportTplDatasetGroup/getTableList', // 获取分组列表
  insertGroupApi: '/api/reportTplDatasetGroup/insert', // 新增分组
  updateGroupApi: '/api/reportTplDatasetGroup/update', // 修改分组
  deleteGroupApi: '/api/reportTplDatasetGroup/delete', // 删除分组
  getTplGroupDatasetsApi: '/api/reportTplDataset/getTplGroupDatasets', // 获取分组数据集
  deleteReportDataApi:'/api/reportTpl/deleteReportData',//填报报表删除数据
};

//预览报表对应的api
api.previewReport = {
  getPreviewReportParamApi: '/api/reportTplDataset/getReportDatasetsParam', //获取数据集参数api
  getPreviewReportDataApi: '/api/reportTpl/previewReportData', //获取预览数据api
  getLuckyPreviewReportDataApi: '/api/reportTpl/previewLuckysheetReportData', //获取预览数据api
  luckySheetExportExcelApi: '/api/reportTpl/luckySheetExportExcel', //导出luckysheetexcelapi
  previewLuckysheetFormsReportData: '/api/reportTpl/previewLuckysheetFormsReportData', //获取填报报表预览数据api
  reportDataApi: '/api/reportTpl/reportData', //上报数据
  transf2OnlineReportApi: '/api/reportTpl/transf2OnlineReport', //转成在线报表文档
  getSheetPdfApi: '/api/reportTpl/getSheetPdf', //转成pdf文档接口
  getSharePreviewReportParamApi: '/api/reportTplDataset/getShareReportDatasetsParam', //获取数据集参数api(分享链接用)
  getShareLuckyPreviewReportDataApi: '/api/reportTpl/previewShareLuckysheetReportData', //获取预览数据api(分享链接用)
  previewShareLuckysheetFormsReportData: '/api/reportTpl/previewShareLuckysheetFormsReportData', //获取填报报表预览数据api(分享链接用)
  shareReportDataApi: '/api/reportTpl/shareReportData', //上报数据
  shareLuckySheetExportExcelApi: '/api/reportTpl/shareLuckySheetExportExcel', //导出luckysheetexcelapi(分享链接用)
  getShareSheetPdfApi: '/api/reportTpl/getShareSheetPdf', //转成pdf文档接口
  getMobileReportApi: '/api/reportTpl/getMobileReport', //获取手机端报表信息
  getMobileShareReportApi: '/api/reportTpl/getMobileShareReport', //获取手机端报表信息
  getSheetPdfStreamApi: '/api/reportTpl/getSheetPdfStream', //获取打印pdf文件流
  getShareSheetPdfStreamApi: '/api/reportTpl/getShareSheetPdfStream', //获取打印pdf文件流
};

//用户管理对应的api
api.sysUser = {
  listApi: '/api/sysUser/getTableList', //获取表格数据api
  insertApi: '/api/sysUser/insert', //新增用api
  updateApi: '/api/sysUser/update', //更新用api
  getDetailApi: '/api/sysUser/getDetail', //获取详情用api
  deleteOneApi: '/api/sysUser/delete', //单条删除api
  deleteBatchApi: '/api/sysUser/deletebatch', //批量删除api
  getRolesApi: '/api/sysRole/getRoles', //获取角色api
  resetPwdApi: '/api/sysUser/resetPwd', //重置密码
  changePwdApi: '/api/sysUser/changePwd', //修改密码
  getUsersApi: '/api/sysUser/getUsers', //获取用户
  getDeptUserTreeApi: '/api/sysUser/getDeptUserTree', //获取部门用户树
};

//角色管理对应的api
api.sysRole = {
  listApi: '/api/sysRole/getTableList', //获取表格数据api
  insertApi: '/api/sysRole/insert', //新增用api
  updateApi: '/api/sysRole/update', //更新用api
  getDetailApi: '/api/sysRole/getDetail', //获取详情用api
  deleteOneApi: '/api/sysRole/delete', //单条删除api
  deleteBatchApi: '/api/sysRole/deletebatch', //批量删除api
  getAuthTreeApi: '/api/sysMenu/getAuthTree', //获取权限树
  authApi: '/api/sysRole/authed', //授权api
  getReportTreeApi: '/api/sysRoleSheet/getReportTree', //获取报表树api
  reportAuthApi: '/api/sysRoleSheet/authed', //报表授权api
};

//菜单管理对应的api
api.sysMenu = {
  listApi: '/api/sysMenu/getTableList', //获取表格数据api
  insertApi: '/api/sysMenu/insert', //新增用api
  updateApi: '/api/sysMenu/update', //更新用api
  getDetailApi: '/api/sysMenu/getDetail', //获取详情用api
  deleteOneApi: '/api/sysMenu/delete', //单条删除api
  deleteBatchApi: '/api/sysMenu/deletebatch', //批量删除api
  getParentMenusApi: '/api/sysMenu/getParentMenus', //获取上级菜单api
};

//功能管理对应的api
api.sysApi = {
  listApi: '/api/sysApi/getTableList', //获取表格数据api
  insertApi: '/api/sysApi/insert', //新增用api
  updateApi: '/api/sysApi/update', //更新用api
  getDetailApi: '/api/sysApi/getDetail', //获取详情用api
  deleteOneApi: '/api/sysApi/delete', //单条删除api
  deleteBatchApi: '/api/sysApi/deletebatch', //批量删除api
};

//大屏模板管理对应的api
api.screenTpl = {
  listApi: '/api/screenTpl/getTableList', //获取表格数据api
  insertApi: '/api/screenTpl/insert', //新增用api
  updateApi: '/api/screenTpl/update', //更新用api
  getDetailApi: '/api/screenTpl/getDetail', //获取详情用api
  deleteOneApi: '/api/screenTpl/delete', //单条删除api
  deleteBatchApi: '/api/screenTpl/deletebatch', //批量删除api
  getScreenDesignApi: '/api/screenTpl/getScreenDesign', //获取大屏设计模板
  saveScreenDesignApi: '/api/screenTpl/saveScreenDesign', //保存大屏模板设计
  refreshScreenApi: '/api/screenTpl/refreshScreen', //刷新大屏用api
  getMultiScreenApi: '/api/screenTpl/getMultiScreen', //多大屏获取
  getMultiScreenAndTplidsApi: '/api/screenTpl/getMultiScreenAndTplids', //获取多大屏基本信息和关联的大屏id
  getScreensApi: '/api/screenTpl/getScreens', //获取所有大屏api
  doCopyScreenApi: '/api/screenTpl/copyScreen', //复制大屏
  getChildrenApi: '/api/screenTpl/getChildren',
};

//大屏组件对应的api
api.screenContent = {
  listApi: '/api/screenContent/getTableList', //获取表格数据api
  refreshComponentApi: '/api/screenContent/refreshComponent', //刷新组件用api
};

//大屏设计对应的api
api.screenDesign = {
  uploadFileApi: '/api/common/upload', //上传图片
  shareUploadFileApi: '/api/common/shareUpload', //上传图片(分享链接用)
  getDynamicDatasApi: '/api/screenDesign/getDynamicDatas', //获取动态数据
  deleteDatasetApi: '/api/reportTplDataset/delete', //删除数据集
};

//打印模板页面对应的api
api.printTpl = {
  listApi: '/api/printTpl/getTableList', //获取表格数据api
  insertApi: '/api/printTpl/insert', //新增用api
  updateApi: '/api/printTpl/update', //更新用api
  getDetailApi: '/api/printTpl/getDetail', //获取详情用api
  deleteOneApi: '/api/printTpl/delete', //单条删除api
  deleteBatchApi: '/api/printTpl/deletebatch', //批量删除api
  getPrintDesignApi: '/api/printTpl/getPrintDesign', //获取大屏设计模板
  getPrintTplDataApi: '/api/printTpl/getPrintTplData', //获取打印模板以及数据
};

//打印设计对应的api
api.printDesign = {
  savePrintDesignApi: '/api/printTpl/savePrintDesign', //保存打印模板设计
  apiTestApi: '/api/common/printApiTest', //接口测试
};

//word设计对应的api
api.wordTpl = {
  listApi: '/api/wordTpl/getTableList', //获取表格数据api
  insertApi: '/api/wordTpl/insert', //新增用api
  updateApi: '/api/wordTpl/update', //更新用api
  getDetailApi: '/api/wordTpl/getDetail', //获取详情用api
  deleteOneApi: '/api/wordTpl/delete', //单条删除api
  deleteBatchApi: '/api/wordTpl/deletebatch', //批量删除api
  saveTplApi: '/api/wordTpl/saveTpl',
};

//多大屏对应的url
api.multiScreen = {
  listApi: '/api/multiScreen/getTableList', //获取表格数据api
  insertApi: '/api/multiScreen/insert', //新增用api
  updateApi: '/api/multiScreen/update', //更新用api
  getDetailApi: '/api/multiScreen/getDetail', //获取详情用api
  deleteOneApi: '/api/multiScreen/delete', //单条删除api
  deleteBatchApi: '/api/multiScreen/deletebatch', //批量删除api
};

//数据源数据字典对应的url
api.reportDatasourceDictType = {
  listApi: '/api/reportDatasourceDictType/getTableList', //获取表格数据api
  insertApi: '/api/reportDatasourceDictType/insert', //新增用api
  updateApi: '/api/reportDatasourceDictType/update', //更新用api
  getDetailApi: '/api/reportDatasourceDictType//getDetail', //获取详情用api
  deleteOneApi: '/api/reportDatasourceDictType/delete', //单条删除api
  deleteBatchApi: '/api/reportDatasourceDictType/deletebatch', //批量删除api
  getDatasourceDictTypesApi: '/api/reportDatasourceDictType/getDatasourceDictTypes', //获取数据源字典类型api
};

//数据源字典值对应的url
api.reportDatasourceDictData = {
  listApi: '/api/reportDatasourceDictData/getTableList', //获取表格数据api
  insertApi: '/api/reportDatasourceDictData/insert', //新增用api
  updateApi: '/api/reportDatasourceDictData/update', //更新用api
  getDetailApi: '/api/reportDatasourceDictData//getDetail', //获取详情用api
  deleteOneApi: '/api/reportDatasourceDictData/delete', //单条删除api
  deleteBatchApi: '/api/reportDatasourceDictData/deletebatch', //批量删除api
  getDictDatasApi: '/api/reportDatasourceDictData/getDictDatas', //获取数据源数据字典值api
  getShareDictDatasApi: '/api/reportDatasourceDictData/getShareDictDatas', //获取数据源数据字典值api
};

api.luckysheetReportFormsHis = {
  listApi: '/api/luckysheetReportFormsHis/getTableList', //获取表格数据api
  shareListApi: '/api/luckysheetReportFormsHis/getShareTableList', //获取表格数据api
};

api.onlineTpl = {
  listApi: '/api/onlineTpl/getTableList', //获取表格数据api
  insertApi: '/api/onlineTpl/insert', //新增用api
  updateApi: '/api/onlineTpl/update', //更新用api
  getDetailApi: '/api/onlineTpl//getDetail', //获取详情用api
  deleteOneApi: '/api/onlineTpl/delete', //单条删除api
  deleteBatchApi: '/api/onlineTpl/deletebatch', //批量删除api
  getOnlineTplSettingsApi: '/api/onlineTpl/getOnlineTplSettings',
  // saveOnlineDocApi: "/api/onlineTpl/saveOnlineDoc",
  getOnlineTplInfoApi: '/api/onlineTpl/getOnlineTplInfo',
  rangeAuthApi: '/api/onlineTpl/rangeAuth',
  deleteRangeAuthApi: '/api/onlineTpl/deletRangeAuth',
  getCoeditAuthApi: '/api/onlineTpl/getCoeditAuth', //获取授权范围
  getRangeUsersApi: '/api/reportRangeAuthUser/getRangeUsers', //获取授权用户
  getChildrenApi: '/api/onlineTpl/getChildren',
};

api.reportTask = {
  listApi: '/api/qrtzReportDetail/getTableList', //获取表格数据api
  insertApi: '/api/qrtzReportDetail/insert', //新增用api
  updateApi: '/api/qrtzReportDetail/update', //更新用api
  getDetailApi: '/api/qrtzReportDetail/getDetail', //获取详情用api
  deleteOneApi: '/api/qrtzReportDetail/delete', //单条删除api
  deleteBatchApi: '/api/qrtzReportDetail/deletebatch', //批量删除api
  runTaskApi: '/api/qrtzReportDetail/runTask', //立即执行
  pauseTaskApi: '/api/qrtzReportDetail/pauseTask', //暂停任务
  resumeTaskApi: '/api/qrtzReportDetail/resumeTask', //恢复任务,
  getIndexTaskListApi: '/api/qrtzReportDetail/getIndexTaskList', // 获取首页任务列表
};

api.luckysheetHis = {
  listApi: '/api/luckysheetHis/getTableList', //获取表格数据api
};

api.coedit = {
  downLoadExcelApi: '/api/coedit/downLoadExcel', //获取表格数据api
  beforeEnterShareModeApi: '/api/coedit/beforeEnterShareMode', //进入共享模式前调用api
  getRangeValuesApi: '/api/coedit/getRangeValues', //获取范围内的单元格数据
};

api.sysPost = {
  listApi: '/api/sysPost/getTableList', //获取表格数据api
  insertApi: '/api/sysPost/insert', //新增用api
  updateApi: '/api/sysPost/update', //更新用api
  getDetailApi: '/api/sysPost//getDetail', //获取详情用api
  deleteOneApi: '/api/sysPost/delete', //单条删除api
  deleteBatchApi: '/api/sysPost/deletebatch', //批量删除api
  getSysPostsApi: '/api/sysPost/getSysPosts', //获取所有岗位
};

api.sysMerchantAuthTemplate = {
  listApi: '/api/sysMerchantAuthTemplate/getTableList', //获取表格数据api
  insertApi: '/api/sysMerchantAuthTemplate/insert', //新增用api
  updateApi: '/api/sysMerchantAuthTemplate/update', //更新用api
  getDetailApi: '/api/sysMerchantAuthTemplate/getDetail', //获取详情用api
  deleteOneApi: '/api/sysMerchantAuthTemplate/delete', //单条删除api
  deleteBatchApi: '/api/sysMerchantAuthTemplate/deletebatch', //批量删除api
  getMerchantTemplateAuthTreeApi: '/api/sysMerchantAuthTemplate/getMerchantTemplateAuthTree',
  getAuthTemplatesApi: '/api/sysMerchantAuthTemplate/getAuthTemplates',
};

api.sysMerchant = {
  listApi: '/api/sysMerchant/getTableList', //获取表格数据api
  insertApi: '/api/sysMerchant/insert', //新增用api
  updateApi: '/api/sysMerchant/update', //更新用api
  getDetailApi: '/api/sysMerchant//getDetail', //获取详情用api
  deleteOneApi: '/api/sysMerchant/delete', //单条删除api
  deleteBatchApi: '/api/sysMerchant/deletebatch', //批量删除api
};

api.sysDept = {
  listApi: '/api/sysDept/getTableList', //获取表格数据api
  insertApi: '/api/sysDept/insert', //新增用api
  updateApi: '/api/sysDept/update', //更新用api
  getDetailApi: '/api/sysDept//getDetail', //获取详情用api
  deleteOneApi: '/api/sysDept/delete', //单条删除api
  deleteBatchApi: '/api/sysDept/deletebatch', //批量删除api
};

api.docTpl = {
  listApi: '/api/docTpl/getTableList', //获取表格数据api
  insertApi: '/api/docTpl/insert', //新增用api
  updateApi: '/api/docTpl/update', //更新用api
  getDetailApi: '/api/docTpl//getDetail', //获取详情用api
  deleteOneApi: '/api/docTpl/delete', //单条删除api
  deleteBatchApi: '/api/docTpl/deletebatch', //批量删除api
  getDocTplSettingsApi: '/api/docTpl/getDocTplSettings', //获取文档模板api
  saveDocTplSettingsApi: '/api/docTpl/saveDocTplSettings', //保存文档模板api
  downLoadDocTplApi: '/api/docTpl/downLoadDocTpl', //导出模板
  previewDocApi: '/api/docTpl/previewDoc', //保存文档模板api
  uploadDocxApi: '/api/docTpl/uploadDocx', //上传docx文件
  getChildrenApi: '/api/docTpl/getChildren',
  doCopyReportApi:'/api/docTpl/copyReport',
};

api.common = {
  uploadFileApi: '/api/common/uploadFile',
  parseXlsxByUrlApi: '/api/common/parseXlsxByUrl',
};

api.onlyoffice = {
  listApi:"/api/docOnlyOffice/getTableList",//获取表格数据api
  insertApi:"/api/docOnlyOffice/insert",//新增用api
  updateApi:"/api/docOnlyOffice/update",//更新用api
  getDetailApi:"/api/docOnlyOffice//getDetail",//获取详情用api
  deleteOneApi:"/api/docOnlyOffice/delete",//单条删除api
  deleteBatchApi:"/api/docOnlyOffice/deletebatch",//批量删除api
  getChildrenApi:"/api/docOnlyOffice/getChildren",
  getDocByIdApi:"/api/docOnlyOffice/getDocById",
  getPreviewReportParamApi: '/api/docOnlyOffice/getReportDatasetsParam', // 获取数据集参数api
  previewDocApi: '/api/docOnlyOffice/previewDoc', // 预览word文档api
  callbackApi:'/SpringReport/api/onlyoffice/callback'
}

api.springreportField = {
  getTemplateFieldTreeApi:"/api/springreportField/getTemplateFieldTree",
  insertApi:"/api/springreportField/insert",
  updateApi:"/api/springreportField/update",
  getTemplateFieldsApi:"/api/springreportField/getTemplateFields",
  deleteOneApi:"/api/springreportField/delete",
  getTemplatesApi:"/api/springreportField/getTemplates",
}
export default api;
