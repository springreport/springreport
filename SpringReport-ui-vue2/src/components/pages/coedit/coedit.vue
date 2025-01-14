<template>
  <div class="contentbox" style="height: 100%;display: flex;flex-direction: column;">
    <div style="width: 100%;flex: none;">
      <el-header class="_header df-c-b">
        <div class="left df-c">
          <img
            src="@/static/img/logoWithName.png"
            height="30px"
            style="margin-left:8px"
          >
        </div>
        <div class="right df-c">
          <el-dropdown v-if="users.length > 0" class="white font" trigger="click" placement="bottom">
            <span class="el-dropdown-link df-c">
              <el-avatar v-for="(item,index) in headerUsers" :key="index" size="small" :style="{marginRight:'4px',backgroundColor:item.color+' !important'}" shape="circle" :title="item.userName"> {{ (item.userName.slice(0,1)).toUpperCase() }} </el-avatar>
              <i class="el-icon-arrow-down el-icon--right" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="(item,index) in users" :key="index">{{ item.userName }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
    </div>
    <div v-loading="loading" :element-loading-text="loadingText" style="width: 100%;flex: 1;overflow: auto;">
      <!-- <div id="luckysheet" style="width:100%;height:100%;left: 0px;overflow:auto;"></div> -->
      <div v-if="chartSettingShow" class="config-panel">
        <div class="config-header">图表设置</div>
        <div class="config-box">
          <vchartsetting :component="chartOptions" :is-preview="false" :is-coedit="true" />
        </div>
      </div>
      <div id="luckysheet" style="margin:0px;padding:0px;width:100%;height:100%;left: 0px;top: 50px" />
      <el-dialog
        :modal="false"
        :close-on-click-modal="false"
        :title="hisDialogTitle"
        :visible.sync="hisdialogVisible"
        width="300px"
        custom-class="hisdialog"
        @close="closeModal"
      >
        <div v-if="historyData && historyData.length > 0" class="el-dialog-div">
          <div v-for="(item,index) in historyData" :key="index">
            <el-divider content-position="left">{{ item.createTime }}</el-divider>
            <el-descriptions title="" :column="1" border>
              <el-descriptions-item label="修改内容">{{ item.changeDesc }}</el-descriptions-item>
              <el-descriptions-item label="操作人">{{ item.operator }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
        <span v-if="historyData && historyData.length > 0" slot="footer" class="dialog-footer">
          <el-pagination
            small
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page.sync="currentPage"
            @current-change="handleCurrentChange"
          />
        </span>
        <el-empty v-if="!historyData || historyData.length == 0" description="暂无改动记录" />
      </el-dialog>
      <el-drawer
        :title="authTitle"
        :visible.sync="addAuthVisiable"
        custom-class="handle-drawer"
        class="handle-drawer"

        :modal="true"
        :close-on-click-modal="false"
        @close="closeAddAuth"
      >
        <el-form ref="addAuthRef" label-position="top" :model="addAuthForm" class="demo-form-inline">
          <!-- <el-transfer
                        v-model="addAuthForm.userIds"
                        :data="authUsers"
                        :titles="['用户信息', '授权用户']"
                        :filterable="true"
                        :props="{key:'id',label:'userName'}"
                    >
                    <span slot-scope="{ option }" style="display: flex">
                        <div :class="{'authuserclass':addAuthForm.userIds.includes(option.id)}" :title="option.userName">{{ option.userName }}</div>
                        <el-select  placeholder="请选择" size="mini" v-if="addAuthForm.userIds.includes(option.id)" v-model="option.authType" style="margin-left: 10px;width:120px">
                            <el-option label="编辑" :value=1></el-option>
                            <el-option label="不可查看" :value=2></el-option>
                          </el-select>
                    </span>
                    </el-transfer> -->
          <el-tree
            ref="tree"
            :data="authUsers"
            show-checkbox
            default-expand-all
            node-key="id"
            highlight-current
            :props="defaultProps"
            :default-checked-keys="defaultCheckedUsers"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span>{{ node.label }}</span>
              <span v-if="node.key.indexOf('dept')<0">
                <el-select v-model="data.authType" placeholder="请选择" size="mini" style="margin-left: 10px;width:120px;height:20px" @change="changeAuthType(data)">
                  <el-option label="编辑" :value="1" />
                  <el-option label="不可查看" :value="2" />
                </el-select>
              </span>
            </span>
          </el-tree>
        </el-form>
        <div class="handle-drawer__footer">
          <el-button size="small" @click="closeAddAuth">取 消</el-button>
          <el-button type="primary" size="small" @click="confirmAddAuth">确 定</el-button>
        </div>
      </el-drawer>
      <el-drawer
        :modal="false"
        :close-on-click-modal="false"
        :title="authedRangeTitle"
        :visible.sync="authdialogVisible"
        custom-class="handle-drawer"
        class="handle-drawer"
        @close="closeAuthDialog"
      >
        <div v-if="authedRange && authedRange.length > 0" class="el-dialog-div">
          <div v-for="(item,index) in authedRange" :key="index">
            <el-descriptions title="" :column="1" border>
              <el-descriptions-item label="保护范围">{{ item.rangeAxis }}</el-descriptions-item>
              <el-descriptions-item v-if="!isCreator" label="保护类型">{{ item.authType==1?'可编辑':'不可查看' }}</el-descriptions-item>
              <el-descriptions-item v-if="isCreator" label="授权人数">{{ item.userIds.length }}</el-descriptions-item>
            </el-descriptions>
            <div v-if="isCreator" style="text-align:right;margin-top:5px">
              <el-button type="primary" title="编辑" icon="el-icon-edit" circle size="mini" @click="editRange(item)" />
              <el-button type="warning" icon="el-icon-monitor" title="显示选区" circle size="mini" @click="showRange(item)" />
              <el-button type="danger" icon="el-icon-delete" title="删除" circle size="mini" @click="deleteRange(item,index)" />
            </div>
            <el-divider content-position="left" />
          </div>
        </div>
        <el-empty v-if="(!authedRange || authedRange.length == 0) && isCreator" description="暂无授权信息" />
        <el-empty v-if="(!authedRange || authedRange.length == 0) && !isCreator" description="暂无操作权限" />
      </el-drawer>
      <div style="display:none">
        <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel">
      </div>
      <div style="display:none">
        <input id="uploadAttachmentBtn" type="file" accept="*" @change="changeAttachment">
      </div>
    </div>
    <vchart :show.sync="vchartShow" @closeModal="closeAddChartModal()" />
  </div>
</template>

<script src="./coedit.js"></script>

<style scoped lang="scss">
::v-deep .el-tree-node__content{
    font-size: 14px;
}
.center{
    flex: 1;
    height: 100vh;
}
::v-deep .el-dialog__wrapper {
   overflow: hidden;
//    z-index: 2005 !important;
   pointer-events: none !important;
}

::v-deep .el-dialog{
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
}
::v-deep .hisdialog{
    margin-top: 136px !important;
    margin-left: 0px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    top:0 !important;
    // right:14px !important;
    left:calc(100% - 314px)!important;
    bottom: 0;
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
}
.hisdialog ::v-deep .el-dialog__body{
    height: calc(100% - 90px) !important;
    overflow: auto;
}
.hisdialog .el-dialog-div{
     max-height: 60vh;
     overflow: auto;
     margin-left: 10px;
}
.hisdialog .el-dialog-div::-webkit-scrollbar {
    display: none; /*隐藏滚动条*/
}
.hisdialog ::v-deep .el-dialog__title{
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
::v-deep .el-transfer-panel{
    width:300px !important
}
.authuserclass{
    width:100px;
    overflow: hidden;  /* 将超出部分隐藏 */
    text-overflow: ellipsis;  /* 显示省略号 */
    white-space: nowrap;  /* 禁止换行 */
}
.config-panel{
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
      .config-header{
        width: 100%;
        height: 32px;
        // background: #2F343D;
        font-size: 13px;
        font-weight: 400;
        color: #000000;
        line-height: 32px;
        text-align: center;
      }
      .config-box{
        flex:1;
        padding: 10px;
        overflow: auto;
      }

      /*定义滚动条的宽度*/
      .config-box::-webkit-scrollbar {
        width: 0;
      }
}
</style>
