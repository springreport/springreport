<template>
    <div class="contentbox" v-loading="loading" :element-loading-text="loadingText" style="height:100vh;display: flex;flex-direction: column;overflow:hidden">
        <div style="width: 100%;flex: none;">
            <el-header class="_header df-c-b">
                <div class="headerLeft df-c" style="width:30%">
                <div class="tplname" style="width: 100%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" :title="tplName">
                        {{tplName}}
                    </div>
                </div>
                <div class="headerRight df-c">
                <el-dropdown class="white font" trigger="click" placement="bottom" v-if="users.length > 0">
                    <span class="el-dropdown-link df-c">
                    <el-avatar size="small" :style="{marginRight:'4px',backgroundColor:item.color+' !important'}" shape="circle" :title="item.userName" v-for="(item,index) in headerUsers" :key="index"> {{(item.userName.slice(0,1)).toUpperCase()}} </el-avatar>
                     <icon-down theme="outline" size="16"/>
                    </span>
                    <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item  v-for="(item,index) in users" :key="index">{{item.userName}}</el-dropdown-item>
                    </el-dropdown-menu>
                    </template>
                </el-dropdown>
                </div>
            </el-header>
        </div>
        <div :style="{flex:1,display:'flex',height:designHeight+'px'}">
        <div class="left">
            <!-- <div class="left-head">
                <i class="el-icon-s-fold"></i>
            </div> -->
            <div class="left-dataset-title">
                <span class="dataset-title">数据集管理</span>
                <el-button class="addBtn" @click="addDataSets" v-has="'reportDesign_addDataSet'">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
            </div>
            <div v-for="o in datasets" :key="o.id">
                <div :class="o.isActive?'dataset-box-active':'dataset-box'" style="position:relative">
                    <icon-right v-show="!o.isActive" theme="outline" size="16" fill="#999" class="el-icon-arrow-down"  @click="clickDatasets(o)"/>
                    <icon-down v-show="o.isActive" theme="outline" size="16" fill="#999" class="el-icon-arrow-right"  @click="clickDatasets(o)"/>
                    <span class="dataset-name" @click="clickDatasets(o)">{{o.datasetName}}</span>
                    <icon-edit class="el-icon-edit" @click="editDataSet(o)" v-has="'reportDesign_editDataSet'"/>
                    <icon-delete class="el-icon-delete" @click="deleteDataSet(o)" v-has="'reportDesign_deleteDataSet'"/>
                </div>
                <div class="dataset-box-content" v-if="o.isActive">
                    <draggable class="wrapper" v-model="o.columns" :sort= "false" :disabled= "false" item-key="id">
                        <template #item="{element}">
                        <div class="column-tag" :title="element.name" @dragend="endDraggable(o.datasetName,element.name)"><icon-copy @click="copyColumn(o.datasetName,element.name)"/>{{element.name}}</div>
                        </template>
                    </draggable>
                    <el-input v-show="o.apiResult" type="textarea" placeholder="" v-model="o.apiResult" rows="6"></el-input>
                </div>
            </div>
        </div>
        <div class="center">
           <div style="display:none">
            <input id="uploadBtn" type="file" accept="xlsx/*"  @change="loadExcel" />
          </div>
          <div style="display:none">
            <input id="uploadAttachmentBtn" type="file" accept="*"  @change="changeAttachment" />
        </div>
                <div id="luckysheet" style="width:100%;height:100%;left: 0px;overflow:auto;"></div>
        </div>
        <div class="right">
            <!-- <div class="right-head">
                <i class="el-icon-s-unfold"></i>
            </div> -->
            <div class="right-title">
                <span :class="tabIndex==1?'cell-property':'cell-property cell-property-noactive'" @click="clickTab(1)">单元格属性</span>
                <span :class="tabIndex==2?'cell-property':'cell-property cell-property-noactive'" @click="clickTab(2)">报表属性</span>
            </div>
            <div class="right-form">
                <el-form :inline="true" class="demo-form-inline" :model="cellForm" ref="reportCellForm" v-show="tabIndex==1">
                      <el-form-item label="扩展方向">
                          <el-select  style="width:150px" placeholder="扩展方向" size="small" v-model="cellForm.cellExtend" @change="changeCellExtend" :disabled="attrDisabled">
                            <el-option label="不扩展" :value=1></el-option>
                            <el-option label="向右扩展" :value=2></el-option>
                            <el-option label="向下扩展" :value=3></el-option>
                            <el-option label="交叉扩展" :value=4></el-option>
                          </el-select>
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
                      <el-form-item label="聚合方式" v-show="cellForm.cellExtend != 4">
                          <el-select  style="width:150px" placeholder="聚合方式" size="small" v-model="cellForm.aggregateType" @change="changeAggregateType" :disabled="attrDisabled">
                            <el-option label="列表" value="list"></el-option>
                            <el-option label="分组" value="group"></el-option>
                            <el-option label="分组汇总" value="groupSummary"></el-option>
                            <el-option label="汇总" value="summary"></el-option>
                          </el-select>
                      </el-form-item>
                      <el-form-item label="分组属性" v-show="cellForm.aggregateType=='group' || cellForm.aggregateType=='groupSummary' || cellForm.cellExtend==4" :disabled="attrDisabled">
                        <el-input v-model="cellForm.groupProperty" style="width:125px" size="small" placeholder="多个用,分隔" @input="changeCellAttr('groupProperty')" :disabled="attrDisabled"></el-input>
                    </el-form-item>
                      <el-form-item label="分组单元格是否合一" size="small" v-show="cellForm.aggregateType=='group' || cellForm.aggregateType=='groupSummary'">
                        <el-switch v-model="cellForm.isGroupMerge" @change="changeIsGroupMerge" :disabled="attrDisabled">
                        </el-switch>
                        </el-form-item>
                      <el-form-item label="数据来源">
                          <el-select  style="width:150px" placeholder="数据来源" size="small" v-if="cellForm.cellExtend != 4" v-model="cellForm.dataFrom" @change="changeDataFrom" :disabled="attrDisabled">
                            <el-option label="默认" :value=1></el-option>
                            <el-option label="原始数据" :value=2></el-option>
                            <el-option label="单元格" :value=3></el-option>
                          </el-select>
                          <el-select  style="width:150px" placeholder="数据来源" size="small" v-if="cellForm.cellExtend == 4" v-model="cellForm.dataFrom" @change="changeDataFrom" :disabled="attrDisabled">
                            <el-option label="单元格" :value=3></el-option>
                          </el-select>
                      </el-form-item>
                    <el-form-item label="数据来源行号" v-show="cellForm.dataFrom==3">
                        <el-input v-model="cellForm.groupSummaryDependencyr" style="width:125px" size="small" placeholder="数据来源行号" @input="changeGroupSummary('r')" :disabled="attrDisabled"></el-input>
                        <el-alert title="填写行值,如1" type="success" :closable="false" style="height:30px;width:125px"></el-alert>
                    </el-form-item>
                    <el-form-item label="数据来源列号" v-show="cellForm.dataFrom==3">
                        <el-input v-model="cellForm.groupSummaryDependencyc" style="width:125px" size="small"  placeholder="数据来源列号" @input="changeGroupSummary('c')" :disabled="attrDisabled"></el-input>
                        <el-alert title="填写列值,如A" type="success" :closable="false" style="height:30px;width:126px"></el-alert>
                    </el-form-item>
                    <el-form-item label="汇总方式" size="small" v-show="(cellForm.aggregateType=='summary' || cellForm.aggregateType=='groupSummary') && cellForm.dataFrom!=4">
                        <el-select v-model="cellForm.cellFunction" style="width:150px" placeholder="汇总方式" @change="changeSummaryType" :disabled="attrDisabled">
                        <el-option label="合计" value="1"></el-option>
                        <el-option label="平均值" value="2"></el-option>
                        <el-option label="最大值" value="3"></el-option>
                        <el-option label="最小值" value="4"></el-option>
                        <el-option label="计数" value="5"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="是否数值单位转换">
                        <el-switch
                        v-model="cellForm.unitTransfer"
                        active-text="是"
                        inactive-text="否" @change="changeCellAttr('unitTransfer')" :disabled="attrDisabled">
                    </el-switch>
                    </el-form-item>
                    <el-form-item label="转换方式" size="small" v-show="cellForm.unitTransfer">
                        <el-select v-model="cellForm.transferType" style="width:150px" placeholder="转换方式" @change="changeCellAttr('transferType')" :disabled="attrDisabled">
                        <el-option label="乘法" :value=1></el-option>
                        <el-option label="除法" :value=2></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="倍数" size="small" v-show="cellForm.unitTransfer">
                        <el-select v-model="cellForm.multiple" style="width:150px" placeholder="倍数" @change="changeCellAttr('multiple')" :disabled="attrDisabled">
                        <el-option label="1" value="1"></el-option>
                        <el-option label="10" value="10"></el-option>
                        <el-option label="100" value="100"></el-option>
                        <el-option label="1000" value="1000"></el-option>
                        <el-option label="10000" value="10000"></el-option>
                        <el-option label="100000" value="100000"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="小数位数" size="small" v-show="(cellForm.aggregateType=='summary' || cellForm.aggregateType=='groupSummary' || cellForm.unitTransfer) && cellForm.dataFrom!=4">
                        <el-input v-model="cellForm.digit" style="width:150px"  placeholder="小数位数" @input="changeDigit" :disabled="attrDisabled"></el-input>
                    </el-form-item>
                    <!-- 2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了 -->
                    <!-- <el-form-item label="是否预警" >
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
                    <el-form-item label="预警阈值" size="small" v-show="cellForm.warning">
                        <el-input v-model="cellForm.threshold" style="width:150px"  placeholder="预警阈值" @input="changeThreshold" :disabled="attrDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="预警颜色" size="small" v-show="cellForm.warning">
                        <el-color-picker v-model="cellForm.warningColor" size="small" :predefine="commonConstants.predefineColors" @change="changeWarningColor" :disabled="attrDisabled"></el-color-picker>
                    </el-form-item>
                    <el-form-item label="预警内容" size="small" v-show="cellForm.warning">
                        <el-input type="textarea" :rows="4" v-model="cellForm.warningContent" style="width:150px"  placeholder="预警内容" @input="changeWarningContent" :disabled="attrDisabled"></el-input>
                    </el-form-item> -->
                    <el-form-item label="是否数据字典" >
                        <el-switch
                        v-model="cellForm.isDict"
                        active-text="是"
                        inactive-text="否" @change="changeCellAttr('isDict')" :disabled="attrDisabled">
                    </el-switch>
                    </el-form-item>
                    <el-form-item label="数据源" v-show="cellForm.isDict">
                          <el-select  style="width:150px" placeholder="数据源" size="small" v-model="cellForm.datasourceId" @change="changeCellAttr('datasourceId')" :disabled="attrDisabled">
                            <el-option v-for="op in dataSource" :label="op.dataSourceName" :value="op.datasourceId" :key="op.datasourceId"></el-option>
                          </el-select>
                      </el-form-item>
                      <el-form-item label="字典类型" v-show="cellForm.isDict">
                          <el-select  style="width:150px" placeholder="字典类型" size="small" v-model="cellForm.dictType" @change="changeCellAttr('dictType')" :disabled="attrDisabled">
                            <el-option v-for="op in dictTypes" :label="op.dictType" :value="op.dictType" :key="op.id"></el-option>
                          </el-select>
                      </el-form-item>
                      <el-form-item label="是否下钻" >
                            <el-switch
                                v-model="cellForm.isDrill"
                                active-text="是"
                                inactive-text="否" @change="changeCellAttr('isDrill')" :disabled="attrDisabled">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="下钻报表" v-show="cellForm.isDrill">
                            <el-select  style="width:150px" placeholder="数据源" size="small" filterable remote :remote-method="getDrillReport" v-model="cellForm.drillId" @change="changeCellAttr('drillId')" :disabled="attrDisabled">
                                <el-option v-for="op in reportTpls" :label="op.tplName" :value="op.id" :key="op.id"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="参数属性" size="small" v-show="cellForm.isDrill">
                            <el-input type="textarea" v-model="cellForm.drillAttrs" style="width:150px"  placeholder="多个属性用,分割" @input="changeCellAttr('drillAttrs')" :disabled="attrDisabled"></el-input>
                        </el-form-item>
                        <el-form-item label="追加小计" size="small">
                        <el-switch
                            v-model="cellForm.isSubtotal"
                            active-text="是"
                            inactive-text="否" @change="changeCellAttr('isSubtotal')" :disabled="attrDisabled">
                        </el-switch>
                        </el-form-item>
                        <div class="right-dataset-title" v-show="cellForm.isSubtotal">
                            <span class="attr-dataset-title">小计单元格</span>
                            <el-button class="addBtn" @click="addSubTotalCells" :disabled="attrDisabled">添加<i class="el-icon-plus el-icon--right"></i></el-button>
                        </div>
                        <el-collapse v-if="cellForm.isSubtotal && cellForm.subTotalCells && cellForm.subTotalCells.length > 0">
                            <el-collapse-item v-for="(o,index) in cellForm.subTotalCells" :key="index">
                                <template #title>
                                    小计单元格{{index+1}}
                                    <el-button class="right-block-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editSubtotalCell(o,index)"  :disabled="attrDisabled"></el-button>
                                    <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteSubtotalCell(index)"  :disabled="attrDisabled"></el-button>
                                </template>
                                <p class="column-tag" :title="o.coords" style="min-width:220px;max-width:220px;margin:0">单元格：{{o.coords}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">小计类型：{{commonUtil.getDictionaryValueName('subtotalType',o.type)}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">小数位数：{{o.digit}}</p>
                            </el-collapse-item>
                        </el-collapse>
                        <div class="right-dataset-title" v-show="cellForm.isSubtotal">
                            <span class="attr-dataset-title">小计属性</span>
                            <el-button class="addBtn" @click="addSubTotalAttrs" v-show="cellForm.isSubtotal && (!cellForm.subTotalAttrs || cellForm.subTotalAttrs.length == 0)"  :disabled="attrDisabled">添加<i class="el-icon-plus el-icon--right"></i></el-button>
                        </div>
                        <el-collapse v-if="cellForm.isSubtotal && cellForm.subTotalAttrs && cellForm.subTotalAttrs.length > 0">
                            <el-collapse-item v-for="(o,index) in cellForm.subTotalAttrs" :key="index">
                                <template #title>
                                    小计属性
                                    <el-button class="right-block-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editSubtotalAttrs(o,index)"  :disabled="attrDisabled"></el-button>
                                    <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteSubtotalAttrs(index)"  :disabled="attrDisabled"></el-button>
                                </template>
                                <p class="column-tag" :title="o.name" style="min-width:220px;max-width:220px;margin:0">小计名称：{{o.name}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">字体颜色：{{o.fontColor}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">字体大小：{{o.fontSize}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">背景颜色：{{o.bgColor}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">是否加粗：{{o.fontWeight?"是":"否"}}</p>
                            </el-collapse-item>
                        </el-collapse>
                        <div class="right-dataset-title" >
                            <span class="attr-dataset-title">分组小计链</span>
                            <el-button class="addBtn" @click="addSubTotalCalc"  :disabled="attrDisabled">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
                        </div>
                        <el-collapse v-if="cellForm.subTotalCalc && cellForm.subTotalCalc.length > 0">
                            <el-collapse-item v-for="(o,index) in cellForm.subTotalCalc" :key="index">
                                <template #title>
                                    分组小计链{{index+1}}
                                    <el-button class="right-block-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editSubtotalCalc(o,index)"  :disabled="attrDisabled"></el-button>
                                    <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteSubtotalCalc(index)"  :disabled="attrDisabled"></el-button>
                                </template>
                                <!-- <p class="column-tag" :title="o.coords" style="min-width:220px;max-width:220px;margin:0">单元格：{{o.coords}}</p> -->
                                <p class="column-tag" v-for="(attr,index) in o.attrs" :key="index" style="min-width:220px;max-width:220px;margin:0">字段{{index+1}}：{{attr}}</p>
                            </el-collapse-item>
                        </el-collapse>
                        <div class="right-dataset-title">
                            <span class="attr-dataset-title">单元格过滤条件</span>
                            <el-button class="addBtn" @click="addCellConditions"  :disabled="attrDisabled">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
                        </div>
                        <el-radio v-model="cellForm.filterType" label="and" @change="changeCellAttr('filterType')"  :disabled="attrDisabled">AND</el-radio>
                        <el-radio v-model="cellForm.filterType" label="or" @change="changeCellAttr('filterType')"  :disabled="attrDisabled">OR</el-radio>
                        <el-collapse v-if="cellForm.cellconditions && cellForm.cellconditions.length > 0">
                            <el-collapse-item v-for="(o,index) in cellForm.cellconditions" :key="index">
                                <template #title>
                                    过滤条件{{index+1}}
                                    <el-button class="right-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editCellCondition(index)"  :disabled="attrDisabled"></el-button>
                                    <el-button class="right-el-icon-copy-document" title="复制" type="warning" icon="icon-copy" circle size="small" @click.stop="copyCellCondition(o)"  :disabled="attrDisabled"></el-button>
                                    <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteCellCondition(index)"  :disabled="attrDisabled"></el-button>
                                </template>
                                <p class="column-tag" :title="o.property" style="min-width:220px;max-width:220px;margin:0">属性：{{o.property}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">操作符：{{o.operator}}</p>
                                <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">数据类型：<label v-if="o.type=='varchar'">字符串</label>
                                <label v-else-if="o.type=='number'">数值</label><label v-else-if="o.type=='cell'">单元格</label><label v-else>日期</label></p>
                                <p class="column-tag" :title="o.value" style="min-width:220px;max-width:220px;margin:0">条件值：{{o.value}}</p>
                                <p class="column-tag" :title="o.dateFormat" v-if="o.type == 'date'" style="min-width:220px;max-width:220px;margin:0">日期格式：{{o.dateFormat}}</p>
                            </el-collapse-item>
                        </el-collapse>
                        <div class="right-dataset-title">
                        <span class="attr-dataset-title">单元格隐藏条件</span>
                        <el-button class="addBtn" @click="addCellHiddenConditions" :disabled="attrDisabled">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
                    </div>
                     <el-radio v-model="cellForm.hiddenType" label="and" @change="changeCellAttr('hiddenType')" :disabled="attrDisabled">AND</el-radio>
                     <el-radio v-model="cellForm.hiddenType" label="or" @change="changeCellAttr('hiddenType')" :disabled="attrDisabled">OR</el-radio>
                     <el-collapse v-if="cellForm.cellHiddenConditions && cellForm.cellHiddenConditions.length > 0">
                         <el-collapse-item v-for="(o,index) in cellForm.cellHiddenConditions" :key="index">
                            <template #title>
                                隐藏条件{{index+1}}
                                 <el-button class="right-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editCellHiddenCondition(index)" :disabled="attrDisabled"></el-button>
                                 <el-button class="right-el-icon-copy-document" title="复制" type="warning" icon="icon-copy" circle size="small" @click.stop="copyCellHiddenCondition(o)" :disabled="attrDisabled"></el-button>
                                 <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteCellHiddenCondition(index)" :disabled="attrDisabled"></el-button>
                            </template>
                            <p class="column-tag" :title="o.propertyName" style="min-width:220px;max-width:220px;margin:0">参数名称：{{o.propertyName}}</p>
                            <p class="column-tag" :title="o.property" style="min-width:220px;max-width:220px;margin:0">参数编码：{{o.property}}</p>
                            <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">操作符：{{o.operator}}</p>
                            <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">数据类型：<label v-if="o.type=='varchar'">字符串</label>
                            <label v-else-if="o.type=='number'">数值</label><label v-else>日期</label></p>
                            <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">条件值：{{o.value}}</p>
                            <p class="column-tag" :title="o.dateFormat" v-if="o.type == 'date'" style="min-width:220px;max-width:220px;margin:0">日期格式：{{o.dateFormat}}</p>
                         </el-collapse-item>
                     </el-collapse>
                  </el-form>
                  <div v-show="tabIndex==2" class="demo-form-inline">
                    <el-switch
                        v-model="isParamMerge"
                        active-text="参数合并"
                        inactive-text="参数不合并"
                        @change="changeParamMerge">
                    </el-switch>
                    <div class="right-dataset-title">
                        <span class="attr-dataset-title">循环块</span>
                        <el-button class="addBtn" @click="addBlock">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
                    </div>
                     <el-collapse v-if="sheetBlockData && sheetBlockData.length > 0">
                         <el-collapse-item v-for="(o,index) in sheetBlockData" :key="index">
                             <template  #title>
                                循环块{{index+1}}
                                 <el-button class="right-block-el-icon-edit" title="编辑" type="primary" icon="icon-edit" circle size="small" @click.stop="editBlock(o,index)"></el-button>
                                 <el-button class="right-el-icon-delete" type="danger" title="删除" icon="icon-delete" circle size="small" @click.stop="deleteBlock(index)"></el-button>
                            </template>
                             <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">循环块范围：{{o.startCell}}:{{o.endCell}}</p>
                             <p class="column-tag" style="min-width:220px;max-width:220px;margin:0">聚合方式：<label v-if="o.aggregateType == 'list'">列表</label>
                             <label v-else>分组</label></p>
                             <p class="column-tag" v-if="o.aggregateType == 'group'" :title="o.groupProperty" style="min-width:220px;max-width:220px;margin:0">分组属性：{{o.groupProperty}}</p>
                         </el-collapse-item>
                     </el-collapse>
                  </div>
            </div>
            <div  class="config-panel" v-if="chartSettingShow">
                <div class="config-header">图表设置</div>
                <div class="config-box">
                    <vchartsetting :component="chartOptions" :datasets="datasets" :isPreview="false"></vchartsetting>
                </div>
            </div>
        </div>
        </div>
        <el-dialog title="数据集" v-model="addDatasetsDialogVisiable" width="80%"  top="20px" :close-on-click-modal='false' @close='closeAddDataSet'>
              <el-tabs type="border-card">
                  <el-tab-pane label="sql语句">
                  <div>
                      <el-form :inline="true" :model="sqlForm" class="demo-form-inline" ref="sqlRef">
                      <el-form-item label="数据集名称"  prop="datasetName" :rules="filter_rules('数据集名称',{required:true})">
                          <el-input v-model="sqlForm.datasetName" placeholder="数据集名称" size="small"></el-input>
                      </el-form-item>
                      <el-form-item  label="选择数据源" prop="datasourceId" :rules="filter_rules('选择数据源',{required:true})">
                          <el-select v-model="sqlForm.datasourceId" placeholder="选择数据源" size="small" @change="changeDatasource">
                              <el-option v-for="op in dataSource" :label="op.dataSourceName" :value="op.datasourceId" :key="op.datasourceId"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item  label="sql类型" prop="sqlType" :rules="filter_rules('选择sql类型',{required:true})" v-if="datasourceType == 1">
                          <el-select v-model="sqlForm.sqlType" placeholder="选择sql类型" size="small">
                              <el-option v-for="op in selectUtil.sqlType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                          </el-select>
                          </el-form-item><br>
                          <el-form-item  label="系统变量">
                             <p class="column-tag" v-for="(item,index) in commonConstants.systemParam" :key="index" ><icon-copy @click="doCopy(item)"/>{{item.label}}({{item.value}})</p> 
                          </el-form-item>
                      </el-form>

                  <div style="height:25px;" v-if="datasourceType == 1">
                  <el-tooltip content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中" placement="bottom"><el-tag type="success" @click="execSql" size="small" style="cursor:pointer" ><icon-play/>执行</el-tag></el-tooltip>
                  <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"><el-tag @click="formatSql" size="small" style="cursor:pointer"><icon-align-left-one/>格式化</el-tag> </el-tooltip>
                  <el-tooltip content="该操作会插入注释标签" placement="right"><el-tag @click="addComment(' <!--  -->')" type="warning" size="small" style="cursor:pointer"><icon-add-one/>添加注释</el-tag> </el-tooltip>
                  <el-dropdown v-if="paramTableData.tableData && paramTableData.tableData.length > 0">
                    <el-tag  type="danger" size="small" style="cursor:pointer"><icon-add-one/>添加参数</el-tag>
                    <template #dropdown>
                    <el-dropdown-menu >
                    <el-dropdown-item v-for="(row,index) in paramTableData.tableData" :key="index" v-on:click="getWhereByParam(row)">{{row.paramCode}}</el-dropdown-item>
                    
                    </el-dropdown-menu>
                     </template>
                    </el-dropdown>
                  </div>
                  <div style="height:300px;" v-if="datasourceType == 1">
                    <div style="height:100%;width:75%;float:left;">
                        <codemirror ref="codeMirror"  :options="cmOptions"  v-model:value="sqlText"></codemirror>
                    </div>
                    <div style="height:100%;width:24.5%;float:right;" class="tablecolumn">
                        <el-tag type="success" size="small" style="cursor:pointer">请选择解析表</el-tag>
                        <el-select v-model="datasourceTableName" placeholder="请选择解析表" size="small" @change="getTableColumns" filterable  style="width:150px">
                            <el-option v-for="op in dataSourceTables" :label="op.name" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                        <div class="dataset-box-content2" >
                            <!-- <div class="column-tag" v-for="(column,index) in tableColumns" :key="index" :title="column.name"><icon-copy @click="copyColumn(null,column.name)"/>{{column.name}}</div> -->
                            <div class="column-tag" v-for="(column,index) in tableColumns" :key="index" :title="column.name">
                                <el-dropdown>
                                    <icon-add-one style="margin-top:8px"/>
                                    <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item v-on:click="getWhereByColumn(1,column)">仅字段</el-dropdown-item>
                                        <el-dropdown-item v-on:click="getWhereByColumn(2,column)">表名.字段</el-dropdown-item>
                                        <el-dropdown-item v-on:click="getWhereByColumn(3,column)">查询条件(=)</el-dropdown-item>
                                        <el-dropdown-item v-on:click="getWhereByColumn(4,column)">查询条件(in)</el-dropdown-item>
                                    </el-dropdown-menu>
                                    </template>
                                </el-dropdown>
                                {{column.name}}
                            </div>
                        </div>
                    </div>
                  </div>
                  <div style="height:1px"></div>
                  <div>
                      <!--表格 start-->
                      <el-table :data="sqlColumnTableData.tableData.slice((sqlColumnTableData.tablePage.currentPage-1)*sqlColumnTableData.tablePage.pageSize,sqlColumnTableData.tablePage.currentPage*sqlColumnTableData.tablePage.pageSize)" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                      <el-table-column prop="columnName" label="列名"  align="center"></el-table-column>
                      <el-table-column prop="name" label="别名"  align="center"></el-table-column>
                      <el-table-column prop="dataType" label="数据类型"  align="center"></el-table-column>
                      <el-table-column prop="width" label="宽度"  align="center"></el-table-column>
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
                          :total="sqlColumnTableData.tablePage.pageTotal">
                          </el-pagination>
                          <!--分页 end-->
                  </div>
                  </div>
                  </el-tab-pane>
                  <el-tab-pane label="参数配置" >
                      <div v-show="sqlForm.sqlType == '1' || datasourceType == 2">
                      <el-divider content-position="left" v-if="datasourceType == 1 || datasourceType == 2">分页参数</el-divider>
                      <el-form :inline="true" :model="paginationForm" class="demo-form-inline" ref="paginationRef">
                          <el-form-item label="是否分页"  prop="isPagination" v-if="datasourceType == 1 || datasourceType == 2">
                              <el-select v-model="paginationForm.isPagination" placeholder="是否分页" size="small">
                              <el-option label="是" :value=1></el-option>
                              <el-option label="否" :value=2></el-option>
                              </el-select>
                          </el-form-item>
                          <el-form-item v-if="paginationForm.isPagination == '1'" label="每页条数"  prop="pageCount" :rules="filter_rules('每页条数',{required:true})">
                              <!-- <el-input v-model="paginationForm.pageCount" placeholder="每页条数" size="small"></el-input> -->
                              <el-select v-model="paginationForm.pageCount" placeholder="请选择" size="small">
                                <el-option
                                    v-for="item in selectUtil.pageCount"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                                </el-select>
                          </el-form-item>
                          <el-form-item v-if="paginationForm.isPagination == '1' && datasourceType == 2" label="当前页参数属性"  prop="currentPageAttr" :rules="filter_rules('当前页参数属性',{required:true})">
                                <el-input v-model="paginationForm.currentPageAttr" placeholder="当前页参数属性" size="small"></el-input>
                          </el-form-item>
                          <el-form-item v-if="paginationForm.isPagination == '1' && datasourceType == 2" label="每页条数参数属性"  prop="pageCountAttr" :rules="filter_rules('每页条数参数属性',{required:true})">
                                <el-input v-model="paginationForm.pageCountAttr" placeholder="每页条数参数属性" size="small"></el-input>
                          </el-form-item>
                          <el-form-item v-if="paginationForm.isPagination == '1' && datasourceType == 2" label="总条数属性"  prop="totalAttr" :rules="filter_rules('总条数条数属性',{required:true})">
                                <el-input v-model="paginationForm.totalAttr" placeholder="总条数属性" size="small"></el-input>
                          </el-form-item>
                      </el-form>
                      </div>
                      <div v-show="sqlForm.sqlType == 1 || datasourceType == 2">
                      <el-divider content-position="left">字段参数</el-divider>
                      <el-form :inline="true" :model="paramForm" class="demo-form-inline" ref="paramRef">
                          <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                          <el-input v-model="paramForm.paramName" placeholder="参数名称" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                          <el-input v-model="paramForm.paramCode" placeholder="参数编码" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数前缀" v-if="datasourceType == 2"  prop="paramPrefix" :rules="filter_rules('参数前缀',{required:false})">
                          <el-input v-model="paramForm.paramPrefix" placeholder="参数前缀" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
                          <el-select v-model="paramForm.paramType" placeholder="参数类型"  size="small">
                              <el-option label="字符串" value="varchar"></el-option>
                              <el-option label="数值" value="number"></el-option>
                              <el-option label="日期" value="date"></el-option>
                              <el-option label="下拉单选" value="select"></el-option>
                              <el-option label="下拉多选" value="mutiselect"></el-option>
                              <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                              <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'date'" label="日期格式" prop="dateFormat" :rules="filter_rules('日期格式',{required:false})">
                          <el-select v-model="paramForm.dateFormat" placeholder="日期格式"  size="small">
                              <el-option label="年" value="YYYY"></el-option>
                              <el-option label="年-月" value="YYYY-MM"></el-option>
                              <el-option label="年-月-日" value="YYYY-MM-DD"></el-option>
                              <el-option label="年-月-日 时:分" value="YYYY-MM-DD HH:mm"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="默认值">
                          <el-input v-model="paramForm.paramDefault" placeholder="默认值" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="是否必填" prop="paramRequired" :rules="filter_rules('是否必填',{required:true})">
                          <el-select v-model="paramForm.paramRequired" placeholder="是否必填" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="是否隐藏" prop="paramHidden" :rules="filter_rules('是否隐藏',{required:true})"  key="paramHidden">
                          <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" label="选择内容来源" prop="selectType" key="selectType" :rules="filter_rules('选择内容来源',{required:true})">
                          <el-select v-model="paramForm.selectType" placeholder="选择内容来源" @change="changeSlectType" size="small">
                              <el-option label="自定义" value="1"></el-option>
                              <el-option label="sql语句" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item  v-if="(paramForm.paramType == 'select' && paramForm.selectType == '2') || (paramForm.paramType == 'mutiselect' && paramForm.selectType == '2') || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" label="选择数据源" prop="datasourceId" :rules="filter_rules('选择数据源',{required:true})">
                          <el-select v-model="paramForm.datasourceId" placeholder="选择数据源" size="small">
                              <el-option v-for="op in dataSource" :label="op.dataSourceName" :value="op.datasourceId" :key="op.datasourceId"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2'" label="是否依赖其他参数" prop="isRelyOnParams" key="isRelyOnParams" :rules="filter_rules('是否依赖其他参数',{required:true})">
                          <el-select v-model="paramForm.isRelyOnParams" placeholder="是否依赖其他参数" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2' && paramForm.isRelyOnParams == '1'" label="依赖参数代码" prop="relyOnParams" key="relyOnParams" :rules="filter_rules('依赖参数代码',{required:true})">
                            <el-input v-model="paramForm.relyOnParams" placeholder="依赖参数代码" size="small"></el-input>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'multiTreeSelect'" label="父子联动" prop="checkStrictly" key="checkStrictly" :rules="filter_rules('父子联动',{required:true})">
                          <el-select v-model="paramForm.checkStrictly" placeholder="选择父子联动" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'  || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" label="下拉选择内容" key="selectContent" prop="selectContent" :rules="filter_rules('下拉选择内容',{required:true})">
                          <el-input type="textarea" :cols="80" v-model="paramForm.selectContent" placeholder="下拉选择内容" size="small"></el-input>
                          <div class="sub-title">{{selectContentSuggestion}}</div>
                          </el-form-item>
                           <el-form-item>
                            <el-button type="primary" @click="addParam" size="small">添加</el-button>
                          </el-form-item>
                          <el-tag v-if="paramForm.paramType == 'date'" type="warning">注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag>
                          <el-tag v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" type="warning">自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}] 注意：两个key必须是value 和 name</el-tag><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'">
                          <el-tag v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" type="warning">sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有 value 和 name</el-tag>
                          <el-tag v-if="paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" type="warning">sql语句格式：select deptId as id, deptName as name,parentId as pid from table 注意：返回的属性中必须有 id,name和pid</el-tag>
                          
                      </el-form>
                      <div style="height:2px"></div>
                      <div style="height:50%">
                      <!--表格 start-->
                      <el-table :data="paramTableData.tableData" border style="width: 98%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                      <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                      <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                      <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                      <el-table-column prop="paramDefault" label="默认值"  align="center"></el-table-column>
                      <el-table-column prop="paramRequired" label="是否必填"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="paramHidden" label="是否隐藏"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="isRelyOnParams" label="是否依赖其他参数"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="relyOnParams" label="依赖参数"  align="center"></el-table-column>
                      <el-table-column  label="操作"  align="center">
                          <template #default="scope">
                              <el-button @click="editParam(scope.row)" type="primary" text size="small">编辑</el-button>
                              <el-button @click="moveUp(scope.$index,'3')" type="primary" text size="small">上移</el-button>
                              <el-button @click="moveDown(scope.$index,'3')" type="primary" text size="small">下移</el-button>
                              <el-button @click="deleteParam(scope.$index)" type="primary" text size="small">删除</el-button>
                          </template>
                      </el-table-column>
                      </el-table>
                          <!--表格 end-->
                      </div>
                      </div>
                      <div v-show="sqlForm.sqlType == 2 && datasourceType == 1">
                          <el-divider content-position="left">输入参数</el-divider>
                          <el-form :inline="true" :model="procedureParamForm" class="demo-form-inline" ref="inParamRef">
                              <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                                  <el-input v-model="procedureParamForm.paramName" placeholder="参数名称" ></el-input>
                              </el-form-item>
                              <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                                  <el-input v-model="procedureParamForm.paramCode" placeholder="参数编码"></el-input>
                              </el-form-item>
                              <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
                              <el-select v-model="procedureParamForm.paramType" placeholder="参数类型"  >
                                  <el-option label="int" value="int"></el-option>
                                  <el-option label="String" value="String"></el-option>
                                  <el-option label="Long" value="Long"></el-option>
                                  <el-option label="BigDecimal" value="BigDecimal"></el-option>
                                  <el-option label="Double" value="Double"></el-option>
                                  <el-option label="Float" value="Float"></el-option>
                                  <el-option label="Date" value="Date"></el-option>
                                  <el-option label="DateTime" value="DateTime"></el-option>
                              </el-select>
                              </el-form-item>
                              <el-form-item label="默认值" prop="paramDefault" :rules="filter_rules('默认值',{required:false})">
                                  <el-input v-model="procedureParamForm.paramDefault" placeholder="默认值"></el-input>
                              </el-form-item>
                              <el-form-item label="是否必填" prop="paramRequired" :rules="filter_rules('是否必填',{required:true})">
                                <el-select v-model="procedureParamForm.paramRequired" placeholder="是否必填" size="small">
                                    <el-option label="是" value="1"></el-option>
                                    <el-option label="否" value="2"></el-option>
                                </el-select>
                             </el-form-item>
                              <el-form-item v-if="procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime'" label="组件类型" prop="componentType" :rules="filter_rules('组件类型',{required:true})">
                              <el-select v-model="procedureParamForm.componentType" placeholder="组件类型"  >
                                   <el-option label="输入框" value="input"></el-option>
                                   <el-option label="下拉单选" value="select"></el-option>
                                   <el-option label="下拉多选" value="mutiselect"></el-option>
                                   <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                                   <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                              </el-select>
                              </el-form-item>
                              <el-form-item v-if="(procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime') && (procedureParamForm.componentType == 'select' || procedureParamForm.componentType == 'mutiselect')" label="选择内容来源" key="selectType" prop="selectType" :rules="filter_rules('选择内容来源',{required:true})">
                                <el-select v-model="procedureParamForm.selectType" placeholder="选择内容来源"  size="small">
                                    <el-option label="自定义" value="1"></el-option>
                                    <el-option label="sql语句" value="2"></el-option>
                                </el-select>
                               </el-form-item>
                               <el-form-item  v-if="(procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime') && ((procedureParamForm.componentType == 'select' && procedureParamForm.selectType == '2') || (procedureParamForm.componentType == 'mutiselect' && procedureParamForm.selectType == '2') || procedureParamForm.componentType == 'treeSelect' || procedureParamForm.componentType == 'multiTreeSelect')" label="选择数据源" prop="datasourceId" :rules="filter_rules('选择数据源',{required:true})">
                                <el-select v-model="procedureParamForm.datasourceId" placeholder="选择数据源" size="small">
                                    <el-option v-for="op in dataSource" :label="op.dataSourceName" :value="op.datasourceId" :key="op.datasourceId"></el-option>
                                </el-select>
                                </el-form-item>
                                <el-form-item v-if="(procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime') &&(procedureParamForm.componentType == 'select' && procedureParamForm.selectType == '2')" label="是否依赖其他参数" prop="isRelyOnParams" key="isRelyOnParams" :rules="filter_rules('是否依赖其他参数',{required:true})">
                                <el-select v-model="procedureParamForm.isRelyOnParams" placeholder="是否依赖其他参数" size="small">
                                    <el-option label="是" value="1"></el-option>
                                    <el-option label="否" value="2"></el-option>
                                </el-select>
                                </el-form-item>
                                <el-form-item v-if="(procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime') && (procedureParamForm.componentType == 'multiTreeSelect')" label="父子联动" prop="checkStrictly" key="checkStrictly" :rules="filter_rules('父子联动',{required:true})">
                                <el-select v-model="procedureParamForm.checkStrictly" placeholder="选择父子联动" size="small">
                                    <el-option label="是" value="1"></el-option>
                                    <el-option label="否" value="2"></el-option>
                                </el-select>
                                </el-form-item>
                                <el-form-item v-if="(procedureParamForm.paramType != 'Date' && procedureParamForm.paramType != 'DateTime') && (procedureParamForm.componentType == 'select' || procedureParamForm.componentType == 'mutiselect' || procedureParamForm.componentType == 'treeSelect' || procedureParamForm.componentType == 'multiTreeSelect')" key="selectContent" label="下拉选择内容" prop="selectContent" :rules="filter_rules('下拉选择内容',{required:true})">
                                <el-input type="textarea" :cols="80" v-model="procedureParamForm.selectContent" placeholder="下拉选择内容" size="small"></el-input>
                                <div class="sub-title">{{selectContentSuggestion}}</div>
                                </el-form-item>
                              <el-form-item v-if="procedureParamForm.paramType == 'Date'" label="日期格式" prop="dateFormat" :rules="filter_rules('日期格式',{required:false})">
                                <el-select v-model="procedureParamForm.dateFormat" placeholder="日期格式"  size="small">
                                    <el-option label="年" value="YYYY"></el-option>
                                    <el-option label="年-月" value="YYYY-MM"></el-option>
                                    <el-option label="年-月-日" value="YYYY-MM-DD"></el-option>
                                </el-select>
                                </el-form-item>
                              <el-form-item label="是否隐藏" prop="paramHidden" :rules="filter_rules('是否隐藏',{required:true})">
                                <el-select v-model="procedureParamForm.paramHidden" placeholder="是否隐藏" >
                                    <el-option label="是" value="1"></el-option>
                                    <el-option label="否" value="2"></el-option>
                                </el-select>
                                </el-form-item>
                              <el-form-item>
                              <el-button type="primary" @click="addInParam">添加</el-button>
                              </el-form-item>
                          </el-form>
                          <el-tag v-if="procedureParamForm.paramType == 'Date' || procedureParamForm.paramType == 'DateTime'" type="warning">注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag>
                          <el-tag v-if="procedureParamForm.componentType == 'select' || procedureParamForm.componentType == 'mutiselect'" type="warning">自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}] 注意：两个key必须是value 和 name</el-tag><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'">
                          <el-tag v-if="procedureParamForm.componentType == 'select' || procedureParamForm.componentType == 'mutiselect'" type="warning">sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有 value 和 name</el-tag>
                          <el-tag v-if="procedureParamForm.componentType == 'treeSelect' || procedureParamForm.componentType == 'multiTreeSelect'" type="warning">sql语句格式：select deptId as id, deptName as name,parentId as pid from table 注意：返回的属性中必须有 id,name和pid</el-tag>
                          <div style="height:40%">
                              <!--表格 start-->
                              <el-table :data="procedureInParamTableData.tableData" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                              <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                              <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                              <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                              <el-table-column prop="paramDefault" label="默认值"  align="center"></el-table-column>
                              <el-table-column prop="paramHidden" label="是否隐藏"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                              <el-table-column fixed="right" label="操作" width="180" align="center">
                                  <template #default="scope">
                                      <el-button @click="editInParam(scope.row)" type="primary" size="small" text>编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'1')" type="primary" size="small" text>上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'1')" type="primary" size="small" text>下移</el-button>
                                      <el-button @click="deleteInParam(scope.$index)" type="primary" size="small" text>删除</el-button>
                                  </template>
                              </el-table-column>
                              </el-table>
                              <!--表格 end-->
                          </div>
                          <el-divider content-position="left">输出参数</el-divider>
                          <el-form :inline="true" :model="procedureOutParamForm" class="demo-form-inline" ref="outParamRef">
                              <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                                  <el-input v-model="procedureOutParamForm.paramName" placeholder="参数名称" ></el-input>
                              </el-form-item>
                              <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                                  <el-input v-model="procedureOutParamForm.paramCode" placeholder="参数编码"></el-input>
                              </el-form-item>
                              <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
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
                          <div style="height:30%">
                              <!--表格 start-->
                              <el-table :data="procedureOutParamTableData.tableData" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                              <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                              <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                              <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                              <el-table-column fixed="right" label="操作" width="180" align="center">
                                  <template #default="scope">
                                      <el-button @click="editOutParam(scope.row)" type="primary" size="small" text>编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'2')" type="primary" size="small" text>上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'2')" type="primary" size="small" text>下移</el-button>
                                      <el-button @click="deleteOutParam(scope.$index)" type="primary" size="small" text>删除</el-button>
                                  </template>
                              </el-table-column>
                              </el-table>
                              <!--表格 end-->
                          </div>
                      </div>
                  </el-tab-pane>
                  </el-tabs>
                  <template #footer>
                  <span  class="dialog-footer">
                  <el-button @click="closeAddDataSet" size="small">取 消</el-button>
                  <el-button type="primary" @click="addDataSet" size="small">确 定</el-button>
                  </span>
                  </template>
              </el-dialog>
              <el-dialog title="循环块" v-model="blockVisiable" width="50%"  :close-on-click-modal='false' @close='closeBlockDialog'>
                <el-form :inline="true" :model="blockForm" class="demo-form-inline" ref="blockRef">
                    <el-form-item label="起始单元格"  prop="startCell" key="startCell" :rules="filter_rules('起始单元格',{required:true})">
                        <el-input v-model="blockForm.startCell" placeholder="起始单元格" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="结束单元格"  prop="endCell" key="endCell" :rules="filter_rules('结束单元格',{required:true})">
                        <el-input v-model="blockForm.endCell" placeholder="结束单元格" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="聚合方式"  prop="aggregateType" key="aggregateType" :rules="filter_rules('聚合方式',{required:true})">
                        <el-select  placeholder="聚合方式" size="small" v-model="blockForm.aggregateType" >
                            <el-option label="列表" value="list"></el-option>
                            <el-option label="分组" value="group"></el-option>
                          </el-select>
                    </el-form-item>
                    <el-form-item label="分组属性"  v-show="blockForm.aggregateType=='group'">
                        <el-input v-model="blockForm.groupProperty" placeholder="分组属性" size="small"></el-input>
                    </el-form-item>
                </el-form>
                <el-alert
                    title="说明：起始结束单元格为单元格的坐标，如起始单元格坐标为A1，结束单元格坐标为E20，则循环块范围为A1:E20"
                    type="warning" :closable="false">
                </el-alert>
                <template #footer>
                <span  class="dialog-footer">
                    <el-button @click="closeBlockDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddBlock" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="单元格过滤" v-model="cellConditionVisiable" width="50%"  :close-on-click-modal='false' @close='closeCellConditionDialog'>
                <el-form :inline="true" :model="cellConditionForm" class="demo-form-inline" ref="conditionRef">
                    <el-form-item label="属性"  prop="property" key="property" :rules="filter_rules('属性',{required:true})">
                        <el-input v-model="cellConditionForm.property" placeholder="属性" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="操作符"  prop="operator" key="operator" :rules="filter_rules('操作符',{required:true})">
                        <!-- <el-input v-model="cellConditionForm.operator" placeholder="操作符" size="small"></el-input> -->
                        <el-select  placeholder="操作符" size="small" v-model="cellConditionForm.operator">
                            <el-option v-for="op in selectUtil.operate" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据类型"  prop="type" key="type" :rules="filter_rules('数据类型',{required:true})">
                        <el-select  placeholder="数据类型" size="small" v-model="cellConditionForm.type">
                            <el-option v-for="op in selectUtil.type" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="cellConditionForm.type == 'date'" key="dateFormat" label="日期格式"  prop="dateFormat" :rules="filter_rules('日期格式',{required:false})">
                         <el-select  placeholder="日期格式" size="small" v-model="cellConditionForm.dateFormat">
                            <el-option v-for="op in selectUtil.dateFormat2" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="条件值"  prop="value" key="value" :rules="filter_rules('条件值',{required:true})">
                        <el-input type="textarea" :rows="2" style="width:480px" v-model="cellConditionForm.value" placeholder="条件值" size="small"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeCellConditionDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddCellCondition" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="单元格隐藏" v-model="cellHiddenConditionVisiable" width="50%" :close-on-click-modal='false' @close='closeCellHiddenConditionDialog'>
                <el-form :inline="true" :model="cellHiddenConditionForm" class="demo-form-inline" ref="hiddenConditionRef">
                    <el-form-item label="参数名称"  prop="propertyName" key="propertyName" :rules="filter_rules('参数名称',{required:true})">
                        <el-input v-model="cellHiddenConditionForm.propertyName" placeholder="参数名称" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="参数编码"  prop="property" key="property2" :rules="filter_rules('参数编码',{required:true})">
                        <el-input v-model="cellHiddenConditionForm.property" placeholder="参数编码" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="操作符"  prop="operator"  key="operator2" :rules="filter_rules('操作符',{required:true})">
                        <el-select  placeholder="操作符" size="small" v-model="cellHiddenConditionForm.operator">
                            <el-option v-for="op in selectUtil.operate" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据类型"  prop="type" key="type2" :rules="filter_rules('数据类型',{required:true})">
                        <el-select  placeholder="数据类型" size="small" v-model="cellHiddenConditionForm.type">
                            <el-option v-for="op in selectUtil.type2" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="cellHiddenConditionForm.type == 'date'" label="日期格式"  prop="dateFormat" key="dateFormat2" :rules="filter_rules('日期格式',{required:false})">
                         <el-select  placeholder="日期格式" size="small" v-model="cellHiddenConditionForm.dateFormat">
                            <el-option v-for="op in selectUtil.dateFormat2" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="条件值"  prop="value" key="value2" :rules="filter_rules('条件值',{required:true})">
                        <el-input type="textarea" :rows="2" style="width:480px" v-model="cellHiddenConditionForm.value" placeholder="条件值" size="small"></el-input>
                    </el-form-item>
                </el-form>
                 <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeCellHiddenConditionDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddCellHiddenCondition" size="small">确 定</el-button>
                </span>
                 </template>
            </el-dialog>
            <el-dialog title="x轴属性" v-model="xAxisVisiable"  :close-on-click-modal='false' @close='closexAxisData'>
                <el-form :inline="true" :model="xAxisForm" class="demo-form-inline" ref="xAxisRef">
                    <el-form-item label="图表"  prop="chartId" :rules="filter_rules('图表',{required:true})">
                        <el-select  placeholder="图表" size="small" v-model="xAxisForm.chartId" @change="getChartTile()">
                            <el-option v-for="op in sheetCharts" :label="op.title" :value="op.chartId" :key="op.chartId"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据来源"  prop="dataType" :rules="filter_rules('数据来源',{required:true})">
                        <el-select  placeholder="数据来源" size="small" v-model="xAxisForm.dataType">
                            <el-option v-for="op in selectUtil.xAxisDataType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="xAxisForm.dataType == 2" label="数据集"  prop="datasetId" :rules="filter_rules('数据集',{required:true})">
                        <el-select  placeholder="数据集" size="small" v-model="xAxisForm.datasetId" @change="getDatasetAttrs()">
                            <el-option v-for="op in datasets" :label="op.datasetName" :value="op.id" :key="op.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="xAxisForm.dataType == 2" label="数据集属性"  prop="attr" :rules="filter_rules('数据集属性',{required:true})">
                        <el-select  placeholder="数据集属性" size="small" v-model="xAxisForm.attr">
                            <el-option v-for="op in dataSetAttrs" :label="op.name" :value="op.name" :key="op.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="xAxisForm.dataType == 1" label="x轴数据"  prop="xAxisDatas" :rules="filter_rules('x轴数据',{required:true})">
                        <el-input type="textarea" :rows="2" style="width:480px" v-model="xAxisForm.xAxisDatas" placeholder="x轴数据" size="small"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closexAxisData" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddxAxisData" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="单元格小计" v-model="cellSubTotalVisiable" width="50%" :close-on-click-modal='false' @close='closeSubtotalDialog'>
                <el-form :inline="true" :model="cellSubTotalForm" class="demo-form-inline" ref="subtotalRef">
                    <el-form-item label="单元格"  prop="coords" :rules="filter_rules('单元格',{required:true})">
                        <el-input v-model="cellSubTotalForm.coords" placeholder="单元格,例如A1" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="小计类型"  prop="type" :rules="filter_rules('小计类型',{required:true})">
                        <el-select  placeholder="小计类型" size="small" v-model="cellSubTotalForm.type">
                            <el-option v-for="op in selectUtil.subtotalType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="小数位数" size="small" :rules="filter_rules('小数位数',{required:true})">
                        <el-input v-model.number="cellSubTotalForm.digit" style="width:150px"  placeholder="小数位数"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeSubtotalDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddSubtotal" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="分组小计链" v-model="subTotalCalcVisiable" width="50%" :close-on-click-modal='false' @close='closeSubtotalCalcDialog'>
                <el-form :inline="true" :model="subTotalCalcForm" class="demo-form-inline" ref="subtotalCalcRef">
                    <el-form-item label="字段"  prop="attrs" :rules="filter_rules('字段',{required:true})">
                        <el-select  style="width:400px" placeholder="请选择字段" size="small" multiple filterable clearable v-model="subTotalCalcForm.attrs">
                            <el-option v-for="op in dataSetAttrs" :label="op.name" :value="op.name" :key="op.name"></el-option>
                          </el-select>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeSubtotalCalcDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddSubtotalCalc" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="小计属性" v-model="subTotalAttrsVisiable" width="50%" :close-on-click-modal='false' @close='closeSubtotalAttrDialog'>
                <el-form :inline="true" :model="subTotalAttrsForm" class="demo-form-inline" ref="subtotalAttrsRef">
                    <el-form-item label="小计名称"  prop="name" :rules="filter_rules('小计名称',{required:true})">
                        <el-input v-model="subTotalAttrsForm.name" placeholder="小计名称" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="字体颜色"  prop="fontColor" :rules="filter_rules('字体颜色',{required:true})">
                        <el-color-picker v-model="subTotalAttrsForm.fontColor" size="small"  :predefine="commonConstants.predefineColors"></el-color-picker>
                    </el-form-item>
                    <el-form-item label="字体大小"  prop="fontSize" :rules="filter_rules('字体大小',{required:true})">
                        <el-select  placeholder="请选择字体大小" size="small" clearable v-model="subTotalAttrsForm.fontSize">
                            <el-option v-for="op in selectUtil.fontSize" :label="op.label" :value="op.value" :key="op.value"></el-option>
                          </el-select>
                    </el-form-item>
                    <el-form-item label="是否加粗"  prop="fontWeight" :rules="filter_rules('是否加粗',{required:true})">
                        <el-switch v-model="subTotalAttrsForm.fontWeight"/>
                    </el-form-item>
                    <el-form-item label="背景颜色"  prop="bgColor" :rules="filter_rules('背景颜色',{required:true})">
                        <el-color-picker v-model="subTotalAttrsForm.bgColor" size="small"  :predefine="commonConstants.predefineColors"></el-color-picker>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeSubtotalAttrDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddSubtotalAttrs" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog :title="authTitle" v-model="addAuthVisiable" width="650px"  modal-class="addauthdialog"  :modal="true" :close-on-click-modal='false' @close='closeAddAuth'>
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
                    :default-checked-keys="defaultCheckedUsers">
                    </el-tree>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeAddAuth" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddAuth" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <modal
                ref="settingRef"
                :modalConfig="settingModalConfig"
                :modalForm="settingModalForm"
                :modalData="settingFormData"
                :modalHandles="settingModalHandles"
                @closeModal="closeSettingModal()"
            ></modal>
            <el-dialog
                :modal="false"
                :close-on-click-modal='false'
                :title="authedRangeTitle"
                v-model="authdialogVisible"
                @close="closeAuthDialog"
                modal-class="authdialog"
                width="240px"
                >
                <div class="el-dialog-div" v-if="authedRange && authedRange.length > 0" >
                    <div v-for="(item,index) in authedRange" :key="index">
                    <el-descriptions title="" :column="1" border>
                        <el-descriptions-item label="保护范围">{{item.rangeAxis}}</el-descriptions-item>
                        <el-descriptions-item label="授权人数" v-if="isCreator">{{item.userIds.length}}</el-descriptions-item>
                    </el-descriptions>
                    <div style="text-align:right;margin-top:5px" v-if="isCreator">
                    <el-button type="primary" title="编辑" icon="icon-edit" circle size="small" @click="editRange(item)"></el-button>
                        <el-button type="warning" icon="icon-monitor-one" title="显示选区" circle size="small" @click="showRange(item)"></el-button>
                        <el-button type="danger" icon="icon-delete" title="删除" circle size="small" @click="deleteRange(item,index)"></el-button>
                    </div>
                    <el-divider content-position="left"></el-divider>
                    </div>
                </div>
                <el-empty v-if="(!authedRange || authedRange.length == 0) && isCreator" description="暂无授权信息"></el-empty>
                <el-empty v-if="(!authedRange || authedRange.length == 0) && !isCreator" description="暂无操作权限"></el-empty>
            </el-dialog>
            <vchart :show="vchartShow" @closeModal="closeAddChartModal()"></vchart>
            <textarea id="clipboradInput" value="" style="opacity:0;position:absolute" />
    </div>
</template>

<script src="./luckyReportDesign.js"></script>

<style scoped  lang="scss">
.pagebox {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: #ffffff;
}
.left {
    box-sizing: border-box;
    width: 245px;
    height: 99vh;
    background: #FFFFFF;
    // box-shadow: 0px 4px 8px #D2E3FF;
    overflow-y:auto;
    overflow-x:hidden;
    border-top: 1px solid #E7E7E7;
}
.left-head{
    box-sizing: border-box;
    position: absolute;
    width: 240px;
    height: 32px;
    left: 0px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.left-head .el-icon-s-fold{
    position: absolute;
    right: 5%;
    top: 18.75%;
    bottom: 18.66%;
}
.left-dataset-title{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 5px 16px;
    gap: 62px;
    width: 240px;
    height: 40px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 1;
    flex-grow: 0;
    // margin-top: 2px;
}
.dataset-title{
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
.right-dataset-title{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 62px;
    width: 240px;
    height: 40px;
    background: #FFFFFF;
    /* border-bottom: 1px solid rgba(0, 0, 0, 0.1); */
    flex: none;
    order: 1;
    flex-grow: 0;
    margin-top: 2px;
}
.attr-dataset-title{
    width: 100px;
    height: 32px;
    font-style: normal;
    font-weight: 500;
    font-size: 14px;
    line-height: 32px;
    color:#606266;
    flex: none;
    order: 0;
    flex-grow: 0;
    font-weight: bold;
}
.addBtn{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 2px 12px;
    gap: 4px;
    width: 66px;
    height: 22px;
    background: #45c5a9;
    border-radius: 4px;
    flex: none;
    order: 1;
    flex-grow: 0;
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    font-size: 12px;
    line-height: 18px;
    color: #FFFFFF;
}
.dataset-box{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    padding: 7px 16px;
    gap: 103px;

    width: 240px;
    height: 36px;

    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 0;
    flex-grow: 0;
}
.el-icon-arrow-right{
    position: absolute;
    left: 5.25%;
    cursor:pointer;
}
.el-icon-arrow-down{
    position: absolute;
    left: 5.25%;
    cursor:pointer;
}
.dataset-name{
    width: 140px;
    height: 22px;
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #191919;
    flex: none;
    order: 1;
    flex-grow: 0;
    max-width: 190px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow:ellipsis;
    padding-right:50px;
    cursor:pointer;
}
.el-icon-edit{
    position: absolute;
    right: 17.3%;
    color: #ED7B2F;
    cursor:pointer;
}
.el-icon-delete{
    position: absolute;
    right: 7.3%;
    color: #ED7B2F;
    cursor:pointer;
}

.right-el-icon-edit{
    position: absolute;
    right: 32.3%;
    cursor:pointer;
}
.right-block-el-icon-edit{
    position: absolute;
    right: 20.3%;
    cursor:pointer;
}
.right-el-icon-copy-document{
    position: absolute;
    right: 20.3%;
    cursor:pointer;
}
.right-el-icon-delete{
    position: absolute;
    right: 8.3%;
    cursor:pointer;
}

.dataset-box-active{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    padding: 7px 16px;
    gap: 103px;
    /* background: #A5C3F5; */
    width: 240px;
    height: 36px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 0;
    flex-grow: 0;
}
.dataset-box-content{
    width: 240px;
    min-height:150px;
    /* background: #A5C3F5; */
    flex: none;
    order: 4;
    flex-grow: 0;
    max-height:400px;
    overflow-y: auto;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    padding-left: 5px;
    padding-top: 3px;
}
.dataset-box-content2{
    width: 100%;
    height:100%;
    /* background: #A5C3F5; */
    flex: none;
    order: 4;
    flex-grow: 0;
    max-height:270px;
    overflow-y: auto;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    padding-left: 5px;
    padding-top: 3px;
    scrollbar-width: none;
}
.column-tag{
    max-width:150px;
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
    border: 1px solid rgba(64,158,255,.2);
    font-weight: bold;
}
.center{
    flex: 1;
    /* height: 100vh; */
}
.right{
    width: 254px;
    height: 99vh;
    background: #FFFFFF;
    box-shadow: 0px 4px 8px #D2E3FF;
}
.right-head{
    box-sizing: border-box;
    position: absolute;
    width: 254px;
    height: 32px;
    right: 0px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.right-head .el-icon-s-unfold{
    position: absolute;
    left: 5%;
    top: 18.75%;
    bottom: 18.66%;
}
.right-title{
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    padding: 6px 4px;
    gap: 4px;
    width: 254px;
    height: 30px;
    background: #E7E7E7;
    flex: none;
    order: 1;
    align-self: stretch;
    flex-grow: 0;
    // margin-top: 2px;
}
.cell-property{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 3px 16px;
    gap: 4px;
    width: 50px;
    height: 22px;
    background: #FFFFFF;
    border-radius: 4px;
    flex: none;
    order: 0;
    flex-grow: 1;
    color:#45c5a9;
    font-size: 16px;
    cursor: pointer;
}
.cell-property-noactive{
    background: #E7E7E7;
    cursor: pointer;
    color:rgba(0, 0, 0, 0.6);;

}
.right-form{
    position: absolute;
    width: 254px;
    height: 100vh;
    right: 0px;
    // top: 50px;
    background: #FFFFFF;
    overflow: auto;
}
.demo-form-inline{
    padding-left: 8px;
}
.blockBtn{
    width: 230px;
    height: 30px;
    background: #45c5a9;
    border-radius: 4px;
    line-height: 5px;
    border-color: #45c5a9;
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
  padding: 0px 20px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 19px;
  line-height: 30px;
  color: #45c5a9;
  font-weight: bold;
  margin: 5px 0;
}
._header {
  height: 45px !important;
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

:deep(.authdialog){
    pointer-events: none !important;
}
:deep(.authdialog .el-overlay-dialog) {
   width:300px !important;
   flex-direction: column !important;
   overflow: hidden !important;
   pointer-events: none !important;
}
:deep(.el-dialog){
    pointer-events: auto !important;
}
:deep(.authdialog .el-dialog){
    margin: 0 !important;
    top:50px !important;
    left: 2px !important;
}
:deep(.el-dialog__body){
    height: calc(100% - 90px) !important;
    overflow: auto;
}
:deep(.el-dialog-div){
     max-height: 60vh;
     overflow: auto;
     margin-left: 10px;
}
:deep(.el-dialog__title){
    font-weight: bold;
}

.el-divider--horizontal{
    margin: 10px 0
}
:deep(.el-tabs__content .el-tab-pane){
    height:600px;
    overflow: auto;
}

:deep(.codemirror-container) {
  border: 1px solid #eee;
}
.tablecolumn{
    border: 1px solid #eee;
}

:deep(.el-button.is-text){
    width:20px
}

.config-panel{
      background: #ffffff;
      margin-left: 1px;
      top:-40px;
      position: relative;
      width: 254px;
      height: 95%;
      display: flex;
      flex-direction: column;
      overflow: auto;
      .config-header{
        width: 100%;
        height: 32px;
        // background: #2F343D;
        font-size: 13px;
        font-weight: 400;
        color: #000000;
        line-height: 32px;
        text-align: center;
      }
      .config-box{
        flex:1;
        padding: 10px;
        overflow: auto;
      }

      /*定义滚动条的宽度*/
      .config-box::-webkit-scrollbar {
        width: 0;
      }
}
</style>
