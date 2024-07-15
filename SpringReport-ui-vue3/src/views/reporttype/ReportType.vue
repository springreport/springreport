<!--
 * @Description: 
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-02-09 09:36:25
 * @LastEditors: caiyang
 * @LastEditTime: 2021-02-09 11:22:21
-->
<template>
    <div  class="echarts-container">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm>
       <cusTable  
      :isSelection='false'
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
<script src="./ReportType.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
  }
</style>