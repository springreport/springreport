<template>
  <div class="right-panel">
    <icon-refresh
      title="刷新"
      @click="handleRefresh"
      class="icon-hover refresh"
      theme="filled"
      size="16"
      :fill="color"
      :strokeWidth="4"
    />
    <FullScreen :color="color" @refresh="onRefresh" />

    <Avatar :color="color" />
  </div>
</template>

<script>
  export default {
    name: 'RightPanel',
  };
</script>

<script setup>
  import FullScreen from '@/components/FullScreen/index.vue';

  import { useStore } from 'vuex';

  import { computed, nextTick, ref } from 'vue';
  defineProps({
    color: {
      type: String,
      default: '#666',
    },
  });

  const store = useStore();

  let activeName = ref('first');

  const onRefresh = () => {};

  const handleRefresh = () => {
    store.dispatch('setting/setRouterView', false);
    nextTick(() => {
      store.dispatch('setting/setRouterView', true);
    });
  };

  const handleChangeTheme = () => {
    store.dispatch('setting/setSettingDrawer', true);
  };
</script>

<style lang="scss" scoped>
  .right-panel {
    display: flex;
    align-content: center;
    align-items: center;
    justify-content: flex-end;
    height: $base-nav-bar-height;
    .msg-badge {
      :deep(.el-badge__content.is-fixed) {
        right: calc(10px + var(--el-badge-size) / 2);
      }
    }
    .refresh,
    .theme {
      padding: $base-padding-20-10;
    }
  }
  .message-box {
    padding: $base-padding-5-15;
    :deep(.el-tabs__active-bar) {
      width: $base-tab-width_active !important;
    }
  }
</style>
