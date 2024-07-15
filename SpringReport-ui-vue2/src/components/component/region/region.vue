<!--
 * @Description: 省市区控件
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-22 10:13:19
 * @LastEditors: caiyang
 * @LastEditTime: 2020-07-18 15:22:58
--> 
<template>
    <div>
        <el-cascader
              id="cascaderRegion"
             :style="customStyle"
              v-model="value"
              ref="cascaderRegion"
              placeholder="请选择省市区"
              :props="props"
              @change="handleChange"
              :disabled="disabled"></el-cascader>
    </div>
</template>

<script>
import commonUtil from '../../common/common'
export default {
    props:{
        inline:{
           type:Boolean,
           default:true
        },
        customStyle:{
           type:String,
           default:'' 
        },
        areaCode:{
          type:Array,
          default:()=>[]
        },
        disabled:{
          type:Boolean,
          default:false
        }
    },
    name:"region",
    data () {
        return {
          value:[],
          props: {
            lazy: true,
            lazyLoad(node, resolve) {
              if(node.level == 0){
                let provinces = commonUtil.getNextArea('000000');
                let result = [];
                if(provinces && provinces.length>0)
                {
                  for (let index = 0; index < provinces.length; index++) {
                    const element = provinces[index];
                    let data = {
                      value: element.areaCode,
                      label:element.areaName,
                      leaf: node.level >= 2
                    }
                    result.push(data);
                  }
                }
                resolve(result);
              }else if(node.level == 1){
                let cities = commonUtil.getNextArea(node.value);
                let result = [];
                if(cities && cities.length>0)
                {
                  for (let index = 0; index < cities.length; index++) {
                    const element = cities[index];
                    let data = {
                      value: element.areaCode,
                      label:element.areaName,
                      leaf: node.level >= 2
                    }
                    result.push(data);
                  }
                }
                resolve(result);
              }else if(node.level == 2){
                let regins = commonUtil.getNextArea(node.value);
                let result = [];
                if(regins && regins.length>0)
                {
                  for (let index = 0; index < regins.length; index++) {
                    const element = regins[index];
                    let data = {
                      value: element.areaCode,
                      label:element.areaName,
                      leaf: node.level >= 2
                    }
                    result.push(data);
                  }
                }
                resolve(result);
              }
            }},
        };
    },
 methods: {
   handleChange(){
    this.$emit("update:areaCode", this.value)
   }
 }
}

</script>
<style>
</style>
