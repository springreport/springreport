<!-- 搜索表单 -->
<template>
  <div>
    <div class="search-content df-c-b">
      <div class="left-warp df-c">
        <!-- 查询显示与否根据searchForm -->
        <div v-if="searchForm.length" class="action-item df-c" @click="drawer = true">
          <icon-search fill="#666" style="margin-right: 4px" />
          <span>查询</span>
          <!-- conditionNum这块不点击查询，会有视觉上的bug，点击清除条件 -->
          <div v-show="conditionNum" class="condition df-c" @click.stop="clearCondition">
            <span style="margin-right: 4px">{{ conditionNum }}</span>
            <icon-close fill="#959ea6" />
          </div>
        </div>
        <div
          v-for="(item, index) in leftTableHandles"
          :key="index"
          class="action-item df-c"
          :class="{ 'action-item-del': item.iconClass == 'action-icon-del' }"
          v-has="item.auth"
          @click="item.handle()"
        >
          <i v-if="item.iconClass" :class="item.iconClass" />
          <span>{{ item.label }}</span>
        </div>
      </div>

      <div class="right-warp df-c">
        <template v-for="item in rightTableHandles">
          <div
            v-if="item.iconClass == 'action-icon-template'"
            :key="item.label"
            class="tem df-c"
            @click="item.handle()"
          >
            <img
              src="@/assets/img/template/tem.png"
              style="width: 24px; height: 24px; display: block"
            />
            <div class="tem-name">模板市场</div>
          </div>
          <el-button
            v-else
            :key="item.label"
            v-has="item.auth"
            :type="item.type"
            size="small"
            @click="item.handle()"
          >
            <div class="df-c">
              <span
                v-if="item.iconClass"
                :class="item.iconClass"
                style="width: 12px; height: 12px; margin-right: 4px"
              />
              <span style="height: 12px">{{ item.label }}</span>
            </div>
          </el-button>
        </template>
      </div>
    </div>

    <el-drawer
      v-if="searchForm.length"
      title="添加筛选项"
      v-model="drawer"
      :close-on-press-escape="false"
      direction="rtl"
      size="36%"
      custom-class="search-drawer"
      class="search-drawer"
    >
      <el-form
        :inline="inline"
        ref="searchFormRef"
        class="demo-form-inline"
        label-position="top"
        :model="searchData"
      >
        <el-form-item
          v-for="item in searchForm"
          :class="itemClass"
          :label="item.label"
          :key="item.prop"
          :prop="item.prop"
          :rules="filter_rules(item.label, item.rules)"
        >
          <!-- 输入框 -->
          <el-input
            v-if="item.type === 'Input'"
            v-model="searchData[item.prop]"
            :placeholder="'请输入' + item.label"
            :size="item.size"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            @input="item.input && item.input(searchData[item.prop])"
            :disabled="item.disabled"
          ></el-input>
          <!-- 下拉框 -->
          <el-select
            style="width: 100%"
            v-if="item.type === 'Select'"
            v-model="searchData[item.prop]"
            :size="item.size"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled"
          >
            <el-option :label="'请选择' + item.label" value></el-option>
            <el-option
              v-for="op in item.options"
              :label="item.props ? op[item.props.label] : op.label"
              :value="item.props ? op[item.props.value] : op.value"
              :key="item.props ? op[item.props.value] : op.value"
            ></el-option>
          </el-select>
          <!-- 单选 -->
          <el-radio-group
            v-if="item.type === 'Radio'"
            v-model="searchData[item.prop]"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          >
            <el-radio v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{
              ra.label
            }}</el-radio>
          </el-radio-group>
          <!-- 单选按钮 -->
          <el-radio-group
            v-if="item.type === 'RadioButton'"
            v-model="searchData[item.prop]"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          >
            <el-radio-button v-for="ra in item.radios" :label="ra.value" :key="ra.value">{{
              ra.label
            }}</el-radio-button>
          </el-radio-group>
          <!-- 复选框 -->
          <el-checkbox-group
            v-if="item.type === 'Checkbox'"
            v-model="searchData[item.prop]"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          >
            <el-checkbox v-for="ch in item.checkboxs" :label="ch.value" :key="ch.value">{{
              ch.label
            }}</el-checkbox>
          </el-checkbox-group>
          <!-- 日期 -->
          <el-date-picker
            v-if="item.type === 'Date'"
            v-model="searchData[item.prop]"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          ></el-date-picker>
          <!-- 月份 -->
          <el-date-picker
            v-if="item.type === 'Month'"
            format="yyyy-MM"
            value-format="yyyy-MM"
            v-model="searchData[item.prop]"
            type="month"
            placeholder="选择月"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          ></el-date-picker>
          <!-- 日期范围 -->
          <el-date-picker
            v-if="item.type === 'DateRange'"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            v-model="searchData[item.prop]"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          ></el-date-picker>
          <!-- 时间 -->
          <el-time-picker
            v-if="item.type === 'Time'"
            v-model="searchData[item.prop]"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            @change="item.change && item.change(searchData[item.prop], item.params)"
          ></el-time-picker>
          <!-- 时间范围 -->
          <el-time-picker
            v-if="item.type === 'TimeRange'"
            v-model="searchData[item.prop]"
            is-range
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          ></el-time-picker>
          <!-- 日期时间 -->
          <el-date-picker
            v-if="item.type === 'DateTime'"
            type="datetime"
            v-model="searchData[item.prop]"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            @change="item.change && item.change(searchData[item.prop], item.params)"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
          ></el-date-picker>
          <!-- 日期范围 -->
          <el-date-picker
            v-if="item.type === 'DateTimeRange'"
            type="datetimerange"
            v-model="searchData[item.prop]"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :disabled="item.disabled && item.disabled(modalData[item.prop])"
            @change="item.change && item.change(searchData[item.prop], item.params)"
          ></el-date-picker>
          <!-- 省市 -->
          <!-- 滑块 -->
          <!-- <el-slider v-if="item.type==='Slider'" v-model="searchData[item.prop]"></el-slider> -->
          <!-- 开关 -->
          <el-switch v-if="item.type === 'Switch'" v-model="searchData[item.prop]"></el-switch>
          <!-- 按钮-->
          <el-button v-if="item.type === 'Button'" :type="item.style" @click="item.handle()">{{
            item.name
          }}</el-button>
        </el-form-item>
        <el-form-item> </el-form-item>
      </el-form>
      <!-- <template #footer></template> -->
      <div class="search-drawer__footer">
        <el-button
          v-for="item in searchHandle"
          :key="item.label"
          :type="item.type"
          @click="handleAction(item)"
          v-has="item.auth"
          :size="size || btn.size"
          >{{ item.label }}</el-button
        >
      </div>
    </el-drawer>
  </div>
</template>

<script>
  // import regionExcRegion from '../region/regionExcRegion.vue';
  export default {
    //   components: { regionExcRegion },
    props: {
      inline: {
        type: Boolean,
        default: false,
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
      // 表格操作按钮
      tableHandles: {
        type: Array,
        default: () => [],
      },
      searchData: {
        type: Object,
        default: () => ({}),
      },
    },
    computed: {
      // 查询条件的数量
      conditionNum() {
        let count = 0;
        for (const key in this.searchData) {
          if (this.searchData.hasOwnProperty(key)) {
            const value = this.searchData[key];
            // 检查值是否为 null 或者是空字符串
            if (value !== null && value !== '') {
              // 如果值是其他类型的非空值，比如数字或布尔值，也视为有效
              if (
                typeof value === 'number' ||
                typeof value === 'boolean' ||
                (typeof value === 'string' && value.trim() !== '')
              ) {
                count++;
              }
            }
          }
        }
        return count;
      },
      // 左侧操作 删除、刷新类
      leftTableHandles() {
        return this.tableHandles.filter((item) => item.position !== 'right');
      },
      // 右侧操作 一般是添加类
      rightTableHandles() {
        return this.tableHandles.filter((item) => item.position === 'right');
      },
    },
    name: 'searchForm',
    data() {
      return {
        drawer: false,
      };
    },
    methods: {
      handleAction(item) {
        this.drawer = false;
        item.handle();
      },
      // 清除条件
      clearCondition() {
        this.searchHandle.forEach((item) => {
          if (item.label.includes('重置') || item.label.includes('清除条件')) {
            item.handle();
          }
        });
      },
    },
  };
</script>
<style lang="scss" scoped>
  .search-content {
    height: 32px;
    line-height: 32px;
    margin-bottom: 16px;

    .left-warp {
      .action-item {
        height: 32px;
        line-height: 32px;
        padding: 0 16px;
        color: #595959;
        font-size: 14px;
        font-weight: 400;
        transition: all 0.3s;

        &:hover {
          cursor: pointer;
          border-radius: 4px;
          background: #fff;
        }

        .condition {
          margin-left: 8px;
          height: 20px;
          line-height: 20px;
          padding: 0 4px 0 8px;
          border-radius: 4px;
          background: rgba(23, 183, 148, 0.05);
          font-size: 12px;
          color: $base-color-primary;
        }
      }

      .action-item-del {
        color: #ff4d4f;
      }
    }

    .right-warp {
      :deep(.el-button--small) {
        font-size: 12px !important;
        padding: 13px 15px !important;
      }
    }
  }
  .tem {
    cursor: pointer;
    height: 28px;
    line-height: 28px;
    margin-right: 16px;
    .tem-name {
      margin-left: 6px;
      color: $base-color-primary;
      font-family: 'PingFang SC';
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: normal;
    }
  }
</style>
