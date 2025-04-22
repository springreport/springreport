<template>
<div
  :style="{ height: '100%', width: '100%' }"
>
<component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :color="component.borderColor" :reverse="component.borderType.indexOf('-reverse')>-1">
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
        'background-image': `linear-gradient(to ${
          component.style.direction ? component.style.direction : 'bottom'
        }, ${component.style.color}, ${
          component.style.colorEnd
            ? component.style.colorEnd
            : component.style.color
        })`,
      }"
      >{{ component.content }}</span>
    <Vue3Marquee v-if="component.textType=='marquee'"  :duration="component.speed" :style="{height:component.h+'px',width:component.w+'px',lineHeight:component.h+'px'}">
      <span class="text" :style="{'background-image':`linear-gradient(to ${component.style.direction?component.style.direction:'bottom'}, ${component.style.color}, ${component.style.colorEnd?component.style.colorEnd:component.style.color})`}">{{component.content}}</span>
    </Vue3Marquee>
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
    :id="component.id"
    v-else
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
      >{{ component.content }}</span>
    <Vue3Marquee v-if="component.textType=='marquee'"  :duration="component.speed" :style="{height:component.h+'px',width:component.w+'px',lineHeight:component.h+'px'}">
      <span class="text" :style="{'background-image':`linear-gradient(to ${component.style.direction?component.style.direction:'bottom'}, ${component.style.color}, ${component.style.colorEnd?component.style.colorEnd:component.style.color})`}">{{component.content}}</span>
    </Vue3Marquee>
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
import { Vue3Marquee  } from 'vue3-marquee'
// import 'vue3-marquee/dist/style.css'
export default {
  name: "textComponent",
  components: {
    Vue3Marquee,
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
  },
  mounted() {
    this.initData();
  },
  methods: {
    //数据初始化
    initData() {
      if(this.component.type == "date"){
        const self = this
        setInterval(() => {
            setTimeout(function() { self.refreshTime(self.component) }, 0)
        }, 1000)
      }else{
        if (this.sendRequest) {
          if (this.component.dataSource == "2") {
              this.getData(this.component);
              if(this.component.refresh){
                var self = this;
                  setInterval(() => {
                      setTimeout(function(){self.getData(self.component)}, 0)
                  }, this.component.refreshTime)
              }
          }
        }
      }
      
    },
    getData(component) {
      var params = {
        dataSetId: component.dynamicDataSettings.datasetId,
        dataColumns: component.dynamicDataSettings.dataColumns,
        sqlType: "1",
      };
      var componentParams = this.commonUtil.getComponentParams(
        component.params
      );
      params.params = Object.assign({}, componentParams, {});
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
    refreshTime(component) {
       component.content = this.commonUtil.getCurrentDate(component)
    },
  },
};
</script>

<style scoped lang="scss">
.text {
  // text-align: center;
  -webkit-background-clip: text;
  // color: transparent;
  display:inline;
}
</style>