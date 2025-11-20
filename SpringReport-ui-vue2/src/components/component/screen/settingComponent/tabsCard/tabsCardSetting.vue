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
          <input-color-picker :value="component.style.backgroundColor" @change="(val)=>{component.style.backgroundColor=val;}" />
        </el-form-item>
      </div>
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">标签内容</span>
        <el-button
           class="addBtn"
          @click="addTab()"
        ><i class="el-icon-plus el-icon--left" />添加</el-button>
      </div>
      <el-collapse
            v-if="component.tabs && component.tabs.length > 0"
            class="sub-collapse"
          >
             <el-collapse-item
                v-for="(item, index) in component.tabs"
                :key="index"
              >
              <template slot="title">
                  {{ '标签内容'+(index+1) }}
                  <div
                    class="right-block-el-icon-edit"
                    @click.stop="editTab(item, index)"
                  />
                  <div
                    class="right-el-icon-delete"
                    @click.stop="deleteTab(index)"
                  />
                </template>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  标签：{{ item.tabContent }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  组件类型：{{ getSubComponentTypeName(item.subComponentType) }}
                </p>
                <sub-settings
                  ref="tabForm"
                  :component="item.subComponent"
                  :charts-components="chartsComponents"
                  :timer-map="timerMap"
                  :components="components"
                />
             </el-collapse-item>
          </el-collapse>
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">标签设置</span>
      </div>
      <div class="right-dataset-warp">
        <el-form-item label="自动切换">
          <el-switch v-model="component.autoChangeTab"/>
        </el-form-item>
        <el-form-item
            v-if="component.autoChangeTab"
            label="切换时间间隔"
          >
            <el-input v-model.number="component.changeTabInterval">
              <template slot="append">毫秒</template>
            </el-input>
          </el-form-item>
         <el-form-item label="间隔距离">
          <el-input v-model="component.style.titleStyle.tabinterval" />
        </el-form-item>
        <el-form-item label="宽度">
          <el-input v-model="component.style.titleStyle.width" />
        </el-form-item>
        <el-form-item label="高度">
          <el-input v-model="component.style.titleStyle.height" />
        </el-form-item>
        <el-form-item label="字体大小">
          <el-input v-model="component.style.titleStyle.fontSize" />
        </el-form-item>
        <el-form-item label="字体颜色">
            <input-color-picker :input-width="152" :value="component.style.titleStyle.color" @change="(val)=>{component.style.titleStyle.color=val}" />
        </el-form-item>
        <el-form-item label="激活字体颜色">
            <input-color-picker :input-width="152" :value="component.style.titleStyle.activeColor" @change="(val)=>{component.style.titleStyle.activeColor=val}" />
        </el-form-item>
        <el-form-item label="是否加粗">
            <el-select
              v-model="component.style.titleStyle.fontWeight"
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
        <el-form-item label="标签颜色">
            <input-color-picker :input-width="152" :value="component.style.titleStyle.tagColor" @change="(val)=>{component.style.titleStyle.tagColor=val}" />
        </el-form-item>
        <el-form-item label="激活标签颜色">
            <input-color-picker :input-width="152" :value="component.style.titleStyle.activeTagColor" @change="(val)=>{component.style.titleStyle.activeTagColor=val}" />
        </el-form-item>
      </div>
    </el-form>
  <el-dialog
      title="标签设置"
      :visible.sync="tabsDialog"
      :close-on-click-modal="false"
      width="500px"
      @close="closeTab"
    >
      <el-form
        ref="tabsFormRef"
        class="demo-form-inline"
        :model="tabsForm"
        label-position="top"
        size="mini"
      >
        <el-form-item
          label="标签名称"
          prop="tabContent"
          :rules="filter_rules('标签名称', { required: true })"
        >
          <el-input v-model="tabsForm.tabContent" size="small" placeholder="标签名称"/>
        </el-form-item>
        <el-form-item
          label="绑定组件"
          prop="subComponentType"
          :rules="filter_rules('绑定组件', { required: true })"
        >
           <el-select
            v-model="tabsForm.subComponentType"
            placeholder="绑定组件"
            size="small"
            filterable
          >
            <el-option
              v-for="(value, key)  in screenConstants.compType"
              :key="key"
              :label="value.text"
              :value="key"
              v-show="key != 'border' && key != 'decoration' && key != 'tabsCard'"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeTab">取 消</el-button>
        <el-button type="primary" @click="confirmAddTab">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import VChart from '@visactor/vchart'
import InputColorPicker from '../../colorpicker/inputColorPicker.vue'
export default {
  name:"tabsCardSetting",
  components: {
    InputColorPicker,
    subSettings:(d) => import("../../settings/settings.vue")
  },
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    },
    components: {
      type: Array,
      default: () => []
    },
    timerMap: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      tabsDialog:false,
      tabsForm:{
        index:null,
        tabContent:"",//标签标题
        subComponentType:null,//子组件类型
      },
      tableTimer:{}
    }
  },
  mounted() {
  },
  methods: {
    //
    addTab(){
      this.tabsDialog = true;
    },
    closeTab(){
      this.tabsDialog = false;
      this.$refs['tabsFormRef'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.tabsForm)
    },
    confirmAddTab(){
      var that = this;
      this.$refs['tabsFormRef'].validate((valid) => {
        if (valid) {
          if(that.tabsForm.index != null){
            let index = that.tabsForm.index;
            let subComponentId = that.component.tabs[index].subComponent.id;
            if(that.component.tabs[index].subComponentType != that.tabsForm.subComponentType){
              
              that.component.tabs[index].subComponent = that.getSubComponent(that.tabsForm.subComponentType);
              that.$nextTick(() => {
                if(that.chartsComponents[subComponentId]){
                  that.chartsComponents[subComponentId].release();
                }
                delete that.chartsComponents[subComponentId]
                if(that.component.tabs[index].subComponent.category == that.screenConstants.category.vchart){
                  if (that.component.tabs[index].subComponent.type.toLowerCase().indexOf('sankey') >= 0) {
                    that.component.tabs[index].subComponent.spec.nodeKey = (datum) => datum.name
                  }
                  const vchart = new VChart(that.component.tabs[index].subComponent.spec, { dom: that.component.tabs[index].subComponent.id })
                  // 绘制
                  vchart.renderSync()
                  that.chartsComponents[that.component.tabs[index].subComponent.id] = vchart
                }else if (that.component.tabs[index].subComponent.type == that.screenConstants.type.date) {
                  setInterval(() => {
                    const self = that
                    setTimeout(function() {
                      self.refreshTime(that.component.tabs[index].subComponent)
                    }, 0)
                  }, 1000)
                }
              })
            }
            that.component.tabs[index].tabContent = that.tabsForm.tabContent
            that.component.tabs[index].subComponentType = that.tabsForm.subComponentType
          }else{
            var obj = {};
            obj.tabContent = that.tabsForm.tabContent
            obj.subComponentType = that.tabsForm.subComponentType
            obj.subComponent = that.getSubComponent(obj.subComponentType);
            that.component.tabs.push(obj);
            that.$nextTick(() => {
              if(obj.subComponent.category == that.screenConstants.category.vchart){
                if (obj.subComponent.type.toLowerCase().indexOf('sankey') >= 0) {
                  obj.subComponent.spec.nodeKey = (datum) => datum.name
                }
                const vchart = new VChart(obj.subComponent.spec, { dom: obj.subComponent.id })
                // 绘制
                vchart.renderSync()
                that.chartsComponents[obj.subComponent.id] = vchart
              }else if (obj.subComponent.type == that.screenConstants.type.date) {
                setInterval(() => {
                  const self = that
                  setTimeout(function() {
                    self.refreshTime(obj.subComponent)
                  }, 0)
                }, 1000)
              }
            })
          }
          that.closeTab();
        }else{
          return false;
        }
      })
    },
    refreshTime(component) {
      component.content = this.commonUtil.getCurrentDate(component)
    },
    editTab( item, index){
      this.tabsDialog = true;
      this.tabsForm.index = index;
      this.tabsForm.tabContent = item.tabContent;
      this.tabsForm.subComponentType = item.subComponentType
    },
    deleteTab(index){
        this.component.tabs.splice(index, 1)
    },
    getSubComponent(subComponentType){
      let obj = JSON.parse(
        JSON.stringify(this.screenConstants[subComponentType+'Init'])
      )
      obj.id = this.commonUtil.getUuid()
      obj.isSubContent = true;
      return obj;
    },
    getSubComponentTypeName(type){
      let typeName = type;
      let obj = this.screenConstants.compType[type];
      if(obj){
        typeName = obj.text
      }
      return typeName;
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
