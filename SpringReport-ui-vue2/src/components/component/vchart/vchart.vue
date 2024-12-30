<template>
  <el-dialog
    title="添加图表"
    :visible.sync="show"
    :close-on-click-modal="false"
    append-to-body
    :show-close="false"
    width="83%"
    @close="closeModal"
  >
    <div class="el-dialog-div">
      <div
        v-for="op in selectUtil.luckyChartType"
        :key="op.value"
        class="vchart-item"
        :class="{ 'vchart-item-active': vchartForm.chartType == op.value }"
        @click="vchartForm.chartType = op.value"
      >
        <div class="vchart-item-img df-c-c">
          <img :src="require('@/static/img/chart/' + op.img)" height="120px">
        </div>
        <div class="vchart-item-name">
          {{ op.label }}
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button size="small" @click="closeModal">取 消</el-button>
      <el-button
        type="primary"
        size="small"
        @click="confirmAddChart"
      >确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'Vchart',
  props: {
    show: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      vchartForm: {
        chartType: ''
      }
    }
  },
  watch: {},
  created() {},

  mounted() {},
  beforeDestroy() {},
  methods: {
    confirmAddChart() {
      var that = this
      if (this.vchartForm.chartType == '') {
        this.$message.error('请选择图表类型')
        return
      }
      luckysheet.createChart(that.vchartForm.chartType)
      that.closeModal()
    },
    closeModal() {
      this.vchartForm.chartType = ''
      this.$emit('closeModal')
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/element-variables.scss";

.el-dialog-div {
  display: flex;
  flex-wrap: wrap;
}
.vchart-item {
  width: calc((100% - 80px) / 5);
  margin-right: 20px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.10);
  box-sizing: border-box;
  margin-bottom: 36px;
  cursor: pointer;
  transition: all .3s;;
  &:hover,&-active{
    border: 1px solid $--color-primary;
    .vchart-item-name{
      background: rgba(23, 183, 148, 0.10) !important;
    }
  }
  &:nth-child(5n){
    margin-right: 0;
  }
  .vchart-item-img{
    height: 151px;
    width: 100%;

    img{

    }
  }
  .vchart-item-name{
    width: 100%;
    height: 36px;
    line-height: 36px;
    border-radius: 0px 0px 8px 8px;
    background: #F6F6F6;
    color: #303030;
    font-size: 14px;
    text-align: center;
  }
}
</style>
