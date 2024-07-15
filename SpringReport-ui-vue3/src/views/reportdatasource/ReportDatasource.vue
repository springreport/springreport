<template>
    <div  class="echarts-container">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm>
       <cusTable  
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='pageData.tableCols' 
      :tableHandles='pageData.tableHandles'
      :tableData='pageData.tableData'
      :tablePage='pageData.tablePage'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
       <modal ref="resultPropRef" :modalConfig='pageData.resultPropModalConfig' 
       :modalForm='pageData.resultPropModalForm' :modalData='pageData.resultPropModalData' 
       :modalHandles='pageData.resultPropModalHandles'
       @closeModal="closePropsModal()"></modal>
       <modal ref="apiHeaderRef" :modalConfig='pageData.apiHeaderModalConfig' 
       :modalForm='pageData.apiHeaderModalForm' :modalData='pageData.apiHeaderModalData' 
       :modalHandles='pageData.apiHeaderModalHandles'
       @closeModal="closeHeadersModal()"></modal>
    </div>
</template>
<script src="./ReportDatasource.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
  }
</style>