import vuedraggable from 'vuedraggable';
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
import settings from '../../../components/screen/settings/settings.vue';

import { Vue3RulerTool } from 'vue3-ruler-tool';
import 'vue3-ruler-tool/dist/style.css';
export default {
  data() {
    return {
      components: [], // 组件
      isShowLayer: true, // 是否显示左侧图层
      isShowConf: true, // 是否显示右侧配置面板
      showSubComponents: false, //是否显示子组件列表
      subComponents: [], //子组件列表数据
      scaleList: [200, 150, 120, 100, 95, 90, 85, 80, 75, 70, 65, 60, 50, 40, 30, 20, 10],
      scaleSelected: 75,
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
      },
      chartsComponents: {}, // 图表 key id，value：charts
      activated: {}, // 选中的控件
      isBubblingEvent: false, // 是否是冒泡事件
      timerMap: {},
      x: 0,
      y: 0,
      isCtrl: false, //是否按下ctrl键
      resourceType: 1, // 资源类型 1 组件 2 图层
      comKeyword: '', // 组件搜索关键词
      layerKeyword: '', // 图层搜索关键词
      renameVisible: false, // 是否显示重命名弹窗
      renameForm: {},
      subComponentIndex: 0, // 子组件索引
    };
  },
  computed: {
    // 组件类型
    componentsType() {
      const comArr = this.screenConstants.componentsType1
        .filter((item) => item.type !== 'panel')
        .map((item) => {
          const key = item.type + 'Type';
          const subComponents = this.screenConstants[key] || [];
          return {
            ...item,
            subComponents,
            subComponentsNum: subComponents.length,
          };
        });
      return this.comKeyword
        ? comArr.filter((item) => item.text.toLowerCase().includes(this.comKeyword.toLowerCase()))
        : comArr;
    },
  },
  components: {
    Vue3RulerTool,
    vuedraggable,
    settings,
  },
  mounted() {
    var dragDiv = document.getElementById('draggableDiv');
    var that = this;
    dragDiv.addEventListener('mousemove', function (event) {
      that.x = event.offsetX;
      that.y = event.offsetY;
    });
    document.addEventListener('keydown', function (event) {
      that.isCtrl = true;
    });
    document.addEventListener('keyup', function (event) {
      that.isCtrl = false;
    });
    // this.$refs.rulerTool.windowResize()
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

    // vue3-ruler-tool组件有bug x轴 和y轴会有两个0 是个bug
    this.$refs['rulerTool'].xScale.shift();
    this.$refs['rulerTool'].yScale.shift();
  },
  methods: {
    // 获取图标
    getIcon(name) {
      return new URL(`../../../assets/img/screenDesign/${name}`, import.meta.url).href;
    },
    clickShow(value) {
      this[value] = !this[value];
    },
    //点击一级组件事件
    clickComponent(item) {
      let key = item.type + 'Type';
      if (this.screenConstants[key]) {
        this.subComponents = this.screenConstants[key];
        this.showSubComponents = true;
      } else {
        this.subComponents = [];
        this.showSubComponents = false;
      }
    },
    //拖拽结束事件
    endDraggable(item) {
      var that = this;
      setTimeout(() => {
        that.addComponent(item, true);
        that.x = 0;
        that.y = 0;
      }, 200);
    },
    clickPanel() {
      if (!this.isBubblingEvent) {
        if (this.activated) {
          this.activated.active = false;
        }
        this.activated = this.screenProperties;
        document.onkeydown = null;
      } else {
        this.isBubblingEvent = false;
      }
    },
    // 鼠标右键事件
    onContextmenu(event, item) {
      event.preventDefault();
      this.$contextmenu({
        x: event.x,
        y: event.y,
        items: this.getConttextMenu(item),
        event, // 鼠标事件信息
        customClass: 'layer-menu', // 自定义菜单 class
        zIndex: 9999, // 菜单样式 z-index
        minWidth: 104, // 主菜单最小宽度
      });
      return false;
    },
    getConttextMenu(item) {
      var menus = [
        {
          label: '重命名',
          onClick: () => {
            this.openRenameVisible(item);
          },
        },
        {
          label: '复制',
          onClick: () => {
            this.$refs.draggable.copyItem(item);
          },
        },
        {
          label: !item.locked ? '锁定' : '解锁',
          onClick: () => {
            if (!item.locked) {
              this.$refs.draggable.lockItem(item);
            } else {
              this.$refs.draggable.unlockItem(item);
            }
          },
        },
        {
          label: '删除',
          onClick: () => {
            this.$refs.draggable.delItem(item);
          },
        },
      ];

      return menus;
    },
    // 关闭弹框
    closeRenameVisible() {
      this.renameVisible = false;
      this.renameForm = {};
    },
    // 打开弹框
    openRenameVisible(item) {
      this.renameVisible = true;
      this.renameForm = item;
      this.renameForm.comName = item.text || this.screenConstants.compType[item.type].text;
    },
    // 确定更改
    sureRename() {
      this.renameVisible = false;
      this.renameForm.text = this.renameForm.comName;
    },
    clickSubComponent(index) {
      this.subComponentIndex = index;
    },
    subComponentsSrc(subComponents) {
      return subComponents[this.subComponentIndex]?.src + '?t=' + new Date().getTime() ?? '';
    },
    // 使用组件
    useSubComponent(item, index) {
      this.addComponent(item);
      // 关闭弹框
      this.$refs['subPopover' + index][0]?.hide();
    },
    //往画板中添加组件
    async addComponent(item, isDraggable) {
      this.components.forEach(com => {
        com.active = false
      })
      let obj = null;
      if (item.category == this.screenConstants.category.border) {
        obj = JSON.parse(JSON.stringify(this.screenConstants['dvBorderBoxInit']));
        if (isDraggable) {
          obj.x = this.x;
          obj.y = this.y;
        }
        obj.id = this.commonUtil.getUuid();
        obj.name = item.name;
        obj.active = true;

        if (item.text.indexOf('反向') >= 0) {
          obj.reverse = true;
        }
        this.components.push(obj);
      } else if (item.category == this.screenConstants.category.decoration) {
        obj = JSON.parse(JSON.stringify(this.screenConstants['dvDecorationBoxInit']));
        if (isDraggable) {
          obj.x = this.x;
          obj.y = this.y;
        }
        obj.id = this.commonUtil.getUuid();
        obj.name = item.name;
        obj.active = true;

        if (item.text.indexOf('反向') >= 0) {
          obj.reverse = true;
        }
        this.components.push(obj);
      } else {
        obj = JSON.parse(JSON.stringify(this.screenConstants[item.name + 'Init']));
        if (obj) {
          if (isDraggable) {
            obj.x = this.x;
            obj.y = this.y;
          }
          obj.id = this.commonUtil.getUuid();
          obj.active = true;

          this.components.push(obj);
          if (item.category == this.screenConstants.category.vchart) {
            if (obj.type.toLowerCase().indexOf('scattermap') >= 0) {
              let mapCode = obj.spec.series[0].map
              if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
              }
            }
            else if (obj.type.toLowerCase().indexOf('basicmap') >= 0) {
              const mapCode = obj.spec.map
              if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
              }
            } else if (obj.type.toLowerCase().indexOf('circlepacking') >= 0) {
              let categoryField = obj.spec.categoryField;
              let valueField = obj.spec.valueField;
              obj.spec.label.style.text = (datum) => [
                `${datum[categoryField]}`,
                `${datum[valueField]}`,
              ];
            } else if (obj.type.toLowerCase().indexOf('sankey') >= 0) {
              obj.spec.nodeKey = (datum) => datum.name;
            }
            this.$nextTick(() => {
              const vchart = new VChart(obj.spec, { dom: obj.id });
              // 绘制
              vchart.renderSync();
              this.chartsComponents[obj.id] = vchart;
            });
          } else if (obj.type == this.screenConstants.type.date) {
            obj.content = this.commonUtil.getCurrentDate(obj);
            // setInterval(() => {
            //   const self = this
            //   setTimeout(function() {
            //     self.refreshTime(obj)
            //   }, 0)
            // }, 1000)
          }else if (obj.type == this.screenConstants.type.tableMap) {
              const mapCode = obj.spec.map
              if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
              }
              obj.tableDatas = obj.spec.data.values;
              this.$nextTick(() => {
                const vchart = new VChart(obj.spec, { dom: obj.id })
                // 绘制
                vchart.renderSync()
                this.chartsComponents[obj.id] = vchart
            })
          }
        }
      }
      this.$refs.draggable.changeCurrent(obj)
    },
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
          if (
            response.responseData.components != null &&
            response.responseData.components.length > 0
          ) {
            for (let index = 0; index < response.responseData.components.length; index++) {
              const element = response.responseData.components[index];
              const component = JSON.parse(element.content);
              component.active = false;
              component.actived = false;
              that.components.push(component);
            }
            that.$nextTick(() => {
              that.initComponent();
            });
          }
        }
      });
    },
    //组件初始化
    async initComponent() {
      for (let index = 0; index < this.components.length; index++) {
        const element = this.components[index]
         if (element.category == this.screenConstants.category.tabsCard) {
          if(element.tabs && element.tabs.length > 0){
            for (let i = 0; i < element.tabs.length; i++) {
              const subComponent = element.tabs[i].subComponent;
              this.initSingleComponent(subComponent)
            }
          }
        }else{
          this.initSingleComponent(element)
        }
      }
    },
    async initSingleComponent(element){
      var that = this;
      if (element.category == this.screenConstants.category.vchart) {
          if (element.type.toLowerCase().indexOf('basicmap') >= 0) {
            const mapCode = element.spec.map
            if (!VChart.getMap(mapCode)) {
              const geojson = await this.commonUtil.getMapData(mapCode)
              VChart.registerMap(mapCode, geojson)
              
            }
          } else  if (element.type.toLowerCase().indexOf('scattermap') >= 0) {
              let mapCode = element.spec.series[0].map
              if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
              }
            }else if (element.type.toLowerCase().indexOf('pie') >= 0) {
            if (element.spec.isLoop) {
              element.spec.animationNormal =
                this.screenConstants.pieLoopanimation
            }
          } else if (element.type.toLowerCase().indexOf('circlepacking') >= 0) {
            const categoryField = element.spec.categoryField
            const valueField = element.spec.valueField
            element.spec.label.style.text = (datum) => [
              `${datum[categoryField]}`,
              `${datum[valueField]}`
            ]
          } else if (element.type.toLowerCase().indexOf('sankey') >= 0) {
            element.spec.nodeKey = (datum) => datum.name
          }
          var obj = { dom: element.id }
          if (element.theme) {
            obj.theme = element.theme
          }
          const vchart = new VChart(element.spec, obj)
          // 绘制
          vchart.renderSync()
          if(element.type == "basicMap" || element.type == "scatterMap"){
            if(element.isDrill){
              vchart.on('click', (params) => {
                  that.commonUtil.mapDrill(that.chartsComponents,element,params);
              })
            }
          }else{
            vchart.on('click', (params) => {
                  that.commonUtil.chartDrill(that.chartsComponents,element,params);
              })
          }
          this.chartsComponents[element.id] = vchart
        }else if (element.category == this.screenConstants.category.text) {
          if (element.type == this.screenConstants.type.date) {
            element.content = ''
            setInterval(() => {
              const self = this
              setTimeout(function() {
                self.refreshTime(element)
              }, 0)
            }, 1000)
          }
        }else if (element.type == this.screenConstants.type.tableMap) {
              const mapCode = element.spec.map
              if (!VChart.getMap(mapCode)) {
                const geojson = await this.commonUtil.getMapData(mapCode)
                VChart.registerMap(mapCode, geojson)
              }
              this.$nextTick(() => {
                const vchart = new VChart(element.spec, { dom: element.id })
                // 绘制
                vchart.renderSync()
                if(element.isDrill){
                  vchart.on('click', (params) => {
                      that.commonUtil.mapDrill(that.chartsComponents,element,params);
                  })
                }
                this.chartsComponents[element.id] = vchart
            })
          }
    },
    //保存模板
    save() {
      const params = {
        id: this.screenProperties.id,
        width: this.screenProperties.width,
        height: this.screenProperties.height,
        imgUrl: this.screenProperties.imgUrl,
        background: this.screenProperties.background,
        imgType: this.screenProperties.imgType,
        imgName: this.screenProperties.imgName,
      };
      if (this.components != null && this.components.length > 0) {
        const components = [];
        for (let index = 0; index < this.components.length; index++) {
          const element = this.components[index];
          if (!element.isDelete) {
            components.push(element);
          }
        }
        params.components = components;
      }
      const obj = {
        params: params,
        url: this.apis.screenTpl.saveScreenDesignApi,
      };
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code === '200') {
        }
      });
    },
    selectComponent(item) {
      if (this.activated) {
        this.activated.active = false;
        // this.activated.zindex = 99
      }
      item.active = true;
      //   item.zindex = 100
      this.activated = item;
    },
    // 锁定解锁组件
    lockComponent(item) {
      item.locked = !item.locked;
    },
    // 显示隐藏组件
    showComponent(item) {
      if (item.isShow === undefined) {
        item.isShow = false;
      } else {
        item.isShow = !item.isShow;
      }
    },
    refreshTime(component) {
      component.content = this.commonUtil.getCurrentDate(component);
    },
    async preview() {
      await this.save();
      const viewReport = this.$router.resolve({
        name: 'screenView',
        query: {
          tplId: this.screenProperties.id,
          sendRequest: 2,
          thirdPartyType: localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType),
        },
      });
      window.open(viewReport.href, '_blank');
    },
  },
};
