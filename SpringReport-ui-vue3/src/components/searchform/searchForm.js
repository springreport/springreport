import searchFormComponent from './searchForm.vue';
const searchForm={
        install:function (Vue){
            Vue.component('searchForm',searchFormComponent)
        }
}
export default searchForm