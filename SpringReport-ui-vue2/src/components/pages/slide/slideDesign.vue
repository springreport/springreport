<template>
    <div v-loading="loading" :element-loading-text="loadingText" style="height: 100%;display: flex;flex-direction: column">
      <div style="flex: 1;height:100vh;display:flex">
        <div class="left">
            <div class="left-dataset-title">
                <span class="dataset-title">数据集管理</span>
                <el-button class="addBtn" @click="addDataSets">添加<i class="el-icon-plus el-icon--right"></i></el-button>
            </div>
            <div v-for="o in datasets" :key="o.id">
                <div :class="o.isActive?'dataset-box-active':'dataset-box'" style="position:relative">
                    <i :class="o.isActive?'el-icon-arrow-down el-icon-arrow-down_dataset':'el-icon-arrow-right'" @click="clickDatasets(o)"></i>
                    <span class="dataset-name" @click="clickDatasets(o)" :title="o.datasetName">{{o.datasetName}}
                       <el-dropdown>
                           <i class="el-icon-copy-document" title="复制"></i>
                           <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-on:click.native="copyAttr(5,o.datasetName)">列表</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(6,o.datasetName)">区块对</el-dropdown-item>
                          </el-dropdown-menu>
                          </el-dropdown>
                    </span>
                    <i class="el-icon-edit" @click="editDataSet(o)"></i>
                    <i class="el-icon-delete" @click="deleteDataSet(o)"></i>
                </div>
                <div class="dataset-box-content" v-if="o.isActive">
                        <p class="column-tag" v-for="(column,index) in o.columns" :key="index" :title="column.name" >
                          <el-dropdown>
                           <i class="el-icon-copy-document" title="复制"></i>
                           <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-on:click.native="copyAttr(1,o.datasetName,column.name)">文本</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(2,o.datasetName,column.name)">图片</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(3,o.datasetName,column.name)">列表文本</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(4,o.datasetName,column.name)">列表图片</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(7,o.datasetName,column.name)">区块对文本</el-dropdown-item>
                            <el-dropdown-item v-on:click.native="copyAttr(8,o.datasetName,column.name)">区块对图片</el-dropdown-item>
                          </el-dropdown-menu>
                          </el-dropdown>
                          {{column.name}}
                        </p>
                    <el-input v-show="o.apiResult" type="textarea" placeholder="" v-model="o.apiResult" rows="6"></el-input>
                </div>
            </div>
        </div>
        <div class="center">
            <iframe id="pptIframe" width="100%" height="100%" :src="pptSrc" style="border: none;"></iframe>
        </div>
    </div>
    <el-dialog title="数据集" :visible.sync="addDatasetsDialogVisiable" width="80%" height="80%" top="20px" :close-on-click-modal='false' @close='closeAddDataSet'>
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
                          <el-form-item  label="sql类型" prop="sqlType" :rules="filter_rules('选择数据源',{required:true})" v-if="datasourceType == 1">
                          <el-select v-model="sqlForm.sqlType" placeholder="选择sql类型" size="small">
                              <el-option v-for="op in selectUtil.sqlType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                          </el-select>
                          </el-form-item><br>
                          <el-form-item  label="系统变量">
                             <p class="column-tag" v-for="(item,index) in commonConstants.systemParam" :key="index" ><i class="el-icon-copy-document" title="复制" @click="doCopy(item)"></i>{{item.label}}({{item.value}})</p> 
                          </el-form-item>
                      </el-form>

                  <div style="height:25px;" v-if="datasourceType == 1">
                  <el-tooltip content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中" placement="bottom"><el-tag type="success" @click="execSql" size="small" style="cursor:pointer" ><i class="el-icon-caret-right"></i>执行</el-tag></el-tooltip>
                  <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"><el-tag @click="formatSql" size="small" style="cursor:pointer"><i class="el-icon-document"></i>格式化</el-tag> </el-tooltip>
                  </div>
                  <div style="height:300px;" v-if="datasourceType == 1">
                  <codemirror ref="codeMirror"  :options="cmOptions"></codemirror>
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
                      <div v-show="sqlForm.sqlType == 1 || datasourceType == 2">
                      <el-divider content-position="left">字段参数</el-divider>
                      <el-form :inline="true" :model="paramForm" class="demo-form-inline" ref="paramRef">
                          <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                          <el-input v-model="paramForm.paramName" placeholder="参数名称" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                          <el-input v-model="paramForm.paramCode" placeholder="参数编码" size="small"></el-input>
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
                              <el-option label="yyyy-MM-dd" value="yyyy-MM-dd"></el-option>
                              <el-option label="yyyy-MM" value="yyyy-MM"></el-option>
                              <el-option label="yyyy-MM-dd HH:mm" value="yyyy-MM-dd HH:mm"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="默认值" prop="paramDefault">
                          <el-input v-model="paramForm.paramDefault" placeholder="默认值" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="是否必填" prop="paramRequired" :rules="filter_rules('是否必填',{required:true})">
                          <el-select v-model="paramForm.paramRequired" placeholder="是否必填" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="是否隐藏" prop="paramHidden" :rules="filter_rules('是否隐藏',{required:true})" key="paramHidden">
                          <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" label="选择内容来源" key="selectType" prop="selectType" :rules="filter_rules('选择内容来源',{required:true})">
                          <el-select v-model="paramForm.selectType" placeholder="选择内容来源"  size="small">
                              <el-option label="自定义" value="1"></el-option>
                              <el-option label="sql语句" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2'" label="是否依赖其他参数" prop="isRelyOnParams" key="isRelyOnParams" :rules="filter_rules('是否依赖其他参数',{required:true})">
                          <el-select v-model="paramForm.isRelyOnParams" placeholder="是否依赖其他参数" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2' && paramForm.isRelyOnParams == '1'" label="依赖参数代码" key="relyOnParams" prop="relyOnParams" :rules="filter_rules('依赖参数代码',{required:true})">
                            <el-input v-model="paramForm.relyOnParams" placeholder="依赖参数代码" size="small"></el-input>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'multiTreeSelect'" label="父子联动" prop="checkStrictly" key="checkStrictly" :rules="filter_rules('父子联动',{required:true})">
                          <el-select v-model="paramForm.checkStrictly" placeholder="选择父子联动" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" key="selectContent" label="下拉选择内容" prop="selectContent" :rules="filter_rules('下拉选择内容',{required:true})">
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
                          <template slot-scope="scope">
                              <el-button @click="editParam(scope.row)" type="text" size="small">编辑</el-button>
                              <el-button @click="deleteParam(scope.$index)" type="text" size="small">删除</el-button>
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
                              </el-select>
                              </el-form-item>
                              <el-form-item label="默认值" prop="paramDefault" :rules="filter_rules('默认值',{required:true})">
                                  <el-input v-model="procedureParamForm.paramDefault" placeholder="默认值"></el-input>
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
                          <div style="height:40%">
                              <!--表格 start-->
                              <el-table :data="procedureInParamTableData.tableData" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                              <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                              <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                              <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                              <el-table-column prop="paramDefault" label="默认值"  align="center"></el-table-column>
                              <el-table-column prop="paramHidden" label="是否隐藏"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                              <el-table-column fixed="right" label="操作" width="180" align="center">
                                  <template slot-scope="scope">
                                      <el-button @click="editInParam(scope.row)" type="text" size="small">编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'1')" type="text" size="small">上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'1')" type="text" size="small">下移</el-button>
                                      <el-button @click="deleteInParam(scope.$index)" type="text" size="small">删除</el-button>
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
                                  <template slot-scope="scope">
                                      <el-button @click="editOutParam(scope.row)" type="text" size="small">编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'2')" type="text" size="small">上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'2')" type="text" size="small">下移</el-button>
                                      <el-button @click="deleteOutParam(scope.$index)" type="text" size="small">删除</el-button>
                                  </template>
                              </el-table-column>
                              </el-table>
                              <!--表格 end-->
                          </div>
                      </div>
                  </el-tab-pane>
                  </el-tabs>
                  <span slot="footer" class="dialog-footer">
                  <el-button @click="closeAddDataSet" size="small">取 消</el-button>
                  <el-button type="primary" @click="addDataSet" size="small">确 定</el-button>
                  </span>
              </el-dialog>
              <modal
              ref="commonModal"
              :modalConfig="modalConfig"
              :modalForm="modalForm"
              :modalData="modalData"
              :modalHandles="modalHandles"
              @closeModal="closeModal()"
            ></modal>
          <textarea id="clipboradInput" value="" style="opacity:0;position:absolute" />
  </div>
</template>

<script src="./slideDesign.js"></script>
<style scoped lang="scss">
.pagebox {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: #ffffff;
}
.left {
    box-sizing: border-box;
    width: 240px;
    height: 99vh;
    background: #FFFFFF;
    // box-shadow: 0px 4px 8px #D2E3FF;
    overflow-y:auto;
    overflow-x:hidden;
    border-top: 1px solid #E7E7E7;
    border-right: 1px solid #E7E7E7;
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
.el-icon-arrow-down_dataset{
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
    overflow:auto
    // height: 100vh;
}
.right{
    // top:50px;
    width: 254px;
    height: 99vh;
    background: #FFFFFF;
    // box-shadow: 0px 4px 8px #D2E3FF;
}
.right-head{
    // top:50px;
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
::v-deep .el-avatar{
    background:#45c5a9 !important
}

::v-deep .el-dialog__wrapper {
   overflow: hidden;
//    z-index: 2005 !important;
   pointer-events: none !important;
}

::v-deep .el-dialog{
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
} 
 ::v-deep .authdialog{
    margin-top: 50px !important;
    margin-left: 0px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    top:0 !important;
    left:0px!important;
    bottom: 0;
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
} 
.authdialog ::v-deep .el-dialog__body{
    height: calc(100% - 90px) !important;
    overflow: auto;
}
.authdialog ::v-deep .el-dialog-div{
     max-height: 60vh;
     overflow: auto;
     margin-left: 10px;
}
.authdialog ::v-deep .el-dialog-div::-webkit-scrollbar {
    display: none; /*隐藏滚动条*/
}
.authdialog ::v-deep .el-dialog__title{
    font-weight: bold;
}
.el-divider--horizontal{
    margin: 10px 0
}
::v-deep .el-tabs__content .el-tab-pane{
    height:600px;
    overflow: auto;
}
</style>
