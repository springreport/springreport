<!--
 * @Description: luckysheetreport预览页面
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:21
 * @LastEditors: caiyang
 * @LastEditTime: 2022-04-01 11:18:22
-->
<template>
    <div class="_tablepage" style="height: 100%;display: flex;flex-direction: column;" v-loading="loading" :element-loading-text="loadingText">
       <div style="width: 100%;flex: none;">
            <el-header class="_header df-c-b">
                <div class="headerLeft df-c" style="width:30%">
                <div class="tplname" style="width: 100%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" :title="tplName">
                        {{tplName}} <el-button v-if="!showSearch"
                        type="primary"
                        icon="el-icon-arrow-down"
                        circle
                        @click="showSearchClick"
                        size="mini"
                        title="展开搜索"
                      ></el-button>
                    </div>
                </div>
                <div class="headerRight df-c">
                <el-dropdown class="white font" trigger="click" placement="bottom" v-if="users.length > 0">
                    <span class="el-dropdown-link df-c">
                    <el-avatar size="small" :style="{marginRight:'4px',backgroundColor:item.color+' !important'}" shape="circle" :title="item.userName" v-for="(item,index) in headerUsers" :key="index"> {{(item.userName.slice(0,1)).toUpperCase()}} </el-avatar>
                    <i class="el-icon-arrow-down el-icon--right" ></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item  v-for="(item,index) in users" :key="index">{{item.userName}}</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                </div>
            </el-header>
        </div>
        <div style="width: 100%;flex: none;">
        <reportForm v-if="showReportForm && showSearch" ref="reportRef" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" :showSearch.sync="showSearch" :isParamMerge="isParamMerge" :isDrill="isDrill">
        </reportForm>
        </div>
        <div style="width: 100%;flex: 1;overflow: auto;">
          <div  class="config-panel" v-if="chartSettingShow">
                <div class="config-header">图表设置</div>
                <div class="config-box">
                    <vchartsetting :component="chartOptions" :datasets="datasets" :isPreview="true"></vchartsetting>
                </div>
            </div>
            <div id="luckysheet" style="margin:0px;padding:0px;width:100%;height:100%;left: 0px;top: 50px"></div>
            <!-- <div id="print_html" ref="print" style="text-align: center;page-break-after:always"></div>
            <el-button ref="confirmPrintBtn" v-print="'#print_html'" v-show="false"></el-button> -->
        </div>
        
        <modal ref="modalRef" :modalConfig='modalConfig' 
       :modalForm='modalForm' :modalData='modalData' 
       :modalHandles='modalHandles'
       @closeModal="closeModal()"></modal>
       <el-dialog title="指定分页" :visible.sync="pageDialogVisiable" width="50%" height="80%" :close-on-click-modal='false' @close='closePageDialog'>
            <el-form :inline="true" :model="pageForm" class="demo-form-inline" ref="pageFormRef">
                <el-form-item label="起始页"  prop="startPage" :rules="filter_rules('起始页',{required:true})" v-if="sheetOptions.pager.pageSize">
                    <el-select  placeholder="起始页" size="small" v-model="pageForm.startPage">
                        <el-option v-for="i in (Math.ceil(sheetOptions.pager.total/(sheetOptions.pager.pageSize?sheetOptions.pager.pageSize:1)))" :label="i" :value="i" :key="i"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="结束页"  prop="endPage" :rules="filter_rules('结束页',{required:true})" v-if="sheetOptions.pager.pageSize">
                    <el-select  placeholder="结束页" size="small" v-model="pageForm.endPage">
                        <el-option v-for="i in (Math.ceil(sheetOptions.pager.total/(sheetOptions.pager.pageSize?sheetOptions.pager.pageSize:1)))" :label="i" :value="i" :key="i"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="closePageDialog" size="small">取 消</el-button>
                <el-button type="primary" @click="customPageExport" size="small">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="指定分页" :visible.sync="pdfPageDialogVisiable" width="50%" height="80%" :close-on-click-modal='false' @close='closePdfPageDialog'>
            <el-form :inline="true" :model="pdfPageForm" class="demo-form-inline" ref="pdfPageFormRef">
                <el-form-item label="起始页"  prop="startPage" :rules="filter_rules('起始页',{required:true})" v-if="sheetOptions.pager.pageSize">
                    <el-select  placeholder="起始页" size="small" v-model="pdfPageForm.startPage">
                        <el-option v-for="i in (Math.ceil(sheetOptions.pager.total/(sheetOptions.pager.pageSize?sheetOptions.pager.pageSize:1)))" :label="i" :value="i" :key="i"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="结束页"  prop="endPage" :rules="filter_rules('结束页',{required:true})" v-if="sheetOptions.pager.pageSize">
                    <el-select  placeholder="结束页" size="small" v-model="pdfPageForm.endPage">
                        <el-option v-for="i in (Math.ceil(sheetOptions.pager.total/(sheetOptions.pager.pageSize?sheetOptions.pager.pageSize:1)))" :label="i" :value="i" :key="i"></el-option>
                    </el-select>
                </el-form-item>
                <!-- <el-form-item label="方向"  prop="type" :rules="filter_rules('方向',{required:true})">
                    <el-select  placeholder="方向" size="small" v-model="pdfPageForm.type">
                      <el-option label="纵向" :value=1></el-option>
                      <el-option label="横向" :value=2></el-option>
                    </el-select>
                </el-form-item> -->
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="closePdfPageDialog" size="small">取 消</el-button>
                <el-button type="primary" @click="customPdfPageExport" size="small">确 定</el-button>
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
        <el-dialog title="上报历史" :visible.sync="reportHisDialog" top="20px" :close-on-click-modal='false' @close='closeReportHisDialog'>
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
                    <!-- <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button> -->
                </span>
            </el-dialog>
            <div style="display:none">
              <input id="uploadPic" type="file" accept="image/*"  @change="uploadPic" />
            </div>
            <div ref="dragArea" class="drag-area" v-if="showReportSql && reportSqls && reportSqls.length > 0">
              <el-tooltip effect="dark" content="显示sql语句" placement="top">
                <el-button type="primary" size="mini" circle @click="showSql">sql</el-button>
              </el-tooltip>
            </div>
            <el-dialog
              :modal="false"
              :close-on-click-modal='false'
              title="本次查询sql"
              :visible.sync="reportDialogVisiable"
              width="300px"
              custom-class="authdialog"
              >
              <div class="el-dialog-div">
                  <div v-for="(item,index) in reportSqls" :key="index">
                  <el-divider content-position="left">数据集：{{item.name}}</el-divider>
                  <el-descriptions title="" :column="1" border>
                      <!-- <el-descriptions-item label="数据集">{{item.name}}</el-descriptions-item> -->
                      <el-descriptions-item label="sql语句">{{item.sql}}</el-descriptions-item>
                  </el-descriptions>
                  <br>
                  </div>
              </div>
          </el-dialog>
          <div style="display:none">
            <input id="uploadBtn" type="file" accept="xlsx/*"  @change="loadExcel" />
          </div>
    </div>
</template>
<script src="./luckyReportPreview.js"></script>
<style scoped lang="scss">
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
.drag-area {
  position: fixed;
  right: 5px;
  bottom: 100px;
  z-index: 99999;
  padding: 5px;
  width: fit-content;
  opacity: 1;
  background-color: #17b794;
  border-radius: 8px;
  box-shadow: 0px 2px 15px 0px rgba(9, 41, 77, 0.15);
  // cursor: move;
  user-select: none;
  text-align: center;
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
    margin-top: 100px !important;
    margin-left: 0px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    top:0 !important;
    left:calc(100% - 314px)!important;
    bottom: 0;
    pointer-events: auto !important;
    overflow: auto;
    /* background:#d9ebf0 !important; */
} 
::v-deep .authdialog::-webkit-scrollbar {
    display: none;
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
.config-panel{
      background: #ffffff;
      // margin-left: 1px;
      top: 10px;
      position: relative;
      width: 254px;
      height: 95%;
      display: flex;
      flex-direction: column;
      overflow: auto;
      z-index: 1999;
      float: right;
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
