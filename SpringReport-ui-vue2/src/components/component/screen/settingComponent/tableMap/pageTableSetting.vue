<template>
    <div>
      <el-form
      ref="settingForm"
      class="demo-form-inline"
      :model="component"
      label-position="top"
      size="mini"
    >
         <div class="right-dataset-title">
          <span class="attr-dataset-title">表格设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="行号" class="df-form-item">
            <el-switch
              v-model="component.isIndex"
              active-text="是"
              inactive-text="否"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item v-if="component.isIndex" label="行号列宽度">
            <el-input v-model="component.indexWidth" style="width: 160px" />
          </el-form-item>
          <el-form-item label="对齐方式">
            <el-select
              v-model="component.align"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in screenConstants.tableAlign"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表头样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.headStyle.backgroundColor"
              @change="
                (val) => {
                  component.headStyle.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker
              :value="component.headStyle.color"
              @change="
                (val) => {
                  component.headStyle.color = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input
              v-model.number="component.headStyle.fontSize"
              style="width: 100%"
            >
              <template slot="append">pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.headStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">行数据样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.rowStyle.backgroundColor"
              @change="
                (val) => {
                  component.rowStyle.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker
              :value="component.rowStyle.color"
              @change="
                (val) => {
                  component.rowStyle.color = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input
              v-model.number="component.rowStyle.fontSize"
              style="width: 100%"
            >
              <template slot="append">pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.rowStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">分页设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="每页显示条数">
            <el-input
              v-model.number="component.pagination.pageSize"
              style="width: 160px"
            />
          </el-form-item>
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.pagination.backgroundColor"
              @change="
                (val) => {
                  component.pagination.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="自动翻页" class="df-form-item">
            <el-switch
              v-model="component.autoPage"
              active-text="是"
              inactive-text="否"
              @change="changeAutoPage(component)"
            />
          </el-form-item>
          <el-form-item label="翻页时间间隔(秒)" v-if="component.autoPage">
            <el-input
              v-model.number="component.autoPageInterval"
              style="width: 160px"
              @change="changeAutoPageInterval(component)"
            />
          </el-form-item>
        </div>
        </el-form>
    </div>
</template>

<script>
import InputColorPicker from '../../colorpicker/inputColorPicker.vue'
export default {
  components: {
    InputColorPicker
  },
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      tableColumnDialogVisiable: false,
      tableColumnForm: {
        name: '',
        key: '',
        width: '',
        type: '1', // 1新增 2编辑
        index: null,
        conditions:[],//颜色条件
        isHyperLink:false,
        hyperLink:"",
        hyperLinkParam:"",
        isProgress:false,//是否进度条显示
        progressWidth:10,
        thresholdValue:"",//阈值
        upColor:"",//大于阈值显示的颜色
        downColor:"",//小于阈值显示的颜色
        equalColor:"",//等于阈值显示的颜色
        textColor:"",
      },
      colorConditionForm:{
        conditionOperator:'',
        conditionProperty:'',
        conditionValue:'',
        conditionColor:'',
        index:null
      },
      colorConditionDialogVisiable:false,
      tableTimer:{}
    }
  },
  mounted() {},
  methods: {
    changeTableLineHeight(component) {
      component.options.singleHeight = component.bodyStyle.height
      this.reloadTable(component)
    },
    reloadTable(component) {
      this.$parent.$parent.$parent.$refs['draggable'].$refs[
        component.id
      ][0].$refs[component.id].reset()
    },
    // type 类型 1新增 2编辑
    showAddTableColumn(type, row, index) {
      this.tableColumnDialogVisiable = true
      if (type == '2') {
        this.tableColumnForm.name = row.name
        this.tableColumnForm.key = row.key
        if (this.component.type == 'scrollTable') {
          this.tableColumnForm.width = row.style.width
        }
        this.tableColumnForm.type = type
        this.tableColumnForm.index = index
        this.tableColumnForm.conditions = row.conditions;
        this.tableColumnForm.isHyperLink = row.isHyperLink==null?false:row.isHyperLink;
        this.tableColumnForm.hyperLink = row.hyperLink;
        this.tableColumnForm.hyperLinkParam = row.hyperLinkParam;
        this.tableColumnForm.isProgress = row.isProgress==null?false:row.isProgress;
        this.tableColumnForm.thresholdValue = row.thresholdValue;
        this.tableColumnForm.progressWidth = row.progressWidth;
        this.tableColumnForm.upColor = row.upColor;
        this.tableColumnForm.downColor = row.downColor;
        this.tableColumnForm.equalColor = row.equalColor;
        this.tableColumnForm.textColor = row.textColor;
      } else {
        this.tableColumnForm.name = ''
        this.tableColumnForm.key = ''
        this.tableColumnForm.width = ''
        this.tableColumnForm.type = type
        this.tableColumnForm.index = index
        this.tableColumnForm.isHyperLink = false;
        this.tableColumnForm.hyperLink = '';
        this.tableColumnForm.hyperLinkParam = '';
        this.tableColumnForm.isProgress = false;
        this.tableColumnForm.thresholdValue = '50';
        this.tableColumnForm.progressWidth = 10;
        this.tableColumnForm.upColor = '';
        this.tableColumnForm.downColor = '';
        this.tableColumnForm.equalColor = '';
        this.tableColumnForm.textColor = null;
      }
    },
    addTableColomn() {
      this.$refs['tableColumnForm'].validate((valid) => {
        if (valid) {
          if (this.tableColumnForm.type == '1') {
            var obj = {
              name: this.tableColumnForm.name,
              key: this.tableColumnForm.key,
              isHyperLink: this.tableColumnForm.isHyperLink,
              hyperLink: this.tableColumnForm.hyperLink,
              hyperLinkParam: this.tableColumnForm.hyperLinkParam,
              isProgress: this.tableColumnForm.isProgress,
              thresholdValue: this.tableColumnForm.thresholdValue,
              progressWidth: this.tableColumnForm.progressWidth,
              upColor: this.tableColumnForm.upColor,
              downColor: this.tableColumnForm.downColor,
              equalColor: this.tableColumnForm.equalColor,
              textColor: this.tableColumnForm.textColor
            }
            if (this.component.type == 'scrollTable') {
              obj.style = { width: this.tableColumnForm.width }
            }
            this.component.tableColumn.push(obj)
          } else {
            this.component.tableColumn[this.tableColumnForm.index].name =
              this.tableColumnForm.name
            this.component.tableColumn[this.tableColumnForm.index].key =
              this.tableColumnForm.key
            if (this.component.type == 'scrollTable') {
              this.component.tableColumn[
                this.tableColumnForm.index
              ].style.width = this.tableColumnForm.width
            }
            this.component.tableColumn[this.tableColumnForm.index].conditions =
              this.tableColumnForm.conditions
            this.component.tableColumn[this.tableColumnForm.index].isHyperLink =
              this.tableColumnForm.isHyperLink
            this.component.tableColumn[this.tableColumnForm.index].hyperLink =
              this.tableColumnForm.hyperLink
            this.component.tableColumn[this.tableColumnForm.index].hyperLinkParam =
              this.tableColumnForm.hyperLinkParam
            this.component.tableColumn[this.tableColumnForm.index].isProgress =
              this.tableColumnForm.isProgress
            this.component.tableColumn[this.tableColumnForm.index].thresholdValue =
              this.tableColumnForm.thresholdValue
            this.component.tableColumn[this.tableColumnForm.index].progressWidth =
              this.tableColumnForm.progressWidth
            this.component.tableColumn[this.tableColumnForm.index].upColor =
              this.tableColumnForm.upColor
            this.component.tableColumn[this.tableColumnForm.index].downColor =
              this.tableColumnForm.downColor
            this.component.tableColumn[this.tableColumnForm.index].equalColor =
              this.tableColumnForm.equalColor
            this.component.tableColumn[this.tableColumnForm.index].textColor =
              this.tableColumnForm.textColor
          }
          this.tableColumnForm.name = ''
          this.tableColumnForm.key = ''
          this.tableColumnForm.width = ''
          this.tableColumnForm.type = '1'
          this.tableColumnForm.index = null
          this.tableColumnForm.isHyperLink = false
          this.tableColumnForm.hyperLink = null
          this.tableColumnForm.hyperLinkParam = null

          this.tableColumnForm.isProgress = false
          this.tableColumnForm.thresholdValue = ""
          this.tableColumnForm.progressWidth = 10
          this.tableColumnForm.upColor = ""
          this.tableColumnForm.downColor = ""
          this.tableColumnForm.equalColor = ""
          this.tableColumnForm.textColor = null
          this.closeTableColumnDialog()
          this.reloadTable(this.component)
        }
      })
    },
    // 删除列
    deleteTableColumn(index) {
      this.component.tableColumn.splice(index, 1)
      this.reloadTable(this.component)
    },
    closeTableColumnDialog() {
      this.tableColumnDialogVisiable = false
      this.commonUtil.clearObj(this.tableColumnForm)
      this.$refs['tableColumnForm'].resetFields() // 校验重置
    },
    changeAutoPage(component){
      let timer = this.tableTimer[component.id];
      if(component.autoPage){
        if(timer == null){
          timer = setInterval(function(){
            if((component.pagination.currentPage*component.pagination.pageSize)>=component.tableDatas.length) {
              component.pagination.currentPage = 1;
            }else{
              component.pagination.currentPage = component.pagination.currentPage + 1
            }
          },component.autoPageInterval?component.autoPageInterval*1000:5000);
          this.tableTimer[component.id] = timer
        }
      }else{
        clearInterval(timer);     
        this.tableTimer[component.id] = null;
      }
    },
    changeAutoPageInterval(component){
      let timer = this.tableTimer[component.id];
      if(timer != null){
        clearInterval(timer);     
        this.tableTimer[component.id] = null;
      }
      this.changeAutoPage(component);
    },
    addColorCondition(){
      this.colorConditionDialogVisiable = true;
    },
    closeColorConditionDialog(){
      this.colorConditionDialogVisiable = false;
      this.$refs['colorConditionForm'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.colorConditionForm)
    },
    confirmColorCondition(){
      this.$refs['colorConditionForm'].validate((valid) => {
        if (valid) {
          if (this.colorConditionForm.index != null) {
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionOperator =
              this.colorConditionForm.conditionOperator
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionProperty =
              this.colorConditionForm.conditionProperty
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionValue =
              this.colorConditionForm.conditionValue
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionColor =
              this.colorConditionForm.conditionColor
          }else{
            var obj = {
              conditionOperator: this.colorConditionForm.conditionOperator,
              conditionProperty: this.colorConditionForm.conditionProperty,
              conditionValue: this.colorConditionForm.conditionValue,
              conditionColor: this.colorConditionForm.conditionColor
            }
            if(!this.tableColumnForm.conditions){
              this.tableColumnForm.conditions = [];
            }
            this.tableColumnForm.conditions.push(obj)
          }
          this.closeColorConditionDialog();
          this.reloadTable(this.component)
        }
      })
    },
    editConditionColor(row,index){
      this.colorConditionDialogVisiable = true;
      this.colorConditionForm.conditionOperator = row.conditionOperator
      this.colorConditionForm.conditionProperty = row.conditionProperty
      this.colorConditionForm.conditionValue = row.conditionValue
      this.colorConditionForm.conditionColor = row.conditionColor
      this.colorConditionForm.index = index
    },
    deleteConditionColor(index){
      this.tableColumnForm.conditions.splice(index, 1)
    }
  }
}
</script>
<style scoped >
.el-form-item {
  margin-bottom: 5px !important;
}
::v-deep .el-form-item__label-wrap {
  margin-left: 0px !important;
}
::v-deep .el-color-picker__trigger {
  /* top:-12px */
}
::v-deep .customLabel {
  font-weight: bold;
}
::v-deep .customLabel .el-form-item__label {
  color: #15a585 !important;
}

.showAll_dialog {
    overflow: hidden;

}

::v-deep .el-dialog {
        max-height: 80%;
        overflow: auto;
    }


</style>