<template>
  <div>
    <el-header class="_header df-c-b">
      <div class="left df-c">
        <img
          v-show="!navShow"
          src="@/static/img/logoWithName.png"
          height="30px"
          style="margin-left:28px"
        >
        <img v-show="navShow" src="@/static/img/logo.png" height="24px" style="margin-left:20px">
      </div>
      <div class="right df-c">
        <div v-if="isSystemMerchant == 1" class="role-name">商户</div>
        <div v-if="isSystemMerchant == 1" class="role-name">
          <el-select v-model="merchantNo" placeholder="请选择" size="small" @change="changeMerchant">
            <el-option
              v-for="item in merchants"
              :key="item.merchantNo"
              :label="item.merchantName"
              :value="item.merchantNo"
            />
          </el-select>
        </div>
        <div class="role-name">{{ roleName }}</div>
        <el-dropdown class="white font" trigger="click" placement="bottom" @command="handleCommand">
          <span class="el-dropdown-link df-c">
            <img src="@/static/img/user-circle.png" height="20" style="margin-right:8px">
            {{ username }}
            <i class="el-icon-arrow-down el-icon--right" />
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
            <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    <!-- <div class="collapse-btn" @click="navChangeShow" :style="{width:!navShow?'232px':'64px'}"></div> -->
    </el-header>
    <modal
      ref="changePwd"
      :modal-config="changePwdConfig"
      :modal-form="changePwdForm"
      :modal-data="changePwdModalData"
      :modal-handles="changePwdModalHandles"
      @closeModal="closePwdModal()"
    />
  </div>
</template>
<script>
import bus from '../common/bus'
export default {
  data() {
    return {
      navShow: false, // 左侧导航是否折叠
      fullscreen: false, // 全屏
      roleName: '', // 角色
      username: '', // 用户名
      isSystemMerchant: 2,
      merchants: [],
      merchantNo: '',
      changePwdConfig: {
        title: '修改密码', // 弹窗标题,值为:新增，查看，编辑
        show: false, // 弹框显示
        formEditDisabled: false, // 编辑弹窗是否可编辑
        width: '700px', // 弹出框宽度
        modalRef: 'modalRef', // modal标识
        type: '1'// 类型 1新增 2编辑 3保存
      },
      changePwdForm: [
        { type: 'Password', label: '旧密码', prop: 'oldPwd', rules: { required: true, maxLength: 32 }},
        { type: 'Password', label: '新密码', prop: 'newPwd', rules: { required: true, maxLength: 32 }}
      ],
      // modal表单 end
      // modal 数据 start
      changePwdModalData: { // modal页面数据
        oldPwd: '',
        newPwd: ''
      },
      changePwdModalHandles: [
        { label: '取消', type: 'default', handle: () => this.closePwdModal() },
        { label: '提交', type: 'primary', handle: () => this.changePwd() }
      ]
    }
  },
  mounted() {
    this.roleName = localStorage.getItem('roleName')
    this.username = localStorage.getItem('userName')
    this.isSystemMerchant = localStorage.getItem('isSystemMerchant')
    if (this.isSystemMerchant == 1) {
      this.merchantNo = localStorage.getItem('merchantNo')
      this.getMerchants()
    }
  },
  created() {
    bus.$on('navShowChange', (navShow) => {
      this.navShow = navShow
    })
  },
  methods: {
    getMerchants() {
      var object = {
        url: this.apis.login.getMerchantListApi,
        params: {},
        removeEmpty: false
      }
      var that = this
      this.commonUtil.doPost(object).then((response) => {
        if (response.code === '200') {
          that.merchants = response.responseData
        }
      })
    },
    changeMerchant() {
      localStorage.setItem(
        this.commonConstants.sessionItem.merchantNo,
        this.merchantNo
      )
      // location.reload()
      this.$router.replace({
        path: '/tempRefresh'
      })
    },
    // 全屏事件
    handleFullScreen() {
      const element = document.documentElement
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    },
    // 下拉框事件
    handleCommand(command) {
      if (command == 'loginout') {
        localStorage.removeItem(this.commonConstants.sessionItem.authorization)
        localStorage.removeItem(this.commonConstants.sessionItem.position)
        localStorage.removeItem(this.commonConstants.sessionItem.userName)
        localStorage.removeItem(this.commonConstants.sessionItem.roleName)
        localStorage.removeItem(this.commonConstants.sessionItem.apiList)
        localStorage.removeItem(this.commonConstants.sessionItem.isSystemMerchant)
        localStorage.removeItem(this.commonConstants.sessionItem.merchantNo)
        localStorage.removeItem(this.commonConstants.sessionItem.userId)
        this.$router.replace('/login')
      } else if (command == 'changePwd') {
        alert('你小子别改我密码了，想改密码自己下载源码本地部署哦！')
        // this.showChangePwdModal();
      }
    },
    showChangePwdModal() {
      this.changePwdConfig.show = true
    },
    closePwdModal() {
      this.changePwdConfig.show = false// 关闭modal
      this.commonUtil.clearObj(this.changePwdModalData)// 清空modalData
      this.$refs['changePwd'].$refs['modalFormRef'].resetFields()// 校验重置
    },
    changePwd() {
      var that = this
      this.$refs['changePwd'].$refs['modalFormRef'].validate((valid) => {
        if (valid) {
          var obj = {
            params: that.changePwdModalData,
            removeEmpty: false
          }
          obj.url = that.apis.sysUser.changePwdApi
          that.commonUtil.doPost(obj).then(response => {
            if (response.code == '200') {
              that.closePwdModal()
              localStorage.removeItem(that.commonConstants.sessionItem.authorization)
              localStorage.removeItem(that.commonConstants.sessionItem.position)
              localStorage.removeItem(that.commonConstants.sessionItem.userName)
              localStorage.removeItem(that.commonConstants.sessionItem.roleName)
              localStorage.removeItem(that.commonConstants.sessionItem.apiList)
              localStorage.removeItem(that.commonConstants.sessionItem.isSystemMerchant)
              localStorage.removeItem(that.commonConstants.sessionItem.merchantNo)
              localStorage.removeItem(this.commonConstants.sessionItem.userId)
              that.$router.replace('/login')
            }
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
._header {
  height: 56px !important;
  padding: 0px;
  background-color: #fff;
  border-bottom: 1px solid #ccc;
  .left {
    img {
      cursor: pointer;
      transition: all 0.3s;
    }
  }
  .right {
    padding-right: 24px;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.9);
    .role-name {
      margin-right: 8px;
    }
  }
  .el-dropdown-link {
    color: rgba(0, 0, 0, 0.9);
  }
}
// .collapse-btn {
//   position: fixed;
//   bottom: 0;
//   left: 0;
//   background: #ffffff;
//   box-shadow: inset 0px 1px 0px #f0f0f0;
//   width: 232px;
//   height: 56px;
//   z-index: 100;
//   transition: all 0.3s;
// }
</style>
