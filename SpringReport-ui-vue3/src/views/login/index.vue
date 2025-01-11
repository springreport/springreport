<template>
  <div class="login">
    <div class="login__left df-c-c">
      <img src="@/assets/img/loginIcon.png" style="width: 500px" />
    </div>
    <div class="login__right df-c-c">
      <div class="login-box">
        <div class="df-c">
          <img src="@/assets/img/logoWithName.png" style="width: auto; height: 48px" />
          <el-divider direction="vertical" />
          <div class="welcome">欢迎登录</div>
        </div>
        <LoginForm />
      </div>
      <div class="filing-information df-c-c">
        <a
          target="_blank"
          href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=37021302001006"
          >鲁公网安备 37021302001006号</a
        >
        <img src="@/assets/img/aicon.png" class="beian-icon" />
        <a target="_blank" href="https://beian.miit.gov.cn/">鲁ICP备2021031250号-2</a>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed } from 'vue';
  import { useStore } from 'vuex';
  import LoginForm from 'views/login/comp/LoginForm.vue';

  const store = useStore();
  const activeName = ref('first');
  const isMobile = computed(() => {
    return store.getters['setting/isMobile'];
  });

  const handleClick = (val) => {};
</script>

<style lang="scss" scoped>
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
      background-image: url('@/assets/img/loginLeftBg.png');
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
          text-decoration: none;
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
