<template>
  <div v-if="state.isSystemMerchant == 1" class="role-name">商户&nbsp;</div>
      <div v-if="state.isSystemMerchant == 1" class="role-name">
        <el-select v-model="state.merchantNo" placeholder="请选择" size="small" @change="changeMerchant" style="width:120px">
          <el-option
            v-for="item in state.merchants"
            :key="item.merchantNo"
            :label="item.merchantName"
            :value="item.merchantNo">
          </el-option>
      </el-select>
      </div>
  <span
    class="icon-hover full-screen-wrapper"
    :title="isFullScreen ? '退出全屏' : '全屏'"
  >
    <component
      theme="filled"
      size="16"
      :fill="color"
      :strokeWidth="4"
      :is="(isFullScreen ? 'icon-off' : 'icon-full') + '-screen'"
      @click="handleClick"
    />
  </span>
</template>

<script setup>
  import { reactive,computed,onMounted } from 'vue';
  import screenfull from 'screenfull';
  import { useStore } from 'vuex';
  import { ElMessage } from 'element-plus';
  import { useRouter } from 'vue-router';
  import commonUtil from '../common/common';
  import apis from '../common/api';
  import commonConstants from '../common/constants';
  const store = useStore();
  const state = reactive({
      isSystemMerchant:2,
      merchants:[],
      merchantNo:"",
    });
  const router = useRouter();
  defineProps({
    color: {
      type: String,
      default: '#666',
    },
  });

  onMounted(() => {
      state.isSystemMerchant = localStorage.getItem(commonConstants.sessionItem.isSystemMerchant)
      if(state.isSystemMerchant == 1)
      {
        state.merchantNo = localStorage.getItem('merchantNo')
        getMerchants();
      }
  })
  const changeMerchant =async (e) =>{
      localStorage.setItem(
          commonConstants.sessionItem.merchantNo,
          state.merchantNo
      )
      router.push('/tempRefresh').catch(() => {});
    }
  const getMerchants =async (e) =>{
      var object = {
          url: apis.login.getMerchantListApi,
          params: {},
          removeEmpty: false,
        }
        commonUtil.doPost(object).then((response) => {
            if (response.code === '200') {
               state.merchants = response.responseData
            }
          })
    }
  const isFullScreen = computed(() => {
    return store.getters['setting/isFullScreen'];
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
</style>
