<template>
  <div class="report-chart">
    <div class="chart-item" v-for="(options, index) in info" :key="index" v-show="info && info.length>0">
      <EchartMap
        :id="`chartMap${index}`"
        :options="options"
        height="358px"
        v-if="options.map && options.map.max != null"
      />
      <Echart :id="`chart${index}`" :options="options" height="358px" v-else />
    </div>
    <div class="chart-item" v-show="!info || info.length == 0">
      <van-empty description="暂无数据" />
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import { Empty } from "vant";
Vue.use(Empty);
import Echart from "@/components/component/echart/index.vue";
import EchartMap from "@/components/component/echart/map.vue";
export default {
  name: "ReportChart",
  data() {
    return {};
  },
  props: {
    info: {
      type: Array,
      default: () => [],
    },
  },
  components: {
    Echart,
    EchartMap,
  },
  watch: {},
  mounted() {},
  methods: {},
};
</script>

<style scoped lang="scss"></style>
