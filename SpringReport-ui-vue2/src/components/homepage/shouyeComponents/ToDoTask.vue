<!--  -->
<template>
  <div class="task df">
    <div class="task-left">
      <div class="task-title">待办任务</div>
      <MyCalendar v-model="date" />
    </div>
    <div class="line" />
    <div class="task-right">
      <div class="task-tab">
        <div
          v-for="item in taskType"
          :key="item.value"
          class="tab-item"
          :class="{ 'tab-item-active': item.value == activeTask }"
          @click="changeTab(item.value)"
        >
          {{ item.label }}
        </div>
      </div>

      <div class="task-list">
        <div v-for="item in 3" :key="item" class="task-item">
          <div class="df-c-b">
            <div class="task-item-title overflow-text">
              任务名称任务名称任务名称任务名称任务名称任务名称任务名称任务名称任务名称任务名称任务名称任务名称
            </div>
            <div class="status">
              已暂停
            </div>
          </div>
          <div class="desc">导出类型：Excel</div>
          <div class="desc">时间类型：指定时间</div>
          <div class="desc">任务执行时间：2024-10-30 00:00:00</div>
          <div class="desc">
            发送邮箱：mayaghost@163.com；109807790@qq.com
          </div>
        </div>
      </div>

      <el-pagination :page-size="pagination.pageSize" :current-page="pagination.currentPage" small layout="prev, pager, next" :total="pagination.total" @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script>
import MyCalendar from './MyCalendar'
import moment from 'moment'
export default {
  components: {
    MyCalendar
  },
  data() {
    return {
      date: moment().format('YYYY-MM-DD'),
      activeTask: 1,
      taskType: [
        { label: '全部任务', value: 1 },
        { label: '我创建的', value: 2 },
        { label: '我执行的', value: 3 },
        { label: '我暂停的', value: 4 }
      ],
      pagination: {
        total: 100,
        pageSize: 3,
        currentPage: 1
      }
    }
  },
  computed: {},
  watch: {
    date() {
      this.pagination.currentPage = 1
      this.getData()
    }
  },
  created() {},
  mounted() {},
  methods: {
    changeTab(val) {
      this.activeTask = val
      this.pagination.currentPage = 1
      this.getData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.getData()
    },
    getData() {

    }
  }
}
</script>
<style lang='scss' scoped>
@import "@/element-variables.scss";

::v-deep .el-pager li {
  font-size: 14px;
  color: #666;
  font-weight: normal;
}
::v-deep .el-pager li.active {
  color: $--color-primary;
  border-radius: 3px;
  border: 1px solid $--color-primary;
}
.task {
  margin-top: 15px;
  border-radius: 5px;
  background: #fff;
  padding: 16px;
  .task-title {
    color: #1a1a1a;
    font-family: "PingFang SC";
    font-size: 18px;
    font-style: normal;
    font-weight: 500;
    line-height: 28px; /* 155.939% */
    margin-bottom: 16px;
    padding-left: 4px;
  }
  .line {
    width: 1px;
    height: 524px;
    background-color: rgba($color: #000000, $alpha: 0.1);
  }
  .task-right {
    flex: 1;
    height: 100%;
  }
  .task-tab {
    display: flex;
    width: 465px;
    height: 59px;
    padding: 0px 16px;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;
    .tab-item {
      cursor: pointer;
      padding: 8px 16px;
      color: #666;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: normal;
      transition: all 0.3s;
      &:hover {
        color: #17b794;
        font-weight: 500;
        border-radius: 4px;
        background: rgba(23, 183, 148, 0.05);
      }
    }
    .tab-item-active {
      color: #17b794;
      font-weight: 500;
      border-radius: 4px;
      background: rgba(23, 183, 148, 0.05);
    }
  }
  .task-list {
    padding: 0 16px;
    .task-item {
      padding: 16px 0 4px 0;
      border-bottom: 1px solid #edf0f7;
      &:last-child {
        border-bottom: none;
      }
      .task-item-title {
        flex: 1;
        color: #3c3c3c;
        font-size: 14px;
        font-style: normal;
        font-weight: 500;
        line-height: 22px; /* 157.143% */
        margin-bottom: 12px;
      }
      .status {
        color: #3859ff;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 12px; /* 100% */
      }

      .desc {
        color: #666;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 12px; /* 100% */
        margin-bottom: 12px;
      }
    }
  }
}
</style>
