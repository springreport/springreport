<!-- 数据设置组件 -->
<template>
  <div>
    <el-form class="demo-form-inline" :model="component" label-position="top" ref="settingForm">
      <el-form-item label="颜色1">
        <input-color-picker
          :inputWidth="152"
          :value="component.color[0]"
          @change="
            (val) => {
              changeColor(0, val);
            }
          "
        />
      </el-form-item>
      <el-form-item label="颜色2">
        <input-color-picker
          :inputWidth="152"
          :value="component.color[1]"
          @change="
            (val) => {
              changeColor(1, val);
            }
          "
        />
      </el-form-item>
      <el-form-item label="背景色">
        <input-color-picker
          :inputWidth="152"
          :value="component.backgroundColor"
          @change="
            (val) => {
              component.backgroundColor = val;
            }
          "
        />
      </el-form-item>
      <el-form-item label="标题内容" v-if="component.name == 'dv-border-box-11'">
        <el-input v-model="component.content"></el-input>
      </el-form-item>
      <el-form-item label="标题宽度" v-if="component.name == 'dv-border-box-11'">
        <el-input v-model.number="component.titleWidth"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import InputColorPicker from '../../colorpicker/inputColorPicker.vue';
  export default {
    components: {
      InputColorPicker,
    },
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
    mounted() {},
    data() {
      return {};
    },
    methods: {
      changeColor(index, val) {
        this.component.color[index] = val;
        // this.$set(this.component.color,index,val);
      },
    },
  };
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 2px !important;
  }
  :deep(.el-form-item__label-wrap) {
    margin-left: 0px !important;
  }
</style>
