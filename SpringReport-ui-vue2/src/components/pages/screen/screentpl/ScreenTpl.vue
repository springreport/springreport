<template>
  <div class="_tablepage">
    <searchForm
      :table-handles="pageData.tableHandles"
    />
    <cusTable
      ref="custable"
      :is-selection="false"
      :is-index="false"
      :is-pagination="false"
      :is-handle="true"
      :table-cols="pageData.tableCols"
      :table-data="pageData.tableData"
      :table-page="pageData.tablePage"
      :lazy="pageData.lazy"
      @handleCurrentChange="searchtablelist()"
      @selectChange="selectChange"
      @load="loadData"
    />
    <modal
      ref="modalRef"
      :modal-config="pageData.modalConfig"
      :modal-form="pageData.modalForm"
      :modal-data="pageData.modalData"
      :modal-handles="pageData.modalHandles"
      @closeModal="closeModal()"
    />
    <modal
      ref="copyModalRef"
      :modal-config="pageData.copyModalConfig"
      :modal-form="pageData.copyModalForm"
      :modal-data="pageData.copyModalData"
      :modal-handles="pageData.copyModalHandles"
      @closeModal="closeCopyModal()"
    />
    <modal
      ref="folderModalRef"
      :modal-config="pageData.folderModalConfig"
      :modal-form="pageData.folderModalForm"
      :modal-data="pageData.folderModalData"
      :modal-handles="pageData.folderModalHandles"
      @closeModal="closeFolderModal()"
    />
  </div>
</template>
<script src="./ScreenTpl.js"></script>
