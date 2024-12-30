export default {
  name: 'sysDept',
  data() {
    return {
      pageData: {
        // 查询表单内容 start
        searchForm: [
          { type: 'Input', label: '部门名称', prop: 'deptName' },
          { type: 'Input', label: '负责人', prop: 'leader' },
          { type: 'Input', label: '联系电话', prop: 'phone' },
          { type: 'Input', label: '邮箱', prop: 'email' }
        ],
        // 查询表单内容 end
        // 查询条件 start
        queryData: {
          deptName: '', // 部门名称
          leader: '', // 负责人
          phone: '', // 联系电话
          email: ''// 邮箱
        },
        // 查询条件 end
        // 查询表单按钮start
        searchHandle: [
          { label: '清除条件', type: '', handle: () => this.resetSearch(), auth: 'sysDept_search' },
          { label: '查询', type: 'primary', handle: () => this.searchtablelist(), auth: 'sysDept_search' },
        ],
        // 查询表单按钮end
        // 表格数据start
        tableData: [],
        // 表格数据end
        // 表格工具栏按钮 start
        tableHandles: [
          { label: '新增', type: 'primary', position: 'right', iconClass: 'action-icon-add', handle: () => this.showModal(this.commonConstants.modalType.insert), auth: 'sysDept_insert' }
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
            { label: '查看', type: 'text', auth: 'sysDept_getDetail', handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id) },
            { label: '编辑', type: 'text', auth: 'sysDept_edit', handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id) },
            { label: '删除', type: 'text', auth: 'sysDept_delete', handle: (row) => this.deleteOne(row.id) }
          ] },
          { label: '部门名称', prop: 'deptName', align: 'center' },
          { label: '负责人', prop: 'leader', align: 'center' },
          { label: '联系电话', prop: 'phone', align: 'center' },
          { label: '邮箱', prop: 'email', align: 'center' },
          { label: '状态', prop: 'status', align: 'center', codeType: 'status', formatter: this.commonUtil.getTableCodeName },
          { label: '排序', prop: 'deptSort', align: 'center' },
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
          { type: 'TreeSelect', label: '上级部门', prop: 'parentId', lazy: false, clearable: true, ref: 'treeSelect', data: [], valueProp: 'id', props: { parent: 'parentId', value: 'id', label: 'deptName', children: 'children' }},
          { type: 'Input', label: '部门名称', prop: 'deptName', rules: { required: true, maxLength: 50 }},
          { type: 'Input', label: '负责人', prop: 'leader', rules: { required: true, maxLength: 50 }},
          { type: 'Input', label: '联系电话', prop: 'phone', rules: { required: true, maxLength: 20 }},
          { type: 'Input', label: '邮箱', prop: 'email', rules: { required: false, maxLength: 50, type: 'email' }},
          { type: 'Radio', label: '状态', prop: 'status', rules: { required: true }, radios: this.selectUtil.status },
          { type: 'Input', label: '排序', prop: 'deptSort', rules: { required: true, type: 'number' }}
        ],
        // modal表单 end
        // modal 数据 start
        modalData: { // modal页面数据
          id: null,
          parentId: null,
          deptName: '', // 部门名称
          leader: '', // 负责人
          phone: '', // 联系电话
          email: '', // 邮箱
          deptSort: '', // 排序
          status: null
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
    this.pageData.tableData = []
    this.searchtablelist()
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
        url: this.apis.sysDept.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage)
      }
      var that = this
      this.commonUtil.getTableList(obj).then(response => {
        that.pageData.tableData = response.responseData
        that.pageData.modalForm[0].data = response.responseData
        // this.commonUtil.tableAssignment(response,this.pageData.tablePage,this.pageData.tableData);
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
        url: this.apis.sysDept.getDetailApi,
        params: { id: id }
      }
      this.commonUtil.doGet(obj).then(response => {
        this.commonUtil.coperyProperties(this.pageData.modalData, response.responseData)// 数据赋值
        this.pageData.modalData.deptSort = this.pageData.modalData.deptSort + ''
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
            obj.url = this.apis.sysDept.insertApi
          } else {
            obj.url = this.apis.sysDept.updateApi
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
        url: this.apis.sysDept.deleteOneApi,
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
          url: this.apis.sysDept.deleteBatchApi,
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
    }
  }
}
