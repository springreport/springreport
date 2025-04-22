<template>
  <div style="width: 100%; height: 100%; position: relative; margin: 0 auto">
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
    <Vue3DraggableResizable v-for="(item,index) in components" 
      :key="index" 
      :class="myclass" 
      :parent="true" 
      :z="item.zindex" 
      :isConflictCheck="false" 
      :snap="true" :snapTolerance="10" 
      :x="item.x ? Number(item.x) : 0"
      :y="item.y ? Number(item.y) : 0"
      :w="Number(item.w)"
      :h="Number(item.h)"
      :active="item.active" 
      @activated="onActivated(index,item)"
      @deactivated="onDeactivated" @resize-end="onResizstop"
      @drag-end="onDragstop" 
      v-show="!item.isDelete" 
      :draggable="!item.locked" 
      :resizable="!item.locked">
      <div @contextmenu.prevent="onContextmenu($event, item)" v-if="item.category == screenConstants.category.text && !item.isDelete" >
        <text-component :component="item" :chartsComponents="chartsComponents" :sendRequest="sendRequest" :ref="item.id"></text-component>
      </div>
      <div @contextmenu.prevent="onContextmenu($event, item)" v-if="item.category == screenConstants.category.border && !item.isDelete" >
          <border-component :component="item" :ref="item.id"></border-component>
      </div>
      <div @contextmenu.prevent="onContextmenu($event, item)" v-if="item.category == screenConstants.category.decoration && !item.isDelete">
       <decoration-component :component="item" :ref="item.id"></decoration-component>
      </div>
      <div @contextmenu.prevent="onContextmenu($event, item)" v-if="item.category == screenConstants.category.picture && !item.isDelete" :id="item.id" :style="{height:item.h+'px',width:item.w+'px'}">
        <img v-if="item.imgUrl" :src="item.imgUrl" style="height:100%;width:100%">
      </div>
      <div @contextmenu.prevent="onContextmenu($event, item)" v-if="item.category == screenConstants.category.table && !item.isDelete" >
        <table-component :component="item" :sendRequest="sendRequest" :chartsComponents="chartsComponents" :ref="item.id"></table-component>
      </div>
      <div @contextmenu.prevent="onContextmenu($event, item)"  v-if="item.category == screenConstants.category.vchart && !item.isDelete" :style="{height:item.h+'px',width:item.w+'px'}">
        <vchart-component  :component="item" :sendRequest="sendRequest" :chartsComponents="chartsComponents" :ref="item.id"></vchart-component>
      </div>
    </Vue3DraggableResizable>
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