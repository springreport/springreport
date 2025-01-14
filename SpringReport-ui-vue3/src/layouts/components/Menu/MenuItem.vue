<template>
  <el-menu-item :key="item.path" :index="'/'+item.path" v-if="item.subs.length == 0">
    <span class="iconfont" :class="item.icon" />
    <template #title>
      <span class="title">{{ item.title }}</span>
    </template>
  </el-menu-item>
  <el-sub-menu :class="{ 'is-black': isBlack }" :index="'/'+item.path" v-else>
    <template #title>
      <span class="iconfont" :class="item.icon" />
      <span class="title">{{ item.title }}</span>
    </template>
    <template v-for="(option, index) in item.subs">
      <menu-item v-if="option.subs" :key="option.path" :item="option" />
      <el-menu-item v-else :index="'/'+option.path" :key="index">
        <span class="iconfont" :class="option.icon" />
        <span class="title">{{ option.title }}</span>
      </el-menu-item>
    </template>
  </el-sub-menu>
</template>

<script>
  export default {
    name: 'MenuItem',
  };
</script>

<script setup>
  import { computed } from 'vue';
  import { useStore } from 'vuex';

  import { themeConfig } from '@/config/theme';
  const { themeOptions } = themeConfig;

  const whiteColors = ['#fff', '#ffffff', '#FFF', '#FFF', 'rgb(255, 255, 255)'];

  defineProps({
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
  });

  const store = useStore();

  const theme = computed(() => {
    return store.getters['setting/theme'];
  });

  const menuBgColor = computed(() => {
    return themeOptions[theme.value].menuBgColor;
  });

  const isBlack = computed(() => {
    return whiteColors.indexOf(menuBgColor.value) === -1;
  });
</script>
<style lang="scss">
  .el-menu--collapse > .el-sub-menu > .el-sub-menu__title > span {
    margin-right: 0 !important;
  }
</style>
<style lang="scss" scoped>
  .menu-icon,
  .icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: $base-icon-width-big !important;
    height: $base-icon-height-super-max !important;
    margin-right: $base-margin-5;
    visibility: initial !important;
  }

  .iconfont {
    width: fit-content !important;
    height: fit-content !important;
    visibility: visible !important;
    font-size: 20px !important;
    margin-right: 8px;
  }

  :deep(.el-sub-menu__title:hover) {
    background-color: #f0fcf9 !important;
  }
</style>
