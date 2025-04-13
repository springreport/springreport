<!--  -->
<template>
  <div class="template-header df-c-b">
    <div class="template-header-title"> 模板市场 </div>
    <div class="template-header-search">
      <el-input v-model="searchValue" placeholder="输入关键字搜索模板" clearable>
        <template #prefix>
          <el-icon class="el-input__icon">
            <search />
          </el-icon>
        </template>
      </el-input>
      <div class="search-btn" @click="searchTem()">立即搜索</div>
    </div>
    <div class="template-header-back" />
  </div>
</template>

<script>
  import { Calendar, Search } from '@element-plus/icons-vue';
  export default {
    data() {
      return {
        searchValue: '',
      };
    },

    methods: {
      searchTem() {
        this.$emit('search', this.searchValue);
      },
    },
    components: {
      Search,
    },
  };
</script>
<style lang="scss" scoped>
  @import '@/element-variables.scss';

  .template-header {
    height: 64px;
    padding: 0 16px;
    background: #fff;
    box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.05);
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 10;

    .template-header-title {
      color: rgba(0, 0, 0, 0.8);
      text-align: center;
      font-family: 'PingFang SC';
      font-size: 20px;
      font-style: normal;
      font-weight: bold;
      line-height: 25.413px;
      /* 127.065% */
      letter-spacing: 0.2px;
    }

    .template-header-search {
      width: 580px;
      position: relative;

      ::v-deep .el-input__wrapper {
        box-shadow: none;
      }

      ::v-deep .el-input__inner {
        height: 36px;
        line-height: 36px;
        border-radius: 54px;
        border: 1px solid rgba(0, 0, 0, 0.1);
        padding-left: 42px;
        padding-right: 100px;
      }

      ::v-deep .el-input__prefix {
        position: absolute;
        left: 28px;
      }

      ::v-deep .el-input__suffix {
        right: 100px;
      }

      ::v-deep .el-input__icon {
        line-height: 36px;
      }

      .search-btn {
        position: absolute;
        right: 14px;
        top: 4px;
        width: 70px;
        text-align: center;
        height: 30px;
        line-height: 30px;
        border-radius: 299px;
        background: $--color-primary;
        padding: 0 8px;
        color: #fff;
        font-family: 'PingFang SC';
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          opacity: 0.7;
        }
      }
    }
  }
</style>
