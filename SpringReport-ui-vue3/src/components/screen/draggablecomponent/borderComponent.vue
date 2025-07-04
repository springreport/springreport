<template>
    <div :id="component.id" :style="{height:component.h+'px',width:component.w+'px'}">
        <dv-border-box-1 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-1'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-1>
        <dv-border-box-2 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-2'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-2>
        <dv-border-box-3 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-3'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-3>
        <dv-border-box-4 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-4'" :color="component.color" :backgroundColor="component.backgroundColor" :reverse="component.reverse"></dv-border-box-4>
        <dv-border-box-5 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-5'" :color="component.color" :backgroundColor="component.backgroundColor" :reverse="component.reverse"></dv-border-box-5>
        <dv-border-box-6 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-6'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-6>
        <dv-border-box-7 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-7'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-7>
        <dv-border-box-8 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-8'" :color="component.color" :backgroundColor="component.backgroundColor" :reverse="component.reverse"></dv-border-box-8>
        <dv-border-box-9 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-9'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-9>
        <dv-border-box-10 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-10'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-10>
        <dv-border-box-11 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-11'" :color="component.color" :backgroundColor="component.backgroundColor" :title="component.content" :titleWidth="component.titleWidth"></dv-border-box-11>
        <dv-border-box-12 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-12'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-12>
        <dv-border-box-13 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-border-box-13'" :color="component.color" :backgroundColor="component.backgroundColor" ></dv-border-box-13>
    </div>
</template>

<script>
export default {
    name:"borderComponent",
    components:{
    },
    props:{
        component:{
            type:Object,
            default:()=>({})
        },
        isPreview:{//是否是预览组件
            type:Boolean,
            default:true
        }
    },
    mounted() {
    },
    methods:{
        initData(){
            
        }
    }
}
</script>
