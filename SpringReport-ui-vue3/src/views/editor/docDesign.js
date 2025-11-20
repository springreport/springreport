import Editor from './assets/js/canvas-editor.es.js'
import { format } from "sql-formatter";
import codemirror from "codemirror-editor-vue3";
// base style
import "codemirror/lib/codemirror.css";
// theme css
import "codemirror/theme/eclipse.css";
import "codemirror/addon/hint/sql-hint";
// language
import "codemirror/mode/sql/sql.js";
// active-line.js
import "codemirror/addon/selection/active-line.js";
// styleSelectedText
import "codemirror/addon/selection/mark-selection.js";
import "codemirror/addon/search/searchcursor.js";
// highlightSelectionMatches
import "codemirror/addon/scroll/annotatescrollbar.js";
import "codemirror/addon/search/matchesonscrollbar.js";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/match-highlighter.js";

// keyMap
import "codemirror/mode/clike/clike.js";
import "codemirror/addon/edit/matchbrackets.js";
import "codemirror/addon/comment/comment.js";
import "codemirror/addon/dialog/dialog.js";
import "codemirror/addon/dialog/dialog.css";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/search.js";
import "codemirror/keymap/sublime.js";
 
// foldGutter
import "codemirror/addon/fold/foldgutter.css";
import "codemirror/addon/fold/brace-fold.js";
import "codemirror/addon/fold/comment-fold.js";
import "codemirror/addon/fold/foldcode.js";
import "codemirror/addon/fold/foldgutter.js";
import "codemirror/addon/fold/indent-fold.js";
import "codemirror/addon/fold/markdown-fold.js";
import Axios from 'axios'
export default {
  components: {
    codemirror,
},
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
        sqlType: 1,
        groupId: '',
        isCommon:2,
        isConvert:2,
        headerName:"",
        valueField:"",
        fixedColumn:[],
        mongoTable:'',
        mongoSearchType:null,
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
        paramPrefix:"",//参数前缀
        datasourceId:"",//数据源id
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
      subParamAttrs:[],//主表传递给子表的参数属性
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
      sqlText:"",
      orderSql:"",
      chartModalConfig:{ 
        title: "图表设置", //弹窗标题,值为:新增，查看，编辑
        show: false, //弹框显示
        formEditDisabled:false,//编辑弹窗是否可编辑
        width:'800px',//弹出框宽度
        modalRef:"modalRef",//modal标识
        type:"1"//类型 1超链接 2水印
      },
      chartModalForm:[
        {type:'Input',label:'图表标题',prop:'chartName',rules:{required:true},width:"200px"},
        {type:'Select',label:'显示标题',prop:'showChartName',rules:{required:true},options:this.selectUtil.yesNo,width:"200px"},
        {type:'Select',label:'图表类型',prop:'chartType',rules:{required:true},props:{label:"label",value:"value"},options:this.selectUtil.docChartType,change:this.changeChartType,width:"200px"},
        {type:'Select',label:'数据集',prop:'datasetId',rules:{required:true},props:{label:"datasetName",value:"id"},change:this.changeDatasets,width:"200px"},
        {type:'Select',label:'分组字段',prop:'categoryField',rules:{required:true},props:{label:"name",value:"name"},width:"200px"},
        {type:'Select',label:'数值字段',prop:'valueField',rules:{required:true},props:{label:"name",value:"name"},width:"200px",multiple:true},
        {type:'Select',label:'系列字段',prop:'seriesField',rules:{required:false},props:{label:"name",value:"name"},width:"200px",multiple:true},
        {type:'Table',label:'已添加图表',tableCols:[],tableHandles:[],isPagination:false,isIndex:true},
      ],
      chartModalData : {//modal页面数据
        index:null,
        chartName:"",//图表名称
        showChartName:1,
        chartType:"",//图表类型
        datasetId:null,//数据集id
        datasetName:"",//数据集名称
        categoryField:null,
        valueField:null,
        seriesField:null,
      },
      chartModalHandles:[
        {label:'关闭',type:'default',handle:()=>this.closeChartModal()},
        {label:'确认',type:'primary',handle:()=>this.confimChartModal()}
      ],
      tableCols:[
        {label:'图表名称',prop:'chartName',align:'center',overflow:true},
        {label:'图表类型',prop:'chartType',align:'center',overflow:true,formatter:this.commonUtil.getTableCodeName,codeType:'chartType'},
        {label:'数据集',prop:'datasetName',align:'center',overflow:true},
        {label:'分组字段',prop:'categoryField',align:'center',overflow:true},
        {label:'数据字段',prop:'valueField',align:'center',overflow:true},
        {label:'系列字段',prop:'seriesField',align:'center',overflow:true},
        {label:'操作',prop:'operation',align:'center',type:'button',btnList:[
          {label:'编辑',type:'primary',auth:'ignore',handle:(row,index)=>this.editChart(row,index)},
          {label:'删除',type:'primary',auth:'ignore',handle:(row,index)=>this.deleteChart(row,index)},
        ]}
      ],
      docTplCharts:[],//文档图表
      chartUrlPrefix:import.meta.env.VITE_BASE_URL_PREFIX+"/images/chart/",//图表图片的前缀
      highlightVisiable:false,
      highlightForm:{
        color:"",
      },
      datasourceTableName:"",
      dataSourceTables:[],
      datasourceTableColumns:{},//表对应的列
      tableColumns:[],
      codeModalConfig:{ 
        title: "条形码/二维码", //弹窗标题,值为:新增，查看，编辑
        show: false, //弹框显示
        formEditDisabled:false,//编辑弹窗是否可编辑
        width:'700px',//弹出框宽度
        modalRef:"modalRef",//modal标识
        type:"1"//类型 1超链接 2水印
      },
      codeModalForm:[
        {type:'Input',label:'名称',prop:'codeName',rules:{required:true}},
        {type:'Select',label:'数据集',prop:'datasetId',rules:{required:true},props:{label:"datasetName",value:"id"},change:this.changeCodeDatasets},
        {type:'Select',label:'数据字段',prop:'valueField',rules:{required:true},props:{label:"name",value:"name"}},
        {type:'Table',label:'已添加数据',tableCols:[],tableHandles:[],isPagination:false,isIndex:true},
      ],
      codeModalData : {//modal页面数据
        index:null,
        codeName:"",//名称
        datasetId:null,//数据集id
        datasetName:"",//数据集名称
        valueField:null,
        codeType:"1",//1 条形码 2二维码
        codeUrl:"",
      },
      codeModalHandles:[
        {label:'关闭',type:'default',handle:()=>this.closeCodeModal()},
        {label:'确认',type:'primary',handle:()=>this.confirmCodeModal()}
      ],
      codeTableCols:[
        {label:'名称',prop:'codeName',align:'center',overflow:true},
        {label:'数据集',prop:'datasetName',align:'center',overflow:true},
        {label:'数据字段',prop:'valueField',align:'center',overflow:true},
        {label:'操作',prop:'operation',align:'center',type:'button',btnList:[
          {label:'编辑',type:'primary',auth:'ignore',handle:(row,index)=>this.editCode(row,index)},
          {label:'删除',type:'primary',auth:'ignore',handle:(row,index)=>this.deleteCode(row,index)},
        ]}
      ],
      docTplCodes:[],//文档条形码和二维码
      paperMarginModalConfig:{ 
        title: "页边距", //弹窗标题,值为:新增，查看，编辑
        show: false, //弹框显示
        formEditDisabled:false,//编辑弹窗是否可编辑
        width:'650px',//弹出框宽度
        modalRef:"modalRef",//modal标识
        type:"1"//类型 1超链接 2水印
      },
      paperMarginModalForm:[
        {type:'Input',label:'上边距',prop:'topMargin',rules:{required:true,type:"positiveInt"}},
        {type:'Input',label:'下边距',prop:'bottomMargin',rules:{required:true,type:"positiveInt"}},
        {type:'Input',label:'左边距',prop:'leftMargin',rules:{required:true,type:"positiveInt"}},
        {type:'Input',label:'右边距',prop:'rightMargin',rules:{required:true,type:"positiveInt"}},
      ],
      paperMarginModalData : {//modal页面数据
        topMargin:'100',//上边距
        rightMargin:'120',//右边距
        bottomMargin:'100',//下边距
        leftMargin:'120',//左边距
      },
      paperMarginModalHandles:[
        {label:'关闭',type:'default',handle:()=>this.closePaperMarginModal()},
        {label:'确认',type:'primary',handle:()=>this.confirmPaperMarginModal()}
      ],
      leftOpen: true, // 右侧展开
      rightFormCollapse: ['generalConfig', 'subtotalCells', 'subtotalAttribute', 'groupSubtotal', 'cellFilter', 'cellHide'],
      rightFormCollapse2: ['generalConfig', 'sheetBlock'],
      groupSetVisible: false, // 分组设置弹框
      groupList: [],
      groupForm: {
        groupName: undefined
      },
      groupHandleVisible: false, // 新增、编辑分组
      groupHandleLoading: false, // 新增、编辑分组loading
      addDatasetType: 1, // 数据集类型 sql语句 | 参数配置
      datasetKeyword: '', // 报表搜索
      filedKeyword: '', // 检索所选报表内字段
      dataGroupLoading: false, // 数据集分组loading
      datasetItemActive: null, // 数据集选中项
      filedLoading: false, // 字段loading
      designHeight: 0,
      selectVariableOpen: true, // 数据集选择变量展开
    }
  },
  methods: {
    getDocTplSettings(){
      this.loading = true;
      this.loadingText = "模板数据加载中..."
      const tplId = this.$route.query.tplId
      const param = {
        url: this.apis.docTpl.getDocTplSettingsApi,
        params: { tplId: tplId }
      }
      param.callback = this.doPostCallback;
      var that = this;
      this.commonUtil.doPost(param).then(response => {
        if (response.code == '200') {
            that.tplName = response.responseData.tplName;
            if(response.responseData.chartUrlPrefix){
              that.chartUrlPrefix = response.responseData.chartUrlPrefix;
            }
            that.docTplCharts = response.responseData.docTplCharts;
            that.docTplCodes = response.responseData.docTplCodes;
            that.initEditor(response.responseData);
            //设置纸张大小并回显
            that.instance.command.executePaperSize(response.responseData.width,response.responseData.height);
            const paperSizeDom = document.querySelector('.paper-size');
            const paperSizeDomOptionsDom = paperSizeDom.querySelector('.options')
            let pagers = paperSizeDomOptionsDom.querySelectorAll('li');
            for (let index = 0; index < pagers.length; index++) {
              let element = pagers[index];
              element.classList.remove('active')
              if(element.dataset.paperSize == (response.responseData.height+"*"+response.responseData.width)||element.dataset.paperSize == (response.responseData.width+"*"+response.responseData.height))
              {
                element.classList.add('active')
              }
             
            }
            //设置纸张方向并回显
            let pageDirection = response.responseData.paperDirection
            that.instance.command.executePaperDirection(pageDirection);
            const paperDirectionDom = document.querySelector('.paper-direction')
            const paperDirectionDomOptionsDom = paperDirectionDom.querySelector('.options')
            let pagerDirections = paperDirectionDomOptionsDom.querySelectorAll('li')
            for (let index = 0; index < pagerDirections.length; index++) {
              let element = pagerDirections[index];
              element.classList.remove('active')
              if(element.dataset.paperDirection ==  pageDirection){
                element.classList.add('active')
              }
            }
            //设置水印
            let watermark = response.responseData.watermark;
            if(watermark){
              let watermarkObj = JSON.parse(watermark);
              that.instance.command.executeAddWatermark(watermarkObj)
            }
            let margins = response.responseData.margins;
            if(margins != "[]"){
              margins = JSON.parse(margins);
              that.instance.command.executeSetPaperMargin([
                Number(margins[0]),
                Number(margins[1]),
                Number(margins[2]),
                Number(margins[3])
              ])
            }
            if(response.responseData.firstpageHeaderFooterShow == 2){
              that.updateOptions("header","firstPage",true);
              that.updateOptions("footer","firstPage",true);
              that.updateOptions("pageNumber","fromPageNo",1);
            }
        }
      })
    },
    //回调函数
    doPostCallback(){
      this.loading = false;
    },
    closehighlightDialog(){
      this.highlightForm.color = "";
      this.highlightVisiable = false;
    },
    confirmSetHighlight(){
      var that = this;
      this.$refs['highlightRef'].validate((valid) => {
        if (valid) {
          if(that.highlightForm.color == "#"){
            that.instance.command.executeHighlight("")
          }else{
            that.instance.command.executeHighlight(that.highlightForm.color)
          }
          that.closehighlightDialog();
        } else {
          return false
        }
      })
    },
    async initEditor(responseData){
      const isApple =
      typeof navigator !== 'undefined' && /Mac OS X/.test(navigator.userAgent)
      // 初始化编辑器
      this.instance = new Editor(document.querySelector('.editor'), 
        {
            "header":  JSON.parse(responseData.header),
            "main": JSON.parse(responseData.main),
            "footer": JSON.parse(responseData.footer)
        }
      )
      const contextMenuList = await this.instance.register.getContextMenuList()
      // 修改内部右键菜单示例
      contextMenuList.forEach(menu => {
        // 通过菜单key找到菜单项后进行属性修改
        if (menu.key === "imageChange") {
          menu.when = () => false
        }
      })
        var that = this;
        //保存
        const saveDom = document.querySelector('.menu-item__save')
        saveDom.title = `保存`
        saveDom.onclick = function () {
          console.log('save')
          that.saveTpl();
        }
        //上传word模板
        const uploadDom = document.querySelector('.menu-item__upload')
        uploadDom.title = `上传`
        uploadDom.onclick = function () {
          console.log('upload')
          $("#uploadDocxBtn").click()
          // that.commonUtil.downloadFile(that.apis.docTpl.downLoadDocTplApi,{tplId:tplId},null,{})
        }
        //导出word模板
        const downloadDom = document.querySelector('.menu-item__download')
        downloadDom.title = `导出模板`
        downloadDom.onclick = function () {
          console.log('download')
          const tplId = that.$route.query.tplId// reportTplId
          that.commonUtil.downloadFile(that.apis.docTpl.downLoadDocTplApi,{tplId:tplId},null,{})
        }
        //预览
        const previewDom = document.querySelector('.menu-item__preview')
        previewDom.title = `预览`
        previewDom.onclick = function () {
          console.log('preview')
          const tplId = that.$route.query.tplId// reportTplId
          const viewReport = that.$router.resolve({ name: 'docPreview', query: { tplId: tplId ,thirdPartyType: localStorage.getItem(that.commonConstants.sessionItem.thirdPartyType)}})
          window.open(viewReport.href, '_blank')
        }
        // 动态设置展开面板的位置
        function setDropdownContentPosition(parentNode, dropdownNode) {
          const dropdownContent = dropdownNode
          const rect = parentNode.getBoundingClientRect()
          const screenWidth = window.innerWidth
          dropdownContent.style.top = `${rect.bottom}px`
          dropdownContent.style.left = `${rect.left}px`
          if (rect.left + dropdownContent.offsetWidth > screenWidth) {
            dropdownContent.style.left = `${rect.right - dropdownContent.offsetWidth}px`
          }
        }
        // | 撤销 | 重做 | 格式刷 | 清除格式 |
        const undoDom = document.querySelector('.menu-item__undo')
        undoDom.title = `撤销(${isApple ? '⌘' : 'Ctrl'}+Z)`
        undoDom.onclick = function () {
          console.log('undo')
          console.log(that.instance.command.getValue())
          that.instance.command.executeUndo()
        }

        const redoDom = document.querySelector('.menu-item__redo')
        redoDom.title = `重做(${isApple ? '⌘' : 'Ctrl'}+Y)`
        redoDom.onclick = function () {
          console.log('redo')
          that.instance.command.executeRedo()
        }
        const painterDom = document.querySelector('.menu-item__painter')
        let isFirstClick = true
        let painterTimeout = 0;
        painterDom.onclick = function () {
          if (isFirstClick) {
            isFirstClick = false
            painterTimeout = window.setTimeout(() => {
              console.log('painter-click')
              isFirstClick = true
              that.instance.command.executePainter({
                isDblclick: false
              })
            }, 200)
          } else {
            window.clearTimeout(painterTimeout)
          }
        }
        painterDom.ondblclick = function () {
          console.log('painter-dblclick')
          isFirstClick = true
          window.clearTimeout(painterTimeout)
          that.instance.command.executePainter({
            isDblclick: true
          })
        }
        const clearFormat =  document.querySelector('.menu-item__format');
        clearFormat.onclick = function () {
          console.log('format')
          that.instance.command.executeFormat()
        }
        // | 字体 | 字体变大 | 字体大小 | 加粗 | 斜体 | 下划线 | 删除线 | 上标 | 下标 | 字体颜色 | 背景色 |

        const fontDom = document.querySelector('.menu-item__font');
        const fontSelectDom = fontDom.querySelector('.select')
        const fontOptionDom = fontDom.querySelector('.options')
        fontDom.onclick = function () {
          console.log('font')
          fontOptionDom.classList.toggle('visible')
          setDropdownContentPosition(fontDom, fontOptionDom)
        }
        fontOptionDom.onclick = function (evt) {
          const family = evt.target.dataset.family
          that.instance.command.executeFont(family)
        }

        const sizeSetDom = document.querySelector('.menu-item__size')
        const sizeSelectDom = sizeSetDom.querySelector('.select')
        const sizeOptionDom = sizeSetDom.querySelector('.options')
        sizeSetDom.title = `设置字号`
        sizeSetDom.onclick = function () {
          console.log('size')
          sizeOptionDom.classList.toggle('visible')
          setDropdownContentPosition(sizeSetDom, sizeOptionDom)
        }
        sizeOptionDom.onclick = function (evt) {
          const size = evt.target.dataset.size
          that.instance.command.executeSize(Number(size))
        }

        const sizeAddDom = document.querySelector('.menu-item__size-add')
        sizeAddDom.title = `增大字号(${isApple ? '⌘' : 'Ctrl'}+[)`
        sizeAddDom.onclick = function () {
          console.log('size-add')
          that.instance.command.executeSizeAdd()
        }

        const sizeMinusDom = document.querySelector('.menu-item__size-minus')
        sizeMinusDom.title = `减小字号(${isApple ? '⌘' : 'Ctrl'}+])`
        sizeMinusDom.onclick = function () {
          console.log('size-minus')
          that.instance.command.executeSizeMinus()
        }

        const boldDom = document.querySelector('.menu-item__bold')
        boldDom.title = `加粗(${isApple ? '⌘' : 'Ctrl'}+B)`
        boldDom.onclick = function () {
          console.log('bold')
          that.instance.command.executeBold()
        }

        const italicDom = document.querySelector('.menu-item__italic')
        italicDom.title = `斜体(${isApple ? '⌘' : 'Ctrl'}+I)`
        italicDom.onclick = function () {
          console.log('italic')
          that.instance.command.executeItalic()
        }

        const underlineDom = document.querySelector('.menu-item__underline')
        underlineDom.title = `下划线(${isApple ? '⌘' : 'Ctrl'}+U)`
        const underlineOptionDom = underlineDom.querySelector('.options')
        underlineDom.querySelector('.select').onclick = function () {
          underlineOptionDom.classList.toggle('visible')
        }
        underlineDom.querySelector('i').onclick = function () {
          console.log('underline')
          that.instance.command.executeUnderline()
          underlineOptionDom.classList.remove('visible')
        }
        underlineDom.querySelector('ul').onmousedown = function (evt) {
          const li = evt.target
          const decorationStyle = li.dataset.decorationStyle
          that.instance.command.executeUnderline({
            style: decorationStyle
          })
          underlineOptionDom.classList.remove('visible')
        }

        const strikeoutDom = document.querySelector('.menu-item__strikeout')
        strikeoutDom.onclick = function () {
          console.log('strikeout')
          that.instance.command.executeStrikeout()
        }

        const superscriptDom = document.querySelector('.menu-item__superscript')
        superscriptDom.title = `上标(${isApple ? '⌘' : 'Ctrl'}+Shift+,)`
        superscriptDom.onclick = function () {
          console.log('superscript')
          that.instance.command.executeSuperscript()
        }

        const subscriptDom = document.querySelector('.menu-item__subscript')
        subscriptDom.title = `下标(${isApple ? '⌘' : 'Ctrl'}+Shift+.)`
        subscriptDom.onclick = function () {
          console.log('subscript')
          that.instance.command.executeSubscript()
        }

        const colorControlDom = document.querySelector('#color')
        colorControlDom.oninput = function () {
          that.instance.command.executeColor(colorControlDom.value)
        }
        const colorDom = document.querySelector('.menu-item__color')
        const colorSpanDom = colorDom.querySelector('span')
        colorDom.onclick = function () {
          console.log('color')
          colorControlDom.click()
        }

        const highlightControlDom = document.querySelector('#highlight')
        highlightControlDom.oninput = function () {
          that.instance.command.executeHighlight(highlightControlDom.value)
        }
        const highlightDom = document.querySelector('.menu-item__highlight')
        const highlightSpanDom = highlightDom.querySelector('span')
        highlightDom.onclick = function () {
          console.log('highlight')
          that.highlightVisiable = true;
        }

        const cellcolorControlDom = document.querySelector('#cellcolor')
        cellcolorControlDom.oninput = function () {
          that.instance.command.executeTableTdBackgroundColor(cellcolorControlDom.value)
        }
        const cellcolorDom = document.querySelector('.menu-item__cellcolor')
        const cellcolorSpanDom = cellcolorDom.querySelector('span')
        cellcolorDom.onclick = function () {
          console.log('cellcolor')
          cellcolorControlDom.click()
        }

        const titleDom = document.querySelector('.menu-item__title')
        const titleSelectDom = titleDom.querySelector('.select')
        const titleOptionDom = titleDom.querySelector('.options')
        titleOptionDom.querySelectorAll('li').forEach((li, index) => {
          li.title = `Ctrl+${isApple ? 'Option' : 'Alt'}+${index}`
        })
        titleDom.onclick = function () {
          console.log('title')
          titleOptionDom.classList.toggle('visible')
          setDropdownContentPosition(titleDom, titleOptionDom)
        }
        titleOptionDom.onclick = function (evt) {
          const li = evt.target 
          const level = li.dataset.level
          that.instance.command.executeTitle(level || null)
        }

        const leftDom = document.querySelector('.menu-item__left')
        leftDom.title = `左对齐(${isApple ? '⌘' : 'Ctrl'}+L)`
        leftDom.onclick = function () {
          console.log('left')
          that.instance.command.executeRowFlex(that.commonConstants.editor.RowFlex.LEFT)
        }

        const centerDom =
        document.querySelector('.menu-item__center')
        centerDom.title = `居中对齐(${isApple ? '⌘' : 'Ctrl'}+E)`
        centerDom.onclick = function () {
          console.log('center')
          that.instance.command.executeRowFlex(that.commonConstants.editor.RowFlex.CENTER)
        }

        const rightDom = document.querySelector('.menu-item__right')
        rightDom.title = `右对齐(${isApple ? '⌘' : 'Ctrl'}+R)`
        rightDom.onclick = function () {
          console.log('right')
          that.instance.command.executeRowFlex(that.commonConstants.editor.RowFlex.RIGHT)
        }

        const alignmentDom = document.querySelector('.menu-item__alignment')
        alignmentDom.title = `两端对齐(${isApple ? '⌘' : 'Ctrl'}+J)`
        alignmentDom.onclick = function () {
          console.log('alignment')
          that.instance.command.executeRowFlex(that.commonConstants.editor.RowFlex.ALIGNMENT)
        }

        const rowMarginDom = document.querySelector('.menu-item__row-margin')
        const rowOptionDom = rowMarginDom.querySelector('.options')
        rowMarginDom.onclick = function () {
          console.log('row-margin')
          rowOptionDom.classList.toggle('visible')
          setDropdownContentPosition(rowMarginDom, rowOptionDom)
        }
        rowOptionDom.onclick = function (evt) {
          const li = evt.target
          that.instance.command.executeRowMargin(Number(li.dataset.rowmargin))
        }

        const listDom = document.querySelector('.menu-item__list')
        listDom.title = `列表(${isApple ? '⌘' : 'Ctrl'}+Shift+U)`
        const listOptionDom = listDom.querySelector('.options')
        listDom.onclick = function () {
          console.log('list')
          listOptionDom.classList.toggle('visible')
          setDropdownContentPosition(listDom, listOptionDom)
        }
        listOptionDom.onclick = function (evt) {
          const li = evt.target 
          const listType = li.dataset.listType || null
          const listStyle = (li.dataset.listStyle)
          that.instance.command.executeList(listType, listStyle)
        }

        // | 表格 | 图片 | 超链接 | 分割线 | 水印 | 代码块 | 分隔符 | 控件 | 复选框 | LaTeX | 日期选择器
        const tableDom = document.querySelector('.menu-item__table')
        const tablePanelContainer = document.querySelector('.menu-item__table__collapse')
        const tableClose = document.querySelector('.table-close')
        const tableTitle = document.querySelector('.table-select')
        const tablePanel = document.querySelector('.table-panel')
        // 绘制行列
        const tableCellList= []
        for (let i = 0; i < 10; i++) {
          const tr = document.createElement('tr')
          tr.classList.add('table-row')
          const trCellList = []
          for (let j = 0; j < 10; j++) {
            const td = document.createElement('td')
            td.classList.add('table-cel')
            tr.append(td)
            trCellList.push(td)
          }
          tablePanel.append(tr)
          tableCellList.push(trCellList)
        }
        let colIndex = 0
        let rowIndex = 0
        // 移除所有格选择
        function removeAllTableCellSelect() {
          tableCellList.forEach(tr => {
            tr.forEach(td => td.classList.remove('active'))
          })
        }
        // 设置标题内容
        function setTableTitle(payload) {
          tableTitle.innerText = payload
        }
        // 恢复初始状态
        function recoveryTable() {
          // 还原选择样式、标题、选择行列
          removeAllTableCellSelect()
          setTableTitle('插入')
          colIndex = 0
          rowIndex = 0
          // 隐藏panel
          tablePanelContainer.style.display = 'none'
        }
        tableDom.onclick = function () {
          console.log('table')
          tablePanelContainer.style.display = 'block'
          setDropdownContentPosition(tableDom, tablePanelContainer)
        }
        tablePanel.onmousemove = function (evt) {
          const celSize = 16
          const rowMarginTop = 10
          const celMarginRight = 6
          const { offsetX, offsetY } = evt
          // 移除所有选择
          removeAllTableCellSelect()
          colIndex = Math.ceil(offsetX / (celSize + celMarginRight)) || 1
          rowIndex = Math.ceil(offsetY / (celSize + rowMarginTop)) || 1
          // 改变选择样式
          tableCellList.forEach((tr, trIndex) => {
            tr.forEach((td, tdIndex) => {
              if (tdIndex < colIndex && trIndex < rowIndex) {
                td.classList.add('active')
              }
            })
          })
          // 改变表格标题
          setTableTitle(`${rowIndex}×${colIndex}`)
        }
        tableClose.onclick = function () {
          recoveryTable()
        }
        tablePanel.onclick = function () {
          // 应用选择
          that.instance.command.executeInsertTable(rowIndex, colIndex)
          recoveryTable()
        }

        const imageDom = document.querySelector('.menu-item__image')
        const imageFileDom = document.querySelector('#image')
        imageDom.onclick = function () {
          imageFileDom.click()
        }
        imageFileDom.onchange = function () {
          const file = imageFileDom.files[0]
          that.uploadFile(file,imageFileDom)
        }
        const hyperlinkDom = document.querySelector(
          '.menu-item__hyperlink'
        )

        hyperlinkDom.onclick = function(){
          console.log('hyperlink')
          that.modalForm = [
            {type:'Input',label:'文本',prop:'name',rules:{required:true}},
            {type:'Input',label:'链接',prop:'url',rules:{required:true}},
          ]
          that.modalData={
            name:that.instance.command.getRangeText(),
            url:"",
          }
          that.modalConfig.title="超链接";
          that.modalConfig.show = true;
          that.modalConfig.type = "1";
        }

        const separatorDom = document.querySelector('.menu-item__separator')
        const separatorOptionDom =
          separatorDom.querySelector('.options')
        separatorDom.onclick = function () {
          console.log('separator')
          separatorOptionDom.classList.toggle('visible')
        }
        separatorOptionDom.onmousedown = function (evt) {
          let payload = []
          const li = evt.target
          const separatorDash = li.dataset.separator?.split(',').map(Number)
          if (separatorDash) {
            const isSingleLine = separatorDash.every(d => d === 0)
            if (!isSingleLine) {
              payload = separatorDash
            }
          }
          that.instance.command.executeSeparator(payload)
        }

        const watermarkDom = document.querySelector(
          '.menu-item__watermark'
        )
        const watermarkOptionDom =
        watermarkDom.querySelector('.options')
        watermarkDom.onclick = function () {
          console.log('watermark')
          watermarkOptionDom.classList.toggle('visible')
          setDropdownContentPosition(watermarkDom, watermarkOptionDom)
        }
        watermarkOptionDom.onmousedown = function (evt) {
          const li = evt.target
          const menu = li.dataset.menu
          watermarkOptionDom.classList.toggle('visible')
          if (menu === 'add') {
            that.modalForm = [
              {type:'Input',label:'内容',prop:'data',rules:{required:true}},
              // {type:'Input',label:'颜色',prop:'color',rules:{required:false}},
              // {type:'Input',label:'字体大小',prop:'size',rules:{required:true,type:"positiveInt"}},
            ]
            that.modalData={
              name:"",
              size:"100",
            }
            that.modalConfig.title="水印";
            that.modalConfig.show = true;
            that.modalConfig.type = "2";
          }else {
            that.instance.command.executeDeleteWatermark()
          }
        }
        const pageBreakDom = document.querySelector(
          '.menu-item__page-break'
        )
        pageBreakDom.onclick = function () {
          console.log('pageBreak')
          that.instance.command.executePageBreak()
        }
        // const checkboxDom = document.querySelector('.menu-item__checkbox')
        // checkboxDom.onclick = function () {
        //   console.log('checkbox')
        //   that.instance.command.executeInsertElementList([
        //     {
        //       type: that.commonConstants.editor.ElementType.CHECKBOX,
        //       checkbox: {
        //         value: false
        //       },
        //       value: ''
        //     }
        //   ])
        // }

        // 5. | 搜索&替换 | 打印 |
        const searchCollapseDom = document.querySelector('.menu-item__search__collapse')
        const searchInputDom = document.querySelector('.menu-item__search__collapse__search input')
        const replaceInputDom = document.querySelector('.menu-item__search__collapse__replace input')
        const searchDom =document.querySelector('.menu-item__search')
        searchDom.title = `搜索与替换(${isApple ? '⌘' : 'Ctrl'}+F)`
        const searchResultDom = searchCollapseDom.querySelector('.search-result')
        function setSearchResult() {
          const result = that.instance.command.getSearchNavigateInfo()
          if (result) {
            const { index, count } = result
            searchResultDom.innerText = `${index}/${count}`
          } else {
            searchResultDom.innerText = ''
          }
        }
        searchDom.onclick = function () {
          console.log('search')
          searchCollapseDom.style.display = 'block'
          const bodyRect = document.body.getBoundingClientRect()
          const searchRect = searchDom.getBoundingClientRect()
          const searchCollapseRect = searchCollapseDom.getBoundingClientRect()
          if (searchRect.left + searchCollapseRect.width > bodyRect.width) {
            searchCollapseDom.style.right = '0px'
            searchCollapseDom.style.left = 'unset'
          } else {
            searchCollapseDom.style.right = 'unset'
          }
          searchInputDom.focus()
        }
        searchCollapseDom.querySelector('span').onclick =
          function () {
            searchCollapseDom.style.display = 'none'
            searchInputDom.value = ''
            replaceInputDom.value = ''
            that.instance.command.executeSearch(null)
            setSearchResult()
          }
        searchInputDom.oninput = function () {
          that.instance.command.executeSearch(searchInputDom.value || null)
          setSearchResult()
        }
        searchInputDom.onkeydown = function (evt) {
          if (evt.key === 'Enter') {
            that.instance.command.executeSearch(searchInputDom.value || null)
            setSearchResult()
          }
        }
        searchCollapseDom.querySelector('button').onclick =
          function () {
            const searchValue = searchInputDom.value
            const replaceValue = replaceInputDom.value
            if (searchValue && replaceValue && searchValue !== replaceValue) {
              that.instance.command.executeReplace(replaceValue)
            }
          }
        searchCollapseDom.querySelector('.arrow-left').onclick =
          function () {
            that.instance.command.executeSearchNavigatePre()
            setSearchResult()
          }
        searchCollapseDom.querySelector('.arrow-right').onclick =
          function () {
            that.instance.command.executeSearchNavigateNext()
            setSearchResult()
          }
        const printDom = document.querySelector('.menu-item__print')
        printDom.title = `打印(${isApple ? '⌘' : 'Ctrl'}+P)`
        printDom.onclick = function () {
          console.log('print')
          that.instance.command.executePrint()
        }

        // 目录显隐 | 页面模式 | 纸张缩放 | 纸张大小 | 纸张方向 | 页边距 | 全屏 | 设置
        
        async function updateCatalog() {
          const catalog = await that.instance.command.getCatalog()
          const catalogMainDom =
            document.querySelector('.catalog__main')
          catalogMainDom.innerHTML = ''
          if (catalog) {
            const appendCatalog = (
              parent,
              catalogItems
            ) => {
              for (let c = 0; c < catalogItems.length; c++) {
                const catalogItem = catalogItems[c]
                const catalogItemDom = document.createElement('div')
                catalogItemDom.classList.add('catalog-item')
                // 渲染
                const catalogItemContentDom = document.createElement('div')
                catalogItemContentDom.classList.add('catalog-item__content')
                const catalogItemContentSpanDom = document.createElement('span')
                catalogItemContentSpanDom.innerText = catalogItem.name
                catalogItemContentDom.append(catalogItemContentSpanDom)
                // 定位
                catalogItemContentDom.onclick = () => {
                  that.instance.command.executeLocationCatalog(catalogItem.id)
                }
                catalogItemDom.append(catalogItemContentDom)
                if (catalogItem.subCatalog && catalogItem.subCatalog.length) {
                  appendCatalog(catalogItemDom, catalogItem.subCatalog)
                }
                // 追加
                parent.append(catalogItemDom)
              }
            }
            appendCatalog(catalogMainDom, catalog)
          }
        }
        let isCatalogShow = true
        const catalogDom = document.querySelector('.catalog')
        const catalogModeDom = document.querySelector('.catalog-mode')
        const catalogHeaderCloseDom = document.querySelector('.catalog__header__close')
        const switchCatalog = () => {
          isCatalogShow = !isCatalogShow
          if (isCatalogShow) {
            catalogDom.style.display = 'block'
            updateCatalog()
          } else {
            catalogDom.style.display = 'none'
          }
        }
        catalogModeDom.onclick = switchCatalog
        catalogHeaderCloseDom.onclick = switchCatalog

        const pageModeDom = document.querySelector('.page-mode')
        const pageModeOptionsDom =
          pageModeDom.querySelector('.options')
        pageModeDom.onclick = function () {
          pageModeOptionsDom.classList.toggle('visible')
        }
        pageModeOptionsDom.onclick = function (evt) {
          const li = evt.target 
          that.instance.command.executePageMode(li.dataset.pageMode)
        }

        document.querySelector('.page-scale-percentage').onclick =
        function () {
          console.log('page-scale-recovery')
          that.instance.command.executePageScaleRecovery()
        }

        document.querySelector('.page-scale-minus').onclick =
          function () {
            console.log('page-scale-minus')
            that.instance.command.executePageScaleMinus()
          }

        document.querySelector('.page-scale-add').onclick =
          function () {
            console.log('page-scale-add')
            that.instance.command.executePageScaleAdd()
          }

        // 纸张大小
        const paperSizeDom = document.querySelector('.paper-size')
        const paperSizeDomOptionsDom =
          paperSizeDom.querySelector('.options')
        paperSizeDom.onclick = function () {
          paperSizeDomOptionsDom.classList.toggle('visible')
        }
        paperSizeDomOptionsDom.onclick = function (evt) {
          const li = evt.target 
          const paperType = li.dataset.paperSize
          const [width, height] = paperType.split('*').map(Number)
          that.instance.command.executePaperSize(width, height)
          // 纸张状态回显
          paperSizeDomOptionsDom
            .querySelectorAll('li')
            .forEach(child => child.classList.remove('active'))
          li.classList.add('active')
        }

        // 纸张方向
        const paperDirectionDom =
          document.querySelector('.paper-direction')
        const paperDirectionDomOptionsDom =
          paperDirectionDom.querySelector('.options')
        paperDirectionDom.onclick = function () {
          paperDirectionDomOptionsDom.classList.toggle('visible')
        }
        paperDirectionDomOptionsDom.onclick = function (evt) {
          const li = evt.target
          const paperDirection = li.dataset.paperDirection
          that.instance.command.executePaperDirection(paperDirection)
          // 纸张方向状态回显
          paperDirectionDomOptionsDom
            .querySelectorAll('li')
            .forEach(child => child.classList.remove('active'))
          li.classList.add('active')
        }
        // 页面边距
        const paperMarginDom = document.querySelector('.paper-margin')
        paperMarginDom.onclick = function () {
          const [topMargin, rightMargin, bottomMargin, leftMargin] = that.instance.command.getPaperMargin();
            that.paperMarginModalData.topMargin = topMargin+'';
            that.paperMarginModalData.rightMargin = rightMargin+'';
            that.paperMarginModalData.bottomMargin = bottomMargin+'';
            that.paperMarginModalData.leftMargin = leftMargin+'';
            that.paperMarginModalConfig.show = true;
        }

        // 全屏
        const fullscreenDom = document.querySelector('.fullscreen')
        fullscreenDom.onclick = toggleFullscreen
        window.addEventListener('keydown', evt => {
          if (evt.key === 'F11') {
            toggleFullscreen()
            evt.preventDefault()
          }
        })
        document.addEventListener('fullscreenchange', () => {
          fullscreenDom.classList.toggle('exist')
        })
        function toggleFullscreen() {
          console.log('fullscreen')
          if (!document.fullscreenElement) {
            document.documentElement.requestFullscreen()
          } else {
            document.exitFullscreen()
          }
        }

        // 内部事件监听
        this.instance.listener.rangeStyleChange = function (payload) {
          // 控件类型
          payload.type === that.commonConstants.editor.ElementType.SUBSCRIPT
            ? subscriptDom.classList.add('active')
            : subscriptDom.classList.remove('active')
          payload.type === that.commonConstants.editor.ElementType.SUPERSCRIPT
            ? superscriptDom.classList.add('active')
            : superscriptDom.classList.remove('active')
          payload.type === that.commonConstants.editor.ElementType.SEPARATOR
            ? separatorDom.classList.add('active')
            : separatorDom.classList.remove('active')
          separatorOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          if (payload.type === that.commonConstants.editor.ElementType.SEPARATOR) {
            const separator = payload.dashArray.join(',') || '0,0'
            const curSeparatorDom = separatorOptionDom.querySelector(`[data-separator='${separator}']`)
            if (curSeparatorDom) {
              curSeparatorDom.classList.add('active')
            }
          }
          // 富文本
          fontOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          const curFontDom = fontOptionDom.querySelector(
            `[data-family='${payload.font}']`
          )
          if (curFontDom) {
            fontSelectDom.innerText = curFontDom.innerText
            fontSelectDom.style.fontFamily = payload.font
            curFontDom.classList.add('active')
          }

          sizeOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          const curSizeDom = sizeOptionDom.querySelector(
            `[data-size='${payload.size}']`
          )
          if (curSizeDom) {
            sizeSelectDom.innerText = curSizeDom.innerText
            curSizeDom.classList.add('active')
          } else {
            sizeSelectDom.innerText = `${payload.size}`
          }

          payload.bold
            ? boldDom.classList.add('active')
            : boldDom.classList.remove('active')
          payload.italic
            ? italicDom.classList.add('active')
            : italicDom.classList.remove('active')
          payload.underline
            ? underlineDom.classList.add('active')
            : underlineDom.classList.remove('active')
          payload.strikeout
            ? strikeoutDom.classList.add('active')
            : strikeoutDom.classList.remove('active')
          if (payload.color) {
            colorDom.classList.add('active')
            colorControlDom.value = payload.color
            colorSpanDom.style.backgroundColor = payload.color
          } else {
            colorDom.classList.remove('active')
            colorControlDom.value = '#000000'
            colorSpanDom.style.backgroundColor = '#000000'
          }
          if (payload.highlight) {
            highlightDom.classList.add('active')
            highlightControlDom.value = payload.highlight
            highlightSpanDom.style.backgroundColor = payload.highlight
          } else {
            highlightDom.classList.remove('active')
            highlightControlDom.value = '#ffff00'
            highlightSpanDom.style.backgroundColor = '#ffff00'
          }

          // 行布局
          leftDom.classList.remove('active')
          centerDom.classList.remove('active')
          rightDom.classList.remove('active')
          alignmentDom.classList.remove('active')
          if (payload.rowFlex && payload.rowFlex === 'right') {
            rightDom.classList.add('active')
          } else if (payload.rowFlex && payload.rowFlex === 'center') {
            centerDom.classList.add('active')
          } else if (payload.rowFlex && payload.rowFlex === 'alignment') {
            alignmentDom.classList.add('active')
          } else {
            leftDom.classList.add('active')
          }

          // 行间距
          rowOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          const curRowMarginDom = rowOptionDom.querySelector(
            `[data-rowmargin='${payload.rowMargin}']`
          )
          curRowMarginDom.classList.add('active')

          // 功能
          payload.undo
            ? undoDom.classList.remove('no-allow')
            : undoDom.classList.add('no-allow')
          payload.redo
            ? redoDom.classList.remove('no-allow')
            : redoDom.classList.add('no-allow')
          payload.painter
            ? painterDom.classList.add('active')
            : painterDom.classList.remove('active')

          // 标题
          titleOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          if (payload.level) {
            const curTitleDom = titleOptionDom.querySelector(
              `[data-level='${payload.level}']`
            )
            titleSelectDom.innerText = curTitleDom.innerText
            curTitleDom.classList.add('active')
          } else {
            titleSelectDom.innerText = '正文'
            titleOptionDom.querySelector('li:first-child').classList.add('active')
          }

          // 列表
          listOptionDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          if (payload.listType) {
            listDom.classList.add('active')
            const listType = payload.listType
            const listStyle =
              payload.listType === that.commonConstants.editor.ListType.OL ? that.commonConstants.editor.ListStyle.DECIMAL : payload.listType
            const curListDom = listOptionDom.querySelector(
              `[data-list-type='${listType}'][data-list-style='${listStyle}']`
            )
            if (curListDom) {
              curListDom.classList.add('active')
            }
          } else {
            listDom.classList.remove('active')
          }
        }
        this.instance.listener.visiblePageNoListChange = function (payload) {
          const text = payload.map(i => i + 1).join('、')
          document.querySelector('.page-no-list').innerText = text
        }

        this.instance.listener.pageSizeChange = function (payload) {
          document.querySelector(
            '.page-size'
          ).innerText = `${payload}`
        }

        this.instance.listener.intersectionPageNoChange = function (payload) {
          document.querySelector('.page-no').innerText = `${
            payload + 1
          }`
        }

        this.instance.listener.pageScaleChange = function (payload) {
          document.querySelector(
            '.page-scale-percentage'
          ).innerText = `${Math.floor(payload * 10 * 10)}%`
        }

        this.instance.listener.controlChange = function (payload) {
          const disableMenusInControlContext = [
            'table',
            'hyperlink',
            'separator',
            'page-break'
          ]
          // 菜单操作权限
          disableMenusInControlContext.forEach(menu => {
            const menuDom = document.querySelector(
              `.menu-item__${menu}`
            )
            payload
              ? menuDom.classList.add('disable')
              : menuDom.classList.remove('disable')
          })
        }

        this.instance.listener.pageModeChange = function (payload) {
          const activeMode = pageModeOptionsDom.querySelector(
            `[data-page-mode='${payload}']`
          )
          pageModeOptionsDom
            .querySelectorAll('li')
            .forEach(li => li.classList.remove('active'))
          activeMode.classList.add('active')
        }

        const handleContentChange = async function () {
          // 字数
          const wordCount = await that.instance.command.getWordCount()
          document.querySelector('.word-count').innerText = `${
            wordCount || 0
          }`
          // 目录
          if (isCatalogShow) {
            updateCatalog()
          }
        }
        this.instance.listener.contentChange = this.debounce(handleContentChange, 200)
        handleContentChange()

        // 9. 右键菜单注册
        // this.instance.register.contextMenuList([
        //   {
        //     name: '批注',
        //     when: payload => {
        //       return (
        //         !payload.isReadonly &&
        //         payload.editorHasSelection &&
        //         payload.zone === "main"
        //       )
        //     },
        //     callback: (command) => {
        //       new Dialog({
        //         title: '批注',
        //         data: [
        //           {
        //             type: 'textarea',
        //             label: '批注',
        //             height: 100,
        //             name: 'value',
        //             required: true,
        //             placeholder: '请输入批注'
        //           }
        //         ],
        //         onConfirm: payload => {
        //           const value = payload.find(p => p.name === 'value')?.value
        //           if (!value) return
        //           const groupId = command.executeSetGroup()
        //           if (!groupId) return
        //           commentList.push({
        //             id: groupId,
        //             content: value,
        //             userName: 'Hufe',
        //             rangeText: command.getRangeText(),
        //             createdDate: new Date().toLocaleString()
        //           })
        //         }
        //       })
        //     }
        //   },
        //   // {
        //   //   name: '格式整理',
        //   //   icon: 'word-tool',
        //   //   when: payload => {
        //   //     return !payload.isReadonly
        //   //   },
        //   //   callback: (command) => {
        //   //     command.executeWordTool()
        //   //   }
        //   // }
        // ])

        // this.instance.listener.saved = function (payload) {
        //   console.log('elementList: ', payload)
        // }
        //图表
        const chartDom = document.querySelector('.menu-item__chart')
        chartDom.title = `图表`
        chartDom.onclick = function () {
          that.chartModalConfig.show = true;
          that.chartModalForm[7].tableCols = that.tableCols;
          that.chartModalForm[7].tableData = that.docTplCharts;
          console.log('chart')
        }
        //二维码
        const qrCodeDom = document.querySelector('.menu-item__qrcode')
        qrCodeDom.title = `二维码`
        qrCodeDom.onclick = function () {
          that.codeModalForm[3].tableCols = that.codeTableCols;
          that.codeModalForm[3].tableData = that.docTplCodes;
          that.codeModalConfig.show = true;
          that.codeModalConfig.title = "二维码"
          that.codeModalData.codeType = "2";
          console.log('qrcode')
        }
        //条形码
        const barCodeDom = document.querySelector('.menu-item__barcode')
        barCodeDom.title = `条形码`
        barCodeDom.onclick = function () {
          that.codeModalForm[3].tableCols = that.codeTableCols;
          that.codeModalForm[3].tableData = that.docTplCodes;
          that.codeModalConfig.show = true;
          that.codeModalConfig.title = "条形码"
          that.codeModalData.codeType = "1";
          console.log('barcode')
        }
    },
    debounce(func, delay) {
      let timer
      return function (that, ...args) {
        if (timer) {
          window.clearTimeout(timer)
        }
        timer = window.setTimeout(() => {
          func.apply(that, args)
        }, delay)
      }
    },
     scrollIntoView(container, selected) {
      if (!selected) {
        container.scrollTop = 0
        return
      }
      const offsetParents = []
      let pointer = selected.offsetParent
      while (pointer && container !== pointer && container.contains(pointer)) {
        offsetParents.push(pointer)
        pointer = pointer.offsetParent
      }
      const top =
        selected.offsetTop +
        offsetParents.reduce((prev, curr) => prev + curr.offsetTop, 0)
      const bottom = top + selected.offsetHeight
      const viewRectTop = container.scrollTop
      const viewRectBottom = viewRectTop + container.clientHeight
      if (top < viewRectTop) {
        container.scrollTop = top
      } else if (bottom > viewRectBottom) {
        container.scrollTop = bottom - container.clientHeight
      }
    },
    //保存模板数据
    saveTpl(){
      const tplId = this.$route.query.tplId// tplId
      let tplSettings = this.instance.command.getValue();
      let options = this.instance.command.getOptions();
      let paperDirection = options.paperDirection;
      let paperMargin = this.instance.command.getPaperMargin();
      var obj = {
        params:{tplId:tplId,width:tplSettings.width,height:tplSettings.height,
          margins:JSON.stringify(tplSettings.margins),
          header:JSON.stringify(tplSettings.data.header),
          main:JSON.stringify(tplSettings.data.main),
          footer:JSON.stringify(tplSettings.data.footer),
          paperDirection:paperDirection,
          watermark:JSON.stringify(tplSettings.options.watermark),
          docTplCharts:this.docTplCharts,
          docTplCodes:this.docTplCodes,
          margins:JSON.stringify(paperMargin),
          height:options.height,
          width:options.width
        },
        removeEmpty:false,
        url:this.apis.docTpl.saveDocTplSettingsApi,
      }
      this.commonUtil.doPost(obj) .then(response=>{
        if (response.code == "200")
        {
        }
      });
    },
    // 添加数据集
    addDataSets() {
      this.addDatasetsDialogVisiable = true
      this.$nextTick(() => {
        this.sqlText = " ";
      });
    },
     // 执行sql语句并解析
     execSql() {
      this.$refs['sqlRef'].validate((valid) => {
        if (valid) {
          const reportTplId = this.$route.query.tplId// reportTplId
          const obj = {
            url: this.apis.reportDesign.execSqlApi,
            params: { tplId: reportTplId, tplSql: this.sqlText, datasourceId: this.sqlForm.datasourceId, sqlType: this.sqlForm.sqlType,
              inParam: this.procedureInParamTableData.tableData ? JSON.stringify(this.procedureInParamTableData.tableData) : '', outParam: this.procedureOutParamTableData.tableData ? JSON.stringify(this.procedureOutParamTableData.tableData) : '',
              sqlParams: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '' ,
              sqlParams: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '',mongoTable:this.sqlForm.mongoTable,mongoSearchType:this.sqlForm.mongoSearchType},
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
      sqlContent=this.sqlText;
      this.sqlText = format(sqlContent)
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
            this.paramTableData.tableData[result.index].paramPrefix = this.paramForm.paramPrefix
            this.paramTableData.tableData[result.index].datasourceId = this.paramForm.datasourceId
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
              paramPrefix: this.paramForm.paramPrefix,
              datasourceId: this.paramForm.datasourceId,
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
      this.paramForm.paramPrefix = row.paramPrefix
      this.paramForm.datasourceId = row.datasourceId
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
      } else if (type == '2'){
        // 输出参数
        this.commonUtil.moveUp(this.procedureOutParamTableData.tableData, index)
      } else if (type == '3') {
        // sql参数
        this.commonUtil.moveUp(this.paramTableData.tableData, index)
      }
    },
    moveDown(index, type) {
      if (type == '1') {
        // 输入参数
        this.commonUtil.moveDown(this.procedureInParamTableData.tableData, index)
      } else if (type == '2') {
        // 输出参数
        this.commonUtil.moveDown(this.procedureOutParamTableData.tableData, index)
      }else if (type == '3') {
        // sql参数
        this.commonUtil.moveDown(this.paramTableData.tableData, index)
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
    getReportTplDateSource(isEdit) {
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getReportTplDateSourceApi,
        params: { tplId: reportTplId },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.dataSource = response.responseData
          this.changeDatasource(isEdit)
        }
      })
    },
    // 选择数据源修改
    changeDatasource(isEdit) {
      for (let index = 0; index < this.dataSource.length; index++) {
        const element = this.dataSource[index]
        if (this.sqlForm.datasourceId == element.datasourceId) {
          if(!isEdit){
            this.sqlColumnTableData.tableData = []
          }
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
          } else if (element.type == '14'){
              this.datasourceType = '3'
              this.sqlForm.sqlType = 1;
              this.getDatabaseTables()
          } else {
            this.datasourceType = '1'
            this.getDatabaseTables();
          }
          break
        }
      }
    },
    // 编辑数据及
    editDataSet(dataSet) {
      this.addDatasetsDialogVisiable = true
      this.datasourceType = dataSet.datasetType
      this.$nextTick(() => {
        this.sqlText = dataSet.tplSql;
        if(dataSet.datasetType == 3 && dataSet.mongoSearchType == 1){
          this.orderSql = dataSet.mongoOrder;
        }
      })
      this.paramTableData.tableData = eval('(' + dataSet.tplParam + ')')
      this.sqlColumnTableData.tableData = dataSet.columns?dataSet.columns:[];
      if(!dataSet.columns || dataSet.columns.length == 0){
        this.getDatasetColumns(dataSet,null,true);
      }
      this.sqlColumnTableData.tablePage.pageTotal = dataSet.columns?this.sqlColumnTableData.tableData.length:0
      this.sqlForm.datasetName = dataSet.datasetName
      this.sqlForm.datasourceId = dataSet.datasourceId
      this.sqlForm.id = dataSet.id
      this.sqlForm.sqlType = dataSet.sqlType
      this.sqlForm.groupId = dataSet.groupId
      this.sqlForm.isCommon = dataSet.isCommon
      this.sqlForm.isConvert = dataSet.isConvert
      this.sqlForm.valueField = dataSet.valueField
      this.sqlForm.headerName = dataSet.headerName
      if(typeof dataSet.fixedColumn === 'string'){
        this.sqlForm.fixedColumn = JSON.parse(dataSet.fixedColumn);
      }else{
        this.sqlForm.fixedColumn = dataSet.fixedColumn;
      }
      this.sqlForm.mongoTable = dataSet.mongoTable;
      this.sqlForm.mongoSearchType = dataSet.mongoSearchType;
      if(dataSet.subParamAttrs){
        this.subParamAttrs = JSON.parse(dataSet.subParamAttrs);
      }else{
        this.subParamAttrs = [];
      }
      if (dataSet.sqlType == 2) {
        this.procedureInParamTableData.tableData = JSON.parse(dataSet.inParam)
        this.procedureOutParamTableData.tableData = JSON.parse(dataSet.outParam)
      }
      this.getReportTplDateSource(true)
    },
    // 获取数据集
    getDataSets() {
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getDataSetsApi,
        params: { tplId: reportTplId,commonType:2},
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.datasets = response.responseData
          this.chartModalForm[3].options = response.responseData
          this.codeModalForm[1].options = response.responseData
        }
      })
    },
    // 添加数据集
    addDataSet() {
      const reportTplId = this.$route.query.tplId// reportTplId
      let paginationValidate = true
      let tplSql = ''
      let orderSql = '';
      if (this.datasourceType == '1' || this.datasourceType == '3') {
        tplSql = this.sqlText;
        if (this.datasourceType == '1' && (tplSql == null || tplSql == '')) {
          this.commonUtil.showMessage({ message: 'sql语句不能为空', type: this.commonConstants.messageType.error })
          return
        }
      }
      if(this.datasourceType == '3' && this.$refs.orderCodeMirror){
        orderSql = this.orderSql;
      }
      this.$refs['sqlRef'].validate((valid) => {
        if (valid) {
          const obj = {
            url: this.apis.reportDesign.addDataSetApi,
            params: { tplId: reportTplId,groupId: this.sqlForm.groupId, datasetType: this.datasourceType, sqlType: this.sqlForm.sqlType, isCommon:this.sqlForm.isCommon,commonType:2,isConvert:this.sqlForm.isConvert,valueField:this.sqlForm.valueField,headerName:this.sqlForm.headerName,fixedColumn:JSON.stringify(this.sqlForm.fixedColumn),tplSql: tplSql, tplParam: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '', datasourceId: this.sqlForm.datasourceId, datasetName: this.sqlForm.datasetName, id: this.sqlForm.id,
              inParam: this.procedureInParamTableData.tableData ? JSON.stringify(this.procedureInParamTableData.tableData) : '', outParam: this.procedureOutParamTableData.tableData ? JSON.stringify(this.procedureOutParamTableData.tableData) : '',
              subParamAttrs: JSON.stringify(this.subParamAttrs),mongoTable:this.sqlForm.mongoTable,mongoOrder:orderSql,mongoSearchType:this.sqlForm.mongoSearchType},
            removeEmpty: false
          }
          this.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              this.getDataSets()
              this.getTplGroupDatasets();
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
      if (this.datasourceType == '1' || this.datasourceType == '3') {
        this.sqlText = " ";
        if(this.$refs.orderCodeMirror){
          this.orderSql = "";
        }
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
      // if (o.isActive) {
      //   o.isActive = false;
      // } else {
      //   o.isActive = true;
      //   if(!o.columns || o.columns == null || o.columns.length == 0){
      //     await this.getDatasetColumns(o)
      //   }
      // }
      // if (o.datasetType == '2') {
      //   this.getApiDefaultRequestResult(o)
      // }
      this.datasetItemActive = o.id
      await this.getDatasetColumns(o)
      if (o.datasetType == '2') {
        this.getApiDefaultRequestResult(o)
      }
      this.$forceUpdate()
    },
    getDatasetColumns(element,type,isEdit) {
      const obj = {
        url: this.apis.reportDesign.getDataSetColumnsApi,
        params: { id: element.id },
        removeEmpty: false
      }
      var that = this;
      this.commonUtil.doPost(obj).then(response => {
        element.columns = response.responseData;
        that.dataSetAttrs = element.columns;
        if(type == "1"){
          that.chartModalForm[4].options = element.columns;
          that.chartModalForm[5].options = element.columns;
          that.chartModalForm[6].options = element.columns;
        }else if(type == "2"){
          that.codeModalForm[2].options = element.columns;
        }
        if(isEdit){
          this.sqlColumnTableData.tableData = response.responseData
          this.sqlColumnTableData.tablePage.pageTotal = response.responseData.length
        }
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
    copyAttr(type,datasetName,colulmnName,isInsert){
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
      if(isInsert){
        this.instance.command.executePaste();
        this.commonUtil.showMessage({
          message: '属性已添加到文档光标处，如未添加成功可直接使用ctrl+v粘贴到对应的位置',
          type: this.commonConstants.messageType.success,
        });
      }else{
        this.commonUtil.showMessage({
          message: '复制成功',
          type: this.commonConstants.messageType.success,
        });
      }
      
      // 
    },
    confimModal(){
      var that = this;
      this.$refs['commonModal'].$refs['modalFormRef'].validate((valid) => {
        if(valid){
          if(that.modalConfig.type == "1"){
            //超链接
            that.instance.command.executeHyperlink({
              type: 'hyperlink',
              value: '',
              url:that.modalData.url,
              valueList: that.commonUtil.splitText(that.modalData.name).map(n => ({
                value: n,
                size: 16
              }))
            })
          }else if(that.modalConfig.type == "2"){
            //水印
            that.instance.command.executeAddWatermark({
              data: that.modalData.data,
              color: "#c0c0c0",
              size: 100
            })
          }
          that.closeModal();
        }else{
          return false;
        }
        
      });
      
    },
    closeModal(){
      this.modalConfig.title = "";
      this.modalConfig.show = false;
      this.modalConfig.type = "";
      this.modalForm = [];
      this.modalData = {};
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
      this.getTplGroupDatasets();
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
    uploadDocx(evt){
      const files = evt.target.files
      if (files == null || files.length == 0) {
        alert('请选择文件')
        return
      }
      // 获取文件名
      const name = files[0].name
      // 获取文件后缀
      const suffixArr = name.split('.');
      const suffix = suffixArr[suffixArr.length - 1]
      if(suffix != "docx"){
        this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.upload.filetype",['docx']), type: this.commonConstants.messageType.error })
        return;
      }
      var that = this
      const formData = new FormData();
      formData.append("file",files[0]);
      formData.append("tplId",this.$route.query.tplId);
      let config = {
          headers: {'Content-Type': 'multipart/form-data',
          'Authorization':localStorage.getItem(that.commonConstants.sessionItem.authorization),
          thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)}
      }
      try {
        Axios.post(that.apis.docTpl.uploadDocxApi, formData, config)
      .then(res => {
          if(res.data.code == "200")
          {
            let response = res.data;
            that.instance.command.executeSetValue({
                "header":  JSON.parse(response.responseData.header),
                "main": JSON.parse(response.responseData.main),
                "footer": JSON.parse(response.responseData.footer)
            });
            //设置纸张大小并回显
            that.instance.command.executePaperSize(response.responseData.width,response.responseData.height);
            const paperSizeDom = document.querySelector('.paper-size');
            const paperSizeDomOptionsDom = paperSizeDom.querySelector('.options')
            let pagers = paperSizeDomOptionsDom.querySelectorAll('li');
            for (let index = 0; index < pagers.length; index++) {
              let element = pagers[index];
              element.classList.remove('active')
              if(element.dataset.paperSize == (response.responseData.height+"*"+response.responseData.width)||element.dataset.paperSize == (response.responseData.width+"*"+response.responseData.height))
              {
                element.classList.add('active')
              }
             
            }
            //设置纸张方向并回显
            let pageDirection = response.responseData.paperDirection
            that.instance.command.executePaperDirection(pageDirection);
            const paperDirectionDom = document.querySelector('.paper-direction')
            const paperDirectionDomOptionsDom = paperDirectionDom.querySelector('.options')
            let pagerDirections = paperDirectionDomOptionsDom.querySelectorAll('li')
            for (let index = 0; index < pagerDirections.length; index++) {
              let element = pagerDirections[index];
              element.classList.remove('active')
              if(element.dataset.paperDirection ==  pageDirection){
                element.classList.add('active')
              }
            }
          }else{
              that.commonUtil.showMessage({message:res.data.message,type: res.data.msgLevel})
          }
          evt.target.value = ''
      })
      } catch (error) {
          evt.target.value = ''
          that.loading = false;
      }
    },
    confimChartModal(){
      var that = this;
      this.$refs['chartModalRef'].$refs['modalFormRef'].validate((valid) => {
        if(valid){
          const timestamp = new Date().getTime();
          let chartUrl = that.chartModalData.chartUrl;
          const tplId = that.$route.query.tplId// tplId
          if(that.chartModalData.index==null || that.chartModalData.chartType != that.docTplCharts[that.chartModalData.index].chartType){
            chartUrl = that.chartUrlPrefix+that.chartModalData.chartType+".png?t="+timestamp;
            that.instance.command.executeImage({
              value:chartUrl,
              width: 520,
              height: 250,
            })
          }
          
          let chartObj = {
            chartName:that.chartModalData.chartName,
            showChartName:that.chartModalData.showChartName,
            chartType:that.chartModalData.chartType,
            datasetName:that.chartModalData.datasetName,
            categoryField:that.chartModalData.categoryField,
            valueField:that.chartModalData.valueField,
            chartUrl:chartUrl,
            tplId:tplId,
            datasetId:that.chartModalData.datasetId,
            datasetName:that.chartModalData.datasetName,
            seriesField:that.chartModalData.seriesField
          }
          if(that.chartModalData.index != null){
            that.docTplCharts[that.chartModalData.index] = chartObj;
            that.closeChartModal(true);
          }else{
            that.docTplCharts.push(chartObj);
            that.closeChartModal(false);
          }
        }else{
          return false;
        }
      });
      
    },
    closeChartModal(isKeepOpen){
      this.$refs['chartModalRef'].$refs['modalFormRef'].resetFields();//校验重置
      this.commonUtil.clearObj(this.chartModalData);//清空modalData
      this.chartModalForm[4].options = [];
      this.chartModalForm[5].options = [];
      this.chartModalForm[6].options = [];
      if(!isKeepOpen){
        this.chartModalConfig.show = false;
      }
      this.$refs['chartModalRef'].$forceUpdate();
      this.changeChartType();
    },
    changeDatasets(datasetId){
      if(datasetId){
        for (let index = 0; index < this.datasets.length; index++) {
          const element = this.datasets[index];
          if(element.id == datasetId){
            if(!element.columns || element.columns.length == 0){
              this.getDatasetColumns(element,"1");
            }else{
              this.chartModalForm[4].options = element.columns;
              this.chartModalForm[5].options = element.columns;
              this.chartModalForm[6].options = element.columns;
            }
            this.chartModalData.datasetName = element.datasetName;
            break;
          }
        }
      }else{
        this.chartModalForm[4].options = [];
        this.chartModalForm[5].options = [];
        this.chartModalForm[6].options = [];
        this.chartModalData.categoryField = null;
        this.chartModalData.valueField = null;
      }
      // await this.getDatasetColumns(o);
    },
    changeChartType(){
      if(this.chartModalData.chartType == "pie" || this.chartModalData.chartType == "pie3d"){
        this.chartModalForm[6].rules.required = false;
        this.chartModalForm[6].show = false;
      }else{
        this.chartModalForm[6].rules.required = false;
        this.chartModalForm[6].show = true;
      }
    },
    deleteChart(row,index){
      this.docTplCharts.splice(index,1)
    },
    doCopy(item,isInsert){
      let text = item.value;
      if(item.type == "number"){
        text = '<if test="'+item.value+'!=null' + '"> \n' 
        text = text + "  and " + item.column + " = #{"+item.value+"} \n" + "</if>"
      }else{
        text = '<if test="'+item.value+'!=null and ' + item.value + "!=''" + '">\n' 
        text = text + "  and " + item.column + " = #{"+item.value+"} \n" + "</if>"
      }
      if (!isInsert) {
        const input = document.getElementById('clipboradInput'); // 承载复制内容
        input.value = text; // 修改文本框的内容
        input.select(); // 选中文本
        document.execCommand('copy'); // 执行浏览器复制命令
        this.commonUtil.showMessage({
          message: '复制成功',
          type: this.commonConstants.messageType.success,
        });
      } else {
        this.addComment(text);
      }
    },
    copyColumn(datasetName, columnName){
      let text = "";
      if(datasetName){
        text = datasetName + '.${' + columnName + '}';
      }else{
        text = columnName;
      }
      const input = document.getElementById('clipboradInput'); // 承载复制内容
      input.value = text; // 修改文本框的内容
      input.select(); // 选中文本
      document.execCommand('copy'); // 执行浏览器复制命令
      this.commonUtil.showMessage({message:"复制成功",type: this.commonConstants.messageType.success})
    },
    addComment(val){
      let pos1 = this.$refs.codeMirror.cminstance.getCursor();
      let pos2 = {};
      pos2.line = pos1.line;
      pos2.ch = pos1.ch;
      this.$refs.codeMirror.cminstance.replaceRange(val,pos2);
    },
    //获取数据源的表结构
    getDatabaseTables(){
      var obj = {
          params:{id:this.sqlForm.datasourceId},
          url:this.apis.reportDatasource.getDatabseTablesApi
        }
        var that = this;
        this.datasourceTableName = "";
        this.tableColumns = [];
        this.commonUtil.doPost(obj) .then(response=>{
          if(response.code == "200")
          {
            that.dataSourceTables = response.responseData;
          }
        });
    },
    //获取表对应的列
    getTableColumns(){
      var key = this.sqlForm.datasourceId + "_" + this.datasourceTableName;
      var columns = this.datasourceTableColumns[key];
      if(columns)
      {
          this.tableColumns = columns;
      }else{
          var obj = {
              params:{datasourceId:this.sqlForm.datasourceId,tplSql:"select * from " + this.datasourceTableName,sqlType:1},
              url:this.apis.reportDesign.execSqlApi
            }
            var that = this;
            this.commonUtil.doPost(obj) .then(response=>{
              if(response.code == "200")
              {
                that.datasourceTableColumns[key] = response.responseData;
                that.tableColumns = this.datasourceTableColumns[key];
              }
            });
      }
    },
    getWhereByColumn(type,column,isInsert){
      let text = "";
      let columnType = column.dataType.toLowerCase();
      if(type == 1){
        text = column.name;
      }else if(type == 2){
        text = this.datasourceTableName+"."+column.name;
      }else if(type == 3){
        if(columnType.indexOf("varchar")>=0){
          text = '<if test="'+column.name+'!=null and ' + column.name + "!=''" + '">\n' 
          text = text + "  and " + column.name + " = #{"+column.name+"} \n" + "</if>"
        }else if(columnType.indexOf("int")>=0 || columnType.indexOf("number")>=0 || columnType.indexOf("date")>=0 || columnType.indexOf("time")>=0){
          text = '<if test="'+column.name+'!=null' + '"> \n' 
          text = text + "  and " + column.name + " = #{"+column.name+"} \n" + "</if>"
        }else{
          text = '<if test="'+column.name+'!=null and ' + column.name + "!=''" + '">\n' 
          text = text + "  and " + column.name + " = #{"+column.name+"} \n" + "</if>"
        }
      }else if(type == 4){
        text = '<if test="'+column.name+'!=null and ' + column.name + ".size() > 0" + '">\n' 
        text = text + "  and " + column.name + " in\n"
        text = text + ' <foreach collection="'+column.name + '" open="(" separator="," close=")" item="item" index="index">\n #{item} \n</foreach>\n</if>'
      }else{
          text = '<if test="'+column.name+'!=null and ' + column.name + "!=''" + '">\n' 
          text = text + "  and " + column.name + " = #{"+column.name+"} \n" + "</if>"
      }
      if (isInsert) {
        this.addComment(text)
      } else {
        const input = document.getElementById('clipboradInput') // 承载复制内容
        input.value = text // 修改文本框的内容
        input.select() // 选中文本
        document.execCommand('copy') // 执行浏览器复制命令
        this.commonUtil.showMessage({
          message: '复制成功',
          type: this.commonConstants.messageType.success,
        });
      }
    },
    getWhereByParam(row){
      let text = "";
      if(row.paramType == "varchar" || row.paramType == "select" || row.paramType == "treeSelect"){
        text = '<if test="'+row.paramCode+'!=null and ' + row.paramCode + "!=''" + '">\n' 
        text = text + "  and " + row.paramCode + " = #{"+row.paramCode+"} \n" + "</if>"
      }else if(row.paramType == "number" || row.paramType == "date"){
        text = '<if test="'+row.paramCode+'!=null' + '"> \n' 
        text = text + "  and " + row.paramCode + " = #{"+row.paramCode+"} \n" + "</if>"
      }else if(row.paramType == "mutiselect" || row.paramType == "multiTreeSelect"){
        text = '<if test="'+row.paramCode+'!=null and ' + row.paramCode + ".size() > 0" + '">\n' 
        text = text + "  and " + row.paramCode + " in\n"
        text = text + '   <foreach collection="'+row.paramCode + '" open="(" separator="," close=")" item="item" index="index">\n   #{item} \n  </foreach>\n</if>'
      }
      this.addComment(text)
    },
    closeCodeModal(isKeepOpen){
      this.$refs['codeModalRef'].$refs['modalFormRef'].resetFields();//校验重置
      this.commonUtil.clearObj(this.codeModalData);//清空modalData
      this.codeModalForm[2].options = [];
      if(!isKeepOpen){
        this.codeModalConfig.show = false;
      }
      this.$refs['codeModalRef'].$forceUpdate();
    },
    changeCodeDatasets(datasetId){
      if(datasetId){
        for (let index = 0; index < this.datasets.length; index++) {
          const element = this.datasets[index];
          if(element.id == datasetId){
            if(!element.columns || element.columns.length == 0){
              this.getDatasetColumns(element,"2");
            }else{
              this.codeModalForm[2].options = element.columns;
            }
            this.codeModalData.datasetName = element.datasetName;
            break;
          }
        }
      }else{
        this.codeModalForm[3].options = [];
        this.codeModalData.valueField = null;
      }
    },
    confirmCodeModal(){
      var that = this;
      this.$refs['codeModalRef'].$refs['modalFormRef'].validate((valid) => {
        if(valid){
          const timestamp = new Date().getTime();
          let type = "barcode"
          if(that.codeModalData.codeType == "2"){
            type = "qrcode";
          }
          let chartUrl = that.codeModalData.codeUrl
          const tplId = that.$route.query.tplId// tplId
          if(this.codeModalData.index == null){
            chartUrl = that.chartUrlPrefix+type+".png?t="+timestamp;
            that.instance.command.executeImage({
              value:chartUrl,
              width: that.codeModalData.codeType == "2"?400:354,
              height: that.codeModalData.codeType == "2"?400:120,
            })
          }
          
          let chartObj = {
            codeName:that.codeModalData.codeName,
            codeType:that.codeModalData.codeType,
            datasetName:that.codeModalData.datasetName,
            valueField:that.codeModalData.valueField,
            codeUrl:chartUrl,
            tplId:tplId,
            datasetId:that.codeModalData.datasetId,
          }
          if(this.codeModalData.index != null){
            that.docTplCodes[that.codeModalData.index] = chartObj;
            that.closeCodeModal(true);
          }else{
            that.docTplCodes.push(chartObj);
            that.closeCodeModal(false);
          }
        }else{
          return false;
        }
      });
    },
    deleteCode(row,index){
      this.docTplCodes.splice(index,1)
    },
    closePaperMarginModal(){
      this.paperMarginModalConfig.show = false;
    },
    confirmPaperMarginModal(){
      var that = this;
      this.$refs['paperMarginModalRef'].$refs['modalFormRef'].validate((valid) => {
        if(valid){
          that.closePaperMarginModal();
          that.instance.command.executeSetPaperMargin([
            Number(that.paperMarginModalData.topMargin),
            Number(that.paperMarginModalData.rightMargin),
            Number(that.paperMarginModalData.bottomMargin),
            Number(that.paperMarginModalData.leftMargin)
          ])
        }else{
          return false;
        }
      });
    },
    editChart(row,index){
      this.chartModalData.chartName = row.chartName;
      this.chartModalData.showChartName = row.showChartName;
      this.chartModalData.chartType = row.chartType;
      this.chartModalData.datasetName = row.datasetName;
      this.chartModalData.categoryField = row.categoryField;
      if(typeof row.valueField === 'string'){
        this.chartModalData.valueField = JSON.parse(row.valueField);
      }else{
        this.chartModalData.valueField = row.valueField;
      }
      this.chartModalData.datasetId = row.datasetId;
      this.chartModalData.datasetName = row.datasetName;
      if(typeof row.seriesField === 'string'){
        this.chartModalData.seriesField = JSON.parse(row.seriesField);
      }else{
        this.chartModalData.seriesField = row.seriesField;
      }
      this.chartModalData.chartUrl = row.chartUrl;
      this.chartModalData.index = index;
      this.changeChartType();
      this.changeDatasets(row.datasetId);
    },
    editCode(row,index){
      this.codeModalData.codeName = row.codeName;
      this.codeModalData.codeType = row.codeType;
      this.codeModalData.valueField = row.valueField;
      this.codeModalData.datasetId = row.datasetId;
      this.codeModalData.codeUrl = row.codeUrl;
      this.codeModalData.index = index;
      this.changeCodeDatasets(row.datasetId);
    },
    updateOptions(key1,key2,value){
      const options = this.instance.command.getOptions()
      if(key2){
        options[key1][key2] = value;
        this.instance.command.executeUpdateOptions(options)
      }else{
        options[key1] = value;
        this.instance.command.executeUpdateOptions(options)
      }
    },
    // 左侧折叠
    switchOpenLeftPanel() {
      this.leftOpen = !this.leftOpen
    },
    // 关闭分组设置弹框
    closeGroupDialog() {
      this.groupSetVisible = false
    },
    // 打开分组编辑、添加弹框
    openGroupHandleDialog(item) {
      this.groupHandleVisible = true
      this.groupForm = {
        groupName: item ? item.groupName : undefined,
        id: item ? item.id : undefined
      }
    },
    // 关闭分组编辑、添加弹框
    closeGroupHandleDialog() {
      this.groupHandleVisible = false
    },
    // 添加或编辑分组
    addOrEditGroup() {
      if (!this.groupForm.groupName) {
        this.$message.error('请输入分组名称')
        return
      }
      this.groupHandleLoading = true
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.groupForm.id ? this.apis.reportDesign.updateGroupApi : this.apis.reportDesign.insertGroupApi,
        params: { tplId: reportTplId, ...this.groupForm },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        this.groupHandleLoading = false
        if (response.code === '200') {
          this.getTplGroupDatasets()
          this.groupHandleVisible = false
        }
      })
    },
    // 删除分组
    deleteGroup(dataSet) {
      const params = {
        url: this.apis.reportDesign.deleteGroupApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.deleteDataSetCallback,
        params: { id: dataSet.id },
        type: 'get'
      }
      // 弹出删除确认框
      this.commonUtil.showConfirm(params)
    },
    // 获取分组数据集
    getTplGroupDatasets() {
      this.dataGroupLoading = true
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getTplGroupDatasetsApi,
        params: { tplId: reportTplId ,commonType:2},
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        this.groupList = response.responseData
        const accarr = this.groupList.map(item => {
          return {
            id: item.id,
            groupName: item.id,
            data: item.data.map(daset => {
              return {
                id: daset.id,
                name: daset.datasetName
              }
            })
          }
        })
        this.dataGroupLoading = false
      })
    },
  },
//使用mounted的原因是因为在mounted中dom已经加载完毕，否则会报错，找不到getAttribute这个方法
  mounted() {
    this.getDocTplSettings();
    this.getReportTplDateSource();
    this.getTplGroupDatasets();
    this.getDataSets();
    this.designHeight = document.body.clientHeight - 46 - 10;
    var that = this;
    window.onresize = function () {
      that.designHeight = document.body.clientHeight - 46 - 10;
    };
  },
  computed: {
    // 被过滤展示的数据集列表
    displayGroupList() {
      if (this.datasetKeyword) {
        return this.groupList.map(group => ({
          ...group,
          data: group.data.filter(item => item.datasetName.includes(this.datasetKeyword))
        })).filter(group => group.data.length > 0) // 只保留有匹配项的组
      }
      return this.groupList
    },
    // 过滤后的数据集字段列表
    displayFields() {
      let dataArr = []
      this.groupList.forEach(element => {
        dataArr = dataArr.concat(element.data)
      })
      const fileds = this.datasetItemActive ? dataArr.find(item => item.id === this.datasetItemActive)?.columns || [] : []
      return this.filedKeyword ? fileds.filter(item => item.columnName.includes(this.filedKeyword)) : fileds
    },
    // 数据集id对应的名称
    datasetItem() {
      let dataArr = []
      this.groupList.forEach(element => {
        dataArr = dataArr.concat(element.data)
      })
      return dataArr.find(item => item.id === this.datasetItemActive) || {}
    },
  },
};