<!-- 数据设置组件 -->
<template>
    <div>
        <el-collapse>
            <el-collapse-item title="装饰设置" >
                <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left" size="small" ref="settingForm">
                    <el-form-item label="颜色1" >
                        <input-color-picker :inputWidth=152 :value="component.color[0]" @change="(val)=>{changeColor(0,val)}" />
                    </el-form-item>
                    <el-form-item label="颜色2">
                        <input-color-picker :inputWidth=152 :value="component.color[1]" @change="(val)=>{changeColor(1,val)}" />
                    </el-form-item>
                    <div v-if="component.name == 'dv-decoration-7' || component.name == 'dv-decoration-9'  || component.name == 'dv-decoration-11'">
                    <el-form-item label="文字内容">
                        <el-input v-model="component.content" style="width:180px">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="字体大小">
                        <el-input v-model="component.style.fontSize" style="width:180px">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="字体颜色" >
                        <input-color-picker :inputWidth=152 :value="component.style.color" @change="(val)=>{component.style.color=val}" />
                    </el-form-item>
                    <el-form-item label="字体间距">
                        <el-input v-model="component.style.letterSpacing">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="是否加粗" >
                        <el-switch v-model="component.style.fontWeight" />
                    </el-form-item>
                    </div>
                </el-form>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
import InputColorPicker from '../../colorpicker/inputColorPicker.vue'
export default {
    components:{
        InputColorPicker,
    },
    props:{
        component:{
            type:Object,
            default:()=>({})
        },
        chartsComponents:{
            type:Object,
            default:() => ({}),
        },
    },
    mounted() {
    },
    data(){
        return{
        }
    },
    methods:{
        changeColor(index,val){
            this.$set(this.component.color,index,val);
        }
    }
}
</script>
<style scoped>
.el-form-item{
  margin-bottom:2px !important
}
:deep( .el-form-item__label-wrap){
    margin-left:0px !important
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
