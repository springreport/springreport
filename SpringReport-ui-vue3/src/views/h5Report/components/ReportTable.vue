<template>
  <div>
    <div class="report-table" v-if="table" v-html="table"></div>
    <div class="report-table" v-else>
      <van-empty image="search" description="暂无数据" />
    </div>
    <van-popup v-model:show="showImg" position="bottom" :style="{ height: '50%' }" >
      <van-image
      width="100%"
      height="100%"
      :src="picUrl"
    />
    </van-popup>
  </div>
</template>

<script>
export default {
  name: "ReportTable",
  props: {
    info: {
      type: String,
      default: "",
    },
    imgs: {//单元格图片信息
      type: Object,
      default: () => ({}),
    },
    drillCells: {//下钻单元格信息
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      table: "",
      showImg:false,
      picUrl:"",
    };
  },
  components: {},
  watch: {
    info: {
      handler(newValue) {
        this.table = newValue;
        var that = this;
        this.$nextTick(() => {
          if(that.imgs)
          {
            that.bindImgClick(that.imgs);
          }
          if(that.drillCells)
          {
            that.bindDrillClick(that.drillCells);
          }
        });
      },
      immediate: true,
    },
  },
  mounted() {},
  methods: {
    //绑定图片点击事件
    bindImgClick(imgs){
      var that = this;
      for(var key in imgs)
      {
        var td = document.getElementById(key);
        if(td)
        {
          td.onclick = function(e)
          {
            that.picUrl = "";
            that.showImg = true;
            that.picUrl = that.imgs[e.target.className]
          }
        }
      }
    },
    //绑定单元格下钻点击事件
    bindDrillClick(drillCells){
      var that = this;
      for(var key in drillCells)
      {
        var td = document.getElementById(key);
        if(td)
        {
          td.onclick = function(e)
          {
            var drillId = that.drillCells[e.target.className].drillId;
            var drillParams = that.drillCells[e.target.className].drillParams;
            // that.$parent.drill(drillId,drillParams)
            that.$emit("drill",drillId,drillParams);
          }
        }
      }
    }
  },
};
</script>

<style scoped lang="scss">
.report-table {
  // border: 1px solid #dadada !important;
}
:deep(table) {
  border: 1px solid #dadada !important;
  min-width: 100%;

  tr {
    // display: flex;
    border-bottom: 1px solid #dadada;
    &:last-child {
      border-bottom: 0;
    }
  }
  td {
    flex: 1;
    flex-shrink: 0;
    padding: 4px;
    color: #000;
    text-align: center;
    font-family: Roboto;
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 20px;
    letter-spacing: 0.1px;

    // display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #dadada !important;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis !important;
    max-width: 80px;
    min-width: 30px;
  }
  tr:hover :nth-child(n){
    overflow: visible;  /* 鼠标放上去显示全部文字 */
    word-wrap: break-word;
    white-space: normal;
  }
}
</style>
