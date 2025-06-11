<template>
  <div>
    <div class="btn-group df-c">
      <div
        v-for="(item, index) in ['基础', '边框']"
        :key="index"
        class="btn"
        :class="{ 'btn-active': currentIndex === index }"
        @click="currentIndex = index"
      >
        {{ item }}
      </div>
    </div>
    <site v-if="currentIndex==0" :component="component" :charts-components="chartsComponents" />
    <decoration-setting v-else :component="component" :charts-components="chartsComponents" />
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
  },
  data() {
    return {
      currentIndex: 0
    }
  }
}
</script>

<style  scoped>
 ::v-deep .el-collapse-item__header{
    border-top: 0px !important;
 }

</style>
