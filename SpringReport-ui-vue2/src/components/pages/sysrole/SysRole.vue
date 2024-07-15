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
       <modal ref="authRef" :modalConfig='pageData.authDialogParam' 
       :modalForm='pageData.authModalForm'
       :modalHandles='pageData.authModalHandles'
       @closeModal="closeAuthModal()">
       </modal>
       <modal ref="reportAuthRef" :modalConfig='pageData.reportAuthDialogParam' 
       :modalForm='pageData.reportAuthModalForm'
       :modalHandles='pageData.reportAuthModalHandles'
       @closeModal="closeReportAuthModal()">
       </modal>
    </div>
</template>
<script src="./SysRole.js"></script>