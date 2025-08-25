<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item :title="component.type != 'date'?'文本设置':'日期设置'"> -->
        <el-form ref="settingForm" class="demo-form-inline" :model="component" label-position="top" size="mini">
          <el-form-item label="字体大小">
            <el-input v-model="component.style.fontSize" />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker :input-width="152" :value="component.style.fontColor" @change="(val)=>{component.style.fontColor=val;changeFontColor(1)}" />
          </el-form-item>
          <el-form-item label="背景色">
            <input-color-picker :input-width="152" :value="component.style.background" @change="(val)=>{component.style.background=val;changeBackgroundColor(1)}" />
          </el-form-item>
          <el-form-item label="翻牌器高度">
            <el-input v-model="component.style.boxHeight" />
          </el-form-item>
          <el-form-item label="翻牌器宽度">
            <el-input v-model="component.style.boxWidth" />
          </el-form-item>
          <el-form-item label="动画持续时间（毫秒）">
            <el-input v-model="component.style.duration" />
          </el-form-item>
          <el-form-item label="前缀">
            <el-input v-model="component.style.prefix" />
          </el-form-item>
          <el-form-item label="前缀字体颜色">
            <input-color-picker :input-width="152" :value="component.style.prefixColor" @change="(val)=>{component.style.prefixColor=val}" />
          </el-form-item>
          <el-form-item label="前缀字体大小">
            <el-input v-model="component.style.prefixFontSize" />
          </el-form-item>
          <el-form-item label="后缀">
            <el-input v-model="component.style.suffix" />
          </el-form-item>
           <el-form-item label="后缀字体颜色">
             <input-color-picker :input-width="152" :value="component.style.suffixColor" @change="(val)=>{component.style.suffixColor=val}" />
          </el-form-item>
          <el-form-item label="后缀字体大小">
            <el-input v-model="component.style.suffixFontSize" />
          </el-form-item>
          <el-form-item label="开启千分位" class="df-form-item">
            <el-switch v-model="component.style.thousandSeparator" active-text="是" inactive-text="否" />
          </el-form-item>
          <el-form-item label="小数位数">
            <el-input v-model.number="component.style.decimalPlaces" />
          </el-form-item>
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
    changeBackgroundColor(type) {
      if (type == 1) {
        if (this.component.style.background == null) {
          this.component.style.background = 'rgba(128, 128, 128, 0.0)'
        }
      }
    },
    changeFontColor(type) {
      if (type == 1) {
        if (this.component.style.fontColor == null) {
          this.component.style.fontColor = '#ffffff'
        }
      }
    }
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
</style>
