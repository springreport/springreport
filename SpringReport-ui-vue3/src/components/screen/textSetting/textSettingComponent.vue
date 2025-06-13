<template>
  <div>
    <div class="btn-group df-c">
      <div class="btn" :class="{ 'btn-active': currentIndex === 0 }" @click="currentIndex = 0">
        基础
      </div>

      <div
        class="btn"
        :class="{ 'btn-active': currentIndex === 1 }"
        @click="currentIndex = 1"
        v-if="component.type != 'date'"
      >
        参数
      </div>

      <div
        class="btn"
        :class="{ 'btn-active': currentIndex === 2 }"
        @click="currentIndex = 2"
        v-if="component.type != 'date'"
      >
        数据
      </div>

      <div class="btn" :class="{ 'btn-active': currentIndex === 3 }" @click="currentIndex = 3">
        {{ component.type != 'date' ? '文本' : '日期' }}
      </div>
    </div>
    <site
      v-if="currentIndex == 0"
      :component="component"
      :chartsComponents="chartsComponents"
    ></site>
    <param-setting
      :component="component"
      :chartsComponents="chartsComponents"
      v-if="component.type != 'date' && currentIndex == 1"
    ></param-setting>
    <data-setting
      :component="component"
      :chartsComponents="chartsComponents"
      v-if="component.type != 'date' && currentIndex == 2"
    ></data-setting>
    <text-setting
      v-if="currentIndex == 3"
      :component="component"
      :chartsComponents="chartsComponents"
    ></text-setting>
  </div>
</template>

<script>
  import 'codemirror/mode/javascript/javascript.js';
  import 'codemirror/lib/codemirror.css';
  import 'codemirror/addon/hint/show-hint.css';
  import 'codemirror/theme/eclipse.css';
  import 'codemirror/theme/erlang-dark.css';

  import 'codemirror/addon/fold/foldgutter.css';
  import 'codemirror/addon/fold/foldcode.js';
  import 'codemirror/addon/fold/foldgutter.js';
  import 'codemirror/addon/fold/xml-fold.js';
  import 'codemirror/addon/fold/indent-fold.js';
  import 'codemirror/addon/fold/brace-fold';
  import 'codemirror/addon/fold/markdown-fold.js';
  import 'codemirror/addon/fold/comment-fold.js';
  import 'codemirror/addon/selection/active-line';
  import site from '../settingComponent/site/site.vue';
  import dataSetting from '../settingComponent/data/dataSetting.vue';
  import chartSetting from '../settingComponent/chart/chartSetting.vue';
  import paramSetting from '../settingComponent/param/paramSetting.vue';
  import textSetting from '../settingComponent/text/textSetting.vue';
  export default {
    name: 'textSettingComponent',
    components: {
      site,
      dataSetting,
      chartSetting,
      paramSetting,
      textSetting,
    },
    props: {
      component: {
        type: Object,
        default: () => ({}),
      },
      chartsComponents: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        currentIndex: 0,
      };
    },
  };
</script>

<style scoped>
  ::v-deep .el-collapse-item__header {
    border-top: 0px !important;
  }
</style>
