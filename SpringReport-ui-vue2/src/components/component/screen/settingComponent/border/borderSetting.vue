<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="边框设置"> -->
        <el-form ref="settingForm" class="demo-form-inline" :inline="true" :model="component" label-position="top" size="mini">
          <el-form-item label="颜色1">
            <input-color-picker :value="component.color[0]" @change="(val)=>{changeColor(0,val)}" />
          </el-form-item>
          <el-form-item label="颜色2">
            <input-color-picker :value="component.color[1]" @change="(val)=>{changeColor(1,val)}" />
          </el-form-item>
          <el-form-item label="背景色">
            <input-color-picker :value="component.backgroundColor" @change="(val)=>{component.backgroundColor=val}" />
          </el-form-item>
          <el-form-item v-if="component.name == 'dv-border-box-11'" label="标题内容">
            <el-input v-model="component.content" />
          </el-form-item>
          <el-form-item v-if="component.name == 'dv-border-box-11'" label="标题宽度">
            <el-input v-model.number="component.titleWidth" />
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
    changeColor(index, val) {
      this.$set(this.component.color, index, val)
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
