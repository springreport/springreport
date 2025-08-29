<template>
<div
  :style="{ height: '100%', width: '100%' }"
>
 <component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :backgroundColor="component.borderBackgroundColor" :color="component.borderColor" :reverse="component.borderType.indexOf('-reverse')>-1">
  <div
             style="height:100%;width:100%;display: flex;justify-content: center;align-items: center;"
          >
  <div class="test-fan" :style="{
        height: (component.h-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0)) + 'px',
        width: (component.w-(component.borderLeft?component.borderLeft:0)-(component.borderRight?component.borderRight:0)) + 'px',
        overflow:'auto',
        marginTop:(component.borderTop?component.borderTop:0)+'px',
        marginBottom:(component.borderBottom?component.borderBottom:0)+'px',
        marginLeft:(component.borderLeft?component.borderLeft:0)+'px',
        marginRight:(component.borderRight?component.borderRight:0)+'px',
    }">
    <card-list :component="component"></card-list>
  </div>
  </div>
 </component>
  <div
    v-else
    :id="component.id"
  >
  <div class="test-fan" :style="{
        height: component.h + 'px',
        width: component.w + 'px',
        overflow:'auto',
    }">
    <card-list :component="component"></card-list>
  </div>
  </div>
 </div>
</template>

<script>
import cardList from "./cardList.vue";
export default {
  name: "cardListComponent",
  components: {
    cardList
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
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">

/* 对于 WebKit 浏览器（如 Chrome, Safari） */
.test-fan::-webkit-scrollbar {
    display: none;
}
 
/* 对于 IE 和 Edge */
.test-fan {
    -ms-overflow-style: none; /* IE 和 Edge */
}
 
/* 对于 Firefox */
.test-fan {
    scrollbar-width: none; /* Firefox */
}
</style>