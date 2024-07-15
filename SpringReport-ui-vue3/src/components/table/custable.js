import cusTableComponent from './custable.vue';
const cusTable={
        install:function (Vue){
            Vue.component('cusTable',cusTableComponent)
        }
}
export default cusTable