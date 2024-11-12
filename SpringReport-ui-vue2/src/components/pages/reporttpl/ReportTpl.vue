<!--
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-09-18 08:49:59
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-07-05 18:51:03
-->
<template>
  <div class="container">
    <!-- <div class="left">
      <el-tree
        :data="pageData.treeData"
        :props="pageData.defaultProps"
        @node-click="handleNodeClick"
        default-expand-all
      ></el-tree>
    </div> -->
    <div class="_tablepage">
      <!-- <searchForm
        ref="searchRef"
        :searchForm="pageData.searchForm"
        :searchData="pageData.queryData"
        :searchHandle="pageData.searchHandle"
      ></searchForm> -->
      <cusTable
        ref="custable"
        :isSelection="false"
        :isIndex="false"
        :isPagination="false"
        :isHandle="true"
        :tableCols="pageData.tableCols"
        :tableHandles="pageData.tableHandles"
        :tableData="pageData.tableData"
        :tablePage="pageData.tablePage"
        :lazy='pageData.lazy'
        @handleCurrentChange="searchtablelist()"
        @selectChange="selectChange"
        @load="loadData"
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
      <modal ref="folderModalRef" :modalConfig='pageData.folderModalConfig' 
       :modalForm='pageData.folderModalForm' :modalData='pageData.folderModalData' 
       :modalHandles='pageData.folderModalHandles'
       @closeModal="closeFolderModal()"></modal>
      <textarea id="clipboradInput" style="opacity:0;position:absolute" />
    </div>
  </div>
</template>
<script src="./ReportTpl.js"></script>
<style scoped>
.container {
  display: flex;
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
  flex: 1;
}
</style>
