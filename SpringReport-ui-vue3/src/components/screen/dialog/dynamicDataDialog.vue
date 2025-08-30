<template>
  <div>
    <el-dialog
      title="动态数据"
      :modelValue="dynamicDialogVisiable"
      width="30%"
      :close-on-click-modal="false"
      @close="closeDynamicDataDialog"
    >
      <el-form
        class="demo-form-inline"
        :model="dataSetForm"
        label-position="top"
        label-width="auto"
        ref="dataSetForm"
      >
      <div class="df-c-b" style="flex: 1;padding-left:380px">
              <div>
                <el-button type="text" @click="showAddDatasetDialog(component)"
                  >添加数据集</el-button
                >
                <el-button type="text" @click="datasetEditDialog(component)">管理数据集</el-button>
              </div>
            </div>
        <el-form-item
          prop="dataSetId"
          label="数据列"
          :rules="filter_rules('返回值类型', { required: true })"
        >
          <el-select v-model="dataSetForm.dataSetId" placeholder="请选择" @change="changeDataset" filterable clearable>
            <el-option
              v-for="item in dataSets"
              :key="item.id"
              :label="item.datasetName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="数据列"
          prop="column"
          :rules="filter_rules('数据列', { required: true })"
        >
          <el-select
            v-model="dataSetForm.column"
            placeholder="请选择"
            multiple
            style="width: 200px"
            filterable clearable
          >
            <el-option
              v-for="item in dataColumn"
              :key="item.name"
              :label="item.name"
              :value="item.name"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDynamicDataDialog()">取 消</el-button>
          <el-button type="primary" @click="dataSetConfirm()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="数据集"
      width="82%"
      v-model="addDatasetsDialogVisiable"
      :close-on-click-modal="false"
      class="add-dataset-dialog"
      @close="closeAddDataSet"
    >
      <el-form :inline="true" :model="sqlForm" class="demo-form-inline" ref="sqlRef">
        <el-form-item
          label="数据集名称"
          prop="datasetName"
          :rules="filter_rules('数据集名称', { required: true })"
        >
          <el-input v-model="sqlForm.datasetName" placeholder="数据集名称"></el-input>
        </el-form-item>
        <el-form-item
          label="选择数据源"
          prop="datasourceId"
          :rules="filter_rules('选择数据源', { required: true })"
          style="width: 240px"
        >
          <el-select
            v-model="sqlForm.datasourceId"
            placeholder="选择数据源"
            @change="changeDatasource"
            filterable clearable
          >
            <el-option
              v-for="op in dataSource"
              :label="op.dataSourceName"
              :value="op.datasourceId"
              :key="op.datasourceId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
            label="查询集合(表)"
            prop="mongoTable"
            :rules="filter_rules('查询集合(表)', { required: true })"
            v-if="datasourceType == 3"
            style="width: 240px"
          >
            <el-select
              v-model="sqlForm.mongoTable"
              placeholder="查询集合(表)"
              filterable clearable
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
            style="width: 260px"
          >
            <el-select
              v-model="sqlForm.mongoSearchType"
              placeholder="查询方式"
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
                <icon-copy title="复制" @click="doCopy(item)" />
                <icon-add-one title="添加" style="margin-left: 4px" @click="doCopy(item, true)" />
              </div>
            </div>
          </div>
        </div>
        <div class="sql-content">
          <div v-if="datasourceType == 1 || datasourceType == 3" style="height: 25px">
            <el-tooltip
              content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中"
              placement="bottom"
              ><el-tag type="success" @click="execSql" style="cursor: pointer"
                ><icon-play />执行</el-tag
              ></el-tooltip
            >
            <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"
              ><el-tag @click="formatSql" style="cursor: pointer"  v-if="datasourceType == 1"
                ><icon-align-left-one />格式化</el-tag
              >
            </el-tooltip>
            <el-tooltip content="该操作会插入注释标签" placement="right"
              ><el-tag @click="addComment(' <!--  -->')" type="warning" style="cursor: pointer" v-if="datasourceType == 1"
                ><icon-add-one />添加注释</el-tag
              >
            </el-tooltip>
          </div>

          <div v-if="datasourceType == '1'  || datasourceType == '3'" style="height: 300px">
            <div style="height: 100%; width: 100%"   v-if="datasourceType == 1">
              <codemirror ref="codeMirror" :options="cmOptions" v-model:value="sqlText" />
            </div>
            <div v-if="datasourceType == 3" style="height: 300px">
              <div :style="{height: '100%',width: sqlForm.mongoSearchType == 1?'50%':'100%',float:'left'}" v-if="datasourceType == 3">
                <codemirror ref="codeMirror" :options="cmOptions" v-model:value="sqlText"/>
              </div>
              <div style="height: 100%; width: 50%;float:right" v-if="datasourceType == 3 && sqlForm.mongoSearchType == 1">
                <codemirror ref="orderCodeMirror" :options="cmOptions" v-model:value="orderSql"/>
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
                  sqlColumnTableData.tablePage.currentPage * sqlColumnTableData.tablePage.pageSize
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
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeAddDataSet" size="small">取 消</el-button>
          <el-button type="primary" @click="addDataSet" size="small">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="数据集管理"
      v-model="showDatasetsDialog"
      :close-on-click-modal="false"
      width="800px"
    >
      <section class="ces-table">
        <el-table :data="dataSets" border style="width: 100%">
          <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
          <el-table-column prop="datasetName" label="数据集" align="center"> </el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button size="small" type="primary" @click="editDatasets(scope.$index, scope.row)"
                >编辑</el-button
              >
              <el-button size="small" type="danger" @click="deleteDatasets(scope.$index, scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </section>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDatasetsDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    <textarea id="clipboradInput" value="" style="opacity: 0; position: absolute" />
  </div>
</template>
<script src="./dynamicDataDialog.js"></script>
<style scoped lang="scss">
  @import '@/element-variables.scss';

  .add-dataset-dialog {
    ::v-deep .el-radio-button:first-child .el-radio-button__inner {
      border-radius: 10px 0 0 10px;
    }

    ::v-deep .el-radio-button:last-child .el-radio-button__inner {
      border-radius: 0 10px 10px 0;
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
        border-bottom: 1px solid #efebeb;
      }

      .variable-warp {
        padding: 9px 17px;

        .variable-warp-title {
          color: #979191;
          font-size: 12px;
          font-style: normal;
          font-weight: bold;
          line-height: 22px;
          /* 183.333% */
          margin-bottom: 12px;
        }

        .variable-list {
          flex-wrap: wrap;

          .variable-item {
            width: 100%;
            box-sizing: border-box;
            padding: 0 10px;
            border-radius: 4px;
            background: #e1f2f0;
            height: 32px;
            line-height: 32px;
            color: #595959;
            font-size: 12px;
            transition: all 0.3s;
            margin-bottom: 12px;
            cursor: pointer;

            &:hover {
              color: #fff;
              background: $base-color-primary;

              ::v-deep .el-icon-circle-plus-outline {
                color: #fff;
              }
            }

            &:nth-child(3n) {
              margin-right: 0;
            }
          }
        }

        .analysis-list {
          border-radius: 3px;
          border: 1px solid #c1e0d9;
          background: #fff;
          padding: 8px 10px 0;

          .variable-item {
            border-radius: 4px;
            background: #f1f2f3;
          }
        }
      }
    }

    .sql-content {
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
        background-color: $base-color-primary;
        width: 10px;
        height: 48px;

        &:hover {
          opacity: 0.7;
        }
      }
    }

    .table-warp {
      padding: 10px;
      border: 1px solid #eee;

      .table-title {
        color: #1a1a1a;
        font-feature-settings: 'liga' off, 'clig' off;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: bold;
        line-height: normal;
        margin-bottom: 14px;
      }
    }

    .parameter-content {
      .parameter-warp {
        border-radius: 4px;
        background: #f7f9fc;
        padding: 0 14px;

        .warp-title {
          height: 56px;
          line-height: 56px;
          font-size: 14px;
          font-weight: bold;
          color: #1a1a1a;
        }
      }
    }
  }
</style>
