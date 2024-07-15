<template>
  <el-select
    ref="select"
    popper-class="mod-select-tree"
    :value="labelModel"
    :filterable="filterable"
    :disabled="disabledSelected"
    :filter-method="filterMethod"
    :clearable="filterable || lazy"
    @clear="clearHandler"
    @visible-change="visibleChange">
    <el-option :value="valueModel" :label="labelModel" style="height: auto">
      <el-tree
        ref="tree"
        empty-text="无数据"
        :expand-on-click-node="false"
        :accordion="accordion"
        :data="options"
        :props="propsMerge"
        :lazy="lazy"
        :load="onLoadNodes"
        :default-expand-all="!lazy"
        :node-key="propsMerge.value"
        :filter-node-method="filterNode"
        :renderContent="renderContent"
        @node-click="onNodeClick">
      </el-tree>
    </el-option>
  </el-select>
</template>

<script>
// import { analyzeResp } from '@/libs/util';

export default {
  name: 'SelectTree',
  // 设置绑定参数
  model: {
    prop: 'value',
    event: 'selected',
  },
  props: {
    // 接收绑定参数
    value: [String, Number],
    // 是否可以清空选项
    clearable: Boolean,
    // 根节点key值
    rootKey: String,
    // 懒加载是否使用地址传参方式
    joint: Boolean,
    // 禁用
    disabled: Boolean,
    // 启用搜索功能
    filterable: Boolean,
    // 树形控件 - 启用懒加载
    lazy: Boolean,
    // 树形控件 - 懒加载接口地址
    url: String,
    // 树形控件 - 是否每次只打开一个同级树节点展开
    accordion: Boolean,
    // 树形控件 - 选项数据，懒加载时无需设置
    options: {
      type: Array,
      default: () => ([]),
    },
    // 树形控件 - 配置项
    props: {
      type: Object,
      default: () => ({}),
    },
    // 查询参数
    queryParams: {
      type: Object,
      default: () => ({
        rows: 100000,
        page: 1,
        sort: 'orderNum-',
      }),
    },
  },
  data() {
    return {
      // eslint-disable-next-line
      labelModel: '',   // 输入框显示值
      valueModel: '',   // 实际请求传值
      once: false,
    };
  },
  computed: {
    disabledSelected() {
      if (this.disabled) return true;
      return this.$parent.form ? this.$parent.form.disabled : false;
    },
    propsMerge() {
      const propsTemp = Object.assign({
        parent: 'parentId',
        value: 'id',
        label: 'label',
        children: 'children',
        disabled: 'disabled', // 是否禁用
        isLeaf: 'isLeaf',     // 是否为叶子节点
      }, this.props);
      return propsTemp;
    },
  },
  watch: {
    value: {
      handler(val) {
        this.valueModel = val;
        this.initHandler();
      },
      immediate: true,
    },
    options: {
      handler(val) {
        if (!val || this.lazy) return;
        this.initHandler();
      },
      immediate: true,
    },
  },
  methods: {
    // 初始化值
    initHandler() {
      if (!this.value) return;
      this.$nextTick(() => {
        const temp = this.$refs.tree.getNode(this.value);
        // 初始化显示
        this.labelModel = temp ? temp.label : '';
        // 设置默认选中
        this.$refs.tree.setCurrentKey(this.value);
      });
    },
    // 切换选项
    onNodeClick(data, node) {
      if (node.disabled) {
        this.$refs.tree.setCurrentKey(this.value || null);
      } else {
        this.labelModel = node.label;
        this.valueModel = node.key;
        this.$emit('selected', node.key);
        this.$emit('change', node.data, node);
        this.$refs.select.blur(); // 收起下拉框
        this.$refs.select.focus();  // 获取焦点
        // 修复懒加载下无法关闭选项问题
        if (this.lazy) {
          this.$set(this.$refs.select, 'visible', false);
        }
      }
    },
    // 下拉菜单显示回调
    visibleChange(show) {
      if (!show) return;
      // 确保滚动条位置正确显示
      if (show && !this.labelModel) {
        this.labelModel = ' ';
        this.$nextTick(() => { this.labelModel = ''; });
      }
      if (this.lazy && this.once) {
        this.getNodeList(this.rootKey).then((data) => {
          this.$refs.tree.updateKeyChildren(this.rootKey, data);
        });
      } else {
        this.$emit('refresh');
      }
      this.initHandler();
      this.once = true;
    },
    // 选择器检索过滤方法
    filterMethod(query) {
      // 调用树形控件的过滤
      this.$refs.tree.filter(query);
      // 忽略选择器本身的过滤
      return true;
    },
    // 树节点过滤方法
    filterNode(query, data) {
      if (!query) return true;
      return data[this.propsMerge.label].includes(query);
    },
    // 清空已选
    clearHandler() {
      this.valueModel = this.lazy ? this.rootKey : this.$enum.emptyRowGuid;
      this.labelModel = this.lazy ? '' : '所有';
      this.$emit('selected', '');
      this.$refs.tree.filter();
      this.$refs.tree.setCurrentKey(null);
    },
    // 加载子节点
    onLoadNodes(node, resolve) {
      this.getNodeList(node.data[this.propsMerge.value]).then(data => resolve(data));
    },
    // 获取节点列表
    getNodeList(id = this.rootKey) {
      const params = this.queryParams;
      const paramsUrl = this.joint ? `/${id}` : '';
      if (!this.joint) params.parentId = id;
      return new Promise((resolve) => {
        this.$http.get(`${this.url}${paramsUrl}`, { params }).then((response) => {
          resolve((response.data.data || response.data) || []);
        }, ({ response }) => {
        //   analyzeResp(response);
        });
      });
    },
    // 节点渲染函数
    renderContent(h, { node /* , store */ }) {
      return (
        <span class={`el-tree-node__label ${node.disabled ? 'is-disabled' : ''}`}>{node.label}</span>
      );
    },
  },
};
</script>