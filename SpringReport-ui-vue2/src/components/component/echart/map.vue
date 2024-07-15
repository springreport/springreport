<template>
  <div
    :id="id"
    :class="className"
    :style="{ height: height, width: width }"
    style="flex: 1"
  />
</template>

<script>

var echarts = require("echarts");

export default {
  name: "EchartMap",
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
      country: null, // 当前地区的json
      countryEng: null, // 当前地区的英文名
      option: {
        tooltip: {
          show: false,
        },
        visualMap: {
          show: false,
        }, // backgroundColor: '#013954',
        series: [
          {
            type: "map",
            roam: false,
            layoutSize: "100%",
            label: {
              normal: {
                show: true,
                textStyle: {
                  color: "#fff",
                },
              },
              emphasis: {
                show: false,
                textStyle: {
                  color: "#fff",
                },
              },
            },

            itemStyle: {
              normal: {
                areaColor: "#394ee9",
                borderColor: "#909cfa",
                borderWidth: 2,
                borderType: "solid",
              },
              emphasis: {
                show: false,
                areaColor: "#394ee9",
                borderWidth: 0,
                color: "green",
              },
            },
            emphasis: {
              show: false,
            },
            zoom: 1.2,
            roam: false,
            map: "1", //使用
          },
        ],
        geo: {
          map: "china", // 引入地图
          layoutSize: "100%",
          zoom: 1.2, // 缩放级别
          roam: false, // 开启鼠标缩放和漫
          label: {
            normal: {
              // 静态的时候展示样式
              show: false, // 是否显示地图省份得名称
            },
            emphasis: {
              show: false,
              color: "#fff",
            },
          }, // 区域颜色
          itemStyle: {
            shadowColor: "#909cfa",
            shadowOffsetY: 20,
            shadowOffsetX: 10,
          }, // 鼠标聚焦时的区域颜色
          emphasis: {
            show: false,
          },
        },
      },
    };
  },
  watch: {
    options: {},
  },
  async created() {
    await this.getMapJson();
    this.init();
  },

  mounted() {},
  beforeDestroy() {},
  methods: {
    // 获取地图信息
    async getMapJson() {
      var obj = {
        url: `geoJson/${this.options.map.mapType}.json`,
      };
      const data = await this.commonUtil.doGet(obj);
      this.country = data;
      this.countryEng = this.options.map.mapType;
    },
    init() {
      const that = this; // 初始化echarts地图
      const myChart = echarts.init(this.$el);
      echarts.registerMap(that.countryEng, that.country, {});
      myChart.setOption(this.options);
    },
  },
};
</script>

<style></style>
