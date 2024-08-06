<template>
  <div>
     <el-form ref="settingForm" class="demo-form-inline" :inline="true" :model="component" label-position="left"  label-width="auto" size="small">
        <el-form-item label="大屏配置" class="customLabel">
        </el-form-item><br>
        <el-form-item label="宽度">
            <el-input v-model.number="component.width" style="width:90px"/>
        </el-form-item>
        <el-form-item label="高度" >
            <el-input v-model.number="component.height" style="width:90px"/>
        </el-form-item>
        <el-form-item label="背景图片" >
          <el-input v-model="component.imgUrl" style="width:180px">
            <template #append><el-button icon="icon-upload" @click="uploadTrigger" /></template>
          </el-input>
          <input id="uploadFile" ref="fileBtn" type="file" accept="image/*" style="display:none" @change="uploadFile">
        </el-form-item>
        <el-form-item label="背景颜色" prop="background">
          <input-color-picker
            :value="component.background"
            @change="(val)=>{component.background=val}"
          />
        </el-form-item>
     </el-form>
  </div>
</template>

<script>
import Axios from 'axios'
import InputColorPicker from '../colorpicker/inputColorPicker.vue'
export default {
  components: {
    InputColorPicker
  },
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
      predefineColors: []
    }
  },
  mounted() {
    this.predefineColors = this.commonConstants.predefineColors
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
    },
  }
}
</script>

<style scoped>
.el-form-item{
  margin-bottom:2px !important
}
:deep(.el-form-item__label-wrap){
    margin-left:0px !important
}
:deep(.customLabel){
    font-weight: bold;
}
:deep(.customLabel .el-form-item__label){
    color:#15a585 !important;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  background-color: var(--colorWhite);
  color: var(--colorTextPrimary);
  border: 1px solid var(--borderColorBase);
}

:deep(.el-select-dropdown) {
   border: 1px solid var(--borderColorBase) !important;
   background-color: var(--colorWhite) !important;
 }

 :deep(.el-select__selected-item){
    color:var(--colorTextPrimary) !important;
  
}

:deep(.el-select--small .el-select__wrapper){
    background-color: var(--colorWhite) !important;
    box-shadow: 0 0 0 1px black inset;
}

:deep(.el-input--small .el-input__wrapper){
    /* padding: 0px 0px; */
    background-color: var(--colorWhite);
    box-shadow: 0 0 0 1px black inset;
}
</style>