/*
 * @Author: liren
 * @Date: 2022-10-10 14:03:12
 * @LastEditTime: 2022-10-10 15:48:25
 * @Description: 设置深色主题
 */

import themes from './theme.json' // 主题样式：深色

export function setDarkTheme() {
  const { black, obj = {}} = themes
  Object.assign(obj, black) // 拷贝对象属性
  // html 添加主题样式
  for (const key in obj) {
    document.documentElement.style.setProperty(key, obj[key])
  }
}
