<!--
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-09-18 08:49:59
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-07-06 08:15:08
-->
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
    <div class="_tablepage">
     <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
      <cusTable
        ref="custable"
        :is-selection="false"
        :is-index="false"
        :is-pagination="true"
        :is-handle="true"
        :table-cols="pageData.tableCols"
        :table-data="pageData.tableData"
        :table-page="pageData.tablePage"
        :lazy="pageData.lazy"
        @handleCurrentChange="searchtablelist()"
        @selectChange="selectChange"
      />
    </div>
  </div>
</template>
<script src="./viewReport.js"></script>
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
  width: 100%;
  flex: 1;
}
</style>
