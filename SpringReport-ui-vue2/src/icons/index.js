/*
 * @Author: liren
 * @Date: 2022-09-26 13:52:37
 * @LastEditTime: 2022-09-28 11:04:24
 * @Description:
 */
import Vue from 'vue'
import SvgIcon from '../components/component/SvgIcon/index.vue'// svg component

// register globally
Vue.component('svg-icon', SvgIcon)

const req = require.context('./svg', false, /\.svg$/)
const requireAll = requireContext => requireContext.keys().map(requireContext)
requireAll(req)

// console.log('**********')
// console.log(requireAll(req))
