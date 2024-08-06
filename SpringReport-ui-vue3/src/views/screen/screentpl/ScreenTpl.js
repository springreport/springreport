export default {
  name: 'screenTpl',
  data() {
    return {
      pageData: {
        // 查询表单内容 start
        searchForm: [
          { type: 'Input', label: '模板标识', prop: 'tplCode' },
          { type: 'Input', label: '模板名称', prop: 'tplName' }
        ],
        // 查询表单内容 end
        // 查询条件 start
        queryData: {
          tplCode: '', // 模板标识
          tplName: '' // 模板名称
        },
        // 查询条件 end
        // 查询表单按钮start
        searchHandle: [
          { label: '查询', type: 'primary', handle: () => this.searchtablelist(), auth: 'screenTpl_search' },
          { label: '重置', type: 'warning', handle: () => this.resetSearch(), auth: 'screenTpl_search' }
        ],
        // 查询表单按钮end
        // 表格数据start
        tableData: [],
        // 表格数据end
        // 表格工具栏按钮 start
        tableHandles: [
          { label: '新增', type: 'primary', handle: () => this.showModal(this.commonConstants.modalType.insert), auth: 'screenTpl_insert' },
          { label: '批量删除', type: 'danger', handle: () => this.deleteBatch(), auth: 'screenTpl_deleteBatch' }
        ],
        // 表格工具栏按钮 end
        selectList: [], // 表格选中的数据
        // 表格分页信息start
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50]
        },
        // 表格分页信息end
        // 表格列表头start
        tableCols: [
          { label: '模板标识', prop: 'tplCode', align: 'center',overflow:true},
          { label: '模板名称', prop: 'tplName', align: 'center',overflow:true},
          { label: '大屏宽度', prop: 'width', align: 'center',overflow:true},
          { label: '大屏高度', prop: 'height', align: 'center',overflow:true},
          { label: '背景图', prop: 'imgUrl', align: 'center', type: 'image', popover: true},
          { label: '操作', prop: 'operation', align: 'center', type: 'button',width:400,fixed:'right', btnList: [
            { label: '查看', type: 'primary', auth: 'screenTpl_getDetail', handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id) },
            { label: '编辑', type: 'primary', auth: 'screenTpl_update', handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id) },
            { label: '大屏设计', type: 'primary', auth: 'screenTpl_screenDesign', handle: (row) => this.screenDesign(row) },
            { label: '查看大屏', type: 'primary', auth: 'screenTpl_viewScreen', handle: (row) => this.screenView(row) },
            { label: '删除', type: 'primary', auth: 'screenTpl_delete', handle: (row) => this.deleteOne(row.id) }
          ] }
        ],
        // 表格列表头end
        // modal配置 start
        modalConfig: {
          title: '新增', // 弹窗标题,值为:新增，查看，编辑
          show: false, // 弹框显示
          formEditDisabled: false, // 编辑弹窗是否可编辑
          width: '700px', // 弹出框宽度
          modalRef: 'modalRef', // modal标识
          type: '1'// 类型 1新增 2编辑 3保存
        },
        // modal配置 end
        // modal表单 start
        modalForm: [
          { type: 'Input', label: '模板标识', prop: 'tplCode', rules: { required: true, maxLength: 40 }},
          { type: 'Input', label: '模板名称', prop: 'tplName', rules: { required: true, maxLength: 40 }},
          { type: 'Input', label: '大屏宽度', prop: 'width', rules: { required: true, type: 'number' }},
          { type: 'Input', label: '大屏高度', prop: 'height', rules: { required: true, type: 'number' }},
          { type: 'Select', label: '大屏数据源', prop: 'dataSource', rules: { required: false }, multiple: true, props: { label: 'code', value: 'id' }}
        ],
        // modal表单 end
        // modal 数据 start
        modalData: {// modal页面数据
          tplCode: '', // 模板标识
          tplName: '', // 模板名称
          width: '', // 大屏宽度
          height: '' // 大屏高度
        },
        // modal 数据 end
        // modal 按钮 start
        modalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeModal() },
          { label: '提交', type: 'primary', handle: () => this.save() }
        ],
        // modal 按钮 end
        // modal配置 start
        copyModalConfig: {
          title: '复制模板', // 弹窗标题,值为:新增，查看，编辑
          show: false, // 弹框显示
          formEditDisabled: false, // 编辑弹窗是否可编辑
          width: '700px', // 弹出框宽度
          modalRef: 'modalRef', // modal标识
          type: '1'// 类型 1新增 2编辑 3保存
        },
        // modal配置 end
        // modal表单 start
        copyModalForm: [
          { type: 'Input', label: '模板标识', prop: 'tplCode', rules: { required: true, maxLength: 40 }},
          { type: 'Input', label: '模板名称', prop: 'tplName', rules: { required: true, maxLength: 40 }},
          { type: 'Input', label: '大屏宽度', prop: 'width', rules: { required: true, type: 'number' }},
          { type: 'Input', label: '大屏高度', prop: 'height', rules: { required: true, type: 'number' }},
          { type: 'Select', label: '大屏数据源', prop: 'dataSource',disabled:this.commonUtil.disabled, rules: { required: false }, multiple: true, props: { label: 'code', value: 'id' }}
        ],
        // modal表单 end
        // modal 数据 start
        copyModalData: {// modal页面数据
          tplCode: '', // 模板标识
          tplName: '', // 模板名称
          width: '', // 大屏宽度
          height: '' // 大屏高度
        },
        // modal 数据 end
        // modal 按钮 start
        copyModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeCopyModal() },
          { label: '提交', type: 'primary', handle: () => this.doCopy() }
        ]
        // modal 按钮 end
      }
    }
  },
  mounted() {
    this.pageData.tableData = [];
    this.searchtablelist()
    this.getReportDatasource()
  },
  methods: {
    /**
     * @description: 获取表格数据
     * @param {type}
     * @return:
     * @author: caiyang
     */
    searchtablelist() {
      var obj = {
        url: this.apis.screenTpl.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage)
      }
      this.commonUtil.getTableList(obj).then(response => {
        this.commonUtil.tableAssignment(response, this.pageData.tablePage, this.pageData.tableData)
        this.$nextTick(() => {
          this.$refs.custable.$refs.cesTable.doLayout();
        });
      })
    },
    resetSearch() {
      this.commonUtil.clearObj(this.pageData.queryData)
      this.searchtablelist()
    },
    /**
     * @description: modal显示
     * @param {type} 类型 1新增，2编辑 3详情
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    showModal(type, id) {
      this.commonUtil.showModal(this.pageData.modalConfig, type)
      if (type != this.commonConstants.modalType.insert) {
        this.getDetail(id)
      }
    },
    /**
     * @description: 获取详细数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    getDetail(id) {
      var obj = {
        url: this.apis.screenTpl.getDetailApi,
        params: { id: id }
      }
      this.commonUtil.doGet(obj).then(response => {
        response.responseData.width = response.responseData.width + '';
        response.responseData.height = response.responseData.height + '';
        this.commonUtil.coperyProperties(this.pageData.modalData, response.responseData)// 数据赋值
      })
    },
    /**
     * @description: 关闭modal
     * @param
     * @return:
     * @author: caiyang
     */
    closeModal() {
      this.$refs['modalRef'].$refs['modalFormRef'].resetFields()// 校验重置
      this.pageData.modalConfig.show = false// 关闭modal
      this.commonUtil.clearObj(this.pageData.modalData)// 清空modalData
    },
    /**
     * @description: 保存数据
     * @param {type}
     * @return:
     * @author: caiyang
     */
    save() {
      this.$refs['modalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            params: this.pageData.modalData,
            removeEmpty: false
          }
          if (this.pageData.modalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.screenTpl.insertApi
          } else {
            obj.url = this.apis.screenTpl.updateApi
          }
          this.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              this.closeModal()
              this.searchtablelist()
            }
          })
        } else {
          return false
        }
      })
    },
    /**
     * @description: 删除一条数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    deleteOne(id) {
      const obj = {
        url: this.apis.screenTpl.deleteOneApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.searchtablelist,
        params: { id: id },
        type: 'get'
      }
      // 弹出删除确认框
      this.commonUtil.showConfirm(obj)
    },
    /**
     * @description: 批量删除
     * @param {type}
     * @return:
     * @author: caiyang
     */
    deleteBatch() {
      const length = this.pageData.selectList.length
      if (length == 0) {
        this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.batchdelete.empty', null), type: this.commonConstants.messageType.error })
      } else {
        const ids = new Array()
        for (let i = 0; i < length; i++) {
          ids.push(this.pageData.selectList[i].id)
        }
        const obj = {
          url: this.apis.screenTpl.deleteBatchApi,
          messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
          callback: this.searchtablelist,
          params: ids,
          type: 'post'
        }
        this.commonUtil.showConfirm(obj)
      }
    },
    selectChange(rows) {
      this.pageData.selectList = rows
    },
    screenDesign(row) {
      const viewReport = this.$router.resolve({ name: 'screenDesign', query: { tplId: row.id }})
      window.open(viewReport.href, '_blank')
    },
    // 获取数据源
    getReportDatasource() {
      var obj = {
        params: {},
        url: this.apis.reportDatasource.getReportDatasourceApi
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.pageData.modalForm[4].options = response.responseData
          this.pageData.copyModalForm[4].options = response.responseData
        }
      })
    },
    // 通过socket刷新大屏
    refreshScreen(row) {
      var obj = {
        url: this.apis.screenTpl.refreshScreenApi,
        params: { id: row.id }
      }
      this.commonUtil.doPost(obj).then(response => {
      })
    },
    routerTo(row) {
      this.$store.commit('setParameters', { key: 'screenTplId', value: row.id })
      this.$router.push({ name: 'screenContent' })
    },
    screenView(row) {
      const viewReport = this.$router.resolve({ name: 'screenView', query: { tplId: row.id,sendRequest:1 }})
      window.open(viewReport.href, '_blank')
    },
    copyTpl(row)
    {
      var obj = {
        url: this.apis.screenTpl.getDetailApi,
        params: { id: row.id }
      }
      var that = this;
      this.commonUtil.doGet(obj).then(response => {
        this.commonUtil.coperyProperties(that.pageData.copyModalData, response.responseData)// 数据赋值
        that.pageData.copyModalData.tplCode = "";
        that.pageData.copyModalData.tplName = "";
      })
      this.pageData.copyModalConfig.show = true; 
    },
    closeCopyModal(){
      this.pageData.copyModalConfig.show = false; 
    },
    doCopy(){
      this.$refs['copyModalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
            var obj = {
              params:this.pageData.copyModalData,
              removeEmpty:false,
            }
            obj.url = this.apis.screenTpl.doCopyScreenApi;
            this.commonUtil.doPost(obj) .then(response=>{
              if (response.code == "200")
              {
                this.closeCopyModal();
                this.searchtablelist();
              }
            });
        }else{
            return false;
        }
      });
    }
  }
}
