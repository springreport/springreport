<template>
    <div  class="_tablepage">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle">
        </searchForm>
       <cusTable  
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='pageData.tableCols' 
      :tableHandles='pageData.tableHandles'
      :tableData='pageData.tableData'
      :tablePage='pageData.tablePage'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
       <el-dialog title="参数设置" :visible.sync="pageData.paramDialog"  height="80%" top="20px" :close-on-click-modal='false' @close='closeParamDialog'>
            <taskReportForm  ref="reportRef" :reportName="''" :reportForm="pageData.reportForm" :searchData="pageData.searchData">
            </taskReportForm>
             <span slot="footer" class="dialog-footer">
                <el-button @click="closeParamDialog" size="small">取 消</el-button>
                <el-button type="primary" @click="confirmParams" size="small">确 定</el-button>
            </span>
       </el-dialog>

    </div>
</template>
<script src="./QrtzReportDetail.js"></script>