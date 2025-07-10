<!-- 图表设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="图表设置"> -->
    <el-form ref="settingForm" class="demo-form-inline" :model="component" label-position="top" size="mini">
      <div class="right-dataset-title">
        <span class="attr-dataset-title">主题设置</span>
      </div>
      <div class="right-dataset-warp">
        <el-form-item label="主题">
          <el-select v-model="component.theme" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
            <el-option
              v-for="item in screenConstants.vchartthemes"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="背景颜色">
          <input-color-picker :value="component.spec.background" @change="(val)=>{component.spec.background=val;commonUtil.reLoadChart(chartsComponents,component)}" />
        </el-form-item>
        <el-form-item>
          <div slot="label" class="df-c-b">
            <span>图表色系设置</span>
            <el-button
              type="primary"
              size="mini"
              class="addBtn"
              @click="addColor(component)"
            ><i class="el-icon-plus el-icon--left" />添加</el-button>
          </div>

          <el-select v-model="systemColor" placeholder="请选择" style="width:150px" @change="changeSystemColor">
            <el-option
              v-for="item in screenConstants.systemChartColorsNames"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <div v-if="component.type.toLowerCase().indexOf('map')<0" class="df-c" style="flex-wrap: wrap;">
          <el-form-item v-for="(item,index) in component.spec.color" :key="index" label="" class="color-el-form-item">
            <el-color-picker v-model="component.spec.color[index]" size="mini" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            <i
              class="el-icon-error"
              style="
                    cursor: pointer;
                    position: absolute;
                    top: 12px;
                    right: 8px;
                  "
              @click="confirmDeleteColor(index)"
            />
          </el-form-item>
        </div>
        <div v-if="component.type.toLowerCase().indexOf('map')>=0" class="df-c" style="flex-wrap: wrap;">
          <el-form-item v-for="(item,index) in component.spec.color.range" :key="index" label="" style="position: relative;width:20%;">
            <el-color-picker v-model="component.spec.color.range[index]" size="mini" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            <i
              class="el-icon-error"
              style="
                    cursor: pointer;
                    position: absolute;
                    top: 12px;
                    right: 8px;
                  "
              @click="confirmDeleteMapColor(index)"
            />
          </el-form-item>
        </div>
        <div style="text-align: center">
          <el-link
            type="info"
            @click="clearColor(component)"
          ><i class="el-icon-delete el-icon--left" />清空</el-link>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('wordcloud')<0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">标题设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="是否显示" class="df-form-item">
            <el-switch v-model="component.spec.title.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.title.visible" label="标题内容">
            <el-input v-model="component.spec.title.text" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.title.visible" label="对齐方式">
            <el-select v-model="component.spec.title.align" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in selectUtil.textAlign"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.title.visible" label="字体颜色">
            <input-color-picker :value="component.spec.title.textStyle.fill" @change="(val)=>{component.spec.title.textStyle.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('pie')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">饼图设置</span>
        </div>

        <div class="right-dataset-warp">
          <el-form-item label="外半径(0-1)">
            <el-input-number v-model="component.spec.outerRadius" style="width:100%" @change="changeRadius('1')" />
          </el-form-item>
          <el-form-item label="内半径(0-1)">
            <el-input-number v-model="component.spec.innerRadius" style="width:100%" @change="changeRadius('2')" />
          </el-form-item>
          <el-form-item label="扇区间隔">
            <el-input v-model="component.spec.padAngle" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="圆角">
            <el-input v-model="component.spec.pie.style.cornerRadius" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="轮播动画" class="df-form-item">
            <el-switch v-model="component.spec.isLoop" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('radar')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">雷达图设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="折线宽度">
            <el-input v-model="component.spec.line.style.lineWidth" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="区域显示" class="df-form-item">
            <el-switch v-model="component.spec.area.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="圆形网格" class="df-form-item">
            <el-switch v-model="component.spec.axes[0].grid.smooth" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="坐标轴颜色">
            <input-color-picker :input-width="160" :value="component.spec.axes[0].grid.style.stroke" @change="(val)=>{component.spec.axes[0].grid.style.stroke=val;changeRadarColor(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="轴标签颜色">
            <input-color-picker :input-width="160" :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;changeRadarLabelColor(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('scatter')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">散点设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="散点大小">
            <el-input v-model="component.spec.size" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('gauge')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">仪表盘设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="上边距">
            <el-input v-model.number="component.spec.padding.top" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="下边距">
            <el-input v-model.number="component.spec.padding.bottom" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="外半径(0-1)">
            <el-input-number v-model="component.spec.outerRadius" style="width:100%" @change="changeRadius('1')" />
          </el-form-item>
          <el-form-item label="内半径(0-1)">
            <el-input-number v-model="component.spec.innerRadius" style="width:100%" @change="changeRadius('2')" />
          </el-form-item>
          <el-form-item label="起始角度">
            <el-input v-model="component.spec.startAngle" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="结束角度">
            <el-input v-model="component.spec.endAngle" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="仪表盘背景色">
            <input-color-picker :input-width="150" :value="component.spec.gauge.track.style.fill" @change="(val)=>{component.spec.gauge.track.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="最小值">
            <el-input v-model.number="component.spec.axes[0].min" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="最大值">
            <el-input v-model.number="component.spec.axes[0].max" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="标签字体颜色">
            <input-color-picker :input-width="140" :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="标签字体大小">
            <el-input v-model.number="component.spec.axes[0].label.style.fontSize" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('seriesgauge')<0" label="指标卡是否显示" class="df-form-item">
            <el-switch v-model="component.spec.indicator.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible" label="上边距">
            <el-input v-model="component.spec.indicator.offsetY" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible" label="指标卡字体大小">
            <el-input v-model.number="component.spec.indicator.content.style.fontSize" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible" label="指标卡字体颜色">
            <input-color-picker :input-width="140" :value="component.spec.indicator.content.style.fill" @change="(val)=>{component.spec.indicator.content.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>

          <div v-if="component.type.toLowerCase().indexOf('seriesgauge')>=0">
            <div class="attr-dataset-title-small">数值标签设置</div>
            <el-form-item label="标签是否显示" class="df-form-item">
              <el-switch v-model="component.spec.gauge.label.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </el-form-item>
            <el-form-item v-if="component.spec.gauge.label.visible" label="标签位置">
              <el-select v-model="component.spec.gauge.label.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
                <el-option
                  v-for="item in screenConstants.pieLabelPosition"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item v-if="component.spec.gauge.label.visible" label="标签颜色">
              <input-color-picker :value="component.spec.gauge.label.style.fill" @change="(val)=>{component.spec.gauge.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
            </el-form-item>
            <el-form-item v-if="component.spec.gauge.label.visible" label="字体大小">
              <el-input v-model.number="component.spec.gauge.label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </el-form-item>
            <el-form-item v-if="component.spec.gauge.label.visible" label="标签格式">
              <el-input v-model="component.spec.gauge.label.formatter" style="width:120px" @change="commonUtil.reLoadChart(chartsComponents,component)">
                <el-button slot="suffix" type="text" @click="formatterRule()">设置规则</el-button>

              </el-input>
            </el-form-item>
          </div>
          <div class="attr-dataset-title-small">图例设置</div>
          <el-form-item label="是否显示" class="df-form-item">
            <el-switch v-model="component.spec.legends.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="图例位置">
            <el-select v-model="component.spec.legends.orient" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendOrient"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="对齐方式">
            <el-select v-model="component.spec.legends.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="字体颜色">
            <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('circularprogress')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">仪表盘设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="外半径(0-1)">
            <el-input-number v-model="component.spec.outerRadius" style="width: 100%;" @change="changeRadius('1')" />
          </el-form-item>
          <el-form-item label="内半径(0-1)">
            <el-input-number v-model="component.spec.innerRadius" style="width: 100%;" @change="changeRadius('2')" />
          </el-form-item>
          <!-- <el-form-item label="最小值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].min"></el-input>
                        </el-form-item>
                        <el-form-item label="最大值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].max"></el-input>
                        </el-form-item> -->
          <el-form-item label="指标卡是否显示" class="df-form-item">
            <el-switch v-model="component.spec.indicator.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.indicator.visible" label="指标卡字体大小">
            <el-input v-model.number="component.spec.indicator.title.style.fontSize" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.indicator.visible" label="指标卡字体颜色">
            <input-color-picker :input-width="140" :value="component.spec.indicator.title.style.fill" @change="(val)=>{component.spec.indicator.title.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="指标卡触发类型">
            <el-select v-model="component.spec.indicator.trigger" placeholder="请选择" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.triggerType"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="是否显示" class="df-form-item">
            <el-switch v-model="component.spec.legends.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="图例位置">
            <el-select v-model="component.spec.legends.orient" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendOrient"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="对齐方式">
            <el-select v-model="component.spec.legends.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="字体颜色">
            <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('barprogress')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">进度条设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="进度条宽度">
            <el-input v-model.number="component.spec.bandWidth" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="圆角">
            <el-input v-model.number="component.spec.cornerRadius" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="x轴字体">
            <input-color-picker :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="y轴字体">
            <input-color-picker :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="y轴位置">
            <el-select v-model="component.spec.axes[0].orient" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.orient"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('liquid')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">水波图设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="形状">
            <el-select v-model="component.spec.maskShape" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.liquidShape"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="指标卡是否显示" class="df-form-item">
            <el-switch v-model="component.spec.indicator.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.indicator.visible" label="指标卡字体大小">
            <el-input v-model.number="component.spec.indicator.title.style.fontSize" style="width:140px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.indicator.visible" label="指标卡字体颜色">
            <input-color-picker :input-width="140" :value="component.spec.indicator.title.style.fill" @change="(val)=>{component.spec.indicator.title.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('map')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">地图设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item label="地图类型">
            <el-select v-model="component.spec.map" style="width:170px" filterable placeholder="地图类型" @change="changeMapType(component)">
              <el-option
                v-for="item in screenConstants.map"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="默认填充色">
            <input-color-picker :input-width="140" :value="component.spec.defaultFillColor" @change="(val)=>{component.spec.defaultFillColor=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <div class="attr-dataset-title-small">标签设置</div>
          <el-form-item label="标签是否显示" class="df-form-item">
            <el-switch v-model="component.spec.label.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="标签颜色">
            <input-color-picker :value="component.spec.label.style.fill" @change="(val)=>{component.spec.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="字体大小">
            <el-input v-model.number="component.spec.label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="标签格式">
            <el-input v-model="component.spec.label.formatter" style="width:120px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-button slot="suffix" type="text" @click="formatterRule()">设置规则</el-button>

            </el-input>
          </el-form-item>
          <div class="attr-dataset-title-small">地区名称映射</div>
          <el-form-item label="">
            <div v-for="(value,key) in component.spec.nameMap" :key="key">
              <el-tag :title="key" style="width:100px">{{ key.length>6?key.substring(0,6)+'...':key }}</el-tag> <b style="color:white">-></b>
              <el-input v-model="component.spec.nameMap[key]" style="width:120px" size="small" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </div>
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0 || component.type.toLowerCase().indexOf('histogram')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">{{ getColorLabel() }}设置</span>
        </div>
        <div class="right-dataset-warp">

          <el-form-item v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0" :label="getColorLabel()+'宽度'">
            <el-input v-model="component.spec.line.style.lineWidth" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('histogram')>=0" :label="getColorLabel()+'宽度'">
            <el-input v-model="component.spec.barWidth" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.type.toLowerCase().indexOf('histogram')>=0" label="圆角">
            <el-input v-model.number="component.spec.bar.style.cornerRadius" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <!-- <el-form-item v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0" label="折线标签是否显示" class="df-form-item">
            <el-switch v-model="component.spec.lineLabel.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="(component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0) && component.spec.lineLabel.visible" label="标签位置">
            <el-select v-model="component.spec.lineLabel.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.lineLabelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="(component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0) && component.spec.lineLabel.visible" label="标签颜色">
            <input-color-picker :value="component.spec.lineLabel.style.fill" @change="(val)=>{component.spec.lineLabel.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item> -->
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('combocharthl')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">柱体/折线设置</span>
        </div>
        <div class="right-dataset-warp">
          <el-form-item :label="'柱体宽度'">
            <el-input v-model="component.spec.series[0].barWidth" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="圆角">
            <el-input v-model.number="component.spec.series[0].bar.style.cornerRadius" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item :label="'折线宽度'">
            <el-input v-model="component.spec.series[1].line.style.lineWidth" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="(component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0) && component.spec.lineLabel.visible" label="标签颜色">
            <input-color-picker :value="component.spec.lineLabel.style.fill" @change="(val)=>{component.spec.lineLabel.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0 || component.type.toLowerCase().indexOf('histogram')>=0 || component.type.toLowerCase().indexOf('combocharthl')>=0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">坐标轴设置</span>
        </div>
        <div class="right-dataset-warp">

          <el-form-item label="x轴字体颜色">
            <input-color-picker :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="x轴字体大小">
            <el-input v-model.number="component.spec.axes[0].label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="开启自动旋转" class="df-form-item">
            <el-switch v-model="component.spec.axes[0].label.autoRotate" active-text="是" inactive-text="否" @change="changeAutoRotate(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="开启自动省略" class="df-form-item">
            <el-switch v-model="component.spec.axes[0].label.autoLimit" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="y轴字体颜色">
            <input-color-picker :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item label="y轴字体大小">
            <el-input v-model.number="component.spec.axes[1].label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item label="开启自动省略" class="df-form-item">
            <el-switch v-model="component.spec.axes[1].label.autoLimit" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
        </div>
      </div>
      <div v-if="component.type.toLowerCase().indexOf('wordcloud')<0 && component.type.toLowerCase().indexOf('gauge')<0 && component.type.toLowerCase().indexOf('circularprogress')<0 && component.type.toLowerCase().indexOf('barprogress')<0 && component.type.toLowerCase().indexOf('liquid')<0 && component.type.toLowerCase().indexOf('map')<0 && component.type.toLowerCase().indexOf('boxplot')<0">
        <div class="right-dataset-title">
          <span class="attr-dataset-title">标签设置</span>
        </div>
        <div class="right-dataset-warp">

          <el-form-item label="标签是否显示" class="df-form-item">
            <el-switch v-model="component.spec.label.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible && component.type.toLowerCase().indexOf('funnel')<0" label="标签位置">
            <el-select v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0 || component.type.toLowerCase().indexOf('histogram')>=0 " v-model="component.spec.label.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.labelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
            <el-select v-if="component.type.toLowerCase().indexOf('pie')>=0 " v-model="component.spec.label.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.pieLabelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
            <el-select v-if="component.type.toLowerCase().indexOf('scatter')>=0 " v-model="component.spec.label.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.scatterLabelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
            <el-select v-if="component.type.toLowerCase().indexOf('radar')>=0 " v-model="component.spec.label.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.radarLabelPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="标签颜色">
            <input-color-picker :value="component.spec.label.style.fill" @change="(val)=>{component.spec.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="字体大小">
            <el-input v-model.number="component.spec.label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.label.visible" label="标签格式">
            <el-input v-model="component.spec.label.formatter" style="width:120px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-button slot="suffix" type="text" @click="formatterRule()">设置规则</el-button>
            </el-input>
          </el-form-item>
          <div class="attr-dataset-title-small">图例设置</div>

          <el-form-item label="是否显示" class="df-form-item">
            <el-switch v-model="component.spec.legends.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="图例位置">
            <el-select v-model="component.spec.legends.orient" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendOrient"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="对齐方式">
            <el-select v-model="component.spec.legends.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
              <el-option
                v-for="item in screenConstants.legendPosition"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="字体颜色">
            <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
          </el-form-item>
          <el-form-item v-if="component.spec.legends.visible" label="字体大小">
            <el-input v-model.number="component.spec.legends.item.label.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
          </el-form-item>
          <div v-if="component.type.toLowerCase().indexOf('funnel')>=0">
            <!-- <div class="attr-dataset-title-small">漏斗图形状</div> -->

            <el-form-item label="漏斗图形状">
              <el-select v-model="component.spec.shape" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
                <el-option
                  v-for="item in screenConstants.funnelShape"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <div class="attr-dataset-title-small">外部标签配置</div>

            <el-form-item label="是否显示" class="df-form-item">
              <el-switch v-model="component.spec.outerLabel.visible" active-text="是" inactive-text="否" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </el-form-item>
            <el-form-item v-if="component.spec.outerLabel.visible" label="标签位置">
              <el-select v-model="component.spec.outerLabel.position" placeholder="请选择" style="width:180px" @change="commonUtil.reLoadChart(chartsComponents,component)">
                <el-option
                  v-for="item in screenConstants.legendOrient"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item v-if="component.spec.outerLabel.visible" label="字体大小">
              <el-input v-model.number="component.spec.outerLabel.style.fontSize" @change="commonUtil.reLoadChart(chartsComponents,component)" />
            </el-form-item>
            <el-form-item v-if="component.spec.outerLabel.visible" label="字体颜色">
              <input-color-picker :value="component.spec.outerLabel.style.fill" @change="(val)=>{component.spec.outerLabel.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
            </el-form-item>
          </div>
        </div>
      </div>
      <div class="right-dataset-title">
        <span class="attr-dataset-title">提示框配置</span>
      </div>
      <div class="right-dataset-warp">
        <el-form-item label="提示框标题">
          <el-input v-model="component.spec.tooltip.mark.title.value" style="width:120px" @change="commonUtil.reLoadChart(chartsComponents,component)" />
        </el-form-item>
      </div>
      <div class="right-dataset-title">
        <span class="attr-dataset-title">动画设置</span>
      </div>
      <div class="right-dataset-warp">
        <el-form-item label="动画效果">
          <el-select v-model="component.amination" placeholder="请选择" style="width:180px" @change="commonUtil.changeHistogramAmination(chartsComponents,component)">
            <el-option
              v-for="item in screenConstants.baranimation"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </div>
    </el-form>
    <!-- </el-collapse-item>
    </el-collapse> -->
    <el-dialog title="添加" width="32%" :visible.sync="addColorDialogVisiable" :close-on-click-modal="false" @close="closeAddDialog">
      <el-form ref="addColorRef" label-position="top" class="demo-form-inline" size="mini">
        <el-form-item key="color" label="颜色">
          <input-color-picker :value="color" @change="(val)=>{color=val}" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeAddDialog">取 消</el-button>
        <el-button type="primary" size="small" @click="confirmAddColor">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import InputColorPicker from '../../colorpicker/inputColorPicker.vue'
import VChart from '@visactor/vchart'
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
      predefineColors: [],
      addColorDialogVisiable: false,
      color: '',
      systemColor: ''
    }
  },
  mounted() {
    this.predefineColors = this.commonConstants.predefineColors
  },
  methods: {
    addColor() {
      this.addColorDialogVisiable = true
    },
    clearColor() {
      this.systemColor = ''
      if (this.component.type.toLowerCase().indexOf('map') >= 0) {
        this.component.spec.color.range = []
      } else {
        this.component.spec.color = []
      }
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    },
    confirmAddColor() {
      if (!this.color) {
        this.commonUtil.showMessage({ message: '请选择颜色', type: this.commonConstants.messageType.error })
        return
      }
      if (this.component.type.toLowerCase().indexOf('map') >= 0) {
        this.component.spec.color.range.push(this.color)
      } else {
        this.component.spec.color.push(this.color)
      }

      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
      this.closeAddDialog()
    },
    closeAddDialog() {
      this.addColorDialogVisiable = false
      this.color = ''
    },
    confirmDeleteColor(index) {
      this.component.spec.color.splice(index, 1)
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    },
    confirmDeleteMapColor(index) {
      this.component.spec.color.range.splice(index, 1)
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    },
    changeAutoRotate(chartsComponents, component) {
      component.spec.axes[0].sampling = !component.spec.axes[0].label.autoRotate
      this.commonUtil.reLoadChart(chartsComponents, component)
    },
    getColorLabel() {
      const type = this.component.type.toLowerCase()
      let label = ''
      if (type.indexOf('histogram') >= 0) {
        label = '柱体'
      } else if (type.indexOf('line') >= 0 || type.indexOf('area') >= 0) {
        label = '折线'
      }
      return label
    },
    radiusMiuns(type) {
      if (type == 1) { // 外半径
        this.component.spec.outerRadius = (Number(this.component.spec.outerRadius) - 0.1).toFixed(1)
      } else { // 内半径
        this.component.spec.innerRadius = (Number(this.component.spec.innerRadius) - 0.1).toFixed(1)
      }
      this.changeRadius(type)
    },
    radiusPlus(type) {
      if (type == 1) { // 外半径
        this.component.spec.outerRadius = (Number(this.component.spec.outerRadius) + 0.1).toFixed(1)
      } else { // 内半径
        this.component.spec.innerRadius = (Number(this.component.spec.innerRadius) + 0.1).toFixed(1)
      }
      this.changeRadius(type)
    },
    changeRadius(type) {
      if (type == 1) {
        if (this.component.spec.outerRadius > 1) {
          this.component.spec.outerRadius = 1.0
        } else if (this.component.spec.outerRadius < 0.1) {
          this.component.spec.outerRadius = 0.1
        }
      } else {
        if (this.component.spec.innerRadius > 1) {
          this.component.spec.innerRadius = 1.0
        } else if (this.component.spec.innerRadius < 0) {
          this.component.spec.innerRadius = 0.0
        }
      }
      this.component.spec.outerRadius = Number(this.component.spec.outerRadius)
      this.component.spec.innerRadius = Number(this.component.spec.innerRadius)
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    },
    formatterRule() {
      window.open('https://www.visactor.io/vchart/guide/tutorial_docs/Chart_Plugins/Formatter')
    },
    changeRadarColor(chartsComponents, component) {
      component.spec.axes[1].domainLine.style.stroke = component.spec.axes[0].grid.style.stroke
      component.spec.axes[1].grid.style.stroke = component.spec.axes[0].grid.style.stroke
      this.commonUtil.reLoadChart(chartsComponents, component)
    },
    changeRadarLabelColor(chartsComponents, component) {
      component.spec.axes[0].label.style.fill = component.spec.axes[1].label.style.fill
      this.commonUtil.reLoadChart(chartsComponents, component)
    },
    async changeMapType(component) {
      const mapCode = component.spec.map

      if (!VChart.getMap(mapCode)) {
        const geojson = await this.commonUtil.getMapData(mapCode)
        VChart.registerMap(mapCode, geojson)
      }
      component.spec.nameMap = this.screenConstants.nameMap[mapCode]
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    },
    changeSystemColor() {
      const colors = this.screenConstants.systemChartColors[this.systemColor]
      if (this.component.type.toLowerCase().indexOf('map') >= 0) {
        this.component.spec.color.range = colors
      } else {
        this.component.spec.color = colors
      }
      this.commonUtil.reLoadChart(this.chartsComponents, this.component)
    }
  }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:5px !important
}
::v-deep .el-form-item__label-wrap{
    margin-left:0px !important
}
::v-deep .el-color-picker__trigger{
    /* top:-12px */
}
::v-deep .customLabel{
    font-weight: bold;
}
::v-deep .customLabel .el-form-item__label{
    color:#15a585 !important;
}
</style>
