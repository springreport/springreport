<template>
    <el-dialog
        title="添加图表"
        :model-value="show"
        :close-on-click-modal='false'
        @close="closeModal"
        append-to-body
        :show-close="false"
        >
        <div class="el-dialog-div">
            <el-form :inline="true" :model="vchartForm" class="demo-form-inline" ref="vchartRef">
                <el-form-item label="图表类型"  prop="chartType" key="chartType" :rules="filter_rules('图表类型',{required:true})">
                        <el-select  placeholder="图表类型" size="small" v-model="vchartForm.chartType">
                            <el-option v-for="op in selectUtil.luckyChartType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                        </el-select>
                    </el-form-item>
            </el-form>
        </div>
        <template #footer>
        <span class="dialog-footer">
            <el-button @click="closeModal" size="small">取 消</el-button>
            <el-button type="primary" @click="confirmAddChart" size="small">确 定</el-button>
        </span>
        </template>
    </el-dialog>
</template>

<script>
export default {
  name: "vchart",
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      vchartForm:{
        chartType:""
      }
    };
  },
  watch: {
  },
  created() {},

  mounted() {
  },
  beforeDestroy() {
  },
  methods: {
    confirmAddChart(){
        var that = this;
        this.$refs['vchartRef'].validate((valid) => {
        if (valid) {
             luckysheet.createChart(that.vchartForm.chartType);
             that.closeModal();
        } else {
          return false
        }
      })
    },
    closeModal(){
        this.vchartForm.chartType = "";
        this.$emit('closeModal');
    },
  },
};
</script>

<style></style>
