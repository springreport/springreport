<template>
  <div class="login">
    <div class="login__left df-c-c">
      <img src="@/static/img/loginIcon.png" style="width: 500px">
    </div>
    <div class="login__right df-c-c">
      <div class="login-box">
        <div class="df-c">
          <img src="@/static/img/logoWithName.png" height="48px">
          <el-divider direction="vertical" />
          <div class="welcome">欢迎登录</div>
        </div>
        <el-form ref="form" :model="formLogin" class="login-form">
          <el-form-item v-if="merchantMode == 1" prop="merchantNo" label="商户号" :rules="filter_rules('商户号', { required: true })">
            <el-input v-model="formLogin.merchantNo" placeholder="商户号" />
          </el-form-item>
          <el-form-item prop="loginName" label="账号" :rules="filter_rules('账号', { required: true })">
            <el-input v-model="formLogin.loginName" placeholder="账号" />
          </el-form-item>
          <el-form-item prop="password" label="密码" :rules="filter_rules('密码', { required: true })">
            <el-input v-model="formLogin.password" type="password" placeholder="密码" show-password />
          </el-form-item>
          <el-form-item style="margin-top: 56px">
            <el-button type="primary" style="width: 100%" @click="login">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="filing-information df-c-c">
        <a
          target="_blank"
          href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=37021302001006"
        >鲁公网安备 37021302001006号</a>
        <img src="@/static/img/aicon.png" class="beian-icon">
        <a target="_blank" href="https://beian.miit.gov.cn/">鲁ICP备2021031250号-2</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      merchantMode: 2, // 是否是租户模式 1是 2否
      formLogin: {
        merchantNo: 'SR00000000',
        loginName: 'test',
        password: '123456'
      }
    }
  },
  mounted() {
    // let userAccount = this.getCookie('userName');
    // this.$refs['form'].resetFields();//校验重置
    // this.formLogin.loginName = userAccount;
    this.getMerchantMode()
    window.addEventListener('keydown', this.keyDown)
  },
  destroyed() {
    window.removeEventListener('keydown', this.keyDown, false)
  },
  methods: {
    getMerchantMode() {
      var object = {
        url: this.apis.login.getMerchantModeApi,
        params: {},
        removeEmpty: false
      }
      var that = this
      this.commonUtil.doPost(object).then((response) => {
        if (response.code === '200') {
          that.merchantMode = response.responseData
        }
      })
    },
    // 提交登录
    login() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          var param = {
            userName: this.formLogin.loginName,
            password: this.$md5(this.formLogin.password),
            merchantNo: this.formLogin.merchantNo
          }
          var object = {
            url: this.apis.login.loginApi,
            params: param,
            removeEmpty: false
          }
          this.commonUtil.doPost(object).then((response) => {
            if (response.code === '200') {
              var responseData = response.responseData
              localStorage.setItem(
                this.commonConstants.sessionItem.position,
                responseData.roleName
              ) // 用户职位
              localStorage.setItem(
                this.commonConstants.sessionItem.userName,
                responseData.userName
              ) // 用户名
              localStorage.setItem(
                this.commonConstants.sessionItem.roleName,
                responseData.roleName
              ) // 用户名
              localStorage.setItem(
                this.commonConstants.sessionItem.apiList,
                responseData.apis
              ) // 接口权限，用于判断页面按钮是否显示
              localStorage.setItem(
                this.commonConstants.sessionItem.authorization,
                responseData.token
              )
              localStorage.setItem(
                this.commonConstants.sessionItem.isSystemMerchant,
                responseData.isSystemMerchant
              )
              localStorage.setItem(
                this.commonConstants.sessionItem.merchantNo,
                responseData.merchantNo
              )
              localStorage.setItem(
                this.commonConstants.sessionItem.isAdmin,
                responseData.isAdmin
              )
              localStorage.setItem(
                this.commonConstants.sessionItem.userId,
                responseData.userId
              )
              this.$router.replace({ path: '/index' })
              // this.setCookie('userAccount',responseData.userAccount);
            }
          })
        } else {
          return false
        }
      })
    },
    // 设置cookie
    setCookie(key, value) {
      // 字符串拼接cookie
      window.document.cookie = key + '=' + value
    },
    getCookie(key) {
      if (window.document.cookie.length > 0) {
        var arr = document.cookie.split('; ')
        for (var i = 0; i < arr.length; i++) {
          var arr2 = arr[i].split('=') // 再次切割
          // 判断查找相对应的值
          if (arr2[0] === key) {
            return arr2[1]
          }
        }
      }
    },

    keyDown(e) {
      // 如果是回车则执行登录方法
      if (e.keyCode == 13) {
        this.login()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.login {
  width: 100vw;
  padding: 0;
  margin: 0;
  height: 100vh;
  font-size: 16px;
  position: relative;
  display: flex;
  &__left {
    min-width: 675px;
    flex: 0.8;
    height: 100%;
    background-size: 100% 100%;
    background-image: url('~@/static/img/loginLeftBg.png');
    background-repeat: no-repeat;
  }
  &__right {
    flex: 1;
    position: relative;
    .login-box {
      .welcome {
        height: 70px;
        font-style: normal;
        font-weight: 600;
        font-size: 30px;
        line-height: 70px;
        color: rgba(0, 0, 0, 0.9);
      }
      .login-form {
        margin-top: 20px;
        /deep/.el-form-item__label {
          &::before {
            content: unset;
          }
        }
      }
    }

    .filing-information {
      position: absolute;
      bottom: 50px;
      width: 100%;
      a {
        color: rgba(0, 0, 0, 0.4);
        font-size: 12px;
        line-height: 16px;
        height: 16px;
      }
      .beian-icon {
        width: 16px;
        height: 16px;
        margin-left: 8px;
      }
    }
  }

  #bgd {
    height: 100vh;
    width: 100vw;
    overflow: hidden;
  }

  h2 {
    color: #d3d7f7;
  }
}
</style>
