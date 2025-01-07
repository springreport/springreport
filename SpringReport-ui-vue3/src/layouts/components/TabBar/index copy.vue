<template>
  <div id="tabs-bar-container" class="tabs-bar-container" :class="{ horizontal: true }">
    <el-tabs
      v-model="tabActive"
      type="card"
      class="tabs-content"
      @tab-click="handleTabClick"
      @tab-remove="handleTabRemove"
    >
      <el-tab-pane
        v-for="item in visitedRouteList"
        :key="item.path"
        :name="item.path"
        :closable="!isAffix(item)"
      >
        <template #label>
          <div class="item">
            <component
              class="menu-icon"
              v-if="item.meta.icon"
              theme="outline"
              strokeWidth="3"
              :is="item.meta.icon"
            />
            <span>
              {{ item.meta.title }}
            </span>
          </div>
        </template>
      </el-tab-pane>
    </el-tabs>
    <el-popover
      placement="bottom"
      width="auto"
      trigger="hover"
      @show="handleShow"
      @hide="handleHide"
    >
      <template #reference>
        <span class="more" :class="{ active: visible }" style="cursor: pointer">
          <icon-all-application theme="filled" size="18" :strokeWidth="3" />
        </span>
      </template>
      <div
        class="command-item"
        v-for="(item, index) in commandList"
        :key="index"
        @click="handleCommand(item.command)"
      >
        <component class="icon" theme="filled" size="14" :strokeWidth="3" :is="item.icon" />
        <span class="command-label">{{ item.text }}</span>
      </div>
    </el-popover>
  </div>
</template>

<script>
  import { reactive, watch, toRefs, computed, nextTick } from 'vue';
  import { useStore } from 'vuex';
  import { useRouter } from 'vue-router';

  export default {
    name: 'TabBar',
    setup() {
      const store = useStore();
      const router = useRouter();

      const state = reactive({
        affixtabs: [],
        tabActive: '',
        visible: false,
        commandList: [
          {
            command: 'refreshRoute',
            text: '重新加载',
            icon: 'icon-refresh',
          },
          {
            command: 'closeOtherstabs',
            text: '关闭其它',
            icon: 'icon-close',
          },
          {
            command: 'closeLefttabs',
            text: '关闭左侧',
            icon: 'icon-to-left',
          },
          {
            command: 'closeRighttabs',
            text: '关闭右侧',
            icon: 'icon-to-right',
          },
          {
            command: 'closeAlltabs',
            text: '关闭所有',
            icon: 'icon-minus',
          },
        ],
      });

      const visitedRouteList = computed(() => {
        return store.getters['tabsBar/visitedRoutes'];
      });

      const routes = computed(() => {
        return store.getters['routes/routes'];
      });

      const addtabs = () => {
        let target = router.resolve({ name: 'Index' });
        if (target) {
          store.dispatch('tabsBar/addVisitedRoute', target);
        }
        const { name } = router.currentRoute.value;
        if (name && name != 'tempRefresh') {
          store.dispatch('tabsBar/addVisitedRoute', router.currentRoute.value);
        }
        return false;
      };

      watch(
        () => router.currentRoute.value,
        () => {
          addtabs();
          let tabActiveR = '';
          visitedRouteList.value.forEach((item) => {
            if (item.path === router.currentRoute.value.path) {
              tabActiveR = item.path;
            }
          });
          state.tabActive = tabActiveR;
        },
        { immediate: true }
      );

      const isActive = (route) => {
        return route.path === router.currentRoute.value.path;
      };
      const isAffix = (tag) => {
        return tag.meta && tag.meta.affix;
      };

      const toLastTag = (visitedRoutes) => {
        const latestView = visitedRoutes.slice(-1)[0];
        if (latestView) {
          router.push(latestView);
        } else {
          router.push('/');
        }
      };

      const handleTabRemove = async (tabActive) => {
        let view;
        visitedRouteList.value.forEach((item) => {
          if (tabActive == item.path) {
            view = item;
          }
        });
        const { visitedRoutes } = await store.dispatch('tabsBar/delRoute', view);
        if (isActive(view)) {
          toLastTag(visitedRoutes, view);
        }
      };

      const handleTabClick = (tab) => {
        const route = visitedRouteList.value.filter((item, index) => {
          if (tab.index == index) return item;
        })[0];
        if (router.currentRoute.value.path !== route.path) {
          router.push({
            path: route.path,
            query: route.query,
            fullPath: route.fullPath,
          });
        } else {
          return false;
        }
      };

      const refreshRoute = async () => {
        store.dispatch('setting/setRouterView', false);
        nextTick(() => {
          store.dispatch('setting/setRouterView', true);
        });
      };

      const closeOtherstabs = async () => {
        const view = await toThisTag();
        await store.dispatch('tabsBar/delOthersRoutes', view);
      };
      const closeLefttabs = async () => {
        const view = await toThisTag();
        await store.dispatch('tabsBar/delLeftRoutes', view);
      };
      const closeRighttabs = async () => {
        const view = await toThisTag();
        await store.dispatch('tabsBar/delRightRoutes', view);
      };
      const closeAlltabs = async () => {
        const view = await toThisTag();
        const { visitedRoutes } = await store.dispatch('tabsBar/delAllRoutes');
        if (state.affixtabs.some((tag) => tag.path === view.path)) {
          return;
        }
        toLastTag(visitedRoutes, view);
      };

      const toThisTag = async () => {
        const { fullPath, path } = router.currentRoute.value;

        const view = visitedRouteList.value.filter((item) => {
          if (item.path === fullPath) {
            return item;
          }
        })[0];
        if (path !== view.path) router.push(view);
        return view;
      };

      const handleCommand = (command) => {
        switch (command) {
          case 'refreshRoute':
            refreshRoute();
            break;
          case 'closeOtherstabs':
            closeOtherstabs();
            break;
          case 'closeLefttabs':
            closeLefttabs();
            break;
          case 'closeRighttabs':
            closeRighttabs();
            break;
          case 'closeAlltabs':
            closeAlltabs();
            break;
          default:
            return '错误的事件类型';
        }
      };

      const handleShow = () => {
        state.visible = true;
      };

      const handleHide = () => {
        state.visible = false;
      };

      return {
        ...toRefs(state),
        visitedRouteList,
        routes,
        isAffix,
        refreshRoute,
        closeAlltabs,
        closeRighttabs,
        closeLefttabs,
        closeOtherstabs,
        handleTabClick,
        handleTabRemove,
        handleCommand,
        handleShow,
        handleHide,
      };
    },
  };
</script>

<style lang="scss" scoped>
  .tabs-bar-container {
    position: relative;
    box-sizing: border-box;
    display: flex;
    align-content: center;
    align-items: center;
    justify-content: space-between;
    height: $base-tabs-bar-height;
    padding-right: $base-padding;
    padding-left: $base-padding;
    user-select: none;
    background: $base-color-white;
    border-top: 1px solid #f6f6f6;
    &.horizontal {
      padding: 0 40px;
    }
    :deep(.fold-unfold) {
      margin-right: $base-padding;
    }
    :deep(.el-tabs__item) {
      display: inline-flex;
      align-items: center;
    }
    .item {
      display: inline-flex;
      align-items: center;
      .menu-icon {
        display: flex;
        padding-right: $base-margin-5;
      }
    }

    .tabs-content {
      width: calc(100% - 90px);
      height: $base-tag-item-height;
      :deep(.el-tabs__nav-next, .el-tabs__nav-prev) {
        height: $base-tag-item-height;
        line-height: $base-tag-item-height;
      }
      :deep(.el-tabs__header) {
        border-bottom: 0;
        .el-tabs__nav {
          border: 0;
          .el-tabs__item {
            box-sizing: border-box;
            height: $base-tag-item-height;
            margin-right: $base-margin-5;
            line-height: $base-tag-item-height;
            border: none;
            border-radius: $base-border-radius;
            transition: padding 0.5s cubic-bezier(0.645, 0.045, 0.355, 1) !important;
            &.is-active {
              color: $base-color-primary;
              background: $base-color-primary-light9;
              border: none;
              border-bottom: 2px solid;
            }
            &:hover {
              color: $base-color-primary;
              background: $base-color-primary-light9;
              border: none;
              border-bottom: 2px solid;
            }
          }
        }
      }
    }
  }
  .command-item {
    display: flex;
    align-content: center;
    align-items: center;
    padding: 5px 10px;
    cursor: pointer;
    .command-label {
      padding-left: 5px;
    }
    &:hover {
      color: $base-color-primary;
      background-color: $base-color-primary-light9;
    }
    .icon {
      display: flex;
    }
  }
  .more {
    display: flex;
    align-content: center;
    align-items: center;
    color: $base-font-color;
    cursor: pointer;
    transition: all 0.5s;
    &.active {
      color: $base-color-primary !important;
      transform: rotate(180deg);
    }
  }
</style>
