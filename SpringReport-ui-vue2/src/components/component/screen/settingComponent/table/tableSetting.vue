<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="表格设置"> -->
    <el-form
      ref="settingForm"
      class="demo-form-inline"
      :model="component"
      label-position="top"
      size="mini"
    >
      <div class="right-dataset-title df-c-b">
        <span class="attr-dataset-title">数据列设置</span>
        <el-button
          class="addBtn"
          @click="showAddTableColumn('1')"
        ><i class="el-icon-plus el-icon--left" />添加</el-button>
      </div>

      <el-collapse
        v-if="component.tableColumn && component.tableColumn.length > 0"
        class="sub-collapse"
      >
        <el-collapse-item
          v-for="(item, index) in component.tableColumn"
          :key="index"
        >
          <template slot="title">
            {{ item.name }}
            <div
              class="right-block-el-icon-edit"
              @click.stop="showAddTableColumn('2', item, index)"
            />
            <div
              class="right-el-icon-delete"
              @click.stop="deleteTableColumn(index)"
            />
          </template>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            列名称：{{ item.name }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            列属性：{{ item.key }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px" v-if="item.style">
            列宽：{{ item.style.width }}%
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px">
            是否超链接：{{ item.isHyperLink?'是':'否' }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px" v-if="item.isHyperLink">
            超链接：{{ item.hyperLink }}
          </p>
          <p class="column-tag" style="min-width: 220px; max-width: 220px" v-if="item.isHyperLink">
            超链接参数：{{ item.hyperLinkParam }}
          </p>
          
          
          <p class="column-tag" style="min-width: 220px; max-width: 220px" v-if="item.conditions && item.conditions.length > 0">
            <el-collapse
              class="sub-collapse"
            >
              <el-collapse-item
                v-for="(conditionItem, conditionindex) in item.conditions"
                :key="conditionindex"
              >
              <template slot="title">
                {{ '自定义颜色条件'+ conditionindex}}
              </template>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  属性：{{ conditionItem.conditionProperty }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  操作符：{{ conditionItem.conditionOperator }}
                </p>
                <p class="column-tag" style="min-width: 220px; max-width: 220px">
                  条件值：{{ conditionItem.conditionValue }}
                </p>
                <p class="column-tag" :style="{minWidth:'220px',maxWidth:'220px',color:conditionItem.conditionColor}">
                  字体颜色：{{ conditionItem.conditionColor }}
                </p>
                
              </el-collapse-item>
            </el-collapse>
          </p>
        </el-collapse-item>
      </el-collapse>

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
              <template slot="append">%</template>
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
              <template slot="append">px</template>
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
            <el-input
              v-model="component.headStyle.height"
              style="width: 100%"
            >
              <template slot="append">px</template>
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
            <el-input
              v-model.number="component.headStyle.fontSize"
              style="width: 100%"
            >
              <template slot="append">pt</template>
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
              <template slot="append">px</template>
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
              <template slot="append">pt</template>
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
              <template slot="append">行</template>
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
            <el-input
              v-model="component.options.waitTime"
              style="width: 100%"
            >
              <template slot="append">ms</template>
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
            <el-select
              v-model="component.align"
              placeholder="请选择"
              style="width: 100%"
            >
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
            <el-input
              v-model.number="component.headStyle.fontSize"
              style="width: 100%"
            >
              <template slot="append">pt</template>
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
            <el-input
              v-model.number="component.rowStyle.fontSize"
              style="width: 100%"
            >
              <template slot="append">pt</template>
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
            <el-input
              v-model.number="component.pagination.pageSize"
              style="width: 160px"
            />
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
          <el-form-item label="自动翻页" class="df-form-item">
            <el-switch
              v-model="component.autoPage"
              active-text="是"
              inactive-text="否"
              @change="changeAutoPage(component)"
            />
          </el-form-item>
          <el-form-item label="翻页时间间隔(秒)" v-if="component.autoPage">
            <el-input
              v-model.number="component.autoPageInterval"
              style="width: 160px"
              @change="changeAutoPageInterval(component)"
            />
          </el-form-item>
        </div>
      </div>
    </el-form>
    <!-- </el-collapse-item>
    </el-collapse> -->
    <el-dialog
      title="表格数据列"
      :visible.sync="tableColumnDialogVisiable"
      :close-on-click-modal="false"
      width="500px"
      @close="closeTableColumnDialog"
      class="showAll_dialog"
      top="50px"
    >
      <el-form
        ref="tableColumnForm"
        class="demo-form-inline"
        :model="tableColumnForm"
        label-position="top"
        size="mini"
      >
        <el-form-item
          label="列名称"
          prop="name"
          :rules="filter_rules('列名称', { required: true })"
        >
          <el-input v-model="tableColumnForm.name" />
        </el-form-item>
        <el-form-item
          label="列属性"
          prop="key"
          :rules="filter_rules('列属性', { required: true })"
        >
          <el-input v-model="tableColumnForm.key" />
        </el-form-item>
        <el-form-item
          v-if="component.type == 'scrollTable'"
          label="列宽"
          prop="width"
          :rules="filter_rules('列宽', { required: true })"
        >
          <el-input v-model="tableColumnForm.width">
            <template slot="append">%</template>
          </el-input>
        </el-form-item>
        <el-form-item label="是否超链接"  prop="isHyperLink" :rules="filter_rules('是否超链接', { required: true })">
          <el-switch v-model="tableColumnForm.isHyperLink" active-text="是" inactive-text="否"  />
        </el-form-item>
        <el-form-item label="超链接"  prop="hyperLink" :rules="filter_rules('超链接', { required: true })" v-if="tableColumnForm.isHyperLink">
          <el-input v-model="tableColumnForm.hyperLink" type="textarea" :rows="3" style="width:100%"></el-input>
        </el-form-item>
        <el-form-item label="参数"  prop="hyperLinkParam" :rules="filter_rules('参数', { required: true })" v-if="tableColumnForm.isHyperLink">
          <el-input v-model="tableColumnForm.hyperLinkParam" placeholder="多个参数用,分隔" style="width:100%"></el-input>
        </el-form-item>

        <el-form-item label="是否进度条"  prop="isProgress" :rules="filter_rules('是否进度条', { required: true })">
          <el-switch v-model="tableColumnForm.isProgress" active-text="是" inactive-text="否"  />
        </el-form-item>
        <el-form-item label="阈值"  prop="isProgress" :rules="filter_rules('阈值', { required: true })" v-if="tableColumnForm.isProgress">
          <el-input v-model="tableColumnForm.thresholdValue" placeholder="阈值" ></el-input>
        </el-form-item>
         <el-form-item label="进度条宽度"  prop="progressWidth" :rules="filter_rules('进度条宽度', { required: true })" v-if="tableColumnForm.isProgress">
          <el-input v-model.number="tableColumnForm.progressWidth" placeholder="进度条宽度" ></el-input>
        </el-form-item>
        <el-form-item label="大于阈值显示的颜色"  prop="upColor" :rules="filter_rules('大于阈值显示的颜色', { required: true })" v-if="tableColumnForm.isProgress">
           <input-color-picker
                  :value="tableColumnForm.upColor"
                  @change="
                    (val) => {
                      tableColumnForm.upColor = val;
                    }
                  "
                />
        </el-form-item>
        <el-form-item label="小于阈值显示的颜色"  prop="downColor" :rules="filter_rules('小于阈值显示的颜色', { required: true })" v-if="tableColumnForm.isProgress">
           <input-color-picker
                  :value="tableColumnForm.downColor"
                  @change="
                    (val) => {
                      tableColumnForm.downColor = val;
                    }
                  "
                />
        </el-form-item>
        <el-form-item label="等于阈值显示的颜色"  prop="equalColor" :rules="filter_rules('等于阈值显示的颜色', { required: true })" v-if="tableColumnForm.isProgress">
           <input-color-picker
                  :value="tableColumnForm.equalColor"
                  @change="
                    (val) => {
                      tableColumnForm.equalColor = val;
                    }
                  "
                />
        </el-form-item>
        <el-form-item label="进度条字体颜色"  prop="textColor" :rules="filter_rules('进度条字体颜色', { required: true })" v-if="tableColumnForm.isProgress">
           <input-color-picker
                  :value="tableColumnForm.textColor"
                  @change="
                    (val) => {
                      tableColumnForm.textColor = val;
                    }
                  "
                />
        </el-form-item>

        <el-form-item
          label="自定义字体颜色条件"
        >
        <el-button type="primary" @click="addColorCondition">添加条件</el-button>
        <el-table
          size="small"
          :data="tableColumnForm.conditions"
          style="width: 100%; margin-top: 12px"
          :border="true"
        >
          <el-table-column prop="conditionProperty" label="属性" />
          <el-table-column prop="conditionOperator" label="操作符" />
          <el-table-column prop="conditionValue" label="条件值" />
          <el-table-column prop="conditionColor" label="颜色" >
             <template slot-scope="scope">
              <div  :style="{color:scope.row.conditionColor}">字体颜色</div>
             </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-link
                type="info"
                style="margin-right: 12px"
                @click="editConditionColor(scope.row,scope.$index)"
              >编辑</el-link>
              <el-link
                type="info"
                @click="deleteConditionColor(scope.$index)"
                v-show="scope.row.id != 0"
              >删除</el-link>
            </template>
          </el-table-column>
        </el-table>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeTableColumnDialog">取 消</el-button>
        <el-button type="primary" @click="addTableColomn">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="自定义字体颜色条件"
      :visible.sync="colorConditionDialogVisiable"
      :close-on-click-modal="false"
      width="500px"
      @close="closeColorConditionDialog"
    >
      <el-form
        ref="colorConditionForm"
        class="demo-form-inline"
        :model="colorConditionForm"
        label-position="top"
        size="mini"
      >
        <el-form-item
          label="属性"
          prop="conditionProperty"
          :rules="filter_rules('属性', { required: true })"
        >
          <el-input v-model="colorConditionForm.conditionProperty" size="small" placeholder="属性"/>
        </el-form-item>
        <el-form-item
          label="操作符"
          prop="conditionOperator"
          :rules="filter_rules('操作符', { required: true })"
        >
           <el-select
            v-model="colorConditionForm.conditionOperator"
            placeholder="操作符"
            size="small"
          >
            <el-option
              v-for="op in selectUtil.operate"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="条件值"
          prop="conditionValue"
          :rules="filter_rules('条件值', { required: true })"
        >
          <el-input
            v-model="colorConditionForm.conditionValue"
            placeholder="条件值"
            size="small"
          />
        </el-form-item>
        <el-form-item
          label="字体颜色"
          prop="conditionColor"
          :rules="filter_rules('字体颜色', { required: true })"
        >
           <input-color-picker
                  :value="colorConditionForm.conditionColor"
                  @change="
                    (val) => {
                      colorConditionForm.conditionColor = val;
                    }
                  "
                />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeColorConditionDialog">取 消</el-button>
        <el-button type="primary" @click="confirmColorCondition">确 定</el-button>
      </span>
    </el-dialog>
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
      tableColumnDialogVisiable: false,
      tableColumnForm: {
        name: '',
        key: '',
        width: '',
        type: '1', // 1新增 2编辑
        index: null,
        conditions:[],//颜色条件
        isHyperLink:false,
        hyperLink:"",
        hyperLinkParam:"",
        isProgress:false,//是否进度条显示
        progressWidth:10,
        thresholdValue:"",//阈值
        upColor:"",//大于阈值显示的颜色
        downColor:"",//小于阈值显示的颜色
        equalColor:"",//等于阈值显示的颜色
        textColor:"",
      },
      colorConditionForm:{
        conditionOperator:'',
        conditionProperty:'',
        conditionValue:'',
        conditionColor:'',
        index:null
      },
      colorConditionDialogVisiable:false,
      tableTimer:{}
    }
  },
  mounted() {},
  methods: {
    changeTableLineHeight(component) {
      component.options.singleHeight = component.bodyStyle.height
      this.reloadTable(component)
    },
    reloadTable(component) {
      this.$parent.$parent.$parent.$refs['draggable'].$refs[
        component.id
      ][0].$refs[component.id].reset()
    },
    // type 类型 1新增 2编辑
    showAddTableColumn(type, row, index) {
      this.tableColumnDialogVisiable = true
      if (type == '2') {
        this.tableColumnForm.name = row.name
        this.tableColumnForm.key = row.key
        if (this.component.type == 'scrollTable') {
          this.tableColumnForm.width = row.style.width
        }
        this.tableColumnForm.type = type
        this.tableColumnForm.index = index
        this.tableColumnForm.conditions = row.conditions;
        this.tableColumnForm.isHyperLink = row.isHyperLink==null?false:row.isHyperLink;
        this.tableColumnForm.hyperLink = row.hyperLink;
        this.tableColumnForm.hyperLinkParam = row.hyperLinkParam;
        this.tableColumnForm.isProgress = row.isProgress==null?false:row.isProgress;
        this.tableColumnForm.thresholdValue = row.thresholdValue;
        this.tableColumnForm.progressWidth = row.progressWidth;
        this.tableColumnForm.upColor = row.upColor;
        this.tableColumnForm.downColor = row.downColor;
        this.tableColumnForm.equalColor = row.equalColor;
        this.tableColumnForm.textColor = row.textColor;
      } else {
        this.tableColumnForm.name = ''
        this.tableColumnForm.key = ''
        this.tableColumnForm.width = ''
        this.tableColumnForm.type = type
        this.tableColumnForm.index = index
        this.tableColumnForm.isHyperLink = false;
        this.tableColumnForm.hyperLink = '';
        this.tableColumnForm.hyperLinkParam = '';
        this.tableColumnForm.isProgress = false;
        this.tableColumnForm.thresholdValue = '50';
        this.tableColumnForm.progressWidth = 10;
        this.tableColumnForm.upColor = '';
        this.tableColumnForm.downColor = '';
        this.tableColumnForm.equalColor = '';
        this.tableColumnForm.textColor = null;
      }
    },
    addTableColomn() {
      this.$refs['tableColumnForm'].validate((valid) => {
        if (valid) {
          if (this.tableColumnForm.type == '1') {
            var obj = {
              name: this.tableColumnForm.name,
              key: this.tableColumnForm.key,
              isHyperLink: this.tableColumnForm.isHyperLink,
              hyperLink: this.tableColumnForm.hyperLink,
              hyperLinkParam: this.tableColumnForm.hyperLinkParam,
              isProgress: this.tableColumnForm.isProgress,
              thresholdValue: this.tableColumnForm.thresholdValue,
              progressWidth: this.tableColumnForm.progressWidth,
              upColor: this.tableColumnForm.upColor,
              downColor: this.tableColumnForm.downColor,
              equalColor: this.tableColumnForm.equalColor,
              textColor: this.tableColumnForm.textColor
            }
            if (this.component.type == 'scrollTable') {
              obj.style = { width: this.tableColumnForm.width }
            }
            this.component.tableColumn.push(obj)
          } else {
            this.component.tableColumn[this.tableColumnForm.index].name =
              this.tableColumnForm.name
            this.component.tableColumn[this.tableColumnForm.index].key =
              this.tableColumnForm.key
            if (this.component.type == 'scrollTable') {
              this.component.tableColumn[
                this.tableColumnForm.index
              ].style.width = this.tableColumnForm.width
            }
            this.component.tableColumn[this.tableColumnForm.index].conditions =
              this.tableColumnForm.conditions
            this.component.tableColumn[this.tableColumnForm.index].isHyperLink =
              this.tableColumnForm.isHyperLink
            this.component.tableColumn[this.tableColumnForm.index].hyperLink =
              this.tableColumnForm.hyperLink
            this.component.tableColumn[this.tableColumnForm.index].hyperLinkParam =
              this.tableColumnForm.hyperLinkParam
            this.component.tableColumn[this.tableColumnForm.index].isProgress =
              this.tableColumnForm.isProgress
            this.component.tableColumn[this.tableColumnForm.index].thresholdValue =
              this.tableColumnForm.thresholdValue
            this.component.tableColumn[this.tableColumnForm.index].progressWidth =
              this.tableColumnForm.progressWidth
            this.component.tableColumn[this.tableColumnForm.index].upColor =
              this.tableColumnForm.upColor
            this.component.tableColumn[this.tableColumnForm.index].downColor =
              this.tableColumnForm.downColor
            this.component.tableColumn[this.tableColumnForm.index].equalColor =
              this.tableColumnForm.equalColor
            this.component.tableColumn[this.tableColumnForm.index].textColor =
              this.tableColumnForm.textColor
          }
          this.tableColumnForm.name = ''
          this.tableColumnForm.key = ''
          this.tableColumnForm.width = ''
          this.tableColumnForm.type = '1'
          this.tableColumnForm.index = null
          this.tableColumnForm.isHyperLink = false
          this.tableColumnForm.hyperLink = null
          this.tableColumnForm.hyperLinkParam = null

          this.tableColumnForm.isProgress = false
          this.tableColumnForm.thresholdValue = ""
          this.tableColumnForm.progressWidth = 10
          this.tableColumnForm.upColor = ""
          this.tableColumnForm.downColor = ""
          this.tableColumnForm.equalColor = ""
          this.tableColumnForm.textColor = null
          this.closeTableColumnDialog()
          this.reloadTable(this.component)
        }
      })
    },
    // 删除列
    deleteTableColumn(index) {
      this.component.tableColumn.splice(index, 1)
      this.reloadTable(this.component)
    },
    closeTableColumnDialog() {
      this.tableColumnDialogVisiable = false
      this.commonUtil.clearObj(this.tableColumnForm)
      this.$refs['tableColumnForm'].resetFields() // 校验重置
    },
    changeAutoPage(component){
      let timer = this.tableTimer[component.id];
      if(component.autoPage){
        if(timer == null){
          timer = setInterval(function(){
            if((component.pagination.currentPage*component.pagination.pageSize)>=component.spec.data.total) {
              component.pagination.currentPage = 1;
            }else{
              component.pagination.currentPage = component.pagination.currentPage + 1
            }
          },component.autoPageInterval?component.autoPageInterval*1000:5000);
          this.tableTimer[component.id] = timer
        }
      }else{
        clearInterval(timer);     
        this.tableTimer[component.id] = null;
      }
    },
    changeAutoPageInterval(component){
      let timer = this.tableTimer[component.id];
      if(timer != null){
        clearInterval(timer);     
        this.tableTimer[component.id] = null;
      }
      this.changeAutoPage(component);
    },
    addColorCondition(){
      this.colorConditionDialogVisiable = true;
    },
    closeColorConditionDialog(){
      this.colorConditionDialogVisiable = false;
      this.$refs['colorConditionForm'].resetFields()// 校验重置
      this.commonUtil.clearObj(this.colorConditionForm)
    },
    confirmColorCondition(){
      this.$refs['colorConditionForm'].validate((valid) => {
        if (valid) {
          if (this.colorConditionForm.index != null) {
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionOperator =
              this.colorConditionForm.conditionOperator
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionProperty =
              this.colorConditionForm.conditionProperty
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionValue =
              this.colorConditionForm.conditionValue
              this.tableColumnForm.conditions[this.colorConditionForm.index].conditionColor =
              this.colorConditionForm.conditionColor
          }else{
            var obj = {
              conditionOperator: this.colorConditionForm.conditionOperator,
              conditionProperty: this.colorConditionForm.conditionProperty,
              conditionValue: this.colorConditionForm.conditionValue,
              conditionColor: this.colorConditionForm.conditionColor
            }
            if(!this.tableColumnForm.conditions){
              this.tableColumnForm.conditions = [];
            }
            this.tableColumnForm.conditions.push(obj)
          }
          this.closeColorConditionDialog();
          this.reloadTable(this.component)
        }
      })
    },
    editConditionColor(row,index){
      this.colorConditionDialogVisiable = true;
      this.colorConditionForm.conditionOperator = row.conditionOperator
      this.colorConditionForm.conditionProperty = row.conditionProperty
      this.colorConditionForm.conditionValue = row.conditionValue
      this.colorConditionForm.conditionColor = row.conditionColor
      this.colorConditionForm.index = index
    },
    deleteConditionColor(index){
      this.tableColumnForm.conditions.splice(index, 1)
    }
  }
}
</script>
<style scoped >
.el-form-item {
  margin-bottom: 5px !important;
}
::v-deep .el-form-item__label-wrap {
  margin-left: 0px !important;
}
::v-deep .el-color-picker__trigger {
  /* top:-12px */
}
::v-deep .customLabel {
  font-weight: bold;
}
::v-deep .customLabel .el-form-item__label {
  color: #15a585 !important;
}

.showAll_dialog {
    overflow: hidden;

}

::v-deep .el-dialog {
        height: 70%;
        overflow: auto;
    }


</style>
