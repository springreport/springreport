/*
 * @Description: 地区控件
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-22 10:13:26
 * @LastEditors: caiyang
 * @LastEditTime: 2020-06-22 10:54:38
 */ 
import regionComponent from './region.vue';
const region={
        install:function (Vue){
            Vue.component('region',regionComponent)
        }
}
export default region