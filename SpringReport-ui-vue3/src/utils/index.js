/**
 * @author hu-snail 1217437592@qq.com
 * @description 常用公共工具函数
 */

import { setting } from '@/config/setting';
const { title } = setting;

export const getPageTitle = (pageTitle) => {
  if (pageTitle) {
    return `${pageTitle}-${title}`;
  }
  return `${title}`;
};
