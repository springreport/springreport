<template>
  <div>
    <el-tabs value="basic"  type="card">
      <el-tab-pane label="基础" name="basic">
        <site :component="component" :charts-components="chartsComponents" />
      </el-tab-pane>
      <el-tab-pane label="边框" name="param">
         <border-setting :component="component" :charts-components="chartsComponents" />
      </el-tab-pane>
    </el-tabs>
   
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
