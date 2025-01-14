<template>
  <el-scrollbar height="100vh" class="aside">
    <el-menu
      :default-active="defaultActive"
      :default-openeds="defaultOpened"
      :unique-opened="uniqueOpenedFlag"
      background-color="#ffffff"
      class="el-menu-vertical"
      :collapse="isCollapse"
      text-color="rgba(0, 0, 0, 0.9)"
      router
      :mode="mode"
      @open="handleOpen"
      @close="handleClose"
    >
      <div class="logo df-c">
        <div>
          <img
            v-show="!isCollapse"
            src="@/assets/img/logoWithName.png"
            style="margin-left: 20px; height: 30px; width: auto"
          />
          <img
            v-show="isCollapse"
            src="@/assets/img/logo.png"
            style="margin-left: 20px; height: 24px; width: auto"
          />
        </div>
      </div>
      <div v-if="!isCollapse" class="filter-menu">
        <el-input v-model="filterText" style="width: 100%" placeholder="搜索菜单" clearable>
          <template #prefix>
            <icon-search />
          </template>
        </el-input>
      </div>
      <template v-for="item in displayMenuData">
        <template v-if="!item.hidden">
          <MenuItem :item="{ ...item, isBlack }" :key="item.path" />
        </template>
      </template>
      <div class="collapse-btn df-c-c" @click="handleCollapse()">
        <img
          src="@/assets/img/menu-fold.png"
          v-show="isCollapse"
          style="height: 16px; width: auto"
        />
        <img
          src="@/assets/img/menu-unfold.png"
          v-show="!isCollapse"
          style="height: 16px; width: auto"
        />
      </div>
    </el-menu>
  </el-scrollbar>
</template>

<script>
  export default {
    name: 'Menu',
  };
</script>

<script setup>
  import { computed, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { useStore } from 'vuex';

  import { setting } from '@/config/setting';
  const { defaultOpeneds, uniqueOpened } = setting;

  import { themeConfig } from '@/config/theme';
  const { themeOptions } = themeConfig;

  const whiteColors = ['#fff', '#ffffff', '#FFF', '#FFF', 'rgb(255, 255, 255)'];
  import commonUtil from '@/components/common/common.js';
  import apis from '@/components/common/api.js';
  defineProps({
    isCollapse: {
      type: Boolean,
      default: false,
    },
    mode: {
      type: String,
      default: 'vertical',
    },
  });

  const uniqueOpenedFlag = ref(uniqueOpened);

  const filterText = ref('');

  const store = useStore();
  const router = useRouter();

  const theme = computed(() => {
    return store.getters['setting/theme'];
  });

  const menuBgColor = computed(() => {
    return themeOptions[theme.value].menuBgColor;
  });

  const displayMenuData = computed(() => {
    if (filterText.value) {
      return routes.value
        .map((group) => ({
          ...group,
          subs: group.subs.filter((item) => item.title.includes(filterText.value)),
        }))
        .filter((group) => group.subs.length > 0); // 只保留有匹配项的组
    }
    return routes.value;
  });

  const isBlack = computed(() => {
    return whiteColors.indexOf(menuBgColor.value) === -1;
  });

  const textColor = computed(() => {
    return whiteColors.indexOf(menuBgColor.value) !== -1 ? '#333' : '#fff';
  });

  const activeTextColor = computed(() => {
    const mcolor = whiteColors.indexOf(menuBgColor.value) !== -1;
    return mcolor ? theme : '#fff';
  });

  const routes = reactive({
    value: [],
  });
  onMounted(() => {
    var object = {
      url: apis.index.getIndexMenuApi,
      params: {},
    };
    commonUtil.doPost(object).then((response) => {
      routes.value = response.responseData;
    });
  });
  const isLogo = computed(() => {
    return store.getters['setting/isLogo'];
  });

  const defaultOpened = computed(() => {
    return defaultOpeneds;
  });

  const defaultActive = computed(() => {
    const { fullPath } = router.currentRoute.value;
    return fullPath || '/index';
  });

  const handleOpen = (key, keyPath) => {
    console.log('open:', key, keyPath);
  };

  const handleClose = (key, keyPath) => {
    console.log('close:', key, keyPath);
  };

  const emit = defineEmits(['handleCollapse']);

  const handleCollapse = () => {
    emit('handleCollapse');
  };
</script>

<style lang="scss" scoped>
  .filter-menu {
    padding: 12px 8px;
    background-color: #fff;
    box-sizing: border-box;
    :deep(.el-input__wrapper) {
      width: 100%;
      max-width: calc(228px - 16px);
      border-radius: 6px;
      background: #f5f6f7;
      border-color: #f5f6f7;
    }
  }
  .el-menu-vertical {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    height: 100vh;
    overflow-x: hidden;
    overflow-y: auto;
    @include base-scrollbar;
    &:not(.el-menu--collapse) {
      width: $base-menu-width;
      padding: 0 8px;
      box-sizing: border-box;
    }
  }
  .logo {
    height: $base-nav-bar-height;
    border-bottom: 1px solid #f6f6f6;
    transition: all 0.3s;
  }
  .aside {
    position: relative;
    .collapse-btn {
      position: absolute;
      bottom: 0;
      left: 0;
      background: #ffffff;
      box-shadow: inset 0px 1px 0px #f0f0f0;
      width: 100%;
      height: $base-nav-bar-height;
      z-index: 8000;
      padding-left: 0;
      box-sizing: border-box;
      img {
        cursor: pointer;
        transition: all 0.3s;
        &:hover {
          background-color: rgba(0, 0, 0, 0.1);
        }
      }
    }
  }
</style>
