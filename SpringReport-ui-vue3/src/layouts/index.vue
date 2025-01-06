<template>
  <div class="admin-container">
    <el-container>
      <Menu :isCollapse="isCollapse" class="hidden-xs-only" @handleCollapse="handleCollapse" />
      <el-container class="container" :style="{ left: isCollapse ? '64px' : '228px' }">
        <el-header
          class="header"
          :class="{ fixed: true }"
          height="64px"
          :style="{ left: isCollapse ? '64px' : '228px' }"
        >
          <NavBar />
          <TabBar />
        </el-header>
        <el-main class="main" :class="{ fixed: true }">
          <AppMain />
        </el-main>
      </el-container>
    </el-container>
    <el-backtop />
  </div>
</template>

<script setup>
  import { useStore } from 'vuex';
  const store = useStore();
  const handleCollapse = () => {
    store.dispatch('setting/changeCollapse');
  };
  const isCollapse = computed(() => {
    return store.getters.collapse;
  });
</script>

<style lang="scss" scoped>
  .admin-container {
    position: relative;
    background-color: $base-content-bg-color;
    padding-bottom: 0 !important;
    .container {
      position: absolute;
      right: 0;
      transition: all $base-transition-time-4;
    }
    .header {
      padding: 0;
      transition: all $base-transition-time-4;
      &.fixed {
        position: fixed;
        top: 0;
        right: 0;
        z-index: $base-z-index-999;
      }
    }
    .main {
      position: relative;
      top: $base-main-vertical-top;
      overflow-y: auto;
      padding: 8px 8px 0 8px;
      &.fixed {
        top: $base-main-fixed-top;
      }
      &[class='el-main main fixed notag'] {
        top: $base-main-vertical-fixed-notag-top;
      }
      &[class='el-main main notag'] {
        top: $base-main-notag-top;
      }
      background-color: $base-content-bg-color;
    }
  }
</style>
<style>
  .el-main {
    padding-bottom: 0 !important;
  }
</style>
