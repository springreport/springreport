<template>
  <div>
    <!-- :width="modalConfig.width" -->
    <el-drawer
      :title="modalConfig.title"
      :visible.sync="modalConfig.show"
      :close-on-press-escape="false"
      :wrapper-closable="false"
      size="36%"
      append-to-body
      custom-class="handle-drawer"
      class="handle-drawer"
      @close="closeModal"
    >
      <div class="el-dialog-div">
        <el-form
          ref="modalFormRef"
          label-position="top"
          class="demo-form-inline"
          :model="modalData"
          :disabled="modalConfig.formEditDisabled"
          autocomplete="modalConfig.autocomplete"
          :label-width="labelWidth"
        >
          <el-form-item
            v-for="item in modalForm"
            v-show="item.show == undefined ? true : item.show"
            :key="item.prop"
            :class="itemClass"
            :label="item.label"
            :prop="item.prop"
            :rules="filter_rules(item.label, item.rules)"
            :label-width="item.labelWidth"
          >
            <!-- 输入框 -->
            <el-input
              v-if="item.type === 'Input'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              :placeholder="'请输入' + item.label"
              size="item.size"
              :clearable="item.clearable"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              @clear="item.clear && item.clear(modalData[item.prop])"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
              @input="item.input && item.input(modalData[item.prop])"
              @focus="item.focus && item.focus(modalData[item.prop])"
            />
            <el-input
              v-if="item.type === 'Password'"
              v-model="modalData[item.prop]"
              type="password"
              :show-password="false"
              :style="'width:' + item.width"
              :placeholder="'请输入' + item.label"
              size="item.size"
              :clearable="item.clearable"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              @clear="item.clear && item.clear(modalData[item.prop])"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
              @input="item.input && item.input(modalData[item.prop])"
              @focus="item.focus && item.focus(modalData[item.prop])"
            />
            <el-col
              v-if="item.type === 'Line'"
              class="line"
              :span="2"
            >-</el-col>
            <el-input
              v-if="item.type === 'Input2'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              :placeholder="'请输入' + item.title"
              size="item.size"
              :clearable="item.clearable"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              @clear="item.clear && item.clear(modalData[item.prop])"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
              @input="item.input && item.input(modalData[item.prop])"
              @focus="item.focus && item.focus(modalData[item.prop])"
            />
            <cron
              v-if="item.cron"
              i18n="cn"
              @change="item.changeCron"
              @close="item.cron = false"
            />
            <!-- 下拉框 -->
            <!-- :style="'width:' + item.width" -->
            <el-select
              v-if="item.type === 'Select'"
              :ref="item.ref"
              v-model="modalData[item.prop]"
              style="width: 100% !important"
              :multiple="item.multiple"
              :filterable="item.remote"
              :remote="item.remote"
              :remote-method="item.remoteMethod"
              size="item.size"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
              @focus="
                item.focus && item.focus(modalData[item.prop], item.params)
              "
            >
              <el-option
                v-if="!item.multiple"
                :label="'请选择' + item.label"
                value=""
              />
              <el-option
                v-for="op in item.options"
                :key="item.props ? op[item.props.value] : op.value"
                :label="item.props ? op[item.props.label] : op.label"
                :value="item.props ? op[item.props.value] : op.value"
              />
            </el-select>
            <!-- 单选 -->
            <el-radio-group
              v-if="item.type === 'Radio'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              :disabled="item.disabled"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            >
              <el-radio
                v-for="ra in item.radios"
                :key="ra.value"
                :label="ra.value"
              >{{ ra.label }}</el-radio>
            </el-radio-group>
            <!-- 单选按钮 -->
            <el-radio-group
              v-if="item.type === 'RadioButton'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            >
              <el-radio-button
                v-for="ra in item.radios"
                :key="ra.value"
                :label="ra.value"
              >{{ ra.label }}</el-radio-button>
            </el-radio-group>
            <!-- 复选框 -->
            <el-checkbox-group
              v-if="item.type === 'Checkbox'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            >
              <el-checkbox
                v-for="ch in item.checkboxs"
                :key="ch.value"
                :label="ch.value"
              >{{ ch.label }}</el-checkbox>
            </el-checkbox-group>
            <!-- 日期 -->
            <el-date-picker
              v-if="item.type === 'Date'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- 日期范围 -->
            <el-date-picker
              v-if="item.type === 'DateRange'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- 时间 -->
            <el-time-picker
              v-if="item.type === 'Time'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              format="HH:mm:ss"
              value-format="HH:mm:ss"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- 时间范围 -->
            <el-time-picker
              v-if="item.type === 'TimeRange'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              is-range
              format="HH:mm:ss"
              value-format="HH:mm:ss"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- 日期时间 -->
            <el-date-picker
              v-if="item.type === 'DateTime'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="item.disable && item.disabled(modalData[item.prop])"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- 日期范围 -->
            <el-date-picker
              v-if="item.type === 'DateTimeRange'"
              v-model="modalData[item.prop]"
              :style="'width:' + item.width"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              :default-time="item.defaultTime"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
            />
            <!-- textarea -->
            <!-- :style="'width:' + item.width" -->
            <el-input
              v-if="item.type === 'Textarea'"
              v-model="modalData[item.prop]"
              type="textarea"
              style="width: 100%"
              :rows="item.rows ? item.rows : 2"
              :placeholder="'请输入' + item.label"
              size="item.size"
              :disabled="item.disabled && item.disabled(modalData[item.prop])"
              :label-width="item.labelWidth"
              @change="
                item.change && item.change(modalData[item.prop], item.params)
              "
              @input="item.input && item.input(modalData[item.prop])"
              @focus="item.focus && item.focus(modalData[item.prop])"
            />
            <el-button
              v-if="item.type === 'Button'"
              type="text"
              @click="item.click"
            >{{ item.buttonText }}</el-button>
            <!-- 省市区 -->
            <region
              v-if="item.type === 'Region'"
              ref="cascaderRegion"
              v-model="modalData[item.prop]"
              :area-code.sync="modalData[item.prop]"
              :custom-style="'width:' + item.width"
              :disabled="item.disabled && item.disabled()"
            />
            <!-- 文件上传 -->
            <fileUpload
              v-if="item.type === 'Upload'"
              :style="'width:' + item.width"
              :file-list.sync="modalData[item.prop]"
              :accept="item.accept"
              :tips="item.tips"
              :limit="item.limit"
              :multiple="item.multiple"
              :readonly="item.readonly"
              :filesize="item.filesize"
              :uploadUrl="item.uploadUrl"
              :listType="item.listType"
            />
            <cusTable
              v-if="item.type === 'Table'"
              :is-index="item.isIndex"
              :is-pagination="item.isPagination"
              :table-cols="item.tableCols"
              :table-handles="item.tableHandles"
              :table-data="item.tableData"
              :table-page="item.tablePage"
            />
            <div class="sub-title">
              <a target="_blank" :href="item.suggestionLink">{{
                item.suggestions
              }}</a>
            </div>

            <!-- 树 -->
            <el-tree
              v-if="item.type === 'Tree'"
              :ref="item.ref"
              :data="item.data"
              show-checkbox
              :default-expand-all="defaultexpandall"
              :node-key="item.key"
              :default-checked-keys="item.checked"
              highlight-current
              :props="item.props"
              :style="'width:' + item.width"
            />
            <!-- treeselect -->
            <selectNode
              v-if="item.type === 'TreeSelect'"
              :ref="item.ref"
              v-model="modalData[item.prop]"
              :props="item.props"
              :value-id="modalData[item.valueProp]"
              :options="item.data"
              :lazy="item.lazy"
              :url="item.url"
              :clearable="item.clearable"
            />
            <multiselectNode
              v-if="item.type === 'MultiTreeSelect'"
              :ref="item.ref"
              v-model="modalData[item.prop]"
              :props="item.props"
              :options="item.data"
              :lazy="item.lazy"
              :url="item.url"
            />
            <el-divider v-if="item.divider" />
          </el-form-item>
        </el-form>
      </div>
      <div class="handle-drawer__footer">
        <el-button
          v-for="item in modalHandles"
          v-show="modalConfig.type != commonConstants.modalType.detail"
          :key="item.label"
          :type="item.type"
          :icon="item.icon"
          :disabled="modalConfig.disabled && item.disabled()"
          @click="item.handle()"
        >{{ item.label }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { cron } from 'vue-cron'
export default {
  name: 'ModalPage',
  components: { cron },
  props: {
    labelWidth: {
      type: String,
      default: 'auto'
    },
    itemClass: {
      type: String,
      default: 'form_input'
    },
    modalConfig: {
      type: Object,
      default: () => ({
        title: '新增',
        show: false,
        width: '30%',
        formEditDisabled: true,
        autocomplete: false
      })
    },
    modalFormRef: {
      type: String,
      default: 'modalFormRef'
    },
    modalData: {
      type: Object,
      default: () => ({})
    },
    modalHandles: {
      type: Array,
      default: () => []
    },
    modalForm: {
      type: Array,
      default: () => []
    },
    defaultexpandall: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {}
  },
  methods: {
    closeModal() {
      this.$emit('closeModal')
    }
  }
}
</script>
<style lang="scss" scoped>
.el-dialog-div {
  max-height: calc(100vh - 120px);
  overflow: auto;
  width: calc(100% + 8px);
  padding-right: 8px;
  margin-right: -8px;
  &::-webkit-scrollbar {
    height: 6px;
    width: 6px;
    overflow: visible;
  }

  &::-webkit-scrollbar-button {
    width: 0;
    height: 0;
  }

  &::-webkit-scrollbar-corner {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #ddd;
    background-clip: padding-box;
    border: 4px solid #f2f4f7;
    border-radius: 8px;
    min-height: 24px;
  }
}
::v-deep .ces-table {
  height: 25vh !important;
}
</style>
