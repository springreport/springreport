export default {
  name:'docTpl',
  data() {
    return{
      pageData:{
        //查询表单内容 start
        searchForm:[
					{type:'Input',label:'模板标识',prop:'tplCode'},
					{type:'Input',label:'模板名称',prop:'tplName'},
        ],
        //查询表单内容 end
        //查询条件 start
        queryData:{
					tplCode:"",//模板标识 
					tplName:"",//模板名称 
        },
        //查询条件 end
        //查询表单按钮start
        searchHandle:[
          {label:'查询',type:'primary',handle:()=>this.searchtablelist(),auth:'docTpl_search'},
          {label:'重置',type:'warning',handle:()=>this.resetSearch(),auth:'docTpl_search'}
        ],
        //查询表单按钮end
        //表格数据start
        tableData:[],
        //表格数据end
        //表格工具栏按钮 start
        tableHandles:[
          {label:'新增',type:'primary',handle:()=>this.showModal(this.commonConstants.modalType.insert),auth:'docTpl_insert'},
          {label:'批量删除',type:'danger',handle:()=>this.deleteBatch(),auth:'docTpl_delete'}
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
					{label:'模板标识',prop:'tplCode',align:'center'},
					{label:'模板名称',prop:'tplName',align:'center'},
          {label:'数据源代码',prop:'dataSourceCode',align:'center',overflow:true},
          {label:'数据源名称',prop:'dataSourceName',align:'center',overflow:true},
					{label:'操作',prop:'operation',align:'center',type:'button',btnList:[
						{label:'查看',type:'primary',auth:'docTpl_getDetail',handle:(row)=>this.showModal(this.commonConstants.modalType.detail,row.id)},
						{label:'编辑',type:'primary',auth:'docTpl_edit',handle:(row)=>this.showModal(this.commonConstants.modalType.update,row.id)},
            {label:'设计',type:'primary',auth:'docTpl_design',handle:(row)=>this.routerTo('docDesign',row)},
            {label:'报表查看',type:'primary',auth:'docTpl_view',handle:(row)=>this.routerTo('docPreview',row)},
						{label:'删除',type:'primary',auth:'docTpl_delete',handle:(row)=>this.deleteOne(row.id)},
					]}
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
					{type:'Input',label:'模板标识',prop:'tplCode',rules:{required:true,maxLength:40}},
					{type:'Input',label:'模板名称',prop:'tplName',rules:{required:true,maxLength:40}},
          {type:'Select',label:'数据源',prop:'dataSource',rules:{required:true},multiple:true,props:{label:"code",value:"id"}},
        ],
        //modal表单 end
        //modal 数据 start
        modalData : {//modal页面数据
					tplCode:"",//模板标识 
					tplName:"",//模板名称 
          dataSource:[],//报表数据源
        },
        //modal 数据 end
        //modal 按钮 start
        modalHandles:[
          {label:'取消',type:'default',handle:()=>this.closeModal()},
          {label:'提交',type:'primary',handle:()=>this.save()}
        ],
        //modal 按钮 end
      }
    }
  },
  mounted(){
    this.searchtablelist();
    this.getReportDatasource();
  },
  methods:{
    /**
     * @description: 获取表格数据
     * @param {type} 
     * @return: 
     * @author: caiyang
     */    
    searchtablelist(){
      var obj = {
        url:this.apis.docTpl.listApi,
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
        url:this.apis.docTpl.getDetailApi,
        params:{id:id},
      }
      this.commonUtil.doGet(obj).then(response=>{
        this.commonUtil.coperyProperties(this.pageData.modalData,response.responseData);//数据赋值
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
            var obj = {
              params:this.pageData.modalData,
              removeEmpty:false,
            }
            if(this.pageData.modalConfig.type == this.commonConstants.modalType.insert)
            {
              obj.url = this.apis.docTpl.insertApi;
            }else{
              obj.url = this.apis.docTpl.updateApi
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
        url:this.apis.docTpl.deleteOneApi,
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
            url:this.apis.docTpl.deleteBatchApi,
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
    //获取数据源
    getReportDatasource(){
      var obj = {
        params:{},
        url:this.apis.reportDatasource.getReportDatasourceApi
      }
      this.commonUtil.doPost(obj) .then(response=>{
        if (response.code == "200")
        {
          this.pageData.modalForm[2].options = response.responseData;
        }
      });
    },
    //页面跳转
    routerTo(name,row){
      let viewReport = this.$router.resolve({ name:name,query: {tplId:row.id}});
      window.open(viewReport.href, '_blank');
    },
  }
};