/**
 * @description 主题全局配置状态
 * @author hu-snail 1217437592@qq.com
 */

import { themeConfig } from '@/config/theme';
import { setting } from '@/config/setting';
import { getLanguage, setLanguage, setSettings, getSettings } from '@/utils/cookies';

const { mode, theme, fixedHead, fullScreen, refresh, collapse, notice, isBreadcrumb, isLogo, tag } =
  themeConfig;

const { lang } = setting;

const state = {
  routerView: true, // 是否显示路由
  isDrawerSetting: false, // 是否打开主题设置
  isMobile: false, // 是否为移动端
  isDrawer: false, // 是否展开移动端菜单
  isFullScreen: false, // 是否显示全屏
  collapse,
  fullScreen,
  refresh,
  mode: getSettings() ? getSettings().mode : mode,
  theme,
  fixedHead,
  notice,
  isBreadcrumb,
  isLogo,
  tag,
  lang: getLanguage() || lang,
};

const getters = {
  routerView: (state) => state.routerView,
  isMobile: (state) => state.isMobile,
  isDrawer: (state) => state.isDrawer,
  isFullScreen: (state) => state.isFullScreen,
  theme: (state) => state.theme,
  isDrawerSetting: (state) => state.isDrawerSetting,
  fullScreen: (state) => state.fullScreen,
  refresh: (state) => state.refresh,
  fixedHead: (state) => state.fixedHead,
  notice: (state) => state.notice,
  isBreadcrumb: (state) => state.isBreadcrumb,
  isLogo: (state) => state.isLogo,
  tag: (state) => state.tag,
  mode: (state) => state.mode,
  settings: (state) => state,
  lang: (state) => state.lang,
};

const mutations = {
  CHANGE_COLLAPSE: (state) => {
    state.collapse = !state.collapse;
  },
  CHANGE_FULL_SCREEN: (state, flag) => {
    state.isFullScreen = flag;
  },
  SET_ROUTER_VIEW: (state) => {
    state.routerView = !state.routerView;
  },
  CHANGE_IS_MOBILE: (state, flag) => {
    state.isMobile = flag;
  },
  CHANGE_IS_DRAWER: (state, flag) => {
    state.isDrawer = flag;
  },
  SET_THEME: (state, theme) => {
    state.theme = theme;
  },
  CHANGE_SETTING_DRAWER: (state, flag) => {
    state.isDrawerSetting = flag;
  },
  CHANGE_BREADCRUMB: (state, flag) => {
    state.isBreadcrumb = flag;
  },
  CHANGE_TAG: (state, flag) => {
    state.tag = flag;
  },
  CHANE_MODE: (state, mode) => {
    state.mode = mode;
  },
  SET_SETTING_OPTIONS: (state, options) => {
    setSettings(options.value);
    Object.assign(state, { ...options.value });
  },
  CHANGE_LANGUAGE: (state, lang) => {
    setLanguage(lang);
    state.lang = lang;
  },
};

const actions = {
  /**
   * @description 切换展开收起
   */
  changeCollapse: ({ commit }) => {
    commit('CHANGE_COLLAPSE');
  },
  /**
   * @description 切换是否全屏
   *  @param {boolean} flag true|false
   */
  changeFullScreen: ({ commit }, flag) => {
    commit('CHANGE_FULL_SCREEN', flag);
  },
  /**
   * @description 是否刷新路由
   *  @param {boolean} flag true|false
   */
  setRouterView: ({ commit }, flag) => {
    commit('SET_ROUTER_VIEW', flag);
  },
  /**
   * @description 是否为移动端
   *  @param {boolean} flag true|false
   */
  changeMobile: ({ commit }, flag) => {
    commit('CHANGE_IS_MOBILE', flag);
  },
  /**
   * @description 是否展开移动端菜单
   *  @param {boolean} flag true|false
   */
  changeDrawer: ({ commit }, flag) => {
    commit('CHANGE_IS_DRAWER', flag);
  },
  /**
   * @description 设置主题
   * @param {strinng} theme 系统默认：blue|green|red|default
   */
  setTheme: ({ commit }, theme) => {
    commit('SET_THEME', theme);
  },

  /**
   * @description 是否打开主题设置
   * @param {boolean} flag true|false
   */
  setSettingDrawer: ({ commit }, flag) => {
    commit('CHANGE_SETTING_DRAWER', flag);
  },
  /**
   * @description 是否显示面包导航
   * @param {boolean} flag true|false
   */
  setBreadcrumb: ({ commit }, flag) => {
    commit('CHANGE_BREADCRUMB', flag);
  },
  /**
   * @description 是否显示标签
   * @param {boolean} flag true|false
   */
  setTag: ({ commit }, flag) => {
    commit('CHANGE_TAG', flag);
  },

  /**
   * @description 切换布局
   * @param {string} mode 可选值：vertical|horizontal
   */
  setMode: ({ commit }, mode) => {
    commit('CHANE_MODE', mode);
  },

  /**
   * @description 切换语言
   * @param {string} lang 语言 可选值： zh-cn|en
   */
  changeLanguage: ({ commit }, lang) => {
    commit('CHANGE_LANGUAGE', lang);
  },

  /**
   * @description 设置主题配置信息
   * @param {object} options 配置项
   */
  setSettingOptions: ({ commit }, options) => {
    commit('SET_SETTING_OPTIONS', options);
  },
};

export default {
  getters,
  state,
  mutations,
  actions,
};
