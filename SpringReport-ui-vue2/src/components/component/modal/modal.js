import modalComponent from './modal.vue';
const modal={
        install:function (Vue){
            Vue.component('modal',modalComponent)
        }
}
export default modal