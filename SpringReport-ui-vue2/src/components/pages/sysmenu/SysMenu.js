/*
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-06-15 07:18:21
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-06-30 06:48:46
 */
export default {
  name: 'sysMenu',
  data() {
    return {
      pageData: {
        // 查询表单内容 start
        searchForm: [
          { type: 'Input', label: '菜单名称', prop: 'menuName' }
        ],
        // 查询表单内容 end
        // 查询条件 start
        queryData: {
          menuName: '', // 菜单名称
          menuUrl: '', // 菜单url
          menuIcon: '', // 菜单图标
          accessRule: '', // 访问规则 1登录后访问 2登陆并授权后访问
          updater: ''// 更新人
        },
        // 查询条件 end
        // 查询表单按钮start
        searchHandle: [
          { label: '清除条件', type: '', handle: () => this.resetSearch(), auth: 'sysMenu_search' },
          { label: '查询', type: 'primary', handle: () => this.searchtablelist(), auth: 'sysMenu_search' },
        ],
        // 查询表单按钮end
        // 表格数据start
        tableData: [],
        // 表格数据end
        // 表格工具栏按钮 start
        tableHandles: [
          { label: '新增', type: 'primary', position: 'right', iconClass: 'action-icon-add', handle: () => this.showModal(this.commonConstants.modalType.insert), auth: 'sysMenu_insert' },
          { label: '批量删除', type: 'danger', position: 'left', iconClass: 'action-icon-del', handle: () => this.deleteBatch(), auth: 'sysMenu_batchDelete' }
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
            { label: '查看', type: 'text', auth: 'sysMenu_getDetail', handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id) },
            { label: '编辑', type: 'text', auth: 'sysMenu_update', handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id) },
            { label: '菜单功能', type: 'text', auth: 'sysMenu_sysApi', handle: (row) => this.routerTo(row) },
            { label: '删除', type: 'text', auth: 'sysMenu_delete', handle: (row) => this.deleteOne(row.id) }
          ] },
          { label: '菜单名称', prop: 'menuName', align: 'left', overflow: true },
          { label: '菜单地址', prop: 'menuUrl', align: 'center', overflow: true },
          { label: '菜单图标', prop: 'menuIcon', align: 'center', overflow: true },
          // {label:'访问规则',prop:'accessRule',align:'center',codeType:'menuRule',formatter:this.commonUtil.getTableCodeName},
          { label: '是否隐藏', prop: 'isHidden', align: 'center', codeType: 'yesNo', formatter: this.commonUtil.getTableCodeName, overflow: true },
          { label: '排序', prop: 'sort', align: 'center', overflow: true },
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
          { type: 'Input', label: '菜单名称', prop: 'menuName', rules: { required: true, maxLength: 20 }},
          { type: 'Input', label: '菜单地址', prop: 'menuUrl', rules: { required: true, maxLength: 200 }},
          { type: 'Input', label: '菜单图标', prop: 'menuIcon', rules: { required: true, maxLength: 50 }},
          { type: 'Select', label: '是否隐藏 ', prop: 'isHidden', rules: { required: true }, options: this.selectUtil.yesNo },
          // {type:'Select',label:'上级菜单 ',prop:'parentMenuId',rules:{required:false},props:{label:'menuName',value:'id'}},
          { type: 'TreeSelect', label: '上级菜单', prop: 'parentMenuId', lazy: false, clearable: true, ref: 'treeSelect', data: [], valueProp: 'id', props: { parent: 'parentMenuId', value: 'id', label: 'menuName', children: 'children' }},
          { type: 'Input', label: '排序', prop: 'sort', rules: { required: true, type: 'positiveInt' }}
        ],
        // modal表单 end
        // modal 数据 start
        modalData: { // modal页面数据
          menuName: '', // 菜单名称
          menuUrl: '', // 菜单url
          menuIcon: '', // 菜单图标
          isHidden: '', // 是否隐藏
          parentMenuId: '', // 上级菜单id
          sort: ''// 排序
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
        url: this.apis.sysMenu.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage)
      }
      var that = this
      this.commonUtil.getTableList(obj).then(response => {
        that.pageData.tableData = response.responseData
        that.pageData.modalForm[4].data = response.responseData
        that.$nextTick(() => {
          that.$refs.custable.$refs.cesTable.doLayout()
        })
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
      } else {
        // this.getParentMenus('1');
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
        url: this.apis.sysMenu.getDetailApi,
        params: { id: id }
      }
      this.commonUtil.doGet(obj).then(response => {
        response.responseData.sort = response.responseData.sort + ''
        this.commonUtil.coperyProperties(this.pageData.modalData, response.responseData)// 数据赋值
        if (this.pageData.modalData.parentMenuId == '0') {
          this.pageData.modalData.parentMenuId = ''
        }
        // this.getParentMenus('2');
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
            obj.url = this.apis.sysMenu.insertApi
          } else {
            obj.url = this.apis.sysMenu.updateApi
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
        url: this.apis.sysMenu.deleteOneApi,
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
          url: this.apis.sysMenu.deleteBatchApi,
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
    routerTo(row) {
      this.$store.commit('setParameters', { key: 'menuId', value: row.id })
      this.$router.push({ name: 'sysApi' })
    }
    // getParentMenus(type){
    //   var obj = {
    //     url:this.apis.sysMenu.getParentMenusApi,
    //     params:{},
    //   }
    //   if(type != "1")
    //   {
    //     obj.params = {id:this.pageData.modalData.id}
    //   }
    //   this.commonUtil.getTableList(obj).then(response=>{
    //     this.pageData.modalForm[4].options = response.responseData;
    //     this.$refs['modalRef'].$forceUpdate();//在methods中需强制更新，mounted中不需要
    //   });
    // }

  }
}
