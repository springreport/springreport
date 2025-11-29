/*
 * @Description: luckysheetreport预览页面js
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-02-08 07:56:31
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-23 16:40:43
 */

// import VueOfficePdf from '@vue-office/pdf'
import headerReportForm from '../../components/reportForm/headerReportForm.vue'
export default {
    components:{
        // VueOfficePdf
        headerReportForm
    },
    data() {
        return{
            searchFormType:"1",//查询条件组件类型 1在页面头部(查询条件较少，对显示范围影响小的情况适用) 2侧边栏弹出(查询条件多，放在表头影响显示范围建议使用该种方式)
            drawer:false,
            showReportSql:false,
            reportSqls:{},
            reportDialogVisiable:false,
            reportForm:[],
            showReportForm:false,
            tplName:"",
            loadingText:"",
            loading:false,
            searchData:{
                params:[],
            },//参数
            showSearch:true,//是否隐藏查询条件
            docxUrl: '', //docx文档地址
            pdfUrl: '', //pdf文档地址
            activitiName:"",
            fileId:"",
            apiHeaders:[],
            isParamMerge:"2",
        }
    },
    mounted(){
        this.getReportParam();
     },
    methods:{
        //获取报表参数
        getReportParam(){
            let tplId = this.$route.query.tplId;
            let urlParamsLength = 0;
            if(this.$route.query)
            {
                let urlParams = { ...this.$route.query}
                delete urlParams['tplId']
                delete urlParams['token']
                urlParamsLength = Object.keys(urlParams).length
            }
            let obj = {
                url:this.apis.previewReport.getPreviewReportParamApi,
                params:{tplId:tplId,reportType:2},
            }
            let headers = {};
            if(this.isShare == 1)
            {
                headers.shareCode = this.shareCode;
                headers.shareUser = this.shareUser;
                obj.url = this.apis.previewReport.getSharePreviewReportParamApi
            }
            this.searchHandle=[
                // {label:'查询',drawerBtn: true, icon:'el-icon-search',type:'primary',handle:()=>this.getReportData(),size:'small'},
                // {label:'重置',drawerBtn: true, icon:'el-icon-refresh-left',type:'',handle:()=>this.resetSearch(),size:'small'},
                // {label:'导出word',icon:'icon-word',type:'success',handle:()=>this.downLoadDoc(1),size:'small'},
            ];
            var that = this;
            this.commonUtil.doPost(obj,headers) .then(response=>{
                if (response.code == "200")
                {
                    let result = response.responseData.params;
                    this.searchData.params = [];
                    that.apiHeaders = response.responseData.apiHeaders;
                    this.searchFormType = response.responseData.searchFormType
                    if(this.searchFormType == "1"){
                        this.searchHandle=[
                            { btnType: 'button',label: '查询', iconClass: 'action-icon-search', handle: () => this.getReportData(), size: 'mini' },
                            { btnType: 'button',label: '重置',  iconClass: 'action-icon-refresh', handle: () => this.resetSearch(), size: 'mini' },
                            {btnType: 'button', label: '导出word', iconClass: 'iconfont icon-daochuword',handle: () => this.downLoadDoc(1), size: 'mini'}
                        ];

                    }else{
                        this.searchHandle=[
                            { label: '查询', drawerBtn: true, iconClass: 'icon-search', type: 'primary', handle: () => this.getReportData(), size: 'mini' },
                            { label: '重置', drawerBtn: true, iconClass: 'icon-refresh', type: '', handle: () => this.resetSearch(), size: 'mini' },
                            {btnType: 'button', label: '导出word', iconClass: 'iconfont icon-daochuword',handle: () => this.downLoadDoc(1), size: 'mini'}
                        ];
                    }
                    for(let i = 0;i<result.length;i++)
                    {
                        var dataSet = {};
                        dataSet.datasetId = result[i].datasetId;
                        dataSet.datasetName = result[i].datasetName;
                        dataSet.datasourceId = result[i].datasourceId;
                        dataSet.params = [];
                        let tempParams = {};
                        for(let m = 0;m<result[i].params.length;m++){
                            var param = {};
                            if(result[i].params[m].paramType == "mutiselect" || result[i].params[m].paramType == "multiTreeSelect")
                            {
                                var data = new Array();
                                if(this.isDrillBack == 1)
                                {
                                    if(this.parentParams && this.parentParams[dataSet.datasetId] && this.parentParams[dataSet.datasetId][result[i].params[m].paramCode])
                                    {
                                        data =  this.parentParams[dataSet.datasetId][result[i].params[m].paramCode];
                                    }
                                }else if(this.isDrill == 1 && this.isDrillBack == 2 && this.drillParams)
                                {
                                    if(this.drillParams[result[i].params[m].paramCode])
                                    {
                                        if(this.drillParams[result[i].params[m].paramCode] instanceof Array)
                                        {
                                            data = this.drillParams[result[i].params[m].paramCode]
                                        }else{
                                            data.push(this.drillParams[result[i].params[m].paramCode]);
                                        }
                                    }else{
                                        if(result[i].params[m].paramDefault != null && result[i].params[m].paramDefault != "")
                                        {
                                            data = result[i].params[m].paramDefault.split(",");
                                        } 
                                    }
                                }else{
                                    if(this.$route.query[result[i].params[m].paramCode])
                                    {
                                        data = this.$route.query[result[i].params[m].paramCode].split(",");
                                    }else{
                                        if(result[i].params[m].paramDefault != null && result[i].params[m].paramDefault != "")
                                        {
                                            data = result[i].params[m].paramDefault.split(",");
                                        }
                                    }
                                }
                                param[result[i].params[m].paramCode] = data;
                            }else{
                                if(this.$route.query[result[i].params[m].paramCode])
                                    {
                                        param[result[i].params[m].paramCode]=this.$route.query[result[i].params[m].paramCode]
                                        if(result[i].params[m].paramType == "select")
                                        {
                                            let relyOnParams = result[i].params[m].relyOnParams;
                                            let isRelyOnParams = result[i].params[m].isRelyOnParams;
                                            if(isRelyOnParams == 1 && (this.$route.query[relyOnParams] || tempParams[relyOnParams])){
                                                that.getRelyOnParamys(result[i].params[m],this.$route.query[relyOnParams]?this.$route.query[relyOnParams]:tempParams[relyOnParams]);
                                            }
                                        }
                                    }else{
                                        if(result[i].params[m].paramDefault != null && result[i].params[m].paramDefault != "")
                                        {
                                            param[result[i].params[m].paramCode] = result[i].params[m].paramDefault
                                        }else{
                                            param[result[i].params[m].paramCode] = "";
                                        }
                                        if(result[i].params[m].paramType == "select")
                                        {
                                            let relyOnParams = result[i].params[m].relyOnParams;
                                            let isRelyOnParams = result[i].params[m].isRelyOnParams;
                                            if(isRelyOnParams == 1 && (this.$route.query[relyOnParams] || tempParams[relyOnParams])){
                                                that.getRelyOnParamys(result[i].params[m],this.$route.query[relyOnParams]?this.$route.query[relyOnParams]:tempParams[relyOnParams]);
                                            }
                                        }
                                    }
                            }
                            param.paramCode =  result[i].params[m].paramCode;
                            param.dateFormat = result[i].params[m].dateFormat;
                            param.paramHidden = result[i].params[m].paramHidden;
                            param.paramDefault = result[i].params[m].paramDefault;
                            param.paramType = result[i].params[m].paramType;
                            param.checkStrictly = result[i].params[m].checkStrictly;
                            param.paramPrefix = result[i].params[m].paramPrefix;
                            if(!tempParams[result[i].params[m].paramCode]){
                                tempParams[result[i].params[m].paramCode] = result[i].params[m].paramDefault;
                            }
                            dataSet.params.push(param);
                        }
                        that.searchData.params.push(dataSet);
                    }
                    if(result && result.length > 0)
                    {
                        this.activitiName = result[0].datasetName;
                    }
                    that.reportForm = result;
                    this.isParamMerge = response.responseData.isParamMerge + '';
                    that.showReportForm = true;
                    that.$nextTick(() => {
                        this.getReportData(1);
                    });
                }
            });
        },
        getRelyOnParamys(item,relyOnValue){
            var params = {
                selectContent: item.selectContent,
                datasourceId: item.datasourceId,
                params: {},
              };
              params.params[item.relyOnParams] = relyOnValue;
              var obj = {
                url: "/api/reportTplDataset/getRelyOnData",
                params: params,
              };
              this.commonUtil.doPost(obj).then((response) => {
                if (response.code == "200") {
                  item.selectData = response.responseData;
                }
              });
        },
        resetSearch:function(){
            this.getReportParam();
        },
        getReportData(isInit){
            var that = this;
            // 项目初始化的时候 不进行this.$refs['reportRef'].$refs['reportFormRef'] 因为此时抽屉组件未加载
            if (isInit == 1) {
                that.sendReportDataRequest(isInit)
                that.drawer = false;
                return
            }
            this.$refs['reportRef'].$refs['reportFormRef'].validate((valid) => {
                if (valid) {
                    that.sendReportDataRequest(isInit)
                    that.drawer = false;
                }else{
                    that.commonUtil.showMessage({message:that.commonUtil.getMessageFromList("error.search.param",null),type: that.commonConstants.messageType.error})
                    that.loading = false;
                    return false;
                }
            });
        },
        sendReportDataRequest(isInit) {
            const that = this
            let apiHeaders = {};
            if(that.apiHeaders && that.apiHeaders.length > 0){
                for (let index = 0; index < that.apiHeaders.length; index++) {
                    const element = that.apiHeaders[index];
                    if(that.$route.query[element]){
                        apiHeaders[element] = that.$route.query[element];
                    }
                }
            }
            let tplId = that.$route.query.tplId;
            let obj = {
                url:that.apis.docTpl.previewDocApi,
                params:{tplId:tplId,searchData:that.commonUtil.processPageParam(that.searchData.params),fileId:that.fileId,apiHeaders:apiHeaders},
            }
            let headers = {};
            if(that.isShare == 1)
            {
                headers.shareCode = that.shareCode;
                headers.shareUser = that.shareUser;
                obj.url = that.apis.docTpl.previewDocApi
            }
            that.loadingText = "文档生成中...";
            that.loading = true;
            that.commonUtil.doPost(obj,headers) .then(response=>{
                if (response.code == "200")
                {
                    that.showReportSql = response.responseData.showReportSql;
                    that.reportSqls = response.responseData.reportSqls;
                    that.tplName = response.responseData.tplName;
                    that.docxUrl = response.responseData.docxUrl;
                    const iframe = document.getElementById('pdfIframe');
                    iframe.src = response.responseData.pdfUrl;
                    that.fileId = response.responseData.fileId;
                    that.loading = false;
                }else{
                    that.loading = false;
                }
            });
        },
        closeLoading(){
            this.loading = false;
        },
        showSearchClick(){
            this.showSearch = true;
        },
        showSql(){
            this.reportDialogVisiable = true;
        },
        rendered(){
            console.log("渲染完成")
        },
        downLoadDoc(type){
            window.open(this.docxUrl);
        },
        searchClick(){
            this.drawer = true;
        },
        closeSearch(){
          this.drawer = false;
        }
    }
}
