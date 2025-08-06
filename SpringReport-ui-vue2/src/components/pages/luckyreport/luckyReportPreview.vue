<!--
 * @Description: luckysheetreport预览页面
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:21
 * @LastEditors: caiyang
 * @LastEditTime: 2022-04-01 11:18:22
-->
<template>
  <div
    v-loading="loading"
    class="_tablepage"
    style="height: 100%; display: flex; flex-direction: column"
    :element-loading-text="loadingText"
  >
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
    </div> -->
    <div style="display: none">
          <input
            id="uploadAttachmentBtn"
            type="file"
            accept="*"
            @change="changeAttachment"
          >
        </div>
    <div style="width: 100%; flex: none">
      <reportForm
        v-if="showReportForm && showSearch"
        ref="reportRef"
        :report-name="tplName"
        :report-form="reportForm"
        :search-data="searchData"
        :search-handle="searchHandle"
        :activiti-name="activitiName"
        :show-search.sync="showSearch"
        :is-param-merge="isParamMerge"
        :is-drill="isDrill"
        :users="users"
        :headerUsers="headerUsers"
        :drawer="drawer"
      />
    </div>
    <div style="width: 100%; flex: 1; overflow: auto">
      <div v-if="chartSettingShow" class="config-panel">
        <div class="config-header">图表设置</div>
        <div class="config-box">
          <vchartsetting
            :component="chartOptions"
            :datasets="datasets"
            :is-preview="true"
          />
        </div>
      </div>
      <div
        id="luckysheet"
        style="
          margin: 0px;
          padding: 0px;
          width: 100%;
          height: 100%;
          left: 0px;
          top: 50px;
        "
      />
      <!-- <div id="print_html" ref="print" style="text-align: center;page-break-after:always"></div>
            <el-button ref="confirmPrintBtn" v-print="'#print_html'" v-show="false"></el-button> -->
    </div>

    <modal
      ref="modalRef"
      :modal-config="modalConfig"
      :modal-form="modalForm"
      :modal-data="modalData"
      :modal-handles="modalHandles"
      @closeModal="closeModal()"
    />
    <el-dialog
      title="指定分页"
      :visible.sync="pageDialogVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closePageDialog"
    >
      <el-form
        ref="pageFormRef"
        label-position="top"
        :model="pageForm"
        class="demo-form-inline"
      >
        <el-form-item
          v-if="sheetOptions.pager.pageSize"
          label="起始页"
          prop="startPage"
          :rules="filter_rules('起始页', { required: true })"
        >
          <el-select
            v-model="pageForm.startPage"
            placeholder="起始页"
            size="small"
            style="width: 100%;"
          >
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize
                    ? sheetOptions.pager.pageSize
                    : 1)
              )"
              :key="i"
              :label="i"
              :value="i"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="sheetOptions.pager.pageSize"
          label="结束页"
          prop="endPage"
          :rules="filter_rules('结束页', { required: true })"
        >
          <el-select
            v-model="pageForm.endPage"
            placeholder="结束页"
            size="small"
            style="width: 100%;"
          >
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize
                    ? sheetOptions.pager.pageSize
                    : 1)
              )"
              :key="i"
              :label="i"
              :value="i"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closePageDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="customPageExport"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="指定分页"
      :visible.sync="pdfPageDialogVisiable"
      width="30%"
      height="80%"
      :close-on-click-modal="false"
      @close="closePdfPageDialog"
    >
      <el-form
        ref="pdfPageFormRef"
        label-position="top"
        :model="pdfPageForm"
        class="demo-form-inline"
      >
        <el-form-item
          v-if="sheetOptions.pager.pageSize"
          label="起始页"
          prop="startPage"
          :rules="filter_rules('起始页', { required: true })"
        >
          <el-select
            v-model="pdfPageForm.startPage"
            placeholder="起始页"
            size="small"
            style="width: 100%;"
          >
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize
                    ? sheetOptions.pager.pageSize
                    : 1)
              )"
              :key="i"
              :label="i"
              :value="i"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="sheetOptions.pager.pageSize"
          label="结束页"
          prop="endPage"
          :rules="filter_rules('结束页', { required: true })"
        >
          <el-select
            v-model="pdfPageForm.endPage"
            placeholder="结束页"
            size="small"
            style="width: 100%;"
          >
            <el-option
              v-for="i in Math.ceil(
                sheetOptions.pager.total /
                  (sheetOptions.pager.pageSize
                    ? sheetOptions.pager.pageSize
                    : 1)
              )"
              :key="i"
              :label="i"
              :value="i"
            />
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
        <el-button size="small" @click="closePdfPageDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="customPdfPageExport"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="填写内容"
      :visible.sync="editDialog"
      :close-on-click-modal="false"
      @close="closeEditDialog"
    >
      <el-form
        ref="editFormRef"
        :inline="true"
        :model="editForm"
        class="demo-form-inline"
      >
        <el-form-item
          v-if="cellConfig.valueType == '1' || cellConfig.valueType == '2'"
          label="单元格内容"
          prop="cellContent"
          :rules="filter_rules('单元格内容', rules)"
        >
          <el-input
            v-model="editForm.cellContent"
            placeholder="请填写单元格内容"
          />
        </el-form-item>
        <el-form-item
          v-if="cellConfig.valueType == '3'"
          label="单元格内容"
          prop="cellContent"
          :rules="filter_rules('单元格内容', rules)"
        >
          <el-date-picker
            v-if="dateFormat == 'yyyy'"
            v-model="editForm.cellContent"
            type="year"
            format="yyyy"
            value-format="yyyy"
            placeholder="选择日期"
          />
          <el-date-picker
            v-else-if="dateFormat == 'yyyy-MM'"
            v-model="editForm.cellContent"
            type="month"
            format="yyyy-MM"
            value-format="yyyy-MM"
            placeholder="选择日期"
          />
          <el-date-picker
            v-else-if="dateFormat == 'yyyy-MM-dd'"
            v-model="editForm.cellContent"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            placeholder="选择日期"
          />
          <el-time-picker
            v-else-if="dateFormat == 'HH:mm:ss'"
            v-model="editForm.cellContent"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            placeholder="选择时间"
          />
          <el-time-picker
            v-else-if="dateFormat == 'HH:mm'"
            v-model="editForm.cellContent"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
          />
          <el-date-picker
            v-else-if="dateFormat == 'yyyy-MM-dd HH:mm:ss'"
            v-model="editForm.cellContent"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期"
          />
          <el-date-picker
            v-else-if="dateFormat == 'yyyy-MM-dd HH:mm'"
            v-model="editForm.cellContent"
            type="datetime"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item
          v-if="cellConfig.valueType == '4'"
          label="选择项"
          prop="cellContent"
          :rules="filter_rules('选择项', rules)"
        >
          <el-select
            v-model="editForm.cellContent"
            placeholder="请选择"
            size="small"
            filterable
          >
            <el-option
              v-for="op in dictTypeDatas"
              :key="op.id"
              :label="op.dictLabel"
              :value="op.dictLabel"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeEditDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmEdit"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="上报历史"
      :visible.sync="reportHisDialog"
      top="20px"
      :close-on-click-modal="false"
      @close="closeReportHisDialog"
    >
      <cusTable
        ref="custable"
        :is-selection="false"
        :is-index="true"
        :is-pagination="true"
        :is-handle="true"
        :table-cols="tableCols"
        :table-handles="tableHandles"
        :table-data="tableData"
        :table-page="tablePage"
        @handleCurrentChange="searchtablelist()"
      />
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeReportHisDialog">取 消</el-button>
        <!-- <el-button type="primary" @click="confirmEdit" size="small">确 定</el-button> -->
      </span>
    </el-dialog>
    <div style="display: none">
      <input id="uploadPic" type="file" accept="image/*" @change="uploadPic">
    </div>
    <div
      v-if="showReportSql && reportSqls && reportSqls.length > 0"
      ref="dragArea"
      class="drag-area"
    >
      <el-tooltip effect="dark" content="显示sql语句" placement="top">
        <el-button
          type="primary"
          size="mini"
          circle
          @click="showSql"
        >sql</el-button>
      </el-tooltip>
    </div>
    <el-dialog
      :modal="false"
      :close-on-click-modal="false"
      title="本次查询sql"
      :visible.sync="reportDialogVisiable"
      width="300px"
      custom-class="authdialog"
    >
      <div class="el-dialog-div">
        <div v-for="(item, index) in reportSqls" :key="index">
          <el-divider
            content-position="left"
          >数据集：{{ item.name }}</el-divider>
          <el-descriptions title="" :column="1" border>
            <!-- <el-descriptions-item label="数据集">{{item.name}}</el-descriptions-item> -->
            <el-descriptions-item label="sql语句">{{
              item.sql
            }}</el-descriptions-item>
          </el-descriptions>
          <br>
        </div>
      </div>
    </el-dialog>
    <div style="display: none">
      <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel">
    </div>
  </div>
</template>
<script src="./luckyReportPreview.js"></script>
<style scoped lang="scss">
@import "@/element-variables.scss";

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
  text-align: "center";
}
#print_html >>> table {
  margin: 0 auto;
}
::v-deep .el-form-item__label-wrap {
  margin-left: 0px !important;
}
.tplname {
  padding: 0px 16px;
  font-size: 16px;
  line-height: 30px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: bold;
  margin: 5px 0;
}
._header {
  height: 64px !important;
  padding: 0px;
  background-color: #fff;
  box-shadow: 0px -1px 0px 0px #F0F0F0 inset;
  position: relative;
  z-index: 10;
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
  z-index: 2000;
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

::v-deep .el-dialog {
  pointer-events: auto !important;
  /* background:#d9ebf0 !important; */
}
::v-deep .authdialog {
  margin-top: 100px !important;
  margin-left: 0px !important;
  flex-direction: column !important;
  // overflow: hidden !important;
  max-height: calc(100% - 90px) !important;
  top: 0 !important;
  left: calc(100% - 314px) !important;
  bottom: 0;
  pointer-events: auto !important;
  overflow: auto;
  /* background:#d9ebf0 !important; */
}
::v-deep .authdialog::-webkit-scrollbar {
  display: none;
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
.config-panel {
  background: #ffffff;
  // margin-left: 1px;
  top: 10px;
  position: relative;
  width: 306px;
  height: calc(100vh - 64px - 10px - 10px);
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
