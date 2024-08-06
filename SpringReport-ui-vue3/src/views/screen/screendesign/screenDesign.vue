<template>
    <div class="pagebox">
        <div class="header">
            <div class="left">
                <!-- 图层 -->
                <el-tooltip effect="dark" content="左侧图层" placement="bottom-start" popper-class="atooltip">
                <div class="svg-box" @click="clickShow('isShowLayer')">
                    <svg-icon icon-class="layer" class="svg" name="layer"/>
                </div>
                </el-tooltip>
                <!-- 面板 -->
                <el-tooltip effect="dark" content="右侧面板" placement="bottom-start" popper-class="atooltip">
                <div class="svg-box" style="margin-left:6px" @click="clickShow('isShowConf')">
                    <svg-icon icon-class="panel" class="svg" name="panel"/>
                </div>
                </el-tooltip>
            </div>
            <div class="middle">
                <svg-icon icon-class="screen" class="svg" name="screen"/>
                <span style="margin-left: 6px;">test</span>
            </div>
            <div class="right">
                <!-- 发布 -->
                <el-tooltip effect="dark" content="保存" placement="bottom-start" popper-class="atooltip">
                <div class="svg-box" @click="save()">
                    <svg-icon icon-class="public" class="svg" name="public"/>
                </div>
                </el-tooltip>
                <!-- 预览 -->
                <el-tooltip effect="dark" content="预览" placement="bottom-start" popper-class="atooltip">
                <div class="svg-box" style="margin-left:6px" @click="preview()">
                    <svg-icon icon-class="preview" class="svg" name="preview"/>
                </div>
                </el-tooltip>
            </div>
        </div>
        <div class="contentbox">
          <!-- 图层面板 -->
          <div v-if="isShowLayer" class="layer-panel">
            <div class="layer-header">图层</div>
            <div class="layer-main">
                <div
                    v-for="(item,index) in components"
                    :key="index"
                    :class="item.active?'box box-active':'box'"
                    @click="selectComponent(item)"
                    v-show="!item.isDelete"
                >
                    <div v-if="!item.isDelete && screenConstants.compType[item.type]" class="img-box">
                      <svg-icon :icon-class="screenConstants.compType[item.type].icon" class="svg" :name="screenConstants.compType[item.type].icon"/>
                    </div>
                    <span v-if="!item.isDelete && screenConstants.compType[item.type]" class="text">{{ screenConstants.compType[item.type].text }} </span>
                </div>
            </div>
          </div>
          <!-- 组件面板 -->
          <div class="comp-panel">
            <div class="comp-header">组件</div>
            <div class="comp-wrap">
                <div class="left-box">
                    <div class="box" v-for="(item,index) in screenConstants.componentsType1" :key="index" @click="clickComponent(item)" v-show="item.type != 'panel'">
                    <svg-icon :icon-class="item.icon" class="svg" :name="item.icon"/>
                    <span class="text">{{item.text}}</span>
                    </div>
                </div>
                <div class="left-box">
                    <div class="box" v-for="(item,index) in screenConstants.componentsType2" :key="index" @click="clickComponent(item)" v-show="item.type != 'panel'">
                    <svg-icon :icon-class="item.icon" class="svg" :name="item.icon"/>
                    <span class="text">{{item.text}}</span>
                    </div>
                </div>
                <div v-if="showSubComponents" class="right-box">
                    <!-- <vuedraggable class="wrapper" :sort= "false" :disabled= "false"> -->
                        <div  v-for="(item,index) in subComponents" class="box" @click="addComponent(item)" :key="index" @dragend="endDraggable(item)">
                                <el-popover  v-if="item.name != 'picture'"
                                    width="325px"
                                    trigger="hover"
                                    placement="left-start"
                                    content="">
                                    <img :src="item.src+'?t='+new Date().getTime()" style="width: 300px;height:150px" />
                                    <template #reference>
                                    <p>{{item.text}}</p>
                                    </template>
                                </el-popover>
                                
                                <p v-if="item.name == 'picture'">{{item.text}}</p>
                        </div>
                    <!-- </vuedraggable> -->
                </div>
            </div>
          </div>
          <div ref="dragContainer" class="mainbox">
            <div class="mainwrap">
                <!-- <vue3-ruler-tool ref='rulerTool' :content-layout="{left:0,top:0}" :is-scale-revise="true" :is-hot-key="false" :step-length="50" :style="{ transformOrigin: '0 0',transform: 'scale('+scaleSelected/100+')', height:'100%', overflow:'hidden'}"> -->
                    <!-- <vuedraggable class="wrapper" :sort= "false" :disabled= "true"> -->
                    <div id="draggableDiv"
                    :style="{transformOrigin: '0% 0%',transform: 'scale('+scaleSelected/100+')',height: screenProperties.height+'px',width:screenProperties.width+'px',background:screenProperties.background,backgroundImage:'url(' + screenProperties.imgUrl + ')',backgroundSize:'cover',backgroundRepeat:'no-repeat',backgroundPosition:'center', overflow:'hidden'}"
                    @click="clickPanel"
                    >
                    <draggables
                        ref="draggable"
                        :components="components"
                        v-model:activated="activated"
                        v-model:is-bubbling-event="isBubblingEvent"
                        :charts-components="chartsComponents"
                        :isDesign="true"
                        :sendRequest="false"
                        :isCtrl="isCtrl"
                        :myclass="'newclass'"
                    />
                    </div>
                    <!-- </vuedraggable> -->
                <!-- </vue3-ruler-tool> -->
            </div>
            <div  class="scaleWrap">
                <el-select v-model="scaleSelected" size="small" :popper-append-to-body="false" class="select" style="width:80px">
                    <el-option v-for="item in scaleList" :key="item" :value="item" :label="item+'%'" />
                </el-select>
            </div>
        </div>
        <div v-if="isShowConf"  class="config-panel">
            <div class="config-header">组件设置</div>
            <div class="config-box">
                <settings ref="tabForm" :component="activated" :charts-components="chartsComponents" :timer-map="timerMap" />
            </div>
        </div>
        </div>
        
    </div>
</template>

<script src="./screenDesign.js"></script>

<style scoped>
.atooltip {
  background: #2681ff !important;
  color: #FFFFFF !important;
  font-size: 12px;
  height: 26px !important;
  line-height: 26px !important;
  padding: 0px 8px !important;
  border-radius: 0;
}

/* 修改箭头背景颜色 */
.el-tooltip__popper[x-placement^=bottom] .popper__arrow{
  border-bottom-color: #2681ff;
}
.el-tooltip__popper[x-placement^=bottom] .popper__arrow:after {
  border-bottom-color: #2681ff;
}
.el-tooltip__popper.is-dark {
  background: #2681ff !important;
  color: #ffffff !important;
}
</style>

<style scoped lang="scss">
 @import './index.scss';

  .pagebox {
    height: 100vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    background: #090B0D;
  }

  .svg-box{
    width: 38px;
    height: 24px;
    background: transparent;
    border: 1px solid rgba(255,255,255,0.3);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .svg{
    width: 14px;
    height: 14px;
    font-size: 14px !important;
    color:#FFFFFF;
  }
  .svg-box:hover, .svg-box:active, .svg-box:focus{
    background: #2681FF;
  }

  .pagebox .header {
    display: flex;
    background: #1D1D1F;
    justify-content: space-between;
    padding: 11px 12px 9px 12px;
    align-items: center;

    .left, .right, .middle{
      display: flex;
      justify-content:flex-start;
    }

    .middle{
      font-size: 13px;
      font-weight: 400;
      color: #FFFFFF;
      line-height: 13px;
    }
  }

  .contentbox {
    flex: 1;
    display: flex;
    overflow: hidden;
    background: #090B0D;
    padding-top: 1px;

    .layer-panel {
      background: #1D2126;
      position: relative;
      width: 165px;
      height: 100%;
      display: flex;
      flex-direction: column;
      border-right:1px solid #090B0D;
      .layer-header{
        box-sizing: border-box;
        width: 100%;
        height: 32px;
        background: #2F343D;
        font-size: 13px;
        font-weight: 400;
        color: #FFFFFF;
        line-height: 32px;
        padding-left: 12px;
      }
      .layer-main{
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow-y: auto;
        overflow-x: hidden;
        .box{
          width: 100%;
          height: 46px;
          background: rgba(255,255,255,0.1);
          padding-left: 12px;
          display: flex;
          align-items: center;
          border:1px solid #090B0D;
          .img-box{
            width: 46px;
            height: 28px;
            border: 1px solid #2681FF;
            background: #1D2126;
            display: flex;
            justify-content: center;
            align-items: center;
            .svg{
              width: 16px;
              height: 16px;
              color:#2681FF;
            }
          }
          .text{
            margin-left: 13px;
            width: 84px;
            height: 46px;
            font-size: 12px;
            font-weight: 400;
            color: #FFFFFF;
            line-height: 46px;
          }
        }
        .box:hover{
          background: rgba(255,255,255,0.2);
        }
        .box:active, .box:focus{
          background:#2681FF;
        }
        .box-active, .box-active:hover{
          background: #2681FF;
        }
      }
      .layer-main::-webkit-scrollbar {
        width: 0;
      }
    }

    .comp-panel{
      background: #1D2126;
      position: relative;
      // width: 102px;
      height: 100%;
      display: flex;
      flex-direction: column;
      margin-right: 1px;

      .comp-header{
        width: 100%;
        height: 32px;
        background: #2F343D;
        font-size: 13px;
        font-weight: 400;
        color: #FFFFFF;
        line-height: 32px;
        text-align: center;
      }
      
      .comp-wrap{
        width: 100%;
        flex: 1;
        display: flex;
        justify-content: flex-start;
        background: #090B0D;
        overflow: auto;
         height:100vh;
        .left-box{
          width: 45px;
          display: flex;
          flex-direction: column;
          background: #191C20;
          height:100vh;
          .box{
            height: 60px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            color: #FFFFFF;
            .text{
              margin-top: 6px;
              display: inline-block;
              height: 11px;
              font-size: 11px;
              font-weight: 400;
              line-height: 11px;
            }
          }
          .box:hover{
            background: rgba(255,255,255,0.1);
          }

          .box:active, .box:focus{
            color: #2681FF;
            border-left: 2px solid #2681FF;
            background: #15161A;
            .svg{
              color: #2681FF;
            }
          }

          .box-active, .box-active:hover{
            color: #2681FF;
            border-left: 2px solid #2681FF;
            background: #15161A;
            .svg{
              color: #2681FF;
            }
          }
        }
        .right-box::-webkit-scrollbar {
          width: 0;
        }
        .right-box{
          margin-left: 1px;
          padding: 6px;
          flex: 1;
          display: flex;
          flex-direction: column;
          background: #14161A;
          overflow: auto;
          height:100vh;
          width:60px;
          .box{
            display: inline-block;
            min-height: 30px;
            width:60px;
            // line-height: 30px;
            text-align: center;
            font-size: 11px;
            font-weight: 400;
            color: #FFFFFF;
            // height:95vh;
          }
          .box:hover{
            background: rgba(255,255,255,0.1);
          }
          .box:active, .box:focus{
            background: #090B0D;
            color: #2681FF;
          }
          .box-active, .box-active:hover{
            background: #090B0D;
            color: #2681FF;
          }
        }
      }
    }

    .config-panel{
      background: #1a1a1a;
      margin-left: 1px;
      position: relative;
      width: 310px;
      height: 100%;
      display: flex;
      flex-direction: column;
      .config-header{
        width: 100%;
        height: 32px;
        background: #2F343D;
        font-size: 13px;
        font-weight: 400;
        color: #FFFFFF;
        line-height: 32px;
        text-align: center;
      }
      .config-box{
        flex:1;
        padding: 10px;
        overflow: auto;
      }

      /*定义滚动条的宽度*/
      .config-box::-webkit-scrollbar {
        width: 0;
      }
    }
  }
  .comp-wrap::-webkit-scrollbar {
    width: 0; /* 设置滚动条的宽度为0 */
    background-color: transparent; /* 设置滚动条背景为透明 */
  }
  .contentbox .mainbox {
    flex: 1;
    background: #313239;
    padding: 0;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }

  .contentbox .mainbox .mainwrap {
    height: calc(100% - 30px);
    /* background: #181B24; */
    overflow-y: auto;
    overflow-x: auto;
  }

  .contentbox .mainbox .scaleWrap {
    height: 30px;
    background: #222528;
    text-align: right;

    .select{
      height: 20px;
      width: 74px;
      margin-right: 12px;
      margin-bottom: 6px;
    }
  }

  .contentbox .rightbox {
    flex: none;
/*    padding-left: 30px; */
    background: #22242B;
    position: relative;
    width: 334px;
  }

  .contentbox .rightbox.collapse {
    width: 30px;
  }

  .clarrow {
    position: absolute;
    top: 50%;
    left: 3px;
    font-size: 20px;
    cursor: pointer;
  }

  .el-tabs :deep(.el-tabs__content) {
    padding-bottom: 15px;
    height: 95%;
    overflow-y: auto;
    overflow-x: hidden;
  }

  .el-menu-demo .el-menu-item,
  .el-menu-demo .el-submenu :deep(.el-submenu__title) {
    font-size: 16px;
  }

  .el-menu-demo .el-menu-item i,
  .el-menu-demo .el-submenu :deep(.el-submenu__title i) {
    color: #B3B4B6;
  }

  .el-menu-demo .el-menu-item.is-active,
  .el-menu-demo .el-submenu.is-active :deep(.el-submenu__title) {
    background-color: #2f3544 !important;
    border-bottom-color: #2f3544 !important;
  }

  .el-menu-demo .el-menu-item.is-active i,
  .el-menu-demo .el-submenu.is-active :deep(.el-submenu__title i) {
    color: white;
  }

  .el-menu-demo .el-submenu :deep(.el-submenu__title .el-submenu__icon-arrow) {
    margin-top: 0px;
  }

  .mytaba :deep(.el-tabs__header .el-tabs__item.is-active) {
    color: white;
  }

  .mytaba :deep(.el-tabs__nav-wrap::after) {
    background-color: #3a3f48;
  }

  .mytaba :deep(.el-tabs__header .el-tabs__active-bar) {
    background-color: white;
  }

  .mytaba :deep(.el-tabs__header) {
    background-color: #2D2F38;
    padding: 0 10px;
    margin-bottom: 0;
  }

  .mytaba :deep(.el-tabs__item) {
    font-size: 14px;
  }

  .mycarda.el-card  :deep(.el-card__body) {
    padding: 8px;
  }

  .mycarda.el-card {
    border: 0;
    border-radius: 0;
    background-color: unset;
    color: white;
  }

  .mycarda.el-card label {
    color: white;
    font-size: 14px;
  }

  .mainwrap::-webkit-scrollbar {
    width: 4px;
    height: 4px;
  }
  .mainwrap::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(117, 114, 114, 0.7);
  }

 .mainwrap::-webkit-scrollbar-track{
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 0;
    background: rgba(0, 0, 0, 0.1);

  }

  .titlea{
    background: #2D2F38;
    display: flex;
    justify-content: space-between;
    color: white;
    font-size: 14px;
    padding:10px;
  }
  .infobox :deep(.el-form-item__label){
    color: #c5c5c5;
  }
  .infobox :deep(.el-collapse){
    border-top:1px solid #393B4A;
    border-bottom: 1px solid #393B4A;
  }
  .infobox :deep(.el-collapse .el-collapse-item__header){
    background-color:#2D2F38 ;
    color:#fff ;
    padding-left: 10px;
    border-bottom: 1px solid #2D2F38;
  }
  .infobox :deep(.el-collapse-item__wrap){
    background-color: #22242B;
    padding:20px 16px;
  }
  .infobox :deep(.el-collapse-item__content){
    padding-bottom: 0;
  }
  .vue-ruler-wrapper{
    height:200% !important;
    width:200% !important
  }

/*  .infobox>>>.el-form.demo-form-inline.el-form--label-left{
    padding:20px 16px;
  } */
</style>