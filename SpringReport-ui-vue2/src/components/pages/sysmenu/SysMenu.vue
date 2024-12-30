<template>
  <div class="_tablepage">
    <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
    <cusTable
      ref="custable"
      :is-selection="false"
      :is-index="false"
      :is-pagination="false"
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
</template>
<script src="./SysMenu.js"></script>
