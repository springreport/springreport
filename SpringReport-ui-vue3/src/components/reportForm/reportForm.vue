<!-- 搜索表单 -->
<template>
  <div>
    <div class="search-content df-c-b">
      <div class="left-warp" />

      <div class="headerLeft df-c" style="width: 30%">
        <div
          class="tplname"
          style="width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap"
          :title="reportName"
        >
          {{ reportName }}
        </div>
      </div>
      <div class="right-warp df-c">
        <div v-if="searchHandle.length" class="action-item df-c" @click="searchClick">
          <icon-search style="margin-right: 4px" fill="#999" />
          <div>查询</div>
        </div>
        <div
          v-for="(item, index) in buttonSearchHandles"
          :key="item.label + index"
          class="action-item df-c"
          @click="item.handle()"
        >
          <template v-if="item.iconClass || item.icon">
            <component
              v-if="(item.iconClass || item.icon).startsWith('icon-')"
              :is="item.iconClass || item.icon"
            ></component>
            <i v-else :class="item.iconClass || item.icon" />
          </template>
          <div>{{ item.label }}</div>
        </div>

        <template v-for="(item, index) in dropDownSearchHandles">
          <el-dropdown
            v-if="item.btnType == 'dropDown'"
            :key="index"
            class="white font"
            trigger="hover"
            placement="bottom"
          >
            <!-- <el-button :type="item.type" :icon="item.icon" circle size="mini" /> -->
            <div class="action-item df-c">
              <i v-if="item.iconClass || item.icon" :class="item.iconClass || item.icon" />
              <div>{{ item.label }}</div>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  v-for="(op, index) in item.downs"
                  :key="index"
                  v-on:click="op.handle()"
                  >{{ op.label }}</el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>

        <div v-show="isDrill == 1" class="action-item df-c" @click="back">
          <icon-return theme="outline" size="16" fill="#595959" style="margin-right: 4px"/>
          <div >返回上级报表</div>
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
                :size=30
                :style="{
                  marginRight: '4px',
                  backgroundColor: item.color + ' !important',
                }"
                shape="circle"
                :title="item.userName"
              >
                {{ item.userName.slice(0, 1).toUpperCase() }}
              </el-avatar>
               <icon-down theme="outline" size="16" />
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
    <el-drawer
      title="添加筛选项"
      :model-value="drawer"
      :close-on-press-escape="false"
      :wrapper-closable="false"
      direction="rtl"
      size="36%"
      custom-class="search-drawer"
      class="search-drawer"
      @close="closeSearch"
    >
      <el-form
        label-position="top"
        ref="reportFormRef"
        class="demo-form-inline"
        :model="searchData"
        :activitiName="activitiName"
        :showSearch="showSearch"
        :isParamMerge="isParamMerge"
      >
        <div v-show="isShowSearch" style="display: flex; width: 100%">
          <!-- <div style="display: flex; width: 15%">
          <div style="width: 100%; height: 100%; padding-top:18px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
            <span :title="reportName">
              {{reportName}}
            </span>
          </div>
          </div> -->
          <div style="display: flex; width: 100%; padding-left: 0px">
            <el-tabs v-model="tabFocus" v-if="reportForm.length > 1" style="width: 100%">
              <el-tab-pane
                v-for="(data, i) in reportForm"
                :key="data.datasetName"
                :label="isParamMerge == '1' ? '报表参数' : '数据集【' + data.datasetName + '】参数'"
                :name="data.datasetName"
              >
                <el-form-item
                  v-for="(item, index) in data.params"
                  :class="itemClass"
                  :label="item.paramName"
                  :key="item.paramCode"
                  :prop="'params.' + i + '.' + 'params.' + index + '.' + item.paramCode"
                  v-show="item.paramHidden == 2"
                  :rules="item.paramHidden == 2 ? filter_rules(item.paramName, item.rules) : null"
                >
                  <!-- 输入框 -->
                  <el-input
                    v-if="
                      (item.paramType === 'varchar' || item.paramType === 'number') &&
                      item.componentType != 'select' &&
                      item.componentType != 'mutiselect' &&
                      item.componentType != 'treeSelect' &&
                      item.componentType != 'multiTreeSelect'
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :placeholder="'请输入' + item.paramName"
                    :size="item.size"
                    :disabled="item.disabled"
                  ></el-input>
                  <!-- 下拉框 -->
                  <el-select
                    v-if="
                      (item.paramType === 'select' || item.componentType === 'select') &&
                      item.isRelyOnParams !== 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :size="item.size"
                    clearable
                    @focus="getSelectData(item)"
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
                      (item.paramType === 'select' || item.componentType === 'select') &&
                      item.isRelyOnParams === 1
                    "
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :size="item.size"
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
                    v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect') &&
                        item.isRelyOnParams !== 1"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :size="item.size"
                    :multiple="true"
                    @focus="getSelectData(item)"
                    clearable
                  >
                    <el-option
                      v-for="op in item.selectData"
                      :label="op.name"
                      :value="op.value"
                      :key="op.value"
                    ></el-option>
                  </el-select>
                  <el-select
                    v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect') &&
                        item.isRelyOnParams === 1"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :size="item.size"
                    :multiple="true"
                    @focus="getRelyOnParamys(
                        item,
                        searchData.params[i],
                        searchData.params[i].params[index])"
                    clearable
                  >
                    <el-option
                      v-for="op in item.selectData"
                      :label="op.name"
                      :value="op.value"
                      :key="op.value"
                    ></el-option>
                  </el-select>
                  <!-- 日期 -->
                  <!-- <el-date-picker v-if="item.paramType==='date'" type="date" v-model="searchData.params[i].params[index][item.paramCode]" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker> -->
                  <el-date-picker
                    v-if="item.paramType === 'date'"
                    :size="item.size"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :format="
                      item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                        ? 'YYYY-MM-DD'
                        : item.dateFormat == 'yyyy-MM-dd HH:mm:ss'
                        ? 'YYYY-MM-DD HH:mm:ss'
                        : item.dateFormat
                    "
                    :value-format="
                      item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                        ? 'YYYY-MM-DD'
                        : item.dateFormat == 'yyyy-MM-dd HH:mm:ss'
                        ? 'YYYY-MM-DD HH:mm:ss'
                        : item.dateFormat
                    "
                    :type="
                      item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                        ? 'date'
                        : item.dateFormat == 'YYYY-MM'
                        ? 'month'
                        : item.dateFormat == 'YYYY'
                        ? 'year'
                        : 'datetime'
                    "
                  ></el-date-picker>
                  <el-tree-select
                    v-if="
                      item.paramType === 'multiTreeSelect' ||
                      item.componentType === 'multiTreeSelect'
                    "
                    :size="item.size"
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
                    :size="item.size"
                    v-model="searchData.params[i].params[index][item.paramCode]"
                    :data="item.selectData == null ? [] : item.selectData"
                    :props="{ parent: 'pid', label: 'name', children: 'children' }"
                    clearable
                    :check-strictly="true"
                    @focus="getTreeData(item,searchData.params[i])"
                  />
                </el-form-item>
                <el-form-item>
                  <div
                    class="flex"
                    :style="{ 'text-align': inline ? 'center' : 'inherit', display: '-webkit-box' }"
                  >
                    <!-- <el-button v-for='item in searchHandle' :key="item.label" :type="item.type"  @click='item.handle()' :icon="item.icon" :size="item.size" :title="item.label" circle ></el-button> -->
                    <el-button
                      v-show="isDrill == 1"
                      style="margin-left: 10px"
                      type="primary"
                      icon="icon-back"
                      @click="back"
                      size="small"
                      circle
                      title="返回上级报表"
                    ></el-button>
                    <!-- <el-button
                      style="margin-left: 10px"
                      type="primary"
                      :icon="icon"
                      @click="showSearchClick"
                      size="small"
                      circle
                      :title="text"
                      class="showSearchBtn"
                    ></el-button> -->
                  </div>
                </el-form-item>
              </el-tab-pane>
            </el-tabs>
            <div v-if="reportForm.length == 1" style="width: 100%">
              <el-form-item
                v-for="(item, index) in reportForm[0].params"
                :class="itemClass"
                :label="item.paramName"
                :key="item.paramCode"
                :prop="'params.' + 0 + '.' + 'params.' + index + '.' + item.paramCode"
                :rules="item.paramHidden == 2 ? filter_rules(item.paramName, item.rules) : null"
                v-show="item.paramHidden == 2"
              >
                <!-- 输入框 -->
                <el-input
                  v-if="
                    (item.paramType === 'varchar' || item.paramType === 'number') &&
                    item.componentType != 'select' &&
                    item.componentType != 'mutiselect' &&
                    item.componentType != 'treeSelect' &&
                    item.componentType != 'multiTreeSelect'
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :placeholder="'请输入' + item.paramName"
                  :size="item.size"
                  :disabled="item.disabled"
                ></el-input>
                <!-- 下拉框 -->
                <el-select
                  v-if="
                    (item.paramType === 'select' || item.componentType === 'select') &&
                    item.isRelyOnParams !== 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  clearable
                  :size="item.size"
                  @focus="getSelectData(item)"
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
                    (item.paramType === 'select' || item.componentType === 'select') &&
                    item.isRelyOnParams === 1
                  "
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :size="item.size"
                  @focus="
                    getRelyOnParamys(item, searchData.params[0], searchData.params[0].params[index])
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
                  v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect') &&
                      item.isRelyOnParams !== 1"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :size="item.size"
                  :multiple="true"
                  @focus="getSelectData(item)"
                >
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <el-select
                  v-if="(item.paramType === 'mutiselect' || item.componentType === 'mutiselect') &&
                      item.isRelyOnParams === 1"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :size="item.size"
                  :multiple="true"
                  @focus="getRelyOnParamys(
                      item,
                      searchData.params[0],
                      searchData.params[0].params[index]
                    )"
                >
                  <el-option
                    v-for="op in item.selectData"
                    :label="op.name"
                    :value="op.value"
                    :key="op.value"
                  ></el-option>
                </el-select>
                <!-- 日期 -->
                <!-- <el-date-picker v-if="item.paramType==='date'" v-model="searchData.params[0].params[index][item.paramCode]" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker> -->
                <el-date-picker
                  v-if="item.paramType === 'date'"
                  :size="item.size"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :format="
                    item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                      ? 'YYYY-MM-DD'
                      : item.dateFormat == 'yyyy-MM-dd HH:mm:ss'
                      ? 'YYYY-MM-DD HH:mm:ss'
                      : item.dateFormat
                  "
                  :value-format="
                    item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                      ? 'YYYY-MM-DD'
                      : item.dateFormat == 'yyyy-MM-dd HH:mm:ss'
                      ? 'YYYY-MM-DD HH:mm:ss'
                      : item.dateFormat
                  "
                  :type="
                    item.dateFormat == 'YYYY-MM-DD' || item.dateFormat == 'yyyy-MM-dd'
                      ? 'date'
                      : item.dateFormat == 'YYYY-MM'
                      ? 'month'
                      : item.dateFormat == 'YYYY'
                      ? 'year'
                      : 'datetime'
                  "
                ></el-date-picker>
                <el-tree-select
                  v-if="
                    item.paramType === 'multiTreeSelect' || item.componentType === 'multiTreeSelect'
                  "
                  :size="item.size"
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
                  :size="item.size"
                  v-model="searchData.params[0].params[index][item.paramCode]"
                  :data="item.selectData == null ? [] : item.selectData"
                  :props="{ parent: 'pid', label: 'name', children: 'children' }"
                  clearable
                  :check-strictly="true"
                  @focus="getTreeData(item,searchData.params[0])"
                />
              </el-form-item>
            </div>
            <div v-if="!reportForm || reportForm.length == 0" style="width: 100%">
              <el-form-item>
                <div
                  class="flex"
                  :style="{ 'text-align': inline ? 'center' : 'inherit', display: '-webkit-box' }"
                >
                  <div v-for="item in searchHandle" :key="item.label" style="margin-left: 10px">
                    <el-button
                      v-if="!item.btnType || item.btnType == 'button'"
                      :type="item.type"
                      @click="item.handle()"
                      :icon="item.icon"
                      :size="item.size"
                      :title="item.label"
                      circle
                    ></el-button>
                    <el-dropdown
                      v-if="item.btnType == 'dropDown'"
                      class="white font"
                      style="margin-top: 5px"
                      trigger="hover"
                      placement="bottom"
                    >
                      <el-button
                        :type="item.type"
                        :icon="item.icon"
                        circle
                        :size="item.size"
                      ></el-button>
                      <template #dropdown>
                        <el-dropdown-menu>
                          <el-dropdown-item
                            v-for="(op, index) in item.downs"
                            :key="index"
                            @click="op.handle()"
                            >{{ op.label }}</el-dropdown-item
                          >
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                  </div>
                  <el-button
                    v-show="isDrill == 1"
                    style="margin-left: 10px"
                    type="primary"
                    icon="icon-back"
                    @click="back"
                    size="small"
                    circle
                    title="返回上级报表"
                  ></el-button>
                  <el-button
                    style="margin-left: 10px"
                    type="primary"
                    :icon="icon"
                    @click="showSearchClick"
                    size="small"
                    circle
                    :title="text"
                    class="showSearchBtn"
                  ></el-button>
                </div>
              </el-form-item>
            </div>
          </div>
        </div>
        <div v-show="!isShowSearch" style="display: flex; width: 100%">
          <div style="width: 100%; height: 100%" />
        </div>
      </el-form>
      <div class="search-drawer__footer">
        <el-button
          v-for="item in drawerHandles"
          :key="item.label"
          :type="item.type"
          @click="handleAction(item)"
          >{{ item.label }}</el-button
        >
      </div>
    </el-drawer>
  </div>
</template>

<script>
  export default {
    props: {
      inline: {
        type: Boolean,
        default: true,
      },
      itemClass: {
        type: String,
        default: 'form_input',
      },
      isHandle: {
        type: Boolean,
        default: true,
      },
      labelWidth: {
        type: String,
        default: 'auto',
      },
      size: {
        type: String,
        default: 'default',
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
        default: '',
      },
      showSearch: {
        type: Boolean,
        default: false,
      },
      isParamMerge: {
        type: String,
        default: '2',
      },
      reportName: {
        type: String,
        default: '',
      },
      isDrill: {
        type: Number,
        default: 2,
      },
      users: {
        type: Array,
        default: () => [],
      },
      headerUsers: {
        type: Array,
        default: () => [],
      },
      drawer: {
        type: Boolean,
        default: false,
      },
    },
    name: 'reportForm',
    data() {
      return {
        tabFocus: this.activitiName,
        isShowSearch: this.showSearch,
        text: '收起搜索',
        icon: 'icon-up',
      };
    },
    computed: {
      // 按钮
      buttonSearchHandles() {
        return this.searchHandle.filter(
          (item) => (!item.btnType || item.btnType === 'button') && !item.drawerBtn
        );
      },
      // 下拉菜单
      dropDownSearchHandles() {
        return this.searchHandle.filter((item) => item.btnType === 'dropDown' && !item.drawerBtn);
      },
      // 现在在抽屉中的按钮
      drawerHandles() {
        return this.searchHandle.filter((item) => item.drawerBtn);
      },
    },
    mounted() {},
    methods: {
      handleAction(item) {
        item.handle();
        setTimeout(() => {
          // this.drawer = false
        }, 100);
      },
      showSearchClick() {
        if (this.isShowSearch) {
          this.isShowSearch = false;
          this.text = '展开搜索';
          this.icon = 'icon-down';
        } else {
          this.isShowSearch = true;
          this.text = '收起搜索';
          this.icon = 'icon-up';
        }
        this.$emit('update:showSearch', this.isShowSearch);
        this.$nextTick(() => {
          try {
            luckysheet.resize();
          } catch (error) {}
        });
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
            params: {},
          };
          for(var key in relyOnValue) {
            params.params[key] = relyOnValue[key]
          }
          var obj = {
            url: '/api/reportTplDataset/getRelyOnData',
            params: params,
          };
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              item.selectData = response.responseData;
            }
          });
        } else {
          item.selectData = [];
          modelData[item.paramCode] = null;
        }
      },
      //返回上级报表
      back() {
        this.$emit('back');
      },
      searchClick() {
        this.$emit('searchClick');
      },
      closeSearch() {
        this.$emit('closeSearch');
      },
      getSelectData(item) {
        if (!item.init) {
          if (item.selectType == '1') {
            item.selectData = JSON.parse(item.selectContent);
          } else {
            var params = {
              selectContent: item.selectContent,
              datasourceId: item.datasourceId,
              params: {},
            };
            var obj = {
              url: '/api/reportTplDataset/getSelectData',
              params: params,
            };
            this.commonUtil.doPost(obj).then((response) => {
              if (response.code == '200') {
                item.selectData = response.responseData;
                if (response.responseData && response.responseData.length > 0) {
                  item.init = true;
                }
              }
            });
          }
        }
      },
      getTreeData(item,data) {
        if (!item.init || item.isRelyOnParams == 1) {
          var params = {
            selectContent: item.selectContent,
            datasourceId: item.datasourceId,
            params: {},
          };
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
            params: params,
          };
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              item.selectData = response.responseData;
              if (response.responseData && response.responseData.length > 0) {
                item.init = true;
              }
            }
          });
        }
      },
    },
  };
</script>
<style lang="scss" scoped>
  :deep(.el-tabs__nav-next) {
    line-height: 50px !important;
  }
  :deep(.el-tabs__nav-prev) {
    line-height: 50px !important;
  }
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
      border: 1px solid rgba(0, 0, 0, 0.1);
      background: #fff;
      margin-left: 9px;
      box-sizing: border-box;
      &:hover {
        cursor: pointer;
        border-color: $base-color-primary;
        opacity: 0.7;
      }
    }
    .action-item-del {
      color: #ff4d4f;
    }
  }
  .headerRight {
    margin-left: 10px;
  }

  :deep(.el-button--primary) {
    color: #ffffff;
    background-color: $base-color-primary;
    border-color: $base-color-primary;
  }
  // :deep(.el-form-item) {
  //   margin-bottom: 10px !important;
  // }
  :deep(.el-form-item__error) {
    line-height: 0 !important;
    padding-top: 8px !important;
  }
  :deep(.el-tabs__content) {
    padding: 0 10px;
  }
  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }
  :deep(.el-tabs__item) {
    height: 46px;
  }
  :deep(.el-tabs__nav-wrap::after) {
    content: unset !important;
  }
  :deep(.el-tabs__active-bar) {
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
    left: 10px;
  }
</style>
