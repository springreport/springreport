<template>
  <div class="calendar">
    <div class="calendar-header df-c-b">
      <div class="month">{{ currentMonth.clone().format("YYYY年MM月") }}</div>
      <div class="action">
        <i
          class="el-icon-arrow-left"
          style="
            color: rgba(0, 0, 0, 0.25);
            margin-right: 20px;
            cursor: pointer;
          "
          @click="handleMonth('subtract')"
        />
        <i
          class="el-icon-arrow-right"
          style="color: rgba(0, 0, 0, 0.25); cursor: pointer"
          @click="handleMonth('add')"
        />
      </div>
    </div>
    <div class="calendar-body">
      <div class="week-days df-c-b">
        <div v-for="day in weekDays" :key="day">{{ day }}</div>
      </div>
      <div class="days">
        <div
          v-for="(day, index) in daysInMonth"
          :key="index"
          class="day-item"
          @click="selectDate(day)"
        >
          <div
            class="date"
            :class="{
              'gray-color': currentMonth.clone().format('YYYY-MM') != day.format('YYYY-MM'),
              'is-current': today == day.format('YYYY-MM-DD'),
              'date-selected': day.format('YYYY-MM-DD') == value,
              executed: executed.includes(day.format('YYYY-MM-DD')),
              paused: paused.includes(day.format('YYYY-MM-DD')),
            }"
          >
            {{ day ? day.format("D") : "" }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
// 周一作为一周的开始
moment.updateLocale('en', {
  week: {
    dow: 1
  }
})

export default {
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      moment,
      currentMonth: moment(),
      today: moment().format('YYYY-MM-DD'),
      weekDays: ['一', '二', '三', '四', '五', '六', '日'],
      executed: [], // 已执行
      paused: [] // 已暂停
    }
  },
  computed: {
    daysInMonth() {
      const startOfMonth = this.currentMonth.clone().startOf('month')
      const endOfMonth = this.currentMonth.clone().endOf('month')
      const days = []

      // 前置补空位
      const emptyDaysCount = (startOfMonth.day() + 6) % 7

      for (let i = emptyDaysCount; i >= 1; i--) {
        days.push(startOfMonth.clone().subtract(i, 'days'))
      }

      // 填充月份
      for (let i = 0; i < endOfMonth.date(); i++) {
        days.push(startOfMonth.clone().add(i, 'days'))
      }

      // 后置补空位
      const remainingDaysCount = (7 - endOfMonth.day()) % 7

      for (let i = 1; i <= remainingDaysCount; i++) {
        days.push(endOfMonth.clone().add(i, 'days'))
      }
      const lastDat = days[days.length - 1].clone()
      const addDays = 49 - days.length
      if (addDays > 0) {
        for (let i = 1; i <= addDays; i++) {
          days.push(lastDat.clone().add(i, 'days'))
        }
      }

      return days
    }
  },
  created() {
    this.getData()
  },
  methods: {
    handleMonth(type) {
      console.log(type)
      if (type === 'add') {
        this.currentMonth = this.currentMonth.clone().add(1, 'month')
      } else {
        this.currentMonth = this.currentMonth.clone().subtract(1, 'month')
      }
      // 切换调用
      this.getData()
    },
    selectDate(day) {
      const time = day.format('YYYY-MM-DD')
      this.$emit('change', time)
    },
    // 模拟获取面板数据,startDate、endDate作为参数
    getData() {
      const startDate = this.daysInMonth[0].format('YYYY-MM-DD')
      const endDate =
        this.daysInMonth[this.daysInMonth.length - 1].format('YYYY-MM-DD')
      this.executed = []
      this.paused = []
      // 角标蓝色1是已执行 黄色2是已暂停
      const data = [
        // {
        //   date: '2024-12-08',
        //   status: 1
        // },
        // {
        //   date: '2024-12-16',
        //   status: 2
        // }
      ]
      this.executed = data
        .filter((item) => item.status === 1)
        .map((item) => item.date)
      this.paused = data
        .filter((item) => item.status === 2)
        .map((item) => item.date)
    }
  }
}
</script>

<style scoped lang="scss">
@import "@/element-variables.scss";

.calendar {
  width: 430px;
  margin: auto;
  border-radius: 4px;
}

.calendar-header {
  display: flex;
  height: 62px;
  padding: 0 16px;
}

.calendar-body {
  box-sizing: border-box;
  .week-days {
    display: flex;
    text-align: center;
    margin-bottom: 16px;
    & > div {
      width: calc(100% / 7);
      height: 38px;
      color: rgba(0, 0, 0, 0.65);
      text-align: center;
      font-family: "PingFang SC";
      font-size: 17.862px;
      font-style: normal;
      font-weight: 400;
      line-height: 38px; /* 200% */
    }
  }

  .days {
    display: flex;
    flex-wrap: wrap;
  }

  .day-item {
    margin-bottom: 16px;
    width: calc(100% / 7);
    text-align: center;
    height: 38px;
    cursor: pointer;
    box-sizing: border-box;
  }
  .gray-color {
    color: rgba(0, 0, 0, 0.25);
  }
  .date {
    margin: auto;
    width: 30px;
    height: 30px;
    line-height: 30px;
    border-radius: 50%;
    transition: all 0.3s;
    text-align: center;
    box-sizing: border-box;
    border: 1px solid #fff;
    position: relative;
    &:hover {
        color: #fff;
      background-color: $--color-primary;
    }
  }
  .is-current {
    border: 1px solid $--color-primary;
  }
  .date-selected {
    color: #fff;
    background-color: $--color-primary !important;
  }
  .executed::before {
    position: absolute;
    content: "";
    top: 0;
    right: 0;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: #3859ff;
  }
  .paused::before {
    position: absolute;
    content: "";
    top: 0;
    right: 0;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: #f48245;
  }
}
</style>
