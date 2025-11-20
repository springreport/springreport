<template>
    <div class="test-fan" :style="{
        height: component.h + 'px',
        width: component.w + 'px',
        overflow:'auto',
        backgroundColor: component.style.backgroundColor
    }">
    <el-tabs v-model="activeName" type="card" :style="cssVars" >
      <el-tab-pane v-for="(item,index) in component.tabs" :key="index" :label="item.tabContent" :name="item.tabContent">
        <div
        v-if="item.subComponent.category == screenConstants.category.text && !item.subComponent.isDelete"
      >
        <text-component
          :ref="item.subComponent.id"
          :component="item.subComponent"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="item.subComponent.category == screenConstants.category.numberFlipper && !item.subComponent.isDelete"
      >
        <number-flipper-component
        :ref="item.subComponent.id"
          :component="item.subComponent"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="
          item.subComponent.category == screenConstants.category.picture && !item.subComponent.isDelete
        "
        :id="item.subComponent.id"
        :style="{ height: item.subComponent.h + 'px', width: item.subComponent.w + 'px' }"
      >
        <img v-if="item.subComponent.imgUrl" :src="item.subComponent.imgUrl">
      </div>
      <div
        v-if="item.subComponent.category == screenConstants.category.table && !item.subComponent.isDelete"
      >
        <table-component
          :ref="item.subComponent.id"
          :component="item.subComponent"
          :send-request="sendRequest"
          :charts-components="chartsComponents"
          :searchParams="searchParams"
        />
      </div>
        <div
        v-if="
          item.subComponent.category == screenConstants.category.vchart && !item.subComponent.isDelete
        "
        :style="{ height: '100%', width: '100%' }"
      >
        <vchart-component
          :ref="item.subComponent.id"
          :component="item.subComponent"
          :send-request="sendRequest"
          :charts-components="chartsComponents"
          :searchParams="searchParams"
        />
        </div>
        <div
        v-if="item.subComponent.category == screenConstants.category.cardList && !item.subComponent.isDelete"
      >
        <card-list-component
        :ref="item.subComponent.id"
          :component="item.subComponent"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      </el-tab-pane>
  </el-tabs>
  </div>
</template>

<script>
import vchartComponent from './vchartComponent.vue'
import textComponent from './textComponent.vue'
import tableComponent from './tableComponent.vue'
import numberFlipperComponent from './numberFlipperComponent.vue'
import cardListComponent from './cardListComponent.vue'
import tabsCardComponent from './tabsCardComponent.vue'
export default {
  name: "cardTabsComponent",
  components: {
    vchartComponent,
    textComponent,
    tableComponent,
    numberFlipperComponent,
    cardListComponent,
    tabsCardComponent
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
    sendRequest: {
      //是否需要动态获取数据，//预览和设计的时候不需要动态获取数据，真正查看的时候才需要
      type: Boolean,
      default: false,
    },
    searchParams: {
      type: Array,
      default: () => []
    },
  },
  mounted() {
    if(this.component.tabs && this.component.tabs.length>0){
      this.activeName = this.component.tabs[0].tabContent;
    }
    this.changeTab();
    this.initData(this.searchParams);
  },
   data() {
      return {
        activeName:"",
        timerMap:{},
        indexMap:{},
      };
    },
  methods: {
    //数据初始化
    initData(searchParams) {
      if (this.sendRequest) {
        if(this.component.tabs && this.component.tabs.length > 0){
          for (let index = 0; index < this.component.tabs.length; index++) {
            const element = this.component.tabs[index].subComponent;
            if (element.dataSource == "2") {
                this.getData(element,searchParams);
                if(element.refresh){
                    var self = this;
                    setInterval(() => {
                        setTimeout(function(){self.getData(element,searchParams)}, 0)
                    }, element.refreshTime)
                }
            }
          }
        }
      }
    },
    getData(component,searchParams) {
      let pageParams = this.commonUtil.searchParamMap(searchParams)
      var params = {
        dataSetId: component.dynamicDataSettings.datasetId,
        dataColumns: component.dynamicDataSettings.dataColumns,
        sqlType: "1",
      };
      var componentParams = this.commonUtil.getComponentParams(
        component.params
      );
      params.params = Object.assign({}, componentParams, pageParams);
      let obj = {
        url: this.apis.screenDesign.getDynamicDatasApi,
        params: params,
        removeEmpty: false,
      };
      var that = this;
      this.commonUtil.doPost(obj).then((response) => {
        if (response.code == "200") {
          component.spec.data.values = response.responseData;
          if(component.category == that.screenConstants.category.vchart){
            that.commonUtil.reLoadChart(this.chartsComponents, component,this.sendRequest,this.viewThat);
          }
        }
      });
    },
    changeTab(){
      if(this.component.autoChangeTab){
            if(this.component.tabs && this.component.tabs.length > 1){
              var timer = this.timerMap[this.component.id]
              if(timer != null){
                clearInterval(timer);
                timer = null;
              }
              var self = this;
              this.indexMap[this.component.id] = 0;
              timer = setInterval(() => {
                        setTimeout(function(){self.autoChangeTab(self.component)}, 0)
                    }, this.component.changeTabInterval)
               this.timerMap[this.component.id] = timer;
            }else{
              var timer = this.timerMap[this.component.id]
              if(timer != null){
                clearInterval(timer);
                timer = null;
                this.timerMap[this.component.id]
              }
            }
          }else{
              var timer = this.timerMap[this.component.id]
              if(timer != null){
                clearInterval(timer);
                timer = null;
                this.timerMap[this.component.id] = null;
              }
          }
    },
    autoChangeTab(component){
      if(this.indexMap[component.id] != null){
        if(!this.component.tabs || this.component.tabs.length <= 1){
          var timer = this.timerMap[this.component.id]
          if(timer != null){
            clearInterval(timer);
            timer = null;
            this.timerMap[this.component.id]
          }
        }
        let index = this.indexMap[component.id] + 1;
        if(index >= component.tabs.length){
          index = 0;
        }
        this.indexMap[component.id] = index;
        this.activeName = component.tabs[index].tabContent
      }
    },
  },
  watch: {
    'component.autoChangeTab': {
        handler(newVal, oldVal) {
          this.changeTab();
        },
        deep:true
      },
      'component.changeTabInterval': {
        handler(newVal, oldVal) {
          this.changeTab();
        },
        deep:true
      },
      'component.tabs': {
        handler(newVal, oldVal) {
          if((this.activeName == "" || this.activeName == 0) && this.component.tabs && this.component.tabs.length>0){
            this.activeName = this.component.tabs[0].tabContent
          }
        },
        deep:true
      }
    },
  computed: {
    cssVars() {
      return {
        "--backgroundColor": this.component.style.backgroundColor,
        "--color": this.component.style.titleStyle.color,
        "--height": this.component.style.titleStyle.height+'px',
        "--width": this.component.style.titleStyle.width+'px',
        "--activeColor": this.component.style.titleStyle.activeColor,
        "--fontSize": this.component.style.titleStyle.fontSize+'px',
        "--fontWeight": this.component.style.titleStyle.fontWeight,
        "--tagColor": this.component.style.titleStyle.tagColor,
        "--activeTagColor": this.component.style.titleStyle.activeTagColor,
        "--tabinterval": this.component.style.titleStyle.tabinterval+'px',
      };
    }
  }
};
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__item.is-active {
  color: var(--color);
  opacity: 1;
}

::v-deep .el-tabs{
  .el-tabs__header{
    border:none;
    margin:0 0 5px;
    .el-tabs__nav-wrap{
      .el-tabs__nav-scroll{
        .el-tabs__nav{
          border:none;
          .el-tabs__active-bar{
            display: none;
          }
          .el-tabs__item{
            width: var(--width);
            height:  var(--height);
            text-align: center;
            line-height: var(--height);
            font-size: var(--fontSize);
            color: var(--color);
            // border-radius: 40px;
            border:none;
            padding: 0;
            background: var(--tagColor);
            font-weight: var(--fontWeight);
            margin-right: var(--tabinterval);
          }
          .el-tabs__item.is-active{
            color: var(--activeColor);
            background: var(--activeTagColor);
          }
        }
      }
    }
  }
  .el-tabs__nav-wrap::after{
    display: none;
  }
}
</style>