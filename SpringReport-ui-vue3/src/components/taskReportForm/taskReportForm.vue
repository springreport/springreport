<!-- 搜索表单 -->
<template>
  <div class="el-dialog-div">
    <el-form
      label-position="top"
      :label-width="labelWidth"
      ref="reportFormRef"
      class="demo-form-inline"
      :model="searchData"
      :activitiName="activitiName"
      :showSearch="showSearch"
      :isParamMerge="isParamMerge"
    >
      <div>
        <div>
          <div v-if="reportForm.length==1" style="width:100%;">
                <el-form-item
                v-for="(item, index) in reportForm[0].params"
                :class="itemClass"
                :label="item.paramName"
                :key="item.paramCode"
                :prop="
                  'params.' + 0 + '.' + 'params.' + index + '.' + item.paramCode
                "
                :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):{}"
                >
                <!-- 输入框 -->
                <el-input
                  v-if="
                    item.paramType === 'varchar' || item.paramType === 'number'
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :placeholder="'请输入' + item.paramName"
                  
                  :disabled="item.disabled"
                  clearable
                ></el-input>
                <!-- 下拉框 -->
                <el-select
                  v-if="
                    item.paramType === 'select' && item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  clearable
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
                    item.paramType === 'select' && item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  clearable
                  
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
                  v-if="item.paramType === 'mutiselect'"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  :multiple="true"
                >
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
                  
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':item.dateFormat"
                  :value-format="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'YYYY-MM-DD':item.dateFormat"
                  :type="(item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd')?'date':item.dateFormat == 'YYYY-MM'?'month':'datetime'"
                ></el-date-picker>
                <div class="sub-title">是否使用默认值
                  <el-switch
                    v-model="searchData.params[0].params[index].isDefault" @change="changeDefault(searchData.params[0].params[index])">
                  </el-switch>
                </div>
                </el-form-item>
          </div>
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
      default: "default",
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
      value1:false
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
      this.$nextTick(() => {
        luckysheet.resize();
      });
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
    changeDefault(obj){
      var paramCode = obj.paramCode
      var isDefault = obj.isDefault
      if(isDefault)
      {
        obj[paramCode] = obj.paramDefault
      }
    }
  },
};
</script>
<style lang="scss" scoped>
span {
  padding: 0px 20px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 19px;
  line-height: 30px;
  color: #17b794;
  font-weight: bold;
  margin: 5px 0;
}
:deep(.el-button--primary) {
    color: #FFFFFF;
    background-color: #17b794;
    border-color: #17b794
}
</style>