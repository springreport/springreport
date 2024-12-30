<template>
  <div class="_tablepage">
    <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
    <cusTable
      :is-selection="false"
      :is-index="false"
      :is-pagination="false"
      :is-handle="true"
      :stripe="true"
      :default-expand-all="true"
      :table-cols="pageData.tableCols"
      :table-data="pageData.tableData"
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
</template>
<script src="./SysDept.js"></script>
