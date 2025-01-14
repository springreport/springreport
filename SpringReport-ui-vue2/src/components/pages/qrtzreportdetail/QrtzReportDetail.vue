<template>
    <div  class="_tablepage">
        <searchForm  :searchForm="pageData.searchForm" :searchData="pageData.queryData" :searchHandle="pageData.searchHandle" :table-handles="pageData.tableHandles">
        </searchForm>
       <cusTable  
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='pageData.tableCols' 
      :tableData='pageData.tableData'
      :tablePage='pageData.tablePage'
      @handleCurrentChange='searchtablelist()'
      @selectChange='selectChange'></cusTable>
       <modal ref="modalRef" :modalConfig='pageData.modalConfig' 
       :modalForm='pageData.modalForm' :modalData='pageData.modalData' 
       :modalHandles='pageData.modalHandles'
       @closeModal="closeModal()"></modal>
       <el-drawer title="参数设置" :visible.sync="pageData.paramDialog" :close-on-click-modal='false' @close='closeParamDialog' custom-class="handle-drawer"
      class="handle-drawer" size="36%">
            <taskReportForm  ref="reportRef" :reportName="''" :reportForm="pageData.reportForm" :searchData="pageData.searchData">
            </taskReportForm>
             <div class="handle-drawer__footer">
                <span class="dialog-footer">
                <el-button @click="closeParamDialog" size="small">取 消</el-button>
                <el-button type="primary" @click="confirmParams" size="small">确 定</el-button>
                </span>
            </div>
       </el-drawer>

    </div>
</template>
<script src="./QrtzReportDetail.js"></script>