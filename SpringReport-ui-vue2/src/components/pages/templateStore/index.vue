<!--  -->
<template>
  <div class="template-store">
    <TemHeader @search="searchTem" />
    <div class="template-type df-c-c">
      <div
        v-for="item in temTypeList"
        :key="item.value"
        :class="{ 'template-item-active': item.value == searchForm.temType }"
        class="template-item df-c"
        @click="changeTemType(item.value)"
      >
        <img class="img" :src="require('@/static/img/template/' + item.img)">
        <div class="name">{{ item.label }}</div>
      </div>
    </div>
    <div class="container">
      <div class="sub-type-list df">
        <div
          v-for="item in subTypeList"
          :key="item.value"
          class="sub-item"
          :class="{ 'sub-item-active': item.value == searchForm.subType }"
          @click="changeSubType(item.value)"
        >
          {{ item.label }}
        </div>
      </div>

      <div>
        <el-row :gutter="24">
          <template v-if="temList.length">
            <el-col v-for="item in temList" :key="item.id" :xl="6" :lg="8" :md="8" style="margin-bottom: 24px;">
              <div class="tem-item">
                <div class="tem-item-img df">
                  <!-- 如果有图片则给按照设计稿铺满 -->
                  <img v-if="item.img" :src="item.img">
                  <!-- 没有图片给默认样式 -->
                  <div v-else class="default-box">
                    <img :src="require(`@/static/img/template/${searchForm.temType}-default.png`)">
                  </div>
                </div>
                <div class="tem-item-content">
                  <div class="title overflow-text">{{ item.name }}</div>
                  <div class="df-c-b">
                    <div class="df">
                      <div class="tag">SaaS软件</div>
                      <div class="tag">PPT模板</div>
                    </div>
                    <div class="use-btn">立即使用</div>
                  </div>
                </div>
              </div>
            </el-col>
          </template>
          <el-empty v-else description="暂无模板" :span="24" />

        </el-row>
      </div>

      <!-- 分页 -->
      <div v-if="temList.length" class="ces-pagination df-c-c" style="margin-bottom: 96px;">
        <el-pagination
          background
          layout="total,prev, pager, next,sizes ,jumper"
          :page-size="tablePage.pageSize"
          :page-sizes="tablePage.pageSizeRange"
          :current-page.sync="tablePage.currentPage"
          :total.sync="tablePage.total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { Loading } from 'element-ui'

import TemHeader from './components/TemHeader.vue'
export default {
  components: {
    TemHeader
  },
  data() {
    return {
      searchForm: {
        keyword: '',
        temType: 'ppt',
        subType: 1
      },
      // 模板类型
      temTypeList: [
        {
          label: 'PPT模板',
          value: 'ppt',
          img: 'ppt.png'
        },
        {
          label: 'Excel模板 ',
          value: 'excel',
          img: 'excel.png'
        },
        {
          label: 'Word模板',
          value: 'word',
          img: 'word.png'
        },
        {
          label: '大屏模板',
          value: 'screen',
          img: 'screen.png'
        }
      ],
      // 小类
      subTypeList: [
        {
          label: '销售管理',
          value: 1
        },
        {
          label: '市场营销',
          value: 2
        }
      ],
      // 模板列表
      temList: [
        {
          id: 1,
          name: 'Unity机房数字孪生项Unity机房数战目实战课战目实战课战目实战课',
          img: require('@/static/img/template/screen1.png')
        },
        {
          id: 2,
          name: '机器人可视化大屏'
        },
        {
          id: 3,
          name: 'Unity机房数字孪生项Unity机房数战目实战课战目实战课战目实战课'
        },
        {
          id: 4,
          name: '机器人可视化大屏'
        },
        {
          id: 5,
          name: 'Unity机房数字孪生项Unity机房数战目实战课战目实战课战目实战课'
        },
        {
          id: 6,
          name: '机器人可视化大屏'
        },
        {
          id: 7,
          name: 'Unity机房数字孪生项Unity机房数战目实战课战目实战课战目实战课'
        },
        {
          id: 8,
          name: '机器人可视化大屏'
        },
        {
          id: 9,
          name: 'Unity机房数字孪生项Unity机房数战目实战课战目实战课战目实战课'
        },
        {
          id: 10,
          name: '机器人可视化大屏'
        }
      ],
      tablePage: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
        pageSizeRange: [5, 10, 20, 50, 100]
      }
    }
  },
  computed: {},
  watch: {},
  created() {
    const temType = this.$route.query.temType
    this.searchForm.temType = temType || 'ppt'
  },

  methods: {
    searchTem(val) {
      this.tablePage.currentPage = 1
      this.searchForm.keyword = val
      this.getData()
    },
    // 模板类型
    changeTemType(val) {
      this.tablePage.currentPage = 1
      this.searchForm.temType = val
      this.getData()
    },
    // 小类
    changeSubType(val) {
      this.tablePage.currentPage = 1
      this.searchForm.subType = val
      this.getData()
    },
    // 更改页码
    handleCurrentChange(val) {
      this.tablePage.currentPage = val
      this.getData()
    },
    // 更改每页条数
    handleSizeChange(val) {
      this.tablePage.currentPage = 1
      this.tablePage.pageSize = val
      this.getData()
    },
    // 获取数据
    getData() {
      const loadingInstance = Loading.service({ fullscreen: true })
      setTimeout(() => {
        loadingInstance.close()
      }, 500)
    }
  }
}
</script>
<style lang='scss' scoped>
@import "@/element-variables.scss";

.template-store {
    padding-top: 88px;
    overflow-y: auto;
    height: 100%;

    .template-type {
        margin-bottom: 24px;

        .template-item {
            cursor: pointer;
            margin-right: 32px;
            padding: 9px 26px;
            border-radius: 30px;
            transition: all 0.3s;

            &:hover {
                background: rgba(23, 183, 148, 0.10);

                .name {
                    color: $--color-primary;
                }
            }

            &:last-child {
                margin-right: 0;
            }

            .img {
                width: 28px;
                height: 28px;
                margin-right: 12px;
            }

            .name {
                color: #595959;
                font-family: "PingFang SC";
                font-size: 18px;
                font-style: normal;
                font-weight: bold;
                line-height: 26px;
                /* 144.444% */
                letter-spacing: 2px;
            }
        }

        .template-item-active {
            background: rgba(23, 183, 148, 0.10);

            .name {
                color: $--color-primary;
            }
        }
    }

    .container {
        width: 84%;
        max-width: 1600px;
        margin: 0 auto;
    }

    .sub-type-list {
        border-radius: 4px;
        border: 1px solid rgba(0, 0, 0, 0.10);
        background: #FAFAFA;
        padding: 10px 30px 0px;
        flex-wrap: wrap;
        margin-bottom: 40px;

        .sub-item {
            padding: 0 10px;
            border-radius: 4px;
            height: 26px;
            line-height: 26px;
            color: #595959;
            font-family: "PingFang SC";
            font-size: 14px;
            font-style: normal;
            font-weight: 400;
            margin-right: 12px;
            margin-bottom: 10px;
            transition: all 0.3s;
            cursor: pointer;

            &:hover {
                background: $--color-primary;
                color: #fff;
            }

        }

        .sub-item-active {
            background: $--color-primary;
            color: #fff;
        }
    }

    .tem-item {
        box-shadow: 0px 0px 20px 0px rgba(7, 49, 40, 0.20);
        border-radius: 8px;

        .tem-item-img {
            height: 240px;
            background: #F3F3F3;
            border-radius: 8px 8px 0 0;
            align-items: flex-end;
            justify-content: center;
            img{
                width: 100%;
                height: 100%;
                display: block;
                border-radius: 8px 8px 0 0;
            }
            .default-box{
                width: auto;
                height: 200px;
                img{
                    width: 100%;
                    border-radius:0;
                    margin: 0 auto;
                }
            }
        }

        .tem-item-content {
            border-radius: 0 0 8px 8px;
            padding: 12px 12px 15px 20px;
            background: #FFF;

            .title {
                color: #272C40;
                font-family: "PingFang SC";
                font-size: 16px;
                font-style: normal;
                font-weight: bold;
                line-height: 24px;
                margin-bottom: 12px;
            }

            .tag {
                height: 24px;
                line-height: 24px;
                padding: 0 10px;
                color: #7A7A7A;
                font-family: "PingFang SC";
                font-size: 12px;
                font-style: normal;
                font-weight: 400;
                border-radius: 2px;
                background: #F1F1F1;
                margin-right: 8px;
                &:last-child{
                    margin-right: 0;
                }
            }

            .use-btn {
                width: 90px;
                height: 34px;
                line-height: 34px;
                text-align: center;
                border-radius: 6px;
                border: 1px solid $--color-primary;
                background: #FFF;
                color: $--color-primary;
                font-size: 14px;
                font-style: normal;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s;
                &:hover {
                    color: #fff;
                    background-color: $--color-primary;
                }
            }
        }
    }
    .ces-pagination{
        padding: 16px 0;
    }
}
</style>
