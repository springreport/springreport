<template>
  <div>
    <el-dialog
      title="动态数据"
      :visible="dynamicDialogVisiable"
      width="800px"
      :close-on-click-modal="false"
      @close="closeDynamicDataDialog"
    >
      <el-form
        class="demo-form-inline"
        :model="dataSetForm"
        label-position="left"
        label-width="auto"
        size="mini"
        ref="dataSetForm"
      >
        <el-form-item
          label="数据集"
          prop="dataSetId"
          :rules="filter_rules('返回值类型', { required: true })"
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
            >
            </el-option>
          </el-select>
          &nbsp;<el-button type="text" @click="showAddDatasetDialog(component)"
            >添加数据集</el-button
          >
          &nbsp;<el-button type="text" @click="datasetEditDialog(component)"
            >管理数据集</el-button
          >
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
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDynamicDataDialog()" size="mini"
          >取 消</el-button
        >
        <el-button type="primary" @click="dataSetConfirm()" size="mini"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="数据集"
      :visible.sync="addDatasetsDialogVisiable"
      :close-on-click-modal="false"
      @close="closeAddDataSet"
    >
      <el-form
        :inline="true"
        :model="sqlForm"
        class="demo-form-inline"
        ref="sqlRef"
        size="mini"
      >
        <el-form-item
          label="数据集名称"
          prop="datasetName"
          :rules="filter_rules('数据集名称', { required: true })"
        >
          <el-input
            v-model="sqlForm.datasetName"
            placeholder="数据集名称"
          ></el-input>
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
              :label="op.dataSourceName"
              :value="op.datasourceId"
              :key="op.datasourceId"
            ></el-option>
          </el-select>
        </el-form-item><br>
        <el-form-item  label="系统变量">
          <p class="column-tag" v-for="(item,index) in commonConstants.systemParam" :key="index" ><i class="el-icon-copy-document" title="复制" @click="doCopy(item)">{{item.label}}({{item.value}})</i></p> 
        </el-form-item>
      </el-form>
      <el-button-group v-if="datasourceType == '1'">
        <el-button type="primary" size="mini" @click="execSql"
          >执 行<i class="el-icon-caret-right"></i
        ></el-button>
        <el-button type="primary" size="mini" @click="formatSql"
          >格式化<i class="el-icon-document"></i
        ></el-button>
      </el-button-group>
      <div style="height: 3px"></div>
      <div style="height: 300px" v-if="datasourceType == '1'">
        <codemirror ref="codeMirror" :options="cmOptions"></codemirror>
      </div>
      <div style="height: 3px"></div>
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
        size="mini"
        max-height="230px"
        :cell-style="{color:'#fff'}"
      >
        <el-table-column
          prop="name"
          label="列名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dataType"
          label="数据类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="width"
          label="宽度"
          align="center"
        ></el-table-column>
      </el-table>
      <!--表格 end-->
      <!--分页 start-->
      <el-pagination
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :current-page="sqlColumnTableData.tablePage.currentPage"
        :page-sizes="sqlColumnTableData.tablePage.pageSizeRange"
        :page-size="sqlColumnTableData.tablePage.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="sqlColumnTableData.tablePage.pageTotal"
      >
      </el-pagination>
      <!--分页 end-->
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAddDataSet" size="mini">取 消</el-button>
        <el-button type="primary" @click="addDataSet" size="mini"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="数据集管理"
      :visible.sync="showDatasetsDialog"
      :close-on-click-modal="false"
      width="800px"
    >
      <section class="ces-table">
        <el-table :data="dataSets" border style="width: 100%" :cell-style="{color:'#fff'}">
          <el-table-column
            type="index"
            label="序号"
            align="center"
            width="50"
          ></el-table-column>
          <el-table-column prop="datasetName" label="数据集" align="center">
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editDatasets(scope.$index, scope.row)"
                >编辑</el-button
              >
              <el-button
                size="mini"
                type="danger"
                @click="deleteDatasets(scope.$index, scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showDatasetsDialog = false">关闭</el-button>
      </span>
    </el-dialog>
    <textarea id="clipboradInput" value="" style="opacity:0;position:absolute" />
  </div>
</template>
<script src="./dynamicDataDialog.js">

</script>
<style scoped>
.column-tag{
    max-width:150px;
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
    border: 1px solid rgba(64,158,255,.2);
    font-weight: bold;
}
</style>