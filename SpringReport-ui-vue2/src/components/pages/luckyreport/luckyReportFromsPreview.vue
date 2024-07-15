<!--
 * @Description: luckysheetreport预览页面
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:21
 * @LastEditors: caiyang
 * @LastEditTime: 2022-04-01 11:18:22
-->
<template>
    <div class="_tablepage" style="height: 100%;display: flex;flex-direction: column;" v-loading="loading">
        <div style="width: 100%;flex: none;">
        <reportForm v-if="showReportForm" ref="reportRef" :formsReport="true" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" :showSearch="showSearch" :isParamMerge="isParamMerge">
        </reportForm>
        </div>
        <div style="width: 100%;flex: 1;overflow: auto;">
            <!-- <div class="tabledata" style="margin:auto;margin-bottom:20px" v-html="table"></div> -->
            <div id="luckysheet" style="margin:0px;padding:0px;width:100%;height:100%;left: 0px;top: 50px;"></div>
            <div id="print_html" ref="print" style="text-align: center;page-break-after:always"></div>
            <el-button ref="confirmPrintBtn" v-print="'#print_html'" v-show="false"></el-button>
        </div>
        <el-dialog title="打印" :visible.sync="printDialogVisiable" width="50%" height="80%" :close-on-click-modal='false' @close='closePrintDialog'>
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
            <span slot="footer" class="dialog-footer">
                  <el-button @click="closePrintDialog">取 消</el-button>
                  <el-button type="primary" @click="confirmPrint">确 定</el-button>
                  </span>
        </el-dialog>
        <el-dialog title="填写内容" :visible.sync="editDialog"  :close-on-click-modal='false' @close='closeEditDialog'>
                <el-form :inline="true" :model="editForm" class="demo-form-inline" ref="editFormRef">
                    <el-form-item label="单元格内容"  prop="cellContent" v-if="cellConfig.valueType == '1' || cellConfig.valueType == '2'" :rules="filter_rules('单元格内容',rules)">
                      <el-input v-model="editForm.cellContent"  placeholder="请填写单元格内容"></el-input>
                    </el-form-item>
                    <el-form-item label="单元格内容"  prop="cellContent" v-if="cellConfig.valueType == '3'" :rules="filter_rules('单元格内容',rules)">
                      <el-date-picker v-model="editForm.cellContent" v-if="dateFormat == 'yyyy'" type="year" format="yyyy" value-format="yyyy" placeholder="选择日期">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM'" type="month" format="yyyy-MM" value-format="yyyy-MM" placeholder="选择日期">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd'" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择日期">
                      </el-date-picker>
                      <el-time-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'HH:mm:ss'" format="HH:mm:ss" value-format="HH:mm:ss" placeholder="选择时间">
                      </el-time-picker>
                      <el-time-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'HH:mm'" format="HH:mm" value-format="HH:mm" placeholder="选择时间">
                      </el-time-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd HH:mm:ss'" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期">
                      </el-date-picker>
                      <el-date-picker v-model="editForm.cellContent" v-else-if="dateFormat == 'yyyy-MM-dd HH:mm'" type="datetime" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" placeholder="选择日期">
                      </el-date-picker>
                    </el-form-item>
                    <el-form-item label="选择项" prop="cellContent" v-if="cellConfig.valueType == '4'" :rules="filter_rules('选择项',rules)">
                          <el-select placeholder="请选择" size="small" v-model="editForm.cellContent">
                            <el-option v-for="op in dictTypeDatas" :label="op.dictLabel" :value="op.dictLabel" :key="op.id"></el-option>
                          </el-select>
                      </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="closeEditDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button>
                </span>
            </el-dialog>
            <el-dialog title="上报历史" :visible.sync="reportHisDialog"  :close-on-click-modal='false' @close='closeReportHisDialog'>
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
                <span slot="footer" class="dialog-footer">
                    <el-button @click="closeReportHisDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button>
                </span>
            </el-dialog>
            <div style="display:none">
              <input id="uploadPic" type="file" accept="image/*"  @change="uploadPic" />
            </div>
    </div>
</template>
<script src="./luckyReportFromsPreview.js"></script>
<style scoped>
/*去除页眉页脚*/
  @page{
    size:  auto;   /* auto is the initial value */
    margin: 3mm;  /* this affects the margin in the printer settings */
  }
 
  html{
    background-color: #FFFFFF;
    margin: 0;  /* this affects the margin on the html before sending to printer */
  }
 
  body{
    border: solid 1px blue ;
    margin: 10mm 15mm 10mm 15mm; /* margin you want for the content */
  }
  /*去除页眉页脚*/
  #print_html{
    text-align: 'center';
  }
  #print_html >>> table{
    margin: 0 auto
  }
   ::v-deep .el-form-item__label-wrap{
    margin-left:0px !important
  }
</style>
