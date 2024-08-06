import componentFormComponent from './componentForm.vue';
const componentForm={
        install:function (Vue){
            Vue.component('componentForm',componentFormComponent)
        }
}
export default componentForm