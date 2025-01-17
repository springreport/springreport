<template>
  <div>
    <iframe :src="state.url" style="width: 100%; height: calc(100vh - 87px); border: none;" />
  </div>
</template>

<script setup lang="ts">
import {getToken } from '../../common/utils';
const state = reactive({
  url: 'http://localhost:9527/SpringReport/reportTpl',
});

onMounted(() => {
     getLinkUrl();
});

const getLinkUrl = async() =>{
    let url = state.url + '?thirdPartyType=orange&token=' + getToken()
     state.url = url;
}
</script>


