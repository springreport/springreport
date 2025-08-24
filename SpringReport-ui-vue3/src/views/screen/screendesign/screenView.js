import { allThemeMap } from '@visactor/vchart-theme';
import veODesignLight from '@visactor/vchart-theme/public/veODesignLight.json';
import veODesignLightFinance from '@visactor/vchart-theme/public/veODesignLightFinance.json';
import veODesignLightGovernment from '@visactor/vchart-theme/public/veODesignLightGovernment.json';
import veODesignLightConsumer from '@visactor/vchart-theme/public/veODesignLightConsumer.json';
import veODesignLightAutomobile from '@visactor/vchart-theme/public/veODesignLightAutomobile.json';
import veODesignLightCulturalTourism from '@visactor/vchart-theme/public/veODesignLightCulturalTourism.json';
import veODesignLightMedical from '@visactor/vchart-theme/public/veODesignLightMedical.json';
import veODesignLightNewEnergy from '@visactor/vchart-theme/public/veODesignLightNewEnergy.json';
import veODesignDark from '@visactor/vchart-theme/public/veODesignDark.json';
import veODesignDarkFinance from '@visactor/vchart-theme/public/veODesignDarkFinance.json';
import veODesignDarkGovernment from '@visactor/vchart-theme/public/veODesignDarkGovernment.json';
import veODesignDarkConsumer from '@visactor/vchart-theme/public/veODesignDarkConsumer.json';
import veODesignDarkAutomobile from '@visactor/vchart-theme/public/veODesignDarkAutomobile.json';
import veODesignDarkCulturalTourism from '@visactor/vchart-theme/public/veODesignDarkCulturalTourism.json';
import veODesignDarkMedical from '@visactor/vchart-theme/public/veODesignDarkMedical.json';
import veODesignDarkNewEnergy from '@visactor/vchart-theme/public/veODesignDarkNewEnergy.json';
import VChart from '@visactor/vchart';
import { registerLiquidChart } from '@visactor/vchart';
export default {
  data() {
    return {
      screenProperties: {
        // 大屏属性
        name: '', // 名称
        id: '',
        width: 1920,
        height: 1080,
        imgUrl: '',
        background: '#0D2942',
        type: 'panel',
        category: 'panel',
        scale: 1,
        offsetX: 0,
        offsetY: 0,
      },
      components: [], //组件
      chartsComponents: {}, //图表 key id，value：charts
      sendRequest: false,
      scaleTimer: null,
      searchData: {
        params: []
      }, 
      searchHandle: [
        { label: '查询', drawerBtn: true, icon: 'el-icon-search', type: 'primary', handle: () => this.refreshComponentData(), size: 'mini' },
        { label: '重置', drawerBtn: true, icon: 'el-icon-refresh-left', type: '', handle: () => this.refreshPage(), size: 'mini' },
      ],
      drawer:false,
      viewThat:null
    };
  },
  mounted() {
    this.viewThat = this;
    if (this.$route.query.sendRequest == 1) {
      this.sendRequest = true;
    } else {
      this.sendRequest = false;
    }
    registerLiquidChart();
    allThemeMap.forEach((theme, name) => {
      VChart.ThemeManager.registerTheme(name, theme);
    });
    VChart.ThemeManager.registerTheme('veODesignLight', veODesignLight);
    VChart.ThemeManager.registerTheme('veODesignLightFinance', veODesignLightFinance);
    VChart.ThemeManager.registerTheme('veODesignLightGovernment', veODesignLightGovernment);
    VChart.ThemeManager.registerTheme('veODesignLightConsumer', veODesignLightConsumer);
    VChart.ThemeManager.registerTheme('veODesignLightAutomobile', veODesignLightAutomobile);
    VChart.ThemeManager.registerTheme(
      'veODesignLightCulturalTourism',
      veODesignLightCulturalTourism
    );
    VChart.ThemeManager.registerTheme('veODesignLightMedical', veODesignLightMedical);
    VChart.ThemeManager.registerTheme('veODesignLightNewEnergy', veODesignLightNewEnergy);
    VChart.ThemeManager.registerTheme('veODesignDark', veODesignDark);
    VChart.ThemeManager.registerTheme('veODesignDarkFinance', veODesignDarkFinance);
    VChart.ThemeManager.registerTheme('veODesignDarkGovernment', veODesignDarkGovernment);
    VChart.ThemeManager.registerTheme('veODesignDarkConsumer', veODesignDarkConsumer);
    VChart.ThemeManager.registerTheme('veODesignDarkAutomobile', veODesignDarkAutomobile);
    VChart.ThemeManager.registerTheme('veODesignDarkCulturalTourism', veODesignDarkCulturalTourism);
    VChart.ThemeManager.registerTheme('veODesignDarkMedical', veODesignDarkMedical);
    VChart.ThemeManager.registerTheme('veODesignDarkNewEnergy', veODesignDarkNewEnergy);
    this.getScreenDesign();
    window.addEventListener('resize', this.setScale);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.setScale);
  },
  methods: {
    //获取大屏设计信息
    getScreenDesign() {
      const tplId = this.$route.query.tplId;
      const obj = {
        params: { id: tplId },
        url: this.apis.screenTpl.getScreenDesignApi,
      };
      var that = this;
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code === '200') {
          that.screenProperties.name = response.responseData.tplName;
          that.screenProperties.id = response.responseData.id;
          that.screenProperties.width = response.responseData.width;
          that.screenProperties.height = response.responseData.height;
          that.screenProperties.imgUrl = response.responseData.imgUrl;
          that.screenProperties.background = response.responseData.background;
          that.screenProperties.imgType = response.responseData.imgType;
          that.screenProperties.imgName = response.responseData.imgName;
          that.activated = that.screenProperties;
          that.setScale();
          let params = [];
          let paramsCode = {};
          let tempComponent = [];
          if (
            response.responseData.components != null &&
            response.responseData.components.length > 0
          ) {
            for (let index = 0; index < response.responseData.components.length; index++) {
              const element = response.responseData.components[index];
              const component = JSON.parse(element.content);
              component.locked = true;
              tempComponent.push(component)
              if(component.params && component.params.length > 0){
                for (let index = 0; index < component.params.length; index++) {
                  const paramElement = component.params[index];
                  let paramCode = paramElement.paramCode;
                  if(!paramsCode[paramCode]){
                    let paramDefault = paramElement.paramDefault;
                    if (this.$route.query[paramCode]) {
                      paramElement[paramCode] = this.$route.query[paramCode]
                    }else if(paramDefault){
                      paramElement[paramCode] = paramDefault
                    }
                    params.push(paramElement);
                    paramsCode[paramCode] = '1';
                    let paramType = paramElement.paramType;
                    if (paramType == 'mutiselect' || paramType == 'multiTreeSelect') {
                      var data = new Array()
                      if(paramElement[paramCode]){
                        data = paramElement[paramCode].split(',')
                      }
                      if(paramType == 'mutiselect'){
                        if(data && data.length > 0){
                          that.getSelectData(paramElement);
                        }
                      }else{
                        if(data && data.length > 0){
                          that.getTreeData(paramElement,{"params":params});
                        }
                      }
                      paramElement[paramCode] = data
                    }else if(paramType == 'select'){
                      if(paramElement[paramCode]){
                        if(paramElement.isRelyOnParams == 1){
                          that.getRelyOnParamys(paramElement,{"params":params},paramElement);
                        }else{
                          that.getSelectData(paramElement);
                        }
                      }
                    }else if(paramType == 'treeSelect'){
                      if(paramElement[paramCode]){
                        that.getTreeData(paramElement,{"params":params});
                      }
                    }else if(paramType == 'date'){
                      // paramDefault
                      if(paramDefault != null && paramDefault != ''){
                        if (!this.$route.query[paramCode]) {
                          let value = that.commonUtil.getDefaultDateValue(paramElement);
                          paramElement[paramCode] = value;
                        }
                      }
                    }
                  }
                }
              }
            }
            var dataSet = {}
            dataSet.datasetId = 0
            dataSet.datasetName = '组件参数'
            dataSet.datasourceId = 0
            dataSet.params = params
            that.searchData.params.push(dataSet)
            that.components = tempComponent;
            that.$nextTick(() => {
              that.initComponent();
            });
          }
        }
      });
    },
    // 设置缩放
    setScale() {
      if (this.scaleTimer) {
             clearTimeout(this.scaleTimer)
           }
           this.scaleTimer = setTimeout(() => {
             const designWidth = this.screenProperties.width
             const designHeight = this.screenProperties.height
             const scaleX = window.innerWidth / designWidth
             const scaleY = window.innerHeight / designHeight
             const scale = Math.min(scaleX, scaleY)
             const offsetX = (window.innerWidth - designWidth * scale) / 2
             const offsetY = (window.innerHeight - designHeight * scale) / 2
             this.screenProperties.scale = scale
             this.screenProperties.offsetX = offsetX/scale
             this.screenProperties.offsetY = offsetY/scale
           }, 100) // 100可以替换为50，如果还嫌慢 直接去掉setTimeout延时器也行，本意是防抖
    },
    //组件初始化
    async initComponent() {
      for (let index = 0; index < this.components.length; index++) {
        const element = this.components[index];
        element.active = false
        if (element.category == this.screenConstants.category.vchart) {
          if (element.type.toLowerCase().indexOf('scattermap') >= 0) {
              let mapCode = element.spec.series[0].map
              if (!VChart.getMap(mapCode)) {
                  const geojson = await this.commonUtil.getMapData(mapCode)
                  VChart.registerMap(mapCode, geojson)
              }
          }
          else if (element.type.toLowerCase().indexOf('basicmap') >= 0) {
            let mapCode = element.spec.map;
            if (!VChart.getMap(mapCode)) {
              const geojson = await this.commonUtil.getMapData(mapCode);
              VChart.registerMap(mapCode, geojson);
            }
          }else if(element.type.toLowerCase().indexOf("pie")>=0){
            if(element.spec.isLoop){
                element.spec.animationNormal = this.screenConstants.pieLoopanimation
            }
          }else if(element.type.toLowerCase().indexOf("circlepacking")>=0){
            let categoryField = element.spec.categoryField;
            let valueField = element.spec.valueField;
            element.spec.label.style.text = datum => [`${datum[categoryField]}`, `${datum[valueField]}`];
          }else if(element.type.toLowerCase().indexOf("sankey") >= 0){
            element.spec.nodeKey = datum => datum.name;
        }
          var obj = { dom: element.id };
          if (element.theme) {
            obj.theme = element.theme;
          }
          const vchart = new VChart(element.spec, obj);
          // 绘制
          vchart.renderSync();
          var that = this;
          if(element.type == "basicMap" || element.type == "scatterMap"){
            if(element.isDrill){
              vchart.on('click', (params) => {
                  that.commonUtil.mapDrill(that.chartsComponents,element,params,that.sendRequest,that);
              })
            }
          }else{
            vchart.on('click', (params) => {
                  that.commonUtil.chartDrill(that.chartsComponents,element,params);
              })
          }
          this.chartsComponents[element.id] = vchart;
        } else if (element.category == this.screenConstants.category.text) {
          if (element.type == this.screenConstants.type.date) {
            element.content = '';
            // setInterval(() => {
            //     const self = this
            //     setTimeout(function() { self.refreshTime(element) }, 0)
            // }, 1000)
          }
        }
      }
    },
    refreshTime(component) {
      component.content = this.commonUtil.getCurrentDate(component);
    },
    showSearch(){
      this.drawer = true;
    },
    closeSearch(){
      this.drawer = false;
    },
    refreshComponentData(){
      for (let index = 0; index < this.components.length; index++) {
        const element = this.components[index];
        if(element.type != "picture"){
          this.$refs['draggable'].$refs[element.id][0].initData((this.searchData.params && this.searchData.params.length>0)?this.searchData.params[0].params:[]);
        }
      }
      this.closeSearch();
    },
    refreshPage(){
      location.reload();
    },
    getSelectData(item) {
      if (!item.init) {
        if (item.selectType == '1') {
          item.selectData = JSON.parse(item.selectContent)
        } else {
          var params = {
            selectContent: item.selectContent,
            datasourceId: item.datasourceId,
            params: {}
          }
          var obj = {
            url: '/api/reportTplDataset/getSelectData',
            params: params
          }
          this.commonUtil.doPost(obj).then((response) => {
            if (response.code == '200') {
              item.selectData = response.responseData
              if (response.responseData && response.responseData.length > 0) {
                item.init = true
              }
            }
          })
        }
      }
    },
    getTreeData(item,data) {
      if (!item.init || item.isRelyOnParams == 1) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {}
        }
        if(item.isRelyOnParams == 1){
          var relyOnValue = null;
          for (let index = 0; index < data.params.length; index++) {
            const element = data.params[index]
            let relyOnParams = item.relyOnParams.split(",");
            for (let t = 0; t < relyOnParams.length; t++) {
              if (relyOnParams[t] && element.hasOwnProperty(relyOnParams[t])) {
                if(relyOnValue == null){
                  relyOnValue = {};
                }
                relyOnValue[relyOnParams[t]] = element[relyOnParams[t]]
              }
            }
          }
          for(var key in relyOnValue) {
            params.params[key] = relyOnValue[key]
          }
        }
        var obj = {
          url: '/api/reportTplDataset/getTreeSelectData',
          params: params
        }
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == '200') {
            item.selectData = response.responseData
            if (response.responseData && response.responseData.length > 0) {
              item.init = true
            }
          }
        })
      }
    },
    getRelyOnParamys(item, data, modelData) {
      var relyOnValue = null;
      for (let index = 0; index < data.params.length; index++) {
        const element = data.params[index]
        let relyOnParams = item.relyOnParams.split(",");
        for (let t = 0; t < relyOnParams.length; t++) {
          if (relyOnParams[t] && element.hasOwnProperty(relyOnParams[t])) {
            if(relyOnValue == null){
              relyOnValue = {};
            }
            relyOnValue[relyOnParams[t]] = element[relyOnParams[t]]
          }
        }
        
      }
      if (relyOnValue) {
        var params = {
          selectContent: item.selectContent,
          datasourceId: item.datasourceId,
          params: {}
        }
        for(var key in relyOnValue) {
          params.params[key] = relyOnValue[key]
        }
        var obj = {
          url: '/api/reportTplDataset/getRelyOnData',
          params: params
        }
        this.commonUtil.doPost(obj).then((response) => {
          if (response.code == '200') {
            item.selectData = response.responseData
          }
        })
      } else {
        item.selectData = []
        modelData[item.paramCode] = null
      }
    },
  },
};
