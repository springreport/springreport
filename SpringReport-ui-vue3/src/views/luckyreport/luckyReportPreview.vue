<!--
 * @Description: luckysheet 报表设计
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-01-22 15:50:09
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-09 06:55:12
-->
<template>
  <el-scrollbar height="100vh" ref="scroll">
    <div class="pagebox">
      <div class="contentbox" v-loading="loading" :element-loading-text="loadingText">
        <div class="mainbox" style="height: 100%; display: flex; flex-direction: column">
          <!-- <div style="width: 100%; flex: none">
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
                  <el-button
                    v-if="!showSearch"
                    style="font-size: 14px"
                    type="primary"
                    icon="icon-down"
                    circle
                    @click="showSearchClick"
                    size="small"
                    title="展开搜索"
                  ></el-button>
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
                      size="small"
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
          </div> -->
          <div style="display: none">
          <input
            id="uploadAttachmentBtn"
            type="file"
            accept="*"
            @change="changeAttachment"
          >
        </div>
          <div style="width: 100%; flex: none; overflow: auto">
            <reportForm
              v-if="showReportForm && showSearch"
              ref="reportRef"
              :reportName="tplName"
              :reportForm="reportForm"
              :searchData="searchData"
              :searchHandle="searchHandle"
              :activitiName="activitiName"
              v-model:showSearch="showSearch"
              :isParamMerge="isParamMerge"
              :isDrill="isDrill"
              :users="users"
              :headerUsers="headerUsers"
              :drawer="drawer"
              @searchClick="searchClick"
              @closeSearch="closeSearch"
              @back="back"
            >
            </reportForm>
          </div>
          <div id="right" style="width: 100%; flex: 1; overflow: auto">
            <div class="config-panel" v-if="chartSettingShow">
              <div class="config-header">图表设置</div>
              <div class="config-box">
                <vchartsetting
                  :component="chartOptions"
                  :datasets="datasets"
                  :isPreview="true"
                ></vchartsetting>
              </div>
            </div>
            <div
              id="luckysheet"
              style="margin: 0px; padding: 0px; width: 100%; height: 100%; left: 0px; top: 50px"
            ></div>
            <div
              id="print_html"
              ref="print"
              style="text-align: center; page-break-after: always"
            ></div>
            <el-button
              ref="confirmPrintBtn"
              id="confirmPrintBtn"
              v-print="'#print_html'"
              v-show="false"
            ></el-button>
          </div>
        </div>
      </div>
    </div>
    <modal
      ref="modalRef"
      :modalConfig="modalConfig"
      :modalForm="modalForm"
      :modalData="modalData"
      :modalHandles="modalHandles"
      @closeModal="closeModal()"
    ></modal>
    <el-dialog
      title="指定分页"
      v-model="pageDialogVisiable"
      width="50%"
      :close-on-click-modal="false"
      @close="closePageDialog"
    >
      <el-form :inline="true" :model="pageForm" class="demo-form-inline" ref="pageFormRef">
        <el-form-item
          label="起始页"
          prop="startPage"
          :rules="filter_rules('起始页', { required: true })"
          v-if="sheetOptions.pager.pageSize"
        >
          <el-select placeholder="起始页" size="small" v-model="pageForm.startPage">
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize ? sheetOptions.pager.pageSize : 1)
              )"
              :label="i"
              :value="i"
              :key="i"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="结束页"
          prop="endPage"
          :rules="filter_rules('结束页', { required: true })"
          v-if="sheetOptions.pager.pageSize"
        >
          <el-select placeholder="结束页" size="small" v-model="pageForm.endPage">
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize ? sheetOptions.pager.pageSize : 1)
              )"
              :label="i"
              :value="i"
              :key="i"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closePageDialog" size="small">取 消</el-button>
          <el-button type="primary" @click="customPageExport" size="small">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="指定分页"
      v-model="pdfPageDialogVisiable"
      width="50%"
      :close-on-click-modal="false"
      @close="closePdfPageDialog"
    >
      <el-form :inline="true" :model="pdfPageForm" class="demo-form-inline" ref="pdfPageFormRef">
        <el-form-item
          label="起始页"
          prop="startPage"
          :rules="filter_rules('起始页', { required: true })"
          v-if="sheetOptions.pager.pageSize"
        >
          <el-select placeholder="起始页" size="small" v-model="pdfPageForm.startPage">
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize ? sheetOptions.pager.pageSize : 1)
              )"
              :label="i"
              :value="i"
              :key="i"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="结束页"
          prop="endPage"
          :rules="filter_rules('结束页', { required: true })"
          v-if="sheetOptions.pager.pageSize"
        >
          <el-select placeholder="结束页" size="small" v-model="pdfPageForm.endPage">
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize ? sheetOptions.pager.pageSize : 1)
              )"
              :label="i"
              :value="i"
              :key="i"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="PDF方向"  prop="type" :rules="filter_rules('PDF方向',{required:true})">
                    <el-select  placeholder="PDF方向" size="small" v-model="pdfPageForm.type">
                      <el-option label="纵向PDF" :value=1></el-option>
                      <el-option label="横向PDF" :value=2></el-option>
                    </el-select>
                </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closePdfPageDialog" size="small">取 消</el-button>
          <el-button type="primary" @click="customPdfPageExport" size="small">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="填写内容"
      v-model="editDialog"
      :close-on-click-modal="false"
      width="30%"
      @close="closeEditDialog"
    >
      <el-form :inline="true" :model="editForm" class="demo-form-inline" ref="editFormRef" label-position="top">
        <el-form-item
          label="单元格内容"
          prop="cellContent"
          v-if="cellConfig.valueType == '1' || cellConfig.valueType == '2'"
          :rules="filter_rules('单元格内容', rules)"
        >
          <el-input v-model="editForm.cellContent" placeholder="请填写单元格内容" style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item
          label="单元格内容"
          prop="cellContent"
          v-if="cellConfig.valueType == '3'"
          :rules="filter_rules('单元格内容', rules)"
        >
          <el-date-picker
            v-model="editForm.cellContent"
            v-if="dateFormat == 'yyyy'"
            type="year"
            placeholder="选择日期"
            value-format="YYYY"
            style="width: 400px"
          >
          </el-date-picker>
          <el-date-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'yyyy-MM'"
            type="month"
            placeholder="选择日期"
            value-format="YYYY-MM"
            style="width: 400px"
          >
          </el-date-picker>
          <el-date-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'yyyy-MM-dd'"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            style="width: 400px"
          >
          </el-date-picker>
          <el-time-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'HH:mm:ss'"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            placeholder="选择时间"
            style="width: 400px"
          >
          </el-time-picker>
          <el-time-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'HH:mm'"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 400px"
          >
          </el-time-picker>
          <el-date-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'yyyy-MM-dd HH:mm:ss'"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择日期"
            style="width: 400px"
          >
          </el-date-picker>
          <el-date-picker
            v-model="editForm.cellContent"
            v-else-if="dateFormat == 'yyyy-MM-dd HH:mm'"
            type="datetime"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            placeholder="选择日期"
            style="width: 400px"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="选择项"
          prop="cellContent"
          v-if="cellConfig.valueType == '4'"
          :rules="filter_rules('选择项', rules)"
          
        >
          <el-select placeholder="请选择" size="default" v-model="editForm.cellContent" style="width: 400px" filterable>
            <el-option
              v-for="op in dictTypeDatas"
              :label="op.dictLabel"
              :value="op.dictLabel"
              :key="op.id"
            ></el-option>
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
    <el-dialog
      title="上报历史"
      v-model="reportHisDialog"
      :close-on-click-modal="false"
      @close="closeReportHisDialog"
    >
      <cusTable
        ref="custable"
        :isSelection="false"
        :isIndex="true"
        :isPagination="true"
        :isHandle="true"
        :tableCols="tableCols"
        :tableHandles="tableHandles"
        :tableData="tableData"
        :tablePage="tablePage"
        @handleCurrentChange="searchtablelist()"
      ></cusTable>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeReportHisDialog" size="small">取 消</el-button>
          <!-- <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button> -->
        </span>
      </template>
    </el-dialog>
    <div
      ref="dragArea"
      class="drag-area"
      v-if="showReportSql && reportSqls && reportSqls.length > 0"
    >
      <el-tooltip effect="dark" content="显示sql语句" placement="top">
        <el-button type="primary" size="small" circle @click="showSql">sql</el-button>
      </el-tooltip>
    </div>
    <el-dialog
      :modal="false"
      :close-on-click-modal="false"
      title="本次查询sql"
      v-model="reportDialogVisiable"
      width="300px"
      modal-class="hisDialog"
    >
      <div class="el-dialog-div">
        <div v-for="(item, index) in reportSqls" :key="index">
          <el-divider content-position="left">数据集：{{ item.name }}</el-divider>
          <el-descriptions title="" :column="1" border>
            <!-- <el-descriptions-item label="数据集">{{item.name}}</el-descriptions-item> -->
            <el-descriptions-item label="sql语句">{{ item.sql }}</el-descriptions-item>
          </el-descriptions>
          <br />
        </div>
      </div>
    </el-dialog>
    <div style="display: none">
      <input id="uploadPic" type="file" accept="image/*" @change="uploadPic" />
    </div>
    <div style="display: none">
      <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel" />
    </div>
  </el-scrollbar>
</template>
<script src="./luckyReportPreview.js"></script>
<style scoped lang="scss">
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
    line-height: 60px;
  }

  .contentbox {
    flex: 1;
    display: flex;
  }

  .contentbox .mainbox {
    flex: 1;
    overflow-y: auto;
    overflow-x: auto;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .contentbox .rightbox {
    width: 250px;
    flex: none;
    padding-left: 10px;
    background: white;
    position: relative;
    box-shadow: 0 2px 2px #ebebeb;
  }
  .contentbox .rightbox.collapse {
    width: 30px;
  }
  .clarrow {
    position: absolute;
    top: 50%;
    left: 3px;
    font-size: 20px;
    cursor: pointer;
  }
  .el-tabs :deep(.el-tabs__content) {
    padding-right: 15px;
    padding-bottom: 1px;
    height: 93%;
    overflow-y: auto;
    overflow-x: auto;
  }
  .el-scrollbar :deep(.el-scrollbar__view) {
    height: 100% !important;
  }
  /*去除页眉页脚*/
  @page {
    size: auto; /* auto is the initial value */
    margin: 3mm; /* this affects the margin in the printer settings */
  }

  html {
    background-color: #ffffff;
    margin: 0; /* this affects the margin on the html before sending to printer */
  }

  body {
    border: solid 1px blue;
    margin: 10mm 15mm 10mm 15mm; /* margin you want for the content */
  }
  /*去除页眉页脚*/
  #print_html {
    text-align: 'center';
  }
  #print_html :deep(table) {
    margin: 0 auto;
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
  :deep(.hisDialog) {
    pointer-events: none !important;
  }
  :deep(.el-overlay-dialog) {
    flex-direction: column !important;
    overflow: hidden !important;
    pointer-events: none !important;
  }
  :deep(.hisDialog .el-dialog) {
    margin-top: 90px !important;
    margin-right: 14px;
  }
  :deep(.el-dialog) {
    pointer-events: auto !important;
  }
  :deep(.el-dialog__body) {
    height: calc(100% - 90px) !important;
    overflow: auto;
  }
  :deep(.el-dialog-div) {
    max-height: 60vh;
    overflow: auto;
    margin-left: 10px;
  }
  :deep(.el-dialog__title) {
    font-weight: bold;
  }
  .config-panel {
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
    .config-header {
      width: 100%;
      height: 32px;
      // background: #2F343D;
      font-size: 13px;
      font-weight: 400;
      color: #000000;
      line-height: 32px;
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
</style>
