<template>
  <div class="home-page">
    <div class="workbench">
      <img src="@/static/img/homePage/home-header.png" style="width: 100%">
    </div>
    <!-- 报表摘要 -->
    <Statement />
    <!-- 待办任务 -->
    <ToDoTask />
  </div>
</template>
<script>
import Statement from './shouyeComponents/Statement'
import ToDoTask from './shouyeComponents/ToDoTask'
// import { mapState, mapGetters, mapActions, mapMutations } from 'vuex'
export default {
  name: 'Index',
  components: { Statement, ToDoTask },
  data() {
    return {}
  },
  activated() {},
  methods: {}
}
</script>
<style lang='scss' scoped>
@import "~@/static/index/icomoon.css";
@import "~@/static/index/themify-icons.css";
// @import '~@/static/index/bootstrap.css';
@import "~@/static/index/style.scss";
.home-page {
  width: 100%;
  max-height: 100%;
  .workbench {
    width: 100%;
  }
}
</style>
