<template>
  <div>
    <site :component="component" :chartsComponents="chartsComponents"></site>
    <decoration-setting :component="component" :chartsComponents="chartsComponents"></decoration-setting>
  </div>
</template>

<script>
import site from '../settingComponent/site/site.vue'
import decorationSetting from '../settingComponent/decoration/decorationSetting.vue'
export default {
    name:"decorationSettingComponent",
    components:{
        site,
        decorationSetting
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
 ::v-deep .el-collapse-item__header{
    border-top: 0px !important;
 }

</style>