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
        <div style="width: 100%;flex: none;"  v-if="searchFormType == '1'">
        <headerReportForm ref="reportRef" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" :showSearch.sync="showSearch" :isParamMerge="isParamMerge" :isDrill="2" :drawer="drawer">
        </headerReportForm>
        </div>
        <div style="width: 100%;flex: none;" v-if="searchFormType != '1'">
        <reportForm v-if="showReportForm && showSearch" ref="reportRef" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" :showSearch.sync="showSearch" :isParamMerge="isParamMerge" :isDrill="2" :drawer="drawer">
        </reportForm>
        </div>
        <div style="width: 100%;flex: 1;overflow: hidden;">
          <iframe id="pdfIframe" width="100%" height="100%" style="border: none;"></iframe>
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
    </div>
</template>
<script src="./docPreview.js"></script>
<style scoped lang="scss">
@import "@/element-variables.scss";

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
    color: #17b794;
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
  background-color: $--color-primary;
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
</style>
