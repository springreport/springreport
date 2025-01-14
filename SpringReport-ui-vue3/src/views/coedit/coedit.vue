<template>
  <div class="contentbox" style="height: 100vh; display: flex; flex-direction: column">
    <div style="width: 100%; flex: none">
      <el-header class="_header df-c-b">
        <div class="left df-c">
          <img src="@/assets/img/logoWithName.png" height="30" style="margin-left: 8px" />
        </div>
        <div class="right df-c">
          <el-dropdown
            class="white font"
            trigger="click"
            placement="bottom"
            v-if="users.length > 0"
          >
            <span class="el-dropdown-link df-c">
              <el-avatar
                :size="30"
                :style="{ marginRight: '4px', backgroundColor: item.color + ' !important' }"
                shape="circle"
                :title="item.userName"
                v-for="(item, index) in headerUsers"
                :key="index"
              >
                {{ item.userName.slice(0, 1).toUpperCase() }}
              </el-avatar>
              <icon-down theme="outline" size="16" />
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="(item, index) in users" :key="index">{{
                  item.userName
                }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
    </div>
    <div
      class="center"
      v-loading="loading"
      :element-loading-text="loadingText"
      style="width: 100%; flex: 1; overflow: auto"
    >
      <!-- <div id="luckysheet" style="width:100%;height:100%;left: 0px;overflow:auto;"></div> -->
      <div class="config-panel" v-if="chartSettingShow">
        <div class="config-header">图表设置</div>
        <div class="config-box">
          <vchartsetting
            :component="chartOptions"
            :isPreview="false"
            :isCoedit="true"
          ></vchartsetting>
        </div>
      </div>
      <div
        id="luckysheet"
        style="margin: 0px; padding: 0px; width: 100%; height: 100%; left: 0px; top: 50px"
      ></div>
      <el-dialog
        :modal="false"
        :close-on-click-modal="false"
        :title="hisDialogTitle"
        v-model="hisdialogVisible"
        @close="closeModal"
        width="300px"
        modal-class="hisDialog"
      >
        <div class="el-dialog-div" v-if="historyData && historyData.length > 0">
          <div v-for="(item, index) in historyData" :key="index">
            <el-divider content-position="left">{{ item.createTime }}</el-divider>
            <el-descriptions title="" :column="1" border>
              <el-descriptions-item label="修改内容">{{ item.changeDesc }}</el-descriptions-item>
              <el-descriptions-item label="操作人">{{ item.operator }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer" v-if="historyData && historyData.length > 0">
            <el-pagination
              small
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handleCurrentChange"
            >
            </el-pagination>
          </span>
        </template>
        <el-empty
          v-if="!historyData || historyData.length == 0"
          description="暂无改动记录"
        ></el-empty>
      </el-dialog>
      <el-drawer
        :title="authTitle"
        v-model="addAuthVisiable"
        size="36%"
        custom-class="handle-drawer"
        class="handle-drawer"
        :modal="true"
        :close-on-click-modal="false"
        @close="closeAddAuth"
      >
        <el-form
          label-position="top"
          :model="addAuthForm"
          class="demo-form-inline"
          ref="addAuthRef"
        >
          <!-- <el-transfer
                            v-model="addAuthForm.userIds"
                            :data="authUsers"
                            :titles="['用户信息', '授权用户']"
                            :filterable="true"
                            :props="{key:'id',label:'userName'}"
                        >
                        <template #default="{ option }">
                        <span style="display: flex">
                        <div :class="{'authuserclass':addAuthForm.userIds.includes(option.id)}" :title="option.userName">{{ option.userName }}</div>
                        <el-select  placeholder="请选择" size="small" v-if="addAuthForm.userIds.includes(option.id)" v-model="option.authType" style="margin-left: 10px;width:120px">
                            <el-option label="编辑" :value=1></el-option>
                            <el-option label="不可查看" :value=2></el-option>
                          </el-select>
                        </span>
                        </template>
                        </el-transfer> -->
          <el-tree
            :data="authUsers"
            show-checkbox
            default-expand-all
            node-key="id"
            ref="tree"
            highlight-current
            :props="defaultProps"
            :default-checked-keys="defaultCheckedUsers"
          >
            <template #default="{ node, data }">
              <span>{{ node.label }}</span>
              <span v-if="node.key.indexOf('dept') < 0">
                <el-select
                  placeholder="请选择"
                  size="small"
                  v-model="data.authType"
                  style="margin-left: 10px; width: 120px; height: 20px"
                  @change="changeAuthType(data)"
                >
                  <el-option label="编辑" :value="1"></el-option>
                  <el-option label="不可查看" :value="2"></el-option>
                </el-select>
              </span>
            </template>
          </el-tree>
        </el-form>
        <div class="handle-drawer__footer">
          <el-button @click="closeAddAuth" size="small">取 消</el-button>
          <el-button type="primary" @click="confirmAddAuth" size="small">确 定</el-button>
        </div>
      </el-drawer>
      <el-drawer
        :modal="false"
        :close-on-click-modal="false"
        :title="authedRangeTitle"
        v-model="authdialogVisible"
        @close="closeAuthDialog"
        custom-class="handle-drawer"
        class="handle-drawer"
      >
        <div class="el-dialog-div" v-if="authedRange && authedRange.length > 0">
          <div v-for="(item, index) in authedRange" :key="index">
            <el-descriptions title="" :column="1" border>
              <el-descriptions-item label="保护范围">{{ item.rangeAxis }}</el-descriptions-item>
              <el-descriptions-item label="保护类型" v-if="!isCreator">{{
                item.authType == 1 ? '可编辑' : '不可查看'
              }}</el-descriptions-item>
              <el-descriptions-item label="授权人数" v-if="isCreator">{{
                item.userIds.length
              }}</el-descriptions-item>
            </el-descriptions>
            <div style="text-align: right; margin-top: 5px" v-if="isCreator">
              <el-button
                type="primary"
                title="编辑"
                icon="icon-edit"
                circle
                size="small"
                @click="editRange(item)"
              ></el-button>
              <el-button
                type="warning"
                icon="icon-monitor-one"
                title="显示选区"
                circle
                size="small"
                @click="showRange(item)"
              ></el-button>
              <el-button
                type="danger"
                icon="icon-delete"
                title="删除"
                circle
                size="small"
                @click="deleteRange(item, index)"
              ></el-button>
            </div>
            <el-divider content-position="left"></el-divider>
          </div>
        </div>
        <el-empty
          v-if="(!authedRange || authedRange.length == 0) && isCreator"
          description="暂无授权信息"
        ></el-empty>
        <el-empty
          v-if="(!authedRange || authedRange.length == 0) && !isCreator"
          description="暂无操作权限"
        ></el-empty>
      </el-drawer>
      <div style="display: none">
        <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel" />
      </div>
      <div style="display: none">
        <input id="uploadAttachmentBtn" type="file" accept="*" @change="changeAttachment" />
      </div>
    </div>
    <vchart :show="vchartShow" @closeModal="closeAddChartModal()"></vchart>
  </div>
</template>

<script src="./coedit.js"></script>

<style scoped lang="scss">
  .center {
    flex: 1;
    height: 100vh;
  }
  :deep(.hisDialog) {
    pointer-events: none !important;
  }
  :deep(.hisDialog .el-overlay-dialog) {
    flex-direction: column !important;
    overflow: hidden !important;
    pointer-events: none !important;
  }
  :deep(.el-dialog) {
    pointer-events: auto !important;
  }
  :deep(.hisDialog .el-dialog) {
    margin-top: 90px !important;
    margin-right: 14px;
  }
  :deep(.el-dialog__body) {
    height: calc(100% - 90px) !important;
    overflow: auto;
  }
  :deep(.el-dialog-div) {
    max-height: 60vh;
    overflow: auto;
    margin-left: 10px;
  }
  :deep(.el-dialog__title) {
    font-weight: bold;
  }

  ._header {
    height: 46px !important;
    padding: 0px;
    background-color: #fff;
    //   border-bottom: 1px solid #ccc;
    .left {
      img {
        cursor: pointer;
        transition: all 0.3s;
      }
    }
    .right {
      padding-right: 24px;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.9);
      .role-name {
        margin-right: 8px;
      }
    }
    .el-dropdown-link {
      color: rgba(0, 0, 0, 0.9);
    }
  }
  .authuserclass {
    width: 100px;
    overflow: hidden; /* 将超出部分隐藏 */
    text-overflow: ellipsis; /* 显示省略号 */
    white-space: nowrap; /* 禁止换行 */
  }
  :deep(.el-transfer-panel) {
    width: 300px !important;
  }
  .config-panel {
    background: #ffffff;
    // margin-left: 1px;
    top: 45px;
    position: relative;
    width: 306px;
    height: calc(100vh - 64px - 10px - 20px);
    display: flex;
    flex-direction: column;
    overflow: auto;
    z-index: 1999;
    float: right;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    .config-header {
      width: 100%;
      height: 32px;
      // background: #2F343D;
      font-size: 13px;
      font-weight: 400;
      color: #000000;
      line-height: 32px;
      text-align: center;
    }
    .config-box {
      flex: 1;
      padding: 10px;
      overflow: auto;
    }

    /*定义滚动条的宽度*/
    .config-box::-webkit-scrollbar {
      width: 0;
    }
  }
</style>
