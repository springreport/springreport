<template>
  <div
        :style="{ height: '100%', width: '100%' }"
  >
  <component :is="component.borderType.replace('-reverse','')" v-if="component.isborder && component.borderType" :color="component.borderColor" :backgroundColor="component.borderBackgroundColor" :reverse="component.borderType.indexOf('-reverse')>-1">
    <div :id="component.id" :style="{height:component.h+'px',width:component.w+'px'}">
        <!-- <div
          :style="{ height: '28px', width: '100%' }"
          v-if="component.params && component.params.length > 0 && component.params.length>component.hiddenParamSize"
        >
        <componentForm :params="component.params"></componentForm>
        </div> -->
        <div
            style="height:100%;width:100%;display: flex;justify-content: center;align-items: center;"
          >
        <div :style="{ height:(component.h-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0))+'px',
              width: (component.w-(component.borderLeft?component.borderLeft:0)-(component.borderRight?component.borderRight:0))+'px',
              marginTop:(component.borderTop?component.borderTop:0)+'px',
              marginBottom:(component.borderBottom?component.borderBottom:0)+'px',
              marginLeft:(component.borderLeft?component.borderLeft:0)+'px',
              marginRight:(component.borderRight?component.borderRight:0)+'px',}" v-if="component.type == screenConstants.type.scrollTable">
                <!-- 表头 -->
                <div class="warp-title" :style="{height:component.headStyle.height + 'px',backgroundColor:component.headStyle.backgroundColor,color:component.headStyle.color,fontSize:component.headStyle.fontSize+'pt',fontWeight:component.headStyle.fontWeight,borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">
                <ul class="item">
                    <li :style="{height:component.headStyle.height + 'px', lineHeight:component.headStyle.height + 'px'}">
                        <span v-if="component.style.showIndex" class="title" :style="{width: component.style.indexWidth+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">序号</span> 
                        <span v-for="(column,i) in component.tableColumn" :key="i" class="title" :style="{width:column.style.width+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">{{column.name}}</span>
                    </li>
                </ul>
                </div>
                <!-- 表格滚动区 -->
                <div>
                    <vue-seamless-scroll :style="{height: (component.h-component.headStyle.height-5-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0)) + 'px',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}" :data="component.spec.data.values" :class-option="component.options" class="warp-content" :ref="component.id">
                        <li v-for="(d, t) in component.spec.data.values" :key="t" :style="{backgroundColor:((t+1)%2 == 0) ? component.bodyStyle.evenRowColor : component.bodyStyle.oddRowColor,height:component.bodyStyle.height + 'px', lineHeight:component.bodyStyle.height + 'px',color:component.bodyStyle.color,fontSize:component.bodyStyle.fontSize+'pt',fontWeight:component.bodyStyle.fontWeight,}">
                            <span v-if="component.style.showIndex" class="title" :style="{width: component.style.indexWidth+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">{{t+1}}</span> 
                            <div v-for="(column,i) in component.tableColumn" :key="i" class="title " :style="{width:column.style.width+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:'',color:getCellColor(d,column)}"> 
                              <a  v-if="!column.isProgress" :href="getUrl(d,column)" :style="{color:'inherit',textDecoration:(column.isHyperLink && column.hyperLink)?'underline':'none'}" target="_blank">{{d[column.key]}}</a>
                              <el-progress :style="{lineHeight:component.bodyStyle.height/20}"  :percentage="d[column.key]*1"  :stroke-width="column.progressWidth" v-if="column.isProgress" :color="customColor(d,column)" :text-color="column.textColor"></el-progress>
                            </div>
                        </li>
                    </vue-seamless-scroll>
                </div>
        </div>
        <div  v-if="component.type == screenConstants.type.pageTable" :style="{height:(component.h-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0))+'px',
        width:(component.w-(component.borderLeft?component.borderLeft:0)-(component.borderRight?component.borderRight:0))+'px',
        marginTop:(component.borderTop?component.borderTop:0)+'px',
        marginBottom:(component.borderBottom?component.borderBottom:0)+'px',
        marginLeft:(component.borderLeft?component.borderLeft:0)+'px',
        marginRight:(component.borderRight?component.borderRight:0)+'px'}">
            <div :style="{}">
            <el-table size="mini" :style="{background:component.rowStyle.backgroundColor}" :height="(component.h-24-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0)) + 'px'" :highlight-current-row="component.highlight" :border="component.border" :stripe="component.stripe" 
            :data="component.spec.data.values.slice((component.pagination.currentPage-1)*component.pagination.pageSize,component.pagination.currentPage*component.pagination.pageSize)"
            :header-cell-style="{fontSize: component.headStyle.fontSize+'px', backgroundColor: component.headStyle.backgroundColor,color:component.headStyle.color,fontWeight:component.headStyle.fontWeight}"
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
            <div :style="{height:'30px',width:'100%',textAlign:'right',background:component.pagination.backgroundColor}">
            <el-pagination
              small
              :page-size="component.pagination.pageSize"
              layout="prev, pager, next"
              :total="component.spec.data.total"
              style="color: white;"
              background
              :current-page.sync="component.pagination.currentPage"
              >
            </el-pagination>
            </div>
        </div>
        </div>
    </div>
  </component>
    <div  v-else :id="component.id" :style="{height:component.h+'px',width:component.w+'px'}">
        <!-- <div
          :style="{ height: '28px', width: '100%' }"
          v-if="component.params && component.params.length > 0 && component.params.length>component.hiddenParamSize"
        >
        <componentForm :params="component.params"></componentForm>
        </div> -->
        <div :style="{height:(component.h)+'px',width:'100%'}" v-if="component.type == screenConstants.type.scrollTable">
                <!-- 表头 -->
                <div class="warp-title" :style="{height:component.headStyle.height + 'px',backgroundColor:component.headStyle.backgroundColor,color:component.headStyle.color,fontSize:component.headStyle.fontSize+'pt',fontWeight:component.headStyle.fontWeight,borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">
                <ul class="item">
                    <li :style="{height:component.headStyle.height + 'px', lineHeight:component.headStyle.height + 'px'}">
                        <span v-if="component.style.showIndex" class="title" :style="{width: component.style.indexWidth+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">序号</span> 
                        <span v-for="(column,i) in component.tableColumn" :key="i" class="title" :style="{width:column.style.width+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">{{column.name}}</span>
                    </li>
                </ul>
                </div>
                <!-- 表格滚动区 -->
                <div>
                    <vue-seamless-scroll :style="{height: (component.h-component.headStyle.height-5) + 'px',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}" :data="component.spec.data.values" :class-option="component.options" class="warp-content" :ref="component.id">
                        <li v-for="(d, t) in component.spec.data.values" :key="t" :style="{backgroundColor:((t+1)%2 == 0) ? component.bodyStyle.evenRowColor : component.bodyStyle.oddRowColor,height:component.bodyStyle.height + 'px', lineHeight:component.bodyStyle.height + 'px',color:component.bodyStyle.color,fontSize:component.bodyStyle.fontSize+'pt',fontWeight:component.bodyStyle.fontWeight,}">
                            <span v-if="component.style.showIndex" class="title" :style="{width: component.style.indexWidth+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:''}">{{t+1}}</span> 
                            <div v-for="(column,i) in component.tableColumn" :key="i" class="title " :style="{width:column.style.width+'%',borderWidth:component.style.isBorder?component.style.borderWidth+'px':'',borderStyle:component.style.isBorder?component.style.borderStyle:'',borderColor:component.style.isBorder?component.style.borderColor:'',color:getCellColor(d,column)}"> 
                              <a  v-if="!column.isProgress" :href="getUrl(d,column)" :style="{color:'inherit',textDecoration:(column.isHyperLink && column.hyperLink)?'underline':'none'}" target="_blank">{{d[column.key]}}</a>
                              <el-progress :style="{lineHeight:component.bodyStyle.height/20}"  :percentage="d[column.key]*1"  :stroke-width="column.progressWidth" v-if="column.isProgress" :color="customColor(d,column)" :text-color="column.textColor"></el-progress>
                            </div>
                        </li>
                    </vue-seamless-scroll>
                </div>
        </div>
        <div  v-if="component.type == screenConstants.type.pageTable">
            <div :style="{height:(component.h-30)+'px',width:'100%'}">
            <el-table size="mini" :style="{background:component.rowStyle.backgroundColor}" :height="(component.h-30) + 'px'" :highlight-current-row="component.highlight" :border="component.border" :stripe="component.stripe" 
            :data="component.spec.data.values.slice((component.pagination.currentPage-1)*component.pagination.pageSize,component.pagination.currentPage*component.pagination.pageSize)"
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
              :total="component.spec.data.total"
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
export default {
    name:"tableComponent",
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
        params.params = Object.assign({}, componentParams, pageParams);
        let obj = {
          url: this.apis.screenDesign.getDynamicDatasApi,
          params: params,
          removeEmpty: false,
        };
        let that = this;
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == "200") {
            component.spec.data.values = response.responseData;
            if(component.type == "pageTable" && Array.isArray(component.spec.data.values)){
              component.spec.data.total = component.spec.data.values.length;
            }
            that.autoPage(that.component);
          }
        });
      },
      autoPage(component){
        if(component.type != "pageTable"){
          return;
        }
        this.component.pagination.currentPage = 1;
        let timer = this.tableTimer[component.id];
        if(component.autoPage){
          if(timer == null){
            timer = setInterval(function(){
              if((component.pagination.currentPage*component.pagination.pageSize)>=component.spec.data.total) {
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
      getCellColorByCondition(condition,d){
        let conditionProperty = condition.conditionProperty;
        let conditionColor = condition.conditionColor;
        let conditionOperator = condition.conditionOperator;
        let conditionValue = condition.conditionValue;
        let cellValue = d[conditionProperty];
        if(conditionOperator == "="){
          if((cellValue+'')==conditionValue){
            return conditionColor;
          }
        }else if(conditionOperator == "!="){
          if((cellValue+'')!=conditionValue){
            return conditionColor;
          }
        }else if(conditionOperator == ">"){
          if(this.commonUtil.isNumber(cellValue) && this.commonUtil.isNumber(conditionValue)){
            if((cellValue*1)>(conditionValue*1)){
              return conditionColor;
            }
          }else if(this.commonUtil.isDate(cellValue) && this.commonUtil.isDate(conditionValue)){
            const date1 = new Date(cellValue);
            const date2 = new Date(conditionValue);
            if(date1.getTime() > date2.getTime()){
              return conditionColor;
            }
          }
        }else if(conditionOperator == ">="){
          if(this.commonUtil.isNumber(cellValue) && this.commonUtil.isNumber(conditionValue)){
            if((cellValue*1)>=(conditionValue*1)){
              return conditionColor;
            }
          }
          else if(this.commonUtil.isDate(cellValue) && this.commonUtil.isDate(conditionValue)){
            const date1 = new Date(cellValue);
            const date2 = new Date(conditionValue);
            if(date1.getTime() >= date2.getTime()){
              return conditionColor;
            }
          }
        }else if(conditionOperator == "<"){
          if(this.commonUtil.isNumber(cellValue) && this.commonUtil.isNumber(conditionValue)){
            if((cellValue*1)<(conditionValue*1)){
              return conditionColor;
            }
          }else if(this.commonUtil.isDate(cellValue) && this.commonUtil.isDate(conditionValue)){
            const date1 = new Date(cellValue);
            const date2 = new Date(conditionValue);
            if(date1.getTime() < date2.getTime()){
              return conditionColor;
            }
          }
        }else if(conditionOperator == "<="){
          if(this.commonUtil.isNumber(cellValue) && this.commonUtil.isNumber(conditionValue)){
            if((cellValue*1)<=(conditionValue*1)){
              return conditionColor;
            }
          }else if(this.commonUtil.isDate(cellValue) && this.commonUtil.isDate(conditionValue)){
            const date1 = new Date(cellValue);
            const date2 = new Date(conditionValue);
            if(date1.getTime() <= date2.getTime()){
              return conditionColor;
            }
          }
        }else if(conditionOperator == "in"){
          if((cellValue+'').indexOf(conditionValue)>=0){
            return conditionColor;
          }
        }else if(conditionOperator == "not in"){
          if((cellValue+'').indexOf(conditionValue)>=0){
            return conditionColor;
          }
        }
        return "";
      },
      getUrl(d,column){
        if(column.isHyperLink && column.hyperLink){
          let params = this.commonUtil.getTableDrillParams(d,column)
          let url = this.commonUtil.buildUrlWithParams(column.hyperLink,params);
          // window.open(url,'_blank')
          return url
        }
      },
      customColor(d,column){
        let value = d[column.key]*1;
        let thresholdValue = column.thresholdValue*1
        if(value > thresholdValue){
          return column.upColor;
        }else if(value = thresholdValue){
          return column.equalColor;
        }else if(value < thresholdValue){
          return column.downColor;
        }
      }
    }
}
</script>
<style scoped lang="scss">
@import "@/element-variables.scss";

.handle,
.vdr {
  /* position:static !important; */
}
.box {
  width: 200px;
  height: 200px;
  border: 1px solid #ccc;
  background-color: #fff;
}
img {
  width: 99.7%;
  height: 99.6%;
  max-width: 99.7%;
  max-height: 99.6%;
}

.warp-title {
  overflow: hidden;
  ul {
    list-style: none;
    padding: 0;
    margin: 0 auto;
  }
  li {
    //   height: 40px;
    //   line-height: 40px;
    text-align: center;
    display: flex;
    justify-content: space-between;
    align-items: center;
    //   font-size: 20px;
    //   color:#00FFFF;
    //   font-weight: bold;
  }
}

.warp-content {
  overflow: hidden;
  ul {
    list-style: none;
    padding: 0;
    margin: 0 auto;
  }
  li {
    //   height: 30px;
    //   line-height: 30px;
    text-align: center;
    display: flex;
    justify-content: space-between;
    //   font-size: 15px;
    //   color:#ffffff
  }
}
.myclass {
  border: none;
}
.newclass {
  border: 1px solid #00ced1;
}
.player-container >>> video {
  width: 100%;
  height: 100%;
  object-fit: fill !important;
}
.text {
  // text-align: center;
  -webkit-background-clip: text;
  color: transparent;
}

::v-deep .el-pagination.is-background .btn-prev, ::v-deep .el-pagination.is-background .btn-next, ::v-deep .el-pagination.is-background .el-pager li{
  // color:#ffffff !important
}

::v-deep .el-pagination.is-background .el-pager .active{
  // color:$--color-primary !important
}

::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    display: none; /* 针对Webkit浏览器 */
}

::v-deep  .el-table th.gutter{
  display: none;
  width:0
}
::v-deep .el-table colgroup col[name='gutter']{
  display: none;
  width: 0;
}
::v-deep  .el-table__body{
  width: 100% !important;
}

/*最外层透明*/
// ::v-deep .el-table, ::v-deep.el-table__expanded-cell{
//     background-color: transparent;
// }
// /* 表格内背景颜色 */
::v-deep .el-table th,
::v-deep .el-table tr,
::v-deep .el-table td {
    background-color: transparent;
}

</style>
