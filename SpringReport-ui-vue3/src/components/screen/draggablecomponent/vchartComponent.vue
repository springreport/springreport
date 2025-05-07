<template>
      <div
        :style="{ height: '100%', width: '100%' }"
      >
        <!-- <div
          :style="{ height: '28px', width: '100%' }"
          v-if="component.params && component.params.length > 0 && component.params.length>component.hiddenParamSize"
        >
        <componentForm :params="component.params"></componentForm>
        </div>
        <div
          :id="component.id"
          :style="{
            height:
              component.params && component.params.length > 0 && component.params.length>component.hiddenParamSize
                ? component.h - 30 + 'px'
                : '100%',
            width: '100%',
          }"
        ></div> -->
        <component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :color="component.borderColor" :reverse="component.borderType.indexOf('-reverse')>-1">
          <div
            style="height:100%;width:100%;display: flex;justify-content: center;align-items: center;"
          >
          <div
            :id="component.id"
            :style="{
              height:'92%',
              width: '92%',
            }"
          ></div>
          </div>
        </component>
        <div v-else
          :id="component.id"
          :style="{
            height:'100%',
            width: '100%',
          }"
        ></div>
    </div>
</template>

<script>
export default {
    name:"vchartComponent",
    components:{
    },
    props:{
        component:{
            type:Object,
            default:()=>({})
        },
        chartsComponents: {
          type: Object,
          default: () => ({}),
        },
        sendRequest:{//是否需要动态获取数据，//预览和设计的时候不需要动态获取数据，真正查看的时候才需要
            type:Boolean,
            default:false
        }
    },
    mounted() {
      this.initData();
    },
    methods:{
      //数据初始化
      initData() {
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
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == "200") {
            component.spec.data.values = response.responseData;
            if(component.type == "sankey"){
              component.spec.data.values = [{nodes:[],links:[]}];
              this.commonUtil.processSankeyData(component,response.responseData);
            }
            this.commonUtil.reLoadChart(this.chartsComponents, component);
          }
        });
      },
    }
}
</script>
