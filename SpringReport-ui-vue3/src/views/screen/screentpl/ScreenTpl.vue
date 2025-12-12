<template>
  <div class="container">
    <div class="left">
      <el-tree
        :data="pageData.treeData"
        :props="pageData.defaultProps"
        @node-click="handleNodeClick"
        default-expand-all
      >
        <template #default="{ node, data }">
          <div class="set-group df-c" @click="groupSetVisible = true" style="fontsize: 14px">
            <img
              v-if="data.id != '1'"
              src="@/assets/img/sheet/del.png"
              width="14px"
              height="14px"
              @click="() => removeNode(node, data)"
            />
            <div class="setting-text">{{ node.label }}</div>
          </div>
        </template>
      </el-tree>
    </div>
    <div class="_tablepage">
    <searchForm :search-form="pageData.searchForm" :search-data="pageData.queryData" :search-handle="pageData.searchHandle" :table-handles="pageData.tableHandles" />
    <cusTable
      ref="custable"
      :is-selection="true"
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
  <div style="display: none">
        <input id="uploadPic" type="file" accept=".sr" @change="uploadPic">
      </div>
      <textarea id="clipboradInput" style="opacity:0;position:absolute" />
</div>
</template>
<script src="./ScreenTpl.js"></script>
<style scoped lang="scss">
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
    background: #ffffff;
    padding: 16px;

    .screen-list {
      height: calc(100vh - 300px);
      overflow-y: auto;
      overflow-x: hidden;
    }

    .more-btn {
      transition: all 0.3s;
      cursor: pointer;
    }

    .ces-pagination {
      padding: 10px 16px 4px;
      text-align: right;
      .pagination-total {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.6);
      }
    }

    ::v-deep .el-dropdown-menu--small {
      padding: 0 !important;
    }
    ::v-deep .el-dropdown-menu__item {
      height: 32px;
      line-height: 32px;
      padding: 0 16px;
      min-width: 70px;
      text-align: center;
      font-size: 14px;
    }
    ::v-deep .el-dropdown-menu {
      padding: 0;
    }
    ::v-deep .el-dropdown-item-del {
      color: #ff4d4f !important;
    }
    ::v-deep .el-dropdown-menu__item {
      text-align: left;
    }

    .screen-item {
      border-radius: 6px;
      border: 1px solid rgba(0, 0, 0, 0.1);
      background: #fff;
      box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.05);
      padding: 8px 16px;
      margin-bottom: 24px;
      .tpl-code {
        color: rgba(0, 0, 0, 0.6);
        text-align: justify;
        font-family: 'PingFang SC';
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 18px; /* 17.794px */
        margin-bottom: 10px;
      }
      .img-box {
        border-radius: 8px 8px 0px 0px;
        width: 100%;
        height: 144px;
        background-color: #f5f6f7;
        margin-bottom: 10px;

        .img {
          border-radius: 8px 8px 0px 0px;
          width: 100%;
          height: 144px;
          display: block;
        }
      }

      .name {
        color: rgba(0, 0, 0, 0.8);
        text-align: justify;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: 400;
        line-height: 21px; /* 21px */
        margin-bottom: 4px;
      }
      .size {
        color: rgba(0, 0, 0, 0.45);
        text-align: justify;
        font-family: 'PingFang SC';
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 18px; /* 18px */
      }
    }
  }
</style>
