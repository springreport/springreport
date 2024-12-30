<template>
  <div class="_homepage">
    <el-container v-if="!ycyShow" class="ycy-container is-vertical">
      <v-Header />
      <el-container>
        <v-Aside />
        <el-container class="is-vertical ycy-tag-container right">
          <v-Tag />
          <el-container class="scroll is-vertical">
            <el-main class="ycy-main">
              <transition name="fade" mode="out-in">
                <keep-alive>
                  <router-view class="view" />
                </keep-alive>
              </transition>
            </el-main>
            <v-Footer />
          </el-container>
        </el-container>
      </el-container>
    </el-container>

    <section v-else class="headContainer">
      <v-Header />
      <section class="asideContainer">
        <v-Aside class="aside" />
        <section class="tagContainer">
          <v-Tag />
          <div class="mainContainer">
            <main class="main">
              <router-view />
            </main>
            <v-Footer class="footer" />
          </div>
        </section>
      </section>
    </section>
  </div>
</template>

<script>
import vAside from './aside.vue'
import vFooter from './footer.vue'
import vHeader from './header.vue'
import vTag from './tag.vue'
export default {
  components: {
    vAside,
    vFooter,
    vHeader,
    vTag
  },
  data() {
    return {
      ycyShow: false // 切换手写布局样式
    }
  }
}
</script>

<style lang="scss">
._homepage {
  background: #f3f3f3;
  // elementui布局样式
  .ycy-container {
    height: 100vh;
    .ycy-tag-container {
      overflow: auto;
      .scroll {
        height: calc(100vh - 160px);
        overflow: auto;
        .ycy-main {
          overflow: visible;
          padding: 8px 8px 0px 8px;
        }
      }
    }
  }
  // 手写flex布局样式
  .headContainer {
    height: 100vh;
    display: flex;
    flex-flow: column nowrap;
    .asideContainer {
      display: flex;
      flex-flow: row nowrap;
      flex: auto;
      .aside {
        flex: none;
      }
      .tagContainer {
        // border:1px solid blue;
        flex: auto;
        display: flex;
        flex-flow: column nowrap;
        min-width: 0; //保证内容不会超出外层容器
        overflow: auto;
        .mainContainer {
          // border:1px solid red;
          background-color: #f0f0f0; //灰色背景
          flex: auto;
          display: flex;
          flex-flow: column nowrap;
          box-sizing: border-box;
          height: calc(100vh - 100px);
          overflow: auto;
          padding: 10px 10px 0 10px;
          .main {
            // border:1px solid green;
            flex: auto;
            background-color: white;
            box-sizing: border-box;
            padding: 10px;
          }
          .footer {
            flex: 0 0 60px;
          }
        }
      }
    }
  }

  // 路由切换动画特效
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.1s;
  }
  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
}
</style>

