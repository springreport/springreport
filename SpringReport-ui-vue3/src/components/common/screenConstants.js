//大屏相关的模板用到的常量
const screenConstants = {
}

//主题
screenConstants.vchartthemes = [
    {name:"亮色主题",value:"light"},
    {name:"暗色主题",value:"dark"},
    {name:"火山蓝",value:"vScreenVolcanoBlue"},
    {name:"清新蜡笔",value:"vScreenClean"},
    {name:"郊外",value:"vScreenOutskirts"},
    {name:"汽车蓝橙",value:"vScreenBlueOrange"},
    {name:"金融黄",value:"vScreenFinanceYellow"},
    {name:"文旅青",value:"vScreenWenLvCyan"},
    {name:"电力绿",value:"vScreenElectricGreen"},
    {name:"电商紫",value:"vScreenECommercePurple"},
    {name:"红蓝",value:"vScreenRedBlue"},
    {name:"党建红",value:"vScreenPartyRed"},
    {name:"源力亮色",value:"veODesignLight"},
    {name:"源力亮色-金融",value:"veODesignLightFinance"},
    {name:"源力亮色-政府",value:"veODesignLightGovernment"},
    {name:"源力亮色-消费",value:"veODesignLightConsumer"},
    {name:"源力亮色-汽车",value:"veODesignLightAutomobile"},
    {name:"源力亮色-文旅",value:"veODesignLightCulturalTourism"},
    {name:"源力亮色-医疗",value:"veODesignLightMedical"},
    {name:"源力亮色-新能源",value:"veODesignLightNewEnergy"},
    {name:"源力暗色",value:"veODesignDark"},
    {name:"源力暗色-金融",value:"veODesignDarkFinance"},
    {name:"源力暗色-政府",value:"veODesignDarkGovernment"},
    {name:"源力暗色-消费",value:"veODesignDarkConsumer"},
    {name:"源力暗色-汽车",value:"veODesignDarkAutomobile"},
    {name:"源力暗色-文旅",value:"veODesignDarkCulturalTourism"},
    {name:"源力暗色-医疗",value:"veODesignDarkMedical"},
    {name:"源力暗色-新能源",value:"veODesignDarkNewEnergy"},
]

screenConstants.labelPosition = [
    {name:"outside",value:"outside"},
    {name:"top",value:"top"},
    {name:"bottom",value:"bottom"},
    {name:"left",value:"left"},
    {name:"right",value:"right"},
    {name:"inside",value:"inside"},
    {name:"inside-top",value:"inside-top"},
    {name:"inside-bottom",value:"inside-bottom"},
    {name:"inside-right",value:"inside-right"},
    {name:"inside-left",value:"inside-left"},
    {name:"top-right",value:"top-right"},
    {name:"top-left",value:"top-left"},
    {name:"bottom-right",value:"bottom-right"},
    {name:"bottom-left",value:"bottom-left"},
]

screenConstants.lineLabelPosition = [
    {name:"start",value:"start"},
    {name:"end",value:"end"},
]

screenConstants.pieLabelPosition = [
    {name:"outside",value:"outside"},
    {name:"inside",value:"inside"},
    {name:"inside-outer",value:"inside-outer"},
    {name:"inside-inner",value:"inside-inner"}
]

screenConstants.scatterLabelPosition = [
    {name:"top",value:"top"},
    {name:"bottom",value:"bottom"},
    {name:"left",value:"left"},
    {name:"right",value:"right"},
    {name:"top-right",value:"top-right"},
    {name:"top-left",value:"top-left"},
    {name:"bottom-right",value:"bottom-right"},
    {name:"bottom-left",value:"bottom-left"},
    {name:"center",value:"center"}
]

screenConstants.radarLabelPosition = [
    {name:"top",value:"top"},
    {name:"bottom",value:"bottom"},
    {name:"left",value:"left"},
    {name:"right",value:"right"},
]

screenConstants.legendOrient = [
    {name:"left",value:"left"},
    {name:"top",value:"top"},
    {name:"bottom",value:"bottom"},
    {name:"right",value:"right"},
]

screenConstants.legendPosition = [
    {name:"起始",value:"start"},
    {name:"居中",value:"middle"},
    {name:"末尾",value:"end"},
]

screenConstants.orient = [
  {name:"left",value:"left"},
  {name:"right",value:"right"},
]

screenConstants.baranimation=[
    {name:"无动画",value:""},
    {name:"渐入动画(依次)",value:"fadeIn"},
    {name:"渐入动画(整体)",value:"fadeIn2"},
    {name:"缩放动画(依次)",value:"scaleIn"},
    {name:"缩放动画(整体)",value:"scaleIn2"},
    {name:"移入动画(依次)",value:"moveIn"},
    {name:"移入动画(整体)",value:"moveIn2"},
    {name:"高度生长动画(依次)",value:"growHeightIn"},
    {name:"高度生长动画(整体)",value:"growHeightIn2"},
    {name:"宽度生长动画(依次)",value:"growWidthIn"},
    {name:"宽度生长动画(整体)",value:"growWidthIn2"},
    {name:"中心生长动画(依次)",value:"growCenterIn"},
    {name:"中心生长动画(整体)",value:"growCenterIn2"},
]

screenConstants.triggerType = [
  {name:"不可更新",value:"none"},
  {name:"鼠标选中",value:"select"},
  {name:"鼠标悬停",value:"hover"},
]

screenConstants.liquidShape = [
  {name:"drop",value:"drop"},
  {name:"circle",value:"circle"},
  {name:"cross",value:"cross"},
  {name:"diamond",value:"diamond"},
  {name:"square",value:"square"},
  {name:"arrow",value:"arrow"},
  {name:"arrow2Left",value:"arrow2Left"},
  {name:"arrow2Right",value:"arrow2Right"},
  {name:"wedge",value:"wedge"},
  {name:"thinTriangle",value:"thinTriangle"},
  {name:"triangle",value:"triangle"},
  {name:"triangleUp",value:"triangleUp"},
  {name:"triangleDown",value:"triangleDown"},
  {name:"triangleRight",value:"triangleRight"},
  {name:"triangleLeft",value:"triangleLeft"},
  {name:"star",value:"star"},
  {name:"wye",value:"wye"},
  {name:"rect",value:"rect"},
]

screenConstants.tableAlign = [
  {name:"left",value:"left"},
  {name:"center",value:"center"},
  {name:"right",value:"right"},
]

screenConstants.funnelShape = [
  { name: '梯形', value: 'trapezoid' },
  { name: '矩形', value: 'rect' },
]

screenConstants.type = {
    text:"text",//文本
    numberFlipper:'numberFlipper',//翻牌器
    date:"date",//日期时间
    picture:"picture",//图片
    border:"border",//边框
    decoration:"decoration",//装饰
    histogram:"histogram",//基础柱状图
    stackHistogram:"stackHistogram",//堆叠柱状图
    rangeHistogram:"rangeHistogram",//区间柱状图
    horizontalHistogram:"horizontalHistogram",//条形图
    rangeHorizontalHistogram:"rangeHorizontalHistogram",//区间条形图
    stackHorizontalHistogram:"stackHorizontalHistogram",//堆叠条形图
    line:"line",//折线图
    horizontalLine:"horizontalLine",//垂直折线图
    smoothLine:"smoothLine",//平滑折线图
    stepLine:"stepLine",//阶梯折线图
    stackLine:"stackLine",//堆叠折线图
    area:"area",//面积图
    horizontalArea:"horizontalArea",//垂直面积图
    smoothArea:"smoothArea",//平滑面积图
    stepArea:"stepArea",//阶梯面积图
    stackArea:"stackArea",//堆叠面积图
    pie:"pie",//饼图
    pieRose:"pieRose",//玫瑰图
    scatter:"scatter",//散点图
    radar:"radar",//雷达图
    stackRadar:"stackRadar",//堆叠雷达图
    funnel:"funnel",//漏斗图
    gauge:"gauge",//仪表盘
    tickGauge:"tickGauge",//刻度线模式仪表盘
    seriesGauge:"seriesGauge",//系列仪表盘
    tickSeriesGauge:"tickSeriesGauge",//刻度系列仪表盘
    circularProgress:"circularProgress",//环形进度条
    barProgress:"barProgress",//条形进度条
    liquid:"liquid",//水波图
    wordCloud:"wordCloud",//词云
    basicMap:"basicMap",//基础地图
    scatterMap: 'scatterMap', // 散点地图
    scrollTable:"scrollTable",//循环表格
    pageTable:"pageTable",//分页表格
    boxPlot:"boxPlot",//基础箱型图
    circlePacking:"circlePacking",//基础气泡图
    transformFunnel:"transformFunnel",//转化漏斗图
    zhifangtu:"zhifangtu",//直方图
    sankey: 'sankey',// 桑葚图
    comboCharthl: 'comboCharthl',// 折柱图
    comboChartdbbar: 'comboChartdbbar',// 双向条形图
    cardList: 'cardList',// 卡片列表
}

screenConstants.category = {
   panel:"panel",//画板
   vchart:"vchart",//图表类别 
   text:"text",//文本类别 
   picture:"picture",//文本类别 
   border:"border",//边框
   decoration:"decoration",//装饰
   table:"table",
   numberFlipper: 'numberFlipper', // 数字翻牌器
   cardList:'cardList',//卡片列表
}

//大屏组件类型
// 大屏组件类型
screenConstants.componentsType1 = [
  { 'type': 'panel', 'text': '画板', icon: 'panel' },
  { 'type': 'text', 'text': '文本', icon: 'text' },
  { 'type': 'numberFlipper', 'text': '数字翻牌器', icon: 'text' },
  { 'type': 'picture', 'text': '图片', icon: 'picture' },
  { 'type': 'border', 'text': '边框', icon: 'border' },
  { 'type': 'decoration', 'text': '装饰', icon: 'decoration' },
  { 'type': 'histogram', 'text': '柱状图', icon: 'histogram' },
  { 'type': 'line', 'text': '折线图', icon: 'line' },
  { 'type': 'area', 'text': '面积图', icon: 'area' },
  { 'type': 'pie', 'text': '饼图', icon: 'pie' },
  { 'type': 'boxPlot', 'text': '箱型图', icon: 'boxPlot' },
  { 'type': 'zhifangtu', 'text': '直方图', icon: 'histogram' },
  { 'type': 'sankey', 'text': '桑葚图', icon: 'sankey' },
  { 'type': 'circlePacking', 'text': '气泡图', icon: 'circlePacking' },
  { 'type': 'scatter', 'text': '散点图', icon: 'scatter' },
  { 'type': 'radar', 'text': '雷达图', icon: 'radar' },
  { 'type': 'funnel', 'text': '漏斗图', icon: 'funnel' },
  { 'type': 'gauge', 'text': '仪表盘', icon: 'gauge' },
  { 'type': 'progress', 'text': '进度条', icon: 'progress' },
  { 'type': 'wordCloud', 'text': '词云图', icon: 'wordCloud' },
  { 'type': 'comboChart', 'text': '组合图', icon: 'comboChart' },
  { 'type': 'map', 'text': '地图', icon: 'map' },
  { 'type': 'table', 'text': '表格', icon: 'table' }
]
screenConstants.componentsType2 = [

] 

screenConstants.compType = {
    text: { text: '文本', icon: 'text'},
    date: { text: '日期时间', icon: 'date'},
    numberFlipper: { text: '数字翻牌器', icon: 'numberFlipper' },
    picture: { text: '图片', icon: 'picture'},
    border: { text: '边框', icon: 'border'},
    decoration: { text: '装饰', icon: 'decoration'},
    histogram: { text: '柱状图', icon: 'histogram' },
    stackHistogram: { text: '堆叠柱状图', icon: 'histogram' },
    rangeHistogram: { text: '区间柱状图', icon: 'histogram' },
    horizontalHistogram: { text: '条形图', icon: 'histogram' },
    stackHorizontalHistogram: { text: '堆叠条形图', icon: 'histogram' },
    rangeHorizontalHistogram: { text: '区间条形图', icon: 'histogram' },
    line: { text: '折线图', icon: 'line' },
    horizontalLine: { text: '垂直折线图', icon: 'line' },
    smoothLine: { text: '平滑折线图', icon: 'line' },
    stepLine: { text: '阶梯折线图', icon: 'line' },
    stackLine: { text: '堆叠折线图', icon: 'line' },
    area: { text: '面积图', icon: 'area' },
    horizontalArea: { text: '垂直面积图', icon: 'area' },
    smoothArea: { text: '平滑面积图', icon: 'area' },
    stepArea: { text: '阶梯面积图', icon: 'line' },
    stackArea: { text: '堆叠面积图', icon: 'area' },
    pie: { text: '饼图', icon: 'pie' },
    pieRose: { text: '玫瑰图', icon: 'pieRose'},
    scatter: { text: '散点图', icon: 'scatter'},
    radar: { text: '雷达图', icon: 'radar'},
    stackRadar: { text: '堆叠雷达图', icon: 'radar'},
    wordCloud: { text: '词云图', icon: 'wordCloud'},
    funnel: { text: '漏斗图', icon: 'funnel'},
    gauge: { text: '仪表盘', icon: 'gauge'},
    tickGauge: { text: '刻度仪表盘', icon: 'gauge'},
    seriesGauge: { text: '系列仪表盘', icon: 'gauge'},
    tickSeriesGauge: { text: '刻度系列仪表盘', icon: 'gauge'},
    progress: { text: '进度条', icon: 'progress'},
    circularProgress: { text: '环形进度条', icon: 'circleProgress'},
    barProgress: { text: '条形进度条', icon: 'progress'},
    wordCloud: { text: '词云图', icon: 'wordCloud'},
    liquid: { text: '水波图', icon: 'liquid'},
    basicMap: { text: '地图', icon: 'map'},
    scatterMap: { text: '地图', icon: 'map' },
    scrollTable: { text: '循环表格', icon: 'table'},
    pageTable: { text: '分页表格', icon: 'table'},
    boxPlot: { text: '箱型图', icon: 'boxPlot'},
    circlePacking: { text: '气泡图', icon: 'circlePacking'},
    transformFunnel: { text: '转化漏斗图', icon: 'funnel'},
    zhifangtu: { text: '直方图', icon: 'histogram' },
    sankey: { text: '桑葚图', icon: 'sankey' },
    comboCharthl: { text: '折柱图', icon: 'comboChart' },
    comboChartdbbar: { text: '双向条形图', icon: 'comboChartdbbar' },
    cardList: { text: '卡片列表', icon: 'cardList' }
}

screenConstants.textType = [
  {"name":"text","text":"文本","src":"https://www.springreport.vip/images/chart/text.png",category:screenConstants.category.text},
  {"name":"date","text":"日期时间","src":"https://www.springreport.vip/images/chart/time.png",category:screenConstants.category.text},
]

screenConstants.numberFlipperType = [
  { 'name': 'numberFlipper', 'text': '数字翻牌器', 'src': 'https://www.springreport.vip/images/chart/numberflipper.png', category: screenConstants.category.numberFlipper },
]

screenConstants.pictureType = [
  {"name":"picture","text":"图片","src":"",category:screenConstants.category.picture},
]

screenConstants.borderType = [
  {"name":"dv-border-box-1","text":"边框1","src":"https://www.springreport.vip/images/chart/border1.png",category:screenConstants.category.border},
  {"name":"dv-border-box-2","text":"边框2","src":"https://www.springreport.vip/images/chart/border2.png",category:screenConstants.category.border},
  {"name":"dv-border-box-3","text":"边框3","src":"https://www.springreport.vip/images/chart/border3.png",category:screenConstants.category.border},
  {"name":"dv-border-box-4","text":"边框4","src":"https://www.springreport.vip/images/chart/border4.png",category:screenConstants.category.border},
  {"name":"dv-border-box-4","text":"边框4(反向)","src":"https://www.springreport.vip/images/chart/rborder4.png",category:screenConstants.category.border},
  {"name":"dv-border-box-5","text":"边框5","src":"https://www.springreport.vip/images/chart/border5.png",category:screenConstants.category.border},
  {"name":"dv-border-box-5","text":"边框5(反向)","src":"https://www.springreport.vip/images/chart/rborder5.png",category:screenConstants.category.border},
  {"name":"dv-border-box-6","text":"边框6","src":"https://www.springreport.vip/images/chart/border6.png",category:screenConstants.category.border},
  {"name":"dv-border-box-7","text":"边框7","src":"https://www.springreport.vip/images/chart/border7.png",category:screenConstants.category.border},
  {"name":"dv-border-box-8","text":"边框8","src":"https://www.springreport.vip/images/chart/border8.png",category:screenConstants.category.border},
  {"name":"dv-border-box-8","text":"边框8(反向)","src":"https://www.springreport.vip/images/chart/border8.png",category:screenConstants.category.border},
  {"name":"dv-border-box-9","text":"边框9","src":"https://www.springreport.vip/images/chart/border9.png",category:screenConstants.category.border},
  {"name":"dv-border-box-10","text":"边框10","src":"https://www.springreport.vip/images/chart/border10.png",category:screenConstants.category.border},
  {"name":"dv-border-box-11","text":"边框11","src":"https://www.springreport.vip/images/chart/border11.png",category:screenConstants.category.border},
  {"name":"dv-border-box-12","text":"边框12","src":"https://www.springreport.vip/images/chart/border12.png",category:screenConstants.category.border},
  {"name":"dv-border-box-13","text":"边框13","src":"https://www.springreport.vip/images/chart/border13.png",category:screenConstants.category.border},
]
screenConstants.decorationType = [
  {"name":"dv-decoration-1","text":"装饰1","src":"https://www.springreport.vip/images/chart/decoration1.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-2","text":"装饰2","src":"https://www.springreport.vip/images/chart/decoration2.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-2","text":"装饰2(反向)","src":"https://www.springreport.vip/images/chart/rdecoration2.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-3","text":"装饰3","src":"https://www.springreport.vip/images/chart/decoration3.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-4","text":"装饰4","src":"https://www.springreport.vip/images/chart/decoration4.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-4","text":"装饰4(反向)","src":"https://www.springreport.vip/images/chart/rdecoration4.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-5","text":"装饰5","src":"https://www.springreport.vip/images/chart/decoration5.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-6","text":"装饰6","src":"https://www.springreport.vip/images/chart/decoration6.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-7","text":"装饰7","src":"https://www.springreport.vip/images/chart/decoration7.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-8","text":"装饰8","src":"https://www.springreport.vip/images/chart/rdecoration8.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-8","text":"装饰8(反向)","src":"https://www.springreport.vip/images/chart/decoration8.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-9","text":"装饰9","src":"https://www.springreport.vip/images/chart/decoration9.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-10","text":"装饰10","src":"https://www.springreport.vip/images/chart/decoration10.png",category:screenConstants.category.decoration},
  {"name":"dv-decoration-11","text":"装饰111","src":"https://www.springreport.vip/images/chart/decoration11.png",category:screenConstants.category.decoration},
]
//柱状图类型
screenConstants.histogramType = [
    {"name":"histogram","text":"柱状图","src":"https://www.springreport.vip/images/chart/histogram.png",category:screenConstants.category.vchart},
    {"name":"stackHistogram","text":"堆叠柱状图","src":"https://www.springreport.vip/images/chart/stackHistogram.png",category:screenConstants.category.vchart},
    {"name":"rangeHistogram","text":"区间柱状图","src":"https://www.springreport.vip/images/chart/rangeHistogram.png",category:screenConstants.category.vchart},
    {"name":"horizontalHistogram","text":"条形图","src":"https://www.springreport.vip/images/chart/horizontalHistogram.png",category:screenConstants.category.vchart},
    {"name":"stackHorizontalHistogram","text":"堆叠条形图","src":"https://www.springreport.vip/images/chart/stackHorizontalHistogram.png",category:screenConstants.category.vchart},
    {"name":"rangeHorizontalHistogram","text":"区间条形图","src":"https://www.springreport.vip/images/chart/rangeHorizontalHistogram.png",category:screenConstants.category.vchart},
] 

screenConstants.lineType = [
    {"name":"line","text":"折线图","src":"https://www.springreport.vip/images/chart/line.png",category:screenConstants.category.vchart},
    {"name":"horizontalLine","text":"垂直折线图","src":"https://www.springreport.vip/images/chart/horizontalLine.png",category:screenConstants.category.vchart},
    {"name":"smoothLine","text":"平滑折线图","src":"https://www.springreport.vip/images/chart/smoothLine.png",category:screenConstants.category.vchart},
    {"name":"stepLine","text":"阶梯折线图","src":"https://www.springreport.vip/images/chart/stepLine.png",category:screenConstants.category.vchart},
    {"name":"stackLine","text":"堆叠折线图","src":"https://www.springreport.vip/images/chart/stackLine.png",category:screenConstants.category.vchart},
] 

screenConstants.areaType = [
    {"name":"area","text":"面积图","src":"https://www.springreport.vip/images/chart/area.png",category:screenConstants.category.vchart},
    {"name":"horizontalArea","text":"垂直面积图","src":"https://www.springreport.vip/images/chart/horizontalArea.png",category:screenConstants.category.vchart},
    {"name":"smoothArea","text":"平滑面积图","src":"https://www.springreport.vip/images/chart/smoothArea.png",category:screenConstants.category.vchart},
    {"name":"stepArea","text":"阶梯面积图","src":"https://www.springreport.vip/images/chart/stepArea.png",category:screenConstants.category.vchart},
    {"name":"stackArea","text":"堆叠面积图","src":"https://www.springreport.vip/images/chart/stackArea.png",category:screenConstants.category.vchart},
] 
screenConstants.pieType = [
    {"name":"pie","text":"饼图","src":"https://www.springreport.vip/images/chart/pie.png",category:screenConstants.category.vchart},
    {"name":"pieRose","text":"玫瑰图","src":"https://www.springreport.vip/images/chart/pieRose.png",category:screenConstants.category.vchart},
] 

screenConstants.scatterType = [
    {"name":"scatter","text":"散点图","src":"https://www.springreport.vip/images/chart/scatter.png",category:screenConstants.category.vchart},
]

screenConstants.radarType = [
    {"name":"radar","text":"雷达图","src":"https://www.springreport.vip/images/chart/radar.png",category:screenConstants.category.vchart},
    {"name":"stackRadar","text":"堆叠雷达图","src":"https://www.springreport.vip/images/chart/stackRadar.png",category:screenConstants.category.vchart}
]

screenConstants.wordCloudType = [
    {"name":"wordCloud","text":"词云图","src":"https://www.springreport.vip/images/chart/wordCloud.png",category:screenConstants.category.vchart},
]

screenConstants.funnelType = [
    {"name":"funnel","text":"漏斗图","src":"https://www.springreport.vip/images/chart/funnel.png",category:screenConstants.category.vchart},
    {"name":"transformFunnel","text":"转化漏斗图","src":"https://www.springreport.vip/images/chart/funnel.png",category:screenConstants.category.vchart},
]

screenConstants.gaugeType = [
    {"name":"gauge","text":"仪表盘","src":"https://www.springreport.vip/images/chart/gauge.png",category:screenConstants.category.vchart},
    {"name":"tickGauge","text":"刻度仪表盘","src":"https://www.springreport.vip/images/chart/tickGauge.png",category:screenConstants.category.vchart},
    {"name":"seriesGauge","text":"系列仪表盘","src":"https://www.springreport.vip/images/chart/seriesGauge.png",category:screenConstants.category.vchart},
    {"name":"tickSeriesGauge","text":"刻度系列仪表盘","src":"https://www.springreport.vip/images/chart/tickSeriesGauge.png",category:screenConstants.category.vchart},
]

screenConstants.progressType = [
  {"name":"circularProgress","text":"环形进度条","src":"https://www.springreport.vip/images/chart/circularProgress.png",category:screenConstants.category.vchart},
  {"name":"barProgress","text":"条形进度条","src":"https://www.springreport.vip/images/chart/linearProgress.png",category:screenConstants.category.vchart},
  {"name":"liquid","text":"水波图","src":"https://www.springreport.vip/images/chart/liquid.png",category:screenConstants.category.vchart},
]

screenConstants.mapType = [
  { 'name': 'basicMap', 'text': '基础地图', 'src': 'https://www.springreport.vip/images/chart/map.png', category: screenConstants.category.vchart },
  { 'name': 'scatterMap', 'text': '散点地图', 'src': 'https://www.springreport.vip/images/chart/scatterMap.png', category: screenConstants.category.vchart }
]

screenConstants.tableType = [
  {"name":"scrollTable","text":"轮播列表","src":"https://www.springreport.vip/images/chart/scrollTable.png",category:screenConstants.category.table},
  {"name":"pageTable","text":"分页列表","src":"https://www.springreport.vip/images/chart/pageTable.png",category:screenConstants.category.table},
  { 'name': 'cardList', 'text': '卡片列表', 'src': 'https://www.springreport.vip/images/chart/cardlist.png', category: screenConstants.category.table }
]

screenConstants.boxPlotType = [
  {"name":"boxPlot","text":"箱型图","src":"https://www.springreport.vip/images/chart/boxPlot.png",category:screenConstants.category.vchart},
]

screenConstants.circlePackingType = [
  {"name":"circlePacking","text":"气泡图","src":"https://www.springreport.vip/images/chart/circlePacking.png",category:screenConstants.category.vchart},
]

screenConstants.zhifangtuType = [
  {"name":"zhifangtu","text":"直方图","src":"https://www.springreport.vip/images/chart/zhifangtu.png",category:screenConstants.category.vchart},
]

screenConstants.sankeyType = [
  { 'name': 'sankey', 'text': '桑葚图', 'src': 'https://www.springreport.vip/images/chart/sankey.png', category: screenConstants.category.vchart }
]

screenConstants.comboChartType = [
  { 'name': 'comboCharthl', 'text': '折柱图', 'src': 'https://www.springreport.vip/images/chart/comboCharthl.png', category: screenConstants.category.vchart },
  { 'name': 'comboChartdbbar', 'text': '双向条形图', 'src': 'https://www.springreport.vip/images/chart/comboChartdbbar.png', category: screenConstants.category.vchart },
]

screenConstants.chartTitleSettings={
    visible:false,
    text:"",
    align:"center",//水平对齐方式，left right center
    verticalAlign:"top", //垂直对齐方式 'top' 'middle' 'bottom'
    textStyle:{
        // fill:'',//颜色
        // fontSize:'',
    },
    innerPadding:-10
}

screenConstants.tooltipSettings={
  visible: true,
  mark: {
      title: {
        value: ''
      }
  }
}

screenConstants.textInit = {
  type: screenConstants.type.text,
  category:screenConstants.category.text,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 300, // 组件初始化宽度
  h: 100, // 组件初始化高度
  speed: 10, // 滚动速度
  active: false,
  zindex: '99',
  locked:false,
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  hiddenParamSize:0,//隐藏参数个数
  textType: 'text', // 文本类型
  style: {
    display: 'table-cell', textAlign: 'center', verticalAlign: 'middle', fontSize: '14', color: '#ffffff',colorEnd:"#ffffff", letterSpacing: '1', background: '',backgroundEnd:'',backgroundDirection:'bottom', fontWeight: false,direction:'bottom'
  },
  content: '文本',
  spec:{
    data: {
      values:[ { value: '文本'}]
    },
    valueField:'value',
  }
}

screenConstants.numberFlipperInit = {
  type: screenConstants.type.numberFlipper,
  category: screenConstants.category.numberFlipper,
  isDelete: false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 100, // 组件初始化高度
  active: false,
  zindex: '99',
  locked: false,
  isborder: false, // 是否添加边框
  borderType: '', // 边框类型
  borderColor: [], // 边框颜色
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource: '1', // 数据来源 1静态数据 2动态数据
  dynamicDataSettings: {
    datasetId: '', // 数据集
    dataColumns: []// 数据列
  }, // 动态数据配置
  params: [], // 图表参数
  hiddenParamSize: 0, // 隐藏参数个数
  style: {
     display: 'table-cell', textAlign: 'center', verticalAlign: 'middle', background: 'rgba(0, 206, 209, 1)', fontSize: '24', fontColor: '#ffffff', 
     duration:1000,prefix:"",prefixColor:"#FFF",prefixFontSize:"14",suffix:"",suffixColor:"#FFF",suffixFontSize:"14",boxHeight:"80",boxWidth:"60",
     thousandSeparator:false,decimalPlaces:0
  },
  content: '666666',
  spec: {
    data: {
      values: [{ value: '666666' }]
    },
    valueField: 'value'
  }
}

screenConstants.dateInit = {
  type: screenConstants.type.date,
  category:screenConstants.category.text,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 300, // 组件初始化宽度
  h: 100, // 组件初始化高度
  active: false,
  zindex: '99',
  locked:false,
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  style: {
    display: 'table-cell', textAlign: 'center', verticalAlign: 'middle', fontSize: '14', color: '#ffffff',colorEnd:"#ffffff", letterSpacing: '1', background: '',backgroundEnd:'',backgroundDirection:'bottom', fontWeight: false,direction:'bottom'
  },
  content: '',
  format: '1'
}

screenConstants.pictureInit = {
  type: screenConstants.type.picture,
  category:screenConstants.category.picture,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 300, // 组件初始化宽度
  h: 300, // 组件初始化高度
  imgUrl: '',
  zindex: 98
}

screenConstants.dvBorderBoxInit = {
  type: screenConstants.type.border,
  category:screenConstants.category.border,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  name:"dv-border-box-1",
  zindex: 98,
  color:['#0AEEDF', '#0B90EF'],
  backgroundColor:"",
  content:"",
  titleWidth:250,
  reverse:false,
  content:"ceshi",
}

screenConstants.dvDecorationBoxInit = {
    type: screenConstants.type.decoration,
    category:screenConstants.category.decoration,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 300, // 组件初始化宽度
    h: 200, // 组件初始化高度
    name:"dv-border-box-1",
    zindex: 98,
    color:['#0AEEDF', '#0B90EF'],
    style: {
     fontSize: '14', color: '#ffffff',letterSpacing: '1',  fontWeight: false
    },
    content:"",
    reverse:false
}

//大屏柱状图初始化数据
screenConstants.histogramInit = {
    type: screenConstants.type.histogram,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'bar',
        color: [],
        data: {
            values: [
                { type: 'Nail polish', country: 'Africa', value: 4229 },
                { type: 'Nail polish', country: 'EU', value: 4376 },
                { type: 'Nail polish', country: 'China', value: 3054 },
                { type: 'Nail polish', country: 'USA', value: 12814 },
                { type: 'Eyebrow pencil', country: 'Africa', value: 3932 },
                { type: 'Eyebrow pencil', country: 'EU', value: 3987 },
                { type: 'Eyebrow pencil', country: 'China', value: 5067 },
                { type: 'Eyebrow pencil', country: 'USA', value: 13012 },
                { type: 'Rouge', country: 'Africa', value: 5221 },
                { type: 'Rouge', country: 'EU', value: 3574 },
                { type: 'Rouge', country: 'China', value: 7004 },
                { type: 'Rouge', country: 'USA', value: 11624 },
            ]
        },
        seriesField: ['country'],
        xField: ['type', 'country'],
        yField: ['value'],
        background: '',//背景颜色
        barWidth:'100%',//柱体宽度
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        bar:{
            style:{
                cornerRadius:0,//圆角
            }
        },
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏区间柱状图初始化数据
screenConstants.rangeHistogramInit = {
  type: screenConstants.type.rangeHistogram,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      'type':'rangeColumn',
      color: [],
      data: {
          values: [
            { type: 'Category One', min: 76, max: 100 },
            { type: 'Category Two', min: 56, max: 108 },
            { type: 'Category Three', min: 38, max: 129 },
            { type: 'Category Four', min: 58, max: 155 },
            { type: 'Category Five', min: 45, max: 120 },
            { type: 'Category Six', min: 23, max: 99 },
            { type: 'Category Seven', min: 18, max: 56 },
            { type: 'Category Eight', min: 18, max: 34 }
          ]
      },
      seriesField: [],
      xField: ['type'],
      yField: ['min','max'],
      background: '',//背景颜色
      barWidth:'100%',//柱体宽度
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      bar:{
          style:{
              cornerRadius:0,//圆角
          }
      },
      label:{
          visible:true,
          position:'outside',
          style:{
              fontSize:14,
              fill:null,
          }
      },
      axes:[
          {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
          {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
      ],
      legends:{
          visible:false,
          orient: 'top',//图例位置
          position:'middle',//对齐方式
          item: {
              label:{
                style:{
                  // fill:'#0BF1DA',//图例字体颜色
                }
              }
          },
      },
  }
}
//大屏堆叠柱状图初始化数据
screenConstants.stackHistogramInit = {
    type: screenConstants.type.histogram,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'bar',
        color: [],
        data: {
            values: [
                { type: 'Nail polish', country: 'Africa', value: 4229 },
                { type: 'Nail polish', country: 'EU', value: 4376 },
                { type: 'Nail polish', country: 'China', value: 3054 },
                { type: 'Nail polish', country: 'USA', value: 12814 },
                { type: 'Eyebrow pencil', country: 'Africa', value: 3932 },
                { type: 'Eyebrow pencil', country: 'EU', value: 3987 },
                { type: 'Eyebrow pencil', country: 'China', value: 5067 },
                { type: 'Eyebrow pencil', country: 'USA', value: 13012 },
                { type: 'Rouge', country: 'Africa', value: 5221 },
                { type: 'Rouge', country: 'EU', value: 3574 },
                { type: 'Rouge', country: 'China', value: 7004 },
                { type: 'Rouge', country: 'USA', value: 11624 },
            ]
        },
        seriesField: ['type'],
        xField: ['country'],
        yField: ['value'],
        stack: true,
        background: '',//背景颜色
        barWidth:'100%',//柱体宽度
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        bar:{
            style:{
                cornerRadius:0,//圆角
            }
        },
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}
//条形图
screenConstants.horizontalHistogramInit = {
    type: screenConstants.type.horizontalHistogram,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'bar',
        color: [],
        data: {
            values: [
                {
                    province: '北京',
                    value: 3080,
                    type: 'top1'
                },
                {
                    province: '天津',
                    value: 2880,
                    type: 'top2'
                },
                {
                    province: '重庆',
                    value: 880,
                    type: 'top3'
                },
                {
                    province: '深圳',
                    value: 780,
                    type: 'common'
                },
                {
                    province: '广州',
                    value: 680,
                    type: 'common'
                },
                {
                    province: '山东',
                    value: 580,
                    type: 'common'
                },
            ]
        },
        direction: 'horizontal',
        seriesField: ['province'],
        xField: ['value'],
        yField: ['province'],
        stack: true,
        background: '',//背景颜色
        barWidth:'100%',//柱体宽度
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        bar:{
            style:{
                cornerRadius:0,//圆角
            }
        },
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

screenConstants.rangeHorizontalHistogramInit = {
  type: screenConstants.type.rangeHorizontalHistogram,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      'type':'rangeColumn',
      color: [],
      data: {
          values: [
            { type: 'Category One', min: 76, max: 100 },
            { type: 'Category Two', min: 56, max: 108 },
            { type: 'Category Three', min: 38, max: 129 },
            { type: 'Category Four', min: 58, max: 155 },
            { type: 'Category Five', min: 45, max: 120 },
            { type: 'Category Six', min: 23, max: 99 },
            { type: 'Category Seven', min: 18, max: 56 },
            { type: 'Category Eight', min: 18, max: 34 }
          ]
      },
      direction: 'horizontal',
      seriesField: [],
      xField: ['min', 'max'],
      yField: ['type'],
      background: '',//背景颜色
      barWidth:'100%',//柱体宽度
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      bar:{
          style:{
              cornerRadius:0,//圆角
          }
      },
      label:{
          visible:true,
          position:'outside',
          style:{
              fontSize:14,
              fill:null,
          }
      },
      axes:[
          {orient:'bottom',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}},
          {orient:'left',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}}
      ],
      legends:{
          visible:false,
          orient: 'top',//图例位置
          position:'middle',//对齐方式
          item: {
              label:{
                style:{
                  // fill:'#0BF1DA',//图例字体颜色
                }
              }
          },
      },
  }
}
//堆叠条形图
screenConstants.stackHorizontalHistogramInit = {
    type: screenConstants.type.stackHorizontalHistogram,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'bar',
        color: [],
        data: {
            values: [
                { type: 'Nail polish', country: 'Africa', value: 4229 },
                { type: 'Nail polish', country: 'EU', value: 4376 },
                { type: 'Nail polish', country: 'China', value: 3054 },
                { type: 'Nail polish', country: 'USA', value: 12814 },
                { type: 'Eyebrow pencil', country: 'Africa', value: 3932 },
                { type: 'Eyebrow pencil', country: 'EU', value: 3987 },
                { type: 'Eyebrow pencil', country: 'China', value: 5067 },
                { type: 'Eyebrow pencil', country: 'USA', value: 13012 },
                { type: 'Rouge', country: 'Africa', value: 5221 },
                { type: 'Rouge', country: 'EU', value: 3574 },
                { type: 'Rouge', country: 'China', value: 7004 },
                { type: 'Rouge', country: 'USA', value: 11624 },
            ]
        },
        seriesField: ['type'],
        xField: ['value'],
        yField: ['country'],
        stack: true,
        background: '',//背景颜色
        barWidth:'100%',//柱体宽度
        direction: 'horizontal',
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        bar:{
            style:{
                cornerRadius:0,//圆角
            }
        },
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'}},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏折线图初始化数据
screenConstants.lineInit = {
    type: screenConstants.type.line,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'line',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false,style:{} },
        line:{
            style:{
                lineWidth:2,
                curveType:"linear"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏垂直折线图初始化数据
screenConstants.horizontalLineInit = {
    type: screenConstants.type.horizontalLine,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'line',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['value'],
        yField: ['time'],
        background: '',//背景颜色
        direction: 'horizontal',
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
                curveType:"linear"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏平滑折线图初始化数据
screenConstants.smoothLineInit = {
    type: screenConstants.type.line,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'line',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
                curveType:"monotone"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏阶梯折线图初始化数据
screenConstants.stepLineInit = {
    type: screenConstants.type.stepLine,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'line',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
                curveType:"stepAfter"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

screenConstants.stackLineInit = {
    type: screenConstants.type.stackLine,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'line',
        color: [],
        data: {
            values: [
                { type: 'Nail polish', country: 'Africa', value: 4229 },
                { type: 'Nail polish', country: 'EU', value: 4376 },
                { type: 'Nail polish', country: 'China', value: 3054 },
                { type: 'Nail polish', country: 'USA', value: 12814 },
                { type: 'Eyebrow pencil', country: 'Africa', value: 3932 },
                { type: 'Eyebrow pencil', country: 'EU', value: 3987 },
                { type: 'Eyebrow pencil', country: 'China', value: 5067 },
                { type: 'Eyebrow pencil', country: 'USA', value: 13012 },
                { type: 'Rouge', country: 'Africa', value: 5221 },
                { type: 'Rouge', country: 'EU', value: 3574 },
                { type: 'Rouge', country: 'China', value: 7004 },
                { type: 'Rouge', country: 'USA', value: 11624 },
                { type: 'Lipstick', country: 'Africa', value: 9256 },
                { type: 'Lipstick', country: 'EU', value: 4376 },
                { type: 'Lipstick', country: 'China', value: 9054 },
                { type: 'Lipstick', country: 'USA', value: 8814 },
                { type: 'Eyeshadows', country: 'Africa', value: 3308 },
                { type: 'Eyeshadows', country: 'EU', value: 4572 },
                { type: 'Eyeshadows', country: 'China', value: 12043 },
                { type: 'Eyeshadows', country: 'USA', value: 12998 },
                { type: 'Eyeliner', country: 'Africa', value: 5432 },
                { type: 'Eyeliner', country: 'EU', value: 3417 },
                { type: 'Eyeliner', country: 'China', value: 15067 },
                { type: 'Eyeliner', country: 'USA', value: 12321 },
                { type: 'Foundation', country: 'Africa', value: 13701 },
                { type: 'Foundation', country: 'EU', value: 5231 },
                { type: 'Foundation', country: 'China', value: 10119 },
                { type: 'Foundation', country: 'USA', value: 10342 },
                { type: 'Lip gloss', country: 'Africa', value: 4008 },
                { type: 'Lip gloss', country: 'EU', value: 4572 },
                { type: 'Lip gloss', country: 'China', value: 12043 },
                { type: 'Lip gloss', country: 'USA', value: 22998 },
                { type: 'Mascara', country: 'Africa', value: 18712 },
                { type: 'Mascara', country: 'EU', value: 6134 },
                { type: 'Mascara', country: 'China', value: 10419 },
                { type: 'Mascara', country: 'USA', value: 11261 }
            ]
        },
        stack: true,
        seriesField: ['country'],
        xField: ['type'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏面积图图初始化数据
screenConstants.areaInit = {
    type: screenConstants.type.area,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'area',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false,style:{} },
        line:{
            style:{
                lineWidth:2,
                curveType:"linear"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}
//大屏垂直面积图图初始化数据
screenConstants.horizontalAreaInit = {
    type: screenConstants.type.horizontalArea,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'area',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['value'],
        yField: ['time'],
        direction: 'horizontal',
        background: '',//背景颜色
        lineLabel: { visible: false,style:{} },
        line:{
            style:{
                lineWidth:2,
                curveType:"linear"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}
//大屏平滑面积图初始化数据
screenConstants.smoothAreaInit = {
    type: screenConstants.type.smoothArea,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'area',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
                curveType:"monotone"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏阶梯面积图初始化数据
screenConstants.stepAreaInit = {
    type: screenConstants.type.stepArea,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'area',
        color: [],
        data: {
            values: [
                {
                  time: '2:00',
                  value: 8
                },
                {
                  time: '4:00',
                  value: 9
                },
                {
                  time: '6:00',
                  value: 11
                },
                {
                  time: '8:00',
                  value: 14
                },
                {
                  time: '10:00',
                  value: 16
                },
                {
                  time: '12:00',
                  value: 17
                },
                {
                  time: '14:00',
                  value: 17
                },
                {
                  time: '16:00',
                  value: 16
                },
                {
                  time: '18:00',
                  value: 15
                }
              ]
        },
        seriesField: [],
        xField: ['time'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
                curveType:"stepAfter"
            }
        },
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}
//堆叠面积图
screenConstants.stackAreaInit = {
    type: screenConstants.type.stackArea,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'area',
        color: [],
        data: {
            values: [
                { type: 'Nail polish', country: 'Africa', value: 4229 },
                { type: 'Nail polish', country: 'EU', value: 4376 },
                { type: 'Nail polish', country: 'China', value: 3054 },
                { type: 'Nail polish', country: 'USA', value: 12814 },
                { type: 'Eyebrow pencil', country: 'Africa', value: 3932 },
                { type: 'Eyebrow pencil', country: 'EU', value: 3987 },
                { type: 'Eyebrow pencil', country: 'China', value: 5067 },
                { type: 'Eyebrow pencil', country: 'USA', value: 13012 },
                { type: 'Rouge', country: 'Africa', value: 5221 },
                { type: 'Rouge', country: 'EU', value: 3574 },
                { type: 'Rouge', country: 'China', value: 7004 },
                { type: 'Rouge', country: 'USA', value: 11624 },
                { type: 'Lipstick', country: 'Africa', value: 9256 },
                { type: 'Lipstick', country: 'EU', value: 4376 },
                { type: 'Lipstick', country: 'China', value: 9054 },
                { type: 'Lipstick', country: 'USA', value: 8814 },
                { type: 'Eyeshadows', country: 'Africa', value: 3308 },
                { type: 'Eyeshadows', country: 'EU', value: 4572 },
                { type: 'Eyeshadows', country: 'China', value: 12043 },
                { type: 'Eyeshadows', country: 'USA', value: 12998 },
                { type: 'Eyeliner', country: 'Africa', value: 5432 },
                { type: 'Eyeliner', country: 'EU', value: 3417 },
                { type: 'Eyeliner', country: 'China', value: 15067 },
                { type: 'Eyeliner', country: 'USA', value: 12321 },
                { type: 'Foundation', country: 'Africa', value: 13701 },
                { type: 'Foundation', country: 'EU', value: 5231 },
                { type: 'Foundation', country: 'China', value: 10119 },
                { type: 'Foundation', country: 'USA', value: 10342 },
                { type: 'Lip gloss', country: 'Africa', value: 4008 },
                { type: 'Lip gloss', country: 'EU', value: 4572 },
                { type: 'Lip gloss', country: 'China', value: 12043 },
                { type: 'Lip gloss', country: 'USA', value: 22998 },
                { type: 'Mascara', country: 'Africa', value: 18712 },
                { type: 'Mascara', country: 'EU', value: 6134 },
                { type: 'Mascara', country: 'China', value: 10419 },
                { type: 'Mascara', country: 'USA', value: 11261 }
            ]
        },
        seriesField: ['country'],
        xField: ['type'],
        yField: ['value'],
        background: '',//背景颜色
        lineLabel: { visible: false ,style:{}},
        line:{
            style:{
                lineWidth:2,
            }
        },
        stack: true,
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'outside',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}
//饼图
screenConstants.pieInit = {
    type: screenConstants.type.pie,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'pie',
        color: [],
        data: {
            values: [
                { type: 'oxygen', value: '46.60' },
                { type: 'silicon', value: '27.72' },
                { type: 'aluminum', value: '8.13' },
                { type: 'iron', value: '5' },
                { type: 'calcium', value: '3.63' },
                { type: 'sodium', value: '2.83' },
                { type: 'potassium', value: '2.59' },
                { type: 'others', value: '3.5' }
            ]
        },
        padAngle:0,//扇区间隔
        outerRadius: 0.8,
        innerRadius:0,
        categoryField: 'type',
        valueField: 'value',
        // seriesField: 'type',
        background: '',//背景颜色
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        isLoop:false,//轮播展示
        pie: {
            style: {
              cornerRadius: 0
            },
            state: {
              hover: {
               centerOffset: 10,
              },
              selected: {
                centerOffset: 10
              },
              active: {
                centerOffset: 10
              }
            }
        },
        interactions: [
          {
            type: 'element-active-by-legend'
          }
        ],
        label:{
            visible:true,
            position:'outside',
            formatter: '',//标签格式
            style:{
                fontSize:14,
                fill:null,
            }
        },
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

screenConstants.pieRoseInit = {
    type: screenConstants.type.pieRose,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'rose',
        color: [],
        data: {
            values: [
                { value: 10, category: 'One' },
                { value: 9, category: 'Two' },
                { value: 6, category: 'Three' },
                { value: 5, category: 'Four' },
                { value: 4, category: 'Five' },
                { value: 3, category: 'Six' },
                { value: 1, category: 'Seven' }
            ]
        },
        padAngle:0,//扇区间隔
        outerRadius: 0.8,
        innerRadius:0,
        categoryField: 'category',
        valueField: 'value',
        seriesField: 'category',
        background: '',//背景颜色
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        pie: {
            style: {
              cornerRadius: 0
            },
            state: {
              hover: {
               centerOffset: 10,
              },
              selected: {
                centerOffset: 10
              }
            }
        },
        label:{
            visible:true,
            position:'outside',
            formatter: '',//标签格式
            style:{
                fontSize:14,
                fill:null,
            }
        },
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏散点图初始化数据
screenConstants.scatterInit = {
    type: screenConstants.type.scatter,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'scatter',
        color: [],
        data: {
            values: [
                {type:'A',x:9,y:81,r:63 },
                {type:'A',x:98,y:5,r:89 },
                {type:'A',x:51,y:50,r:73 },
                {type:'A',x:41,y:22,r:14 },
                {type:'A',x:58,y:24,r:20 },
                {type:'A',x:78,y:37,r:34 },
                {type:'A',x:55,y:56,r:53 },
                {type:'A',x:18,y:45,r:70 },
                {type:'A',x:42,y:44,r:28 },
                {type:'A',x:3,y:52,r:59 },
                {type:'A',x:31,y:18,r:97 },
                {type:'A',x:79,y:91,r:63 },
                {type:'A',x:93,y:23,r:23 },
                {type:'A',x:44,y:83,r:22 }
            ]
        },
        seriesField: 'type',
        xField: 'x',
        yField: 'y',
        sizeField: '',
        size: 20,
        background: '',//背景颜色
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'top',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes:[
            {type: 'linear', orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
            {type: 'linear', orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
        ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏雷达图初始化数据
screenConstants.radarInit = {
    type: screenConstants.type.radar,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'radar',
        color: [],
        data: {
            values: [
                {key:'Strength',value:5},
                {key:'Speed',value:5},
                {key:'Shooting',value:3},
                {key:'Endurance',value:5},
                {key:'Precision',value:5},
                {key:'Growth',value:5}
            ]
        },
        categoryField: 'key',
        valueField: 'value',
        seriesField: '',
        line: {
            style: {
              lineWidth: 1
            }
        },
        point: {
            visible: true,
            style:{
                shape:'circle'
            }
        },
        area: {
            visible: true, // display area
            state: {
              // The style in the hover state of the area
              hover: {
                fillOpacity: 0.5
              }
            }
          },
        radius:0.8,
        background: '',//背景颜色
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'right',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        axes: [
            {
              orient: 'radius',
              label: {
                style:{
                    fill:'#333',
                    fontSize:14
                },
                visible: true
              },
              grid: {
                smooth: true,
                style:{
                  stroke:'#333',
                  lineDash: [0],
                  opacity:0.5
                }
              }
            },
            {
                orient: 'angle',
                domainLine: {
                    visible: true,
                    style: {
                        stroke: '#333',
                        lineDash: [0]
                      }
                },
                grid: {
                     visible: true,
                     style: {
                        stroke: '#333',
                        lineDash: [0],
                        opacity:0.5
                    }
                 },
                 label: {
                    visible: true,
                    space:10,
                    style:{
                      fill:'#333',
                      fontSize:14
                    }
                    
                },
            }
          ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏堆叠雷达图初始化数据
screenConstants.stackRadarInit = {
    type: screenConstants.type.radar,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'radar',
        color: [],
        data: {
            values: [
                {month:'Jan.',value:45,type:'A'},{month:'Feb.',value:61,type:'A'},{month:'Mar.',value:92,type:'A'},{month:'Apr.',value:57,type:'A'},{month:'May.',value:46,type:'A'},{month:'Jun.',value:36,type:'A'},{month:'Jul.',value:33,type:'A'},{month:'Aug.',value:63,type:'A'},{month:'Sep.',value:57,type:'A'},{month:'Oct.',value:53,type:'A'},{month:'Nov.',value:69,type:'A'},{month:'Dec.',value:40,type:'A'},{month:'Jan.',value:31,type:'B'},{month:'Feb.',value:39,type:'B'},{month:'Mar.',value:81,type:'B'},{month:'Apr.',value:39,type:'B'},{month:'May.',value:64,type:'B'},{month:'Jun.',value:21,type:'B'},{month:'Jul.',value:58,type:'B'},{month:'Aug.',value:72,type:'B'},{month:'Sep.',value:47,type:'B'},{month:'Oct.',value:37,type:'B'},{month:'Nov.',value:80,type:'B'},{month:'Dec.',value:74,type:'B'},{month:'Jan.',value:90,type:'C'},{month:'Feb.',value:95,type:'C'},{month:'Mar.',value:62,type:'C'},{month:'Apr.',value:52,type:'C'},{month:'May.',value:74,type:'C'},{month:'Jun.',value:87,type:'C'},{month:'Jul.',value:80,type:'C'},{month:'Aug.',value:69,type:'C'},{month:'Sep.',value:74,type:'C'},{month:'Oct.',value:84,type:'C'},{month:'Nov.',value:94,type:'C'},{month:'Dec.',value:23,type:'C'}
            ]
        },
        categoryField: 'month',
        valueField: 'value',
        seriesField: 'type',
        line: {
            style: {
              lineWidth: 1
            }
        },
        point: {
            visible: true,
            style:{
                shape:'circle'
            }
        },
        area: {
            visible: true, // display area
            state: {
              // The style in the hover state of the area
              hover: {
                fillOpacity: 0.5
              }
            }
          },
        radius:0.8,
        background: '',//背景颜色
         title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
        label:{
            visible:true,
            position:'right',
            style:{
                fontSize:14,
                fill:null,
            }
        },
        stack: true,
        axes: [
            {
              orient: 'radius',
              label: {
                style:{
                    fill:'#333',
                    fontSize:14
                },
                visible: true
              },
              grid: {
                smooth: true,
                style:{
                  stroke:'#333',
                  lineDash: [0],
                  opacity:0.5
                }
              }
            },
            {
                orient: 'angle',
                domainLine: {
                    visible: true,
                    style: {
                        stroke: '#333',
                        lineDash: [0]
                      }
                },
                grid: {
                     visible: true,
                     style: {
                        stroke: '#333',
                        lineDash: [0],
                        opacity:0.5
                    }
                 },
                 label: {
                    visible: true,
                    space:10,
                    style:{
                      fill:'#333',
                      fontSize:14
                    }
                    
                },
            }
          ],
        legends:{
            visible:false,
            orient: 'top',//图例位置
            position:'middle',//对齐方式
            item: {
                label:{
                  style:{
                    // fill:'#0BF1DA',//图例字体颜色
                  }
                }
            },
        },
    }
}

//大屏词云图初始化数据
screenConstants.wordCloudInit = {
    type: screenConstants.type.wordCloud,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
        'type':'wordCloud',
        color: [],
        data: {
            values: [
                {"challenge_name":"宅家dou剧场宅家dou剧场","sum_count":128},
                {"challenge_name":"我的观影报告","sum_count":103},
                {"challenge_name":"抖瓜小助手","sum_count":76},
                {"challenge_name":"搞笑","sum_count":70},
                {"challenge_name":"我要上热门","sum_count":69},
                {"challenge_name":"热门","sum_count":54},
                {"challenge_name":"正能量","sum_count":52},
                {"challenge_name":"上热门","sum_count":36},
                {"challenge_name":"情感","sum_count":34},
                {"challenge_name":"dou上热门","sum_count":32},
                {"challenge_name":"影视剪辑","sum_count":25}
            ]
        },
        wordCloudConfig: {
            drawOutOfBound: 'clip',
            zoomToFit: {
                enlarge: true
            }
        },
        tooltip: screenConstants.tooltipSettings,
        nameField: 'challenge_name',
        valueField: 'sum_count',
        seriesField: 'challenge_name',
        background: '',//背景颜色
    }
}

//大屏仪表盘初始化数据
screenConstants.gaugeInit = {
    type: screenConstants.type.gauge,
    category:screenConstants.category.vchart,
    isDelete:false,
    x: 0, // 初始化横坐标
    y: 0, // 初始化纵坐标
    w: 500, // 组件初始化宽度
    h: 300, // 组件初始化高度
    active: false,
    zindex: 99,
    locked:false,
    refresh: false, // 是否定时刷新
    refreshTime: 30000, // 定时刷新时间，单位(ms)
    dataSource:'1',//数据来源 1静态数据 2动态数据
    dynamicDataSettings:{
        datasetId:"",//数据集
        dataColumns:[],//数据列
    },//动态数据配置
    params: [], // 图表参数
    clickType: '1', // 点击类型
    thirdUrl: '', // 第三方跳转链接
    bindComponent: null, // 绑定组件
    hiddenParamSize:0,//隐藏参数个数
    theme:"",//主题
    amination:"",//动画效果
    isborder:false,//是否添加边框
    borderType:"",//边框类型
    borderColor:[],//边框颜色
    spec:{
      type: 'gauge',
      data: 
        {
          values: [
            {
              type: '目标A',
              value: 0.6
            }
          ]
        },
      color:['#4FC6B4'],
      categoryField: 'type',
      valueField: 'value',
      seriesField: 'type',
      outerRadius: 1,
      innerRadius: 0.7,
      startAngle: -180,
      endAngle: 0,
      padding:{
        top:60
      },
      gauge: {
        type: 'circularProgress',
        progress: {},
        track: {
          style: {
            fill: '#ccc'
          }
        },
    },
    pointer:{
      visible:true,
      style:{

      }
    },
    pin:{
      visible:true,
      style:{

      }
    },
    pinBackground:{
      visible:true,
      style:{

      }
    },
    axes: [
      {
        visible: true,
        type: 'linear',
        orient: 'angle',
        min: 0,
        max: 1,
        tick:{
          visible:true,
          style:{}
        },
        subTick:{
           visible:true,
           style:{}
        },
        label:{
          visible:true,
          inside:false,
          style:{

          }
        }
      }
    ],
    indicator: {
      visible: true,
      offsetY: '20',
      title: {
        visible: true,
        autoLimit: true,
        space: 12,
        style: {
          fontSize: 16,
          fill: 'gray',
          text: '目标A'
        }
      },
      content: {
        style: {
          text: '0.6',
          fontSize: 20,
        }
      }
    },
    background: '',//背景颜色
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      label:{
          visible:false,
          position:'right',
          style:{
              fontSize:14,
              fill:null,
          }
      },
      legends:{
          visible:false,
          orient: 'top',//图例位置
          position:'middle',//对齐方式
          item: {
              label:{
                style:{
                  // fill:'#0BF1DA',//图例字体颜色
                }
              }
          },
      },
  }
}

//大屏刻度仪表盘初始化数据
screenConstants.tickGaugeInit = {
  type: screenConstants.type.tickGauge,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'gauge',
    data: 
      {
        values: [
          {
            type: '目标A',
            value: 0.6
          }
        ]
      },
    color:['#4FC6B4'],
    categoryField: 'type',
    valueField: 'value',
    outerRadius: 1,
    innerRadius: 0.7,
    startAngle: -180,
    endAngle: 0,
    padding:{
      top:60
    },
    gauge: {
      type: 'circularProgress',
      progress: {},
      tickMask: {
        visible: true,
        angle: 4,
        offsetAngle: 0,
        forceAlign: true,
        style: {
          cornerRadius: 15
        }
      },
      track: {
        style: {
          fill: '#ccc'
        }
      },
      label: {
        visible: true,
        position: 'inside-inner',
        offsetRadius: 10,
        style: {
          
        }
      }
  },
    pointer:{
      visible:true,
      style:{

      }
    },
    pin:{
      visible:true,
      style:{

      }
    },
    pinBackground:{
      visible:true,
      style:{

      }
    },
  axes: [
    {
      visible: true,
      type: 'linear',
      orient: 'angle',
      min: 0,
      max: 1,
      tick:{
        visible:true,
        style:{}
      },
      subTick:{
         visible:true,
         style:{}
      },
      label:{
        visible:true,
        inside:false,
        style:{

        }
      }
    }
  ],
  indicator: {
    visible: true,
    offsetY: '20',
    content: {
      style: {
        text: '0.6',
        fontSize: 20,
      }
    }
  },
  background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    label:{
        visible:true,
        position:'right',
        style:{
            fontSize:14,
            fill:null,
        }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
}
}

//大屏系列仪表盘初始化数据
screenConstants.seriesGaugeInit = {
  type: screenConstants.type.seriesGauge,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'gauge',
    data: 
      {
        values: [
          {
            type: '目标A',
            value: 0.6
          },
          {
            type: '目标B',
            value: 0.2
          }
        ]
      },
    color:['#4FC6B4','#FFC528'],
    categoryField: 'type',
    valueField: 'value',
    seriesField: 'type',
    outerRadius: 1,
    innerRadius: 0.7,
    startAngle: -180,
    endAngle: 0,
    padding:{
      top:60
    },
    gauge: {
      type: 'gauge',
      categoryField: 'type',
      valueField: 'value',
      seriesField: 'type',
      // tickMask: {
      //   visible: true,
      //   angle: 4,
      //   offsetAngle: 0,
      //   forceAlign: true,
      //   style: {
      //     cornerRadius: 15
      //   }
      // },
      track: {
        style: {
          fill: '#ccc'
        }
      },
      label: {
        visible: true,
        position: 'inside-inner',
        offsetRadius: 10,
        style: {
          
        }
      }
  },
    pointer:{
      visible:true,
      style:{

      }
    },
    pin:{
      visible:true,
      style:{

      }
    },
    pinBackground:{
      visible:true,
      style:{

      }
    },
  axes: [
    {
      visible: true,
      type: 'linear',
      orient: 'angle',
      min: 0,
      max: 1,
      tick:{
        visible:true,
        style:{}
      },
      subTick:{
         visible:true,
         style:{}
      },
      label:{
        visible:true,
        inside:false,
        style:{

        }
      }
    }
  ],
  indicator: {
    visible: false,
    offsetY: '20',
    content: {
      style: {
        text: '0.6',
        fontSize: 20,
      }
    }
  },
  background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    label:{
        visible:true,
        position:'right',
        style:{
            fontSize:14,
            fill:null,
        }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
}
}

//大屏刻度系列仪表盘初始化数据
screenConstants.tickSeriesGaugeInit = {
  type: screenConstants.type.tickSeriesGauge,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'gauge',
    data: 
      {
        values: [
          {
            type: '目标A',
            value: 0.6
          },
          {
            type: '目标B',
            value: 0.2
          }
        ]
      },
    color:['#4FC6B4','#FFC528'],
    categoryField: 'type',
    valueField: 'value',
    seriesField: 'type',
    outerRadius: 1,
    innerRadius: 0.7,
    startAngle: -180,
    endAngle: 0,
    padding:{
      top:60
    },
    gauge: {
      type: 'gauge',
      categoryField: 'type',
      valueField: 'value',
      seriesField: 'type',
      tickMask: {
        visible: true,
        angle: 4,
        offsetAngle: 0,
        forceAlign: true,
        style: {
          cornerRadius: 15
        }
      },
      track: {
        style: {
          fill: '#ccc'
        }
      },
      label: {
        visible: true,
        position: 'inside-inner',
        offsetRadius: 10,
        style: {
          
        }
      }
  },
    pointer:{
      visible:true,
      style:{

      }
    },
    pin:{
      visible:true,
      style:{

      }
    },
    pinBackground:{
      visible:true,
      style:{

      }
    },
  axes: [
    {
      visible: true,
      type: 'linear',
      orient: 'angle',
      min: 0,
      max: 1,
      tick:{
        visible:true,
        style:{}
      },
      subTick:{
         visible:true,
         style:{}
      },
      label:{
        visible:true,
        inside:false,
        style:{

        }
      }
    }
  ],
  indicator: {
    visible: false,
    offsetY: '20',
    content: {
      style: {
        text: '0.6',
        fontSize: 20,
      }
    }
  },
  background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    label:{
        visible:true,
        position:'right',
        style:{
            fontSize:14,
            fill:null,
        }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
 }
}

screenConstants.funnelInit = {
  type: screenConstants.type.funnel,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  spec:{
    type:'funnel',
    maxSize: '75%',
    color: [],
    categoryField: 'name',
    valueField: 'value',
    data: 
      {
        values: [
          {
            value: 100,
            name: 'Step1'
          },
          {
            value: 80,
            name: 'Step2'
          },
          {
            value: 60,
            name: 'Step3'
          },
          {
            value: 40,
            name: 'Step4'
          },
          {
            value: 20,
            name: 'Step5'
          }
        ]
      }
    ,
    background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    shape: 'rect',//梯形 trapezoid 矩形rect
    label:{
      visible:true,
      position:'outside',
      formatter: '',//标签格式
      style:{
          fontSize:14,
          fill:null,
      }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
    outerLabel: {
      visible:false,
      position: 'left',
      style:{
        fill:null,
      }
    },
  }
}

screenConstants.circularProgressInit = {
  type: screenConstants.type.circularProgress,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'circularProgress',
    data: 
      {
        values: [
          {
            type: '目标A',
            value: 0.795,
          }
        ]
      },
    color:['#4FC6B4'],
    categoryField: 'type',
    valueField: 'value',
    seriesField: 'type',
    outerRadius: 1,
    innerRadius: 0.9,
    roundCap: false,
    cornerRadius: 20,
    background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    progress:{
      style:{

      }
    },
    // track: {
    //   style: {
    //     fill: '#ccc'
    //   }
    // },
    axes: [
      {
        visible: false,
        type: 'linear',
        orient: 'angle',
        min:0,
        max:1,
        label:{
          formatter: '{value:~%}',
          style:{}
        }
      },
      {
        visible: false,
        type: 'band',
        orient: 'radius',
        label:{
          style:{}
        }
      }
    ],
    indicator: {
      visible: true,
      fixed: true,
      trigger: 'none',
      title: {
        visible: true,
        autoLimit: true,
        space: 12,
        field:null,
        style: {
          fontSize: 16,
          fill: '#000',
          text:"目标A",
        }
      },
      content: 
        {
          visible: true,
          field:null,
          style: {
            fontSize: 16,
            fill: '#000',
            text:"0.795",
          }
        }
      
    },
    label:{
        visible:false,
        position:'right',
        style:{
            fontSize:14,
            fill:null,
        }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
 }
}

screenConstants.barProgressInit = {
  type: screenConstants.type.barProgress,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'linearProgress',
    data: 
    {
      values: [
        {
          type: 'Tradition Industries',
          value: 0.795,
        },
        {
          type: 'Business Companies',
          value: 0.25,
        },
        {
          type: 'Customer-facing Companies',
          value: 0.65,
        }
      ]
    },
    color:['#4FC6B4','#F88D0A','#EF0EE4'],
    direction: 'horizontal',
    xField: 'value',
    yField: 'type',
    seriesField: 'type',
    cornerRadius: 20,
    bandWidth: 30,
    background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    axes: [
      {
        orient: 'right',
        label: { visible: true,style:{}},
        type: 'band',
        domainLine: { visible: false },
        tick: { visible: false },
      },
      { orient: 'bottom', label: { visible: true,formatter: '{value:~%}',style:{}}, type: 'linear', visible: true }
    ],
    label:{
        visible:false,
        position:'right',
        style:{
            fontSize:14,
            fill:null,
        }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
 }
}
screenConstants.liquidInit = {
  type: screenConstants.type.liquid,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    type: 'liquid',
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    color:[],
    categoryField:"name",
    valueField: 'value',
    maskShape:"circle",
    data: {
      id: 'data',
      values: [
        {
          name:"进度",
          value: 0.3
        }
      ]
    },
    indicator: {
      visible: true,
      title: {
        visible: true,
        style: {
          text: '进度',
          fill:'#000',
          fontSize:20
        }
      },
      content: 
        {
          visible: true,
          style: {
            fill: '#000',
            text: '30%',
            fontSize:20
          }
        }
    }
  }
}

screenConstants.basicMapInit = {
  type: screenConstants.type.basicMap,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  isDrill: false, // 是否支持下钻
  spec:{
    type: 'map',
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    nameField: 'name',
    valueField: 'value',
    // nameProperty: 'name',
    map: '100000',
    defaultFillColor:'#f3f3f3',
    data: {
      values: [
          { name: '北京市', value: 80 },
          { name: '天津市', value: 10 },
          { name: '上海市', value: 100 },
          { name: '重庆市', value: 20 },
          { name: '河北省', value: 30 },
          { name: '河南省', value: 400 },
          { name: '云南省', value: 200 },
          { name: '辽宁省', value: 110 },
          { name: '黑龙江省', value: 160 },
          { name: '湖南省', value: 45 },
          { name: '安徽省', value: 70 },
          { name: '山东省', value: 330 },
          { name: '新疆维吾尔自治区', value: 290 },
          { name: '江苏省', value: 140 },
          { name: '浙江省', value: 222 },
          { name: '江西省', value: 555 },
          { name: '湖北省', value: 66 },
          { name: '广西壮族自治区', value: 59 },
          { name: '甘肃省', value: 452 },
          { name: '山西省', value: 369 },
          { name: '内蒙古自治区', value: 147 },
          { name: '陕西省', value: 258 },
          { name: '吉林省', value: 123 },
          { name: '福建省', value: 85 },
          { name: '贵州省', value: 98 },
          { name: '广东省', value: 96 },
          { name: '青海省', value: 32 },
          { name: '西藏自治区', value: 69 },
          { name: '四川省', value: 79 },
          { name: '宁夏回族自治区', value: 65 },
          { name: '海南省', value: 456 },
          { name: '台湾省', value: 478 },
          { name: '香港特别行政区', value: 589 },
          { name: '澳门特别行政区', value: 413 }
      ]
    },
    nameMap: {
      '新疆维吾尔自治区': '新疆维吾尔自治区',
      '西藏自治区': '西藏自治区',
      '内蒙古自治区': '内蒙古自治区',
      '青海省': '青海省',
      '四川省': '四川省',
      '黑龙江省': '黑龙江省',
      '甘肃省': '甘肃省',
      '云南省': '云南省',
      '广西壮族自治区': '广西壮族自治区',
      '湖南省': '湖南省',
      '陕西省': '陕西省',
      '广东省': '广东省',
      '吉林省': '吉林省',
      '河北省': '河北省',
      '湖北省': '湖北省',
      '贵州省': '贵州省',
      '山东省': '山东省',
      '江西省': '江西省',
      '河南省': '河南省',
      '辽宁省': '辽宁省',
      '山西省': '山西省',
      '安徽省': '安徽省',
      '福建省': '福建省',
      '浙江省': '浙江省',
      '江苏省': '江苏省',
      '重庆市': '重庆市',
      '宁夏回族自治区': '宁夏回族自治区',
      '海南省': '海南省',
      '台湾省': '台湾省',
      '北京市': '北京市',
      '天津市': '天津市',
      '上海市': '上海市',
      '香港特别行政区': '香港特别行政区',
      '澳门特别行政区': '澳门特别行政区'
    },
    region: [
      {
        roam: true,
      }
    ],
    color: {
      type: 'linear',
      range: ['rgb(252,250,97)', 'rgb(252,150,134)', 'rgb(87,33,15)']
    },
    area: {
      style: {
        fill: {
          field: 'value',
          scale: 'color',
          changeDomain: 'replace'
        },
        stroke:'#00EEFF',
        lineWidth:2
      }
    },
    label:{
      visible:true,
      // position:'outside',
      formatter:'',
      style:{
          fontSize:10,
          fill:null,
      }
    },
    legends: 
      {
        visible: false,
        type: 'color',
        field: 'value',
        orient: 'bottom',
        position: '',
        title: {
          visible: true,
          text: ''
        }
      }
    
  }
}

screenConstants.scatterMapInit = {
  type: screenConstants.type.scatterMap,
  category: screenConstants.category.vchart,
  isDelete: false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked: false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource: '1', // 数据来源 1静态数据 2动态数据
  dynamicDataSettings: {
    datasetId: '', // 数据集
    dataColumns: []// 数据列
  }, // 动态数据配置
  params: [], // 图表参数
  hiddenParamSize: 0, // 隐藏参数个数
  theme: '', // 主题
  amination: '', // 动画效果
  isDrill: false, // 是否支持下钻
  isborder: false, // 是否添加边框
  borderType: '', // 边框类型
  borderColor: [], // 边框颜色
  spec: {
    type: 'common',
    title: screenConstants.chartTitleSettings,
    tooltip: screenConstants.tooltipSettings,
    region: [
    {
      roam: true,
      coordinate: 'geo',
      longitudeField: 'lng',
      latitudeField: 'lat',
    }
  ],
    data: {
      values: [
        { name: '山东省', value: 80,lng:118.187759,lat: 36.376092},
        { name: '江苏省', value: 40,lng:118.767413,lat: 32.041544},
        { name: '河南省', value: 80,lng:113.665412,lat: 34.757975},
        { name: '湖北省', value: 80,lng:114.298572,lat: 30.584355},
        { name: '湖南省', value: 80,lng:112.982279,lat: 28.19409},
        { name: '广东省', value: 80,lng:113.280637,lat: 23.125178},
        { name: '海南省', value: 80,lng:110.33119,lat: 20.031971},
        { name: '重庆市', value: 80,lng:106.504962,lat: 29.533155},
        { name: '四川省', value: 80,lng:104.065735,lat: 30.659462},
        { name: '陕西省', value: 80,lng:108.948024,lat: 34.263161},
        { name: '宁夏回族自治区', value: 80,lng:106.278179,lat: 38.46637},
        { name: '新疆维吾尔自治区', value: 80,lng:87.617733,lat: 43.792818},
      ]
    },
    series: [
       { type: 'map', map: '100000', tooltip: { visible: false }, 
        area: {
          style: {
            fill: '#0A35F4',
            stroke:'#00EEFF',
            lineWidth:2
          }
        },
      },
       {
          type: 'scatter',
          xField: 'name',
          yField: 'value',
          point: {
            style: {
              size: 10,
              fill: '#E0F409',
            }
          }
        }
    ],
    label: {
      visible: true,
      // position:'outside',
      formatter: '',
      style: {
        fontSize: 10,
        fill: null
      }
    },
    legends:
      {
        visible: false,
        type: 'color',
        field: 'value',
        orient: 'bottom',
        position: '',
        title: {
          visible: true,
          text: ''
        }
      }

  }
}

screenConstants.scrollTableInit = {
  type: screenConstants.type.scrollTable,
  category:screenConstants.category.table,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 280, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  hiddenParamSize:0,//隐藏参数个数
  // 数据列
  tableColumn: [{ 'name': '销售数量', 'key': 'count', 'style': { 'width': '40' }}, { 'name': '日期', 'key': 'date', 'style': { 'width': '40' }}],
  headStyle: {
    height: 40, // 高度
    backgroundColor: '#2570a1',
    color: '#00FFFF', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'bold' // 是否加粗 normal 和bold
  }, // 表头样式
  style: {// 整体样式
    showIndex: true, // 是否显示序号
    isBorder: true, // 是否显示边框
    borderColor: '#FFFFFF', // 边框颜色
    borderWidth: 1, // 边框宽度
    borderStyle: 'solid', // 边框样式
    indexWidth: '20'
  },
  bodyStyle: {// 表体样式
    oddRowColor: '#33a3dc', // 奇数行颜色
    evenRowColor: '#2570a1', // 偶数行颜色
    height: 30, // 行高
    color: '#ffffff', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'normal' // 是否加粗
  },
  options: {// 表格配置项
    step: 0.5, // 数值越大速度滚动越快
    limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
    hoverStop: true, // 是否开启鼠标悬停stop
    direction: 'up', // 0向下 1向上 2向左 3向右
    openWatch: true, // 开启数据实时监控刷新dom
    singleHeight: 30, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
    singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
    waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
  },
  spec:{
    data: {
      values: [
        {
          'count': '100',
          'date': '2021-08-01'
        }, {
          'count': '95',
          'date': '2021-08-02'
        }, {
          'count': '112',
          'date': '2021-08-03'
        }, {
          'count': '125',
          'date': '2021-08-04'
        }, {
          'count': '144',
          'date': '2021-08-05'
        }, {
          'count': '168',
          'date': '2021-08-06'
        }, {
          'count': '158',
          'date': '2021-08-07'
        }, {
          'count': '99',
          'date': '2021-08-08'
        }
      ]
    }
  }
}
screenConstants.pageTableInit = {
  type: screenConstants.type.pageTable,
  category:screenConstants.category.table,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 220, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  hiddenParamSize:0,//隐藏参数个数
  // 数据列
  tableColumn: [{ 'name': '销售数量', 'key': 'count'}, { 'name': '日期', 'key': 'date'}],
  isIndex:true,//是否显示行号
  indexWidth:50,//行号列宽度
  align:'center',//对齐方式
  overflow:true,
  border:true,//边框
  stripe:false,//斑马纹
  highlight:false,
  headStyle: {
    backgroundColor: '#2570a1',
    color: '#00FFFF', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'bold' // 是否加粗 normal 和bold
  }, // 表头样式
  rowStyle:{
    backgroundColor: '#2570a1',
    color: '#00FFFF', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'bold' // 是否加粗 normal 和bold
  },
  pagination:{
    pageSize:5,//每页显示条数
    currentPage:1,//当前页
    backgroundColor:"#2570a1",
  },
  spec:{
    data: {
      total:8,//总条数
      values: [
        {
          'count': '100',
          'date': '2021-08-01'
        }, {
          'count': '95',
          'date': '2021-08-02'
        }, {
          'count': '112',
          'date': '2021-08-03'
        }, {
          'count': '125',
          'date': '2021-08-04'
        }, {
          'count': '144',
          'date': '2021-08-05'
        }, {
          'count': '168',
          'date': '2021-08-06'
        }, {
          'count': '158',
          'date': '2021-08-07'
        }, {
          'count': '99',
          'date': '2021-08-08'
        }
      ]
    }
  }
}

//大屏卡片列表组件初始化数据
screenConstants.cardListInit = {
  type: screenConstants.type.cardList,
  category: screenConstants.category.cardList,
  isDelete: false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 248, // 组件初始化高度
  active: false,
  zindex: 99,
  locked: false,
  isborder: false, // 是否添加边框
  borderType: '', // 边框类型
  borderColor: [], // 边框颜色
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource: '1', // 数据来源 1静态数据 2动态数据
  dynamicDataSettings: {
    datasetId: '', // 数据集
    dataColumns: []// 数据列
  }, // 动态数据配置
  params: [], // 图表参数
  hiddenParamSize: 0, // 隐藏参数个数
  bodyStyle:{//卡片背景颜色
    backgroundColor:"#FFF"
  },
  leftHead: {//表头左侧内容配置
    property:"",//属性值，数据中没有属性对应的值则直接显示该属性
    color: '#00FFFF', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'bold', // 是否加粗 normal 和bold
    conditions:[],//自定义字体颜色条件
  },
  rightHead: {//表头右侧内容配置
    property:"",//属性值，数据中没有属性对应的值则直接显示该属性
    color: '#00FFFF', // 字体颜色
    fontSize: 12, // 字体大小
    fontWeight: 'bold', // 是否加粗 normal 和bold
    conditions:[],//自定义字体颜色条件
  }, 
  bodyContent:{
    rowContents:[],//每一行显示的内容和对应的属性
  },
  pagination: {
    pageColumn:2,//每页列数
    pageSize: 6, // 每页显示条数
    backgroundColor: '#2570a1',
    autoPageInterval:3000
  },
  spec: {
    data: {
      total: 8, // 总条数
      values: [
        {
          'count': '100',
          'date': '2021-08-01'
        }, {
          'count': '95',
          'date': '2021-08-02'
        }, {
          'count': '112',
          'date': '2021-08-03'
        }, {
          'count': '125',
          'date': '2021-08-04'
        }, {
          'count': '144',
          'date': '2021-08-05'
        }, {
          'count': '168',
          'date': '2021-08-06'
        }, {
          'count': '158',
          'date': '2021-08-07'
        }, {
          'count': '99',
          'date': '2021-08-08'
        }
      ]
    }
  }
}

//大屏箱型图初始化数据
screenConstants.boxPlotInit = {
  type: screenConstants.type.boxPlot,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      type:'boxPlot',
      color: [],
      data: {
          values: [
            {
              x: 'Sub-Saharan Africa',
              y1: 8.72,
              y2: 9.73,
              y3: 10.17,
              y4: 10.51,
              y5: 11.64
            },
            {
              x: 'South Asia',
              y1: 9.4,
              y2: 10.06,
              y3: 10.75,
              y4: 11.56,
              y5: 12.5
            },
            {
              x: 'Middle East & North Africa',
              y1: 9.54,
              y2: 10.6,
              y3: 11.05,
              y4: 11.5,
              y5: 11.92
            },
            {
              x: 'Latin America & Caribbean',
              y1: 8.74,
              y2: 9.46,
              y3: 10.35,
              y4: 10.94,
              y5: 12.21
            },
            {
              x: 'East Asia & Pacific',
              y1: 7.8,
              y2: 8.95,
              y3: 10.18,
              y4: 11.57,
              y5: 13.25
            },
            {
              x: 'Europe & Central Asia',
              y1: 9.52,
              y2: 10.39,
              y3: 10.93,
              y4: 11.69,
              y5: 12.63
            }
          ]
      },
      xField: ['x'],
      minField: 'y1',//箱型图最小值字段
      q1Field: 'y2',//箱型图 Q1
      medianField: 'y3',//箱型图 Q2
      q3Field: 'y4',//箱型图 Q3
      maxField: 'y5',//箱型图最大值字段
      seriesField: null,
      background: '',//背景颜色
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      direction: 'vertical',
      boxPlot: {
        style: {
          // boxWidth: 50, // 不指定则自适应宽度
          // shaftWidth: 60,
          shaftShape: 'line',
          lineWidth: 2
        }
      }
  }
}

//大屏气泡图初始化数据
screenConstants.circlePackingInit = {
  type: screenConstants.type.circlePacking,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      type:'circlePacking',
      color: [],
      data: {
          values: [
            {
                "name": "数据1",
                "value": 1
            },
            {
                "name": "数据2",
                "value": 2
            },
            {
                "name": "数据3",
                "value": 3
            },
            {
                "name": "数据4",
                "value": 4
            },
            {
                "name": "数据5",
                "value": 5
            },
            {
                "name": "数据6",
                "value": 6
            },
            {
                "name": "数据7",
                "value": 7
            },
            {
                "name": "数据8",
                "value": 8
            },
            {
                "name": "数据9",
                "value": 9
            },
            {
                "name": "数据10",
                "value": 10
            }
        ]
      },
      categoryField: 'name',
      valueField: 'value',
      background: '',//背景颜色
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      drill: true,
      layoutPadding: 5,
      label: {
        style: {
          visible:true,
          fontSize: 10,
          text: datum => [`${datum.name}`, `${datum.value}`]
        }
      },
      legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
  }
}


//转化漏斗图初始化数据
screenConstants.transformFunnelInit = {
  type: screenConstants.type.transformFunnel,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
    'type':'funnel',
    color: [],
    categoryField: 'name',
    valueField: 'value',
    isTransform: true,
    isCone: false,
    data: 
      {
        values: [
          {
            value: 5676,
            name: 'Sent'
          },
          {
            value: 3872,
            name: 'Viewed'
          },
          {
            value: 1668,
            name: 'Clicked'
          },
          {
            value: 610,
            name: 'Add to Cart'
          },
          {
            value: 565,
            name: 'Purchased'
          }
        ]
      }
    ,
    background: '',//背景颜色
     title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
    shape: 'rect',//梯形 trapezoid 矩形rect
    label:{
      visible:true,
      position:'outside',
      formatter: '',//标签格式
      style:{
          fontSize:14,
          fill:null,
      }
    },
    legends:{
        visible:false,
        orient: 'top',//图例位置
        position:'middle',//对齐方式
        item: {
            label:{
              style:{
                // fill:'#0BF1DA',//图例字体颜色
              }
            }
        },
    },
    transformLabel: {
      visible: true
    },
    outerLabel: {
      position: 'right',
      visible: true,
      style:{
        fill:"#00CCFF",
      }
    },
  }
}

//大屏直方图初始化数据
screenConstants.zhifangtuInit = {
  type: screenConstants.type.zhifangtu,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      type:'histogram',
      color: [],
      data: {
          values: [
            {
              from: 0,
              to: 10,
              profit: 2,
              type: 'A'
            },
            {
              from: 10,
              to: 16,
              profit: 3,
              type: 'B'
            },
            {
              from: 16,
              to: 18,
              profit: 15,
              type: 'C'
            },
            {
              from: 18,
              to: 26,
              profit: 12,
              type: 'D'
            },
            {
              from: 26,
              to: 32,
              profit: 22,
              type: 'E'
            },
            {
              from: 32,
              to: 56,
              profit: 7,
              type: 'F'
            },
            {
              from: 56,
              to: 62,
              profit: 17,
              type: 'G'
            }
          ]
      },
      seriesField: 'type',
      xField: "from",
      x2Field:"to",
      yField: 'profit',
      background: '',//背景颜色
      barWidth:'100%',//柱体宽度
       title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      bar:{
          style:{
              cornerRadius:0,//圆角
          }
      },
      label:{
          visible:true,
          position:'outside',
          style:{
              fontSize:14,
              fill:null,
          }
      },
      axes:[
          {orient:'bottom',sampling: true,label:{visible:true,style:{fill:'#6E6F73'},autoRotate:false,autoRotateAngle: [0, 90],autoLimit:false},unit:{visible:false,style:{}}},
          {orient:'left',label:{visible:true,style:{fill:'#6E6F73'},autoLimit:false},unit:{visible:false,style:{}}}
      ],
      legends:{
          visible:false,
          orient: 'top',//图例位置
          position:'middle',//对齐方式
          item: {
              label:{
                style:{
                  // fill:'#0BF1DA',//图例字体颜色
                }
              }
          },
      },
  }
}

//大屏桑葚图初始化数据
screenConstants.sankeyInit = {
  type: screenConstants.type.sankey,
  category:screenConstants.category.vchart,
  isDelete:false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked:false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource:'1',//数据来源 1静态数据 2动态数据
  dynamicDataSettings:{
      datasetId:"",//数据集
      dataColumns:[],//数据列
  },//动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize:0,//隐藏参数个数
  theme:"",//主题
  amination:"",//动画效果
  isborder:false,//是否添加边框
  borderType:"",//边框类型
  borderColor:[],//边框颜色
  spec:{
      type:'sankey',
      color: [],
      data: {
        values:[{
          nodes: [
            { name: 'Bight of Benin'},
            { name: 'Brazil'},
            { name: 'Bight of Biafra and Gulf of Guinea islands'},
            { name: 'Gold Coast'},
            { name: 'Others Dep.'},
            { name: 'Senegambia and offshore Atlantic'},
            { name: 'Sierra Leone e Windward Coast'},
            { name: 'Southeast Africa and Indian Ocean islands'},
            { name: 'West Central Africa and St. Helena'},
            { name: 'Caribbean'},
            { name: 'Mainland North America'},
            { name: 'Others Arr'},
            { name: 'Spanish American Mainland'}
          ],
          links: [
            { target: 'Brazil', source: 'Bight of Benin', value: 733769 },
            { target: 'Brazil', source: 'Bight of Biafra and Gulf of Guinea islands', value: 98256 },
            { target: 'Brazil', source: 'Gold Coast', value: 40507 },
            { target: 'Brazil', source: 'Others Dep.', value: 18627 },
            { target: 'Brazil', source: 'Senegambia and offshore Atlantic', value: 86001 },
            { target: 'Brazil', source: 'Sierra Leone e Windward Coast', value: 5409 },
            { target: 'Brazil', source: 'Southeast Africa and Indian Ocean islands', value: 232940 },
            { target: 'Brazil', source: 'West Central Africa and St. Helena', value: 1818611 },
            { target: 'Caribbean', source: 'Bight of Benin', value: 494753 },
            { target: 'Caribbean', source: 'Bight of Biafra and Gulf of Guinea islands', value: 678927 },
            { target: 'Caribbean', source: 'Gold Coast', value: 517280 },
            { target: 'Caribbean', source: 'Others Dep.', value: 192389 },
            { target: 'Caribbean', source: 'Senegambia and offshore Atlantic', value: 144125 },
            { target: 'Caribbean', source: 'Sierra Leone e Windward Coast', value: 284412 },
            { target: 'Caribbean', source: 'Southeast Africa and Indian Ocean islands', value: 57138 },
            { target: 'Caribbean', source: 'West Central Africa and St. Helena', value: 793963 },
            { target: 'Mainland North America', source: 'Bight of Benin', value: 7153 },
            { target: 'Mainland North America', source: 'Bight of Biafra and Gulf of Guinea islands', value: 39389 },
            { target: 'Mainland North America', source: 'Gold Coast', value: 26918 },
            { target: 'Mainland North America', source: 'Others Dep.', value: 12532 },
            { target: 'Mainland North America', source: 'Senegambia and offshore Atlantic', value: 49118 },
            { target: 'Mainland North America', source: 'Sierra Leone e Windward Coast', value: 40366 },
            { target: 'Mainland North America', source: 'Southeast Africa and Indian Ocean islands', value: 3958 },
            { target: 'Mainland North America', source: 'West Central Africa and St. Helena', value: 62966 },
            { target: 'Others Arr', source: 'Bight of Benin', value: 40607 },
            { target: 'Others Arr', source: 'Bight of Biafra and Gulf of Guinea islands', value: 34687 },
            { target: 'Others Arr', source: 'Gold Coast', value: 2108 },
            { target: 'Others Arr', source: 'Others Dep.', value: 1499 },
            { target: 'Others Arr', source: 'Senegambia and offshore Atlantic', value: 8435 },
            { target: 'Others Arr', source: 'Sierra Leone e Windward Coast', value: 12793 },
            { target: 'Others Arr', source: 'Southeast Africa and Indian Ocean islands', value: 9924 },
            { target: 'Others Arr', source: 'West Central Africa and St. Helena', value: 50046 },
            { target: 'Spanish American Mainland', source: 'Bight of Benin', value: 15822 },
            { target: 'Spanish American Mainland', source: 'Bight of Biafra and Gulf of Guinea islands', value: 13700 },
            { target: 'Spanish American Mainland', source: 'Gold Coast', value: 5030 },
            { target: 'Spanish American Mainland', source: 'Others Dep.', value: 5155 },
            { target: 'Spanish American Mainland', source: 'Senegambia and offshore Atlantic', value: 44889 },
            { target: 'Spanish American Mainland', source: 'Sierra Leone e Windward Coast', value: 326 },
            { target: 'Spanish American Mainland', source: 'Southeast Africa and Indian Ocean islands', value: 14327 },
            { target: 'Spanish American Mainland', source: 'West Central Africa and St. Helena', value: 131837 }
          ],
        }]
      },
      categoryField: 'name',
      valueField: 'value',
      sourceField: 'source',
      targetField: 'target',
      nodeKey: datum => datum.name,
      background: '',//背景颜色
      title:screenConstants.chartTitleSettings,
      tooltip:screenConstants.tooltipSettings,
      label:{
          visible:true,
          position:'outside',
          style:{
              fontSize:14,
              fill:null,
          }
      },
      node: {
        state: {
          hover: {
            stroke: '#333333'
          },
          selected: {
            fill: '#dddddd',
            stroke: '#333333',
            lineWidth: 1,
            brighter: 1,
            fillOpacity: 1
          }
        }
      },
    
      link: {
        style: {
          fillOpacity: 0.3
        },
        state: {
          selected : {
            fillOpacity: 1
          }
        }
      },
      emphasis: {
        enable: true,
        trigger: 'selected',
        effect: 'adjacency'
      },
      legends:{
          visible:false,
          orient: 'top',//图例位置
          position:'middle',//对齐方式
          item: {
              label:{
                style:{
                  // fill:'#0BF1DA',//图例字体颜色
                }
              }
          },
      },
  }
}

// 大屏折柱图初始化数据
screenConstants.comboCharthlInit = {
  type: screenConstants.type.comboCharthl,
  category: screenConstants.category.vchart,
  isDelete: false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked: false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource: '1', // 数据来源 1静态数据 2动态数据
  lineDataSource:'1',// 数据来源 1静态数据 2动态数据
  dynamicDataSettings: {
    datasetId: "", // 数据集
    dataColumns: []// 数据列
  }, // 动态数据配置
  lineDynamicDataSettings: {
    datasetId: '', // 数据集
    dataColumns: []// 数据列
  }, // 折线图动态数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize: 0, // 隐藏参数个数
  theme: '', // 主题
  amination: '', // 动画效果
  isborder: false, // 是否添加边框
  borderType: '', // 边框类型
  borderColor: [], // 边框颜色
  spec: {
    'type': 'bar',
    color: [],
    data: [
      {
        id: 'id0',
        values: [
          { x: '周一', type: '早餐', y: 15 },
          { x: '周一', type: '午餐', y: 25 },
          { x: '周二', type: '早餐', y: 12 },
          { x: '周二', type: '午餐', y: 30 },
          { x: '周三', type: '早餐', y: 15 },
          { x: '周三', type: '午餐', y: 24 },
          { x: '周四', type: '早餐', y: 10 },
          { x: '周四', type: '午餐', y: 25 },
          { x: '周五', type: '早餐', y: 13 },
          { x: '周五', type: '午餐', y: 20 },
          { x: '周六', type: '早餐', y: 10 },
          { x: '周六', type: '午餐', y: 22 },
          { x: '周日', type: '早餐', y: 12 },
          { x: '周日', type: '午餐', y: 19 }
        ]
      },
      {
        id: 'id1',
        values: [
          { x: '周一', type: '饮料', y: 22 },
          { x: '周二', type: '饮料', y: 43 },
          { x: '周三', type: '饮料', y: 33 },
          { x: '周四', type: '饮料', y: 22 },
          { x: '周五', type: '饮料', y: 10 },
          { x: '周六', type: '饮料', y: 30 },
          { x: '周日', type: '饮料', y: 50 }
        ]
      }
    ],
    series: [
      {
        type: 'bar',
        dataIndex: 0,
        label: { visible: true },
        seriesField: ['type'],
        xField: ['x', 'type'],
        yField: ['y'],
        barWidth: '100%', // 柱体宽度
        bar: {
          style: {
            cornerRadius: 0// 圆角
          }
        },
        label: {
          visible: true,
          position: 'outside',
          style: {
            fontSize: 14,
            fill: null
          }
        },
      },
      {
        type: 'line',
        dataIndex: 1,
        label: { visible: true },
        seriesField: ['type'],
        xField: ['x'],
        yField: ['y'],
        stack: false,
        lineLabel: { visible: false, style: {}},
        line: {
          style: {
            lineWidth: 2,
            curveType: 'linear'
          }
        },
        label: {
          visible: true,
          position: 'outside',
          style: {
            fontSize: 14,
            fill: null
          }
        },
      }
    ],
    title: screenConstants.chartTitleSettings,
    tooltip: screenConstants.tooltipSettings,
    axes: [
      { orient: 'bottom', sampling: true, label: { visible: true, style: { fill: '#6E6F73', }, autoRotate: false, autoRotateAngle: [0, 90], autoLimit: false }, unit: { visible: false, style: {}}},
      { orient: 'left', label: { visible: true, style: { fill: '#6E6F73' }, autoLimit: false }, unit: { visible: false, style: {}}}
    ],
    legends: {
      visible: false,
      orient: 'top', // 图例位置
      position: 'middle', // 对齐方式
      item: {
        label: {
          style: {
            // fill:'#0BF1DA',//图例字体颜色
          }
        }
      }
    }
  }
}

// 大屏双向条形图初始化数据
screenConstants.comboChartdbbarInit = {
  type: screenConstants.type.comboChartdbbar,
  category: screenConstants.category.vchart,
  isDelete: false,
  x: 0, // 初始化横坐标
  y: 0, // 初始化纵坐标
  w: 500, // 组件初始化宽度
  h: 300, // 组件初始化高度
  active: false,
  zindex: 99,
  locked: false,
  refresh: false, // 是否定时刷新
  refreshTime: 30000, // 定时刷新时间，单位(ms)
  dataSource: '1', // 数据来源 1静态数据 2动态数据
  lineDataSource:'1',// 数据来源 1静态数据 2动态数据
  dynamicDataSettings: {
    datasetId: "", // 数据集
    dataColumns: []// 数据列
  }, // 右侧数据配置
  lineDynamicDataSettings: {
    datasetId: '', // 数据集
    dataColumns: []// 数据列
  }, // 左侧数据配置
  params: [], // 图表参数
  clickType: '1', // 点击类型
  thirdUrl: '', // 第三方跳转链接
  bindComponent: null, // 绑定组件
  hiddenParamSize: 0, // 隐藏参数个数
  theme: '', // 主题
  amination: '', // 动画效果
  isborder: false, // 是否添加边框
  borderType: '', // 边框类型
  borderColor: [], // 边框颜色
  spec: {
    type: 'common',
    color: [],
     data: [{
        id: 'mainSeriesData',
        values: [
          {
            value: 80,
            type: '销售额',
            name: '河北',
          },
          {
            value: 60,
            type: '销售额',
            name: '山西',
          },
          {
            value: 65,
            type: '销售额',
            name: '内蒙古',
          },
          {
            value: 75,
            type: '销售额',
            name: '辽宁',
          },
          {
            value: 66,
            type: '销售额',
            name: '吉林',
          }
        ],
      },
      {
        id: 'subSeriesData',
        values: [
          {
            value: 30,
            type: '利润',
            name: '河北',
          },
          {
            value: 25,
            type: '利润',
            name: '山西',
          },
          {
            value: 40,
            type: '利润',
            name: '内蒙古',
          },
          {
            value: 39,
            type: '利润',
            name: '辽宁',
          },
          {
            value: 20,
            type: '利润',
            name: '吉林',
          }
        ],
      }],
    series: [
    {
      id: 'mainSeries',
      type: 'bar',
      yField: ['name'],
      xField: ['value'],
      seriesField: ['type'],
      direction: 'horizontal',
      regionId: 'mainRegion',
      dataIndex: 0,
      stack: false,
    },
    {
      id: 'subSeries',
      type: 'bar',
      yField: ['name'],
      xField: ['value'],
      seriesField: ['type'],
      direction: 'horizontal',
      regionId: 'subRegion',
      dataIndex: 1,
      stack: false,
      bar: {
        style: {
          cornerRadius: 0,
          lineWidth: 2,
        }
      },
    }
  ],
  // layout
  layout: {
    type: 'grid',
    row: 3,
    col: 4,
    elements: [
      {
        modelId: 'legend-discrete',
        col: 0,
        colSpan: 4,
        row: 0
      },
      {
        modelId: 'mainRegion',
        col: 0,
        row: 1
      },
      {
        modelId: 'dimensionAxis',
        col: 1,
        row: 1
      },
      {
        modelId: 'subRegion',
        col: 3,
        row: 1
      },
      {
        modelId: 'measureAxisLeft',
        col: 0,
        row: 2
      },
      {
        modelId: 'measureAxisRight',
        col: 3,
        row: 2
      },
      {
        modelId: 'dimensionAxis2',
        col: 2,
        row: 1
      }
    ]
  },
  region: [
    {
      clip: true,
      id: 'mainRegion'
    },
    {
      id: 'subRegion'
    }
  ],
  // component style
  axes: [
    {
      id: 'dimensionAxis',
      type: 'band',
      orient: 'left',
      visible: true,
      label: {
        visible: true,
        style: {
          fontSize: 12,
        },
      },
      hover: true,
      paddingInner: 0.35,
      paddingOuter: 0.2,
      maxWidth: 200,
      
      inverse: true,
      offsetX: 0,      
    },
    {
      id: 'measureAxisLeft',
      type: 'linear',
      orient: 'bottom',
      visible: true,
      label: {
        visible: true,
        space: 4,
        flush: true,
        padding: 0,
        style: {
          fontSize: 12,
          maxLineWidth: 174,
        },
        
      },
      zero: true,
      nice: true,
      regionId: 'mainRegion',
      seriesId: 'mainSeries',
      inverse: true,
    },
    {
      id: 'measureAxisRight',
      type: 'linear',
      orient: 'bottom',
      visible: true,
      label: {
        visible: true,
        space: 4,
        flush: true,
        padding: 0,
        style: {
          fontSize: 12,
        },
      },
      regionId: 'subRegion',
      seriesId: 'subSeries',
    },
    {
      id: 'dimensionAxis2',
      type: 'band',
      orient: 'left',
      visible: false,
      paddingInner: 0.35,
      paddingOuter: 0.2,
      maxWidth: 0,
      inverse: true,
      offsetX: 0,
      regionId: 'subRegion',
      seriesId: 'subSeries',
      ticks: true,
      bandPadding: 0.22
    }
  ],
    title: screenConstants.chartTitleSettings,
    tooltip: screenConstants.tooltipSettings,
    label: {
      visible: true,
      position: 'outside',
      style: {
        fontSize: 14,
        fill: null
      }
    },
    legends: {
      type: 'discrete',
      id: 'legend-discrete',
      visible: true,
      position: 'end',
      item: {
        focus: true,
        focusIconStyle: {
          size: 14
        },
        label: {
          style: {
            fontSize: 12,
          }
        },
      },
    }
  }
}

screenConstants.map=[
  {value:'100000',label:'中华人民共和国'},
  {value:'110000',label:'北京市'},
  {value:'120000',label:'天津市'},
  {value:'130000',label:'河北省'},
  {value:'140000',label:'山西省'},
  {value:'150000',label:'内蒙古自治区'},
  {value:'210000',label:'辽宁省'},
  {value:'220000',label:'吉林省'},
  {value:'230000',label:'黑龙江省'},
  {value:'310000',label:'上海市'},
  {value:'320000',label:'江苏省'},
  {value:'330000',label:'浙江省'},
  {value:'340000',label:'安徽省'},
  {value:'350000',label:'福建省'},
  {value:'360000',label:'江西省'},
  {value:'370000',label:'山东省'},
  {value:'410000',label:'河南省'},
  {value:'420000',label:'湖北省'},
  {value:'430000',label:'湖南省'},
  {value:'440000',label:'广东省'},
  {value:'450000',label:'广西壮族自治区'},
  {value:'460000',label:'海南省'},
  {value:'500000',label:'重庆市'},
  {value:'510000',label:'四川省'},
  {value:'520000',label:'贵州省'},
  {value:'530000',label:'云南省'},
  {value:'540000',label:'西藏自治区'},
  {value:'610000',label:'陕西省'},
  {value:'620000',label:'甘肃省'},
  {value:'630000',label:'青海省'},
  {value:'640000',label:'宁夏回族自治区'},
  {value:'650000',label:'新疆维吾尔自治区'},
  {value:'710000',label:'台湾省'},
  {value:'810000',label:'香港特别行政区'},
  {value:'820000',label:'澳门特别行政区'},
  {value:'110100',label:'北京市市辖区'},
  {value:'120100',label:'天津市市辖区'},
  {value:'130100',label:'石家庄市'},
  {value:'130200',label:'唐山市'},
  {value:'130300',label:'秦皇岛市'},
  {value:'130400',label:'邯郸市'},
  {value:'130500',label:'邢台市'},
  {value:'130600',label:'保定市'},
  {value:'130700',label:'张家口市'},
  {value:'130800',label:'承德市'},
  {value:'130900',label:'沧州市'},
  {value:'131000',label:'廊坊市'},
  {value:'131100',label:'衡水市'},
  {value:'140100',label:'太原市'},
  {value:'140200',label:'大同市'},
  {value:'140300',label:'阳泉市'},
  {value:'140400',label:'长治市'},
  {value:'140500',label:'晋城市'},
  {value:'140600',label:'朔州市'},
  {value:'140700',label:'晋中市'},
  {value:'140800',label:'运城市'},
  {value:'140900',label:'忻州市'},
  {value:'141000',label:'临汾市'},
  {value:'141100',label:'吕梁市'},
  {value:'150100',label:'呼和浩特市'},
  {value:'150200',label:'包头市'},
  {value:'150300',label:'乌海市'},
  {value:'150400',label:'赤峰市'},
  {value:'150500',label:'通辽市'},
  {value:'150600',label:'鄂尔多斯市'},
  {value:'150700',label:'呼伦贝尔市'},
  {value:'150800',label:'巴彦淖尔市'},
  {value:'150900',label:'乌兰察布市'},
  {value:'152200',label:'兴安盟'},
  {value:'152500',label:'锡林郭勒盟'},
  {value:'152900',label:'阿拉善盟'},
  {value:'210100',label:'沈阳市'},
  {value:'210200',label:'大连市'},
  {value:'210300',label:'鞍山市'},
  {value:'210400',label:'抚顺市'},
  {value:'210500',label:'本溪市'},
  {value:'210600',label:'丹东市'},
  {value:'210700',label:'锦州市'},
  {value:'210800',label:'营口市'},
  {value:'210900',label:'阜新市'},
  {value:'211000',label:'辽阳市'},
  {value:'211100',label:'盘锦市'},
  {value:'211200',label:'铁岭市'},
  {value:'211300',label:'朝阳市'},
  {value:'211400',label:'葫芦岛市'},
  {value:'220100',label:'长春市'},
  {value:'220200',label:'吉林市'},
  {value:'220300',label:'四平市'},
  {value:'220400',label:'辽源市'},
  {value:'220500',label:'通化市'},
  {value:'220600',label:'白山市'},
  {value:'220700',label:'松原市'},
  {value:'220800',label:'白城市'},
  {value:'222400',label:'延边朝鲜族自治州'},
  {value:'230100',label:'哈尔滨市'},
  {value:'230200',label:'齐齐哈尔市'},
  {value:'230300',label:'鸡西市'},
  {value:'230400',label:'鹤岗市'},
  {value:'230500',label:'双鸭山市'},
  {value:'230600',label:'大庆市'},
  {value:'230700',label:'伊春市'},
  {value:'230800',label:'佳木斯市'},
  {value:'230900',label:'七台河市'},
  {value:'231000',label:'牡丹江市'},
  {value:'231100',label:'黑河市'},
  {value:'231200',label:'绥化市'},
  {value:'232700',label:'大兴安岭地区'},
  {value:'310100',label:'上海市市辖区'},
  {value:'320100',label:'南京市'},
  {value:'320200',label:'无锡市'},
  {value:'320300',label:'徐州市'},
  {value:'320400',label:'常州市'},
  {value:'320500',label:'苏州市'},
  {value:'320600',label:'南通市'},
  {value:'320700',label:'连云港市'},
  {value:'320800',label:'淮安市'},
  {value:'320900',label:'盐城市'},
  {value:'321000',label:'扬州市'},
  {value:'321100',label:'镇江市'},
  {value:'321200',label:'泰州市'},
  {value:'321300',label:'宿迁市'},
  {value:'330100',label:'杭州市'},
  {value:'330200',label:'宁波市'},
  {value:'330300',label:'温州市'},
  {value:'330400',label:'嘉兴市'},
  {value:'330500',label:'湖州市'},
  {value:'330600',label:'绍兴市'},
  {value:'330700',label:'金华市'},
  {value:'330800',label:'衢州市'},
  {value:'330900',label:'舟山市'},
  {value:'331000',label:'台州市'},
  {value:'331100',label:'丽水市'},
  {value:'340100',label:'合肥市'},
  {value:'340200',label:'芜湖市'},
  {value:'340300',label:'蚌埠市'},
  {value:'340400',label:'淮南市'},
  {value:'340500',label:'马鞍山市'},
  {value:'340600',label:'淮北市'},
  {value:'340700',label:'铜陵市'},
  {value:'340800',label:'安庆市'},
  {value:'341000',label:'黄山市'},
  {value:'341100',label:'滁州市'},
  {value:'341200',label:'阜阳市'},
  {value:'341300',label:'宿州市'},
  {value:'341500',label:'六安市'},
  {value:'341600',label:'亳州市'},
  {value:'341700',label:'池州市'},
  {value:'341800',label:'宣城市'},
  {value:'350100',label:'福州市'},
  {value:'350200',label:'厦门市'},
  {value:'350300',label:'莆田市'},
  {value:'350400',label:'三明市'},
  {value:'350500',label:'泉州市'},
  {value:'350600',label:'漳州市'},
  {value:'350700',label:'南平市'},
  {value:'350800',label:'龙岩市'},
  {value:'350900',label:'宁德市'},
  {value:'360100',label:'南昌市'},
  {value:'360200',label:'景德镇市'},
  {value:'360300',label:'萍乡市'},
  {value:'360400',label:'九江市'},
  {value:'360500',label:'新余市'},
  {value:'360600',label:'鹰潭市'},
  {value:'360700',label:'赣州市'},
  {value:'360800',label:'吉安市'},
  {value:'360900',label:'宜春市'},
  {value:'361000',label:'抚州市'},
  {value:'361100',label:'上饶市'},
  {value:'370100',label:'济南市'},
  {value:'370200',label:'青岛市'},
  {value:'370300',label:'淄博市'},
  {value:'370400',label:'枣庄市'},
  {value:'370500',label:'东营市'},
  {value:'370600',label:'烟台市'},
  {value:'370700',label:'潍坊市'},
  {value:'370800',label:'济宁市'},
  {value:'370900',label:'泰安市'},
  {value:'371000',label:'威海市'},
  {value:'371100',label:'日照市'},
  {value:'371300',label:'临沂市'},
  {value:'371400',label:'德州市'},
  {value:'371500',label:'聊城市'},
  {value:'371600',label:'滨州市'},
  {value:'371700',label:'菏泽市'},
  {value:'410100',label:'郑州市'},
  {value:'410200',label:'开封市'},
  {value:'410300',label:'洛阳市'},
  {value:'410400',label:'平顶山市'},
  {value:'410500',label:'安阳市'},
  {value:'410600',label:'鹤壁市'},
  {value:'410700',label:'新乡市'},
  {value:'410800',label:'焦作市'},
  {value:'410900',label:'濮阳市'},
  {value:'411000',label:'许昌市'},
  {value:'411100',label:'漯河市'},
  {value:'411200',label:'三门峡市'},
  {value:'411300',label:'南阳市'},
  {value:'411400',label:'商丘市'},
  {value:'411500',label:'信阳市'},
  {value:'411600',label:'周口市'},
  {value:'411700',label:'驻马店市'},
  {value:'419001',label:'济源市'},
  {value:'420100',label:'武汉市'},
  {value:'420200',label:'黄石市'},
  {value:'420300',label:'十堰市'},
  {value:'420500',label:'宜昌市'},
  {value:'420600',label:'襄阳市'},
  {value:'420700',label:'鄂州市'},
  {value:'420800',label:'荆门市'},
  {value:'420900',label:'孝感市'},
  {value:'421000',label:'荆州市'},
  {value:'421100',label:'黄冈市'},
  {value:'421200',label:'咸宁市'},
  {value:'421300',label:'随州市'},
  {value:'422800',label:'恩施土家族苗族自治州'},
  {value:'429004',label:'仙桃市'},
  {value:'429005',label:'潜江市'},
  {value:'429006',label:'天门市'},
  {value:'429021',label:'神农架林区'},
  {value:'430100',label:'长沙市'},
  {value:'430200',label:'株洲市'},
  {value:'430300',label:'湘潭市'},
  {value:'430400',label:'衡阳市'},
  {value:'430500',label:'邵阳市'},
  {value:'430600',label:'岳阳市'},
  {value:'430700',label:'常德市'},
  {value:'430800',label:'张家界市'},
  {value:'430900',label:'益阳市'},
  {value:'431000',label:'郴州市'},
  {value:'431100',label:'永州市'},
  {value:'431200',label:'怀化市'},
  {value:'431300',label:'娄底市'},
  {value:'433100',label:'湘西土家族苗族自治州'},
  {value:'440100',label:'广州市'},
  {value:'440200',label:'韶关市'},
  {value:'440300',label:'深圳市'},
  {value:'440400',label:'珠海市'},
  {value:'440500',label:'汕头市'},
  {value:'440600',label:'佛山市'},
  {value:'440700',label:'江门市'},
  {value:'440800',label:'湛江市'},
  {value:'440900',label:'茂名市'},
  {value:'441200',label:'肇庆市'},
  {value:'441300',label:'惠州市'},
  {value:'441400',label:'梅州市'},
  {value:'441500',label:'汕尾市'},
  {value:'441600',label:'河源市'},
  {value:'441700',label:'阳江市'},
  {value:'441800',label:'清远市'},
  {value:'441900',label:'东莞市'},
  {value:'442000',label:'中山市'},
  {value:'445100',label:'潮州市'},
  {value:'445200',label:'揭阳市'},
  {value:'445300',label:'云浮市'},
  {value:'450100',label:'南宁市'},
  {value:'450200',label:'柳州市'},
  {value:'450300',label:'桂林市'},
  {value:'450400',label:'梧州市'},
  {value:'450500',label:'北海市'},
  {value:'450600',label:'防城港市'},
  {value:'450700',label:'钦州市'},
  {value:'450800',label:'贵港市'},
  {value:'450900',label:'玉林市'},
  {value:'451000',label:'百色市'},
  {value:'451100',label:'贺州市'},
  {value:'451200',label:'河池市'},
  {value:'451300',label:'来宾市'},
  {value:'451400',label:'崇左市'},
  {value:'460100',label:'海口市'},
  {value:'460200',label:'三亚市'},
  {value:'460300',label:'三沙市'},
  {value:'460400',label:'儋州市'},
  {value:'469001',label:'五指山市'},
  {value:'469002',label:'琼海市'},
  {value:'469005',label:'文昌市'},
  {value:'469006',label:'万宁市'},
  {value:'469007',label:'东方市'},
  {value:'469021',label:'定安县'},
  {value:'469022',label:'屯昌县'},
  {value:'469023',label:'澄迈县'},
  {value:'469024',label:'临高县'},
  {value:'469025',label:'白沙黎族自治县'},
  {value:'469026',label:'昌江黎族自治县'},
  {value:'469027',label:'乐东黎族自治县'},
  {value:'469028',label:'陵水黎族自治县'},
  {value:'469029',label:'保亭黎族苗族自治县'},
  {value:'469030',label:'琼中黎族苗族自治县'},
  {value:'500100',label:'重庆市市辖区'},
  {value:'500200',label:'重庆市郊县'},
  {value:'510100',label:'成都市'},
  {value:'510300',label:'自贡市'},
  {value:'510400',label:'攀枝花市'},
  {value:'510500',label:'泸州市'},
  {value:'510600',label:'德阳市'},
  {value:'510700',label:'绵阳市'},
  {value:'510800',label:'广元市'},
  {value:'510900',label:'遂宁市'},
  {value:'511000',label:'内江市'},
  {value:'511100',label:'乐山市'},
  {value:'511300',label:'南充市'},
  {value:'511400',label:'眉山市'},
  {value:'511500',label:'宜宾市'},
  {value:'511600',label:'广安市'},
  {value:'511700',label:'达州市'},
  {value:'511800',label:'雅安市'},
  {value:'511900',label:'巴中市'},
  {value:'512000',label:'资阳市'},
  {value:'513200',label:'阿坝藏族羌族自治州'},
  {value:'513300',label:'甘孜藏族自治州'},
  {value:'513400',label:'凉山彝族自治州'},
  {value:'520100',label:'贵阳市'},
  {value:'520200',label:'六盘水市'},
  {value:'520300',label:'遵义市'},
  {value:'520400',label:'安顺市'},
  {value:'520500',label:'毕节市'},
  {value:'520600',label:'铜仁市'},
  {value:'522300',label:'黔西南布依族苗族自治州'},
  {value:'522600',label:'黔东南苗族侗族自治州'},
  {value:'522700',label:'黔南布依族苗族自治州'},
  {value:'530100',label:'昆明市'},
  {value:'530300',label:'曲靖市'},
  {value:'530400',label:'玉溪市'},
  {value:'530500',label:'保山市'},
  {value:'530600',label:'昭通市'},
  {value:'530700',label:'丽江市'},
  {value:'530800',label:'普洱市'},
  {value:'530900',label:'临沧市'},
  {value:'532300',label:'楚雄彝族自治州'},
  {value:'532500',label:'红河哈尼族彝族自治州'},
  {value:'532600',label:'文山壮族苗族自治州'},
  {value:'532800',label:'西双版纳傣族自治州'},
  {value:'532900',label:'大理白族自治州'},
  {value:'533100',label:'德宏傣族景颇族自治州'},
  {value:'533300',label:'怒江傈僳族自治州'},
  {value:'533400',label:'迪庆藏族自治州'},
  {value:'540100',label:'拉萨市'},
  {value:'540200',label:'日喀则市'},
  {value:'540300',label:'昌都市'},
  {value:'540400',label:'林芝市'},
  {value:'540500',label:'山南市'},
  {value:'540600',label:'那曲市'},
  {value:'542500',label:'阿里地区'},
  {value:'610100',label:'西安市'},
  {value:'610200',label:'铜川市'},
  {value:'610300',label:'宝鸡市'},
  {value:'610400',label:'咸阳市'},
  {value:'610500',label:'渭南市'},
  {value:'610600',label:'延安市'},
  {value:'610700',label:'汉中市'},
  {value:'610800',label:'榆林市'},
  {value:'610900',label:'安康市'},
  {value:'611000',label:'商洛市'},
  {value:'620100',label:'兰州市'},
  {value:'620200',label:'嘉峪关市'},
  {value:'620300',label:'金昌市'},
  {value:'620400',label:'白银市'},
  {value:'620500',label:'天水市'},
  {value:'620600',label:'武威市'},
  {value:'620700',label:'张掖市'},
  {value:'620800',label:'平凉市'},
  {value:'620900',label:'酒泉市'},
  {value:'621000',label:'庆阳市'},
  {value:'621100',label:'定西市'},
  {value:'621200',label:'陇南市'},
  {value:'622900',label:'临夏回族自治州'},
  {value:'623000',label:'甘南藏族自治州'},
  {value:'630100',label:'西宁市'},
  {value:'630200',label:'海东市'},
  {value:'632200',label:'海北藏族自治州'},
  {value:'632300',label:'黄南藏族自治州'},
  {value:'632500',label:'海南藏族自治州'},
  {value:'632600',label:'果洛藏族自治州'},
  {value:'632700',label:'玉树藏族自治州'},
  {value:'632800',label:'海西蒙古族藏族自治州'},
  {value:'640100',label:'银川市'},
  {value:'640200',label:'石嘴山市'},
  {value:'640300',label:'吴忠市'},
  {value:'640400',label:'固原市'},
  {value:'640500',label:'中卫市'},
  {value:'650100',label:'乌鲁木齐市'},
  {value:'650200',label:'克拉玛依市'},
  {value:'650400',label:'吐鲁番市'},
  {value:'650500',label:'哈密市'},
  {value:'652300',label:'昌吉回族自治州'},
  {value:'652700',label:'博尔塔拉蒙古自治州'},
  {value:'652800',label:'巴音郭楞蒙古自治州'},
  {value:'652900',label:'阿克苏地区'},
  {value:'653000',label:'克孜勒苏柯尔克孜自治州'},
  {value:'653100',label:'喀什地区'},
  {value:'653200',label:'和田地区'},
  {value:'654000',label:'伊犁哈萨克自治州'},
  {value:'654200',label:'塔城地区'},
  {value:'654300',label:'阿勒泰地区'},
  {value:'659001',label:'石河子市'},
  {value:'659002',label:'阿拉尔市'},
  {value:'659003',label:'图木舒克市'},
  {value:'659004',label:'五家渠市'},
  {value:'659005',label:'北屯市'},
  {value:'659006',label:'铁门关市'},
  {value:'659007',label:'双河市'},
  {value:'659008',label:'可克达拉市'},
  {value:'659009',label:'昆玉市'},
  {value:'810001',label:'中西区'},
  {value:'810002',label:'湾仔区'},
  {value:'810003',label:'东区'},
  {value:'810004',label:'南区'},
  {value:'810005',label:'油尖旺区'},
  {value:'810006',label:'深水埗区'},
  {value:'810007',label:'九龙城区'},
  {value:'810008',label:'黄大仙区'},
  {value:'810009',label:'观塘区'},
  {value:'810010',label:'荃湾区'},
  {value:'810011',label:'屯门区'},
  {value:'810012',label:'元朗区'},
  {value:'810013',label:'北区'},
  {value:'810014',label:'大埔区'},
  {value:'810015',label:'西贡区'},
  {value:'810016',label:'沙田区'},
  {value:'810017',label:'葵青区'},
  {value:'810018',label:'离岛区'},
  {value:'820001',label:'花地玛堂区'},
  {value:'820002',label:'花王堂区'},
  {value:'820003',label:'望德堂区'},
  {value:'820004',label:'大堂区'},
  {value:'820005',label:'风顺堂区'},
  {value:'820006',label:'嘉模堂区'},
  {value:'820007',label:'路凼填海区'},
  {value:'820008',label:'圣方济各堂区'}
]

screenConstants.nameMap = {
  100000: {
    '北京市': '北京市',
    '天津市': '天津市',
    '河北省': '河北省',
    '山西省': '山西省',
    '内蒙古自治区': '内蒙古自治区',
    '辽宁省': '辽宁省',
    '吉林省': '吉林省',
    '黑龙江省': '黑龙江省',
    '上海市': '上海市',
    '江苏省': '江苏省',
    '浙江省': '浙江省',
    '安徽省': '安徽省',
    '福建省': '福建省',
    '江西省': '江西省',
    '山东省': '山东省',
    '河南省': '河南省',
    '湖北省': '湖北省',
    '湖南省': '湖南省',
    '广东省': '广东省',
    '广西壮族自治区': '广西壮族自治区',
    '海南省': '海南省',
    '重庆市': '重庆市',
    '四川省': '四川省',
    '贵州省': '贵州省',
    '云南省': '云南省',
    '西藏自治区': '西藏自治区',
    '陕西省': '陕西省',
    '甘肃省': '甘肃省',
    '青海省': '青海省',
    '宁夏回族自治区': '宁夏回族自治区',
    '新疆维吾尔自治区': '新疆维吾尔自治区',
    '台湾省': '台湾省',
    '香港特别行政区': '香港特别行政区',
    '澳门特别行政区': '澳门特别行政区'
  }, 110000: {
    '北京市市辖区': '北京市市辖区'
  }, 120000: {
    '天津市市辖区': '天津市市辖区'
  }, 130000: {
    '石家庄市': '石家庄市',
    '唐山市': '唐山市',
    '秦皇岛市': '秦皇岛市',
    '邯郸市': '邯郸市',
    '邢台市': '邢台市',
    '保定市': '保定市',
    '张家口市': '张家口市',
    '承德市': '承德市',
    '沧州市': '沧州市',
    '廊坊市': '廊坊市',
    '衡水市': '衡水市'
  }, 140000: {
    '太原市': '太原市',
    '大同市': '大同市',
    '阳泉市': '阳泉市',
    '长治市': '长治市',
    '晋城市': '晋城市',
    '朔州市': '朔州市',
    '晋中市': '晋中市',
    '运城市': '运城市',
    '忻州市': '忻州市',
    '临汾市': '临汾市',
    '吕梁市': '吕梁市'
  }, 150000: {
    '呼和浩特市': '呼和浩特市',
    '包头市': '包头市',
    '乌海市': '乌海市',
    '赤峰市': '赤峰市',
    '通辽市': '通辽市',
    '鄂尔多斯市': '鄂尔多斯市',
    '呼伦贝尔市': '呼伦贝尔市',
    '巴彦淖尔市': '巴彦淖尔市',
    '乌兰察布市': '乌兰察布市',
    '兴安盟': '兴安盟',
    '锡林郭勒盟': '锡林郭勒盟',
    '阿拉善盟': '阿拉善盟'
  }, 210000: {
    '沈阳市': '沈阳市',
    '大连市': '大连市',
    '鞍山市': '鞍山市',
    '抚顺市': '抚顺市',
    '本溪市': '本溪市',
    '丹东市': '丹东市',
    '锦州市': '锦州市',
    '营口市': '营口市',
    '阜新市': '阜新市',
    '辽阳市': '辽阳市',
    '盘锦市': '盘锦市',
    '铁岭市': '铁岭市',
    '朝阳市': '朝阳市',
    '葫芦岛市': '葫芦岛市'
  }, 220000: {
    '长春市': '长春市',
    '吉林市': '吉林市',
    '四平市': '四平市',
    '辽源市': '辽源市',
    '通化市': '通化市',
    '白山市': '白山市',
    '松原市': '松原市',
    '白城市': '白城市',
    '延边朝鲜族自治州': '延边朝鲜族自治州'
  }, 230000: {
    '哈尔滨市': '哈尔滨市',
    '齐齐哈尔市': '齐齐哈尔市',
    '鸡西市': '鸡西市',
    '鹤岗市': '鹤岗市',
    '双鸭山市': '双鸭山市',
    '大庆市': '大庆市',
    '伊春市': '伊春市',
    '佳木斯市': '佳木斯市',
    '七台河市': '七台河市',
    '牡丹江市': '牡丹江市',
    '黑河市': '黑河市',
    '绥化市': '绥化市',
    '大兴安岭地区': '大兴安岭地区'
  }, 310000: {
    '上海市市辖区': '上海市市辖区'
  }, 320000: {
    '南京市': '南京市',
    '无锡市': '无锡市',
    '徐州市': '徐州市',
    '常州市': '常州市',
    '苏州市': '苏州市',
    '南通市': '南通市',
    '连云港市': '连云港市',
    '淮安市': '淮安市',
    '盐城市': '盐城市',
    '扬州市': '扬州市',
    '镇江市': '镇江市',
    '泰州市': '泰州市',
    '宿迁市': '宿迁市'
  }, 330000: {
    '杭州市': '杭州市',
    '宁波市': '宁波市',
    '温州市': '温州市',
    '嘉兴市': '嘉兴市',
    '湖州市': '湖州市',
    '绍兴市': '绍兴市',
    '金华市': '金华市',
    '衢州市': '衢州市',
    '舟山市': '舟山市',
    '台州市': '台州市',
    '丽水市': '丽水市'
  }, 340000: {
    '合肥市': '合肥市',
    '芜湖市': '芜湖市',
    '蚌埠市': '蚌埠市',
    '淮南市': '淮南市',
    '马鞍山市': '马鞍山市',
    '淮北市': '淮北市',
    '铜陵市': '铜陵市',
    '安庆市': '安庆市',
    '黄山市': '黄山市',
    '滁州市': '滁州市',
    '阜阳市': '阜阳市',
    '宿州市': '宿州市',
    '六安市': '六安市',
    '亳州市': '亳州市',
    '池州市': '池州市',
    '宣城市': '宣城市'
  }, 350000: {
    '福州市': '福州市',
    '厦门市': '厦门市',
    '莆田市': '莆田市',
    '三明市': '三明市',
    '泉州市': '泉州市',
    '漳州市': '漳州市',
    '南平市': '南平市',
    '龙岩市': '龙岩市',
    '宁德市': '宁德市'
  }, 360000: {
    '南昌市': '南昌市',
    '景德镇市': '景德镇市',
    '萍乡市': '萍乡市',
    '九江市': '九江市',
    '新余市': '新余市',
    '鹰潭市': '鹰潭市',
    '赣州市': '赣州市',
    '吉安市': '吉安市',
    '宜春市': '宜春市',
    '抚州市': '抚州市',
    '上饶市': '上饶市'
  }, 370000: {
    '济南市': '济南市',
    '青岛市': '青岛市',
    '淄博市': '淄博市',
    '枣庄市': '枣庄市',
    '东营市': '东营市',
    '烟台市': '烟台市',
    '潍坊市': '潍坊市',
    '济宁市': '济宁市',
    '泰安市': '泰安市',
    '威海市': '威海市',
    '日照市': '日照市',
    '临沂市': '临沂市',
    '德州市': '德州市',
    '聊城市': '聊城市',
    '滨州市': '滨州市',
    '菏泽市': '菏泽市'
  }, 410000: {
    '郑州市': '郑州市',
    '开封市': '开封市',
    '洛阳市': '洛阳市',
    '平顶山市': '平顶山市',
    '安阳市': '安阳市',
    '鹤壁市': '鹤壁市',
    '新乡市': '新乡市',
    '焦作市': '焦作市',
    '濮阳市': '濮阳市',
    '许昌市': '许昌市',
    '漯河市': '漯河市',
    '三门峡市': '三门峡市',
    '南阳市': '南阳市',
    '商丘市': '商丘市',
    '信阳市': '信阳市',
    '周口市': '周口市',
    '驻马店市': '驻马店市',
    '济源市': '济源市'
  }, 420000: {
    '武汉市': '武汉市',
    '黄石市': '黄石市',
    '十堰市': '十堰市',
    '宜昌市': '宜昌市',
    '襄阳市': '襄阳市',
    '鄂州市': '鄂州市',
    '荆门市': '荆门市',
    '孝感市': '孝感市',
    '荆州市': '荆州市',
    '黄冈市': '黄冈市',
    '咸宁市': '咸宁市',
    '随州市': '随州市',
    '恩施土家族苗族自治州': '恩施土家族苗族自治州',
    '仙桃市': '仙桃市',
    '潜江市': '潜江市',
    '天门市': '天门市',
    '神农架林区': '神农架林区'
  }, 430000: {
    '长沙市': '长沙市',
    '株洲市': '株洲市',
    '湘潭市': '湘潭市',
    '衡阳市': '衡阳市',
    '邵阳市': '邵阳市',
    '岳阳市': '岳阳市',
    '常德市': '常德市',
    '张家界市': '张家界市',
    '益阳市': '益阳市',
    '郴州市': '郴州市',
    '永州市': '永州市',
    '怀化市': '怀化市',
    '娄底市': '娄底市',
    '湘西土家族苗族自治州': '湘西土家族苗族自治州'
  }, 440000: {
    '广州市': '广州市',
    '韶关市': '韶关市',
    '深圳市': '深圳市',
    '珠海市': '珠海市',
    '汕头市': '汕头市',
    '佛山市': '佛山市',
    '江门市': '江门市',
    '湛江市': '湛江市',
    '茂名市': '茂名市',
    '肇庆市': '肇庆市',
    '惠州市': '惠州市',
    '梅州市': '梅州市',
    '汕尾市': '汕尾市',
    '河源市': '河源市',
    '阳江市': '阳江市',
    '清远市': '清远市',
    '东莞市': '东莞市',
    '中山市': '中山市',
    '潮州市': '潮州市',
    '揭阳市': '揭阳市',
    '云浮市': '云浮市'
  }, 450000: {
    '南宁市': '南宁市',
    '柳州市': '柳州市',
    '桂林市': '桂林市',
    '梧州市': '梧州市',
    '北海市': '北海市',
    '防城港市': '防城港市',
    '钦州市': '钦州市',
    '贵港市': '贵港市',
    '玉林市': '玉林市',
    '百色市': '百色市',
    '贺州市': '贺州市',
    '河池市': '河池市',
    '来宾市': '来宾市',
    '崇左市': '崇左市'
  }, 460000: {
    '海口市': '海口市',
    '三亚市': '三亚市',
    '三沙市': '三沙市',
    '儋州市': '儋州市',
    '五指山市': '五指山市',
    '琼海市': '琼海市',
    '文昌市': '文昌市',
    '万宁市': '万宁市',
    '东方市': '东方市',
    '定安县': '定安县',
    '屯昌县': '屯昌县',
    '澄迈县': '澄迈县',
    '临高县': '临高县',
    '白沙黎族自治县': '白沙黎族自治县',
    '昌江黎族自治县': '昌江黎族自治县',
    '乐东黎族自治县': '乐东黎族自治县',
    '陵水黎族自治县': '陵水黎族自治县',
    '保亭黎族苗族自治县': '保亭黎族苗族自治县',
    '琼中黎族苗族自治县': '琼中黎族苗族自治县'
  }, 500000: {
    '重庆市市辖区': '重庆市市辖区',
    '重庆市郊县': '重庆市郊县'
  }, 510000: {
    '成都市': '成都市',
    '自贡市': '自贡市',
    '攀枝花市': '攀枝花市',
    '泸州市': '泸州市',
    '德阳市': '德阳市',
    '绵阳市': '绵阳市',
    '广元市': '广元市',
    '遂宁市': '遂宁市',
    '内江市': '内江市',
    '乐山市': '乐山市',
    '南充市': '南充市',
    '眉山市': '眉山市',
    '宜宾市': '宜宾市',
    '广安市': '广安市',
    '达州市': '达州市',
    '雅安市': '雅安市',
    '巴中市': '巴中市',
    '资阳市': '资阳市',
    '阿坝藏族羌族自治州': '阿坝藏族羌族自治州',
    '甘孜藏族自治州': '甘孜藏族自治州',
    '凉山彝族自治州': '凉山彝族自治州'
  }, 520000: {
    '贵阳市': '贵阳市',
    '六盘水市': '六盘水市',
    '遵义市': '遵义市',
    '安顺市': '安顺市',
    '毕节市': '毕节市',
    '铜仁市': '铜仁市',
    '黔西南布依族苗族自治州': '黔西南布依族苗族自治州',
    '黔东南苗族侗族自治州': '黔东南苗族侗族自治州',
    '黔南布依族苗族自治州': '黔南布依族苗族自治州'
  }, 530000: {
    '昆明市': '昆明市',
    '曲靖市': '曲靖市',
    '玉溪市': '玉溪市',
    '保山市': '保山市',
    '昭通市': '昭通市',
    '丽江市': '丽江市',
    '普洱市': '普洱市',
    '临沧市': '临沧市',
    '楚雄彝族自治州': '楚雄彝族自治州',
    '红河哈尼族彝族自治州': '红河哈尼族彝族自治州',
    '文山壮族苗族自治州': '文山壮族苗族自治州',
    '西双版纳傣族自治州': '西双版纳傣族自治州',
    '大理白族自治州': '大理白族自治州',
    '德宏傣族景颇族自治州': '德宏傣族景颇族自治州',
    '怒江傈僳族自治州': '怒江傈僳族自治州',
    '迪庆藏族自治州': '迪庆藏族自治州'
  }, 540000: {
    '拉萨市': '拉萨市',
    '日喀则市': '日喀则市',
    '昌都市': '昌都市',
    '林芝市': '林芝市',
    '山南市': '山南市',
    '那曲市': '那曲市',
    '阿里地区': '阿里地区'
  }, 610000: {
    '西安市': '西安市',
    '铜川市': '铜川市',
    '宝鸡市': '宝鸡市',
    '咸阳市': '咸阳市',
    '渭南市': '渭南市',
    '延安市': '延安市',
    '汉中市': '汉中市',
    '榆林市': '榆林市',
    '安康市': '安康市',
    '商洛市': '商洛市'
  }, 620000: {
    '兰州市': '兰州市',
    '嘉峪关市': '嘉峪关市',
    '金昌市': '金昌市',
    '白银市': '白银市',
    '天水市': '天水市',
    '武威市': '武威市',
    '张掖市': '张掖市',
    '平凉市': '平凉市',
    '酒泉市': '酒泉市',
    '庆阳市': '庆阳市',
    '定西市': '定西市',
    '陇南市': '陇南市',
    '临夏回族自治州': '临夏回族自治州',
    '甘南藏族自治州': '甘南藏族自治州'
  }, 630000: {
    '西宁市': '西宁市',
    '海东市': '海东市',
    '海北藏族自治州': '海北藏族自治州',
    '黄南藏族自治州': '黄南藏族自治州',
    '海南藏族自治州': '海南藏族自治州',
    '果洛藏族自治州': '果洛藏族自治州',
    '玉树藏族自治州': '玉树藏族自治州',
    '海西蒙古族藏族自治州': '海西蒙古族藏族自治州'
  }, 640000: {
    '银川市': '银川市',
    '石嘴山市': '石嘴山市',
    '吴忠市': '吴忠市',
    '固原市': '固原市',
    '中卫市': '中卫市'
  }, 650000: {
    '乌鲁木齐市': '乌鲁木齐市',
    '克拉玛依市': '克拉玛依市',
    '吐鲁番市': '吐鲁番市',
    '哈密市': '哈密市',
    '昌吉回族自治州': '昌吉回族自治州',
    '博尔塔拉蒙古自治州': '博尔塔拉蒙古自治州',
    '巴音郭楞蒙古自治州': '巴音郭楞蒙古自治州',
    '阿克苏地区': '阿克苏地区',
    '克孜勒苏柯尔克孜自治州': '克孜勒苏柯尔克孜自治州',
    '喀什地区': '喀什地区',
    '和田地区': '和田地区',
    '伊犁哈萨克自治州': '伊犁哈萨克自治州',
    '塔城地区': '塔城地区',
    '阿勒泰地区': '阿勒泰地区',
    '石河子市': '石河子市',
    '阿拉尔市': '阿拉尔市',
    '图木舒克市': '图木舒克市',
    '五家渠市': '五家渠市',
    '北屯市': '北屯市',
    '铁门关市': '铁门关市',
    '双河市': '双河市',
    '可克达拉市': '可克达拉市',
    '昆玉市': '昆玉市'
  }, 710000: {}, 810000: {
    '中西区': '中西区',
    '湾仔区': '湾仔区',
    '东区': '东区',
    '南区': '南区',
    '油尖旺区': '油尖旺区',
    '深水埗区': '深水埗区',
    '九龙城区': '九龙城区',
    '黄大仙区': '黄大仙区',
    '观塘区': '观塘区',
    '荃湾区': '荃湾区',
    '屯门区': '屯门区',
    '元朗区': '元朗区',
    '北区': '北区',
    '大埔区': '大埔区',
    '西贡区': '西贡区',
    '沙田区': '沙田区',
    '葵青区': '葵青区',
    '离岛区': '离岛区'
  }, 820000: {
    '花地玛堂区': '花地玛堂区',
    '花王堂区': '花王堂区',
    '望德堂区': '望德堂区',
    '大堂区': '大堂区',
    '风顺堂区': '风顺堂区',
    '嘉模堂区': '嘉模堂区',
    '路凼填海区': '路凼填海区',
    '圣方济各堂区': '圣方济各堂区'
  },
  110100: {
    '东城区': '东城区',
    '西城区': '西城区',
    '朝阳区': '朝阳区',
    '丰台区': '丰台区',
    '石景山区': '石景山区',
    '海淀区': '海淀区',
    '门头沟区': '门头沟区',
    '房山区': '房山区',
    '通州区': '通州区',
    '顺义区': '顺义区',
    '昌平区': '昌平区',
    '大兴区': '大兴区',
    '怀柔区': '怀柔区',
    '平谷区': '平谷区',
    '密云区': '密云区',
    '延庆区': '延庆区'
  }, 120100: {
    '和平区': '和平区',
    '河东区': '河东区',
    '河西区': '河西区',
    '南开区': '南开区',
    '河北区': '河北区',
    '红桥区': '红桥区',
    '东丽区': '东丽区',
    '西青区': '西青区',
    '津南区': '津南区',
    '北辰区': '北辰区',
    '武清区': '武清区',
    '宝坻区': '宝坻区',
    '滨海新区': '滨海新区',
    '宁河区': '宁河区',
    '静海区': '静海区',
    '蓟州区': '蓟州区'
  }, 130100: {
    '石家庄市市辖区': '石家庄市市辖区',
    '长安区': '长安区',
    '桥西区': '桥西区',
    '新华区': '新华区',
    '井陉矿区': '井陉矿区',
    '裕华区': '裕华区',
    '藁城区': '藁城区',
    '鹿泉区': '鹿泉区',
    '栾城区': '栾城区',
    '井陉县': '井陉县',
    '正定县': '正定县',
    '行唐县': '行唐县',
    '灵寿县': '灵寿县',
    '高邑县': '高邑县',
    '深泽县': '深泽县',
    '赞皇县': '赞皇县',
    '无极县': '无极县',
    '平山县': '平山县',
    '元氏县': '元氏县',
    '赵县': '赵县',
    '辛集市': '辛集市',
    '晋州市': '晋州市',
    '新乐市': '新乐市'
  }, 130200: {
    '唐山市市辖区': '唐山市市辖区',
    '路南区': '路南区',
    '路北区': '路北区',
    '古冶区': '古冶区',
    '开平区': '开平区',
    '丰南区': '丰南区',
    '丰润区': '丰润区',
    '曹妃甸区': '曹妃甸区',
    '滦州市': '滦州市',
    '滦南县': '滦南县',
    '乐亭县': '乐亭县',
    '迁西县': '迁西县',
    '玉田县': '玉田县',
    '遵化市': '遵化市',
    '迁安市': '迁安市'
  }, 130300: {
    '秦皇岛市市辖区': '秦皇岛市市辖区',
    '海港区': '海港区',
    '山海关区': '山海关区',
    '北戴河区': '北戴河区',
    '抚宁区': '抚宁区',
    '青龙满族自治县': '青龙满族自治县',
    '昌黎县': '昌黎县',
    '卢龙县': '卢龙县'
  }, 130400: {
    '邯郸市市辖区': '邯郸市市辖区',
    '邯山区': '邯山区',
    '丛台区': '丛台区',
    '复兴区': '复兴区',
    '峰峰矿区': '峰峰矿区',
    '肥乡区': '肥乡区',
    '永年区': '永年区',
    '临漳县': '临漳县',
    '成安县': '成安县',
    '大名县': '大名县',
    '涉县': '涉县',
    '磁县': '磁县',
    '邱县': '邱县',
    '鸡泽县': '鸡泽县',
    '广平县': '广平县',
    '馆陶县': '馆陶县',
    '魏县': '魏县',
    '曲周县': '曲周县',
    '武安市': '武安市'
  }, 130500: {
    '邢台市市辖区': '邢台市市辖区',
    '桥东区': '桥东区',
    '桥西区': '桥西区',
    '邢台县': '邢台县',
    '临城县': '临城县',
    '内丘县': '内丘县',
    '柏乡县': '柏乡县',
    '隆尧县': '隆尧县',
    '任县': '任县',
    '南和县': '南和县',
    '宁晋县': '宁晋县',
    '巨鹿县': '巨鹿县',
    '新河县': '新河县',
    '广宗县': '广宗县',
    '平乡县': '平乡县',
    '威县': '威县',
    '清河县': '清河县',
    '临西县': '临西县',
    '南宫市': '南宫市',
    '沙河市': '沙河市'
  }, 130600: {
    '保定市市辖区': '保定市市辖区',
    '竞秀区': '竞秀区',
    '莲池区': '莲池区',
    '满城区': '满城区',
    '清苑区': '清苑区',
    '徐水区': '徐水区',
    '涞水县': '涞水县',
    '阜平县': '阜平县',
    '定兴县': '定兴县',
    '唐县': '唐县',
    '高阳县': '高阳县',
    '容城县': '容城县',
    '涞源县': '涞源县',
    '望都县': '望都县',
    '安新县': '安新县',
    '易县': '易县',
    '曲阳县': '曲阳县',
    '蠡县': '蠡县',
    '顺平县': '顺平县',
    '博野县': '博野县',
    '雄县': '雄县',
    '涿州市': '涿州市',
    '定州市': '定州市',
    '安国市': '安国市',
    '高碑店市': '高碑店市'
  }, 130700: {
    '张家口市市辖区': '张家口市市辖区',
    '桥东区': '桥东区',
    '桥西区': '桥西区',
    '宣化区': '宣化区',
    '下花园区': '下花园区',
    '万全区': '万全区',
    '崇礼区': '崇礼区',
    '张北县': '张北县',
    '康保县': '康保县',
    '沽源县': '沽源县',
    '尚义县': '尚义县',
    '蔚县': '蔚县',
    '阳原县': '阳原县',
    '怀安县': '怀安县',
    '怀来县': '怀来县',
    '涿鹿县': '涿鹿县',
    '赤城县': '赤城县'
  }, 130800: {
    '承德市市辖区': '承德市市辖区',
    '双桥区': '双桥区',
    '双滦区': '双滦区',
    '鹰手营子矿区': '鹰手营子矿区',
    '承德县': '承德县',
    '兴隆县': '兴隆县',
    '滦平县': '滦平县',
    '隆化县': '隆化县',
    '丰宁满族自治县': '丰宁满族自治县',
    '宽城满族自治县': '宽城满族自治县',
    '围场满族蒙古族自治县': '围场满族蒙古族自治县',
    '平泉市': '平泉市'
  }, 130900: {
    '沧州市市辖区': '沧州市市辖区',
    '新华区': '新华区',
    '运河区': '运河区',
    '沧县': '沧县',
    '青县': '青县',
    '东光县': '东光县',
    '海兴县': '海兴县',
    '盐山县': '盐山县',
    '肃宁县': '肃宁县',
    '南皮县': '南皮县',
    '吴桥县': '吴桥县',
    '献县': '献县',
    '孟村回族自治县': '孟村回族自治县',
    '泊头市': '泊头市',
    '任丘市': '任丘市',
    '黄骅市': '黄骅市',
    '河间市': '河间市'
  }, 131000: {
    '廊坊市市辖区': '廊坊市市辖区',
    '安次区': '安次区',
    '广阳区': '广阳区',
    '固安县': '固安县',
    '永清县': '永清县',
    '香河县': '香河县',
    '大城县': '大城县',
    '文安县': '文安县',
    '大厂回族自治县': '大厂回族自治县',
    '霸州市': '霸州市',
    '三河市': '三河市'
  }, 131100: {
    '衡水市市辖区': '衡水市市辖区',
    '桃城区': '桃城区',
    '冀州区': '冀州区',
    '枣强县': '枣强县',
    '武邑县': '武邑县',
    '武强县': '武强县',
    '饶阳县': '饶阳县',
    '安平县': '安平县',
    '故城县': '故城县',
    '景县': '景县',
    '阜城县': '阜城县',
    '深州市': '深州市'
  }, 140100: {
    '太原市市辖区': '太原市市辖区',
    '小店区': '小店区',
    '迎泽区': '迎泽区',
    '杏花岭区': '杏花岭区',
    '尖草坪区': '尖草坪区',
    '万柏林区': '万柏林区',
    '晋源区': '晋源区',
    '清徐县': '清徐县',
    '阳曲县': '阳曲县',
    '娄烦县': '娄烦县',
    '古交市': '古交市'
  }, 140200: {
    '大同市市辖区': '大同市市辖区',
    '平城区': '平城区',
    '云冈区': '云冈区',
    '新荣区': '新荣区',
    '阳高县': '阳高县',
    '天镇县': '天镇县',
    '广灵县': '广灵县',
    '灵丘县': '灵丘县',
    '浑源县': '浑源县',
    '左云县': '左云县',
    '云州区': '云州区'
  }, 140300: {
    '阳泉市市辖区': '阳泉市市辖区',
    '城区': '城区',
    '矿区': '矿区',
    '郊区': '郊区',
    '平定县': '平定县',
    '盂县': '盂县'
  }, 140400: {
    '长治市市辖区': '长治市市辖区',
    '潞州区': '潞州区',
    '上党区': '上党区',
    '襄垣县': '襄垣县',
    '屯留区': '屯留区',
    '平顺县': '平顺县',
    '黎城县': '黎城县',
    '壶关县': '壶关县',
    '长子县': '长子县',
    '武乡县': '武乡县',
    '沁县': '沁县',
    '沁源县': '沁源县',
    '潞城区': '潞城区'
  }, 140500: {
    '晋城市市辖区': '晋城市市辖区',
    '城区': '城区',
    '沁水县': '沁水县',
    '阳城县': '阳城县',
    '陵川县': '陵川县',
    '泽州县': '泽州县',
    '高平市': '高平市'
  }, 140600: {
    '朔州市市辖区': '朔州市市辖区',
    '朔城区': '朔城区',
    '平鲁区': '平鲁区',
    '山阴县': '山阴县',
    '应县': '应县',
    '右玉县': '右玉县',
    '怀仁市': '怀仁市'
  }, 140700: {
    '晋中市市辖区': '晋中市市辖区',
    '榆次区': '榆次区',
    '榆社县': '榆社县',
    '左权县': '左权县',
    '和顺县': '和顺县',
    '昔阳县': '昔阳县',
    '寿阳县': '寿阳县',
    '太谷县': '太谷县',
    '祁县': '祁县',
    '平遥县': '平遥县',
    '灵石县': '灵石县',
    '介休市': '介休市'
  }, 140800: {
    '运城市市辖区': '运城市市辖区',
    '盐湖区': '盐湖区',
    '临猗县': '临猗县',
    '万荣县': '万荣县',
    '闻喜县': '闻喜县',
    '稷山县': '稷山县',
    '新绛县': '新绛县',
    '绛县': '绛县',
    '垣曲县': '垣曲县',
    '夏县': '夏县',
    '平陆县': '平陆县',
    '芮城县': '芮城县',
    '永济市': '永济市',
    '河津市': '河津市'
  }, 140900: {
    '忻州市市辖区': '忻州市市辖区',
    '忻府区': '忻府区',
    '定襄县': '定襄县',
    '五台县': '五台县',
    '代县': '代县',
    '繁峙县': '繁峙县',
    '宁武县': '宁武县',
    '静乐县': '静乐县',
    '神池县': '神池县',
    '五寨县': '五寨县',
    '岢岚县': '岢岚县',
    '河曲县': '河曲县',
    '保德县': '保德县',
    '偏关县': '偏关县',
    '原平市': '原平市'
  }, 141000: {
    '临汾市市辖区': '临汾市市辖区',
    '尧都区': '尧都区',
    '曲沃县': '曲沃县',
    '翼城县': '翼城县',
    '襄汾县': '襄汾县',
    '洪洞县': '洪洞县',
    '古县': '古县',
    '安泽县': '安泽县',
    '浮山县': '浮山县',
    '吉县': '吉县',
    '乡宁县': '乡宁县',
    '大宁县': '大宁县',
    '隰县': '隰县',
    '永和县': '永和县',
    '蒲县': '蒲县',
    '汾西县': '汾西县',
    '侯马市': '侯马市',
    '霍州市': '霍州市'
  }, 141100: {
    '吕梁市市辖区': '吕梁市市辖区',
    '离石区': '离石区',
    '文水县': '文水县',
    '交城县': '交城县',
    '兴县': '兴县',
    '临县': '临县',
    '柳林县': '柳林县',
    '石楼县': '石楼县',
    '岚县': '岚县',
    '方山县': '方山县',
    '中阳县': '中阳县',
    '交口县': '交口县',
    '孝义市': '孝义市',
    '汾阳市': '汾阳市'
  }, 150100: {
    '呼和浩特市市辖区': '呼和浩特市市辖区',
    '新城区': '新城区',
    '回民区': '回民区',
    '玉泉区': '玉泉区',
    '赛罕区': '赛罕区',
    '土默特左旗': '土默特左旗',
    '托克托县': '托克托县',
    '和林格尔县': '和林格尔县',
    '清水河县': '清水河县',
    '武川县': '武川县'
  }, 150200: {
    '包头市市辖区': '包头市市辖区',
    '东河区': '东河区',
    '昆都仑区': '昆都仑区',
    '青山区': '青山区',
    '石拐区': '石拐区',
    '白云鄂博矿区': '白云鄂博矿区',
    '九原区': '九原区',
    '土默特右旗': '土默特右旗',
    '固阳县': '固阳县',
    '达尔罕茂明安联合旗': '达尔罕茂明安联合旗'
  }, 150300: {
    '乌海市市辖区': '乌海市市辖区',
    '海勃湾区': '海勃湾区',
    '海南区': '海南区',
    '乌达区': '乌达区'
  }, 150400: {
    '赤峰市市辖区': '赤峰市市辖区',
    '红山区': '红山区',
    '元宝山区': '元宝山区',
    '松山区': '松山区',
    '阿鲁科尔沁旗': '阿鲁科尔沁旗',
    '巴林左旗': '巴林左旗',
    '巴林右旗': '巴林右旗',
    '林西县': '林西县',
    '克什克腾旗': '克什克腾旗',
    '翁牛特旗': '翁牛特旗',
    '喀喇沁旗': '喀喇沁旗',
    '宁城县': '宁城县',
    '敖汉旗': '敖汉旗'
  }, 150500: {
    '通辽市市辖区': '通辽市市辖区',
    '科尔沁区': '科尔沁区',
    '科尔沁左翼中旗': '科尔沁左翼中旗',
    '科尔沁左翼后旗': '科尔沁左翼后旗',
    '开鲁县': '开鲁县',
    '库伦旗': '库伦旗',
    '奈曼旗': '奈曼旗',
    '扎鲁特旗': '扎鲁特旗',
    '霍林郭勒市': '霍林郭勒市'
  }, 150600: {
    '鄂尔多斯市市辖区': '鄂尔多斯市市辖区',
    '东胜区': '东胜区',
    '康巴什区': '康巴什区',
    '达拉特旗': '达拉特旗',
    '准格尔旗': '准格尔旗',
    '鄂托克前旗': '鄂托克前旗',
    '鄂托克旗': '鄂托克旗',
    '杭锦旗': '杭锦旗',
    '乌审旗': '乌审旗',
    '伊金霍洛旗': '伊金霍洛旗'
  }, 150700: {
    '呼伦贝尔市市辖区': '呼伦贝尔市市辖区',
    '海拉尔区': '海拉尔区',
    '扎赉诺尔区': '扎赉诺尔区',
    '阿荣旗': '阿荣旗',
    '莫力达瓦达斡尔族自治旗': '莫力达瓦达斡尔族自治旗',
    '鄂伦春自治旗': '鄂伦春自治旗',
    '鄂温克族自治旗': '鄂温克族自治旗',
    '陈巴尔虎旗': '陈巴尔虎旗',
    '新巴尔虎左旗': '新巴尔虎左旗',
    '新巴尔虎右旗': '新巴尔虎右旗',
    '满洲里市': '满洲里市',
    '牙克石市': '牙克石市',
    '扎兰屯市': '扎兰屯市',
    '额尔古纳市': '额尔古纳市',
    '根河市': '根河市'
  }, 150800: {
    '巴彦淖尔市市辖区': '巴彦淖尔市市辖区',
    '临河区': '临河区',
    '五原县': '五原县',
    '磴口县': '磴口县',
    '乌拉特前旗': '乌拉特前旗',
    '乌拉特中旗': '乌拉特中旗',
    '乌拉特后旗': '乌拉特后旗',
    '杭锦后旗': '杭锦后旗'
  }, 150900: {
    '乌兰察布市市辖区': '乌兰察布市市辖区',
    '集宁区': '集宁区',
    '卓资县': '卓资县',
    '化德县': '化德县',
    '商都县': '商都县',
    '兴和县': '兴和县',
    '凉城县': '凉城县',
    '察哈尔右翼前旗': '察哈尔右翼前旗',
    '察哈尔右翼中旗': '察哈尔右翼中旗',
    '察哈尔右翼后旗': '察哈尔右翼后旗',
    '四子王旗': '四子王旗',
    '丰镇市': '丰镇市'
  }, 152200: {
    '乌兰浩特市': '乌兰浩特市',
    '阿尔山市': '阿尔山市',
    '科尔沁右翼前旗': '科尔沁右翼前旗',
    '科尔沁右翼中旗': '科尔沁右翼中旗',
    '扎赉特旗': '扎赉特旗',
    '突泉县': '突泉县'
  }, 152500: {
    '二连浩特市': '二连浩特市',
    '锡林浩特市': '锡林浩特市',
    '阿巴嘎旗': '阿巴嘎旗',
    '苏尼特左旗': '苏尼特左旗',
    '苏尼特右旗': '苏尼特右旗',
    '东乌珠穆沁旗': '东乌珠穆沁旗',
    '西乌珠穆沁旗': '西乌珠穆沁旗',
    '太仆寺旗': '太仆寺旗',
    '镶黄旗': '镶黄旗',
    '正镶白旗': '正镶白旗',
    '正蓝旗': '正蓝旗',
    '多伦县': '多伦县'
  }, 152900: {
    '阿拉善左旗': '阿拉善左旗',
    '阿拉善右旗': '阿拉善右旗',
    '额济纳旗': '额济纳旗'
  }, 210100: {
    '沈阳市市辖区': '沈阳市市辖区',
    '和平区': '和平区',
    '沈河区': '沈河区',
    '大东区': '大东区',
    '皇姑区': '皇姑区',
    '铁西区': '铁西区',
    '苏家屯区': '苏家屯区',
    '浑南区': '浑南区',
    '沈北新区': '沈北新区',
    '于洪区': '于洪区',
    '辽中区': '辽中区',
    '康平县': '康平县',
    '法库县': '法库县',
    '新民市': '新民市'
  }, 210200: {
    '大连市市辖区': '大连市市辖区',
    '中山区': '中山区',
    '西岗区': '西岗区',
    '沙河口区': '沙河口区',
    '甘井子区': '甘井子区',
    '旅顺口区': '旅顺口区',
    '金州区': '金州区',
    '普兰店区': '普兰店区',
    '长海县': '长海县',
    '瓦房店市': '瓦房店市',
    '庄河市': '庄河市'
  }, 210300: {
    '鞍山市市辖区': '鞍山市市辖区',
    '铁东区': '铁东区',
    '铁西区': '铁西区',
    '立山区': '立山区',
    '千山区': '千山区',
    '台安县': '台安县',
    '岫岩满族自治县': '岫岩满族自治县',
    '海城市': '海城市'
  }, 210400: {
    '抚顺市市辖区': '抚顺市市辖区',
    '新抚区': '新抚区',
    '东洲区': '东洲区',
    '望花区': '望花区',
    '顺城区': '顺城区',
    '抚顺县': '抚顺县',
    '新宾满族自治县': '新宾满族自治县',
    '清原满族自治县': '清原满族自治县'
  }, 210500: {
    '本溪市市辖区': '本溪市市辖区',
    '平山区': '平山区',
    '溪湖区': '溪湖区',
    '明山区': '明山区',
    '南芬区': '南芬区',
    '本溪满族自治县': '本溪满族自治县',
    '桓仁满族自治县': '桓仁满族自治县'
  }, 210600: {
    '丹东市市辖区': '丹东市市辖区',
    '元宝区': '元宝区',
    '振兴区': '振兴区',
    '振安区': '振安区',
    '宽甸满族自治县': '宽甸满族自治县',
    '东港市': '东港市',
    '凤城市': '凤城市'
  }, 210700: {
    '锦州市市辖区': '锦州市市辖区',
    '古塔区': '古塔区',
    '凌河区': '凌河区',
    '太和区': '太和区',
    '黑山县': '黑山县',
    '义县': '义县',
    '凌海市': '凌海市',
    '北镇市': '北镇市'
  }, 210800: {
    '营口市市辖区': '营口市市辖区',
    '站前区': '站前区',
    '西市区': '西市区',
    '鲅鱼圈区': '鲅鱼圈区',
    '老边区': '老边区',
    '盖州市': '盖州市',
    '大石桥市': '大石桥市'
  }, 210900: {
    '阜新市市辖区': '阜新市市辖区',
    '海州区': '海州区',
    '新邱区': '新邱区',
    '太平区': '太平区',
    '清河门区': '清河门区',
    '细河区': '细河区',
    '阜新蒙古族自治县': '阜新蒙古族自治县',
    '彰武县': '彰武县'
  }, 211000: {
    '辽阳市市辖区': '辽阳市市辖区',
    '白塔区': '白塔区',
    '文圣区': '文圣区',
    '宏伟区': '宏伟区',
    '弓长岭区': '弓长岭区',
    '太子河区': '太子河区',
    '辽阳县': '辽阳县',
    '灯塔市': '灯塔市'
  }, 211100: {
    '盘锦市市辖区': '盘锦市市辖区',
    '双台子区': '双台子区',
    '兴隆台区': '兴隆台区',
    '大洼区': '大洼区',
    '盘山县': '盘山县'
  }, 211200: {
    '铁岭市市辖区': '铁岭市市辖区',
    '银州区': '银州区',
    '清河区': '清河区',
    '铁岭县': '铁岭县',
    '西丰县': '西丰县',
    '昌图县': '昌图县',
    '调兵山市': '调兵山市',
    '开原市': '开原市'
  }, 211300: {
    '朝阳市市辖区': '朝阳市市辖区',
    '双塔区': '双塔区',
    '龙城区': '龙城区',
    '朝阳县': '朝阳县',
    '建平县': '建平县',
    '喀喇沁左翼蒙古族自治县': '喀喇沁左翼蒙古族自治县',
    '北票市': '北票市',
    '凌源市': '凌源市'
  }, 211400: {
    '葫芦岛市市辖区': '葫芦岛市市辖区',
    '连山区': '连山区',
    '龙港区': '龙港区',
    '南票区': '南票区',
    '绥中县': '绥中县',
    '建昌县': '建昌县',
    '兴城市': '兴城市'
  }, 220100: {
    '长春市市辖区': '长春市市辖区',
    '南关区': '南关区',
    '宽城区': '宽城区',
    '朝阳区': '朝阳区',
    '二道区': '二道区',
    '绿园区': '绿园区',
    '双阳区': '双阳区',
    '九台区': '九台区',
    '农安县': '农安县',
    '榆树市': '榆树市',
    '德惠市': '德惠市'
  }, 220200: {
    '吉林市市辖区': '吉林市市辖区',
    '昌邑区': '昌邑区',
    '龙潭区': '龙潭区',
    '船营区': '船营区',
    '丰满区': '丰满区',
    '永吉县': '永吉县',
    '蛟河市': '蛟河市',
    '桦甸市': '桦甸市',
    '舒兰市': '舒兰市',
    '磐石市': '磐石市'
  }, 220300: {
    '四平市市辖区': '四平市市辖区',
    '铁西区': '铁西区',
    '铁东区': '铁东区',
    '梨树县': '梨树县',
    '伊通满族自治县': '伊通满族自治县',
    '公主岭市': '公主岭市',
    '双辽市': '双辽市'
  }, 220400: {
    '辽源市市辖区': '辽源市市辖区',
    '龙山区': '龙山区',
    '西安区': '西安区',
    '东丰县': '东丰县',
    '东辽县': '东辽县'
  }, 220500: {
    '通化市市辖区': '通化市市辖区',
    '东昌区': '东昌区',
    '二道江区': '二道江区',
    '通化县': '通化县',
    '辉南县': '辉南县',
    '柳河县': '柳河县',
    '梅河口市': '梅河口市',
    '集安市': '集安市'
  }, 220600: {
    '白山市市辖区': '白山市市辖区',
    '浑江区': '浑江区',
    '江源区': '江源区',
    '抚松县': '抚松县',
    '靖宇县': '靖宇县',
    '长白朝鲜族自治县': '长白朝鲜族自治县',
    '临江市': '临江市'
  }, 220700: {
    '松原市市辖区': '松原市市辖区',
    '宁江区': '宁江区',
    '前郭尔罗斯蒙古族自治县': '前郭尔罗斯蒙古族自治县',
    '长岭县': '长岭县',
    '乾安县': '乾安县',
    '扶余市': '扶余市'
  }, 220800: {
    '白城市市辖区': '白城市市辖区',
    '洮北区': '洮北区',
    '镇赉县': '镇赉县',
    '通榆县': '通榆县',
    '洮南市': '洮南市',
    '大安市': '大安市'
  }, 222400: {
    '延吉市': '延吉市',
    '图们市': '图们市',
    '敦化市': '敦化市',
    '珲春市': '珲春市',
    '龙井市': '龙井市',
    '和龙市': '和龙市',
    '汪清县': '汪清县',
    '安图县': '安图县'
  }, 230100: {
    '哈尔滨市市辖区': '哈尔滨市市辖区',
    '道里区': '道里区',
    '南岗区': '南岗区',
    '道外区': '道外区',
    '平房区': '平房区',
    '松北区': '松北区',
    '香坊区': '香坊区',
    '呼兰区': '呼兰区',
    '阿城区': '阿城区',
    '双城区': '双城区',
    '依兰县': '依兰县',
    '方正县': '方正县',
    '宾县': '宾县',
    '巴彦县': '巴彦县',
    '木兰县': '木兰县',
    '通河县': '通河县',
    '延寿县': '延寿县',
    '尚志市': '尚志市',
    '五常市': '五常市'
  }, 230200: {
    '齐齐哈尔市市辖区': '齐齐哈尔市市辖区',
    '龙沙区': '龙沙区',
    '建华区': '建华区',
    '铁锋区': '铁锋区',
    '昂昂溪区': '昂昂溪区',
    '富拉尔基区': '富拉尔基区',
    '碾子山区': '碾子山区',
    '梅里斯达斡尔族区': '梅里斯达斡尔族区',
    '龙江县': '龙江县',
    '依安县': '依安县',
    '泰来县': '泰来县',
    '甘南县': '甘南县',
    '富裕县': '富裕县',
    '克山县': '克山县',
    '克东县': '克东县',
    '拜泉县': '拜泉县',
    '讷河市': '讷河市'
  }, 230300: {
    '鸡西市市辖区': '鸡西市市辖区',
    '鸡冠区': '鸡冠区',
    '恒山区': '恒山区',
    '滴道区': '滴道区',
    '梨树区': '梨树区',
    '城子河区': '城子河区',
    '麻山区': '麻山区',
    '鸡东县': '鸡东县',
    '虎林市': '虎林市',
    '密山市': '密山市'
  }, 230400: {
    '鹤岗市市辖区': '鹤岗市市辖区',
    '向阳区': '向阳区',
    '工农区': '工农区',
    '南山区': '南山区',
    '兴安区': '兴安区',
    '东山区': '东山区',
    '兴山区': '兴山区',
    '萝北县': '萝北县',
    '绥滨县': '绥滨县'
  }, 230500: {
    '双鸭山市市辖区': '双鸭山市市辖区',
    '尖山区': '尖山区',
    '岭东区': '岭东区',
    '四方台区': '四方台区',
    '宝山区': '宝山区',
    '集贤县': '集贤县',
    '友谊县': '友谊县',
    '宝清县': '宝清县',
    '饶河县': '饶河县'
  }, 230600: {
    '大庆市市辖区': '大庆市市辖区',
    '萨尔图区': '萨尔图区',
    '龙凤区': '龙凤区',
    '让胡路区': '让胡路区',
    '红岗区': '红岗区',
    '大同区': '大同区',
    '肇州县': '肇州县',
    '肇源县': '肇源县',
    '林甸县': '林甸县',
    '杜尔伯特蒙古族自治县': '杜尔伯特蒙古族自治县'
  }, 230700: {
    '伊春市市辖区': '伊春市市辖区',
    '伊春区': '伊春区',
    '南岔区': '南岔区',
    '友好区': '友好区',
    '西林区': '西林区',
    '翠峦区': '翠峦区',
    '新青区': '新青区',
    '美溪区': '美溪区',
    '金山屯区': '金山屯区',
    '五营区': '五营区',
    '乌马河区': '乌马河区',
    '汤旺河区': '汤旺河区',
    '带岭区': '带岭区',
    '乌伊岭区': '乌伊岭区',
    '红星区': '红星区',
    '上甘岭区': '上甘岭区',
    '嘉荫县': '嘉荫县',
    '铁力市': '铁力市'
  }, 230800: {
    '佳木斯市市辖区': '佳木斯市市辖区',
    '向阳区': '向阳区',
    '前进区': '前进区',
    '东风区': '东风区',
    '郊区': '郊区',
    '桦南县': '桦南县',
    '桦川县': '桦川县',
    '汤原县': '汤原县',
    '同江市': '同江市',
    '富锦市': '富锦市',
    '抚远市': '抚远市'
  }, 230900: {
    '七台河市市辖区': '七台河市市辖区',
    '新兴区': '新兴区',
    '桃山区': '桃山区',
    '茄子河区': '茄子河区',
    '勃利县': '勃利县'
  }, 231000: {
    '牡丹江市市辖区': '牡丹江市市辖区',
    '东安区': '东安区',
    '阳明区': '阳明区',
    '爱民区': '爱民区',
    '西安区': '西安区',
    '林口县': '林口县',
    '绥芬河市': '绥芬河市',
    '海林市': '海林市',
    '宁安市': '宁安市',
    '穆棱市': '穆棱市',
    '东宁市': '东宁市'
  }, 231100: {
    '黑河市市辖区': '黑河市市辖区',
    '爱辉区': '爱辉区',
    '嫩江县': '嫩江县',
    '逊克县': '逊克县',
    '孙吴县': '孙吴县',
    '北安市': '北安市',
    '五大连池市': '五大连池市'
  }, 231200: {
    '绥化市市辖区': '绥化市市辖区',
    '北林区': '北林区',
    '望奎县': '望奎县',
    '兰西县': '兰西县',
    '青冈县': '青冈县',
    '庆安县': '庆安县',
    '明水县': '明水县',
    '绥棱县': '绥棱县',
    '安达市': '安达市',
    '肇东市': '肇东市',
    '海伦市': '海伦市'
  }, 232700: {
    '加格达奇区': '加格达奇区',
    '呼玛县': '呼玛县',
    '塔河县': '塔河县',
    '漠河市': '漠河市'
  }, 310100: {
    '黄浦区': '黄浦区',
    '徐汇区': '徐汇区',
    '长宁区': '长宁区',
    '静安区': '静安区',
    '普陀区': '普陀区',
    '虹口区': '虹口区',
    '杨浦区': '杨浦区',
    '闵行区': '闵行区',
    '宝山区': '宝山区',
    '嘉定区': '嘉定区',
    '浦东新区': '浦东新区',
    '金山区': '金山区',
    '松江区': '松江区',
    '青浦区': '青浦区',
    '奉贤区': '奉贤区',
    '崇明区': '崇明区'
  }, 320100: {
    '南京市市辖区': '南京市市辖区',
    '玄武区': '玄武区',
    '秦淮区': '秦淮区',
    '建邺区': '建邺区',
    '鼓楼区': '鼓楼区',
    '浦口区': '浦口区',
    '栖霞区': '栖霞区',
    '雨花台区': '雨花台区',
    '江宁区': '江宁区',
    '六合区': '六合区',
    '溧水区': '溧水区',
    '高淳区': '高淳区'
  }, 320200: {
    '无锡市市辖区': '无锡市市辖区',
    '锡山区': '锡山区',
    '惠山区': '惠山区',
    '滨湖区': '滨湖区',
    '梁溪区': '梁溪区',
    '新吴区': '新吴区',
    '江阴市': '江阴市',
    '宜兴市': '宜兴市'
  }, 320300: {
    '徐州市市辖区': '徐州市市辖区',
    '鼓楼区': '鼓楼区',
    '云龙区': '云龙区',
    '贾汪区': '贾汪区',
    '泉山区': '泉山区',
    '铜山区': '铜山区',
    '丰县': '丰县',
    '沛县': '沛县',
    '睢宁县': '睢宁县',
    '新沂市': '新沂市',
    '邳州市': '邳州市'
  }, 320400: {
    '常州市市辖区': '常州市市辖区',
    '天宁区': '天宁区',
    '钟楼区': '钟楼区',
    '新北区': '新北区',
    '武进区': '武进区',
    '金坛区': '金坛区',
    '溧阳市': '溧阳市'
  }, 320500: {
    '苏州市市辖区': '苏州市市辖区',
    '虎丘区': '虎丘区',
    '吴中区': '吴中区',
    '相城区': '相城区',
    '姑苏区': '姑苏区',
    '吴江区': '吴江区',
    '苏州工业园区': '苏州工业园区',
    '常熟市': '常熟市',
    '张家港市': '张家港市',
    '昆山市': '昆山市',
    '太仓市': '太仓市'
  }, 320600: {
    '南通市市辖区': '南通市市辖区',
    '崇川区': '崇川区',
    '港闸区': '港闸区',
    '通州区': '通州区',
    '海安市': '海安市',
    '如东县': '如东县',
    '启东市': '启东市',
    '如皋市': '如皋市',
    '海门市': '海门市'
  }, 320700: {
    '连云港市市辖区': '连云港市市辖区',
    '连云区': '连云区',
    '海州区': '海州区',
    '赣榆区': '赣榆区',
    '东海县': '东海县',
    '灌云县': '灌云县',
    '灌南县': '灌南县'
  }, 320800: {
    '淮安市市辖区': '淮安市市辖区',
    '淮安区': '淮安区',
    '淮阴区': '淮阴区',
    '清江浦区': '清江浦区',
    '洪泽区': '洪泽区',
    '涟水县': '涟水县',
    '盱眙县': '盱眙县',
    '金湖县': '金湖县'
  }, 320900: {
    '盐城市市辖区': '盐城市市辖区',
    '亭湖区': '亭湖区',
    '盐都区': '盐都区',
    '大丰区': '大丰区',
    '响水县': '响水县',
    '滨海县': '滨海县',
    '阜宁县': '阜宁县',
    '射阳县': '射阳县',
    '建湖县': '建湖县',
    '东台市': '东台市'
  }, 321000: {
    '扬州市市辖区': '扬州市市辖区',
    '广陵区': '广陵区',
    '邗江区': '邗江区',
    '江都区': '江都区',
    '宝应县': '宝应县',
    '仪征市': '仪征市',
    '高邮市': '高邮市'
  }, 321100: {
    '镇江市市辖区': '镇江市市辖区',
    '京口区': '京口区',
    '润州区': '润州区',
    '丹徒区': '丹徒区',
    '丹阳市': '丹阳市',
    '扬中市': '扬中市',
    '句容市': '句容市'
  }, 321200: {
    '泰州市市辖区': '泰州市市辖区',
    '海陵区': '海陵区',
    '高港区': '高港区',
    '姜堰区': '姜堰区',
    '兴化市': '兴化市',
    '靖江市': '靖江市',
    '泰兴市': '泰兴市'
  }, 321300: {
    '宿迁市市辖区': '宿迁市市辖区',
    '宿城区': '宿城区',
    '宿豫区': '宿豫区',
    '沭阳县': '沭阳县',
    '泗阳县': '泗阳县',
    '泗洪县': '泗洪县'
  }, 330100: {
    '杭州市市辖区': '杭州市市辖区',
    '上城区': '上城区',
    '下城区': '下城区',
    '江干区': '江干区',
    '拱墅区': '拱墅区',
    '西湖区': '西湖区',
    '滨江区': '滨江区',
    '萧山区': '萧山区',
    '余杭区': '余杭区',
    '富阳区': '富阳区',
    '临安区': '临安区',
    '桐庐县': '桐庐县',
    '淳安县': '淳安县',
    '建德市': '建德市'
  }, 330200: {
    '宁波市市辖区': '宁波市市辖区',
    '海曙区': '海曙区',
    '江北区': '江北区',
    '北仑区': '北仑区',
    '镇海区': '镇海区',
    '鄞州区': '鄞州区',
    '奉化区': '奉化区',
    '象山县': '象山县',
    '宁海县': '宁海县',
    '余姚市': '余姚市',
    '慈溪市': '慈溪市'
  }, 330300: {
    '温州市市辖区': '温州市市辖区',
    '鹿城区': '鹿城区',
    '龙湾区': '龙湾区',
    '瓯海区': '瓯海区',
    '洞头区': '洞头区',
    '永嘉县': '永嘉县',
    '平阳县': '平阳县',
    '苍南县': '苍南县',
    '文成县': '文成县',
    '泰顺县': '泰顺县',
    '瑞安市': '瑞安市',
    '乐清市': '乐清市'
  }, 330400: {
    '嘉兴市市辖区': '嘉兴市市辖区',
    '南湖区': '南湖区',
    '秀洲区': '秀洲区',
    '嘉善县': '嘉善县',
    '海盐县': '海盐县',
    '海宁市': '海宁市',
    '平湖市': '平湖市',
    '桐乡市': '桐乡市'
  }, 330500: {
    '湖州市市辖区': '湖州市市辖区',
    '吴兴区': '吴兴区',
    '南浔区': '南浔区',
    '德清县': '德清县',
    '长兴县': '长兴县',
    '安吉县': '安吉县'
  }, 330600: {
    '绍兴市市辖区': '绍兴市市辖区',
    '越城区': '越城区',
    '柯桥区': '柯桥区',
    '上虞区': '上虞区',
    '新昌县': '新昌县',
    '诸暨市': '诸暨市',
    '嵊州市': '嵊州市'
  }, 330700: {
    '金华市市辖区': '金华市市辖区',
    '婺城区': '婺城区',
    '金东区': '金东区',
    '武义县': '武义县',
    '浦江县': '浦江县',
    '磐安县': '磐安县',
    '兰溪市': '兰溪市',
    '义乌市': '义乌市',
    '东阳市': '东阳市',
    '永康市': '永康市'
  }, 330800: {
    '衢州市市辖区': '衢州市市辖区',
    '柯城区': '柯城区',
    '衢江区': '衢江区',
    '常山县': '常山县',
    '开化县': '开化县',
    '龙游县': '龙游县',
    '江山市': '江山市'
  }, 330900: {
    '舟山市市辖区': '舟山市市辖区',
    '定海区': '定海区',
    '普陀区': '普陀区',
    '岱山县': '岱山县',
    '嵊泗县': '嵊泗县'
  }, 331000: {
    '台州市市辖区': '台州市市辖区',
    '椒江区': '椒江区',
    '黄岩区': '黄岩区',
    '路桥区': '路桥区',
    '三门县': '三门县',
    '天台县': '天台县',
    '仙居县': '仙居县',
    '温岭市': '温岭市',
    '临海市': '临海市',
    '玉环市': '玉环市'
  }, 331100: {
    '丽水市市辖区': '丽水市市辖区',
    '莲都区': '莲都区',
    '青田县': '青田县',
    '缙云县': '缙云县',
    '遂昌县': '遂昌县',
    '松阳县': '松阳县',
    '云和县': '云和县',
    '庆元县': '庆元县',
    '景宁畲族自治县': '景宁畲族自治县',
    '龙泉市': '龙泉市'
  }, 340100: {
    '合肥市市辖区': '合肥市市辖区',
    '瑶海区': '瑶海区',
    '庐阳区': '庐阳区',
    '蜀山区': '蜀山区',
    '包河区': '包河区',
    '长丰县': '长丰县',
    '肥东县': '肥东县',
    '肥西县': '肥西县',
    '庐江县': '庐江县',
    '巢湖市': '巢湖市'
  }, 340200: {
    '芜湖市市辖区': '芜湖市市辖区',
    '镜湖区': '镜湖区',
    '弋江区': '弋江区',
    '鸠江区': '鸠江区',
    '三山区': '三山区',
    '芜湖县': '芜湖县',
    '繁昌县': '繁昌县',
    '南陵县': '南陵县',
    '无为县': '无为县'
  }, 340300: {
    '蚌埠市市辖区': '蚌埠市市辖区',
    '龙子湖区': '龙子湖区',
    '蚌山区': '蚌山区',
    '禹会区': '禹会区',
    '淮上区': '淮上区',
    '怀远县': '怀远县',
    '五河县': '五河县',
    '固镇县': '固镇县'
  }, 340400: {
    '淮南市市辖区': '淮南市市辖区',
    '大通区': '大通区',
    '田家庵区': '田家庵区',
    '谢家集区': '谢家集区',
    '八公山区': '八公山区',
    '潘集区': '潘集区',
    '凤台县': '凤台县',
    '寿县': '寿县'
  }, 340500: {
    '马鞍山市市辖区': '马鞍山市市辖区',
    '花山区': '花山区',
    '雨山区': '雨山区',
    '博望区': '博望区',
    '当涂县': '当涂县',
    '含山县': '含山县',
    '和县': '和县'
  }, 340600: {
    '淮北市市辖区': '淮北市市辖区',
    '杜集区': '杜集区',
    '相山区': '相山区',
    '烈山区': '烈山区',
    '濉溪县': '濉溪县'
  }, 340700: {
    '铜陵市市辖区': '铜陵市市辖区',
    '铜官区': '铜官区',
    '义安区': '义安区',
    '郊区': '郊区',
    '枞阳县': '枞阳县'
  }, 340800: {
    '安庆市市辖区': '安庆市市辖区',
    '迎江区': '迎江区',
    '大观区': '大观区',
    '宜秀区': '宜秀区',
    '怀宁县': '怀宁县',
    '潜山市': '潜山市',
    '太湖县': '太湖县',
    '宿松县': '宿松县',
    '望江县': '望江县',
    '岳西县': '岳西县',
    '桐城市': '桐城市'
  }, 341000: {
    '黄山市市辖区': '黄山市市辖区',
    '屯溪区': '屯溪区',
    '黄山区': '黄山区',
    '徽州区': '徽州区',
    '歙县': '歙县',
    '休宁县': '休宁县',
    '黟县': '黟县',
    '祁门县': '祁门县'
  }, 341100: {
    '滁州市市辖区': '滁州市市辖区',
    '琅琊区': '琅琊区',
    '南谯区': '南谯区',
    '来安县': '来安县',
    '全椒县': '全椒县',
    '定远县': '定远县',
    '凤阳县': '凤阳县',
    '天长市': '天长市',
    '明光市': '明光市'
  }, 341200: {
    '阜阳市市辖区': '阜阳市市辖区',
    '颍州区': '颍州区',
    '颍东区': '颍东区',
    '颍泉区': '颍泉区',
    '临泉县': '临泉县',
    '太和县': '太和县',
    '阜南县': '阜南县',
    '颍上县': '颍上县',
    '界首市': '界首市'
  }, 341300: {
    '宿州市市辖区': '宿州市市辖区',
    '埇桥区': '埇桥区',
    '砀山县': '砀山县',
    '萧县': '萧县',
    '灵璧县': '灵璧县',
    '泗县': '泗县'
  }, 341500: {
    '六安市市辖区': '六安市市辖区',
    '金安区': '金安区',
    '裕安区': '裕安区',
    '叶集区': '叶集区',
    '霍邱县': '霍邱县',
    '舒城县': '舒城县',
    '金寨县': '金寨县',
    '霍山县': '霍山县'
  }, 341600: {
    '亳州市市辖区': '亳州市市辖区',
    '谯城区': '谯城区',
    '涡阳县': '涡阳县',
    '蒙城县': '蒙城县',
    '利辛县': '利辛县'
  }, 341700: {
    '池州市市辖区': '池州市市辖区',
    '贵池区': '贵池区',
    '东至县': '东至县',
    '石台县': '石台县',
    '青阳县': '青阳县'
  }, 341800: {
    '宣城市市辖区': '宣城市市辖区',
    '宣州区': '宣州区',
    '郎溪县': '郎溪县',
    '广德县': '广德县',
    '泾县': '泾县',
    '绩溪县': '绩溪县',
    '旌德县': '旌德县',
    '宁国市': '宁国市'
  }, 350100: {
    '福州市市辖区': '福州市市辖区',
    '鼓楼区': '鼓楼区',
    '台江区': '台江区',
    '仓山区': '仓山区',
    '马尾区': '马尾区',
    '晋安区': '晋安区',
    '长乐区': '长乐区',
    '闽侯县': '闽侯县',
    '连江县': '连江县',
    '罗源县': '罗源县',
    '闽清县': '闽清县',
    '永泰县': '永泰县',
    '平潭县': '平潭县',
    '福清市': '福清市'
  }, 350200: {
    '厦门市市辖区': '厦门市市辖区',
    '思明区': '思明区',
    '海沧区': '海沧区',
    '湖里区': '湖里区',
    '集美区': '集美区',
    '同安区': '同安区',
    '翔安区': '翔安区'
  }, 350300: {
    '莆田市市辖区': '莆田市市辖区',
    '城厢区': '城厢区',
    '涵江区': '涵江区',
    '荔城区': '荔城区',
    '秀屿区': '秀屿区',
    '仙游县': '仙游县'
  }, 350400: {
    '三明市市辖区': '三明市市辖区',
    '梅列区': '梅列区',
    '三元区': '三元区',
    '明溪县': '明溪县',
    '清流县': '清流县',
    '宁化县': '宁化县',
    '大田县': '大田县',
    '尤溪县': '尤溪县',
    '沙县': '沙县',
    '将乐县': '将乐县',
    '泰宁县': '泰宁县',
    '建宁县': '建宁县',
    '永安市': '永安市'
  }, 350500: {
    '泉州市市辖区': '泉州市市辖区',
    '鲤城区': '鲤城区',
    '丰泽区': '丰泽区',
    '洛江区': '洛江区',
    '泉港区': '泉港区',
    '惠安县': '惠安县',
    '安溪县': '安溪县',
    '永春县': '永春县',
    '德化县': '德化县',
    '金门县': '金门县',
    '石狮市': '石狮市',
    '晋江市': '晋江市',
    '南安市': '南安市'
  }, 350600: {
    '漳州市市辖区': '漳州市市辖区',
    '芗城区': '芗城区',
    '龙文区': '龙文区',
    '云霄县': '云霄县',
    '漳浦县': '漳浦县',
    '诏安县': '诏安县',
    '长泰县': '长泰县',
    '东山县': '东山县',
    '南靖县': '南靖县',
    '平和县': '平和县',
    '华安县': '华安县',
    '龙海市': '龙海市'
  }, 350700: {
    '南平市市辖区': '南平市市辖区',
    '延平区': '延平区',
    '建阳区': '建阳区',
    '顺昌县': '顺昌县',
    '浦城县': '浦城县',
    '光泽县': '光泽县',
    '松溪县': '松溪县',
    '政和县': '政和县',
    '邵武市': '邵武市',
    '武夷山市': '武夷山市',
    '建瓯市': '建瓯市'
  }, 350800: {
    '龙岩市市辖区': '龙岩市市辖区',
    '新罗区': '新罗区',
    '永定区': '永定区',
    '长汀县': '长汀县',
    '上杭县': '上杭县',
    '武平县': '武平县',
    '连城县': '连城县',
    '漳平市': '漳平市'
  }, 350900: {
    '宁德市市辖区': '宁德市市辖区',
    '蕉城区': '蕉城区',
    '霞浦县': '霞浦县',
    '古田县': '古田县',
    '屏南县': '屏南县',
    '寿宁县': '寿宁县',
    '周宁县': '周宁县',
    '柘荣县': '柘荣县',
    '福安市': '福安市',
    '福鼎市': '福鼎市'
  }, 360100: {
    '南昌市市辖区': '南昌市市辖区',
    '东湖区': '东湖区',
    '西湖区': '西湖区',
    '青云谱区': '青云谱区',
    '湾里区': '湾里区',
    '青山湖区': '青山湖区',
    '新建区': '新建区',
    '南昌县': '南昌县',
    '安义县': '安义县',
    '进贤县': '进贤县'
  }, 360200: {
    '景德镇市市辖区': '景德镇市市辖区',
    '昌江区': '昌江区',
    '珠山区': '珠山区',
    '浮梁县': '浮梁县',
    '乐平市': '乐平市'
  }, 360300: {
    '萍乡市市辖区': '萍乡市市辖区',
    '安源区': '安源区',
    '湘东区': '湘东区',
    '莲花县': '莲花县',
    '上栗县': '上栗县',
    '芦溪县': '芦溪县'
  }, 360400: {
    '九江市市辖区': '九江市市辖区',
    '濂溪区': '濂溪区',
    '浔阳区': '浔阳区',
    '柴桑区': '柴桑区',
    '武宁县': '武宁县',
    '修水县': '修水县',
    '永修县': '永修县',
    '德安县': '德安县',
    '都昌县': '都昌县',
    '湖口县': '湖口县',
    '彭泽县': '彭泽县',
    '瑞昌市': '瑞昌市',
    '共青城市': '共青城市',
    '庐山市': '庐山市'
  }, 360500: {
    '新余市市辖区': '新余市市辖区',
    '渝水区': '渝水区',
    '分宜县': '分宜县'
  }, 360600: {
    '鹰潭市市辖区': '鹰潭市市辖区',
    '月湖区': '月湖区',
    '余江区': '余江区',
    '贵溪市': '贵溪市'
  }, 360700: {
    '赣州市市辖区': '赣州市市辖区',
    '章贡区': '章贡区',
    '南康区': '南康区',
    '赣县区': '赣县区',
    '信丰县': '信丰县',
    '大余县': '大余县',
    '上犹县': '上犹县',
    '崇义县': '崇义县',
    '安远县': '安远县',
    '龙南县': '龙南县',
    '定南县': '定南县',
    '全南县': '全南县',
    '宁都县': '宁都县',
    '于都县': '于都县',
    '兴国县': '兴国县',
    '会昌县': '会昌县',
    '寻乌县': '寻乌县',
    '石城县': '石城县',
    '瑞金市': '瑞金市'
  }, 360800: {
    '吉安市市辖区': '吉安市市辖区',
    '吉州区': '吉州区',
    '青原区': '青原区',
    '吉安县': '吉安县',
    '吉水县': '吉水县',
    '峡江县': '峡江县',
    '新干县': '新干县',
    '永丰县': '永丰县',
    '泰和县': '泰和县',
    '遂川县': '遂川县',
    '万安县': '万安县',
    '安福县': '安福县',
    '永新县': '永新县',
    '井冈山市': '井冈山市'
  }, 360900: {
    '宜春市市辖区': '宜春市市辖区',
    '袁州区': '袁州区',
    '奉新县': '奉新县',
    '万载县': '万载县',
    '上高县': '上高县',
    '宜丰县': '宜丰县',
    '靖安县': '靖安县',
    '铜鼓县': '铜鼓县',
    '丰城市': '丰城市',
    '樟树市': '樟树市',
    '高安市': '高安市'
  }, 361000: {
    '抚州市市辖区': '抚州市市辖区',
    '临川区': '临川区',
    '东乡区': '东乡区',
    '南城县': '南城县',
    '黎川县': '黎川县',
    '南丰县': '南丰县',
    '崇仁县': '崇仁县',
    '乐安县': '乐安县',
    '宜黄县': '宜黄县',
    '金溪县': '金溪县',
    '资溪县': '资溪县',
    '广昌县': '广昌县'
  }, 361100: {
    '上饶市市辖区': '上饶市市辖区',
    '信州区': '信州区',
    '广丰区': '广丰区',
    '上饶县': '上饶县',
    '玉山县': '玉山县',
    '铅山县': '铅山县',
    '横峰县': '横峰县',
    '弋阳县': '弋阳县',
    '余干县': '余干县',
    '鄱阳县': '鄱阳县',
    '万年县': '万年县',
    '婺源县': '婺源县',
    '德兴市': '德兴市'
  }, 370100: {
    '济南市市辖区': '济南市市辖区',
    '历下区': '历下区',
    '市中区': '市中区',
    '槐荫区': '槐荫区',
    '天桥区': '天桥区',
    '历城区': '历城区',
    '长清区': '长清区',
    '章丘区': '章丘区',
    '平阴县': '平阴县',
    '济阳区': '济阳区',
    '商河县': '商河县',
    '莱芜区': '莱芜区',
    '钢城区': '钢城区'
  }, 370200: {
    '青岛市市辖区': '青岛市市辖区',
    '市南区': '市南区',
    '市北区': '市北区',
    '黄岛区': '黄岛区',
    '崂山区': '崂山区',
    '李沧区': '李沧区',
    '城阳区': '城阳区',
    '即墨区': '即墨区',
    '胶州市': '胶州市',
    '平度市': '平度市',
    '莱西市': '莱西市'
  }, 370300: {
    '淄博市市辖区': '淄博市市辖区',
    '淄川区': '淄川区',
    '张店区': '张店区',
    '博山区': '博山区',
    '临淄区': '临淄区',
    '周村区': '周村区',
    '桓台县': '桓台县',
    '高青县': '高青县',
    '沂源县': '沂源县'
  }, 370400: {
    '枣庄市市辖区': '枣庄市市辖区',
    '市中区': '市中区',
    '薛城区': '薛城区',
    '峄城区': '峄城区',
    '台儿庄区': '台儿庄区',
    '山亭区': '山亭区',
    '滕州市': '滕州市'
  }, 370500: {
    '东营市市辖区': '东营市市辖区',
    '东营区': '东营区',
    '河口区': '河口区',
    '垦利区': '垦利区',
    '利津县': '利津县',
    '广饶县': '广饶县'
  }, 370600: {
    '烟台市市辖区': '烟台市市辖区',
    '芝罘区': '芝罘区',
    '福山区': '福山区',
    '牟平区': '牟平区',
    '莱山区': '莱山区',
    '长岛县': '长岛县',
    '龙口市': '龙口市',
    '莱阳市': '莱阳市',
    '莱州市': '莱州市',
    '蓬莱市': '蓬莱市',
    '招远市': '招远市',
    '栖霞市': '栖霞市',
    '海阳市': '海阳市'
  }, 370700: {
    '潍坊市市辖区': '潍坊市市辖区',
    '潍城区': '潍城区',
    '寒亭区': '寒亭区',
    '坊子区': '坊子区',
    '奎文区': '奎文区',
    '临朐县': '临朐县',
    '昌乐县': '昌乐县',
    '青州市': '青州市',
    '诸城市': '诸城市',
    '寿光市': '寿光市',
    '安丘市': '安丘市',
    '高密市': '高密市',
    '昌邑市': '昌邑市'
  }, 370800: {
    '济宁市市辖区': '济宁市市辖区',
    '任城区': '任城区',
    '兖州区': '兖州区',
    '微山县': '微山县',
    '鱼台县': '鱼台县',
    '金乡县': '金乡县',
    '嘉祥县': '嘉祥县',
    '汶上县': '汶上县',
    '泗水县': '泗水县',
    '梁山县': '梁山县',
    '曲阜市': '曲阜市',
    '邹城市': '邹城市'
  }, 370900: {
    '泰安市市辖区': '泰安市市辖区',
    '泰山区': '泰山区',
    '岱岳区': '岱岳区',
    '宁阳县': '宁阳县',
    '东平县': '东平县',
    '新泰市': '新泰市',
    '肥城市': '肥城市'
  }, 371000: {
    '威海市市辖区': '威海市市辖区',
    '环翠区': '环翠区',
    '文登区': '文登区',
    '荣成市': '荣成市',
    '乳山市': '乳山市'
  }, 371100: {
    '日照市市辖区': '日照市市辖区',
    '东港区': '东港区',
    '岚山区': '岚山区',
    '五莲县': '五莲县',
    '莒县': '莒县'
  }, 371300: {
    '临沂市市辖区': '临沂市市辖区',
    '兰山区': '兰山区',
    '罗庄区': '罗庄区',
    '河东区': '河东区',
    '沂南县': '沂南县',
    '郯城县': '郯城县',
    '沂水县': '沂水县',
    '兰陵县': '兰陵县',
    '费县': '费县',
    '平邑县': '平邑县',
    '莒南县': '莒南县',
    '蒙阴县': '蒙阴县',
    '临沭县': '临沭县'
  }, 371400: {
    '德州市市辖区': '德州市市辖区',
    '德城区': '德城区',
    '陵城区': '陵城区',
    '宁津县': '宁津县',
    '庆云县': '庆云县',
    '临邑县': '临邑县',
    '齐河县': '齐河县',
    '平原县': '平原县',
    '夏津县': '夏津县',
    '武城县': '武城县',
    '乐陵市': '乐陵市',
    '禹城市': '禹城市'
  }, 371500: {
    '聊城市市辖区': '聊城市市辖区',
    '东昌府区': '东昌府区',
    '阳谷县': '阳谷县',
    '莘县': '莘县',
    '茌平县': '茌平县',
    '东阿县': '东阿县',
    '冠县': '冠县',
    '高唐县': '高唐县',
    '临清市': '临清市'
  }, 371600: {
    '滨州市市辖区': '滨州市市辖区',
    '滨城区': '滨城区',
    '沾化区': '沾化区',
    '惠民县': '惠民县',
    '阳信县': '阳信县',
    '无棣县': '无棣县',
    '博兴县': '博兴县',
    '邹平市': '邹平市'
  }, 371700: {
    '菏泽市市辖区': '菏泽市市辖区',
    '牡丹区': '牡丹区',
    '定陶区': '定陶区',
    '曹县': '曹县',
    '单县': '单县',
    '成武县': '成武县',
    '巨野县': '巨野县',
    '郓城县': '郓城县',
    '鄄城县': '鄄城县',
    '东明县': '东明县'
  }, 410100: {
    '郑州市市辖区': '郑州市市辖区',
    '中原区': '中原区',
    '二七区': '二七区',
    '管城回族区': '管城回族区',
    '金水区': '金水区',
    '上街区': '上街区',
    '惠济区': '惠济区',
    '中牟县': '中牟县',
    '巩义市': '巩义市',
    '荥阳市': '荥阳市',
    '新密市': '新密市',
    '新郑市': '新郑市',
    '登封市': '登封市'
  }, 410200: {
    '开封市市辖区': '开封市市辖区',
    '龙亭区': '龙亭区',
    '顺河回族区': '顺河回族区',
    '鼓楼区': '鼓楼区',
    '禹王台区': '禹王台区',
    '祥符区': '祥符区',
    '杞县': '杞县',
    '通许县': '通许县',
    '尉氏县': '尉氏县',
    '兰考县': '兰考县'
  }, 410300: {
    '洛阳市市辖区': '洛阳市市辖区',
    '老城区': '老城区',
    '西工区': '西工区',
    '瀍河回族区': '瀍河回族区',
    '涧西区': '涧西区',
    '吉利区': '吉利区',
    '洛龙区': '洛龙区',
    '孟津县': '孟津县',
    '新安县': '新安县',
    '栾川县': '栾川县',
    '嵩县': '嵩县',
    '汝阳县': '汝阳县',
    '宜阳县': '宜阳县',
    '洛宁县': '洛宁县',
    '伊川县': '伊川县',
    '偃师市': '偃师市'
  }, 410400: {
    '平顶山市市辖区': '平顶山市市辖区',
    '新华区': '新华区',
    '卫东区': '卫东区',
    '石龙区': '石龙区',
    '湛河区': '湛河区',
    '宝丰县': '宝丰县',
    '叶县': '叶县',
    '鲁山县': '鲁山县',
    '郏县': '郏县',
    '舞钢市': '舞钢市',
    '汝州市': '汝州市'
  }, 410500: {
    '安阳市市辖区': '安阳市市辖区',
    '文峰区': '文峰区',
    '北关区': '北关区',
    '殷都区': '殷都区',
    '龙安区': '龙安区',
    '安阳县': '安阳县',
    '汤阴县': '汤阴县',
    '滑县': '滑县',
    '内黄县': '内黄县',
    '林州市': '林州市'
  }, 410600: {
    '鹤壁市市辖区': '鹤壁市市辖区',
    '鹤山区': '鹤山区',
    '山城区': '山城区',
    '淇滨区': '淇滨区',
    '浚县': '浚县',
    '淇县': '淇县'
  }, 410700: {
    '新乡市市辖区': '新乡市市辖区',
    '红旗区': '红旗区',
    '卫滨区': '卫滨区',
    '凤泉区': '凤泉区',
    '牧野区': '牧野区',
    '新乡县': '新乡县',
    '获嘉县': '获嘉县',
    '原阳县': '原阳县',
    '延津县': '延津县',
    '封丘县': '封丘县',
    '长垣县': '长垣县',
    '卫辉市': '卫辉市',
    '辉县市': '辉县市'
  }, 410800: {
    '焦作市市辖区': '焦作市市辖区',
    '解放区': '解放区',
    '中站区': '中站区',
    '马村区': '马村区',
    '山阳区': '山阳区',
    '修武县': '修武县',
    '博爱县': '博爱县',
    '武陟县': '武陟县',
    '温县': '温县',
    '沁阳市': '沁阳市',
    '孟州市': '孟州市'
  }, 410900: {
    '濮阳市市辖区': '濮阳市市辖区',
    '华龙区': '华龙区',
    '清丰县': '清丰县',
    '南乐县': '南乐县',
    '范县': '范县',
    '台前县': '台前县',
    '濮阳县': '濮阳县'
  }, 411000: {
    '许昌市市辖区': '许昌市市辖区',
    '魏都区': '魏都区',
    '建安区': '建安区',
    '鄢陵县': '鄢陵县',
    '襄城县': '襄城县',
    '禹州市': '禹州市',
    '长葛市': '长葛市'
  }, 411100: {
    '漯河市市辖区': '漯河市市辖区',
    '源汇区': '源汇区',
    '郾城区': '郾城区',
    '召陵区': '召陵区',
    '舞阳县': '舞阳县',
    '临颍县': '临颍县'
  }, 411200: {
    '三门峡市市辖区': '三门峡市市辖区',
    '湖滨区': '湖滨区',
    '陕州区': '陕州区',
    '渑池县': '渑池县',
    '卢氏县': '卢氏县',
    '义马市': '义马市',
    '灵宝市': '灵宝市'
  }, 411300: {
    '南阳市市辖区': '南阳市市辖区',
    '宛城区': '宛城区',
    '卧龙区': '卧龙区',
    '南召县': '南召县',
    '方城县': '方城县',
    '西峡县': '西峡县',
    '镇平县': '镇平县',
    '内乡县': '内乡县',
    '淅川县': '淅川县',
    '社旗县': '社旗县',
    '唐河县': '唐河县',
    '新野县': '新野县',
    '桐柏县': '桐柏县',
    '邓州市': '邓州市'
  }, 411400: {
    '商丘市市辖区': '商丘市市辖区',
    '梁园区': '梁园区',
    '睢阳区': '睢阳区',
    '民权县': '民权县',
    '睢县': '睢县',
    '宁陵县': '宁陵县',
    '柘城县': '柘城县',
    '虞城县': '虞城县',
    '夏邑县': '夏邑县',
    '永城市': '永城市'
  }, 411500: {
    '信阳市市辖区': '信阳市市辖区',
    '浉河区': '浉河区',
    '平桥区': '平桥区',
    '罗山县': '罗山县',
    '光山县': '光山县',
    '新县': '新县',
    '商城县': '商城县',
    '固始县': '固始县',
    '潢川县': '潢川县',
    '淮滨县': '淮滨县',
    '息县': '息县'
  }, 411600: {
    '周口市市辖区': '周口市市辖区',
    '川汇区': '川汇区',
    '扶沟县': '扶沟县',
    '西华县': '西华县',
    '商水县': '商水县',
    '沈丘县': '沈丘县',
    '郸城县': '郸城县',
    '淮阳县': '淮阳县',
    '太康县': '太康县',
    '鹿邑县': '鹿邑县',
    '项城市': '项城市'
  }, 411700: {
    '驻马店市市辖区': '驻马店市市辖区',
    '驿城区': '驿城区',
    '西平县': '西平县',
    '上蔡县': '上蔡县',
    '平舆县': '平舆县',
    '正阳县': '正阳县',
    '确山县': '确山县',
    '泌阳县': '泌阳县',
    '汝南县': '汝南县',
    '遂平县': '遂平县',
    '新蔡县': '新蔡县'
  }, 420100: {
    '武汉市市辖区': '武汉市市辖区',
    '江岸区': '江岸区',
    '江汉区': '江汉区',
    '硚口区': '硚口区',
    '汉阳区': '汉阳区',
    '武昌区': '武昌区',
    '青山区': '青山区',
    '洪山区': '洪山区',
    '东西湖区': '东西湖区',
    '汉南区': '汉南区',
    '蔡甸区': '蔡甸区',
    '江夏区': '江夏区',
    '黄陂区': '黄陂区',
    '新洲区': '新洲区'
  }, 420200: {
    '黄石市市辖区': '黄石市市辖区',
    '黄石港区': '黄石港区',
    '西塞山区': '西塞山区',
    '下陆区': '下陆区',
    '铁山区': '铁山区',
    '阳新县': '阳新县',
    '大冶市': '大冶市'
  }, 420300: {
    '十堰市市辖区': '十堰市市辖区',
    '茅箭区': '茅箭区',
    '张湾区': '张湾区',
    '郧阳区': '郧阳区',
    '郧西县': '郧西县',
    '竹山县': '竹山县',
    '竹溪县': '竹溪县',
    '房县': '房县',
    '丹江口市': '丹江口市'
  }, 420500: {
    '宜昌市市辖区': '宜昌市市辖区',
    '西陵区': '西陵区',
    '伍家岗区': '伍家岗区',
    '点军区': '点军区',
    '猇亭区': '猇亭区',
    '夷陵区': '夷陵区',
    '远安县': '远安县',
    '兴山县': '兴山县',
    '秭归县': '秭归县',
    '长阳土家族自治县': '长阳土家族自治县',
    '五峰土家族自治县': '五峰土家族自治县',
    '宜都市': '宜都市',
    '当阳市': '当阳市',
    '枝江市': '枝江市'
  }, 420600: {
    '襄阳市市辖区': '襄阳市市辖区',
    '襄城区': '襄城区',
    '樊城区': '樊城区',
    '襄州区': '襄州区',
    '南漳县': '南漳县',
    '谷城县': '谷城县',
    '保康县': '保康县',
    '老河口市': '老河口市',
    '枣阳市': '枣阳市',
    '宜城市': '宜城市'
  }, 420700: {
    '鄂州市市辖区': '鄂州市市辖区',
    '梁子湖区': '梁子湖区',
    '华容区': '华容区',
    '鄂城区': '鄂城区'
  }, 420800: {
    '荆门市市辖区': '荆门市市辖区',
    '东宝区': '东宝区',
    '掇刀区': '掇刀区',
    '京山市': '京山市',
    '沙洋县': '沙洋县',
    '钟祥市': '钟祥市'
  }, 420900: {
    '孝感市市辖区': '孝感市市辖区',
    '孝南区': '孝南区',
    '孝昌县': '孝昌县',
    '大悟县': '大悟县',
    '云梦县': '云梦县',
    '应城市': '应城市',
    '安陆市': '安陆市',
    '汉川市': '汉川市'
  }, 421000: {
    '荆州市市辖区': '荆州市市辖区',
    '沙市区': '沙市区',
    '荆州区': '荆州区',
    '公安县': '公安县',
    '监利县': '监利县',
    '江陵县': '江陵县',
    '石首市': '石首市',
    '洪湖市': '洪湖市',
    '松滋市': '松滋市'
  }, 421100: {
    '黄冈市市辖区': '黄冈市市辖区',
    '黄州区': '黄州区',
    '团风县': '团风县',
    '红安县': '红安县',
    '罗田县': '罗田县',
    '英山县': '英山县',
    '浠水县': '浠水县',
    '蕲春县': '蕲春县',
    '黄梅县': '黄梅县',
    '麻城市': '麻城市',
    '武穴市': '武穴市'
  }, 421200: {
    '咸宁市市辖区': '咸宁市市辖区',
    '咸安区': '咸安区',
    '嘉鱼县': '嘉鱼县',
    '通城县': '通城县',
    '崇阳县': '崇阳县',
    '通山县': '通山县',
    '赤壁市': '赤壁市'
  }, 421300: {
    '随州市市辖区': '随州市市辖区',
    '曾都区': '曾都区',
    '随县': '随县',
    '广水市': '广水市'
  }, 422800: {
    '恩施市': '恩施市',
    '利川市': '利川市',
    '建始县': '建始县',
    '巴东县': '巴东县',
    '宣恩县': '宣恩县',
    '咸丰县': '咸丰县',
    '来凤县': '来凤县',
    '鹤峰县': '鹤峰县'
  }, 430100: {
    '长沙市市辖区': '长沙市市辖区',
    '芙蓉区': '芙蓉区',
    '天心区': '天心区',
    '岳麓区': '岳麓区',
    '开福区': '开福区',
    '雨花区': '雨花区',
    '望城区': '望城区',
    '长沙县': '长沙县',
    '浏阳市': '浏阳市',
    '宁乡市': '宁乡市'
  }, 430200: {
    '株洲市市辖区': '株洲市市辖区',
    '荷塘区': '荷塘区',
    '芦淞区': '芦淞区',
    '石峰区': '石峰区',
    '天元区': '天元区',
    '渌口区': '渌口区',
    '攸县': '攸县',
    '茶陵县': '茶陵县',
    '炎陵县': '炎陵县',
    '醴陵市': '醴陵市'
  }, 430300: {
    '湘潭市市辖区': '湘潭市市辖区',
    '雨湖区': '雨湖区',
    '岳塘区': '岳塘区',
    '湘潭县': '湘潭县',
    '湘乡市': '湘乡市',
    '韶山市': '韶山市'
  }, 430400: {
    '衡阳市市辖区': '衡阳市市辖区',
    '珠晖区': '珠晖区',
    '雁峰区': '雁峰区',
    '石鼓区': '石鼓区',
    '蒸湘区': '蒸湘区',
    '南岳区': '南岳区',
    '衡阳县': '衡阳县',
    '衡南县': '衡南县',
    '衡山县': '衡山县',
    '衡东县': '衡东县',
    '祁东县': '祁东县',
    '耒阳市': '耒阳市',
    '常宁市': '常宁市'
  }, 430500: {
    '邵阳市市辖区': '邵阳市市辖区',
    '双清区': '双清区',
    '大祥区': '大祥区',
    '北塔区': '北塔区',
    '邵东县': '邵东县',
    '新邵县': '新邵县',
    '邵阳县': '邵阳县',
    '隆回县': '隆回县',
    '洞口县': '洞口县',
    '绥宁县': '绥宁县',
    '新宁县': '新宁县',
    '城步苗族自治县': '城步苗族自治县',
    '武冈市': '武冈市'
  }, 430600: {
    '岳阳市市辖区': '岳阳市市辖区',
    '岳阳楼区': '岳阳楼区',
    '云溪区': '云溪区',
    '君山区': '君山区',
    '岳阳县': '岳阳县',
    '华容县': '华容县',
    '湘阴县': '湘阴县',
    '平江县': '平江县',
    '汨罗市': '汨罗市',
    '临湘市': '临湘市'
  }, 430700: {
    '常德市市辖区': '常德市市辖区',
    '武陵区': '武陵区',
    '鼎城区': '鼎城区',
    '安乡县': '安乡县',
    '汉寿县': '汉寿县',
    '澧县': '澧县',
    '临澧县': '临澧县',
    '桃源县': '桃源县',
    '石门县': '石门县',
    '津市市': '津市市'
  }, 430800: {
    '张家界市市辖区': '张家界市市辖区',
    '永定区': '永定区',
    '武陵源区': '武陵源区',
    '慈利县': '慈利县',
    '桑植县': '桑植县'
  }, 430900: {
    '益阳市市辖区': '益阳市市辖区',
    '资阳区': '资阳区',
    '赫山区': '赫山区',
    '南县': '南县',
    '桃江县': '桃江县',
    '安化县': '安化县',
    '沅江市': '沅江市'
  }, 431000: {
    '郴州市市辖区': '郴州市市辖区',
    '北湖区': '北湖区',
    '苏仙区': '苏仙区',
    '桂阳县': '桂阳县',
    '宜章县': '宜章县',
    '永兴县': '永兴县',
    '嘉禾县': '嘉禾县',
    '临武县': '临武县',
    '汝城县': '汝城县',
    '桂东县': '桂东县',
    '安仁县': '安仁县',
    '资兴市': '资兴市'
  }, 431100: {
    '永州市市辖区': '永州市市辖区',
    '零陵区': '零陵区',
    '冷水滩区': '冷水滩区',
    '祁阳县': '祁阳县',
    '东安县': '东安县',
    '双牌县': '双牌县',
    '道县': '道县',
    '江永县': '江永县',
    '宁远县': '宁远县',
    '蓝山县': '蓝山县',
    '新田县': '新田县',
    '江华瑶族自治县': '江华瑶族自治县'
  }, 431200: {
    '怀化市市辖区': '怀化市市辖区',
    '鹤城区': '鹤城区',
    '中方县': '中方县',
    '沅陵县': '沅陵县',
    '辰溪县': '辰溪县',
    '溆浦县': '溆浦县',
    '会同县': '会同县',
    '麻阳苗族自治县': '麻阳苗族自治县',
    '新晃侗族自治县': '新晃侗族自治县',
    '芷江侗族自治县': '芷江侗族自治县',
    '靖州苗族侗族自治县': '靖州苗族侗族自治县',
    '通道侗族自治县': '通道侗族自治县',
    '洪江市': '洪江市'
  }, 431300: {
    '娄底市市辖区': '娄底市市辖区',
    '娄星区': '娄星区',
    '双峰县': '双峰县',
    '新化县': '新化县',
    '冷水江市': '冷水江市',
    '涟源市': '涟源市'
  }, 433100: {
    '吉首市': '吉首市',
    '泸溪县': '泸溪县',
    '凤凰县': '凤凰县',
    '花垣县': '花垣县',
    '保靖县': '保靖县',
    '古丈县': '古丈县',
    '永顺县': '永顺县',
    '龙山县': '龙山县'
  }, 440100: {
    '广州市市辖区': '广州市市辖区',
    '荔湾区': '荔湾区',
    '越秀区': '越秀区',
    '海珠区': '海珠区',
    '天河区': '天河区',
    '白云区': '白云区',
    '黄埔区': '黄埔区',
    '番禺区': '番禺区',
    '花都区': '花都区',
    '南沙区': '南沙区',
    '从化区': '从化区',
    '增城区': '增城区'
  }, 440200: {
    '韶关市市辖区': '韶关市市辖区',
    '武江区': '武江区',
    '浈江区': '浈江区',
    '曲江区': '曲江区',
    '始兴县': '始兴县',
    '仁化县': '仁化县',
    '翁源县': '翁源县',
    '乳源瑶族自治县': '乳源瑶族自治县',
    '新丰县': '新丰县',
    '乐昌市': '乐昌市',
    '南雄市': '南雄市'
  }, 440300: {
    '深圳市市辖区': '深圳市市辖区',
    '罗湖区': '罗湖区',
    '福田区': '福田区',
    '南山区': '南山区',
    '宝安区': '宝安区',
    '龙岗区': '龙岗区',
    '盐田区': '盐田区',
    '龙华区': '龙华区',
    '坪山区': '坪山区',
    '光明区': '光明区'
  }, 440400: {
    '珠海市市辖区': '珠海市市辖区',
    '香洲区': '香洲区',
    '斗门区': '斗门区',
    '金湾区': '金湾区'
  }, 440500: {
    '汕头市市辖区': '汕头市市辖区',
    '龙湖区': '龙湖区',
    '金平区': '金平区',
    '濠江区': '濠江区',
    '潮阳区': '潮阳区',
    '潮南区': '潮南区',
    '澄海区': '澄海区',
    '南澳县': '南澳县'
  }, 440600: {
    '佛山市市辖区': '佛山市市辖区',
    '禅城区': '禅城区',
    '南海区': '南海区',
    '顺德区': '顺德区',
    '三水区': '三水区',
    '高明区': '高明区'
  }, 440700: {
    '江门市市辖区': '江门市市辖区',
    '蓬江区': '蓬江区',
    '江海区': '江海区',
    '新会区': '新会区',
    '台山市': '台山市',
    '开平市': '开平市',
    '鹤山市': '鹤山市',
    '恩平市': '恩平市'
  }, 440800: {
    '湛江市市辖区': '湛江市市辖区',
    '赤坎区': '赤坎区',
    '霞山区': '霞山区',
    '坡头区': '坡头区',
    '麻章区': '麻章区',
    '遂溪县': '遂溪县',
    '徐闻县': '徐闻县',
    '廉江市': '廉江市',
    '雷州市': '雷州市',
    '吴川市': '吴川市'
  }, 440900: {
    '茂名市市辖区': '茂名市市辖区',
    '茂南区': '茂南区',
    '电白区': '电白区',
    '高州市': '高州市',
    '化州市': '化州市',
    '信宜市': '信宜市'
  }, 441200: {
    '肇庆市市辖区': '肇庆市市辖区',
    '端州区': '端州区',
    '鼎湖区': '鼎湖区',
    '高要区': '高要区',
    '广宁县': '广宁县',
    '怀集县': '怀集县',
    '封开县': '封开县',
    '德庆县': '德庆县',
    '四会市': '四会市'
  }, 441300: {
    '惠州市市辖区': '惠州市市辖区',
    '惠城区': '惠城区',
    '惠阳区': '惠阳区',
    '博罗县': '博罗县',
    '惠东县': '惠东县',
    '龙门县': '龙门县'
  }, 441400: {
    '梅州市市辖区': '梅州市市辖区',
    '梅江区': '梅江区',
    '梅县区': '梅县区',
    '大埔县': '大埔县',
    '丰顺县': '丰顺县',
    '五华县': '五华县',
    '平远县': '平远县',
    '蕉岭县': '蕉岭县',
    '兴宁市': '兴宁市'
  }, 441500: {
    '汕尾市市辖区': '汕尾市市辖区',
    '城区': '城区',
    '海丰县': '海丰县',
    '陆河县': '陆河县',
    '陆丰市': '陆丰市'
  }, 441600: {
    '河源市市辖区': '河源市市辖区',
    '源城区': '源城区',
    '紫金县': '紫金县',
    '龙川县': '龙川县',
    '连平县': '连平县',
    '和平县': '和平县',
    '东源县': '东源县'
  }, 441700: {
    '阳江市市辖区': '阳江市市辖区',
    '江城区': '江城区',
    '阳东区': '阳东区',
    '阳西县': '阳西县',
    '阳春市': '阳春市'
  }, 441800: {
    '清远市市辖区': '清远市市辖区',
    '清城区': '清城区',
    '清新区': '清新区',
    '佛冈县': '佛冈县',
    '阳山县': '阳山县',
    '连山壮族瑶族自治县': '连山壮族瑶族自治县',
    '连南瑶族自治县': '连南瑶族自治县',
    '英德市': '英德市',
    '连州市': '连州市'
  }, 445100: {
    '潮州市市辖区': '潮州市市辖区',
    '湘桥区': '湘桥区',
    '潮安区': '潮安区',
    '饶平县': '饶平县'
  }, 445200: {
    '揭阳市市辖区': '揭阳市市辖区',
    '榕城区': '榕城区',
    '揭东区': '揭东区',
    '揭西县': '揭西县',
    '惠来县': '惠来县',
    '普宁市': '普宁市'
  }, 445300: {
    '云浮市市辖区': '云浮市市辖区',
    '云城区': '云城区',
    '云安区': '云安区',
    '新兴县': '新兴县',
    '郁南县': '郁南县',
    '罗定市': '罗定市'
  }, 450100: {
    '南宁市市辖区': '南宁市市辖区',
    '兴宁区': '兴宁区',
    '青秀区': '青秀区',
    '江南区': '江南区',
    '西乡塘区': '西乡塘区',
    '良庆区': '良庆区',
    '邕宁区': '邕宁区',
    '武鸣区': '武鸣区',
    '隆安县': '隆安县',
    '马山县': '马山县',
    '上林县': '上林县',
    '宾阳县': '宾阳县',
    '横县': '横县'
  }, 450200: {
    '柳州市市辖区': '柳州市市辖区',
    '城中区': '城中区',
    '鱼峰区': '鱼峰区',
    '柳南区': '柳南区',
    '柳北区': '柳北区',
    '柳江区': '柳江区',
    '柳城县': '柳城县',
    '鹿寨县': '鹿寨县',
    '融安县': '融安县',
    '融水苗族自治县': '融水苗族自治县',
    '三江侗族自治县': '三江侗族自治县'
  }, 450300: {
    '桂林市市辖区': '桂林市市辖区',
    '秀峰区': '秀峰区',
    '叠彩区': '叠彩区',
    '象山区': '象山区',
    '七星区': '七星区',
    '雁山区': '雁山区',
    '临桂区': '临桂区',
    '阳朔县': '阳朔县',
    '灵川县': '灵川县',
    '全州县': '全州县',
    '兴安县': '兴安县',
    '永福县': '永福县',
    '灌阳县': '灌阳县',
    '龙胜各族自治县': '龙胜各族自治县',
    '资源县': '资源县',
    '平乐县': '平乐县',
    '荔浦市': '荔浦市',
    '恭城瑶族自治县': '恭城瑶族自治县'
  }, 450400: {
    '梧州市市辖区': '梧州市市辖区',
    '万秀区': '万秀区',
    '长洲区': '长洲区',
    '龙圩区': '龙圩区',
    '苍梧县': '苍梧县',
    '藤县': '藤县',
    '蒙山县': '蒙山县',
    '岑溪市': '岑溪市'
  }, 450500: {
    '北海市市辖区': '北海市市辖区',
    '海城区': '海城区',
    '银海区': '银海区',
    '铁山港区': '铁山港区',
    '合浦县': '合浦县'
  }, 450600: {
    '防城港市市辖区': '防城港市市辖区',
    '港口区': '港口区',
    '防城区': '防城区',
    '上思县': '上思县',
    '东兴市': '东兴市'
  }, 450700: {
    '钦州市市辖区': '钦州市市辖区',
    '钦南区': '钦南区',
    '钦北区': '钦北区',
    '灵山县': '灵山县',
    '浦北县': '浦北县'
  }, 450800: {
    '贵港市市辖区': '贵港市市辖区',
    '港北区': '港北区',
    '港南区': '港南区',
    '覃塘区': '覃塘区',
    '平南县': '平南县',
    '桂平市': '桂平市'
  }, 450900: {
    '玉林市市辖区': '玉林市市辖区',
    '玉州区': '玉州区',
    '福绵区': '福绵区',
    '容县': '容县',
    '陆川县': '陆川县',
    '博白县': '博白县',
    '兴业县': '兴业县',
    '北流市': '北流市'
  }, 451000: {
    '百色市市辖区': '百色市市辖区',
    '右江区': '右江区',
    '田阳县': '田阳县',
    '田东县': '田东县',
    '平果县': '平果县',
    '德保县': '德保县',
    '那坡县': '那坡县',
    '凌云县': '凌云县',
    '乐业县': '乐业县',
    '田林县': '田林县',
    '西林县': '西林县',
    '隆林各族自治县': '隆林各族自治县',
    '靖西市': '靖西市'
  }, 451100: {
    '贺州市市辖区': '贺州市市辖区',
    '八步区': '八步区',
    '平桂区': '平桂区',
    '昭平县': '昭平县',
    '钟山县': '钟山县',
    '富川瑶族自治县': '富川瑶族自治县'
  }, 451200: {
    '河池市市辖区': '河池市市辖区',
    '金城江区': '金城江区',
    '宜州区': '宜州区',
    '南丹县': '南丹县',
    '天峨县': '天峨县',
    '凤山县': '凤山县',
    '东兰县': '东兰县',
    '罗城仫佬族自治县': '罗城仫佬族自治县',
    '环江毛南族自治县': '环江毛南族自治县',
    '巴马瑶族自治县': '巴马瑶族自治县',
    '都安瑶族自治县': '都安瑶族自治县',
    '大化瑶族自治县': '大化瑶族自治县'
  }, 451300: {
    '来宾市市辖区': '来宾市市辖区',
    '兴宾区': '兴宾区',
    '忻城县': '忻城县',
    '象州县': '象州县',
    '武宣县': '武宣县',
    '金秀瑶族自治县': '金秀瑶族自治县',
    '合山市': '合山市'
  }, 451400: {
    '崇左市市辖区': '崇左市市辖区',
    '江州区': '江州区',
    '扶绥县': '扶绥县',
    '宁明县': '宁明县',
    '龙州县': '龙州县',
    '大新县': '大新县',
    '天等县': '天等县',
    '凭祥市': '凭祥市'
  }, 460100: {
    '海口市市辖区': '海口市市辖区',
    '秀英区': '秀英区',
    '龙华区': '龙华区',
    '琼山区': '琼山区',
    '美兰区': '美兰区'
  }, 460200: {
    '三亚市市辖区': '三亚市市辖区',
    '海棠区': '海棠区',
    '吉阳区': '吉阳区',
    '天涯区': '天涯区',
    '崖州区': '崖州区'
  }, 460300: {
    '三沙市市辖区': '三沙市市辖区',
    '西沙群岛': '西沙群岛',
    '南沙群岛': '南沙群岛',
    '中沙群岛的岛礁及其海域': '中沙群岛的岛礁及其海域'
  }, 500100: {
    '万州区': '万州区',
    '涪陵区': '涪陵区',
    '渝中区': '渝中区',
    '大渡口区': '大渡口区',
    '江北区': '江北区',
    '沙坪坝区': '沙坪坝区',
    '九龙坡区': '九龙坡区',
    '南岸区': '南岸区',
    '北碚区': '北碚区',
    '綦江区': '綦江区',
    '大足区': '大足区',
    '渝北区': '渝北区',
    '巴南区': '巴南区',
    '黔江区': '黔江区',
    '长寿区': '长寿区',
    '江津区': '江津区',
    '合川区': '合川区',
    '永川区': '永川区',
    '南川区': '南川区',
    '璧山区': '璧山区',
    '铜梁区': '铜梁区',
    '潼南区': '潼南区',
    '荣昌区': '荣昌区',
    '开州区': '开州区',
    '梁平区': '梁平区',
    '武隆区': '武隆区'
  }, 500200: {
    '城口县': '城口县',
    '丰都县': '丰都县',
    '垫江县': '垫江县',
    '忠县': '忠县',
    '云阳县': '云阳县',
    '奉节县': '奉节县',
    '巫山县': '巫山县',
    '巫溪县': '巫溪县',
    '石柱土家族自治县': '石柱土家族自治县',
    '秀山土家族苗族自治县': '秀山土家族苗族自治县',
    '酉阳土家族苗族自治县': '酉阳土家族苗族自治县',
    '彭水苗族土家族自治县': '彭水苗族土家族自治县'
  }, 510100: {
    '成都市市辖区': '成都市市辖区',
    '锦江区': '锦江区',
    '青羊区': '青羊区',
    '金牛区': '金牛区',
    '武侯区': '武侯区',
    '成华区': '成华区',
    '龙泉驿区': '龙泉驿区',
    '青白江区': '青白江区',
    '新都区': '新都区',
    '温江区': '温江区',
    '双流区': '双流区',
    '郫都区': '郫都区',
    '金堂县': '金堂县',
    '大邑县': '大邑县',
    '蒲江县': '蒲江县',
    '新津县': '新津县',
    '都江堰市': '都江堰市',
    '彭州市': '彭州市',
    '邛崃市': '邛崃市',
    '崇州市': '崇州市',
    '简阳市': '简阳市'
  }, 510300: {
    '自贡市市辖区': '自贡市市辖区',
    '自流井区': '自流井区',
    '贡井区': '贡井区',
    '大安区': '大安区',
    '沿滩区': '沿滩区',
    '荣县': '荣县',
    '富顺县': '富顺县'
  }, 510400: {
    '攀枝花市市辖区': '攀枝花市市辖区',
    '东区': '东区',
    '西区': '西区',
    '仁和区': '仁和区',
    '米易县': '米易县',
    '盐边县': '盐边县'
  }, 510500: {
    '泸州市市辖区': '泸州市市辖区',
    '江阳区': '江阳区',
    '纳溪区': '纳溪区',
    '龙马潭区': '龙马潭区',
    '泸县': '泸县',
    '合江县': '合江县',
    '叙永县': '叙永县',
    '古蔺县': '古蔺县'
  }, 510600: {
    '德阳市市辖区': '德阳市市辖区',
    '旌阳区': '旌阳区',
    '中江县': '中江县',
    '罗江区': '罗江区',
    '广汉市': '广汉市',
    '什邡市': '什邡市',
    '绵竹市': '绵竹市'
  }, 510700: {
    '绵阳市市辖区': '绵阳市市辖区',
    '涪城区': '涪城区',
    '游仙区': '游仙区',
    '安州区': '安州区',
    '三台县': '三台县',
    '盐亭县': '盐亭县',
    '梓潼县': '梓潼县',
    '北川羌族自治县': '北川羌族自治县',
    '平武县': '平武县',
    '江油市': '江油市'
  }, 510800: {
    '广元市市辖区': '广元市市辖区',
    '利州区': '利州区',
    '昭化区': '昭化区',
    '朝天区': '朝天区',
    '旺苍县': '旺苍县',
    '青川县': '青川县',
    '剑阁县': '剑阁县',
    '苍溪县': '苍溪县'
  }, 510900: {
    '遂宁市市辖区': '遂宁市市辖区',
    '船山区': '船山区',
    '安居区': '安居区',
    '蓬溪县': '蓬溪县',
    '射洪县': '射洪县',
    '大英县': '大英县'
  }, 511000: {
    '内江市市辖区': '内江市市辖区',
    '市中区': '市中区',
    '东兴区': '东兴区',
    '威远县': '威远县',
    '资中县': '资中县',
    '隆昌市': '隆昌市'
  }, 511100: {
    '乐山市市辖区': '乐山市市辖区',
    '市中区': '市中区',
    '沙湾区': '沙湾区',
    '五通桥区': '五通桥区',
    '金口河区': '金口河区',
    '犍为县': '犍为县',
    '井研县': '井研县',
    '夹江县': '夹江县',
    '沐川县': '沐川县',
    '峨边彝族自治县': '峨边彝族自治县',
    '马边彝族自治县': '马边彝族自治县',
    '峨眉山市': '峨眉山市'
  }, 511300: {
    '南充市市辖区': '南充市市辖区',
    '顺庆区': '顺庆区',
    '高坪区': '高坪区',
    '嘉陵区': '嘉陵区',
    '南部县': '南部县',
    '营山县': '营山县',
    '蓬安县': '蓬安县',
    '仪陇县': '仪陇县',
    '西充县': '西充县',
    '阆中市': '阆中市'
  }, 511400: {
    '眉山市市辖区': '眉山市市辖区',
    '东坡区': '东坡区',
    '彭山区': '彭山区',
    '仁寿县': '仁寿县',
    '洪雅县': '洪雅县',
    '丹棱县': '丹棱县',
    '青神县': '青神县'
  }, 511500: {
    '宜宾市市辖区': '宜宾市市辖区',
    '翠屏区': '翠屏区',
    '南溪区': '南溪区',
    '叙州区': '叙州区',
    '江安县': '江安县',
    '长宁县': '长宁县',
    '高县': '高县',
    '珙县': '珙县',
    '筠连县': '筠连县',
    '兴文县': '兴文县',
    '屏山县': '屏山县'
  }, 511600: {
    '广安市市辖区': '广安市市辖区',
    '广安区': '广安区',
    '前锋区': '前锋区',
    '岳池县': '岳池县',
    '武胜县': '武胜县',
    '邻水县': '邻水县',
    '华蓥市': '华蓥市'
  }, 511700: {
    '达州市市辖区': '达州市市辖区',
    '通川区': '通川区',
    '达川区': '达川区',
    '宣汉县': '宣汉县',
    '开江县': '开江县',
    '大竹县': '大竹县',
    '渠县': '渠县',
    '万源市': '万源市'
  }, 511800: {
    '雅安市市辖区': '雅安市市辖区',
    '雨城区': '雨城区',
    '名山区': '名山区',
    '荥经县': '荥经县',
    '汉源县': '汉源县',
    '石棉县': '石棉县',
    '天全县': '天全县',
    '芦山县': '芦山县',
    '宝兴县': '宝兴县'
  }, 511900: {
    '巴中市市辖区': '巴中市市辖区',
    '巴州区': '巴州区',
    '恩阳区': '恩阳区',
    '通江县': '通江县',
    '南江县': '南江县',
    '平昌县': '平昌县'
  }, 512000: {
    '资阳市市辖区': '资阳市市辖区',
    '雁江区': '雁江区',
    '安岳县': '安岳县',
    '乐至县': '乐至县'
  }, 513200: {
    '马尔康市': '马尔康市',
    '汶川县': '汶川县',
    '理县': '理县',
    '茂县': '茂县',
    '松潘县': '松潘县',
    '九寨沟县': '九寨沟县',
    '金川县': '金川县',
    '小金县': '小金县',
    '黑水县': '黑水县',
    '壤塘县': '壤塘县',
    '阿坝县': '阿坝县',
    '若尔盖县': '若尔盖县',
    '红原县': '红原县'
  }, 513300: {
    '康定市': '康定市',
    '泸定县': '泸定县',
    '丹巴县': '丹巴县',
    '九龙县': '九龙县',
    '雅江县': '雅江县',
    '道孚县': '道孚县',
    '炉霍县': '炉霍县',
    '甘孜县': '甘孜县',
    '新龙县': '新龙县',
    '德格县': '德格县',
    '白玉县': '白玉县',
    '石渠县': '石渠县',
    '色达县': '色达县',
    '理塘县': '理塘县',
    '巴塘县': '巴塘县',
    '乡城县': '乡城县',
    '稻城县': '稻城县',
    '得荣县': '得荣县'
  }, 513400: {
    '西昌市': '西昌市',
    '木里藏族自治县': '木里藏族自治县',
    '盐源县': '盐源县',
    '德昌县': '德昌县',
    '会理县': '会理县',
    '会东县': '会东县',
    '宁南县': '宁南县',
    '普格县': '普格县',
    '布拖县': '布拖县',
    '金阳县': '金阳县',
    '昭觉县': '昭觉县',
    '喜德县': '喜德县',
    '冕宁县': '冕宁县',
    '越西县': '越西县',
    '甘洛县': '甘洛县',
    '美姑县': '美姑县',
    '雷波县': '雷波县'
  }, 520100: {
    '贵阳市市辖区': '贵阳市市辖区',
    '南明区': '南明区',
    '云岩区': '云岩区',
    '花溪区': '花溪区',
    '乌当区': '乌当区',
    '白云区': '白云区',
    '观山湖区': '观山湖区',
    '开阳县': '开阳县',
    '息烽县': '息烽县',
    '修文县': '修文县',
    '清镇市': '清镇市'
  }, 520200: {
    '钟山区': '钟山区',
    '六枝特区': '六枝特区',
    '水城县': '水城县',
    '盘州市': '盘州市'
  }, 520300: {
    '遵义市市辖区': '遵义市市辖区',
    '红花岗区': '红花岗区',
    '汇川区': '汇川区',
    '播州区': '播州区',
    '桐梓县': '桐梓县',
    '绥阳县': '绥阳县',
    '正安县': '正安县',
    '道真仡佬族苗族自治县': '道真仡佬族苗族自治县',
    '务川仡佬族苗族自治县': '务川仡佬族苗族自治县',
    '凤冈县': '凤冈县',
    '湄潭县': '湄潭县',
    '余庆县': '余庆县',
    '习水县': '习水县',
    '赤水市': '赤水市',
    '仁怀市': '仁怀市'
  }, 520400: {
    '安顺市市辖区': '安顺市市辖区',
    '西秀区': '西秀区',
    '平坝区': '平坝区',
    '普定县': '普定县',
    '镇宁布依族苗族自治县': '镇宁布依族苗族自治县',
    '关岭布依族苗族自治县': '关岭布依族苗族自治县',
    '紫云苗族布依族自治县': '紫云苗族布依族自治县'
  }, 520500: {
    '七星关区': '七星关区',
    '大方县': '大方县',
    '黔西县': '黔西县',
    '金沙县': '金沙县',
    '织金县': '织金县',
    '纳雍县': '纳雍县',
    '威宁彝族回族苗族自治县': '威宁彝族回族苗族自治县',
    '赫章县': '赫章县'
  }, 520600: {
    '碧江区': '碧江区',
    '万山区': '万山区',
    '江口县': '江口县',
    '玉屏侗族自治县': '玉屏侗族自治县',
    '石阡县': '石阡县',
    '思南县': '思南县',
    '印江土家族苗族自治县': '印江土家族苗族自治县',
    '德江县': '德江县',
    '沿河土家族自治县': '沿河土家族自治县',
    '松桃苗族自治县': '松桃苗族自治县'
  }, 522300: {
    '兴义市': '兴义市',
    '兴仁市': '兴仁市',
    '普安县': '普安县',
    '晴隆县': '晴隆县',
    '贞丰县': '贞丰县',
    '望谟县': '望谟县',
    '册亨县': '册亨县',
    '安龙县': '安龙县'
  }, 522600: {
    '凯里市': '凯里市',
    '黄平县': '黄平县',
    '施秉县': '施秉县',
    '三穗县': '三穗县',
    '镇远县': '镇远县',
    '岑巩县': '岑巩县',
    '天柱县': '天柱县',
    '锦屏县': '锦屏县',
    '剑河县': '剑河县',
    '台江县': '台江县',
    '黎平县': '黎平县',
    '榕江县': '榕江县',
    '从江县': '从江县',
    '雷山县': '雷山县',
    '麻江县': '麻江县',
    '丹寨县': '丹寨县'
  }, 522700: {
    '都匀市': '都匀市',
    '福泉市': '福泉市',
    '荔波县': '荔波县',
    '贵定县': '贵定县',
    '瓮安县': '瓮安县',
    '独山县': '独山县',
    '平塘县': '平塘县',
    '罗甸县': '罗甸县',
    '长顺县': '长顺县',
    '龙里县': '龙里县',
    '惠水县': '惠水县',
    '三都水族自治县': '三都水族自治县'
  }, 530100: {
    '昆明市市辖区': '昆明市市辖区',
    '五华区': '五华区',
    '盘龙区': '盘龙区',
    '官渡区': '官渡区',
    '西山区': '西山区',
    '东川区': '东川区',
    '呈贡区': '呈贡区',
    '晋宁区': '晋宁区',
    '富民县': '富民县',
    '宜良县': '宜良县',
    '石林彝族自治县': '石林彝族自治县',
    '嵩明县': '嵩明县',
    '禄劝彝族苗族自治县': '禄劝彝族苗族自治县',
    '寻甸回族彝族自治县': '寻甸回族彝族自治县',
    '安宁市': '安宁市'
  }, 530300: {
    '曲靖市市辖区': '曲靖市市辖区',
    '麒麟区': '麒麟区',
    '沾益区': '沾益区',
    '马龙区': '马龙区',
    '陆良县': '陆良县',
    '师宗县': '师宗县',
    '罗平县': '罗平县',
    '富源县': '富源县',
    '会泽县': '会泽县',
    '宣威市': '宣威市'
  }, 530400: {
    '玉溪市市辖区': '玉溪市市辖区',
    '红塔区': '红塔区',
    '江川区': '江川区',
    '澄江县': '澄江县',
    '通海县': '通海县',
    '华宁县': '华宁县',
    '易门县': '易门县',
    '峨山彝族自治县': '峨山彝族自治县',
    '新平彝族傣族自治县': '新平彝族傣族自治县',
    '元江哈尼族彝族傣族自治县': '元江哈尼族彝族傣族自治县'
  }, 530500: {
    '保山市市辖区': '保山市市辖区',
    '隆阳区': '隆阳区',
    '施甸县': '施甸县',
    '龙陵县': '龙陵县',
    '昌宁县': '昌宁县',
    '腾冲市': '腾冲市'
  }, 530600: {
    '昭通市市辖区': '昭通市市辖区',
    '昭阳区': '昭阳区',
    '鲁甸县': '鲁甸县',
    '巧家县': '巧家县',
    '盐津县': '盐津县',
    '大关县': '大关县',
    '永善县': '永善县',
    '绥江县': '绥江县',
    '镇雄县': '镇雄县',
    '彝良县': '彝良县',
    '威信县': '威信县',
    '水富市': '水富市'
  }, 530700: {
    '丽江市市辖区': '丽江市市辖区',
    '古城区': '古城区',
    '玉龙纳西族自治县': '玉龙纳西族自治县',
    '永胜县': '永胜县',
    '华坪县': '华坪县',
    '宁蒗彝族自治县': '宁蒗彝族自治县'
  }, 530800: {
    '普洱市市辖区': '普洱市市辖区',
    '思茅区': '思茅区',
    '宁洱哈尼族彝族自治县': '宁洱哈尼族彝族自治县',
    '墨江哈尼族自治县': '墨江哈尼族自治县',
    '景东彝族自治县': '景东彝族自治县',
    '景谷傣族彝族自治县': '景谷傣族彝族自治县',
    '镇沅彝族哈尼族拉祜族自治县': '镇沅彝族哈尼族拉祜族自治县',
    '江城哈尼族彝族自治县': '江城哈尼族彝族自治县',
    '孟连傣族拉祜族佤族自治县': '孟连傣族拉祜族佤族自治县',
    '澜沧拉祜族自治县': '澜沧拉祜族自治县',
    '西盟佤族自治县': '西盟佤族自治县'
  }, 530900: {
    '临沧市市辖区': '临沧市市辖区',
    '临翔区': '临翔区',
    '凤庆县': '凤庆县',
    '云县': '云县',
    '永德县': '永德县',
    '镇康县': '镇康县',
    '双江拉祜族佤族布朗族傣族自治县': '双江拉祜族佤族布朗族傣族自治县',
    '耿马傣族佤族自治县': '耿马傣族佤族自治县',
    '沧源佤族自治县': '沧源佤族自治县'
  }, 532300: {
    '楚雄市': '楚雄市',
    '双柏县': '双柏县',
    '牟定县': '牟定县',
    '南华县': '南华县',
    '姚安县': '姚安县',
    '大姚县': '大姚县',
    '永仁县': '永仁县',
    '元谋县': '元谋县',
    '武定县': '武定县',
    '禄丰县': '禄丰县'
  }, 532500: {
    '个旧市': '个旧市',
    '开远市': '开远市',
    '蒙自市': '蒙自市',
    '弥勒市': '弥勒市',
    '屏边苗族自治县': '屏边苗族自治县',
    '建水县': '建水县',
    '石屏县': '石屏县',
    '泸西县': '泸西县',
    '元阳县': '元阳县',
    '红河县': '红河县',
    '金平苗族瑶族傣族自治县': '金平苗族瑶族傣族自治县',
    '绿春县': '绿春县',
    '河口瑶族自治县': '河口瑶族自治县'
  }, 532600: {
    '文山市': '文山市',
    '砚山县': '砚山县',
    '西畴县': '西畴县',
    '麻栗坡县': '麻栗坡县',
    '马关县': '马关县',
    '丘北县': '丘北县',
    '广南县': '广南县',
    '富宁县': '富宁县'
  }, 532800: {
    '景洪市': '景洪市',
    '勐海县': '勐海县',
    '勐腊县': '勐腊县'
  }, 532900: {
    '大理市': '大理市',
    '漾濞彝族自治县': '漾濞彝族自治县',
    '祥云县': '祥云县',
    '宾川县': '宾川县',
    '弥渡县': '弥渡县',
    '南涧彝族自治县': '南涧彝族自治县',
    '巍山彝族回族自治县': '巍山彝族回族自治县',
    '永平县': '永平县',
    '云龙县': '云龙县',
    '洱源县': '洱源县',
    '剑川县': '剑川县',
    '鹤庆县': '鹤庆县'
  }, 533100: {
    '瑞丽市': '瑞丽市',
    '芒市': '芒市',
    '梁河县': '梁河县',
    '盈江县': '盈江县',
    '陇川县': '陇川县'
  }, 533300: {
    '泸水市': '泸水市',
    '福贡县': '福贡县',
    '贡山独龙族怒族自治县': '贡山独龙族怒族自治县',
    '兰坪白族普米族自治县': '兰坪白族普米族自治县'
  }, 533400: {
    '香格里拉市': '香格里拉市',
    '德钦县': '德钦县',
    '维西傈僳族自治县': '维西傈僳族自治县'
  }, 540100: {
    '拉萨市市辖区': '拉萨市市辖区',
    '城关区': '城关区',
    '堆龙德庆区': '堆龙德庆区',
    '达孜区': '达孜区',
    '林周县': '林周县',
    '当雄县': '当雄县',
    '尼木县': '尼木县',
    '曲水县': '曲水县',
    '墨竹工卡县': '墨竹工卡县'
  }, 540200: {
    '桑珠孜区': '桑珠孜区',
    '南木林县': '南木林县',
    '江孜县': '江孜县',
    '定日县': '定日县',
    '萨迦县': '萨迦县',
    '拉孜县': '拉孜县',
    '昂仁县': '昂仁县',
    '谢通门县': '谢通门县',
    '白朗县': '白朗县',
    '仁布县': '仁布县',
    '康马县': '康马县',
    '定结县': '定结县',
    '仲巴县': '仲巴县',
    '亚东县': '亚东县',
    '吉隆县': '吉隆县',
    '聂拉木县': '聂拉木县',
    '萨嘎县': '萨嘎县',
    '岗巴县': '岗巴县'
  }, 540300: {
    '卡若区': '卡若区',
    '江达县': '江达县',
    '贡觉县': '贡觉县',
    '类乌齐县': '类乌齐县',
    '丁青县': '丁青县',
    '察雅县': '察雅县',
    '八宿县': '八宿县',
    '左贡县': '左贡县',
    '芒康县': '芒康县',
    '洛隆县': '洛隆县',
    '边坝县': '边坝县'
  }, 540400: {
    '巴宜区': '巴宜区',
    '工布江达县': '工布江达县',
    '米林县': '米林县',
    '墨脱县': '墨脱县',
    '波密县': '波密县',
    '察隅县': '察隅县',
    '朗县': '朗县'
  }, 540500: {
    '乃东区': '乃东区',
    '扎囊县': '扎囊县',
    '贡嘎县': '贡嘎县',
    '桑日县': '桑日县',
    '琼结县': '琼结县',
    '曲松县': '曲松县',
    '措美县': '措美县',
    '洛扎县': '洛扎县',
    '加查县': '加查县',
    '隆子县': '隆子县',
    '错那县': '错那县',
    '浪卡子县': '浪卡子县'
  }, 540600: {
    '色尼区': '色尼区',
    '嘉黎县': '嘉黎县',
    '比如县': '比如县',
    '聂荣县': '聂荣县',
    '安多县': '安多县',
    '申扎县': '申扎县',
    '索县': '索县',
    '班戈县': '班戈县',
    '巴青县': '巴青县',
    '尼玛县': '尼玛县',
    '双湖县': '双湖县'
  }, 542500: {
    '普兰县': '普兰县',
    '札达县': '札达县',
    '噶尔县': '噶尔县',
    '日土县': '日土县',
    '革吉县': '革吉县',
    '改则县': '改则县',
    '措勤县': '措勤县'
  }, 610100: {
    '西安市市辖区': '西安市市辖区',
    '新城区': '新城区',
    '碑林区': '碑林区',
    '莲湖区': '莲湖区',
    '灞桥区': '灞桥区',
    '未央区': '未央区',
    '雁塔区': '雁塔区',
    '阎良区': '阎良区',
    '临潼区': '临潼区',
    '长安区': '长安区',
    '高陵区': '高陵区',
    '鄠邑区': '鄠邑区',
    '蓝田县': '蓝田县',
    '周至县': '周至县'
  }, 610200: {
    '铜川市市辖区': '铜川市市辖区',
    '王益区': '王益区',
    '印台区': '印台区',
    '耀州区': '耀州区',
    '宜君县': '宜君县'
  }, 610300: {
    '宝鸡市市辖区': '宝鸡市市辖区',
    '渭滨区': '渭滨区',
    '金台区': '金台区',
    '陈仓区': '陈仓区',
    '凤翔县': '凤翔县',
    '岐山县': '岐山县',
    '扶风县': '扶风县',
    '眉县': '眉县',
    '陇县': '陇县',
    '千阳县': '千阳县',
    '麟游县': '麟游县',
    '凤县': '凤县',
    '太白县': '太白县'
  }, 610400: {
    '咸阳市市辖区': '咸阳市市辖区',
    '秦都区': '秦都区',
    '杨陵区': '杨陵区',
    '渭城区': '渭城区',
    '三原县': '三原县',
    '泾阳县': '泾阳县',
    '乾县': '乾县',
    '礼泉县': '礼泉县',
    '永寿县': '永寿县',
    '彬州市': '彬州市',
    '长武县': '长武县',
    '旬邑县': '旬邑县',
    '淳化县': '淳化县',
    '武功县': '武功县',
    '兴平市': '兴平市'
  }, 610500: {
    '渭南市市辖区': '渭南市市辖区',
    '临渭区': '临渭区',
    '华州区': '华州区',
    '潼关县': '潼关县',
    '大荔县': '大荔县',
    '合阳县': '合阳县',
    '澄城县': '澄城县',
    '蒲城县': '蒲城县',
    '白水县': '白水县',
    '富平县': '富平县',
    '韩城市': '韩城市',
    '华阴市': '华阴市'
  }, 610600: {
    '延安市市辖区': '延安市市辖区',
    '宝塔区': '宝塔区',
    '安塞区': '安塞区',
    '延长县': '延长县',
    '延川县': '延川县',
    '子长县': '子长县',
    '志丹县': '志丹县',
    '吴起县': '吴起县',
    '甘泉县': '甘泉县',
    '富县': '富县',
    '洛川县': '洛川县',
    '宜川县': '宜川县',
    '黄龙县': '黄龙县',
    '黄陵县': '黄陵县'
  }, 610700: {
    '汉中市市辖区': '汉中市市辖区',
    '汉台区': '汉台区',
    '南郑区': '南郑区',
    '城固县': '城固县',
    '洋县': '洋县',
    '西乡县': '西乡县',
    '勉县': '勉县',
    '宁强县': '宁强县',
    '略阳县': '略阳县',
    '镇巴县': '镇巴县',
    '留坝县': '留坝县',
    '佛坪县': '佛坪县'
  }, 610800: {
    '榆林市市辖区': '榆林市市辖区',
    '榆阳区': '榆阳区',
    '横山区': '横山区',
    '府谷县': '府谷县',
    '靖边县': '靖边县',
    '定边县': '定边县',
    '绥德县': '绥德县',
    '米脂县': '米脂县',
    '佳县': '佳县',
    '吴堡县': '吴堡县',
    '清涧县': '清涧县',
    '子洲县': '子洲县',
    '神木市': '神木市'
  }, 610900: {
    '安康市市辖区': '安康市市辖区',
    '汉滨区': '汉滨区',
    '汉阴县': '汉阴县',
    '石泉县': '石泉县',
    '宁陕县': '宁陕县',
    '紫阳县': '紫阳县',
    '岚皋县': '岚皋县',
    '平利县': '平利县',
    '镇坪县': '镇坪县',
    '旬阳县': '旬阳县',
    '白河县': '白河县'
  }, 611000: {
    '商洛市市辖区': '商洛市市辖区',
    '商州区': '商州区',
    '洛南县': '洛南县',
    '丹凤县': '丹凤县',
    '商南县': '商南县',
    '山阳县': '山阳县',
    '镇安县': '镇安县',
    '柞水县': '柞水县'
  }, 620100: {
    '兰州市市辖区': '兰州市市辖区',
    '城关区': '城关区',
    '七里河区': '七里河区',
    '西固区': '西固区',
    '安宁区': '安宁区',
    '红古区': '红古区',
    '永登县': '永登县',
    '皋兰县': '皋兰县',
    '榆中县': '榆中县'
  }, 620200: {
    '嘉峪关市市辖区': '嘉峪关市市辖区'
  }, 620300: {
    '金昌市市辖区': '金昌市市辖区',
    '金川区': '金川区',
    '永昌县': '永昌县'
  }, 620400: {
    '白银市市辖区': '白银市市辖区',
    '白银区': '白银区',
    '平川区': '平川区',
    '靖远县': '靖远县',
    '会宁县': '会宁县',
    '景泰县': '景泰县'
  }, 620500: {
    '天水市市辖区': '天水市市辖区',
    '秦州区': '秦州区',
    '麦积区': '麦积区',
    '清水县': '清水县',
    '秦安县': '秦安县',
    '甘谷县': '甘谷县',
    '武山县': '武山县',
    '张家川回族自治县': '张家川回族自治县'
  }, 620600: {
    '武威市市辖区': '武威市市辖区',
    '凉州区': '凉州区',
    '民勤县': '民勤县',
    '古浪县': '古浪县',
    '天祝藏族自治县': '天祝藏族自治县'
  }, 620700: {
    '张掖市市辖区': '张掖市市辖区',
    '甘州区': '甘州区',
    '肃南裕固族自治县': '肃南裕固族自治县',
    '民乐县': '民乐县',
    '临泽县': '临泽县',
    '高台县': '高台县',
    '山丹县': '山丹县'
  }, 620800: {
    '平凉市市辖区': '平凉市市辖区',
    '崆峒区': '崆峒区',
    '泾川县': '泾川县',
    '灵台县': '灵台县',
    '崇信县': '崇信县',
    '华亭市': '华亭市',
    '庄浪县': '庄浪县',
    '静宁县': '静宁县'
  }, 620900: {
    '酒泉市市辖区': '酒泉市市辖区',
    '肃州区': '肃州区',
    '金塔县': '金塔县',
    '瓜州县': '瓜州县',
    '肃北蒙古族自治县': '肃北蒙古族自治县',
    '阿克塞哈萨克族自治县': '阿克塞哈萨克族自治县',
    '玉门市': '玉门市',
    '敦煌市': '敦煌市'
  }, 621000: {
    '庆阳市市辖区': '庆阳市市辖区',
    '西峰区': '西峰区',
    '庆城县': '庆城县',
    '环县': '环县',
    '华池县': '华池县',
    '合水县': '合水县',
    '正宁县': '正宁县',
    '宁县': '宁县',
    '镇原县': '镇原县'
  }, 621100: {
    '定西市市辖区': '定西市市辖区',
    '安定区': '安定区',
    '通渭县': '通渭县',
    '陇西县': '陇西县',
    '渭源县': '渭源县',
    '临洮县': '临洮县',
    '漳县': '漳县',
    '岷县': '岷县'
  }, 621200: {
    '陇南市市辖区': '陇南市市辖区',
    '武都区': '武都区',
    '成县': '成县',
    '文县': '文县',
    '宕昌县': '宕昌县',
    '康县': '康县',
    '西和县': '西和县',
    '礼县': '礼县',
    '徽县': '徽县',
    '两当县': '两当县'
  }, 622900: {
    '临夏市': '临夏市',
    '临夏县': '临夏县',
    '康乐县': '康乐县',
    '永靖县': '永靖县',
    '广河县': '广河县',
    '和政县': '和政县',
    '东乡族自治县': '东乡族自治县',
    '积石山保安族东乡族撒拉族自治县': '积石山保安族东乡族撒拉族自治县'
  }, 623000: {
    '合作市': '合作市',
    '临潭县': '临潭县',
    '卓尼县': '卓尼县',
    '舟曲县': '舟曲县',
    '迭部县': '迭部县',
    '玛曲县': '玛曲县',
    '碌曲县': '碌曲县',
    '夏河县': '夏河县'
  }, 630100: {
    '西宁市市辖区': '西宁市市辖区',
    '城东区': '城东区',
    '城中区': '城中区',
    '城西区': '城西区',
    '城北区': '城北区',
    '大通回族土族自治县': '大通回族土族自治县',
    '湟中县': '湟中县',
    '湟源县': '湟源县'
  }, 630200: {
    '乐都区': '乐都区',
    '平安区': '平安区',
    '民和回族土族自治县': '民和回族土族自治县',
    '互助土族自治县': '互助土族自治县',
    '化隆回族自治县': '化隆回族自治县',
    '循化撒拉族自治县': '循化撒拉族自治县'
  }, 632200: {
    '门源回族自治县': '门源回族自治县',
    '祁连县': '祁连县',
    '海晏县': '海晏县',
    '刚察县': '刚察县'
  }, 632300: {
    '同仁县': '同仁县',
    '尖扎县': '尖扎县',
    '泽库县': '泽库县',
    '河南蒙古族自治县': '河南蒙古族自治县'
  }, 632500: {
    '共和县': '共和县',
    '同德县': '同德县',
    '贵德县': '贵德县',
    '兴海县': '兴海县',
    '贵南县': '贵南县'
  }, 632600: {
    '玛沁县': '玛沁县',
    '班玛县': '班玛县',
    '甘德县': '甘德县',
    '达日县': '达日县',
    '久治县': '久治县',
    '玛多县': '玛多县'
  }, 632700: {
    '玉树市': '玉树市',
    '杂多县': '杂多县',
    '称多县': '称多县',
    '治多县': '治多县',
    '囊谦县': '囊谦县',
    '曲麻莱县': '曲麻莱县'
  }, 632800: {
    '格尔木市': '格尔木市',
    '德令哈市': '德令哈市',
    '茫崖市': '茫崖市',
    '乌兰县': '乌兰县',
    '都兰县': '都兰县',
    '天峻县': '天峻县',
    '海西蒙古族藏族自治州直辖': '海西蒙古族藏族自治州直辖'
  }, 640100: {
    '银川市市辖区': '银川市市辖区',
    '兴庆区': '兴庆区',
    '西夏区': '西夏区',
    '金凤区': '金凤区',
    '永宁县': '永宁县',
    '贺兰县': '贺兰县',
    '灵武市': '灵武市'
  }, 640200: {
    '石嘴山市市辖区': '石嘴山市市辖区',
    '大武口区': '大武口区',
    '惠农区': '惠农区',
    '平罗县': '平罗县'
  }, 640300: {
    '吴忠市市辖区': '吴忠市市辖区',
    '利通区': '利通区',
    '红寺堡区': '红寺堡区',
    '盐池县': '盐池县',
    '同心县': '同心县',
    '青铜峡市': '青铜峡市'
  }, 640400: {
    '固原市市辖区': '固原市市辖区',
    '原州区': '原州区',
    '西吉县': '西吉县',
    '隆德县': '隆德县',
    '泾源县': '泾源县',
    '彭阳县': '彭阳县'
  }, 640500: {
    '中卫市市辖区': '中卫市市辖区',
    '沙坡头区': '沙坡头区',
    '中宁县': '中宁县',
    '海原县': '海原县'
  }, 650100: {
    '乌鲁木齐市市辖区': '乌鲁木齐市市辖区',
    '天山区': '天山区',
    '沙依巴克区': '沙依巴克区',
    '新市区': '新市区',
    '水磨沟区': '水磨沟区',
    '头屯河区': '头屯河区',
    '达坂城区': '达坂城区',
    '米东区': '米东区',
    '乌鲁木齐县': '乌鲁木齐县'
  }, 650200: {
    '克拉玛依市市辖区': '克拉玛依市市辖区',
    '独山子区': '独山子区',
    '克拉玛依区': '克拉玛依区',
    '白碱滩区': '白碱滩区',
    '乌尔禾区': '乌尔禾区'
  }, 650400: {
    '高昌区': '高昌区',
    '鄯善县': '鄯善县',
    '托克逊县': '托克逊县'
  }, 650500: {
    '伊州区': '伊州区',
    '巴里坤哈萨克自治县': '巴里坤哈萨克自治县',
    '伊吾县': '伊吾县'
  }, 652300: {
    '昌吉市': '昌吉市',
    '阜康市': '阜康市',
    '呼图壁县': '呼图壁县',
    '玛纳斯县': '玛纳斯县',
    '奇台县': '奇台县',
    '吉木萨尔县': '吉木萨尔县',
    '木垒哈萨克自治县': '木垒哈萨克自治县'
  }, 652700: {
    '博乐市': '博乐市',
    '阿拉山口市': '阿拉山口市',
    '精河县': '精河县',
    '温泉县': '温泉县'
  }, 652800: {
    '库尔勒市': '库尔勒市',
    '轮台县': '轮台县',
    '尉犁县': '尉犁县',
    '若羌县': '若羌县',
    '且末县': '且末县',
    '焉耆回族自治县': '焉耆回族自治县',
    '和静县': '和静县',
    '和硕县': '和硕县',
    '博湖县': '博湖县'
  }, 652900: {
    '阿克苏市': '阿克苏市',
    '温宿县': '温宿县',
    '库车县': '库车县',
    '沙雅县': '沙雅县',
    '新和县': '新和县',
    '拜城县': '拜城县',
    '乌什县': '乌什县',
    '阿瓦提县': '阿瓦提县',
    '柯坪县': '柯坪县'
  }, 653000: {
    '阿图什市': '阿图什市',
    '阿克陶县': '阿克陶县',
    '阿合奇县': '阿合奇县',
    '乌恰县': '乌恰县'
  }, 653100: {
    '喀什市': '喀什市',
    '疏附县': '疏附县',
    '疏勒县': '疏勒县',
    '英吉沙县': '英吉沙县',
    '泽普县': '泽普县',
    '莎车县': '莎车县',
    '叶城县': '叶城县',
    '麦盖提县': '麦盖提县',
    '岳普湖县': '岳普湖县',
    '伽师县': '伽师县',
    '巴楚县': '巴楚县',
    '塔什库尔干塔吉克自治县': '塔什库尔干塔吉克自治县'
  }, 653200: {
    '和田市': '和田市',
    '和田县': '和田县',
    '墨玉县': '墨玉县',
    '皮山县': '皮山县',
    '洛浦县': '洛浦县',
    '策勒县': '策勒县',
    '于田县': '于田县',
    '民丰县': '民丰县'
  }, 654000: {
    '伊宁市': '伊宁市',
    '奎屯市': '奎屯市',
    '霍尔果斯市': '霍尔果斯市',
    '伊宁县': '伊宁县',
    '察布查尔锡伯自治县': '察布查尔锡伯自治县',
    '霍城县': '霍城县',
    '巩留县': '巩留县',
    '新源县': '新源县',
    '昭苏县': '昭苏县',
    '特克斯县': '特克斯县',
    '尼勒克县': '尼勒克县'
  }, 654200: {
    '塔城市': '塔城市',
    '乌苏市': '乌苏市',
    '额敏县': '额敏县',
    '沙湾县': '沙湾县',
    '托里县': '托里县',
    '裕民县': '裕民县',
    '和布克赛尔蒙古自治县': '和布克赛尔蒙古自治县'
  }, 654300: {
    '阿勒泰市': '阿勒泰市',
    '布尔津县': '布尔津县',
    '富蕴县': '富蕴县',
    '福海县': '福海县',
    '哈巴河县': '哈巴河县',
    '青河县': '青河县',
    '吉木乃县': '吉木乃县'
  }
}

//图表系统色系
screenConstants.systemChartColorsNames = [
  {
    "name": "粉青",
    "value":"chalk"
  },
  {
    "name": "暗淡",
    "value":"customed"
  },
  {
    "name": "明亮",
    "value":"dark"
  },
  {
    "name": "橘红",
    "value":"essos"
  },
  {
    "name": "马卡龙",
    "value":"macarons"
  },
  {
    "name": "深紫",
    "value":"purplepassion"
  },
  {
    "name": "罗马红",
    "value":"roma"
  },
  {
    "name": "深色",
    "value":"shine"
  },
  {
    "name": "复古",
    "value":"vintage",
  },
  {
    "name": "蓝绿",
    "value":"walden"
  },
  {
    "name": "灰粉",
    "value":"westeros"
  },
  {
    "name": "青草",
    "value":"wonderland"
  }
]

screenConstants.systemChartColors = {
  chalk:["#fc97af","#87f7cf","#f7f494","#72ccff","#f7c5a0","#d4a4eb","#d2f5a6","#76f2f2"],
  customed:["#5470c6","#91cc75","#fac858","#ee6666","#73c0de","#3ba272","#fc8452","#9a60b4","#ea7ccc"],
  dark:["#4992ff","#7cffb2","#fddd60","#ff6e76","#58d9f9","#05c091","#ff8a45","#8d48e3","#dd79ff"],
  essos:["#893448","#d95850","#eb8146","#ffb248","#f2d643","#ebdba4"],
  macarons:["#2ec7c9","#b6a2de","#5ab1ef","#ffb980","#d87a80","#8d98b3","#e5cf0d","#97b552","#95706d","#dc69aa","#07a2a4","#9a7fd1","#588dd5","#f5994e","#c05050","#59678c","#c9ab00","#7eb00a","#6f5553","#c14089"],
  purplepassion:["#9b8bba","#e098c7","#8fd3e8","#71669e","#cc70af","#7cb4cc"],
  roma:["#e01f54","#5e4ea5","#f5e8c8","#b8d2c7","#c6b38e","#a4d8c2","#f3d999","#d3758f","#dcc392","#2e4783","#82b6e9","#ff6347","#a092f1","#0a915d","#eaf889","#6699FF","#ff6666","#3cb371","#d5b158","#38b6b6"],
  shine:["#c12e34","#e6b600","#0098d9","#2b821d","#005eaa","#339ca8","#cda819","#32a487"],
  vintage:["#d87c7c","#919e8b","#d7ab82","#6e7074","#61a0a8","#efa18d","#787464","#cc7e63","#724e58","#4b565b"],
  walden:["#3fb1e3","#6be6c1","#626c91","#a0a7e6","#c4ebad","#96dee8"],
  westeros:["#516b91","#59c4e6","#edafda","#93b7e3","#a5e7f0","#cbb0e3"],
  wonderland:["#4ea397","#22c3aa","#7bd9a5","#d0648a","#f58db2","#f2b3c9"]
}

screenConstants.pieLoopanimation = {
  pie: [
    {
      loop: true,
      startTime: 800,
      oneByOne: true,
      timeSlices: [
        {
          effects: {
            channel: {
              fillOpacity: { to: 1 },
              outerRadius: {
                from: (...p) => {
                  return p[1].graphicItem.attribute.outerRadius;
                },
                to: (...p) => {
                  return p[1].graphicItem.attribute.outerRadius + 10;
                }
              }
            }
          },
          duration: 500
        },
        {
          effects: {
            channel: {
              fillOpacity: { to: 1 },
              outerRadius: {
                from: (...p) => {
                  return p[1].graphicItem.attribute.outerRadius;
                },
                to: (...p) => {
                  return p[1].graphicItem.attribute.outerRadius;
                }
              }
            },
            easing: 'linear'
          },
          duration: 500
        },
        {
          effects: {
            channel: {
              fillOpacity: { to: 1 },
            }
          },
          duration: 500
        }
      ]
    }
  ]
}


screenConstants.mapDrillType = [
  { name: '地图下钻', value: '1' },
  { name: '跳转链接', value: '2' },
]

screenConstants.chartDrillType = [
  { name: '跳转链接', value: '2' },
]

export default screenConstants