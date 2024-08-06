<!-- 数据设置组件 -->
<template>
    <div>
        <el-collapse>
            <el-collapse-item title="边框设置" >
                <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left" size="small" ref="settingForm">
                    <el-form-item label="颜色1" >
                        <input-color-picker :inputWidth=152 :value="component.color[0]" @change="(val)=>{changeColor(0,val)}" />
                    </el-form-item>
                    <el-form-item label="颜色2">
                        <input-color-picker :inputWidth=152 :value="component.color[1]" @change="(val)=>{changeColor(1,val)}" />
                    </el-form-item>
                    <el-form-item label="背景色" >
                        <input-color-picker :inputWidth=152 :value="component.backgroundColor" @change="(val)=>{component.backgroundColor=val}" />
                    </el-form-item>
                    <el-form-item label="标题内容" v-if="component.name == 'dv-border-box-11'">
                        <el-input v-model="component.content"></el-input>
                    </el-form-item>
                    <el-form-item label="标题宽度" v-if="component.name == 'dv-border-box-11'">
                        <el-input v-model.number="component.titleWidth"></el-input>
                    </el-form-item>
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
            this.component.color[index] = val;
            // this.$set(this.component.color,index,val);
        }
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
