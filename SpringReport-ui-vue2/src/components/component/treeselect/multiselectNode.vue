<!--
 * @Description: 
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-05 16:00:15
 * @LastEditors: caiyang
 * @LastEditTime: 2020-07-23 18:16:50
--> 
<template>
    <div>
        <el-select
        ref="select"
        popper-class="mod-select-tree"
        :value="labelModel"
        :multiple="false"
        :clearable="clearable"
        :size="size"
        @focus="selectFocus"
        @clear="removeSelectedNode"
        style="width:100%"
        >
        <el-option :value="valueModel"  style="height: auto">
            <el-tree
            ref="tree"
            empty-text="无数据"
            :expand-on-click-node="false"
            :data="options"
            clearable
            :props="props"
            :node-key="props.value"
            :load="loadNode"
            :lazy="lazy"
            :show-checkbox="showCheckbox"
            :check-strictly="checkStrictly"
            :default-checked-keys="value"
            @check-change="handleCheckChange"
            style="width:100%"
            >
            </el-tree>
        </el-option>
        </el-select>
    </div>
</template>
<script>
    export default {
        name: 'MultiSelectTree',
        // 设置绑定参数
        model: {
            prop: 'value',
            event: 'selected',
        },
        data() {
            return {
            // eslint-disable-next-line
            labelModel: "",   // 输入框显示值
            valueModel: [],   // 实际请求传值
            once: false,
            };
        },
        props: {
            // 接收绑定参数
            value:{
                type: Array,
                default: () => ([]),
            },
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
            lazy:{
                type:Boolean,
                default:true
            },
            checkStrictly:{
                type:Boolean,
                default:true
            },
            clearable:{
                type:Boolean,
                default:true
            },
            showCheckbox:{
                type:Boolean,
                default:true
            },
            url:{
                type:String,
                default:''
            },
            size:{
                type:String,
                default:'mini'
            },
            focusMethod: {
                type: Function,
                default: () => {},
            }
        },
        mounted(){
            this.handleCheckChange();
        },
         watch: {
            value: {
                handler(val) {
                    this.valueModel = val;
                    if (!this.value) return;
                    this.$nextTick(() => {
                        // 初始化显示
                        this.labelModel =  '';
                        let res = this.$refs.tree.getCheckedNodes(false, false);
                        let arr = [];
                        let arrLabel = [];
                        res.forEach(item => {
                            arrLabel.push(item[this.props.label]);
                            arr.push(item[this.props.value]);
                        });
                        this.labelModel = arrLabel.join(',');
                        this.valueModel = arr;
                    });
                },
                immediate: true,
            },
        },
        methods:{
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
            handleCheckChange(data,isChecked,isLeafChecked){
                let res = this.$refs.tree.getCheckedNodes(false, false);
                let arr = [];
                let arrLabel = [];
                res.forEach(item => {
                    arrLabel.push(item[this.props.label]);
                    arr.push(item[this.props.value]);
                });
                this.labelModel = arrLabel.join(',');
                this.valueModel = arr;
                this.$emit('selected', this.valueModel);
            },
            removeSelectedNode () {
                this.labelModel = '';
                this.valueModel = [];
                this.$refs.tree.setCheckedKeys([]);
            },
            selectFocus(){
                this.focusMethod(this.item,this.data);
            }
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