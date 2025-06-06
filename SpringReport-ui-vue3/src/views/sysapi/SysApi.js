export default {
  name:'sysApi',
  data() {
    return{
      tableLoading: true,
      pageData:{
        //查询表单内容 start
        searchForm:[
					{type:'Input',label:'权限标识',prop:'apiCode'},
					{type:'Input',label:'权限名称',prop:'apiName'},
        ],
        //查询表单内容 end
        //查询条件 start
        queryData:{
					apiCode:"",//接口唯一标识 
					apiName:"",//接口名称 
        },
        //查询条件 end
        //查询表单按钮start
        searchHandle:[
          {label:'清除条件',type:'',handle:()=>this.resetSearch(),auth:'sysApi_search'},
          {label:'查询',type:'primary',handle:()=>this.searchtablelist(),auth:'sysApi_search'},
        ],
        //查询表单按钮end
        //表格数据start
        tableData:[],
        //表格数据end
        //表格工具栏按钮 start
        tableHandles:[
          {label:'新增',type:'primary',position: 'right',iconClass: 'action-icon-add',handle:()=>this.showModal(this.commonConstants.modalType.insert),auth:'sysApi_insert'},
          {label:'批量删除',type:'danger', position: 'left',iconClass: 'action-icon-del',handle:()=>this.deleteBatch(),auth:'sysApi_batchDelete'}
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
          {label:'操作',prop:'operation',align:'center',width:54,type:'dropdown',btnList:[
						{label:'查看',type:'primary',auth:'sysApi_getDetail',handle:(row)=>this.showModal(this.commonConstants.modalType.detail,row.id)},
						{label:'编辑',type:'primary',auth:'sysApi_update',handle:(row)=>this.showModal(this.commonConstants.modalType.update,row.id)},
						{label:'删除',type:'danger',auth:'sysApi_delete',handle:(row)=>this.deleteOne(row.id)},
					]},
					{label:'权限标识',prop:'apiCode',align:'center',overflow:true},
					{label:'权限名称',prop:'apiName',align:'center',overflow:true},
					{label:'权限描述',prop:'apiFunction',align:'center',overflow:true},
          {label:'排序',prop:'sort',align:'center',overflow:true},
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
					{type:'Input',label:'权限标识',prop:'apiCode',rules:{required:true,maxLength:50}},
					{type:'Input',label:'权限名称',prop:'apiName',rules:{required:true,maxLength:50}},
					{type:'Input',label:'权限描述',prop:'apiFunction',rules:{required:true,maxLength:200}},
          {type:'Input',label:'排序',prop:'sort',rules:{required:true,type:'positiveInt'}},
        ],
        //modal表单 end
        //modal 数据 start
        modalData : {//modal页面数据
					apiCode:"",//接口唯一标识 
					apiName:"",//接口名称 
					apiFunction:"",//接口功能 
          sort:"",
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
  },
  methods:{
    /**
     * @description: 获取表格数据
     * @param {type} 
     * @return: 
     * @author: caiyang
     */    
    searchtablelist(){
      this.tableLoading = true;
      var obj = {
        url:this.apis.sysApi.listApi,
        params:Object.assign({menuId:this.$route.query.menuId}, this.pageData.queryData, this.pageData.tablePage),
      }
      this.commonUtil.getTableList(obj).then(response=>{
        this.commonUtil.tableAssignment(response,this.pageData.tablePage,this.pageData.tableData);
        this.tableLoading = false;
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
        url:this.apis.sysApi.getDetailApi,
        params:{id:id},
      }
      this.commonUtil.doGet(obj).then(response=>{
        response.responseData.sort = response.responseData.sort + '';
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
            obj.params.menuId = this.$route.query.menuId;
            if(this.pageData.modalConfig.type == this.commonConstants.modalType.insert)
            {
              obj.url = this.apis.sysApi.insertApi;
            }else{
              obj.url = this.apis.sysApi.updateApi
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
        url:this.apis.sysApi.deleteOneApi,
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
            url:this.apis.sysApi.deleteBatchApi,
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
  }
};