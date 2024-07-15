<template>
    <div  class="_tablepage">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm>
       <cusTable  ref="custable"
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
    </div>
</template>
<script src="./ReportDatasourceDictData.js"></script>