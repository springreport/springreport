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
        :searchForm="pageData.searchForm"
        :searchData="pageData.queryData"
        :searchHandle="pageData.searchHandle"
        :tableHandles="pageData.tableHandles"
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
  </div>
</template>
<script src="./SysUser.js"></script>
<style lang="scss" scoped>
  .echarts-container {
    padding: $base-main-padding;
    background-color: $base-color-white;
    width: calc(100% - 200px);
  }
  .container {
    display: flex;
    overflow: auto;
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
    width: calc(100% - 232px - 16px);
  }
</style>
