<!--
 * @Description: 自定义数据对话框组件
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2024年7月7日12:12:38
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-07-13 11:09:31
-->
<template>
  <div>
    <el-dialog title="静态数据" :modelValue="customDataDialogVisiable" width="80%" height="80%" :close-on-click-modal='false' @close='closeCustomDataDialog'>
        <div style="height:40%">
            <div style="height:300px;">
            <codemirror ref="dataCodeMirror" :options="cmOptions" v-model:value="sqlText"></codemirror>
            </div>
        </div>
        <template #footer>
        <span  class="dialog-footer">
            <el-button @click="closeCustomDataDialog()" size="small">取 消</el-button>
            <el-button type="primary" @click="confirmCustomData()" size="small">确 定</el-button>
        </span>
        </template>
    </el-dialog>
  </div>
</template>

<script>
import codemirror from "codemirror-editor-vue3";
// base style
import "codemirror/lib/codemirror.css";
// theme css
import "codemirror/theme/eclipse.css";
import "codemirror/addon/hint/sql-hint";
// language
import "codemirror/mode/sql/sql.js";
// active-line.js
import "codemirror/addon/selection/active-line.js";
// styleSelectedText
import "codemirror/addon/selection/mark-selection.js";
import "codemirror/addon/search/searchcursor.js";
// highlightSelectionMatches
import "codemirror/addon/scroll/annotatescrollbar.js";
import "codemirror/addon/search/matchesonscrollbar.js";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/match-highlighter.js";

// keyMap
import "codemirror/mode/clike/clike.js";
import "codemirror/addon/edit/matchbrackets.js";
import "codemirror/addon/comment/comment.js";
import "codemirror/addon/dialog/dialog.js";
import "codemirror/addon/dialog/dialog.css";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/search.js";
import "codemirror/keymap/sublime.js";
 
// foldGutter
import "codemirror/addon/fold/foldgutter.css";
import "codemirror/addon/fold/brace-fold.js";
import "codemirror/addon/fold/comment-fold.js";
import "codemirror/addon/fold/foldcode.js";
import "codemirror/addon/fold/foldgutter.js";
import "codemirror/addon/fold/indent-fold.js";
import "codemirror/addon/fold/markdown-fold.js";
export default {
    components: {
        codemirror,
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
        customDataDialogVisiable:{
            type:Boolean,
            default:false
        },
    },
    mounted() {
        this.cmOptions = this.commonConstants.cmOptions;
        this.cmOptions.theme="erlang-dark"
        this.init();
    },
    data(){
        return{
            cmOptions: {},
            sqlText:"",
        }
    },
    methods:{
        confirmCustomData(){
            let sqlContent=this.sqlText;
            if(sqlContent.trim()){
                try {
                        let values = eval('(' + sqlContent + ')');
                        if(typeof values === 'object' || Array.isArray(values)){
                             if(this.component.type == "pageTable" ){
                                if(Array.isArray(values)){
                                 this.component.spec.data.total = values.length;
                                }else{
                                    this.commonUtil.showMessage({ message: '请添加JSON对象数组格式的数据', type: this.commonConstants.messageType.error })
                                    return;
                                }
                             }
                            if(this.component.type == "sankey"){
                                this.component.spec.data.values = [{nodes:[],links:[]}];
                                this.commonUtil.processSankeyData(this.component,values);
                            }else{
                                this.component.spec.data.values = values;
                            }
                            if(this.component.category == this. screenConstants.category.vchart){
                                this.commonUtil.reLoadChart(this.chartsComponents,this.component);
                            }
                            this.component.dynamicDataSettings.dataColumns = [];
                            if(Array.isArray(values)){
                                for(var key in values[0]){
                                    this.component.dynamicDataSettings.dataColumns.push(key)
                                }
                            }else{
                                for(var key in values){
                                    this.component.dynamicDataSettings.dataColumns.push(key)
                                }
                            }
                            this.closeCustomDataDialog();
                            this.commonUtil.reLoadChart(this.chartsComponents,this.component);
                        }else{
                            this.commonUtil.showMessage({ message: '请添加JSON格式的数据', type: this.commonConstants.messageType.error })
                        }
                    } catch (error) {
                        this.commonUtil.showMessage({ message: '请添加JSON格式的数据', type: this.commonConstants.messageType.error })
                    }
            }
        },
        closeCustomDataDialog(){
           this.$nextTick(() => {
                this.$emit('update:customDataDialogVisiable', false)
                this.sqlText = " ";
            });
        },
        init(){
            let dataContent = "";
            if(this.component.type.toLowerCase().indexOf("sankey") >= 0){
                dataContent = JSON.stringify(this.component.spec.data.values[0].links)
            }else{
                dataContent = JSON.stringify(this.component.spec.data.values)
            }
            this.$nextTick(() => {
                this.sqlText = dataContent;
            });
        }
    }
}
     
</script>

<style>

</style>