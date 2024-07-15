/*
 * @Description: 
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-16 14:06:15
 * @LastEditors: caiyang
 * @LastEditTime: 2020-06-16 14:12:30
 */ 
import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
Vue.use(Vuex)
export default new Vuex.Store({
    plugins: [createPersistedState()],
    state: {
        parameters:{}//保存页面传参
    },
    getters: {
        parameters : state => {//获取方法
            return state.parameters;
        }
    },
    mutations: {
        setParameters : (state,parametersData) => {//页面参数传递格式{key:'key',value:'value'}
            if(parametersData){
                //试了好久state.parameters[key]=value这样会报错只能先取出来添加属性再赋值
                let parameters=state.parameters;
                parameters[parametersData.key]=parametersData.value;
                state.parameters=parameters;
            }
        }
    },
    actions: {
    },
    modules: {
    }
})
