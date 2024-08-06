/*
 * @Description: 
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-07-01 18:19:29
 * @LastEditors: caiyang
 * @LastEditTime: 2021-07-01 18:20:44
 */
import draggablesComponent from './draggables.vue';
const draggables={
        install:function (Vue){
            Vue.component('draggables',draggablesComponent)
        }
}
export default draggables