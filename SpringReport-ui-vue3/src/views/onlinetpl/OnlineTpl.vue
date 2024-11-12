<template>
    <div  class="echarts-container">
        <!-- <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm> -->
       <cusTable  
      :isSelection='false'
      :isIndex='false'
      :isPagination='false'
      :isHandle='true'
      :tableCols='pageData.tableCols' 
      :tableHandles='pageData.tableHandles'
      :tableData='pageData.tableData'
      :tablePage='pageData.tablePage'
      :lazy='pageData.lazy'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'
      @load="loadData"></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
       <modal ref="folderModalRef" :modalConfig='pageData.folderModalConfig' 
       :modalForm='pageData.folderModalForm' :modalData='pageData.folderModalData' 
       :modalHandles='pageData.folderModalHandles'
       @closeModal="closeFolderModal()"></modal>
    </div>
</template>
<script src="./OnlineTpl.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
  }
</style>