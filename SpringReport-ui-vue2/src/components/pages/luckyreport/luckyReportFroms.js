/*
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-01-22 15:50:34
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-08 07:01:47
 */
import { codemirror } from 'vue-codemirror'
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
import vuedraggable from 'vuedraggable'
import Axios from 'axios'

export default {
  components: {
    vuedraggable
  },
  data() {
    return {
      isThirdParty: 2, // 是否第三方iframe调用
      rightOpen: true, // 左侧展开
      leftOpen: true, // 右侧展开
      rightFormCollapse: ['generalConfig', 'subtotalCells', 'subtotalAttribute', 'cellDiff', 'cellFilter', 'cellHide'],
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
      selectVariableOpen: true, // 数据集选择变量展开

      users: [],
      headerUsers: [],
      loading: false,
      loadingText: '文件上传中，请耐心等待...',
      tplName: '',
      tabIndex: 1,
      activeDatasetsName: '',
      addDatasetsDialogVisiable: false, // 添加数据集页面是否显示
      datasets: [], // 数据集
      sqlForm: {
        datasetName: '',
        datasourceId: '',
        id: '',
        sqlType: 1,
        groupId: ''
      },
      dataSource: [], // 模板数据源
      dataSourceTables: [], // 数据源解析的表
      datasourceTableColumns: {}, // 表对应的列
      tableColumns: [],
      isParamMerge: true,
      paramForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: '', // 参数类型
        paramDefault: '', // 默认值
        paramRequired: '', // 是否必选
        selectContent: '', // 下拉选择内容
        selectType: '', // 内容来源
        isRelyOnParams: '', // 是否依赖其他参数
        relyOnParams: '', // 依赖参数代码
        paramHidden: '', // 是否隐藏 1是 2否
        checkStrictly: '', // 父子联动 1是 2否
        paramPrefix: '', // 参数前缀
        dateFormat: ''// 日期格式
      },
      procedureParamForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: '', // 参数类型
        paramDefault: ''// 默认值
      },
      procedureOutParamForm: {
        paramName: '', // 参数名称
        paramCode: '', // 参数编码
        paramType: ''// 参数类型
      },
      selectContentSuggestion: '', // 下拉选择自定义内容提示
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
      // 参数表单对应的参数
      paginationForm: {
        isPagination: 2, // 是否分页
        pageCount: 100, // 每页显示条数，默认为100
        currentPageAttr: '', // 当前页码属性
        pageCountAttr: '', // 每页显示条数属性
        totalAttr: ''// 数据总条数属性
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
      blockVisiable: false, // 循环块对话框是否显示
      blockForm: {
        startCell: '',
        endCell: ''
      },
      // blockData:[],//循环块数据
      sheetBlockData: [],
      blockData: {}, // 循环块数据
      datasourceType: '1', // 1数据库 2api
      dragEndR: 0, // 拖拽停止单元格横坐标
      dragEndC: 0, // 拖拽停止单元格纵坐标
      cellDatas: [],
      extraCustomCellConfigs: {}, // 单元格额外的自定义配置
      cellFormats: {}, // 单元格格式
      title: '报表', // 报表标题
      cellCompareForm: {
        sheetName: '',
        coordinate: '',
        cellType: '',
        compareType: '',
        index: null
      },
      cellCompareVisiable: false,
      sheets: [],
      cellForm: { // 自定义单元格属性
        cellExtend: '1', // 扩展方向1不扩展 2纵向扩展 2横向扩展
        aggregateType: 'list', // 聚合类型 list：列表 group：分组 summary汇总
        digit: '2', // 小数位数
        cellFunction: '', // 函数
        dataFrom: 1, // 数据来源
        dependencyCell: '', // 数据来源单元格
        groupSummaryDependencyr: '', // 分组依赖行号
        groupSummaryDependencyc: '', // 分组依赖列号
        valueType: '1', // 值类型 1文本 2数值 3日期 4下拉单选
        require: false, // 必填项
        lengthValid: false, // 长度校验
        minLength: null, // 最小长度
        maxLength: null, // 最大长度
        textValidRule: '0', // 文本校验规则 0无 1邮箱 2手机号 3座机号 4身份证 99自定义
        regex: '', // 自定义正则表达式
        numberRangeValid: false, // 数值大小校验
        minValue: null, // 最小值
        maxValue: null, // 最大值
        digit: '0', // 小数位数
        dateFormat: 'yyyy-MM-dd', // 日期格式
        datasourceId: '', // 下拉选择数据源
        dictType: '', // 数据字典类型
        allowEdit: true, // 是否允许编辑
        otherCellCompare: false,
        cellType: '1',
        compareCells: [],
        warning: false, // 是否预警
        threshold: '80', // 预警阈值
        warningColor: '#FF0000',
        warningContent: '', // 预警内容
        warningRules: '>=', // 预警规则
        cellconditions: [], // 单元格过滤条件
        filterType: 'and',
        groupProperty: '', // 分组属性
        unitTransfer: false, // 是否数值单位转换
        transferType: 1, // 1 乘法 2除法
        multiple: '', // 倍数
        isDrill: false, // 是否下钻
        drillId: '', // 下钻报表id
        drillAttrs: ''// 下钻参数属性，多个用,分割
      },
      // 单元格属性默认值
      defaultCellForm: { // 自定义单元格属性
        cellExtend: '1', // 扩展方向1不扩展 2纵向扩展 2横向扩展
        aggregateType: 'list', // 聚合类型 list：列表 group：分组 summary汇总
        digit: '2', // 小数位数
        cellFunction: '', // 函数
        dataFrom: 1, // 数据来源
        dependencyCell: '', // 数据来源单元格
        valueType: '1', // 值类型 1文本 2数值 3日期 4下拉单选
        require: false, // 必填项
        lengthValid: false, // 长度校验
        minLength: null, // 最小长度
        maxLength: null, // 最大长度
        textValidRule: '0', // 文本校验规则 0无 1邮箱 2手机号 3座机号 4身份证 99自定义
        regex: '', // 自定义正则表达式
        numberRangeValid: false, // 数值大小校验
        minValue: null, // 最小值
        maxValue: null, // 最大值
        digit: '0', // 小数位数
        dateFormat: 'yyyy-MM-dd', // 日期格式
        datasourceId: '', // 下拉选择数据源
        dictType: '', // 数据字典类型
        allowEdit: true, // 是否允许编辑 true是 false否
        otherCellCompare: false,
        cellType: '1',
        compareCells: [],
        warning: false, // 是否预警
        threshold: '80', // 预警阈值
        warningColor: '#FF0000',
        warningContent: '', // 预警内容
        warningRules: '>=', // 预警规则
        cellconditions: [], // 单元格过滤条件
        groupProperty: '', // 分组属性
        unitTransfer: false, // 是否数值单位转换
        transferType: 1, // 1 乘法 2除法
        multiple: '', // 倍数
        isDrill: false, // 是否下钻
        drillId: '', // 下钻报表id
        drillAttrs: ''// 下钻参数属性，多个用,分割
      },
      cellConditionVisiable: false, // 单元格过滤条件对话框
      cellConditionForm: {
        index: null,
        property: '', // 属性
        operator: '', // 操作符
        type: '', // 数据类型
        value: '', // 条件
        dateFormat: ''// 日期格式
      },
      dictTypes: [], // 字典类型
      datasourceDialog: false,
      datasourceAttrDialog: false,
      isEditDatasourceAttr: false,
      datasourceAttrForm: {
        name: ''
      },
      datasourceColumnDialog: false,
      datasourceColumnForm: {
        columnName: '', // 数据列
        cellCoords: ''// 单元格坐标
      },
      datasourceKeyDialog: false,
      datasourceKeyForm: {
        columnName: '', // 数据列
        idType: ''// 1 自定义填写 2雪花算法 3自增主键
      },
      sheetDatasource: {}, // sheet填报属性
      datasources: [],
      datasourceAttr: {
        name: '', // 名称
        datasourceId: '', // 数据源id
        table: '', // 表名
        keys: [], // 主键
        tableDatas: []// 属性对应关系
      },
      delSheetsIndex: [], // 删除的sheet index
      reportTpls: [], // 下钻报表
      sheetOptions: {
        container: 'luckysheet', // luckysheet为容器id
        title: '', // 表 头名
        lang: 'zh', // 中文
        // plugins: ['chart'],
        fontList: [
          {
            'fontName': '条形码（barCode128）',
            'url': ''
          },
          {
            'fontName': '二维码（qrCode）',
            'url': ''
          }
        ],
        forceCalculation: true,
        index: '0', // 工作表索引
        status: '1', // 激活状态
        order: '0', // 工作表的顺序
        hide: '0', // 是否隐藏
        showtoolbar: true, // 是否显示工具栏
        showinfobar: false, // 是否显示顶部信息栏
        showsheetbar: true, // 是否显示底部sheet按钮
        showsheetbarConfig: {
          add: true, // 新增sheet
          menu: false, // sheet管理菜单
          sheet: true // sheet页显示
        },
        sheetRightClickConfig: {
          delete: true, // 删除
          copy: true, // 复制
          rename: false, // 重命名
          color: false, // 更改颜色
          hide: false, // 隐藏，取消隐藏
          move: false // 向左移，向右移
        },
        allowEdit: true,
        showtoolbarConfig: {
          save: true, // 保存
          preview: true,
          upload: true,
          download: false,
          downloadpdf: false,
          history: false,
          saveAs: false,
          picture: false,
          redo: false,
          undo: false,
          shareMode: false,
          currencyFormat: false, // 货币格式
          percentageFormat: false, // 百分比格式
          numberDecrease: false, // '减少小数位数'
          numberIncrease: false, // '增加小数位数
          moreFormats: true, // '更多格式'
          border: true, // '边框'
          textWrapMode: true, // '换行方式'
          textRotateMode: false, // '文本旋转方式'
	                  image: true, // '插入图片'
          chart: false, // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
          postil: true, // '批注'
          pivotTable: false, // '数据透视表'
          function: true, // '公式'
          frozenMode: true, // '冻结方式'
          sortAndFilter: false, // '排序和筛选'
          conditionalFormat: true, // '条件格式'
          dataVerification: false, // '数据验证'
          splitColumn: false, // '分列'
          screenshot: false, // '截图'
          protection: false, // '工作表保护'
	                print: false // '打印'
        },
        cellRightClickConfig: {
          copyAs: false, // 复制为
          deleteCell: true, // 删除单元格
          hideRow: true, // 隐藏选中行和显示选中行
          hideColumn: true, // 隐藏选中列和显示选中列
          matrix: false, // 矩阵操作选区
          sort: false, // 排序选区
          filter: false, // 筛选选区
          chart: false, // 图表生成
          image: false, // 插入图片
          data: false, // 数据验证
	                  cellFormat: false // 设置单元格格式
        },
        hook: {
          rangeSelect: this.rangeSelect,
          cellDragStop: this.cellDragStop,
          rowInsertAfter: this.rowInsertAfter,
          rowDeleteAfter: this.rowDeleteAfter,
          sheetActivate: this.sheetActivate,
          sheetDeleteBefore: this.sheetDeleteBefore,
          sheetCreateAfter: this.sheetCreateAfter,
          sheetCopyBefore: this.sheetCopyBefore,
          luckysheetDeleteCell: this.luckysheetDeleteCell,
          userChanged: this.userChanged,
          cellUpdated: this.saveTplCache,
          afterUpdateFormatCell: this.saveTplCache,
          changeReportAttr: this.changeReportAttr,
          changeBorder: this.saveTplCache,
          afterMergeOperation: this.saveTplCache,
          afterInsertImg: this.saveTplCache,
          afterMoveImg: this.saveTplCache,
          afterResizeImg: this.saveTplCache,
          afterCropImg: this.saveTplCache,
          afterRestoreImg: this.saveTplCache,
          afterInsertLink: this.saveTplCache,
          commentInsertAfter: this.saveTplCache,
          commentUpdateAfter: this.saveTplCache,
          commentDeleteAfter: this.saveTplCache,
          commentShowHideAfter: this.saveTplCache,
          commentShowHideAllAfter: this.saveTplCache,
          afterFormulaOperation: this.saveTplCache,
          frozenCancelAfter: this.frozenCancelAfter,
          frozenCreateAfter: this.saveTplCache,
          afterOperateVerifycation: this.saveTplCache,
          afterHideRow: this.saveTplCache,
          afterHideCol: this.saveTplCache,
          afterShowRow: this.saveTplCache,
          afterShowCol: this.saveTplCache,
          afterChangeRowColSize: this.saveTplCache,
          rangeClear: this.saveTplCache,
          changeRowLen: this.saveTplCache,
          changeColumnLen: this.saveTplCache,
          rangePasteAfter: this.saveTplCache,
          deleteSheet: this.deleteSheet,
          sheetCopyAfter: this.saveNewSheetCache,
          addAuthClick: this.addAuthClick,
          viewAuthClick: this.viewAuthClick,
          cellEditBefore: this.cellEditBefore,
          copyPasteBefore: this.copyPasteBefore,
          pasteHandlerBefore: this.pasteHandlerBefore,
          rangeAuthCheck: this.rangeAuthCheck,
          sheetActivateAfter: this.sheetActivateAfter,
          formulaAuthCheck: this.formulaAuthCheck,
          loadDataAfter: this.loadDataAfter,
          updateCellFormat: this.updateCellFormat,
          cellMousedown: this.cellMousedown,
          saveClick: this.saveTpl,
          previewClick: this.previewReport,
          uploadFileClick: this.uploadFileClick,
          datasourceClick: this.datasourceClick,
          pdfsettingClick: this.pdfsettingClick,
          uploadAttachment: this.uploadAttachment,
          viewAttachment: this.viewAttachment
        }
      },
      settingModalConfig: {
        title: 'PDF/打印设置', // 弹窗标题,值为:新增，查看，编辑
        show: false, // 弹框显示
        formEditDisabled: false, // 编辑弹窗是否可编辑
        width: '800px', // 弹出框宽度
        modalRef: 'modalRef', // modal标识
        type: '1'// 类型 1新增 2编辑 3保存
      },
      settingModalForm: [
        { type: 'Select', label: '纸张类型', prop: 'pageType', rules: { required: true }, options: this.selectUtil.pageType },
        { type: 'Select', label: '纸张布局', prop: 'pageLayout', rules: { required: true }, options: this.selectUtil.pageLayout },
        { type: 'Select', label: '页眉是否显示', prop: 'pageHeaderShow', rules: { required: true }, options: this.selectUtil.yesNo, change: this.changePageHeaderShow },
        { type: 'Input', label: '页眉显示内容', prop: 'pageHeaderContent', rules: { required: true, maxLength: 100 }},
        { type: 'Select', label: '页眉显示位置', prop: 'pageHeaderPosition', rules: { required: true }, options: this.selectUtil.pdfPosition },
        { type: 'Select', label: '水印是否显示', prop: 'waterMarkShow', rules: { required: true }, options: this.selectUtil.yesNo, change: this.changeWaterMarkShow },
        { type: 'Select', label: '水印类型', prop: 'waterMarkType', rules: { required: true }, options: this.selectUtil.waterMarkType, change: this.changeWaterMarkType },
        { type: 'Input', label: '水印内容', prop: 'waterMarkContent', rules: { required: true, maxLength: 100 }},
        { type: 'Upload', label: '水印图片', prop: 'waterMarkImgs', rules: { required: false }, width: '400px', multiple: false, limit: 1 },
        { type: 'Input', label: '水印图片链接', prop: 'waterMarkImg', rules: { required: true }, width: '500px' },
        { type: 'Input', label: '水印透明度', prop: 'waterMarkOpacity', rules: { required: true, type: 'number', min: 0.01, max: 0.99, float: 2 }},
        { type: 'Select', label: '页码是否显示', prop: 'pageShow', rules: { required: true }, options: this.selectUtil.yesNo, change: this.changePageShow },
        { type: 'Select', label: '页码显示位置', prop: 'pagePosition', rules: { required: true }, options: this.selectUtil.pdfPosition },
        { type: 'Select', label: '是否水平分页', prop: 'horizontalPage', rules: { required: true }, options: this.selectUtil.yesNo, change: this.changeHorizontalPage },
        { type: 'Input', label: '分页列', prop: 'horizontalPageColumn', rules: { required: true }}
      ],
      settingFormData: {
        pageType: null, // 纸张类型
        pageLayout: null,
        pageHeaderShow: null,
        pageHeaderContent: null,
        pageHeaderPosition: null,
        waterMarkShow: null,
        waterMarkType: null,
        waterMarkContent: '',
        waterMarkImgs: [],
        waterMarkImg: '',
        waterMarkOpacity: null,
        pageShow: null,
        pagePosition: null,
        horizontalPage: null,
        horizontalPageColumn: null
      },
      defaultSettingFormData: {
        pageType: 2, // 纸张类型
        pageLayout: 1,
        pageHeaderShow: 1,
        pageHeaderContent: '',
        pageHeaderPosition: 1,
        waterMarkShow: 1,
        waterMarkType: 1,
        waterMarkContent: '',
        waterMarkImgs: [],
        waterMarkImg: '',
        waterMarkOpacity: '0.4',
        pageShow: 1,
        pagePosition: 2,
        horizontalPage: 2,
        horizontalPageColumn: ''
      },
      // modal 数据 end
      // modal 按钮 start
      settingModalHandles: [
        { label: '取消', type: 'default', handle: () => this.closeSettingModal() },
        { label: '确定', type: 'primary', handle: () => this.confirmPrintSettings() }
      ],
      sheetPrintSettings: {},
      designHeight: 0,
      addAuthVisiable: false,
      addAuthForm: {
        userIds: []
      },
      authUsers: [],
      authTitle: '',
      sheetRangeAuth: {},
      rangeAxis: null,
      range: null,
      isCreator: false,
      creatorName: '',
      sheetAuthedCells: {}, // sheet页已经授权的单元格
      authdialogVisible: false,
      authedRange: [],
      attrDisabled: false, // 单元格属性是否禁用，没权限的情况下需要禁用，禁止操作
      authedRangeTitle: '',
      uploadType: 'xlsx',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      defaultCheckedUsers: []
    }
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
    }
  },
  mounted() {
    this.isInputPassWord()
    this.getUsers()
    this.getTplGroupDatasets()
    this.getReportTplDateSource()
    var that = this
    this.designHeight = document.body.clientHeight - 46 - 10
    window.onresize = function() {
      that.designHeight = document.body.clientHeight - 46 - 10
    }
  },
  methods: {
    // 获取分组数据集
    getTplGroupDatasets() {
      this.dataGroupLoading = true
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportDesign.getTplGroupDatasetsApi,
        params: { tplId: reportTplId },
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

    init() {
      const _this = this
      const reportTplId = this.$route.query.tplId// reportTplId
      var options = this.sheetOptions
      options.isReport = true
      if (this.isThirdParty != 1) {
        options.allowUpdate = true
        options.gridKey = 'designMode-' + reportTplId
        options.updateUrl = location.protocol === 'https:' ? 'wss' + '://' + location.host + '/SpringReport/api/coedit/websocket/luckysheet' : 'ws' + '://' + location.host + '/SpringReport/api/coedit/websocket/luckysheet'
      }
      options.uploadImage = this.commonUtil.uploadImage
      if (!this.isCreator) {
        options.cellRightClickConfig.insertRow = false
        options.cellRightClickConfig.insertColumn = false
        options.cellRightClickConfig.deleteRow = false
        options.cellRightClickConfig.deleteColumn = false
        options.cellRightClickConfig.deleteCell = false
      }
      luckysheet.create(options)
    },
    // 右侧折叠
    switchOpenRightPanel() {
      this.rightOpen = !this.rightOpen
      this.$nextTick(() => {
        luckysheet.resize()
      })
    },
    // 左侧折叠
    switchOpenLeftPanel() {
      this.leftOpen = !this.leftOpen
      this.$nextTick(() => {
        luckysheet.resize()
      })
    },
    // 数据集选择变量折叠
    switchOpenSelectVarPanel() {
      this.selectVariableOpen = !this.selectVariableOpen
      this.$nextTick(() => {
        // 这个地方需要重绘编辑器!!!
        // this.$refs['codeMirror'].resize()
        // console.log(this.$refs['codeMirror'])
      })
    },
    // 范围选中事件
    rangeSelect(sheet, range) {
      this.attrDisabled = !this.checkRangeAuth(range)
      var cellFormData = this.getExtraCustomCellConfigs(range[0].row[0], range[0].column[0])
      if (cellFormData) {
        this.cellForm = JSON.parse(JSON.stringify(cellFormData))
        if (this.cellForm.valueType == '4' && this.cellForm.datasourceId) {
          this.getDatasourceAttr()
        }
        if (cellFormData.isDrill && cellFormData.drillId) {
          this.reportTpls = []
          var drillReport = {
            id: cellFormData.drillId,
            tplName: cellFormData.drillTplName
          }
          this.reportTpls.push(drillReport)
        } else {
          // this.getDrillReport();
        }
      } else {
        this.cellForm = JSON.parse(JSON.stringify(this.defaultCellForm))
        // var cells = this.getSelectRangeCells();
        // if(cells && cells.length>0)
        // {
        //     for (let index = 0; index < cells.length; index++) {
        //         const element = cells[index];
        //         var extraCellConfig = this.getExtraCustomCellConfigs(element[0],element[1]);
        //         if(!extraCellConfig)
        //         {
        //             this.setExtraCustomCellConfigs(element[0],element[1],JSON.parse(JSON.stringify(this.defaultCellForm)));
        //         }
        //     }
        // }
        // this.getDrillReport();
      }
    },
    // 插入行或者列监听事件
    rowInsertAfter(coordinate, count, direction, type, sheetIndex) {
      if (type == 'row') {
        this.insertRows(coordinate, count, direction, sheetIndex)
      } else {
        this.insertCols(coordinate, count, direction, sheetIndex)
      }
    },
    // 删除行或者列监听事件
    rowDeleteAfter(deleteRange, type, sheetIndex) {
      for (let index = deleteRange.length - 1; index >= 0; index--) {
        const element = deleteRange[index]
        if (type == 'row') {
          this.deleteRows(element[0], element[1], sheetIndex)
        } else {
          this.deleteCols(element[0], element[1], sheetIndex)
        }
      }
    },
    cellDragStop(cell, position, sheet) {
      this.dragEndR = position.r
      this.dragEndC = position.c
    },
    // 拖拽结束事件
    endDraggable(datasetName, columnName) {
      const checkResult = this.checkUserEditAuth(this.dragEndR, this.dragEndC)
      if (!checkResult) {
        this.commonUtil.showMessage({ message: '暂无编辑权限，如需编辑可联系创建者【' + this.creatorName + '】。', type: this.commonConstants.messageType.error })
        return
      }
      luckysheet.setCellValue(this.dragEndR, this.dragEndC, datasetName + '.${' + columnName + '}', null)
    },
    // 获取当前选中的单元格
    getSelectRangeCells() {
      var cells = []
      var selectedRanges = luckysheet.getRange()
      if (selectedRanges && selectedRanges.length > 0) {
        for (let index = 0; index < selectedRanges.length; index++) {
          const range = selectedRanges[index]
          for (let r = range.row[0]; r <= range.row[1]; r++) {
            for (let c = range.column[0]; c <= range.column[1]; c++) {
              var cell = [r, c]
              cells.push(cell)
            }
          }
        }
      }
      return cells
    },
    // 修改单元格属性
    changeCellAttr(attr) {
      var cells = this.getSelectRangeCells()
      if (cells && cells.length > 0) {
        for (let index = 0; index < cells.length; index++) {
          const element = cells[index]
          var obj = this.getExtraCustomCellConfigs(element[0], element[1])
          if (!obj) {
            obj = JSON.parse(JSON.stringify(this.defaultCellForm))
            this.setExtraCustomCellConfigs(element[0], element[1], obj)
          }
          obj[attr] = this.cellForm[attr]
        }
        var obj = {
          cells: cells,
          value: this.cellForm[attr]
        }
        const sheetIndex = luckysheet.getSheet().index
        luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': attr })
        this.saveTplCache()
      }
      if (attr == 'datasourceId') {
        this.getDatasourceAttr()
      } else if (attr == 'isDrill') {
        if (this.cellForm[attr]) {
          this.getDrillReport()
        }
      }
    },
    // 获取数据源的数据字典类型
    getDatasourceAttr() {
      const obj = {
        url: this.apis.reportDatasourceDictType.getDatasourceDictTypesApi,
        params: { datasourceId: this.cellForm.datasourceId },
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.dictTypes = response.responseData
        }
      })
    },
    // 插入行
    // coordinate：插入行横坐标 count 插入数量
    insertRows(coordinate, count, direction, sheetIndex) {
      var isSaveCache = true
      var sheetExtraCustomCellConfigs = {}
      if (sheetIndex == null) {
        sheetIndex = luckysheet.getSheet().index
      } else {
        isSaveCache = false
      }
      const map = this.extraCustomCellConfigs[sheetIndex]
      if (Object.keys(map).length > 0) {
        for (var key in map) {
          const value = map[key]
          var r = parseInt(key.split('_')[0])
          var c = parseInt(key.split('_')[1])
          if (r >= coordinate) {
            var newR = r + count
            sheetExtraCustomCellConfigs[newR + '_' + c] = value
          } else {
            sheetExtraCustomCellConfigs[key] = value
          }
        }
        this.extraCustomCellConfigs[sheetIndex] = sheetExtraCustomCellConfigs
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex] && this.sheetDatasource[sheetIndex].length > 0) {
        for (let index = 0; index < this.sheetDatasource[sheetIndex].length; index++) {
          const datasource = this.sheetDatasource[sheetIndex][index]
          var tableDatas = datasource.tableDatas
          var newTableDatas = []
          if (tableDatas && tableDatas.length > 0) {
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              var cellCoords = element.cellCoords
              var coords = this.commonUtil.getCoordsFromColumnName(cellCoords, true)
              var r = coords[0]
              var c = coords[1]
              if (direction == 'lefttop') {
                if (r >= coordinate) {
                  var newR = r + count
                  element.cellCoords = this.commonUtil.getColumnFromCoords(newR, c)
                  newTableDatas.push(element)
                } else {
                  newTableDatas.push(element)
                }
              } else {
                if (r > coordinate) {
                  var newR = r + count
                  element.cellCoords = this.commonUtil.getColumnFromCoords(newR, c)
                  newTableDatas.push(element)
                } else {
                  newTableDatas.push(element)
                }
              }
            }
            datasource.tableDatas = newTableDatas
          }
        }
      }
      if (isSaveCache) {
        this.saveTplCache()
      }
    },
    // 插入列
    // coordinate插入列纵坐标 count 插入数量
    insertCols(coordinate, count, direction, sheetIndex) {
      var isSaveCache = true
      var sheetExtraCustomCellConfigs = {}
      if (sheetIndex == null) {
        sheetIndex = luckysheet.getSheet().index
      } else {
        isSaveCache = false
      }
      const map = this.extraCustomCellConfigs[sheetIndex]
      if (Object.keys(map).length > 0) {
        for (var key in map) {
          const value = map[key]
          var r = parseInt(key.split('_')[0])
          var c = parseInt(key.split('_')[1])
          if (c >= coordinate) {
            var newC = c + count
            sheetExtraCustomCellConfigs[r + '_' + newC] = value
          } else {
            sheetExtraCustomCellConfigs[key] = value
          }
        }
        this.extraCustomCellConfigs[sheetIndex] = sheetExtraCustomCellConfigs
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex] && this.sheetDatasource[sheetIndex].length > 0) {
        for (let index = 0; index < this.sheetDatasource[sheetIndex].length; index++) {
          const datasource = this.sheetDatasource[sheetIndex][index]
          var tableDatas = datasource.tableDatas
          var newTableDatas = []
          if (tableDatas && tableDatas.length > 0) {
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              var cellCoords = element.cellCoords
              var coords = this.commonUtil.getCoordsFromColumnName(cellCoords, true)
              var r = coords[0]
              var c = coords[1]
              if (direction == 'lefttop') {
                if (c >= coordinate) {
                  var newC = c + count
                  element.cellCoords = this.commonUtil.getColumnFromCoords(r, newC)
                  newTableDatas.push(element)
                } else {
                  newTableDatas.push(element)
                }
              } else {
                if (c > coordinate) {
                  var newC = c + count
                  element.cellCoords = this.commonUtil.getColumnFromCoords(r, newC)
                  newTableDatas.push(element)
                } else {
                  newTableDatas.push(element)
                }
              }
            }
            datasource.tableDatas = newTableDatas
          }
        }
      }
      if (isSaveCache) {
        this.saveTplCache()
      }
    },
    // 删除行
    // start开始坐标 end：结束坐标
    deleteRows(start, end, sheetIndex) {
      var isSaveCache = true
      var sheetExtraCustomCellConfigs = {}
      if (sheetIndex == null) {
        sheetIndex = luckysheet.getSheet().index
      } else {
        isSaveCache = false
      }
      const map = this.extraCustomCellConfigs[sheetIndex]
      if (Object.keys(map).length > 0) {
        for (var key in map) {
          const value = map[key]
          var r = parseInt(key.split('_')[0])
          var c = parseInt(key.split('_')[1])
          if (r < start) {
            sheetExtraCustomCellConfigs[key] = value
          } else if (r > end) {
            var newR = r - (end - start + 1)
            sheetExtraCustomCellConfigs[newR + '_' + c] = value
          }
        }
        this.extraCustomCellConfigs[sheetIndex] = sheetExtraCustomCellConfigs
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex] && this.sheetDatasource[sheetIndex].length > 0) {
        for (let index = 0; index < this.sheetDatasource[sheetIndex].length; index++) {
          const datasource = this.sheetDatasource[sheetIndex][index]
          var tableDatas = datasource.tableDatas
          var newTableDatas = []
          if (tableDatas && tableDatas.length > 0) {
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              var cellCoords = element.cellCoords
              var coords = this.commonUtil.getCoordsFromColumnName(cellCoords, true)
              var r = coords[0]
              var c = coords[1]
              if (r < start) {
                newTableDatas.push(element)
              } else if (r > end) {
                var newR = r - (end - start + 1)
                element.cellCoords = this.commonUtil.getColumnFromCoords(newR, c)
                newTableDatas.push(element)
              }
            }
            datasource.tableDatas = newTableDatas
          }
        }
      }
      if (isSaveCache) {
        this.saveTplCache()
      }
    },
    // 删除列
    // start开始坐标 end：结束坐标
    deleteCols(start, end, sheetIndex) {
      var isSaveCache = true
      var sheetExtraCustomCellConfigs = {}
      if (sheetIndex == null) {
        sheetIndex = luckysheet.getSheet().index
      } else {
        isSaveCache = false
      }
      const map = this.extraCustomCellConfigs[sheetIndex]
      if (Object.keys(map).length > 0) {
        for (var key in map) {
          const value = map[key]
          var r = parseInt(key.split('_')[0])
          var c = parseInt(key.split('_')[1])
          if (c < start) {
            sheetExtraCustomCellConfigs[key] = value
          } else if (c > end) {
            var newC = c - (end - start + 1)
            sheetExtraCustomCellConfigs[r + '_' + newC] = value
          }
        }
        this.extraCustomCellConfigs[sheetIndex] = sheetExtraCustomCellConfigs
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex] && this.sheetDatasource[sheetIndex].length > 0) {
        for (let index = 0; index < this.sheetDatasource[sheetIndex].length; index++) {
          const datasource = this.sheetDatasource[sheetIndex][index]
          var tableDatas = datasource.tableDatas
          var newTableDatas = []
          if (tableDatas && tableDatas.length > 0) {
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              var cellCoords = element.cellCoords
              var coords = this.commonUtil.getCoordsFromColumnName(cellCoords, true)
              var r = coords[0]
              var c = coords[1]
              if (c < start) {
                newTableDatas.push(element)
              } else if (c > end) {
                var newC = c - (end - start + 1)
                element.cellCoords = this.commonUtil.getColumnFromCoords(r, newC)
                newTableDatas.push(element)
              }
            }
            datasource.tableDatas = newTableDatas
          }
        }
      }
      if (isSaveCache) {
        this.saveTplCache()
      }
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
    addDataSets() {
      // this.getReportTplDateSource();
      this.addDatasetsDialogVisiable = true
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
    // 关闭添加数据源
    closeAddDataSet() {
      if (!this.addDatasetsDialogVisiable) {
        return
      }
      this.addDatasetsDialogVisiable = false
      if (this.datasourceType == '1') {
        this.$refs.codeMirror.codemirror.setValue('')
      }

      this.commonUtil.clearObj(this.paramForm)
      this.commonUtil.clearObj(this.paginationForm)
      this.commonUtil.clearObj(this.sqlForm)
      this.$refs['sqlRef'].resetFields()// 校验重置
      this.$refs['paginationRef'].resetFields()// 校验重置
      this.$refs['paramRef'].resetFields()// 校验重置
      this.sqlColumnTableData.tableData = []
      this.sqlColumnTableData.tablePage.currentPage = 1
      this.sqlColumnTableData.tablePage.pageTotal = 0
      this.paramTableData.tableData = []
      this.paramTableData.tablePage.currentPage = 1
      this.paramTableData.tablePage.pageTotal = 0
      this.paginationForm.isPagination = 2
      this.paginationForm.pageCount = 100
      this.datasourceType = '1'
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
            this.paramTableData.tableData[result.index].paramDefault = this.paramForm.paramDefault
            this.paramTableData.tableData[result.index].paramRequired = this.paramForm.paramRequired
            this.paramTableData.tableData[result.index].selectType = this.paramForm.selectType
            this.paramTableData.tableData[result.index].selectContent = this.paramForm.selectContent
            this.paramTableData.tableData[result.index].isRelyOnParams = this.paramForm.isRelyOnParams
            this.paramTableData.tableData[result.index].relyOnParams = this.paramForm.relyOnParams
            this.paramTableData.tableData[result.index].paramHidden = this.paramForm.paramHidden
            this.paramTableData.tableData[result.index].checkStrictly = this.paramForm.checkStrictly
            this.paramTableData.tableData[result.index].paramPrefix = this.paramForm.paramPrefix
            this.paramTableData.tableData[result.index].dateFormat = this.paramForm.dateFormat
          } else {
            // 未添加该参数，则列表中新增一条数据
            const row = {
              paramName: this.paramForm.paramName,
              paramCode: this.paramForm.paramCode,
              paramType: this.paramForm.paramType,
              paramDefault: this.paramForm.paramDefault,
              paramRequired: this.paramForm.paramRequired,
              selectType: this.paramForm.selectType,
              selectContent: this.paramForm.selectContent,
              isRelyOnParams: this.paramForm.isRelyOnParams == '' ? '2' : this.paramForm.isRelyOnParam,
              relyOnParams: this.paramForm.relyOnParams,
              paramHidden: this.paramForm.paramHidden,
              checkStrictly: this.paramForm.checkStrictly == '' ? '' : this.paramForm.checkStrictly,
              paramPrefix: this.paramForm.paramPrefix,
              dateFormat: this.paramForm.dateFormat
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
      this.paramForm.paramDefault = row.paramDefault
      this.paramForm.paramRequired = row.paramRequired
      this.paramForm.selectType = row.selectType
      this.paramForm.selectContent = row.selectContent
      this.paramForm.isRelyOnParams = row.isRelyOnParams
      this.paramForm.relyOnParams = row.relyOnParams
      this.paramForm.paramHidden = row.paramHidden
      this.paramForm.checkStrictly = row.checkStrictly
      this.paramForm.paramPrefix = row.paramPrefix
      this.paramForm.dateFormat = row.dateFormat
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
          } else {
            // 未添加该参数，则列表中新增一条数据
            const row = {
              paramName: this.procedureParamForm.paramName,
              paramCode: this.procedureParamForm.paramCode,
              paramType: this.procedureParamForm.paramType,
              paramDefault: this.procedureParamForm.paramDefault
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
    // 编辑数据及
    editDataSet(dataSet) {
      this.addDatasetsDialogVisiable = true
      this.$nextTick(() => {
        this.$refs.codeMirror.codemirror.setValue(dataSet.tplSql)
      })
      this.paramTableData.tableData = eval('(' + dataSet.tplParam + ')')
      this.sqlColumnTableData.tableData = dataSet.columns ? dataSet.columns : []
      this.sqlColumnTableData.tablePage.pageTotal = dataSet.columns ? this.sqlColumnTableData.tableData.length : 0
      this.paginationForm.isPagination = dataSet.isPagination
      this.paginationForm.pageCount = dataSet.pageCount
      this.paginationForm.currentPageAttr = dataSet.currentPageAttr
      this.paginationForm.pageCountAttr = dataSet.pageCountAttr
      this.paginationForm.totalAttr = dataSet.totalAttr
      this.sqlForm.datasetName = dataSet.datasetName
      this.sqlForm.datasourceId = dataSet.datasourceId
      this.sqlForm.id = dataSet.id
      this.sqlForm.sqlType = dataSet.sqlType
      this.sqlForm.groupId = dataSet.groupId
      if (dataSet.sqlType == 2) {
        this.procedureInParamTableData.tableData = JSON.parse(dataSet.inParam)
        this.procedureOutParamTableData.tableData = JSON.parse(dataSet.outParam)
      }
      this.getReportTplDateSource()
      this.getTplGroupDatasets()
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
        if (this.sqlForm.sqlType == '1') {
          this.$refs['paginationRef'].validate((valid) => {
            if (valid) {

            } else {
              paginationValidate = false
            }
          })
        } else {
          this.paginationForm.isPagination = 2
          this.paginationForm.pageCount = 100
        }
        if (!paginationValidate) {
          return
        }
      } else if (this.datasourceType == '2') {
        this.$refs['paginationRef'].validate((valid) => {
          if (valid) {

          } else {
            paginationValidate = false
          }
        })
        if (!paginationValidate) {
          return
        }
      }

      this.$refs['sqlRef'].validate((valid) => {
        if (valid) {
          const obj = {
            url: this.apis.reportDesign.addDataSetApi,
            params: { tplId: reportTplId, groupId: this.sqlForm.groupId, datasetType: this.datasourceType, sqlType: this.sqlForm.sqlType, tplSql: tplSql, tplParam: this.paramTableData.tableData ? JSON.stringify(this.paramTableData.tableData) : '', datasourceId: this.sqlForm.datasourceId, datasetName: this.sqlForm.datasetName, id: this.sqlForm.id,
              inParam: this.procedureInParamTableData.tableData ? JSON.stringify(this.procedureInParamTableData.tableData) : '', outParam: this.procedureOutParamTableData.tableData ? JSON.stringify(this.procedureOutParamTableData.tableData) : '',
              isPagination: this.paginationForm.isPagination, pageCount: this.paginationForm.pageCount, currentPageAttr: this.paginationForm.currentPageAttr, pageCountAttr: this.paginationForm.pageCountAttr, totalAttr: this.paginationForm.totalAttr },
            removeEmpty: false
          }
          this.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              this.getTplGroupDatasets()
              // let isExist = false;
              // let dataSet = response.responseData;
              // let index = -1;
              // for(let i=0;i<this.datasets.length;i++)
              // {
              //     if(this.datasets[i].datasetName == dataSet.datasetName)
              //     {
              //         this.datasets[i] = dataSet;
              //         isExist = true;
              //         index = i;
              //         break;
              //     }
              // }
              // if(!isExist)
              // {
              //     this.datasets.push(dataSet);
              // }else{

              // }
              this.closeAddDataSet()
              this.$forceUpdate()
              var sheetIndex = luckysheet.getSheet().index
              luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, { 'datasetName': obj.params.datasetName }, { 'k': 'datasetChanged' })
            }
          })
        } else {
          return
        }
      })
    },
    // 保存模板
    saveTpl() {
      this.loadingText = '模板保存中...'
      this.loading = true
      var luckysheetfiles = luckysheet.getLuckysheetfile()
      if (luckysheetfiles) {
        var params = {}
        var sheetConfigs = []
        for (let index = 0; index < luckysheetfiles.length; index++) {
          const luckysheetfile = luckysheetfiles[index]
          if (!luckysheetfile.isPivotTable) {
            var cellDatas = this.getCellDatas(luckysheetfile)
            var extraCustomCellConfigs = this.getSheetExtraCustomCellConfigs(cellDatas, luckysheetfile.index)
            var configs = this.getSheetConfigs(luckysheetfile)
            var emptyMergeCell = this.getEmptyMergeCell(configs.config.merge, cellDatas)
            cellDatas = cellDatas.concat(emptyMergeCell)
            var borderCellDatas = this.getEmptyBorderCellDatas(configs.config, cellDatas)
            var datasourceConfig = this.getDatasourceConfig(luckysheetfile.index)
            cellDatas = cellDatas.concat(borderCellDatas)
            if (cellDatas && cellDatas.length > 0) {
              var sheetCellFormats = this.cellFormats[luckysheetfile.index]
              if (sheetCellFormats) {
                for (let index = 0; index < cellDatas.length; index++) {
                  const element = cellDatas[index]
                  var ct = sheetCellFormats[element.r + '_' + element.c]
                  if (ct && ct.t && ct.t == 'inlineStr') {

                  } else {
                    if (ct) {
                      element.v.ct = ct
                    }
                  }
                }
              }
            }
            configs.cellDatas = cellDatas
            configs.extraCustomCellConfigs = extraCustomCellConfigs
            configs.datasourceConfig = datasourceConfig
            var printSettings = this.sheetPrintSettings[luckysheetfiles[index].index]
            if (printSettings) {
              configs.printSettings = printSettings
            }
            sheetConfigs.push(configs)
          }
        }
        const reportTplId = this.$route.query.tplId
        params.tplId = reportTplId
        params.configs = sheetConfigs
        params.isParamMerge = this.isParamMerge ? '1' : '2'
        params.delSheetsIndex = this.delSheetsIndex
        const param = {
          url: this.apis.reportDesign.saveReportFormsTplApi,
          params: params,
          callback: this.doPostCallback
        }
        var _this = this
        this.commonUtil.doPost(param).then(response => {
          if (response.code == '200') {
            const printSettings = response.responseData.printSettings
            if (printSettings) {
              for (var i in printSettings) {
                _this.sheetPrintSettings[i].id = printSettings[i]
              }
            }
            // this.refreshTpl();
            // _this.getTplSettings();
            if (_this.isCreator) {
              luckysheet.sendServerSocketMsg('reportDesign', null, {}, { 'k': 'refreshAuth' })
            }
          }
        })
      }
    },
    // 获取表格数据，将content为空的单元格排除掉
    getCellDatas(luckysheetfile) {
      var result = []
      // var luckysheetfile = luckysheet.getLuckysheetfile();
      var data = luckysheet.transToCellData(luckysheetfile.data)
      if (data && data.length > 0) {
        for (let index = 0; index < data.length; index++) {
          const element = data[index]
          if (element.v) {
            if (element.v.mc && element.v.mc.rs) {
              if (element.v.v != null) {
                result.push(element)
              } else if (element.v.ct && element.v.ct.t == 'inlineStr') {
                result.push(element)
              } else if (element.v.bg) {
                result.push(element)
              }
            } else {
              if (element.v.v != null) {
                result.push(element)
              } else if (element.v.ct && element.v.ct.t == 'inlineStr') {
                result.push(element)
              } else if (element.v.bg) {
                result.push(element)
              }
            }
          }
          // if(element.v && element.v.mc && !element.v.v)
          // {
          //     if(element.v.ct && element.v.ct.t == "inlineStr")
          //     {
          //         result.push(element);
          //     }else  if (element.v.bg) {
          //         result.push(element)
          //       }
          // }else{
          //     result.push(element);
          // }
        }
      }
      return result
    },
    // 获取没有内容的边框单元格
    getEmptyBorderCellDatas(config, cellDatas) {
      var result = []
      if (config != null) {
        var borderinfo = config.borderInfo
        if (borderinfo != null) {
          var obj = this.cellDatasToJsonObj(cellDatas)
          var cells = this.getBorderCells(borderinfo)
          if (cells && cells.length > 0) {
            for (let index = 0; index < cells.length; index++) {
              const element = cells[index]
              var cellObj = obj[element]
              if (!cellObj) {
                var r = parseInt(element.split('-')[0])
                var c = parseInt(element.split('-')[1])
                var cellData = {
                  'r': r,
                  'c': c,
                  'v': {
                    'ct': {
                      'fa': 'General',
                      't': 'g'
                    },
                    'v': '',
                    'm': ''
                  }
                }
                result.push(cellData)
              }
            }
          }
        }
      }
      return result
    },
    getEmptyMergeCell(merge, cellDatas) {
      var result = []
      if (merge && Object.keys(merge).length > 0) {
        var obj = this.cellDatasToJsonObj(cellDatas)
        Object.keys(merge).forEach((key) => {
          var value = merge[key]
          var r = value.r
          var c = value.c
          if (!obj[r + '-' + c]) {
            var cellData = {
              'r': r,
              'c': c,
              'v': {
                'ct': {
                  'fa': 'General',
                  't': 'g'
                },
                'v': '',
                'm': '',
                'mc': {
                  'r': r,
                  'c': c,
                  'rs': value.rs,
                  'cs': value.cs
                }
              }

            }
            result.push(cellData)
          }
        })
      }
      return result
    },
    // 获取有边框的单元格
    getBorderCells(borderinfo) {
      var result = new Array()
      for (let index = 0; index < borderinfo.length; index++) {
        const element = borderinfo[index]
        if (element.rangeType && element.rangeType == 'range') {
          var borderType = element.borderType
          if (borderType == 'border-all' || borderType == 'border-left' || borderType == 'border-right' ||
                    borderType == 'border-top' || borderType == 'border-bottom') {
            var ranges = element.range
            for (let t = 0; t < ranges.length; t++) {
              const range = ranges[t]
              var row = range.row
              var column = range.column
              var startRow = row[0]
              var startCol = column[0]
              var rowAmount = row[1] - row[0] + 1
              var colAmount = column[1] - column[0] + 1
              for (var i = 0; i < rowAmount; i++) {
                for (var j = 0; j < colAmount; j++) {
                  var coor = (startRow + i) + '-' + (startCol + j)
                  if (result.indexOf(coor) < 0) {
                    result.push(coor)
                  }
                }
              }
            }
          } else if (borderType == 'border-none') {
            var ranges = element.range
            for (let t = 0; t < ranges.length; t++) {
              const range = ranges[t]
              var row = range.row
              var column = range.column
              var startRow = row[0]
              var startCol = column[0]
              var rowAmount = row[1] - row[0] + 1
              var colAmount = column[1] - column[0] + 1
              for (var i = 0; i < rowAmount; i++) {
                for (var j = 0; j < colAmount; j++) {
                  var coor = (startRow + i) + '-' + (startCol + j)
                  if (result.indexOf(coor) >= 0) {
                    result.splice(result.indexOf(coor), 1)
                  }
                }
              }
            }
          }
        }
      }
      return result
    },
    cellDatasToJsonObj(data) {
      var obj = {}
      for (let index = 0; index < data.length; index++) {
        const element = data[index]
        if (element.v.mc) {
          var r = element.r
          var c = element.c
          var rs = element.v.mc.rs
          var cs = element.v.mc.cs
          for (let i = 0; i < rs; i++) {
            for (let t = 0; t < cs; t++) {
              obj[(r + i) + '-' + (c + t)] = '1'
            }
          }
        } else {
          obj[element.r + '-' + element.c] = '1'
        }
      }
      return obj
    },
    // 获取自定义单元格配置
    getSheetExtraCustomCellConfigs(cellDatas, sheetIndex) {
      var result = {}
      if (cellDatas && cellDatas.length > 0) {
        for (let index = 0; index < cellDatas.length; index++) {
          const element = cellDatas[index]
          if (this.extraCustomCellConfigs[sheetIndex]) {
            var obj = this.extraCustomCellConfigs[sheetIndex][element.r + '_' + element.c]
            if (obj) {
              result[element.r + '_' + element.c] = obj
            }
          }
        }
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex]) {
        const dataSource = this.sheetDatasource[sheetIndex]
        if (dataSource && dataSource.length > 0) {
          for (let t = 0; t < dataSource.length; t++) {
            const datasourceConfig = dataSource[t]
            const tableDatas = datasourceConfig.tableDatas
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              if (element.cellCoords) {
                const coords = this.commonUtil.getCoordsFromColumnName(element.cellCoords, false)
                if (!result[coords]) {
                  var obj = this.extraCustomCellConfigs[sheetIndex][coords]
                  if (obj) {
                    result[coords] = obj
                  }
                }
              }
            }
          }
        }
      }
      return result
    },
    getSheetConfigs(luckysheetfile) {
      var result = {}
      // result.title = json.title;
      result.hyperlinks = luckysheetfile.hyperlink
      result.config = luckysheetfile.config
      result.frozen = luckysheetfile.frozen
      result.images = luckysheetfile.images
      result.sheetIndex = luckysheetfile.index
      result.calcChain = luckysheetfile.calcChain
      result.sheetName = luckysheetfile.name
      result.sheetOrder = luckysheetfile.order
      result.pageDivider = luckysheetfile.pageDivider
      result.luckysheetConditionformatSave = luckysheetfile.luckysheet_conditionformat_save
      if (this.sheetRangeAuth[luckysheetfile.index]) {
        result.sheetRangeAuth = this.sheetRangeAuth[luckysheetfile.index]
      }
      return result
    },
    getDatasourceConfig(sheetIndex) {
      var datasourceConfig = this.sheetDatasource[sheetIndex]
      if (datasourceConfig) {
        return datasourceConfig
      } else {
        return null
      }
    },
    getTplSettings() {
      const reportTplId = this.$route.query.tplId
      const param = {
        url: this.apis.reportDesign.getReportFormsTplApi,
        params: { id: reportTplId }
      }
      param.callback = this.doPostCallback
      var _this = this
      this.loadingText = '模板数据加载中...'
      this.loading = true
      this.commonUtil.doPost(param).then(response => {
        if (response.code == '200') {
          if (response.responseData && response.responseData.settings) {
            _this.sheetOptions.data = []
            _this.tplName = response.responseData.tplName
            document.title = response.responseData.tplName
            _this.sheetRangeAuth = response.responseData.sheetRangeAuth
            _this.isCreator = response.responseData.creator
            _this.creatorName = response.responseData.creatorName
            var datas = {}
            for (let index = 0; index < response.responseData.settings.length; index++) {
              const element = response.responseData.settings[index]
              element.config.curentsheetView = null
              var cellDatas = {
                celldata: element.cellDatas,
                hyperlink: element.hyperlinks == null ? {} : element.hyperlinks,
                config: element.config,
                frozen: element.frozen,
                images: element.images,
                calcChain: element.calcChain,
                index: element.sheetIndex,
                name: element.sheetName,
                order: element.sheetOrder,
                isPivotTable: false,
                pivotTable: null,
                pageDivider: element.pageDivider,
                luckysheet_conditionformat_save: element.luckysheetConditionformatSave
              }
              if (!_this.isCreator) {
                if (_this.sheetRangeAuth) {
                  if (_this.sheetRangeAuth[element.sheetIndex]) {
                    const range = []
                    for (var key in _this.sheetRangeAuth[element.sheetIndex]) {
                      range.push(_this.sheetRangeAuth[element.sheetIndex][key].range)
                    }
                    cellDatas.authrange = range
                  }
                }
              }
              _this.sheetOptions.data.push(cellDatas)
              _this.cellFormats[element.sheetIndex] = element.cellFormats
              // datas[element.sheetIndex] = element.cellDatas;
              datas[element.sheetIndex] = JSON.parse(JSON.stringify(element.cellDatas))
              if (element.extraCustomCellConfigs) {
                _this.extraCustomCellConfigs[element.sheetIndex] = element.extraCustomCellConfigs
              } else {
                _this.extraCustomCellConfigs[element.sheetIndex] = {}
              }
              if (element.datasourceConfig && element.datasourceConfig.length > 0) {
                this.sheetDatasource[element.sheetIndex] = element.datasourceConfig
              }
              if (element.reportSheetPdfPrintSetting) {
                _this.sheetPrintSettings[element.sheetIndex] = element.reportSheetPdfPrintSetting
              }
            }
          }
          _this.isParamMerge = response.responseData.isParamMerge == '1'
          _this.isThirdParty = response.responseData.isThirdParty
          _this.init()
        }
      })
    },
    // 预览
    previewReport() {
      const reportTplId = this.$route.query.tplId// reportTplId
      const viewReport = this.$router.resolve({ name: 'luckyReportPreview', query: { tplId: reportTplId, thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType) }})
      window.open(viewReport.href, '_blank')
    },
    // 添加循环块
    addBlock() {
      this.blockVisiable = true
    },
    // 关闭循环块对话框
    closeBlockDialog() {
      this.blockVisiable = false
      this.$refs['blockRef'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.blockForm)
    },
    // 确认添加循环块
    confirmAddBlock() {
      this.$refs['blockRef'].validate((valid) => {
        if (valid) {
          var data = {
            startCell: this.blockForm.startCell,
            endCell: this.blockForm.endCell
          }
          var sheetIndex = luckysheet.getSheet().index
          if (!this.blockData[sheetIndex]) {
            this.blockData[sheetIndex] = []
          }
          this.blockData[sheetIndex].push(data)
          this.sheetBlockData = this.blockData[sheetIndex]
          this.closeBlockDialog()
        } else {
          return false
        }
      })
    },
    // 删除循环块
    deleteBlock(index) {
      var sheetIndex = luckysheet.getSheet().index
      this.blockData[sheetIndex].splice(index, 1)
      this.sheetBlockData = this.blockData[sheetIndex]
    },
    // 获取循环块信息，起始横坐标，起始纵坐标，colspan，rowspan
    getBlockInfos(blockData) {
      var result = {}
      var startCell = blockData.startCell
      var endCell = blockData.endCell
      var startx = startCell.replace(/[^0-9]/ig, '') - 1
      var starty = this.columnStringTonum(startCell.replace((startx + 1), '')) - 1
      var endx = endCell.replace(/[^0-9]/ig, '') - 1
      var endy = this.columnStringTonum(endCell.replace((endx + 1), '')) - 1
      var rowspan = endx - startx + 1
      var colspan = endy - starty + 1
      result.startx = startx
      result.starty = starty
      result.rowspan = rowspan
      result.colspan = colspan
      return result
    },
    // 获取循环块的单元格
    getBlockCells(startx, starty, rowspan, colspan) {
      var result = []
      for (let i = 0; i < rowspan; i++) {
        for (let t = 0; t < colspan; t++) {
          result.push((startx + i) + '-' + (starty + t))
        }
      }
      return result
    },
    // 列字母转数字
    columnStringTonum(a) {
      var str = a.toLowerCase().split('')
      var num = 0
      var al = str.length
      var getCharNumber = function(charx) {
        return charx.charCodeAt() - 96
      }
      var numout = 0
      var charnum = 0
      for (var i = 0; i < al; i++) {
        charnum = getCharNumber(str[i])
        numout += charnum * Math.pow(26, al - i - 1)
      };
      return numout
    },
    // 将celldatas转成map
    cellDatasToMap(cellDatas) {
      var result = {}
      for (let index = 0; index < cellDatas.length; index++) {
        const element = cellDatas[index]
        result[element.r + '-' + element.c] = element
      }
      return result
    },
    isInputPassWord() {
      const reportTplId = this.$route.query.tplId
      var obj = {
        url: this.apis.reportTpl.getDetailApi,
        params: { id: reportTplId }
      }
      this.commonUtil.doGet(obj).then(response => {
        if (response.responseData.designPwd) {
          this.openInputPwd()
        } else {
          this.initTableSettings()
        }
      })
    },
    openInputPwd() {
      const reportTplId = this.$route.query.tplId
      this.$prompt('', '请输入密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password',
        inputValidator: (value) => { // 点击按钮时，对文本框里面的值进行验证
          if (!value) {
            return '请输入密码'
          }
        }
      }).then(({ value }) => {
        var obj = {
          params: { id: reportTplId, designPwd: value },
          url: this.apis.reportTpl.validateDesignPwdApi
        }
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            this.initTableSettings()
          } else {
            (
              this.openInputPwd()
            )
          }
        })
      }).catch(() => {
      })
    },
    initTableSettings() {
      this.getDataSets()
      this.getTplSettings()
    },
    async clickDatasets(o) {
      this.datasetItemActive = o.id
      if (o.isActive) {
        return
      }
      this.$set(o, 'isActive', true)
      await this.getDatasetColumns(o)
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
      this.commonUtil.doPost(obj).then(response => {
        element.columns = response.responseData
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
    clickTab(index) {
      this.tabIndex = index
    },
    // 引入Excel
    loadExcel(evt) {
      const files = evt.target.files
      if (files == null || files.length == 0) {
        alert('请选择文件')
        return
      }

      // 获取文件名
      const name = files[0].name
      // 获取文件后缀
      const suffixArr = name.split('.')
      const suffix = suffixArr[suffixArr.length - 1]
      if (this.uploadType == 'xlsx') {
        if (suffix != 'xlsx') {
          this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.upload.filetype', ['xlsx']), type: this.commonConstants.messageType.error })
          return
        }
      } else if (this.uploadType == 'csv') {
        if (suffix != 'csv') {
          this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.upload.filetype', ['csv']), type: this.commonConstants.messageType.error })
          return
        }
      } else {
        this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.upload.filetype', ['xlsx/csv']), type: this.commonConstants.messageType.error })
        return
      }
      this.loadingText = '文件上传中，请耐心等待...'
      this.loading = true
      var that = this
      // 转换导入的excel
      const formData = new FormData()
      formData.append('file', files[0])
      formData.append('tplId', this.$route.query.tplId)
      formData.append('isFormsReport', 2)
      const config = {
        headers: { 'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem(that.commonConstants.sessionItem.authorization),
          'thirdPartyType': localStorage.getItem(that.commonConstants.sessionItem.thirdPartyType) }
      }
      try {
        Axios.post(that.apis.reportTpl.uploadReportTplApi, formData, config)
          .then(res => {
            if (res.data.code == '200') {
              const sheetDatas = res.data.responseData
              var sameName = ''
              if (sheetDatas && sheetDatas.length > 0) {
                var luckysheetfiles = luckysheet.getLuckysheetfile()
                for (let index = 0; index < sheetDatas.length; index++) {
                  const element = sheetDatas[index]
                  var flag = false
                  for (let index = 0; index < luckysheetfiles.length; index++) {
                    const luckysheetfile = luckysheetfiles[index]
                    if (luckysheetfile.name == element.name) {
                      if (sameName) {
                        sameName = sameName + '，' + luckysheetfile.name
                      } else {
                        sameName = luckysheetfile.name
                      }
                      flag = true
                      break
                    }
                  }
                  if (!flag) {
                    var data = luckysheet.buildGridData(element)
                    element.data = data
                    luckysheet.appendSheets(element, false)
                  }
                }
              }
              if (sameName) {
                that.commonUtil.showMessage({ message: 'sheet名称【' + sameName + '】已经存在。', type: 'error' })
              }
            } else {
              that.commonUtil.showMessage({ message: res.data.message, type: res.data.msgLevel })
            }
            that.loading = false
            evt.target.value = ''
          })
      } catch (error) {
        evt.target.value = ''
        that.loading = false
      }
    },
    setExtraCustomCellConfigs(r, c, obj) {
      var sheetIndex = luckysheet.getSheet().index
      if (!this.extraCustomCellConfigs[sheetIndex]) {
        this.extraCustomCellConfigs[sheetIndex] = {}
      }
      this.extraCustomCellConfigs[sheetIndex][r + '_' + c] = obj
    },
    getExtraCustomCellConfigs(r, c, sheetIndex) {
      if (!sheetIndex) {
        sheetIndex = luckysheet.getSheet().index
      }
      if (this.extraCustomCellConfigs[sheetIndex]) {
        return this.extraCustomCellConfigs[sheetIndex][r + '_' + c]
      }
      return null
    },
    // 切换sheet监听事件
    sheetActivate(index, isPivotInitial, isNewSheet) {
      if (!this.blockData) {
        this.blockData = {}
      }
      if (this.blockData[index]) {
        this.sheetBlockData = this.blockData[index]
      }
    },
    // 删除sheet监听
    sheetDeleteBefore(sheet) {
      this.delSheetsIndex.push(sheet.sheet.index)
    },
    // 创建sheet监听
    sheetCreateAfter(sheet) {
      var index = this.delSheetsIndex.indexOf(sheet.sheet.index)
      if (index > -1) {
        this.delSheetsIndex.splice(index, 1)
      } else {
        var data = luckysheet.buildGridData(sheet.sheet)
        sheet.sheet.data = data
        this.saveNewSheetCache(sheet)
      }
    },
    // sheet复制监听事件
    sheetCopyBefore(object) {
      var oldSheet = object.targetSheet
      var newSheet = object.copySheet
      var extraConfig = this.extraCustomCellConfigs[oldSheet.index]
      if (extraConfig) {
        var newExtraConfig = JSON.parse(JSON.stringify(extraConfig))
        this.extraCustomCellConfigs[newSheet.index] = newExtraConfig
      }
      var blockData = this.blockData[oldSheet.index]
      if (blockData) {
        var newBlockData = JSON.parse(JSON.stringify(blockData))
        this.blockData[newSheet.index] = newBlockData
      }
    },
    closeDatasourceDialog() {
      this.datasourceDialog = false
    },
    confirmAddDatasource() {
      var sheetIndex = luckysheet.getSheet().index
      if (this.datasources && this.datasources.length > 0) {
        this.sheetDatasource[sheetIndex] = JSON.parse(JSON.stringify(this.datasources))
        this.datasources = []
        this.closeDatasourceDialog()
      } else {
        this.datasources = []
        this.sheetDatasource[sheetIndex] = JSON.parse(JSON.stringify(this.datasources))
        this.closeDatasourceDialog()
      }
      var obj = {
        cells: {},
        sheetName: luckysheet.getSheet().name,
        value: this.sheetDatasource[sheetIndex]
      }
      luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': 'datasourceChanged' })
      this.saveTplCache()
    },
    // 添加绑定属性
    addDatasourceAttr() {
      this.datasourceAttrDialog = true
      this.isEditDatasourceAttr = false
    },
    editDatasourceAttr() {
      this.datasourceAttrDialog = true
      this.isEditDatasourceAttr = true
      this.datasourceAttrForm.name = this.datasourceAttr.name
    },
    deleteDatasourceAttr(index) {
      this.datasources.splice(index, 1)
      var isActive = false
      if (this.datasources && this.datasources.length > 0) {
        for (let index = 0; index < this.datasources.length; index++) {
          const element = this.datasources[index]
          if (element.isActive) {
            isActive = true
          }
        }
        if (!isActive) {
          this.datasources[0].isActive = true
          this.datasourceAttr = this.datasources[0]
        }
      } else {
        this.commonUtil.clearObj(this.datasourceAttr)
      }
    },
    closeDatasourceAttr() {
      this.datasourceAttrDialog = false
    },
    confirmDatasourceAttr() {
      this.$refs['datasourceAttrRef'].validate((valid) => {
        if (valid) {
          if (!this.isEditDatasourceAttr) {
            var sheetIndex = luckysheet.getSheet().index
            var sheetAttrs = this.sheetDatasource[sheetIndex]
            if (sheetAttrs) {
              var obj = {
                name: this.datasourceAttrForm.name, // 名称
                datasourceId: '', // 数据源id
                table: '', // 表名
                keys: [], // 主键
                tableDatas: []// 属性对应关系
              }
              sheetAttrs.push(obj)
            } else {
              sheetAttrs = []
              var obj = {
                name: this.datasourceAttrForm.name, // 名称
                datasourceId: '', // 数据源id
                table: '', // 表名
                keys: [], // 主键
                tableDatas: []// 属性对应关系
              }
              sheetAttrs.push(obj)
              this.sheetDatasource[sheetIndex] = sheetAttrs
            }
            this.datasources = this.sheetDatasource[sheetIndex]
            var isActive = false
            for (let index = 0; index < this.datasources.length; index++) {
              const element = this.datasources[index]
              if (element.isActive) {
                isActive = true
              }
            }
            if (!isActive) {
              this.datasources[0].isActive = true
              this.datasourceAttr = this.datasources[0]
            }
          } else {
            this.datasourceAttr.name = this.datasourceAttrForm.name
          }
          this.closeDatasourceAttr()
          this.commonUtil.clearObj(this.datasourceAttrForm)
        } else {
          return false
        }
      })
    },
    // 填报属性左侧名字点击事件
    clickAttrName(object) {
      for (let index = 0; index < this.datasources.length; index++) {
        const element = this.datasources[index]
        element.isActive = false
      }
      object.isActive = true
      this.$forceUpdate()
      this.datasourceAttr = object
    },
    // 填报属性数据源下拉事件
    getDatabaseTables() {
      var obj = {
        params: { id: this.datasourceAttr.datasourceId },
        url: this.apis.reportDatasource.getDatabseTablesApi
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.dataSourceTables = response.responseData
        }
      })
    },
    // 获取表对应的列
    getTableColumns() {
      var key = this.datasourceAttr.datasourceId + '_' + this.datasourceAttr.table
      var columns = this.datasourceTableColumns[key]
      if (columns) {
        this.tableColumns = columns
      } else {
        var obj = {
          params: { datasourceId: this.datasourceAttr.datasourceId, tplSql: 'select * from ' + this.datasourceAttr.table, sqlType: 1 },
          url: this.apis.reportDesign.execSqlApi
        }
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            this.datasourceTableColumns[key] = response.responseData
            this.tableColumns = this.datasourceTableColumns[key]
          }
        })
      }
    },
    addDatasourceColumn() {
      if (!this.datasourceAttr.datasourceId) {
        this.commonUtil.showMessage({ message: '请选择数据源', type: this.commonConstants.messageType.error })
        return
      }
      if (!this.datasourceAttr.table) {
        this.commonUtil.showMessage({ message: '请选择表', type: this.commonConstants.messageType.error })
        return
      }
      this.datasourceColumnDialog = true
    },
    closeDatasourceColumn() {
      this.datasourceColumnDialog = false
    },
    deleteDatasourceColumn(index) {
      this.datasourceAttr.tableDatas.splice(index, 1)
    },
    confirmDatasourceColumn() {
      this.$refs['datasourceColumnRef'].validate((valid) => {
        if (valid) {
          var obj = {
            columnName: this.datasourceColumnForm.columnName,
            cellCoords: this.datasourceColumnForm.cellCoords
          }
          this.datasourceAttr.tableDatas.push(obj)
          this.closeDatasourceColumn()
          this.commonUtil.clearObj(this.datasourceColumnForm)
        } else {
          return false
        }
      })
    },
    addDatasourceKey() {
      if (!this.datasourceAttr.datasourceId) {
        this.commonUtil.showMessage({ message: '请选择数据源', type: this.commonConstants.messageType.error })
        return
      }
      if (!this.datasourceAttr.table) {
        this.commonUtil.showMessage({ message: '请选择表', type: this.commonConstants.messageType.error })
        return
      }
      this.datasourceKeyDialog = true
    },
    deleteDatasourceKey(index) {
      this.datasourceAttr.keys.splice(index, 1)
    },
    closeDatasourceKey() {
      this.datasourceKeyDialog = false
    },
    confirmDatasourceKey() {
      this.$refs['datasourceKeyRef'].validate((valid) => {
        if (valid) {
          var obj = {
            columnName: this.datasourceKeyForm.columnName,
            idType: this.datasourceKeyForm.idType
          }
          this.datasourceAttr.keys.push(obj)
          this.closeDatasourceKey()
          this.commonUtil.clearObj(this.datasourceKeyForm)
        } else {
          return false
        }
      })
    },
    getMaxSheetOrder() {
      var luckysheetfiles = luckysheet.getLuckysheetfile()
      let order = 0
      if (luckysheetfiles) {
        for (let index = 0; index < luckysheetfiles.length; index++) {
          const element = luckysheetfiles[index]
          if (element.order > order) {
            order = element.order
          }
        }
      }
      return order
    },
    luckysheetDeleteCell(type, str, edr, stc, edc, sheetIndex, isWebsocket) {
      this.cellDelete(type, str, edr, stc, edc, sheetIndex, isWebsocket)
    },
    cellDelete(type, str, edr, stc, edc, sheetIndex, isWebsocket) {
      const map = this.extraCustomCellConfigs[sheetIndex]
      if (Object.keys(map).length > 0) {
        for (var key in map) {
          const value = map[key]
          var r = parseInt(key.split('_')[0])
          var c = parseInt(key.split('_')[1])
          if (type == 'moveLeft') {
            if (r >= str && r <= edr) {
              if (c > edc) {
                delete map[r + '_' + c]
                var newc = c - (edc - stc + 1)
                map[r + '_' + newc] = value
              } else if (c >= stc && c <= edc) {
                delete map[r + '_' + c]
              }
            }
          } else if (type == 'moveUp') {
            if (c >= stc && c <= edc) {
              if (r > edr) {
                delete map[r + '_' + c]
                var newr = r - (edr - str + 1)
                map[newr + '_' + c] = value
              } else if (r >= str && r <= edr) {
                delete map[r + '_' + c]
              }
            }
          }
        }
      }
      if (this.sheetDatasource && this.sheetDatasource[sheetIndex] && this.sheetDatasource[sheetIndex].length > 0) {
        for (let index = 0; index < this.sheetDatasource[sheetIndex].length; index++) {
          const datasource = this.sheetDatasource[sheetIndex][index]
          var tableDatas = datasource.tableDatas
          var newTableDatas = []
          if (tableDatas && tableDatas.length > 0) {
            for (let index = 0; index < tableDatas.length; index++) {
              const element = tableDatas[index]
              var cellCoords = element.cellCoords
              var coords = this.commonUtil.getCoordsFromColumnName(cellCoords, true)
              var r = coords[0]
              var c = coords[1]
              if (type == 'moveLeft') {
                if (r >= str && r <= edr) {
                  if (c > edc) {
                    var newc = c - (edc - stc + 1)
                    element.cellCoords = this.commonUtil.getColumnFromCoords(r, newc)
                    newTableDatas.push(element)
                  } else if (c < stc) {
                    newTableDatas.push(element)
                  }
                } else {
                  newTableDatas.push(element)
                }
              } else if (type == 'moveUp') {
                if (c >= stc && c <= edc) {
                  if (r > edr) {
                    var newr = r - (edr - str + 1)
                    element.cellCoords = this.commonUtil.getColumnFromCoords(newr, c)
                    newTableDatas.push(element)
                  } else if (r < str) {
                    newTableDatas.push(element)
                  }
                } else {
                  newTableDatas.push(element)
                }
              }
            }
            datasource.tableDatas = newTableDatas
          }
        }
      }
      if (!isWebsocket) {
        this.saveTplCache()
      }
    },
    addCompareCells() {
      this.cellCompareVisiable = true
    },
    closeCompareCells() {
      this.cellCompareVisiable = false
      this.$refs['compareRef'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.cellCompareForm)
    },
    onfocuseSheet() {
      var luckysheetfiles = luckysheet.getLuckysheetfile()
      this.sheets = []
      for (let index = 0; index < luckysheetfiles.length; index++) {
        const luckysheetfile = luckysheetfiles[index]
        var obj = {
          label: luckysheetfile.name,
          value: luckysheetfile.name
        }
        this.sheets.push(obj)
      }
    },
    confirmAddCompareCells() {
      this.$refs['compareRef'].validate((valid) => {
        if (valid) {
          if (this.cellCompareForm.index != null) {
            this.cellForm.compareCells[this.cellCompareForm.index].sheetName = this.cellCompareForm.sheetName
            this.cellForm.compareCells[this.cellCompareForm.index].coordinate = this.cellCompareForm.coordinate
            this.cellForm.compareCells[this.cellCompareForm.index].cellType = this.cellCompareForm.cellType
            this.cellForm.compareCells[this.cellCompareForm.index].compareType = this.cellCompareForm.compareType
          } else {
            var obj = {
              sheetName: this.cellCompareForm.sheetName,
              coordinate: this.cellCompareForm.coordinate,
              cellType: this.cellCompareForm.cellType,
              compareType: this.cellCompareForm.compareType
            }
            if (!this.cellForm.compareCells) {
              this.cellForm.compareCells = []
            }
            this.cellForm.compareCells.push(obj)
          }
          this.changeCellAttr('compareCells')
          this.closeCompareCells()
        }
      })
    },
    editCompareCell(obj, index) {
      if (this.attrDisabled) {
        return
      }
      this.cellCompareVisiable = true
      this.cellCompareForm.index = index
      this.cellCompareForm.sheetName = obj.sheetName
      this.cellCompareForm.coordinate = obj.coordinate
      this.cellCompareForm.cellType = obj.cellType
      this.cellCompareForm.compareType = obj.compareType
    },
    deleteCompareCell(index) {
      if (this.attrDisabled) {
        return
      }
      this.cellForm.compareCells.splice(index, 1)
      var cells = this.getSelectRangeCells()
      if (cells && cells.length > 0) {
        var obj = {
          cells: cells,
          value: this.cellForm['compareCells']
        }
        const sheetIndex = luckysheet.getSheet().index
        luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': 'compareCells' })
        this.saveTplCache()
      }
    },
    addCellConditions() {
      this.cellConditionVisiable = true
    },
    // 确认单元格过滤
    confirmAddCellCondition() {
      this.$refs['conditionRef'].validate((valid) => {
        if (valid) {
          if (this.cellConditionForm.index != null) { // 修改属性
            this.cellForm.cellconditions[this.cellConditionForm.index].property = this.cellConditionForm.property
            this.cellForm.cellconditions[this.cellConditionForm.index].operator = this.cellConditionForm.operator
            this.cellForm.cellconditions[this.cellConditionForm.index].type = this.cellConditionForm.type
            this.cellForm.cellconditions[this.cellConditionForm.index].value = this.cellConditionForm.value
            this.cellForm.cellconditions[this.cellConditionForm.index].dateFormat = this.cellConditionForm.dateFormat
          } else {
            var data = {
              property: this.cellConditionForm.property,
              operator: this.cellConditionForm.operator,
              type: this.cellConditionForm.type,
              value: this.cellConditionForm.value,
              dateFormat: this.cellConditionForm.dateFormat
            }
            if (!this.cellForm.cellconditions) {
              this.cellForm.cellconditions = []
            }
            this.cellForm.cellconditions.push(data)
          }
          this.changeCellAttr('cellconditions')
          this.closeCellConditionDialog()
        } else {
          return false
        }
      })
    },
    closeCellConditionDialog() {
      this.$refs['conditionRef'].resetFields()
      this.commonUtil.clearObj(this.cellConditionForm)
      this.cellConditionForm.index = null
      this.cellConditionVisiable = false
    },
    deleteCellCondition(index) {
      if (this.attrDisabled) {
        return
      }
      this.cellForm.cellconditions.splice(index, 1)
      this.changeCellAttr('cellconditions')
    },
    editCellCondition(index) {
      if (this.attrDisabled) {
        return
      }
      const cellCondition = this.cellForm.cellconditions[index]
      this.cellConditionForm.property = cellCondition.property
      this.cellConditionForm.operator = cellCondition.operator
      this.cellConditionForm.type = cellCondition.type
      this.cellConditionForm.value = cellCondition.value
      this.cellConditionForm.dateFormat = cellCondition.dateFormat
      this.cellConditionForm.index = index
      this.cellConditionVisiable = true
    },
    copyCellCondition(row) {
      if (this.attrDisabled) {
        return
      }
      this.cellConditionForm.property = row.property
      this.cellConditionForm.operator = row.operator
      this.cellConditionForm.type = row.type
      this.cellConditionForm.value = row.value
      this.cellConditionForm.dateFormat = row.dateFormat
      this.cellConditionForm.index = null
      this.cellConditionVisiable = true
    },
    // 获取下钻报表模板
    getDrillReport(query) {
      var param = {
        tplName: query,
        currentPage: 1,
        pageSize: 4
      }
      var obj = {
        url: this.apis.reportTpl.getAllTplsApi,
        params: param
      }
      var that = this
      this.commonUtil.getTableList(obj).then(response => {
        that.reportTpls = response.responseData
      })
    },
    changePageHeaderShow() {
      if (this.settingFormData.pageHeaderShow == 1) {
        this.settingModalForm[3].show = true
        this.settingModalForm[3].rules.required = true
        this.settingModalForm[4].show = true
        this.settingModalForm[4].rules.required = true
      } else {
        this.settingModalForm[3].show = false
        this.settingModalForm[3].rules.required = false
        this.settingModalForm[4].show = false
        this.settingModalForm[4].rules.required = false
      }
    },
    changeWaterMarkShow() {
      if (this.settingFormData.waterMarkShow == 1) {
        this.settingModalForm[6].show = true
        this.settingModalForm[6].rules.required = true
        this.settingModalForm[7].show = true
        this.settingModalForm[7].rules.required = true
        this.settingModalForm[8].show = true
        this.settingModalForm[8].rules.required = false
        this.settingModalForm[9].show = true
        this.settingModalForm[9].rules.required = true
        this.settingModalForm[10].show = true
        this.settingModalForm[10].rules.required = true
        this.changeWaterMarkType()
      } else {
        this.settingModalForm[6].show = false
        this.settingModalForm[6].rules.required = false
        this.settingModalForm[7].show = false
        this.settingModalForm[7].rules.required = false
        this.settingModalForm[8].show = false
        this.settingModalForm[8].rules.required = false
        this.settingModalForm[9].show = false
        this.settingModalForm[9].rules.required = false
        this.settingModalForm[10].show = false
        this.settingModalForm[10].rules.required = false
      }
    },
    changeWaterMarkType() {
      if (this.settingFormData.waterMarkType == 1) {
        this.settingModalForm[7].show = true
        this.settingModalForm[7].rules.required = true
        this.settingModalForm[8].show = false
        this.settingModalForm[8].rules.required = false
        this.settingModalForm[9].show = false
        this.settingModalForm[9].rules.required = false
      } else {
        this.settingModalForm[7].show = false
        this.settingModalForm[7].rules.required = false
        this.settingModalForm[8].show = true
        this.settingModalForm[8].rules.required = false
        this.settingModalForm[9].show = true
        this.settingModalForm[9].rules.required = true
      }
    },
    changePageShow() {
      if (this.settingFormData.pageShow == 1) {
        this.settingModalForm[12].show = true
        this.settingModalForm[12].rules.required = true
      } else {
        this.settingModalForm[12].show = false
        this.settingModalForm[12].rules.required = false
      }
    },
    confirmPrintSettings() {
      var that = this
      this.$refs['settingRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var sheetIndex = luckysheet.getSheet().index
          var printSettings = that.sheetPrintSettings[sheetIndex]
          if (!printSettings) {
            printSettings = {}
            that.sheetPrintSettings[sheetIndex] = printSettings
          }
          printSettings.pageType = that.settingFormData.pageType
          printSettings.pageLayout = that.settingFormData.pageLayout
          printSettings.pageHeaderShow = that.settingFormData.pageHeaderShow
          printSettings.pageHeaderContent = that.settingFormData.pageHeaderContent
          printSettings.pageHeaderPosition = that.settingFormData.pageHeaderPosition
          printSettings.waterMarkShow = that.settingFormData.waterMarkShow
          printSettings.waterMarkType = that.settingFormData.waterMarkType
          printSettings.waterMarkContent = that.settingFormData.waterMarkContent
          printSettings.waterMarkImg = that.settingFormData.waterMarkImg
          printSettings.waterMarkOpacity = that.settingFormData.waterMarkOpacity
          printSettings.pageShow = that.settingFormData.pageShow
          printSettings.pagePosition = that.settingFormData.pagePosition
          printSettings.horizontalPage = that.settingFormData.horizontalPage
          printSettings.horizontalPageColumn = that.settingFormData.horizontalPageColumn
          if (printSettings.horizontalPage == 1) {
            luckysheet.addLuckysheetDivider(printSettings.horizontalPageColumn)
          } else {
            luckysheet.addLuckysheetDivider(null)
          }
          that.closeSettingModal()
          this.$refs['settingRef'].$refs['modalFormRef'].resetFields()
          that.commonUtil.clearObj(that.settingFormData)
          that.settingFormData = { ...that.defaultSettingFormData }
          that.changePageHeaderShow()
          that.changeWaterMarkShow()
          that.changePageShow()
          var obj = {
            value: printSettings
          }
          luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': 'printSettings' })
          this.saveTplCache()
        } else {
          return false
        }
      })
    },
    closeSettingModal() {
      this.settingFormData.waterMarkImgs = []
      this.settingModalConfig.show = false
    },
    // 回调函数
    doPostCallback() {
      this.loading = false
    },
    saveTplCache() {
      const configs = this.getSheetDatas()
      if (configs) {
        const reportTplId = this.$route.query.tplId
        var gridKey = 'designMode-' + reportTplId
        const sheetIndex = luckysheet.getSheet().index
        luckysheet.sendServerSocketMsg('reportTplTempCache', gridKey + '-' + sheetIndex, configs, { 'k': 'viewReportCache' })
      }
    },
    getSheetDatas(luckysheetfile) {
      if (!luckysheetfile) {
        luckysheetfile = luckysheet.getSheet()
      }
      if (!luckysheetfile.isPivotTable) {
        var cellDatas = this.getCellDatas(luckysheetfile)
        var extraCustomCellConfigs = this.getSheetExtraCustomCellConfigs(cellDatas, luckysheetfile.index)
        var configs = this.getSheetConfigs(luckysheetfile)
        var emptyMergeCell = this.getEmptyMergeCell(configs.config.merge, cellDatas)
        cellDatas = cellDatas.concat(emptyMergeCell)
        var borderCellDatas = this.getEmptyBorderCellDatas(configs.config, cellDatas)
        var datasourceConfig = this.getDatasourceConfig(luckysheetfile.index)
        cellDatas = cellDatas.concat(borderCellDatas)
        if (cellDatas && cellDatas.length > 0) {
          var sheetCellFormats = this.cellFormats[luckysheetfile.index]
          if (sheetCellFormats) {
            for (let index = 0; index < cellDatas.length; index++) {
              const element = cellDatas[index]
              var ct = sheetCellFormats[element.r + '_' + element.c]
              if (ct && ct.t && ct.t == 'inlineStr') {

              } else {
                if (ct) {
                  element.v.ct = ct
                }
              }
            }
          }
        }
        configs.cellDatas = cellDatas
        configs.extraCustomCellConfigs = extraCustomCellConfigs
        configs.datasourceConfig = datasourceConfig
        var printSettings = this.sheetPrintSettings[luckysheetfile.index]
        if (printSettings) {
          configs.printSettings = printSettings
        }
        return configs
      }
    },
    changeReportAttr(data) {
      const k = data.k
      const sheetIndex = data.i
      const currentIndex = luckysheet.getSheet().index
      const v = data.v
      const cells = v.cells
      const value = v.value
      if (k == 'isParamMerge') { // 参数合并
        this.isParamMerge = value
      } else if (k == 'printSettings') {
        // 打印设置
        var printSettings = this.sheetPrintSettings[sheetIndex]
        if (!printSettings) {
          printSettings = {}
          this.sheetPrintSettings[sheetIndex] = printSettings
        }
        printSettings.pageType = value.pageType
        printSettings.pageLayout = value.pageLayout
        printSettings.pageHeaderShow = value.pageHeaderShow
        printSettings.pageHeaderContent = value.pageHeaderContent
        printSettings.pageHeaderPosition = value.pageHeaderPosition
        printSettings.waterMarkShow = value.waterMarkShow
        printSettings.waterMarkType = value.waterMarkType
        printSettings.waterMarkContent = value.waterMarkContent
        printSettings.waterMarkImg = value.waterMarkImg
        printSettings.waterMarkOpacity = value.waterMarkOpacity
        printSettings.pageShow = value.pageShow
        printSettings.pagePosition = value.pagePosition
        printSettings.horizontalPage = value.horizontalPage
        printSettings.horizontalPageColumn = value.horizontalPageColumn
        if (currentIndex == sheetIndex && this.settingModalConfig.show) {
          this.settingFormData.pageType = value.pageType
          this.settingFormData.pageLayout = value.pageLayout
          this.settingFormData.pageHeaderShow = value.pageHeaderShow
          this.settingFormData.pageHeaderContent = value.pageHeaderContent
          this.settingFormData.pageHeaderPosition = value.pageHeaderPosition
          this.settingFormData.waterMarkShow = value.waterMarkShow
          this.settingFormData.waterMarkType = value.waterMarkType
          this.settingFormData.waterMarkContent = value.waterMarkContent
          this.settingFormData.waterMarkImg = value.waterMarkImg
          this.settingFormData.waterMarkOpacity = value.waterMarkOpacity
          this.settingFormData.pageShow = value.pageShow
          this.settingFormData.pagePosition = value.pagePosition
          this.settingFormData.horizontalPage = value.horizontalPage
          this.settingFormData.horizontalPageColumn = value.horizontalPageColumn
          this.changePageHeaderShow()
          this.changeWaterMarkShow()
          this.changePageShow()
          this.changeHorizontalPage()
        }
      } else if (k == 'datasetChanged') {
        this.getTplGroupDatasets()
        this.commonUtil.showMessage({ message: '报表数据集更新，数据集名称：' + v.datasetName + '，操作人：' + data.userName, type: this.commonConstants.messageType.warning })
      } else if (k == 'sheetNotExist') {
        this.commonUtil.showMessage({ message: '该sheet页已经被删除，请尝试刷新页面获取最新的模板数据', type: this.commonConstants.messageType.warning })
      } else if (k == 'deleteDataSet') {
        if (this.datasets && this.datasets.length > 0) {
          for (let index = 0; index < this.datasets.length; index++) {
            const element = this.datasets[index]
            if (element.id == value.id) {
              this.commonUtil.showMessage({ message: '报表数据集【' + element.datasetName + '】被删除，操作人：' + value.userName, type: this.commonConstants.messageType.warning })
              this.datasets.splice(index, 1)
              break
            }
          }
        }
      } else if (k == 'datasourceChanged') {
        const sheetName = v.sheetName
        this.sheetDatasource[sheetIndex] = value
        if (sheetIndex == currentIndex) {
          this.commonUtil.showMessage({ message: '【' + sheetName + '】绑定数据源属性发生变更，操作人：' + data.userName, type: this.commonConstants.messageType.warning })
          if (this.datasourceDialog) {
            if (value) {
              this.datasources = JSON.parse(JSON.stringify(value))
              for (let index = 0; index < this.datasources.length; index++) {
                const element = this.datasources[index]
                element.isActive = false
                if (index == 0) {
                  element.isActive = true
                  this.datasourceAttr = element
                  if (this.datasourceAttr.datasourceId) {
                    this.getDatabaseTables()
                    if (this.datasourceAttr.table) {
                      this.getTableColumns()
                    }
                  }
                }
              }
            }
          }
        }
      } else if (k == 'refreshAuth') {
        // 更新权限
        if (!this.isCreator) {
          this.getTplAuth()
        }
      } else {
        if (cells && cells.length > 0) {
          var selectedRanges = luckysheet.getRange()
          var currentr = selectedRanges[0].row[0]
          var currentc = selectedRanges[0].column[0]
          for (let index = 0; index < cells.length; index++) {
            const r = cells[index][0]
            const c = cells[index][1]
            var obj = this.getExtraCustomCellConfigs(r, c, sheetIndex)
            if (obj) {
              obj[k] = value
            } else {
              obj = {}
              obj[k] = value
            }
            if (sheetIndex == currentIndex) {
              if (currentr == r && currentc == c) {
                this.cellForm[k] = value
                if (k == 'datasourceId') {
                  this.getDatasourceAttr()
                } else if (k == 'isDrill') {
                  if (value) {
                    this.getDrillReport()
                  }
                }
              }
            }
          }
        }
      }
    },
    userChanged(data) {
      this.users = data.v
      if (this.users && this.users.length > 15) {
        this.headerUsers = this.users.slice(0, 15)
      } else {
        this.headerUsers = this.users
      }
    },
    frozenCancelAfter() {
      const configs = this.getSheetDatas()
      if (configs) {
        const reportTplId = this.$route.query.tplId
        var gridKey = 'designMode-' + reportTplId
        const sheetIndex = luckysheet.getSheet().index
        luckysheet.sendServerSocketMsg('frozenCancleTempCache', gridKey + '-' + sheetIndex, configs, { 'k': 'viewReportCache' })
      }
    },
    deleteSheet(index) {
      const reportTplId = this.$route.query.tplId
      var gridKey = 'designMode-' + reportTplId + '-' + index
      luckysheet.sendServerSocketMsg('deleteSheet', gridKey, null, { 'k': 'deleteSheet' })
    },
    changeParamMerge() {
      var obj = {
        value: this.isParamMerge
      }
      const sheetIndex = luckysheet.getSheet().index
      luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': 'isParamMerge' })
      this.saveTplAttrCache()
    },
    deleteDataSetCallback(result) {
      this.getDataSets()
      this.getTplGroupDatasets();
      var obj = {
        cells: {},
        value: result.responseData
      }
      const sheetIndex = luckysheet.getSheet().index
      luckysheet.sendServerSocketMsg('reportDesign', sheetIndex, obj, { 'k': 'deleteDataSet' })
    },
    saveNewSheetCache(sheet) {
      const configs = this.getSheetDatas(sheet.sheet)
      if (configs) {
        const reportTplId = this.$route.query.tplId
        var gridKey = 'designMode-' + reportTplId
        const sheetIndex = sheet.sheet.index
        luckysheet.sendServerSocketMsg('reportTplNewTempCache', gridKey + '-' + sheetIndex, configs, { 'k': 'viewReportCache' })
      }
    },
    changeHorizontalPage() {
      if (this.settingFormData.horizontalPage == 1) {
        this.settingModalForm[14].show = true
        this.settingModalForm[14].rules.required = true
      } else {
        this.settingModalForm[14].show = false
        this.settingModalForm[14].rules.required = false
      }
    },
    addAuthClick() {
      const rangeAxis = luckysheet.getRangeAxis()
      if (!rangeAxis || rangeAxis.length == 0) {
        this.commonUtil.showMessage({ message: '请先选择要设置的区域。', type: this.commonConstants.messageType.error })
        return
      } else if (rangeAxis.length > 1) {
        this.commonUtil.showMessage({ message: '不能对多个选区执行此操作，请选择单个区域进行操作。', type: this.commonConstants.messageType.error })
        return
      }
      if (!this.isCreator) {
        this.commonUtil.showMessage({ message: '您没有权限进行此操作，只有创建者【' + this.creatorName + '】才有权限进行操作。', type: this.commonConstants.messageType.error })
        return
      }
      this.rangeAxis = rangeAxis[0]
      const sheetIndex = luckysheet.getSheet().index
      if (!this.sheetRangeAuth[sheetIndex]) {
        this.sheetRangeAuth[sheetIndex] = {}
      }
      const rangeAuth = this.sheetRangeAuth[sheetIndex]
      if (rangeAuth[this.rangeAxis]) {
        this.commonUtil.showMessage({ message: '该选区已经设置权限，请勿重复设置。', type: this.commonConstants.messageType.error })
        return
      }
      this.range = luckysheet.getRange()[0]
      this.authTitle = '为选区【' + rangeAxis[0] + '】添加保护权限'
      this.addAuthVisiable = true
    },
    closeAddAuth() {
      this.addAuthVisiable = false
      this.addAuthForm.userIds = []
      this.rangeAxis = null
      this.range = null
      this.$refs.tree.setCheckedKeys([])
      this.defaultCheckedUsers = []
    },
    confirmAddAuth() {
      const checkedKeys = this.$refs.tree.getCheckedKeys()
      if (checkedKeys && checkedKeys.length > 0) {
        const userIds = []
        for (let index = 0; index < checkedKeys.length; index++) {
          const element = checkedKeys[index]
          if (element.indexOf('_dept') < 0) {
            userIds.push(element)
          }
        }
        const sheetIndex = luckysheet.getSheet().index
        if (!this.sheetRangeAuth[sheetIndex]) {
          this.sheetRangeAuth[sheetIndex] = {}
        }
        const rangeAuth = this.sheetRangeAuth[sheetIndex]
        rangeAuth[this.rangeAxis] = {
          rangeAxis: this.rangeAxis,
          range: this.range,
          sheetIndex: sheetIndex,
          userIds: userIds
        }
        this.authRangeToArray()
        this.closeAddAuth()
      } else {
        this.commonUtil.showMessage({ message: '请添加授权用户。', type: this.commonConstants.messageType.error })
      }
    },
    viewAuthClick() {
      this.authRangeToArray()
      if (this.isCreator) {
        this.authedRangeTitle = '保护范围'
      } else {
        this.authedRangeTitle = '可操作范围'
      }
      this.authdialogVisible = true
    },
    authRangeToArray() {
      const sheetIndex = luckysheet.getSheet().index
      this.authedRange = []
      if (this.sheetRangeAuth) {
        if (this.sheetRangeAuth[sheetIndex]) {
          for (var key in this.sheetRangeAuth[sheetIndex]) {
            this.authedRange.push(this.sheetRangeAuth[sheetIndex][key])
          }
        }
      }
    },
    getUsers() {
      const obj = {
        url: this.apis.sysUser.getDeptUserTreeApi,
        params: {},
        removeEmpty: false
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.authUsers = response.responseData
        }
      })
    },
    checkUserEditAuth(r, c) {
      const result = false
      if (this.isCreator) {
        return true
      } else {
        const sheetIndex = luckysheet.getSheet().index
        if (!this.sheetRangeAuth[sheetIndex]) {
          return false
        } else {
          const rangeAuth = this.sheetRangeAuth[sheetIndex]
          for (var key in rangeAuth) {
            const range = rangeAuth[key].range
            const row = range.row
            const column = range.column
            const str = row[0]; const edr = row[1]
            const stc = column[0]; const edc = column[1]
            if (r >= str && r <= edr && c >= stc && c <= edc) {
              return true
            }
          }
        }
        return result
      }
    },
    cellEditBefore(range) {
      const row = range[0].row
      const column = range[0].column
      const checkResult = this.checkUserEditAuth(row[0], column[0])
      if (!checkResult) {
        this.commonUtil.showMessage({ message: '暂无编辑权限，如需编辑可联系创建者【' + this.creatorName + '】。', type: this.commonConstants.messageType.error })
        return false
      }
    },
    copyPasteBefore(range, copyRange) {
      if (this.isCreator) {
        return true
      } else {
        const r = range[0].row[0]
        const c = range[0].column[0]
        const copyRowColSpan = this.getCopyRangeRowColSpan(copyRange.copyRange)
        const checkResult = this.checkPasteRange(r, c, copyRowColSpan[0], copyRowColSpan[1])
        if (!checkResult) {
          this.commonUtil.showMessage({ message: '粘贴部分包含暂无编辑权限的单元格，如需编辑可联系创建者【' + this.creatorName + '】。', type: this.commonConstants.messageType.error })
          return false
        }
        return checkResult
      }
    },
    checkPasteRange(r, c, rowspan, colspan) {
      const sheetIndex = luckysheet.getSheet().index
      if (!this.sheetRangeAuth[sheetIndex]) {
        return false
      } else {
        const rangeAuth = this.sheetRangeAuth[sheetIndex]
        const authedCells = this.getAuthedCells(rangeAuth)
        const pastRangeCells = this.getRangeCells(r, c, rowspan, colspan)
        if (authedCells == null || Object.keys(authedCells).length === 0) {
          return false
        } else {
          for (var key in pastRangeCells) {
            if (!(key in authedCells)) {
              return false
            }
          }
        }
        return true
      }
    },
    // 获取所有已授权的单元格
    getAuthedCells(rangeAuth) {
      const sheetIndex = luckysheet.getSheet().index
      let cells = this.sheetAuthedCells[sheetIndex]
      if (cells == null) {
        cells = {}
        for (var key in rangeAuth) {
          const range = rangeAuth[key].range
          const row = range.row
          const column = range.column
          const str = row[0]; const edr = row[1]
          const stc = column[0]; const edc = column[1]
          for (let i = str; i <= edr; i++) {
            for (let t = stc; t <= edc; t++) {
              cells[i + '_' + t] = '1'
            }
          }
        }
        this.sheetAuthedCells[sheetIndex] = cells
      }
      return cells
    },
    getRangeCells(r, c, rowspan, colspan) {
      const cells = {}
      for (let i = 0; i < rowspan; i++) {
        for (let t = 0; t < colspan; t++) {
          cells[(r + i) + '_' + (c + t)] = '1'
        }
      }
      return cells
    },
    getCopyRangeRowColSpan(copyRange) {
      let rowSpan = 0
      let colSpan = 0
      if (copyRange.length > 1) {
        let isSameRow = true
        const str_r = copyRange[0].row[0]
        const end_r = copyRange[0].row[1]
        for (let s = 1; s < copyRange.length; s++) {
          if (copyRange[s].row[0] != str_r || copyRange[s].row[1] != end_r) {
            isSameRow = false
            break
          }
        }
        if (isSameRow) {
          rowSpan = copyRange[0].row[1] - copyRange[0].row[0] + 1
        } else {
          colSpan = copyRange[0].column[1] - copyRange[0].column[0] + 1
        }
        for (let index = 0; index < copyRange.length; index++) {
          const element = copyRange[index]
          const row = element.row
          const column = element.column
          const rs = row[1] - row[0] + 1
          const cs = column[1] - column[0] + 1
          if (isSameRow) {
            colSpan = colSpan + cs
          } else {
            rowSpan = rowSpan + rs
          }
        }
      } else {
        const row = copyRange[0].row
        const column = copyRange[0].column
        rowSpan = row[1] - row[0] + 1
        colSpan = column[1] - column[0] + 1
      }
      const result = [rowSpan, colSpan]
      return result
    },
    pasteHandlerBefore(range, data) {
      if (this.isCreator) {
        return true
      } else {
        const r = range[0].row[0]
        const c = range[0].column[0]
        const rs = data.length
        const cs = data[0].length
        const checkResult = this.checkPasteRange(r, c, rs, cs)
        if (!checkResult) {
          this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.auth.operate', [this.creatorName]), type: this.commonConstants.messageType.error })
          return false
        }
        return checkResult
      }
    },
    rangeAuthCheck(range) {
      if (this.isCreator) {
        return true
      } else {
        const checkResult = this.checkRangeAuth(range)
        if (!checkResult) {
          this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.auth.operate', [this.creatorName]), type: this.commonConstants.messageType.error })
        }
        return checkResult
      }
    },
    formulaAuthCheck(range) {
      if (this.isCreator) {
        return true
      } else {
        const checkResult = this.checkRangeAuth(range)
        if (!checkResult) {
          this.commonUtil.showMessage({ message: '公式计算结果不在可操作范围内，如需编辑可联系创建者【' + this.creatorName + '】。', type: this.commonConstants.messageType.error })
        }
        return checkResult
      }
    },
    checkRangeAuth(range) {
      if (this.isCreator) {
        return true
      }
      const sheetIndex = luckysheet.getSheet().index
      const rangeAuth = this.sheetRangeAuth[sheetIndex]
      const authedCells = this.getAuthedCells(rangeAuth)
      let rangeCells = {}
      for (let index = 0; index < range.length; index++) {
        const element = range[index]
        const r = element.row[0]
        const c = element.column[0]
        const rs = element.row[1] - element.row[0] + 1
        const cs = element.column[1] - element.column[0] + 1
        const cells = this.getRangeCells(r, c, rs, cs)
        rangeCells = { ...rangeCells, ...cells }
      }
      if (authedCells == null || Object.keys(authedCells).length === 0) {
        return false
      } else {
        for (var key in rangeCells) {
          if (!(key in authedCells)) {
            return false
          }
        }
        return true
      }
    },
    editRange(range) {
      this.rangeAxis = range.rangeAxis
      this.range = range.range
      this.authTitle = '修改选区【' + range.rangeAxis + '】权限'
      // this.addAuthForm.userIds = range.userIds;
      this.defaultCheckedUsers = range.userIds
      this.addAuthVisiable = true
    },
    deleteRange(range, index) {
      this.authedRange.splice(index, 1)
      const sheetIndex = luckysheet.getSheet().index
      if (this.sheetRangeAuth) {
        if (this.sheetRangeAuth[sheetIndex]) {
          delete this.sheetRangeAuth[sheetIndex][range.rangeAxis]
        }
      }
    },
    showRange(range) {
      luckysheet.addLuckysheetAuthRange([range.range])
    },
    closeAuthDialog() {
      this.authdialogVisible = false
      this.authedRange = []
      if (this.isCreator) {
        luckysheet.addLuckysheetAuthRange(null)
      }
    },
    // 获取使用者授权范围
    getTplAuth() {
      const sheetIndex = luckysheet.getSheet().index
      const reportTplId = this.$route.query.tplId// reportTplId
      const obj = {
        url: this.apis.reportTpl.getTplAuthApi,
        params: { id: reportTplId },
        removeEmpty: false
      }
      this.sheetAuthedCells = {}
      this.authedRange = []
      // luckysheet.addLuckysheetAuthRange();
      var that = this
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          that.sheetRangeAuth = response.responseData
          that.authRangeToArray()
          that.showSheetAuthedRanges(sheetIndex)
        }
      })
    },
    showSheetAuthedRanges(sheetIndex) {
      if (this.sheetRangeAuth) {
        if (this.sheetRangeAuth[sheetIndex]) {
          const range = []
          for (var key in this.sheetRangeAuth[sheetIndex]) {
            range.push(this.sheetRangeAuth[sheetIndex][key].range)
          }
          luckysheet.addLuckysheetAuthRange(range)
        } else {
          luckysheet.addLuckysheetAuthRange(null)
        }
      } else {
        luckysheet.addLuckysheetAuthRange(null)
      }
    },
    sheetActivateAfter(index) {
      this.addAuthVisiable = false
      this.authdialogVisible = false
      if (!this.isCreator) {
        this.showSheetAuthedRanges(index)
      } else {
        luckysheet.addLuckysheetAuthRange(null)
      }
      this.showAuthInfoMsg()
    },
    loadDataAfter() {
      this.showAuthInfoMsg()
    },
    showAuthInfoMsg() {
      if (!this.isCreator) {
        const sheetIndex = luckysheet.getSheet().index
        if (this.sheetRangeAuth) {
          if (this.sheetRangeAuth[sheetIndex]) {
            this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('warning.auth.operate', [this.creatorName]), type: this.commonConstants.messageType.warning })
          } else {
            this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('warning.auth.nooperate', [this.creatorName]), type: this.commonConstants.messageType.warning })
          }
        } else {
          this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('warning.auth.nooperate', [this.creatorName]), type: this.commonConstants.messageType.warning })
        }
      }
    },
    // 修改单元格格式监听事件
    updateCellFormat(r, c, foucsStatus, type) {
      var sheetIndex = luckysheet.getSheet().index
      var sheetCellFormats = this.cellFormats[sheetIndex]
      if (sheetCellFormats) {
        var ct = sheetCellFormats[r + '_' + c]
        if (ct) {
          ct.t = type
          ct.fa = foucsStatus
        }
      }
    },
    // 单元格鼠标点击监听事件
    cellMousedown(cell, postion, sheetFile, ctx) {
      var selectedRanges = luckysheet.getRange()
      var r = selectedRanges[0].row[0]
      var c = selectedRanges[0].column[0]
      var sheetIndex = luckysheet.getSheet().index
      var sheetCellFormats = this.cellFormats[sheetIndex]
      var order = luckysheet.getSheet().order
      if (sheetCellFormats) {
        var ct = sheetCellFormats[r + '_' + c]
        if (ct && ct.t && ct.t == 'inlineStr') {

        } else {
          if (ct) {
            luckysheet.setCellFormat(r, c, 'ct', ct, { order: order })
          }
        }
      }
    },
    uploadFileClick(type) {
      this.uploadType = type
      $('#uploadBtn').click() // 触发父容器中的保存模板按钮事件
    },
    datasourceClick() {
      var that = this
      // 绑定数据源属性
      var sheetIndex = luckysheet.getSheet().index
      var datasource = that.sheetDatasource[sheetIndex]
      if (datasource) {
        that.datasources = JSON.parse(JSON.stringify(datasource))
        for (let index = 0; index < that.datasources.length; index++) {
          const element = that.datasources[index]
          element.isActive = false
          if (index == 0) {
            element.isActive = true
            that.datasourceAttr = element
            if (that.datasourceAttr.datasourceId) {
              that.getDatabaseTables()
              if (that.datasourceAttr.table) {
                that.getTableColumns()
              }
            }
          }
        }
      } else {
        that.datasources = []
        that.datasourceAttr = {
          name: '', // 名称
          datasourceId: '', // 数据源id
          table: '', // 表名
          keys: [], // 主键
          tableDatas: []// 属性对应关系
        }
      }
      that.datasourceDialog = true
    },
    pdfsettingClick() {
      var that = this
      // PDF/打印设置
      that.settingModalConfig.show = true
      var sheetIndex = luckysheet.getSheet().index
      var printSettings = that.sheetPrintSettings[sheetIndex]
      if (printSettings) {
        that.settingFormData.pageType = printSettings.pageType
        that.settingFormData.pageLayout = printSettings.pageLayout
        that.settingFormData.pageHeaderShow = printSettings.pageHeaderShow
        that.settingFormData.pageHeaderContent = printSettings.pageHeaderContent
        that.settingFormData.pageHeaderPosition = printSettings.pageHeaderPosition
        that.settingFormData.waterMarkShow = printSettings.waterMarkShow
        that.settingFormData.waterMarkType = printSettings.waterMarkType
        that.settingFormData.waterMarkContent = printSettings.waterMarkContent
        that.settingFormData.waterMarkImg = printSettings.waterMarkImg
        that.settingFormData.waterMarkOpacity = printSettings.waterMarkOpacity + ''
        that.settingFormData.pageShow = printSettings.pageShow
        that.settingFormData.pagePosition = printSettings.pagePosition
        that.settingFormData.horizontalPage = printSettings.horizontalPage
        that.settingFormData.horizontalPageColumn = printSettings.horizontalPageColumn
      } else {
        that.settingFormData = { ...that.defaultSettingFormData }
      }
      that.changePageHeaderShow()
      that.changeWaterMarkShow()
      that.changePageShow()
      that.changeHorizontalPage()
    },
    doCopy(item) {
      let text = item.value
      if (item.type == 'number') {
        text = '<if test="' + item.value + '!=null' + '"> \n'
        text = text + '  and ' + item.column + ' = #{' + item.value + '} \n' + '</if>'
      } else {
        text = '<if test="' + item.value + '!=null and ' + item.value + "!=''" + '">\n'
        text = text + '  and ' + item.column + ' = #{' + item.value + '} \n' + '</if>'
      }
      const input = document.getElementById('clipboradInput') // 承载复制内容
      input.value = text // 修改文本框的内容
      input.select() // 选中文本
      document.execCommand('copy') // 执行浏览器复制命令
      this.$message.success('复制成功')
    },
    copyColumn(datasetName, columnName) {
      const text = datasetName + '.${' + columnName + '}'
      const input = document.getElementById('clipboradInput') // 承载复制内容
      input.value = text // 修改文本框的内容
      input.select() // 选中文本
      document.execCommand('copy') // 执行浏览器复制命令
      this.$message.success('复制成功')
    },
    uploadAttachment() {
      const rangeAxis = luckysheet.getRangeAxis()
      if (!rangeAxis || rangeAxis.length == 0) {
        this.commonUtil.showMessage({ message: '请先选择单元格。', type: this.commonConstants.messageType.error })
        return
      }
      $('#uploadAttachmentBtn').click() // 触发父容器中的保存模板按钮事件
    },
    viewAttachment(item, r, c) {
      const fileType = this.commonUtil.getFileExt(item.linkAddress)
      if (fileType) {
        if (this.commonConstants.attachPreviewExt.includes(fileType)) {
          const viewReport = this.$router.resolve({ name: 'attachment', query: { url: item.linkAddress, name: item.fileName, fileType: fileType, 'thirdPartyType': localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType) }})
          window.open(viewReport.href, '_blank')
        } else {
          window.open(item.linkAddress, '_blank')
        }
      } else {
        window.open(item.linkAddress, '_blank')
      }
    },
    changeAttachment(evt) {
      this.loading = true
      const obj = {
        url: this.apis.common.uploadFileApi,
        callback: this.doPostCallback
      }
      const files = evt.target.files
      if (files == null || files.length == 0) {
        alert('请选择文件')
        return
      }
      // 获取文件名
      this.commonUtil.uploadFile(files[0], obj).then(response => {
        if (response.code == '200') {
          const range = luckysheet.getRange()[0]
          const r = range.row[0]
          const c = range.column[0]
          const luckysheetfile = luckysheet.getSheet()
          let cell = luckysheetfile.data[r][c]
          if (cell == null) {
            cell = {}
          }
          cell.fc = 'rgb(0, 0, 255)'
          cell.un = 1
          cell.v = cell.m = '附件:' + response.responseData.fileName
          const item = {
            linkType: 'attachment',
            linkAddress: response.responseData.fileUri,
            linkTooltip: '',
            fileName: response.responseData.fileName
          }
          if (luckysheetfile.hyperlink == null) {
            luckysheetfile.hyperlink = {}
          }
          luckysheetfile.hyperlink[r + '_' + c] = item
          luckysheetfile.data[r][c] = cell
          luckysheet.refresh({}, true, '上传附件：' + response.responseData.fileName + '，附件地址：' + response.responseData.fileUri)
        }
      })
      evt.target.value = ''
    }
  },
  watch: {
    'settingFormData.waterMarkImgs': function(newValue, oldValue) {
      // 当obj的prop1属性发生变化时，执行的回调函数
      if (newValue && newValue.length > 0) {
        const url = newValue[0].url
        this.settingFormData.waterMarkImg = url
      }
    }
  }
}
