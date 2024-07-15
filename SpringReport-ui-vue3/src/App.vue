<template>
  <el-config-provider :locale="locale">
    <el-scrollbar height="100vh" ref="scroll">
      <router-view></router-view>
    </el-scrollbar>
  </el-config-provider>
</template>

<script setup>
  import { onMounted, computed, ref, watch } from 'vue';
  import { useStore } from 'vuex';

  import { useRouter } from 'vue-router';
  // import zhCn from 'element-plus/lib/locale/lang/zh-cn';
  import zhCn from "element-plus/es/locale/lang/zh-cn";

  const store = useStore();


  const scroll = ref(null);

  const router = useRouter();
  onMounted(() => {
    changeBodyWidth();
    window.addEventListener('resize', changeResize);
  });
const { locale } = reactive({
  locale: zhCn,
});
  watch(
    () => router.currentRoute.value,
    () => {
      scroll.value.setScrollTop(0);
      let token = router.currentRoute.value.query.token;
      if(token && !localStorage.getItem("token"))
      {
        localStorage.setItem("token",token);
      }
    },
  );

  const changeBodyWidth = () => {
    const flag = document.body.getBoundingClientRect().width - 1 < 992;
    store.dispatch('setting/changeMobile', flag);
  };

  const changeResize = () => {
    changeBodyWidth();
  };
</script>

<style lang="scss">
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    font-size: $base-font-size-default;
    color: #2c3e50;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
</style>
