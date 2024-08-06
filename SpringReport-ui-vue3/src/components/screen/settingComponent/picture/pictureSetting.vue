<!-- 数据设置组件 -->
<template>
    <div>
        <el-collapse>
                  <el-collapse-item title="图片设置">
                    <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left" size="small" ref="settingForm">
                        <el-form-item label="图片路径" prop="height">
                            <el-input v-model="component.imgUrl">
                                <template #append><el-button icon="icon-upload" @click="uploadTrigger"></el-button></template>
                            </el-input>
                            <input type="file" ref="fileBtn" @change="uploadFile" id="uploadFile" accept="image/*" style="display:none"/>
                        </el-form-item>
                    </el-form>
                  </el-collapse-item>
            </el-collapse>
    </div>
</template>

<script>
import Axios from 'axios';
export default {
    props:{
        component:{
            type:Object,
            default:()=>({})
        },
        chartsComponents:{
            type:Object,
            default:() => ({}),
        },
    },
    mounted() {
    },
    data(){
        return{
        }
    },
    methods:{
        uploadTrigger(){
            this.$refs.fileBtn.dispatchEvent(new MouseEvent("click"));
        },
        uploadFile(event){
            let file = event.target.files[0]; //获取input的图片file值
            let param = new FormData()  // 创建form对象
            param.append('file', file)  // 通过append向form对象添加数据
            let config = {
                headers: {'Content-Type': 'multipart/form-data',
                'Authorization':localStorage.getItem(this.commonConstants.sessionItem.authorization)}
            }
            Axios.post(this.apis.screenDesign.uploadFileApi, param, config)
            .then(res => {
                this.component.imgUrl = res.data.responseData.fileUri;
                event.target.value = '';
            })
        },
    }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:2px !important
}
:deep(.el-form-item__label-wrap){
    margin-left:0px !important
}
:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  background-color: var(--colorWhite);
  color: var(--colorTextPrimary);
  border: 1px solid var(--borderColorBase);
}

:deep(.el-select-dropdown) {
   border: 1px solid var(--borderColorBase) !important;
   background-color: var(--colorWhite) !important;
 }

 :deep(.el-select__selected-item){
    color:var(--colorTextPrimary) !important;
  
}

:deep(.el-select--small .el-select__wrapper){
    background-color: var(--colorWhite) !important;
    box-shadow: 0 0 0 1px black inset;
}

:deep(.el-input--small .el-input__wrapper){
    /* padding: 0px 0px; */
    background-color: var(--colorWhite);
    box-shadow: 0 0 0 1px black inset;
}
</style>
