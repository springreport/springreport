<template>
      <div
        :style="{ height: component.h+'px', width: component.w+'px' }"
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
        <component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :color="component.borderColor" :backgroundColor="component.borderBackgroundColor" :reverse="component.borderType.indexOf('-reverse')>-1">
          <div
            style="height:100%;width:100%;display: flex;justify-content: center;align-items: center;"
          >
          <div
            :id="component.id"
            :style="{
              height:(component.h-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0))+'px',
              width: (component.w-(component.borderLeft?component.borderLeft:0)-(component.borderRight?component.borderRight:0))+'px',
              marginTop:(component.borderTop?component.borderTop:0)+'px',
              marginBottom:(component.borderBottom?component.borderBottom:0)+'px',
              marginLeft:(component.borderLeft?component.borderLeft:0)+'px',
              marginRight:(component.borderRight?component.borderRight:0)+'px',
            }"
          ></div>
          </div>
        </component>
        <div v-else
          :id="component.id"
          :style="{
            height:component.h+'px',
            width: component.w+'px',
          }"
        ></div>
    </div>
</template>

<script>
import VChart from '@visactor/vchart'
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
        },
        viewThat: { // 查看页面的this对象
          type: Object,
          default: () => ({}),
        },
        searchParams: {
          type: Array,
          default: () => []
        },
    },
    mounted() {
      this.initData(this.searchParams);
    },
    methods:{
      //数据初始化
      async initData(searchParams) {
        if (this.sendRequest) {
          if(this.component.type == "comboCharthl" || this.component.type == "comboChartdbbar"){
              if(this.component.dataSource == "2"){
                this.getComboCharthlData(this.component,"1",searchParams);
                if(this.component.refresh){
                  var self = this;
                    setInterval(() => {
                        setTimeout(function(){self.getComboCharthlData(self.component,"1",searchParams)}, 0)
                    }, this.component.refreshTime)
                }
              }
              if(this.component.lineDataSource == "2"){
                this.getComboCharthlData(this.component,"2",searchParams);
                if(this.component.refresh){
                  var self = this;
                    setInterval(() => {
                        setTimeout(function(){self.getComboCharthlData(self.component,"2",searchParams)}, 0)
                    }, this.component.refreshTime)
                }
              }
          }else{
            if (this.component.type.toLowerCase().indexOf('scattermap') >= 0) {
              let mapCode = this.component.spec.series[0].map
                if (!VChart.getMap(mapCode)) {
                    const geojson = await this.commonUtil.getMapData(mapCode)
                    VChart.registerMap(mapCode, geojson)
                }
              }
              else if (this.component.type.toLowerCase().indexOf('basicmap') >= 0) {
                let mapCode = this.component.spec.map
                if (!VChart.getMap(mapCode)) {
                  const geojson = await this.commonUtil.getMapData(mapCode)
                  VChart.registerMap(mapCode, geojson)
                }
              }
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
        if (component.type.toLowerCase().indexOf('basicmap') >= 0) {
          //地图组件，将地区编码添加到参数中，固定使用mapCode属性
          let mapCode = component.spec.map;
          componentParams.mapCode = mapCode;
        }else if(component.type.toLowerCase().indexOf('scattermap') >= 0){
          let mapCode = component.spec.series[0].map;
          componentParams.mapCode = mapCode;
        }
        params.params = Object.assign({}, componentParams, pageParams);
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
            this.commonUtil.reLoadChart(this.chartsComponents, component,this.sendRequest,this.viewThat);
          }
        });
      },

      getComboCharthlData(component,type,searchParams) {
        let pageParams = this.commonUtil.searchParamMap(searchParams)
        let datasetId =  component.dynamicDataSettings.datasetId;
        let dataColumns = component.dynamicDataSettings.dataColumns;
        if(type == "2"){
          datasetId =  component.lineDynamicDataSettings.datasetId;
          dataColumns = component.lineDynamicDataSettings.dataColumns;
        }
        var params = {
          dataSetId: datasetId,
          dataColumns: dataColumns,
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
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == "200") {
            if(type == "2"){
              component.spec.data[1].values = response.responseData;
            }else{
              component.spec.data[0].values = response.responseData;
            }
            
            this.commonUtil.reLoadChart(this.chartsComponents, component);
          }
        });
      },
    }
}
</script>
