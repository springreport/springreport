<template>
    <div class="container">
        <div class="left">
        <el-tree
            :data="pageData.treeData"
            :props="pageData.defaultProps"
            @node-click="handleNodeClick"
            default-expand-all
        ></el-tree>
        </div>
        <div  class="_tablepage">
            <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
            </searchForm>
            <cusTable ref="custable" 
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
  flex: 1;
}
</style>