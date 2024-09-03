import Editor from '@hufe921/canvas-editor'
import sqlFormatter from 'sql-formatter'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/theme/eclipse.css'
import 'codemirror/theme/material-palenight.css'
import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/fold/foldgutter.js'
import 'codemirror/addon/fold/xml-fold.js'
import 'codemirror/addon/fold/indent-fold.js'
import 'codemirror/addon/fold/brace-fold'
import 'codemirror/addon/fold/markdown-fold.js'
import 'codemirror/addon/fold/comment-fold.js'
import 'codemirror/addon/selection/active-line'
// import 'codemirror/addon/edit/closeBrackets';
// import 'codemirror/addon/edit/matchBrackets';
import 'codemirror/addon/hint/sql-hint.js'
import Axios from 'axios'
export default {
  data(){
    return {
      tplName:"",
      instance:null,
      datasets:[],
      loading:false,
      loadingText:"加载中",
      addDatasetsDialogVisiable:false,
      sqlForm: {
        datasetName: '',
        datasourceId: '',
        id: '',
        sqlType: 1
      },
      dataSource: [], // 模板数据源
      paramForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: '', // 参数类型
        dateFormat:'',//日期类型
        paramDefault: '', // 默认值
        paramRequired: '', // 是否必选
        selectContent: '', // 下拉选择内容
        selectType: '', // 内容来源
        isRelyOnParams: '', // 是否依赖其他参数
        relyOnParams: '',// 依赖参数代码
        paramHidden:"",//是否隐藏 1是 2否
        checkStrictly:"",//父子联动 1是 2否
      },
      procedureParamForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: '', // 参数类型
        paramDefault: '',// 默认值
        paramHidden:"",//是否隐藏
      },
      procedureOutParamForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: ''// 参数类型
      },
      datasourceType: '1', // 1数据库 2api
      cmOptions: { // codemirror参数配置
        tabSize: 4,
        mode: { name: 'text/x-mysql' },
        theme: 'eclipse',
        styleActiveLine: true,
        lineNumbers: true,
        line: true,
        foldgutter: true,
        gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter', 'CodeMirror-lint-markers'],
        lineWrapping: true, // 代码折叠
        foldGutter: true,
        matchBrackets: true,  // 括号匹配
        autoCloseBrackets: true
      },
      // sql解析对应的列表格数据
      sqlColumnTableData: {
        tableData: [],
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50]
        }
      },
      paramTableData: {
        tableData: [],
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50]
        }
      },
      procedureInParamTableData: {
        tableData: [],
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50]
        }
      },
      procedureOutParamTableData: {
        tableData: [],
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50]
        }
      },
      modalConfig:{ 
        title: "", //弹窗标题,值为:新增，查看，编辑
        show: false, //弹框显示
        formEditDisabled:false,//编辑弹窗是否可编辑
        width:'700px',//弹出框宽度
        modalRef:"modalRef",//modal标识
        type:"1"//类型 1超链接 2水印
      },
      modalForm:[
       
      ],
      modalData : {//modal页面数据
       
      },
      modalHandles:[
        {label:'取消',type:'default',handle:()=>this.closeModal()},
        {label:'确认',type:'primary',handle:()=>this.confimModal()}
      ],
      pptSrc:""
    }
  },
  methods: {
    // 添加数据集
    addDataSets() {
      this.addDatasetsDialogVisiable = true
    },
     // 执行sql语句并解析
     execSql() {
      this.$refs['sqlRef'].validate((valid) => {
        if (valid) {
          const reportTplId = this.$route.query.tplId// reportTplId
          const obj = {
            url: this.apis.reportDesign.execSqlApi,
            params: { tplId: reportTplId, tplSql: this.$refs.codeMirror.codemirror.getValue(), datasourceId: this.sqlForm.datasourceId, sqlType: this.sqlForm.sqlType,
              inParam: this.procedureInParamTableData.tableData ? JSON.stringify(this.procedureInParamTableData.tableData) : '', outParam: this.procedureOutParamTableData.tableData ? JSON.stringify(this.procedureOutParamTableData.tableData) : '',
              sqlParams: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '' },
            removeEmpty: false
          }
          this.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              this.sqlColumnTableData.tableData = response.responseData
              this.sqlColumnTableData.tablePage.pageTotal = response.responseData.length
            }
          })
        }
      })
    },
    formatSql() { // sql格式化
      let sqlContent = ''
      sqlContent = this.$refs.codeMirror.codemirror.getValue()
      this.$refs.codeMirror.codemirror.setValue(sqlFormatter.format(sqlContent))
    },
    // sql语句列表修改当前页触发事件
    handleCurrentChange: function(val) {
      this.sqlColumnTableData.tablePage.currentPage = val
    },
    // sql语句列表修改当每页显示条数触发事件
    handleSizeChange: function(val) {
      this.sqlColumnTableData.tablePage.pageSize = val
    },
    // 添加参数
    addParam() {
      this.$refs['paramRef'].validate((valid) => {
        if (valid) {
          // 从列表冲根据paramCode获取是否已经添加该参数
          const result = this.commonUtil.getItemIndexFromList(this.paramTableData.tableData, 'paramCode', this.paramForm.paramCode)
          if (result.index >= 0) { // 已经添加该参数，则修改参数内容
            this.paramTableData.tableData[result.index].paramName = this.paramForm.paramName
            this.paramTableData.tableData[result.index].paramType = this.paramForm.paramType
            this.paramTableData.tableData[result.index].dateFormat = this.paramForm.dateFormat
            this.paramTableData.tableData[result.index].paramDefault = this.paramForm.paramDefault
            this.paramTableData.tableData[result.index].paramRequired = this.paramForm.paramRequired
            this.paramTableData.tableData[result.index].selectType = this.paramForm.selectType
            this.paramTableData.tableData[result.index].selectContent = this.paramForm.selectContent
            this.paramTableData.tableData[result.index].isRelyOnParams = this.paramForm.isRelyOnParams
            this.paramTableData.tableData[result.index].relyOnParams = this.paramForm.relyOnParams
            this.paramTableData.tableData[result.index].paramHidden = this.paramForm.paramHidden
            this.paramTableData.tableData[result.index].checkStrictly = this.paramForm.checkStrictly
          } else {
            // 未添加该参数，则列表中新增一条数据
            const row = {
              paramName: this.paramForm.paramName,
              paramCode: this.paramForm.paramCode,
              paramType: this.paramForm.paramType,
              dateFormat: this.paramForm.dateFormat,
              paramDefault: this.paramForm.paramDefault,
              paramRequired: this.paramForm.paramRequired,
              selectType: this.paramForm.selectType,
              selectContent: this.paramForm.selectContent,
              isRelyOnParams: this.paramForm.isRelyOnParams == ""?"2":this.paramForm.isRelyOnParam,
              relyOnParams: this.paramForm.relyOnParams,
              paramHidden: this.paramForm.paramHidden,
              checkStrictly: this.paramForm.checkStrictly == ""?"":this.paramForm.checkStrictly,
            }
            this.paramTableData.tableData.push(row)
          }
          this.$refs['paramRef'].resetFields()// 校验重置
          this.commonUtil.clearObj(this.paramForm)
        } else {
          return false
        }
      })
    },
    // 编辑参数
    editParam(row) {
      this.paramForm.paramName = row.paramName
      this.paramForm.paramCode = row.paramCode
      this.paramForm.paramType = row.paramType
      this.paramForm.dateFormat = row.dateFormat
      this.paramForm.paramDefault = row.paramDefault
      this.paramForm.paramRequired = row.paramRequired
      this.paramForm.selectType = row.selectType
      this.paramForm.selectContent = row.selectContent
      this.paramForm.isRelyOnParams = row.isRelyOnParams
      this.paramForm.relyOnParams = row.relyOnParams
      this.paramForm.paramHidden = row.paramHidden
      this.paramForm.checkStrictly = row.checkStrictly
    },
    // 删除参数
    deleteParam(index) {
      this.paramTableData.tableData.splice(index, 1)
    },
    // 添加输入参数
    addInParam() {
      this.$refs['inParamRef'].validate((valid) => {
        if (valid) {
          // 从列表冲根据paramCode获取是否已经添加该参数
          const result = this.commonUtil.getItemIndexFromList(this.procedureInParamTableData.tableData, 'paramCode', this.procedureParamForm.paramCode)
          if (result.index >= 0) { // 已经添加该参数，则修改参数内容
            this.procedureInParamTableData.tableData[result.index].paramName = this.procedureParamForm.paramName
            this.procedureInParamTableData.tableData[result.index].paramType = this.procedureParamForm.paramType
            this.procedureInParamTableData.tableData[result.index].paramDefault = this.procedureParamForm.paramDefault
            this.procedureInParamTableData.tableData[result.index].paramRequired = this.procedureParamForm.paramRequired
            this.procedureInParamTableData.tableData[result.index].paramHidden = this.procedureParamForm.paramHidden
          } else {
            // 未添加该参数，则列表中新增一条数据
            const row = {
              paramName: this.procedureParamForm.paramName,
              paramCode: this.procedureParamForm.paramCode,
              paramType: this.procedureParamForm.paramType,
              paramDefault: this.procedureParamForm.paramDefault,
              paramHidden: this.procedureParamForm.paramHidden
            }
            this.procedureInParamTableData.tableData.push(row)
          }
          this.$refs['inParamRef'].resetFields()// 校验重置
          this.commonUtil.clearObj(this.procedureParamForm)
        } else {
          return false
        }
      })
    },
    // 编辑输入参数
    editInParam(row) {
      this.procedureParamForm.paramName = row.paramName
      this.procedureParamForm.paramCode = row.paramCode
      this.procedureParamForm.paramType = row.paramType
      this.procedureParamForm.paramDefault = row.paramDefault
      this.procedureParamForm.paramHidden = row.paramHidden
    },
    // 删除输入参数
    deleteInParam(index) {
      this.procedureInParamTableData.tableData.splice(index, 1)
    },
    moveUp(index, type) {
      if (type == '1') {
        // 输入参数
        this.commonUtil.moveUp(this.procedureInParamTableData.tableData, index)
      } else {
        // 输出参数
        this.commonUtil.moveUp(this.procedureOutParamTableData.tableData, index)
      }
    },
    moveDown(index, type) {
      if (type == '1') {
        // 输入参数
        this.commonUtil.moveDown(this.procedureInParamTableData.tableData, index)
      } else {
        // 输出参数
        this.commonUtil.moveDown(this.procedureOutParamTableData.tableData, index)
      }
    },
    // 添加输入参数
    addOutParam() {
      this.$refs['outParamRef'].validate((valid) => {
        if (valid) {
          // 从列表冲根据paramCode获取是否已经添加该参数
          const result = this.commonUtil.getItemIndexFromList(this.procedureOutParamTableData.tableData, 'paramCode', this.procedureOutParamForm.paramCode)
          if (result.index >= 0) { // 已经添加该参数，则修改参数内容
            this.procedureOutParamTableData.tableData[result.index].paramName = this.procedureOutParamForm.paramName
            this.procedureOutParamTableData.tableData[result.index].paramType = this.procedureOutParamForm.paramType
            this.procedureOutParamTableData.tableData[result.index].paramDefault = this.procedureOutParamForm.paramDefault
          } else {
            // 未添加该参数，则列表中新增一条数据
            const row = {
              paramName: this.procedureOutParamForm.paramName,
              paramCode: this.procedureOutParamForm.paramCode,
              paramType: this.procedureOutParamForm.paramType
            }
            this.procedureOutParamTableData.tableData.push(row)
          }
          this.$refs['outParamRef'].resetFields()// 校验重置
          this.commonUtil.clearObj(this.procedureOutParamForm)
        } else {
          return false
        }
      })
    },
    // 编辑输出参数
    editOutParam(row) {
      this.procedureOutParamForm.paramName = row.paramName
      this.procedureOutParamForm.paramCode = row.paramCode
      this.procedureOutParamForm.paramType = row.paramType
    },
    // 删除输出参数
    deleteOutParam(index) {
      this.procedureOutParamTableData.tableData.splice(index, 1)
    },
    // 获取模板关联的数据源
    getReportTplDateSource() {
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getReportTplDateSourceApi,
        params: { tplId: reportTplId },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.dataSource = response.responseData
          this.changeDatasource()
        }
      })
    },
    // 选择数据源修改
    changeDatasource() {
      for (let index = 0; index < this.dataSource.length; index++) {
        const element = this.dataSource[index]
        if (this.sqlForm.datasourceId == element.datasourceId) {
          if (element.type == '4') {
            this.sqlColumnTableData.tableData = []
            this.datasourceType = '2'
            if (element.apiColumns) {
              const columns = JSON.parse(element.apiColumns)
              if (columns.length > 0) {
                for (let index = 0; index < columns.length; index++) {
                  const element = columns[index]
                  var obj = {
                    columnName: element.propName,
                    name: element.propCode
                  }
                  this.sqlColumnTableData.tableData.push(obj)
                }
              }
            }
          } else {
            this.datasourceType = '1'
          }
          break
        }
      }
    },
    // 编辑数据及
    editDataSet(dataSet) {
      this.addDatasetsDialogVisiable = true
      this.$nextTick(() => {
        this.$refs.codeMirror.codemirror.setValue(dataSet.tplSql)
      })
      this.paramTableData.tableData = eval('(' + dataSet.tplParam + ')')
      this.sqlColumnTableData.tableData = dataSet.columns?dataSet.columns:[];
      this.sqlColumnTableData.tablePage.pageTotal = dataSet.columns?this.sqlColumnTableData.tableData.length:0
      this.sqlForm.datasetName = dataSet.datasetName
      this.sqlForm.datasourceId = dataSet.datasourceId
      this.sqlForm.id = dataSet.id
      this.sqlForm.sqlType = dataSet.sqlType
      if (dataSet.sqlType == 2) {
        this.procedureInParamTableData.tableData = JSON.parse(dataSet.inParam)
        this.procedureOutParamTableData.tableData = JSON.parse(dataSet.outParam)
      }
      this.getReportTplDateSource()
    },
    // 获取数据集
    getDataSets() {
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getDataSetsApi,
        params: { tplId: reportTplId },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.datasets = response.responseData
        }
      })
    },
    // 添加数据集
    addDataSet() {
      const reportTplId = this.$route.query.tplId// reportTplId
      let paginationValidate = true
      let tplSql = ''
      if (this.datasourceType == '1') {
        tplSql = this.$refs.codeMirror.codemirror.getValue()
        if (tplSql == null || tplSql == '') {
          this.commonUtil.showMessage({ message: 'sql语句不能为空', type: this.commonConstants.messageType.error })
          return
        }
      }

      this.$refs['sqlRef'].validate((valid) => {
        if (valid) {
          const obj = {
            url: this.apis.reportDesign.addDataSetApi,
            params: { tplId: reportTplId, datasetType: this.datasourceType, sqlType: this.sqlForm.sqlType, tplSql: tplSql, tplParam: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '', datasourceId: this.sqlForm.datasourceId, datasetName: this.sqlForm.datasetName, id: this.sqlForm.id,
              inParam: this.procedureInParamTableData.tableData ? JSON.stringify(this.procedureInParamTableData.tableData) : '', outParam: this.procedureOutParamTableData.tableData ? JSON.stringify(this.procedureOutParamTableData.tableData) : '',
              type:3},
            removeEmpty: false
          }
          this.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              this.getDataSets()
              this.closeAddDataSet()
              this.$forceUpdate()
            }
          })
        } else {
          return
        }
      })
    },
    // 关闭添加数据源
    closeAddDataSet() {
      // if (!this.addDatasetsDialogVisiable) {
      //   return
      // }
      this.addDatasetsDialogVisiable = false
      if (this.datasourceType == '1') {
        this.$refs.codeMirror.codemirror.setValue('')
      }

      this.$refs['sqlRef'].resetFields()// 校验重置
      this.$refs['paramRef'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.sqlForm)
      this.commonUtil.clearObj(this.paramForm)
      
      this.sqlColumnTableData.tableData = []
      this.sqlColumnTableData.tablePage.currentPage = 1
      this.sqlColumnTableData.tablePage.pageTotal = 0
      this.paramTableData.tableData = []
      this.paramTableData.tablePage.currentPage = 1
      this.paramTableData.tablePage.pageTotal = 0
      this.datasourceType = '1'
    },
    async clickDatasets(o) {
      if (o.isActive) {
        this.$set(o, 'isActive', false)
      } else {
        this.$set(o, 'isActive', true)
        await this.getDatasetColumns(o)
      }
      if (o.datasetType == '2') {
        this.getApiDefaultRequestResult(o)
      }
      this.$forceUpdate()
    },
    getDatasetColumns(element) {
      const obj = {
        url: this.apis.reportDesign.getDataSetColumnsApi,
        params: { id: element.id },
        removeEmpty: false
      }
      var that = this;
      this.commonUtil.doPost(obj).then(response => {
        element.columns = response.responseData;
        that.$refs['chartModalRef'].$forceUpdate();
      })
    },
    // 获取api接口默认参数的返回值
    getApiDefaultRequestResult(element) {
      const obj = {
        url: this.apis.reportDesign.getApiDefaultRequestResultApi,
        params: { id: element.id },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        this.$set(element, 'apiResult', response.responseData.apiResult)
      })
    },
    copyAttr(type,datasetName,colulmnName){
      let text = "";
      if(type == 1){//文本
        text = "{{" + datasetName + "." + colulmnName + "}}"
      }else if(type == 2){//图片
        text = "{{@" + datasetName + "." + colulmnName + "}}"
      }else if(type == 3){//单元格文本
        text = "[" + colulmnName + "]"
      }else if(type == 4){//单元格图片
        text = "[@" + colulmnName + "]"
      }else if(type == 5){//数据集名称
        text = "{{" + datasetName + "}}"
      }else if(type == 6){//区块对
        text = "{{?" + datasetName + "}}{{/" + datasetName + "}}";
      }else if(type == 7){//区块对文本
        text = "{{" + colulmnName + "}}";
      }else if(type == 8){//区块对图片
        text = "{{@" + colulmnName + "}}";
      }
      const input = document.getElementById('clipboradInput'); // 承载复制内容
      input.value = text; // 修改文本框的内容
      input.select(); // 选中文本
      document.execCommand('copy'); // 执行浏览器复制命令
      this.$message.success('复制成功')
    },
    // 删除数据集
    deleteDataSet(dataSet) {
      const params = {
        url: this.apis.reportDesign.deleteDataSetApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.deleteDataSetCallback,
        params: { id: dataSet.id },
        type: 'get'
      }
      // 弹出删除确认框
      this.commonUtil.showConfirm(params)
    },
    deleteDataSetCallback(){
      this.getDataSets();
    },
    uploadFile(file,imageFileDom) {
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem(this.commonConstants.sessionItem.authorization) }
      }
      var that = this
      let options = this.instance.command.getOptions();
      let paperDirection = options.paperDirection;
      let pageWidth = options.width;
      let pageHeight = options.height;
      if(paperDirection == "horizontal"){
        pageWidth = options.height;
        pageHeight = options.width;
      }
      Axios.post(this.apis.screenDesign.uploadFileApi, param, config)
        .then(res => {
          let width = 0;
          let height = 0;
          width = res.data.responseData.width>pageWidth?pageWidth:res.data.responseData.width;
          height = res.data.responseData.height>pageHeight?pageHeight:res.data.responseData.height;

          that.instance.command.executeImage({
            value:res.data.responseData.fileUri,
            width: width,
            height: height
          })
          imageFileDom.value = ''
        })
    },
    doCopy(item){
      console.log(item)
      let text = item.value;
      if(item.type == "number"){
        text = '<if test="'+item.value+'!=null' + '"> \n' 
        text = text + "  and " + item.column + " = #{"+item.value+"} \n" + "</if>"
      }else{
        text = '<if test="'+item.value+'!=null and ' + item.value + "!=''" + '">\n' 
        text = text + "  and " + item.column + " = #{"+item.value+"} \n" + "</if>"
      }
      const input = document.getElementById('clipboradInput'); // 承载复制内容
      input.value = text; // 修改文本框的内容
      input.select(); // 选中文本
      document.execCommand('copy'); // 执行浏览器复制命令
      this.$message.success('复制成功')
    }
  },
//使用mounted的原因是因为在mounted中dom已经加载完毕，否则会报错，找不到getAttribute这个方法
  mounted() {
    this.pptSrc = `${process.env.VUE_APP_BASE_PPT_URL}`
    this.getReportTplDateSource();
    this.getDataSets();
    const iframe = document.querySelector('#pptIframe');
    const tplId = this.$route.query.tplId// reportTplId
    iframe.onload = function(){
        var obj = {
            tplId:tplId,
            token:localStorage.getItem("token"),
            designMode:"1"
        }
        iframe.contentWindow.postMessage(obj, '*')
    }
  },
};