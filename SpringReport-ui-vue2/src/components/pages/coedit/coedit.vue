<template>
    <div class="contentbox" style="height: 100%;display: flex;flex-direction: column;">
        <div style="width: 100%;flex: none;">
            <el-header class="_header df-c-b">
                <div class="left df-c">
                <img
                    src="@/static/img/logoWithName.png"
                    height="30px"
                    style="margin-left:8px"
                />
                </div>
                <div class="right df-c">
                <el-dropdown class="white font" trigger="click" placement="bottom" v-if="users.length > 0">
                    <span class="el-dropdown-link df-c">
                    <el-avatar size="small" :style="{marginRight:'4px',backgroundColor:item.color+' !important'}" shape="circle" :title="item.userName" v-for="(item,index) in headerUsers" :key="index"> {{(item.userName.slice(0,1)).toUpperCase()}} </el-avatar>
                    <i class="el-icon-arrow-down el-icon--right" ></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item  v-for="(item,index) in users" :key="index">{{item.userName}}</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                </div>
            </el-header>
        </div>
        <div class="center" v-loading="loading" :element-loading-text="loadingText">
            <div id="luckysheet" style="width:100%;height:100%;left: 0px;overflow:auto;"></div>
            <el-dialog
            :modal="false"
            :close-on-click-modal='false'
            :title="hisDialogTitle"
            :visible.sync="hisdialogVisible"
             @close="closeModal"
            width="300px"
            custom-class="hisdialog"
            >
            <div class="el-dialog-div" v-if="historyData && historyData.length > 0" >
                <div v-for="(item,index) in historyData" :key="index">
                <el-divider content-position="left">{{item.createTime}}</el-divider>
                <el-descriptions title="" :column="1" border>
                    <el-descriptions-item label="修改内容">{{item.changeDesc}}</el-descriptions-item>
                    <el-descriptions-item label="操作人">{{item.operator}}</el-descriptions-item>
                </el-descriptions>
                </div>
            </div>
            <span slot="footer" class="dialog-footer"  v-if="historyData && historyData.length > 0">
                <el-pagination
                    small
                    layout="prev, pager, next"
                    :total="total"
                    :page-size="pageSize"
                    :current-page.sync="currentPage"
                    @current-change="handleCurrentChange">
                    </el-pagination>
            </span>
            <el-empty v-if="!historyData || historyData.length == 0" description="暂无改动记录"></el-empty>
        </el-dialog>
        <el-dialog :title="authTitle" :visible.sync="addAuthVisiable" width="850px" height="80%" custom-class="addauthdialog"  :modal="true" :close-on-click-modal='false' @close='closeAddAuth'>
                <el-form :inline="true" :model="addAuthForm" class="demo-form-inline" ref="addAuthRef">
                    <el-transfer
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
                    </el-transfer>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="closeAddAuth" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddAuth" size="small">确 定</el-button>
                </span>
            </el-dialog>
        <el-dialog
            :modal="false"
            :close-on-click-modal='false'
            :title="authedRangeTitle"
            :visible.sync="authdialogVisible"
             @close="closeAuthDialog"
             custom-class="hisdialog"
            width="300px"
            >
            <div class="el-dialog-div" v-if="authedRange && authedRange.length > 0" >
                <div v-for="(item,index) in authedRange" :key="index">
                <el-descriptions title="" :column="1" border>
                    <el-descriptions-item label="保护范围">{{item.rangeAxis}}</el-descriptions-item>
                    <el-descriptions-item label="保护类型" v-if="!isCreator">{{item.authType==1?'可编辑':'不可查看'}}</el-descriptions-item>
                    <el-descriptions-item label="授权人数" v-if="isCreator">{{item.userIds.length}}</el-descriptions-item>
                </el-descriptions>
                <div style="text-align:right;margin-top:5px" v-if="isCreator">
                <el-button type="primary" title="编辑" icon="el-icon-edit" circle size="mini" @click="editRange(item)"></el-button>
                    <el-button type="warning" icon="el-icon-monitor" title="显示选区" circle size="mini" @click="showRange(item)"></el-button>
                    <el-button type="danger" icon="el-icon-delete" title="删除" circle size="mini" @click="deleteRange(item,index)"></el-button>
                </div>
                <el-divider content-position="left"></el-divider>
                </div>
            </div>
            <el-empty v-if="(!authedRange || authedRange.length == 0) && isCreator" description="暂无授权信息"></el-empty>
            <el-empty v-if="(!authedRange || authedRange.length == 0) && !isCreator" description="暂无操作权限"></el-empty>
        </el-dialog>
        <div style="display:none">
            <input id="uploadBtn" type="file" accept="xlsx/*"  @change="loadExcel" />
          </div>
        </div>
        
    </div>
</template>

<script src="./coedit.js"></script>

<style scoped lang="scss">
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
/* ::v-deep .el-divider__text{
    background:#d9ebf0 !important;
}

::v-deep .el-divider{
    background-color:#17b794
}

::v-deep .el-descriptions__body{
    background-color:#d9ebf0
}

::v-deep .el-descriptions .is-bordered .el-descriptions-item__cell{
    border:1px solid #c0c4cc
}

::v-deep .el-descriptions-item__label.is-bordered-label{
    background:#d9ebf0
}
::v-deep .el-pagination button:disabled{
    background-color:#d9ebf0
}
::v-deep .el-pagination .btn-prev, ::v-deep .el-pagination .btn-next{
    background-color:#d9ebf0
}
::v-deep  .el-pager .number{
    background-color:#d9ebf0
}
::v-deep .el-icon-more{
    background-color:#d9ebf0
} */
</style>
