<template>
<el-scrollbar height="100vh" ref="scroll">
  <div class="index" v-loading="loading" element-loading-text="数据加载中">
    <!-- 导航栏 -->
    <van-nav-bar
      :title="reportName"
      @click-left="onClickLeft"
      @click-right="onClickRight"
    >
      <template #left v-if="isDrill == 1">
        <img class="back" src="@/assets/img/back.png" width="24px" height="24px" />
      </template>
      <template #right>
        <img class="filter"
          src="@/assets/img/filter.png"
          width="18px"
          height="18px"
          @click="openFilter"
        />
      </template>
    </van-nav-bar>
    <!-- sheet信息 -->
    <div class="sheet-list df-c">
      <div
        class="sheet-item"
        :class="{ 'sheet-item-active': item.sheetIndex == activeSheet }"
        v-for="item in sheetList"
        :key="item.sheetIndex"
        @click="changeSheet(item)"
      >
        {{ item.sheetName }}
      </div>
    </div>
    <!-- table或图表 -->
    <div class="content" v-if="currentSheetInfo">
      <template v-if="activeBottom == 1">
        <ReportTable :info="currentSheetInfo.table" :imgs="currentSheetInfo.images" :drillCells="currentSheetInfo.drillCells" @drill="drill"/>
      </template>
      <template v-else>
        <ReportChart :info="currentSheetInfo.chartsOptions" />
      </template>
    </div>
    <div style=" margin-top: 14px" v-if="activeBottom == 1 && sheetPagination[activeSheet]">
      <div class="pagination df-c-b">
      <el-pagination size="small" background layout="prev, pager, next,total" :total="pagination.total*1"  
      @current-change="changeCurrentPage" :page-size="pagination.pageSize" 
      :current-page="pagination.currentPage"
      :hide-on-single-page="false" :pager-count="11"/>
      </div>
    </div>
    <!-- 底部切换 如果只有图标或者table其中一项则不显示-->
    <div
      class="bottom-tab df-c"
    >
      <div
        class="tab-item"
        :class="{ 'tab-item-active': activeBottom == 1 }"
        @click="changBottomActive(1)"
      >
        表格内容
      </div>
      <div
        class="tab-item"
        :class="{ 'tab-item-active': activeBottom == 2 }"
        @click="changBottomActive(2)"
      >
        图表内容
      </div>
    </div>
    <!-- 此处根据数据集切换  暂时写死测试 -->
    <ReportFilter
      ref="ReportFilterRef"
      :info="reportForm[0]?reportForm[0]:{params:[]}"
      :searchData="searchData.params[0]"
      @search="search"
    />
  </div>
</el-scrollbar>
</template>

<script>
import ReportTable from "./components/ReportTable.vue";
import ReportChart from "./components/ReportChart.vue";
import ReportFilter from "./components/ReportFilter.vue";

export default {
  name: "index",
  data() {
    return {
      sheetPagination:{},
      activeSheet: "",
      sheetList: [],
      activeBottom: 1,
      reportForm:[],
      searchData: {
        params: [],
      },
      pagination: {
        //分页信息
        currentPage: 1,
        pageSize: 20,
        total: 101,
      },
      currentTplId: null,
      reportName:"",//报表标题
      pageParam:{
          currentPage:"1",//当前页数
          pageCount:"",//每页显示条数
      },
      isPagination:false,//是否分页
      isShare:false,//是否是分享链接
      shareCode:"",
      shareUser:"",
      loading:true,
      isDrill:2,//当前展示报表是否是下钻报表 1是 2否
      isDrillBack:2,//是否是下钻报表返回 1是 2否
      drillParams:{},//下钻参数
      parentParams:{},//上级报表参数
      drillRelations:{},
    };
  },
  components: {
    ReportTable,
    ReportChart,
    ReportFilter,
  },
  watch: {},
  computed: {
    // 当前选中的参数
    // 当前选中的sheet
    currentSheetInfo() {
      return this.sheetList && this.sheetList.length
        ? this.sheetList.find((item) => item.sheetIndex == this.activeSheet)
        : {};
    },
  },
  mounted() {
    var shareCode = this.$route.query.shareCode;
    var shareUser = this.$route.query.shareUser;
    if(shareCode && shareUser)
    {
      this.isShare = true;
      this.shareCode = shareCode;
      this.shareUser = shareUser;
    }else{
      this.isShare = false;
      this.shareCode = "";
      this.shareUser = "";
    }
    this.currentTplId = this.$route.query.tplId;
    this.getReportDatasetsParam();
    // this.getMobileReport();
  },
  methods: {
    // 打开搜索框
    openFilter() {
      this.$refs["ReportFilterRef"].openDrawer();
    },
    // 点击回到上一页
    onClickLeft() {
      // this.$route.go(-1);
      this.back();
    },
    // 点击打开右侧
    onClickRight() {
      this.$refs["ReportFilterRef"].openDrawer();
    },
    // tab发生改变时
    // activeChange(name, title) {
    //   this.activeName = name;
    //   this.tabIndex = name;
    //   // 调用接口
    // },
    // 切换sheet表的时候  清除筛选条件
    changeSheet(item) {
      // this.$refs["ReportFilterRef"].resetForm();
      // // this.searchData = {};
      // this.pagination.currentPage = 1;
      this.activeSheet = item.sheetIndex;
      if(this.sheetPagination[this.activeSheet])
      {
        this.pagination.currentPage = this.sheetPagination[this.activeSheet].currentPage*1;
        this.pagination.pageSize = this.sheetPagination[this.activeSheet].pageCount*1;
        this.pagination.total = this.sheetPagination[this.activeSheet].totalCount*1;
     }
    },
    // 底部表格或者图表显示切换
    changBottomActive(val) {
      this.activeBottom = val;
    },
    // 获取报表参数
    getReportDatasetsParam() {
      var obj = {
        url: this.apis.previewReport.getPreviewReportParamApi,
        params: { tplId: this.currentTplId,isMobile:1,initSelectData:true},
      };
      let headers = {};
      if(this.isShare == 1)
      {
        headers.shareCode = this.shareCode;
        headers.shareUser = this.shareUser;
        obj.url = this.apis.previewReport.getSharePreviewReportParamApi
      }
      var that = this;
      this.commonUtil.doPost(obj,headers).then((response) => {
        if (response.code == "200") {
          try {
            let result = response.responseData.params;
            that.searchData.params = [];
            let isPagination = response.responseData.isPagination; //是否分页
            for (let i = 0; i < result.length; i++) {
              var dataSet = {};
              dataSet.datasetId = result[i].datasetId;
              dataSet.datasetName = result[i].datasetName;
              dataSet.datasourceId = result[i].datasourceId;
              dataSet.params = [];
              for (let m = 0; m < result[i].params.length; m++) {
                var param = {};
                if (result[i].params[m].paramType == "mutiselect" || result[i].params[m].paramType == "multiTreeSelect") {
                  var data = new Array();
                  if (that.isDrillBack == 1) {
                    if (
                      that.parentParams &&
                      that.parentParams[dataSet.datasetId] &&
                      that.parentParams[dataSet.datasetId][
                        result[i].params[m].paramCode
                      ]
                    ) {
                      data =
                        that.parentParams[dataSet.datasetId][
                          result[i].params[m].paramCode
                        ];
                    }
                  } else if (
                    that.isDrill == 1 &&
                    that.isDrillBack == 2 &&
                    that.drillParams
                  ) {
                    if (that.drillParams[result[i].params[m].paramCode]) {
                      if (
                        that.drillParams[result[i].params[m].paramCode] instanceof
                        Array
                      ) {
                        data = that.drillParams[result[i].params[m].paramCode];
                      } else {
                        data.push(
                          that.drillParams[result[i].params[m].paramCode]
                        );
                      }
                    } else {
                      if (
                        result[i].params[m].paramDefault != null &&
                        result[i].params[m].paramDefault != ""
                      ) {
                        data = result[i].params[m].paramDefault.split(",");
                      }
                    }
                  } else {
                    if (that.$route.query[result[i].params[m].paramCode]) {
                      data.push(that.$route.query[result[i].params[m].paramCode]);
                    } else {
                      if (
                        result[i].params[m].paramDefault != null &&
                        result[i].params[m].paramDefault != ""
                      ) {
                        data = result[i].params[m].paramDefault.split(",");
                      }
                    }
                  }
                  param[result[i].params[m].paramCode] = data;
                } else {
                  if (that.isDrillBack == 1) {
                    if (
                      that.parentParams &&
                      that.parentParams[dataSet.datasetId] &&
                      that.parentParams[dataSet.datasetId][
                        result[i].params[m].paramCode
                      ]
                    ) {
                      param[result[i].params[m].paramCode] =
                        that.parentParams[dataSet.datasetId][
                          result[i].params[m].paramCode
                        ];
                    }
                  } else if (
                    that.isDrill == 1 &&
                    that.isDrillBack == 2 &&
                    that.drillParams
                  ) {
                    if (that.drillParams[result[i].params[m].paramCode]) {
                      param[result[i].params[m].paramCode] =
                        that.drillParams[result[i].params[m].paramCode];
                    } else {
                      if (
                        result[i].params[m].paramDefault != null &&
                        result[i].params[m].paramDefault != ""
                      ) {
                        param[result[i].params[m].paramCode] =
                          result[i].params[m].paramDefault;
                      } else {
                        param[result[i].params[m].paramCode] = "";
                      }
                    }
                  } else {
                    if (that.$route.query[result[i].params[m].paramCode]) {
                      param[result[i].params[m].paramCode] =
                        that.$route.query[result[i].params[m].paramCode];
                    } else {
                      if (
                        result[i].params[m].paramDefault != null &&
                        result[i].params[m].paramDefault != ""
                      ) {
                        param[result[i].params[m].paramCode] =
                          result[i].params[m].paramDefault;
                      } else {
                        param[result[i].params[m].paramCode] = "";
                      }
                    }
                  }
                }
                param.paramCode =  result[i].params[m].paramCode;
                param.dateFormat = result[i].params[m].dateFormat;
                param.paramHidden = result[i].params[m].paramHidden;
                param.paramDefault = result[i].params[m].paramDefault;
                param.paramType = result[i].params[m].paramType;
                dataSet.params.push(param);
              }
              that.searchData.params.push(dataSet);
            }
            that.reportForm = result;
            that.isPagination = isPagination;
            if(response.responseData.isPagination)
            {
              that.pageParam.currentPage = response.responseData.pagination.currentPage;
              that.pageParam.pageCount = response.responseData.pagination.pageCount;
            }
            that.$nextTick(() => {
              that.getMobileReport();
            });
          } catch (error) {
            this.loading = false;
          }
        }else{
          this.loading = false;
        }
      });
    },
    // 获取手机端报表信息
    getMobileReport() {
      var that = this;
      this.$refs['ReportFilterRef'].$refs['ruleForm'].validate((valid) => {
         if (valid) {
            that.loading = true;
            var obj = {
              url: that.apis.previewReport.getMobileReportApi,
              params: {tplId:that.currentTplId,searchData:that.searchData.params,pagination:that.pageParam,activeSheet:that.activeSheet},
            };
            let headers = {};
            if(that.isShare == 1)
            {
              headers.shareCode = that.shareCode;
              headers.shareUser = that.shareUser;
              obj.url = that.apis.previewReport.getMobileShareReportApi
            }
            that.commonUtil.doPost(obj,headers).then((response) => {
              if (response.code == "200")
              {
                try {
                  that.reportName = response.responseData.reportName;
                  that.sheetList = response.responseData.reports;
                  if(response.responseData.activeSheet){
                      that.activeSheet = response.responseData.activeSheet;
                  }else{
                      that.activeSheet = that.sheetList[0].sheetIndex;
                  }
                  for (let index = 0; index < that.sheetList.length; index++) {
                    const element = that.sheetList[index];
                    if(element.mergePagination){
                      that.sheetPagination[element.sheetIndex] = element.mergePagination
                    }
                  }
                  if(response.responseData.pagination && that.sheetPagination[that.activeSheet])
                  {
                      that.pagination.currentPage = this.sheetPagination[that.activeSheet].currentPage*1;
                      that.pagination.pageSize = this.sheetPagination[that.activeSheet].pageCount*1;
                      that.pagination.total = this.sheetPagination[that.activeSheet].totalCount*1;
                  }
                  that.loading = false;
                } catch (error) {
                  that.loading = false;
                }
              }else{
                that.loading = false;
              }
            });
         }else{
            that.loading = false;
            that.commonUtil.showMessage({message:that.commonUtil.getMessageFromList("error.search.param",null),type: that.commonConstants.messageType.error})
            return false;
          }
      })
      
    },
    // 侧边划入的搜索
    search(data) {
        this.getMobileReport();
      // 获取接口
    },
    // 更改页码
    changeCurrentPage(val) {
      // 获取接口
      this.pageParam.currentPage = val;
      this.getMobileReport();
    },
    //报表下钻
    drill(drillId,drillParams){
      //获取当前报表的参数
      var parentId = this.currentTplId;//上一级的报表id
      var parentParams = {};//上一级参数
      if(this.searchData.params && this.searchData.params.length > 0)
      {
        for (let index = 0; index < this.searchData.params.length; index++) {
          const element = this.searchData.params[index];
          let datasetId = element.datasetId;
          let params = element.params;
          if(params && params.length > 0)
            {
              for (let t = 0; t < params.length; t++) {
                const param = params[t];
                for(var key in param) {
                  if(key && param[key])
                  {
                    if(!parentParams[datasetId])
                    {
                      parentParams[datasetId] = {}
                    }
                    parentParams[datasetId][key] = param[key];
                  }
                }
              }
            }
        }
      }
      var parentInfo = {
        parentId:parentId,
        parentParams:parentParams
      }
      this.drillRelations[drillId] = parentInfo;
      this.currentTplId = drillId;
      this.drillParams = drillParams;
      this.isDrill = 1;
      this.isDrillBack = 2;
      this.getReportDatasetsParam();
    },
    back(){
      //获取上一级报表信息
      this.isDrillBack = 1;
      var parent = this.drillRelations[this.currentTplId];
      this.parentParams = parent.parentParams;
      delete this.drillRelations[this.currentTplId];//删除下钻报表信息
      this.currentTplId = parent.parentId;//当前报表id更新
      if(!this.drillRelations[this.currentTplId])
      {//判断是否还有上级报表
          this.isDrill = 2;
      }
      this.getReportDatasetsParam();
    },
  },
};
</script>

<style scoped lang="scss">
.index {
  height: 100vh;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}
.back{
  width:24px !important;
  height:24px !important;
}
.filter{
  width:18px !important;
  height:18px !important;
}
.content {
  flex: 1;
  overflow-y: auto;
  padding: 0 16px;
}
.sheet-list {
  padding: 0 17px;
  margin-bottom: 12px;
  .sheet-item {
    width: fit-content;
    padding: 4px 12px;
    color: #677574;
    text-align: center;
    font-family: PingFang SC;
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    border-radius: 23px;
    background: #f5f5f5;
  }
  .sheet-item-active {
    background: #17b794;
    color: #fff;
  }
}
.pagination {
  padding: 8px 14px;
  border-bottom: 1px solid #f2f2f2;
  .total-info {
    text-align: center;
    .count {
      color: rgba(0, 0, 0, 0.85);
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 12px;
    }
    .label {
      color: rgba(0, 0, 0, 0.6);
      font-family: PingFang SC;
      font-size: 8px;
      font-style: normal;
      font-weight: 400;
      line-height: 12px; /* 150% */
    }
  }
  .quick-action {
    .pre {
      margin-right: 8px;
    }
    .action {
      justify-content: center;
      width: 24px;
      height: 24px;
      text-align: center;
      line-height: 24px;
      border-radius: 24px;
      background: #f5f7f9;
    }
    .action-disabled {
      background: #666;
    }
  }
}
:deep(.van-tabs__line) {
  bottom: 20px !important;
}
:deep(.el-form-item__error) {
  padding-top: 0px !important;
}
:deep(.el-pagination)
{
  width:100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.el-scrollbar :deep(.el-scrollbar__view){
    height:100% !important;
  }
.bottom-tab {
  margin-top: 12px;
  width: 100%;
  height: 40px;
  line-height: 40px;
  left: 0;
  bottom: 0;
  .tab-item {
    flex: 1;
    height: 40px;
    line-height: 40px;
    background: linear-gradient(
        0deg,
        rgba(0, 0, 0, 0.05) 0%,
        rgba(0, 0, 0, 0.05) 100%
      ),
      #f5f5f5;
    color: #000;
    text-align: center;
    font-family: PingFang SC;
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
  }
  .tab-item-active {
    color: #17b794;
    background: #d4f6ef;
  }
}
:deep(.xxbt_img) {
  width: 100%;
  height: auto;
}
</style>
