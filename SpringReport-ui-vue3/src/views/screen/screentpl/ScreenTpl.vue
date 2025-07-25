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
      <searchForm
        :search-form="pageData.searchForm"
        :search-data="pageData.queryData"
        :search-handle="pageData.searchHandle"
        :table-handles="pageData.tableHandles"
      />
      <div v-loading="requestLoading" element-loading-text="加载中" class="screen-list">
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
                <!-- :teleported="false" -->
                <el-dropdown size="small" class="table-dropdown">
                  <div class="more-btn">
                    <img src="@/assets/img/template/more.png" width="18px" height="18px" />
                  </div>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        v-for="btn in pageData.tableCols[0].btnList"
                        v-show="btn.show ? btn.show(item) : true"
                        :key="(typeof btn.label).toLowerCase() == 'string' ? btn.label : btn.auth"
                        v-has="btn.auth"
                        :disabled="btn.disabled && btn.disabled(item)"
                        :type="btn.type"
                        :size="btn.size"
                        :icon="btn.icon"
                        :class="{ 'el-dropdown-item-del': btn.type === 'danger' }"
                        @click="btn.handle && btn.handle(item, index)"
                        >{{
                          (typeof btn.label).toLowerCase() == 'string' ? btn.label : btn.label(item)
                        }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
              <div class="img-box">
                <!-- style="object-fit: contain" 这个根据情况改 -->
                <img
                  class="img"
                  style="object-fit: contain"
                  src="@/assets/img/template/screen-tem-default.png"
                />
              </div>
              <div class="name overflow-text">{{ item.tplName }}</div>
              <div class="size">尺寸：{{ item.width }}*{{ item.height }}</div>
            </div>
          </el-col>
        </el-row>
        <el-empty v-else description="暂无数据" />
      </div>

      <section class="ces-pagination df-c-b">
        <div class="pagination-total"> 共 {{ pageData.tablePage.pageTotal }} 项数据 </div>
        <el-pagination
          background
          layout="sizes ,prev, pager, next,jumper"
          :page-size="pageData.tablePage.pageSize"
          :page-sizes="pageData.tablePage.pageSizeRange"
          v-model:current-page="pageData.tablePage.currentPage"
          v-model:total="pageData.tablePage.pageTotal"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </section>
      <modal
        ref="modalRef"
        :modalConfig="pageData.modalConfig"
        :modalForm="pageData.modalForm"
        :modalData="pageData.modalData"
        :modalHandles="pageData.modalHandles"
        @closeModal="closeModal()"
      ></modal>
      <modal
        ref="copyModalRef"
        :modalConfig="pageData.copyModalConfig"
        :modalForm="pageData.copyModalForm"
        :modalData="pageData.copyModalData"
        :modalHandles="pageData.copyModalHandles"
        @closeModal="closeCopyModal()"
      ></modal>
      <modal
        ref="folderModalRef"
        :modalConfig="pageData.folderModalConfig"
        :modalForm="pageData.folderModalForm"
        :modalData="pageData.folderModalData"
        :modalHandles="pageData.folderModalHandles"
        @closeModal="closeFolderModal()"
      ></modal>
      <modal
        ref="shareReport"
        :modal-config="pageData.shareReportConfig"
        :modal-form="pageData.shareReportForm"
        :modal-data="pageData.shareReportModalData"
        :modal-handles="pageData.shareReportModalHandles"
        @closeModal="closeShareReportModal()"
      />
      <textarea id="clipboradInput" style="opacity:0;position:absolute" />
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
