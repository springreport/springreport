<template>
  <div class="pagebox">
    <div class="header">
      <div class="middle">
        <span style="margin-left: 6px">{{ screenProperties.name }}</span>
      </div>
      <div class="right">
        <!-- 发布 -->
        <div class="svg-box" @click="save()">
          <img src="@/assets/img/screenDesign/publish.png" width="18" height="18" />
          <span>发布</span>
        </div>
        <!-- 预览 -->
        <div class="svg-box" style="margin-left: 8px" @click="preview()">
          <img src="@/assets/img/screenDesign/preview.png" width="18" height="18" />
          <span>预览</span>
        </div>
      </div>
    </div>
    <div class="contentbox">
      <!-- 图层面板 -->
      <div v-if="isShowLayer" class="resource-config">
        <div class="config-title">资源配置</div>

        <div class="btn-group df-c">
          <div
            v-for="(item, index) in ['组件', '图层']"
            :key="index"
            class="btn"
            :class="{ 'btn-active': resourceType === index + 1 }"
            @click="resourceType = index + 1"
          >
            {{ item }}
          </div>
        </div>

        <div class="search-box">
          <el-input
            v-if="resourceType === 1"
            v-model="comKeyword"
            placeholder="搜索组件"
            class="search"
            clearable
          >
            <template #prefix>
              <icon-search />
            </template>
          </el-input>

          <el-input v-else v-model="layerKeyword" placeholder="搜索图层" class="search" clearable>
            <template #prefix>
              <icon-search />
            </template>
          </el-input>
        </div>
        <!-- 组件面板 -->
        <div v-if="resourceType === 1" class="comp-panel">
          <div class="comp-wrap df">
            <div
              v-for="(item, index) in componentsType"
              :key="index"
              class="comp-type-item"
              :class="[item.subComponentsNum > 1 ? 'comp-type-item-muti' : '']"
            >
              <el-popover
                trigger="hover"
                placement="right"
                :ref="'subPopover' + index"
                @hide="subComponentIndex = 0"
                popper-class="sub-comp-popover"
              >
                <div class="sub-comp">
                  <div class="df-c-b sub-comp-header">
                    <div class="sub-comp-title">{{ item.text }}</div>
                    <el-button
                      type="primary"
                      size="small"
                      @click="useSubComponent(item.subComponents[subComponentIndex], index)"
                      >使用组件</el-button
                    >
                  </div>
                  <div class="sub-comp-warp df">
                    <div class="sub-comp-list" v-if="item.subComponentsNum > 1">
                      <div
                        v-for="(subItem, subIndex) in item.subComponents"
                        :key="subIndex"
                        class="sub-type-item overflow-text"
                        :class="{
                          'sub-type-item-active': subComponentIndex === subIndex,
                        }"
                        @click="clickSubComponent(subIndex)"
                        :title="subItem.text"
                      >
                        {{ subItem.text }}
                      </div>
                    </div>

                    <div class="sub-comp-img df-c-c">
                      <!-- 使用 subComponentsSrc 函数获取图片地址 -->
                      <!-- <vuedraggable class="wrapper" :sort="false" :disabled="false"> -->
                      <img
                        v-if="subComponentsSrc(item.subComponents).includes('http')"
                        :src="subComponentsSrc(item.subComponents)"
                        style="width: 416px; height: 195px"
                        @dragend="useSubComponent(item.subComponents[subComponentIndex], index)"
                      />
                      <el-empty v-else :image-size="100" description="暂无预览图"></el-empty>
                      <!-- </vuedraggable> -->
                    </div>
                  </div>
                </div>

                <template #reference>
                  <div style="width: 100%">
                    <img class="icon" :src="getIcon(`${item.type}.png`)" />
                    <div class="text">
                      {{ item.text }}
                    </div>
                  </div>
                </template>
              </el-popover>
            </div>
          </div>
        </div>
        <!-- 图层面板 -->
        <div v-else class="layer-panel">
          <div class="layer-main">
            <!-- 这块下方以及组件有直接操作对象的逻辑，所以不使用computed -->
            <div
              v-for="(item, index) in components.filter((item) =>
                screenConstants.compType[item.type] && layerKeyword
                  ? (item.text || screenConstants.compType[item.type].text).includes(
                      layerKeyword
                    ) &&
                    !item.isDelete &&
                    screenConstants.compType[item.type]
                  : !item.isDelete && screenConstants.compType[item.type]
              )"
              :key="index"
              class="df-c"
              :class="item.active ? 'layer-item layer-item-active' : 'layer-item'"
              @contextmenu.prevent="onContextmenu($event, item)"
              @click="selectComponent(item)"
            >
              <div class="img-box df-c-c">
                <img
                  class="icon"
                  :src="getIcon(`${screenConstants.compType[item.type].icon}.png`)"
                />
              </div>
              <div class="name overflow-text">
                {{ item.text || screenConstants.compType[item.type].text }}
              </div>

              <div class="action df-c" @click.stop>
                <div
                  class="view"
                  style="margin-right: 4px"
                  @click="lockComponent(item)"
                  :title="!item.locked ? '锁定' : '解锁'"
                >
                  <component
                    :is="item.locked ? 'icon-lock' : 'icon-unlock'"
                    style="color: rgba(153, 153, 153, 1)"
                  ></component>
                </div>
                <span
                  :class="item.isShow == false ? 'hide-icon' : 'show-icon'"
                  @click="showComponent(item)"
                  :title="item.isShow == undefined || item.isShow ? '隐藏' : '显示'"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div ref="dragContainer" class="mainbox">
        <div class="mainwrap">
          <vue3-ruler-tool
            ref="rulerTool"
            :value="[]"
            :content-layout="{ left: 0, top: 0 }"
            :is-scale-revise="true"
            :is-hot-key="false"
            :step-length="50"
            :parent="true"
            :style="{
              zoom: scaleSelected / 100,
              height: '100%',
              overflow: 'hidden',
              height: screenProperties.height + 'px',
              width: screenProperties.width + 'px',
            }"
          >
            <div
              id="draggableDiv"
              :style="{
                height: '100%',
                width: '100%',
                background: screenProperties.background,
                backgroundImage: 'url(' + screenProperties.imgUrl + ')',
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                backgroundPosition: 'center',
                overflow: 'hidden',
              }"
              @click="clickPanel"
            >
              <draggables
                ref="draggable"
                :components="components"
                v-model:activated="activated"
                v-model:is-bubbling-event="isBubblingEvent"
                :charts-components="chartsComponents"
                :isDesign="true"
                :sendRequest="false"
                :isCtrl="isCtrl"
              />
            </div>
          </vue3-ruler-tool>
        </div>
        <div class="scaleWrap">
          <el-select
            v-model="scaleSelected"
            size="small"
            :popper-append-to-body="false"
            class="select"
            style="width: 80px"
          >
            <el-option v-for="item in scaleList" :key="item" :value="item" :label="item + '%'" />
          </el-select>
        </div>

        <div class="left-action action-icon df-c-b" @click="clickShow('isShowLayer')">
          <img v-if="isShowLayer" src="@/assets/img/sheet/left.png" width="8px" height="8px" />
          <img v-else src="@/assets/img/sheet/right.png" width="8px" height="8px" />
        </div>
        <div class="right-action action-icon df-c-b" @click="clickShow('isShowConf')">
          <img v-if="!isShowConf" src="@/assets/img/sheet/left.png" width="8px" height="8px" />
          <img v-else src="@/assets/img/sheet/right.png" width="8px" height="8px" />
        </div>
      </div>
      <div v-if="isShowConf" class="config-panel">
        <div class="config-title">配置信息</div>

        <div class="config-box">
          <settings
            ref="tabForm"
            :component="activated"
            :charts-components="chartsComponents"
            :timer-map="timerMap"
            :components="components"
          />
        </div>
      </div>
    </div>

    <!-- 重命名 -->
    <el-dialog
      :close-on-click-modal="false"
      title="重命名"
      :modelValue="renameVisible"
      width="417px"
      @close="closeRenameVisible()"
    >
      <el-input v-model="renameForm.comName" size="medium" placeholder="请输入" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeRenameVisible()">取 消</el-button>
          <el-button type="primary" @click="sureRename()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script src="./screenDesign.js"></script>

<style lang="scss">
  @import '@/element-variables.scss';

  .vue-ruler-h,
  .vue-ruler-v {
    transform-origin: 0 0;
  }

  .layer-menu {
    padding: 0 !important;
    border-radius: 6px;
    background: #fff;
    box-shadow: 0px 0px 10px 0px rgba(38, 38, 38, 0.16);
    text-align: center;
    .contextmenu_menu_item {
      border: 0 !important;
      &:hover {
        background: rgba(23, 183, 148, 0.05);
      }
      &:first-child {
        border-radius: 6px 6px 0 0;
      }
      &:last-child {
        border-radius: 0 0 6px 6px;
        .menu_item_label {
          color: #ff4d4f !important;
        }
      }
      .menu_item_label {
        font-size: 14px;
        color: #000;
      }
      .menu_item_expand_icon {
        display: none;
      }
    }
  }
  .sub-comp-popover {
    width: fit-content !important;
  }
  .sub-comp {
    width: fit-content;
    .sub-comp-header {
      padding: 0 4px 12px 4px;
      .sub-comp-title {
        height: 30px;
        line-height: 30px;
        font-size: 16px;
        color: #000;
        font-weight: bold;
      }
    }
    .sub-comp-warp {
      padding: 0 12px 12px 12px;
      .sub-comp-list {
        width: 68px;
        margin-right: 12px;
        max-height: 246px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }
        &::-webkit-scrollbar-track {
          background-color: transparent;
        }

        &::-webkit-scrollbar-thumb {
          background-color: #d9d9d9;
          border-radius: 5px;
        }

        .sub-type-item {
          cursor: pointer;
          margin-bottom: 8px;
          color: #999;
          text-align: left;
          font-family: 'PingFang SC';
          font-size: 14px;
          font-style: normal;
          font-weight: 400;
          line-height: 22px; /* 157.143% */
        }
        .sub-type-item-active {
          color: #17b794;
          font-weight: bold;
        }
      }

      .sub-comp-img {
        width: 452px;
        height: 246px;
        border-radius: 2px;
        background: #f3f3f3;
      }
    }
  }

  .config-box {
    .demo-form-inline.el-form--label-top .el-form-item__label {
      width: 100%;
      padding-bottom: 0 !important;
      line-height: 32px !important;
      margin-bottom: 0 !important;
      font-size: 14px;
    }
    .public-input-color-picker {
      width: 100%;
    }
    .demo-form-inline .el-select,
    .demo-form-inline .el-textarea,
    .demo-form-inline .el-input {
      width: 100% !important;
    }

    .p-16 {
      padding: 0 16px !important;
    }

    .config-btn {
      cursor: pointer;
      width: 66px;
      height: 24px;
      line-height: 24px;
      font-size: 12px;
      color: #17b794;
      font-weight: 400;
      text-align: center;
      border-radius: 4px;
      border: 1px solid #17b794;
      background: #fff;
    }

    .el-collapse-item__arrow {
      margin-right: 0 !important;
    }

    .el-collapse-item {
      .el-form-item__label {
        width: 100%;
      }

      .el-icon-arrow-right {
        position: absolute;
        right: 10px;
        top: 10px;
        cursor: pointer;
      }
      .el-collapse-item__header {
        position: relative;
        padding: 0 12px;
        height: 36px;
        line-height: 36px;
        background-color: #f9fafa;
        color: #666;
        font-size: 14px;
        font-weight: bold;
      }
      .el-collapse-item__wrap {
        padding: 16px;
        background-color: #fff;
      }
      .el-collapse-item__content {
        padding-bottom: 0;
      }
    }

    .sub-collapse {
      margin-top: 6px;
      border-bottom: 0;
      .el-collapse-item {
        border-left: 1px solid rgba(0, 0, 0, 0.05);
        border-right: 1px solid rgba(0, 0, 0, 0.05);
        margin-bottom: 8px;
      }
      .el-collapse-item__header {
        padding-left: 16px !important;
      }
      .el-collapse-item__wrap {
        background-color: #fafafa !important;
        padding: 5px 12px 10px 12px !important;
        border-bottom: 0;
        .el-collapse-item__content {
          background-color: #fff;
          padding: 5px 7px;
          border-radius: 3px;
        }
      }
    }

    .right-dataset-title {
      box-sizing: border-box;
      display: flex;
      flex-direction: row;
      align-items: center;
      gap: 62px;
      height: 32px;
      background: #ffffff;
      /* border-bottom: 1px solid rgba(0, 0, 0, 0.1); */
      flex: none;
      order: 1;
      flex-grow: 0;
      margin-top: 2px;
    }

    .right-dataset-warp {
      border-radius: 4px;
      border: 0.5px solid rgba(23, 183, 148, 0.1);
      background: rgba(23, 183, 148, 0.05);
      padding: 10px;
    }

    .attr-dataset-title {
      width: fit-content;
      height: 20px;
      font-style: normal;
      font-weight: 500;
      font-size: 14px;
      line-height: 20px;
      color: #292e33;
      flex: none;
      order: 0;
      flex-grow: 0;
      font-weight: bold;
    }

    .attr-dataset-title-small {
      width: fit-content;
      height: 20px;
      font-style: normal;
      font-size: 12px;
      line-height: 20px;
      color: #666;
      flex: none;
      order: 0;
      flex-grow: 0;
      font-weight: bold;
    }

    .color-el-form-item {
      position: relative;
      width: 20%;
      margin-bottom: 6px !important;
      .el-form-item__content {
        text-align: center;
        width: fit-content;
        margin: 0 auto;
      }
    }

    .addBtn {
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      padding: 2px 8px;
      height: 22px;
      background: #17b794;
      border-radius: 3px;
      flex: none;
      order: 1;
      flex-grow: 0;
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 18px;
      color: #ffffff;
    }

    .df-form-item {
      display: flex;
      align-items: center;
      .el-form-item__label {
        min-width: 60px;
        width: fit-content !important;
        margin-right: 12px !important;
      }
    }

    .right-block-el-icon-edit {
      position: absolute;
      right: 52px;
      cursor: pointer;
      width: 14px;
      height: 14px;
      top: 9px;
      background-image: url('@/assets/img/sheet/edit.png');
      background-size: 14px 14px;
      background-repeat: no-repeat;
    }

    .right-el-icon-delete {
      position: absolute;
      right: 30px;
      cursor: pointer;
      width: 14px;
      height: 14px;
      top: 9px;
      background-image: url('@/assets/img/sheet/del.png');
      background-size: 14px 14px;
      background-repeat: no-repeat;
    }
  }

  .contentbox {
    .btn-group {
      padding: 12px 0 10px;
      .btn {
        flex: 1;
        margin-right: 12px;
        border-radius: 2px;
        background: #ededed;
        line-height: 34px;
        text-align: center;
        color: #252525;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: 400;
        line-height: normal;
        transform: all 0.3s ease-in-out;
        cursor: pointer;
        &:last-child {
          margin-right: 0;
        }
      }
      .btn:hover,
      .btn-active {
        background: $--color-primary;
        color: #fff;
      }
    }
  }
</style>

<style scoped lang="scss">
  // @import "./index.scss";
  @import '@/element-variables.scss';

  .pagebox {
    height: 100vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    background: #f0f2f5;
  }

  .svg-box {
    cursor: pointer;
    width: 80px;
    height: 34px;
    background: transparent;
    border: 1px solid #d9d9d9;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 6px;
    transition: all 0.3s ease-in-out;
    span {
      margin-left: 6px;
      color: #3c3c3c;
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
    }
  }
  .svg {
    width: 14px;
    height: 14px;
    font-size: 14px !important;
    color: rgba(125, 125, 125, 1);
  }
  .svg-box:hover,
  .svg-box:active,
  .svg-box:focus {
    border: 1px solid $--color-primary;
    color: #000;
    box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.05);
  }

  .pagebox .header {
    display: flex;
    justify-content: space-between;
    height: 64px;
    padding: 0 16px;
    align-items: center;
    border-bottom: 1px solid #e8e8e8;
    background: #fff;
    box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.05);
    position: relative;
    z-index: 10;

    .left,
    .right,
    .middle {
      display: flex;
      justify-content: flex-start;
    }

    .middle {
      color: rgba(0, 0, 0, 0.8);
      text-align: center;
      font-size: 20px;
      font-style: normal;
      font-weight: bold;
      line-height: 26px; /* 127.065% */
      letter-spacing: 0.2px;
    }
  }
  .search-box {
    padding: 10px 0;
    .search {
      margin-bottom: 10px;
      :deep(.el-input__wrapper) {
        height: 36px;
        line-height: 36px;
        border-radius: 6px;
        padding: 0px 11px;
        border-color: rgba(0, 0, 0, 0.1);
      }

      :deep(.el-input__icon) {
        line-height: 36px;
      }
    }
  }

  .contentbox {
    flex: 1;
    display: flex;
    overflow: hidden;
    background: #f0f2f5;

    .resource-config {
      width: 274px;
      padding: 0 16px;
      background-color: #fff;
      flex-shrink: 0;
      .config-title {
        height: 48px;
        text-align: center;
        line-height: 48px;
        color: #999;
        font-size: 14px;
        font-weight: 400;
      }

      .layer-panel {
        position: relative;
        .layer-main {
          overflow-y: auto;
          overflow-x: hidden;
          border-radius: 4px;
          height: calc(100vh - 240px);

          .layer-item {
            padding: 12px 10px;
            border-radius: 4px;
            background: #f1f2f3;
            border: 1px solid #f1f2f3;
            margin-bottom: 6px;
            cursor: pointer;
            transition: all 0.3s;

            .img-box {
              width: 36px;
              height: 36px;
              border: 1px solid #fff;
              background-color: #fff;
              margin-right: 8px;
              border-radius: 4px;
              .icon {
                width: 28px;
                height: 28px;
              }
            }

            .name {
              width: 154px;
              color: #595959;
              font-family: 'PingFang SC';
              font-size: 12px;
              line-height: 12px;
              font-style: normal;
              font-weight: 400;
            }

            .action {
              flex: 1;
              justify-content: flex-end;

              .hide-icon {
                width: 18px;
                height: 18px;
                background-size: 100% 100%;
                background-image: url('@/assets/img/screenDesign/hide.png');
              }

              .show-icon {
                width: 18px;
                height: 18px;
                background-size: 100% 100%;
                background-image: url('@/assets/img/screenDesign/show.png');
              }
            }
          }

          .layer-item:hover,
          .layer-item-active {
            background: #fff;
            border: 1px solid $--color-primary;
            .img-box {
              border: 1px solid #f1f2f3;
            }
          }
        }
        .layer-main::-webkit-scrollbar {
          width: 0;
        }
      }

      .comp-panel {
        .comp-wrap {
          width: 100%;
          flex-wrap: wrap;
          height: calc(100vh - 240px);
          overflow-y: auto;
          overflow-x: hidden;
          border-radius: 4px;

          .comp-type-item {
            width: 130px;
            height: 80px;
            border-radius: 4px;
            border: 0.5px solid rgba(23, 183, 148, 0.1);
            background: rgba(23, 183, 148, 0.05);
            margin-right: 10px;
            margin-bottom: 12px;
            transition: all 0.3s;
            cursor: pointer;
            padding-top: 8px;
            &:hover {
              border: 1px solid #17b794;
              background-color: #fff;
            }
            &:nth-child(2n) {
              margin-right: 0;
            }

            .icon {
              width: 46px;
              height: 46px;
              display: block;
              margin: 0 auto;
              margin-bottom: 8px;
            }

            .text {
              color: #17b794;
              font-family: 'PingFang SC';
              font-size: 14px;
              font-style: normal;
              font-weight: 400;
              line-height: normal;
              text-align: center;
            }
          }

          .comp-type-item-muti {
            position: relative;
            &::before {
              position: absolute;
              right: 1px;
              bottom: 1px;
              content: '';
              width: 18px;
              height: 18px;
              background-image: url('@/assets/img/screenDesign/marker.png');
              background-size: 100% 100%;
            }
          }
        }
      }
    }

    .config-panel {
      background: #fff;
      position: relative;
      width: 306px;
      height: 100%;
      display: flex;
      flex-direction: column;
      padding: 0 16px;
      box-sizing: border-box;
      .config-title {
        height: 48px;
        text-align: center;
        line-height: 48px;
        color: #999;
        font-size: 14px;
        font-weight: 400;
      }
      .config-box {
        flex: 1;
        padding: 0px 0 12px 0;
        overflow: auto;
      }

      /*定义滚动条的宽度*/
      .config-box::-webkit-scrollbar {
        width: 0;
      }
    }
  }
  .comp-wrap::-webkit-scrollbar {
    width: 0; /* 设置滚动条的宽度为0 */
    background-color: transparent; /* 设置滚动条背景为透明 */
  }
  .contentbox .mainbox {
    flex: 1;
    padding: 0;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    position: relative;
    padding: 0 10px;

    .action-icon {
      cursor: pointer;
      transition: all 0.3s;
      position: absolute;
      top: 50%;

      transform: translateY(-50%);
      z-index: 999;
      background-color: $--color-primary;
      width: 10px;
      height: 48px;
      margin-right: 0;
      &:hover {
        opacity: 0.7;
      }
    }

    .left-action {
      left: 0;
      border-radius: 0 3px 3px 0;
    }
    .right-action {
      border-radius: 3px 0 0 3px;
      right: 0px;
    }
  }

  .contentbox .mainbox .mainwrap {
    height: calc(100% - 30px);
    /* background: #181B24; */
    overflow-y: auto;
    overflow-x: auto;
  }

  .contentbox .mainbox .scaleWrap {
    height: 30px;
    background: #fff;
    text-align: right;

    .select {
      height: 20px;
      width: 74px;
      margin-right: 12px;
      margin-bottom: 6px;
    }
  }

  .contentbox .rightbox {
    flex: none;
    /*    padding-left: 30px; */
    background: #22242b;
    position: relative;
    width: 334px;
  }

  .contentbox .rightbox.collapse {
    width: 30px;
  }

  .clarrow {
    position: absolute;
    top: 50%;
    left: 3px;
    font-size: 20px;
    cursor: pointer;
  }

  .el-tabs ::v-deep .el-tabs__content {
    padding-bottom: 15px;
    height: 95%;
    overflow-y: auto;
    overflow-x: hidden;
  }

  .el-menu-demo .el-menu-item,
  .el-menu-demo .el-submenu ::v-deep .el-submenu__title {
    font-size: 16px;
  }

  .el-menu-demo .el-menu-item i,
  .el-menu-demo .el-submenu ::v-deep .el-submenu__title i {
    color: #b3b4b6;
  }

  .el-menu-demo .el-menu-item.is-active,
  .el-menu-demo .el-submenu.is-active ::v-deep .el-submenu__title {
    background-color: #2f3544 !important;
    border-bottom-color: #2f3544 !important;
  }

  .el-menu-demo .el-menu-item.is-active i,
  .el-menu-demo .el-submenu.is-active ::v-deep .el-submenu__title i {
    color: white;
  }

  .el-menu-demo .el-submenu ::v-deep .el-submenu__title .el-submenu__icon-arrow {
    margin-top: 0px;
  }

  .mytaba ::v-deep .el-tabs__header .el-tabs__item.is-active {
    color: white;
  }

  .mytaba ::v-deep .el-tabs__nav-wrap::after {
    background-color: #3a3f48;
  }

  .mytaba ::v-deep .el-tabs__header .el-tabs__active-bar {
    background-color: white;
  }

  .mytaba ::v-deep .el-tabs__header {
    background-color: #2d2f38;
    padding: 0 10px;
    margin-bottom: 0;
  }

  .mytaba ::v-deep .el-tabs__item {
    font-size: 14px;
  }

  .mycarda.el-card ::v-deep .el-card__body {
    padding: 8px;
  }

  .mycarda.el-card {
    border: 0;
    border-radius: 0;
    background-color: unset;
    color: white;
  }

  .mycarda.el-card label {
    color: white;
    font-size: 14px;
  }

  .mainwrap::-webkit-scrollbar {
    width: 4px;
    height: 4px;
  }
  .mainwrap::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(117, 114, 114, 0.7);
  }

  .mainwrap::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 0;
    background: rgba(0, 0, 0, 0.1);
  }

  .titlea {
    background: #2d2f38;
    display: flex;
    justify-content: space-between;
    color: white;
    font-size: 14px;
    padding: 10px;
  }
  .infobox ::v-deep .el-form-item__label {
    color: #c5c5c5;
  }
  .infobox ::v-deep .el-collapse {
    border-top: 1px solid #393b4a;
    border-bottom: 1px solid #393b4a;
  }
  .infobox ::v-deep .el-collapse .el-collapse-item__header {
    background-color: #2d2f38;
    color: #fff;
    padding-left: 10px;
    border-bottom: 1px solid #2d2f38;
  }
  .infobox ::v-deep .el-collapse-item__wrap {
    background-color: #22242b;
    padding: 20px 16px;
  }
  .infobox ::v-deep .el-collapse-item__content {
    padding-bottom: 0;
  }

  ::v-deep .vue-ruler-content {
    width: 100% !important;
    height: 100% !important;
  }

  /*  .infobox::v-deep.el-form.demo-form-inline.el-form--label-left{
    padding:20px 16px;
  } */
</style>
