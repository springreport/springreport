<template>
  <div class="_tag">
    <el-scrollbar class="ycy_scrollbar">
      <div class="left">
        <div
          v-for="tag in visitedRouteList"
          :key="tag.path"
          class="menu-tag df-c"
          :class="[`${tag.path.replaceAll('/', '')}`]"
          @click="handleTabClick(tag)"
        >
          <span
            class="menu-tag-title"
            :class="[fullPath.split('?')[0] == tag.path ? 'menu-tag-active' : '']"
            >{{ tag.meta.title }}</span
          >
          <div v-if="!isAffix(tag)" class="close" @click.stop="handleTabRemove(tag.path)" />
        </div>
      </div>
    </el-scrollbar>

    <div class="right">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="df-c more-menu">
          <span>更多菜单</span>
          <icon-down style="margin-left: 10px" fill="#333" />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="closeOtherstabs">关闭其它</el-dropdown-item>
            <el-dropdown-item command="closeAlltabs">关闭所有</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
  import { reactive, watch, toRefs, computed, nextTick } from 'vue';
  import { useStore } from 'vuex';
  import { useRouter, useRoute } from 'vue-router';

  export default {
    name: 'TabBar',
    setup() {
      const store = useStore();
      const router = useRouter();

      const route = useRoute();

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

      const fullPath = computed(() => {
        return route.fullPath;
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

      const scrollIntoView = (path) => {
        const currentEle = document.querySelector(`.${path.split('?')[0].replaceAll('/', '')}`);
        if (currentEle) {
          currentEle.scrollIntoView({
            behavior: 'smooth', // 平滑滚动
            block: 'nearest', // 保持垂直位置不变
            inline: 'end', // 水平方向上尽可能接近视口边缘
          });
        }
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

          setTimeout(() => {
            scrollIntoView(router.currentRoute.value.fullPath);
          }, 100);
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
        if (tab.path !== fullPath.value) {
          router.push({
            path: tab.path,
            query: tab.query,
            fullPath: tab.fullPath,
          });
          setTimeout(() => {
            scrollIntoView(tab.path);
          }, 100);
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
        fullPath,
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
  ._tag {
    flex: 0 0 56px;
    display: flex;
    align-items: center;
    background-color: white;
    border-bottom: 1px solid #e6e6e6;
    justify-content: space-between;
    .ycy_scrollbar {
      width: 100%;
      margin-right: 6px;
    }
    ::v-deep .el-scrollbar__wrap {
      overflow-y: hidden;
    }
    .left {
      display: -webkit-box;
      -webkit-box-align: center;
      height: 56px;
      align-items: center;
      // overflow-x: auto;
      // white-space: nowrap; /* 防止Tab项换行 */
      .el-tag {
        height: 32px;
        line-height: 32px;
        margin-left: 8px;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        &:first-child {
          margin-left: 0px;
        }
      }
      .menu-tag {
        padding: 0 16px;
        height: 56px;
        line-height: 56px;
        color: #333;
        font-size: 14px;
        cursor: pointer;
        // transition: all 0.3s;
        position: relative;

        .close {
          margin-left: 4px;
          background-image: url('@/assets/img/common/close.png');
          width: 12px;
          height: 12px;
          background-size: 100% 100%;
        }
      }
      .menu-tag-title:hover,
      .menu-tag-active {
        color: $base-color-primary;
        font-weight: bold;
        position: relative;

        &::before {
          position: absolute;
          content: '';
          left: 50%;
          bottom: 0;
          width: 60%;
          max-width: 62px;
          transform: translate(-50%, -50%);
          height: 3px;
          background: $base-color-primary;
          border-radius: 2px;
        }
      }
    }

    .right {
      height: 56px;
      display: flex;
      align-items: center;
      flex-shrink: 0;
      .more-menu {
        cursor: pointer;
        color: #333;
        font-feature-settings: 'liga' off, 'clig' off;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: 400;
        line-height: normal;
        position: relative;
        padding: 0 14px;

        &:after {
          content: '';
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 1px;
          height: 18px;
          background: rgba(52, 52, 52, 0.1);
        }
      }
    }
  }
</style>
