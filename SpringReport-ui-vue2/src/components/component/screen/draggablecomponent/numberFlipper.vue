<!-- src/components/NumberFlipper.vue -->
<template>
  <div class="number-flipper df-c">
    <!-- 前缀 -->
    <div
      class="prefix"
      v-if="prefix"
      :style="{
        color: prefixColor?prefixColor:'#FFF',
        fontSize: prefixFontSize+'px',
        fontWeight: 'bold',
      }"
    >
      {{ prefix }}
    </div>
    <!-- 数字翻牌器 -->
    <div
      class="number-flipper-box"
      v-for="(item, index) in formattedValue"
      :key="index"
      :style="{
        background: background,
        color: fontColor,
        fontSize: fontSize,
        height:boxHeight+'px',
        width:boxWidth+'px',
        lineHeight:boxHeight+'px'
      }"
    >
      <div class="number-separator" v-if="item == ','">{{ item }}</div>
      <div class="number-decimal" v-else-if="item == '.'">{{ item }}</div>

      <div class="number-list" :style="getDigitStyle(item, index)" v-else>
        <div
          class="number-value"
          :style="{
            height:boxHeight+'px',
            width:boxWidth+'px',
          }"
          v-for="(item2, index2) in numberList"
          :key="index2"
        >
          {{ item2 }}
        </div>
      </div>
    </div>
    <!-- 后缀 -->
    <div
      class="suffix"
      v-if="suffix"
      :style="{
        color: suffixColor?suffixColor:'#FFF',
        fontSize: suffixFontSize+'px',
        fontWeight: 'bold',
      }"
    >
      {{ suffix }}
    </div>
  </div>
</template>
  
  <script>
export default {
  name: "NumberFlipper",
  props: {
    value: {
      type: [Number, String],
      default: 0,
    },
    // 是否启用千分位分割
    thousandSeparator: {
      type: Boolean,
      default: true,
    },
    // 小数点位数
    decimalPlaces: {
      type: Number,
      default: 0,
    },
    prefix: {
      type: String,
      default: "",
    },
    prefixColor: {
      type: String,
      default: "#FFF",
    },
    prefixFontSize: {
      type: String,
      default: "14",
    },
    // 后缀
    suffix: {
      type: String,
      default: "",
    },
    suffixColor: {
      type: String,
      default: "#FFF",
    },
    suffixFontSize: {
      type: String,
      default: "14",
    },
    // 动画持续时间（毫秒）
    duration: {
      type: Number,
      default: 1000,
    },
    // 背景颜色
    background: {
      type: String,
      default: "linear-gradient(to bottom, #1a1a2e 0%, #16213e 100%)",
    },
    // 背景颜色
    fontSize: {
      type: String,
      default: "32px",
    },
    // 背景颜色
    fontColor: {
      type: String,
      default: "#fff",
    },
    //翻牌器高度
    boxHeight: {
      type: String,
      default: "80",
    },
    //翻牌器宽度
    boxWidth: {
      type: String,
      default: "60",
    },
  },
  data() {
    return {
      numberList: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
      startAni: false,

      prevValueArr: [],
    };
  },
  computed: {
    formattedValue() {
      let num = parseFloat(this.value) || 0;

      // 处理小数位数
      if (this.decimalPlaces > 0) {
        num = parseFloat(num.toFixed(this.decimalPlaces));
      } else {
        num = Math.round(num);
      }

      // 处理千分位分割
      if (this.thousandSeparator) {
        const parts = num.toString().split(".");
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        return parts.join(".");
      }
      return num.toString();
    },
  },
  methods: {
    getDigitStyle(item, index) {
      // 确保只有数字才应用动画
      if (isNaN(item)) return {};
      if (this.startAni) {
        return {
          transform: `translateY(-${item * Number(this.boxHeight)}px)`,
          transition: `transform ${this.duration}ms linear`,
        };
      }
      return {
        transform:
          this.prevValueArr && this.prevValueArr.length
            ? `translateY(-${this.prevValueArr[index] * Number(this.boxHeight)}px)`
            : "translateY(0px)",
      };
    },
  },
  watch: {
    value: {
      handler(_, oldVal) {
        this.startAni = false;
        // 记录上一组的数字数据
        if (oldVal) {
          this.prevValueArr = [];
          oldVal
            .toString()
            .split("")
            .forEach((item) => {
              if (item !== ".") {
                this.prevValueArr.push(item);
              }
            });
        }
        // 延迟300s执行动画
        this.$nextTick(() => {
          setTimeout(() => {
            this.startAni = true;
          }, 300);
        });
      },
      immediate: true,
    },
  },
};
</script>
  
  <style scoped>
.number-flipper {
  font-family: "Arial", sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
}

.number-flipper-box {
  /* width: 60px;
  height: 80px; */
  text-align: center;
  /* line-height: 80px; */
  /* border: 2px dashed #0962ba; */
  margin-right: 8px;
  font-weight: bold;
  position: relative;
  overflow: hidden;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.number-list {
  width: 60px;
  height: 800px; /* 10个数字 * 80px */
  position: absolute;
  display: flex;
  flex-direction: column;
}

.number-value {
  /* width: 60px;
  height: 80px; */
  display: flex;
  align-items: center;
  justify-content: center;
}

.number-decimal,
.number-separator {
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.suffix,
.prefix {
  margin-right: 8px;
  /* font-size: 16px; */
  margin-left: 4px;
  padding-bottom: 2px;
  align-self: flex-end;
}
</style>