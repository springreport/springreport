<template>
  <span class="icon-hover full-screen-wrapper" :title="isFullScreen ? '退出全屏' : '全屏'">
    <component
      theme="filled"
      size="16"
      :fill="color"
      :strokeWidth="4"
      :is="(isFullScreen ? 'icon-off' : 'icon-full') + '-screen'"
      @click="handleClick"
    />
  </span>
  <div class="line" />
  <div class="tem df-c" @click="goTemStore" >
    <img src="@/assets/img/template/tem.png" style="width: 24px; height: 24px; display: block" />
    <div class="tem-name">模板市场</div>
  </div>
  <div class="line" />
  <div v-if="state.isSystemMerchant == 1" class="df-c">
    <el-dropdown class="white font" trigger="click" placement="bottom" @command="changeMerchant">
      <div class="system-merchant df-c">
        <span style="margin-right: 8px">{{ merchantName }}</span>
        <icon-sort-two theme="outline" size="12" style="transform: rotate(90deg)" />
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="item in state.merchants"
            :key="item.merchantNo"
            :command="item.merchantNo"
            >{{ item.merchantName }}</el-dropdown-item
          >
        </el-dropdown-menu>
      </template>
    </el-dropdown>

    <div class="line" />
  </div>
</template>

<script setup>
  import { reactive, computed, onMounted } from 'vue';
  import screenfull from 'screenfull';
  import { useStore } from 'vuex';
  import { ElMessage } from 'element-plus';
  import { useRouter } from 'vue-router';
  import commonUtil from '../common/common';
  import apis from '../common/api';
  import commonConstants from '../common/constants';
  const store = useStore();
  const state = reactive({
    isSystemMerchant: 2,
    merchants: [],
    merchantNo: '',
  });
  const router = useRouter();
  defineProps({
    color: {
      type: String,
      default: '#666',
    },
  });

  onMounted(() => {
    state.isSystemMerchant = localStorage.getItem(commonConstants.sessionItem.isSystemMerchant);
    if (state.isSystemMerchant == 1) {
      state.merchantNo = localStorage.getItem('merchantNo');
      getMerchants();
    }
  });

  const goTemStore = () => {
    window.open(router.resolve({ name: 'templateStore' }).href, '_blank');
  };
  const changeMerchant = async (key) => {
    state.merchantNo = key;
    localStorage.setItem(commonConstants.sessionItem.merchantNo, state.merchantNo);
    router.push('/tempRefresh').catch(() => {});
  };
  const getMerchants = async (e) => {
    var object = {
      url: apis.login.getMerchantListApi,
      params: {},
      removeEmpty: false,
    };
    commonUtil.doPost(object).then((response) => {
      if (response.code === '200') {
        state.merchants = response.responseData;
      }
    });
  };
  const isFullScreen = computed(() => {
    return store.getters['setting/isFullScreen'];
  });

  const merchantName = computed(() => {
    return state.merchants.find((item) => item.merchantNo == state.merchantNo)?.merchantName;
  });

  const emit = defineEmits(['refresh']);
  const handleClick = () => {
    if (!screenfull.isEnabled) {
      ElMessage.warning('进入全屏失败');
      return false;
    }
    store.dispatch('setting/changeFullScreen', !isFullScreen.value);
    screenfull.toggle();
    emit('refresh', screenfull.isFullscreen);
  };
</script>

<style lang="scss" scoped>
  .full-screen-wrapper {
    padding: 20px 10px;
  }
  .line {
    width: 1px;
    height: 24px;
    background: rgba(0, 0, 0, 0.1);
    margin: 0 24px;
  }
  .tem {
    cursor: pointer;
    .tem-name {
      margin-left: 6px;
      color: $base-color-primary;
      font-family: 'PingFang SC';
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: normal;
    }
  }
  .system-merchant {
    cursor: pointer;
    color: $base-color-primary;
    font-family: 'PingFang SC';
    font-size: 14px;
    font-style: normal;
    font-weight: 500;
    line-height: 14px;
    padding: 8px 10px;
    border: 1px solid $base-color-primary;
    border-radius: 6px;
  }
</style>
