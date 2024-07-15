/*
 * @Description: 多选下拉树
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-10 13:23:11
 * @LastEditors: caiyang
 * @LastEditTime: 2020-06-10 13:24:07
 */ 
import multiselectNodeComponent from './multiselectNode.vue';
const multiselectNode={
        install:function (Vue){
            Vue.component('multiselectNode',multiselectNodeComponent)
        }
}
export default multiselectNode