<!--  author:hu-snail 1217437592@qq.com  标题/描述组件 -->
<template>
  <div class="desc-wrapper">
    <h2 class="title">{{ title }}</h2>
    <div class="desc" v-if="showDesc">
      <slot name="descrition"></slot>
    </div>
  </div>
</template>
<script setup>
  defineProps({
    title: {
      type: String,
      default: '标题',
    },
    descrition: {
      type: String,
      default: '描述',
    },
    showDesc: {
      type: Boolean,
      default: true,
    },
  });
</script>
<style lang="scss" scoped>
  .desc-wrapper {
    .desc {
      padding: $base-content-padding;
      color: $base-color-primary;
      background-color: $base-color-primary-light9;
      border-left: $base-border-width-default solid $base-color-primary;
    }
  }
</style>
