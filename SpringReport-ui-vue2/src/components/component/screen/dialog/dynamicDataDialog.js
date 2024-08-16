import sqlFormatter from "sql-formatter";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/theme/eclipse.css";
import "codemirror/theme/material-palenight.css";
import "codemirror/addon/fold/foldgutter.css";
import "codemirror/addon/fold/foldcode.js";
import "codemirror/addon/fold/foldgutter.js";
import "codemirror/addon/fold/xml-fold.js";
import "codemirror/addon/fold/indent-fold.js";
import "codemirror/addon/fold/brace-fold";
import "codemirror/addon/fold/markdown-fold.js";
import "codemirror/addon/fold/comment-fold.js";
import "codemirror/addon/selection/active-line";
import "codemirror/addon/hint/sql-hint.js";
export default {
  props: {
    component: {
      type: Object,
      default: () => ({}),
    },
    chartsComponents: {
      type: Object,
      default: () => ({}),
    },
    dynamicDialogVisiable: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      cmOptions: {},
      dataSetForm: {
        //数据集表单
        dataSetId: "", //选中的数据集id
        column: null, //选中的数据列z
        isMulti: false,
      },
      addDatasetsDialogVisiable: false,
      dataSets: [], //数据集
      dataColumn: [], //数据列
      sqlForm: {
        id: "",
        datasetName: "", //数据集名称
        datasourceId: "", //数据源id
        sqlType: 1,
      }, //sql表单
      dataSource: [],
      //sql解析对应的列表格数据
      sqlColumnTableData: {
        tableData: [],
        tablePage: {
          currentPage: 1,
          pageSize: 10,
          pageTotal: 0,
          pageSizeRange: [5, 10, 20, 50],
        },
      },
      showDatasetsDialog: false,
      datasourceType: "1", //1 sql 2 api
    };
  },
  mounted() {
    this.cmOptions = this.commonConstants.cmOptions;
    this.cmOptions.theme = "erlang-dark";
    this.init();
  },
  methods: {
    init() {
      this.getDataSets();
    },
    //获取数据集
    getDataSets() {
      let tplId = this.$route.query.tplId; //tplId
      let obj = {
        url: this.apis.reportDesign.getDataSetsApi,
        params: { tplId: tplId },
        removeEmpty: false,
      };
      var that = this;
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          that.dataSets = response.responseData;
          that.dataSetForm.dataSetId = that.component.dynamicDataSettings.datasetId;
          that.dataSetForm.column = that.component.dynamicDataSettings.dataColumns;
        }
      });
    },
    closeDynamicDataDialog() {
      this.$emit("update:dynamicDialogVisiable", false);
    },
    showAddDatasetDialog() {
      this.addDatasetsDialogVisiable = true;
      this.getScreenTplDateSource();
    },
    //获取模板关联的数据源
    getScreenTplDateSource() {
      let tplId = this.$route.query.tplId; //tplId
      let obj = {
        url: this.apis.reportDesign.getReportTplDateSourceApi,
        params: { tplId: tplId },
        removeEmpty: false,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          this.dataSource = response.responseData;
          this.changeDatasource();
        }
      });
    },
    // 选择数据源修改
    changeDatasource() {
      this.sqlColumnTableData.tableData = [];
      for (let index = 0; index < this.dataSource.length; index++) {
        const element = this.dataSource[index];
        if (this.sqlForm.datasourceId == element.datasourceId) {
          if (element.type == "4") {
            this.datasourceType = "2";
            if (element.apiColumns) {
              const columns = JSON.parse(element.apiColumns);
              if (columns.length > 0) {
                for (let index = 0; index < columns.length; index++) {
                  const element = columns[index];
                  var obj = {
                    columnName: element.propName,
                    name: element.propCode,
                  };
                  this.sqlColumnTableData.tableData.push(obj);
                }
              }
            }
          } else {
            this.datasourceType = "1";
          }
          break;
        }
      }
    },
    addDataSet() {
      let tplId = this.$route.query.tplId; //tplId
      let obj = {};
      if (this.datasourceType == "1") {
        let tplSql = this.$refs.codeMirror.codemirror.getValue();
        if (!tplSql.trim()) {
          this.commonUtil.showMessage({
            message: "sql语句不能为空",
            type: this.commonConstants.messageType.error,
          });
          return;
        }
        obj = {
          url: this.apis.reportDesign.addDataSetApi,
          params: {
            id: this.sqlForm.id,
            tplId: tplId,
            datasetType: 1,
            sqlType: 1,
            tplSql: tplSql,
            datasourceId: this.sqlForm.datasourceId,
            datasetName: this.sqlForm.datasetName,
          },
          removeEmpty: false,
        };
      } else {
        obj = {
          url: this.apis.reportDesign.addDataSetApi,
          params: {
            id: this.sqlForm.id,
            tplId: tplId,
            datasetType: 2,
            datasourceId: this.sqlForm.datasourceId,
            datasetName: this.sqlForm.datasetName,
          },
          removeEmpty: false,
        };
      }
      this.$refs["sqlRef"].validate((valid) => {
        if (valid) {
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == "200") {
              this.addDatasetsDialogVisiable = false;
              this.getDataSets();
            }
          });
        } else {
          return;
        }
      });
    },
    //修改数据集，获取数据集对应的列
    changeDataset() {
      if (this.dataSetForm.dataSetId) {
        this.getDatasetColumn(this.dataSetForm.dataSetId);
      } else {
        this.dataColumn = [];
      }
    },
    getDatasetColumn(dataSetId) {
      let obj = {
        url: this.apis.reportDesign.getDataSetColumnsApi,
        params: { id: dataSetId },
        removeEmpty: false,
      };
      this.dataSetForm.column = [];
      var that = this;
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          that.dataColumn = response.responseData;
        }
      });
    },
    execSql() {
      this.$refs["sqlRef"].validate((valid) => {
        if (valid) {
          let obj = {
            url: this.apis.reportDesign.execSqlApi,
            params: {
              tplSql: this.$refs.codeMirror.codemirror.getValue(),
              datasourceId: this.sqlForm.datasourceId,
              sqlType: 1,
            },
            removeEmpty: false,
          };
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == "200") {
              this.sqlColumnTableData.tableData = response.responseData;
              this.sqlColumnTableData.tablePage.pageTotal =
                response.responseData.length;
            }
          });
        }
      });
    },
    //关闭添加数据源
    closeAddDataSet() {
      this.addDatasetsDialogVisiable = false;
      if (this.datasourceType == 1) {
        this.$refs.codeMirror.codemirror.setValue("");
      }
      this.$refs["sqlRef"].resetFields(); //校验重置
      this.commonUtil.clearObj(this.sqlForm); //清空modalData
    },
    formatSql() {
      //sql格式化
      let sqlContent = "";
      sqlContent = this.$refs.codeMirror.codemirror.getValue();
      this.$refs.codeMirror.codemirror.setValue(
        sqlFormatter.format(sqlContent)
      );
    },
    //sql语句列表修改当前页触发事件
    handleCurrentChange: function (val) {
      this.sqlColumnTableData.tablePage.currentPage = val;
    },
    //sql语句列表修改当每页显示条数触发事件
    handleSizeChange: function (val) {
      this.sqlColumnTableData.tablePage.pageSize = val;
    },
    datasetEditDialog() {
      this.showDatasetsDialog = true;
    },
    //确认绑定数据集
    dataSetConfirm() {
      var that = this;
      this.$refs["dataSetForm"].validate((valid) => {
        if (valid) {
          var params = {
            dataSetId: that.dataSetForm.dataSetId,
            dataColumns: that.dataSetForm.column,
            requestKey: that.commonUtil.getUuid(),
            sqlType: "1",
          };
          var componentParams = that.commonUtil.getComponentParams(
            that.component.params
          );
          params.params = Object.assign({}, componentParams,that.$route.query);
          let obj = {
            url: that.apis.screenDesign.getDynamicDatasApi,
            params: params,
            removeEmpty: false,
          };
          that.commonUtil.doPost(obj).then((response) => {
            if (response.code == "200") {
              that.processDynamicData(response);
            }
          });
        } else {
          return false;
        }
      });
    },
    processDynamicData(response) {
      this.component.spec.data.values = response.responseData;
      this.component.dynamicDataSettings.datasetId = this.dataSetForm.dataSetId;
      this.component.dynamicDataSettings.dataColumns = this.dataSetForm.column;
      this.commonUtil.reLoadChart(this.chartsComponents, this.component);
      if(this.component.type == "pageTable" && Array.isArray(this.component.spec.data.values)){
        this.component.spec.data.total = this.component.spec.data.values.length;
      }
      this.closeDynamicDataDialog();
    },
    editDatasets(index, item) {
      this.addDatasetsDialogVisiable = true;
      this.getScreenTplDateSource();
      this.sqlForm.datasetName = item.datasetName;
      this.sqlForm.datasourceId = item.datasourceId;
      this.sqlForm.id = item.id;
      if (item.datasetType == 1) {
        this.datasourceType = 1;
        this.$nextTick(() => {
          this.$refs.codeMirror.codemirror.setValue(item.tplSql);
          this.execSql();
        });
      } else {
        this.datasourceType = 2;
      }
    },
    //删除数据集
    deleteDatasets(index, item) {
      let obj = {
        url: this.apis.screenDesign.deleteDatasetApi,
        messageContent: this.commonUtil.getMessageFromList(
          "confirm.delete",
          null
        ),
        callback: this.getDataSets,
        params: { id: item.id },
        type: "get",
      };
      //弹出删除确认框
      this.commonUtil.showConfirm(obj);
    },
    doCopy(item){
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
};