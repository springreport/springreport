<!-- 数据设置组件 -->
<template>
  <div>
    <el-form
      ref="settingForm"
      class="demo-form-inline"
      :model="component"
      label-position="top"
      size="small"
    >
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">数据列设置</span>
        <el-button class="addBtn" @click="showAddTableColumn('1')"
          ><i class="el-icon-plus el-icon--left" />添加</el-button
        >
      </div>

      <el-collapse
        v-if="component.tableColumn && component.tableColumn.length > 0"
        class="sub-collapse"
      >
        <el-collapse-item v-for="(item, index) in component.tableColumn" :key="index">
          <template v-slot:title>
            {{ item.name }}
            <div
              class="right-block-el-icon-edit"
              @click.stop="showAddTableColumn('2', item, index)"
            />
            <div class="right-el-icon-delete" @click.stop="deleteTableColumn(index)" />
          </template>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            列名称：{{ item.name }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            列属性：{{ item.key }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            列宽：{{ item.style.width }}%
          </p>
        </el-collapse-item>
      </el-collapse>

      <!-- <el-table :data="component.tableColumn" border style="width: 100%" size="small" :cell-style="{color:'#fff'}">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="key" label="属性" />
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="showAddTableColumn('2',scope.row,scope.$index)">编辑</el-button>
                <el-button type="text" size="small" @click="deleteTableColumn(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table> -->
      <div v-if="component.type == 'scrollTable'">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表格样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="显示序号" class="df-form-item">
            <el-switch
              v-model="component.style.showIndex"
              active-text="是"
              inactive-text="否"
              @change="reloadTable(component)"
            />
          </el-form-item>
          <el-form-item v-if="component.style.showIndex" label="序号列宽度">
            <el-input
              v-model="component.style.indexWidth"
              style="width: 100%"
              @change="reloadTable(component)"
            >
              <template v-slot:append>%</template>
            </el-input>
          </el-form-item>
          <el-form-item label="显示边框" class="df-form-item">
            <el-switch
              v-model="component.style.isBorder"
              active-text="是"
              inactive-text="否"
              @change="reloadTable(component)"
            />
          </el-form-item>
          <el-form-item v-if="component.style.isBorder" label="边框宽度">
            <el-input
              v-model="component.style.borderWidth"
              style="width: 100%"
              @change="reloadTable(component)"
            >
              <template v-slot:append>px</template>
            </el-input>
          </el-form-item>
          <el-form-item v-if="component.style.isBorder" label="边框颜色">
            <input-color-picker
              :value="component.style.borderColor"
              @change="
                (val) => {
                  component.style.borderColor = val;
                  reloadTable(component);
                }
              "
            />
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表头样式</span>
        </div>

        <div class="right-dataset-warp">
          <el-form-item label="高度">
            <el-input v-model="component.headStyle.height" style="width: 100%">
              <template v-slot:append>px</template>
            </el-input>
          </el-form-item>
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.headStyle.backgroundColor"
              @change="
                (val) => {
                  component.headStyle.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker
              :value="component.headStyle.color"
              @change="
                (val) => {
                  component.headStyle.color = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input v-model.number="component.headStyle.fontSize" style="width: 100%">
              <template v-slot:append>pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.headStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表体样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="奇数行背景色">
            <input-color-picker
              :input-width="150"
              :value="component.bodyStyle.oddRowColor"
              @change="
                (val) => {
                  component.bodyStyle.oddRowColor = val;
                  reloadTable(component);
                }
              "
            />
          </el-form-item>
          <el-form-item label="偶数行背景色">
            <input-color-picker
              :input-width="150"
              :value="component.bodyStyle.evenRowColor"
              @change="
                (val) => {
                  component.bodyStyle.evenRowColor = val;
                  reloadTable(component);
                }
              "
            />
          </el-form-item>
          <el-form-item label="行高">
            <el-input
              v-model="component.bodyStyle.height"
              style="width: 100%"
              @change="changeTableLineHeight(component)"
            >
              <template v-slot:append>px</template>
            </el-input>
          </el-form-item>

          <el-form-item label="字体颜色">
            <input-color-picker
              :input-width="150"
              :value="component.bodyStyle.color"
              @change="
                (val) => {
                  component.bodyStyle.color = val;
                  reloadTable(component);
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input
              v-model.number="component.bodyStyle.fontSize"
              style="width: 100%"
              @change="reloadTable(component)"
            >
              <template v-slot:append>pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.bodyStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
              @change="reloadTable(component)"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">滚动设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="滚动速度">
            <el-input v-model="component.options.step" style="width: 100%" />
          </el-form-item>
          <el-form-item label="滚动最小条数">
            <el-input
              v-model="component.options.limitMoveNum"
              style="width: 152px"
              @change="reloadTable(component)"
            >
              <template v-slot:append>行</template>
            </el-input>
          </el-form-item>
          <el-form-item label="鼠标悬停" class="df-form-item">
            <el-switch
              v-model="component.options.hoverStop"
              active-text="是"
              inactive-text="否"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="停顿时间">
            <el-input v-model="component.options.waitTime" style="width: 100%">
              <template v-slot:append>ms</template>
            </el-input>
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type == 'pageTable'">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表格设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="行号" class="df-form-item">
            <el-switch
              v-model="component.isIndex"
              active-text="是"
              inactive-text="否"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item v-if="component.isIndex" label="行号列宽度">
            <el-input v-model="component.indexWidth" style="width: 160px" />
          </el-form-item>
          <el-form-item label="对齐方式">
            <el-select v-model="component.align" placeholder="请选择" style="width: 100%">
              <el-option
                v-for="item in screenConstants.tableAlign"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">表头样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.headStyle.backgroundColor"
              @change="
                (val) => {
                  component.headStyle.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker
              :value="component.headStyle.color"
              @change="
                (val) => {
                  component.headStyle.color = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input v-model.number="component.headStyle.fontSize" style="width: 100%">
              <template v-slot:append>pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.headStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">行数据样式</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.rowStyle.backgroundColor"
              @change="
                (val) => {
                  component.rowStyle.backgroundColor = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体颜色">
            <input-color-picker
              :value="component.rowStyle.color"
              @change="
                (val) => {
                  component.rowStyle.color = val;
                }
              "
            />
          </el-form-item>
          <el-form-item label="字体大小">
            <el-input v-model.number="component.rowStyle.fontSize" style="width: 100%">
              <template v-slot:append>pt</template>
            </el-input>
          </el-form-item>
          <el-form-item label="是否加粗">
            <el-select
              v-model="component.rowStyle.fontWeight"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in selectUtil.fontWeight"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div class="right-dataset-title">
          <span class="attr-dataset-title">分页设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="每页显示条数">
            <el-input v-model.number="component.pagination.pageSize" style="width: 160px" />
          </el-form-item>
          <el-form-item label="背景颜色">
            <input-color-picker
              :value="component.pagination.backgroundColor"
              @change="
                (val) => {
                  component.pagination.backgroundColor = val;
                }
              "
            />
          </el-form-item>
        </div>
      </div>
    </el-form>
    <el-dialog
      title="表格数据列"
      v-model="tableColumnDialogVisiable"
      :close-on-click-modal="false"
      width="32%"
      @close="closeTableColumnDialog"
    >
      <el-form
        ref="tableColumnForm"
        class="demo-form-inline"
        :model="tableColumnForm"
        label-position="top"
        label-width="auto"
      >
        <el-form-item
          label="列名称"
          prop="name"
          :rules="filter_rules('列名称', { required: true })"
        >
          <el-input v-model="tableColumnForm.name" />
        </el-form-item>
        <el-form-item label="列属性" prop="key" :rules="filter_rules('列属性', { required: true })">
          <el-input v-model="tableColumnForm.key" />
        </el-form-item>
        <el-form-item
          label="列宽"
          prop="width"
          :rules="filter_rules('列宽', { required: true })"
          v-if="component.type == 'scrollTable'"
        >
          <el-input v-model="tableColumnForm.width">
            <template #append>%</template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeTableColumnDialog">取 消</el-button>
          <el-button type="primary" @click="addTableColomn">确 定</el-button>
        </span>
      </template>
    </el-dialog>
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
      return {
        tableColumnDialogVisiable: false,
        tableColumnForm: {
          name: '',
          key: '',
          width: '',
          type: '1', // 1新增 2编辑
          index: null,
        },
      };
    },
    methods: {
      changeTableLineHeight(component) {
        component.options.singleHeight = component.bodyStyle.height;
        this.reloadTable(component);
      },
      reloadTable(component) {
        // this.$parent.$parent.$parent.$refs["draggable"].$refs[component.id][0].$refs[component.id].reset()
      },
      // type 类型 1新增 2编辑
      showAddTableColumn(type, row, index) {
        this.tableColumnDialogVisiable = true;
        if (type == '2') {
          this.tableColumnForm.name = row.name;
          this.tableColumnForm.key = row.key;
          if (this.component.type == 'scrollTable') {
            this.tableColumnForm.width = row.style.width;
          }
          this.tableColumnForm.type = type;
          this.tableColumnForm.index = index;
        } else {
          this.tableColumnForm.name = '';
          this.tableColumnForm.key = '';
          this.tableColumnForm.width = '';
          this.tableColumnForm.type = type;
          this.tableColumnForm.index = index;
        }
      },
      addTableColomn() {
        this.$refs['tableColumnForm'].validate((valid) => {
          if (valid) {
            if (this.tableColumnForm.type == '1') {
              var obj = {
                name: this.tableColumnForm.name,
                key: this.tableColumnForm.key,
              };
              if (this.component.type == 'scrollTable') {
                obj.style = { width: this.tableColumnForm.width };
              }
              this.component.tableColumn.push(obj);
            } else {
              this.component.tableColumn[this.tableColumnForm.index].name =
                this.tableColumnForm.name;
              this.component.tableColumn[this.tableColumnForm.index].key = this.tableColumnForm.key;
              if (this.component.type == 'scrollTable') {
                this.component.tableColumn[this.tableColumnForm.index].style.width =
                  this.tableColumnForm.width;
              }
            }
            this.tableColumnForm.name = '';
            this.tableColumnForm.key = '';
            this.tableColumnForm.width = '';
            this.tableColumnForm.type = '1';
            this.tableColumnForm.index = null;
            this.closeTableColumnDialog();
            this.reloadTable(this.component);
          }
        });
      },
      // 删除列
      deleteTableColumn(index) {
        this.component.tableColumn.splice(index, 1);
        this.reloadTable(this.component);
      },
      closeTableColumnDialog() {
        this.tableColumnDialogVisiable = false;
        this.commonUtil.clearObj(this.tableColumnForm);
        this.$refs['tableColumnForm'].resetFields(); // 校验重置
      },
    },
  };
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 5px !important;
  }
  :deep(.el-form-item__label-wrap) {
    margin-left: 0px !important;
  }
</style>
