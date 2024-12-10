<!--  -->
<template>
  <div class="statement">
    <div class="statement-title">报表摘要</div>
    <div class="statement-statistics df">
      <div
        v-for="(item, index) in statistics"
        :key="index"
        class="statistics-item df-c-b"
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
          num: 658,
          icon: require('@/static/img/homePage/excel.png')
        },
        {
          title: 'Word报表(个)',
          num: 1822,
          icon: require('@/static/img/homePage/word.png')
        },
        {
          title: 'PPT模板(个)',
          num: 451,
          icon: require('@/static/img/homePage/ppt.png')
        },
        {
          title: '大屏模板(个)',
          num: 45,
          icon: require('@/static/img/homePage/screen.png')
        },
        {
          title: '数据源(个)',
          num: 786,
          icon: require('@/static/img/homePage/database.png')
        }
      ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {}
}
</script>
<style lang='scss' scoped>
.statement {
  margin-top: -122px;
  width: 100%;
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
      &:last-child {
        margin-right: 0;
      }
      .label {
        color: #666;
        text-align: center;
        font-size: 15px;
        font-style: normal;
        font-weight: 400;
        line-height: 14px; /* 93.333% */
        margin-bottom: 16px;
      }
      .data-num {
        color: #1a1a1a;
        text-align: center;
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
