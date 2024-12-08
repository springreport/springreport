<!-- 图表设置组件 -->
<template>
    <div>
                <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left" size="small" ref="settingForm" >
                     <el-form-item label="图表类型" class="customLabel">
                        <span>{{getChartType()}}</span>
                    </el-form-item><br>
                    <div v-if="!isPreview">
                    <el-form-item label="数据设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="数据集" v-if="!isCoedit">
                        <el-select v-model="component.dataset" placeholder="请选择" style="width:140px" @change="changeDataset(false)">
                          <el-option
                            v-for="item in datasets"
                            :key="item.datasetName"
                            :label="item.datasetName"
                            :value="item.datasetName">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分组字段" v-if="!isCoedit">
                        <el-select v-model="component.categoryField" placeholder="请选择" style="width:140px">
                          <el-option
                            v-for="item in datasetColumns"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数值字段" v-if="!isCoedit">
                        <el-select v-model="component.valueField" placeholder="请选择" style="width:140px" multiple>
                          <el-option
                            v-for="item in datasetColumns"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="系列字段" v-if="component.chartAllType.indexOf('pie')<=0 &&  !isCoedit">
                        <el-select v-model="component.seriesField" placeholder="请选择" style="width:140px" multiple>
                          <el-option
                            v-for="item in datasetColumns"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="行列转置">
                        <el-switch v-model="component.rangeConfigCheck" @change="rowColTrans()">
                        </el-switch>
                    </el-form-item><br>
                    </div>
                    <el-form-item label="色系设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="">
                        <el-button-group>
                        <el-button type="primary" size="small" @click="addColor(component)">添加<i class="el-icon-plus el-icon--right"></i></el-button>
                         <el-button type="danger" size="small" @click="clearColor(component)">清空<i class="el-icon-delete el-icon--right"></i></el-button>
                        </el-button-group>
                    </el-form-item>
                    <el-form-item label="系统色系">
                        <el-select v-model="systemColor" placeholder="请选择" style="width:140px" @change="changeSystemColor">
                          <el-option
                            v-for="item in screenConstants.systemChartColorsNames"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item><br>
                    <el-form-item label="" v-for="(item,index) in component.defaultOption.spec.color" :key="index" >
                        <el-color-picker v-model="component.defaultOption.spec.color[index]" size="small" @change="reLoadChart()"></el-color-picker><br>
                        <el-button @click="confirmDeleteColor(index)" type="primary" size="small" icon="icon-delete" circle></el-button>
                    </el-form-item><br v-if="component.defaultOption.spec.color && component.defaultOption.spec.color.length > 0">
                    <el-form-item label="标题设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="是否显示">
                        <el-switch v-model="component.defaultOption.spec.title.visible" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标题内容" v-if="component.defaultOption.spec.title.visible">
                        <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.title.text" style="width:140px"></el-input>
                    </el-form-item>
                    <el-form-item label="对齐方式" v-if="component.defaultOption.spec.title.visible">
                        <el-select v-model="component.defaultOption.spec.title.align" placeholder="请选择" @change="reLoadChart()" style="width:140px">
                          <el-option
                            v-for="item in selectUtil.textAlign"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                     <el-form-item v-if="component.defaultOption.spec.title.visible" label="字体颜色">
                        <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.title.textStyle.fill" @change="(val)=>{component.defaultOption.spec.title.textStyle.fill=val;reLoadChart()}" />
                      </el-form-item><br>
                    <div v-if="component.chartAllType.toLowerCase().indexOf('pie')>=0">
                        <el-form-item label="饼图设置" class="customLabel">
                        </el-form-item><br>
                         <el-form-item label="外半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('1')"></el-button>
                            <el-input style="width:80px"  @change="changeRadius('1')" v-model="component.defaultOption.spec.outerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('1')"></el-button>
                        </el-form-item>
                        <el-form-item label="内半径(0-1)">
                             <el-button link type="primary" icon="icon-minus" @click="radiusMiuns('2')"></el-button>
                            <el-input style="width:80px"  @change="changeRadius('2')" v-model="component.defaultOption.spec.innerRadius"></el-input>
                             <el-button link type="primary" icon="icon-plus" @click="radiusPlus('2')"></el-button>
                        </el-form-item>
                        <el-form-item label="扇区间隔" >
                            <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.padAngle" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item label="圆角" >
                            <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.pie.style.cornerRadius" style="width:170px"></el-input>
                        </el-form-item>
                    </div>
                    <div v-if="component.chartAllType.toLowerCase().indexOf('radar')>=0">
                        <el-form-item label="雷达图设置" class="customLabel">
                        </el-form-item><br>
                        <el-form-item label="折线宽度" >
                            <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.line.style.lineWidth" style="width:140px"></el-input>
                        </el-form-item>
                        <el-form-item label="区域显示">
                            <el-switch v-model="component.defaultOption.spec.area.visible" @change="reLoadChart()">
                            </el-switch>
                        </el-form-item><br>
                        <el-form-item label="圆形网格">
                            <el-switch v-model="component.defaultOption.spec.axes[0].grid.smooth" @change="reLoadChart()">
                            </el-switch>
                        </el-form-item><br>
                        <el-form-item  label="坐标轴颜色">
                        <input-color-picker :inputWidth='140' :value="component.defaultOption.spec.axes[0].grid.style.stroke" @change="(val)=>{component.defaultOption.spec.axes[0].grid.style.stroke=val;changeRadarColor(chartsComponents,component)}" />
                      </el-form-item><br>
                      <el-form-item  label="轴标签颜色">
                        <input-color-picker :inputWidth='140' :value="component.defaultOption.spec.axes[1].label.style.fill" @change="(val)=>{component.defaultOption.spec.axes[1].label.style.fill=val;changeRadarLabelColor(chartsComponents,component)}" />
                      </el-form-item><br>
                    </div>
                    <div v-if="component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0 || component.chartAllType.toLowerCase().indexOf('column')>=0 || component.chartAllType.toLowerCase().indexOf('bar')>=0">
                    <el-form-item :label="getColorLabel()+'设置'" class="customLabel">
                    </el-form-item><br>
                    <el-form-item :label="getColorLabel()+'宽度'" v-if="component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0">
                        <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.line.style.lineWidth" style="width:140px"></el-input>
                    </el-form-item>
                    <el-form-item :label="getColorLabel()+'宽度'" v-if="component.chartAllType.toLowerCase().indexOf('column')>=0 || component.chartAllType.toLowerCase().indexOf('bar')>=0">
                        <el-input  @change="reLoadChart()" v-model="component.defaultOption.spec.barWidth" style="width:140px"></el-input>
                    </el-form-item>
                    <el-form-item label="圆角" v-if="component.chartAllType.toLowerCase().indexOf('column')>=0 || component.chartAllType.toLowerCase().indexOf('bar')>=0">
                        <el-input  @change="reLoadChart()" v-model.number="component.defaultOption.spec.bar.style.cornerRadius"></el-input>
                    </el-form-item>
                    <el-form-item label="折线标签是否显示"  v-if="component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0">
                        <el-switch v-model="component.defaultOption.spec.lineLabel.visible" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标签位置" v-if="(component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0) && component.defaultOption.spec.lineLabel.visible">
                        <el-select v-model="component.defaultOption.spec.lineLabel.position" placeholder="请选择" @change="reLoadChart()" style="width:180px">
                          <el-option
                            v-for="item in screenConstants.lineLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="标签颜色" v-if="(component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0) && component.defaultOption.spec.lineLabel.visible">
                        <input-color-picker :value="component.defaultOption.spec.lineLabel.style.fill" @change="(val)=>{component.defaultOption.spec.lineLabel.style.fill=val;reLoadChart()}" />
                      </el-form-item><br>
                    <el-form-item label="坐标轴设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item  label="x轴字体">
                        <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.axes[0].label.style.fill" @change="(val)=>{component.defaultOption.spec.axes[0].label.style.fill=val;reLoadChart()}" />
                    </el-form-item>
                    <el-form-item label="开启自动旋转">
                        <el-switch v-model="component.defaultOption.spec.axes[0].label.autoRotate" @change="changeAutoRotate(chartsComponents,component)">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="开启自动省略">
                        <el-switch v-model="component.defaultOption.spec.axes[0].label.autoLimit" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item>
                    <el-form-item  label="y轴字体">
                        <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.axes[1].label.style.fill" @change="(val)=>{component.defaultOption.spec.axes[1].label.style.fill=val;reLoadChart()}" />
                      </el-form-item>
                      <el-form-item label="开启自动省略">
                        <el-switch v-model="component.defaultOption.spec.axes[1].label.autoLimit" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item><br>
                    </div>
                    <el-form-item label="标签设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="标签是否显示">
                        <el-switch v-model="component.defaultOption.spec.label.visible" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="标签位置" v-if="component.defaultOption.spec.label.visible">
                        <el-select v-if="component.chartAllType.toLowerCase().indexOf('line')>=0 || component.chartAllType.toLowerCase().indexOf('area')>=0 || component.chartAllType.toLowerCase().indexOf('column')>=0 || component.chartAllType.toLowerCase().indexOf('bar')>=0" v-model="component.defaultOption.spec.label.position" placeholder="请选择" @change="reLoadChart()" style="width:140px">
                          <el-option
                            v-for="item in screenConstants.labelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                        <el-select v-if="component.chartAllType.toLowerCase().indexOf('pie')>=0 " v-model="component.defaultOption.spec.label.position" placeholder="请选择" @change="reLoadChart()" style="width:140px">
                          <el-option
                            v-for="item in screenConstants.pieLabelPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="标签颜色" v-if="component.defaultOption.spec.label.visible">
                        <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.label.style.fill" @change="(val)=>{component.defaultOption.spec.label.style.fill=val;reLoadChart()}" />
                      </el-form-item><br>
                      <el-form-item label="字体大小" v-if="component.defaultOption.spec.label.visible">
                        <el-input  @change="reLoadChart()" v-model.number="component.defaultOption.spec.label.style.fontSize" style="width:140px"></el-input>
                    </el-form-item>
                    <el-form-item label="标签格式" v-if="component.defaultOption.spec.label.visible">
                        <el-input  @change="reLoadChart()" v-model.number="component.defaultOption.spec.label.formatter" style="width:100px"></el-input>
                        &nbsp;<el-button link type="primary" @click="formatterRule()">设置规则</el-button>
                    </el-form-item>
                    <el-form-item label="图例设置" class="customLabel">
                    </el-form-item><br>
                    <el-form-item label="是否显示">
                        <el-switch v-model="component.defaultOption.spec.legends.visible" @change="reLoadChart()">
                        </el-switch>
                    </el-form-item>
                    <el-form-item label="图例位置" v-if="component.defaultOption.spec.legends.visible">
                        <el-select v-model="component.defaultOption.spec.legends.orient" placeholder="请选择" @change="reLoadChart()" style="width:140px">
                          <el-option
                            v-for="item in screenConstants.legendOrient"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="对齐方式" v-if="component.defaultOption.spec.legends.visible">
                        <el-select v-model="component.defaultOption.spec.legends.position" placeholder="请选择" @change="reLoadChart()" style="width:140px">
                          <el-option
                            v-for="item in screenConstants.legendPosition"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                          </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  label="字体颜色" v-if="component.defaultOption.spec.legends.visible">
                        <input-color-picker :inputWidth="140" :value="component.defaultOption.spec.legends.item.label.style.fill" @change="(val)=>{component.defaultOption.spec.legends.item.label.style.fill=val;reLoadChart()}" />
                      </el-form-item><br>
                </el-form>
        <el-dialog title="添加" v-model="addColorDialogVisiable" :close-on-click-modal='false' @close='closeAddDialog'>
             <el-form :inline="true" class="demo-form-inline" ref="addColorRef" size="small">
                <el-form-item label="颜色"  key="color">
                    <input-color-picker :value="color" @change="(val)=>{color=val}" />
                </el-form-item>
             </el-form>
             <template #footer>
             <span class="dialog-footer">
                    <el-button @click="closeAddDialog" size="small">取 消</el-button>
                    <el-button type="primary" @click="confirmAddColor" size="small">确 定</el-button>
                </span>
             </template>
        </el-dialog>
    </div>
</template>

<script>
import InputColorPicker from '@/components/screen/colorpicker/inputColorPicker.vue'
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
        datasets:{
            type:Array,
            default:()=>[]
        },
        isPreview:{
            type:Boolean,
            default:false
        },
        isCoedit:{
            type:Boolean,
            default:true
        }
    },
    mounted() {
        this.changeDataset(true);
        this.predefineColors = this.commonConstants.predefineColors;
    },
    data(){
        return{
            predefineColors:[],
            addColorDialogVisiable:false,
            color:"",
            systemColor:"",
            datasetColumns:[],
        }
    },
    methods:{
        addColor(){
            this.addColorDialogVisiable = true;
        },
        clearColor(){
            this.systemColor = "";
            if(this.component.chartAllType.toLowerCase().indexOf('map')>=0){
                this.component.defaultOption.spec.color.range = [];
            }else{
                this.component.defaultOption.spec.color = [];
            }
            this.reLoadChart()
        },
        confirmAddColor(){
            if(!this.color){
                this.commonUtil.showMessage({ message: '请选择颜色', type: this.commonConstants.messageType.error })
                return;
            }
            if(this.component.chartAllType.toLowerCase().indexOf('map')>=0){
                this.component.defaultOption.spec.color.range.push(this.color);
            }else{
                this.component.defaultOption.spec.color.push(this.color);
            }
            
            this.reLoadChart()
            this.closeAddDialog();
        },
        closeAddDialog(){
            this.addColorDialogVisiable = false;
            this.color = "";
        },
        confirmDeleteColor(index){
            this.component.defaultOption.spec.color.splice(index,1)
            this.reLoadChart()
        },
        confirmDeleteMapColor(index){
            this.component.defaultOption.spec.color.range.splice(index,1)
            this.reLoadChart()
        },
        changeAutoRotate(chartsComponents,component){
            component.defaultOption.spec.axes[0].sampling = !component.defaultOption.spec.axes[0].label.autoRotate;
            this.reLoadChart();
        },
        getColorLabel(){
            let type = this.component.chartAllType.toLowerCase();
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
                this.component.defaultOption.spec.outerRadius = (Number(this.component.defaultOption.spec.outerRadius) - 0.1).toFixed(1);
            }else{//内半径
                this.component.defaultOption.spec.innerRadius = (Number(this.component.defaultOption.spec.innerRadius) - 0.1).toFixed(1);
            }
           this.changeRadius(type)
        },
        radiusPlus(type){
            if(type == 1){//外半径
                this.component.defaultOption.spec.outerRadius = (Number(this.component.defaultOption.spec.outerRadius) + 0.1).toFixed(1)
            }else{//内半径
                this.component.defaultOption.spec.innerRadius = (Number(this.component.defaultOption.spec.innerRadius) + 0.1).toFixed(1)
            }
            this.changeRadius(type)
        },
        changeRadius(type){
            if(type == 1){
                if(this.component.defaultOption.spec.outerRadius > 1){
                    this.component.defaultOption.spec.outerRadius = 1.0;
                }else if(this.component.defaultOption.spec.outerRadius <0.1){
                    this.component.defaultOption.spec.outerRadius = 0.1;
                }
            }else{
                if(this.component.defaultOption.spec.innerRadius > 1){
                    this.component.defaultOption.spec.innerRadius = 1.0;
                }else if(this.component.defaultOption.spec.innerRadius <0){
                    this.component.defaultOption.spec.innerRadius = 0.0;
                }
            }
            this.component.defaultOption.spec.outerRadius = Number(this.component.defaultOption.spec.outerRadius);
            this.component.defaultOption.spec.innerRadius = Number(this.component.defaultOption.spec.innerRadius);
            this.reLoadChart();
        },
        formatterRule(){
            window.open("https://www.visactor.io/vchart/guide/tutorial_docs/Chart_Plugins/Formatter");
        },
        changeRadarColor(chartsComponents,component){
            component.defaultOption.spec.axes[1].domainLine.style.stroke = component.defaultOption.spec.axes[0].grid.style.stroke;
            component.defaultOption.spec.axes[1].grid.style.stroke = component.defaultOption.spec.axes[0].grid.style.stroke;
            this.reLoadChart();
        },
        changeRadarLabelColor(chartsComponents,component){
            component.defaultOption.spec.axes[0].label.style.fill = component.defaultOption.spec.axes[1].label.style.fill
            this.reLoadChart();
        },
        changeSystemColor(){
            let colors = this.screenConstants.systemChartColors[this.systemColor];
            if(this.component.chartAllType.toLowerCase().indexOf('map')>=0){
                this.component.defaultOption.spec.color.range = colors;
            }else{
                this.component.defaultOption.spec.color = colors;
            }
            this.reLoadChart();
        },
        reLoadChart(){
            luckysheet.renderChart();
        },
        rowColTrans(){
            luckysheet.rowColTrans(this.component);
        },
        changeDataset(isEdit){
            for (let index = 0; index < this.datasets.length; index++) {
                const element = this.datasets[index];
                if(!isEdit){
                    this.component.categoryField = "";
                    this.component.valueField = [];
                    this.component.seriesField = [];
                }
                if(element.datasetName == this.component.dataset){
                    if(!element.columns || element.columns.length == 0){
                        this.getDatasetColumns(element,"1");
                    }else{
                        this.datasetColumns = element.columns;
                    }
                    break;
                }
            }
        },
        getDatasetColumns(element,type) {
            const obj = {
                url: this.apis.reportDesign.getDataSetColumnsApi,
                params: { id: element.id },
                removeEmpty: false
            }
            var that = this;
            this.commonUtil.doPost(obj).then(response => {
                element.columns = response.responseData;
                that.datasetColumns = element.columns;
            })
        },
        getChartType(){
            let chartAllType = this.component.chartAllType;
            if(chartAllType == "echarts|line|default"){
                return "折线图";
            }else if(chartAllType == "echarts|line|smooth"){
                return "平滑折线图";
            }else if(chartAllType == "echarts|area|default"){
                return "面积图";
            }else if(chartAllType == "echarts|area|stack"){
                return "堆叠面积图";
            }else if(chartAllType == "echarts|column|default"){
                return "柱状图";
            }else if(chartAllType == "echarts|column|stack"){
                return "堆叠柱状图";
            }else if(chartAllType == "echarts|bar|default"){
                return "条形图";
            }else if(chartAllType == "echarts|bar|stack"){
                return "堆叠条形图";
            }else if(chartAllType == "echarts|pie|default"){
                return "饼图";
            }else if(chartAllType == "echarts|radar|default"){
                return "雷达图";
            }
        }
    },
    watch: {
    'component.dataset'(newValue, oldValue) {
        this.changeDataset(true);
    }
  }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:5px !important;
  margin-right:5px !important
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


</style>
