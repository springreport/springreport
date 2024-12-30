export default {
  name: 'sysMerchant',
  data() {
    return {
      pageData: {
        // 查询表单内容 start
        searchForm: [
          { type: 'Input', label: '租户编号', prop: 'merchantNo' },
          { type: 'Input', label: '租户名称', prop: 'merchantName' },
          { type: 'Input', label: '电话', prop: 'phone' },
          { type: 'Input', label: '邮箱', prop: 'email' }
        ],
        // 查询表单内容 end
        // 查询条件 start
        queryData: {
          merchantNo: '',
          merchantName: '', // 租户名称
          phone: '', // 电话
          email: ''// 邮箱
        },
        // 查询条件 end
        // 查询表单按钮start
        searchHandle: [
          { label: '清除条件', type: '', handle: () => this.resetSearch(), auth: 'sysMerchant_search' },
          { label: '查询', type: 'primary', handle: () => this.searchtablelist(), auth: 'sysMerchant_search' },
        ],
        // 查询表单按钮end
        // 表格数据start
        tableData: [],
        // 表格数据end
        // 表格工具栏按钮 start
        tableHandles: [
          { label: '新增', type: 'primary', position: 'right', iconClass: 'action-icon-add', handle: () => this.showModal(this.commonConstants.modalType.insert), auth: 'sysMerchant_insert' },
          { label: '批量删除', type: 'danger', position: 'left', iconClass: 'action-icon-del', handle: () => this.deleteBatch(), auth: 'sysMerchant_delete' }
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
          { label: '操作', prop: 'operation', align: 'center', type: 'dropdown', width: 54, btnList: [
            { label: '查看', type: 'text', auth: 'sysMerchant_getDetail', handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id) },
            { label: '编辑', type: 'text', auth: 'sysMerchant_edit', handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id) },
            { label: '删除', type: 'text', auth: 'sysMerchant_delete', handle: (row) => this.deleteOne(row.id) }
          ] },
          { label: '租户编号', prop: 'merchantNo', align: 'center' },
          { label: '租户名称', prop: 'merchantName', align: 'center' },
          { label: '电话', prop: 'phone', align: 'center' },
          { label: '邮箱', prop: 'email', align: 'center' },
          { label: '状态', prop: 'status', align: 'center', codeType: 'status', formatter: this.commonUtil.getTableCodeName },
          { label: '权限模板', prop: 'templateName', align: 'center' },
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
          { type: 'Input', label: '租户名称', prop: 'merchantName', rules: { required: true, maxLength: 50 }},
          { type: 'Input', label: '电话', prop: 'phone', rules: { required: true, maxLength: 0 }},
          { type: 'Input', label: '邮箱', prop: 'email', rules: { required: true, maxLength: 50, type: 'email' }},
          { type: 'Radio', label: '状态', prop: 'status', rules: { required: true }, radios: this.selectUtil.status },
          { type: 'Input', label: '登录名', prop: 'userName', rules: { required: true, maxLength: 40 }},
          { type: 'Password', label: '密码', prop: 'password', rules: { required: true }},
          { type: 'Select', label: '权限模板', prop: 'authTemplate', rules: { required: true }, props: { label: 'templateName', value: 'id' }}
        ],
        // modal表单 end
        // modal 数据 start
        modalData: { // modal页面数据
          merchantName: '', // 租户名称
          phone: '', // 电话
          email: '', // 邮箱
          authTemplate: null, // 权限模板
          status: null,
          userName: '',
          password: ''
        },
        // modal 数据 end
        // modal 按钮 start
        modalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeModal() },
          { label: '提交', type: 'primary', handle: () => this.save() }
        ]
        // modal 按钮 end
      }
    }
  },
  activated() {
    this.searchtablelist()
    this.getAuthTemplates()
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
        url: this.apis.sysMerchant.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage)
      }
      this.commonUtil.getTableList(obj).then(response => {
        this.commonUtil.tableAssignment(response, this.pageData.tablePage, this.pageData.tableData)
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
        this.pageData.modalForm[4].show = false
        this.pageData.modalForm[4].rules.required = false
        this.pageData.modalForm[5].show = false
        this.pageData.modalForm[5].rules.required = false
        this.getDetail(id)
      } else {
        this.pageData.modalForm[4].show = true
        this.pageData.modalForm[4].rules.required = true
        this.pageData.modalForm[5].show = true
        this.pageData.modalForm[5].rules.required = true
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
        url: this.apis.sysMerchant.getDetailApi,
        params: { id: id }
      }
      this.commonUtil.doGet(obj).then(response => {
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
          this.pageData.modalData.password = this.$md5(this.pageData.modalData.password)
          var obj = {
            params: this.pageData.modalData,
            removeEmpty: false
          }
          if (this.pageData.modalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.sysMerchant.insertApi
          } else {
            obj.url = this.apis.sysMerchant.updateApi
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
        url: this.apis.sysMerchant.deleteOneApi,
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
          url: this.apis.sysMerchant.deleteBatchApi,
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
    // 获取权限模板
    getAuthTemplates() {
      var obj = {
        params: {},
        removeEmpty: false,
        url: this.apis.sysMerchantAuthTemplate.getAuthTemplatesApi
      }
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          this.pageData.modalForm[6].options = response.responseData
        }
      })
    }
  }
}
