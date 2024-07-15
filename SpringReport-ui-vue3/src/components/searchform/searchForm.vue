<!-- 搜索表单 -->
<template>
  <div class="search-content">
    <el-form
      :inline="inline"
      :label-width="labelWidth"
      ref="searchFormRef"
      class="demo-form-inline"
      :model="searchData"
    >
      <el-form-item
        v-for="item in searchForm"
        :class="itemClass"
        :label="item.label"
        :key="item.prop"
        :prop="item.prop"
        :rules="filter_rules(item.label,item.rules)"
      >
        <!-- 输入框 -->
        <el-input
          v-if="item.type==='Input'"
          v-model="searchData[item.prop]"
          :placeholder="'请输入'+item.label"
          :size="item.size"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          @input="item.input && item.input(searchData[item.prop])"
          :disabled="item.disabled"
        ></el-input>
        <!-- 下拉框 -->
        <el-select
          v-if="item.type==='Select'"
          v-model="searchData[item.prop]"
          :size="item.size"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled"
        >
          <el-option :label="'请选择'+item.label" value></el-option>
          <el-option
            v-for="op in item.options"
            :label="item.props?op[item.props.label]:op.label"
            :value="item.props?op[item.props.value]:op.value"
            :key="item.props?op[item.props.value]:op.value"
          ></el-option>
        </el-select>
        <!-- 单选 -->
        <el-radio-group
          v-if="item.type==='Radio'"
          v-model="searchData[item.prop]"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        >
          <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio>
        </el-radio-group>
        <!-- 单选按钮 -->
        <el-radio-group
          v-if="item.type==='RadioButton'"
          v-model="searchData[item.prop]"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        >
          <el-radio-button v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{ra.label}}</el-radio-button>
        </el-radio-group>
        <!-- 复选框 -->
        <el-checkbox-group
          v-if="item.type==='Checkbox'"
          v-model="searchData[item.prop]"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        >
          <el-checkbox v-for="ch in item.checkboxs" :label="ch.value" :key="ch.value">{{ch.label}}</el-checkbox>
        </el-checkbox-group>
        <!-- 日期 -->
        <el-date-picker
          v-if="item.type==='Date'"
          v-model="searchData[item.prop]"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        ></el-date-picker>
        <!-- 月份 -->
        <el-date-picker
          v-if="item.type==='Month'"
          format="yyyy-MM"
          value-format="yyyy-MM"
          v-model="searchData[item.prop]"
          type="month"
          placeholder="选择月"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        ></el-date-picker>
        <!-- 日期范围 -->
        <el-date-picker
          v-if="item.type==='DateRange'"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          v-model="searchData[item.prop]"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled&& item.disabled(modalData[item.prop])"
        ></el-date-picker>
        <!-- 时间 -->
        <el-time-picker
          v-if="item.type==='Time'"
          v-model="searchData[item.prop]"
          format="HH:mm:ss"
          value-format="HH:mm:ss"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        ></el-time-picker>
        <!-- 时间范围 -->
        <el-time-picker
          v-if="item.type==='TimeRange'"
          v-model="searchData[item.prop]"
          is-range
          format="HH:mm:ss"
          value-format="HH:mm:ss"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled&& item.disabled(modalData[item.prop])"
        ></el-time-picker>
        <!-- 日期时间 -->
        <el-date-picker
          v-if="item.type==='DateTime'"
          type="datetime"
          v-model="searchData[item.prop]"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
        ></el-date-picker>
        <!-- 日期范围 -->
        <el-date-picker
          v-if="item.type==='DateTimeRange'"
          type="datetimerange"
          v-model="searchData[item.prop]"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        ></el-date-picker>
        <!-- 省市 -->
        <!-- 滑块 -->
        <!-- <el-slider v-if="item.type==='Slider'" v-model="searchData[item.prop]"></el-slider> -->
        <!-- 开关 -->
        <el-switch v-if="item.type==='Switch'" v-model="searchData[item.prop]"></el-switch>
        <!-- 按钮-->
        <el-button
          v-if="item.type==='Button'"
          :type="item.style"
          @click="item.handle()"
        >{{item.name}}</el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          v-for="item in searchHandle"
          :key="item.label"
          :type="item.type"
          @click="item.handle()"
          v-has="item.auth"
          :size="size || btn.size"
        >{{item.label}}</el-button>
      </el-form-item>
      <!-- <div :style="{'text-align': inline ? 'center' : 'inherit' }">
        <el-form-item v-for='item in searchHandle' :key="item.label">
                <el-button :type="item.type"  @click='item.handle()' v-has="item.auth">{{item.label}}</el-button>
        </el-form-item>
      </div>-->
    </el-form>
  </div>
</template>

<script>
// import regionExcRegion from '../region/regionExcRegion.vue';
export default {
  //   components: { regionExcRegion },
  props: {
    inline: {
      type: Boolean,
      default: true,
    },
    formRef: {
      type: String,
      default: 'queryData',
    },
    itemClass: {
      type: String,
      default: 'form_input',
    },
    isHandle: {
      type: Boolean,
      default: true,
    },
    labelWidth: {
      type: String,
      default: '100px',
    },
    size: {
      type: String,
      default: 'default',
    },
    searchForm: {
      type: Array,
      default: () => [],
    },
    searchHandle: {
      type: Array,
      default: () => [],
    },
    searchData: {
      type: Object,
      default: () => ({}),
    },
  },
  name: 'searchForm',
  data() {
    return {};
  },
};
</script>
<style lang="scss" scoped>
.search-content {
  padding: 24px 32px;
  background: #ffffff;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  margin-bottom: 16px;
}
</style>
