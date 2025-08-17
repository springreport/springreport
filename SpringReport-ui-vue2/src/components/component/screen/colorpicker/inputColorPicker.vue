<!--
 * @Author: liren
 * @Date: 2022-10-08 17:23:59
 * @LastEditTime: 2022-10-08 17:53:26
 * @Description: 输入框颜色选择器
-->
<template>
  <div class="public-input-color-picker">
    <el-input :style="{width:inputWidth+'px'}" v-model="value2" :placeholder="placeholder" @input="handleChange" />
    <el-color-picker
      v-model="value2"
      :show-alpha="showAlpha"
      :predefine="predefineColors"
      @change="handleChange"
    />
  </div>
</template>
<script>
/**
 * 输入框颜色选择器
 * 预定义颜色中，如果颜色不对，将无法选中
 *
 * @showAlpha 是否支持透明度 默认false
 * @predefineColors 预定义颜色
 * @value 默认颜色编码 示例：#ffffff
 * @placeholder 输入框提示文字
 */
export default {
  name: 'InputColorPicker',
  components: {},
  props: {
    showAlpha: { type: Boolean, default: true },
    predefineColors: {
      type: Array,
      default: () => {
        return [
          '#000000',
          '#ff4500',
          '#ff8c00',
          '#ffd700',
          '#90ee90',
          '#00ced1',
          '#1e90ff',
          '#c71585',
          '#FF0000',
          '#1CE611'
        ]
      }
    },
    value: { type: String, default: '' },
    placeholder: { type: String, default: '请输入颜色编码' },
    inputWidth: { type: Number, default: 175 },
  },
  data() {
    return {
      value2: ''
    }
  },
  computed: {},
  watch: {
    value: {
      handler: function(newVal, oldVal) {
        this.value2 = JSON.parse(JSON.stringify(newVal))
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleChange(val) {
      if (val) {
        this.$emit('change', val)
      }else{
        this.$emit('change', null)
      }
    }
  }
}
</script>
<style lang="less" scoped>
// @box-width: 175px;
.public-input-color-picker {
  // display: flex;
  // -webkit-box-align: center;
  // -webkit-align-items: center;
  // align-items: center;
  position: relative;
  // width: @box-width;
  .el-input {
    // width: @box-width;
  }
  .el-color-picker {
    position: absolute;
    right: 0;
    top: 0;
    ::v-deep .el-color-picker__trigger {
      border: 0;
      margin-top: 0;
    }
  }
}
</style>

