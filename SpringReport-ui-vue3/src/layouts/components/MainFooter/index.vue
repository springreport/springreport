<!--
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2021-09-18 08:49:59
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-06-30 18:21:29
-->
<template>
  <el-footer class="footer df-c">
    <div class="filing-information df-c-c">
      <a
        target="_blank"
        href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=37021302001006"
        >鲁公网安备 37021302001006号</a
      >
      <img src="@/assets/img/aicon.png" class="beian-icon" />
      <a target="_blank" href="https://beian.miit.gov.cn/">鲁ICP备2021031250号-2</a>
    </div>
    <!-- <span class="text"></span> -->
  </el-footer>
</template>
<style lang="scss" scoped>
  .footer {
    height: 44px !important;
  }
  .filing-information {
    text-align: center;
    width: fit-content;
    margin: 0 auto;
    a {
      color: rgba(0, 0, 0, 0.4);
      font-size: 12px;
      line-height: 16px;
      height: 16px;
      text-decoration: none;
    }
    .beian-icon {
      width: 16px;
      height: 16px;
      margin-left: 8px;
    }
  }
</style>
