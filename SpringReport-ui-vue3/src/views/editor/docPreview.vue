<!--
 * @Description: luckysheetreport预览页面
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:21
 * @LastEditors: caiyang
 * @LastEditTime: 2022-04-01 11:18:22
-->
<template>
    <div class="_tablepage" style="height: 100vh;display: flex;flex-direction: column;" v-loading="loading" :element-loading-text="loadingText">
       <!-- <div style="width: 100%;flex: none;">
            <el-header class="_header df-c-b">
                <div class="headerLeft df-c" style="width:30%">
                <div class="tplname" style="width: 100%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" :title="tplName">
                        {{tplName}} 
                        <el-button v-if="!showSearch"
                          style="font-size:14px"
                          type="primary"
                          icon="icon-down"
                          circle
                          @click="showSearchClick"
                          size="small"
                          title="展开搜索"
                        ></el-button>
                    </div>
                </div>
            </el-header>
        </div> -->
        <div style="width: 100%;flex: none;">
        <reportForm v-if="showReportForm && showSearch" ref="reportRef" :reportName="tplName" :reportForm="reportForm" :searchData="searchData" :searchHandle="searchHandle" :activitiName="activitiName" 
        v-model:showSearch="showSearch" :isParamMerge="isParamMerge" :isDrill="2" :drawer="drawer" @searchClick="searchClick" @closeSearch="closeSearch">
        </reportForm>
        </div>
        <div style="width: 100%;height:100%;flex: 1;overflow: auto;">
           <iframe id="pdfIframe" width="100%" height="100%" style="border: none;"></iframe>
        </div>
            <div ref="dragArea" class="drag-area" v-if="showReportSql && reportSqls && reportSqls.length > 0">
              <el-tooltip effect="dark" content="显示sql语句" placement="top">
                <el-button type="primary" size="default" circle @click="showSql">sql</el-button>
              </el-tooltip>
            </div>
            <el-dialog
              :modal="false"
              :close-on-click-modal='false'
              title="本次查询sql"
              v-model="reportDialogVisiable"
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
  :deep(.el-form-item__label-wrap){
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
  background-color: #17b794;
  border-radius: 8px;
  box-shadow: 0px 2px 15px 0px rgba(9, 41, 77, 0.15);
  // cursor: move;
  user-select: none;
  text-align: center;
}
:deep(.el-dialog__wrapper) {
   overflow: hidden;
//    z-index: 2005 !important;
   pointer-events: none !important;
}

:deep(.el-dialog){
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
    margin-right:14px;
} 
:deep(.authdialog){
    margin-top: 100px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    pointer-events: auto !important;
    overflow: auto;
    
    /* background:#d9ebf0 !important; */
} 
:deep(.authdialog::-webkit-scrollbar) {
    display: none;
}
.authdialog :deep(.el-dialog__body){
    height: calc(100% - 90px) !important;
    overflow: auto;
}
.authdialog :deep(.el-dialog-div){
     max-height: 60vh;
     overflow: auto;
     margin-left: 10px;
}
.authdialog :deep(.el-dialog-div::-webkit-scrollbar) {
    display: none; /*隐藏滚动条*/
}
.authdialog :deep(.el-dialog__title){
    font-weight: bold;
}
</style>
