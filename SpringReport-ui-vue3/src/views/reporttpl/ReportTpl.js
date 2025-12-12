import Axios from 'axios'
export default {
  name: 'reportTpl',
  data() {
    return {
      loading:false,
      pageData: {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        treeData: [],
        //查询表单内容 start
        searchForm: [
          { type: 'Input', label: '报表标识', prop: 'tplCode' },
          { type: 'Input', label: '报表名称', prop: 'tplName' },
          // {type:'Select',label:'报表分类',prop:'reportType',props:{label:"reportTypeName",value:"id"}},
        ],
        //查询表单内容 end
        //查询条件 start
        queryData: {
          tplCode: '', //模板标识
          tplName: '', //模板名称
          reportType: '', //报表类型
        },
        //查询条件 end
        //查询表单按钮start
        lazy: false,
        searchHandle: [
          {
            label: '查询',
            type: 'primary',
            handle: () => this.searchtablelist(),
            auth: 'reportTpl_search',
          },
          {
            label: '清除条件',
            type: '',
            handle: () => this.resetSearch(),
            auth: 'reportTpl_search',
          },
        ],
        //查询表单按钮end
        //表格数据start
        tableData: [],
        //表格数据end
        //表格工具栏按钮 start
        tableHandles: [
          {
            label: '模板市场',
            type: 'primary',
            position: 'right',
            iconClass: 'action-icon-template',
            handle: () => this.goTemStore(),
            auth: 'template_market',
          },

          {
            label: '新建目录',
            type: 'primary',
            position: 'right',
            iconClass: 'action-icon-add',
            handle: () => this.showModal(this.commonConstants.modalType.insert, null, '1'),
            auth: 'reportTpl_folder',
          },
          {
            label: '新建文档',
            type: 'info',
            position: 'right',
            iconClass: 'action-icon-add',
            handle: () => this.showModal(this.commonConstants.modalType.insert, null, '2'),
            auth: 'reportTpl_insert',
          },
          {
            label: '刷新',
            type: 'danger',
            position: 'left',
            iconClass: 'action-icon-refresh',
            handle: () => this.searchtablelist(),
            auth: 'reportTpl_search',
          },
          { label: '导出模板', type: 'warning', position: 'right', iconClass: 'action-icon-download', handle: () => this.exportTemplate(), auth: 'exceltemplate_export' },
          { label: '导入模板', type: 'danger', position: 'right', iconClass: 'action-icon-upload', handle: () => this.importTemplate(), auth: 'exceltemplate_import' },
        ],
        //表格工具栏按钮 end
        selectList: [], //表格选中的数据
        //表格分页信息start
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50],
        },
        //表格分页信息end
        //表格列表头start
        tableCols: [
          {
            label: '操作',
            prop: 'operation',
            align: 'center',
            type: 'dropdown',
            width: 54,
            btnList: [
              {
                label: '查看',
                type: 'primary',
                auth: 'reportTpl_getDetail',
                handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id),
                show: (row) => this.isShowBtn(row),
              },
              {
                label: '编辑',
                type: 'primary',
                auth: 'reportTpl_update',
                handle: (row) =>
                  this.showModal(this.commonConstants.modalType.update, row.id, row.type),
              },
              {
                label: '删除',
                type: 'danger',
                auth: 'reportTpl_delete',
                handle: (row) => this.deleteOne(row.id, row.type),
              },
              {
                label: '复制',
                type: 'primary',
                auth: 'reportTpl_copy',
                handle: (row) => this.copyReport(row),
                show: (row) => this.isShowBtn(row),
              },
              {
                label: '报表设计',
                type: 'primary',
                auth: 'reportTpl_reportDesign',
                handle: (row) => this.routerTo('luckyReportDesign', row),
                show: (row) => this.isShowBtn(row),
              },
              {
                label: '修改密码',
                type: 'primary',
                auth: 'reportTpl_changePwd',
                show: (row) => this.isShowChangePwd(row),
                handle: (row) => this.showChangePwd(row),
              },
              {
                label: '报表查看(pc)',
                type: 'primary',
                auth: 'reportTpl_reportView',
                handle: (row) => this.routerTo('luckyReportPreview', row),
                show: (row) => this.isShowBtn(row),
              },
              {
                label: '报表查看(手机)',
                type: 'primary',
                auth: 'reportTpl_reportView',
                show: (row) => this.isShowShare(row),
                handle: (row) => this.routerTo('h5ReportPreview', row),
              },
              {
                label: '报表分享',
                type: 'primary',
                auth: 'reportTpl_reportShare',
                handle: (row) => this.showShareReport(row),
                show: (row) => this.isShowBtn(row),
              },
              {
                label: '定时任务',
                type: 'primary',
                auth: 'reportTpl_Task',
                show: (row) => this.isShowShare(row),
                handle: (row) => this.routerToTask(row),
              },
            ],
          },
          { label: '报表标识', prop: 'tplCode', align: 'left', overflow: true, icon: true },
          { label: '报表名称', prop: 'tplName', align: 'center', overflow: true },
          {
            label: '查看权限',
            prop: 'viewAuth',
            align: 'center',
            codeType: 'viewAuth',
            formatter: this.commonUtil.getTableCodeName,
            overflow: true,
          },
          { label: '数据源代码', prop: 'dataSourceCode', align: 'center', overflow: true },
          { label: '数据源名称', prop: 'dataSourceName', align: 'center', overflow: true },
          {
            label: '导出是否加密',
            prop: 'exportEncrypt',
            align: 'center',
            codeType: 'yesNo',
            formatter: this.commonUtil.getTableCodeName,
            overflow: true,
          },
          {
            label: '报表类型',
            prop: 'tplType',
            align: 'center',
            codeType: 'tplType',
            formatter: this.commonUtil.getTableCodeName,
            overflow: true,
          },
        ],
        //表格列表头end
        //modal配置 start
        modalConfig: {
          title: '新增文档', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '850px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        modalForm: [
          {
            type: 'Input',
            label: '报表标识',
            prop: 'tplCode',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Input',
            label: '报表名称',
            prop: 'tplName',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Select',
            label: '所属目录',
            prop: 'reportType',
            rules: { required: true },
            props: { label: 'reportTypeName', value: 'id' },
          },
          {
            type: 'Select',
            label: '报表数据源',
            prop: 'dataSource',
            rules: { required: true },
            multiple: true,
            filterable:true,
            clearable:true,
            props: { label: 'name', value: 'id' },
          },
          {
            type: 'Select',
            label: '查看权限',
            prop: 'viewAuth',
            rules: { required: true },
            options: this.selectUtil.viewAuth,
            change: this.changeViewAuth,
          },
          // {type:'Select',label:'角色配置',prop:'roles',rules:{required:false},multiple:true,width:'520px',props:{label:"roleName",value:"id"}},
          {
            type: 'Password',
            label: '设计密码',
            prop: 'designPwd',
            rules: { required: false, maxLength: 32 },
          },
          {
            type: 'Select',
            label: '导出是否加密',
            prop: 'exportEncrypt',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '报表类型',
            prop: 'tplType',
            rules: { required: true },
            multiple: false,
            options: this.selectUtil.tplType,
            change: this.changeTplType,
          },
          {
            type: 'Select',
            label: '是否并发控制',
            prop: 'concurrencyFlag',
            rules: { required: true },
            multiple: false,
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '提交后是否刷新页面',
            prop: 'refreshPage',
            rules: { required: true },
            multiple: false,
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '工具栏预览是否展示',
            prop: 'showToolbar',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '行标题预览是否展示',
            prop: 'showRowHeader',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '列标题预览是否展示',
            prop: 'showColHeader',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '网格线预览是否展示',
            prop: 'showGridlines',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '是否开启协同',
            prop: 'coeditFlag',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          { type: 'Select', label: '是否开启定时刷新', prop: 'isRefresh', rules: { required: true }, options: this.selectUtil.yesNo, change: this.changeIsRefresh },
          { type: 'Input', label: '刷新间隔时长(秒)', prop: 'refreshTime', rules: { required: true,type:"positiveInt"}, },
          { type: 'Select', label: '查询组件位置', prop: 'searchFormType', rules: { required: true }, options: this.selectUtil.searchFormType },
        ],
        //modal表单 end
        //modal 数据 start
        modalData: {
          //modal页面数据
          tplCode: '', //模板标识
          tplName: '', //模板名称
          reportType: '', //报表类型
          dataSource: [], //报表数据源
          viewAuth: '', //查看权限 1所有人可见 2指定角色
          designPwd: '', //设计密码
          exportEncrypt: '', //导出时是否加密
          refreshPage: '',
          tplType: '', //报表类型 1展示报表 2填报报表
          roles: [], //角色
          concurrencyFlag: 1,
          showToolbar: 2,
          showRowHeader: 1,
          showColHeader: 1,
          showGridlines: 1,
          coeditFlag: 1,
          isRefresh:2,
          refreshTime:'0',
          searchFormType:1,
        },
        //modal 数据 end
        //modal 按钮 start
        modalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeModal() },
          { label: '提交', type: 'primary', handle: () => this.save() },
        ],
        //modal 按钮 end
        changePwdConfig: {
          title: '修改密码', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '700px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        changePwdForm: [
          {
            type: 'Password',
            label: '旧密码',
            prop: 'oldPwd',
            rules: { required: true, maxLength: 32 },
          },
          {
            type: 'Password',
            label: '新密码',
            prop: 'designPwd',
            rules: { required: false, maxLength: 32 },
          },
        ],
        //modal表单 end
        //modal 数据 start
        changePwdModalData: {
          //modal页面数据
          id: null,
          oldPwd: '', //模板标识
          designPwd: '', //模板名称
        },
        //modal 数据 end
        //modal 按钮 start
        changePwdModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closePwdModal() },
          { label: '提交', type: 'primary', handle: () => this.changePwd() },
        ],
        //modal配置 start
        copyModalConfig: {
          title: '复制模板', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '750px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        copyModalForm: [
          {
            type: 'Input',
            label: '报表标识',
            prop: 'tplCode',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Input',
            label: '报表名称',
            prop: 'tplName',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Select',
            label: '报表类型',
            prop: 'reportType',
            rules: { required: true },
            props: { label: 'reportTypeName', value: 'id' },
          },
          {
            type: 'Select',
            label: '报表数据源',
            prop: 'dataSource',
            disabled: this.commonUtil.disabled,
            rules: { required: true },
            multiple: true,
            props: { label: 'code', value: 'id' },
          },
          {
            type: 'Select',
            label: '查看权限',
            prop: 'viewAuth',
            rules: { required: true },
            options: this.selectUtil.viewAuth,
            change: this.changeViewAuth,
          },
          // {type:'Select',label:'角色配置',prop:'roles',rules:{required:false},multiple:true,width:'520px',props:{label:"roleName",value:"id"}},
          {
            type: 'Password',
            label: '设计密码',
            prop: 'designPwd',
            rules: { required: false, maxLength: 32 },
          },
          {
            type: 'Select',
            label: '导出是否加密',
            prop: 'exportEncrypt',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
          {
            type: 'Select',
            label: '报表类型',
            prop: 'tplType',
            rules: { required: true },
            multiple: false,
            options: this.selectUtil.tplType,
          },
        ],
        //modal表单 end
        //modal 数据 start
        copyModalData: {
          //modal页面数据
          tplCode: '', //模板标识
          tplName: '', //模板名称
          reportType: '', //报表类型
          dataSource: [], //报表数据源
          viewAuth: '', //查看权限 1所有人可见 2指定角色
          designPwd: '', //设计密码
          exportEncrypt: '', //导出时是否加密
          tplType: '', //报表类型 1展示报表 2填报报表
          roles: [], //角色
        },
        //modal 数据 end
        //modal 按钮 start
        copyModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeCopyModal() },
          { label: '提交', type: 'primary', handle: () => this.doCopy() },
        ],

        shareReportConfig: {
          title: '报表分享', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '850px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        shareReportForm: [
          { type: 'Select', label: '分享时长设置', prop: 'isShareForever', rules: { required: true }, options: this.selectUtil.shareTime,change: this.changeShareTimeType},
          {
            type: 'Input',
            label: '有效时长(分)',
            prop: 'shareTime',
            rules: { required: true, type: 'integer'},
          },
          {
            type: 'Select',
            label: '分享类型',
            prop: 'shareType',
            rules: { required: true },
            options: this.selectUtil.shareType,
          },
          {
            type: 'Select',
            label: '允许上报数据',
            prop: 'allowReport',
            rules: { required: true },
            options: this.selectUtil.yesNo,
          },
        ],
        shareReportModalData: {
          //modal页面数据
          tplId: null,
          isShareForever:null,
          shareTime: '', //有效时长(分)
          shareType: '', //分享类型
          tplType: '1', //报表类型
          allowReport: 2,
        },
        shareReportModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeShareReportModal() },
          { label: '获取分享链接', type: 'primary', handle: () => this.getShareUrl() },
        ],
        folderModalConfig: {
          title: '新增目录', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '700px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        folderModalForm: [
          {
            type: 'Input',
            label: '目录名称',
            prop: 'reportTypeName',
            rules: { required: true, maxLength: 50 },
          },
        ],
        //modal表单 end
        //modal 数据 start
        folderModalData: {
          //modal页面数据
          reportTypeName: '', //类型名称
        },
        //modal 数据 end
        //modal 按钮 start
        folderModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeFolderModal() },
          { label: '提交', type: 'primary', handle: () => this.saveFolder() },
        ],
      },
    };
  },
  mounted() {
    this.pageData.tableData = [];
    this.searchtablelist();
    this.getReportType();
    this.getReportDatasource();
    this.getReportTypeTree();
    // this.getRoles();
  },
  methods: {
    goTemStore() {
      window.open(
        this.$router.resolve({
          name: 'templateStore',
          query: {
            temType: 'excel',
          },
        }).href,
        '_blank'
      );
    },
    /**
     * @description: 获取表格数据
     * @param {type}
     * @return:
     * @author: caiyang
     */
    searchtablelist() {
      var obj = {
        url: this.apis.reportTpl.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage),
      };
      var that = this;
      that.pageData.tableData = [];
      this.commonUtil.getTableList(obj).then((response) => {
        this.commonUtil.tableAssignment(response, this.pageData.tablePage, this.pageData.tableData);
      });
    },
    resetSearch() {
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
    showModal(type, id, docType) {
      // this.commonUtil.showModal(this.pageData.modalConfig,type);
      // if(type != this.commonConstants.modalType.insert)
      // {
      //   this.getDetail(id,docType);
      // }else{
      //   this.pageData.modalForm[5].disabled = this.commonUtil.undisabled;
      //   // this.showRoleSelect();
      // }
      this.commonUtil.showModal(
        docType == '1' ? this.pageData.folderModalConfig : this.pageData.modalConfig,
        type
      );
      if (type == this.commonConstants.modalType.insert) {
        docType == '1'
          ? (this.pageData.folderModalConfig.title = '新增目录')
          : (this.pageData.modalConfig.title = '新增文档');
      } else {
        docType == '1'
          ? (this.pageData.folderModalConfig.title = '编辑目录')
          : (this.pageData.modalConfig.title = '编辑文档');
      }
      if (type != this.commonConstants.modalType.insert) {
        this.getDetail(id, docType);
      } else {
        this.pageData.modalForm[5].disabled = this.commonUtil.undisabled;
        // this.showRoleSelect();
        this.changeTplType();
        this.changeIsRefresh();
      }
    },
    /**
     * @description: 获取详细数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    getDetail(id, docType) {
      var obj = {
        url: docType == '1' ? this.apis.reportType.getDetailApi : this.apis.reportTpl.getDetailApi,
        params: { id: id },
      };
      this.commonUtil.doGet(obj).then((response) => {
        this.commonUtil.coperyProperties(
          docType == '1' ? this.pageData.folderModalData : this.pageData.modalData,
          response.responseData
        ); //数据赋值
        if (this.pageData.modalData.designPwd) {
          this.pageData.modalForm[5].disabled = this.commonUtil.disabled;
        } else {
          this.pageData.modalForm[5].disabled = this.commonUtil.undisabled;
        }
        this.pageData.modalData.refreshTime = this.pageData.modalData.refreshTime + "";
        // this.showRoleSelect();
        this.changeTplType();
        this.changeIsRefresh();
      });
    },
    /**
     * @description: 关闭modal
     * @param
     * @return:
     * @author: caiyang
     */
    closeModal() {
      this.$refs['modalRef'].$refs['modalFormRef'].resetFields(); //校验重置
      this.pageData.modalConfig.show = false; //关闭modal
      this.commonUtil.clearObj(this.pageData.modalData); //清空modalData
    },
    closeFolderModal() {
      this.$refs['folderModalRef'].$refs['modalFormRef'].resetFields(); //校验重置
      this.pageData.folderModalConfig.show = false; //关闭modal
      this.commonUtil.clearObj(this.pageData.folderModalData); //清空modalData
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
            removeEmpty: false,
          };
          if (this.pageData.modalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.reportTpl.insertApi;
          } else {
            obj.url = this.apis.reportTpl.updateApi;
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              this.closeModal();
              this.searchtablelist();
            }
          });
        } else {
          return false;
        }
      });
    },
    saveFolder() {
      this.$refs['folderModalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          this.pageData.folderModalData.type = 1;
          var obj = {
            params: this.pageData.folderModalData,
            removeEmpty: false,
          };
          if (this.pageData.folderModalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.reportType.insertApi;
          } else {
            obj.url = this.apis.reportType.updateApi;
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              this.closeFolderModal();
              this.searchtablelist();
              this.getReportType();
              this.getReportTypeTree();
            }
          });
        } else {
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
    deleteOne(id, type) {
      let obj = {
        url: this.apis.reportTpl.deleteOneApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.searchtablelist,
        params: { id: id },
        type: 'get',
      };
      if (type == 1) {
        obj.url = this.apis.reportType.deleteOneApi;
        //删除前确认是否有文件，如果有文件则不允许删除
        var checkObj = {
          params: { reportType: id },
          url: this.apis.reportTpl.getChildrenApi,
        };
        this.commonUtil.doPost(checkObj).then((response) => {
          if (response.code == '200') {
            if (response.responseData && response.responseData.length > 0) {
              this.commonUtil.showMessage({
                message: '该目录下有文档，不允许删除！',
                type: this.commonConstants.messageType.error,
              });
            } else {
              //弹出删除确认框
              this.commonUtil.showConfirm(obj);
            }
          }
        });
      } else {
        //弹出删除确认框
        this.commonUtil.showConfirm(obj);
      }
    },
    /**
     * @description: 批量删除
     * @param {type}
     * @return:
     * @author: caiyang
     */
    deleteBatch() {
      const length = this.pageData.selectList.length;
      if (length == 0) {
        this.commonUtil.showMessage({
          message: this.commonUtil.getMessageFromList('error.batchdelete.empty', null),
          type: this.commonConstants.messageType.error,
        });
      } else {
        let ids = new Array();
        for (let i = 0; i < length; i++) {
          ids.push(this.pageData.selectList[i].id);
        }
        let obj = {
          url: this.apis.reportTpl.deleteBatchApi,
          messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
          callback: this.searchtablelist,
          params: ids,
          type: 'post',
        };
        this.commonUtil.showConfirm(obj);
      }
    },
    selectChange(rows) {
      this.pageData.selectList = rows;
    },
    //获取报表类型
    getReportType() {
      var obj = {
        params: { type: 1 },
        url: this.apis.reportType.getReportTypeApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == '200') {
          this.pageData.modalForm[2].options = response.responseData;
          this.pageData.copyModalForm[2].options = response.responseData;
        }
      });
    },
    //获取数据源
    getReportDatasource() {
      var obj = {
        params: {},
        url: this.apis.reportDatasource.getReportDatasourceApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == '200') {
          this.pageData.modalForm[3].options = response.responseData;
          this.pageData.copyModalForm[3].options = response.responseData;
        }
      });
    },
    //页面跳转
    routerTo(name, row) {
      let viewReport = this.$router.resolve({
        name: name,
        query: {
          tplId: row.id,
          thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType),
        },
      });
      window.open(viewReport.href, '_blank');
    },
    //是否显示修改密码按钮
    isShowChangePwd(row) {
      if (row.designPwd) {
        return true;
      } else {
        return false;
      }
    },
    showChangePwd(row) {
      this.pageData.changePwdConfig.show = true;
      this.pageData.changePwdModalData.id = row.id;
    },
    changePwd() {
      this.$refs['changePwd'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            params: this.pageData.changePwdModalData,
            removeEmpty: false,
          };
          obj.url = this.apis.reportTpl.changeDesignPwdApi;
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              this.closePwdModal();
              this.searchtablelist();
            }
          });
        } else {
          return false;
        }
      });
    },
    closePwdModal() {
      this.$refs['changePwd'].$refs['modalFormRef'].resetFields(); //校验重置
      this.pageData.changePwdConfig.show = false; //关闭modal
      this.commonUtil.clearObj(this.pageData.changePwdModalData); //清空modalData
      this.pageData.changePwdModalData.designPwd = '';
    },
    changeViewAuth() {
      this.showRoleSelect();
    },
    showRoleSelect() {
      if (this.pageData.modalData.viewAuth == '2') {
        // this.pageData.modalForm[5].show = true
      } else {
        // this.pageData.modalForm[5].show = false
      }
    },
    getRoles() {
      var obj = {
        params: {},
        url: this.apis.sysUser.getRolesApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == '200') {
          this.pageData.modalForm[5].options = response.responseData;
        }
      });
    },
    //复制报表对话框打开
    copyReport(row) {
      let obj = {
        url: this.apis.reportTpl.doCopyReportApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.copy', null),
        callback: this.searchtablelist,
        params: { id: row.id },
        type: 'post',
      };
      this.commonUtil.showConfirm(obj);
    },
    closeCopyModal() {
      this.$refs['copyModalRef'].$refs['modalFormRef'].resetFields(); //校验重置
      this.pageData.copyModalConfig.show = false; //关闭modal
      this.commonUtil.clearObj(this.pageData.copyModalData); //清空modalData
    },
    //确认复制报表
    doCopy() {
      this.$refs['copyModalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            params: this.pageData.copyModalData,
            removeEmpty: false,
          };
          obj.url = this.apis.reportTpl.doCopyReportApi;
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              this.closeCopyModal();
              this.searchtablelist();
            }
          });
        } else {
          return false;
        }
      });
    },
    getReportTypeTree() {
      var obj = {
        params: { type: '1' },
        removeEmpty: false,
        url: this.apis.reportType.getReportTypeTreeApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == '200') {
          this.pageData.treeData = response.responseData;
        }
      });
    },
    handleNodeClick(data) {
      if (data.id == '1') {
        this.pageData.queryData.reportType = '';
      } else {
        this.pageData.queryData.reportType = data.id;
      }
      this.searchtablelist();
    },
    changeTplType() {
      if (this.pageData.modalData.tplType == 1) {
        this.pageData.modalForm[8].show = false;
        this.pageData.modalForm[8].rules.required = false;
        this.pageData.modalForm[9].show = false;
        this.pageData.modalForm[9].rules.required = false;
        // this.pageData.modalForm[9].show = true;
        // this.pageData.modalForm[9].rules.required = true;
      } else {
        this.pageData.modalForm[8].show = true;
        this.pageData.modalForm[8].rules.required = true;
        this.pageData.modalForm[9].show = true;
        this.pageData.modalForm[9].rules.required = true;
        // this.pageData.modalForm[9].show = false;
        // this.pageData.modalForm[9].rules.required = false;
      }
    },
    showShareReport(row) {
      this.pageData.shareReportConfig.show = true;
      this.pageData.shareReportModalData.tplId = row.id;
      this.pageData.shareReportModalData.tplType = row.tplType;
      if (row.tplType == 1) {
        this.pageData.shareReportForm[3].show = false;
        this.pageData.shareReportForm[3].rules.required = false;
      } else {
        this.pageData.shareReportForm[3].show = true;
        this.pageData.shareReportForm[3].rules.required = true;
      }
    },
    closeShareReportModal() {
      this.pageData.shareReportConfig.show = false;
      this.commonUtil.clearObj(this.pageData.shareReportModalData); //清空modalData
      this.$refs['shareReport'].$refs['modalFormRef'].resetFields(); //校验重置
    },
    getShareUrl() {
      this.$refs['shareReport'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          if (
            this.pageData.shareReportModalData.shareType == 2 &&
            this.pageData.shareReportModalData.tplType == 2
          ) {
            this.commonUtil.showMessage({
              message: '填报报表暂时不支持h5分享。',
              type: this.commonConstants.messageType.error,
            });
            return;
          }
          var extraParam = {};
          if (localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)) {
            extraParam.thirdPartyType = localStorage.getItem(
              this.commonConstants.sessionItem.thirdPartyType
            );
          }
          var obj = {
            params: Object.assign({}, this.pageData.shareReportModalData, extraParam),
            removeEmpty: false,
          };
          obj.url = this.apis.reportTpl.getShareUrlApi;
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              const input = document.getElementById('clipboradInput'); // 承载复制内容
              input.value = response.responseData.shareMsg; // 修改文本框的内容
              input.select(); // 选中文本
              document.execCommand('copy'); // 执行浏览器复制命令
              this.commonUtil.showMessage({
                message: '分享链接已经添加到剪贴板。',
                type: this.commonConstants.messageType.success,
              });
              this.closeShareReportModal();
            }
          });
        } else {
          return false;
        }
      });
    },
    isShowShare(row) {
      // if (row.tplType == 1) {
      //   return true;
      // } else {
      //   return false;
      // }
      return true;
    },
    routerToTask(row) {
      this.$router.push({
        name: 'reportTask',
        query: {
          taskTplId: row.id,
          thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType),
        },
      });
    },
    loadData(tree, treeNode, resolve) {
      var obj = {
        params: { reportType: tree.id },
        url: this.apis.reportTpl.getChildrenApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == '200') {
          resolve(response.responseData);
        }
      });
    },
    isShowBtn(row) {
      if (row.type == '1') {
        return false;
      } else {
        return true;
      }
    },
    removeNode(node, data) {
      const obj = {
        url: this.apis.reportType.deleteOneApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.removeNodeCallBack,
        params: { id: data.id },
        type: 'get',
      };
      var checkObj = {
        params: { reportType: data.id },
        url: this.apis.reportTpl.getChildrenApi,
      };
      this.commonUtil.doPost(checkObj).then((response) => {
        if (response.code == '200') {
          if (response.responseData && response.responseData.length > 0) {
            this.commonUtil.showMessage({
              message: '该目录下有文档，不允许删除！',
              type: this.commonConstants.messageType.error,
            });
          } else {
            // 弹出删除确认框
            this.commonUtil.showConfirm(obj);
          }
        }
      });
    },
    removeNodeCallBack() {
      this.searchtablelist();
      this.getReportType();
      this.getReportTypeTree();
    },
    changeIsRefresh(){
      if (this.pageData.modalData.isRefresh != 1) {
        this.pageData.modalForm[16].show = false
        this.pageData.modalForm[16].rules.required = false
        // this.pageData.modalForm[9].show = true;
        // this.pageData.modalForm[9].rules.required = true;
      } else {
        this.pageData.modalForm[16].show = true
        this.pageData.modalForm[16].rules.required = true
        // this.pageData.modalForm[9].show = false;
        // this.pageData.modalForm[9].rules.required = false;
      }
    },
    changeShareTimeType(){
      if(this.pageData.shareReportModalData.isShareForever == 1){
        this.pageData.shareReportForm[1].show = false
        this.pageData.shareReportForm[1].rules.required = false
        if(this.pageData.shareReportModalData.tplType == 2){
          this.pageData.shareReportForm[3].show = false
          this.pageData.shareReportForm[3].rules.required = false
        }
      }else{
        this.pageData.shareReportForm[1].show = true
        this.pageData.shareReportForm[1].rules.required = true
        if(this.pageData.shareReportModalData.tplType == 2){
          this.pageData.shareReportForm[3].show = true
          this.pageData.shareReportForm[3].rules.required = true
        }
      }
    },
    exportTemplate(){
      const length = this.pageData.selectList.length
      if (length == 0) {
        this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList('error.batchexport.empty', null), type: this.commonConstants.messageType.error })
      } else {
        const ids = new Array()
        for (let i = 0; i < length; i++) {
          ids.push(this.pageData.selectList[i].id)
        }
        const obj = {
          url: this.apis.templateExportImport.templateExportImportApi,
          messageContent: this.commonUtil.getMessageFromList('confirm.export', null),
          callback: null,
          params: {ids:ids},
          type: 'file'
        }
        this.commonUtil.showConfirm(obj)
      }
    },
    exportImportCallback(response){
    },
    importTemplate(){
      $('#uploadPic').click()
    },
    uploadPic(evt) {
      const files = evt.target.files
      if (files == null || files.length == 0) {
        alert('请选择文件')
        return
      }
      this.loading = true;
      const formData = new FormData()
      formData.append('file', files[0])
      const config = {
        headers: { 'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem(this.commonConstants.sessionItem.authorization),
        }
      }
      var that = this
      Axios.post(this.apis.templateExportImport.templateExcelUploadApi , formData, config)
        .then(res => {
          if(res.status == 200)
          {//请求成功
             var result = res.data;//请求返回结果
            if(result.newToken)
            {
                localStorage.setItem(commonConstants.sessionItem.authorization, result.newToken);
            }
            if(result.message)
            {
                that.commonUtil.showMessage({message:result.message,type: result.msgLevel})
            }
            if(result.code == "50004")//50004说明token超时，需重新登录
            {
                localStorage.removeItem(commonConstants.sessionItem.authorization);
                router.replace('/login');
                that.commonUtil.showMessage({message:result.message,type: result.msgLevel})
            }
          }
          evt.target.value = '';
          that.searchtablelist()
          that.getReportType()
          that.getReportDatasource()
          that.getReportTypeTree()
          that.loading = false;
        }).catch(error => {
          that.loading = false;
          evt.target.value = '';
        })
        .finally(() => {
        });
    },
  },
};
