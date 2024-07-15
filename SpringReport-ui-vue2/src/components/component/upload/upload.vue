<template>
    <div >
    <el-upload
            class="upload-demo"
            action="/api/file/upload"
            :ref="fileRef"
            :formitem-ref="formitemRef"
            :name="name"
            :on-preview="previewFile"
            :on-remove="removeFile"
            :on-success="uploadSuccess"
            :file-list="fileList"
            :on-error="uploadFailure"
            :on-exceed="uploadExceed"
            :extMethod="extMethod"
            :extMethodName="extMethodName"
            :auto-upload="true"
            :multiple="multiple"
            :limit="limit"
            :list-type="listType">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
    </el-upload>
    </div>
</template>
<script>
function noop() {
}
    export default{
        data(){
            return{
                
            }
        },
        methods: {
             removeFile(file, fileList) {//上传文件删除方法
               this.$emit('update:fileList', fileList);
               if(this.extMethod && this.extMethodName)
                {
                    this.extMethod(this.extMethodName,file, fileList,'2')
                }
            },
            previewFile(file) {//上传文件预览方法
                window.open(file.url);   
            },
            uploadSuccess(response, file, fileList){//文件上传成功
            if(response.code == "200")
            {
                file.url = response.responseData.url;
                delete file['response'];
                this.$emit('update:fileList', fileList);
                this.$parent.clearValidate();//清空父组件的错误信息
                if(this.extMethod && this.extMethodName)
                {
                    this.extMethod(this.extMethodName,file, fileList,'1')
                }
            }else{
                    if(fileList.length>0)
                    {
                        for (let index = 0; index < fileList.length; index++) {
                            const element = fileList[index];
                            if(element.uid = file.uid){
                                fileList.splice(index,1);
                                break;
                            }
                        }
                    }
                    this.$emit('update:fileList', fileList);
                    this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.upload",null),type: this.commonConstants.messageType.error});
                }
                
            },
            uploadFailure(err, file, fileList)//文件上传失败
            {
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.upload",null),type: this.commonConstants.messageType.error});
            },
            uploadExceed(files, fileList){//文件超出个数限制
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.upload.exceed",[this.limit]),type: this.commonConstants.messageType.error});
            }
        },
        props: {
            fileRef: {
                type: String,
                default: 'fileRef'
            },
            extMethod:{
                type:Function,
                default:noop
            },
            extMethodName:{
                type:String,
                default:null
            },
            multiple: {
                type: Boolean,
                default: true
            },
            limit: {
                type: Number,
                default: 1
            },
            name: {
                type: String,
                default: 'file'
            },
            fileList: {
                type: Array,
                default() {
                    return []
                }
            },
            listType:{
                type:String,
                default:'picture'
            },
            formitemRef:{
                type:String,
                default:'upload'
            }
        },
    } 
</script>