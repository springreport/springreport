<template>
  <div class="container">
    <div class="left">
      <el-tree
        :data="pageData.treeData"
        :props="pageData.defaultProps"
        default-expand-all
        @node-click="handleNodeClick"
      >
        <span slot-scope="{ node, data }">
          <div
            class="set-group df-c"
            style="fontsize: 14px"
            @click="groupSetVisible = true"
          >
            <img
              v-if="data.id != '1'"
              src="@/static/img/sheet/del.png"
              width="14px"
              height="14px"
              @click="() => removeNode(node, data)"
            >
            <div class="setting-text">{{ node.label }}</div>
          </div>
        </span>
      </el-tree>
    </div>
    <div class="_tablepage">
      <searchForm
        :search-form="pageData.searchForm"
        :search-data="pageData.queryData"
        :search-handle="pageData.searchHandle"
        :table-handles="pageData.tableHandles"
      />

      <div
        v-loading="requestLoading"
        element-loading-text="加载中"
        class="screen-list"
      >
        <el-row v-if="pageData.tableData.length > 0" :gutter="24">
          <el-col
            v-for="(item, index) in pageData.tableData"
            :key="item.id"
            :xs="12"
            :sm="12"
            :md="8"
            :xl="6"
          >
            <div class="screen-item">
              <div class="df-c-b">
                <div class="tpl-code">
                  {{ item.tplCode }}
                </div>
                <el-dropdown size="small" class="table-dropdown">
                  <div class="more-btn">
                    <img
                      src="@/static/img/template/more.png"
                      width="18px"
                      height="18px"
                    >
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      v-for="btn in pageData.tableCols[0].btnList"
                      v-show="btn.show ? btn.show(item) : true"
                      :key="
                        (typeof btn.label).toLowerCase() == 'string'
                          ? btn.label
                          : btn.auth
                      "
                      v-has="btn.auth"
                      :disabled="btn.disabled && btn.disabled(item)"
                      :type="btn.type"
                      :size="btn.size"
                      :icon="btn.icon"
                      :class="{ 'el-dropdown-item-del': btn.type === 'danger' }"
                      @click.native="btn.handle && btn.handle(item, index)"
                    >{{
                      (typeof btn.label).toLowerCase() == "string"
                        ? btn.label
                        : btn.label(item)
                    }}</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div class="img-box">
                <img
                  class="img"
                  src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
                >
              </div>
              <div class="name overflow-text">{{ item.tplName }}</div>
              <div class="size">尺寸：{{ item.width }}*{{ item.height }}</div>
            </div>
          </el-col>
        </el-row>
        <el-empty v-else description="暂无数据" />
      </div>

      <section class="ces-pagination df-c-b">
        <div class="pagination-total">
          共 {{ pageData.tablePage.pageTotal }} 项数据
        </div>
        <el-pagination
          background
          layout="sizes ,prev, pager, next,jumper"
          :page-size="pageData.tablePage.pageSize"
          :page-sizes="pageData.tablePage.pageSizeRange"
          :current-page.sync="pageData.tablePage.currentPage"
          :total.sync="pageData.tablePage.pageTotal"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </section>

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
    height: calc(100vh - 286px);
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
      font-family: "PingFang SC";
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 18px; /* 17.794px */
      margin-bottom: 10px;
    }
    .img {
      border-radius: 8px 8px 0px 0px;
      width: 100%;
      height: 144px;
      object-fit: cover;
      display: block;
    }
    .name {
      color: rgba(0, 0, 0, 0.8);
      text-align: justify;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: 21px; /* 21px */
      margin-bottom: 4px;
    }
    .size {
      color: rgba(0, 0, 0, 0.45);
      text-align: justify;
      font-family: "PingFang SC";
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 18px; /* 18px */
    }
  }
}
</style>
