<template>
  <div>
    <site :component="component" :chartsComponents="chartsComponents"></site>
    <picture-setting :component="component" :chartsComponents="chartsComponents"></picture-setting>
  </div>
</template>

<script>
import site from '../settingComponent/site/site.vue'
import pictureSetting from '../settingComponent/picture/pictureSetting.vue'
export default {
    name:"pictureSettingComponent",
    components:{
        site,
        pictureSetting
    },
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
}
</script>

<style  scoped>
 :deep(.el-collapse-item__header){
    border-top: 0px !important;
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