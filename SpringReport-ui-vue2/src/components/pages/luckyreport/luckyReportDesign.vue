<template>
  <div
    v-loading="loading"
    :element-loading-text="loadingText"
    style="
      height: 100%;
      display: flex;
      flex-direction: column;
      background-color: #f0f2f5;
    "
  >
    <div style="width: 100%; flex: none" class="header-box">
      <el-header class="_header df-c-b">
        <div class="headerLeft df-c" style="width: 30%">
          <div
            class="tplname"
            style="
              width: 100%;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            "
            :title="tplName"
          >
            {{ tplName }}
          </div>
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
                size="small"
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
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(item, index) in users" :key="index">{{
                item.userName
              }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
    </div>
    <!-- <div style="flex: 1;height:100vh;display:flex"> -->
    <div
      :style="{
        flex: 1,
        display: 'flex',
        height: designHeight + 'px',
      }"
    >
      <div v-show="leftOpen" class="left">
        <div class="left-dataset-title">
          <span class="dataset-title">数据集管理</span>
          <el-button
            v-has="'reportDesign_addDataSet'"
            type="primary"
            size="small"
            plain
            class="add-dataset"
            @click="addDataSets"
          ><i class="el-icon-plus el-icon--left" />添加数据集</el-button>
        </div>
        <div class="left-dataset-content df">
          <div class="dataset-group">
            <div class="section-name df-c">
              <span style="margin-right: 8px">报表</span>
              <div class="set-group df-c" @click="groupSetVisible = true">
                <img
                  src="@/static/img/sheet/setting.png"
                  width="12px"
                  height="12px"
                >
                <div class="setting-text">分组设置</div>
              </div>
            </div>
            <el-input
              v-model="datasetKeyword"
              prefix-icon="el-icon-search"
              placeholder="报表搜索"
              class="search"
              clearable
            />
            <div
              v-loading="dataGroupLoading"
              class="group-list cus-scrollbar"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
            >
              <el-collapse v-if="displayGroupList && displayGroupList.length">
                <el-collapse-item
                  v-for="groupItem in displayGroupList"
                  :key="groupItem.id"
                  :title="groupItem.groupName"
                  :name="groupItem.id"
                >
                  <template v-if="groupItem.data.length">
                    <div
                      v-for="datasetItem in groupItem.data"
                      :key="datasetItem.id"
                      class="dataset-item df-c-b"
                      :class="
                        datasetItemActive == datasetItem.id
                          ? 'dataset-item-active'
                          : ''
                      "
                      @click="clickDatasets(datasetItem)"
                    >
                      <div
                        class="set-name overflow-text"
                        style="flex: 1"
                        :title="datasetItem.datasetName"
                      >
                        {{ datasetItem.datasetName }}
                      </div>
                      <div class="action-box df-c">
                        <div
                          class="action action-edit"
                          @click.stop="editDataSet(datasetItem)"
                        />
                        <div
                          class="action action-del"
                          @click.stop="deleteDataSet(datasetItem)"
                        />
                      </div>
                    </div>
                  </template>
                  <el-empty v-else :image-size="60" description="暂无数据集" />
                </el-collapse-item>
              </el-collapse>
              <el-empty v-else :image-size="60" description="暂无分组" />
            </div>
          </div>
          <div class="dataset-fields">
            <div class="section-name">字段</div>
            <el-input
              v-model="filedKeyword"
              prefix-icon="el-icon-search"
              placeholder="检索所选报表内字段"
              class="search"
              clearable
            />
            <div
              v-loading="filedLoading"
              class="group-list cus-scrollbar"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
            >
              <template v-if="displayFields.length">
                <vuedraggable
                  v-model="displayFields"
                  class="wrapper"
                  :sort="false"
                  :disabled="false"
                >
                  <div
                    v-for="fieldItem in displayFields"
                    :key="fieldItem.name"
                    class="dataset-item df-c-b"
                    @dragend="
                      endDraggable(datasetItem.datasetName, fieldItem.name)
                    "
                  >
                    <div
                      class="set-name overflow-text"
                      style="flex: 1"
                      :title="fieldItem.name"
                    >
                      {{ fieldItem.name }}<span v-if="datasetItem.datasetType == '2'">({{fieldItem.columnName}})</span>
                      <span v-else>({{fieldItem.remark}})</span>
                    </div>
                    <div class="action-box df-c">
                      <div
                        class="action action-edit"
                        @click="
                          copyColumn(datasetItem.datasetName, fieldItem.name)
                        "
                      />
                      <div class="action action-del" />
                    </div>
                  </div>
                </vuedraggable>
                <el-input
                  v-show="datasetItem.apiResult"
                  v-model="datasetItem.apiResult"
                  type="textarea"
                  placeholder=""
                  rows="6"
                />
              </template>
              <el-empty v-else :image-size="60" description="暂无字段" />
            </div>
          </div>
        </div>
      </div>
      <div class="center">
        <div style="display: none">
          <input
            id="uploadBtn"
            type="file"
            accept="xlsx/*"
            @change="loadExcel"
          >
        </div>
        <div style="display: none">
          <input
            id="uploadAttachmentBtn"
            type="file"
            accept="*"
            @change="changeAttachment"
          >
        </div>
        <vuedraggable
          class="wrapper"
          :sort="false"
          style="width: 100%; height: 100%; overflow: auto"
          :disabled="true"
        >
          <div
            id="luckysheet"
            style="width: 100%; height: 100%; left: 0px; overflow: auto"
          />
        </vuedraggable>
        <div
          class="left-action action-icon df-c-b"
          @click="switchOpenLeftPanel()"
        >
          <img
            v-if="leftOpen"
            src="@/static/img/sheet/left.png"
            width="8px"
            height="8px"
          >
          <img
            v-else
            src="@/static/img/sheet/right.png"
            width="8px"
            height="8px"
          >
        </div>
        <div
          class="right-action action-icon df-c-b"
          @click="switchOpenRightPanel()"
        >
          <img
            v-if="!rightOpen"
            src="@/static/img/sheet/left.png"
            width="8px"
            height="8px"
          >
          <img
            v-else
            src="@/static/img/sheet/right.png"
            width="8px"
            height="8px"
          >
        </div>
      </div>
      <div v-show="rightOpen" class="right">
        <!-- <div class="right-head">
                <i class="el-icon-s-unfold"></i>
            </div> -->

        <div class="right-title">
          <span
            :class="
              tabIndex == 1
                ? 'cell-property'
                : 'cell-property cell-property-noactive'
            "
            @click="clickTab(1)"
          >单元格属性</span>
          <span
            :class="
              tabIndex == 2
                ? 'cell-property'
                : 'cell-property cell-property-noactive'
            "
            @click="clickTab(2)"
          >报表属性</span>
        </div>
        <div class="right-form">
          <!-- :inline="true" -->
          <el-form
            v-show="tabIndex == 1"
            ref="reportCellForm"
            label-position="top"
            class="demo-form-inline"
            :model="cellForm"
          >
            <el-collapse v-model="rightFormCollapse">
              <el-collapse-item title="常规配置" name="generalConfig">
                <el-form-item label="扩展方向">
                  <el-select
                    v-model="cellForm.cellExtend"
                    style="width: 100%"
                    placeholder="扩展方向"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellExtend"
                  >
                    <el-option label="不扩展" :value="1" />
                    <el-option label="向右扩展" :value="2" />
                    <el-option label="向下扩展" :value="3" />
                    <el-option label="交叉扩展" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="
                    cellForm.cellExtend != 4"
                  label="没有数据时是否保留空白单元格"
                  size="small"
                  :disabled="attrDisabled"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.keepEmptyCell"
                    @change="changeCellAttr('keepEmptyCell')"
                  />
                </el-form-item>
                <el-form-item label="数据填充方式" v-if="cellForm.cellExtend != 4">
                  <el-select
                    v-model="cellForm.cellFillType"
                    style="width: 100%"
                    placeholder="数据填充方式"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('cellFillType')"
                  >
                    <el-option label="插入" :value="1" />
                    <el-option label="覆盖" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item label="位置冲突后优先移动方向" v-if="cellForm.cellFillType == 1">
                  <el-select
                    v-model="cellForm.priortyMoveDirection"
                    style="width: 100%"
                    placeholder="位置冲突后优先移动方向"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('priortyMoveDirection')"
                  >
                    <el-option label="向下" :value="1" />
                    <el-option label="向右" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="cellForm.cellExtend != 4"
                  label="聚合方式"
                >
                  <el-select
                    v-model="cellForm.aggregateType"
                    style="width: 100%"
                    placeholder="聚合方式"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeAggregateType"
                  >
                    <el-option label="列表" value="list" />
                    <el-option label="分组" value="group" />
                    <el-option label="分组汇总" value="groupSummary" />
                    <el-option label="汇总" value="summary" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="
                    cellForm.aggregateType == 'group' ||
                      cellForm.aggregateType == 'groupSummary' ||
                      cellForm.cellExtend == 4
                  "
                  label="分组属性"
                  :disabled="attrDisabled"
                >
                  <el-input
                    v-model="cellForm.groupProperty"
                    style="width: 100%"
                    size="small"
                    placeholder="多个用,分隔"
                    @input="changeCellAttr('groupProperty')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="
                    cellForm.aggregateType == 'group' ||
                      cellForm.aggregateType == 'groupSummary'
                  "
                  label="分组单元格是否合一"
                  size="small"
                  :disabled="attrDisabled"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.isGroupMerge"
                    @change="changeIsGroupMerge"
                  />
                </el-form-item>
                <el-form-item label="数据来源">
                  <el-select
                    v-if="cellForm.cellExtend != 4"
                    v-model="cellForm.dataFrom"
                    style="width: 100%"
                    placeholder="数据来源"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeDataFrom"
                  >
                    <el-option label="默认" :value="1" />
                    <el-option label="原始数据" :value="2" />
                    <el-option label="单元格" :value="3" />
                  </el-select>
                  <el-select
                    v-if="cellForm.cellExtend == 4"
                    v-model="cellForm.dataFrom"
                    style="width: 100%"
                    placeholder="数据来源"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeDataFrom"
                  >
                    <el-option label="单元格" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="cellForm.dataFrom == 3"
                  label="数据来源行号(填写行号值,如1)"
                >
                  <el-input
                    v-model="cellForm.groupSummaryDependencyr"
                    style="width: 100%"
                    size="small"
                    placeholder="数据来源行号"
                    :disabled="attrDisabled"
                    @input="changeGroupSummary('r')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="cellForm.dataFrom == 3"
                  label="数据来源列号(填写列值,如A)"
                >
                  <el-input
                    v-model="cellForm.groupSummaryDependencyc"
                    style="width: 100%"
                    size="small"
                    placeholder="数据来源列号"
                    :disabled="attrDisabled"
                    @input="changeGroupSummary('c')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary') &&
                      cellForm.dataFrom != 4
                  "
                  label="汇总方式"
                  size="small"
                >
                  <el-select
                    v-model="cellForm.cellFunction"
                    style="width: 100%"
                    placeholder="汇总方式"
                    :disabled="attrDisabled"
                    @change="changeSummaryType"
                  >
                    <el-option label="合计" value="1" />
                    <el-option label="平均值" value="2" />
                    <el-option label="最大值" value="3" />
                    <el-option label="最小值" value="4" />
                    <el-option label="计数" value="5" />
                    <el-option label="同比/环比差值" value="6" />
                    <el-option label="同比/环比增长率" value="7" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  label="是否去重计算"
                  size="small"
                  class="df-form-item"
                  v-show="cellForm.aggregateType == 'summary'"
                >
                  <el-switch
                    v-model="cellForm.isDump"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isDump')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="(cellForm.aggregateType == 'summary' && cellForm.isDump)"
                  label="去重属性"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.dumpAttr"
                    style="width: 100%"
                    placeholder="去重属性，多个用,分隔"
                    :disabled="attrDisabled"
                    @input="changeCellAttr('dumpAttr')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary') &&
                      (cellForm.cellFunction == '6' || cellForm.cellFunction == '7')
                  "
                  label="本期属性"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.compareAttr1"
                    style="width: 100%"
                    placeholder="本期属性"
                    :disabled="attrDisabled"
                    @input="changeCellAttr('compareAttr1')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary') &&
                      (cellForm.cellFunction == '6' || cellForm.cellFunction == '7')
                  "
                  label="同期属性"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.compareAttr2"
                    style="width: 100%"
                    placeholder="同期属性"
                    :disabled="attrDisabled"
                    @input="changeCellAttr('compareAttr2')"
                  />
                </el-form-item>
                <el-form-item
                  label="数值单位转换"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.unitTransfer"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('unitTransfer')"
                  />
                </el-form-item>
                <el-form-item
                  v-show="cellForm.unitTransfer"
                  label="转换方式"
                  size="small"
                >
                  <el-select
                    v-model="cellForm.transferType"
                    style="width: 100%"
                    placeholder="转换方式"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('transferType')"
                  >
                    <el-option label="乘法" :value="1" />
                    <el-option label="除法" :value="2" />
                    <el-option label="乘法并转成中文大写" :value="3" />
                    <el-option label="除法并转成中文大写" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="cellForm.unitTransfer"
                  label="倍数"
                  size="small"
                >
                  <el-select
                    v-model="cellForm.multiple"
                    style="width: 100%"
                    placeholder="倍数"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('multiple')"
                  >
                    <el-option label="1" value="1" />
                    <el-option label="10" value="10" />
                    <el-option label="100" value="100" />
                    <el-option label="1000" value="1000" />
                    <el-option label="10000" value="10000" />
                    <el-option label="100000" value="100000" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary' ||
                      cellForm.unitTransfer) &&
                      cellForm.dataFrom != 4
                  "
                  label="小数位数"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.digit"
                    style="width: 100%"
                    placeholder="小数位数"
                    :disabled="attrDisabled"
                    @input="changeDigit"
                  />
                </el-form-item>
                <!-- 2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了 -->
                <!-- <el-form-item label="是否预警" size="small">
                        <el-switch
                        v-model="cellForm.warning"
                        active-text="是"
                        inactive-text="否" @change="changeIsWarning" :disabled="attrDisabled">
                    </el-switch>
                    </el-form-item>

                    <el-form-item label="预警规则" v-show="cellForm.warning">
                          <el-select  style="width:150px" placeholder="预警规则" size="small" v-model="cellForm.warningRules" @change="changeCellAttr('warningRules')" :disabled="attrDisabled">
                            <el-option label="大于" value=">"></el-option>
                            <el-option label="大于等于" value=">="></el-option>
                            <el-option label="等于" value="="></el-option>
                            <el-option label="小于" value="<"></el-option>
                            <el-option label="小于等于" value="<="></el-option>
                          </el-select>
                      </el-form-item>
                    <el-form-item label="预警阈值" size="small" v-show="cellForm.warning" >
                        <el-input v-model="cellForm.threshold" style="width:150px"  placeholder="预警阈值" @input="changeThreshold" :disabled="attrDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="预警颜色" size="small" v-show="cellForm.warning">
                        <el-color-picker v-model="cellForm.warningColor" size="small" :predefine="commonConstants.predefineColors" @change="changeWarningColor" :disabled="attrDisabled"></el-color-picker>
                    </el-form-item>
                    <el-form-item label="预警内容" size="small" v-show="cellForm.warning">
                        <el-input type="textarea" :rows="4" v-model="cellForm.warningContent" style="width:150px"  placeholder="预警内容" @input="changeWarningContent" :disabled="attrDisabled"></el-input>
                    </el-form-item> -->
                <el-form-item
                  label="是否数据字典"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.isDict"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isDict')"
                  />
                </el-form-item>
                <el-form-item
                    v-show="cellForm.isDict"
                    label="数据来源"
                  >
                  <el-select
                  v-model="cellForm.sourceType"
                  placeholder="数据来源"
                  size="small"
                  :disabled="attrDisabled"
                  @change="changeCellAttr('sourceType')"
                  clearable
                >
                  <el-option label="数据字典" :value="1" />
                  <el-option label="sql语句" :value="2" />
                  <el-option label="自定义" :value="3" />
                </el-select>
                  </el-form-item>
                <el-form-item v-show="cellForm.isDict && (cellForm.sourceType==1 || cellForm.sourceType==2)" label="数据源">
                  <el-select
                    v-model="cellForm.datasourceId"
                    style="width: 100%"
                    placeholder="数据源"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('datasourceId')"
                  >
                    <el-option
                      v-for="op in dataSource"
                      :key="op.datasourceId"
                      :label="op.dataSourceName"
                      :value="op.datasourceId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item v-show="cellForm.isDict && cellForm.sourceType==1" label="字典类型">
                  <el-select
                    v-model="cellForm.dictType"
                    style="width: 100%"
                    placeholder="字典类型"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('dictType')"
                  >
                    <el-option
                      v-for="op in dictTypes"
                      :key="op.id"
                      :label="op.dictType"
                      :value="op.dictType"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item
                    v-show="cellForm.isDict && (cellForm.sourceType==2 || cellForm.sourceType==3)"
                    :label="cellForm.sourceType==2?'sql语句':'自定义数据'"
                  >
                    <el-input
                    v-model="cellForm.dictContent"
                    type="textarea"
                    :cols="80"
                    :placeholder="cellForm.sourceType==2?'sql语句':'自定义数据'"
                    size="small"
                    @input="changeCellAttr('dictContent')"
                  />
                  </el-form-item>
                <el-form-item
                  label="是否下钻"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.isDrill"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isDrill')"
                  />
                </el-form-item>
                <el-form-item v-show="cellForm.isDrill" label="下钻报表">
                  <el-select
                    v-model="cellForm.drillId"
                    style="width: 100%"
                    placeholder="下钻报表"
                    size="small"
                    filterable
                    clearable
                    :disabled="attrDisabled"
                    @change="changeCellAttr('drillId')"
                  >
                    <el-option
                      v-for="op in reportTpls"
                      :key="op.id"
                      :label="op.tplName"
                      :value="op.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="cellForm.isDrill"
                  label="参数属性"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.drillAttrs"
                    type="textarea"
                    style="width: 100%"
                    placeholder="多个属性用,分割"
                    :disabled="attrDisabled"
                    @input="changeCellAttr('drillAttrs')"
                  />
                </el-form-item>
                <el-form-item
                  label="是否复杂数据"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.isObject"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isObject')"
                  />
                </el-form-item>
                <el-form-item label="数据类型"  v-show="cellForm.isObject">
                  <el-select
                    v-model="cellForm.dataType"
                    style="width: 100%"
                    placeholder="复杂数据类型"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('dataType')"
                  >
                    <el-option label="数组" :value="1" />
                    <el-option label="对象数组" :value="2" />
                    <el-option label="对象" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  v-show="cellForm.isObject"
                  label="数据属性"
                  size="small"
                >
                  <el-input
                    v-model="cellForm.dataAttr"
                    style="width: 100%"
                    placeholder="数据属性"
                    :disabled="attrDisabled"
                    @input="changeCellAttr('dataAttr')"
                  />
                </el-form-item>
                <el-form-item label="扩展方向"
                 v-show="cellForm.isObject">
                  <el-select
                    v-model="cellForm.subExtend"
                    style="width: 100%"
                    placeholder="扩展方向"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('subExtend')"
                  >
                    <el-option label="不扩展" :value="1" />
                    <el-option label="向右扩展" :value="2" />
                    <el-option label="向下扩展" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item
                  label="追加小计"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.isSubtotal"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isSubtotal')"
                  />
                </el-form-item>
              </el-collapse-item>

              <el-collapse-item
                v-show="cellForm.isSubtotal"
                title="小计单元格"
                name="subtotalCells"
              >
                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">小计单元格</span>
                  <el-button
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addSubTotalCells"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>
                <el-collapse
                  v-if="
                    cellForm.isSubtotal &&
                      cellForm.subTotalCells &&
                      cellForm.subTotalCells.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.subTotalCells"
                    :key="index"
                  >
                    <template slot="title">
                      小计单元格{{ index + 1 }}
                      <div
                        class="right-block-el-icon-edit"
                        :disabled="attrDisabled"
                        @click.stop="editSubtotalCell(o, index)"
                      />
                      <div
                        class="right-el-icon-delete"
                        :disabled="attrDisabled"
                        @click.stop="deleteSubtotalCell(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      :title="o.coords"
                      style="min-width: 220px; max-width: 220px"
                    >
                      单元格：{{ o.coords }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      小计类型：{{
                        commonUtil.getDictionaryValueName(
                          "subtotalType",
                          o.type
                        )
                      }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                      v-show="o.type == '6' || o.type == '7' "
                    >
                      本期属性：{{
                        o.compareAttr1
                      }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                      v-show="o.type == '6' || o.type == '7' "
                    >
                      同期属性：{{
                        o.compareAttr2
                      }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      小数位数：{{ o.digit }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      数值单位转换：{{ o.unitTransfer?'是':'否' }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                      v-if="o.unitTransfer"
                    >
                      转换方式：{{commonUtil.getDictionaryValueName(
                          "transferType",
                          o.transferType
                        ) }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                      v-if="o.unitTransfer"
                    >
                      倍数：{{o.multiple
                        }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>

              <el-collapse-item
                v-show="cellForm.isSubtotal"
                title="小计属性"
                name="subtotalAttribute"
              >
                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">小计属性</span>
                  <el-button
                    v-show="
                      cellForm.isSubtotal &&
                        (!cellForm.subTotalAttrs ||
                          cellForm.subTotalAttrs.length == 0)
                    "
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addSubTotalAttrs"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>
                <el-collapse
                  v-if="
                    cellForm.isSubtotal &&
                      cellForm.subTotalAttrs &&
                      cellForm.subTotalAttrs.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.subTotalAttrs"
                    :key="index"
                  >
                    <template slot="title">
                      小计属性
                      <div
                        class="right-block-el-icon-edit"
                        :disabled="attrDisabled"
                        @click.stop="editSubtotalAttrs(o, index)"
                      />
                      <div
                        class="right-el-icon-delete"
                        :disabled="attrDisabled"
                        @click.stop="deleteSubtotalAttrs(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      :title="o.name"
                      style="min-width: 220px; max-width: 220px"
                    >
                      小计名称：{{ o.name }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      字体颜色：{{ o.fontColor }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      字体大小：{{ o.fontSize }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      背景颜色：{{ o.bgColor }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      是否加粗：{{ o.fontWeight ? "是" : "否" }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
              <el-collapse-item title="填报配置" name="fillSettings" v-if="tplType != 1">
                <el-form-item
                  label="打印/导出隐藏列"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.formsAttrs.isOperationCol"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('isOperationCol','formsAttrs')"
                  />
                </el-form-item>
                <el-form-item
                  label="允许修改"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.formsAttrs.allowEdit"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('allowEdit','formsAttrs')"
                  />
                </el-form-item>
                <el-form-item label="值类型">
                  <el-select
                    v-model="cellForm.formsAttrs.valueType"
                    style="width: 100%"
                    placeholder="值类型"
                    size="small"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('valueType','formsAttrs')"
                  >
                    <el-option label="文本" value="1" />
                    <el-option label="数值" value="2" />
                    <el-option label="日期时间" value="3" />
                    <el-option label="下拉单选" value="4" />
                  </el-select>
                </el-form-item>
                <div class="cus-collapse-content">
                  <el-form-item
                    label="必填项"
                    size="small"
                    class="df-form-item"
                  >
                    <el-switch
                      v-model="cellForm.formsAttrs.require"
                      active-text="是"
                      inactive-text="否"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('require','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '1'"
                    label="长度校验"
                    size="small"
                    class="df-form-item"
                  >
                    <el-switch
                      v-model="cellForm.formsAttrs.lengthValid"
                      active-text="是"
                      inactive-text="否"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('lengthValid','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.lengthValid && cellForm.formsAttrs.valueType == '1'"
                    label="最小长度"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.minLength"
                      style="width: 100%"
                      placeholder="最小长度"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('minLength','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.lengthValid && cellForm.formsAttrs.valueType == '1'"
                    label="最大长度"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.maxLength"
                      style="width: 100%"
                      placeholder="最大长度"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('maxLength','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '1'"
                    label="校验规则"
                  >
                    <el-select
                      v-model="cellForm.formsAttrs.textValidRule"
                      style="width: 100%"
                      placeholder="校验规则"
                      size="small"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('textValidRule','formsAttrs')"
                    >
                      <el-option label="无" value="0" />
                      <el-option label="邮箱" value="1" />
                      <el-option label="手机号" value="2" />
                      <el-option label="座机号" value="3" />
                      <el-option label="身份证" value="4" />
                      <el-option label="自定义" value="99" />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="
                      cellForm.formsAttrs.textValidRule == '99' &&
                        cellForm.formsAttrs.valueType == '1'
                    "
                    label="正则表达式"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.regex"
                      style="width: 140px"
                      placeholder="正则表达式"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('regex')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '2'"
                    label="大小校验"
                    size="small"
                    class="df-form-item"
                  >
                    <el-switch
                      v-model="cellForm.formsAttrs.numberRangeValid"
                      active-text="是"
                      inactive-text="否"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('numberRangeValid','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="
                      cellForm.formsAttrs.valueType == '2' && cellForm.formsAttrs.numberRangeValid
                    "
                    label="最小值"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.minValue"
                      style="width: 100%"
                      placeholder="最小值"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('minValue','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="
                      cellForm.formsAttrs.valueType == '2' && cellForm.formsAttrs.numberRangeValid
                    "
                    label="最大值"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.maxValue"
                      style="width: 100%"
                      placeholder="最大值"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('maxValue','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '2'"
                    label="小数位数"
                    size="small"
                  >
                    <el-input
                      v-model="cellForm.formsAttrs.digit"
                      style="width: 100%"
                      placeholder="小数位数"
                      :disabled="attrDisabled"
                      @input="changeCellAttr('digit','formsAttrs')"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '3'"
                    label="格式"
                    size="small"
                  >
                    <!-- <el-input v-model="cellForm.dateFormat" style="width:150px"  placeholder="日期格式" @input="changeCellAttr('dateFormat')"></el-input> -->
                    <el-select
                      v-model="cellForm.formsAttrs.dateFormat"
                      style="width: 100%"
                      placeholder="日期格式"
                      size="small"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('dateFormat','formsAttrs')"
                    >
                      <el-option label="年-月-日" value="yyyy-MM-dd" />
                      <el-option label="年-月" value="yyyy-MM" />
                      <el-option label="年" value="yyyy" />
                      <el-option
                        label="年-月-日 时:分:秒"
                        value="yyyy-MM-dd HH:mm:ss"
                      />
                      <el-option
                        label="年-月-日 时:分"
                        value="yyyy-MM-dd HH:mm"
                      />
                      <el-option label="时:分:秒" value="HH:mm:ss" />
                      <el-option label="时:分" value="HH:mm" />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '4'"
                    label="数据来源"
                  >
                  <el-select
                  v-model="cellForm.formsAttrs.sourceType"
                  placeholder="数据来源"
                  size="small"
                  :disabled="attrDisabled"
                  @change="changeCellAttr('sourceType','formsAttrs')"
                  clearable
                >
                  <el-option label="数据字典" :value="1" />
                  <el-option label="sql语句" :value="2" />
                  <el-option label="自定义" :value="3" />
                </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '4' && (cellForm.formsAttrs.sourceType==1 || cellForm.formsAttrs.sourceType==2)"
                    label="数据源"
                  >
                    <el-select
                      v-model="cellForm.formsAttrs.datasourceId"
                      style="width: 100%"
                      placeholder="数据源"
                      size="small"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('datasourceId','formsAttrs')"
                      clearable
                    >
                      <el-option
                        v-for="op in dataSource"
                        :key="op.datasourceId"
                        :label="op.dataSourceName"
                        :value="op.datasourceId"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '4' && cellForm.formsAttrs.sourceType==1"
                    label="字典类型"
                  >
                    <el-select
                      v-model="cellForm.formsAttrs.dictType"
                      style="width: 100%"
                      placeholder="字典类型"
                      size="small"
                      :disabled="attrDisabled"
                      @change="changeCellAttr('dictType','formsAttrs')"
                    >
                      <el-option
                        v-for="op in dictTypes"
                        :key="op.id"
                        :label="op.dictType"
                        :value="op.dictType"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="cellForm.formsAttrs.valueType == '4' && (cellForm.formsAttrs.sourceType==2 || cellForm.formsAttrs.sourceType==3)"
                    :label="cellForm.formsAttrs.sourceType==2?'sql语句':'自定义数据'"
                  >
                    <el-input
                    v-model="cellForm.formsAttrs.content"
                    type="textarea"
                    :cols="80"
                    :placeholder="cellForm.formsAttrs.sourceType==2?'sql语句':'自定义数据'"
                    size="small"
                  />
                  </el-form-item>
                </div>
                <el-form-item
                  label="与其他单元格比较"
                  size="small"
                  class="df-form-item"
                >
                  <el-switch
                    v-model="cellForm.formsAttrs.otherCellCompare"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('otherCellCompare','formsAttrs')"
                  />
                </el-form-item>
                <div
                  v-if="cellForm.formsAttrs.otherCellCompare"
                  class="right-dataset-title df-c-b"
                >
                  <span class="attr-dataset-title">比较单元格</span>
                  <el-button
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addCompareCells"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>
                <el-collapse
                  v-if="
                    cellForm.formsAttrs.otherCellCompare &&
                      cellForm.formsAttrs.compareCells &&
                      cellForm.formsAttrs.compareCells.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.formsAttrs.compareCells"
                    :key="index"
                  >
                    <template slot="title">
                      比较单元格{{ index + 1 }}
                      <div
                        class="right-block-el-icon-edit"
                        title="编辑"
                        :disabled="attrDisabled"
                        @click.stop="editCompareCell(o, index)"
                      />
                      <div
                        class="right-el-icon-delete"
                        title="删除"
                        :disabled="attrDisabled"
                        @click.stop="deleteCompareCell(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      :title="o.sheetName"
                      style="min-width: 220px; max-width: 220px"
                    >
                      sheet名称：{{ o.sheetName }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      坐标：{{ o.coordinate }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      单元格类型：{{
                        commonUtil.getDictionaryValueName(
                          "cellType",
                          o.cellType
                        )
                      }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      比较类型：{{ o.compareType }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
              <el-collapse-item title="分组小计链" name="groupSubtotal">
                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">分组小计链</span>
                  <el-button
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addSubTotalCalc"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>
                <el-collapse
                  v-if="
                    cellForm.subTotalCalc && cellForm.subTotalCalc.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.subTotalCalc"
                    :key="index"
                  >
                    <template slot="title">
                      分组小计链{{ index + 1 }}
                      <div
                        class="right-block-el-icon-edit"
                        :disabled="attrDisabled"
                        @click.stop="editSubtotalCalc(o, index)"
                      />
                      <div
                        class="right-el-icon-delete"
                        :disabled="attrDisabled"
                        @click.stop="deleteSubtotalCalc(index)"
                      />
                    </template>
                    <!-- <p class="column-tag" :title="o.coords" style="min-width:220px;max-width:220px">单元格：{{o.coords}}</p> -->
                    <p
                      v-for="(attr, index) in o.attrs"
                      :key="index"
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      字段{{ index + 1 }}：{{ attr }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
              
              <el-collapse-item title="单元格过滤条件" name="cellFilter">
                <div class="df" style="padding: 8px 0 16px 0">
                  <span class="cell-label">判断查询</span>
                  <el-radio
                    v-model="cellForm.filterType"
                    label="and"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('filterType')"
                  >AND</el-radio>
                  <el-radio
                    v-model="cellForm.filterType"
                    label="or"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('filterType')"
                  >OR</el-radio>
                </div>
                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">单元格过滤条件</span>
                  <el-button
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addCellConditions"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>

                <el-collapse
                  v-if="
                    cellForm.cellconditions &&
                      cellForm.cellconditions.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.cellconditions"
                    :key="index"
                  >
                    <template slot="title">
                      过滤条件{{ index + 1 }}
                      <div
                        class="right-el-icon-edit"
                        :disabled="attrDisabled"
                        @click.stop="editCellCondition(index)"
                      />
                      <div
                        class="right-el-icon-copy-document"
                        :disabled="attrDisabled"
                        @click.stop="copyCellCondition(o)"
                      />
                      <div
                        class="right-el-icon-delete"
                        :disabled="attrDisabled"
                        @click.stop="deleteCellCondition(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      :title="o.property"
                      style="min-width: 220px; max-width: 220px"
                    >
                      属性：{{ o.property }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      操作符：{{ o.operator }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      数据类型：<label v-if="o.type == 'varchar'">字符串</label>
                      <label v-else-if="o.type == 'number'">数值</label><label v-else-if="o.type == 'cell'">单元格</label><label v-else>日期</label>
                    </p>
                    <p
                      class="column-tag"
                      :title="o.value"
                      style="min-width: 220px; max-width: 220px"
                    >
                      条件值：{{ o.value }}
                    </p>
                    <p
                      v-if="o.type == 'date'"
                      class="column-tag"
                      :title="o.dateFormat"
                      style="min-width: 220px; max-width: 220px"
                    >
                      日期格式：{{ o.dateFormat }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>

              <el-collapse-item title="单元格隐藏条件" name="cellHide">
                <div class="df" style="padding: 8px 0 16px 0">
                  <span class="cell-label">判断查询</span>
                  <el-radio
                    v-model="cellForm.hiddenType"
                    label="and"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('hiddenType')"
                  >AND</el-radio>
                  <el-radio
                    v-model="cellForm.hiddenType"
                    label="or"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('hiddenType')"
                  >OR</el-radio>
                </div>

                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">单元格隐藏条件</span>
                  <el-button
                    class="addBtn"
                    :disabled="attrDisabled"
                    @click="addCellHiddenConditions"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>

                <el-collapse
                  v-if="
                    cellForm.cellHiddenConditions &&
                      cellForm.cellHiddenConditions.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in cellForm.cellHiddenConditions"
                    :key="index"
                  >
                    <template slot="title">
                      隐藏条件{{ index + 1 }}
                      <div
                        class="right-el-icon-edit"
                        :disabled="attrDisabled"
                        @click.stop="editCellHiddenCondition(index)"
                      />
                      <div
                        class="right-el-icon-copy-document"
                        :disabled="attrDisabled"
                        @click.stop="copyCellHiddenCondition(o)"
                      />
                      <div
                        class="right-el-icon-delete"
                        :disabled="attrDisabled"
                        @click.stop="deleteCellHiddenCondition(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      :title="o.propertyName"
                      style="min-width: 220px; max-width: 220px"
                    >
                      参数名称：{{ o.propertyName }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.property"
                      style="min-width: 220px; max-width: 220px"
                    >
                      参数编码：{{ o.property }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      操作符：{{ o.operator }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      数据类型：<label v-if="o.type == 'varchar'">字符串</label>
                      <label v-else-if="o.type == 'number'">数值</label><label v-else>日期</label>
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      条件值：{{ o.value }}
                    </p>
                    <p
                      v-if="o.type == 'date'"
                      class="column-tag"
                      :title="o.dateFormat"
                      style="min-width: 220px; max-width: 220px"
                    >
                      日期格式：{{ o.dateFormat }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
            </el-collapse>
          </el-form>
          <div v-show="tabIndex == 2" class="demo-form-inline">
            <el-collapse v-model="rightFormCollapse2">
              <el-collapse-item title="常规配置" name="generalConfig">
                <div class="df" style="padding: 8px 0 12px 0">
                  <span
                    class="cell-label"
                    style="line-height: 20px"
                  >参数是否合并</span>
                  <el-switch
                    v-model="isParamMerge"
                    active-text="合并"
                    inactive-text="不合并"
                    @change="changeParamMerge"
                  />
                </div>
              </el-collapse-item>
              <el-collapse-item title="循环块配置" name="sheetBlock">
                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">循环块记录</span>
                  <el-button
                    class="addBtn"
                    @click="addBlock"
                  ><i class="el-icon-plus el-icon--left" />添加</el-button>
                </div>
                <el-collapse
                  v-if="sheetBlockData && sheetBlockData.length > 0"
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in sheetBlockData"
                    :key="index"
                  >
                    <template slot="title">
                      循环块{{ index + 1 }}
                      <div
                        class="right-block-el-icon-edit"
                        @click.stop="editBlock(o, index)"
                      />
                      <div
                        class="right-el-icon-delete"
                        @click.stop="deleteBlock(index)"
                      />
                    </template>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      循环块范围：{{ o.startCell }}:{{ o.endCell }}
                    </p>
                    <p
                      class="column-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      聚合方式：<label
                        v-if="o.aggregateType == 'list'"
                      >列表</label>
                      <label v-else>分组</label>
                    </p>
                    <p
                      v-if="o.aggregateType == 'group'"
                      class="column-tag"
                      :title="o.groupProperty"
                      style="min-width: 220px; max-width: 220px"
                    >
                      分组属性：{{ o.groupProperty }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.hloopCount"
                      style="min-width: 220px; max-width: 220px"
                    >
                      横向循环次数：{{ o.hloopCount }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.hloopEmptyCount"
                      style="min-width: 220px; max-width: 220px"
                    >
                      横向循环间隔空行数：{{ o.hloopEmptyCount }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.vloopEmptyCount"
                      style="min-width: 220px; max-width: 220px"
                    >
                      纵向循环间隔空行数：{{ o.vloopEmptyCount }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.subBlockRange"
                      style="min-width: 220px; max-width: 220px"
                    >
                      子循环块范围：{{ o.subBlockRange }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
              <el-collapse-item title="图片配置" name="sheetImages">
                <el-collapse
                  v-if="sheetImages && Object.keys(sheetImages).length>0"
                  class="sub-collapse"
                >
                  <el-collapse-item
                    v-for="(o, index) in sheetImages"
                    :key="index"
                  >
                    <template slot="title">
                      图片
                    </template>
                    <p
                      class="img-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                     <el-image
                    style="width: 200px; height: 100px"
                    :src="o.src"
                    :fit="'fill'"></el-image>
                    </p>
                    <p
                      class="switch-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      锁定：<el-switch v-model="o.isLocked" size="mini" @change="changePictureLockStatus(o,index)"/>
                    </p>
                    <p
                      class="switch-tag"
                      style="min-width: 220px; max-width: 220px"
                    >
                      打印层级：
                      <el-input
                        v-model="o.zIndex"
                        placeholder="打印层级"
                        size="mini"
                      />
                    </p>
                    <br>
                    <p
                      v-if="o.aggregateType == 'group'"
                      class="column-tag"
                      :title="o.groupProperty"
                      style="min-width: 220px; max-width: 220px"
                    >
                      分组属性：{{ o.groupProperty }}
                    </p>
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
        <div v-if="chartSettingShow" class="config-panel">
          <div class="config-header">图表设置</div>
          <div class="config-box">
            <vchartsetting
              :component="chartOptions"
              :datasets="datasets"
              :is-preview="false"
              :is-coedit="false"
            />
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      title="数据集"
      :visible.sync="addDatasetsDialogVisiable"
      width="82%"
      top="20px"
      :close-on-click-modal="false"
      append-to-body
      class="add-dataset-dialog"
      @close="closeAddDataSet"
    >
      <el-radio-group v-model="addDatasetType" style="margin-bottom: 16px">
        <el-radio-button :label="1">sql语句</el-radio-button>
        <el-radio-button :label="2">参数配置</el-radio-button>
      </el-radio-group>
      <div v-show="addDatasetType == 1">
        <el-form
          ref="sqlRef"
          :inline="true"
          :model="sqlForm"
          class="demo-form-inline"
        >
          <el-form-item
            label="数据集名称"
            prop="datasetName"
            :rules="filter_rules('数据集名称', { required: true })"
          >
            <el-input
              v-model="sqlForm.datasetName"
              placeholder="数据集名称"
              size="small"
            />
          </el-form-item>
          <el-form-item
            label="数据源"
            prop="datasourceId"
            :rules="filter_rules('选择数据源', { required: true })"
          >
            <el-select
              v-model="sqlForm.datasourceId"
              placeholder="选择数据源"
              size="small"
              @change="changeDatasource(false)"
            >
              <el-option
                v-for="op in dataSource"
                :key="op.datasourceId"
                :label="op.dataSourceName"
                :value="op.datasourceId"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="datasourceType == 1"
            label="sql类型"
            prop="sqlType"
            :rules="filter_rules('选择sql类型', { required: true })"
          >
            <el-select
              v-model="sqlForm.sqlType"
              placeholder="选择sql类型"
              size="small"
            >
              <el-option
                v-for="op in selectUtil.sqlType"
                :key="op.value"
                :label="op.label"
                :value="op.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="是否公共数据集"
            prop="isCommon"
            :rules="filter_rules('是否公共数据集', { required: true })"
          >
            <el-select
              v-model="sqlForm.isCommon"
              placeholder="是否公共数据集"
              size="small"
            >
              <el-option
                v-for="item in selectUtil.yesNo"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="分组"
            prop="groupId"
            :rules="filter_rules('选择分组', { required: true })"
            v-if="sqlForm.isCommon == 2"
          >
            <el-select
              v-model="sqlForm.groupId"
              placeholder="选择分组"
              size="small"
            >
              <el-option
                v-for="item in groupList"
                :key="item.id"
                :label="item.groupName"
                :value="item.id"
                :disabled="item.id == 0"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="行列转置"
            prop="isConvert"
            :rules="filter_rules('行列转置', { required: true })"
          >
            <el-select
              v-model="sqlForm.isConvert"
              placeholder="行列转置"
              size="small"
            >
              <el-option
                v-for="item in selectUtil.yesNo"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="固定列"
            prop="fixedColumn"
            :rules="filter_rules('固定列', { required: false })"
            v-if="sqlForm.isConvert == 1"
          >
            <el-select
              v-model="sqlForm.fixedColumn"
              placeholder="固定列"
              size="small"
              multiple
              collapse-tags
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="列转行(表头)"
            prop="headerName"
            :rules="filter_rules('列转行(表头)', { required: false })"
            v-if="sqlForm.isConvert == 1"
          >
            <el-select
              v-model="sqlForm.headerName"
              placeholder="列转行(表头)"
              size="small"
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="列转行(数值)"
            prop="valueField"
            :rules="filter_rules('列转行(数值)', { required: false })"
            v-if="sqlForm.isConvert == 1"
          >
            <el-select
              v-model="sqlForm.valueField"
              placeholder="列转行(数值)"
              size="small"
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="查询集合(表)"
            prop="mongoTable"
            :rules="filter_rules('查询集合(表)', { required: true })"
            v-if="datasourceType == 3"
          >
            <el-select
              v-model="sqlForm.mongoTable"
              placeholder="查询集合(表)"
              size="small"
            >
              <el-option
                v-for="item in dataSourceTables"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="查询方式"
            prop="mongoSearchType"
            :rules="filter_rules('查询方式', { required: true })"
            v-if="datasourceType == 3"
          >
            <el-select
              v-model="sqlForm.mongoSearchType"
              placeholder="查询方式"
              size="small"
            >
              <el-option
                v-for="item in selectUtil.mongoSearchType"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <div class="df" style="width: 100%;border: 1px solid #e8e8e8;">
          <div v-show="selectVariableOpen && datasourceType != 3" class="variable-content">
            <div class="variable-title">选择变量</div>
            <div class="variable-warp">
              <div class="variable-warp-title">系统变量</div>
              <div class="variable-list df">
                <div
                  v-for="(item, index) in commonConstants.systemParam"
                  :key="index"
                  :title="item.label"
                  class="variable-item df-c"
                >
                  <div class="overflow-text" style="flex:1;margin-right:8px;">{{ item.label }}({{ item.value }})</div>
                  <i
                    class="el-icon-copy-document"
                    title="复制"
                    @click="doCopy(item)"
                  />
                  <i
                    class="el-icon-circle-plus-outline"
                    title="添加"
                    style="margin-left:4px"
                    @click="doCopy(item,true)"
                  />
                </div>
              </div>
              <div class="variable-warp-title">解析表</div>

              <div class="tablecolumn">
                <el-select
                  v-model="datasourceTableName"
                  placeholder="请选择解析表"
                  size="mini"
                  filterable
                  style="margin-bottom:10px;width: 254px;"
                  @change="getTableColumns"
                >
                  <el-option
                    v-for="op in dataSourceTables"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <div class="variable-list analysis-list df">
                  <template v-if="tableColumns.length">
                    <div
                      v-for="(column, index) in tableColumns"
                      :key="index"
                      class="variable-item df-c"
                      :title="column.name"
                    >
                      <div class="overflow-text" style="flex:1;margin-right:8px;">{{ column.name }}</div>
                      <el-dropdown>
                        <i class="el-icon-copy-document" title="复制" style="flex:1;margin-right:4px;" />
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item
                            @click.native="getWhereByColumn(1, column)"
                          >仅字段</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(2, column)"
                          >表名.字段</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(3, column)"
                          >查询条件(=)</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(4, column)"
                          >查询条件(in)</el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                      <el-dropdown>
                        <i class="el-icon-circle-plus-outline" title="添加" />
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item
                            @click.native="getWhereByColumn(1, column,true)"
                          >仅字段</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(2, column,true)"
                          >表名.字段</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(3, column,true)"
                          >查询条件(=)</el-dropdown-item>
                          <el-dropdown-item
                            @click.native="getWhereByColumn(4, column,true)"
                          >查询条件(in)</el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                    </div>
                  </template>
                  <el-empty v-else style="margin:0 auto" :image-size="60" description="暂无字段" />
                </div>
              </div>
            </div>
          </div>
          <div class="sql-content">
            <!-- <div
              class="left-action action-icon df-c-b"
              @click="switchOpenSelectVarPanel()"
            >
              <img
                v-if="selectVariableOpen"
                src="@/static/img/sheet/left.png"
                width="8px"
                height="8px"
              >
              <img
                v-else
                src="@/static/img/sheet/right.png"
                width="8px"
                height="8px"
              >
            </div> -->
            <div v-if="datasourceType == 1 || datasourceType == 3" style="height: 25px">
              <el-tooltip
                content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中"
                placement="bottom"
              ><el-tag
                type="success"
                size="small"
                style="cursor: pointer"
                @click="execSql"
              ><i class="el-icon-caret-right" />执行</el-tag></el-tooltip>
              <el-tooltip
                content="该操作会将sql语句进行格式化并显示"
                placement="right"
              ><el-tag
                size="small"
                style="cursor: pointer"
                @click="formatSql"
                v-if="datasourceType == 1"
              ><i class="el-icon-document" />格式化</el-tag>
              </el-tooltip>
              <el-tooltip
                content="该操作会插入注释标签"
                placement="right"
              ><el-tag
                type="warning"
                size="small"
                style="cursor: pointer"
                @click="addComment(' <!--  -->')"
                v-if="datasourceType == 1"
              ><i class="el-icon-circle-plus-outline" />添加注释</el-tag>
              </el-tooltip>
              <el-dropdown
                v-if="
                  paramTableData.tableData && paramTableData.tableData.length > 0 && datasourceType == 1
                "
              >
                <el-tag
                  type="danger"
                  size="small"
                  style="cursor: pointer"
                ><i class="el-icon-circle-plus-outline" />添加参数</el-tag>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    v-for="(row, index) in paramTableData.tableData"
                    :key="index"
                    @click.native="getWhereByParam(row)"
                  >{{ row.paramCode }}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div v-if="datasourceType == 1" style="height: 300px">
              <div style="height: 100%; width: 100%" v-if="datasourceType == 1">
                <codemirror ref="codeMirror" :options="cmOptions" />
              </div>
            </div>
            <div v-if="datasourceType == 3" style="height: 300px">
              <div :style="{height: '100%',width: sqlForm.mongoSearchType == 1?'50%':'100%',float:'left'}" v-if="datasourceType == 3">
                <codemirror ref="codeMirror" :options="cmOptions" />
              </div>
              <div style="height: 100%; width: 49%;float:right" v-if="datasourceType == 3 && sqlForm.mongoSearchType == 1">
                <codemirror ref="orderCodeMirror" :options="cmOptions" />
              </div>
            </div>
            <div class="table-warp">
              <!--表格 start-->
              <div class="table-title">执行结果</div>
              <el-table
                :data="
                  sqlColumnTableData.tableData.slice(
                    (sqlColumnTableData.tablePage.currentPage - 1) *
                      sqlColumnTableData.tablePage.pageSize,
                    sqlColumnTableData.tablePage.currentPage *
                      sqlColumnTableData.tablePage.pageSize
                  )
                "
                border
                style="width: 100%"
                align="center"
                size="small"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column width="72" label="序号" align="center">
                  <template slot-scope="scope">
                    <span>{{ (sqlColumnTableData.tablePage.currentPage- 1) * sqlColumnTableData.tablePage.pageSize + scope.$index + 1 }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="columnName" label="列名" align="center" />
                <el-table-column prop="name" label="别名" align="center" />
                <el-table-column prop="dataType" label="数据类型" align="center" />
                <el-table-column prop="width" label="宽度" align="center" />
                <el-table-column prop="remark" label="注释" align="center" />
              </el-table>
              <!--表格 end-->
              <!--分页 start-->
              <el-pagination
                :current-page="sqlColumnTableData.tablePage.currentPage"
                :page-sizes="sqlColumnTableData.tablePage.pageSizeRange"
                :page-size="sqlColumnTableData.tablePage.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="sqlColumnTableData.tablePage.pageTotal"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
              />
              <!--分页 end-->
            </div>
          </div>
        </div>

      </div>
      <div v-show="addDatasetType == 2" class="parameter-content">
        <div v-show="sqlForm.sqlType == '1' || datasourceType == 2" style="margin-bottom:20px">
          <div class="parameter-warp">
            <div v-if="datasourceType == 1 || datasourceType == 2 || datasourceType == 3" class="warp-title">分页参数</div>
            <el-form
              ref="paginationRef"
              label-position="right"
              :model="paginationForm"
              class="df-form"

            >
              <el-form-item
                v-if="datasourceType == 1 || datasourceType == 2 || datasourceType == 3"
                label="是否分页"
                prop="isPagination"
              >
                <el-select
                  v-model="paginationForm.isPagination"
                  placeholder="是否分页"
                  size="small"
                >
                  <el-option label="是" :value="1" />
                  <el-option label="否" :value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paginationForm.isPagination == '1'"
                label="每页条数"
                prop="pageCount"
                :rules="filter_rules('每页条数', { required: true })"
              >
                <!-- <el-input v-model="paginationForm.pageCount" placeholder="每页条数" size="small"></el-input> -->
                <el-select
                  v-model="paginationForm.pageCount"
                  placeholder="请选择"
                  size="small"
                >
                  <el-option
                    v-for="item in selectUtil.pageCount"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paginationForm.isPagination == '1' && datasourceType == 2"
                label="当前页参数属性"
                prop="currentPageAttr"
                :rules="filter_rules('当前页参数属性', { required: true })"
              >
                <el-input
                  v-model="paginationForm.currentPageAttr"
                  placeholder="当前页参数属性"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                v-if="paginationForm.isPagination == '1' && datasourceType == 2"
                label="每页条数参数属性"
                prop="pageCountAttr"
                :rules="filter_rules('每页条数参数属性', { required: true })"
              >
                <el-input
                  v-model="paginationForm.pageCountAttr"
                  placeholder="每页条数参数属性"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                v-if="paginationForm.isPagination == '1' && datasourceType == 2"
                label="总条数属性"
                prop="totalAttr"
                :rules="filter_rules('总条数条数属性', { required: true })"
              >
                <el-input
                  v-model="paginationForm.totalAttr"
                  placeholder="总条数属性"
                  size="small"
                />
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div v-show="sqlForm.sqlType == 1 || datasourceType == 2" style="margin-bottom:20px">
          <div class="parameter-warp">
            <div class="warp-title">字段参数</div>
            <el-form
              ref="paramRef"
              label-position="right"
              :model="paramForm"
              class="df-form"
            >
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input
                  v-model="paramForm.paramName"
                  placeholder="参数名称"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input
                  v-model="paramForm.paramCode"
                  placeholder="参数编码"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                v-if="datasourceType == 2"
                label="参数前缀"
                prop="paramPrefix"
                :rules="filter_rules('参数前缀', { required: false })"
              >
                <el-input
                  v-model="paramForm.paramPrefix"
                  placeholder="参数前缀"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select
                  v-model="paramForm.paramType"
                  placeholder="参数类型"
                  size="small"
                >
                  <el-option label="字符串" value="varchar" />
                  <el-option label="数值" value="number" />
                  <el-option label="日期" value="date" />
                  <el-option label="下拉单选" value="select" />
                  <el-option label="下拉多选" value="mutiselect" />
                  <el-option label="下拉树(单选)" value="treeSelect" />
                  <el-option label="下拉树(多选)" value="multiTreeSelect" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select
                  v-model="paramForm.dateFormat"
                  placeholder="日期格式"
                  size="small"
                >
                  <el-option label="年" value="yyyy" />
                  <el-option label="年-月" value="yyyy-MM" />
                  <el-option label="年-月-日" value="yyyy-MM-dd" />
                  <el-option label="年-月-日 时:分" value="yyyy-MM-dd HH:mm" />
                </el-select>
              </el-form-item>
              <el-form-item label="默认值" prop="paramDefault">
                <el-input
                  v-model="paramForm.paramDefault"
                  placeholder="默认值"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                label="是否必填"
                prop="paramRequired"
                :rules="filter_rules('是否必填', { required: true })"
              >
                <el-select
                  v-model="paramForm.paramRequired"
                  placeholder="是否必填"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                key="paramHidden"
                label="是否隐藏"
                prop="paramHidden"
                :rules="filter_rules('是否隐藏', { required: true })"
              >
                <el-select
                  v-model="paramForm.paramHidden"
                  placeholder="是否隐藏"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' ||
                    paramForm.paramType == 'mutiselect'
                "
                key="selectType"
                label="选择内容来源"
                prop="selectType"
                :rules="filter_rules('选择内容来源', { required: true })"
              >
                <el-select
                  v-model="paramForm.selectType"
                  placeholder="选择内容来源"
                  size="small"
                >
                  <el-option label="自定义" value="1" />
                  <el-option label="sql语句" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  (paramForm.paramType == 'select' &&
                    paramForm.selectType == '2') ||
                    (paramForm.paramType == 'mutiselect' &&
                      paramForm.selectType == '2') ||
                    paramForm.paramType == 'treeSelect' ||
                    paramForm.paramType == 'multiTreeSelect'
                "
                label="选择数据源"
                prop="datasourceId"
                :rules="filter_rules('选择数据源', { required: true })"
              >
                <el-select
                  v-model="paramForm.datasourceId"
                  placeholder="选择数据源"
                  size="small"
                >
                  <el-option
                    v-for="op in dataSource"
                    :key="op.datasourceId"
                    :label="op.dataSourceName"
                    :value="op.datasourceId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  (paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') && (paramForm.selectType == '2' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect')
                "
                key="isRelyOnParams"
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select
                  v-model="paramForm.isRelyOnParams"
                  placeholder="是否依赖其他参数"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  (paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') &&
                    (paramForm.selectType == '2' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') &&
                    paramForm.isRelyOnParams == '1'
                "
                key="relyOnParams"
                label="依赖参数代码"
                prop="relyOnParams"
                :rules="filter_rules('依赖参数代码', { required: true })"
              >
                <el-input
                  v-model="paramForm.relyOnParams"
                  placeholder="依赖参数代码，多个用,分隔"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'multiTreeSelect'"
                key="checkStrictly"
                label="父子联动"
                prop="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select
                  v-model="paramForm.checkStrictly"
                  placeholder="选择父子联动"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' ||
                    paramForm.paramType == 'mutiselect' ||
                    paramForm.paramType == 'treeSelect' ||
                    paramForm.paramType == 'multiTreeSelect'
                "
                key="selectContent"
                label="下拉选择内容"
                prop="selectContent"
                :rules="filter_rules('下拉选择内容', { required: true })"
              >
                <el-input
                  v-model="paramForm.selectContent"
                  type="textarea"
                  :cols="80"
                  placeholder="下拉选择内容"
                  size="small"
                />
                <div class="sub-title">{{ selectContentSuggestion }}</div>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size="small"
                  @click="addParam"
                >添加</el-button>
              </el-form-item><br>
              <div style="width:100%">
              <el-link :underline="false" v-if="paramForm.paramType == 'date'" type="warning" href="https://gitee.com/springreport/springreport/wikis/pages?sort_id=13973093&doc_id=5747656" target="_blank">点击查看日期默认值设置规则</el-link>
              <el-tag
                v-if="
                  paramForm.paramType == 'select' ||
                    paramForm.paramType == 'mutiselect'
                "
                type="warning"
              >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
                注意：两个key必须是value 和 name</el-tag><br
                v-if="
                  paramForm.paramType == 'select' ||
                    paramForm.paramType == 'mutiselect'
                "
              >
              <el-tag
                v-if="
                  paramForm.paramType == 'select' ||
                    paramForm.paramType == 'mutiselect'
                "
                type="warning"
              >sql语句格式：select code as value, name as name from table
                注意：返回的属性中必须有 value 和 name</el-tag>
              <el-tag
                v-if="
                  paramForm.paramType == 'treeSelect' ||
                    paramForm.paramType == 'multiTreeSelect'
                "
                type="warning"
              >sql语句格式：select deptId as id, deptName as name,parentId as
                pid from table 注意：返回的属性中必须有 id,name和pid</el-tag>
              </div>
            </el-form>
          </div>
          <div style="height: 50%">
            <!--表格 start-->
            <el-table
              :data="paramTableData.tableData"
              border
              style="width: 100%"
              align="center"
              size="small"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center" />
              <el-table-column
                prop="paramCode"
                label="参数编码"
                align="center"
              />
              <el-table-column
                prop="paramType"
                label="参数类型"
                align="center"
              />
              <el-table-column
                prop="paramDefault"
                label="默认值"
                align="center"
              />
              <el-table-column
                prop="paramRequired"
                label="是否必填"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              />
              <el-table-column
                prop="paramHidden"
                label="是否隐藏"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              />
              <el-table-column
                prop="isRelyOnParams"
                label="是否依赖其他参数"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              />
              <el-table-column
                prop="relyOnParams"
                label="依赖参数"
                align="center"
              />
              <el-table-column label="操作" align="center" width="200">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="small"
                    @click="editParam(scope.row)"
                  >编辑</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveUp(scope.$index, '3')"
                  >上移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveDown(scope.$index, '3')"
                  >下移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="deleteParam(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
        </div>
        <div v-show="sqlForm.sqlType == 2 && datasourceType == 1" style="margin-bottom:20px">
          <div class="parameter-warp">
            <div class="warp-title">输入参数</div>
            <el-form
              ref="inParamRef"
              abel-position="right"
              :model="procedureParamForm"
              class="df-form"
              size="small"
            >
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input
                  v-model="procedureParamForm.paramName"
                  placeholder="参数名称"
                />
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input
                  v-model="procedureParamForm.paramCode"
                  placeholder="参数编码"
                />
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.paramType"
                  placeholder="参数类型"
                >
                  <el-option label="int" value="int" />
                  <el-option label="String" value="String" />
                  <el-option label="Long" value="Long" />
                  <el-option label="BigDecimal" value="BigDecimal" />
                  <el-option label="Double" value="Double" />
                  <el-option label="Float" value="Float" />
                  <el-option label="Date" value="Date" />
                  <el-option label="DateTime" value="DateTime" />
                </el-select>
              </el-form-item>
              <el-form-item
                label="默认值"
                prop="paramDefault"
                :rules="filter_rules('默认值', { required: false })"
              >
                <el-input
                  v-model="procedureParamForm.paramDefault"
                  placeholder="默认值"
                />
              </el-form-item>
              <el-form-item
                label="是否必填"
                prop="paramRequired"
                :rules="filter_rules('是否必填', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.paramRequired"
                  placeholder="是否必填"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime'
                "
                label="组件类型"
                prop="componentType"
                :rules="filter_rules('组件类型', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.componentType"
                  placeholder="组件类型"
                >
                  <el-option label="输入框" value="input" />
                  <el-option label="下拉单选" value="select" />
                  <el-option label="下拉多选" value="mutiselect" />
                  <el-option label="下拉树(单选)" value="treeSelect" />
                  <el-option label="下拉树(多选)" value="multiTreeSelect" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime' &&
                    (procedureParamForm.componentType == 'select' ||
                      procedureParamForm.componentType == 'mutiselect')
                "
                key="selectType"
                label="选择内容来源"
                prop="selectType"
                :rules="filter_rules('选择内容来源', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.selectType"
                  placeholder="选择内容来源"
                  size="small"
                >
                  <el-option label="自定义" value="1" />
                  <el-option label="sql语句" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime' &&
                    ((procedureParamForm.componentType == 'select' &&
                      procedureParamForm.selectType == '2') ||
                      (procedureParamForm.componentType == 'mutiselect' &&
                        procedureParamForm.selectType == '2') ||
                      procedureParamForm.componentType == 'treeSelect' ||
                      procedureParamForm.componentType == 'multiTreeSelect')
                "
                label="选择数据源"
                prop="datasourceId"
                :rules="filter_rules('选择数据源', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.datasourceId"
                  placeholder="选择数据源"
                  size="small"
                >
                  <el-option
                    v-for="op in dataSource"
                    :key="op.datasourceId"
                    :label="op.dataSourceName"
                    :value="op.datasourceId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime' &&
                    procedureParamForm.componentType == 'select' &&
                    procedureParamForm.selectType == '2'
                "
                key="isRelyOnParams"
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.isRelyOnParams"
                  placeholder="是否依赖其他参数"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime' &&
                    procedureParamForm.componentType == 'multiTreeSelect'
                "
                key="checkStrictly"
                label="父子联动"
                prop="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.checkStrictly"
                  placeholder="选择父子联动"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                    procedureParamForm.paramType != 'DateTime' &&
                    (procedureParamForm.componentType == 'select' ||
                      procedureParamForm.componentType == 'mutiselect' ||
                      procedureParamForm.componentType == 'treeSelect' ||
                      procedureParamForm.componentType == 'multiTreeSelect')
                "
                key="selectContent"
                label="下拉选择内容"
                prop="selectContent"
                :rules="filter_rules('下拉选择内容', { required: true })"
              >
                <el-input
                  v-model="procedureParamForm.selectContent"
                  type="textarea"
                  :cols="80"
                  placeholder="下拉选择内容"
                  size="small"
                />
                <div class="sub-title">{{ selectContentSuggestion }}</div>
              </el-form-item>
              <el-form-item
                v-if="procedureParamForm.paramType == 'Date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select
                  v-model="procedureParamForm.dateFormat"
                  placeholder="日期格式"
                  size="small"
                >
                  <el-option label="年" value="yyyy" />
                  <el-option label="年-月" value="yyyy-MM" />
                  <el-option label="年-月-日" value="yyyy-MM-dd" />
                </el-select>
              </el-form-item>
              <el-form-item
                label="是否隐藏"
                prop="paramHidden"
                :rules="filter_rules('是否隐藏', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.paramHidden"
                  placeholder="是否隐藏"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addInParam">添加</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-tag
            v-if="
              procedureParamForm.paramType == 'Date' ||
                procedureParamForm.paramType == 'DateTime'
            "
            type="warning"
          >注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag>
          <el-tag
            v-if="
              procedureParamForm.componentType == 'select' ||
                procedureParamForm.componentType == 'mutiselect'
            "
            type="warning"
          >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
            注意：两个key必须是value 和 name</el-tag><br
            v-if="
              paramForm.paramType == 'select' ||
                paramForm.paramType == 'mutiselect'
            "
          >
          <el-tag
            v-if="
              procedureParamForm.componentType == 'select' ||
                procedureParamForm.componentType == 'mutiselect'
            "
            type="warning"
          >sql语句格式：select code as value, name as name from table
            注意：返回的属性中必须有 value 和 name</el-tag>
          <el-tag
            v-if="
              procedureParamForm.componentType == 'treeSelect' ||
                procedureParamForm.componentType == 'multiTreeSelect'
            "
            type="warning"
          >sql语句格式：select deptId as id, deptName as name,parentId as pid
            from table 注意：返回的属性中必须有 id,name和pid</el-tag>
          <div style="height: 40%">
            <!--表格 start-->
            <el-table
              :data="procedureInParamTableData.tableData"
              border
              style="width: 100%"
              align="center"
              size="small"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center" />
              <el-table-column
                prop="paramCode"
                label="参数编码"
                align="center"
              />
              <el-table-column
                prop="paramType"
                label="参数类型"
                align="center"
              />
              <el-table-column
                prop="paramDefault"
                label="默认值"
                align="center"
              />
              <el-table-column
                prop="paramHidden"
                label="是否隐藏"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              />
              <el-table-column
                fixed="right"
                label="操作"
                width="180"
                align="center"
              >
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="small"
                    @click="editInParam(scope.row)"
                  >编辑</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveUp(scope.$index, '1')"
                  >上移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveDown(scope.$index, '1')"
                  >下移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="deleteInParam(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
          <div class="parameter-warp">
            <div class="warp-title">输出参数</div>
            <el-form
              ref="outParamRef"
              :inline="true"
              :model="procedureOutParamForm"
              class="demo-form-inline"
              size="small"
            >
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input
                  v-model="procedureOutParamForm.paramName"
                  placeholder="参数名称"
                />
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input
                  v-model="procedureOutParamForm.paramCode"
                  placeholder="参数编码"
                />
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select
                  v-model="procedureOutParamForm.paramType"
                  placeholder="参数类型"
                >
                  <el-option label="VARCHAR" value="VARCHAR" />
                  <el-option label="INTEGER" value="INTEGER" />
                  <el-option label="BIGINT" value="BIGINT" />
                  <el-option label="FLOAT" value="FLOAT" />
                  <el-option label="DOUBLE" value="DOUBLE" />
                  <el-option label="DECIMAL" value="DECIMAL" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addOutParam">添加</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div style="height: 30%">
            <!--表格 start-->
            <el-table
              :data="procedureOutParamTableData.tableData"
              border
              style="width: 100%"
              align="center"
              size="small"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center" />
              <el-table-column
                prop="paramCode"
                label="参数编码"
                align="center"
              />
              <el-table-column
                prop="paramType"
                label="参数类型"
                align="center"
              />
              <el-table-column
                fixed="right"
                label="操作"
                width="180"
                align="center"
              >
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="small"
                    @click="editOutParam(scope.row)"
                  >编辑</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveUp(scope.$index, '2')"
                  >上移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="moveDown(scope.$index, '2')"
                  >下移</el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="deleteOutParam(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
        </div>
        <div class="parameter-warp">
              <div class="warp-title">子表参数</div>
               <el-select
                  v-model="subParamAttrs"
                  placeholder="主表字段"
                  size="small"
                  multiple
                  clearable
                >
                  <el-option
                    v-for="op in sqlColumnTableData.tableData"
                    :key="op.name"
                    :label="op.name"
                    :value="op.name"
                  />
                </el-select>
            </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeAddDataSet">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="addDataSet"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer
      title="循环块"
     :visible.sync="blockVisiable"
      custom-class="handle-drawer"
      class="handle-drawer"
      :modal="true"
      :close-on-click-modal="false"
      size="100%"
      @close="closeBlockDialog"
    >
      <el-form
        ref="blockRef"
        label-position="top"
        :model="blockForm"
        class="demo-form-inline"
      >
        <el-form-item
          key="startCell"
          label="起始单元格"
          prop="startCell"
          :rules="filter_rules('起始单元格', { required: true })"
        >
          <el-input
            v-model="blockForm.startCell"
            placeholder="起始单元格"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="endCell"
          label="结束单元格"
          prop="endCell"
          :rules="filter_rules('结束单元格', { required: true })"
        >
          <el-input
            v-model="blockForm.endCell"
            placeholder="结束单元格"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="aggregateType"
          label="聚合方式"
          prop="aggregateType"
          :rules="filter_rules('聚合方式', { required: true })"
        >
          <el-select
            v-model="blockForm.aggregateType"
            placeholder="聚合方式"
            size="small"
          >
            <el-option label="列表" value="list" />
            <el-option label="分组" value="group" />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="blockForm.aggregateType == 'group'"
          key="groupProperty"
          label="分组属性"
          prop="groupProperty"
          :rules="filter_rules('分组属性', { required: true })"
        >
          <el-input
            v-model="blockForm.groupProperty"
            placeholder="分组属性"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="hloopCount"
          label="横向循环次数"
          prop="hloopCount"
          :rules="filter_rules('横向循环次数', { required: true,type:'positiveInt' })"
        >
          <el-input
            v-model="blockForm.hloopCount"
            placeholder="横向循环次数"
            size="small"
          />
        </el-form-item>
          <el-form-item
          key="hloopEmptyCount"
          label="横向循环间隔空行数"
          prop="hloopEmptyCount"
          :rules="filter_rules('横向循环间隔空行数', { required: true,type:'positiveInt' })"
        >
          <el-input
            v-model="blockForm.hloopEmptyCount"
            placeholder="横向循环间隔空行数"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="vloopEmptyCount"
          label="纵向循环间隔空行数"
          prop="vloopEmptyCount"
          :rules="filter_rules('纵向循环间隔空行数', { required: true,type:'positiveInt' })"
        >
          <el-input
            v-model="blockForm.vloopEmptyCount"
            placeholder="纵向循环间隔空行数"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="subRange"
          label="子循环块范围"
          prop="subRange"
          :rules="filter_rules('子循环块范围', { required: false })"
        >
          <el-input
            v-model="blockForm.subBlockRange"
            placeholder="子循环块范围，例如: A1:A1"
            size="small"
          />
        </el-form-item>
      </el-form>
      <el-alert
        title="说明：起始结束单元格为单元格的坐标，如起始单元格坐标为A1，结束单元格坐标为E20，则循环块范围为A1:E20"
        type="warning"
        :closable="false"
      />
      <div class="handle-drawer__footer">
        <el-button size="small" @click="closeBlockDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddBlock"
        >确 定</el-button>
      </div>
    </el-drawer>
    <el-dialog
      title="单元格过滤"
      :visible.sync="cellConditionVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeCellConditionDialog"
    >
      <el-form
        ref="conditionRef"
        :model="cellConditionForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          key="property"
          label="属性"
          prop="property"
          :rules="filter_rules('属性', { required: true })"
        >
          <el-input
            v-model="cellConditionForm.property"
            placeholder="属性"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="operator"
          label="操作符"
          prop="operator"
          :rules="filter_rules('操作符', { required: true })"
        >
          <!-- <el-input v-model="cellConditionForm.operator" placeholder="操作符" size="small"></el-input> -->
          <el-select
            v-model="cellConditionForm.operator"
            placeholder="操作符"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.operate"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          key="type"
          label="数据类型"
          prop="type"
          :rules="filter_rules('数据类型', { required: true })"
        >
          <el-select
            v-model="cellConditionForm.type"
            placeholder="数据类型"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.type"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="cellConditionForm.type == 'date'"
          key="dateFormat"
          label="日期格式"
          prop="dateFormat"
          :rules="filter_rules('日期格式', { required: false })"
        >
          <el-select
            v-model="cellConditionForm.dateFormat"
            placeholder="日期格式"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.dateFormat2"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          key="value"
          label="条件值"
          prop="value"
          :rules="filter_rules('条件值', { required: true })"
        >
          <el-input
            v-model="cellConditionForm.value"
            type="textarea"
            :rows="2"
            style="width: 480px"
            placeholder="条件值"
            size="small"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="closeCellConditionDialog"
        >取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddCellCondition"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="单元格隐藏"
      :visible.sync="cellHiddenConditionVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeCellHiddenConditionDialog"
    >
      <el-form
        ref="hiddenConditionRef"
        :model="cellHiddenConditionForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          key="propertyName"
          label="参数名称"
          prop="propertyName"
          :rules="filter_rules('参数名称', { required: true })"
        >
          <el-input
            v-model="cellHiddenConditionForm.propertyName"
            placeholder="参数名称"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="property2"
          label="参数编码"
          prop="property"
          :rules="filter_rules('参数编码', { required: true })"
        >
          <el-input
            v-model="cellHiddenConditionForm.property"
            placeholder="参数编码"
            size="small"
          />
        </el-form-item>
        <el-form-item
          key="operator2"
          label="操作符"
          prop="operator"
          :rules="filter_rules('操作符', { required: true })"
        >
          <el-select
            v-model="cellHiddenConditionForm.operator"
            placeholder="操作符"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.operate"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          key="type2"
          label="数据类型"
          prop="type"
          :rules="filter_rules('数据类型', { required: true })"
        >
          <el-select
            v-model="cellHiddenConditionForm.type"
            placeholder="数据类型"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.type2"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="cellHiddenConditionForm.type == 'date'"
          key="dateFormat2"
          label="日期格式"
          prop="dateFormat"
          :rules="filter_rules('日期格式', { required: false })"
        >
          <el-select
            v-model="cellHiddenConditionForm.dateFormat"
            placeholder="日期格式"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.dateFormat2"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          key="value2"
          label="条件值"
          prop="value"
          :rules="filter_rules('条件值', { required: true })"
        >
          <el-input
            v-model="cellHiddenConditionForm.value"
            type="textarea"
            :rows="2"
            style="width: 480px"
            placeholder="条件值"
            size="small"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="closeCellHiddenConditionDialog"
        >取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddCellHiddenCondition"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="x轴属性"
      :visible.sync="xAxisVisiable"
      :close-on-click-modal="false"
      @close="closexAxisData"
    >
      <el-form
        ref="xAxisRef"
        :model="xAxisForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          label="图表"
          prop="chartId"
          :rules="filter_rules('图表', { required: true })"
        >
          <el-select
            v-model="xAxisForm.chartId"
            placeholder="图表"
            size="small"
            @change="getChartTile()"
          >
            <el-option
              v-for="op in sheetCharts"
              :key="op.chartId"
              :label="op.title"
              :value="op.chartId"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="数据来源"
          prop="dataType"
          :rules="filter_rules('数据来源', { required: true })"
        >
          <el-select
            v-model="xAxisForm.dataType"
            placeholder="数据来源"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.xAxisDataType"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="xAxisForm.dataType == 2"
          label="数据集"
          prop="datasetId"
          :rules="filter_rules('数据集', { required: true })"
        >
          <el-select
            v-model="xAxisForm.datasetId"
            placeholder="数据集"
            size="small"
            @change="getDatasetAttrs()"
          >
            <el-option
              v-for="op in datasets"
              :key="op.id"
              :label="op.datasetName"
              :value="op.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="xAxisForm.dataType == 2"
          label="数据集属性"
          prop="attr"
          :rules="filter_rules('数据集属性', { required: true })"
        >
          <el-select
            v-model="xAxisForm.attr"
            placeholder="数据集属性"
            size="small"
          >
            <el-option
              v-for="op in dataSetAttrs"
              :key="op.name"
              :label="op.name"
              :value="op.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="xAxisForm.dataType == 1"
          label="x轴数据"
          prop="xAxisDatas"
          :rules="filter_rules('x轴数据', { required: true })"
        >
          <el-input
            v-model="xAxisForm.xAxisDatas"
            type="textarea"
            :rows="2"
            style="width: 480px"
            placeholder="x轴数据"
            size="small"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closexAxisData">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddxAxisData"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="单元格小计"
      :visible.sync="cellSubTotalVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeSubtotalDialog"
    >
      <el-form
        ref="subtotalRef"
        :model="cellSubTotalForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          label="单元格"
          prop="coords"
          :rules="filter_rules('单元格', { required: true })"
        >
          <el-input
            v-model="cellSubTotalForm.coords"
            placeholder="单元格,例如A1"
            size="small"
          />
        </el-form-item>
        <el-form-item
          label="小计类型"
          prop="type"
          :rules="filter_rules('小计类型', { required: true })"
        >
          <el-select
            v-model="cellSubTotalForm.type"
            placeholder="小计类型"
            style="width: 100%"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.subtotalType"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-show="(cellSubTotalForm.type == '6' || cellSubTotalForm.type == '7')"
          label="本期属性"
          size="small"
        >
          <el-input
          v-model="cellSubTotalForm.compareAttr1"
          style="width: 100%"
          placeholder="本期属性"
         />
        </el-form-item>
          <el-form-item
            v-show="(cellSubTotalForm.type == '6' || cellSubTotalForm.type == '7')"
            label="同期属性"
            size="small"
          >
            <el-input
              v-model="cellSubTotalForm.compareAttr2"
              style="width: 100%"
              placeholder="同期属性"
            />
        </el-form-item>
        <el-form-item
          label="小数位数"
          size="small"
          :rules="filter_rules('小数位数', { required: true })"
        >
          <el-input
            v-model.number="cellSubTotalForm.digit"
            style="width: 100%"
            placeholder="小数位数"
          />
        </el-form-item>
        <el-form-item
          label="数值单位转换"
          size="small"
          class="df-form-item"
          prop="unitTransfer"
          :rules="filter_rules('数值单位转换', { required: true })"
        >
          <el-switch
            v-model="cellSubTotalForm.unitTransfer"
            active-text="是"
            inactive-text="否"
            />
          </el-form-item>
          <el-form-item
            v-if="cellSubTotalForm.unitTransfer"
            label="转换方式"
            size="small"
            prop="transferType"
            :rules="filter_rules('转换方式', { required: true })"
          >
            <el-select
            v-model="cellSubTotalForm.transferType"
            style="width: 100%"
            placeholder="转换方式"
            >
              <el-option label="乘法" :value="1" />
              <el-option label="除法" :value="2" />
              <el-option label="乘法并转成中文大写" :value="3" />
              <el-option label="除法并转成中文大写" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="cellSubTotalForm.unitTransfer"
            label="倍数"
            size="small"
            prop="multiple"
            :rules="filter_rules('倍数', { required: true })"
          >
            <el-select
            v-model="cellSubTotalForm.multiple"
            style="width: 100%"
            placeholder="倍数"
            >
            <el-option label="1" value="1" />
            <el-option label="10" value="10" />
            <el-option label="100" value="100" />
            <el-option label="1000" value="1000" />
            <el-option label="10000" value="10000" />
            <el-option label="100000" value="100000" />
            </el-select>
          </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeSubtotalDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddSubtotal"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="分组小计链"
      :visible.sync="subTotalCalcVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeSubtotalCalcDialog"
    >
      <el-form
        ref="subtotalCalcRef"
        :model="subTotalCalcForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          label="字段"
          prop="attrs"
          :rules="filter_rules('字段', { required: true })"
        >
          <el-select
            v-model="subTotalCalcForm.attrs"
            style="width: 100%"
            placeholder="请选择字段"
            size="small"
            multiple
            filterable
            clearable
          >
            <el-option
              v-for="op in dataSetAttrs"
              :key="op.name"
              :label="op.name"
              :value="op.name"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="closeSubtotalCalcDialog"
        >取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddSubtotalCalc"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="小计属性"
      :visible.sync="subTotalAttrsVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeSubtotalAttrDialog"
    >
      <el-form
        ref="subtotalAttrsRef"
        label-position="top"
        :model="subTotalAttrsForm"
        class="demo-form-inline"
      >
        <el-form-item
          label="小计名称"
          prop="name"
          :rules="filter_rules('小计名称', { required: true })"
        >
          <el-input
            v-model="subTotalAttrsForm.name"
            placeholder="小计名称"
            size="small"
          />
        </el-form-item>
        <el-form-item
          label="字体颜色"
          prop="fontColor"
          :rules="filter_rules('字体颜色', { required: true })"
        >
          <el-color-picker
            v-model="subTotalAttrsForm.fontColor"
            size="small"
            :predefine="commonConstants.predefineColors"
          />
        </el-form-item>
        <el-form-item
          label="字体大小"
          prop="fontSize"
          :rules="filter_rules('字体大小', { required: true })"
        >
          <el-select
            v-model="subTotalAttrsForm.fontSize"
            placeholder="请选择字体大小"
            size="small"
            clearable
          >
            <el-option
              v-for="op in selectUtil.fontSize"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="是否加粗"
          prop="fontWeight"
          :rules="filter_rules('是否加粗', { required: true })"
        >
          <el-switch v-model="subTotalAttrsForm.fontWeight" />
        </el-form-item>
        <el-form-item
          label="背景颜色"
          prop="bgColor"
          :rules="filter_rules('背景颜色', { required: true })"
        >
          <el-color-picker
            v-model="subTotalAttrsForm.bgColor"
            size="small"
            :predefine="commonConstants.predefineColors"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="closeSubtotalAttrDialog"
        >取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddSubtotalAttrs"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer
      :title="authTitle"
      :visible.sync="addAuthVisiable"
      custom-class="handle-drawer"
      class="handle-drawer"
      :modal="true"
      :close-on-click-modal="false"
      @close="closeAddAuth"
      size="100%"
    >
      <el-form
        ref="addAuthRef"
        label-position="top"
        :model="addAuthForm"
        class="demo-form-inline"
      >
        <!-- <el-transfer
                        v-model="addAuthForm.userIds"
                        :data="authUsers"
                        :titles="['用户信息', '授权用户']"
                        :filterable="true"
                        :props="{key:'id',label:'userName'}"
                    >
                    </el-transfer> -->
        <el-tree
          ref="tree"
          :data="authUsers"
          show-checkbox
          default-expand-all
          node-key="id"
          highlight-current
          :props="defaultProps"
          :default-checked-keys="defaultCheckedUsers"
        />
      </el-form>
      <div class="handle-drawer__footer">
        <el-button size="small" @click="closeAddAuth">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddAuth"
        >确 定</el-button>
      </div>
    </el-drawer>
    <modal
      ref="settingRef"
      :modal-config="settingModalConfig"
      :modal-form="settingModalForm"
      :modal-data="settingFormData"
      :modal-handles="settingModalHandles"
      @closeModal="closeSettingModal()"
    />
    <el-drawer
      :modal="false"
      :close-on-click-modal="false"
      :title="authedRangeTitle"
      :visible.sync="authdialogVisible"
      custom-class="handle-drawer"
      class="handle-drawer"
      @close="closeAuthDialog"
      size="100%"
    >
      <div v-if="authedRange && authedRange.length > 0" class="el-dialog-div">
        <div v-for="(item, index) in authedRange" :key="index">
          <el-descriptions title="" :column="1" border>
            <el-descriptions-item label="保护范围">{{
              item.rangeAxis
            }}</el-descriptions-item>
            <el-descriptions-item v-if="isCreator" label="授权人数">{{
              item.userIds.length
            }}</el-descriptions-item>
          </el-descriptions>
          <div v-if="isCreator" style="text-align: right; margin-top: 5px">
            <el-button
              type="primary"
              title="编辑"
              icon="el-icon-edit"
              circle
              size="mini"
              @click="editRange(item)"
            />
            <el-button
              type="warning"
              icon="el-icon-monitor"
              title="显示选区"
              circle
              size="mini"
              @click="showRange(item)"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              title="删除"
              circle
              size="mini"
              @click="deleteRange(item, index)"
            />
          </div>
          <el-divider content-position="left" />
        </div>
      </div>
      <el-empty
        v-if="(!authedRange || authedRange.length == 0) && isCreator"
        description="暂无授权信息"
      />
      <el-empty
        v-if="(!authedRange || authedRange.length == 0) && !isCreator"
        description="暂无操作权限"
      />
    </el-drawer>
    <!-- 左侧分组设置 -->
    <el-dialog
      :close-on-click-modal="false"
      title="分组设置"
      :visible.sync="groupSetVisible"
      width="504px"
      @close="closeGroupDialog"
    >
      <div class="group-dialog">
        <div class="df-c-b">
          <div class="title">全部分组（{{ groupList.length }}）</div>
          <el-button
            size="medium"
            @click="openGroupHandleDialog()"
          >新增分组</el-button>
        </div>
        <el-table
          size="small"
          :data="groupList"
          style="width: 100%; margin-top: 12px"
        >
          <el-table-column prop="groupName" label="选项" />
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-link
                type="info"
                style="margin-right: 12px"
                @click="openGroupHandleDialog(scope.row)"
                v-show="scope.row.id != 0"
              >编辑</el-link>
              <el-link
                type="info"
                @click="deleteGroup(scope.row)"
                v-show="scope.row.id != 0"
              >删除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
    <!-- 新增编辑分组 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="groupForm.id ? '编辑分组' : '新增分组'"
      :visible.sync="groupHandleVisible"
      width="417px"
      @close="closeGroupHandleDialog"
    >
      <el-input
        v-model="groupForm.groupName"
        size="medium"
        placeholder="请输入"
      />
      <span slot="footer" class="dialog-footer">
        <el-button
          size="small"
          @click="closeGroupHandleDialog()"
        >取 消</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="groupHandleLoading"
          @click="addOrEditGroup()"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="单元格比较"
      :visible.sync="cellCompareVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeCompareCells"
    >
      <el-form
        ref="compareRef"
        :model="cellCompareForm"
        class="demo-form-inline"
        label-position="top"
      >
        <el-form-item
          label="sheet名称"
          prop="sheetName"
          :rules="filter_rules('sheet名称', { required: true })"
        >
          <el-select
            v-model="cellCompareForm.sheetName"
            placeholder="sheet名称"
            size="small"
            @focus="onfocuseSheet"
          >
            <el-option
              v-for="op in sheets"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="坐标"
          prop="coordinate"
          :rules="filter_rules('坐标', { required: true })"
        >
          <el-input
            v-model="cellCompareForm.coordinate"
            placeholder="坐标"
            size="small"
          />
        </el-form-item>
        <el-form-item
          label="单元格类型"
          prop="cellType"
          :rules="filter_rules('单元格类型', { required: true })"
        >
          <el-select
            v-model="cellCompareForm.cellType"
            placeholder="单元格类型"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.cellType"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="比较类型"
          prop="compareType"
          :rules="filter_rules('比较类型', { required: true })"
        >
          <el-select
            v-model="cellCompareForm.compareType"
            placeholder="比较类型"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.operate"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeCompareCells">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddCompareCells"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer
      title="填报属性"
      :visible.sync="datasourceDialog"
      size="100%"
      :modal="false"
      :modal-append-to-body="false"
      custom-class="test"
      :close-on-press-escape="false"
      :wrapper-closable="false"
      class="handle-drawer"
      @close="closeDatasourceDialog"
      style="border: 1px solid #eee;"
    >
      <div class="table-box df">
        <div class="table-box-left">
          <div class="table-header df-c-b">
            <span class="attr-dataset-title">填报属性（{{ datasources.length }}）</span>
            <el-button
              class="addBtn"
              @click="addDatasourceAttr"
            ><i class="el-icon-plus el-icon--left" />添加</el-button>
          </div>
          <div class="table-body">
            <div v-for="(o, index) in datasources" :key="index">
              <div
                class="df-c dataset-attr"
                :class="o.isActive ? 'dataset-attr-active' : ''"
                style="position: relative"
              >
                <span
                  class="dataset-name2 overflow-text"
                  :title="o.name"
                  @click="clickAttrName(o, index)"
                >{{ o.name }}</span>
                <div class="action-box df-c">
                  <div
                    class="action action-edit"
                    @click="editDatasourceAttr(o)"
                  />
                  <div
                    class="action action-del"
                    @click="deleteDatasourceAttr(index)"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="table-box-right">
          <div v-show="datasourceAttr.name">
            <el-form
              :model="datasourceAttr"
              class="table-form"
              label-width="42px"
              label-position="left"
            >
              <el-form-item label="数据源" prop="datasourceId">
                <el-select
                  v-model="datasourceAttr.datasourceId"
                  style="width: 100%"
                  placeholder="数据源"
                  size="small"
                  @change="getFormsDatabaseTables"
                >
                  <el-option
                    v-for="op in dataSource"
                    :key="op.datasourceId"
                    :label="op.dataSourceName"
                    :value="op.datasourceId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="表" prop="table">
                <el-select
                  v-model="datasourceAttr.table"
                  style="width: 100%"
                  placeholder="表"
                  size="small"
                  filterable
                  @change="getFormsTableColumns"
                >
                  <el-option
                    v-for="op in dataSourceTables"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
              </el-form-item>
            </el-form>

            <div class="table-card">
              <div class="table-header df-c">
                <span class="attr-dataset-title">主数据集</span>
                <el-switch
                    v-model="datasourceAttr.isMain"
                  />
              </div>
              <div class="table-header df-c">
                <span class="attr-dataset-title">表属性单元格关联</span>
                <el-button
                  class="addBtn"
                  @click="addDatasourceColumn"
                ><i class="el-icon-plus el-icon--left" />添加</el-button>
              </div>

              <el-table
                :data="datasourceAttr.tableDatas"
                border
                style="width: 100%"
                align="center"
                height="280"
                size="small"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="columnName" label="列名" align="center" />
                <el-table-column
                  prop="cellCoords"
                  label="单元格坐标"
                  align="center"
                />
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="deleteDatasourceColumn(scope.$index)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div class="table-card" style="margin-top: 12px">
              <div class="table-header df-c">
                <span class="attr-dataset-title">表主键</span>
                <el-button
                  class="addBtn"
                  @click="addDatasourceKey"
                ><i class="el-icon-plus el-icon--left" />添加</el-button>
              </div>
              <el-table
                :data="datasourceAttr.keys"
                border
                style="width: 100%"
                height="200"
                align="center"
                size="small"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column
                  prop="columnName"
                  label="主键属性"
                  align="center"
                />
                <el-table-column prop="idType" label="主键规则" align="center">
                  <template slot-scope="scope">
                    <span>
                      <span v-if="scope.row.idType == '1'">自定义填写</span>
                      <span v-else-if="scope.row.idType == '2'">雪花算法</span>
                      <span v-else>自增主键</span>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="deleteDatasourceKey(scope.$index)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="table-card" style="margin-top: 12px" v-if="!datasourceAttr.isMain">
              <div class="table-header df-c">
                <span class="attr-dataset-title">主子表关联</span>
                <el-button
                  class="addBtn"
                  @click="addMainAttr"
                ><i class="el-icon-plus el-icon--left" />添加</el-button>
              </div>
              <el-table
                :data="datasourceAttr.mainAttrs"
                border
                style="width: 100%"
                height="200"
                align="center"
                size="small"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column
                  prop="mainName"
                  label="数据源属性"
                  align="center"
                />
                <el-table-column prop="mainTable" label="主表" align="center">
                </el-table-column>
                <el-table-column prop="mainColumn" label="主表字段" align="center">
                </el-table-column>
                <el-table-column prop="columnName" label="子表字段" align="center">
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="deleteMainAttr(scope.$index)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="table-card" style="margin-top: 12px">
              <div class="table-header df-c">
                <span class="attr-dataset-title">自动填充字段</span>
                <el-button
                  class="addBtn"
                  @click="addAutoFillAttr"
                ><i class="el-icon-plus el-icon--left" />添加</el-button>
              </div>
              <el-table
                :data="datasourceAttr.autoFillAttrs"
                border
                style="width: 100%"
                height="200"
                align="center"
                size="small"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column
                  prop="columnName"
                  label="列名"
                  align="center"
                />
                <el-table-column prop="fillType" label="填充内容" align="center">
                  <template slot-scope="scope">
                    <span>
                      <span v-if="scope.row.fillType == '1'">系统时间</span>
                      <span v-else-if="scope.row.fillType == '2'">用户id</span>
                      <span v-else-if="scope.row.fillType == '3'">用户名</span>
                      <span v-else-if="scope.row.fillType == '4'">商户号</span>
                      <span v-else-if="scope.row.fillType == '5'">单元格</span>
                      <span v-else>自定义</span>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="fillValue" label="填充值" align="center">
                </el-table-column>
                <el-table-column prop="fillStrategy" label="填充策略" align="center">
                  <template slot-scope="scope">
                    <span>
                      <span v-if="scope.row.fillStrategy == '1'">插入数据</span>
                      <span v-else-if="scope.row.fillStrategy == '2'">更新数据</span>
                      <span v-else-if="scope.row.fillStrategy == '3'">插入/更新数据</span>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="deleteAutoFillAttr(scope.$index)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div class="table-card" style="margin-top: 12px">
              <div class="table-header df-c">
                <span class="attr-dataset-title">数据删除方式</span>
                <el-button
                  class="addBtn"
                  @click="addDeleteType"
                ><i class="el-icon-plus el-icon--left" />添加</el-button>
              </div>
              <el-table
                :data="datasourceAttr.deleteTypes"
                border
                style="width: 100%"
                height="200"
                align="center"
                size="small"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="deleteType" label="删除方式" align="center">
                  <template slot-scope="scope">
                    <span>
                      <span v-if="scope.row.deleteType == '1'">物理删除</span>
                      <span v-else-if="scope.row.deleteType == '2'">逻辑删除</span>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="columnName"
                  label="列名"
                  align="center"
                />
                <el-table-column prop="deleteValue" label="删除值" align="center">
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="deleteDeleteType(scope.$index)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </div>
      <span class="handle-drawer__footer">
        <el-button size="small" @click="closeDatasourceDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAddDatasource"
        >确 定</el-button>
      </span>
    </el-drawer>
    <el-dialog
      title="添加数据源绑定"
      :visible.sync="datasourceAttrDialog"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeDatasourceAttr"
    >
      <el-form
        ref="datasourceAttrRef"
        label-position="top"
        :model="datasourceAttrForm"
        class="demo-form-inline"
      >
        <el-form-item
          label="绑定名称"
          prop="name"
          :rules="filter_rules('绑定名称', { required: true })"
        >
          <el-input
            v-model="datasourceAttrForm.name"
            placeholder="绑定名称"
            size="small"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDatasourceAttr">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmDatasourceAttr"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer
      title="添加属性关联"
      :visible.sync="datasourceColumnDialog"
      custom-class="handle-drawer"
      class="handle-drawer"
      :modal="false"
      :close-on-click-modal="false"
      @close="closeDatasourceColumn"
      size="100%"
    >
      <el-form
        ref="datasourceColumnRef"
        label-position="top"
        :model="datasourceColumnForm"
        class="demo-form-inline"
      >
        <el-form-item label="列名" prop="columnName">
          <el-select
            v-model="datasourceColumnForm.columnName"
            style="width: 100%"
            placeholder="列名"
            size="small"
            filterable
            :rules="filter_rules('列名', { required: true })"
          >
            <el-option
              v-for="op in tableColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="单元格坐标"
          prop="cellCoords"
          :rules="filter_rules('单元格坐标', { required: true })"
        >
          <el-input
            v-model="datasourceColumnForm.cellCoords"
            placeholder="单元格坐标"
            size="small"
          />
        </el-form-item>
      </el-form>
      <div class="handle-drawer__footer">
        <el-button size="small" @click="closeDatasourceColumn">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmDatasourceColumn"
        >确 定</el-button>
      </div>
    </el-drawer>
    <el-dialog
      title="添加主键"
      :visible.sync="datasourceKeyDialog"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeDatasourceKey"
    >
      <el-form
        ref="datasourceKeyRef"
        label-position="top"
        :model="datasourceKeyForm"
        class="demo-form-inline"
      >
        <el-form-item label="主键列名" prop="columnName">
          <el-select
            v-model="datasourceKeyForm.columnName"
            style="width: 100%"
            placeholder="列名"
            filterable
            size="small"
            :rules="filter_rules('列名', { required: true })"
          >
            <el-option
              v-for="op in tableColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="主键生成规则"
          prop="idType"
          :rules="filter_rules('主键生成规则', { required: true })"
        >
          <el-select
            v-model="datasourceKeyForm.idType"
            style="width: 100%"
            placeholder="主键生成规则"
            size="small"
          >
            <el-option label="自定义填写" value="1" />
            <el-option label="雪花算法" value="2" />
            <el-option label="自增主键" value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDatasourceKey">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmDatasourceKey"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="添加自动填充列"
      :visible.sync="autoFillDialog"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeAutoFillAttr"
    >
      <el-form
        ref="autoFillRef"
        label-position="top"
        :model="autoFillForm"
        class="demo-form-inline"
      >
        <el-form-item label="列名" prop="columnName" :rules="filter_rules('列名', { required: true })">
          <el-select
            v-model="autoFillForm.columnName"
            style="width: 100%"
            placeholder="列名"
            filterable
            size="small"
          >
            <el-option
              v-for="op in tableColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="填充类型"
          prop="fillType"
          :rules="filter_rules('填充类型', { required: true })"
        >
          <el-select
            v-model="autoFillForm.fillType"
            style="width: 100%"
            placeholder="填充类型"
            size="small"
          >
            <el-option label="系统时间" value="1" />
            <el-option label="用户id" value="2" />
            <el-option label="用户名" value="3" />
            <el-option label="商户号" value="4" />
            <el-option label="单元格" value="5" />
            <el-option label="自定义值" value="99" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="autoFillForm.fillType == '99' || autoFillForm.fillType == '5'"
          label="填充值"
          prop="fillValue"
          :rules="filter_rules('填充值', { required: true })"
        >
          <el-input
              v-model="autoFillForm.fillValue"
              placeholder="填充值"
            />
        </el-form-item>
        <el-form-item
          label="填充策略"
          prop="fillType"
          :rules="filter_rules('填充策略', { required: true })"
        >
          <el-select
            v-model="autoFillForm.fillStrategy"
            style="width: 100%"
            placeholder="填充类型"
            size="small"
          >
            <el-option label="插入数据" value="1" />
            <el-option label="更新数据" value="2" />
            <el-option label="插入/更新数据" value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeAutoFillAttr">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmAutoFillAttr"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="添加删除方式"
      :visible.sync="deleteTypeDialog"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeDeleteType"
    >
      <el-form
        ref="deleteTypeRef"
        label-position="top"
        :model="deleteTypeForm"
        class="demo-form-inline"
      >
        <el-form-item label="删除方式" prop="deleteType" :rules="filter_rules('删除方式', { required: true })">
          <el-select
            v-model="deleteTypeForm.deleteType"
            style="width: 100%"
            placeholder="删除方式"
            filterable
            size="small"
          >
            <el-option label="物理删除" value="1" />
            <el-option label="逻辑删除" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="deleteTypeForm.deleteType == '2'"
          label="数据列"
          prop="columnName"
          :rules="filter_rules('数据列', { required: true })"
        >
          <el-select
            v-model="deleteTypeForm.columnName"
            style="width: 100%"
            placeholder="数据列"
            size="small"
          >
           <el-option
              v-for="op in tableColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="deleteTypeForm.deleteType == '2'"
          label="删除值"
          prop="deleteValue"
          :rules="filter_rules('删除值', { required: true })"
        >
          <el-input
              v-model="deleteTypeForm.deleteValue"
              placeholder="删除值"
            />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDeleteType">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmDeleteType"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="添加主子表关联"
      :visible.sync="datasourceMainDialog"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeMainForm"
    >
      <el-form
        ref="datasourceMainRef"
        label-position="top"
        :model="datasourceMainForm"
        class="demo-form-inline"
      >
       <el-form-item label="主数据源" prop="columnName">
          <el-select
            v-model="datasourceMainForm.mainName"
            style="width: 100%"
            placeholder="主数据源"
            size="small"
            filterable
            :rules="filter_rules('主数据源', { required: true })"
            @change="changeMainName"
          >
            <el-option
              v-for="op in datasources"
              :key="op.name"
              :label="op.name"
              :value="op.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="主表名称"
          prop="mainTable"
          :rules="filter_rules('主表名称', { required: true })"
        >
          <el-input
            v-model="datasourceMainForm.mainTable"
            placeholder="主表名称"
            size="small" disabled
          />
        </el-form-item>
        <el-form-item label="主表主键列" prop="columnName">
          <el-select
            v-model="datasourceMainForm.mainColumn"
            style="width: 100%"
            placeholder="主表主键列"
            size="small"
            filterable
            :rules="filter_rules('主表主键列', { required: true })"
          >
            <el-option
              v-for="op in tableMainColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="子表列" prop="columnName">
          <el-select
            v-model="datasourceMainForm.columnName"
            style="width: 100%"
            placeholder="列名"
            size="small"
            filterable
            :rules="filter_rules('列名', { required: true })"
          >
            <el-option
              v-for="op in tableColumns"
              :key="op.columnName"
              :label="op.columnName"
              :value="op.columnName"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeMainForm">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmMainAttr"
        >确 定</el-button>
      </span>
    </el-dialog>
    <vchart :show.sync="vchartShow" @closeModal="closeAddChartModal()" />
    <textarea
      id="clipboradInput"
      value=""
      style="opacity: 0; position: absolute"
    />
  </div>
</template>

<script src="./luckyReportDesign.js"></script>

<style scoped lang="scss">
@import "@/element-variables.scss";

.pagebox {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}

.left {
  box-sizing: border-box;
  width: 460px;
  height: 99vh;
  background: #ffffff;
  // box-shadow: 0px 4px 8px #D2E3FF;
  overflow-y: auto;
  overflow-x: hidden;
  border-top: 1px solid #e7e7e7;

  .group-list {
    padding: 8px;
    border-radius: 4px;
    background: #f1f2f3;
    height: calc(100% - 170px);
    overflow-y: auto;
    ::v-deep .el-collapse-item__header {
      height: 18px;
      line-height: 18px;
      background-color: transparent;
      color: #979797;
      font-size: 12px;
      margin-bottom: 8px;
    }
    ::v-deep .el-collapse-item__wrap {
      background-color: transparent;
    }
    ::v-deep .el-collapse-item__content {
      padding-bottom: 0;
      background-color: transparent;
    }
    .dataset-item {
      width: 188px;
      box-sizing: border-box;
      border-radius: 4px;
      background: #fff;
      color: #3c3c3c;
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 32px;
      height: 32px;
      margin-bottom: 8px;
      padding: 0 10px;
      cursor: pointer;
      transition: all 0.3s;

      .action-box {
        .action {
          width: 18px;
          height: 18px;
          background-size: 100% 100%;
        }
        .action-edit {
          background-image: url("~@/static/img/sheet/dataset-edit.png");
          margin-right: 4px;
        }
        .action-del {
          background-image: url("~@/static/img/sheet/dataset-del.png");
        }
      }
    }
    .dataset-item:hover,
    .dataset-item-active {
      background: $--color-primary;
      color: #fff;
      .action-edit {
        background-image: url("~@/static/img/sheet/dataset-edit-active.png") !important;
      }
      .action-del {
        background-image: url("~@/static/img/sheet/dataset-del-active.png") !important;
      }
    }
  }
  .dataset-fields {
    .group-list {
      padding: 0px;
      border-radius: 0px;
      background: #fff;
      height: calc(100% - 154px);
      .dataset-item {
        width: 100%;
        background: #f1f2f3;
        border: 1px solid #f1f2f3;
        box-sizing: border-box;
        .action-box {
          .action-edit {
            background-image: url("~@/static/img/sheet/dataset-copy.png");
            margin-right: 4px;
          }
          .action-del {
            background-image: url("~@/static/img/sheet/dataset-drag.png");
          }
        }
      }
      .dataset-item:hover,
      .dataset-item-active {
        background: #fff;
        color: $--color-primary;
        border: 1px solid $--color-primary;
        .action-edit {
          background-image: url("~@/static/img/sheet/dataset-copy-active.png") !important;
        }
        .action-del {
          background-image: url("~@/static/img/sheet/dataset-drag-active.png") !important;
        }
      }
    }
  }
}
.left-head {
  box-sizing: border-box;
  position: absolute;
  width: 240px;
  height: 32px;
  left: 0px;
  background: #ffffff;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.left-head .el-icon-s-fold {
  position: absolute;
  right: 5%;
  top: 18.75%;
  bottom: 18.66%;
}
.left-dataset-title {
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding: 0px 16px;
  width: 460px;
  height: 48px;
  // border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  flex: none;
  order: 1;
  flex-grow: 0;
  background: #fff;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.04);
  .add-dataset {
    border-radius: 4px;
    background-color: #fff;
    &:hover {
      // color: $--color-primary;
    }
  }
  // margin-top: 2px;
}

.add-dataset-dialog {
  ::v-deep .el-radio-button:first-child .el-radio-button__inner {
    border-radius: 10px 0 0 10px;
  }
  ::v-deep .el-radio-button:last-child .el-radio-button__inner {
    border-radius: 0 10px 10px 0;
  }
  .variable-content {
    border: 1px solid #e4e9ed;
    background: #fafafa;
    width: 42.5%;

    flex-shrink: 0;
    .variable-title {
      height: 46px;
      padding: 0 16px;
      line-height: 46px;
      color: #1a1a1a;
      font-size: 14px;
      font-style: normal;
      font-weight: bold;
      border-bottom: 1px solid #EFEBEB;
    }
    .variable-warp{
      padding: 9px 17px;
      .variable-warp-title{
        color: #979191;
        font-size: 12px;
        font-style: normal;
        font-weight: bold;
        line-height: 22px; /* 183.333% */
        margin-bottom: 12px;
      }
      .variable-list{
        flex-wrap: wrap;
        .variable-item{
          width: calc((100% - 18px)/3);
          box-sizing: border-box;
          padding: 0 10px;
          border-radius: 4px;
          background: #E1F2F0;
          height: 32px;
          line-height: 32px;
          color: #595959;
          font-size: 12px;
          margin-right: 9px;
          transition: all 0.3s;
          margin-bottom: 12px;
          cursor: pointer;
          &:hover{
            color: #fff;
            background: $--color-primary;
            ::v-deep .el-icon-circle-plus-outline {
              color: #fff;
            }
            ::v-deep .el-icon-copy-document {
              color: #fff;
            }
          }
          &:nth-child(3n){
            margin-right: 0;
          }
        }
      }
      .analysis-list{
        border-radius: 3px;
        border: 1px solid #C1E0D9;
        background: #FFF;
        padding: 8px 10px 0;
        .variable-item{
          border-radius: 4px;
          background: #F1F2F3;
        }
      }
    }
  }
  .sql-content{
    flex: 1;
    position: relative;
    margin-left: 24px;

    .left-action {
      left: -20px;
      border-radius: 0 3px 3px 0;
    }
    .action-icon {
      cursor: pointer;
      transition: all 0.3s;
      position: absolute;
      top: 50%;

      transform: translateY(-50%);
      z-index: 999;
      background-color: $--color-primary;
      width: 10px;
      height: 48px;
      &:hover {
        opacity: 0.7;
      }
    }
  }
  .table-warp{
    width:96%;
    padding: 10px;
    border: 1px solid #eee;
    .table-title{
      color: #1A1A1A;
      font-feature-settings: 'liga' off, 'clig' off;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: bold;
      line-height: normal;
      margin-bottom: 14px;
    }
  }
  .parameter-content{
    .parameter-warp{
        border-radius: 4px;
        background: #f7f9fc;
        padding: 0 14px;
        .warp-title{
           height: 56px;
           line-height: 56px;
           font-size: 14px;
           font-weight: bold;
           color: #1A1A1A;
        }
    }
  }
}

.group-dialog {
  ::v-deep .el-table th.el-table__cell {
    background-color: #fafafa;
    color: rgba(0, 0, 0, 0.85);
  }
}

.dataset-title {
  width: 80px;
  height: 22px;
  font-style: normal;
  font-weight: 500;
  font-size: 16px;
  line-height: 22px;
  color: rgba(0, 0, 0, 0.9);
  flex: none;
  order: 0;
  flex-grow: 0;
}
.left-dataset-content {
  width: 460px;
  height: calc(100% - 48px);
  .dataset-group,
  .dataset-fields {
    flex: 1;
    padding: 0 12px;
    flex-shrink: 0;
    width: 50%;
    .section-name {
      height: 32px;
      color: #666;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: bold;
      line-height: normal;
      line-height: 32px;
      margin-bottom: 4px;
      .set-group {
        cursor: pointer;
      }
      .setting-text {
        color: $--color-primary;
        font-family: "PingFang SC";
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: normal;
        height: 17px;
        margin-left: 1px;
      }
    }
    .search {
      margin-bottom: 10px;
      ::v-deep .el-input__inner {
        height: 36px;
        line-height: 36px;
        border-radius: 6px;
        border: 1px solid rgba(0, 0, 0, 0.1);
      }
      ::v-deep .el-input__icon {
        line-height: 36px;
      }
    }
  }
  .dataset-group {
    border-right: 1px solid #f5f5f5;
  }
}
.right-dataset-title {
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 62px;
  height: 32px;
  background: #ffffff;
  /* border-bottom: 1px solid rgba(0, 0, 0, 0.1); */
  flex: none;
  order: 1;
  flex-grow: 0;
  margin-top: 2px;
}
.attr-dataset-title {
  width: fit-content;
  height: 20px;
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 20px;
  color: #292e33;
  flex: none;
  order: 0;
  flex-grow: 0;
  font-weight: bold;
}
.addBtn {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 2px 8px;
  height: 22px;
  background: #17b794;
  border-radius: 3px;
  flex: none;
  order: 1;
  flex-grow: 0;
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
  line-height: 18px;
  color: #ffffff;
}
.el-icon-arrow-right {
  position: absolute;
  left: 5.25%;
  cursor: pointer;
}

.el-icon-edit {
  position: absolute;
  right: 17.3%;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/edit.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}
.el-icon-delete {
  position: absolute;
  right: 7.3%;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/del.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}

.right-el-icon-edit {
  position: absolute;
  right: 74px;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/edit.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}
::v-deep .el-collapse-item__arrow {
  margin-right: 0;
}
.right-block-el-icon-edit {
  position: absolute;
  right: 52px;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/edit.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}
.right-el-icon-copy-document {
  position: absolute;
  right: 52px;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/copy.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}
.right-el-icon-delete {
  position: absolute;
  right: 30px;
  cursor: pointer;
  width: 14px;
  height: 14px;
  top: 9px;
  background-image: url("~@/static/img/sheet/del.png");
  background-size: 14px 14px;
  background-repeat: no-repeat;
}

.dataset-box-content {
  width: 240px;
  min-height: 150px;
  /* background: #A5C3F5; */
  flex: none;
  order: 4;
  flex-grow: 0;
  max-height: 400px;
  overflow-y: auto;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  padding-left: 5px;
  padding-top: 3px;
}

.right-form {
  .column-tag {
    width: 100% !important;
    height: 14px;
    background: transparent;
    color: #595959;
    font-family: "PingFang SC";
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 14px;
    border: 0;
    padding: 0;
  }
  .img-tag{

  }
  .switch-tag{
    height:40px
  }
}
.column-tag {
  max-width: 150px;
  height: 30px;
  background: #f7bb61;
  border-radius: 2px;
  color: rgba(0, 0, 0, 0.6);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: inline-block;
  padding: 0 10px;
  height: 32px;
  line-height: 30px;
  font-size: 12px;
  border-radius: 4px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border: 1px solid rgba(64, 158, 255, 0.2);
  font-weight: bold;
}
.center {
  flex: 1;
  position: relative;
  margin: 0 10px;
  .left-action {
    left: -10px;
    border-radius: 0 3px 3px 0;
  }
  .right-action {
    border-radius: 3px 0 0 3px;
    right: -13px;
  }
  .action-icon {
    cursor: pointer;
    transition: all 0.3s;
    position: absolute;
    top: 50%;

    transform: translateY(-50%);
    z-index: 999;
    background-color: $--color-primary;
    width: 10px;
    height: 48px;
    &:hover {
      opacity: 0.7;
    }
  }
  // height: 100vh;
}
.right {
  // top:50px;
  width: 306px;
  height: 99vh;
  background: #ffffff;
  // box-shadow: 0px 4px 8px #D2E3FF;
}
.right-head {
  // top:50px;
  box-sizing: border-box;
  position: absolute;
  width: 306px;
  height: 32px;
  right: 0px;
  background: #ffffff;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.right-head .el-icon-s-unfold {
  position: absolute;
  left: 5%;
  top: 18.75%;
  bottom: 18.66%;
}
.right-title {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  width: 306px;
  height: 48px;
  background: #e7e7e7;
  flex: none;
  order: 1;
  align-self: stretch;
  flex-grow: 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  background: #fff;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.04);
}
.cell-property {
  display: inline-block;
  width: 50%;
  height: 48px;
  line-height: 48px;
  font-size: 16px;
  cursor: pointer;
  color: #1a1a1a;
  font-weight: 500;
  position: relative;
  text-align: center;
  &::before {
    position: absolute;
    content: "";
    left: 50%;
    bottom: 0;
    transform: translateX(-50%);
    background: $--color-primary;
    width: 51px;
    height: 3px;
  }
}
.cell-property-noactive {
  color: #666;
  &::before {
    width: 0;
  }
}
.cell-label {
  color: #292e33;
  font-family: "PingFang SC";
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 12px;
  margin-right: 12px;
}
::v-deep .sub-collapse {
  margin-top: 6px;
  border-bottom: 0;
  .el-collapse-item {
    border-left: 1px solid rgba(0, 0, 0, 0.05);
    border-right: 1px solid rgba(0, 0, 0, 0.05);
    margin-bottom: 8px;
  }
  .el-collapse-item__header {
    padding-left: 16px !important;
  }
  .el-collapse-item__wrap {
    background-color: #fafafa !important;
    padding: 5px 12px 10px 12px !important;
    border-bottom: 0;
    .el-collapse-item__content {
      background-color: #fff;
      padding: 5px 7px;
      border-radius: 3px;
    }
  }
}
.right-form {
  position: absolute;
  width: 306px;
  height: calc(100vh - 64px - 10px - 48px - 10px);
  right: 0px;
  // top: 50px;
  background: #ffffff;
  overflow: auto;
  ::v-deep .el-collapse-item {
    .el-collapse-item__header {
      position: relative;
      padding: 0 12px;
      height: 36px;
      line-height: 36px;
      background-color: #f9fafa;
      color: #666;
      font-size: 14px;
      font-weight: bold;
    }
    .el-collapse-item__wrap {
      padding: 16px;
      background-color: #fff;
    }
    .el-collapse-item__content {
      padding-bottom: 0;
    }
  }
  .df-form-item {
    display: flex;
    ::v-deep .el-form-item__label {
      margin-right: 12px;
    }
  }
  ::v-deep .el-form-item {
    margin-bottom: 8px;
  }
  ::v-deep .el-form--label-top .el-form-item__label {
    padding-bottom: 0;
    line-height: 32px;
  }
}
::v-deep .demo-form-inline.el-form--label-top .el-form-item__label {
  padding-bottom: 0;
  line-height: 32px;
}
::v-deep .demo-form-inline .el-select,
::v-deep .demo-form-inline .el-textarea,
::v-deep .demo-form-inline .el-input {
  width: 100% !important;
}
.df-form {
  display: flex;
  flex-wrap: wrap;
  ::v-deep .el-form-item {
    width: 20%;
    display: flex;
    &:nth-child(5n) {
      margin-right: 0;
    }
    .el-form-item__label {
      flex-shrink: 0;
      // width: 80px;
    }
    .el-form-item__content {
      width: calc(100% - 92px);
    }
  }
}

.right-form::-webkit-scrollbar {
  width: 14px;
  height: 14px;
}

.right-form::-webkit-scrollbar-track,
::-webkit-scrollbar-thumb {
  border-radius: 999px;
  border: 5px solid transparent;
}

.right-form::-webkit-scrollbar-track {
  box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2) inset;
}

.right-form::-webkit-scrollbar-thumb {
  min-height: 20px;
  background-clip: content-box;
  box-shadow: 0 0 0 5px rgba(0, 0, 0, 0.2) inset;
}

.right-form::-webkit-scrollbar-corner {
  background: transparent;
}

.blockBtn {
  width: 230px;
  height: 30px;
  background: #17b794;
  border-radius: 4px;
  line-height: 5px;
  border-color: #17b794;
}
.contentbox {
  display: flex;
}
.dataset-box-content::-webkit-scrollbar {
  width: 5px;
}
/*修改左侧垂直滚动条的样式*/
.left::-webkit-scrollbar {
  width: 5px;
}
/*修改左侧垂直滚动条的样式*/
.tplname {
  padding: 0px 16px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 18px;
  line-height: 30px;
  color: #1a1a1a;
  font-weight: bold;
  margin: 5px 0;
}
.header-box {
  margin-bottom: 10px;
}
._header {
  height: 64px !important;
  padding: 0px;
  background-color: #fff;
  //   border-bottom: 1px solid #ccc;
  .headerRight {
    padding-right: 24px;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.9);
    .role-name {
      margin-right: 8px;
    }
  }
  .el-dropdown-link {
    color: rgba(0, 0, 0, 0.9);
  }
}
::v-deep .el-avatar {
  background: #17b794 !important;
}

::v-deep .el-dialog__wrapper {
  overflow: hidden;
  //    z-index: 2005 !important;
  pointer-events: none !important;
}

::v-deep .el-dialog {
  pointer-events: auto !important;
  /* background:#d9ebf0 !important; */
}
::v-deep .authdialog {
  margin-top: 75px !important;
  margin-right: 0px !important;
  flex-direction: column !important;
  // overflow: hidden !important;
  max-height: calc(100% - 90px) !important;
  top: 0 !important;
  right: 5px !important;
  bottom: 0;
  pointer-events: auto !important;
  /* background:#d9ebf0 !important; */
}
.authdialog ::v-deep .el-dialog__body {
  height: calc(100% - 90px) !important;
  overflow: auto;
}
.authdialog ::v-deep .el-dialog-div {
  max-height: 60vh;
  overflow: auto;
  margin-left: 10px;
}
.authdialog ::v-deep .el-dialog-div::-webkit-scrollbar {
  display: none; /*隐藏滚动条*/
}
.authdialog ::v-deep .el-dialog__title {
  font-weight: bold;
}
.el-divider--horizontal {
  margin: 10px 0;
}
::v-deep .el-tabs__content .el-tab-pane {
  height: 600px;
  overflow: auto;
}
::v-deep .vue-codemirror .CodeMirror {
  border: 1px solid #eee;
  width: 96%;
}

.config-panel {
  background: #ffffff;
  margin-left: 1px;
  top: -48px;
  position: relative;
  width: 306px;
  // height: 95%;
  height: calc(100vh - 64px - 10px - 10px);

  display: flex;
  flex-direction: column;
  overflow: auto;
  .config-header {
    width: 100%;
    height: 48px;
    // background: #2F343D;
    font-size: 16px;
    font-weight: bold;
    color: #1a1a1a;
    line-height: 48px;
    text-align: center;
  }
  .config-box {
    flex: 1;
    padding: 10px;
    overflow: auto;
  }

  /*定义滚动条的宽度*/
  .config-box::-webkit-scrollbar {
    width: 0;
  }
}
.cus-collapse-content {
  border-radius: 4px;
  border: 0.5px solid rgba(23, 183, 148, 0.1);
  background: rgba(23, 183, 148, 0.05);
  padding: 8px 10px;
}
::v-deep .handle-drawer{
  width: 36%;
  left: unset;
  // border: 1px solid #eee;
}
::v-deep .handle-drawer .el-drawer__body {
  // background-color: #f7f9fc;
  padding: 8px 10px 52px !important;
}
.table-box {
  .table-box-left {
    width: 200px;
    margin-right: 10px;
    flex-shrink: 0;
  }
  .table-box-left {
    .table-header {
      border-radius: 6px 6px 0px 0px;
      padding: 0px 8px 0px 16px;
      height: 40px;
      background: #fff;
      /* border */
      box-shadow: 0px -1px 0px 0px #eef2f5 inset;
      border-bottom: #eef2f5;
    }

    .table-body {
      padding: 8px 16px;
      background: #fff;
      border-radius: 0px 0px 6px 6px;
      height: calc(100% - 56px);
      &::-webkit-scrollbar {
        width: 5px;
      }
      .dataset-attr {
        padding: 7px 8px;
        border-radius: 4px;
        background: #f5f7fa;
        margin-bottom: 8px;
        cursor: pointer;

        .dataset-name2 {
          font-size: 12px;
          color: #3c3c3c;
          line-height: 18px; /* 150% */
          flex: 1;
          margin-right: 4px;
        }

        .action-box {
          .action {
            width: 18px;
            height: 18px;
            background-size: 100% 100%;
          }
          .action-edit {
            background-image: url("~@/static/img/sheet/dataset-edit.png");
            margin-right: 4px;
          }
          .action-del {
            background-image: url("~@/static/img/sheet/dataset-del.png");
          }
        }
      }
      .dataset-attr:hover,
      .dataset-attr-active {
        background: $--color-primary;
        .dataset-name2{
          color: #fff;
        }
        .action-edit {
          background-image: url("~@/static/img/sheet/dataset-edit-active.png") !important;
        }
        .action-del {
          background-image: url("~@/static/img/sheet/dataset-del-active.png") !important;
        }
      }
    }
  }
  .table-box-right {
    width: calc(100% - 210px);
    .table-form {
      padding: 0 14px 0 26px;
    }
    ::v-deep .el-form-item {
      display: flex;
      align-items: center;
      .el-form-item__content {
        margin-left: 16px !important;
        flex: 1;
      }
    }
    ::v-deep .el-form-item__label {
      color: #292e33 !important;
      font-weight: normal !important;
      line-height: 32px !important;
      height: 32px !important;
      margin-bottom: 0 !important;
    }
    .table-card {
      padding: 18px 14px;
      background: #fff;
      .table-header {
        margin-bottom: 24px;
        .attr-dataset-title {
          margin-right: 12px;
        }
      }
    }
  }
}
</style>
