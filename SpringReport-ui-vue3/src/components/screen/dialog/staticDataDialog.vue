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
    <el-dialog
      title="静态数据"
      :modelValue="customDataDialogVisiable"
      width="82%"
      height="80%"
      :close-on-click-modal="false"
      @close="closeCustomDataDialog"
    >
      <div style="height: 40%">
        <div style="height: 300px">
          <codemirror
            ref="dataCodeMirror"
            :options="cmOptions"
            v-model:value="sqlText"
          ></codemirror>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeCustomDataDialog()">取 消</el-button>
          <el-button type="primary" @click="confirmCustomData()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import codemirror from 'codemirror-editor-vue3';
  // base style
  import 'codemirror/lib/codemirror.css';
  // theme css
  import 'codemirror/theme/eclipse.css';
  import 'codemirror/addon/hint/sql-hint';
  // language
  import 'codemirror/mode/sql/sql.js';
  // active-line.js
  import 'codemirror/addon/selection/active-line.js';
  // styleSelectedText
  import 'codemirror/addon/selection/mark-selection.js';
  import 'codemirror/addon/search/searchcursor.js';
  // highlightSelectionMatches
  import 'codemirror/addon/scroll/annotatescrollbar.js';
  import 'codemirror/addon/search/matchesonscrollbar.js';
  import 'codemirror/addon/search/searchcursor.js';
  import 'codemirror/addon/search/match-highlighter.js';

  // keyMap
  import 'codemirror/mode/clike/clike.js';
  import 'codemirror/addon/edit/matchbrackets.js';
  import 'codemirror/addon/comment/comment.js';
  import 'codemirror/addon/dialog/dialog.js';
  import 'codemirror/addon/dialog/dialog.css';
  import 'codemirror/addon/search/searchcursor.js';
  import 'codemirror/addon/search/search.js';
  import 'codemirror/keymap/sublime.js';

  // foldGutter
  import 'codemirror/addon/fold/foldgutter.css';
  import 'codemirror/addon/fold/brace-fold.js';
  import 'codemirror/addon/fold/comment-fold.js';
  import 'codemirror/addon/fold/foldcode.js';
  import 'codemirror/addon/fold/foldgutter.js';
  import 'codemirror/addon/fold/indent-fold.js';
  import 'codemirror/addon/fold/markdown-fold.js';
  export default {
    components: {
      codemirror,
    },
    props: {
      component: {
        type: Object,
        default: () => ({}),
      },
      chartsComponents: {
        type: Object,
        default: () => ({}),
      },
      customDataDialogVisiable: {
        type: Boolean,
        default: false,
      },
      comboChartType:{
        type:String,
        default:'',//1 折柱图的柱状图部分 2、折柱图的折线部分
      }
    },
    mounted() {
      this.cmOptions = this.commonConstants.cmOptions;
      this.init();
    },
    data() {
      return {
        cmOptions: {},
        sqlText: '',
      };
    },
    methods: {
      confirmCustomData() {
        let sqlContent = this.sqlText;
        if (sqlContent.trim()) {
          try {
              const values = eval('(' + sqlContent + ')')
              if (typeof values === 'object' || Array.isArray(values)) {
                if (this.component.type == 'pageTable') {
                  if (Array.isArray(values)) {
                    this.component.spec.data.total = values.length
                  } else {
                    this.commonUtil.showMessage({ message: '请添加JSON对象数组格式的数据', type: this.commonConstants.messageType.error })
                    return
                  }
                }
                if (this.component.type == 'sankey') {
                  this.component.spec.data.values = [{ nodes: [], links: [] }]
                  this.commonUtil.processSankeyData(this.component, values)
                }else if (this.component.type.toLowerCase().indexOf('combocharthl') >= 0 || this.component.type.toLowerCase().indexOf('combochartdbbar') >= 0){
                  if(this.comboChartType == "1"){
                    this.component.spec.data[0].values = values;
                  }else{
                    this.component.spec.data[1].values = values;
                  }
                } else {
                  this.component.spec.data.values = values
                }
                if (this.component.category == this.screenConstants.category.vchart) {
                  this.commonUtil.reLoadChart(this.chartsComponents, this.component)
                }
                
                if (Array.isArray(values)) {
                  if(this.comboChartType == "2"){
                      this.component.lineDynamicDataSettings.dataColumns = []
                  }else{
                      this.component.dynamicDataSettings.dataColumns = []
                  }
                  for (var key in values[0]) {
                    if(this.comboChartType == "2"){
                      if(this.component.lineDynamicDataSettings.dataColumns.indexOf(key) == -1){
                        this.component.lineDynamicDataSettings.dataColumns.push(key)
                      }
                    }else{
                      if(this.component.dynamicDataSettings.dataColumns.indexOf(key) == -1){
                        this.component.dynamicDataSettings.dataColumns.push(key)
                      }
                    }
                  }
                } else {
                  this.component.dynamicDataSettings.dataColumns = []
                  for (var key in values) {
                    this.component.dynamicDataSettings.dataColumns.push(key)
                  }
                }
                this.closeCustomDataDialog()
                this.commonUtil.reLoadChart(this.chartsComponents, this.component)
              } else {
                this.commonUtil.showMessage({ message: '请添加JSON格式的数据', type: this.commonConstants.messageType.error })
              }
            
          } catch (error) {
            this.commonUtil.showMessage({ message: '请添加JSON格式的数据', type: this.commonConstants.messageType.error })
          }
        }
      },
      closeCustomDataDialog() {
        this.$nextTick(() => {
          this.$emit('update:customDataDialogVisiable', false);
          this.sqlText = ' ';
        });
      },
      init() {
        let dataContent = '';
        if (this.component.type.toLowerCase().indexOf('sankey') >= 0) {
          //桑葚图
          dataContent = JSON.stringify(this.component.spec.data.values[0].links);
        } else if (this.component.type.toLowerCase().indexOf('combocharthl') >= 0  || this.component.type.toLowerCase().indexOf('combochartdbbar') >= 0) {
          //折柱图
          if(this.comboChartType == '1'){
            dataContent = JSON.stringify(this.component.spec.data[0].values);
          }else{
            dataContent = JSON.stringify(this.component.spec.data[1].values);
          }
        }else {
          dataContent = JSON.stringify(this.component.spec.data.values);
        }
        this.$nextTick(() => {
          this.sqlText = dataContent;
        });
      },
    },
  };
</script>

<style></style>
