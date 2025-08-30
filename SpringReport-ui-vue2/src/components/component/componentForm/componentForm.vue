<!--
 * @Author: liren
 * @Date: 2022-10-08 09:44:30
 * @LastEditTime: 2022-10-08 16:55:54
 * @Description:
-->
<template>
  <div>
    <el-form :inline="true" class="demo-form-inline" @submit.native.prevent>
      <el-form-item v-for="(item,index) in params" :key="index" label="">
        <el-input v-if="item.paramType==='varchar' || item.paramType==='number'" v-model="params[index][item.paramCode]" class="custom-input" :placeholder="item.paramName" size="mini" :style="{width:item.width+'px'}" @keyup.enter.native="search"  filterable clearable/>
        <el-select v-if="item.paramType ==='select'" v-model="params[index][item.paramCode]" class="custom-input" :placeholder="item.paramName" size="mini" :style="{width:item.width+'px'}" @change="search">
          <el-option v-for="op in item.selectData" :key="op.value" :label="op.name" :value="op.value" />
        </el-select>
        <el-date-picker v-if="item.paramType==='date'" v-model="params[index][item.paramCode]" class="custom-input" :placeholder="item.paramName" format="yyyy-MM-dd" value-format="yyyy-MM-dd" size="mini" :style="{width:item.width+'px'}" :clearable="false" @change="search" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    params: {
      type: Array,
      default: () => ([])
    },
    isView: {// 是否查看，是则会触发查询事件，否则不触发查询事件
      type: Boolean,
      default: false
    }
  },
  data() {
    return {

    }
  },
  methods: {
    search() {
      if (this.isView) {
        this.$parent.initComponent()
      }
    }
  }
}
</script>

<style scoped>
.custom-input >>>.el-input__inner{
    border-radius:0px;
    border:none;
    height:25px;
    line-height: 25px;
    background: white;
    color: black !important;
}

.custom-input >>>.el-select-dropdown__list{
    padding: 0;
}

.demo-form-inline >>>.el-form-item__content{
    line-height: 25px !important;
}
</style>
