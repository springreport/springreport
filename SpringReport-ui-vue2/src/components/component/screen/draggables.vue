<template>
  <div style="width: 100%; height: 100%; position: relative; margin: 0 auto">
    <vue-draggable-resizable
      v-for="(item, index) in components"
      :key="index"
      :class="myclass"
      :parent="true"
      :z="item.active?1000:item.zindex"
      :is-conflict-check="false"
      :snap="true"
      :snap-tolerance="10"
      :x="item.x ? Number(item.x) : 0"
      :y="item.y ? Number(item.y) : 0"
      :w="Number(item.w)"
      v-show="!item.isDelete&&item.isShow!=false"
      :h="Number(item.h)"
      :active="item.active"
      :draggable="!item.locked"
      :resizable="!item.locked"
      @activated="onActivated(index, item)"
      @deactivated="onDeactivated"
      @resizestop="onResizstop"
      @dragstop="onDragstop"
    >
      <div v-if="item.category == screenConstants.category.text && !item.isDelete" @contextmenu.prevent="onContextmenu($event, item)">
        <text-component :ref="item.id" :component="item" :charts-components="chartsComponents" :send-request="sendRequest" />
      </div>
      <div v-if="item.category == screenConstants.category.border && !item.isDelete" @contextmenu.prevent="onContextmenu($event, item)">
        <border-component :ref="item.id" :component="item" />
      </div>
      <div v-if="item.category == screenConstants.category.decoration && !item.isDelete" @contextmenu.prevent="onContextmenu($event, item)">
        <decoration-component :ref="item.id" :component="item" />
      </div>
      <div v-if="item.category == screenConstants.category.picture && !item.isDelete" :id="item.id" :style="{height:item.h+'px',width:item.w+'px'}" @contextmenu.prevent="onContextmenu($event, item)">
        <img v-if="item.imgUrl" :src="item.imgUrl">
      </div>
      <div v-if="item.category == screenConstants.category.table && !item.isDelete" @contextmenu.prevent="onContextmenu($event, item)">
        <table-component :ref="item.id" :component="item" :send-request="sendRequest" :charts-components="chartsComponents" />
      </div>
      <div v-if="item.category == screenConstants.category.vchart && !item.isDelete" :style="{height:item.h+'px',width:item.w+'px'}" @contextmenu.prevent="onContextmenu($event, item)">
        <vchart-component :ref="item.id" :component="item" :send-request="sendRequest" :charts-components="chartsComponents" />
      </div>
    </vue-draggable-resizable>
  </div>
</template>

<script src="./draggablesFunc.js">

</script>

<style scoped lang="scss">
img {
  width: 99.7%;
  height: 99.6%;
  max-width: 99.7%;
  max-height: 99.6%;
}

.myclass {
  border: none;
}
.newclass {
  border: 1px solid #00ced1;
}

</style>
