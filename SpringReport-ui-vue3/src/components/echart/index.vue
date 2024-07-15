<template>
  <div
    :id="id"
    :class="className"
    :style="{ height: height, width: width }"
    style="flex: 1"
  />
</template>

<script>
// var echarts = require("echarts");

export default {
  name: "Echart",
  props: {
    className: {
      type: String,
      default: "chart",
    },
    id: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "100%",
    },
    options: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      chart: null,
      tootipTimer: null,
    };
  },
  watch: {
    options: {
      handler(options) {
        //  this.chart.clear()
        // 设置true清空echart缓存
        this.chart.setOption(options, true);
      },
      deep: true,
    },
  },
  created() {},
  activated() {
    // 防止 keep-alive 之后图表变形
    if (this.chart) {
      this.chart.resize();
    }
  },

  mounted() {
    this.initChart();
    window.addEventListener("resize", () => {
      this.chart.resize();
    });
  },
  beforeDestroy() {
    clearInterval(this.tootipTimer);
    this.chart.clear();
  },
  methods: {
    initChart() {
      // 初始化echart
      const that = this;
      this.chart = this.$echarts.init(this.$el); // 'tdTheme'
      this.chart.clear();

      this.chart.setOption(this.options, true);
    },
  },
};
</script>

<style></style>
