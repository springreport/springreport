<template>
    <div>
        <el-upload
            class="upload-demo"
            :action="action"
            :headers="headers"
            :accept="accept"
            :drag="drag"
            :name="name"
            :file-list="fileList"
            :auto-upload="autoUpload"
            :multiple="multiple"
            :limit="limit"
            :list-type="listType"
            :on-preview="previewFile"
            :on-remove="removeFile"
            :on-success="uploadSuccess"
            :on-error="uploadFailure"
            :on-exceed="uploadExceed"
            :before-upload="beforeUpload"
            :readonly="readonly"
            :disabled="readonly"
            :filesize="filesize"
            >
            <el-button  v-show="!drag && !readonly" size="small" type="primary">点击上传</el-button>
            <div v-show="!drag && !readonly" slot="tip" class="el-upload__tip">{{tips}}</div>
            <i v-show="drag" class="el-icon-upload"></i>
            <div v-show="drag" class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div v-show="drag" class="el-upload__tip" slot="tip">{{tips}}</div>
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
        },
        previewFile(file) {//上传文件预览方法
            window.open(file.url);   
        },
        beforeUpload(file){
            const size = file.size / 1024 / 1024 
            if(this.filesize<size)
            {
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.upload.size",[this.filesize]),type: this.commonConstants.messageType.error});
                return false;
            }
            if(this.accept == ""||this.accept=="*"||this.accept=="*.*"||this.accept=="image/*")
            {
                return true;
            }else{
                let fileSuffix = file.name.substring(file.name.lastIndexOf('.'))
                if(this.accept.toUpperCase().indexOf(fileSuffix.toUpperCase())>=0)
                {
                    return true;
                }else{
                    this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.upload.type",[file.name,this.accept]),type: this.commonConstants.messageType.error});
                    return false;
                }
            }
            
        },
        uploadSuccess(response, file, fileList) {//上传文件预览方法
            if(response.code == "200")
            {
                file.url = response.responseData.fileUri;
                delete file['response'];
                this.$emit('update:fileList', fileList);
                this.$parent.clearValidate();//清空父组件的错误信息
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
            uploadUrl:{
               type: String,
                default: '/api/common/upload'
            },
            accept: {
                type: String,
                default: '*'
            },
            drag: {
                type: Boolean,
                default: false
            },
            multiple: {
                type: Boolean,
                default: true
            },
            limit: {
                type: Number,
                default: 1
            },
            filesize:{
                type:Number,
                default:5
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
            },
            autoUpload:{
                type:Boolean,
                default:true
            },
            tips:{
                type:String,
                default:'请上传图片文件。'
            },
            readonly:{
                type:Boolean,
                default:false
            }
        },
    data() {
            return {
                action: this.commonUtil.baseUrl+this.uploadUrl,
                headers:{Authorization:localStorage.getItem(this.commonConstants.sessionItem.authorization)}
            };
        }
    }
</script>