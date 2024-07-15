/**
 * 字节跳动图标库：https://iconpark.oceanengine.com/official
 * and vue3 https://github.com/bytedance/IconPark/tree/master/packages/vue-next
 * element-plus icon: https://element-plus.gitee.io/#/zh-CN/component/icon
 * @description 图标库按需注册
 * @author hu-snail
 * @example <icon-user theme="outline" size="16" fill="#999" />
 * @example <el-icon :size="20"> <edit /> </el-icon>
 */
import { install } from '@icon-park/vue-next/es/all';
// iconpark
import {
  User,
  Lock,
  Alipay,
  Wechat,
  Github,
  Twitter,
  Google,
  MenuUnfoldOne,
  MenuFoldOne,
  FullScreen,
  OffScreen,
  Refresh,
  Remind,
  AllApplication,
  Close,
  ToLeft,
  ToRight,
  Minus,
  Mail,
  Home,
  Code,
  ChartLine,
  Like,
  Xigua,
  Performance,
  Pic,
  MoveOne,
  Search,
  Tailoring,
  TailoringTwo,
  AddText,
  ScanCode,
  Play,
  PauseOne,
  VolumeNotice,
  VolumeMute,
  PlayCycle,
  PlayOnce,
  GoStart,
  GoEnd,
  MusicList,
  LinkCloudFaild,
  LinkInterrupt,
  Copy,
  ChartHistogram,
  MultiPictureCarousel,
  Theme,
  Translate,
} from '@icon-park/vue-next';
import '@icon-park/vue-next/styles/index.css';
// el-icon
// 所需的组件
export const components = [
  User,
  Lock,
  Alipay,
  Wechat,
  Github,
  Twitter,
  Google,
  MenuUnfoldOne,
  MenuFoldOne,
  FullScreen,
  OffScreen,
  Refresh,
  Remind,
  AllApplication,
  Close,
  ToLeft,
  ToRight,
  Minus,
  Mail,
  Home,
  Code,
  ChartLine,
  Like,
  Xigua,
  Performance,
  Pic,
  MoveOne,
  Search,
  Tailoring,
  TailoringTwo,
  AddText,
  ScanCode,
  Play,
  PauseOne,
  VolumeNotice,
  VolumeMute,
  PlayCycle,
  PlayOnce,
  GoStart,
  GoEnd,
  MusicList,
  LinkCloudFaild,
  LinkInterrupt,
  Copy,
  ChartHistogram,
  MultiPictureCarousel,
  Theme,
  Translate,
];

import SvgIcon from '@/components/SvgIcon/index.vue';
// 注册
export default (app) => {
  app.component('svg-icon', SvgIcon);
  install(app);
};
