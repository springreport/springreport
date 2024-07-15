import fileUploadComponent from './fileUpload.vue';
const fileUpload={
        install:function (Vue){
            Vue.component('fileUpload',fileUploadComponent)
        }
}
export default fileUpload