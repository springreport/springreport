<template>
    <div  class="_tablepage">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle" :tableHandles="pageData.tableHandles">
        </searchForm>
       <cusTable  
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :loading="tableLoading"
      :tableCols='pageData.tableCols' 
      :tableData='pageData.tableData'
      :tablePage='pageData.tablePage'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
       <el-dialog title="参数设置" v-model="pageData.paramDialog"  top="20px" width="1000px" :close-on-click-modal='false' @close='closeParamDialog'>
            <taskReportForm  ref="reportRef" :reportName="''" :reportForm="pageData.reportForm" :searchData="pageData.searchData">
            </taskReportForm>
            <template #footer>
             <span class="dialog-footer">
                <el-button @click="closeParamDialog" size="small">取 消</el-button>
                <el-button type="primary" @click="confirmParams" size="small">确 定</el-button>
            </span>
            </template>
       </el-dialog>

    </div>
</template>
<script src="./QrtzReportDetail.js"></script>