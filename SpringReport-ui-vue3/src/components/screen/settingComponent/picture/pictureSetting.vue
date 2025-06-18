<!-- 数据设置组件 -->
<template>
  <div>
    <el-form class="demo-form-inline" :model="component" label-position="top" ref="settingForm">
      <el-form-item label="图片路径" prop="height">
        <el-input v-model="component.imgUrl">
          <template #append
            ><el-button icon="icon-upload" @click="uploadTrigger"></el-button
          ></template>
        </el-input>
        <input
          type="file"
          ref="fileBtn"
          @change="uploadFile"
          id="uploadFile"
          accept="image/*"
          style="display: none"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import Axios from 'axios';
  export default {
    props: {
      component: {
        type: Object,
        default: () => ({}),
      },
      chartsComponents: {
        type: Object,
        default: () => ({}),
      },
    },
    mounted() {},
    data() {
      return {};
    },
    methods: {
      uploadTrigger() {
        this.$refs.fileBtn.dispatchEvent(new MouseEvent('click'));
      },
      uploadFile(event) {
        let file = event.target.files[0]; //获取input的图片file值
        let param = new FormData(); // 创建form对象
        param.append('file', file); // 通过append向form对象添加数据
        let config = {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: localStorage.getItem(this.commonConstants.sessionItem.authorization),
          },
        };
        Axios.post(this.apis.screenDesign.uploadFileApi, param, config).then((res) => {
          this.component.imgUrl = res.data.responseData.fileUri;
          event.target.value = '';
        });
      },
    },
  };
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 2px !important;
  }
  :deep(.el-form-item__label-wrap) {
    margin-left: 0px !important;
  }
</style>
