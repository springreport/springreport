<template>
  <div class="_tag">
    <el-scrollbar class="ycy_scrollbar">
      <div class="left">
        <div
          v-for="tag in tagsList"
          :key="tag.title"
          class="menu-tag df-c"
          :class="[
            `${tag.path.replaceAll('/', '')}`
          ]"
          @click="goPage(tag.path)"
        >
          <span class="menu-tag-title" :class="[isActive(tag) ? 'menu-tag-active' : '']">{{ tag.title }}</span>
          <div
            v-if="!tag.hideclose"
            class="close"
            @click.stop="handleCloseTag(tag)"
          />
        </div>
      </div>
    </el-scrollbar>

    <div class="right">
      <el-dropdown trigger="click" @command="handleCloseBtn">
        <div class="df-c more-menu">
          <span>更多菜单</span>
          <i class="el-icon-arrow-down" style="margin-left: 10px" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="closeOther">关闭其它</el-dropdown-item>
          <el-dropdown-item command="closeAll">关闭所有</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<style lang="scss" scoped>
@import "@/element-variables.scss";
._tag {
  flex: 0 0 56px;
  display: flex;
  align-items: center;
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  justify-content: space-between;
  .ycy_scrollbar {
    width: 100%;
    margin-right: 6px;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-y: hidden;
  }
  .left {
    display: -webkit-box;
    -webkit-box-align: center;
    height: 56px;
    align-items: center;
    // overflow-x: auto;
    // white-space: nowrap; /* 防止Tab项换行 */
    .el-tag {
      height: 32px;
      line-height: 32px;
      margin-left: 8px;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      &:first-child {
        margin-left: 0px;
      }
    }
    .menu-tag {
      padding: 0 16px;
      height: 56px;
      line-height: 56px;
      color: #333;
      font-size: 14px;
      cursor: pointer;
      // transition: all 0.3s;
      position: relative;

      .close {
        margin-left: 4px;
        background-image: url("~@/static/img/common/close.png");
        width: 12px;
        height: 12px;
        background-size: 100% 100%;
      }
    }
    .menu-tag-title:hover,
    .menu-tag-active {
      color: $--color-primary;
      font-weight: bold;
      position: relative;
      &::before {
        position: absolute;
        content: "";
        left: 50%;
        bottom: 0;
        width: 60%;
        max-width: 62px;
        transform: translate(-50%, -50%);
        height: 3px;
        background: $--color-primary;
        border-radius: 2px;
      }
    }
  }

  .right {
    height: 56px;
    display: flex;
    align-items: center;
    flex-shrink: 0;
    .more-menu {
      cursor: pointer;
      color: #333;
      font-feature-settings: "liga" off, "clig" off;
      font-family: "PingFang SC";
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: normal;
      position: relative;
      padding: 0 14px;

      &:after {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 1px;
        height: 18px;
        background: rgba(52, 52, 52, 0.1);
      }
    }
  }
}
</style>

<script>
export default {
  data() {
    return {
      tagsList: [
        {
          hideclose: true,
          name: '系统首页',
          path: '/index',
          title: '系统首页'
        }
      ]
    }
  },
  watch: {
    // 路由变化,设置标签
    $route(newValue, oldValue) {
      this.setTags(newValue)
    }
  },
  mounted() {
    this.setTags(this.$route)
  },
  methods: {
    // 设置标签
    setTags(route) {
      if (route.name == 'tempRefresh') {
        return
      }
      const isExsit = this.tagsList.some((item) => {
        if (item.path.indexOf('?') >= 0) {
          if (item.path.split('?')[0] == route.fullPath.split('?')[0]) {
            item.path = route.fullPath
            return true
          } else {
            return false
          }
        } else {
          return item.path === route.fullPath
        }
      })
      if (!isExsit) {
        this.tagsList.push({
          title: route.meta.title, // 标签名
          name: route.name, // 路由里的name对应vue页的name,标签列表里的name可以做vue页面缓存
          path: route.fullPath, // 路由
          hideclose: route.meta.hideclose ? route.meta.hideclose : false // 是否隐藏关闭
        })
      }
      this.GLOBAL.tagsList = this.tagsList

      this.$nextTick(() => {
        this.scrollIntoView(route.path)
      })
    },
    goPage(path) {
      this.$router.push(path)
      this.scrollIntoView(path)
    },
    // 滚动到元素所在位置
    scrollIntoView(path) {
      const currentEle = document.querySelector(`.${path.replaceAll('/', '')}`)
      if (currentEle) {
        currentEle.scrollIntoView({
          behavior: 'smooth', // 平滑滚动
          block: 'nearest', // 保持垂直位置不变
          inline: 'end' // 水平方向上尽可能接近视口边缘
        })
      }
    },
    // 关闭标签
    handleCloseTag(tag) {
      this.tagsList.splice(this.tagsList.indexOf(tag), 1)
      if (this.tagsList.length > 0) {
        this.$router.push(this.tagsList[this.tagsList.length - 1].path)
      } else {
        this.$router.push('/index')
      }
    },
    // 关闭功能按钮
    handleCloseBtn(command) {
      if (command == 'closeOther') {
        // 关闭其它,保留没有删除的标签
        var activeTag = this.tagsList.find((item) => {
          return item.path == this.$route.fullPath
        }) // 查找第一个满足的
        var noCloseTags = this.getNoCloseTabs()
        if (
          !noCloseTags.some((item) => {
            return item.path == activeTag.path && item.title == activeTag.title
          })
        ) {
          // 不包含
          noCloseTags = noCloseTags.concat(activeTag)
        }
        this.tagsList = noCloseTags
      } else if (command == 'closeAll') {
        // 关闭所有,保留没有删除的标签
        this.tagsList = this.getNoCloseTabs()
        this.$router.push(this.tagsList[this.tagsList.length - 1].path)
      }
    },
    getNoCloseTabs() {
      // 获取没有删除的标签
      var noCloseList = this.tagsList.filter((item) => {
        return item.hideclose == true
      })
      return noCloseList
    },
    handleClick(tag) {
      this.$router.push(tag.fullPath)
    },
    // 是否选中
    isActive(tag) {
      if (tag.path == this.$route.fullPath) {
        return true
      } else {
        return false
      }
    }
  }
}
</script>
