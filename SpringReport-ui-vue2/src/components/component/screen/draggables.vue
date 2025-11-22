<template>
  <div
    style="width: 100%; height: 100%; position: relative; margin: 0 auto"
    class="draggable"
  >
    <vue-draggable-resizable
      v-for="(item, index) in components"
      v-show="!item.isDelete && item.isShow != false"
      :key="index"
      :parent="true"
      :z="item.active ? 1000 : (item.zindex*1)"
      :is-conflict-check="false"
      :snap="true"
      :snap-tolerance="10"
      :x="item.x ? Number(item.x) : 0"
      :y="item.y ? Number(item.y) : 0"
      :w="Number(item.w)"
      :h="Number(item.h)"
      :active="item.active"
      :draggable="!item.locked"
      :resizable="!item.locked"
      @activated="onActivated(index, item)"
      @deactivated="onDeactivated"
      @resizestop="onResizstop"
      @dragstop="onDragstop"
      :class-name-active="isDesign?'active':''"
    >
      <div
        v-if="item.category == screenConstants.category.text && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <text-component
          :ref="item.id"
          :component="item"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="item.category == screenConstants.category.numberFlipper && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <number-flipper-component
        :ref="item.id"
          :component="item"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="
          item.category == screenConstants.category.border && !item.isDelete
        "
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <border-component :ref="item.id" :component="item" />
      </div>
      <div
        v-if="
          item.category == screenConstants.category.decoration && !item.isDelete
        "
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <decoration-component :ref="item.id" :component="item" />
      </div>
      <div
        v-if="
          item.category == screenConstants.category.picture && !item.isDelete
        "
        :id="item.id"
        :style="{ height: item.h + 'px', width: item.w + 'px' }"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <img v-if="item.imgUrl" :src="item.imgUrl">
      </div>
      <div
        v-if="item.category == screenConstants.category.table && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <table-component
          :ref="item.id"
          :component="item"
          :send-request="sendRequest"
          :charts-components="chartsComponents"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="
          item.category == screenConstants.category.vchart && !item.isDelete
        "
        :style="{ height: item.h + 'px', width: item.w + 'px' }"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <vchart-component
          :ref="item.id"
          :component="item"
          :send-request="sendRequest"
          :charts-components="chartsComponents"
          :view-that="viewThat"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="item.category == screenConstants.category.cardList && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <card-list-component
        :ref="item.id"
          :component="item"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
       <div
        v-if="item.category == screenConstants.category.tabsCard && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
        <tabs-card-component
        :ref="item.id"
          :component="item"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
        />
      </div>
      <div
        v-if="item.category == screenConstants.category.tableMap && !item.isDelete"
        @contextmenu.prevent="onContextmenu($event, item)"
      >
      <table-map-component
        :ref="item.id"
        :style="{ height: item.h + 'px', width: item.w + 'px' }"
          :component="item"
          :charts-components="chartsComponents"
          :send-request="sendRequest"
          :searchParams="searchParams"
          :view-that="viewThat"
        />
      </div>
    </vue-draggable-resizable>
  </div>
</template>

<script src="./draggablesFunc.js">
</script>

<style lang="scss">
@import "@/element-variables.scss";

.draggable {
  /* 隐藏默认边框 */
  .vdr {
    border: none !important;
  }

  /* 激活时显示边框 */
  .vdr.active {

    &::before {
      position: absolute;
      content: "";
      width: 100%;
      height: 100%;
      z-index: -10;
      border: 3px dashed $--color-primary !important;
      left: -3px;
      top: -3px;
    }
  }
  .handle {
    border-color: $--color-primary;
    background-color: $--color-primary;
    box-sizing: border-box;
    width: 10px !important;
    height: 10px !important;
    z-index: 1000;
  }

  /* 左边三个句柄 */
  .handle-tl,
  .handle-ml,
  .handle-bl {
    left: -6px !important;
  }

  /* 上边三个句柄 */
  .handle-tl,
  .handle-tm,
  .handle-tr {
    top: -6px !important;
  }

  /* 右边三个句柄 */
  .handle-tr,
  .handle-mr,
  .handle-br {
    right: -6px !important;
  }

  /* 底部三个句柄 */
  .handle-bl,
  .handle-bm,
  .handle-br {
    bottom: -6px !important;
  }
}
</style>
<style scoped lang="scss">
img {
  width: 99.7%;
  height: 99.6%;
  max-width: 99.7%;
  max-height: 99.6%;
}
</style>
