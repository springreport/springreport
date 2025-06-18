export default {
  name: 'reportDatasource',
  data() {
    return {
      tableLoading: true,
      pageData: {
        //查询表单内容 start
        searchForm: [
          { type: 'Input', label: '编码', prop: 'code' },
          { type: 'Input', label: '数据源名称', prop: 'name' },
          {
            type: 'Select',
            label: '数据源类型',
            prop: 'type',
            options: this.selectUtil.datasourceType,
          },
        ],
        //查询表单内容 end
        //查询条件 start
        queryData: {
          code: '', //编码
          name: '', //数据源名称
          type: '', //数据源烈性
        },
        //查询条件 end
        //查询表单按钮start
        searchHandle: [
          {
            label: '清除条件',
            type: '',
            handle: () => this.resetSearch(),
            auth: 'reportDatasource_search',
          },
          {
            label: '查询',
            type: 'primary',
            handle: () => this.searchtablelist(),
            auth: 'reportDatasource_search',
          },
        ],
        //查询表单按钮end
        //表格数据start
        tableData: [],
        //表格数据end
        //表格工具栏按钮 start
        tableHandles: [
          {
            label: '新增',
            position: 'right',
            iconClass: 'action-icon-add',
            type: 'primary',
            handle: () => this.showModal(this.commonConstants.modalType.insert),
            auth: 'reportDatasource_insert',
          },
          {
            label: '批量删除',
            position: 'left',
            iconClass: 'action-icon-del',
            type: 'danger',
            handle: () => this.deleteBatch(),
            auth: 'reportDatasource_batchDelete',
          },
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
            type: '',
            type: 'dropdown',
            width: 54,
            btnList: [
              {
                label: '查看',
                type: 'primary',
                auth: 'reportDatasource_getDetail',
                handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id),
              },
              {
                label: '编辑',
                type: 'primary',
                auth: 'reportDatasource_update',
                handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id),
              },
              {
                label: '删除',
                type: 'danger',
                auth: 'reportDatasource_delete',
                handle: (row) => this.deleteOne(row.id),
              },
              {
                label: '数据字典',
                type: 'primary',
                auth: 'reportDatasource_dict',
                handle: (row) => this.routerTo(row),
              },
            ],
          },
          { label: '编码', prop: 'code', align: 'center', overflow: true },
          { label: '数据源名称', prop: 'name', align: 'center', overflow: true },
          {
            label: '数据源类型',
            prop: 'type',
            align: 'center',
            formatter: this.commonUtil.getTableCodeName,
            codeType: 'dataSourceType',
            overflow: true,
          },
          { label: '数据源链接', prop: 'jdbcUrl', align: 'center', overflow: true },
        ],
        //表格列表头end
        //modal配置 start
        modalConfig: {
          title: '新增', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '800px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        modalForm: [
          { type: 'Input', label: '编码', prop: 'code', rules: { required: true, maxLength: 50 } },
          {
            type: 'Input',
            label: '数据源名称',
            prop: 'name',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Select',
            label: '数据源类型',
            prop: 'type',
            rules: { required: true },
            options: this.selectUtil.datasourceType,
            change: this.changeDatasourceType,
          },
          {
            type: 'Input',
            label: '数据源链接',
            prop: 'jdbcUrl',
            rules: { required: true, maxLength: 500 },
          },
          {
            type: 'Input',
            label: '登录名',
            prop: 'userName',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'Password',
            label: '密码',
            prop: 'password',
            rules: { required: true, maxLength: 50 },
          },
          {
            type: 'Select',
            label: '返回值类型',
            prop: 'apiResultType',
            rules: { required: true },
            options: this.selectUtil.apiResultType,
            change: this.changeResultType,
          },
          {
            type: 'Input',
            label: '返回值属性前缀',
            prop: 'apiColumnsPrefix',
            rules: { required: false, maxLength: 200 },
          },
          {
            type: 'Select',
            label: '请求方式',
            prop: 'apiRequestType',
            rules: { required: true },
            options: this.selectUtil.requestType,
          },
          {
            type: 'Table',
            label: '返回值属性',
            prop: 'apiResultProps',
            rules: { required: false },
            tableCols: [],
            tableHandles: [],
            isPagination: false,
            isIndex: false,
          },
          {
            type: 'Table',
            label: '请求header',
            prop: 'apiRequestHeader',
            rules: { required: false },
            tableCols: [],
            tableHandles: [],
            isPagination: false,
            isIndex: false,
          },
          { type: 'Textarea', label: 'JSON请求内容', prop: 'apiParams', rules: { required: false }, rows: 6, width: '500px' },
          {
            type: 'Textarea',
            label: '接口返回结果',
            prop: 'apiResult',
            rules: { required: false },
            rows: 6,
            width: '500px',
          },
        ],
        apiResultProps: [
          { label: '属性值编码', prop: 'propCode', align: 'center', width: 150, overflow: true },
          { label: '属性值名称', prop: 'propName', align: 'center', width: 150, overflow: true },
          {
            label: '操作',
            prop: 'operation',
            align: 'center',
            type: 'button',
            width: 120,
            btnList: [
              {
                label: '编辑',
                type: 'primary',
                auth: 'ignore',
                handle: (row, index) => this.editProps(row, index),
              },
              {
                label: '删除',
                type: 'primary',
                auth: 'ignore',
                handle: (row, index) => this.deleteProps(row, index),
              },
            ],
          },
        ],
        propsTableHandles: [
          { label: '新增', type: 'primary', handle: () => this.showAddProps(), auth: 'ignore' },
          { label: '解析属性', type: 'warning', handle: () => this.parseAttr(), auth: 'ignore' }
        ],
        propsTableData: [],
        apiHeaderProps: [
          { label: '属性', prop: 'headerName', align: 'center', width: 150, overflow: true },
          { label: '属性值', prop: 'headerValue', align: 'center', width: 150, overflow: true },
          {
            label: '操作',
            prop: 'operation',
            align: 'center',
            type: 'button',
            width: 120,
            btnList: [
              {
                label: '编辑',
                type: 'primary',
                auth: 'ignore',
                handle: (row, index) => this.editHeaders(row, index),
              },
              {
                label: '删除',
                type: 'primary',
                auth: 'ignore',
                handle: (row, index) => this.deleteHeaders(row, index),
              },
            ],
          },
        ],
        headersTableHandles: [
          { label: '新增', type: 'primary', handle: () => this.showAddHeaders(), auth: 'ignore' },
        ],
        headersTableData: [],
        //modal表单 end
        //modal 数据 start
        modalData: {
          //modal页面数据
          code: '', //编码
          name: '', //数据源名称
          type: '', //驱动
          jdbcUrl: '', //数据源链接url
          userName: '', //登录名
          password: '', //密码
          apiResultType: '', //返回值类型
          apiResultProps: '', //返回值属性
          apiRequestHeader: '', //请求header
          apiParams: '', //JSON请求内容
          apiColumnsPrefix: '', //返回值属性
          apiRequestType: '', //请求方式
          apiResult: '', //接口返回结果
        },
        //modal 数据 end
        //modal 按钮 start
        modalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeModal() },
          { label: '测试连接', type: 'warning', handle: () => this.testConnection() },
          { label: '提交', type: 'primary', handle: () => this.save() },
        ],
        //modal 按钮 end
        resultPropModalConfig: {
          title: '新增', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '700px', //弹出框宽度
          modalRef: 'resultPropModalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        resultPropModalForm: [
          { type: 'Input', label: '属性值名称', prop: 'propName', rules: { required: true } },
          {
            type: 'Input',
            label: '属性值编码',
            prop: 'propCode',
            rules: { required: true},
          },
        ],
        resultPropModalData: {
          //modal页面数据
          propCode: '', //编码
          propName: '', //数据源名称
        },
        resultPropModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closePropsModal() },
          { label: '提交', type: 'primary', handle: () => this.saveProps() },
        ],
        apiHeaderModalConfig: {
          title: '新增', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '700px', //弹出框宽度
          modalRef: 'apiHeaderModalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        apiHeaderModalForm: [
          { type: 'Input', label: '属性', prop: 'headerName', rules: { required: true } },
          { type: 'Input', label: '属性值', prop: 'headerValue', rules: { required: true } },
        ],
        apiHeaderModalData: {
          //modal页面数据
          headerName: '', //属性
          headerValue: '', //属性值
        },
        apiHeaderModalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeHeadersModal() },
          { label: '提交', type: 'primary', handle: () => this.saveheaders() },
        ],
        isEdit: false,
        editIndex: null,
      },
    };
  },
  mounted() {
    this.pageData.tableData = [];
    this.searchtablelist();
  },
  methods: {
    /**
     * @description: 获取表格数据
     * @param {type}
     * @return:
     * @author: caiyang
     */
    searchtablelist() {
      this.tableLoading = true;

      var obj = {
        url: this.apis.reportDatasource.listApi,
        params: Object.assign({}, this.pageData.queryData, this.pageData.tablePage),
      };
      this.commonUtil.getTableList(obj).then((response) => {
        this.commonUtil.tableAssignment(response, this.pageData.tablePage, this.pageData.tableData);
        this.tableLoading = false;
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
    showModal(type, id) {
      this.commonUtil.showModal(this.pageData.modalConfig, type);
      this.pageData.headersTableData = [];
      this.pageData.propsTableData = [];
      if (type != this.commonConstants.modalType.insert) {
        this.getDetail(id);
      } else {
        this.changeDatasourceType();
      }
      this.pageData.modalForm[9].tableCols = this.pageData.apiResultProps;
      this.pageData.modalForm[9].tableHandles = this.pageData.propsTableHandles;
      this.pageData.modalForm[9].tableData = this.pageData.propsTableData;
      this.pageData.modalForm[10].tableCols = this.pageData.apiHeaderProps;
      this.pageData.modalForm[10].tableHandles = this.pageData.headersTableHandles;
      this.pageData.modalForm[10].tableData = this.pageData.headersTableData;
    },
    /**
     * @description: 获取详细数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    getDetail(id) {
      var obj = {
        url: this.apis.reportDatasource.getDetailApi,
        params: { id: id },
      };
      this.commonUtil.doGet(obj).then((response) => {
        this.commonUtil.coperyProperties(this.pageData.modalData, response.responseData); //数据赋值
        if (this.pageData.modalData.type == '4') {
          var columns = JSON.parse(response.responseData.apiColumns);
          for (let index = 0; index < columns.length; index++) {
            this.pageData.propsTableData.push(columns[index]);
          }
          if (response.responseData.apiRequestHeader) {
            var headers = JSON.parse(response.responseData.apiRequestHeader);
            for (let index = 0; index < headers.length; index++) {
              this.pageData.headersTableData.push(headers[index]);
            }
          }
          this.testConnection();
        }
        this.changeDatasourceType();
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
    /**
     * @description: 保存数据
     * @param {type}
     * @return:
     * @author: caiyang
     */
    save() {
      this.$refs['modalRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var params = {
            id: this.pageData.modalData.id,
            code: this.pageData.modalData.code,
            name: this.pageData.modalData.name,
            type: this.pageData.modalData.type,
            jdbcUrl: this.pageData.modalData.jdbcUrl,
          };
          if (this.pageData.modalData.type == '4') {
            params.apiResultType = this.pageData.modalData.apiResultType;
            if (
              this.pageData.modalData.apiResultType == 'ObjectArray' ||
              this.pageData.modalData.apiResultType == 'Object'
            ) {
              if (!this.pageData.propsTableData || this.pageData.propsTableData.length == 0) {
                this.commonUtil.showMessage({
                  message: this.commonUtil.getMessageFromList('error.empty.apiresultprops', null),
                  type: this.commonConstants.messageType.error,
                });
                return;
              }
              params.apiColumns = JSON.stringify(this.pageData.propsTableData);
              params.apiRequestHeader = JSON.stringify(this.pageData.headersTableData);
              params.apiParams = this.pageData.modalData.apiParams
              params.apiColumnsPrefix = this.pageData.modalData.apiColumnsPrefix;
              params.apiRequestType = this.pageData.modalData.apiRequestType;
            }
          } else {
            params.userName = this.pageData.modalData.userName;
            params.password = this.pageData.modalData.password;
          }
          var obj = {
            params: params,
            removeEmpty: false,
          };
          if (this.pageData.modalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.reportDatasource.insertApi;
          } else {
            obj.url = this.apis.reportDatasource.updateApi;
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
    /**
     * @description: 删除一条数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    deleteOne(id) {
      let obj = {
        url: this.apis.reportDatasource.deleteOneApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.delete', null),
        callback: this.searchtablelist,
        params: { id: id },
        type: 'get',
      };
      //弹出删除确认框
      this.commonUtil.showConfirm(obj);
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
          url: this.apis.reportDatasource.deleteBatchApi,
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
    routerTo(row) {
      this.$router.push({
        name: 'reportDatasourceDictType',
        query: { reportDatasourceId: row.id,thirdPartyType:localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType) },
      });
    },
    //修改数据源类型
    changeDatasourceType() {
      if (this.pageData.modalData.type == '4') {
        this.pageData.modalForm[4].show = false;
        this.pageData.modalForm[4].rules.required = false;
        this.pageData.modalForm[5].show = false;
        this.pageData.modalForm[5].rules.required = false;
        this.pageData.modalForm[6].show = true;
        this.pageData.modalForm[6].rules.required = true;
        this.pageData.modalForm[7].show = true;
        this.pageData.modalForm[8].show = true;
        this.pageData.modalForm[8].rules.required = true;
        this.pageData.modalForm[9].show = true;
        this.pageData.modalForm[10].show = true;
        this.pageData.modalForm[11].show = true;
        this.pageData.modalForm[12].show = true
      } else {
        if (this.pageData.modalData.type == '9') {
          this.pageData.modalForm[4].rules.required = false;
          this.pageData.modalForm[5].rules.required = false;
        } else {
          this.pageData.modalForm[4].rules.required = true;
          this.pageData.modalForm[5].rules.required = true;
        }
        this.pageData.modalForm[4].show = true;
        this.pageData.modalForm[5].show = true;
        this.pageData.modalForm[6].show = false;
        this.pageData.modalForm[6].rules.required = false;
        this.pageData.modalForm[7].show = false;
        this.pageData.modalForm[8].show = false;
        this.pageData.modalForm[8].rules.required = false;
        this.pageData.modalForm[9].show = false;
        this.pageData.modalForm[10].show = false;
        this.pageData.modalForm[11].show = false;
        this.pageData.modalForm[12].show = false
      }
    },
    //修改返回值类型
    changeResultType() {
      if (
        this.pageData.modalData.apiResultType == 'ObjectArray' ||
        this.pageData.modalData.apiResultType == 'Object'
      ) {
        this.pageData.modalForm[9].show = true;
      } else {
        this.pageData.modalForm[9].show = false;
      }
    },
    showAddProps() {
      this.pageData.resultPropModalConfig.show = true;
      this.pageData.isEdit = false;
      this.pageData.editIndex = null;
    },
    editProps(row, index) {
      this.pageData.resultPropModalConfig.show = true;
      this.pageData.resultPropModalData.propCode = row.propCode;
      this.pageData.resultPropModalData.propName = row.propName;
      this.pageData.isEdit = true;
      this.pageData.editIndex = index;
    },
    closePropsModal() {
      this.pageData.resultPropModalConfig.show = false;
      this.$refs['resultPropRef'].$refs['modalFormRef'].resetFields(); //校验重置
      this.commonUtil.clearObj(this.pageData.resultPropModalData); //清空modalData
    },
    saveProps() {
      var that = this;
      this.$refs['resultPropRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            propCode: that.pageData.resultPropModalData.propCode,
            propName: that.pageData.resultPropModalData.propName,
          };
          if (that.pageData.isEdit) {
            that.$set(that.pageData.propsTableData, that.pageData.editIndex, obj);
          } else {
            that.pageData.propsTableData.push(obj);
          }
          that.closePropsModal();
        } else {
          return false;
        }
      });
    },
    deleteProps(row, index) {
      this.pageData.propsTableData.splice(index, 1);
    },
    showAddHeaders() {
      this.pageData.apiHeaderModalConfig.show = true;
      this.pageData.isEdit = false;
      this.pageData.editIndex = null;
    },
    editHeaders(row, index) {
      this.pageData.apiHeaderModalConfig.show = true;
      this.pageData.isEdit = true;
      this.pageData.editIndex = index;
      this.pageData.apiHeaderModalData.headerName = row.headerName;
      this.pageData.apiHeaderModalData.headerValue = row.headerValue;
    },
    closeHeadersModal() {
      this.pageData.apiHeaderModalConfig.show = false;
      this.$refs['apiHeaderRef'].$refs['modalFormRef'].resetFields(); //校验重置
      this.commonUtil.clearObj(this.pageData.apiHeaderModalData); //清空modalData
    },
    deleteHeaders(row, index) {
      this.pageData.headersTableData.splice(index, 1);
    },
    saveheaders() {
      this.$refs['apiHeaderRef'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            headerName: this.pageData.apiHeaderModalData.headerName,
            headerValue: this.pageData.apiHeaderModalData.headerValue,
          };
          if (this.pageData.isEdit) {
            this.pageData.headersTableData[this.pageData.editIndex] = obj;
            // this.$set(this.pageData.headersTableData,this.pageData.editIndex,obj);
          } else {
            this.pageData.headersTableData.push(obj);
          }
          this.closeHeadersModal();
        } else {
          return false;
        }
      });
    },
    testConnection() {
      var params = {
        type: this.pageData.modalData.type,
        jdbcUrl: this.pageData.modalData.jdbcUrl,
        userName: this.pageData.modalData.userName,
        password: this.pageData.modalData.password,
        apiParams: this.pageData.modalData.apiParams,
        apiRequestType: this.pageData.modalData.apiRequestType,
        apiRequestHeader: JSON.stringify(this.pageData.headersTableData),
      };
      var obj = {
        params: params,
        removeEmpty: false,
        url: this.apis.reportDatasource.connectionTestApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        this.pageData.modalData.apiResult = response.responseData.apiResult;
      });
    },
    parseAttr() {
      var params = {
        jdbcUrl: this.pageData.modalData.jdbcUrl,
        apiResultType: this.pageData.modalData.apiResultType,
        apiRequestType: this.pageData.modalData.apiRequestType,
        apiParams: this.pageData.modalData.apiParams,
        apiColumnsPrefix: this.pageData.modalData.apiColumnsPrefix,
        apiRequestHeader: JSON.stringify(this.pageData.headersTableData)
      }
      var obj = {
        params: params,
        removeEmpty: false,
        url: this.apis.reportDatasource.parseApiResultAttrApi
      }
      var that = this
      this.commonUtil.doPost(obj).then(response => {
        that.pageData.propsTableData.splice(0, that.pageData.propsTableData.length)
        if (response.code == '200') {
          if (response.responseData && response.responseData.length > 0) {
            for (let index = 0; index < response.responseData.length; index++) {
              const element = response.responseData[index]
              that.pageData.propsTableData.push(element)
            }
          }
        }
      })
    }
  },
};
