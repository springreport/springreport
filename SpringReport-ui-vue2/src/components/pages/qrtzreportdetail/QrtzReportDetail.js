export default {
  name:'qrtzReportDetail',
  data() {
    return{
      pageData:{
        //查询表单内容 start
        searchForm:[
					{type:'Input',label:'任务名称',prop:'jobName'},
        ],
        //查询表单内容 end
        //查询条件 start
        queryData:{
					jobName:"",//任务名称 
					tplId:"",
        },
        //查询条件 end
        //查询表单按钮start
        searchHandle:[
          {label:'清除条件',type:'',handle:()=>this.resetSearch(),auth:'reportTask_search'},
          {label:'查询',type:'primary',handle:()=>this.searchtablelist(),auth:'reportTask_search'}
        ],
        //查询表单按钮end
        //表格数据start
        tableData:[],
        //表格数据end
        //表格工具栏按钮 start
        tableHandles:[
          {label:'新增',type:'primary',position: 'right', iconClass: 'action-icon-add',handle:()=>this.showModal(this.commonConstants.modalType.insert),auth:'reportTask_insert'},
          {label:'批量删除',position: 'left', iconClass: 'action-icon-del',type:'danger',handle:()=>this.deleteBatch(),auth:'reportTask_batchdelete'},
          {label: '返回', type: 'primary', position: 'left', iconClass: 'action-icon-back', handle: () => this.backTo(), auth: 'ignore',isHidden:true}
        ],
        //表格工具栏按钮 end
        selectList:[],//表格选中的数据
        //表格分页信息start
        tablePage:{
          currentPage: 1,
          pageSize:10,
          pageTotal: 0,
          pageSizeRange:[5, 10, 20, 50]
        },
        //表格分页信息end
        //表格列表头start
        tableCols:[
          {label:'操作',prop:'operation',align:'center', type: '', type: 'dropdown', width: 54,btnList:[
						{label:'查看',type:'text',auth:'reportTask_detail',handle:(row)=>this.showModal(this.commonConstants.modalType.detail,row.id)},
						{label:'编辑',type:'text',auth:'reportTask_edit',handle:(row)=>this.showModal(this.commonConstants.modalType.update,row.id)},
            {label:'立即执行',type:'text',auth:'reportTask_runTask',handle:(row)=>this.runTask(row.id)},
            {label:'暂停任务',type:'text',auth:'reportTask_pause',show:(row)=>this.isShowPause(row),handle:(row)=>this.pauseTask(row.id)},
            {label:'恢复任务',type:'text',auth:'reportTask_resume',show:(row)=>this.isShowResume(row),handle:(row)=>this.resumeTask(row.id)},
						{label:'删除',type:'text',auth:'reportTask_delete',handle:(row)=>this.deleteOne(row.id)},
					]},
					{label:'任务名称',prop:'jobName',align:'center'},
					{label:'任务执行时间',prop:'jobCron',align:'center'},
					{label:'任务参数',prop:'jobData',align:'center',overflow:true},
          {label:'上次执行时间',prop:'previousFireTime',align:'center',overflow:true},
          {label:'下次执行时间',prop:'nextFireTime',align:'center',overflow:true},
          {label:'任务状态',prop:'jobStatus',align:'center',overflow:true,formatter:this.commonUtil.getTableCodeName,codeType:'jobStatus'},
					{label:'发送邮箱',prop:'email',align:'center'},
        ],
        //表格列表头end
        //modal配置 start
        modalConfig:{ 
          title: "新增", //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled:false,//编辑弹窗是否可编辑
          width:'700px',//弹出框宽度
          modalRef:"modalRef",//modal标识
          type:"1"//类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        modalForm:[
					{type:'Input',label:'任务名称',prop:'jobName',rules:{required:true,maxLength:50}},
          {type:'Select',label:'导出类型',prop:'exportType',rules:{required:true},multiple:false,options:this.selectUtil.reportExportType},
          {type:'Select',label:'时间类型',prop:'jobTimeType',rules:{required:true},multiple:false,options:this.selectUtil.jobTimeType,change:this.changeTimeType},
          {type:'DateTime',label:'任务执行时间',prop:'jobTime',rules:{required:true,maxLength:50}},
					{type:'Input',label:'任务执行时间',prop:'jobCron',rules:{required:true,maxLength:50},suggestions:"点击在线生成执行时间",suggestionLink:"https://cron.qqe2.com/"},
					{type:'Button',label:'参数设置',prop:'jobData',rules:{required:false},buttonText:"点击设置参数",click:this.showParamModal},
					{type:'Textarea',label:'发送邮箱',prop:'email',width:'400px',rules:{required:true},suggestions:"多个邮箱用;分隔"},
        ],
        //modal表单 end
        //modal 数据 start
        modalData : {//modal页面数据
					jobName:"",//任务名称 
          jobTimeType:"",//时间类型
          jobTime:"",//指定时间
					jobCron:"",//任务执行时间 
					jobData:"",//任务参数 
					email:"",//导出数据后发送邮箱，多个邮箱用;分割 
          exportType:"",
        },
        //modal 数据 end
        //modal 按钮 start
        modalHandles:[
          {label:'取消',type:'default',handle:()=>this.closeModal()},
          {label:'提交',type:'primary',handle:()=>this.save()}
        ],
        //modal 按钮 end
        paramDialog:false,
        searchData:{
            params:[],
        },//参数
        reportForm:[],
      }
    }
  },
  activated(){
    let thirdPartyType = localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)
    if(!thirdPartyType){
      this.searchtablelist();
      this.getReportParam();
    }
  },
  mounted(){
    let thirdPartyType = localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)
    if(thirdPartyType){
      this.searchtablelist();
      this.getReportParam();
      this.pageData.tableHandles[2].isHidden = false;
    }
  },
  methods:{
    /**
     * @description: 获取表格数据
     * @param {type} 
     * @return: 
     * @author: caiyang
     */    
    searchtablelist(){
      this.pageData.queryData.tplId = this.$store.getters.parameters['taskTplId']
      var obj = {
        url:this.apis.reportTask.listApi,
        params:Object.assign({}, this.pageData.queryData, this.pageData.tablePage),
      }
      this.commonUtil.getTableList(obj).then(response=>{
        this.commonUtil.tableAssignment(response,this.pageData.tablePage,this.pageData.tableData);
      });
    },
    resetSearch(){
      this.commonUtil.clearObj(this.pageData.queryData);
      this.searchtablelist();
    },
    /**
     * @description: modal显示
     * @param {type} 类型 1新增，2编辑 3详情 
     * @param {id} 数据唯一标识
     * @return: 
     * @author: caiyang
     */    
    showModal(type,id){
      this.commonUtil.showModal(this.pageData.modalConfig,type);
      if(type != this.commonConstants.modalType.insert)
      {
        this.getDetail(id);
      }else{
        this.changeTimeType();
      }
      
    },
    /**
     * @description: 获取详细数据
     * @param {id} 数据唯一标识
     * @return: 
     * @author: caiyang
     */    
    getDetail(id){
      var obj = {
        url:this.apis.reportTask.getDetailApi,
        params:{id:id},
      }
      var that = this;
      this.commonUtil.doGet(obj).then(response=>{
        this.commonUtil.coperyProperties(this.pageData.modalData,response.responseData);//数据赋值
        this.changeTimeType();
        if(response.responseData.jobData && response.responseData.jobData != "[]" && that.pageData.searchData.params[0].params && that.pageData.searchData.params[0].params.length > 0)
        {
          var jobData = JSON.parse(response.responseData.jobData);
          for (let index = 0; index < jobData.length; index++) {
            const element = jobData[index];
            var paramCode = element.paramCode;
            var value = element[paramCode];
            var isDefault = element.isDefault;
            for (let index = 0; index < that.pageData.searchData.params[0].params.length; index++) {
              var param = that.pageData.searchData.params[0].params[index];
              var code = param.paramCode;
              if(code == paramCode)
              {
                param[code] = value;
                param.isDefault = isDefault;
                break;
              }
            }
          }
        }else{
          this.getReportParam();
        }
      });
    },
    /**
     * @description: 关闭modal
     * @param 
     * @return: 
     * @author: caiyang
     */    
    closeModal(){
      this.$refs['modalRef'].$refs['modalFormRef'].resetFields();//校验重置
      this.pageData.modalConfig.show = false;//关闭modal
      this.commonUtil.clearObj(this.pageData.modalData);//清空modalData
    },
    /**
     * @description: 保存数据
     * @param {type} 
     * @return: 
     * @author: caiyang
     */    
    save(){
      this.$refs['modalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
            this.pageData.modalData.tplId = this.$store.getters.parameters['taskTplId']
            var obj = {
              params:this.pageData.modalData,
              removeEmpty:false,
            }
            if(this.pageData.modalConfig.type == this.commonConstants.modalType.insert)
            {
              obj.url = this.apis.reportTask.insertApi;
            }else{
              obj.url = this.apis.reportTask.updateApi
            }
            this.commonUtil.doPost(obj) .then(response=>{
              if (response.code == "200")
              {
                this.closeModal();
                this.searchtablelist();
              }
            });
        }else{
            return false;
        }
      });
    },
    /**
     * @description: 删除一条数据
     * @param {id} 数据唯一标识 
     * @return: 
     * @author: caiyang
     */    
    deleteOne(id){
      let obj = {
        url:this.apis.reportTask.deleteOneApi,
        messageContent:this.commonUtil.getMessageFromList("confirm.delete",null),
        callback:this.searchtablelist,
        params:{id:id},
        type:"get",
      }
      //弹出删除确认框
      this.commonUtil.showConfirm(obj)
    },
    /**
     * @description: 批量删除
     * @param {type} 
     * @return: 
     * @author: caiyang
     */    
    deleteBatch(){
        const length = this.pageData.selectList.length;
        if(length == 0)
        {
            this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.batchdelete.empty",null),type: this.commonConstants.messageType.error});
        }else{
          let ids = new Array();
          for (let i = 0; i < length; i++) {
              ids.push(this.pageData.selectList[i].id);
          }
          let obj = {
            url:this.apis.reportTask.deleteBatchApi,
            messageContent:this.commonUtil.getMessageFromList("confirm.delete",null),
            callback:this.searchtablelist,
            params:ids,
            type:"post",
          }
          this.commonUtil.showConfirm(obj);
        }
    },
    selectChange(rows){
      this.pageData.selectList = rows;
    },
    showParamModal(){
      this.pageData.paramDialog = true;
    },
    getReportParam(){
      let tplId = this.$store.getters.parameters['taskTplId']
      let obj = {
          url:this.apis.previewReport.getPreviewReportParamApi,
          params:{tplId:tplId,isMobile:1},
      }
      this.commonUtil.doPost(obj,null) .then(response=>{
          if (response.code == "200")
          {
              let result = response.responseData.params;
              this.pageData.searchData.params = [];
              this.pageData.reportForm = result;
              for(let i = 0;i<result.length;i++)
              {
                  var dataSet = {};
                  dataSet.datasetId = result[i].datasetId;
                  dataSet.datasetName = result[i].datasetName;
                  dataSet.datasourceId = result[i].datasourceId;
                  dataSet.params = [];
                  for(let m = 0;m<result[i].params.length;m++){
                      var param = {};
                      if(result[i].params[m].paramType == "mutiselect")
                      {
                          var data = new Array();
                          if(this.$route.query[result[i].params[m].paramCode])
                          {
                            data = this.$route.query[result[i].params[m].paramCode].split(",");
                          }else{
                              if(result[i].params[m].paramDefault != null && result[i].params[m].paramDefault != "")
                              {
                                   data = result[i].params[m].paramDefault.split(",");
                              }
                          }
                          param[result[i].params[m].paramCode] = data;
                          result[i].params[m].paramDefault = data;
                          
                      }else{
                        if(this.$route.query[result[i].params[m].paramCode])
                        {
                            param[result[i].params[m].paramCode]=this.$route.query[result[i].params[m].paramCode]
                        }else{
                            if(result[i].params[m].paramDefault != null && result[i].params[m].paramDefault != "")
                            {
                                param[result[i].params[m].paramCode] = result[i].params[m].paramDefault
                            }else{
                                param[result[i].params[m].paramCode] = "";
                            }
                        }
                      }
                      param.paramCode =  result[i].params[m].paramCode;
                      param.dateFormat = result[i].params[m].dateFormat;
                      param.paramHidden = result[i].params[m].paramHidden;
                      param.paramDefault = result[i].params[m].paramDefault;
                      param.paramType = result[i].params[m].paramType;
                      param.dateDefaultValue = result[i].params[m].dateDefaultValue;
                      param.isDefault = false;
                      dataSet.params.push(param);
                  }
                  this.pageData.searchData.params.push(dataSet);
              }
          }
      });
    },
    closeParamDialog(){
      this.pageData.paramDialog = false;
    },
    confirmParams(){
      this.$refs['reportRef'].$refs['reportFormRef'].validate((valid) => {
        if(valid)
        {
          this.closeParamDialog();
          this.pageData.modalData.jobData = JSON.stringify(this.pageData.searchData.params[0].params)
        }
      })
    },
    runTask(id){
      let obj = {
        url:this.apis.reportTask.runTaskApi,
        messageContent:this.commonUtil.getMessageFromList("confirm.operate",null),
        callback:this.searchtablelist,
        params:{id:id},
        type:"get",
      }
      //弹出删除确认框
      this.commonUtil.showConfirm(obj)
    },
    isShowPause(row)
    {
      if(row.jobStatus == "NORMAL")
      {
        return true;
      }
      return false;
    },
    isShowResume(row)
    {
      if(row.jobStatus == "PAUSED")
      {
        return true;
      }
      return false;
    },
    pauseTask(id)
    {
      let obj = {
        url:this.apis.reportTask.pauseTaskApi,
        messageContent:this.commonUtil.getMessageFromList("confirm.operate",null),
        callback:this.searchtablelist,
        params:{id:id},
        type:"get",
      }
      //弹出删除确认框
      this.commonUtil.showConfirm(obj)
    },
    resumeTask(id){
      let obj = {
        url:this.apis.reportTask.resumeTaskApi,
        messageContent:this.commonUtil.getMessageFromList("confirm.operate",null),
        callback:this.searchtablelist,
        params:{id:id},
        type:"get",
      }
      //弹出删除确认框
      this.commonUtil.showConfirm(obj)
    },
    changeTimeType(){
      var jobTimeType = this.pageData.modalData.jobTimeType;
      if(jobTimeType)
      {
        if(jobTimeType == 1)
        {
          this.pageData.modalForm[3].show = true;
          this.pageData.modalForm[3].rules.required = true;
          this.pageData.modalForm[4].show = false;
          this.pageData.modalForm[4].rules.required = false;
        }else if(jobTimeType == 2){
          this.pageData.modalForm[3].show = false;
          this.pageData.modalForm[3].rules.required = false;
          this.pageData.modalForm[4].show = true;
          this.pageData.modalForm[4].rules.required = true;
        }
      }else{
        this.pageData.modalForm[3].show = false;
        this.pageData.modalForm[3].rules.required = false;
        this.pageData.modalForm[4].show = false;
        this.pageData.modalForm[4].rules.required = false;
      }
    },
    backTo(){
      this.$router.push({ name: 'reportTpl',query:{thirdPartyType:localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)} })
    }
  }
};