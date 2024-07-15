<template>
  <div class="index-conntainer">
    <div class="content">
      <el-row :gutter="10">
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
          <el-card class="card" shadow="hover">
            <template #header>
              <h3 class="title">生产环境依赖信息</h3>
            </template>
            <el-descriptions class="margin-top" :column="3" border>
              <el-descriptions-item v-for="(value, key) in packpage.dependencies" :key="key">
                <template #label>{{ key }}</template>
                {{ value }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Index',
};
</script>

<script setup>
import packpage from '../../../package.json';
onBeforeMount(() => {});
</script>

<style lang="scss" scoped>
.index-conntainer {
  width: $base-width;
  .head-card {
    display: flex;
    align-items: center;
    padding: $base-main-padding;
    background-color: $base-color-white;
    &-content {
      padding-left: 15px;
      .desc {
        color: $base-font-color;
      }
    }
  }
  .content {
    margin: 15px 0;
    .count-box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .item {
        display: flex;
        flex-direction: column;
        text-align: center;
        .label {
          padding: 10px 0;
          font-size: $base-font-size-big;
        }
        .count {
          font-size: $base-font-size-max;
          font-weight: bolder;
          color: $base-color-primary;
          &.error {
            color: var(--el-color-danger);
          }
          &.success {
            color: var(--el-color-success);
          }
        }
      }
    }
    .title {
      margin: 0;
    }
    .skill-title {
      padding: 10px 0;
      font-weight: 500;
    }
    .card {
      margin-bottom: 15px;
      &-body {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        &.mobile {
          grid-template-columns: repeat(1, 1fr);
        }
        .item {
          box-sizing: border-box;
          padding: 10px 20px;
          margin-top: -1px;
          margin-left: -1px;
          overflow: hidden;
          cursor: pointer;
          border: 1px solid black;
          border: 1px solid #eee;
          transition: box-shadow 0.5;
          .lf {
            display: flex;
            align-items: center;
            max-width: 140px;
            .img {
              width: auto;
              max-width: 120px;
              height: auto;
              max-height: 40px;
            }
          }
          &:hover {
            box-shadow: $base-box-shadow;
          }
          .title {
            padding-left: 5px;
            font-size: 18px;
            font-weight: bold;
          }
          .desc {
            padding: 5px 0;
            font-size: 13px;
            line-height: 1.5;
            color: $base-font-color;
          }
        }
      }
    }
  }
}
</style>
