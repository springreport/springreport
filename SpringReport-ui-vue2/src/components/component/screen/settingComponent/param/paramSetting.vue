<template>
  <div>
    <el-collapse>
      <el-collapse-item title="参数设置">
        <el-button type="primary" @click="showAddParamDailog()" size="mini"
          >添加参数<i class="el-icon-plus el-icon--right"></i
        ></el-button>
        <div style="height: 3px"></div>
        <div
          v-for="(item, index) in component.params"
          :key="index"
          class="demo-form-inline"
        >
          <el-descriptions
            class="margin-top"
            title=""
            direction="vertical"
            :column="3"
            size="mini"
            border
          >
            <el-descriptions-item>
              <template slot="label"> 参数编码 </template>
              {{ item.paramCode }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"> 参数名称 </template>
              {{ item.paramName }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"> 操作 </template>
              <el-button-group>
                <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  size="mini"
                  title="编辑"
                  @click="editParam(item,index)"
                ></el-button>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  size="mini"
                  title="删除"
                  @click="deleteParam(index)"
                ></el-button>
              </el-button-group>
            </el-descriptions-item>
            <template slot="extra"> </template>
          </el-descriptions>
          <div style="height: 3px"></div>
        </div>
      </el-collapse-item>
    </el-collapse>
    <el-dialog
      title="组件参数"
      :visible.sync="isShowParamDialog"
      width="50%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <div class="el-dialog-div">
        <el-form
          :inline="true"
          :model="paramForm"
          class="demo-form-inline"
          ref="paramRef"
          size="mini"
        >
          <el-form-item
            label="参数名称"
            prop="paramName"
            :rules="filter_rules('参数名称', { required: true })"
          >
            <el-input
              v-model="paramForm.paramName"
              placeholder="参数名称"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="参数编码"
            prop="paramCode"
            :rules="filter_rules('参数编码', { required: true })"
          >
            <el-input
              v-model="paramForm.paramCode"
              placeholder="参数编码"
            ></el-input>
          </el-form-item>
          <el-form-item label="参数前缀"  prop="paramPrefix" :rules="filter_rules('参数前缀',{required:false})">
              <el-input v-model="paramForm.paramPrefix" placeholder="参数前缀" size="small"></el-input>
          </el-form-item>
          <el-form-item
            label="参数类型"
            prop="paramType"
            :rules="filter_rules('参数类型', { required: true })"
          >
            <el-select v-model="paramForm.paramType" placeholder="参数类型">
              <el-option label="字符串" value="varchar" />
              <el-option label="数值" value="number" />
              <el-option label="日期" value="date" />
              <el-option label="下拉单选" value="select" />
              <el-option label="下拉多选" value="mutiselect" />
              <el-option label="下拉树(单选)" value="treeSelect" />
              <el-option label="下拉树(多选)" value="multiTreeSelect" />
            </el-select>
          </el-form-item>
          <el-form-item
                v-if="paramForm.paramType == 'date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select
                  v-model="paramForm.dateFormat"
                  placeholder="日期格式"
                  size="small"
                >
                  <el-option label="年" value="yyyy" />
                  <el-option label="年-月" value="yyyy-MM" />
                  <el-option label="年-月-日" value="yyyy-MM-dd" />
                  <el-option label="年-月-日 时:分" value="yyyy-MM-dd HH:mm" />
                </el-select>
              </el-form-item>
          <el-form-item label="默认值">
            <el-input
              v-model="paramForm.paramDefault"
              placeholder="默认值"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="是否必填" prop="paramRequired" :rules="filter_rules('参数必填',{required:true})">
                          <el-select v-model="paramForm.paramRequired" placeholder="是否必填">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item> -->
          <el-form-item
            label="是否隐藏"
            prop="paramHidden"
            :rules="filter_rules('是否隐藏', { required: true })"
          >
            <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏">
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="2"></el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item
            label="参数组件宽度"
            prop="width"
            :rules="
              filter_rules('参数组件宽度', { required: true, type: 'number' })
            "
          >
            <el-input v-model="paramForm.width" placeholder="参数组件宽度">
              <template slot="append">px</template>
            </el-input>
          </el-form-item> -->
          <el-form-item
            v-if="
              paramForm.paramType == 'select' ||
              paramForm.paramType == 'mutiselect'
            "
            label="选择内容来源"
            prop="selectType"
            :rules="filter_rules('选择内容来源', { required: true })"
          >
            <el-select
              v-model="paramForm.selectType"
              placeholder="选择内容来源"
            >
              <el-option label="自定义" value="1"></el-option>
              <el-option label="sql语句" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="(paramForm.paramType == 'select' &&
                    paramForm.selectType == '2') ||
                    (paramForm.paramType == 'mutiselect' &&
                      paramForm.selectType == '2') ||
                    paramForm.paramType == 'treeSelect' ||
                    paramForm.paramType == 'multiTreeSelect'"
            label="选择数据源"
            prop="dataSourceId"
            :rules="filter_rules('选择内容来源', { required: true })"
          >
            <el-select
              v-model="paramForm.dataSourceId"
              placeholder="选择数据源"
            >
              <el-option
                v-for="item in datasource"
                :key="item.id"
                :label="item.code"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
                v-if="
                  (paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') && (paramForm.selectType == '2' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect')
                "
                key="isRelyOnParams"
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select
                  v-model="paramForm.isRelyOnParams"
                  placeholder="是否依赖其他参数"
                  size="small"
                >
                  <el-option label="是" :value=1 />
                  <el-option label="否" :value=2 />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  (paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') &&
                    (paramForm.selectType == '2' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect') &&
                    paramForm.isRelyOnParams == '1'
                "
                key="relyOnParams"
                label="依赖参数代码"
                prop="relyOnParams"
                :rules="filter_rules('依赖参数代码', { required: true })"
              >
                <el-input
                  v-model="paramForm.relyOnParams"
                  placeholder="依赖参数代码，多个用,分隔"
                  size="small"
                />
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'multiTreeSelect'"
                key="checkStrictly"
                label="父子联动"
                prop="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select
                  v-model="paramForm.checkStrictly"
                  placeholder="选择父子联动"
                  size="small"
                >
                  <el-option label="是" value="1" />
                  <el-option label="否" value="2" />
                </el-select>
              </el-form-item>
          <el-form-item
            v-if="
              paramForm.paramType == 'select' ||
              paramForm.paramType == 'mutiselect' ||
              paramForm.paramType == 'treeSelect' ||
              paramForm.paramType == 'multiTreeSelect'
            "
            label="下拉选择内容"
            prop="selectContent"
            :rules="filter_rules('下拉选择内容', { required: true })"
          >
            <el-input
              type="textarea"
              :cols="80"
              v-model="paramForm.selectContent"
              placeholder="下拉选择内容"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <!-- <el-tag v-show="paramForm.paramType == 'date'" type="primary"
              >注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT</el-tag
            >
            <el-tag v-show="paramForm.paramType == 'date'" type="primary"
              >如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag
            > -->
            <el-link :underline="false" v-if="paramForm.paramType == 'date'" type="warning" href="https://gitee.com/springreport/springreport/wikis/pages?sort_id=13973093&doc_id=5747656" target="_blank">点击查看日期默认值设置规则</el-link>
            <el-tag v-show="paramForm.paramType == 'select'" type="primary"
              >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
              注意：两个key必须是value 和 name</el-tag
            >
            <el-tag v-show="paramForm.paramType == 'select'" type="primary"
              >sql语句格式：select code as value, name as name from table
              注意：返回的属性中必须有 value 和 name</el-tag
            >
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog" size="mini">取 消</el-button>
        <el-button type="primary" @click="addParam" size="mini"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
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
  },
  mounted() {
    this.getReportDatasource();
  },
  data() {
    return {
      isShowParamDialog: false,
      paramForm: {
        paramName: "", //参数名称
        paramCode: "", //参数编码
        paramType: "", //参数类型
        paramDefault: "", //默认值
        paramRequired: "", //是否必选
        paramHidden: "", //是否隐藏
        selectContent: "", //下拉选择内容
        selectType: "", //内容来源
        width: "110", //参数组件宽度
        dataSourceId: "", //数据源
        selectData: null,
        dateFormat:"",//日期格式
        isRelyOnParams: '', // 是否依赖其他参数
        relyOnParams: '', // 依赖参数代码
        checkStrictly: '', // 父子联动 1是 2否
      },
      isEdit: false,
      editIndex: 0,
      datasource: [],
    };
  },
  methods: {
    showAddParamDailog() {
      this.isShowParamDialog = true;
    },
    addParam() {
      this.$refs["paramRef"].validate((valid) => {
        if (valid) {
          var key = this.paramForm.paramCode;
          var obj = {
            paramName: this.paramForm.paramName, //参数名称
            paramCode: this.paramForm.paramCode, //参数编码
            paramType: this.paramForm.paramType, //参数类型
            paramDefault: this.paramForm.paramDefault, //默认值
            paramHidden: this.paramForm.paramHidden, //是否隐藏
            paramRequired: this.paramForm.paramRequired, //是否必选
            selectContent: this.paramForm.selectContent, //下拉选择内容
            selectType: this.paramForm.selectType, //内容来源
            datasourceId: this.paramForm.dataSourceId, //数据源
            width: this.paramForm.width, //参数组件宽度
            selectData: null, //下拉选择内容
            paramPrefix:this.paramForm.paramPrefix,//参数前缀  api请求复杂参数用到
            dateFormat:this.paramForm.dateFormat,
            isRelyOnParams:this.paramForm.isRelyOnParams,
            relyOnParams:this.paramForm.relyOnParams,
            checkStrictly:this.paramForm.checkStrictly,
          };
          var value = this.commonUtil.getDefaultValue(obj);
          obj[key] = value;

          if (this.component.params) {
            if (this.isEdit) {
              this.component.params[this.editIndex].paramName =
                this.paramForm.paramName;
              this.component.params[this.editIndex].paramCode =
                this.paramForm.paramCode;
              this.component.params[this.editIndex].paramType =
                this.paramForm.paramType;
              this.component.params[this.editIndex].paramDefault =
                this.paramForm.paramDefault;
              this.component.params[this.editIndex].paramRequired =
                this.paramForm.paramRequired;
              this.component.params[this.editIndex].paramHidden =
                this.paramForm.paramHidden;
              this.component.params[this.editIndex].selectContent =
                this.paramForm.selectContent;
              this.component.params[this.editIndex].selectType =
                this.paramForm.selectType;
              this.component.params[this.editIndex].width =
                this.paramForm.width;
              this.component.params[this.editIndex].datasourceId =
                this.paramForm.dataSourceId;
              this.component.params[this.editIndex].selectData = null;
              this.component.params[this.editIndex].paramPrefix = this.paramForm.paramPrefix;
              this.component.params[this.editIndex].dateFormat = this.paramForm.dateFormat;
              this.component.params[this.editIndex].isRelyOnParams = this.paramForm.isRelyOnParams;
              this.component.params[this.editIndex].relyOnParams = this.paramForm.relyOnParams;
              this.component.params[this.editIndex].checkStrictly = this.paramForm.checkStrictly;
              this.component.params[this.editIndex][key] = value;
              // if (this.component.params[this.editIndex].paramType == "select") {
              //   this.getSelectData(this.component.params[this.editIndex]);
              // }
            } else {
              // if (obj.paramType == "select") {
              //   this.getSelectData(obj);
              // }
              this.component.params.push(obj);
            }
          } else {
            this.component.params = [];
            this.component.params.push(obj);
          }
          this.getHiddenParamSize();
          this.closeDialog();
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.isShowParamDialog = false;
      this.paramForm.width = "110";
      this.isEdit = false;
      this.editIndex = 0;
      this.$refs["paramRef"].resetFields(); //校验重置
      this.commonUtil.clearObj(this.paramForm);
    },
    editParam(item, index) {
      this.isEdit = true;
      this.editIndex = index;
      this.paramForm.paramName = item.paramName;
      this.paramForm.paramCode = item.paramCode;
      this.paramForm.paramType = item.paramType;
      this.paramForm.paramDefault = item.paramDefault;
      this.paramForm.paramRequired = item.paramRequired;
      this.paramForm.paramHidden = item.paramHidden;
      this.paramForm.selectContent = item.selectContent;
      this.paramForm.selectType = item.selectType;
      this.paramForm.width = item.width;
      this.paramForm.dataSourceId = item.datasourceId;
      this.paramForm.paramPrefix = item.paramPrefix;
      this.paramForm.dateFormat = item.dateFormat;
      this.paramForm.isRelyOnParams = item.isRelyOnParams;
      this.paramForm.relyOnParams = item.relyOnParams;
      this.paramForm.checkStrictly = item.checkStrictly;
      this.getHiddenParamSize();
      this.showAddParamDailog();
    },
    deleteParam(index) {
      this.component.params.splice(index, 1);
      this.getHiddenParamSize();
    },
    //获取数据源
    getReportDatasource() {
      var obj = {
        params: { datasourceType: ["1", "2", "3", "5"] },
        url: this.apis.reportDatasource.getReportDatasourceApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          this.datasource = response.responseData;
        }
      });
    },
    getSelectData(obj) {
      if (obj.selectType == "1") {
        this.$set(obj, "selectData", eval(obj.selectContent));
      } else {
        var requestParam = {
          params: {
            dataSourceId: obj.dataSourceId,
            selectContent: obj.selectContent,
          },
          url: this.apis.reportDatasource.getDatasourceSelectDataApi,
        };
        var that = this;
        this.commonUtil.doPost(requestParam).then((response) => {
          if (response.code == "200") {
            that.$set(obj, "selectData", response.responseData);
          }
        });
      }
    },
    getHiddenParamSize(){
      if(this.component.params && this.component.params.length > 0){
        let size = 0;
        for (let index = 0; index < this.component.params.length; index++) {
          const element = this.component.params[index];
          if(element.paramHidden == 1){
              size = size + 1;
          }
        }
        this.component.hiddenParamSize = size;
      }else{
        this.component.hiddenParamSize = 0;
      }
    }
  },
};
</script>