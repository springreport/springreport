<template>
  <div class="mainbox" ref="dragContainer">
    <div
      :style="{
        transformOrigin: '0% 0%',
        height: screenProperties.height + 'px',
        width: screenProperties.width + 'px',
        backgroundImage: 'url(' + screenProperties.imgUrl + ')',
        background: screenProperties.background,
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
      }"
    >
      <draggables
        ref="draggable"
        :components="components"
        :charts-components="chartsComponents"
        :isDesign="false"
        :sendRequest="sendRequest"
        :myclass="'myclass'"
      />
    </div>
  </div>
</template>

<script src="./screenView.js">
</script>

<style scoped lang="scss">
 @import './index.scss';
.mainbox {
  height: 100%;
  width: 100%;
  overflow: auto;
}
.mainbox::-webkit-scrollbar {
  display: none;
}
</style>