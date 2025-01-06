<template>
  <div>
    <el-dropdown @command="handleCommand">
      <div class="el-dropdown-link df-c">
        <img src="@/assets/img/user-circle.png" style="margin-right: 8px; height: 36px" />
        <span style="margin-right: 8px">{{ userName }}</span>
        <div class="role-name" :class="{ 'role-name-apply': roleName == '试用' }">
          {{ roleName }}
        </div>
        <icon-down-one fill="#959ea6" />
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
          <el-dropdown-item command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <modal
      ref="changePwdRef"
      :modalConfig="state.changePwdConfig"
      :modalForm="state.changePwdForm"
      :modalData="state.changePwdModalData"
      :modalHandles="state.changePwdModalHandles"
      @closeModal="closePwdModal()"
    ></modal>
  </div>
</template>

<script>
  export default {
    name: 'Avatar',
  };
</script>

<script setup>
  import { ref, reactive } from 'vue';
  import { useStore } from 'vuex';
  import { ElMessageBox, ElForm } from 'element-plus';
  import { setting } from '@/config/setting';
  import { useRouter } from 'vue-router';
  import commonUtil from '../../../components/common/common';
  import apis from '../../../components/common/api';
  import commonConstants from '../../../components/common/constants';
  const { title, recordRoute } = setting;
  const userName = ref(localStorage.getItem(commonConstants.sessionItem.userName));
  const roleName = ref(localStorage.getItem(commonConstants.sessionItem.roleName));
  const changePwdRef = ref(null);
  const store = useStore();
  const router = useRouter();

  defineProps({
    color: {
      type: String,
      default: '#666',
    },
  });

  const state = reactive({
    changePwdConfig: {
      title: '修改密码', //弹窗标题,值为:新增，查看，编辑
      show: false, //弹框显示
      formEditDisabled: false, //编辑弹窗是否可编辑
      width: '700px', //弹出框宽度
      modalRef: 'modalRef', //modal标识
      type: '1', //类型 1新增 2编辑 3保存
    },
    changePwdForm: [
      {
        type: 'Password',
        label: '旧密码',
        prop: 'oldPwd',
        rules: { required: true, maxLength: 32 },
      },
      {
        type: 'Password',
        label: '新密码',
        prop: 'newPwd',
        rules: { required: true, maxLength: 32 },
      },
    ],
    //modal表单 end
    //modal 数据 start
    changePwdModalData: {
      //modal页面数据
      oldPwd: '',
      newPwd: '',
    },
    changePwdModalHandles: [
      { label: '取消', type: 'default', handle: () => closePwdModal() },
      { label: '提交', type: 'primary', handle: () => changePwd() },
    ],
  });

  const handleCommand = (command) => {
    switch (command) {
      case 'logout':
        handleLogout();
        break;
      case 'changePwd':
        showChangePwdModal();
        break;
      default:
        break;
    }
  };

  const handleLogout = () => {
    ElMessageBox.confirm('您确定要退出系统？', '操作提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      dangerouslyUseHTMLString: true,
      type: 'warning',
    })
      .then(async () => {
        localStorage.removeItem(commonConstants.sessionItem.authorization);
        localStorage.removeItem(commonConstants.sessionItem.isAdmin);
        localStorage.removeItem(commonConstants.sessionItem.userName);
        localStorage.removeItem(commonConstants.sessionItem.roleName);
        localStorage.removeItem(commonConstants.sessionItem.apiList);
        localStorage.removeItem(commonConstants.sessionItem.isSystemMerchant);
        localStorage.removeItem(commonConstants.sessionItem.merchantNo);
        localStorage.removeItem(commonConstants.sessionItem.userId);
        router.push('/login');
      })
      .catch(() => {});
  };

  const showChangePwdModal = () => {
    state.changePwdConfig.show = true;
  };

  const closePwdModal = () => {
    state.changePwdConfig.show = false;
    commonUtil.clearObj(state.changePwdModalData);
    changePwdRef.value.$refs['modalFormRef'].resetFields();
  };

  const changePwd = () => {
    changePwdRef.value.$refs['modalFormRef'].validate((valid) => {
      if (valid) {
        console.log(1);
        var obj = {
          params: state.changePwdModalData,
          removeEmpty: false,
        };
        obj.url = apis.sysUser.changePwdApi;
        commonUtil.doPost(obj).then((response) => {
          if (response.code == '200') {
            closePwdModal();
            localStorage.removeItem(commonConstants.sessionItem.authorization);
            localStorage.removeItem(commonConstants.sessionItem.isAdmin);
            localStorage.removeItem(commonConstants.sessionItem.userName);
            localStorage.removeItem(commonConstants.sessionItem.roleName);
            localStorage.removeItem(commonConstants.sessionItem.apiList);
            localStorage.removeItem(commonConstants.sessionItem.isSystemMerchant);
            localStorage.removeItem(commonConstants.sessionItem.merchantNo);
            localStorage.removeItem(commonConstants.sessionItem.userId);
            router.push('/login');
          }
        });
      } else {
        return false;
      }
    });
  };
</script>

<style lang="scss" scoped>
  .el-dropdown-link {
    cursor: pointer;
    color: #000;
    font-size: 15px;
    font-weight: bold;
    .role-name {
      margin-right: 8px;

      color: #035dff;
      text-align: center;
      font-family: 'PingFang SC';
      font-size: 10px;
      font-style: normal;
      font-weight: 400;
      line-height: 10px; /* 91.667% */
      border-radius: 3px;
      border: 1px solid #035dff;
      background: #e1ecff;
      padding: 4px;
    }
    .role-name-apply {
      border: 1px solid #ff9f9f;
      background: #ffeded;
      color: #f01717;
    }
  }
  .avatar-dropdown {
    display: flex;
    align-content: center;
    align-items: center;
    justify-content: center;
    justify-items: center;
    height: $base-avatar-dropdown-height;
    padding: $base-padding-10;
    .user-avatar {
      width: $base-avatar-widht;
      height: $base-avatar-height;
      cursor: pointer;
      border-radius: $base-border-radius-circle;
    }
  }
</style>
