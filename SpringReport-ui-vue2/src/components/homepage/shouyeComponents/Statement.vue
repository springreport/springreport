<!--  -->
<template>
  <div class="statement">
    <div class="statement-title">报表摘要</div>
    <div class="statement-statistics df">
      <div
        v-for="(item, index) in statistics"
        :key="index"
        class="statistics-item df-c-b"
        @click="routerTo(item)"
      >
        <div>
          <div class="label">{{ item.title }}</div>
          <div class="data-num">{{ item.num }}</div>
        </div>
        <img :src="item.icon" class="icon">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      statistics: [
        {
          title: 'Excel报表(个)',
          num: 0,
          icon: require('@/static/img/homePage/excel.png'),
          path: 'reportTpl'
        },
        {
          title: 'Word报表(个)',
          num: 0,
          icon: require('@/static/img/homePage/word.png'),
          path: 'docTpl'
        },
        {
          title: '协同文档(个)',
          num: 0,
          icon: require('@/static/img/homePage/excel.png'),
          path: 'onlineTpl'
        },
        {
          title: '大屏模板(个)',
          num: 0,
          icon: require('@/static/img/homePage/screen.png'),
          path: 'screenTpl'
        },
        {
          title: '数据源(个)',
          num: 0,
          icon: require('@/static/img/homePage/database.png'),
          path: 'reportDatasource'
        }
      ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  activated(){
    this.getIndexData()
  },
  mounted() {
    this.getIndexData()
  },
  methods: {
    getIndexData() {
      var obj = {
        params: {merchantNo:localStorage.getItem(this.commonConstants.sessionItem.merchantNo)},
        removeEmpty: false,
        url: this.apis.index.getIndexDataApi
      }
      var that = this
      this.commonUtil.doPost(obj).then(response => {
        if (response.code == '200') {
          if (response.responseData) {
            that.statistics[0].num = response.responseData.excelCount
            that.statistics[1].num = response.responseData.wordCount
            that.statistics[2].num = response.responseData.coeditCount
            that.statistics[3].num = response.responseData.screenCount
            that.statistics[4].num = response.responseData.datasourceCount
          }
        }
      })
    },
    routerTo(item) {
      this.$router.push({ name: item.path })
    }
  }
}
</script>
<style lang='scss' scoped>
.statement {
  width: 100%;
  margin-top: -20px;
  padding: 18px 26px 21px 26px;
  border-radius: 20px 20px 0px 0px;
  background: #fff;
  box-sizing: border-box;
  position: relative;
  .statement-title {
    color: #000;
    font-size: 18px;
    font-style: normal;
    font-weight: 500;
    line-height: 28px; /* 155.556% */
    margin-bottom: 19px;
  }
  .statement-statistics {
    .statistics-item {
      flex: 1;
      margin-right: 16px;
      padding: 16px 24px;
      box-sizing: border-box;
      border-radius: 6px;
      background: #f3f4f5;
      cursor: pointer;
      &:last-child {
        margin-right: 0;
      }
      .label {
        color: #666;
        text-align: left;
        font-size: 15px;
        font-style: normal;
        font-weight: 400;
        line-height: 14px; /* 93.333% */
        margin-bottom: 16px;
      }
      .data-num {
        color: #1a1a1a;
        text-align: left;
        font-size: 24px;
        font-style: normal;
        font-weight: 400;
        line-height: 18px; /* 75% */
      }

      .icon {
        width: 66px;
        height: 66px;
      }
    }
  }
}
</style>
