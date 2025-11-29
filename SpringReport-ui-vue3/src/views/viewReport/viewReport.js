export default {
    name:'viewReport',
    data() {
      return{
        tableLoading: true,
        pageData:{
          defaultProps: {
            children: 'children',
            label: 'label'
          },
          treeData:[],
          //查询表单内容 start
          searchForm:[
            {type:'Input',label:'报表名称',prop:'tplName'},
            // {type:'Select',label:'报表类型',prop:'reportType',props:{label:"reportTypeName",value:"id"}},
          ],
          //查询表单内容 end
          //查询条件 start
          queryData:{
            tplName:"",//模板名称
            reportType:"",//报表类型
          },
          //查询条件 end
          //查询表单按钮start
          lazy:true,
          searchHandle:[
            {label:'查询',type:'primary',handle:()=>this.searchtablelist(),auth:'viewReport_Search'},
            {
              label: '清除条件',
              type: '',
              handle: () => this.resetSearch(),
              auth: 'viewReport_Search',
            },
          ],
          //查询表单按钮end
          //表格数据start
          tableData:[],
          //表格数据end
          //表格工具栏按钮 start
          tableHandles:[
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
            {label:'操作',prop:'operation',align:'center',type:'dropdown',width:54,btnList:[
              // {label:'报表查看',type:'primary',auth:'viewReport_view',handle:(row)=>this.routerTo(row.tplType == '1'?'luckyReportPreview':'luckyReportFromsPreview',row)},
              {label:'报表查看(pc)',type:'primary',auth:'viewReport_view',handle:(row)=>this.routerTo("luckyReportPreview",row),show:(row)=>this.isShowBtn(row)},
              {label:'报表查看(手机)',type:'primary',auth:'viewReport_view',show:(row)=>this.isShowShare(row),handle:(row)=>this.routerTo('h5ReportPreview',row)},
            ]},
            {label:'报表名称',prop:'tplName',align:'left',overflow:true,icon:true},
            {label:'报表类型',prop:'reportTypeName',align:'center',overflow:true},
            {label:'导出是否加密',prop:'exportEncrypt',align:'center',codeType:'yesNo',formatter:this.commonUtil.getTableCodeName,overflow:true},
            {label:'报表类型',prop:'tplType',align:'center',codeType:'tplType',formatter:this.commonUtil.getTableCodeName,overflow:true},
          ],
          //表格列表头end
        }
      }
    },
    mounted() {
      this.searchtablelist();
      // this.getReportType();
      this.getReportTypeTree();
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
          url:this.apis.reportTpl.listApi,
          params:Object.assign({}, this.pageData.queryData, this.pageData.tablePage),
        }
        var that = this;
        that.pageData.tableData = []
        this.commonUtil.getTableList(obj).then(response=>{
          this.commonUtil.tableAssignment(response,this.pageData.tablePage,this.pageData.tableData);
        });
      },
      resetSearch(){
        this.commonUtil.clearObj(this.pageData.queryData);
        this.searchtablelist();
      },
      selectChange(rows){
        this.pageData.selectList = rows;
      },
      //获取报表类型
      getReportType(){
        var obj = {
          params:{},
          url:this.apis.reportType.getReportTypeApi
        }
        this.commonUtil.doPost(obj) .then(response=>{
          if (response.code == "200")
          {
            this.pageData.searchForm[1].options = response.responseData;
            this.$refs['searchRef'].$forceUpdate();
          }
        });
      },
      //页面跳转
      routerTo(name,row){
        let viewReport = this.$router.resolve({ name:name,query: {tplId:row.id, thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType) }});
        window.open(viewReport.href, '_blank');
      },
      getReportTypeTree(){
        var obj = {
          params: {"type":"1"},
          removeEmpty:false,
          url:this.apis.reportType.getReportTypeTreeApi
        }
        this.commonUtil.doPost(obj) .then(response=>{
          if (response.code == "200")
          {
            this.pageData.treeData = response.responseData
          }
        });
      },
      handleNodeClick(data){
        if(data.id == '1')
        {
          this.pageData.queryData.reportType = "";
        }else{
          this.pageData.queryData.reportType = data.id;
        }
        this.searchtablelist();
      },
      isShowShare(row){
        if(row.tplType == 1)
        {
          return true
        }else{
          return false
        }
      },
      loadData(tree, treeNode, resolve){
        var obj = {
          params:{reportType:tree.id},
          url:this.apis.reportTpl.getRoleReportsApi
        }
        this.commonUtil.doPost(obj) .then(response=>{
          if (response.code == "200")
          {
            resolve(response.responseData)
          }
        });
      },
      isShowBtn(row){
        if(row.type == "1"){
          return false;
        }else {
          return true;
        }
      },
    }
  };