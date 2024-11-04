<!-- 搜索表单 -->
<template>
<div>
    <el-form :inline="inline" ref="reportFormRef" class="demo-form-inline" :model="searchData" :activitiName="activitiName" :showSearch="showSearch" :isParamMerge="isParamMerge">
        <div v-show="isShowSearch" style="display: flex; width: 100%">
         <!-- <div style="display: flex; width: 15%">
          <div style="width: 100%; height: 100%; padding-top:18px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
            <span :title="reportName">
              {{reportName}}
            </span>
          </div>
          </div> -->
        <div style="display: flex; width: 100%;padding-left:0px">
        <el-tabs type="border-card" v-model="tabFocus" v-if="reportForm.length>1" style="width: 100%">
        <el-tab-pane v-for="(data,i) in reportForm" :key="data.datasetName" :label="isParamMerge=='1'?'报表参数':'数据集【'+data.datasetName+'】参数'" :name="data.datasetName">
        <el-form-item v-for='(item,index)  in data.params' :class="itemClass"  :label="item.paramName" :key='item.paramCode' 
        :prop="'params.' + i + '.'+'params.'+index+'.'+item.paramCode" 
        v-show="item.paramHidden == 2"
        :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):null">
            <!-- 输入框 -->
            <el-input v-if="(item.paramType==='varchar' || item.paramType==='number') && (item.componentType != 'select' && item.componentType != 'mutiselect' && item.componentType != 'treeSelect' && item.componentType != 'multiTreeSelect')" v-model="searchData.params[i].params[index][item.paramCode]" :placeholder="'请输入'+item.paramName" :size="item.size" :disabled="item.disabled"></el-input>
            <!-- 下拉框 -->
            <el-select v-if="(item.paramType==='select' || item.componentType === 'select') && item.isRelyOnParams!==1" v-model="searchData.params[i].params[index][item.paramCode]" :size="item.size" clearable @focus="getSelectData(item)">
                 <el-option :label="'请选择'" value=""></el-option>
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <el-select v-if="(item.paramType==='select' || item.componentType === 'select') && item.isRelyOnParams===1" v-model="searchData.params[i].params[index][item.paramCode]" :size="item.size" @focus="getRelyOnParamys(item,searchData.params[i],searchData.params[i].params[index])">
                 <el-option :label="'请选择'" value=""></el-option>
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <!-- 多选下拉框 -->
            <el-select v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect')" v-model="searchData.params[i].params[index][item.paramCode]" :size="item.size" :multiple="true" @focus="getSelectData(item)">
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <!-- 日期 -->
            <!-- <el-date-picker v-if="item.paramType==='date'" type="date" v-model="searchData.params[i].params[index][item.paramCode]" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker> -->
            <el-date-picker
                  v-if="item.paramType === 'date'"
                  :size="item.size"
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  :format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':(item.dateFormat == 'yyyy-MM-dd HH:mm:ss')?'YYYY-MM-DD HH:mm:ss':item.dateFormat"
                  :value-format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':(item.dateFormat == 'yyyy-MM-dd HH:mm:ss')?'YYYY-MM-DD HH:mm:ss':item.dateFormat"
                  :type="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'date':item.dateFormat == 'YYYY-MM'?'month':item.dateFormat == 'YYYY'?'year':'datetime'"
                ></el-date-picker>
                <el-tree-select
                    v-if="item.paramType==='multiTreeSelect' || item.componentType==='multiTreeSelect'"
                    :size="item.size"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :data="item.selectData==null?[]:item.selectData"
                    :props="{ parent: 'pid', label: 'name',children: 'children'}"
                    show-checkbox
                    multiple
                    check-on-click-node
                    :check-strictly="item.checkStrictly==1?false:true"
                    clearable
                    @focus="getTreeData(item)"
                />
                <el-tree-select
                    v-if="item.paramType==='treeSelect' || item.componentType==='treeSelect'"
                    :size="item.size"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :data="item.selectData==null?[]:item.selectData"
                    :props="{ parent: 'pid', label: 'name',children: 'children'}"
                    clearable
                    :check-strictly="true"
                    @focus="getTreeData(item)"
                />
            </el-form-item>
            <el-form-item >
                <div class="flex" :style="{'text-align': inline ? 'center' : 'inherit',display:'-webkit-box'}"> 
                <!-- <el-button v-for='item in searchHandle' :key="item.label" :type="item.type"  @click='item.handle()' :icon="item.icon" :size="item.size" :title="item.label" circle ></el-button> -->
                <div v-for='item in searchHandle' :key="item.label" style="margin-left:10px">
                        <el-button v-if="!item.btnType || item.btnType == 'button'" :type="item.type"  @click='item.handle()' :icon="item.icon" :size="item.size" :title="item.label" circle ></el-button>
                        <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" style="margin-top:5px" trigger="hover" placement="bottom" :size="item.size">
                            <el-button :type="item.type" :icon="item.icon" circle :size="item.size"></el-button>
                            <template #dropdown>
                                <el-dropdown-menu >
                                    <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click='op.handle()' >{{op.label}}</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                <el-button v-show="isDrill == 1" style="margin-left:10px" type="primary" icon="icon-back" @click="back" size="small" circle title="返回上级报表"></el-button>
                <el-button style="margin-left:10px" type="primary" :icon="icon" @click="showSearchClick" size="small" circle :title="text" class="showSearchBtn"></el-button>
            </div>
            </el-form-item>
            </el-tab-pane>
            </el-tabs>
            <div v-if="reportForm.length==1" style="width:100%;padding-top:10px;padding-left:20px">
            <el-form-item v-for='(item,index)  in reportForm[0].params' :class="itemClass"  :label="item.paramName" 
            :key='item.paramCode' :prop="'params.' + 0 + '.'+'params.'+index+'.'+item.paramCode" 
            :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):null"
            v-show="item.paramHidden == 2"
            >
            <!-- 输入框 -->
            <el-input v-if="(item.paramType==='varchar' || item.paramType==='number') && (item.componentType != 'select' && item.componentType != 'mutiselect' && item.componentType != 'treeSelect' && item.componentType != 'multiTreeSelect')" v-model="searchData.params[0].params[index][item.paramCode]" :placeholder="'请输入'+item.paramName" :size="item.size" :disabled="item.disabled"></el-input>
            <!-- 下拉框 -->
            <el-select v-if="(item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams!==1" v-model="searchData.params[0].params[index][item.paramCode]" clearable :size="item.size" @focus="getSelectData(item)">
                 <el-option :label="'请选择'" value=""></el-option>
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <el-select v-if="(item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams===1" v-model="searchData.params[0].params[index][item.paramCode]" :size="item.size" @focus="getRelyOnParamys(item,searchData.params[0],searchData.params[0].params[index])">
                 <el-option :label="'请选择'" value=""></el-option>
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <!-- 多选下拉框 -->
            <el-select v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect')" v-model="searchData.params[0].params[index][item.paramCode]" :size="item.size" :multiple="true" @focus="getSelectData(item)">
               <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
            </el-select>
            <!-- 日期 -->
            <!-- <el-date-picker v-if="item.paramType==='date'" v-model="searchData.params[0].params[index][item.paramCode]" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker> -->
            <el-date-picker
                  v-if="item.paramType === 'date'"
                  :size="item.size"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':(item.dateFormat == 'yyyy-MM-dd HH:mm:ss')?'YYYY-MM-DD HH:mm:ss':item.dateFormat"
                  :value-format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':(item.dateFormat == 'yyyy-MM-dd HH:mm:ss')?'YYYY-MM-DD HH:mm:ss':item.dateFormat"
                  :type="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'date':item.dateFormat == 'YYYY-MM'?'month':item.dateFormat == 'YYYY'?'year':'datetime'"
                ></el-date-picker>
                <el-tree-select
                    v-if="item.paramType==='multiTreeSelect'|| item.componentType==='multiTreeSelect'"
                    :size="item.size"
                    v-model="searchData.params[0].params[index][item.paramCode]"
                    :data="item.selectData==null?[]:item.selectData"
                    :props="{ parent: 'pid', label: 'name',children: 'children'}"
                    show-checkbox
                    multiple
                    check-on-click-node
                    :check-strictly="item.checkStrictly==1?false:true"
                    clearable
                    @focus="getTreeData(item)"
                />
                <el-tree-select
                    v-if="item.paramType==='treeSelect'|| item.componentType==='treeSelect'"
                    :size="item.size"
                    v-model="searchData.params[0].params[index][item.paramCode]"
                    :data="item.selectData==null?[]:item.selectData"
                    :props="{ parent: 'pid', label: 'name',children: 'children'}"
                    clearable
                    :check-strictly="true"
                    @focus="getTreeData(item)"
                />
            </el-form-item>
            <el-form-item >
                <div class="flex" :style="{'text-align': inline ? 'center' : 'inherit',display:'-webkit-box'}">  
                    <div v-for='item in searchHandle' :key="item.label" style="margin-left:10px">
                        <el-button v-if="!item.btnType || item.btnType == 'button'" :type="item.type"  @click='item.handle()' :icon="item.icon" :size="item.size" :title="item.label" circle ></el-button>
                        <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" style="margin-top:5px" trigger="hover" placement="bottom" :size="item.size">
                            <el-button :type="item.type" :icon="item.icon" circle :size="item.size"></el-button>
                            <template #dropdown>
                                <el-dropdown-menu >
                                    <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click='op.handle()' >{{op.label}}</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                    <el-button v-show="isDrill == 1" style="margin-left:10px" type="primary" icon="icon-back" @click="back" size="small" circle title="返回上级报表"></el-button>
                    <el-button style="margin-left:10px" type="primary" :icon="icon" @click="showSearchClick" size="small" circle :title="text" class="showSearchBtn"></el-button>
                </div>
            </el-form-item>
            </div>
            <div v-if="!reportForm || reportForm.length==0" style="width:100%;padding-top:10px;padding-left:20px">
            <el-form-item >
                <div class="flex" :style="{'text-align': inline ? 'center' : 'inherit',display:'-webkit-box'}">  
                    <div v-for='item in searchHandle' :key="item.label" style="margin-left:10px">
                        <el-button v-if="!item.btnType || item.btnType == 'button'" :type="item.type"  @click='item.handle()' :icon="item.icon" :size="item.size" :title="item.label" circle ></el-button>
                        <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" style="margin-top:5px" trigger="hover" placement="bottom" :size="item.size">
                            <el-button :type="item.type" :icon="item.icon" circle :size="item.size"></el-button>
                            <template #dropdown>
                                <el-dropdown-menu >
                                    <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click='op.handle()' >{{op.label}}</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                    <el-button v-show="isDrill == 1" style="margin-left:10px" type="primary" icon="icon-back" @click="back" size="small" circle title="返回上级报表"></el-button>
                    <el-button style="margin-left:10px" type="primary" :icon="icon" @click="showSearchClick" size="small" circle :title="text" class="showSearchBtn"></el-button>
                </div>
            </el-form-item>
            </div>
        </div>
        </div>
        <div v-show="!isShowSearch" style="display: flex; width: 100%">
        <!-- <div style="width: 100%; height: 100%;">
          <span :title="reportName">{{reportName}}
            <el-button
            type="primary"
            :icon="icon"
            circle
            @click="showSearchClick"
            size="small"
            :title="text"
          ></el-button>
          </span>
        </div> -->
      </div>
    </el-form>
    
</div>
</template>

<script>
export default {
    props:{
        inline:{
           type:Boolean,
           default:true
        },
        itemClass:{
            type:String,
            default:'form_input'
        },
        isHandle:{
            type:Boolean,
            default:true
        },
        labelWidth:{
            type:String,
            default:'auto'
        },
        size:{
            type:String,
            default:'small'
        },
        reportForm:{
            type:Array,
            default:() => ([]),
        },
        searchHandle:{
            type:Array,
            default:()=>[]
        },
        searchData:{
            type:Object,
            default:() => ({}),
        },
        activitiName:{
            type:String,
            default:''
        },
        showSearch:{
            type:Boolean,
            default:false
        },
        isParamMerge:{
            type:String,
            default:'2'
        },
        reportName: {
            type: String,
            default: "",
        },
        isDrill: {
            type: Number,
            default: 2,
        },
    },
    name:"reportForm",
    data () {
        return {
            tabFocus:this.activitiName,
            isShowSearch:this.showSearch,
            text:"收起搜索",
            icon:"icon-up",
        };
    },
    mounted(){

       
    },
    methods:{
        showSearchClick(){
            if(this.isShowSearch)
            {
                this.isShowSearch = false;
                this.text = "展开搜索";
                this.icon = "icon-down"
            }else{
                this.isShowSearch = true;
                this.text = "收起搜索";
                this.icon = "icon-up"
            }
            this.$emit('update:showSearch', this.isShowSearch);
            this.$nextTick(() => {
                try {
                    luckysheet.resize();
                } catch (error) {
                    
                }
            });
        },
        getRelyOnParamys(item,data,modelData){
            var relyOnValue = "";
            for (let index = 0; index < data.params.length; index++) {
                const element = data.params[index];
                if(element.hasOwnProperty(item.relyOnParams))
                {
                    relyOnValue = element[item.relyOnParams];
                }
            }
            if(relyOnValue)
            {
                var params = {
                    selectContent:item.selectContent,
                    datasourceId:item.datasourceId,
                    params:{}
                }
                params.params[item.relyOnParams] = relyOnValue;
                var obj = {
                    url:"/api/reportTplDataset/getRelyOnData",
                    params:params
                }
                this.commonUtil.doPost(obj) .then(response=>{
                    if(response.code == "200")
                    {
                       item.selectData = response.responseData
                    }
                });
            }else{
                item.selectData = [];
                modelData[item.paramCode] = null;
            }
        },
        //返回上级报表
        back(){
            this.$parent.$parent.back();
        },
        getSelectData(item){
            if(!item.init)
            {
                if(item.selectType == "1")
                {
                item.selectData = JSON.parse(item.selectContent);
                }else{
                var params = {
                    selectContent: item.selectContent,
                    datasourceId: item.datasourceId,
                    params: {},
                };
                var obj = {
                    url: "/api/reportTplDataset/getSelectData",
                    params: params,
                };
                this.commonUtil.doPost(obj).then((response) => {
                    if (response.code == "200") {
                    item.selectData = response.responseData;
                    if(response.responseData && response.responseData.length > 0)
                    {
                        item.init = true;
                    }
                    }
                });
                }
                
            }
        },
        getTreeData(item)
        {
            if(!item.init)
            {
                var params = {
                selectContent: item.selectContent,
                datasourceId: item.datasourceId,
                params: {},
                };
                var obj = {
                url: "/api/reportTplDataset/getTreeSelectData",
                params: params,
                };
                this.commonUtil.doPost(obj).then((response) => {
                if (response.code == "200") {
                    item.selectData = response.responseData;
                    if(response.responseData && response.responseData.length > 0)
                    {
                        item.init = true;
                    }
                }
                });
            }
        }
    }
}

</script>
<style lang="scss" scoped>
span {
  padding: 0px 20px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 19px;
  line-height: 30px;
  color: #45c5a9;
  font-weight: bold;
  margin: 5px 0;
}
:deep(.el-form-item__label-wrap){
    margin-left:0px !important
}
:deep(.el-form-item)
{
    margin-bottom:15px !important
}

:deep(.el-form-item__error)
{
    line-height: 0 !important;
    padding-top: 8px !important;
}
</style>