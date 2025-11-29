<!-- 搜索表单 -->
<template>
  <div>
    <!-- v-if="!reportForm || reportForm.length == 0" -->
    <div class="search-content df-c-b" v-if="!isScreen">
      <div class="left-warp" style="width: 200px"/>

      <div class="headerLeft df-c" style="width: 200px">
        <div
          class="tplname"
          style="
              width: 200px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            "
          :title="reportName"
        >
          {{ reportName }}
        </div>
      </div>
      <div class="right-warp df-c" style="overflow:auto">
        <div
          v-for="(item, index) in buttonSearchHandles"
          :key="item.label+index"
          class="action-item df-c"
          @click="item.handle()"
        >
          <i v-if="item.iconClass" :class="item.iconClass" />
          <!-- <component v-if="item.iconClass" :is="item.iconClass" theme="outline" size="16" fill="#d2d2d2"></component> -->
          <div style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;">{{ item.label }}</div>
        </div>

        <template v-for="(item, index) in dropDownSearchHandles">
          <el-dropdown
            v-if="item.btnType == 'dropDown'"
            :key="index"
            class="white font"
            trigger="hover"
            placement="bottom"
            
          >
            <!-- <el-button :type="item.type" :icon="item.icon" circle  /> -->
            <div class="action-item df-c">
              <i v-if="item.iconClass||item.icon" :class="item.iconClass||item.icon" />
              <div style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;">{{ item.label }}</div>
            </div>
             <template #dropdown>
            <el-dropdown-menu >
              <el-dropdown-item
                v-for="(op, index) in item.downs"
                :key="index"
                v-on:click="op.handle()"
              >{{ op.label }}</el-dropdown-item>
            </el-dropdown-menu>
             </template>
          </el-dropdown>
        </template>

        <div
          v-show="isDrill == 1"
          class="action-item df-c"
          @click="back"
        >
          <i class="el-icon-back" style="margin-right: 4px" />
          <div>返回上级报表</div>
        </div>
        <div class="headerRight df-c">
          <el-dropdown
            v-if="users.length > 0"
            class="white font"
            trigger="click"
            placement="bottom"
          >
            <span class="el-dropdown-link df-c">
              <el-avatar
                v-for="(item, index) in headerUsers"
                :key="index"
                
                :style="{
                  marginRight: '4px',
                  backgroundColor: item.color + ' !important',
                }"
                shape="circle"
                :title="item.userName"
              >
                {{ item.userName.slice(0, 1).toUpperCase() }}
              </el-avatar>
              <i class="el-icon-arrow-down el-icon--right" />
            </span>
            <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="(item, index) in users" :key="index">{{
                item.userName
              }}</el-dropdown-item>
            </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div>
      <el-form
        ref="reportFormRef"
        class="demo-form-inline"
        label-position="left"
        :model="searchData"
        :activiti-name="activitiName"
        :show-search="showSearch"
        :is-param-merge="isParamMerge"
        :inline="true"
      >
          <div style="display: flex; width: 100%; padding-left: 0px">
            <el-tabs
              v-if="reportForm.length > 1"
              :value="reportForm[0].datasetName"
              style="width: 100%"
              type="card"
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
                  v-show="item.paramHidden == 2"
                  :key="item.paramCode"
                  :class="itemClass"
                  :label="item.paramName"
                  :prop="
                    'params.' +
                      i +
                      '.' +
                      'params.' +
                      index +
                      '.' +
                      item.paramCode
                  "
                  :rules="
                    item.paramHidden == 2
                      ? filter_rules(item.paramName, item.rules)
                      : null
                  "
                >
                  <!-- 输入框 -->
                  <el-input
                    v-if="
                      (item.paramType === 'varchar' ||
                        item.paramType === 'number') &&
                        item.componentType != 'select' &&
                        item.componentType != 'mutiselect' &&
                        item.componentType != 'treeSelect' &&
                        item.componentType != 'multiTreeSelect'
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :placeholder="'请输入' + item.paramName"
                    
                    :disabled="item.disabled"
                    clearable
                  />
                  <!-- 下拉框 -->
                  <el-select
                    v-if="
                      (item.paramType === 'select' ||
                        item.componentType === 'select') &&
                        item.isRelyOnParams !== 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    
                    clearable
                    filterable
                    @focus="getSelectData(item)"
                  >
                    <el-option :label="'请选择'" value="" />
                    <el-option
                      v-for="op in item.selectData"
                      :key="op.value"
                      :label="op.name"
                      :value="op.value"
                    />
                  </el-select>
                  <el-select
                    v-if="
                      (item.paramType === 'select' ||
                        item.componentType === 'select') &&
                        item.isRelyOnParams === 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    clearable
                    filterable
                    
                    @focus="
                      getRelyOnParamys(
                        item,
                        searchData.params[i],
                        searchData.params[i].params[index]
                      )
                    "
                  >
                    <el-option :label="'请选择'" value="" />
                    <el-option
                      v-for="op in item.selectData"
                      :key="op.value"
                      :label="op.name"
                      :value="op.value"
                    />
                  </el-select>
                  <!-- 多选下拉框 -->
                  <el-select
                    v-if="
                      (item.paramType === 'mutiselect' ||
                        item.componentType === 'mutiselect')&&
                        item.isRelyOnParams !== 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    
                    :multiple="true"
                    filterable
                    @focus="getSelectData(item)"
                    clearable
                  >
                    <el-option
                      v-for="op in item.selectData"
                      :key="op.value"
                      :label="op.name"
                      :value="op.value"
                    />
                  </el-select>
                  <el-select
                    v-if="
                      (item.paramType === 'mutiselect' ||
                        item.componentType === 'mutiselect')&&
                        item.isRelyOnParams === 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    
                    :multiple="true"
                    @focus="getRelyOnParamys(
                        item,
                        searchData.params[i],
                        searchData.params[i].params[index]
                      )"
                    clearable
                    filterable
                  >
                    <el-option
                      v-for="op in item.selectData"
                      :key="op.value"
                      :label="op.name"
                      :value="op.value"
                    />
                  </el-select>
                  <!-- 日期 -->
                  <el-date-picker
                    v-if="item.paramType === 'date'"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    
                    :format="item.dateFormat"
                    :value-format="item.dateFormat"
                    :type="
                      item.dateFormat == 'yyyy-MM-dd'
                        ? 'date'
                        : item.dateFormat == 'yyyy-MM'
                          ? 'month'
                          : item.dateFormat == 'yyyy'
                            ? 'year'
                            : 'datetime'
                    "
                  />
                  <el-tree-select
                    v-if="
                      item.paramType === 'multiTreeSelect' ||
                      item.componentType === 'multiTreeSelect'
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :data="item.selectData == null ? [] : item.selectData"
                    :props="{ parent: 'pid', label: 'name', children: 'children' }"
                    show-checkbox
                    multiple
                    check-on-click-node
                    :check-strictly="item.checkStrictly == 1 ? false : true"
                    clearable
                    @focus="getTreeData(item,searchData.params[i])"
                  />
                  <el-tree-select
                    v-if="item.paramType === 'treeSelect' || item.componentType === 'treeSelect'"
                    
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :data="item.selectData == null ? [] : item.selectData"
                    :props="{ parent: 'pid', label: 'name', children: 'children' }"
                    clearable
                    :check-strictly="true"
                    @focus="getTreeData(item,searchData.params[i])"
                  />
                </el-form-item>
              </el-tab-pane>
            </el-tabs>
          </div>
            <div
              v-if="reportForm.length == 1"
              style="width:100%;padding-left:20px"
            >
              <el-form-item
                v-for="(item, index) in reportForm[0].params"
                v-show="item.paramHidden == 2"
                :key="item.paramCode"
                class="flex-form-item"
                :label="item.paramName"
                :prop="
                  'params.' + 0 + '.' + 'params.' + index + '.' + item.paramCode
                "
                :rules="
                  item.paramHidden == 2
                    ? filter_rules(item.paramName, item.rules)
                    : null
                "
                
              >
                <!-- 输入框 -->
                <el-input
                  v-if="
                    (item.paramType === 'varchar' ||
                      item.paramType === 'number') &&
                      item.componentType != 'select' &&
                      item.componentType != 'mutiselect' &&
                      item.componentType != 'treeSelect' &&
                      item.componentType != 'multiTreeSelect'
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :placeholder="'请输入' + item.paramName"
                  
                  :disabled="item.disabled"
                  clearable
                />
                <!-- 下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'select' ||
                      item.componentType === 'select') &&
                      item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  clearable
                  filterable
                  @focus="getSelectData(item)"
                >
                  <el-option :label="'请选择'" value="" />
                  <el-option
                    v-for="op in item.selectData"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <el-select
                  v-if="
                    (item.paramType === 'select' ||
                      item.componentType === 'select') &&
                      item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  clearable
                  filterable
                  
                  @focus="
                    getRelyOnParamys(
                      item,
                      searchData.params[0],
                      searchData.params[0].params[index]
                    )
                  "
                >
                  <el-option :label="'请选择'" value="" />
                  <el-option
                    v-for="op in item.selectData"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <!-- 多选下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'mutiselect' ||
                      item.componentType === 'mutiselect')
                      &&
                      item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  :multiple="true"
                  @focus="getSelectData(item)"
                  clearable
                  filterable
                >
                  <el-option
                    v-for="op in item.selectData"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <!-- 多选下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'mutiselect' ||
                      item.componentType === 'mutiselect')
                      &&
                      item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  :multiple="true"
                  @focus="getRelyOnParamys(
                      item,
                      searchData.params[0],
                      searchData.params[0].params[index]
                    )"
                  clearable
                  filterable
                >
                  <el-option
                    v-for="op in item.selectData"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <!-- 日期 -->
                <el-date-picker
                  v-if="item.paramType === 'date'"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  
                  :format="item.dateFormat"
                  :value-format="item.dateFormat"
                  :type="
                    item.dateFormat == 'yyyy-MM-dd'
                      ? 'date'
                      : item.dateFormat == 'yyyy-MM'
                        ? 'month'
                        : item.dateFormat == 'yyyy'
                          ? 'year'
                          : 'datetime'
                  "
                />
                <el-tree-select
                  v-if="
                    item.paramType === 'multiTreeSelect' || item.componentType === 'multiTreeSelect'
                  "
                  
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :data="item.selectData == null ? [] : item.selectData"
                  :props="{ parent: 'pid', label: 'name', children: 'children' }"
                  show-checkbox
                  multiple
                  check-on-click-node
                  :check-strictly="item.checkStrictly == 1 ? false : true"
                  clearable
                  @focus="getTreeData(item,searchData.params[0])"
                />
                <el-tree-select
                  v-if="item.paramType === 'treeSelect' || item.componentType === 'treeSelect'"
                  
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :data="item.selectData == null ? [] : item.selectData"
                  :props="{ parent: 'pid', label: 'name', children: 'children' }"
                  clearable
                  :check-strictly="true"
                  @focus="getTreeData(item,searchData.params[0])"
                />
              </el-form-item>
        </div>
        <div v-show="!isShowSearch" style="display: flex; width: 100%">
          <div style="width: 100%; height: 100%" />
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReportForm',
  props: {
    isScreen: {
      type: Boolean,
      default: false
    },
    formsReport: {
      type: Boolean,
      default: false
    },
    inline: {
      type: Boolean,
      default: true
    },
    itemClass: {
      type: String,
      default: 'form_input'
    },
    isHandle: {
      type: Boolean,
      default: true
    },
    labelWidth: {
      type: String,
      default: 'auto'
    },
    size: {
      type: String,
      default: 'mini'
    },
    reportForm: {
      type: Array,
      default: () => []
    },
    searchHandle: {
      type: Array,
      default: () => []
    },
    searchData: {
      type: Object,
      default: () => ({})
    },
    activitiName: {
      type: String,
      default: ''
    },
    showSearch: {
      type: Boolean,
      default: false
    },
    isParamMerge: {
      type: String,
      default: '2'
    },
    reportName: {
      type: String,
      default: ''
    },
    isDrill: {
      type: Number,
      default: 2
    },
    users: {
      type: Array,
      default: () => []
    },
    headerUsers: {
      type: Array,
      default: () => []
    },
    drawer: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      tabFocus: this.activitiName,
      isShowSearch: this.showSearch,
      text: '收起搜索',
      icon: 'el-icon-arrow-up',
      // drawer: false
    }
  },
  computed: {
    // 按钮
    buttonSearchHandles() {
      return this.searchHandle.filter(
        (item) => (!item.btnType || item.btnType === 'button') && !item.drawerBtn
      )
    },
    // 下拉菜单
    dropDownSearchHandles() {
      return this.searchHandle.filter((item) => item.btnType === 'dropDown' && !item.drawerBtn)
    },
    // 现在在抽屉中的按钮
    drawerHandles() {
      return this.searchHandle.filter((item) => item.drawerBtn)
    }
  },
  methods: {
    handleAction(item) {
      item.handle()
      setTimeout(() => {
        // this.drawer = false
      }, 100)
    },
    showSearchClick() {
      if (this.isShowSearch) {
        this.isShowSearch = false
        this.text = '展开搜索'
        this.icon = 'el-icon-arrow-down'
      } else {
        this.isShowSearch = true
        this.text = '收起搜索'
        this.icon = 'el-icon-arrow-up'
      }
      this.$emit('update:showSearch', this.isShowSearch)
      this.$nextTick(() => {
        try {
          luckysheet.resize()
        } catch (error) {}
      })
    },
    getSelectData(item) {
      if (!item.init) {
        if (item.selectType == '1') {
          item.selectData = JSON.parse(item.selectContent)
        } else {
          var params = {
            selectContent: item.selectContent,
            datasourceId: item.datasourceId,
            params: {}
          }
          var obj = {
            url: '/api/reportTplDataset/getSelectData',
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
    getRelyOnParamys(item, data, modelData) {
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
      if (relyOnValue) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {}
        }
        for(var key in relyOnValue) {
          params.params[key] = relyOnValue[key]
        }
        var obj = {
          url: '/api/reportTplDataset/getRelyOnData',
          params: params
        }
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == '200') {
            item.selectData = response.responseData
          }
        })
      } else {
        item.selectData = []
        modelData[item.paramCode] = null
      }
    },
    // 返回上级报表
    back() {
      this.$parent.back()
    },
    searchClick(){
      this.$parent.searchClick()
    },
    closeSearch(){
      this.$parent.closeSearch()
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
  }
}
</script>
<style lang="scss" scoped>
@import "@/element-variables.scss";
.search-content {
  height: 32px;
  line-height: 32px;
  background-color: #fff;
  padding: 15px 10px 9px 20px;
  .action-item {
    height: 32px;
    line-height: 32px;
    padding: 0 16px;
    color: #595959;
    font-size: 14px;
    font-weight: 400;
    transition: all 0.3s;
    border-radius: 6px;
    border: 1px solid rgba(0, 0, 0, 0.10);
    background: #FFF;
    margin-left: 9px;
    box-sizing: border-box;
    &:hover {
      cursor: pointer;
      border-color: $--color-primary;
      opacity: 0.7;
    }
  }
  .action-item-del {
    color: #ff4d4f;
  }
}
.headerRight{
  margin-left: 10px;
}

::v-deep .el-button--primary {
  color: #ffffff;
  background-color: $--color-primary;
  border-color: $--color-primary;
}
::v-deep .el-form-item {
  margin-bottom: 10px !important;
}
::v-deep .el-form-item__error {
  line-height: 0 !important;
  padding-top: 2px !important;
}
::v-deep .el-tabs__content{
  padding: 0 10px;
}
::v-deep .el-tabs__header{
  margin-bottom: 24px;
}
::v-deep .el-tabs__item{
  height:46px;
}
::v-deep .el-tabs__nav-wrap::after{
  content: unset !important;
}
::v-deep .el-tabs__active-bar{
  height: 3px;
  // max-width: 80px;
  border-radius: 1px;
}
.tplname {
  padding: 0px 16px;
  font-size: 16px;
  line-height: 30px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: bold;
  margin: 5px 0;
  position: absolute;
  left:10px
}


</style>
