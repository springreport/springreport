<template>
  <div>
    <site :component="component" :charts-components="chartsComponents" />
    <border-setting :component="component" :charts-components="chartsComponents" />
  </div>
</template>

<script>
import site from '../settingComponent/site/site.vue'
import borderSetting from '../settingComponent/border/borderSetting.vue'
export default {
  name: 'BorderSettingComponent',
  components: {
    site,
    borderSetting
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
