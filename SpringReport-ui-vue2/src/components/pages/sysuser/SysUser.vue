<template>
  <div class="container">
    <div class="left">
      <el-tree
        :data="pageData.treeData"
        :props="pageData.defaultProps"
        default-expand-all
        @node-click="handleNodeClick"
      />
    </div>
    <div class="_tablepage">
      <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
      <cusTable
        ref="custable"
        :is-selection="true"
        :is-index="true"
        :is-pagination="true"
        :is-handle="true"
        :table-cols="pageData.tableCols"

        :table-data="pageData.tableData"
        :table-page="pageData.tablePage"
        @handleCurrentChange="searchtablelist()"
        @selectChange="selectChange"
      />
      <modal
        ref="modalRef"
        :modal-config="pageData.modalConfig"
        :modal-form="pageData.modalForm"
        :modal-data="pageData.modalData"
        :modal-handles="pageData.modalHandles"
        @closeModal="closeModal()"
      />
    </div>
  </div>
</template>
<script src="./SysUser.js"></script>
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
  width: 100%;
  flex: 1;
}
</style>
