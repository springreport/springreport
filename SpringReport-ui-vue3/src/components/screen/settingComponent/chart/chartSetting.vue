<!-- 图表设置组件 -->
<template>
    <div>
        <el-collapse>
            <el-collapse-item title="图表设置">
                <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left" size="small" ref="settingForm">
                    <el-form-item label="主题设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="主题">
                        <el-select v-model="component.theme" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.vchartthemes"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="背景颜色">
                        <input-color-picker :value="component.spec.background" @change="(val)=>{component.spec.background=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item><br>
                    <el-form-item label="图表色系设置">
                        <el-button-group>
                        <el-button type="primary" size="small" @click="addColor(component)">添加<i class="el-icon-plus el-icon--right"></i></el-button>
                         <el-button type="danger" size="small" @click="clearColor(component)">清空<i class="el-icon-delete el-icon--right"></i></el-button>
                        </el-button-group>
                    </el-form-item>
                    <el-form-item label="系统色系">
                        <el-select v-model="systemColor" placeholder="请选择" style="width:150px" @change="changeSystemColor">
                          <el-option
                            v-for="item in screenConstants.systemChartColorsNames"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item><br>
                    <div v-if="component.type.toLowerCase().indexOf('map')<0">
                    <el-form-item label="" v-for="(item,index) in component.spec.color" :key="index" >
                        <el-color-picker v-model="component.spec.color[index]" size="small" @change="commonUtil.reLoadChart(chartsComponents,component)"></el-color-picker><br>
                        <el-button @click="confirmDeleteColor(index)" type="primary" size="small" icon="icon-delete" circle></el-button>
                    </el-form-item><br v-if="component.spec.color && component.spec.color.length > 0">
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('map')>=0">
                    <el-form-item label="" v-for="(item,index) in component.spec.color.range" :key="index" >
                        <el-color-picker v-model="component.spec.color.range[index]" size="small" @change="commonUtil.reLoadChart(chartsComponents,component)"></el-color-picker><br>
                        <el-button @click="confirmDeleteMapColor(index)" type="primary" size="small" icon="icon-delete" circle></el-button>
                    </el-form-item><br v-if="component.spec.color.range && component.spec.color.range.length > 0">
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('wordcloud')<0">
                    <el-form-item label="标题设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="是否显示">
                        <el-switch v-model="component.spec.title.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标题内容" v-if="component.spec.title.visible">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.title.text"></el-input>
                    </el-form-item>
                    <el-form-item label="对齐方式" v-if="component.spec.title.visible">
                        <el-select v-model="component.spec.title.align" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in selectUtil.textAlign"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                     <el-form-item v-if="component.spec.title.visible" label="字体颜色">
                        <input-color-picker :value="component.spec.title.textStyle.fill" @change="(val)=>{component.spec.title.textStyle.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item><br>
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('pie')>=0">
                        <el-form-item label="饼图设置" class="customLabel">
                        </el-form-item><br>
                         <el-form-item label="外半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('1')"></el-button>
                              <el-input style="width:80px"  @change="changeRadius('1')" v-model="component.spec.outerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('1')"></el-button>
                        </el-form-item>
                        <el-form-item label="内半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('2')"></el-button>
                            <el-input style="width:80px"  @change="changeRadius('2')" v-model="component.spec.innerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('2')"></el-button>
                        </el-form-item>
                        <el-form-item label="扇区间隔" >
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.padAngle"></el-input>
                        </el-form-item>
                        <el-form-item label="圆角" >
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.pie.style.cornerRadius"></el-input>
                        </el-form-item>
                        <el-form-item label="轮播动画">
                            <el-switch v-model="component.spec.isLoop" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item><br>
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('radar')>=0">
                        <el-form-item label="雷达图设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="折线宽度" >
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.line.style.lineWidth"></el-input>
                        </el-form-item>
                        <el-form-item label="区域显示">
                            <el-switch v-model="component.spec.area.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item><br>
                        <el-form-item label="圆形网格">
                            <el-switch v-model="component.spec.axes[0].grid.smooth" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item><br>
                        <el-form-item  label="坐标轴颜色">
                        <input-color-picker :inputWidth='160' :value="component.spec.axes[0].grid.style.stroke" @change="(val)=>{component.spec.axes[0].grid.style.stroke=val;changeRadarColor(chartsComponents,component)}" />
                      </el-form-item><br>
                      <el-form-item  label="轴标签颜色">
                        <input-color-picker :inputWidth='160' :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;changeRadarLabelColor(chartsComponents,component)}" />
                      </el-form-item><br>
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('scatter')>=0">
                        <el-form-item label="散点设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="散点大小" >
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.size"></el-input>
                        </el-form-item>
                    </div>
                     <div v-if="component.type.toLowerCase().indexOf('gauge')>=0">
                        <el-form-item label="仪表盘设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="上边距">
                            <el-input   @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.padding.top"></el-input>
                        </el-form-item>
                        <el-form-item label="下边距">
                            <el-input   @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.padding.bottom"></el-input>
                        </el-form-item>
                         <el-form-item label="外半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('1')"></el-button>
                            <el-input style="width:120px"  @change="changeRadius('1')" v-model="component.spec.outerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('1')"></el-button>
                        </el-form-item>
                        <el-form-item label="内半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('2')"></el-button>
                            <el-input style="width:120px"  @change="changeRadius('2')" v-model="component.spec.innerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('2')"></el-button>
                        </el-form-item>
                        <el-form-item label="起始角度">
                            <el-input   @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.startAngle"></el-input>
                        </el-form-item>
                        <el-form-item label="结束角度">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.endAngle"></el-input>
                        </el-form-item>
                        <el-form-item  label="仪表盘背景色">
                            <input-color-picker :inputWidth="150" :value="component.spec.gauge.track.style.fill" @change="(val)=>{component.spec.gauge.track.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="最小值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].min"></el-input>
                        </el-form-item>
                        <el-form-item label="最大值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].max"></el-input>
                        </el-form-item>
                        <el-form-item  label="标签字体颜色">
                            <input-color-picker :inputWidth="140" :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="标签字体大小">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].label.style.fontSize" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item label="指标卡是否显示" v-if="component.type.toLowerCase().indexOf('seriesgauge')<0">
                          <el-switch v-model="component.spec.indicator.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                          </el-switch>
                        </el-form-item>
                        <el-form-item label="上边距" v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.indicator.offsetY"></el-input>
                        </el-form-item>
                         <el-form-item label="指标卡字体大小" v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible">
                           <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.indicator.content.style.fontSize" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item  label="指标卡字体颜色" v-if="component.type.toLowerCase().indexOf('seriesgauge')<0 && component.spec.indicator.visible">
                            <input-color-picker :inputWidth="140" :value="component.spec.indicator.content.style.fill" @change="(val)=>{component.spec.indicator.content.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <div v-if="component.type.toLowerCase().indexOf('seriesgauge')>=0">
                        <el-form-item label="数值标签设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="标签是否显示">
                            <el-switch v-model="component.spec.gauge.label.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="标签位置" v-if="component.spec.gauge.label.visible">
                            <el-select  v-model="component.spec.gauge.label.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.pieLabelPosition"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item  label="标签颜色" v-if="component.spec.gauge.label.visible">
                            <input-color-picker :value="component.spec.gauge.label.style.fill" @change="(val)=>{component.spec.gauge.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="字体大小" v-if="component.spec.gauge.label.visible">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.gauge.label.style.fontSize"></el-input>
                        </el-form-item>
                        <el-form-item label="标签格式" v-if="component.spec.gauge.label.visible">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.gauge.label.formatter" style="width:120px"></el-input>
                            &nbsp;<el-button link type="primary" @click="formatterRule()">设置规则</el-button>
                        </el-form-item>
                        </div>
                        <el-form-item label="图例设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="是否显示">
                            <el-switch v-model="component.spec.legends.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="图例位置" v-if="component.spec.legends.visible">
                            <el-select v-model="component.spec.legends.orient" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.legendOrient"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="对齐方式" v-if="component.spec.legends.visible">
                            <el-select v-model="component.spec.legends.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.legendPosition"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item  label="字体颜色" v-if="component.spec.legends.visible">
                            <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                     </div>
                    <div v-if="component.type.toLowerCase().indexOf('circularprogress')>=0">
                        <el-form-item label="进度条设置" class="customLabel">
                        </el-form-item><br>
                         <el-form-item label="外半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('1')"></el-button>
                            <el-input style="width:120px"  @change="changeRadius('1')" v-model="component.spec.outerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('1')"></el-button>
                        </el-form-item>
                        <el-form-item label="内半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('2')"></el-button>
                            <el-input style="width:120px"  @change="changeRadius('2')" v-model="component.spec.innerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('2')"></el-button>
                        </el-form-item>
                        <!-- <el-form-item label="最小值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].min"></el-input>
                        </el-form-item>
                        <el-form-item label="最大值">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.axes[0].max"></el-input>
                        </el-form-item> -->
                        <el-form-item label="指标卡是否显示">
                          <el-switch v-model="component.spec.indicator.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                          </el-switch>
                        </el-form-item>
                        <el-form-item label="指标卡字体大小" v-if="component.spec.indicator.visible">
                           <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.indicator.title.style.fontSize" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item  label="指标卡字体颜色" v-if="component.spec.indicator.visible">
                            <input-color-picker :inputWidth="140" :value="component.spec.indicator.title.style.fill" @change="(val)=>{component.spec.indicator.title.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="指标卡触发类型">
                            <el-select v-model="component.spec.indicator.trigger" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:140px">
                            <el-option
                                v-for="item in screenConstants.triggerType"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="图例设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="是否显示">
                            <el-switch v-model="component.spec.legends.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="图例位置" v-if="component.spec.legends.visible">
                            <el-select v-model="component.spec.legends.orient" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.legendOrient"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="对齐方式" v-if="component.spec.legends.visible">
                            <el-select v-model="component.spec.legends.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.legendPosition"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item  label="字体颜色" v-if="component.spec.legends.visible">
                            <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                     </div>
                     <div v-if="component.type.toLowerCase().indexOf('barprogress')>=0">
                        <el-form-item label="进度条设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="进度条宽度">
                            <el-input style="width:140px"  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.bandWidth"></el-input>
                        </el-form-item>
                        <el-form-item label="圆角">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.cornerRadius"></el-input>
                        </el-form-item>
                        <el-form-item  label="x轴字体">
                            <input-color-picker :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item>
                        <el-form-item  label="y轴字体">
                            <input-color-picker :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item>
                        <el-form-item label="y轴位置" >
                            <el-select v-model="component.spec.axes[0].orient" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.orient"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                     </div>
                     <div v-if="component.type.toLowerCase().indexOf('liquid')>=0">
                        <el-form-item label="水波图设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="形状" >
                            <el-select v-model="component.spec.maskShape" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                            <el-option
                                v-for="item in screenConstants.liquidShape"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                            </el-option>
                            </el-select>
                        </el-form-item>
                         <el-form-item label="指标卡是否显示">
                          <el-switch v-model="component.spec.indicator.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                          </el-switch>
                        </el-form-item>
                        <el-form-item label="指标卡字体大小" v-if="component.spec.indicator.visible">
                           <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.indicator.title.style.fontSize" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item  label="指标卡字体颜色" v-if="component.spec.indicator.visible">
                            <input-color-picker :inputWidth="140" :value="component.spec.indicator.title.style.fill" @change="(val)=>{component.spec.indicator.title.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                     </div>
                     <div v-if="component.type.toLowerCase().indexOf('map')>=0">
                        <el-form-item label="地图设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="地图类型" >
                         <el-select style="width:170px" v-model="component.spec.map" filterable placeholder="地图类型"  @change="changeMapType(component)">
                          <el-option
                            v-for="item in screenConstants.map"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                      </el-select>
                      </el-form-item>
                      <el-form-item  label="默认填充色">
                            <input-color-picker :inputWidth="140" :value="component.spec.defaultFillColor" @change="(val)=>{component.spec.defaultFillColor=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="标签设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="标签是否显示">
                            <el-switch v-model="component.spec.label.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                            </el-switch>
                        </el-form-item>
                        <el-form-item  label="标签颜色" v-if="component.spec.label.visible">
                            <input-color-picker :value="component.spec.label.style.fill" @change="(val)=>{component.spec.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                        </el-form-item><br>
                        <el-form-item label="字体大小" v-if="component.spec.label.visible">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.label.style.fontSize"></el-input>
                        </el-form-item>
                        <el-form-item label="标签格式" v-if="component.spec.label.visible">
                            <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.label.formatter" style="width:120px"></el-input>
                            &nbsp;<el-button link type="primary" @click="formatterRule()">设置规则</el-button>
                        </el-form-item>
                         <el-form-item label="地区名称映射" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="">
                            <div v-for="(value,key) in component.spec.nameMap" :key="key">
                            <el-tag :title="key" style="width:100px">{{key.length>6?key.substring(0,6)+'...':key}}</el-tag> <b style="color:white">-></b> 
                            <el-input v-model="component.spec.nameMap[key]" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:120px" size="small"></el-input>
                            </div>
                        </el-form-item>
                     </div>
                    <div v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0 || component.type.toLowerCase().indexOf('histogram')>=0">
                    <el-form-item :label="getColorLabel()+'设置'" class="customLabel">
                    </el-form-item><br>
                    <el-form-item :label="getColorLabel()+'宽度'" v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.line.style.lineWidth"></el-input>
                    </el-form-item>
                    <el-form-item :label="getColorLabel()+'宽度'" v-if="component.type.toLowerCase().indexOf('histogram')>=0">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.barWidth"></el-input>
                    </el-form-item>
                    <el-form-item label="圆角" v-if="component.type.toLowerCase().indexOf('histogram')>=0">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.bar.style.cornerRadius"></el-input>
                    </el-form-item>
                    <el-form-item label="折线标签是否显示"  v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0">
                        <el-switch v-model="component.spec.lineLabel.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标签位置" v-if="(component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0) && component.spec.lineLabel.visible">
                        <el-select v-model="component.spec.lineLabel.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.lineLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="标签颜色" v-if="(component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0) && component.spec.lineLabel.visible">
                        <input-color-picker :value="component.spec.lineLabel.style.fill" @change="(val)=>{component.spec.lineLabel.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item><br>
                    <el-form-item label="坐标轴设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item  label="x轴字体">
                        <input-color-picker :value="component.spec.axes[0].label.style.fill" @change="(val)=>{component.spec.axes[0].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                    </el-form-item>
                    <el-form-item label="开启自动旋转">
                        <el-switch v-model="component.spec.axes[0].label.autoRotate" @change="changeAutoRotate(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="开启自动省略">
                        <el-switch v-model="component.spec.axes[0].label.autoLimit" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item  label="y轴字体">
                        <input-color-picker :value="component.spec.axes[1].label.style.fill" @change="(val)=>{component.spec.axes[1].label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item>
                      <el-form-item label="开启自动省略">
                        <el-switch v-model="component.spec.axes[1].label.autoLimit" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item><br>
                    </div>
                    <div v-if="component.type.toLowerCase().indexOf('wordcloud')<0 && component.type.toLowerCase().indexOf('gauge')<0 && component.type.toLowerCase().indexOf('circularprogress')<0 && component.type.toLowerCase().indexOf('barprogress')<0 && component.type.toLowerCase().indexOf('liquid')<0 && component.type.toLowerCase().indexOf('map')<0 && component.type.toLowerCase().indexOf('boxplot')<0">
                    <el-form-item label="标签设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="标签是否显示">
                        <el-switch v-model="component.spec.label.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标签位置" v-if="component.spec.label.visible && component.type.toLowerCase().indexOf('funnel')<0">
                        <el-select v-if="component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0 || component.type.toLowerCase().indexOf('histogram')>=0 " v-model="component.spec.label.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.labelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                        <el-select v-if="component.type.toLowerCase().indexOf('pie')>=0 " v-model="component.spec.label.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.pieLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                        <el-select v-if="component.type.toLowerCase().indexOf('scatter')>=0 " v-model="component.spec.label.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.scatterLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                        <el-select v-if="component.type.toLowerCase().indexOf('radar')>=0 " v-model="component.spec.label.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.radarLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="标签颜色" v-if="component.spec.label.visible">
                        <input-color-picker :value="component.spec.label.style.fill" @change="(val)=>{component.spec.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item><br>
                      <el-form-item label="字体大小" v-if="component.spec.label.visible">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.label.style.fontSize"></el-input>
                    </el-form-item>
                    <el-form-item label="标签格式" v-if="component.spec.label.visible">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.label.formatter" style="width:120px"></el-input>
                        &nbsp;<el-button link type="primary" @click="formatterRule()">设置规则</el-button>
                    </el-form-item>
                    <el-form-item label="图例设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="是否显示">
                        <el-switch v-model="component.spec.legends.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="图例位置" v-if="component.spec.legends.visible">
                        <el-select v-model="component.spec.legends.orient" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.legendOrient"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="对齐方式" v-if="component.spec.legends.visible">
                        <el-select v-model="component.spec.legends.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.legendPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="字体颜色" v-if="component.spec.legends.visible">
                        <input-color-picker :value="component.spec.legends.item.label.style.fill" @change="(val)=>{component.spec.legends.item.label.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                      </el-form-item><br>
                      <div v-if="component.type.toLowerCase().indexOf('funnel')>=0">
                        <el-form-item label="漏斗图形状" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="漏斗图形状" >
                        <el-select v-model="component.spec.shape" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.legendOrient"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                        <el-form-item label="外部标签配置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="是否显示">
                        <el-switch v-model="component.spec.outerLabel.visible" @change="commonUtil.reLoadChart(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标签位置" v-if="component.spec.outerLabel.visible">
                        <el-select v-model="component.spec.outerLabel.position" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.legendOrient"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="字体大小" v-if="component.spec.outerLabel.visible">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model.number="component.spec.outerLabel.style.fontSize"></el-input>
                    </el-form-item>
                    <el-form-item  label="字体颜色" v-if="component.spec.outerLabel.visible">
                        <input-color-picker :value="component.spec.outerLabel.style.fill" @change="(val)=>{component.spec.outerLabel.style.fill=val;commonUtil.reLoadChart(chartsComponents,component)}" />
                    </el-form-item>
                    </div>
                    </div>
                    <el-form-item label="提示框配置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="提示框标题">
                        <el-input  @change="commonUtil.reLoadChart(chartsComponents,component)" v-model="component.spec.tooltip.mark.title.value" style="width:120px"></el-input>
                    </el-form-item><br>
                    <el-form-item label="动画设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="动画效果">
                        <el-select v-model="component.amination" placeholder="请选择" @change="commonUtil.changeHistogramAmination(chartsComponents,component)" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.baranimation"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </el-collapse-item>
        </el-collapse>
        <el-dialog title="添加" v-model="addColorDialogVisiable" :close-on-click-modal='false' @close='closeAddDialog'>
             <el-form :inline="true" class="demo-form-inline" ref="addColorRef" size="small">
                <el-form-item label="颜色"  key="color">
                    <input-color-picker :value="color" @change="(val)=>{color=val}" />
                </el-form-item>
             </el-form>
             <template #footer>
             <span  class="dialog-footer">
                    <el-button @click="closeAddDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddColor" size="small">确 定</el-button>
                </span>
             </template>
        </el-dialog>
    </div>
</template>

<script>
import InputColorPicker from '../../colorpicker/inputColorPicker.vue'
import VChart from '@visactor/vchart';
export default {
    components: {
        InputColorPicker
    },
    props:{
        component:{
            type:Object,
            default:()=>({})
        },
        chartsComponents:{
            type:Object,
            default:() => ({}),
        },
    },
    mounted() {
        this.predefineColors = this.commonConstants.predefineColors;
    },
    data(){
        return{
            predefineColors:[],
            addColorDialogVisiable:false,
            color:"",
            systemColor:"",
        }
    },
    methods:{
        addColor(){
            this.addColorDialogVisiable = true;
        },
        clearColor(){
            this.systemColor = "";
            if(this.component.type.toLowerCase().indexOf('map')>=0){
                this.component.spec.color.range = [];
            }else{
                this.component.spec.color = [];
            }
            this.commonUtil.reLoadChart(this.chartsComponents,this.component)
        },
        confirmAddColor(){
            if(!this.color){
                this.commonUtil.showMessage({ message: '请选择颜色', type: this.commonConstants.messageType.error })
                return;
            }
            if(this.component.type.toLowerCase().indexOf('map')>=0){
                this.component.spec.color.range.push(this.color);
            }else{
                this.component.spec.color.push(this.color);
            }
            
            this.commonUtil.reLoadChart(this.chartsComponents,this.component)
            this.closeAddDialog();
        },
        closeAddDialog(){
            this.addColorDialogVisiable = false;
            this.color = "";
        },
        confirmDeleteColor(index){
            this.component.spec.color.splice(index,1)
            this.commonUtil.reLoadChart(this.chartsComponents,this.component)
        },
        confirmDeleteMapColor(index){
            this.component.spec.color.range.splice(index,1)
            this.commonUtil.reLoadChart(this.chartsComponents,this.component)
        },
        changeAutoRotate(chartsComponents,component){
            component.spec.axes[0].sampling = !component.spec.axes[0].label.autoRotate;
            this.commonUtil.reLoadChart(chartsComponents,component);
        },
        getColorLabel(){
            let type = this.component.type.toLowerCase();
            let label = "";
            if(type.indexOf("histogram")>=0){
                label = "柱体";
            }else if(type.indexOf("line")>=0 || type.indexOf("area")>=0){
                label= "折线";
            }
            return label;
        },
        radiusMiuns(type){
            if(type == 1){//外半径
                this.component.spec.outerRadius = (Number(this.component.spec.outerRadius) - 0.1).toFixed(1);
            }else{//内半径
                this.component.spec.innerRadius = (Number(this.component.spec.innerRadius) - 0.1).toFixed(1);
            }
           this.changeRadius(type)
        },
        radiusPlus(type){
            if(type == 1){//外半径
                this.component.spec.outerRadius = (Number(this.component.spec.outerRadius) + 0.1).toFixed(1)
            }else{//内半径
                this.component.spec.innerRadius = (Number(this.component.spec.innerRadius) + 0.1).toFixed(1)
            }
            this.changeRadius(type)
        },
        changeRadius(type){
            if(type == 1){
                if(this.component.spec.outerRadius > 1){
                    this.component.spec.outerRadius = 1.0;
                }else if(this.component.spec.outerRadius <0.1){
                    this.component.spec.outerRadius = 0.1;
                }
            }else{
                if(this.component.spec.innerRadius > 1){
                    this.component.spec.innerRadius = 1.0;
                }else if(this.component.spec.innerRadius <0){
                    this.component.spec.innerRadius = 0.0;
                }
            }
            this.component.spec.outerRadius = Number(this.component.spec.outerRadius);
            this.component.spec.innerRadius = Number(this.component.spec.innerRadius);
            this.commonUtil.reLoadChart(this.chartsComponents,this.component);
        },
        formatterRule(){
            window.open("https://www.visactor.io/vchart/guide/tutorial_docs/Chart_Plugins/Formatter");
        },
        changeRadarColor(chartsComponents,component){
            component.spec.axes[1].domainLine.style.stroke = component.spec.axes[0].grid.style.stroke;
            component.spec.axes[1].grid.style.stroke = component.spec.axes[0].grid.style.stroke;
            this.commonUtil.reLoadChart(chartsComponents,component);
        },
        changeRadarLabelColor(chartsComponents,component){
            component.spec.axes[0].label.style.fill = component.spec.axes[1].label.style.fill
            this.commonUtil.reLoadChart(chartsComponents,component);
        },
        async changeMapType(component){
            let mapCode = component.spec.map;
            
            if(!VChart.getMap(mapCode)){
                const geojson = await this.commonUtil.getMapData(mapCode);
                VChart.registerMap(mapCode, geojson);
            }
            component.spec.nameMap = this.screenConstants.nameMap[mapCode];
            this.commonUtil.reLoadChart(this.chartsComponents,this.component);
        },
        changeSystemColor(){
            let colors = this.screenConstants.systemChartColors[this.systemColor];
            if(this.component.type.toLowerCase().indexOf('map')>=0){
                this.component.spec.color.range = colors;
            }else{
                this.component.spec.color = colors;
            }
            this.commonUtil.reLoadChart(this.chartsComponents,this.component);
        }
    }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:5px !important
}
:deep(.el-form-item__label-wrap){
    margin-left:0px !important
}
:deep(.el-color-picker__trigger){
    /* top:3px */
}
:deep(.customLabel){
    font-weight: bold;
}
:deep(.customLabel .el-form-item__label){
    color:#15a585 !important;
}
:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  background-color: var(--colorWhite);
  color: var(--colorTextPrimary);
  border: 1px solid var(--borderColorBase);
}

:deep(.el-select-dropdown) {
   border: 1px solid var(--borderColorBase) !important;
   background-color: var(--colorWhite) !important;
 }

 :deep(.el-select__selected-item){
    color:var(--colorTextPrimary) !important;
  
}

:deep(.el-select--small .el-select__wrapper){
    background-color: var(--colorWhite) !important;
    box-shadow: 0 0 0 1px black inset;
}

:deep(.el-input--small .el-input__wrapper){
    /* padding: 0px 0px; */
    background-color: var(--colorWhite);
    box-shadow: 0 0 0 1px black inset;
}
</style>
