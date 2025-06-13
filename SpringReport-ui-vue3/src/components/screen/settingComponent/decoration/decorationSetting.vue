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
      <div
        v-if="
          component.name == 'dv-decoration-7' ||
          component.name == 'dv-decoration-9' ||
          component.name == 'dv-decoration-11'
        "
      >
        <el-form-item label="文字内容">
          <el-input v-model="component.content" style="width: 180px"> </el-input>
        </el-form-item>
        <el-form-item label="字体大小">
          <el-input v-model="component.style.fontSize" style="width: 180px"> </el-input>
        </el-form-item>
        <el-form-item label="字体颜色">
          <input-color-picker
            :inputWidth="152"
            :value="component.style.color"
            @change="
              (val) => {
                component.style.color = val;
              }
            "
          />
        </el-form-item>
        <el-form-item label="字体间距">
          <el-input v-model="component.style.letterSpacing"> </el-input>
        </el-form-item>
        <el-form-item label="是否加粗">
          <el-switch v-model="component.style.fontWeight" />
        </el-form-item>
      </div>
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
        this.$set(this.component.color, index, val);
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
