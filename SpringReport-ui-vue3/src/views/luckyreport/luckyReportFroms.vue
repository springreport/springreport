<template>
  <div
    class="contentbox"
    v-loading="loading"
    :element-loading-text="loadingText"
    style="
      height: 100vh;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      background-color: #f0f2f5;
    "
  >
    <div style="width: 100%; flex: none" class="header-box">
      <el-header class="_header df-c-b">
        <div class="headerLeft df-c" style="width: 30%">
          <div
            class="tplname"
            style="width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap"
            :title="tplName"
          >
            {{ tplName }}
          </div>
        </div>
        <div class="headerRight df-c">
          <el-dropdown
            class="white font"
            trigger="click"
            placement="bottom"
            v-if="users.length > 0"
          >
            <span class="el-dropdown-link df-c">
              <el-avatar
                :style="{ marginRight: '4px', backgroundColor: item.color + ' !important' }"
                shape="circle"
                :title="item.userName"
                v-for="(item, index) in headerUsers"
                :key="index"
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
      </el-header>
    </div>
    <div :style="{ flex: 1, display: 'flex', height: designHeight + 'px' }">
      <div v-show="leftOpen" class="left">
        <!-- <div class="left-head">
                <i class="el-icon-s-fold"></i>
            </div> -->
        <div class="left-dataset-title">
          <span class="dataset-title">数据集管理</span>
          <el-button
            plain
            type="primary"
            class="add-dataset"
            @click="addDataSets"
            v-has="'reportDesign_addDataSet'"
            ><icon-plus
              theme="outline"
              size="12"
              fill="#17b794"
              class="el-icon--left"
            />添加数据集</el-button
          >
        </div>
        <div class="left-dataset-content df">
          <div class="dataset-group">
            <div class="section-name df-c">
              <span style="margin-right: 8px">报表</span>
              <div class="set-group df-c" @click="groupSetVisible = true">
                <img src="@/assets/img/sheet/setting.png" width="12px" height="12px" />
                <div class="setting-text">分组设置</div>
              </div>
            </div>
            <el-input v-model="datasetKeyword" placeholder="报表搜索" class="search" clearable>
              <template #prefix>
                <icon-search />
              </template>
            </el-input>
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
                      :class="datasetItemActive == datasetItem.id ? 'dataset-item-active' : ''"
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
                        <div class="action action-edit" @click.stop="editDataSet(datasetItem)" />
                        <div class="action action-del" @click.stop="deleteDataSet(datasetItem)" />
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
              placeholder="检索所选报表内字段"
              class="search"
              clearable
            >
              <template #prefix>
                <icon-search />
              </template>
            </el-input>
            <div
              v-loading="filedLoading"
              class="group-list cus-scrollbar"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
            >
              <template v-if="displayFields.length">
                <draggable
                  v-model="displayFields"
                  class="wrapper"
                  :sort="false"
                  :disabled="false"
                  item-key="id"
                >
                  <template #item="{ element }">
                    <div
                      class="dataset-item df-c-b"
                      @dragend="endDraggable(datasetItem.datasetName, element.name)"
                    >
                      <div
                        class="set-name overflow-text"
                        style="flex: 1"
                        :title="element.columnName"
                      >
                        {{ element.columnName }}
                      </div>
                      <div class="action-box df-c">
                        <div
                          class="action action-edit"
                          @click="copyColumn(datasetItem.datasetName, element.name)"
                        />
                        <div class="action action-del" />
                      </div>
                    </div>
                  </template>
                </draggable>
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
          <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel" />
        </div>
        <div style="display: none">
          <input id="uploadAttachmentBtn" type="file" accept="*" @change="changeAttachment" />
        </div>
        <div id="luckysheet" style="width: 100%; height: 100%; left: 0px; overflow: auto"></div>
        <div class="left-action action-icon df-c-b" @click="switchOpenLeftPanel()">
          <img v-if="leftOpen" src="@/assets/img/sheet/left.png" width="8px" height="8px" />
          <img v-else src="@/assets/img/sheet/right.png" width="8px" height="8px" />
        </div>
        <div class="right-action action-icon df-c-b" @click="switchOpenRightPanel()">
          <img v-if="!rightOpen" src="@/assets/img/sheet/left.png" width="8px" height="8px" />
          <img v-else src="@/assets/img/sheet/right.png" width="8px" height="8px" />
        </div>
      </div>
      <div class="right" v-show="rightOpen">
        <!-- <div class="right-head">
                <i class="el-icon-s-unfold"></i>
            </div> -->
        <div class="right-title">
          <span
            :class="tabIndex == 1 ? 'cell-property' : 'cell-property cell-property-noactive'"
            @click="clickTab(1)"
            >单元格属性</span
          >
          <span
            :class="tabIndex == 2 ? 'cell-property' : 'cell-property cell-property-noactive'"
            @click="clickTab(2)"
            >报表属性</span
          >
        </div>
        <div class="right-form">
          <el-form
            label-position="top"
            class="demo-form-inline"
            :model="cellForm"
            ref="reportCellForm"
            v-show="tabIndex == 1"
          >
            <el-collapse v-model="rightFormCollapse">
              <el-collapse-item title="常规配置" name="generalConfig">
                <el-form-item label="扩展方向">
                  <el-select
                    style="width: 100%"
                    placeholder="扩展方向"
                    v-model="cellForm.cellExtend"
                    @change="changeCellAttr('cellExtend')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="不扩展" value="1"></el-option>
                    <el-option label="向右扩展" value="2"></el-option>
                    <el-option label="向下扩展" value="3"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="聚合方式" v-show="cellForm.cellExtend != 4">
                  <el-select
                    style="width: 100%"
                    placeholder="聚合方式"
                    v-model="cellForm.aggregateType"
                    @change="changeCellAttr('aggregateType')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="列表" value="list"></el-option>
                    <el-option label="分组" value="group"></el-option>
                    <el-option label="分组汇总" value="groupSummary"></el-option>
                    <el-option label="汇总" value="summary"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item
                  label="分组属性"
                  v-show="
                    cellForm.aggregateType == 'group' || cellForm.aggregateType == 'groupSummary'
                  "
                >
                  <el-input
                    v-model="cellForm.groupProperty"
                    style="width: 125px"
                    placeholder="分组属性"
                    @input="changeCellAttr('groupProperty')"
                    :disabled="attrDisabled"
                  ></el-input>
                </el-form-item>
                <el-form-item label="数据来源">
                  <el-select
                    style="width: 100%"
                    placeholder="数据来源"
                    v-if="cellForm.cellExtend != 4"
                    v-model="cellForm.dataFrom"
                    @change="changeCellAttr('dataFrom')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="默认" :value="1"></el-option>
                    <el-option label="原始数据" :value="2"></el-option>
                    <el-option label="单元格" :value="3"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="来源单元格" v-show="cellForm.dataFrom == 3">
                  <el-input
                    v-model="cellForm.dependencyCell"
                    style="width: 125px"
                    placeholder="来源单元格"
                    @input="changeCellAttr('dependencyCell')"
                    :disabled="attrDisabled"
                  ></el-input>
                  <el-alert
                    title="填写单元格坐标,如B1"
                    type="success"
                    :closable="false"
                    style="height: 30px; width: 140px; padding: 0"
                  ></el-alert>
                </el-form-item>
                <el-form-item
                  label="汇总方式"
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary') &&
                    cellForm.dataFrom != 4
                  "
                >
                  <el-select
                    v-model="cellForm.cellFunction"
                    style="width: 100%"
                    placeholder="汇总方式"
                    @change="changeCellAttr('cellFunction')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="合计" value="1"></el-option>
                    <el-option label="平均值" value="2"></el-option>
                    <el-option label="最大值" value="3"></el-option>
                    <el-option label="最小值" value="4"></el-option>
                    <el-option label="计数" value="5"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="数值单位转换" class="df-form-item">
                  <el-switch
                    v-model="cellForm.unitTransfer"
                    active-text="是"
                    inactive-text="否"
                    @change="changeCellAttr('unitTransfer')"
                    :disabled="attrDisabled"
                  >
                  </el-switch>
                </el-form-item>
                <el-form-item label="转换方式" v-show="cellForm.unitTransfer">
                  <el-select
                    v-model="cellForm.transferType"
                    style="width: 100%"
                    placeholder="转换方式"
                    @change="changeCellAttr('transferType')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="乘法" :value="1"></el-option>
                    <el-option label="除法" :value="2"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="倍数" v-show="cellForm.unitTransfer">
                  <el-select
                    v-model="cellForm.multiple"
                    style="width: 100%"
                    placeholder="倍数"
                    @change="changeCellAttr('multiple')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="10" value="10"></el-option>
                    <el-option label="100" value="100"></el-option>
                    <el-option label="1000" value="1000"></el-option>
                    <el-option label="10000" value="10000"></el-option>
                    <el-option label="100000" value="100000"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item
                  label="小数位数"
                  v-show="
                    (cellForm.aggregateType == 'summary' ||
                      cellForm.aggregateType == 'groupSummary' ||
                      cellForm.unitTransfer) &&
                    cellForm.dataFrom != 4
                  "
                >
                  <el-input
                    v-model="cellForm.digit"
                    style="width: 100%"
                    placeholder="小数位数"
                    @input="changeCellAttr('digit')"
                    :disabled="attrDisabled"
                  ></el-input>
                </el-form-item>
                <el-form-item label="允许修改" class="df-form-item">
                  <el-switch
                    v-model="cellForm.allowEdit"
                    active-text="是"
                    inactive-text="否"
                    @change="changeCellAttr('allowEdit')"
                    :disabled="attrDisabled"
                  >
                  </el-switch>
                </el-form-item>
                <el-form-item label="值类型">
                  <el-select
                    style="width: 100%"
                    placeholder="值类型"
                    v-model="cellForm.valueType"
                    @change="changeCellAttr('valueType')"
                    :disabled="attrDisabled"
                  >
                    <el-option label="文本" value="1"></el-option>
                    <el-option label="数值" value="2"></el-option>
                    <el-option label="日期时间" value="3"></el-option>
                    <el-option label="下拉单选" value="4"></el-option>
                  </el-select>
                </el-form-item>
                <div class="cus-collapse-content">
                  <el-form-item label="必填项" class="df-form-item">
                    <el-switch
                      v-model="cellForm.require"
                      active-text="是"
                      inactive-text="否"
                      @change="changeCellAttr('require')"
                      :disabled="attrDisabled"
                    >
                    </el-switch>
                  </el-form-item>
                  <el-form-item
                    label="长度校验"
                    class="df-form-item"
                    v-show="cellForm.valueType == '1'"
                  >
                    <el-switch
                      v-model="cellForm.lengthValid"
                      active-text="是"
                      inactive-text="否"
                      @change="changeCellAttr('lengthValid')"
                      :disabled="attrDisabled"
                    >
                    </el-switch>
                  </el-form-item>
                  <el-form-item
                    label="最小长度"
                    v-show="cellForm.lengthValid && cellForm.valueType == '1'"
                  >
                    <el-input
                      v-model="cellForm.minLength"
                      style="width: 100%"
                      placeholder="最小长度"
                      @input="changeCellAttr('minLength')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item
                    label="最大长度"
                    v-show="cellForm.lengthValid && cellForm.valueType == '1'"
                  >
                    <el-input
                      v-model="cellForm.maxLength"
                      style="width: 100%"
                      placeholder="最大长度"
                      @input="changeCellAttr('maxLength')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="校验规则" v-show="cellForm.valueType == '1'">
                    <el-select
                      style="width: 100%"
                      placeholder="校验规则"
                      v-model="cellForm.textValidRule"
                      @change="changeCellAttr('textValidRule')"
                      :disabled="attrDisabled"
                    >
                      <el-option label="无" value="0"></el-option>
                      <el-option label="邮箱" value="1"></el-option>
                      <el-option label="手机号" value="2"></el-option>
                      <el-option label="座机号" value="3"></el-option>
                      <el-option label="身份证" value="4"></el-option>
                      <el-option label="自定义" value="99"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    label="正则表达式"
                    v-show="cellForm.textValidRule == '99' && cellForm.valueType == '1'"
                  >
                    <el-input
                      v-model="cellForm.regex"
                      style="width: 140px"
                      placeholder="正则表达式"
                      @input="changeCellAttr('regex')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="大小校验" v-show="cellForm.valueType == '2'">
                    <el-switch
                      v-model="cellForm.numberRangeValid"
                      active-text="是"
                      inactive-text="否"
                      @change="changeCellAttr('numberRangeValid')"
                      :disabled="attrDisabled"
                    >
                    </el-switch>
                  </el-form-item>
                  <el-form-item
                    label="最小值"
                    v-show="cellForm.valueType == '2' && cellForm.numberRangeValid"
                  >
                    <el-input
                      v-model="cellForm.minValue"
                      style="width: 100%"
                      placeholder="最小值"
                      @input="changeCellAttr('minValue')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item
                    label="最大值"
                    v-show="cellForm.valueType == '2' && cellForm.numberRangeValid"
                  >
                    <el-input
                      v-model="cellForm.maxValue"
                      style="width: 100%"
                      placeholder="最大值"
                      @input="changeCellAttr('maxValue')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="小数位数" v-show="cellForm.valueType == '2'">
                    <el-input
                      v-model="cellForm.digit"
                      style="width: 100%"
                      placeholder="小数位数"
                      @input="changeCellAttr('digit')"
                      :disabled="attrDisabled"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="格式" v-show="cellForm.valueType == '3'">
                    <!-- <el-input v-model="cellForm.dateFormat" style="width:150px"  placeholder="日期格式" @input="changeCellAttr('dateFormat')"></el-input> -->
                    <el-select
                      style="width: 100%"
                      placeholder="日期格式"
                      v-model="cellForm.dateFormat"
                      @change="changeCellAttr('dateFormat')"
                      :disabled="attrDisabled"
                    >
                      <el-option label="年-月-日" value="yyyy-MM-dd"></el-option>
                      <el-option label="年-月" value="yyyy-MM"></el-option>
                      <el-option label="年" value="yyyy"></el-option>
                      <el-option label="年-月-日 时:分:秒" value="yyyy-MM-dd HH:mm:ss"></el-option>
                      <el-option label="年-月-日 时:分" value="yyyy-MM-dd HH:mm"></el-option>
                      <el-option label="时:分:秒" value="HH:mm:ss"></el-option>
                      <el-option label="时:分" value="HH:mm"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="数据源" v-show="cellForm.valueType == '4'">
                    <el-select
                      style="width: 100%"
                      placeholder="数据源"
                      v-model="cellForm.datasourceId"
                      @change="changeCellAttr('datasourceId')"
                      :disabled="attrDisabled"
                    >
                      <el-option
                        v-for="op in dataSource"
                        :label="op.dataSourceName"
                        :value="op.datasourceId"
                        :key="op.datasourceId"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="字典类型" v-show="cellForm.valueType == '4'">
                    <el-select
                      style="width: 100%"
                      placeholder="字典类型"
                      v-model="cellForm.dictType"
                      @change="changeCellAttr('dictType')"
                      :disabled="attrDisabled"
                    >
                      <el-option
                        v-for="op in dictTypes"
                        :label="op.dictType"
                        :value="op.dictType"
                        :key="op.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </div>

                <!-- <el-form-item label="是否预警" >
                        <el-switch
                        v-model="cellForm.warning"
                        active-text="是"
                        inactive-text="否" @change="changeCellAttr('warning')" :disabled="attrDisabled">
                    </el-switch>
                    </el-form-item>
                    <el-form-item label="预警规则" v-show="cellForm.warning">
                          <el-select  style="width:150px" placeholder="预警规则"  v-model="cellForm.warningRules" @change="changeCellAttr('warningRules')" :disabled="attrDisabled">
                            <el-option label="大于" value=">"></el-option>
                            <el-option label="大于等于" value=">="></el-option>
                            <el-option label="等于" value="="></el-option>
                            <el-option label="小于" value="<"></el-option>
                            <el-option label="小于等于" value="<="></el-option>
                          </el-select>
                      </el-form-item>
                    <el-form-item label="预警阈值"  v-show="cellForm.warning">
                        <el-input v-model="cellForm.threshold" style="width:150px"  placeholder="预警阈值" @input="changeCellAttr('threshold')" :disabled="attrDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="预警颜色"  v-show="cellForm.warning">
                        <el-color-picker v-model="cellForm.warningColor"  :predefine="commonConstants.predefineColors" @change="changeCellAttr('warningColor')" :disabled="attrDisabled"></el-color-picker>
                    </el-form-item>
                    <el-form-item label="预警内容"  v-show="cellForm.warning">
                        <el-input type="textarea" :rows="4" v-model="cellForm.warningContent" style="width:150px"  placeholder="预警内容" @input="changeCellAttr('warningContent')" :disabled="attrDisabled"></el-input>
                    </el-form-item> -->
                <el-form-item label="是否下钻">
                  <el-switch
                    v-model="cellForm.isDrill"
                    active-text="是"
                    inactive-text="否"
                    @change="changeCellAttr('isDrill')"
                    :disabled="attrDisabled"
                  >
                  </el-switch>
                </el-form-item>
                <el-form-item label="下钻报表" v-show="cellForm.isDrill">
                  <el-select
                    style="width: 100%"
                    placeholder="数据源"
                    filterable
                    clearable
                    v-model="cellForm.drillId"
                    @change="changeCellAttr('drillId')"
                    :disabled="attrDisabled"
                  >
                    <el-option
                      v-for="op in reportTpls"
                      :label="op.tplName"
                      :value="op.id"
                      :key="op.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="参数属性" v-show="cellForm.isDrill">
                  <el-input
                    type="textarea"
                    v-model="cellForm.drillAttrs"
                    style="width: 100%"
                    placeholder="多个属性用,分割"
                    @input="changeCellAttr('drillAttrs')"
                    :disabled="attrDisabled"
                  ></el-input>
                </el-form-item>
              </el-collapse-item>
              <el-collapse-item title="单元格比较" name="cellDiff">
                <el-form-item label="与其他单元格比较" class="df-form-item">
                  <el-switch
                    v-model="cellForm.otherCellCompare"
                    active-text="是"
                    inactive-text="否"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('otherCellCompare')"
                  />
                </el-form-item>

                <div class="right-dataset-title df-c-b" v-if="cellForm.otherCellCompare">
                  <span class="attr-dataset-title">比较单元格</span>
                  <el-button class="addBtn" @click="addCompareCells" :disabled="attrDisabled"
                    ><icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--left" />
                    添加</el-button
                  >
                </div>
                <el-collapse
                  v-if="
                    cellForm.otherCellCompare &&
                    cellForm.compareCells &&
                    cellForm.compareCells.length > 0
                  "
                  class="sub-collapse"
                >
                  <el-collapse-item v-for="(o, index) in cellForm.compareCells" :key="index">
                    <template #title>
                      比较单元格{{ index + 1 }}
                      <div
                        class="right-block-el-icon-edit"
                        title="编辑"
                        @click.stop="editCompareCell(o, index)"
                        :disabled="attrDisabled"
                      ></div>
                      <div
                        class="right-el-icon-delete"
                        title="删除"
                        @click.stop="deleteCompareCell(index)"
                        :disabled="attrDisabled"
                      ></div>
                    </template>
                    <p
                      class="column-tag"
                      :title="o.sheetName"
                      style="min-width: 220px; max-width: 220px; margin: 0"
                    >
                      sheet名称：{{ o.sheetName }}</p
                    >
                    <p class="column-tag" style="min-width: 220px; max-width: 220px; margin: 0"
                      >坐标：{{ o.coordinate }}</p
                    >
                    <p class="column-tag" style="min-width: 220px; max-width: 220px; margin: 0">
                      单元格类型：{{ commonUtil.getDictionaryValueName('cellType', o.cellType) }}</p
                    >
                    <p class="column-tag" style="min-width: 220px; max-width: 220px; margin: 0"
                      >比较类型：{{ o.compareType }}</p
                    >
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
              <el-collapse-item title="单元格过滤条件" name="cellFilter">
                <div class="df-c" style="padding: 8px 0 16px 0">
                  <span class="cell-label">判断查询</span>
                  <el-radio
                    v-model="cellForm.filterType"
                    label="and"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('filterType')"
                    >AND</el-radio
                  >
                  <el-radio
                    v-model="cellForm.filterType"
                    label="or"
                    :disabled="attrDisabled"
                    @change="changeCellAttr('filterType')"
                    >OR</el-radio
                  >
                </div>

                <div class="right-dataset-title df-c-b">
                  <span class="attr-dataset-title">单元格过滤条件</span>
                  <el-button class="addBtn" @click="addCellConditions" :disabled="attrDisabled"
                    ><icon-plus
                      theme="outline"
                      size="16"
                      fill="#FFF"
                      class="el-icon--left"
                    />添加</el-button
                  >
                </div>

                <el-collapse
                  v-if="cellForm.cellconditions && cellForm.cellconditions.length > 0"
                  class="sub-collapse"
                >
                  <el-collapse-item v-for="(o, index) in cellForm.cellconditions" :key="index">
                    <template #title>
                      过滤条件{{ index + 1 }}
                      <div
                        class="right-el-icon-edit"
                        title="编辑"
                        @click.stop="editCellCondition(index)"
                        :disabled="attrDisabled"
                      ></div>
                      <div
                        class="right-el-icon-copy-document"
                        title="复制"
                        @click.stop="copyCellCondition(o)"
                        :disabled="attrDisabled"
                      ></div>
                      <div
                        class="right-el-icon-delete"
                        @click.stop="deleteCellCondition(index)"
                        :disabled="attrDisabled"
                      ></div>
                    </template>
                    <p
                      class="column-tag"
                      :title="o.property"
                      style="min-width: 220px; max-width: 220px; margin: 0"
                    >
                      属性：{{ o.property }}
                    </p>
                    <p class="column-tag" style="min-width: 220px; max-width: 220px; margin: 0"
                      >操作符：{{ o.operator }}</p
                    >
                    <p class="column-tag" style="min-width: 220px; max-width: 220px; margin: 0"
                      >数据类型：<label v-if="o.type == 'varchar'">字符串</label>
                      <label v-else-if="o.type == 'number'">数值</label><label v-else>日期</label>
                    </p>
                    <p
                      class="column-tag"
                      :title="o.value"
                      style="min-width: 220px; max-width: 220px; margin: 0"
                      >条件值：{{ o.value }}
                    </p>
                    <p
                      class="column-tag"
                      :title="o.dateFormat"
                      v-if="o.type == 'date'"
                      style="min-width: 220px; max-width: 220px; margin: 0"
                      >日期格式：{{ o.dateFormat }}</p
                    >
                  </el-collapse-item>
                </el-collapse>
              </el-collapse-item>
            </el-collapse>
          </el-form>
          <div v-show="tabIndex == 2" class="demo-form-inline">
            <el-collapse v-model="rightFormCollapse2">
              <el-collapse-item title="常规配置" name="generalConfig">
                <div class="df-c" style="padding: 8px 0 12px 0">
                  <span class="cell-label" style="line-height: 20px">参数是否合并</span>
                  <el-switch
                    v-model="isParamMerge"
                    active-text="合并"
                    inactive-text="不合并"
                    @change="changeParamMerge"
                  />
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      title="数据集"
      v-model="addDatasetsDialogVisiable"
      width="82%"
      top="20px"
      :close-on-click-modal="false"
      @close="closeAddDataSet"
      class="add-dataset-dialog"
    >
      <el-radio-group v-model="addDatasetType" size="large" style="margin-bottom: 16px">
        <el-radio-button :label="1">sql语句</el-radio-button>
        <el-radio-button :label="2">参数配置</el-radio-button>
      </el-radio-group>
      <div v-show="addDatasetType == 1">
        <el-form :inline="true" :model="sqlForm" class="demo-form-inline" ref="sqlRef">
          <el-form-item
            label="数据集名称"
            prop="datasetName"
            :rules="filter_rules('数据集名称', { required: true })"
          >
            <el-input v-model="sqlForm.datasetName" placeholder="数据集名称"></el-input>
          </el-form-item>
          <el-form-item
            label="选择数据源"
            prop="datasourceId"
            style="width: 270px"
            :rules="filter_rules('选择数据源', { required: true })"
          >
            <el-select
              v-model="sqlForm.datasourceId"
              placeholder="选择数据源"
              @change="changeDatasource"
            >
              <el-option
                v-for="op in dataSource"
                :label="op.dataSourceName"
                :value="op.datasourceId"
                :key="op.datasourceId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="sql类型"
            prop="sqlType"
            :rules="filter_rules('选择数据源', { required: true })"
            v-if="datasourceType == 1"
            style="width: 270px"
          >
            <el-select v-model="sqlForm.sqlType" placeholder="选择sql类型">
              <el-option
                v-for="op in selectUtil.sqlType"
                :label="op.label"
                :value="op.value"
                :key="op.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="分组"
            prop="groupId"
            :rules="filter_rules('选择分组', { required: true })"
            style="width: 270px"
          >
            <el-select v-model="sqlForm.groupId" placeholder="选择分组">
              <el-option
                v-for="item in groupList"
                :key="item.id"
                :label="item.groupName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <div class="df" style="width: 100%; border: 1px solid #e8e8e8">
          <div v-show="selectVariableOpen" class="variable-content">
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
                  <div class="overflow-text" style="flex: 1; margin-right: 8px">
                    {{ item.label }}({{ item.value }})
                  </div>
                  <icon-copy title="复制" @click="doCopy(item)" />
                </div>
              </div>
            </div>
          </div>
          <div class="sql-content">
            <div style="height: 25px" v-if="datasourceType == 1">
              <el-tooltip
                content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中"
                placement="bottom"
                ><el-tag type="success" @click="execSql" style="cursor: pointer"
                  ><i class="el-icon-caret-right"></i>执行</el-tag
                ></el-tooltip
              >
              <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"
                ><el-tag @click="formatSql" style="cursor: pointer"
                  ><i class="el-icon-document"></i>格式化</el-tag
                >
              </el-tooltip>
            </div>
            <div style="height: 300px" v-if="datasourceType == 1">
              <codemirror
                ref="codeMirror"
                :options="cmOptions"
                v-model:value="sqlText"
              ></codemirror>
            </div>
            <div class="table-warp">
              <div class="table-title">执行结果</div>
              <!--表格 start-->
              <el-table
                :data="
                  sqlColumnTableData.tableData.slice(
                    (sqlColumnTableData.tablePage.currentPage - 1) *
                      sqlColumnTableData.tablePage.pageSize,
                    sqlColumnTableData.tablePage.currentPage * sqlColumnTableData.tablePage.pageSize
                  )
                "
                border
                style="width: 100%"
                align="center"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column width="72" label="序号" align="center">
                  <template v-slot="scope">
                    <span>{{
                      (sqlColumnTableData.tablePage.currentPage - 1) *
                        sqlColumnTableData.tablePage.pageSize +
                      scope.$index +
                      1
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="columnName" label="列名" align="center"></el-table-column>
                <el-table-column prop="name" label="别名" align="center"></el-table-column>
                <el-table-column prop="dataType" label="数据类型" align="center"></el-table-column>
                <el-table-column prop="width" label="宽度" align="center"></el-table-column>
              </el-table>
              <!--表格 end-->
              <!--分页 start-->
              <el-pagination
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                :current-page="sqlColumnTableData.tablePage.currentPage"
                :page-sizes="sqlColumnTableData.tablePage.pageSizeRange"
                :page-size="sqlColumnTableData.tablePage.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="sqlColumnTableData.tablePage.pageTotal"
              >
              </el-pagination>
              <!--分页 end-->
            </div>
          </div>
        </div>
      </div>

      <div v-show="addDatasetType == 2" class="parameter-content">
        <div v-show="sqlForm.sqlType == '1'" style="margin-bottom: 20px">
          <div class="parameter-warp">
            <div v-if="datasourceType == 1" class="warp-title"> 分页参数 </div>
            <el-form :inline="true" :model="paginationForm" ref="paginationRef" class="df-form">
              <el-form-item label="是否分页" prop="isPagination" v-if="datasourceType == 1">
                <el-select v-model="paginationForm.isPagination" placeholder="是否分页">
                  <el-option label="是" :value="1"></el-option>
                  <el-option label="否" :value="2"></el-option>
                </el-select>
              </el-form-item>
              <!-- <el-form-item v-if="paginationForm.isPagination == '1'" label="每页条数"  prop="pageCount" :rules="filter_rules('每页条数',{required:true,type:'positiveInt'})">
                              <el-input v-model="paginationForm.pageCount" placeholder="每页条数" ></el-input>
                          </el-form-item> -->
              <el-form-item
                v-if="paginationForm.isPagination == '1'"
                label="每页条数"
                prop="pageCount"
                :rules="filter_rules('每页条数', { required: true })"
              >
                <!-- <el-input v-model="paginationForm.pageCount" placeholder="每页条数" ></el-input> -->
                <el-select v-model="paginationForm.pageCount" placeholder="请选择">
                  <el-option
                    v-for="item in selectUtil.pageCount"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div v-show="sqlForm.sqlType == 1 || datasourceType == 2" style="margin-bottom: 20px">
          <div class="parameter-warp">
            <div class="warp-title"> 字段参数 </div>
            <el-form :inline="true" :model="paramForm" class="df-form" ref="paramRef">
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input v-model="paramForm.paramName" placeholder="参数名称"></el-input>
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input v-model="paramForm.paramCode" placeholder="参数编码"></el-input>
              </el-form-item>
              <el-form-item
                label="参数前缀"
                v-if="datasourceType == 2"
                prop="paramPrefix"
                :rules="filter_rules('参数前缀', { required: false })"
              >
                <el-input v-model="paramForm.paramPrefix" placeholder="参数前缀"></el-input>
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select v-model="paramForm.paramType" placeholder="参数类型">
                  <el-option label="字符串" value="varchar"></el-option>
                  <el-option label="数值" value="number"></el-option>
                  <el-option label="日期" value="date"></el-option>
                  <el-option label="下拉单选" value="select"></el-option>
                  <el-option label="下拉多选" value="mutiselect"></el-option>
                  <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                  <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select v-model="paramForm.dateFormat" placeholder="日期格式">
                  <el-option label="年" value="yyyy"></el-option>
                  <el-option label="年-月" value="yyyy-MM"></el-option>
                  <el-option label="年-月-日" value="yyyy-MM-dd"></el-option>
                  <el-option label="年-月-日 时:分" value="yyyy-MM-dd HH:mm"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="默认值">
                <el-input v-model="paramForm.paramDefault" placeholder="默认值"></el-input>
              </el-form-item>
              <el-form-item
                label="是否必填"
                prop="paramRequired"
                :rules="filter_rules('参数必填', { required: true })"
              >
                <el-select v-model="paramForm.paramRequired" placeholder="是否必填">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="是否隐藏"
                prop="paramHidden"
                :rules="filter_rules('是否隐藏', { required: true })"
                key="paramHidden"
              >
                <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                label="选择内容来源"
                prop="selectType"
                key="selectType"
                :rules="filter_rules('选择内容来源', { required: true })"
              >
                <el-select v-model="paramForm.selectType" placeholder="选择内容来源">
                  <el-option label="自定义" value="1"></el-option>
                  <el-option label="sql语句" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'select' && paramForm.selectType == '2'"
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                key="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select v-model="paramForm.isRelyOnParams" placeholder="是否依赖其他参数">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' &&
                  paramForm.selectType == '2' &&
                  paramForm.isRelyOnParams == '1'
                "
                label="依赖参数代码"
                prop="relyOnParams"
                key="relyOnParams"
                :rules="filter_rules('依赖参数代码', { required: true })"
              >
                <el-input v-model="paramForm.relyOnParams" placeholder="依赖参数代码"></el-input>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'multiTreeSelect'"
                label="父子联动"
                prop="checkStrictly"
                key="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select v-model="paramForm.checkStrictly" placeholder="选择父子联动">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' ||
                  paramForm.paramType == 'mutiselect' ||
                  paramForm.paramType == 'treeSelect' ||
                  paramForm.paramType == 'multiTreeSelect'
                "
                label="下拉选择内容"
                prop="selectContent"
                key="selectContent"
                :rules="filter_rules('下拉选择内容', { required: true })"
              >
                <el-input
                  type="textarea"
                  :cols="80"
                  v-model="paramForm.selectContent"
                  placeholder="下拉选择内容"
                ></el-input>
                <div class="sub-title">{{ selectContentSuggestion }}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addParam">添加</el-button>
              </el-form-item>
              <el-tag v-if="paramForm.paramType == 'date'" type="warning"
                >注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag
              >
              <el-tag
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                type="warning"
                >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
                注意：两个key必须是value 和 name</el-tag
              ><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" />
              <el-tag
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                type="warning"
                >sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有
                value 和 name</el-tag
              >
              <el-tag
                v-if="
                  paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'
                "
                type="warning"
                >sql语句格式：select deptId as id, deptName as name,parentId as pid from table
                注意：返回的属性中必须有 id,name和pid</el-tag
              >
            </el-form>
            <div style="height: 50%">
              <!--表格 start-->
              <el-table
                :data="paramTableData.tableData"
                border
                style="width: 98%"
                align="center"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
                <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
                <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
                <el-table-column
                  prop="paramDefault"
                  label="默认值"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="paramRequired"
                  label="是否必填"
                  align="center"
                  :formatter="commonUtil.formatterTableValue"
                ></el-table-column>
                <el-table-column
                  prop="isRelyOnParams"
                  label="是否依赖其他参数"
                  align="center"
                  :formatter="commonUtil.formatterTableValue"
                ></el-table-column>
                <el-table-column
                  prop="relyOnParams"
                  label="依赖参数"
                  align="center"
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template #default="scope">
                    <el-button @click="editParam(scope.row)" type="primary">编辑</el-button>
                    <el-button @click="deleteParam(scope.$index)" type="primary">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!--表格 end-->
            </div>
          </div>
        </div>
        <div v-show="sqlForm.sqlType == 2 && datasourceType == 1" style="margin-bottom: 20px">
          <div class="parameter-warp">
            <div class="warp-title"> 输入参数 </div>
            <el-form :inline="true" :model="procedureParamForm" class="df-form" ref="inParamRef">
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input v-model="procedureParamForm.paramName" placeholder="参数名称"></el-input>
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input v-model="procedureParamForm.paramCode" placeholder="参数编码"></el-input>
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select v-model="procedureParamForm.paramType" placeholder="参数类型">
                  <el-option label="int" value="int"></el-option>
                  <el-option label="String" value="String"></el-option>
                  <el-option label="Long" value="Long"></el-option>
                  <el-option label="BigDecimal" value="BigDecimal"></el-option>
                  <el-option label="Double" value="Double"></el-option>
                  <el-option label="Float" value="Float"></el-option>
                  <el-option label="Date" value="Date"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="默认值"
                prop="paramDefault"
                :rules="filter_rules('默认值', { required: true })"
              >
                <el-input v-model="procedureParamForm.paramDefault" placeholder="默认值"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addInParam">添加</el-button>
              </el-form-item>
            </el-form>
            <div style="height: 40%">
              <!--表格 start-->
              <el-table
                :data="procedureInParamTableData.tableData"
                border
                style="width: 100%"
                align="center"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
                <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
                <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
                <el-table-column
                  prop="paramDefault"
                  label="默认值"
                  align="center"
                ></el-table-column>
                <el-table-column fixed="right" label="操作" width="180" align="center">
                  <template #default="scope">
                    <el-button @click="editInParam(scope.row)" type="primary">编辑</el-button>
                    <el-button @click="moveUp(scope.$index, '1')" type="primary">上移</el-button>
                    <el-button @click="moveDown(scope.$index, '1')" type="primary">下移</el-button>
                    <el-button @click="deleteInParam(scope.$index)" type="primary">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!--表格 end-->
            </div>
            <div class="warp-title"> 输出参数 </div>
            <el-form
              :inline="true"
              :model="procedureOutParamForm"
              class="df-form"
              ref="outParamRef"
            >
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input
                  v-model="procedureOutParamForm.paramName"
                  placeholder="参数名称"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input
                  v-model="procedureOutParamForm.paramCode"
                  placeholder="参数编码"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select v-model="procedureOutParamForm.paramType" placeholder="参数类型">
                  <el-option label="VARCHAR" value="VARCHAR"></el-option>
                  <el-option label="INTEGER" value="INTEGER"></el-option>
                  <el-option label="BIGINT" value="BIGINT"></el-option>
                  <el-option label="FLOAT" value="FLOAT"></el-option>
                  <el-option label="DOUBLE" value="DOUBLE"></el-option>
                  <el-option label="DECIMAL" value="DECIMAL"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addOutParam">添加</el-button>
              </el-form-item>
            </el-form>
            <div style="height: 30%">
              <!--表格 start-->
              <el-table
                :data="procedureOutParamTableData.tableData"
                border
                style="width: 100%"
                align="center"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
                <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
                <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
                <el-table-column fixed="right" label="操作" width="180" align="center">
                  <template #default="scope">
                    <el-button @click="editOutParam(scope.row)" type="primary">编辑</el-button>
                    <el-button @click="moveUp(scope.$index, '2')" type="primary">上移</el-button>
                    <el-button @click="moveDown(scope.$index, '2')" type="primary">下移</el-button>
                    <el-button @click="deleteOutParam(scope.$index)" type="primary">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!--表格 end-->
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeAddDataSet">取 消</el-button>
          <el-button type="primary" @click="addDataSet">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-drawer
      title="填报属性"
      v-model="datasourceDialog"
      size="100%"
      :modal="false"
      :close-on-click-modal="false"
      @close="closeDatasourceDialog"
      class="handle-drawer"
      custom-class="test"
    >
      <div class="table-box df">
        <div class="table-box-left">
          <div class="table-header df-c-b">
            <span class="attr-dataset-title">填报属性（{{ datasources.length }}）</span>
            <el-button class="addBtn" @click="addDatasourceAttr"
              ><icon-plus
                theme="outline"
                size="16"
                fill="#FFF"
                class="el-icon--left"
              />添加</el-button
            >
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
                  >{{ o.name }}</span
                >
                <div class="action-box df-c">
                  <div class="action action-edit" @click="editDatasourceAttr(o)" />
                  <div class="action action-del" @click="deleteDatasourceAttr(index)" />
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
                  @change="getDatabaseTables"
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
                  filterable
                  @change="getTableColumns"
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
                <span class="attr-dataset-title">关联</span>
                <el-button class="addBtn" @click="addDatasourceColumn"
                  ><icon-plus
                    theme="outline"
                    size="16"
                    fill="#FFF"
                    class="el-icon--left"
                  />添加</el-button
                >
              </div>

              <el-table
                :data="datasourceAttr.tableDatas"
                border
                style="width: 100%"
                align="center"
                height="280"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="columnName" label="列" align="center" />
                <el-table-column prop="cellCoords" label="单元格坐标" align="center" />
                <el-table-column label="操作" align="center">
                  <template v-slot="scope">
                    <el-button type="text" @click="deleteDatasourceColumn(scope.$index)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div class="table-card" style="margin-top: 12px">
              <div class="table-header df-c">
                <span class="attr-dataset-title">关联</span>
                <el-button class="addBtn" @click="addDatasourceKey"
                  ><icon-plus
                    theme="outline"
                    size="16"
                    fill="#FFF"
                    class="el-icon--left"
                  />主键</el-button
                >
              </div>
              <el-table
                :data="datasourceAttr.keys"
                border
                style="width: 100%"
                height="200"
                align="center"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column prop="columnName" label="主键属性" align="center" />
                <el-table-column prop="idType" label="主键规则" align="center">
                  <template v-slot="scope">
                    <span>
                      <span v-if="scope.row.idType == '1'">自定义填写</span>
                      <span v-else-if="scope.row.idType == '2'">雪花算法</span>
                      <span v-else>自增主键</span>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template v-slot="scope">
                    <el-button type="text" @click="deleteDatasourceKey(scope.$index)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </div>
      <span class="handle-drawer__footer">
        <el-button @click="closeDatasourceDialog">取 消</el-button>
        <el-button type="primary" @click="confirmAddDatasource">确 定</el-button>
      </span>
    </el-drawer>
    <el-dialog
      title="添加数据源绑定"
      v-model="datasourceAttrDialog"
      width="30%"
      :close-on-click-modal="false"
      @close="closeDatasourceAttr"
    >
      <el-form
        label-position="top"
        :model="datasourceAttrForm"
        class="demo-form-inline"
        ref="datasourceAttrRef"
      >
        <el-form-item
          label="绑定名称"
          prop="name"
          :rules="filter_rules('绑定名称', { required: true })"
        >
          <el-input v-model="datasourceAttrForm.name" placeholder="绑定名称"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDatasourceAttr">取 消</el-button>
          <el-button type="primary" @click="confirmDatasourceAttr">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="添加属性关联"
      v-model="datasourceColumnDialog"
      width="30%"
      :close-on-click-modal="false"
      @close="closeDatasourceColumn"
    >
      <el-form
        label-position="top"
        :model="datasourceColumnForm"
        class="demo-form-inline"
        ref="datasourceColumnRef"
      >
        <el-form-item label="列名" prop="columnName">
          <el-select
            style="width: 100%"
            placeholder="列名"
            filterable
            v-model="datasourceColumnForm.columnName"
            :rules="filter_rules('列名', { required: true })"
          >
            <el-option
              v-for="op in tableColumns"
              :label="op.columnName"
              :value="op.columnName"
              :key="op.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="单元格坐标"
          prop="cellCoords"
          :rules="filter_rules('单元格坐标', { required: true })"
        >
          <el-input v-model="datasourceColumnForm.cellCoords" placeholder="单元格坐标"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDatasourceColumn">取 消</el-button>
          <el-button type="primary" @click="confirmDatasourceColumn">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="添加主键"
      v-model="datasourceKeyDialog"
      width="30%"
      :close-on-click-modal="false"
      @close="closeDatasourceKey"
    >
      <el-form
        label-position="top"
        :model="datasourceKeyForm"
        class="demo-form-inline"
        ref="datasourceKeyRef"
      >
        <el-form-item label="主键列名" prop="columnName">
          <el-select
            style="width: 100%"
            placeholder="列名"
            filterable
            v-model="datasourceKeyForm.columnName"
            :rules="filter_rules('列名', { required: true })"
          >
            <el-option
              v-for="op in tableColumns"
              :label="op.columnName"
              :value="op.columnName"
              :key="op.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="主键生成规则"
          prop="idType"
          :rules="filter_rules('主键生成规则', { required: true })"
        >
          <el-select
            style="width: 100%"
            placeholder="主键生成规则"
            v-model="datasourceKeyForm.idType"
          >
            <el-option label="自定义填写" value="1"></el-option>
            <el-option label="雪花算法" value="2"></el-option>
            <el-option label="自增主键" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDatasourceKey">取 消</el-button>
          <el-button type="primary" @click="confirmDatasourceKey">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="单元格比较"
      v-model="cellCompareVisiable"
      width="30%"
      :close-on-click-modal="false"
      @close="closeCompareCells"
    >
      <el-form
        label-position="top"
        :model="cellCompareForm"
        class="demo-form-inline"
        ref="compareRef"
      >
        <el-form-item
          label="sheet名称"
          prop="sheetName"
          :rules="filter_rules('sheet名称', { required: true })"
        >
          <el-select
            placeholder="sheet名称"
            v-model="cellCompareForm.sheetName"
            @focus="onfocuseSheet"
          >
            <el-option
              v-for="op in sheets"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="坐标"
          prop="coordinate"
          :rules="filter_rules('坐标', { required: true })"
        >
          <el-input v-model="cellCompareForm.coordinate" placeholder="坐标"></el-input>
        </el-form-item>
        <el-form-item
          label="单元格类型"
          prop="cellType"
          :rules="filter_rules('单元格类型', { required: true })"
        >
          <el-select placeholder="单元格类型" v-model="cellCompareForm.cellType">
            <el-option
              v-for="op in selectUtil.cellType"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="比较类型"
          prop="compareType"
          :rules="filter_rules('比较类型', { required: true })"
        >
          <el-select placeholder="比较类型" v-model="cellCompareForm.compareType">
            <el-option
              v-for="op in selectUtil.operate"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeCompareCells">取 消</el-button>
          <el-button type="primary" @click="confirmAddCompareCells">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="单元格过滤"
      v-model="cellConditionVisiable"
      width="30%"
      :close-on-click-modal="false"
      @close="closeCellConditionDialog"
    >
      <el-form
        label-position="top"
        :model="cellConditionForm"
        class="demo-form-inline"
        ref="conditionRef"
      >
        <el-form-item
          label="属性"
          prop="property"
          key="property"
          :rules="filter_rules('属性', { required: true })"
        >
          <el-input v-model="cellConditionForm.property" placeholder="属性"></el-input>
        </el-form-item>
        <el-form-item
          label="操作符"
          prop="operator"
          key="operator"
          :rules="filter_rules('操作符', { required: true })"
        >
          <!-- <el-input v-model="cellConditionForm.operator" placeholder="操作符" ></el-input> -->
          <el-select placeholder="操作符" v-model="cellConditionForm.operator">
            <el-option
              v-for="op in selectUtil.operate"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="数据类型"
          prop="type"
          key="type"
          :rules="filter_rules('数据类型', { required: true })"
        >
          <el-select placeholder="数据类型" v-model="cellConditionForm.type">
            <el-option
              v-for="op in selectUtil.type"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="cellConditionForm.type == 'date'"
          label="日期格式"
          prop="dateFormat"
          key="dateFormat"
          :rules="filter_rules('日期格式', { required: false })"
        >
          <el-select placeholder="日期格式" v-model="cellConditionForm.dateFormat">
            <el-option
              v-for="op in selectUtil.dateFormat2"
              :label="op.label"
              :value="op.value"
              :key="op.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="条件值"
          prop="value"
          key="value"
          :rules="filter_rules('条件值', { required: true })"
        >
          <el-input
            type="textarea"
            :rows="2"
            style="width: 480px"
            v-model="cellConditionForm.value"
            placeholder="条件值"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeCellConditionDialog">取 消</el-button>
          <el-button type="primary" @click="confirmAddCellCondition">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-drawer
      :title="authTitle"
      v-model="addAuthVisiable"
      width="650px"
      custom-class="handle-drawer"
      class="handle-drawer"
      :modal="true"
      :close-on-click-modal="false"
      @close="closeAddAuth"
    >
      <el-form :inline="true" :model="addAuthForm" class="demo-form-inline" ref="addAuthRef">
        <!-- <el-transfer
                        v-model="addAuthForm.userIds"
                        :data="authUsers"
                        :titles="['用户信息', '授权用户']"
                        :filterable="true"
                        :props="{key:'id',label:'userName'}"
                    >
                    </el-transfer> -->
        <el-tree
          :data="authUsers"
          show-checkbox
          default-expand-all
          node-key="id"
          ref="tree"
          highlight-current
          :props="defaultProps"
          :default-checked-keys="defaultCheckedUsers"
        >
        </el-tree>
      </el-form>
        <div class="handle-drawer__footer">
          <el-button @click="closeAddAuth">取 消</el-button>
          <el-button type="primary" @click="confirmAddAuth">确 定</el-button>
        </div>
    </el-drawer>
    <modal
      ref="settingRef"
      :modalConfig="settingModalConfig"
      :modalForm="settingModalForm"
      :modalData="settingFormData"
      :modalHandles="settingModalHandles"
      @closeModal="closeSettingModal()"
    ></modal>
    <el-drawer
      :modal="false"
      :close-on-click-modal="false"
      :title="authedRangeTitle"
      v-model="authdialogVisible"
      @close="closeAuthDialog"
      custom-class="handle-drawer"
      class="handle-drawer"
    >
      <div class="el-dialog-div" v-if="authedRange && authedRange.length > 0">
        <div v-for="(item, index) in authedRange" :key="index">
          <el-descriptions title="" :column="1" border>
            <el-descriptions-item label="保护范围">{{ item.rangeAxis }}</el-descriptions-item>
            <el-descriptions-item label="授权人数" v-if="isCreator">{{
              item.userIds.length
            }}</el-descriptions-item>
          </el-descriptions>
          <div style="text-align: right; margin-top: 5px" v-if="isCreator">
            <el-button
              type="primary"
              title="编辑"
              icon="icon-edit"
              circle
              @click="editRange(item)"
            ></el-button>
            <el-button
              type="warning"
              icon="icon-monitor-one"
              title="显示选区"
              circle
              @click="showRange(item)"
            ></el-button>
            <el-button
              type="danger"
              icon="icon-delete"
              title="删除"
              circle
              @click="deleteRange(item, index)"
            ></el-button>
          </div>
          <el-divider content-position="left"></el-divider>
        </div>
      </div>
      <el-empty
        v-if="(!authedRange || authedRange.length == 0) && isCreator"
        description="暂无授权信息"
      ></el-empty>
      <el-empty
        v-if="(!authedRange || authedRange.length == 0) && !isCreator"
        description="暂无操作权限"
      ></el-empty>
    </el-drawer>
    <!-- 左侧分组设置 -->
    <el-dialog
      :close-on-click-modal="false"
      title="分组设置"
      v-model="groupSetVisible"
      width="504px"
      @close="closeGroupDialog"
    >
      <div class="group-dialog">
        <div class="df-c-b">
          <div class="title">全部分组（{{ groupList.length }}）</div>
          <el-button size="medium" @click="openGroupHandleDialog()">新增分组</el-button>
        </div>
        <el-table :data="groupList" style="width: 100%; margin-top: 12px">
          <el-table-column prop="groupName" label="选项" />
          <el-table-column label="操作" width="180">
            <template v-slot="scope">
              <el-link
                type="info"
                style="margin-right: 12px"
                @click="openGroupHandleDialog(scope.row)"
                >编辑</el-link
              >
              <el-link type="info" @click="deleteGroup(scope.row)">删除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
    <!-- 新增编辑分组 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="groupForm.id ? '编辑分组' : '新增分组'"
      v-model="groupHandleVisible"
      width="417px"
      @close="closeGroupHandleDialog"
    >
      <el-input v-model="groupForm.groupName" size="medium" placeholder="请输入" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeGroupHandleDialog()">取 消</el-button>
          <el-button type="primary" :loading="groupHandleLoading" @click="addOrEditGroup()"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
    <textarea id="clipboradInput" value="" style="opacity: 0; position: absolute" />
  </div>
</template>

<script src="./luckyReportFroms.js"></script>

<style scoped lang="scss">
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
            background-image: url('@/assets/img/sheet/dataset-edit.png');
            margin-right: 4px;
          }

          .action-del {
            background-image: url('@/assets/img/sheet/dataset-del.png');
          }
        }
      }

      .dataset-item:hover,
      .dataset-item-active {
        background: $base-color-primary;
        color: #fff;

        .action-edit {
          background-image: url('@/assets/img/sheet/dataset-edit-active.png') !important;
        }

        .action-del {
          background-image: url('@/assets/img/sheet/dataset-del-active.png') !important;
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
              background-image: url('@/assets/img/sheet/dataset-copy.png');
              margin-right: 4px;
            }

            .action-del {
              background-image: url('@/assets/img/sheet/dataset-drag.png');
            }
          }
        }

        .dataset-item:hover,
        .dataset-item-active {
          background: #fff;
          color: $base-color-primary;
          border: 1px solid $base-color-primary;

          .action-edit {
            background-image: url('@/assets/img/sheet/dataset-copy-active.png') !important;
          }

          .action-del {
            background-image: url('@/assets/img/sheet/dataset-drag-active.png') !important;
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

      :deep(span) {
        font-size: 12px;
      }

      &:hover {
        color: $base-color-primary;
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
        border-bottom: 1px solid #efebeb;
      }

      .variable-warp {
        padding: 9px 17px;

        .variable-warp-title {
          color: #979191;
          font-size: 12px;
          font-style: normal;
          font-weight: bold;
          line-height: 22px;
          /* 183.333% */
          margin-bottom: 12px;
        }

        .variable-list {
          flex-wrap: wrap;

          .variable-item {
            width: calc((100% - 18px) / 3);
            box-sizing: border-box;
            padding: 0 10px;
            border-radius: 4px;
            background: #e1f2f0;
            height: 32px;
            line-height: 32px;
            color: #595959;
            font-size: 12px;
            margin-right: 9px;
            transition: all 0.3s;
            margin-bottom: 12px;
            cursor: pointer;

            &:hover {
              color: #fff;
              background: $base-color-primary;

              ::v-deep .el-icon-circle-plus-outline {
                color: #fff;
              }
            }

            &:nth-child(3n) {
              margin-right: 0;
            }
          }
        }

        .analysis-list {
          border-radius: 3px;
          border: 1px solid #c1e0d9;
          background: #fff;
          padding: 8px 10px 0;

          .variable-item {
            border-radius: 4px;
            background: #f1f2f3;
          }
        }
      }
    }

    .sql-content {
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
        background-color: $base-color-primary;
        width: 10px;
        height: 48px;

        &:hover {
          opacity: 0.7;
        }
      }
    }

    .table-warp {
      padding: 10px;
      border: 1px solid #eee;

      .table-title {
        color: #1a1a1a;
        font-feature-settings: 'liga' off, 'clig' off;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: bold;
        line-height: normal;
        margin-bottom: 14px;
      }
    }

    .parameter-content {
      .parameter-warp {
        border-radius: 4px;
        background: #f7f9fc;
        padding: 0 14px;

        .warp-title {
          height: 56px;
          line-height: 56px;
          font-size: 14px;
          font-weight: bold;
          color: #1a1a1a;
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
        font-family: 'PingFang SC';
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
          color: $base-color-primary;
          font-family: 'PingFang SC';
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

        ::v-deep .el-input__wrapper {
          height: 36px;
          line-height: 36px;
          border-radius: 6px;
          padding: 0px 11px;
          border-color: rgba(0, 0, 0, 0.1);
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
    font-family: 'PingFang SC';
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
    background-image: url('@/assets/img/sheet/edit.png');
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
    background-image: url('@/assets/img/sheet/del.png');
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
    background-image: url('@/assets/img/sheet/edit.png');
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
    background-image: url('@/assets/img/sheet/edit.png');
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
    background-image: url('@/assets/img/sheet/copy.png');
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
    background-image: url('@/assets/img/sheet/del.png');
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
      font-family: 'PingFang SC';
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 14px;
      border: 0;
      padding: 0;
    }
  }

  .column-tag {
    max-width: 100%;
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
      right: -14px;
    }

    .action-icon {
      cursor: pointer;
      transition: all 0.3s;
      position: absolute;
      top: 50%;

      transform: translateY(-50%);
      z-index: 999;
      background-color: $base-color-primary;
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
      content: '';
      left: 50%;
      bottom: 0;
      transform: translateX(-50%);
      background: $base-color-primary;
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
    font-family: 'PingFang SC';
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
        margin-bottom: 0;
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
      box-sizing: border-box;
      padding-right: 12px;
      margin-right: 0;

      &:nth-child(5n) {
        margin-right: 0;
      }

      .el-form-item__label {
        flex-shrink: 0;
        width: 80px;
      }

      .el-form-item__content {
        // width: calc(100% - 92px);
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
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #f0f2f5;
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
    margin-top: 50px !important;
    margin-left: 0px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    top: 0 !important;
    left: 0px !important;
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
    display: none;
    /*隐藏滚动条*/
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
  }

  .cus-collapse-content {
    border-radius: 4px;
    border: 0.5px solid rgba(23, 183, 148, 0.1);
    background: rgba(23, 183, 148, 0.05);
    padding: 8px 10px;
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

  ::v-deep .handle-drawer {
    width: 36% !important;
    left: unset;
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
            line-height: 18px;
            /* 150% */
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
              background-image: url('@/assets/img/sheet/dataset-edit.png');
              margin-right: 4px;
            }

            .action-del {
              background-image: url('@/assets/img/sheet/dataset-del.png');
            }
          }
        }

        .dataset-attr:hover,
        .dataset-attr-active {
          background: $base-color-primary;

          .dataset-name2 {
            color: #fff;
          }

          .action-edit {
            background-image: url('@/assets/img/sheet/dataset-edit-active.png') !important;
          }

          .action-del {
            background-image: url('@/assets/img/sheet/dataset-del-active.png') !important;
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
<style lang="scss">
  .add-dataset-dialog {
    .el-radio-button:first-child .el-radio-button__inner {
      border-radius: 10px 0 0 10px !important;
    }

    .el-radio-button:last-child .el-radio-button__inner {
      border-radius: 0 10px 10px 0 !important;
    }
  }
</style>
