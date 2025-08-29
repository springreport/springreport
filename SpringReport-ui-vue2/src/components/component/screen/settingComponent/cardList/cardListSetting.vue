<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="表格设置"> -->
    <el-form
      ref="settingForm"
      class="demo-form-inline"
      :model="component"
      label-position="top"
      size="mini"
    >
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">主题设置</span>
      </div>
      <div class="right-dataset-warp">
         <el-form-item label="背景颜色">
          <input-color-picker :value="component.bodyStyle.backgroundColor" @change="(val)=>{component.bodyStyle.backgroundColor=val;}" />
        </el-form-item>
      </div>
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">表头设置</span>
      </div>
      <div class="right-dataset-warp">
        <span class="attr-dataset-title">左侧表头</span>
        <el-form-item label="属性值">
          <el-input v-model="component.leftHead.property" />
        </el-form-item>
        <el-form-item label="字体大小">
          <el-input v-model="component.leftHead.fontSize" />
        </el-form-item>
        <el-form-item label="字体颜色">
            <input-color-picker :input-width="152" :value="component.leftHead.color" @change="(val)=>{component.leftHead.color=val}" />
          </el-form-item>
        <el-form-item label="是否加粗">
            <el-select
              v-model="component.leftHead.fontWeight"
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
          <div class="right-dataset-title df-c-b">
            <span class="attr-dataset-title">自定义字体颜色条件</span>
            <el-button
              class="addBtn"
              @click="addColorCondition('1')"
            ><i class="el-icon-plus el-icon--left" />添加</el-button>
          </div>
          <el-collapse
            v-if="component.leftHead.conditions && component.leftHead.conditions.length > 0"
            class="sub-collapse"
          >
             <el-collapse-item
                v-for="(item, index) in component.leftHead.conditions"
                :key="index"
              >
              <template slot="title">
                  {{ '自定义字体颜色条件'+(index+1) }}
                  <div
                    class="right-block-el-icon-edit"
                    @click.stop="editConditionColor('1', item, index)"
                  />
                  <div
                    class="right-el-icon-delete"
                    @click.stop="deleteConditionColor('1',index)"
                  />
                </template>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  属性：{{ item.conditionProperty }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  操作符：{{ item.conditionOperator }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  条件值：{{ item.conditionValue }}
                </p>
                <p class="column-tag" :style="{minWidth:'220px',maxWidth:'220px',color:item.conditionColor}">
                  字体颜色：{{ item.conditionColor }}
                </p>
             </el-collapse-item>
          </el-collapse>
      </div>
      <div class="right-dataset-warp">
        <span class="attr-dataset-title">右侧表头</span>
        <el-form-item label="属性值">
          <el-input v-model="component.rightHead.property" />
        </el-form-item>
        <el-form-item label="字体大小">
          <el-input v-model="component.rightHead.fontSize" />
        </el-form-item>
        <el-form-item label="字体颜色">
            <input-color-picker :input-width="152" :value="component.rightHead.color" @change="(val)=>{component.rightHead.color=val}" />
          </el-form-item>
        <el-form-item label="是否加粗">
            <el-select
              v-model="component.rightHead.fontWeight"
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
          <div class="right-dataset-title df-c-b">
            <span class="attr-dataset-title">自定义字体颜色条件</span>
            <el-button
              class="addBtn"
              @click="addColorCondition('2')"
            ><i class="el-icon-plus el-icon--left" />添加</el-button>
          </div>
          <el-collapse
            v-if="component.rightHead.conditions && component.rightHead.conditions.length > 0"
            class="sub-collapse"
          >
             <el-collapse-item
                v-for="(item, index) in component.rightHead.conditions"
                :key="index"
              >
              <template slot="title">
                  {{ '自定义字体颜色条件'+(index+1) }}
                  <div
                    class="right-block-el-icon-edit"
                    @click.stop="editConditionColor('2', item, index)"
                  />
                  <div
                    class="right-el-icon-delete"
                    @click.stop="deleteConditionColor('2',index)"
                  />
                </template>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  属性：{{ item.conditionProperty }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  操作符：{{ item.conditionOperator }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  条件值：{{ item.conditionValue }}
                </p>
                <p class="column-tag" :style="{minWidth:'220px',maxWidth:'220px',color:item.conditionColor}">
                  字体颜色：{{ item.conditionColor }}
                </p>
             </el-collapse-item>
          </el-collapse>
      </div>
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">卡片内容设置</span>
      </div>
      <div class="right-dataset-warp">
            <div class="right-dataset-title df-c-b">
              <span class="attr-dataset-title">行数据设置</span>
              <el-button
                class="addBtn"
                @click="addRowData()"
              ><i class="el-icon-plus el-icon--left" />添加</el-button>
            </div>
            <el-collapse
            v-if="component.bodyContent.rowContents && component.bodyContent.rowContents.length > 0"
            class="sub-collapse"
          >
             <el-collapse-item
                v-for="(item, index) in component.bodyContent.rowContents"
                :key="index"
              >
              <template slot="title">
                  {{ '行数据设置'+(index+1) }}
                  <i
                    class="el-icon-copy-document"
                    title="复制"
                    @click="doCopy(item)"
                  />
                  <div
                    class="right-block-el-icon-edit"
                    @click.stop="editRowData(item, index)"
                  />
                  <div
                    class="right-el-icon-delete"
                    @click.stop="deleteRowData(index)"
                  />
                  
                </template>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  标题：{{ item.title }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  标题字体大小：{{ item.titleFontSize }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  标题字体加粗：{{ item.titleFontWeight }}
                </p>
                <p class="column-tag" :style="{minWidth:'220px',maxWidth:'220px',color:item.titleColor}">
                  标题字体颜色：{{ item.titleColor }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  内容属性：{{ item.property }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  内容字体大小：{{ item.contentFontSize }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  内容字体加粗：{{ item.contentFontWeight }}
                </p>
                <p class="column-tag" :style="{minWidth:'220px',maxWidth:'220px',color:item.contentColor}">
                  内容字体颜色：{{ item.contentColor }}
                </p>
             </el-collapse-item>
          </el-collapse>
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
          <el-form-item label="每页列数">
            <el-input
              v-model.number="component.pagination.pageColumn"
              style="width: 160px"
            />
          </el-form-item>
          <el-form-item label="翻页时间间隔(毫秒)">
            <el-input
              v-model.number="component.pagination.autoPageInterval"
              style="width: 160px"
            />
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">下钻设置</span>
        </div>
        <div class="right-dataset-warp" v-if="component.type.toLowerCase().indexOf('basicmap')<0 && component.type.toLowerCase().indexOf('scattermap')<0">
          <el-form-item label="开启下钻" class="df-form-item">
              <el-switch v-model="component.isDrill" active-text="是" inactive-text="否"  />
            </el-form-item>
            <el-form-item label="下钻类型" class="df-form-item" v-if="component.isDrill">
            <el-select  v-model="component.drillType" placeholder="请选择" style="width:180px">
                <el-option
                  v-for="item in screenConstants.chartDrillType"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="跳转链接" class="df-form-item" v-if="component.isDrill && component.drillType == '2'">
              <el-input v-model="component.drillLink" type="textarea" :rows="3"/>
            </el-form-item>
            <el-form-item label="下钻参数" class="df-form-item" v-if="component.isDrill && component.drillType == '2'">
              <el-input v-model="component.drillParam" placeholder="多个参数用,分隔"/>
            </el-form-item>
        </div>
    </el-form>
  <el-dialog
      title="自定义字体颜色条件"
      :visible.sync="colorConditionDialogVisiable"
      :close-on-click-modal="false"
      width="500px"
      @close="closeColorCondition"
    >
      <el-form
        ref="colorConditionForm"
        class="demo-form-inline"
        :model="colorConditionForm"
        label-position="top"
        size="mini"
      >
        <el-form-item
          label="属性"
          prop="conditionProperty"
          :rules="filter_rules('属性', { required: true })"
        >
          <el-input v-model="colorConditionForm.conditionProperty" size="small" placeholder="属性"/>
        </el-form-item>
        <el-form-item
          label="操作符"
          prop="conditionOperator"
          :rules="filter_rules('操作符', { required: true })"
        >
           <el-select
            v-model="colorConditionForm.conditionOperator"
            placeholder="操作符"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.operate"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="条件值"
          prop="conditionValue"
          :rules="filter_rules('条件值', { required: true })"
        >
          <el-input
            v-model="colorConditionForm.conditionValue"
            placeholder="条件值"
            size="small"
          />
        </el-form-item>
        <el-form-item
          label="字体颜色"
          prop="conditionColor"
          :rules="filter_rules('字体颜色', { required: true })"
        >
           <input-color-picker
                  :value="colorConditionForm.conditionColor"
                  @change="
                    (val) => {
                      colorConditionForm.conditionColor = val;
                    }
                  "
                />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeColorCondition">取 消</el-button>
        <el-button type="primary" @click="confirmColorCondition">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="行数据设置"
      :visible.sync="rowDataDialog"
      :close-on-click-modal="false"
      width="500px"
      @close="closeRowDataDialog"
    >
      <el-form
        ref="rowDataForm"
        class="demo-form-inline"
        :model="rowDataForm"
        label-position="top"
        size="mini"
      >
        <el-form-item
          label="标题"
          prop="title"
          :rules="filter_rules('标题', { required: true })"
        >
          <el-input v-model="rowDataForm.title" size="small" placeholder="属性"/>
        </el-form-item>
        <el-form-item label="标题字体颜色" prop="titleColor"  :rules="filter_rules('标题字体颜色', { required: true })">
          <input-color-picker :value="rowDataForm.titleColor" @change="(val)=>{rowDataForm.titleColor=val;}" />
        </el-form-item>
         <el-form-item label="标题字体大小" prop="titleFontSize"  :rules="filter_rules('标题字体大小', { required: true })">
          <el-input v-model="rowDataForm.titleFontSize" />
        </el-form-item>
         <el-form-item label="标题是否加粗" prop="titleFontWeight"  :rules="filter_rules('标题是否加粗', { required: true })">
            <el-select
              v-model="rowDataForm.titleFontWeight"
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
        <el-form-item
          label="内容属性"
          prop="property"
          :rules="filter_rules('内容属性', { required: true })"
        >
          <el-input
            v-model="rowDataForm.property"
            placeholder="内容属性"
            size="small"
          />
        </el-form-item>
         <el-form-item label="内容字体颜色" prop="contentColor"  :rules="filter_rules('内容字体颜色', { required: true })">
          <input-color-picker :value="rowDataForm.contentColor" @change="(val)=>{rowDataForm.contentColor=val;}" />
        </el-form-item>
        <el-form-item label="内容字体大小" prop="contentFontSize"  :rules="filter_rules('内容字体大小', { required: true })">
          <el-input v-model="rowDataForm.contentFontSize" />
        </el-form-item>
        <el-form-item label="内容是否加粗" prop="contentFontWeight"  :rules="filter_rules('内容是否加粗', { required: true })">
            <el-select
              v-model="rowDataForm.contentFontWeight"
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
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeRowDataDialog">取 消</el-button>
        <el-button type="primary" @click="confirmRowData">确 定</el-button>
      </span>
    </el-dialog>
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
      colorConditionForm:{
        conditionOperator:'',
        conditionProperty:'',
        conditionValue:'',
        conditionColor:'',
        index:null,
        type:"1"
      },
      colorConditionDialogVisiable:false,
      rowDataDialog:false,
      rowDataForm:{
        title:"",
        titleColor:"",
        titleFontSize:"",
        titleFontWeight:"",
        property:"",
        contentColor:"",
        contentFontSize:"",
        contentFontWeight:"",
        index:null
      },
      tableTimer:{}
    }
  },
  mounted() {
  },
  methods: {
    //1 左侧表头 2右侧表头 
    addColorCondition(type){
      this.colorConditionDialogVisiable = true;
      this.colorConditionForm.type = type;
    },
    closeColorCondition(){
      this.colorConditionDialogVisiable = false;
      this.$refs['colorConditionForm'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.colorConditionForm)
    },
    confirmColorCondition(){
      this.$refs['colorConditionForm'].validate((valid) => {
        if (valid) {
          if (this.colorConditionForm.index != null) {
              let conditions = null;
              if(this.colorConditionForm.type == '1'){
                conditions = this.component.leftHead.conditions;
              }else{
                conditions = this.component.rightHead.conditions;
              }
              conditions[this.colorConditionForm.index].conditionOperator =
              this.colorConditionForm.conditionOperator
              conditions[this.colorConditionForm.index].conditionProperty =
              this.colorConditionForm.conditionProperty
              conditions[this.colorConditionForm.index].conditionValue =
              this.colorConditionForm.conditionValue
              conditions[this.colorConditionForm.index].conditionColor =
              this.colorConditionForm.conditionColor
          }else{
            var obj = {
              conditionOperator: this.colorConditionForm.conditionOperator,
              conditionProperty: this.colorConditionForm.conditionProperty,
              conditionValue: this.colorConditionForm.conditionValue,
              conditionColor: this.colorConditionForm.conditionColor
            }
            if(!this.component.leftHead.conditions){
              this.component.leftHead.conditions = [];
            }
            if(!this.component.rightHead.conditions){
              this.component.rightHead.conditions = [];
            }
            if(this.colorConditionForm.type == '1'){
              this.component.leftHead.conditions.push(obj)
            }else{
              this.component.rightHead.conditions.push(obj)
            }
          }
          this.closeColorCondition();
        }
      })
    },
    editConditionColor(type, item, index){
      this.colorConditionDialogVisiable = true;
      this.colorConditionForm.index = index;
      this.colorConditionForm.type = type;
      this.colorConditionForm.conditionProperty = item.conditionProperty
      this.colorConditionForm.conditionOperator = item.conditionOperator
      this.colorConditionForm.conditionValue = item.conditionValue
      this.colorConditionForm.conditionColor = item.conditionColor
    },
    deleteConditionColor(type,index){
      if(type == '1'){
        this.component.leftHead.conditions.splice(index, 1)
      }else{
        this.component.rightHead.conditions.splice(index, 1)
      }
    },
    addRowData(){
      this.rowDataDialog = true;
    },
    closeRowDataDialog(){
      this.rowDataDialog = false;
      this.$refs['rowDataForm'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.rowDataForm)
    },
    confirmRowData(){
      this.$refs['rowDataForm'].validate((valid) => {
        if (valid) {
          if (this.rowDataForm.index != null) {
              this.component.bodyContent.rowContents[this.rowDataForm.index].title =
              this.rowDataForm.title
              this.component.bodyContent.rowContents[this.rowDataForm.index].titleColor =
              this.rowDataForm.titleColor
              this.component.bodyContent.rowContents[this.rowDataForm.index].titleFontSize =
              this.rowDataForm.titleFontSize
              this.component.bodyContent.rowContents[this.rowDataForm.index].titleFontWeight =
              this.rowDataForm.titleFontWeight

              this.component.bodyContent.rowContents[this.rowDataForm.index].property =
              this.rowDataForm.property
              this.component.bodyContent.rowContents[this.rowDataForm.index].contentColor =
              this.rowDataForm.contentColor
              this.component.bodyContent.rowContents[this.rowDataForm.index].contentFontSize =
              this.rowDataForm.contentFontSize
              this.component.bodyContent.rowContents[this.rowDataForm.index].contentFontWeight =
              this.rowDataForm.contentFontWeight
          }else{
            var obj = {
              title: this.rowDataForm.title,
              titleColor: this.rowDataForm.titleColor,
              titleFontSize: this.rowDataForm.titleFontSize,
              titleFontWeight: this.rowDataForm.titleFontWeight,
              property: this.rowDataForm.property,
              contentColor: this.rowDataForm.contentColor,
              contentFontSize: this.rowDataForm.contentFontSize,
              contentFontWeight: this.rowDataForm.contentFontWeight
            }
            if(!this.component.bodyContent.rowContents){
              this.component.bodyContent.rowContents = [];
            }
            this.component.bodyContent.rowContents.push(obj)
          }
          this.closeRowDataDialog();
        }
      })
    },
    editRowData(row,index){
      this.rowDataDialog = true;
      this.rowDataForm.index = index;
      this.rowDataForm.title = row.title;
      this.rowDataForm.titleColor = row.titleColor;
      this.rowDataForm.titleFontSize = row.titleFontSize;
      this.rowDataForm.titleFontWeight = row.titleFontWeight;
      this.rowDataForm.property = row.property;
      this.rowDataForm.contentColor = row.contentColor;
      this.rowDataForm.contentFontSize = row.contentFontSize;
      this.rowDataForm.contentFontWeight = row.contentFontWeight;
    },
    deleteRowData(index){
      this.component.bodyContent.rowContents.splice(index, 1)
    },
    doCopy(item){
      let data =  JSON.parse(JSON.stringify(item));
      this.component.bodyContent.rowContents.push(data)
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
    max-height: 70%;
    overflow: auto;
}
::v-deep .el-col{
  padding-left: 0.5px;
  padding-right: 0.5px;
}

</style>
