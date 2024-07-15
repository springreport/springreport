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
import Echart from "@/components/echart/index.vue";
import EchartMap from "@/components/echart/map.vue";
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
