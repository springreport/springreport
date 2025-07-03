<template>
  <div>
    <el-dialog
      title="动态数据"
      :visible="dynamicDialogVisiable"
      width="30%"
      :close-on-click-modal="false"
      @close="closeDynamicDataDialog"
    >
      <el-form
        ref="dataSetForm"
        class="demo-form-inline"
        :model="dataSetForm"
        label-position="top"
        label-width="auto"
        size="mini"
      >
      <div class="df-c-b" style="flex: 1;padding-left:380px">
            <div>
              <el-button
                type="text"
                @click="showAddDatasetDialog(component)"
              >添加数据集</el-button>
              <el-button
                type="text"
                @click="datasetEditDialog(component)"
              >管理数据集</el-button>
            </div>
          </div>
        <el-form-item
        label="数据集"
          prop="dataSetId"
          :rules="filter_rules('数据集', { required: true })"
        >
          <el-select
            v-model="dataSetForm.dataSetId"
            placeholder="请选择"
            @change="changeDataset"
          >
            <el-option
              v-for="item in dataSets"
              :key="item.id"
              :label="item.datasetName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="数据列"
          prop="column"
          :rules="filter_rules('返回值类型', { required: true })"
        >
          <el-select v-model="dataSetForm.column" placeholder="请选择" multiple>
            <el-option
              v-for="item in dataColumn"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          size="mini"
          @click="closeDynamicDataDialog()"
        >取 消</el-button>
        <el-button
          type="primary"
          size="mini"
          @click="dataSetConfirm()"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="数据集"
      width="82%"
      :visible.sync="addDatasetsDialogVisiable"
      :close-on-click-modal="false"
      class="add-dataset-dialog"
      @close="closeAddDataSet"
    >
      <el-form
        ref="sqlRef"
        :inline="true"
        :model="sqlForm"
        class="demo-form-inline"
        size="mini"
        label-position="left"
      >
        <el-form-item
          label="数据集名称"
          prop="datasetName"
          :rules="filter_rules('数据集名称', { required: true })"
        >
          <el-input v-model="sqlForm.datasetName" placeholder="数据集名称" />
        </el-form-item>
        <el-form-item
          label="选择数据源"
          prop="datasourceId"
          :rules="filter_rules('选择数据源', { required: true })"
        >
          <el-select
            v-model="sqlForm.datasourceId"
            placeholder="选择数据源"
            @change="changeDatasource"
          >
            <el-option
              v-for="op in dataSource"
              :key="op.datasourceId"
              :label="op.dataSourceName"
              :value="op.datasourceId"
            />
          </el-select>
        </el-form-item>
        <el-form-item
            label="查询集合(表)"
            prop="mongoTable"
            :rules="filter_rules('查询集合(表)', { required: true })"
            v-if="datasourceType == 3"
          >
            <el-select
              v-model="sqlForm.mongoTable"
              placeholder="查询集合(表)"
              size="small"
            >
              <el-option
                v-for="item in dataSourceTables"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="查询方式"
            prop="mongoSearchType"
            :rules="filter_rules('查询方式', { required: true })"
            v-if="datasourceType == 3"
          >
            <el-select
              v-model="sqlForm.mongoSearchType"
              placeholder="查询方式"
              size="small"
            >
              <el-option
                v-for="item in selectUtil.mongoSearchType"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
      </el-form>

      <div class="df" style="width: 100%">
        <div class="variable-content" v-show="datasourceType != 3">
          <div class="variable-title">选择变量</div>
          <div class="variable-warp">
            <div class="variable-warp-title">系统变量</div>
            <div class="variable-list df">
              <div
                v-for="(item, index) in commonConstants.systemParam"
                :key="index"
                :title="item.label"
                class="variable-item df-c"
              >
                <div class="overflow-text" style="flex: 1; margin-right: 8px">
                  {{ item.label }}({{ item.value }})
                </div>
                <i
                  class="el-icon-copy-document"
                  title="复制"
                  @click="doCopy(item)"
                />
                <i
                  class="el-icon-circle-plus-outline"
                  title="添加"
                  style="margin-left: 4px"
                  @click="doCopy(item, true)"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="sql-content">
          <div v-if="datasourceType == 1 || datasourceType == 3" style="height: 25px">
            <el-tooltip
              content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中"
              placement="bottom"
            ><el-tag
              type="success"
              size="small"
              style="cursor: pointer"
              @click="execSql"
            ><i class="el-icon-caret-right" />执行</el-tag></el-tooltip>
            <el-tooltip
              content="该操作会将sql语句进行格式化并显示"
              placement="right"
            ><el-tag
              size="small"
              style="cursor: pointer"
              @click="formatSql"
              v-if="datasourceType == 1"
            ><i class="el-icon-document" />格式化</el-tag>
            </el-tooltip>

            <el-tooltip
              content="该操作会插入注释标签"
              placement="right"
            ><el-tag
              type="warning"
              size="small"
              style="cursor: pointer"
              @click="addComment(' <!--  -->')"
              v-if="datasourceType == 1"
            ><i class="el-icon-circle-plus-outline" />添加注释</el-tag>
            </el-tooltip>
          </div>

          <div v-if="datasourceType == '1' || datasourceType == '3'" style="height: 300px">
            <div style="height: 100%; width: 100%"  v-if="datasourceType == 1">
              <codemirror ref="codeMirror" :options="cmOptions" />
            </div>
            <div v-if="datasourceType == 3" style="height: 300px">
              <div :style="{height: '100%',width: sqlForm.mongoSearchType == 1?'50%':'100%',float:'left'}" v-if="datasourceType == 3">
                <codemirror ref="codeMirror" :options="cmOptions" />
              </div>
              <div style="height: 100%; width: 50%;float:right" v-if="datasourceType == 3 && sqlForm.mongoSearchType == 1">
                <codemirror ref="orderCodeMirror" :options="cmOptions" />
              </div>
            </div>
          </div>
          <div class="table-warp">
            <div class="table-title">执行结果</div>

            <!--表格 start-->
            <el-table
              :data="
                sqlColumnTableData.tableData.slice(
                  (sqlColumnTableData.tablePage.currentPage - 1) *
                    sqlColumnTableData.tablePage.pageSize,
                  sqlColumnTableData.tablePage.currentPage *
                    sqlColumnTableData.tablePage.pageSize
                )
              "
              border
              style="width: 100%"
              align="center"
              size="small"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="name" label="列名" align="center" />
              <el-table-column prop="dataType" label="数据类型" align="center" />
              <el-table-column prop="width" label="宽度" align="center" />
            </el-table>
            <!--表格 end-->
            <!--分页 start-->
            <el-pagination
              :current-page="sqlColumnTableData.tablePage.currentPage"
              :page-sizes="sqlColumnTableData.tablePage.pageSizeRange"
              :page-size="sqlColumnTableData.tablePage.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="sqlColumnTableData.tablePage.pageTotal"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </div>
        </div>
      </div>

      <!--分页 end-->
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="closeAddDataSet">取 消</el-button>
        <el-button
          type="primary"
          size="mini"
          @click="addDataSet"
        >确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="数据集管理"
      :visible.sync="showDatasetsDialog"
      :close-on-click-modal="false"
      width="800px"
    >
      <section class="ces-table">
        <el-table
          :data="dataSets"
          border
          style="width: 100%"
        >
          <el-table-column
            type="index"
            label="序号"
            align="center"
            width="50"
          />
          <el-table-column prop="datasetName" label="数据集" align="center" />
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editDatasets(scope.$index, scope.row)"
              >编辑</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="deleteDatasets(scope.$index, scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showDatasetsDialog = false">关闭</el-button>
      </span>
    </el-dialog>
    <textarea
      id="clipboradInput"
      value=""
      style="opacity: 0; position: absolute"
    />
  </div>
</template>
<script src="./dynamicDataDialog.js">
</script>
<style scoped lang="scss">
@import "@/element-variables.scss";

.add-dataset-dialog {

  ::v-deep .el-form{
    display: flex;
    .el-form-item{
      display: flex;
    }
  }
  .variable-content {
    border: 1px solid #e4e9ed;
    background: #fafafa;
    width: 240px;

    flex-shrink: 0;
    .variable-title {
      height: 46px;
      padding: 0 16px;
      line-height: 46px;
      color: #1a1a1a;
      font-size: 14px;
      font-style: normal;
      font-weight: bold;
      border-bottom: 1px solid #EFEBEB;
    }
    .variable-warp{
      padding: 9px 17px;
      .variable-warp-title{
        color: #979191;
        font-size: 12px;
        font-style: normal;
        font-weight: bold;
        line-height: 22px; /* 183.333% */
        margin-bottom: 12px;
      }
      .variable-list{
        flex-wrap: wrap;
        .variable-item{
          width: 100%;
          box-sizing: border-box;
          padding: 0 10px;
          border-radius: 4px;
          background: #E1F2F0;
          height: 32px;
          line-height: 32px;
          color: #595959;
          font-size: 12px;
          transition: all 0.3s;
          margin-bottom: 12px;
          cursor: pointer;
          &:hover{
            color: #fff;
            background: $--color-primary;
            ::v-deep .el-icon-circle-plus-outline {
              color: #fff;
            }
            ::v-deep .el-icon-copy-document {
              color: #fff;
            }
          }
          &:nth-child(3n){
            margin-right: 0;
          }
        }
      }
      .analysis-list{
        border-radius: 3px;
        border: 1px solid #C1E0D9;
        background: #FFF;
        padding: 8px 10px 0;
        .variable-item{
          border-radius: 4px;
          background: #F1F2F3;
        }
      }
    }
  }
  .sql-content{
    flex: 1;
    position: relative;
    margin-left: 24px;

    .left-action {
      left: -20px;
      border-radius: 0 3px 3px 0;
    }
    .action-icon {
      cursor: pointer;
      transition: all 0.3s;
      position: absolute;
      top: 50%;

      transform: translateY(-50%);
      z-index: 999;
      background-color: $--color-primary;
      width: 10px;
      height: 48px;
      &:hover {
        opacity: 0.7;
      }
    }
  }
  .table-warp{
    width:97%;
    padding: 10px;
    border: 1px solid #eee;
    .table-title{
      color: #1A1A1A;
      font-feature-settings: 'liga' off, 'clig' off;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: bold;
      line-height: normal;
      margin-bottom: 14px;
    }
  }
  .parameter-content{
    .parameter-warp{
        border-radius: 4px;
        background: #f7f9fc;
        padding: 0 14px;
        .warp-title{
           height: 56px;
           line-height: 56px;
           font-size: 14px;
           font-weight: bold;
           color: #1A1A1A;
        }
    }
  }
}

::v-deep .el-form-item__label{
  display: flex;
}
.column-tag {
  max-width: 150px;
  height: 30px;
  background: #f7bb61;
  border-radius: 2px;
  color: rgba(0, 0, 0, 0.6);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: inline-block;
  padding: 0 10px;
  height: 32px;
  line-height: 30px;
  font-size: 12px;
  border-radius: 4px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border: 1px solid rgba(64, 158, 255, 0.2);
  font-weight: bold;
}
</style>
