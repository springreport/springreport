<template>
  <div
    ref="dragContainer"
    class="mainbox"
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
        :is-design="false"
        :send-request="sendRequest"
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
</style>
