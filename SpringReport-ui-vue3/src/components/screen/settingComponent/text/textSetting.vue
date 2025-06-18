<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
        <el-collapse-item :title="component.type != 'date'?'文本设置':'日期设置'"> -->
    <el-form
      ref="settingForm"
      class="demo-form-inline"
      :model="component"
      label-position="top"
      size="small"
    >
      <el-form-item v-if="component.type != 'date'" label="文本类型" prop="textType">
        <el-select v-model="component.textType" placeholder="请选择">
          <el-option
            v-for="item in selectUtil.textType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="component.type == 'date'" label="日期格式">
        <el-select v-model="component.format" placeholder="日期格式">
          <el-option
            v-for="item in selectUtil.dateFormat"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="字体大小">
        <el-input v-model="component.style.fontSize" />
      </el-form-item>
      <el-form-item label="字体起始颜色">
        <input-color-picker
          :input-width="152"
          :value="component.style.color"
          @change="
            (val) => {
              component.style.color = val;
              changeFontColor(1);
            }
          "
        />
      </el-form-item>
      <el-form-item label="字体结束颜色">
        <input-color-picker
          :input-width="152"
          :value="component.style.colorEnd"
          @change="
            (val) => {
              component.style.colorEnd = val;
              changeFontColor(2);
            }
          "
        />
      </el-form-item>
      <el-form-item label="渐变方向">
        <el-select v-model="component.style.direction" placeholder="请选择">
          <el-option
            v-for="item in selectUtil.lineargardient"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="起始背景色">
        <input-color-picker
          :input-width="152"
          :value="component.style.background"
          @change="
            (val) => {
              component.style.background = val;
              changeBackgroundColor(1);
            }
          "
        />
      </el-form-item>
      <el-form-item label="结束背景色">
        <input-color-picker
          :input-width="152"
          :value="component.style.backgroundEnd"
          @change="
            (val) => {
              component.style.backgroundEnd = val;
              changeBackgroundColor(2);
            }
          "
        />
      </el-form-item>
      <el-form-item label="渐变方向">
        <el-select v-model="component.style.backgroundDirection" placeholder="请选择">
          <el-option
            v-for="item in selectUtil.lineargardient"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="字体间距">
        <el-input v-model="component.style.letterSpacing" />
      </el-form-item>
      <el-form-item label="是否加粗" class="df-form-item">
        <el-switch v-model="component.style.fontWeight" active-text="是" inactive-text="否" />
      </el-form-item>
      <el-form-item
        v-if="component.textType == 'text' || component.textType == 'link'"
        label="水平对齐"
      >
        <el-select v-model="component.style.textAlign" placeholder="请选择">
          <el-option
            v-for="item in selectUtil.textAlign"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="component.textType == 'text' || component.textType == 'link'"
        label="垂直对齐"
      >
        <el-select v-model="component.style.verticalAlign" placeholder="请选择">
          <el-option
            v-for="item in selectUtil.verticalAlign"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="component.textType == 'marquee'" label="滚动速度">
        <el-input v-model.number="component.speed" />
      </el-form-item>
      <el-form-item v-if="component.textType == 'link'" label="链接地址">
        <el-input v-model="component.href" type="textarea" />
      </el-form-item>
    </el-form>
    <!-- </el-collapse-item>
      </el-collapse> -->
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
      changeBackgroundColor(type) {
        if (type == 1) {
          if (this.component.style.background == null) {
            this.component.style.background = 'rgba(128, 128, 128, 0.0)';
          }
        } else {
          if (this.component.style.backgroundEnd == null) {
            this.component.style.backgroundEnd = 'rgba(128, 128, 128, 0.0)';
          }
        }
      },
      changeFontColor(type) {
        if (type == 1) {
          if (this.component.style.color == null) {
            this.component.style.color = '#ffffff';
          }
        } else {
          if (this.component.style.colorEnd == null) {
            this.component.style.colorEnd = '#ffffff';
          }
        }
      },
    },
  };
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 2px !important;
  }
  ::v-deep .el-form-item__label-wrap {
    margin-left: 0px !important;
  }
</style>
