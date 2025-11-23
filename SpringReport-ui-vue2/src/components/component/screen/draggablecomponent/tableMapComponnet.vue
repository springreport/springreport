<template>
<div
  :style="{ height: '100%', width: '100%' }"
>
 <div :style="{float:'left',height: '100%', width: component.chartWidth+'px'}" :id="component.id"></div>
 <div :style="{float:'left',height: '100%', width: (component.w-component.chartWidth)+'px'}">
    <div  >
            <div :style="{height:(component.h-30)+'px',width:'100%'}">
            <el-table size="mini" :style="{background:component.rowStyle.backgroundColor}" :height="(component.h-30) + 'px'" :highlight-current-row="component.highlight" :border="component.border" :stripe="component.stripe" 
            :data="component.tableDatas.slice((component.pagination.currentPage-1)*component.pagination.pageSize,component.pagination.currentPage*component.pagination.pageSize)"
            :header-cell-style="{fontSize: component.headStyle.fontSize+'px', backgroundColor: component.headStyle.backgroundColor,color:component.headStyle.color,fontWeight:component.headStyle.fontWeight}"
            :header-row-style="{backgroundColor: component.headStyle.backgroundColor}"
            :row-style="{fontSize: component.rowStyle.fontSize+'px', backgroundColor: component.rowStyle.backgroundColor,color:component.rowStyle.color,fontWeight:component.rowStyle.fontWeight}">
                <el-table-column v-if="component.isIndex" type="index" label="序号" align="center" :width="component.indexWidth"></el-table-column>
                <el-table-column
                  v-for="(rowHeader,index) in component.tableColumn"
                  :key="index"
                  :prop="rowHeader.key"
                  :label="rowHeader.name"
                  :align="component.align"
                  :show-overflow-tooltip="component.overflow"
                  :highlight-current-row="component.highlight" 
                  :highlight-selection-row="component.highlight"
                >
                 <template slot-scope="scope">
                    <div  :style="{color:getCellColor(scope.row,rowHeader)}">
                      <a :href="getUrl(scope.row,rowHeader)" v-if="!rowHeader.isProgress" :style="{color:'inherit',textDecoration:(rowHeader.isHyperLink && rowHeader.hyperLink)?'underline':'none'}" target="_blank">{{scope.row[rowHeader.key]}}</a>
                      <el-progress :percentage="scope.row[rowHeader.key]*1"  :stroke-width="rowHeader.progressWidth" v-if="rowHeader.isProgress" :color="customColor(scope.row,rowHeader)" :text-color="rowHeader.textColor"></el-progress>
                    </div>
                </template>    
                </el-table-column>
            </el-table>
            </div>
            <div :style="{height:'30px',textAlign:'right',background:component.pagination.backgroundColor}">
            <el-pagination
              small
              :page-size="component.pagination.pageSize"
              layout="prev, pager, next"
              :total="component.tableDatas.length"
              style="color: white;"
              background
              :current-page.sync="component.pagination.currentPage"
              >
            </el-pagination>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import VChart from '@visactor/vchart'
export default {
    name:"vchartComponent",
    components:{
    },
    data() {
      return {
        tableTimer:{}
      }
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
            let mapCode = this.component.spec.map
            if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
            }
            if (this.component.dataSource == "2") {
                this.getData(this.component,searchParams);
                if(this.component.refresh){
                    var self = this;
                      setInterval(() => {
                          setTimeout(function(){self.getData(self.component,searchParams)}, 0)
                    }, this.component.refreshTime)
                }
            }else{
              this.autoPage(this.component);
            }
        }else{
              this.autoPage(this.component);
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
        let mapCode = component.spec.map;
        componentParams.mapCode = mapCode;
        params.params = Object.assign({}, componentParams, pageParams);
        let obj = {
          url: this.apis.screenDesign.getDynamicDatasApi,
          params: params,
          removeEmpty: false,
        };
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == "200") {
            component.tableDatas = response.responseData;
            component.spec.data.values = response.responseData;
            this.commonUtil.reLoadChart(this.chartsComponents, component,this.sendRequest,this.viewThat);
            this.autoPage(this.component);
          }
        });
      },
      getCellColor(d,column){
        let color = "";
        if(column.conditions && column.conditions.length > 0){
          for (let index = 0; index < column.conditions.length; index++) {
            const element = column.conditions[index];
            color = this.getCellColorByCondition(element,d);
            if(color){
              break;
            }
          }
        }
        return color;
      },
      getUrl(d,column){
        if(column.isHyperLink && column.hyperLink){
          let params = this.commonUtil.getTableDrillParams(d,column)
          let url = this.commonUtil.buildUrlWithParams(column.hyperLink,params);
          // window.open(url,'_blank')
          return url
        }
      },
      autoPage(component){
        this.component.pagination.currentPage = 1;
        let timer = this.tableTimer[component.id];
        if(component.autoPage){
          if(timer == null){
            timer = setInterval(function(){
              if((component.pagination.currentPage*component.pagination.pageSize)>=component.tableDatas.length) {
                component.pagination.currentPage = 1;
              }else{
                component.pagination.currentPage = component.pagination.currentPage + 1
              }
            },component.autoPageInterval?component.autoPageInterval*1000:5000);
            this.tableTimer[component.id] = timer
          }
        }else{
          clearInterval(timer);     
          this.tableTimer[component.id] = null;
        }
      },
    }
}
</script>
<style scoped>
 .left {
        float: left;
        width: 200px;
        height: 100%;
        background-color: royalblue;
    }
    .right {
        float: left;
 		width: calc(100% - 200px);
        height: 100%;
        background-color: skyblue;
    }
</style>