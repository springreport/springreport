<!--
 * @Description: 
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-05 16:00:15
 * @LastEditors: caiyang
 * @LastEditTime: 2020-06-29 16:17:01
--> 
<template>
    <div>
        <el-select
        ref="select"
        popper-class="mod-select-tree"
        :value="labelModel"
        :clearable="clearable"
        @clear="removeSelectedNode"
        :size="size"
        @focus="selectFocus"
        style="width:100%"
        >
        <el-option :value="valueModel" :label="labelModel" style="height: auto">
            <el-tree
            ref="tree"
            empty-text="无数据"
            :expand-on-click-node="false"
            :data="options"
            :props="props"
            :node-key="props.value"
            :load="loadNode"
            @node-click="onNodeClick"
            :lazy="lazy"
            :current-node-key="value"
            :default-expanded-keys="expandedKeys"
            :check-strictly="true"
            style="width:100%"
            >
            </el-tree>
        </el-option>
        </el-select>
    </div>
</template>
<script>
    export default {
        name: 'SelectTree',
        // 设置绑定参数
        model: {
            prop: 'value',
            event: 'selected',
        },
        data() {
            return {
                // eslint-disable-next-line
                labelModel: "",   // 输入框显示值
                valueModel: "",   // 实际请求传值
                once: false,
                expandedKeys:[],
            };
        },
        props: {
            // 接收绑定参数
            value: [String],
            valueId: [String],
            // 树形控件 - 选项数据，懒加载时无需设置
            options: {
            type: Array,
            default: () => ([]),
            },
            props: {
                type: Object,
                default: () => ({
                    parent: 'parentId',
                    value: 'id',
                    label: 'label',
                    children: 'children',
                }),
                
            },
            lazy:{
                type:Boolean,
                default:true
            },
            url:{
                type:String,
                default:''
            },
            clearable:{
                type:Boolean,
                default:false
            },
            size:{
                type:String,
                default:''
            },
            focusMethod: {
                type: Function,
                default: () => {},
            },
            item: {
                type: Object,
                default: () => ({
                }),
            },
            data: {
                type: Object,
                default: () => ({
                }),
            },
        },
        mounted(){
            this.initHandler();
        },
        watch: {
            value(){
                this.expandHandle()
                this.expandedKeys = [];
                this.initHandler();
            }
        },
        methods:{
            // 初始化值
            initHandler() {
                if (!this.value)
                {
                    this.$refs.tree.setCurrentKey(null);
                    this.labelModel = null;
                    this.valueModel = null;
                    return;
                }
                this.$nextTick(() => {
                    const temp = this.$refs.tree.getNode(this.value);
                    // 初始化显示
                    this.labelModel = temp ? temp.label : '';
                    // 设置默认选中
                    this.$refs.tree.setCurrentKey(this.value);
                    this.expandHandle()
                    if(temp && temp.data.id != '0')
                    {
                        this.expandedKeys.push(this.id)
                    }
                });
            },
            expandHandle() {
                this.expandNodes(this.$refs.tree.store.root);
                },
                // 遍历树形数据，设置每一项的expanded属性，实现展开收起
                expandNodes(node) {
                    node.expanded = false;
                    for (let i = 0; i < node.childNodes.length; i++) {
                        node.childNodes[i].expanded = false;
                        if (node.childNodes[i].childNodes.length > 0) {
                            this.expandNodes(node.childNodes[i]);
                        }
                    }
                },

            // 切换选项
            onNodeClick(data, node) {
                this.labelModel = node.label;
                this.valueModel = node.key;
                this.$emit('selected', node.key);
                this.$emit('change', node.data, node);
                this.$refs.select.blur(); // 收起下拉框
                this.$refs.select.focus();  // 获取焦点
                
            },
            loadNode(node, resolve){
                this.node = node;
                this.resolve = resolve;
                if(node && node.key)
                {
                    var obj = {
                        params:{id:node.key?node.key:0},
                        url:this.url
                    };
                    this.commonUtil.doGet(obj).then(response=>{
                        resolve(response.responseData);
                    });
                }
                
            },
             // 单选,清空select输入框的回调
            removeSelectedNode () {
                this.labelModel = '';
                this.valueModel = '';
                this.$emit('selected', '');
                this.$emit('change', null, null);
            },
            selectFocus(){
                this.focusMethod(this.item,this.data);
            },
        },
        
    }
</script>
<style lang="scss" scoped>
.el-select-dropdown__item.hover, .el-select-dropdown__item:hover
{
    background-color: #ffffff !important;
}
.el-select-dropdown__item.selected {
    font-weight: normal !important;
}
</style>