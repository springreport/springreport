<!-- 通用设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="通用设置"> -->
        <el-form ref="settingForm" class="demo-form-inline" label-position="top" :model="component" size="mini">
          <div class="right-dataset-title" v-if="!component.isSubContent">
            <span class="attr-dataset-title">位置设置</span>
          </div>
          <div class="right-dataset-warp" v-if="!component.isSubContent">
            <el-form-item label="左" class="df-form-item">
              <el-input-number v-model.number="component.x" style="width: 100%;" placeholder="左" :disabled="component.locked" />
            </el-form-item>
            <el-form-item label="上" class="df-form-item">
              <el-input-number v-model.number="component.y" style="width: 100%;" placeholder="上" :disabled="component.locked" />
            </el-form-item>
          </div>
          <div class="right-dataset-title">
            <span class="attr-dataset-title">尺寸设置</span>
          </div>
          <div class="right-dataset-warp">
            <el-form-item label="宽" class="df-form-item">
              <el-input-number v-model.number="component.w" style="width: 100%;" placeholder="宽度" :disabled="component.locked" />
            </el-form-item>
            <el-form-item label="高" class="df-form-item">
              <el-input-number v-model.number="component.h" style="width: 100%;" placeholder="高度" :disabled="component.locked" />
            </el-form-item>
          </div>
          <div class="right-dataset-title" v-if="!component.isSubContent">
            <span class="attr-dataset-title">组件设置</span>
          </div>
          <div class="right-dataset-warp" v-if="!component.isSubContent">
            <el-form-item label="组件名称" class="df-form-item">
              <el-input v-model="component.text" placeholder="组件名称" />
            </el-form-item>
            <el-form-item label="组件层级" class="df-form-item">
              <el-input v-model.number="component.zindex" placeholder="组件层级" />
            </el-form-item>
            <el-form-item label="锁定组件" class="df-form-item">
              <el-switch v-model="component.locked" active-text="是" inactive-text="否" />
            </el-form-item>
          </div>
          <div class="right-dataset-title" v-if="component.type!='picture' && component.type!='border' && component.type!='decoration' && !component.isSubContent">
            <span class="attr-dataset-title">边框设置</span>
          </div>
          <div class="right-dataset-warp" v-if="component.type!='picture' && component.type!='border' && component.type!='decoration' && !component.isSubContent">
            <el-form-item label="显示边框" class="df-form-item">
              <el-switch v-model="component.isborder" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </el-form-item>
            <el-form-item v-show="component.isborder" label="边框类型">
              <el-select v-model="component.borderType" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)">
                <el-option
                  v-for="item in selectUtil.borderType"
                  :key="item.name"
                  :label="item.text"
                  :value="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item v-if="component.isborder && component.borderColor" label="边框颜色1">
              <input-color-picker :input-width="152" :value="component.borderColor[0]" @change="(val)=>{changeColor(0,val)}" />
            </el-form-item>
            <el-form-item v-if="component.isborder && component.borderColor" label="边框颜色2">
              <input-color-picker :input-width="152" :value="component.borderColor[1]" @change="(val)=>{changeColor(1,val)}" />
            </el-form-item>
            <el-form-item  v-if="component.isborder" label="边框背景色">
              <input-color-picker :value="component.borderBackgroundColor" @change="(val)=>{changeBorderBackgroundColor(val)}" />
            </el-form-item>
            <el-form-item label="上边距" class="df-form-item" v-if="component.isborder && component.type!='text' && component.type!='numberFlipper'">
              <el-input-number v-model.number="component.borderTop" style="width: 100%;" placeholder="上边距" @change="commonUtil.reLoadChart(chartsComponents,component)" :disabled="component.locked" />
            </el-form-item>
            <el-form-item label="下边距" class="df-form-item" v-if="component.isborder && component.type!='text' && component.type!='numberFlipper'">
              <el-input-number v-model.number="component.borderBottom" style="width: 100%;" placeholder="下边距" @change="commonUtil.reLoadChart(chartsComponents,component)" :disabled="component.locked" />
            </el-form-item>
            <el-form-item label="左边距" class="df-form-item" v-if="component.isborder && component.type!='text' && component.type!='numberFlipper'">
              <el-input-number v-model.number="component.borderLeft" style="width: 100%;" placeholder="左边距" @change="commonUtil.reLoadChart(chartsComponents,component)" :disabled="component.locked" />
            </el-form-item>
            <el-form-item label="右边距" class="df-form-item" v-if="component.isborder && component.type!='text' && component.type!='numberFlipper'">
              <el-input-number v-model.number="component.borderRight" style="width: 100%;" placeholder="右边距" @change="commonUtil.reLoadChart(chartsComponents,component)" :disabled="component.locked" />
            </el-form-item>
          </div>
        </el-form>
      <!-- </el-collapse-item>
    </el-collapse> -->
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
    }
  },
  mounted() {
  },
  methods: {
    // type 1 左 2上 3宽 4高
    plusClick(type) {
      if (this.component.locked) {
        return
      }
      if (type == '1') {
        this.component.x = this.component.x + 1
      } else if (type == '2') {
        this.component.y = this.component.y + 1
      } else if (type == '3') {
        this.component.w = this.component.w + 1
      } else if (type == '4') {
        this.component.h = this.component.h + 1
      }
    },
    minusClick(type) {
      if (this.component.locked) {
        return
      }
      if (type == '1') {
        if (this.component.x > 0) {
          this.component.x = this.component.x - 1
        }
      } else if (type == '2') {
        if (this.component.y > 0) {
          this.component.y = this.component.y - 1
        }
      } else if (type == '3') {
        if (this.component.w > 0) {
          this.component.w = this.component.w - 1
        }
      } else if (type == '4') {
        if (this.component.h > 0) {
          this.component.h = this.component.h - 1
        }
      }
    },
    changeColor(index, val) {
      this.$set(this.component.borderColor, index, val)
    },
    changeBorderBackgroundColor( val) {
      this.$set(this.component,"borderBackgroundColor",val)
    },
  }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:2px !important
}
::v-deep .el-form-item__label-wrap{
    margin-left:0px !important
}
::v-deep .customLabel{
    font-weight: bold;
}
::v-deep .customLabel .el-form-item__label{
    color:#15a585 !important;
}
</style>
