<template>
  <div class="home-page">
    <div class="workbench">
      <div class="avatar df-c">
        <img src="@/static/img/homePage/avatar.png" class="icon">
        <div class="welcome">
          <div class="welcome-hi">HI , {{ username }}</div>
          <div class="welcome-tip">欢迎进入表单工作台！</div>
        </div>
      </div>
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
    return {
      username: ''
    }
  },
  activated() {},
  mounted() {
    this.username = localStorage.getItem('userName')
  },
  methods: {}
}
</script>
<style lang='scss' scoped>
// @import "~@/static/index/icomoon.css";
@import "~@/static/index/themify-icons.css";
// @import '~@/static/index/bootstrap.css';
@import "~@/static/index/style.scss";
.home-page {
  width: 100%;
  max-height: 100%;
  .workbench {
    width: 100%;
    height: 140px;
    background-image: url("~@/static/img/homePage/home-header.png");
    background-size: 100% 100%;
    padding: 38px 0 0 33px;
    box-sizing: border-box;
    .icon {
      width: 58px;
      height: 58px;
      margin-right: 16px;
    }
    .welcome-hi {
      color: #fff;

      /* 20/CN-Medium */
      font-family: "PingFang SC";
      font-size: 20px;
      font-style: normal;
      font-weight: 500;
      line-height: 28px; /* 140% */
    }
    .welcome-tip {
      color: #fff;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: 28px; /* 200% */
      margin-top: -4px;
    }
  }
}
</style>
