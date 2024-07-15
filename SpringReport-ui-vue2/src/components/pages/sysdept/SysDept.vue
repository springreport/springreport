<template>
    <div  class="_tablepage">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm>
       <cusTable  
      :isSelection='false'
      :isIndex='false'
      :isPagination='false'
      :isHandle='true'
      :stripe='true'
      :defaultExpandAll='true'
      :tableCols='pageData.tableCols' 
      :tableHandles='pageData.tableHandles'
      :tableData='pageData.tableData'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
    </div>
</template>
<script src="./SysDept.js"></script>