<template>
  <div>
    <div class="btn-group df-c">
      <div
        v-for="(item, index) in ['基础', '参数', '数据', '表格']"
        :key="index"
        class="btn"
        :class="{ 'btn-active': currentIndex === index }"
        @click="currentIndex = index"
      >
        {{ item }}
      </div>
    </div>
    <site
      v-if="currentIndex == 0"
      :component="component"
      :chartsComponents="chartsComponents"
    ></site>
    <param-setting
      v-else-if="currentIndex == 1"
      :component="component"
      :chartsComponents="chartsComponents"
    ></param-setting>
    <data-setting
      v-else-if="currentIndex == 2"
      :component="component"
      :chartsComponents="chartsComponents"
      :components="components"
    ></data-setting>
    <table-setting
      v-else-if="currentIndex == 3"
      :component="component"
      :chartsComponents="chartsComponents"
    ></table-setting>
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
  import paramSetting from '../settingComponent/param/paramSetting.vue';
  import tableSetting from '../settingComponent/table/tableSetting.vue';
  export default {
    name: 'tableSettingComponent',
    components: {
      site,
      dataSetting,
      paramSetting,
      tableSetting,
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
