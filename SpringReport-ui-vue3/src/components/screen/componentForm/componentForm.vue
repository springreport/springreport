<template>
  <div>
      <el-form :inline="true" class="demo-form-inline" @submit.prevent>
          <el-form-item label=""  v-for="(item,index) in params"  :key="index">
                <el-input  v-if="item.paramType==='varchar' || item.paramType==='number'" v-model="params[index][item.paramCode]" :placeholder="item.paramName" size="small" :style="{width:item.width+'px'}" @keyup.enter="search"></el-input>
                <el-select  v-if="item.paramType ==='select'" :placeholder="item.paramName" size="small" :style="{width:item.width+'px'}" v-model="params[index][item.paramCode]" @change="search" filterable clearable>
                    <el-option v-for="op in item.selectData" :label="op.name" :value="op.value" :key="op.value"></el-option>
                </el-select>
                <el-date-picker type="date"  v-if="item.paramType==='date'" :placeholder="item.paramName" v-model="params[index][item.paramCode]" format="YYYY-MM-DD" value-format="YYYY-MM-DD" size="small" :style="{width:item.width+'px'}" :clearable="false" @change="search"></el-date-picker>
          </el-form-item>
      </el-form>
  </div>
</template>

<script>
export default {
    props:{
        params:{
            type:Array,
            default:() => ([]),
        },
        isView:{//是否查看，是则会触发查询事件，否则不触发查询事件
            type:Boolean,
            default:false
        }
    },
    data () {
        return {
            
        };
    },
    methods:{
        search(){
            if(this.isView)
            {
                this.$parent.initComponent();
            }
            
        }
    }
}
</script>

<style scoped>
.custom-input :deep(el-input__inner){
    border-radius:0px;
    border:none;
    height:25px;
    line-height: 25px;
}

.custom-input :deep(.el-select-dropdown__list){
    padding: 0;
}

.demo-form-inline :deep(.el-form-item__content){
    line-height: 25px !important;
}


.el-form--inline .el-form-item {
    margin-right:5px !important
}
</style>
