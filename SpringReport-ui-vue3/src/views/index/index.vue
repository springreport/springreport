<template>
  <div class="home-page">
    <img src="@/assets/img/homePage.png" />
  </div>
</template>

<script>
export default {
  name: 'Index',
};
</script>

<script setup>
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  height: calc(100% - 60px);
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  img {
    height: calc(100vh - 140px);
    width: auto;
    max-width: 100%;
  }
}
</style>
