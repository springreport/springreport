/**
 * @description 配置axios请求基础信息
 * @author hu-snail 1217437592@qq.com
 */

export const netConfig = {
  // axios 基础url地址
  baseURL: process.env.NODE_ENV === 'development' ? 'http://localhost:8089/api' : '/api',
  // 为开发服务器配置 CORS。默认启用并允许任何源，传递一个 选项对象 来调整行为或设为 false 表示禁用
  cors: true,
  // 根据后端定义配置
  contentType: 'application/json;charset=UTF-8',
  //消息框消失时间
  messageDuration: 3000,
  //最长请求时间
  requestTimeout: 300000,
  //操作正常code，支持String、Array、int多种类型
  successCode: [200, 0],
  //登录失效code
  invalidCode: -1,
  //无权限code
  noPermissionCode: -1,
};
