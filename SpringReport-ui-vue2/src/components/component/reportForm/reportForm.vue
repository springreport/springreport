<!-- 搜索表单 -->
<template>
  <div>
    <el-form
      :inline="inline"
      ref="reportFormRef"
      class="demo-form-inline"
      :model="searchData"
      :activitiName="activitiName"
      :showSearch="showSearch"
      :isParamMerge="isParamMerge"
      @submit.native.prevent
    >
      <div v-show="isShowSearch" style="display: flex; width: 100%">
        <!-- <div style="display: flex; width: 15%">
          <div style="width: 100%; height: 100%; padding-top:18px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
            <span :title="reportName">
              {{reportName}}
            </span>
          </div>
        </div> -->
        <div style="display: flex; width: 100%;padding-left:0px">
          <el-tabs
            type="border-card"
            v-model="tabFocus"
            v-if="reportForm.length > 1"
            style="width: 100%"
          >
            <el-tab-pane
              v-for="(data, i) in reportForm"
              :key="data.datasetName"
              :label="
                isParamMerge == '1'
                  ? '报表参数'
                  : '数据集【' + data.datasetName + '】参数'
              "
              :name="data.datasetName"
            >
              <el-form-item
                v-for="(item, index) in data.params"
                :class="itemClass"
                :label="item.paramName"
                :key="item.paramCode"
                :prop="
                  'params.' + i + '.' + 'params.' + index + '.' + item.paramCode
                "
                v-show="item.paramHidden == 2"
                :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):null"
              >
                <!-- 输入框 -->
                <el-input
                  v-if="
                    (item.paramType === 'varchar' || item.paramType === 'number') && (item.componentType != 'select' && item.componentType != 'mutiselect' && item.componentType != 'treeSelect' && item.componentType != 'multiTreeSelect')
                  "
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  :placeholder="'请输入' + item.paramName"
                  size="mini"
                  :disabled="item.disabled"
                  clearable
                ></el-input>
                <!-- 下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  size="mini"
                  clearable
                  @focus="
                    getSelectData(
                      item
                    )
                  "
                >
                  <el-option :label="'请选择'" value=""></el-option>
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <el-select
                  v-if="
                     (item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  clearable
                  size="mini"
                  @focus="
                    getRelyOnParamys(
                      item,
                      searchData.params[i],
                      searchData.params[i].params[index]
                    )
                  "
                >
                  <el-option :label="'请选择'" value=""></el-option>
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <!-- 多选下拉框 -->
                <el-select
                  v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect')"
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  size="mini"
                  :multiple="true"
                  @focus="getSelectData(item)">
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <!-- 日期 -->
                <el-date-picker
                  v-if="item.paramType === 'date'"
                  size="mini"
                  v-model="searchData.params[i].params[index][item.paramCode]"
                  :format="item.dateFormat"
                  :value-format="item.dateFormat"
                  :type="item.dateFormat == 'yyyy-MM-dd'?'date':item.dateFormat == 'yyyy-MM'?'month':item.dateFormat == 'yyyy'?'year':'datetime'"
                ></el-date-picker>
                <multiselectNode v-if="item.paramType==='multiTreeSelect' || item.componentType==='multiTreeSelect'"  :ref="searchData.params[i].params[index][item.paramCode]" 
                v-model="searchData.params[i].params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :lazy="false" 
                :item="item"
                :focusMethod="getTreeData" :checkStrictly="item.checkStrictly==1?false:true">
                </multiselectNode>
                <selectNode v-if="item.paramType==='treeSelect' || item.componentType==='treeSelect'"  :ref="searchData.params[i].params[index][item.paramCode]" 
                v-model="searchData.params[i].params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :item="item" :focusMethod="getTreeData" :lazy="false"  :clearable="true" size="mini">
                </selectNode>
              </el-form-item>
              <span>
              <el-form-item v-for="item in searchHandle" :key="item.label">
                <!-- <el-button  :type="item.type"  @click='item.handle()' :icon="item.icon" size="mini">{{item.label}}</el-button> -->
                <el-button v-if="!item.btnType || item.btnType == 'button'"
                  :icon="item.icon"
                  circle
                  size="mini"
                  :type="item.type"
                  :title="item.label"
                  @click='item.handle()' 
                ></el-button>
                <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" trigger="hover" placement="bottom" size="small">
                  <el-button :type="item.type" :icon="item.icon" circle size="mini"></el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click.native='op.handle()' >{{op.label}}</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-form-item>
              <!-- el-icon-back -->
              <el-form-item v-show="isDrill == 1">
                <el-button
                  type="primary"
                  icon="el-icon-back"
                  circle
                  @click="back"
                  size="mini"
                  title="返回上级报表"
                ></el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  :icon="icon"
                  circle
                  @click="showSearchClick"
                  size="mini"
                  :title="text"
                  class="showSearchBtn"
                ></el-button>
              </el-form-item>
              </span>
              <!-- <el-form-item>
                <el-button type="primary" :icon="icon" @click="showSearchClick" size="mini">{{text}}</el-button>
            </el-form-item> -->
            </el-tab-pane>
          </el-tabs>
          <div v-if="reportForm.length==1" style="width:100%;padding-left:20px">
                <el-form-item
                v-for="(item, index) in reportForm[0].params"
                :class="itemClass"
                :label="item.paramName"
                :key="item.paramCode"
                :prop="
                  'params.' + 0 + '.' + 'params.' + index + '.' + item.paramCode
                "
                :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):null"
                v-show="item.paramHidden == 2"
                >
                <!-- 输入框 -->
                <el-input
                  v-if="
                    (item.paramType === 'varchar' || item.paramType === 'number') && (item.componentType != 'select' && item.componentType != 'mutiselect' && item.componentType != 'treeSelect' && item.componentType != 'multiTreeSelect')
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :placeholder="'请输入' + item.paramName"
                  size="mini"
                  :disabled="item.disabled"
                  clearable
                ></el-input>
                <!-- 下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  size="mini"
                  clearable
                  @focus="
                    getSelectData(
                      item
                    )"
                >
                  <el-option :label="'请选择'" value=""></el-option>
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <el-select
                  v-if="
                    (item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  clearable
                  size="mini"
                  @focus="
                    getRelyOnParamys(
                      item,
                      searchData.params[0],
                      searchData.params[0].params[index]
                    )
                  "
                >
                  <el-option :label="'请选择'" value=""></el-option>
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <!-- 多选下拉框 -->
                <el-select
                  v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect')"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  size="mini"
                  :multiple="true"
                  @focus="getSelectData(item)">
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <!-- 日期 -->
                <el-date-picker
                  v-if="item.paramType === 'date'"
                  size="mini"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :format="item.dateFormat"
                  :value-format="item.dateFormat"
                  :type="item.dateFormat == 'yyyy-MM-dd'?'date':item.dateFormat == 'yyyy-MM'?'month':item.dateFormat == 'yyyy'?'year':'datetime'"
                ></el-date-picker>
                <multiselectNode v-if="item.paramType==='multiTreeSelect' || item.componentType==='multiTreeSelect'"  :ref="searchData.params[0].params[index][item.paramCode]" 
                v-model="searchData.params[0].params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :lazy="false" 
                :item="item"
                :focusMethod="getTreeData" :checkStrictly="item.checkStrictly==1?false:true">
                  </multiselectNode>
                <selectNode v-if="item.paramType==='treeSelect'|| item.componentType==='treeSelect'"  :ref="searchData.params[0].params[index][item.paramCode]" 
                v-model="searchData.params[0].params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :lazy="false"  :clearable="true" size="mini"
                :item="item"
                :focusMethod="getTreeData">
                </selectNode>
                </el-form-item>
                <span>
                <el-form-item v-for="item in searchHandle" :key="item.label">
                    <el-button v-if="!item.btnType || item.btnType == 'button'"
                    :icon="item.icon"
                    circle
                    :size="item.size"
                    :type="item.type"
                    :title="item.label"
                    @click='item.handle()' 
                    ></el-button>
                    <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" trigger="hover" placement="bottom" size="small">
                      <el-button :type="item.type" :icon="item.icon" circle size="mini"></el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click.native='op.handle()' >{{op.label}}</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                </el-form-item>
                 <el-form-item v-show="isDrill == 1">
                <el-button 
                  type="primary"
                  icon="el-icon-back"
                  circle
                  @click="back"
                  size="mini"
                  title="返回上级报表"
                ></el-button>
              </el-form-item>
                <el-form-item>
                    <el-button
                    type="primary"
                    :icon="icon"
                    circle
                    @click="showSearchClick"
                    size="mini"
                    :title="text"
                     class="showSearchBtn"
                    ></el-button>
                </el-form-item>
                </span>
          </div>
          <div v-if="!reportForm || reportForm.length==0" style="width:100%;padding-left:20px">
                <el-form-item v-for="item in searchHandle" :key="item.label" >
                    <el-button v-if="!item.btnType || item.btnType == 'button'"
                    :icon="item.icon"
                    circle
                    :size="item.size"
                    :type="item.type"
                    :title="item.label"
                    @click='item.handle()' 
                    ></el-button>
                    <el-dropdown v-if="item.btnType == 'dropDown'" class="white font" trigger="hover" placement="bottom" size="small">
                      <el-button :type="item.type" :icon="item.icon" circle size="mini"></el-button>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="(op,index) in item.downs" :key="index" @click.native='op.handle()' >{{op.label}}</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                </el-form-item>
                 <el-form-item v-show="isDrill == 1">
                <el-button 
                  type="primary"
                  icon="el-icon-back"
                  circle
                  @click="back"
                  size="mini"
                  title="返回上级报表"
                ></el-button>
              </el-form-item>
                <el-form-item>
                    <el-button
                    type="primary"
                    :icon="icon"
                    circle
                    @click="showSearchClick"
                    size="mini"
                    :title="text"
                     class="showSearchBtn"
                    ></el-button>
                </el-form-item>
          </div>
        </div>
      </div>
      <div v-show="!isShowSearch" style="display: flex; width: 100%">
        <div style="width: 100%; height: 100%;">
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    formsReport: {
      type: Boolean,
      default: false,
    },
    inline: {
      type: Boolean,
      default: true,
    },
    itemClass: {
      type: String,
      default: "form_input",
    },
    isHandle: {
      type: Boolean,
      default: true,
    },
    labelWidth: {
      type: String,
      default: "auto",
    },
    size: {
      type: String,
      default: "mini",
    },
    reportForm: {
      type: Array,
      default: () => [],
    },
    searchHandle: {
      type: Array,
      default: () => [],
    },
    searchData: {
      type: Object,
      default: () => ({}),
    },
    activitiName: {
      type: String,
      default: "",
    },
    showSearch: {
      type: Boolean,
      default: false,
    },
    isParamMerge: {
      type: String,
      default: "2",
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
  name: "reportForm",
  data() {
    return {
      tabFocus: this.activitiName,
      isShowSearch: this.showSearch,
      text: "收起搜索",
      icon: "el-icon-arrow-up",
    };
  },
  mounted() {
  },
  methods: {
    showSearchClick() {
      if (this.isShowSearch) {
        this.isShowSearch = false;
        this.text = "展开搜索";
        this.icon = "el-icon-arrow-down";
      } else {
        this.isShowSearch = true;
        this.text = "收起搜索";
        this.icon = "el-icon-arrow-up";
      }
      this.$emit('update:showSearch', this.isShowSearch);
      this.$nextTick(() => {
        try {
          luckysheet.resize();
        } catch (error) {
          
        }
      });
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
    getRelyOnParamys(item, data, modelData) {
      var relyOnValue = "";
      for (let index = 0; index < data.params.length; index++) {
        const element = data.params[index];
        if (element.hasOwnProperty(item.relyOnParams)) {
          relyOnValue = element[item.relyOnParams];
        }
      }
      if (relyOnValue) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {},
        };
        params.params[item.relyOnParams] = relyOnValue;
        var obj = {
          url: "/api/reportTplDataset/getRelyOnData",
          params: params,
        };
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == "200") {
            item.selectData = response.responseData;
          }
        });
      } else {
        item.selectData = [];
        modelData[item.paramCode] = null;
      }
    },
    //返回上级报表
    back(){
      this.$parent.back();
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
  },
};
</script>
<style lang="scss" scoped>
@import "@/element-variables.scss";

span {
  padding: 0px 20px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 19px;
  line-height: 30px;
  color: #45c5a9;
  font-weight: bold;
  margin: 5px 0;
}
::v-deep .el-button--primary {
    color: #FFFFFF;
    background-color: $--color-primary;
    border-color: $--color-primary;
}
::v-deep .el-form-item{
    margin-bottom:10px !important
}
::v-deep .el-form-item__error{
  line-height: 0 !important;
  padding-top: 2px !important;
}
</style>