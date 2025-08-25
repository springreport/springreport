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
    // console.log(1)
  },
  methods:{
    getUserInfoByToken(){
      var object = {
          url: this.apis.login.getUserInfoByTokenApi,
          params: {},
          removeEmpty: false,
        }
        var that = this;
        this.commonUtil.doPost(object).then((response) => {
            if (response.code === '200') {
               var responseData = response.responseData
              localStorage.setItem(
                that.commonConstants.sessionItem.position,
                responseData.roleName
              ) // 用户职位
              localStorage.setItem(
                that.commonConstants.sessionItem.userName,
                responseData.userName
              ) // 用户名
              localStorage.setItem(
                that.commonConstants.sessionItem.roleName,
                responseData.roleName
              ) // 用户名
              localStorage.setItem(
                that.commonConstants.sessionItem.apiList,
                responseData.apis
              ) // 接口权限，用于判断页面按钮是否显示
              localStorage.setItem(
                that.commonConstants.sessionItem.isSystemMerchant,
                responseData.isSystemMerchant
              ) 
              localStorage.setItem(
                that.commonConstants.sessionItem.merchantNo,
                responseData.merchantNo
              ) 
              localStorage.setItem(
                that.commonConstants.sessionItem.isAdmin,
                responseData.isAdmin
              )
              localStorage.setItem(
                that.commonConstants.sessionItem.userId,
                responseData.userId
              )
            }
        })
    }
  },
  watch:{
    '$route': {
      immediate: true,
      deep: true,
      handler(val){
        let token = val.query.token;
        let thirdPartyType = val.query.thirdPartyType;
        if(thirdPartyType){
          localStorage.setItem(this.commonConstants.sessionItem.thirdPartyType, thirdPartyType);
        }else{
          localStorage.removeItem(this.commonConstants.sessionItem.thirdPartyType)
        }
        if(token)
        {
            localStorage.setItem(this.commonConstants.sessionItem.authorization, token);
            this.getUserInfoByToken();
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
