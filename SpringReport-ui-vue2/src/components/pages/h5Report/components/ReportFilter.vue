<template>
  <div class="ReportFilter">
    <el-drawer
      title="查询"
      :visible.sync="drawer"
      :size="size"
      custom-class="report-filter-drawer"
      :modal="modal"
      ref="drawer"
    >
        <el-form
          :model="searchData"
          label-position="top"
          label-width="80px"
          class="form"
          ref="ruleForm"
        >
          <el-form-item
            v-for="(item, index) in info.params"
            :label="item.paramName"
            :key="item.paramCode"
            :prop="'params.' + index + '.' + item.paramCode"
            :rules="item.paramHidden == 2?filter_rules(item.paramName, item.rules):{}"
             v-show="item.paramHidden == 2"
          >
            <!-- 输入框 -->
            <el-input
              v-if="(item.paramType === 'varchar' || item.paramType === 'number') && (item.componentType != 'select' && item.componentType != 'mutiselect' && item.componentType != 'treeSelect' && item.componentType != 'multiTreeSelect')"
              v-model="searchData.params[index][item.paramCode]"
              :placeholder="'请输入' + item.paramName"
              size="mini"
              :disabled="item.disabled"
              clearable
            ></el-input>
            <!-- 下拉框 -->
            <el-select
              v-if="(item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams !== 1"
              v-model="searchData.params[index][item.paramCode]"
              size="mini"
              clearable
              style="width: 100%"
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
              v-if="(item.paramType === 'select' || item.componentType === 'select') && item.isRelyOnParams === 1"
              v-model="searchData.params[index][item.paramCode]"
              clearable
              size="mini"
              style="width: 100%"
               @focus="
                getRelyOnParamys(
                  item,
                  searchData,
                  searchData.params[index]
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
            <!-- 多选下拉框 -->
            <el-select
              v-if="(item.paramType === 'mutiselect'  || item.componentType === 'mutiselect')"
              v-model="searchData.params[index][item.paramCode]"
              size="mini"
              :multiple="true"
              style="width: 100%"
              @focus="getSelectData(item)"
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
              size="mini"
              style="width: 100%"
              v-model="searchData.params[index][item.paramCode]"
              :format="item.dateFormat"
              :value-format="item.dateFormat"
              :type="item.dateFormat == 'yyyy-MM-dd'?'date':item.dateFormat == 'yyyy-MM'?'month':item.dateFormat == 'yyyy'?'year':'datetime'"
            ></el-date-picker>
            <multiselectNode v-if="item.paramType==='multiTreeSelect' || item.componentType==='multiTreeSelect'"  :ref="searchData.params[index][item.paramCode]" 
                v-model="searchData.params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :lazy="false" 
                :item="item"
                :data="searchData"
                :focusMethod="getTreeData" :checkStrictly="item.checkStrictly==1?false:true">
                  </multiselectNode>
                <selectNode v-if="item.paramType==='treeSelect' || item.componentType==='treeSelect'"  :ref="searchData.params[index][item.paramCode]" 
                v-model="searchData.params[index][item.paramCode]" 
                :props="{ parent: 'pid', value: 'id',label: 'name',children: 'children'}" 
                :options="item.selectData" :lazy="false"  :clearable="true" size="mini"
                :data="searchData"
                :item="item"
                :focusMethod="getTreeData">
              </selectNode>
          </el-form-item>
        </el-form>
        <div class="drawer__footer df-c">
          <el-button @click="resetForm()">重置</el-button>
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
    </el-drawer>
  </div>
</template>
<script>
export default {
  name: "ReportFilter",
  data() {
    return {
      drawer: false,
      size:"0%",
      modal:false
      // searchData: {}, //参数
    };
  },
  props: ["info","searchData"],
  components: {},
  watch: {},
  mounted() {
    this.drawer = true;
    var that = this;
    this.$nextTick(() => {
      that.drawer = false;
      setTimeout(function(){
        that.size = '90%';
        that.modal = true;
      }, 400)
      
    });
  },
  methods: {
    openDrawer() {
      this.drawer = true;
    },
    resetForm() {
      this.$refs["ruleForm"].resetFields();
    },
    search() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.drawer = false;
          this.$emit("search", this.searchData);
          //this.$parent.search()
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
      var relyOnValue = null;
      for (let index = 0; index < data.params.length; index++) {
        const element = data.params[index];
        let relyOnParams = item.relyOnParams.split(",");
        for (let t = 0; t < relyOnParams.length; t++) {
          if (relyOnParams[t] && element.hasOwnProperty(relyOnParams[t])) {
            if(relyOnValue == null){
              relyOnValue = {};
            }
            relyOnValue[relyOnParams[t]] = element[relyOnParams[t]]
          }
        }
      }
      if (relyOnValue) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {},
        };
        for(var key in relyOnValue) {
          params.params[key] = relyOnValue[key]
        }
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
    getTreeData(item,data) {
      if (!item.init || item.isRelyOnParams == 1) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {}
        }
        if(item.isRelyOnParams == 1){
          var relyOnValue = null;
          for (let index = 0; index < data.params.length; index++) {
            const element = data.params[index]
            let relyOnParams = item.relyOnParams.split(",");
            for (let t = 0; t < relyOnParams.length; t++) {
              if (relyOnParams[t] && element.hasOwnProperty(relyOnParams[t])) {
                if(relyOnValue == null){
                  relyOnValue = {};
                }
                relyOnValue[relyOnParams[t]] = element[relyOnParams[t]]
              }
            }
          }
          for(var key in relyOnValue) {
            params.params[key] = relyOnValue[key]
          }
        }
        var obj = {
          url: '/api/reportTplDataset/getTreeSelectData',
          params: params
        }
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == '200') {
            item.selectData = response.responseData
            if (response.responseData && response.responseData.length > 0) {
              item.init = true
            }
          }
        })
      }
    }
  },
};
</script>

<style scoped lang="scss">
.form {
  padding: 0 16px;
  background-color: #fff;
  padding-bottom: 64px;
}
::v-deep .el-form-item {
  margin-bottom: 0;
}
::v-deep .el-form-item__label {
  padding: 0;
  font-weight: bold;
  font-size: 12px;
  height: 14px;
  line-height: 14px;
  margin-bottom: 6px;
  margin-top: 12px;
}
::v-deep .el-drawer__body {
  background-color: #f9f9f9;
}
</style>
<style lang="scss">
.report-filter-drawer {
  .el-drawer__header {
    padding: 16px 16px 4px;
    margin-bottom: 0;
    color: #1a1a1a;
    font-family: PingFang SC;
    font-size: 16px;
    font-style: normal;
    font-weight: 500;
    line-height: 22px;
  }
  .drawer__footer {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 12px 16px;
    box-sizing: border-box;
    background-color: #fff;
    .el-button {
      flex: 1;
      &:nth-child(2) {
        margin-left: 16px;
      }
    }
  }
}
</style>
