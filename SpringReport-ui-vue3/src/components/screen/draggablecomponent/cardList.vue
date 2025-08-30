<template>
<div>
  <el-carousel :height="(component.isborder && component.borderType)?((component.h-(component.borderTop?component.borderTop:0)-(component.borderBottom?component.borderBottom:0)) + 'px'):component.h + 'px'" :interval="component.pagination.autoPageInterval"
  indicator-position="none">
  <el-carousel-item v-for="i in getPageSize()" :key="i">
  <el-row :gutter="1" style="" type="flex" >
    <el-col v-for="(item, index) in getDatas(i)" :key="index" :span="Math.floor(24/component.pagination.pageColumn)">
      <el-card class="box-card" shadow="always" :style="{background:component.bodyStyle.backgroundColor?component.bodyStyle.backgroundColor:'#fff',cursor:(component.isDrill && component.drillLink)?'pointer':'default'}"  v-on:click="cardDrill(item)">
        <template #header>
        <div class="header">
          <span :style="{color:getHeadColor(1,item),fontSize:component.leftHead.fontSize+'px',textAlign: 'center',fontWeight:component.leftHead.fontWeight}">{{item[component.leftHead.property]?item[component.leftHead.property]:component.leftHead.property}}</span>
          <span class="card-header-tag-green" :style="{color:getHeadColor(2,item),fontSize:component.rightHead.fontSize+'px',textAlign: 'center',fontWeight:component.rightHead.fontWeight}">{{item[component.rightHead.property]?item[component.rightHead.property]:component.rightHead.property}}</span>
        </div>
        </template>
        <div v-for="(rowContent, index) in component.bodyContent.rowContents" :key="index" class="body_content">
          <div><div class="card-label" :style="{color:rowContent.titleColor,fontSize:rowContent.titleFontSize+'px',fontWeight:rowContent.titleFontWeight}">{{item[rowContent.title]?item[rowContent.title]:rowContent.title}}</div>
          <span :style="{color:rowContent.contentColor,fontSize:rowContent.contentFontSize+'px',fontWeight:rowContent.contentFontWeight,paddingLeft:'10px'}">{{item[rowContent.property]?item[rowContent.property]:rowContent.property}}</span>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
  </el-carousel-item>
  </el-carousel>
</div>
</template>
<script>

export default {
    name: 'CardList',
    props: {
        components: {
            type: Array,
            default: () => []
        },
        component: {
            type: Object,
            default: () => ({})
        },
    },
    data() {
        return {
        }
    },
    mounted(){
    },
    methods: {
      getPageSize(){
        let dataLength = this.component.spec.data.values.length;
        let pageSize =  this.component.pagination.pageSize;
        let pageCount = Math.floor(dataLength / pageSize);
        let remainder = dataLength%pageSize;
        if(remainder > 0){
          pageCount = pageCount + 1;
        }
        return pageCount;
      },
      getDatas(index){
        let datas = this.component.spec.data.values.slice((index-1)*this.component.pagination.pageSize,(index)*this.component.pagination.pageSize);
        return datas;
      },
      //1 左侧表头  2右侧表头
      getHeadColor(type,d){
        let color = "";
        let conditions = [];
        if(type == 1){
          conditions = this.component.leftHead.conditions;
        }else{
          conditions = this.component.rightHead.conditions;
        }
        if(conditions && conditions.length > 0){
          for (let index = 0; index < conditions.length; index++) {
            const element = conditions[index];
            color = this.getCellColorByCondition(element,d);
            if(color){
              break;
            }
          }
        }
        if(!color){
          if(type == 1){
            color = this.component.leftHead.color
          }else{
            color = this.component.rightHead.color
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
      cardDrill(item){
        if(this.component.isDrill && this.component.drillLink){
          let paramsMap = {};
          let params = this.component.drillParam.split(",");
          for (let index = 0; index < params.length; index++) {
            const element = params[index];
            if(item[element]){
                paramsMap[element] = item[element];
            }
          }
          let url = this.commonUtil.buildUrlWithParams(this.component.drillLink,paramsMap);
          window.open(url, "_blank");
        }
      },
    }
}
</script>


<style scoped lang="scss">
.el-row {
  display:flex;
  flex-wrap: wrap;
  align-items: center;
}
.el-row  .el-card {
  min-width: 100%;
  height: 100%;
  margin-right: 20px;
  transition: all .5s;
}
.el-form-item {
  margin-bottom: 0 !important;
}
.pagination-container {
  margin-top: -3px;
  margin-bottom: 30px;
}
.box-card {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    .label{
      padding: 0 3px;
      background-color: #fdf0da;
      color: #f19901;
    }
    .header-label {
      padding-left: 10px;
    }
    .card-header-tag-blue {
      position: absolute;
      width: 68px;
      height: 30px;
      top: -14px;
      right: -15px;
      //background-image: url("~@/assets/images/img_dpj_t.png");
      display: inline-block;
    }
    .card-header-tag-green {
      position: absolute;
      // width: 68px;
      // height: 30px;
      right: 5px;
    //   background-image: url("~@/assets/images/img_ypj_t.png");
      display: inline-block;
    }
  }
  .footer {
    font-size: 18px !important;
    background-color: rgb(245, 247, 251);
    display: flex;
    height: 50px;
    justify-content: space-evenly;
  }
  .card-label {
    color: rgb(197, 197, 197);
    // width: 70px;
    display: inline-block;
    margin-bottom: 5px;
  }
}
:deep(.el-carousel__item),:deep(.is-animating){
  overflow-x: hidden;
  overflow-y:auto;
}

:deep(.el-carousel__item::-webkit-scrollbar),:deep(.is-animating::-webkit-scrollbar) {
    display: none;
}
 
/* 对于 IE 和 Edge */
:deep(.el-carousel__item),:deep(.is-animating) {
    -ms-overflow-style: none; /* IE 和 Edge */
}
 
/* 对于 Firefox */
:deep(.el-carousel__item),:deep(.is-animating) {
    scrollbar-width: none; /* Firefox */
}

:deep(.el-card) {
    border: none;
}
:deep(.el-card__body) {
    padding: 10px;
}

:deep(.el-card__header) {
    padding: 10px;
}

:deep(.el-card__header) {
	border-bottom:none
}
.body_content{
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>


