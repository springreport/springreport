<!-- 图表设置组件 -->
<template>
  <div>
    <el-form
      class="demo-form-inline"
      :inline="true"
      :model="component"
      label-position="left"
      ref="settingForm"
    >
      <el-collapse v-model="formCollapse">
        <el-collapse-item title="常规配置" name="generalConfig">
          <el-form-item label="图表类型">
            <span>{{ getChartType() }}</span>
          </el-form-item>
          <div v-if="!isPreview">
            <div class="cus-collapse-header df-c-b" @click="dataSetOpen = !dataSetOpen">
              <span>数据设置</span>
              <icon-up v-if="dataSetOpen" />
              <icon-down v-else />
            </div>
            <div v-show="dataSetOpen" class="cus-collapse-content">
              <el-form-item label="行列转置">
                <el-switch v-model="component.rangeConfigCheck" @change="rowColTrans()">
                </el-switch>
              </el-form-item>
              <el-form-item label="数据集" v-if="!isCoedit" class="df-form-item">
                <el-select
                  v-model="component.dataset"
                  placeholder="请选择"
                  style="width: 100%"
                  @change="changeDataset(false)"
                >
                  <el-option
                    v-for="item in datasets"
                    :key="item.datasetName"
                    :label="item.datasetName"
                    :value="item.datasetName"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="分组字段" v-if="!isCoedit" class="df-form-item">
                <el-select
                  v-model="component.categoryField"
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in datasetColumns"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="数值字段" v-if="!isCoedit" class="df-form-item">
                <el-select
                  v-model="component.valueField"
                  placeholder="请选择"
                  style="width: 100%"
                  multiple
                >
                  <el-option
                    v-for="item in datasetColumns"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="系列字段"
                v-if="component.chartAllType.indexOf('pie') <= 0 && !isCoedit"
                class="df-form-item"
              >
                <el-select
                  v-model="component.seriesField"
                  placeholder="请选择"
                  style="width: 100%"
                  multiple
                  allow-create
                  filterable
                >
                  <el-option
                    v-for="item in datasetColumns"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </div>

            <el-form-item label="显示标题" style="margin: 14px 0 12px 0">
              <el-switch
                v-model="component.defaultOption.spec.title.visible"
                @change="reLoadChart()"
              />
            </el-form-item>
            <div v-show="component.defaultOption.spec.title.visible" class="cus-collapse-content">
              <el-form-item
                label="标题内容"
                v-if="component.defaultOption.spec.title.visible"
                class="df-form-item"
              >
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.title.text"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="对齐方式"
                v-if="component.defaultOption.spec.title.visible"
                class="df-form-item"
              >
                <el-select
                  v-model="component.defaultOption.spec.title.align"
                  placeholder="请选择"
                  @change="reLoadChart()"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in selectUtil.textAlign"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="component.defaultOption.spec.title.visible"
                label="字体颜色"
                class="df-form-item"
              >
                <input-color-picker
                  :inputWidth="140"
                  :value="component.defaultOption.spec.title.textStyle.fill"
                  @change="
                    (val) => {
                      component.defaultOption.spec.title.textStyle.fill = val;
                      reLoadChart();
                    }
                  "
                />
              </el-form-item>
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item title="图表装修" name="chartStyle">
          <div v-show="setWidthAndHeightOpen" class="cus-collapse-content">
            <div v-if="component.chartAllType.toLowerCase().indexOf('pie') >= 0">
              <el-form-item label="外半径(0-1)">
                <el-button
                  link
                  type="primary"
                  icon="icon-minus"
                  @click="radiusMiuns('1')"
                ></el-button>
                <el-input
                  style="width: 80px"
                  @change="changeRadius('1')"
                  v-model="component.defaultOption.spec.outerRadius"
                ></el-input>
                <el-button
                  link
                  type="primary"
                  icon="icon-plus"
                  @click="radiusPlus('1')"
                ></el-button>
              </el-form-item>
              <el-form-item label="内半径(0-1)">
                <el-button
                  link
                  type="primary"
                  icon="icon-minus"
                  @click="radiusMiuns('2')"
                ></el-button>
                <el-input
                  style="width: 80px"
                  @change="changeRadius('2')"
                  v-model="component.defaultOption.spec.innerRadius"
                ></el-input>
                <el-button
                  link
                  type="primary"
                  icon="icon-plus"
                  @click="radiusPlus('2')"
                ></el-button>
              </el-form-item>
              <el-form-item label="扇区间隔">
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.padAngle"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item label="圆角">
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.pie.style.cornerRadius"
                  style="width: 170px"
                ></el-input>
              </el-form-item>
            </div>
            <div v-if="component.chartAllType.toLowerCase().indexOf('radar') >= 0">
              <el-form-item label="折线宽度">
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.line.style.lineWidth"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item label="区域显示">
                <el-switch
                  v-model="component.defaultOption.spec.area.visible"
                  @change="reLoadChart()"
                >
                </el-switch> </el-form-item
              ><br />
              <el-form-item label="圆形网格">
                <el-switch
                  v-model="component.defaultOption.spec.axes[0].grid.smooth"
                  @change="reLoadChart()"
                >
                </el-switch> </el-form-item
              ><br />
              <el-form-item label="坐标轴颜色">
                <input-color-picker
                  :inputWidth="140"
                  :value="component.defaultOption.spec.axes[0].grid.style.stroke"
                  @change="
                    (val) => {
                      component.defaultOption.spec.axes[0].grid.style.stroke = val;
                      changeRadarColor(chartsComponents, component);
                    }
                  "
                /> </el-form-item
              ><br />
              <el-form-item label="轴标签颜色">
                <input-color-picker
                  :inputWidth="140"
                  :value="component.defaultOption.spec.axes[1].label.style.fill"
                  @change="
                    (val) => {
                      component.defaultOption.spec.axes[1].label.style.fill = val;
                      changeRadarLabelColor(chartsComponents, component);
                    }
                  "
                /> </el-form-item
              ><br />
            </div>
            <div
              v-if="
                component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('area') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('column') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('bar') >= 0
              "
            >
              <el-form-item :label="getColorLabel() + '设置'" class="customLabel"> </el-form-item
              ><br />
              <el-form-item
                :label="getColorLabel() + '宽度'"
                v-if="
                  component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                  component.chartAllType.toLowerCase().indexOf('area') >= 0
                "
              >
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.line.style.lineWidth"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item
                :label="getColorLabel() + '宽度'"
                v-if="
                  component.chartAllType.toLowerCase().indexOf('column') >= 0 ||
                  component.chartAllType.toLowerCase().indexOf('bar') >= 0
                "
              >
                <el-input
                  @change="reLoadChart()"
                  v-model="component.defaultOption.spec.barWidth"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="圆角"
                v-if="
                  component.chartAllType.toLowerCase().indexOf('column') >= 0 ||
                  component.chartAllType.toLowerCase().indexOf('bar') >= 0
                "
              >
                <el-input
                  @change="reLoadChart()"
                  v-model.number="component.defaultOption.spec.bar.style.cornerRadius"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="折线标签是否显示"
                v-if="
                  component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                  component.chartAllType.toLowerCase().indexOf('area') >= 0
                "
              >
                <el-switch
                  v-model="component.defaultOption.spec.lineLabel.visible"
                  @change="reLoadChart()"
                >
                </el-switch>
              </el-form-item>
              <el-form-item
                label="标签位置"
                v-if="
                  (component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                    component.chartAllType.toLowerCase().indexOf('area') >= 0) &&
                  component.defaultOption.spec.lineLabel.visible
                "
              >
                <el-select
                  v-model="component.defaultOption.spec.lineLabel.position"
                  placeholder="请选择"
                  @change="reLoadChart()"
                  style="width: 180px"
                >
                  <el-option
                    v-for="item in screenConstants.lineLabelPosition"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="标签颜色"
                v-if="
                  (component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                    component.chartAllType.toLowerCase().indexOf('area') >= 0) &&
                  component.defaultOption.spec.lineLabel.visible
                "
              >
                <input-color-picker
                  :value="component.defaultOption.spec.lineLabel.style.fill"
                  @change="
                    (val) => {
                      component.defaultOption.spec.lineLabel.style.fill = val;
                      reLoadChart();
                    }
                  "
                />
              </el-form-item>
            </div>
          </div>
          <el-form-item label="背景颜色">
            <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.background" @change="(val)=>{component.defaultOption.spec.background=val;reLoadChart()}" />
          </el-form-item>
          <div class="cus-collapse-header df-c-b" style="margin: 12px 0 0px">
            <span>色系设置</span>
            <el-button type="primary" class="addBtn" size="small" @click="addColor(component)"
              ><icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--left" />添加
            </el-button>
          </div>
          <el-form-item style="width: 100%; margin-bottom: 12px" class="df-form-item">
            <el-select
              v-model="systemColor"
              placeholder="请选择"
              style="width: 100%"
              @change="changeSystemColor"
            >
              <el-option
                v-for="item in screenConstants.systemChartColorsNames"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <div v-show="component.defaultOption.spec.color.length" class="cus-collapse-content">
            <el-form-item
              label=""
              v-for="(item, index) in component.defaultOption.spec.color"
              :key="index"
              style="margin-right: 12px"
            >
              <div style="position: relative">
                <el-color-picker
                  v-model="component.defaultOption.spec.color[index]"
                  size="small"
                  @change="reLoadChart()"
                ></el-color-picker>
                <icon-close-one
                  theme="filled"
                  fill="#333"
                  style="cursor: pointer; position: absolute; top: 0px; right: -2px"
                  @click="confirmDeleteColor(index)"
                />
              </div>
            </el-form-item>
            <div style="text-align: center">
              <el-link type="info" size="small" @click="clearColor(component)"
                ><icon-delete class="el-icon--left"></icon-delete>清空</el-link
              >
            </div>
          </div>
        </el-collapse-item>

        <el-collapse-item
          title="坐标轴设置"
          name="xAndYAxis"
          v-if="
            component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
            component.chartAllType.toLowerCase().indexOf('area') >= 0 ||
            component.chartAllType.toLowerCase().indexOf('column') >= 0 ||
            component.chartAllType.toLowerCase().indexOf('bar') >= 0
          "
        >
          <div class="cus-collapse-header df-c-b" @click="xAxisOpen = !xAxisOpen">
            <span>X轴字体</span>
            <icon-up v-if="xAxisOpen" />
            <icon-down v-else />
          </div>
          <div v-show="xAxisOpen" class="cus-collapse-content">
            <el-form-item label="x轴字体" class="df-form-item">
              <input-color-picker
                :inputWidth="140"
                :value="component.defaultOption.spec.axes[0].label.style.fill"
                @change="
                  (val) => {
                    component.defaultOption.spec.axes[0].label.style.fill = val;
                    reLoadChart();
                  }
                "
              />
            </el-form-item>
            <el-form-item label="自动旋转" class="df-form-item">
              <el-switch
                v-model="component.defaultOption.spec.axes[0].label.autoRotate"
                @change="changeAutoRotate(chartsComponents, component)"
              >
              </el-switch>
            </el-form-item>
            <el-form-item label="自动省略" class="df-form-item">
              <el-switch
                v-model="component.defaultOption.spec.axes[0].label.autoLimit"
                @change="reLoadChart()"
              >
              </el-switch>
            </el-form-item>
          </div>

          <div
            class="cus-collapse-header df-c-b"
            style="margin-top: 12px"
            @click="yAxisOpen = !yAxisOpen"
          >
            <span>Y轴字体</span>
            <icon-up v-if="yAxisOpen" />
            <icon-down v-else />
          </div>
          <div v-show="yAxisOpen" class="cus-collapse-content">
            <el-form-item label="y轴字体" class="df-form-item">
              <input-color-picker
                :inputWidth="140"
                :value="component.defaultOption.spec.axes[1].label.style.fill"
                @change="
                  (val) => {
                    component.defaultOption.spec.axes[1].label.style.fill = val;
                    reLoadChart();
                  }
                "
              />
            </el-form-item>
            <el-form-item label="自动省略" class="df-form-item">
              <el-switch
                v-model="component.defaultOption.spec.axes[1].label.autoLimit"
                @change="reLoadChart()"
              >
              </el-switch>
            </el-form-item>
          </div>
        </el-collapse-item>

        <el-collapse-item title="标签设置" name="labelSet">
          <el-form-item label="是否显示" class="df-form-item">
            <el-switch v-model="component.defaultOption.spec.label.visible" @change="reLoadChart()">
            </el-switch>
          </el-form-item>
          <el-form-item
            label="标签位置"
            v-if="component.defaultOption.spec.label.visible"
            class="df-form-item"
          >
            <el-select
              v-if="
                component.chartAllType.toLowerCase().indexOf('line') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('area') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('column') >= 0 ||
                component.chartAllType.toLowerCase().indexOf('bar') >= 0
              "
              v-model="component.defaultOption.spec.label.position"
              placeholder="请选择"
              @change="reLoadChart()"
              style="width: 100%"
            >
              <el-option
                v-for="item in screenConstants.labelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
            <el-select
              v-if="component.chartAllType.toLowerCase().indexOf('pie') >= 0"
              v-model="component.defaultOption.spec.label.position"
              placeholder="请选择"
              @change="reLoadChart()"
              style="width: 100%"
            >
              <el-option
                v-for="item in screenConstants.pieLabelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="标签颜色"
            v-if="component.defaultOption.spec.label.visible"
            class="df-form-item"
          >
            <input-color-picker
              :inputWidth="140"
              :value="component.defaultOption.spec.label.style.fill"
              @change="
                (val) => {
                  component.defaultOption.spec.label.style.fill = val;
                  reLoadChart();
                }
              "
            /> </el-form-item
          ><br />
          <el-form-item
            label="字体大小"
            v-if="component.defaultOption.spec.label.visible"
            class="df-form-item"
          >
            <el-input
              @change="reLoadChart()"
              v-model.number="component.defaultOption.spec.label.style.fontSize"
              style="width: 100%"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="标签格式"
            v-if="component.defaultOption.spec.label.visible"
            class="df-form-item"
          >
            <el-input
              @change="reLoadChart()"
              v-model.number="component.defaultOption.spec.label.formatter"
              style="width: 100px"
            ></el-input>
            &nbsp;<el-button link type="primary" @click="formatterRule()">设置规则</el-button>
          </el-form-item>
        </el-collapse-item>

        <el-collapse-item title="图例设置" name="legends">
          <el-form-item label="是否显示" class="df-form-item">
            <el-switch
              v-model="component.defaultOption.spec.legends.visible"
              @change="reLoadChart()"
            >
            </el-switch>
          </el-form-item>
          <el-form-item
            label="图例位置"
            class="df-form-item"
            v-if="component.defaultOption.spec.legends.visible"
          >
            <el-select
              v-model="component.defaultOption.spec.legends.orient"
              placeholder="请选择"
              @change="reLoadChart()"
              style="width: 100%"
            >
              <el-option
                v-for="item in screenConstants.legendOrient"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="对齐方式"
            class="df-form-item"
            v-if="component.defaultOption.spec.legends.visible"
          >
            <el-select
              v-model="component.defaultOption.spec.legends.position"
              placeholder="请选择"
              @change="reLoadChart()"
              style="width: 100%"
            >
              <el-option
                v-for="item in screenConstants.legendPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="字体颜色"
            class="df-form-item"
            v-if="component.defaultOption.spec.legends.visible"
          >
            <input-color-picker
              :inputWidth="140"
              :value="component.defaultOption.spec.legends.item.label.style.fill"
              @change="
                (val) => {
                  component.defaultOption.spec.legends.item.label.style.fill = val;
                  reLoadChart();
                }
              "
            />
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>
    <el-dialog
      title="添加"
      v-model="addColorDialogVisiable"
      :close-on-click-modal="false"
      @close="closeAddDialog"
      width="417px"
    >
      <div class="cus-color-picker">
        <input-color-picker
          :value="color"
          @change="
            (val) => {
              color = val;
            }
          "
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeAddDialog">取 消</el-button>
          <el-button type="primary" @click="confirmAddColor">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import InputColorPicker from '@/components/screen/colorpicker/inputColorPicker.vue';
  import VChart from '@visactor/vchart';
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
      datasets: {
        type: Array,
        default: () => [],
      },
      isPreview: {
        type: Boolean,
        default: false,
      },
      isCoedit: {
        type: Boolean,
        default: true,
      },
    },
    mounted() {
      this.changeDataset(true);
      this.predefineColors = this.commonConstants.predefineColors;
    },
    data() {
      return {
        formCollapse: ['generalConfig', 'chartStyle', 'xAndYAxis', 'labelSet', 'legendSet'],
        dataSetOpen: true,
        setWidthAndHeightOpen: true,
        xAxisOpen: true,
        yAxisOpen: true,

        predefineColors: [],
        addColorDialogVisiable: false,
        color: '',
        systemColor: '',
        datasetColumns: [],
      };
    },
    methods: {
      addColor() {
        this.addColorDialogVisiable = true;
      },
      clearColor() {
        this.systemColor = '';
        if (this.component.chartAllType.toLowerCase().indexOf('map') >= 0) {
          this.component.defaultOption.spec.color.range = [];
        } else {
          this.component.defaultOption.spec.color = [];
        }
        this.reLoadChart();
      },
      confirmAddColor() {
        if (!this.color) {
          this.commonUtil.showMessage({
            message: '请选择颜色',
            type: this.commonConstants.messageType.error,
          });
          return;
        }
        if (this.component.chartAllType.toLowerCase().indexOf('map') >= 0) {
          this.component.defaultOption.spec.color.range.push(this.color);
        } else {
          this.component.defaultOption.spec.color.push(this.color);
        }

        this.reLoadChart();
        this.closeAddDialog();
      },
      closeAddDialog() {
        this.addColorDialogVisiable = false;
        this.color = '';
      },
      confirmDeleteColor(index) {
        this.component.defaultOption.spec.color.splice(index, 1);
        this.reLoadChart();
      },
      confirmDeleteMapColor(index) {
        this.component.defaultOption.spec.color.range.splice(index, 1);
        this.reLoadChart();
      },
      changeAutoRotate(chartsComponents, component) {
        component.defaultOption.spec.axes[0].sampling =
          !component.defaultOption.spec.axes[0].label.autoRotate;
        this.reLoadChart();
      },
      getColorLabel() {
        let type = this.component.chartAllType.toLowerCase();
        let label = '';
        if (type.indexOf('histogram') >= 0) {
          label = '柱体';
        } else if (type.indexOf('line') >= 0 || type.indexOf('area') >= 0) {
          label = '折线';
        }
        return label;
      },
      radiusMiuns(type) {
        if (type == 1) {
          //外半径
          this.component.defaultOption.spec.outerRadius = (
            Number(this.component.defaultOption.spec.outerRadius) - 0.1
          ).toFixed(1);
        } else {
          //内半径
          this.component.defaultOption.spec.innerRadius = (
            Number(this.component.defaultOption.spec.innerRadius) - 0.1
          ).toFixed(1);
        }
        this.changeRadius(type);
      },
      radiusPlus(type) {
        if (type == 1) {
          //外半径
          this.component.defaultOption.spec.outerRadius = (
            Number(this.component.defaultOption.spec.outerRadius) + 0.1
          ).toFixed(1);
        } else {
          //内半径
          this.component.defaultOption.spec.innerRadius = (
            Number(this.component.defaultOption.spec.innerRadius) + 0.1
          ).toFixed(1);
        }
        this.changeRadius(type);
      },
      changeRadius(type) {
        if (type == 1) {
          if (this.component.defaultOption.spec.outerRadius > 1) {
            this.component.defaultOption.spec.outerRadius = 1.0;
          } else if (this.component.defaultOption.spec.outerRadius < 0.1) {
            this.component.defaultOption.spec.outerRadius = 0.1;
          }
        } else {
          if (this.component.defaultOption.spec.innerRadius > 1) {
            this.component.defaultOption.spec.innerRadius = 1.0;
          } else if (this.component.defaultOption.spec.innerRadius < 0) {
            this.component.defaultOption.spec.innerRadius = 0.0;
          }
        }
        this.component.defaultOption.spec.outerRadius = Number(
          this.component.defaultOption.spec.outerRadius
        );
        this.component.defaultOption.spec.innerRadius = Number(
          this.component.defaultOption.spec.innerRadius
        );
        this.reLoadChart();
      },
      formatterRule() {
        window.open('https://www.visactor.io/vchart/guide/tutorial_docs/Chart_Plugins/Formatter');
      },
      changeRadarColor(chartsComponents, component) {
        component.defaultOption.spec.axes[1].domainLine.style.stroke =
          component.defaultOption.spec.axes[0].grid.style.stroke;
        component.defaultOption.spec.axes[1].grid.style.stroke =
          component.defaultOption.spec.axes[0].grid.style.stroke;
        this.reLoadChart();
      },
      changeRadarLabelColor(chartsComponents, component) {
        component.defaultOption.spec.axes[0].label.style.fill =
          component.defaultOption.spec.axes[1].label.style.fill;
        this.reLoadChart();
      },
      changeSystemColor() {
        let colors = this.screenConstants.systemChartColors[this.systemColor];
        if (this.component.chartAllType.toLowerCase().indexOf('map') >= 0) {
          this.component.defaultOption.spec.color.range = colors;
        } else {
          this.component.defaultOption.spec.color = colors;
        }
        this.reLoadChart();
      },
      reLoadChart() {
        luckysheet.renderChart();
      },
      rowColTrans() {
        luckysheet.rowColTrans(this.component);
      },
      changeDataset(isEdit) {
        for (let index = 0; index < this.datasets.length; index++) {
          const element = this.datasets[index];
          if (!isEdit) {
            this.component.categoryField = '';
            this.component.valueField = [];
            this.component.seriesField = [];
          }
          if (element.datasetName == this.component.dataset) {
            if (!element.columns || element.columns.length == 0) {
              this.getDatasetColumns(element, '1');
            } else {
              this.datasetColumns = element.columns;
            }
            break;
          }
        }
      },
      getDatasetColumns(element, type) {
        const obj = {
          url: this.apis.reportDesign.getDataSetColumnsApi,
          params: { id: element.id },
          removeEmpty: false,
        };
        var that = this;
        this.commonUtil.doPost(obj).then((response) => {
          element.columns = response.responseData;
          that.datasetColumns = element.columns;
        });
      },
      getChartType() {
        let chartAllType = this.component.chartAllType;
        if (chartAllType == 'echarts|line|default') {
          return '折线图';
        } else if (chartAllType == 'echarts|line|smooth') {
          return '平滑折线图';
        } else if (chartAllType == 'echarts|area|default') {
          return '面积图';
        } else if (chartAllType == 'echarts|area|stack') {
          return '堆叠面积图';
        } else if (chartAllType == 'echarts|column|default') {
          return '柱状图';
        } else if (chartAllType == 'echarts|column|stack') {
          return '堆叠柱状图';
        } else if (chartAllType == 'echarts|bar|default') {
          return '条形图';
        } else if (chartAllType == 'echarts|bar|stack') {
          return '堆叠条形图';
        } else if (chartAllType == 'echarts|pie|default') {
          return '饼图';
        } else if (chartAllType == 'echarts|radar|default') {
          return '雷达图';
        }
      },
    },
    watch: {
      'component.dataset'(newValue, oldValue) {
        this.changeDataset(true);
      },
    },
  };
</script>
<style scoped lang="scss">
  .demo-form-inline {
    :deep(.el-form-item) {
      margin-right: 0px;
    }
  }

  .cus-collapse-header {
    height: 32px;
    line-height: 32px;
    padding: 0;
    font-size: 14px;
    cursor: pointer;
    margin-bottom: 4px;
  }

  .cus-collapse-content {
    border-radius: 4px;
    border: 0.5px solid rgba(23, 183, 148, 0.1);
    background: rgba(23, 183, 148, 0.05);
    padding: 8px 10px;
  }

  ::v-deep .el-collapse-item {
    .el-collapse-item__header {
      position: relative;
      padding: 0 12px;
      height: 36px;
      line-height: 36px;
      background-color: #f9fafa;
      color: #666;
      font-size: 14px;
      font-weight: bold;
    }

    .el-collapse-item__wrap {
      padding: 16px;
      background-color: #fff;
    }

    .el-collapse-item__content {
      padding-bottom: 0;
    }
  }

  .df-form-item {
    display: flex;
    margin-right: 0;

    ::v-deep .el-form-item__label {
      // margin-right: 12px;
      flex-shrink: 0;
      width: 80px;
    }

    ::v-deep .el-form-item__content {
      flex: 1;
    }
  }

  .cus-color-picker {
    ::v-deep .el-input {
      width: 100% !important;
    }
  }

  ::v-deep .el-form-item {
    margin-bottom: 8px;
  }

  ::v-deep .el-form--label-top .el-form-item__label {
    padding-bottom: 0;
    line-height: 32px;
  }

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

  .addBtn {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 2px 8px;
    height: 22px;
    background: #17b794;
    border-radius: 3px;
    flex: none;
    order: 1;
    flex-grow: 0;
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    font-size: 12px;
    line-height: 18px;
    color: #ffffff;
  }
</style>
