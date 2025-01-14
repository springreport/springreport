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
  import { onMounted } from 'vue';
  import { useStore } from 'vuex';
  const store = useStore();
  const handleCollapse = () => {
    store.dispatch('setting/changeCollapse');
  };
  const isCollapse = computed(() => {
    return store.getters.collapse;
  });

  const checkScreenWidth = () => {
    const navShow = window.innerWidth < 1300;
    store.dispatch('setting/changeCollapse', navShow);
  };

  const debounce = (fn, delay) => {
    let timer = null;
    return function () {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(fn, delay);
    };
  };

  onMounted(() => {
    checkScreenWidth();
    // 屏幕宽度变化时，重新计算菜单宽度
    window.addEventListener(
      'resize',
      debounce(function () {
        checkScreenWidth();
      }, 300)
    );
  });

  onUnmounted(() => {
    window.removeEventListener('resize', checkScreenWidth());
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
