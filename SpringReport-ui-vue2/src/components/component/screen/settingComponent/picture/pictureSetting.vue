<!-- 数据设置组件 -->
<template>
  <div>
    <!-- <el-collapse>
      <el-collapse-item title="图片设置"> -->
        <el-form ref="settingForm" class="demo-form-inline" :model="component" label-position="top" size="mini">
          <el-form-item label="图片路径" prop="height">
            <el-input v-model="component.imgUrl">
              <template slot="append"><el-button icon="el-icon-upload" @click="uploadTrigger" /></template>
            </el-input>
            <input id="uploadFile" ref="fileBtn" type="file" accept="image/*" style="display:none" @change="uploadFile">
          </el-form-item>
        </el-form>
      <!-- </el-collapse-item>
    </el-collapse> -->
  </div>
</template>

<script>
import Axios from 'axios'
export default {
  props: {
    component: {
      type: Object,
      default: () => ({})
    },
    chartsComponents: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
    }
  },
  mounted() {
  },
  methods: {
    uploadTrigger() {
      this.$refs.fileBtn.dispatchEvent(new MouseEvent('click'))
    },
    uploadFile(event) {
      const file = event.target.files[0] // 获取input的图片file值
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem(this.commonConstants.sessionItem.authorization) }
      }
      Axios.post(this.apis.screenDesign.uploadFileApi, param, config)
        .then(res => {
          this.component.imgUrl = res.data.responseData.fileUri
          event.target.value = ''
        })
    }
  }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:2px !important
}
::v-deep .el-form-item__label-wrap{
    margin-left:0px !important
  }
</style>
