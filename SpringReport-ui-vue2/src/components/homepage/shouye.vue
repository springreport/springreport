<template>
  <div class="home-page">
    <img src="@/static/img/homePage.png" />
  </div>
</template>
<script>
import { mapState, mapGetters, mapActions, mapMutations } from 'vuex'
export default {
  name: 'index',
  data() {
    return {}
  },
  activated() {},
  methods: {},
}
</script>
<style lang='scss' scoped>
@import '~@/static/index/icomoon.css';
@import '~@/static/index/themify-icons.css';
// @import '~@/static/index/bootstrap.css';
@import '~@/static/index/style.css';
.home-page {
  width: 100%;
  max-height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  img {
    height: calc(100vh - 46px - 120px);
    width: auto;
    max-width: 100%;
  }
}
</style>
