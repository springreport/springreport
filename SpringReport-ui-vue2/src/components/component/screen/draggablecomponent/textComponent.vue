<template>
<div
  :style="{ height: '100%', width: '100%' }"
>
 <component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :backgroundColor="component.borderBackgroundColor" :color="component.borderColor" :reverse="component.borderType.indexOf('-reverse')>-1">
  <div
    :id="component.id"
    :style="{
      height: component.h + 'px',
      width: component.w + 'px',
      display: component.style.display,
      textAlign: component.style.textAlign,
      color: component.style.color,
      verticalAlign: component.style.verticalAlign,
      fontSize: component.style.fontSize + 'pt',
      letterSpacing: component.style.letterSpacing + 'px',
      background: `linear-gradient(to ${
        component.style.backgroundDirection
          ? component.style.backgroundDirection
          : 'bottom'
      },${component.style.background},${
        component.style.backgroundEnd
          ? component.style.backgroundEnd
          : component.style.background
      })`,
      fontWeight: component.style.fontWeight ? 'bold' : 'normal',
    }"
  >
    <span
      v-if="component.textType == 'text' || component.type == 'date'"
      class="text"
      :style="{
        height: component.h + 'px',
        width: component.w + 'px',
        'background-image': `linear-gradient(to ${
          component.style.direction ? component.style.direction : 'bottom'
        }, ${component.style.color}, ${
          component.style.colorEnd
            ? component.style.colorEnd
            : component.style.color
        })`,
      }"
      v-html="component.content"></span>
    <MarqueeTips
      v-if="component.textType == 'marquee'"
      class="text"
      :content="component.content"
      :speed="component.speed"
      :style="{
        height: component.h + 'px',
        width: component.w + 'px',
        lineHeight: component.h + 'px',
        'background-image': `linear-gradient(to ${
          component.style.direction ? component.style.direction : 'bottom'
        }, ${component.style.color}, ${
          component.style.colorEnd
            ? component.style.colorEnd
            : component.style.color
        })`,
      }"
    >
    </MarqueeTips>
    <span
      ><a
        v-if="component.textType == 'link'"
        class="text"
        :style="{
          fontSize: component.style.fontSize + 'pt',
          'background-image': `linear-gradient(to ${
            component.style.direction ? component.style.direction : 'bottom'
          }, ${component.style.color}, ${
            component.style.colorEnd
              ? component.style.colorEnd
              : component.style.color
          })`,
          letterSpacing: component.style.letterSpacing + 'px',
          fontWeight: component.style.fontWeight ? 'bold' : 'normal',
        }"
        :href="component.href"
        target="_blank"
        >{{ component.content }}</a
      ></span
    >
  </div>
 </component>
  <div
    v-else
    :id="component.id"
    :style="{
      height: component.h + 'px',
      width: component.w + 'px',
      display: component.style.display,
      textAlign: component.style.textAlign,
      color: component.style.color,
      verticalAlign: component.style.verticalAlign,
      fontSize: component.style.fontSize + 'pt',
      letterSpacing: component.style.letterSpacing + 'px',
      background: `linear-gradient(to ${
        component.style.backgroundDirection
          ? component.style.backgroundDirection
          : 'bottom'
      },${component.style.background},${
        component.style.backgroundEnd
          ? component.style.backgroundEnd
          : component.style.background
      })`,
      fontWeight: component.style.fontWeight ? 'bold' : 'normal',
    }"
  >
    <span
      v-if="component.textType == 'text' || component.type == 'date'"
      class="text"
      :style="{
        'background-image': `linear-gradient(to ${
          component.style.direction ? component.style.direction : 'bottom'
        }, ${component.style.color}, ${
          component.style.colorEnd
            ? component.style.colorEnd
            : component.style.color
        })`,
      }"
      v-html="component.content" ></span
    >
    <MarqueeTips
      v-if="component.textType == 'marquee'"
      class="text"
      :content="component.content"
      :speed="component.speed"
      :style="{
        height: component.h + 'px',
        width: component.w + 'px',
        lineHeight: component.h + 'px',
        'background-image': `linear-gradient(to ${
          component.style.direction ? component.style.direction : 'bottom'
        }, ${component.style.color}, ${
          component.style.colorEnd
            ? component.style.colorEnd
            : component.style.color
        })`,
      }"
    >
    </MarqueeTips>
    <span
      ><a
        v-if="component.textType == 'link'"
        class="text"
        :style="{
          fontSize: component.style.fontSize + 'pt',
          'background-image': `linear-gradient(to ${
            component.style.direction ? component.style.direction : 'bottom'
          }, ${component.style.color}, ${
            component.style.colorEnd
              ? component.style.colorEnd
              : component.style.color
          })`,
          letterSpacing: component.style.letterSpacing + 'px',
          fontWeight: component.style.fontWeight ? 'bold' : 'normal',
        }"
        :href="component.href"
        target="_blank"
        >{{ component.content }}</a
      ></span
    >
  </div>
 </div>
</template>

<script>
import MarqueeTips from "vue-marquee-tips";
export default {
  name: "textComponent",
  components: {
    MarqueeTips,
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
    sendRequest: {
      //是否需要动态获取数据，//预览和设计的时候不需要动态获取数据，真正查看的时候才需要
      type: Boolean,
      default: false,
    },
    searchParams: {
      type: Array,
      default: () => []
    },
  },
  mounted() {
    this.initData(this.searchParams);
  },
  methods: {
    //数据初始化
    initData(searchParams) {
      if (this.sendRequest) {
        if (this.component.dataSource == "2") {
            this.getData(this.component,searchParams);
            if(this.component.refresh){
               var self = this;
                setInterval(() => {
                    setTimeout(function(){self.getData(self.component,searchParams)}, 0)
                }, this.component.refreshTime)
            }
        }
      }
    },
    getData(component,searchParams) {
      let pageParams = this.commonUtil.searchParamMap(searchParams)
      var params = {
        dataSetId: component.dynamicDataSettings.datasetId,
        dataColumns: component.dynamicDataSettings.dataColumns,
        sqlType: "1",
      };
      var componentParams = this.commonUtil.getComponentParams(
        component.params
      );
      params.params = Object.assign({}, componentParams, pageParams);
      let obj = {
        url: this.apis.screenDesign.getDynamicDatasApi,
        params: params,
        removeEmpty: false,
      };
      var that = this;
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          component.spec.data.values = response.responseData;
          if(component.spec.valueField && component.spec.data.values && component.spec.data.values.length > 0){
              component.content = component.spec.data.values[0][component.spec.valueField];
              that.commonUtil.reLoadChart(that.chartsComponents,component);
          }
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
::v-deep .text {
  // text-align: center;
  -webkit-background-clip: text;
  color: transparent;
  white-space: pre-wrap;
}
</style>