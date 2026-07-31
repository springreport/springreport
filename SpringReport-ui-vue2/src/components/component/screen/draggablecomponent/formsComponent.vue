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
      backgroundColor:component.style.background,
    }"
  >
       <el-date-picker
          v-if="component.type == 'formsDate'"
          v-model="component.paramValue"
          :type="component.dateFormat=='yyyy-MM-dd'?'date':'datetime'"
          :format="component.dateFormat"
          :value-format="component.dateFormat"
          :size="component.style.size"
          :style="{width: component.width+'px'}"
          placeholder="选择日期"
          @change="changeValue(component)"
        >
        </el-date-picker>
        <el-date-picker
          v-if="component.type == 'formsDateRange'"
          v-model="component.paramValue"
          :type="component.dateFormat=='yyyy-MM-dd'?'daterange':'datetimerange'"
          :format="component.dateFormat"
          :value-format="component.dateFormat"
          :size="component.style.size"
          :style="{width: component.width+'px'}"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeValue(component)"
        >
        </el-date-picker>
        <el-select v-model="component.paramValue" placeholder="请选择" 
        v-if="component.type == 'formsSelect' || component.type == 'formsMultiselect'" 
        :multiple="component.type == 'formsMultiselect'" :size="component.style.size"
        :collapse-tags="component.type == 'formsMultiselect'"
        :style="{width: component.width+'px'}" @change="changeValue(component)">
          <el-option
            v-for="op in component.selectContent"
            :key="op.value"
            :label="op.name"
            :value="op.value">
          </el-option>
        </el-select>
        <selectNode
          v-if="component.type === 'formsTreeselect'"
          :ref="component.paramCode"
          :style="{width: component.width+'px'}"
          v-model="component.paramValue"
          :options="component.selectContent"
          :props="{parent: 'pid',value: 'id',label: 'name',children: 'children',}" 
          :item="component"
          :lazy="false"
          :size="component.style.size"
          @change="changeValue(component)"
        />
        <multiselectNode
          v-if="component.type === 'formsMultitree'"
          :ref="component.paramCode"
          v-model="component.paramValue"
          :props="{parent: 'pid',value: 'id',label: 'name',children: 'children',}" 
          :options="component.selectContent"
          :lazy="false"
          :size="component.style.size"
          @selected="changeMultiTreeValue()"
          :style="{width: component.width+'px'}"
          :check-strictly="component.checkStrictly"
        />
        <el-cascader
            v-if="component.type === 'formsCascader'"
            id="formsCascader"
            v-model="component.paramValue"
            ref="formsCascader"
            placeholder="请选择"
            :props="cascaderProps"
            :style="{width: component.width+'px'}"
            :size="component.style.size"
            @change="changeValue(component)"
            clearable
            ></el-cascader>
        <el-radio-group v-model="component.paramValue" :size="component.style.size"  v-if="component.type === 'formsRadio'" @change="changeValue(component)">
          <el-radio v-for="op in component.selectContent" :key="op.value"
            :label="op.value" border>{{op.name}}</el-radio>
        </el-radio-group>
        <el-button v-if="component.type === 'formsButton'" :size="component.style.size" :style="{ backgroundColor: component.style.btnColor, color: component.style.fontColor,width:component.width+'px',fontSize:component.style.fontSize+'px' }" @click="changeValue(component)">{{component.btnText}}</el-button>
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
      backgroundColor:component.style.background,
    }"
  >
       <el-date-picker
          v-if="component.type == 'formsDate'"
          v-model="component.paramValue"
          :type="component.dateFormat=='yyyy-MM-dd'?'date':'datetime'"
          :format="component.dateFormat"
          :value-format="component.dateFormat"
          :size="component.style.size"
          :style="{width: component.width+'px'}"
          placeholder="选择日期"
          @change="changeValue(component)"
        >
        </el-date-picker>
        <el-date-picker
          v-if="component.type == 'formsDateRange'"
          v-model="component.paramValue"
          :type="component.dateFormat=='yyyy-MM-dd'?'daterange':'datetimerange'"
          :format="component.dateFormat"
          :value-format="component.dateFormat"
          :size="component.style.size"
          :style="{width: component.width+'px'}"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeValue(component)"
        >
        </el-date-picker>
        <el-select v-model="component.paramValue" placeholder="请选择" 
        v-if="component.type == 'formsSelect' || component.type == 'formsMultiselect'" 
        :multiple="component.type == 'formsMultiselect'" :size="component.style.size"
        :collapse-tags="component.type == 'formsMultiselect'"
        :style="{width: component.width+'px'}" @change="changeValue(component)">
          <el-option
            v-for="op in component.selectContent"
            :key="op.value"
            :label="op.name"
            :value="op.value">
          </el-option>
        </el-select>
        <selectNode
          v-if="component.type === 'formsTreeselect'"
          :ref="component.paramCode"
          :style="{width: component.width+'px'}"
          v-model="component.paramValue"
          :options="component.selectContent"
          :props="{parent: 'pid',value: 'id',label: 'name',children: 'children',}" 
          :item="component"
          :lazy="false"
          :size="component.style.size"
          @change="changeValue(component)"
        />
        <multiselectNode
          v-if="component.type === 'formsMultitree'"
          :ref="component.paramCode"
          v-model="component.paramValue"
          :props="{parent: 'pid',value: 'id',label: 'name',children: 'children',}" 
          :options="component.selectContent"
          :lazy="false"
          :size="component.style.size"
          @selected="changeMultiTreeValue()"
          :style="{width: component.width+'px'}"
          :check-strictly="component.checkStrictly"
        />
        <el-cascader
            v-if="component.type === 'formsCascader'"
            id="formsCascader"
            v-model="component.paramValue"
            ref="formsCascader"
            placeholder="请选择"
            :props="cascaderProps"
            :style="{width: component.width+'px'}"
            :size="component.style.size"
            @change="changeValue(component)"
            clearable
            ></el-cascader>
         <el-radio-group v-model="component.paramValue" :size="component.style.size"  v-if="component.type === 'formsRadio'" @change="changeValue(component)">
          <el-radio v-for="op in component.selectContent" :key="op.value"
            :label="op.value" border>{{op.name}}</el-radio>
        </el-radio-group>
        <el-button v-if="component.type === 'formsButton'" :size="component.style.size" :style="{ backgroundColor: component.style.btnColor, color: component.style.fontColor,width:component.width+'px',fontSize:component.style.fontSize+'px' }" @click="changeValue(component)">{{component.btnText}}</el-button>
  </div>
 </div>
</template>

<script>
export default {
  name: "formsComponent",
  components: {
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
    formsParams: {
      type: Object,
      default: () => ({})
    },
  },
  data () {
        return {
          value:[],
          cascaderProps: {
            lazy: true,
            lazyLoad:this.lazyLoad,
            value: 'value',
            label: 'name',
            checkStrictly: true
          },
            
        };
    },
  mounted() {
    this.initData(this.searchParams);
  },
  methods: {
    //数据初始化
    initData(searchParams) {
      let pageParams = this.commonUtil.searchParamMap(searchParams)
      let params = Object.assign({}, pageParams,this.formsParams);
      this.changeDataContent(params);
    },
    
    changeValue(component){
      if(component.triggerTiming != '1' && this.$parent.$parent.$parent.refreshComponentData){
        this.$parent.$parent.$parent.refreshComponentData();
      }
    },
    changeMultiTreeValue(){
      this.changeValue(this.component)
    },
    lazyLoad(node, resolve){
      let paramCode = this.component.paramCode?this.component.paramCode:"code";
      var params = {
            selectContent: this.component.dataContent,
            datasourceId: this.component.datasourceId,
            params: {level:node.level,}
          }
          params.params[paramCode] = node.value
          var obj = {
            url: '/api/reportTplDataset/getSelectData',
            params: params
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              // if(response.responseData && response.responseData.length > 0){
              //   for (let index = 0; index < response.responseData.length; index++) {
              //     response.responseData[index].leaf = node.level>=this.component.level-1
              //   }
              // } 
              resolve(response.responseData);
            }
          })
    },
    changeDataContent(params){
      if(this.component.dataType == '2'){
        if(this.component.type=="formsSelect" || this.component.type=="formsMultiselect" || this.component.type=="formsRadio"){
          this.getSelectData(this.component,params)
        }else if(this.component.type=="formsMultitree" || this.component.type=="formsTreeselect"){
          this.getTreeSelectData(this.component,params)
        }
      }else{
        if(this.component.type=="formsSelect" || this.component.type=="formsMultiselect" || this.component.type=="formsRadio"){
          if(this.component.dataContent){
            this.component.selectContent = JSON.parse(this.component.dataContent)
          }else{
            this.component.selectContent = []
          }
        }else if(this.component.type=="formsMultitree" || this.component.type=="formsTreeselect"){
            this.component.selectContent = this.commonUtil.arrayToTree(JSON.parse(this.component.dataContent))
        }else{
            this.component.selectContent = []
        }
      }
    },
    getSelectData(item,params) {
      if(item.dataContent){
        var params = {
            selectContent: item.dataContent,
            datasourceId: item.datasourceId,
            params: params
          }
          var obj = {
            url: '/api/reportTplDataset/getSelectData',
            params: params
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              item.selectContent = response.responseData
            }
          })
      }
    },
    getTreeSelectData(item,params) {
      if(item.dataContent){
        var params = {
            selectContent: item.dataContent,
            datasourceId: item.datasourceId,
            params: params
          }
          var obj = {
            url: '/api/reportTplDataset/getTreeSelectData',
            params: params
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              item.selectContent = response.responseData
            }
          })
      }
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