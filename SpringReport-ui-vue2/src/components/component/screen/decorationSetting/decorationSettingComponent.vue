<template>
  <div>
    <site :component="component" :charts-components="chartsComponents" />
    <decoration-setting :component="component" :charts-components="chartsComponents" />
  </div>
</template>

<script>
import site from '../settingComponent/site/site.vue'
import decorationSetting from '../settingComponent/decoration/decorationSetting.vue'
export default {
  name: 'DecorationSettingComponent',
  components: {
    site,
    decorationSetting
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
