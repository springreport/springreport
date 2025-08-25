<!-- 数据设置组件 -->
<template>
  <div>
    <el-form class="demo-form-inline" :model="component" label-position="top" ref="settingForm">
      <el-form-item for="-" v-if="component.type.toLowerCase().indexOf('combocharthl') < 0  && component.type.toLowerCase().indexOf('combochartdbbar') < 0">
        <template #label>
          <div style="width: 100%; margin-bottom: 4px" class="df-c-b">
            <span>请求方式</span>
            <div class="config-btn" @click.stop="editData(component)">数据配置</div>
          </div>
        </template>

        <el-select v-model="component.dataSource" placeholder="请选择">
          <el-option label="静态数据" value="1" />
          <el-option label="动态数据" value="2" />
        </el-select>
      </el-form-item>
      <div
            v-if="component.type.toLowerCase().indexOf('combocharthl') >= 0"
          >
          <div class="right-dataset-title">
            <span class="attr-dataset-title">柱状图数据</span>
          </div>
          <el-form-item>
            <div  style="width: 100%;" class="df-c-b">
              <span>请求方式</span>
              <div
                class="config-btn"
                @click="editcomboCharthlData(component,'1')"
              >数据配置</div>
            </div>
            <el-select v-model="component.dataSource" placeholder="请选择">
              <el-option label="静态数据" value="1" />
              <el-option label="动态数据" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="x轴数据">
              <el-select
                v-model="component.spec.series[0].xField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="y轴数据">
              <el-select
                v-model="component.spec.series[0].yField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="系列分组">
              <el-select
                v-model="component.spec.series[0].seriesField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          <div class="right-dataset-title">
            <span class="attr-dataset-title">折线图数据</span>
          </div>
          <el-form-item>
            <div style="width: 100%;" class="df-c-b">
              <span>请求方式</span>
              <div
                class="config-btn"
                @click="editcomboCharthlData(component,'2')"
              >数据配置</div>
            </div>
            <el-select v-model="component.lineDataSource" placeholder="请选择">
              <el-option label="静态数据" value="1" />
              <el-option label="动态数据" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="x轴数据">
              <el-select
                v-model="component.spec.series[1].xField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="y轴数据">
              <el-select
                v-model="component.spec.series[1].yField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="系列分组">
              <el-select
                v-model="component.spec.series[1].seriesField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          </div>
          <div
            v-if="component.type.toLowerCase().indexOf('combochartdbbar') >= 0"
          >
          <div class="right-dataset-title">
            <span class="attr-dataset-title">左侧数据</span>
          </div>
          <el-form-item>
            <div style="width: 100%; margin-bottom: 4px" class="df-c-b">
              <span>请求方式</span>
              <div
                class="config-btn"
                @click="editcomboCharthlData(component,'1')"
              >数据配置</div>
            </div>
            <el-select v-model="component.dataSource" placeholder="请选择">
              <el-option label="静态数据" value="1" />
              <el-option label="动态数据" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="x轴数据">
              <el-select
                v-model="component.spec.series[0].xField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="y轴数据">
              <el-select
                v-model="component.spec.series[0].yField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="系列分组">
              <el-select
                v-model="component.spec.series[0].seriesField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          <div class="right-dataset-title">
            <span class="attr-dataset-title">右侧数据</span>
          </div>
          <el-form-item>
            <div style="width: 100%; margin-bottom: 4px" class="df-c-b">
              <span>请求方式</span>
              <div
                class="config-btn"
                @click="editcomboCharthlData(component,'2')"
              >数据配置</div>
            </div>
            <el-select v-model="component.lineDataSource" placeholder="请选择">
              <el-option label="静态数据" value="1" />
              <el-option label="动态数据" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="x轴数据">
              <el-select
                v-model="component.spec.series[1].xField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="y轴数据">
              <el-select
                v-model="component.spec.series[1].yField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="系列分组">
              <el-select
                v-model="component.spec.series[1].seriesField"
                placeholder="请选择"
                multiple
                allow-create
                filterable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.lineDynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          </div>
      <div
        v-if="
          component.type.toLowerCase().indexOf('line') >= 0 ||
          component.type.toLowerCase().indexOf('area') >= 0 ||
          component.type.toLowerCase().indexOf('histogram') >= 0
        "
      >
        <el-form-item label="x轴数据">
          <el-select
            v-model="component.spec.xField"
            placeholder="请选择"
            multiple
            allow-create
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="y轴数据">
          <el-select
            v-model="component.spec.yField"
            placeholder="请选择"
            multiple
            allow-create
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            multiple
            allow-create
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('zhifangtu') >= 0">
        <el-form-item label="x轴起始数据">
          <el-select
            v-model="component.spec.xField"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="x轴结束数据">
          <el-select
            v-model="component.spec.x2Field"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="y轴数据">
          <el-select
            v-model="component.spec.yField"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div
        v-if="
          component.type.toLowerCase().indexOf('radar') >= 0 ||
          component.type.toLowerCase().indexOf('gauge') >= 0 ||
          component.type.toLowerCase().indexOf('circularprogress') >= 0
        "
      >
        <el-form-item label="分类字段">
          <el-select
            v-model="component.spec.categoryField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据文本" v-if="component.type.toLowerCase().indexOf('circularprogress') >= 0">
              <el-select
                v-model="component.spec.valueTextField"
                placeholder="请选择"
                allow-create
                filterable
                clearable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div
        v-if="
          component.type.toLowerCase().indexOf('pie') >= 0 ||
          component.type.toLowerCase().indexOf('funnel') >= 0 ||
          component.type.toLowerCase().indexOf('circlepacking') >= 0
        "
      >
        <el-form-item label="分类字段">
          <el-select
            v-model="component.spec.categoryField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('scatter') >= 0  && component.type.toLowerCase().indexOf('scattermap') < 0">
        <el-form-item label="x轴数据">
          <el-select
            v-model="component.spec.xField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="y轴数据">
          <el-select
            v-model="component.spec.yField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('wordcloud') >= 0">
        <el-form-item label="文本字段">
          <el-select
            v-model="component.spec.nameField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="权重字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('barprogress') >= 0">
        <el-form-item label="x轴数据">
          <el-select
            v-model="component.spec.xField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="y轴数据">
          <el-select
            v-model="component.spec.yField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('liquid') >= 0">
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类字段">
          <el-select
            v-model="component.spec.categoryField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('basicmap') >= 0">
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="地区字段">
          <el-select
            v-model="component.spec.nameField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('scattermap') >= 0">
            <el-form-item label="数值字段">
              <el-select
                v-model="component.spec.series[1].yField"
                placeholder="请选择"
                allow-create
                filterable
                clearable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="地区字段">
              <el-select
                v-model="component.spec.series[1].xField"
                placeholder="请选择"
                allow-create
                filterable
                clearable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="经度">
              <el-select
                v-model="component.spec.region[0].longitudeField"
                placeholder="请选择"
                allow-create
                filterable
                clearable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="纬度">
              <el-select
                v-model="component.spec.region[0].latitudeField"
                placeholder="请选择"
                allow-create
                filterable
                clearable
                @change="commonUtil.reLoadChart(chartsComponents, component)"
              >
                <el-option
                  v-for="item in component.dynamicDataSettings.dataColumns"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          </div>
      <div v-if="component.type.toLowerCase().indexOf('text') >= 0 || component.type.toLowerCase().indexOf('numberflipper') >= 0">
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            allow-create
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('boxplot') >= 0">
        <el-form-item label="x轴数据">
          <el-select
            v-model="component.spec.xField"
            placeholder="请选择"
            multiple
            allow-create
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="最小值">
          <el-select
            v-model="component.spec.minField"
            placeholder="请选择"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Q1">
          <el-select
            v-model="component.spec.q1Field"
            placeholder="请选择较小四分位数"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Q2">
          <el-select
            v-model="component.spec.medianField"
            placeholder="请选择中位数"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Q3">
          <el-select
            v-model="component.spec.q3Field"
            placeholder="请选择较大四分位数"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="最大值">
          <el-select
            v-model="component.spec.maxField"
            placeholder="请选择"
            filterable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="系列分组">
          <el-select
            v-model="component.spec.seriesField"
            placeholder="请选择"
            filterable
            clearable
            @change="changeBoxSeriesField(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('sankey') >= 0">
        <el-form-item label="起始节点">
          <el-select
            v-model="component.spec.sourceField"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="目标节点">
          <el-select
            v-model="component.spec.targetField"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数值字段">
          <el-select
            v-model="component.spec.valueField"
            placeholder="请选择"
            filterable
            clearable
            @change="commonUtil.reLoadChart(chartsComponents, component)"
          >
            <el-option
              v-for="item in component.dynamicDataSettings.dataColumns"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </div>
      <el-form-item v-if="component.dataSource == '2'" label="定时刷新" class="df-form-item">
        <el-switch v-model="component.refresh" active-text="是" inactive-text="否" />
      </el-form-item>
      <el-form-item label="刷新间隔" v-if="component.dataSource == '2' && component.refresh">
        <el-input v-model.number="component.refreshTime" style="width: 180px">
          <template #append>毫秒</template>
        </el-input>
      </el-form-item>
    </el-form>
    <static-data-dialog
      v-if="customDataDialogVisiable"
      v-model:customDataDialogVisiable="customDataDialogVisiable"
      :component="component"
      :chartsComponents="chartsComponents"
      :comboChartType="comboChartType"
    ></static-data-dialog>
    <dynamic-data-dialog
      v-if="dynamicDialogVisiable"
      v-model:dynamicDialogVisiable="dynamicDialogVisiable"
      :component="component"
      :chartsComponents="chartsComponents"
      :comboChartType="comboChartType"
    ></dynamic-data-dialog>
  </div>
</template>

<script>
  import staticDataDialog from '../../dialog/staticDataDialog.vue';
  import dynamicDataDialog from '../../dialog/dynamicDataDialog.vue';
  export default {
    components: {
      staticDataDialog,
      dynamicDataDialog,
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
        customDataDialogVisiable: false,
        dynamicDialogVisiable: false,
        comboChartType:""
      };
    },
    methods: {
      editData(component) {
        this.comboChartType = "";
        if (component.dataSource == '1') {
          this.customDataDialogVisiable = true;
        } else if (component.dataSource == '2') {
          this.dynamicDialogVisiable = true;
        }
      },
      //编辑折柱图的数据
      editcomboCharthlData(component,type) {
        this.comboChartType = type;
        if(type == "1"){
          if (component.dataSource == '1') {
            this.customDataDialogVisiable = true
          } else if (component.dataSource == '2') {
            this.dynamicDialogVisiable = true
          }
        }else{
          if (component.lineDataSource == '1') {
            this.customDataDialogVisiable = true
          } else if (component.lineDataSource == '2') {
            this.dynamicDialogVisiable = true
          }
        }
      },
      changeBoxSeriesField(chartsComponents, component) {
        if (!component.spec.seriesField) {
          component.spec.seriesField = null;
        }
        this.commonUtil.reLoadChart(chartsComponents, component);
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
