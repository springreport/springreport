<template>
  <div v-if="store.getters['setting/routerView']" class="app-main-container">
    <router-view class="app-main-height" />
    <MainFooter />
    <!-- <footer class="footer-copyright">{{ copyrightStr }} </footer> -->
  </div>
</template>

<script>
export default {
  name: 'AppMain',
};
</script>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import { setting } from '@/config/setting';
const { copyright } = setting;
import MainFooter from '../MainFooter/index.vue';

const copyrightStr = ref(copyright);
const store = useStore();
</script>

<style lang="scss" scoped>
.app-main-container {
  position: relative;
  box-sizing: border-box;
  width: $base-width;
  overflow: hidden;
  text-align: left;
  .app-main-height {
    min-height: $app-main-min-height;
  }
  .footer-copyright {
    min-height: $footer-copyright-height;
    line-height: $footer-copyright-height;
    color: $base-color-3;
    text-align: center;
    border-top: 1px dashed $base-border-color;
  }
}
</style>
