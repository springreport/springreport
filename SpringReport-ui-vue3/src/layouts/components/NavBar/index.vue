<template>
  <div class="nav-bar-container">
    <!-- <el-row :gutter="15">
      <el-col :xs="4" :sm="12" :md="12" :lg="12" :xl="12" v-if="settings.mode !== ''">
        <div class="left-panel">
          <template v-if="isBreadcrumb">
            <Breadcrumb class="hidden-xs-only" />
          </template>
        </div>
      </el-col>
      <el-col :xs="20" :sm="12" :md="12" :lg="12" :xl="12">
        <RightPanel />
      </el-col>
    </el-row> -->
    <RightPanel />
  </div>
</template>

<script>
  export default {
    name: 'NavBar',
  };
</script>

<script setup>
  import { computed } from 'vue';
  import { useStore } from 'vuex';

  const store = useStore();

  const collapse = computed(() => {
    return store.getters.collapse;
  });

  const isBreadcrumb = computed(() => {
    return store.getters['setting/isBreadcrumb'];
  });

  const settings = computed(() => {
    return store.getters['setting/settings'];
  });

  // const emit = defineEmits(['handleCollapse']);

  // const handleCollapse = () => {
  //   emit('handleCollapse');
  // };
</script>

<style lang="scss" scoped>
  .nav-bar-container {
    position: relative;
    height: $base-nav-bar-height;
    padding-right: 24px;
    overflow: hidden;
    user-select: none;
    background: $base-color-white;
    box-shadow: $base-box-shadow;
    .left-panel {
      display: flex;
      align-items: center;
      justify-items: center;
      height: $base-nav-bar-height;
      .fold-unfold {
        color: $base-color-gray;
        cursor: pointer;
      }
      .fold {
        padding: $base-padding-20-10;
      }
      :deep(.breadcrumb-container) {
        margin-left: $base-margin-10;
      }
    }
  }
</style>
