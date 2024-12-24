<!-- 搜索表单 -->
<template>
  <div class="search-content">
    <el-form ref="searchFormRef" :inline="inline" :model="searchData" @submit.native.prevent>
      <el-form-item
        v-for="item in searchForm"
        :key="item.prop"
        :class="itemClass"
        :label="item.label"
        :prop="item.prop"
        :rules="filter_rules(item.label,item.rules)"
      >
        <!-- 输入框 -->
        <el-input
          v-if="item.type==='Input'"
          v-model="searchData[item.prop]"
          :placeholder="'请输入'+item.label"
          :size="size||item.size"
          :disabled="item.disabled"
          @change="item.change && item.change(searchData[item.prop],item.params)"
          @input="item.input && item.input(searchData[item.prop])"
        />
        <!-- 下拉框 -->
        <el-select
          v-if="item.type==='Select'"
          v-model="searchData[item.prop]"
          :size="size||item.size"
          :disabled="item.disabled"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        >
          <el-option :label="'请选择'+item.label" value />
          <el-option
            v-for="op in item.options"
            :key="item.props?op[item.props.value]:op.value"
            :label="item.props?op[item.props.label]:op.label"
            :value="item.props?op[item.props.value]:op.value"
          />
        </el-select>
        <!-- 单选 -->
        <el-radio-group
          v-if="item.type==='Radio'"
          v-model="searchData[item.prop]"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        >
          <el-radio v-for="ra in item.radios" :key="ra.value" :label="ra.value">{{ ra.label }}</el-radio>
        </el-radio-group>
        <!-- 单选按钮 -->
        <el-radio-group
          v-if="item.type==='RadioButton'"
          v-model="searchData[item.prop]"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        >
          <el-radio-button v-for="ra in item.radios" :key="ra.value" :label="ra.value">{{ ra.label }}</el-radio-button>
        </el-radio-group>
        <!-- 复选框 -->
        <el-checkbox-group
          v-if="item.type==='Checkbox'"
          v-model="searchData[item.prop]"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        >
          <el-checkbox v-for="ch in item.checkboxs" :key="ch.value" :label="ch.value">{{ ch.label }}</el-checkbox>
        </el-checkbox-group>
        <!-- 日期 -->
        <el-date-picker
          v-if="item.type==='Date'"
          v-model="searchData[item.prop]"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 月份 -->
        <el-date-picker
          v-if="item.type==='Month'"
          v-model="searchData[item.prop]"
          format="yyyy-MM"
          value-format="yyyy-MM"
          type="month"
          placeholder="选择月"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 日期范围 -->
        <el-date-picker
          v-if="item.type==='DateRange'"
          v-model="searchData[item.prop]"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :disabled="item.disabled&& item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 时间 -->
        <el-time-picker
          v-if="item.type==='Time'"
          v-model="searchData[item.prop]"
          format="HH:mm:ss"
          value-format="HH:mm:ss"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
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
          :disabled="item.disabled&& item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 日期时间 -->
        <el-date-picker
          v-if="item.type==='DateTime'"
          v-model="searchData[item.prop]"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 日期范围 -->
        <el-date-picker
          v-if="item.type==='DateTimeRange'"
          v-model="searchData[item.prop]"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :disabled="item.disabled && item.disabled(modalData[item.prop])"
          @change="item.change && item.change(searchData[item.prop],item.params)"
        />
        <!-- 省市 -->
        <regionExc
          v-if="item.type==='RegionExc'"
          ref="cascaderRegionExc"
          v-model="searchData[item.prop]"
          :area-code.sync="searchData[item.prop]"
          :custom-style="'width:' + item.width"
          :disabled="item.disabled && item.disabled()"
        />
        <!-- 滑块 -->
        <!-- <el-slider v-if="item.type==='Slider'" v-model="searchData[item.prop]"></el-slider> -->
        <!-- 开关 -->
        <el-switch v-if="item.type==='Switch'" v-model="searchData[item.prop]" />
        <!-- 按钮-->
        <el-button
          v-if="item.type==='Button'"
          :type="item.style"
          @click="item.handle()"
        >{{ item.name }}</el-button>
      </el-form-item>

      <!-- <el-form-item v-for='item in searchHandle' :key="item.label">
                <el-button :type="item.type"  @click='item.handle()' v-has="item.auth">{{item.label}}</el-button>
      </el-form-item>-->
      <el-form-item>
        <el-button
          v-for="item in searchHandle"
          :key="item.label"
          v-has="item.auth"
          :type="item.type"
          :size="size || btn.size"
          @click="item.handle()"
        >{{ item.label }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import regionExcRegion from '../region/regionExcRegion.vue'
export default {
  name: 'SearchForm',
  components: { regionExcRegion },
  props: {
    inline: {
      type: Boolean,
      default: true
    },
    formRef: {
      type: String,
      default: 'queryData'
    },
    itemClass: {
      type: String,
      default: 'form_input'
    },
    isHandle: {
      type: Boolean,
      default: true
    },
    labelWidth: {
      type: String,
      default: '100px'
    },
    size: {
      type: String,
      default: 'small'
    },
    searchForm: {
      type: Array,
      default: () => []
    },
    searchHandle: {
      type: Array,
      default: () => []
    },
    searchData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {}
  }
}
</script>
<style lang="scss" scoped>
.search-content {
  padding: 24px 32px;

  border-radius: 6px;
  margin-bottom: 16px;
}
</style>
