import selectNodeComponent from './selectNode.vue';
const selectNode={
        install:function (Vue){
            Vue.component('selectNode',selectNodeComponent)
        }
}
export default selectNode