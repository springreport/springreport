<!--
 * @Description: luckysheetreport预览页面
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:21
 * @LastEditors: caiyang
 * @LastEditTime: 2022-04-01 11:18:22
-->
<template>
  <el-scrollbar height="100vh" ref="scroll">
    <div class="_tablepage" style="height: 100%;display: flex;flex-direction: column;" v-loading="loading">
        <div style="width: 100%;flex: none;">
        <reportForm v-if="showReportForm" ref="reportRef" :formsReport="true" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" :showSearch="showSearch" :isParamMerge="isParamMerge">
        </reportForm>
        </div>
        <div style="width: 100%;flex: 1;overflow: auto;">
            <!-- <div class="tabledata" style="margin:auto;margin-bottom:20px" v-html="table"></div> -->
            <div id="luckysheet" style="margin:0px;padding:0px;width:100%;height:100%;left: 0px;top: 50px;"></div>
            <!-- <div id="print_html" ref="print" style="text-align: center;page-break-after:always"></div> -->
            <!-- <el-button ref="confirmPrintBtn" v-print="'#print_html'" v-show="false"></el-button> -->
        </div>
        <el-dialog title="打印" v-model="printDialogVisiable" width="50%" height="80%" :close-on-click-modal='false' @close='closePrintDialog'>
            <el-form :inline="true" :model="printForm" class="demo-form-inline" ref="printRef">
                <el-form-item label="起始单元格"  prop="start" :rules="filter_rules('起始单元格',{required:true})">
                    <el-input v-model="printForm.start" placeholder="起始单元格"></el-input>
                </el-form-item>
                <el-form-item label="结束单元格"  prop="end" :rules="filter_rules('结束单元格',{required:true})">
                    <el-input v-model="printForm.end" placeholder="结束单元格"></el-input>
                </el-form-item>
            </el-form>
            <el-alert
                title="说明：起始结束单元格为单元格的坐标，如起始单元格坐标为A1，结束单元格坐标为E20，则打印范围为A1:E20"
                type="warning" :closable="false">
            </el-alert>
            <template #footer>
            <span class="dialog-footer">
                  <el-button @click="closePrintDialog">取 消</el-button>
                  <el-button type="primary" @click="confirmPrint">确 定</el-button>
            </span>
            </template>
        </el-dialog>
        <el-dialog title="填写内容" v-model="editDialog"  :close-on-click-modal='false' @close='closeEditDialog'>
                <el-form :inline="true" :model="editForm" class="demo-form-inline" ref="editFormRef">
                    <el-form-item label="单元格内容"  prop="cellContent" v-if="cellConfig.valueType == '1' || cellConfig.valueType == '2'" :rules="filter_rules('单元格内容',rules)">
                      <el-input v-model="editForm.cellContent"  placeholder="请填写单元格内容"></el-input>
                    </el-form-item>
                    <el-form-item label="单元格内容"  prop="cellContent" v-if="cellConfig.valueType == '3'" :rules="filter_rules('单元格内容',rules)">
                      <el-date-picker v-model="editForm.cellContent" v-if="dateFormat == 'yyyy'" type="year" placeholder="选择日期" value-format="YYYY">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM'" type="month" placeholder="选择日期" value-format="YYYY-MM">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd'" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" placeholder="选择日期">
                      </el-date-picker>
                      <el-time-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'HH:mm:ss'" format="HH:mm:ss" value-format="HH:mm:ss" placeholder="选择时间">
                      </el-time-picker>
                      <el-time-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'HH:mm'" format="HH:mm" value-format="HH:mm" placeholder="选择时间">
                      </el-time-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd HH:mm:ss'" type="datetime" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd HH:mm'" type="datetime" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm" placeholder="选择日期">
                      </el-date-picker>
                    </el-form-item>
                    <el-form-item label="选择项" prop="cellContent" v-if="cellConfig.valueType == '4'" :rules="filter_rules('选择项',rules)">
                          <el-select placeholder="请选择" size="small" v-model="editForm.cellContent">
                            <el-option v-for="op in dictTypeDatas" :label="op.dictLabel" :value="op.dictLabel" :key="op.id"></el-option>
                          </el-select>
                      </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeEditDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <el-dialog title="上报历史" v-model="reportHisDialog"  :close-on-click-modal='false' @close='closeReportHisDialog'>
                <cusTable  ref="custable"  
                :isSelection='false'
                :isIndex='true'
                :isPagination='true'
                :isHandle='true'
                :tableCols='tableCols' 
                :tableHandles='tableHandles'
                :tableData='tableData'
                :tablePage='tablePage'
                @handleCurrentChange='searchtablelist()'
                ></cusTable>
                <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeReportHisDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button>
                </span>
                </template>
            </el-dialog>
            <div style="display:none">
              <input id="uploadPic" type="file" accept="image/*"  @change="uploadPic" />
            </div>
    </div>
  </el-scrollbar>
</template>
<script src="./luckyReportFromsPreview.js"></script>
<style scoped>
  .pagebox {
    height: 100%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    background: #ffffff;
  }

  .pagebox .toolbox {
    height: 10px;
    min-height: 10px;
    display: flex;
    background: white;
    /* box-shadow: 0 5px 10px #ebebeb; */
    line-height:60px;
  }

  .contentbox {
    flex: 1;
    display: flex;
  }

  .contentbox .mainbox{
    flex: 1;
    overflow-y:auto;
    overflow-x:auto;
  }
  .contentbox .rightbox{
    width: 250px;
    flex:none;
    padding-left:10px;
    background: white;
    position: relative;
    box-shadow: 0 2px 2px #ebebeb;
  }
  .contentbox .rightbox.collapse{
    width: 30px;
  }
  .clarrow{
    position: absolute;
    top:50%;
    left:3px;
    font-size: 20px;
    cursor:pointer;
  }
  :deep(.el-tabs .el-tabs__content){
    padding-right: 15px;
    padding-bottom:1px;
    height: 93%;
    overflow-y: auto;
    overflow-x:auto;
  }
  .el-scrollbar :deep(.el-scrollbar__view){
    height:100% !important;
  }
  
</style>
