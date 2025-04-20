<!-- 通用设置组件 -->
<template>
    <div>
        <el-collapse>
            <el-collapse-item title="通用设置">
                <el-form class="demo-form-inline" :inline="true" :model="component" label-position="left"  size="mini" ref="settingForm">
                <el-form-item label="图表位置" class="customLabel">
                </el-form-item><br>
                <el-form-item label="左">
                    <el-button type="text" icon="el-icon-minus" @click="minusClick('1')"></el-button>
                    <el-input  v-model.number="component.x" style="width:65px" placeholder="左" :disabled="component.locked">
                    </el-input>
                    <el-button type="text" icon="el-icon-plus" @click="plusClick('1')"></el-button>
                </el-form-item>
                <el-form-item label="上">
                    <el-button type="text" icon="el-icon-minus" @click="minusClick('2')"></el-button>
                    <el-input  v-model.number="component.y" style="width:65px" placeholder="上" :disabled="component.locked"></el-input>
                    <el-button type="text" icon="el-icon-plus" @click="plusClick('2')"></el-button>
                </el-form-item>
                <el-form-item label="图表尺寸" class="customLabel">
                </el-form-item><br>
                <el-form-item label="宽">
                    <el-button type="text" icon="el-icon-minus" @click="minusClick('3')"></el-button>
                    <el-input  v-model.number="component.w" style="width:65px" placeholder="宽度" :disabled="component.locked"></el-input>
                    <el-button type="text" icon="el-icon-plus" @click="plusClick('3')"></el-button>
                </el-form-item>
                <el-form-item label="高">
                    <el-button type="text" icon="el-icon-minus" @click="minusClick('4')"></el-button>
                    <el-input  v-model.number="component.h" style="width:65px" placeholder="高度" :disabled="component.locked"></el-input>
                    <el-button type="text" icon="el-icon-plus" @click="plusClick('4')"></el-button>
                </el-form-item>
                <el-form-item label="" class="customLabel">
                </el-form-item><br>
                <el-form-item label="组件层级">
                    <el-input  v-model.number="component.zindex" style="width:180px" placeholder="组件层级"></el-input>
                </el-form-item>
                <el-form-item label="锁定组件">
                    <el-switch v-model="component.locked">
                    </el-switch>
                </el-form-item><br>
                <el-form-item label="显示边框">
                    <el-switch v-model="component.isborder" @change="commonUtil.reLoadChart(chartsComponents,component)">
                    </el-switch>
                </el-form-item>
                <el-form-item label="边框类型" v-show="component.isborder">
                    <el-select v-model="component.borderType" placeholder="请选择" @change="commonUtil.reLoadChart(chartsComponents,component)" style="width:180px">
                        <el-option
                            v-for="item in selectUtil.borderType"
                            :key="item.name"
                            :label="item.text"
                            :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="边框颜色1" v-show="component.isborder">
                        <input-color-picker :inputWidth=152 :value="component.borderColor[0]" @change="(val)=>{changeColor(0,val)}" />
                    </el-form-item>
                    <el-form-item label="边框颜色2" v-show="component.isborder">
                        <input-color-picker :inputWidth=152 :value="component.borderColor[1]" @change="(val)=>{changeColor(1,val)}" />
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
      // type 1 左 2上 3宽 4高
      plusClick(type){
        if(this.component.locked){
            return;
        }
        if(type == '1'){
            this.component.x = this.component.x + 1;
        }else if(type == '2'){
            this.component.y = this.component.y + 1;
        }else if(type == '3'){
            this.component.w = this.component.w + 1;
        }else if(type == '4'){
            this.component.h = this.component.h + 1;
        }
      },
      minusClick(type){
        if(this.component.locked){
            return;
        }
        if(type == '1'){
            if(this.component.x > 0){
                this.component.x = this.component.x - 1;
            }
        }else if(type == '2'){
            if(this.component.y > 0){
                this.component.y = this.component.y - 1;
            }
        }else if(type == '3'){
            if(this.component.w > 0){
                this.component.w = this.component.w - 1;
            }
        }else if(type == '4'){
            if(this.component.h > 0){
                this.component.h = this.component.h - 1;
            }
        }
      },
      changeColor(index,val){
        this.$set(this.component.borderColor,index,val);
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
::v-deep .customLabel{
    font-weight: bold;
}
::v-deep .customLabel .el-form-item__label{
    color:#15a585 !important;
}
</style>
