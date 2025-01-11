<template>
  <el-dialog
    title="添加图表"
    :model-value="show"
    :close-on-click-modal="false"
    @close="closeModal"
    append-to-body
    width="83%"
    :show-close="false"
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
          <img :src="getImg(op.img)" height="120px" />
        </div>
        <div class="vchart-item-name">
          {{ op.label }}
        </div>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeModal">取 消</el-button>
        <el-button type="primary" @click="confirmAddChart">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
  import { ElMessage } from 'element-plus';

  export default {
    name: 'vchart',
    props: {
      show: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        vchartForm: {
          chartType: '',
        },
      };
    },
    watch: {},
    created() {},

    mounted() {},
    beforeUnmount() {},
    methods: {
      getImg(img) {
        return new URL(`../../assets/img/chart/${img}`, import.meta.url).href;
      },
      confirmAddChart() {
        var that = this;
        if (this.vchartForm.chartType == '') {
          ElMessage.error('请选择图表类型');
          return;
        }
        luckysheet.createChart(that.vchartForm.chartType);
        that.closeModal();
      },
      closeModal() {
        this.vchartForm.chartType = '';
        this.$emit('closeModal');
      },
    },
  };
</script>

<style lang="scss" scoped>
  .el-dialog-div {
    display: flex;
    flex-wrap: wrap;
  }
  .vchart-item {
    width: calc((100% - 80px) / 5);
    margin-right: 20px;
    border-radius: 8px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-sizing: border-box;
    margin-bottom: 36px;
    cursor: pointer;
    transition: all 0.3s;
    &:hover,
    &-active {
      border: 1px solid $base-color-primary;
      .vchart-item-name {
        background: rgba(23, 183, 148, 0.1) !important;
      }
    }
    &:nth-child(5n) {
      margin-right: 0;
    }
    .vchart-item-img {
      height: 151px;
      width: 100%;

      img {
      }
    }
    .vchart-item-name {
      width: 100%;
      height: 36px;
      line-height: 36px;
      border-radius: 0px 0px 8px 8px;
      background: #f6f6f6;
      color: #303030;
      font-size: 14px;
      text-align: center;
    }
  }
</style>
