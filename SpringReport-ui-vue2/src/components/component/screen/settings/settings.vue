<template>
  <div>
    <panel-component v-if="component.category == screenConstants.category.panel" :component="component" />
    <chart-setting-component v-if="component.category == screenConstants.category.vchart" :component="component" :charts-components="chartsComponents" />
    <text-setting-component v-if="component.category == screenConstants.category.text" :component="component" :charts-components="chartsComponents" />
    <picture-setting-component v-if="component.category == screenConstants.category.picture" :component="component" :charts-components="chartsComponents" />
    <border-setting-component v-if="component.category == screenConstants.category.border" :component="component" :charts-components="chartsComponents" />
    <decoration-setting-component v-if="component.category == screenConstants.category.decoration" :component="component" :charts-components="chartsComponents" />
    <table-setting-component v-if="component.category == screenConstants.category.table" :component="component" :charts-components="chartsComponents" />
    <number-flipper-setting-component v-if="component.category == screenConstants.category.numberFlipper" :component="component" :charts-components="chartsComponents"></number-flipper-setting-component>
    <card-list-setting-component  v-if="component.category == screenConstants.category.cardList" :component="component" :charts-components="chartsComponents"></card-list-setting-component>
  </div>
</template>
<script>
import panelComponent from '../panel/panelComponent.vue'
import chartSettingComponent from '../chartSetting/chartSettingComponent.vue'
import textSettingComponent from '../textSetting/textSettingComponent.vue'
import pictureSettingComponent from '../pictureSetting/pictureSettingComponent.vue'
import borderSettingComponent from '../borderSetting/borderSettingComponent.vue'
import decorationSettingComponent from '../decorationSetting/decorationSettingComponent.vue'
import tableSettingComponent from '../tableSetting/tableSettingComponent.vue'
import numberFlipperSettingComponent from '../numberFlipper/numberFlipperSettingComponent.vue'
import cardListSettingComponent from '../cardList/cardListSettingComponent.vue'
export default {
  name: 'Settings',
  components: {
    panelComponent,
    chartSettingComponent,
    textSettingComponent,
    pictureSettingComponent,
    borderSettingComponent,
    decorationSettingComponent,
    tableSettingComponent,
    numberFlipperSettingComponent,
    cardListSettingComponent
  },
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    },
    timerMap: {
      type: Object,
      default: () => ({})
    }
  }
}
</script>

<style scoped>
.el-form-item{
  margin-bottom:16px !important
}
.el-color-picker{
  margin-top:-10px !important
}
.el-form-item__error{
  padding-top:1px !important
}
.basicbox{
  padding:20px 16px;
}
.tabclass>>>.el-tabs__header {
    background-color:#ffffff !important;
    padding: 0 10px;
    margin-bottom: 0;
  }
.tabclass>>>.el-tabs__header .el-tabs__item.is-active {
    color: black;
}
</style>
