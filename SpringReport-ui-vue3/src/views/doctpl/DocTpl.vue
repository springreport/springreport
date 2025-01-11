<template>
  <div class="_tablepage">
    <searchForm :tableHandles="pageData.tableHandles"> </searchForm>
    <cusTable
      :isSelection="false"
      :isIndex="false"
      :isPagination="false"
      :isHandle="true"
      :loading="tableLoading"
      :tableCols="pageData.tableCols"
      :tableData="pageData.tableData"
      :tablePage="pageData.tablePage"
      :lazy="pageData.lazy"
      @load="loadData"
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
    <modal
      ref="folderModalRef"
      :modalConfig="pageData.folderModalConfig"
      :modalForm="pageData.folderModalForm"
      :modalData="pageData.folderModalData"
      :modalHandles="pageData.folderModalHandles"
      @closeModal="closeFolderModal()"
    ></modal>
  </div>
</template>
<script src="./DocTpl.js"></script>
