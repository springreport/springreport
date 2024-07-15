// import { getUserInfo, login } from '@/api/user';
import { getAccessToken, removeAccessToken, setAccessToken } from '@/utils/accessToken';

import { setting } from '@/config/setting';
const { title, tokenName } = setting;
import { resetRouter } from '@/router';

import { ElMessage, ElNotification } from 'element-plus';

const state = {
  accessToken: getAccessToken(),
  username: '',
  avatar: '',
  permissions: [],
};

const getters = {
};
const mutations = {
};
const actions = {
  async logout({ dispatch }) {
  },
  resetAccessToken({ commit }) {
  },
};
export default { state, getters, mutations, actions };
