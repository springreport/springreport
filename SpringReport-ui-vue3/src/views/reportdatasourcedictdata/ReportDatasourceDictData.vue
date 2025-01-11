<template>
  <div>
    <searchForm
      :searchForm="pageData.searchForm"
      :tableHandles="pageData.tableHandles"
      :searchData="pageData.queryData"
      :searchHandle="pageData.searchHandle"
    >
    </searchForm>
    <cusTable
      :isSelection="true"
      :isIndex="true"
      :isPagination="true"
      :isHandle="true"
      :loading="tableLoading"
      :tableCols="pageData.tableCols"
      :tableData="pageData.tableData"
      :tablePage="pageData.tablePage"
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
  </div>
</template>
<script src="./ReportDatasourceDictData.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
  }
</style>
