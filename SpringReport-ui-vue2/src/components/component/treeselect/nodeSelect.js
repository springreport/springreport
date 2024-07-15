import nodeSelectComponent from './nodeSelect.vue';
const nodeSelect={
        install:function (Vue){
            Vue.component('nodeSelect',nodeSelectComponent)
        }
}
export default nodeSelect