import uploadComponent from './upload.vue';
const upload={
        install:function (Vue){
            Vue.component('upload',uploadComponent)
        }
}
export default upload