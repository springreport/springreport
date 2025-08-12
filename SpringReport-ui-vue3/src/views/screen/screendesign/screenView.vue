<template>
  <div
    class="mainbox"
    ref="dragContainer"
    :style="{
      background: screenProperties.background,
      backgroundImage: 'url(' + screenProperties.imgUrl + ')',
    }"
  >
    <div
      :style="{
        transform: `scale(${screenProperties.scale}) translate(${screenProperties.offsetX}px, ${screenProperties.offsetY}px)`,
        transformOrigin: '0 0',
        height: screenProperties.height + 'px',
        width: screenProperties.width + 'px',
      }"
    >
      <draggables
        ref="draggable"
        :components="components"
        :charts-components="chartsComponents"
        :isDesign="false"
        :sendRequest="sendRequest"
        :view-that="viewThat"
        :myclass="'myclass'"
        :searchParams="(searchData.params && searchData.params.length>0)?searchData.params[0].params:[]"
      />
    </div>
    <div style="width: 100%; flex: none">
      <reportForm
        ref="reportRef"
        :report-name="''"
        :report-form="searchData.params"
        :search-data="searchData"
        :search-handle="searchHandle"
        :activiti-name="''"
        :is-param-merge="'1'"
        :show-search='true'
        :is-drill="1"
        v-model:drawer="drawer"
        @closeSearch="closeSearch"
        :isScreen="true"
      />
    </div>
    <div
      ref="dragArea"
      class="drag-area"
    >
      <el-tooltip effect="dark" content="查询" placement="top">
        <el-button
          type="primary"
          size="default"
          circle
          @click="showSearch"
        ><icon-search
              theme="outline"
              size="16"
              class="el-icon--left"
            /></el-button>
      </el-tooltip>
    </div>
  </div>
</template>

<script src="./screenView.js"></script>

<style scoped lang="scss">
  @import './index.scss';

  .mainbox {
    height: 100vh;
    width: 100%;
    // overflow: auto;
    overflow: hidden;
    background-color: #000;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }

  .mainbox::-webkit-scrollbar {
    display: none;
  }

  .drag-area {
    position: fixed;
    right: 5px;
    bottom: 100px;
    z-index: 99999;
    // padding: 5px;
    width: fit-content;
    opacity: 1;
    // background-color: #17b794;
    border-radius: 8px;
    box-shadow: 0px 2px 15px 0px rgba(9, 41, 77, 0.15);
    // cursor: move;
    user-select: none;
    text-align: center;
  }
</style>
