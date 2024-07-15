<template>
  <div
    @click="handleClick"
    class="logo-wrapper"
    :class="{ unfold: collapse, horizontal: mode === 'horizontal' }"
  >
    <span class="logo-title" :style="{ color: textColor }" v-if="!collapse"> SpringReport</span>
    <span class="logo-title" :style="{ color: textColor }" v-if="!collapse"> VUE-3</span>
  </div>
</template>

<script>
  export default {
    name: 'Logo',
  };
</script>

<script setup>
  import { computed } from 'vue';
  import { useRouter } from 'vue-router';
  import { useStore } from 'vuex';

  import { themeConfig } from '@/config/theme';
  const { themeOptions } = themeConfig;

  const store = useStore();
  const router = useRouter();

  const collapse = computed(() => {
    return store.getters.collapse;
  });

  const mode = computed(() => {
    return store.getters['setting/mode'];
  });

  const textColor = computed(() => {
    const whiteColors = ['#fff', '#ffffff', '#FFF', '#FFF', 'rgb(255, 255, 255)'];
    const color = themeOptions[store.getters['setting/theme']].menuBgColor;
    return whiteColors.indexOf(color) !== -1 ? '#333' : '#fff';
  });

  const handleClick = () => {
    router.replace('/');
  };
</script>
<style lang="scss" scoped>
  .logo-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: $base-logo-width;
    cursor: pointer;
    &.unfold {
      width: $base-unfold-width;
      padding: $base-padding-10 0;
    }
    &.horizontal {
      justify-content: flex-start;
    }

    .logo-title {
      display: inline-block;
      max-width: calc(246px - 60px);
      padding-left: $base-padding-10;
      overflow: hidden;
      font-size: $base-font-size-max;
      line-height: $base-logo-height;
      color: #333;
      text-overflow: ellipsis;
      white-space: nowrap;
      vertical-align: middle;
    }
  }
</style>
