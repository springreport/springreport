export default {
  name: 'sysUser',
  data() {
    return {
      tableLoading: true,
      pageData: {
        defaultProps: {
          children: 'children',
          label: 'deptName',
        },
        treeData: [],
        //查询表单内容 start
        searchForm: [
          { type: 'Input', label: '登录名', prop: 'userName' },
          { type: 'Input', label: '邮箱', prop: 'userEmail' },
          { type: 'Input', label: '手机', prop: 'userMobile' },
        ],
        //查询表单内容 end
        //查询条件 start
        queryData: {
          userName: '', //用户登录名
          userMobile: '', //手机，唯一
          userEmail: '',
          deptId: null,
        },
        //查询条件 end
        //查询表单按钮start
        searchHandle: [
          {
            label: '查询',
            type: 'primary',
            handle: () => this.searchtablelist(),
            auth: 'sysUser_search',
          },
          {
            label: '清除条件',
            type: '',
            handle: () => this.resetSearch(),
            auth: 'sysUser_search',
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
            type: 'primary',
            position: 'right',
            iconClass: 'action-icon-add',
            handle: () => this.showModal(this.commonConstants.modalType.insert),
            auth: 'sysUser_insert',
          },
          {
            label: '批量删除',
            type: 'danger',
            position: 'left',
            iconClass: 'action-icon-del',
            handle: () => this.deleteBatch(),
            auth: 'sysUser_batchDelete',
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
            type: 'dropdown',
            width: 54,
            btnList: [
              {
                label: '查看',
                type: 'primary',
                auth: 'sysUser_getDetail',
                handle: (row) => this.showModal(this.commonConstants.modalType.detail, row.id),
              },
              {
                label: '编辑',
                type: 'primary',
                auth: 'sysUser_update',
                handle: (row) => this.showModal(this.commonConstants.modalType.update, row.id),
                show: (row) => this.isShowDelete(row),
              },
              {
                label: '重置密码',
                type: 'primary',
                auth: 'sysUser_resetPwd',
                handle: (row) => this.resetPwd(row.id),
                show: (row) => this.isShowResetPwd(row),
              },
              {
                label: '删除',
                type: 'danger',
                auth: 'sysUser_delete',
                handle: (row) => this.deleteOne(row.id),
                show: (row) => this.isShowDelete(row),
              },
            ],
          },
          { label: '用户登录名', prop: 'userName', align: 'center', overflow: true },
          { label: '用户姓名', prop: 'userRealName', align: 'center', overflow: true },
          { label: '用户邮箱', prop: 'userEmail', align: 'center', overflow: true },
          { label: '座机', prop: 'userPhone', align: 'center', overflow: true },
          { label: '手机', prop: 'userMobile', align: 'center', overflow: true },
          { label: '角色', prop: 'roleName', align: 'center', overflow: true },
          { label: '部门', prop: 'deptName', align: 'center', overflow: true },
          { label: '岗位', prop: 'postName', align: 'center', overflow: true },
          {
            label: '是否锁定',
            prop: 'userLocked',
            align: 'center',
            codeType: 'yesNo',
            formatter: this.commonUtil.getTableCodeName,
            overflow: true,
          },
          {
            label: '是否管理员',
            prop: 'isAdmin',
            align: 'center',
            codeType: 'yesNo',
            formatter: this.commonUtil.getTableCodeName,
            overflow: true,
          },
        ],
        //表格列表头end
        //modal配置 start
        modalConfig: {
          title: '新增', //弹窗标题,值为:新增，查看，编辑
          show: false, //弹框显示
          formEditDisabled: false, //编辑弹窗是否可编辑
          width: '700px', //弹出框宽度
          modalRef: 'modalRef', //modal标识
          type: '1', //类型 1新增 2编辑 3保存
        },
        //modal配置 end
        //modal表单 start
        modalForm: [
          {
            type: 'Input',
            label: '用户登录名',
            prop: 'userName',
            rules: { required: true, maxLength: 40 },
          },
          { type: 'Password', label: '密码', prop: 'password', rules: { required: true } },
          {
            type: 'Input',
            label: '用户姓名',
            prop: 'userRealName',
            rules: { required: true, maxLength: 40 },
          },
          {
            type: 'TreeSelect',
            label: '所属部门',
            prop: 'deptId',
            rules: { required: true },
            lazy: false,
            clearable: true,
            data: [],
            defaultExpandedKeys: [],
            nodeKey: 'id',
            props: { parent: 'parentId', value: 'id', label: 'deptName', children: 'children' },
          },
          {
            type: 'Select',
            label: '岗位',
            prop: 'postId',
            rules: { required: true },
            props: { label: 'postName', value: 'id' },
          },
          {
            type: 'Input',
            label: '用户邮箱',
            prop: 'userEmail',
            rules: { required: false, maxLength: 50, type: 'email' },
          },
          {
            type: 'Input',
            label: '座机',
            prop: 'userPhone',
            rules: { required: false, maxLength: 15, type: 'phone' },
          },
          {
            type: 'Input',
            label: '手机',
            prop: 'userMobile',
            rules: { required: true, maxLength: 20, type: 'mobile' },
          },
          // {type:'Select',label:'超级管理员',prop:'isAdmin',rules:{required:true},options:this.selectUtil.yesNo,change:this.changeIsAdmin},
          {
            type: 'Select',
            label: '角色',
            prop: 'roleId',
            rules: { required: true, maxLength: 20 },
            props: { label: 'roleName', value: 'id' },
          },
        ],
        //modal表单 end
        //modal 数据 start
        modalData: {
          //modal页面数据
          userName: '', //用户登录名
          password: '',
          userRealName: '', //用户真实姓名
          userEmail: '', //用户邮箱，唯一
          userPhone: '', //座机
          userMobile: '', //手机，唯一
          roleId: '', //角色
          isAdmin: '', //是否超级管理员
          deptId: null,
          postId: null,
        },
        //modal 数据 end
        //modal 按钮 start
        modalHandles: [
          { label: '取消', type: 'default', handle: () => this.closeModal() },
          { label: '提交', type: 'primary', handle: () => this.save() },
        ],
        //modal 按钮 end
      },
    };
  },
  mounted() {
    this.pageData.tableData = [];
    this.searchtablelist();
    this.getDepts();
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
        url: this.apis.sysUser.listApi,
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
      this.tableLoading = false;
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
      if (type != this.commonConstants.modalType.insert) {
        this.pageData.modalForm[1].show = false;
        this.getDetail(id);
      } else {
        this.pageData.modalForm[1].show = true;
        this.pageData.modalData.isAdmin = 2;
        // this.changeIsAdmin();
      }
      this.getRoles();
      this.getPosts();
    },
    /**
     * @description: 获取详细数据
     * @param {id} 数据唯一标识
     * @return:
     * @author: caiyang
     */
    getDetail(id) {
      var obj = {
        url: this.apis.sysUser.getDetailApi,
        params: { id: id },
      };
      this.commonUtil.doGet(obj).then((response) => {
        this.commonUtil.coperyProperties(this.pageData.modalData, response.responseData); //数据赋值
        this.pageData.modalForm[3].defaultExpandedKeys = [];
        if (response.responseData.deptId) {
          this.pageData.modalForm[3].defaultExpandedKeys.push(response.responseData.deptId);
        }
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
          var obj = {
            params: this.pageData.modalData,
            removeEmpty: false,
          };
          if (this.pageData.modalConfig.type == this.commonConstants.modalType.insert) {
            obj.url = this.apis.sysUser.insertApi;
          } else {
            obj.url = this.apis.sysUser.updateApi;
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
        url: this.apis.sysUser.deleteOneApi,
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
          url: this.apis.sysUser.deleteBatchApi,
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
    //获取角色
    getRoles() {
      var obj = {
        url: this.apis.sysUser.getRolesApi,
        params: {},
      };
      this.commonUtil.getTableList(obj).then((response) => {
        this.pageData.modalForm[8].options = response.responseData;
        this.$refs['modalRef'].$forceUpdate(); //在methods中需强制更新，mounted中不需要
      });
    },
    //是否超级管理员修改
    changeIsAdmin() {
      if (this.pageData.modalData.isAdmin == '1') {
        this.pageData.modalData.roleId = '';
        this.pageData.modalForm[9].show = false;
        this.pageData.modalForm[9].rules.required = false;
      } else {
        this.pageData.modalForm[9].show = true;
        this.pageData.modalForm[9].rules.required = true;
      }
    },
    handleNodeClick(data) {
      this.pageData.queryData.deptId = data.id;
      this.searchtablelist();
    },
    getDepts() {
      var obj = {
        url: this.apis.sysDept.listApi,
        params: {},
      };
      var that = this;
      this.commonUtil.getTableList(obj).then((response) => {
        that.pageData.treeData = response.responseData;
        that.pageData.modalForm[3].data = response.responseData;
      });
    },
    getPosts() {
      var obj = {
        url: this.apis.sysPost.getSysPostsApi,
        params: {},
      };
      var that = this;
      this.commonUtil.getTableList(obj).then((response) => {
        that.pageData.modalForm[4].options = response.responseData;
        this.$refs['modalRef'].$forceUpdate();
      });
    },
    isShowDelete(row) {
      if (row.isAdmin == 1) {
        return false;
      }
      return true;
    },
    isShowResetPwd(row) {
      if (localStorage.getItem(this.commonConstants.sessionItem.isAdmin) == 1 && row.isAdmin != 1) {
        return true;
      }
      return false;
    },
    resetPwd(id) {
      let obj = {
        url: this.apis.sysUser.resetPwdApi,
        messageContent: this.commonUtil.getMessageFromList('confirm.operate', null),
        callback: this.searchtablelist,
        params: { id: id },
        type: 'post',
      };
      this.commonUtil.showConfirm(obj);
    },
  },
};
