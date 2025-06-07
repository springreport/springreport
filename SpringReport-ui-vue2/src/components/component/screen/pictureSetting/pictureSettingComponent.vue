<template>
  <div>
    <site :component="component" :charts-components="chartsComponents" />
    <picture-setting :component="component" :charts-components="chartsComponents" />
  </div>
</template>

<script>
import site from '../settingComponent/site/site.vue'
import pictureSetting from '../settingComponent/picture/pictureSetting.vue'
export default {
  name: 'PictureSettingComponent',
  components: {
    site,
    pictureSetting
  },
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    }
  }
}
</script>

<style  scoped>
 ::v-deep .el-collapse-item__header{
    border-top: 0px !important;
 }

</style>
