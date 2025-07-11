<template>
    <div :id="component.id" :style="{height:component.h+'px',width:component.w+'px'}">
        <dv-decoration-1 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-1'" :color="component.color"></dv-decoration-1>
        <dv-decoration-2 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-2'" :color="component.color" :reverse="component.reverse"></dv-decoration-2>
        <dv-decoration-3 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-3'" :color="component.color"></dv-decoration-3>
        <dv-decoration-4 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-4'" :color="component.color" :reverse="component.reverse"></dv-decoration-4>
        <dv-decoration-5 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-5'" :color="component.color" :dur="3"></dv-decoration-5>
        <dv-decoration-6 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-6'" :color="component.color" ></dv-decoration-6>
        <dv-decoration-7 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-7'" :color="component.color" ><span v-if="component.content" class="text" :style="{color:component.style.color,fontSize:component.style.fontSize+'pt',letterSpacing:component.style.letterSpacing+'px',fontWeight:component.style.fontWeight?'bold':'normal'}">{{component.content}}</span></dv-decoration-7>
        <dv-decoration-8 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-8'" :color="component.color" :reverse="component.reverse"></dv-decoration-8>
        <dv-decoration-9 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-9'" :color="component.color" ><span v-if="component.content" class="text" :style="{color:component.style.color,fontSize:component.style.fontSize+'pt',letterSpacing:component.style.letterSpacing+'px',fontWeight:component.style.fontWeight?'bold':'normal'}">{{component.content}}</span></dv-decoration-9>
        <dv-decoration-10 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-10'" :color="component.color" ></dv-decoration-10>
        <dv-decoration-11 :style="{height:component.h+'px',width:component.w+'px'}" v-if="component.name == 'dv-decoration-11'" :color="component.color" ><span v-if="component.content" class="text" :style="{color:component.style.color,fontSize:component.style.fontSize+'pt',letterSpacing:component.style.letterSpacing+'px',fontWeight:component.style.fontWeight?'bold':'normal'}">{{component.content}}</span></dv-decoration-11>
    </div>
</template>

<script>
export default {
    name:"decorationComponent",
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
