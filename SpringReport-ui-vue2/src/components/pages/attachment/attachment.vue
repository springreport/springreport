<template>
    <div style="height:100vh">
        <xlsx-view v-if="fileType=='xlsx' || fileType=='csv'" :url="url" :fileType="fileType" :fileName="fileName"></xlsx-view>
        <docx-view v-else-if="fileType=='docx'" :url="url"></docx-view>
        <iframe v-else-if="fileType=='pdf' || fileType=='json'" width="100%" height="100%" style="border: none;" :src="url"></iframe>
        <div v-else></div>
        <div ref="dragArea" class="drag-area" v-show="fileType=='xlsx' || fileType=='docx' || fileType=='json' || fileType=='csv'">
              <el-tooltip effect="dark" content="下载" placement="top">
                <el-button type="primary" size="mini" icon="el-icon-download" circle @click="download"></el-button>
              </el-tooltip>
        </div>
    </div>
</template>
<script>
import xlsxView from './xlsxView.vue'
import docxView from './docxView.vue'
export default {
    components: {
        xlsxView,
        docxView
    },
    data() {
        return {
            url:"",
            fileType: "",
            fileName:"",
        }
    },
    mounted(){
       this.url = this.$route.query.url;
       this.fileName = this.$route.query.name;
       if(this.fileName){
        document.title = this.fileName;
       }
       this.fileType = this.$route.query.fileType;
    },
    methods: {
        download(){
            this.commonUtil.downloadFileByUrl(this.url,this.fileName);
        }
    },
    
}
</script>
<style scoped lang="scss">
@import "@/element-variables.scss";

.drag-area {
  position: fixed;
  right: 20px;
  bottom: 100px;
  z-index: 99999;
  padding: 5px;
  width: fit-content;
  opacity: 1;
  background-color: $--color-primary;
  border-radius: 8px;
  box-shadow: 0px 2px 15px 0px rgba(9, 41, 77, 0.15);
  user-select: none;
  text-align: center;
}
</style>