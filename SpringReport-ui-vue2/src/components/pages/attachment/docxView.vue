<template>
    <vue-office-docx
        :src="url"
        style="height: 100vh;"
        @rendered="renderedHandler"
        @error="errorHandler"
    />
</template>
<script>
//引入VueOfficedocx组件
import VueOfficeDocx from '@vue-office/docx'
//引入相关样式
import '@vue-office/docx/lib/index.css'
export default {
    components: {
        VueOfficeDocx
    },
    props:{
        url:{
            type:String,
            default:''
        }
    },
    data() {
        return {
            
        }
    },
    methods: {
        renderedHandler() {
            console.log("渲染完成")
        },
        errorHandler() {
            console.log("渲染失败")
        }
    }
}
</script>