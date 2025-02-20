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
      <searchForm
        ref="searchRef"
        :searchForm="pageData.searchForm"
        :searchData="pageData.queryData"
        :searchHandle="pageData.searchHandle"
      ></searchForm>
      <cusTable
        :isSelection="false"
        :isIndex="false"
        :isPagination="true"
        :isHandle="true"
        :tableCols="pageData.tableCols"
        :tableHandles="pageData.tableHandles"
        :tableData="pageData.tableData"
        :tablePage="pageData.tablePage"
        :lazy='pageData.lazy'
        @handleCurrentChange="searchtablelist()"
        @selectChange="selectChange"
      ></cusTable>
    </div>
  </div>
</template>
<script src="./viewReport.js"></script>
<style lang="scss" scoped>
.echarts-container {
  padding: $base-main-padding;
  background-color: $base-color-white;
  width: calc(100% - 200px);
}
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
