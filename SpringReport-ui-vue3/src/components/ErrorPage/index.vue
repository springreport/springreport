<template>
  <div class="errorPage-container">
    <el-row :gutter="10" class="error-row">
      <el-col class="error-col" :xs="24" :sm="24" :md="10" :lg="10" :xl="10">
        <div class="error-img">
          <img v-if="type === '401'" src="@/assets/error/401.svg" class="img" :alt="type" />
          <img v-if="type === '404'" src="@/assets/error/404.svg" class="img" :alt="type" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="10" :lg="10" :xl="10">
        <div class="error-content">
          <h2 class="error-title">抱歉!</h2>
          <h3>{{ title }}</h3>
          <p class="desc"> {{ msg }} </p>
          <div class="btn">
            <el-button type="primary" @click="handleBack">返回首页</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { useRouter } from 'vue-router';
  export default {
    props: {
      src: {
        type: String,
        default: '',
      },
      type: {
        type: String,
        default: '',
      },
      title: {
        type: String,
        default: '',
      },
      msg: {
        type: String,
        default: '',
      },
    },
    setup() {
      const router = useRouter();
      const handleBack = () => {
        router.replace('/');
      };
      return {
        handleBack,
      };
    },
  };
</script>

<style lang="scss" scoped>
  .errorPage-container {
    display: flex;
    align-items: center;
    padding: $base-main-padding;
    background-color: $base-color-white;
    .error-row {
      flex: 1;
      justify-content: center;
      .error-col {
        text-align: center;
      }
      .error-img {
        width: 100%;
        .img {
          width: 80%;
        }
      }
      .error-content {
        padding: 40px 20px;
        .error-title {
          font-size: 36px;
          color: $base-color-primary;
        }
        .desc {
          width: 290px;
          line-height: 1.5;
        }
        .btn {
          margin: 20px 0;
        }
      }
    }
  }
</style>
