<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'App',
  mounted(){
  },
  watch:{
    '$route': {
      immediate: true,
      deep: true,
      handler(val){
        let token = val.query.token;
        if(token)
        {
            localStorage.setItem(this.commonConstants.sessionItem.authorization, token);
        }
      }
    }
}
}
</script>

<style>
/*基础样式 start*/
html,
body,
#app {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
* {
  margin: 0;
  padding: 0;
}
a {
  text-decoration: none;
}
/*基础样式 end*/
</style>
