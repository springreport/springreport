<template>
  <div class="container" v-loading="loading">
    <div class="left">
      <el-tree
        :data="pageData.treeData"
        :props="pageData.defaultProps"
        @node-click="handleNodeClick"
        default-expand-all
      >
      <template #default="{ node, data }">
        <div class="set-group df-c" @click="groupSetVisible = true" style="fontSize:14px">
                <img
                  v-if="data.id != '1'"
                  src="@/assets/img/sheet/del.png"
                  width="14px"
                  height="14px"
                  @click="() => removeNode(node, data)"
                >
                <div class="setting-text">{{ node.label }}</div>
              </div>
      </template>
      </el-tree>
    </div>
    <div class="_tablepage">
     <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
      <cusTable
        :isSelection="true"
        :isIndex="false"
        :isPagination="true"
        :isHandle="true"
        :tableCols="pageData.tableCols"
        :tableData="pageData.tableData"
        :tablePage="pageData.tablePage"
        :lazy="pageData.lazy"
        @handleCurrentChange="searchtablelist()"
        @selectChange="selectChange"
      ></cusTable>
      <modal
        ref="modalRef"
        :modalConfig="pageData.modalConfig"
        :modalForm="pageData.modalForm"
        :modalData="pageData.modalData"
        :modalHandles="pageData.modalHandles"
        @closeModal="closeModal()"
      ></modal>
      <modal
        ref="copyModalRef"
        :modalConfig="pageData.copyModalConfig"
        :modalForm="pageData.copyModalForm"
        :modalData="pageData.copyModalData"
        :modalHandles="pageData.copyModalHandles"
        @closeModal="closeCopyModal()"
      ></modal>
      <modal
        ref="changePwd"
        :modalConfig="pageData.changePwdConfig"
        :modalForm="pageData.changePwdForm"
        :modalData="pageData.changePwdModalData"
        :modalHandles="pageData.changePwdModalHandles"
        @closeModal="closePwdModal()"
      ></modal>
      <modal
        ref="shareReport"
        :modalConfig="pageData.shareReportConfig"
        :modalForm="pageData.shareReportForm"
        :modalData="pageData.shareReportModalData"
        :modalHandles="pageData.shareReportModalHandles"
        @closeModal="closeShareReportModal()"
      ></modal>
      <modal
        ref="folderModalRef"
        :modalConfig="pageData.folderModalConfig"
        :modalForm="pageData.folderModalForm"
        :modalData="pageData.folderModalData"
        :modalHandles="pageData.folderModalHandles"
        @closeModal="closeFolderModal()"
      ></modal>
      <textarea id="clipboradInput" style="opacity: 0; position: absolute" />
    </div>
    <div style="display: none">
        <input id="uploadPic" type="file" accept=".sr" @change="uploadPic">
      </div>
  </div>
</template>
<script src="./ReportTpl.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
    width: calc(100% - 200px);
  }
  .container {
    display: flex;
    overflow: auto;
  }
  .left {
    box-sizing: border-box;
    width: 232px;
    flex-shrink: 0;
    background: #ffffff;
    border-radius: 6px;
    margin-right: 16px;
    padding: 6px;
  }
  ._tablepage {
    width: 100%;
  }
</style>
