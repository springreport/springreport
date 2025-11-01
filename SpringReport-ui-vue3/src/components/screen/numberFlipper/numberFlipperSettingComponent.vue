<template>
  <div>
    <div class="btn-group df-c">
      <div
        class="btn"
        :class="{ 'btn-active': currentIndex === 0 }"
        @click="currentIndex = 0"
      >
        基础
      </div>

      <div
        v-if="component.type != 'date'"
        class="btn"
        :class="{ 'btn-active': currentIndex === 1 }"
        @click="currentIndex = 1"
      >
        参数
      </div>

      <div
        v-if="component.type != 'date'"
        class="btn"
        :class="{ 'btn-active': currentIndex === 2 }"
        @click="currentIndex = 2"
      >
        数据
      </div>

      <div
        class="btn"
        :class="{ 'btn-active': currentIndex === 3 }"
        @click="currentIndex = 3"
      >
       {{ '翻牌器' }}
      </div>
    </div>

    <site v-if="currentIndex==0" :component="component" :charts-components="chartsComponents" />
    <param-setting
      v-if="currentIndex==1"
      :component="component"
      :charts-components="chartsComponents"
    />

    <data-setting
      v-if="currentIndex==2"
      :component="component"
      :charts-components="chartsComponents"
      :components="components"
    />

    <numberFlipperSetting
      v-if="currentIndex==3"
      :component="component"
      :charts-components="chartsComponents"
    />

  </div>
</template>

<script>
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/theme/eclipse.css'
import 'codemirror/theme/erlang-dark.css'

import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/fold/foldgutter.js'
import 'codemirror/addon/fold/xml-fold.js'
import 'codemirror/addon/fold/indent-fold.js'
import 'codemirror/addon/fold/brace-fold'
import 'codemirror/addon/fold/markdown-fold.js'
import 'codemirror/addon/fold/comment-fold.js'
import 'codemirror/addon/selection/active-line'
import site from '../settingComponent/site/site.vue'
import dataSetting from '../settingComponent/data/dataSetting.vue'
import chartSetting from '../settingComponent/chart/chartSetting.vue'
import paramSetting from '../settingComponent/param/paramSetting.vue'
import numberFlipperSetting from '../settingComponent/numberFlipper/numberFlipperSetting.vue'
export default {
  name: 'TextSettingComponent',
  components: {
    site,
    dataSetting,
    chartSetting,
    paramSetting,
    numberFlipperSetting
  },
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    components: {
      type: Array,
      default: () => []
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

<style scoped>
::v-deep .el-collapse-item__header {
  border-top: 0px !important;
}
</style>
