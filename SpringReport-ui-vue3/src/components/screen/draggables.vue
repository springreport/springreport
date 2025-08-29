<template>
  <div style="width: 100%; height: 100%; position: relative; margin: 0 auto" class="draggable">
    <!-- <Vue3DraggableResizable
      v-for="(item, index) in components"
      :key="index"
      :class="myclass"
      :parent="true"
      :z="item.active?1000:item.zindex"
      :isConflictCheck="false"
      :snap="true"
      :snapTolerance="10"
      :x="item.x ? Number(item.x) : 0"
      :y="item.y ? Number(item.y) : 0"
      :w="Number(item.w)"
      :h="Number(item.h)"
      :active="item.active"
      @activated="onActivated(index, item)"
      @deactivated="onDeactivated"
      @resizestop="onResizstop"
      @dragstop="onDragstop"
      :draggable="!item.locked"
      :resizable="!item.locked"
      v-show="!item.isDelete"
    > -->
    <Vue3DraggableResizable
      v-for="(item, index) in components"
      :key="index"
      :parent="true"
      :style="{ 'z-index': item.active ? 1000 : item.zindex }"
      :z="item.active ? 1000 : (item.zindex*1)"
      :isConflictCheck="false"
      :snap="true"
      :snapTolerance="10"
      :x="item.x ? Number(item.x) : 0"
      :y="item.y ? Number(item.y) : 0"
      :w="Number(item.w)"
      :h="Number(item.h)"
      :active="item.active"
      @activated="onActivated(index, item)"
      @deactivated="onDeactivated"
      @resize-end="onResizstop"
      @drag-end="onDragstop"
      v-show="!item.isDelete && item.isShow != false"
      :draggable="!item.locked"
      :resizable="!item.locked"
      :class-name-active="isDesign?'active':''"
    >
      <div
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.text && !item.isDelete"
      >
        <text-component
          :component="item"
          :chartsComponents="chartsComponents"
          :sendRequest="sendRequest"
          :ref="item.id"
          :searchParams="searchParams"
        ></text-component>
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
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.border && !item.isDelete"
      >
        <border-component :component="item" :ref="item.id"></border-component>
      </div>
      <div
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.decoration && !item.isDelete"
      >
        <decoration-component :component="item" :ref="item.id"></decoration-component>
      </div>
      <div
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.picture && !item.isDelete"
        :id="item.id"
        :style="{ height: item.h + 'px', width: item.w + 'px' }"
      >
        <img v-if="item.imgUrl" :src="item.imgUrl" style="height: 100%; width: 100%" />
      </div>
      <div
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.table && !item.isDelete"
      >
        <table-component
          :component="item"
          :sendRequest="sendRequest"
          :chartsComponents="chartsComponents"
          :ref="item.id"
          :searchParams="searchParams"
        ></table-component>
      </div>
      <div
        @contextmenu.prevent="onContextmenu($event, item)"
        v-if="item.category == screenConstants.category.vchart && !item.isDelete"
        :style="{ height: item.h + 'px', width: item.w + 'px' }"
      >
        <vchart-component
          :component="item"
          :sendRequest="sendRequest"
          :chartsComponents="chartsComponents"
          :ref="item.id"
          :view-that="viewThat"
          :searchParams="searchParams"
        ></vchart-component>
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
    </Vue3DraggableResizable>
  </div>
</template>

<script src="./draggablesFunc.js"></script>
<style lang="scss">
  @import '@/element-variables.scss';

  .draggable {
    /* 隐藏默认边框 */
    .vdr-container {
      border: none !important;
    }

    /* 激活时显示边框 */
    .vdr-container.active {
      &::before {
        // box-sizing: border-box;
        position: absolute;
        content: '';
        width: 100%;
        height: 100%;
        border: 3px dashed $--color-primary !important;
        left: -3px;
        top: -3px;
        z-index: -10;
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
