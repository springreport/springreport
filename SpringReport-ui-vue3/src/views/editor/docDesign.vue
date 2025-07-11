<template>
  <div
    class="contentbox"
    v-loading="loading"
    :element-loading-text="loadingText"
    style="
      height: 100vh;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      background-color: #f0f2f5;
    "
  >
    <div style="width: 100%; flex: none" class="header-box">
      <el-header class="_header df-c-b">
        <div class="headerLeft df-c" style="width: 30%">
          <div
            class="tplname"
            style="width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap"
            :title="tplName"
          >
            {{ tplName }}
          </div>
        </div>
      </el-header>
    </div>
    <div :style="{ flex: 1, display: 'flex', height: designHeight + 'px' }">
      <div v-show="leftOpen" class="left">
        <div class="left-dataset-title">
          <span class="dataset-title">数据集管理</span>
          <el-button
            plain
            type="primary"
            class="add-dataset"
            @click="addDataSets"
            v-has="'reportDesign_addDataSet'"
            ><icon-plus
              theme="outline"
              size="12"
              fill="#17b794"
              class="el-icon--left"
            />添加数据集</el-button>
        </div>
        <div class="left-dataset-content df">
          <div class="dataset-group">
            <div class="section-name df-c">
              <span style="margin-right: 8px">报表</span>
              <div class="set-group df-c" @click="groupSetVisible = true">
                <img src="@/assets/img/sheet/setting.png" width="12px" height="12px" />
                <div class="setting-text">分组设置</div>
              </div>
            </div>
            <el-input
              v-model="datasetKeyword"
              prefix-icon="el-icon-search"
              placeholder="报表搜索"
              class="search"
              clearable
            />
            <div
              v-loading="dataGroupLoading"
              class="group-list cus-scrollbar"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
            >
              <el-collapse v-if="displayGroupList && displayGroupList.length">
                <el-collapse-item
                  v-for="groupItem in displayGroupList"
                  :key="groupItem.id"
                  :title="groupItem.groupName"
                  :name="groupItem.id"
                >
                  <template v-if="groupItem.data.length">
                    <div
                      v-for="datasetItem in groupItem.data"
                      :key="datasetItem.id"
                      class="dataset-item df-c-b"
                      :class="
                        datasetItemActive == datasetItem.id
                          ? 'dataset-item-active'
                          : ''
                      "
                      @click="clickDatasets(datasetItem)"
                    >
                      <div
                        class="set-name overflow-text"
                        style="flex: 1"
                        :title="datasetItem.datasetName"
                      >
                        {{ datasetItem.datasetName }}
                      </div>
                      <div class="action-box df-c">
                        <el-dropdown>
                          <div class="action action-copy" />
                          <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item
                              v-on:click="
                                copyAttr(5, datasetItem.datasetName)
                              "
                            >列表</el-dropdown-item>
                            <el-dropdown-item
                              v-on:click="
                                copyAttr(6, datasetItem.datasetName)
                              "
                            >区块对</el-dropdown-item>
                          </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                        <el-dropdown>
                          <div class="action action-add" />
                          <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item
                              v-on:click="
                                copyAttr(5, datasetItem.datasetName, null, true)
                              "
                            >列表</el-dropdown-item>
                            <el-dropdown-item
                              v-on:click="
                                copyAttr(6, datasetItem.datasetName, null, true)
                              "
                            >区块对</el-dropdown-item>
                          </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                        <div
                          class="action action-edit"
                          @click.stop="editDataSet(datasetItem)"
                        />
                        <div
                          class="action action-del"
                          @click.stop="deleteDataSet(datasetItem)"
                        />
                      </div>
                    </div>
                  </template>
                  <el-empty v-else :image-size="60" description="暂无数据集" />
                </el-collapse-item>
              </el-collapse>
              <el-empty v-else :image-size="60" description="暂无分组" />
            </div>
          </div>
          <div class="dataset-fields">
            <div class="section-name">字段</div>
            <el-input
              v-model="filedKeyword"
              prefix-icon="el-icon-search"
              placeholder="检索所选报表内字段"
              class="search"
              clearable
            />
            <div
              v-loading="filedLoading"
              class="group-list cus-scrollbar"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
            >
              <template v-if="displayFields.length">
                <div
                  v-for="fieldItem in displayFields"
                  :key="fieldItem.columnName"
                  class="dataset-item df-c-b"
                >
                  <div
                    class="set-name overflow-text"
                    style="flex: 1"
                    :title="fieldItem.name"
                  >
                    {{ fieldItem.name }}<span v-if="datasetItem.datasetType == '2'">({{fieldItem.columnName}})</span>
                      <span v-else>({{fieldItem.remark}})</span>
                  </div>
                  <div class="action-box df-c">
                    <el-dropdown>
                      <div class="action action-edit" />
                      <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(1, datasetItem.datasetName, fieldItem.name)
                          "
                        >文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(2, datasetItem.datasetName, fieldItem.name)
                          "
                        >图片</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(3, datasetItem.datasetName, fieldItem.name)
                          "
                        >列表文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(4, datasetItem.datasetName, fieldItem.name)
                          "
                        >列表图片</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(7, datasetItem.datasetName, fieldItem.name)
                          "
                        >区块对文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(8, datasetItem.datasetName, fieldItem.name)
                          "
                        >区块对图片</el-dropdown-item>
                      </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                    <el-dropdown>
                      <div class="action action-add" />
                      <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              1,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              2,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >图片</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              3,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >列表文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              4,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >列表图片</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              7,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >区块对文本</el-dropdown-item>
                        <el-dropdown-item
                          v-on:click="
                            copyAttr(
                              8,
                              datasetItem.datasetName,
                              fieldItem.name,
                              true
                            )
                          "
                        >区块对图片</el-dropdown-item>
                      </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                  </div>
                </div>
                <el-input
                  v-show="datasetItem.apiResult"
                  v-model="datasetItem.apiResult"
                  type="textarea"
                  placeholder=""
                  :rows=6
                />
              </template>
              <el-empty v-else :image-size="60" description="暂无字段" />
            </div>
          </div>
        </div>
      </div>
      <div class="center" :style="{
          width: leftOpen ? 'calc(100vw - 460px - 10px)' : 'calc(100vw - 10px)',
        }">
        <div class="menu" editor-component="menu">
          <div class="menu-item">
            <div class="menu-item__save" title="保存模板">
              <i />
            </div>
            <div class="menu-item__upload" title="上传">
              <i />
            </div>
            <div style="display: none">
              <input
                id="uploadDocxBtn"
                type="file"
                accept=".docx"
                @change="uploadDocx"
              >
            </div>
            <div class="menu-item__download" title="导出模板">
              <i />
            </div>
            <div class="menu-item__preview" title="预览">
              <i />
            </div>
            <div class="menu-item__undo">
              <i />
            </div>
            <div class="menu-item__redo">
              <i />
            </div>
            <div class="menu-item__painter" title="格式刷(双击可连续使用)">
              <i />
            </div>
            <div class="menu-item__format" title="清除格式">
              <i />
            </div>
          </div>
          <div class="menu-divider" />
          <div class="menu-item">
            <div class="menu-item__font">
              <span class="select" title="字体">微软雅黑</span>
              <div class="options">
                <ul>
                  <li
                    data-family="Microsoft YaHei"
                    style="font-family: 'Microsoft YaHei'"
                  >
                    微软雅黑
                  </li>
                  <li data-family="宋体" style="font-family: '宋体'">宋体</li>
                  <li data-family="黑体" style="font-family: '黑体'">黑体</li>
                  <li data-family="仿宋" style="font-family: '仿宋'">仿宋</li>
                  <li data-family="楷体" style="font-family: '楷体'">楷体</li>
                  <li data-family="等线" style="font-family: '等线'">等线</li>
                  <li data-family="华文琥珀" style="font-family: '华文琥珀'">
                    华文琥珀
                  </li>
                  <li data-family="华文楷体" style="font-family: '华文楷体'">
                    华文楷体
                  </li>
                  <li data-family="华文隶书" style="font-family: '华文隶书'">
                    华文隶书
                  </li>
                  <li data-family="华文新魏" style="font-family: '华文新魏'">
                    华文新魏
                  </li>
                  <li data-family="华文行楷" style="font-family: '华文行楷'">
                    华文行楷
                  </li>
                  <li data-family="华文中宋" style="font-family: '华文中宋'">
                    华文中宋
                  </li>
                  <li data-family="华文彩云" style="font-family: '华文彩云'">
                    华文彩云
                  </li>
                  <li data-family="Arial" style="font-family: 'Arial'">
                    Arial
                  </li>
                  <li data-family="Segoe UI" style="font-family: 'Segoe UI'">
                    Segoe UI
                  </li>
                  <li data-family="Ink Free" style="font-family: 'Ink Free'">
                    Ink Free
                  </li>
                  <li data-family="Fantasy" style="font-family: 'Fantasy'">
                    Fantasy
                  </li>
                </ul>
              </div>
            </div>
            <div class="menu-item__size">
              <span class="select" title="字体">小四</span>
              <div class="options">
                <ul>
                  <li data-size="56">初号</li>
                  <li data-size="48">小初</li>
                  <li data-size="34">一号</li>
                  <li data-size="32">小一</li>
                  <li data-size="29">二号</li>
                  <li data-size="24">小二</li>
                  <li data-size="21">三号</li>
                  <li data-size="20">小三</li>
                  <li data-size="18">四号</li>
                  <li data-size="16">小四</li>
                  <li data-size="14">五号</li>
                  <li data-size="12">小五</li>
                  <li data-size="10">六号</li>
                  <li data-size="8">小六</li>
                  <li data-size="7">七号</li>
                  <li data-size="6">八号</li>
                </ul>
              </div>
            </div>
            <div class="menu-item__size-add">
              <i />
            </div>
            <div class="menu-item__size-minus">
              <i />
            </div>
            <div class="menu-item__bold">
              <i />
            </div>
            <div class="menu-item__italic">
              <i />
            </div>
            <div class="menu-item__underline">
              <i />
              <span class="select" />
              <div class="options">
                <ul>
                  <li data-decoration-style="solid">
                    <i />
                  </li>
                  <li data-decoration-style="double">
                    <i />
                  </li>
                  <li data-decoration-style="dashed">
                    <i />
                  </li>
                  <li data-decoration-style="dotted">
                    <i />
                  </li>
                  <li data-decoration-style="wavy">
                    <i />
                  </li>
                </ul>
              </div>
            </div>
            <div class="menu-item__strikeout" title="删除线(Ctrl+Shift+X)">
              <i />
            </div>
            <div class="menu-item__superscript">
              <i />
            </div>
            <div class="menu-item__subscript">
              <i />
            </div>
            <div class="menu-item__color" title="字体颜色">
              <i />
              <span />
              <input id="color" type="color">
            </div>
            <div class="menu-item__highlight" title="高亮">
              <i />
              <span />
              <input id="highlight" type="color">
            </div>
            <div class="menu-item__cellcolor" title="单元格颜色">
              <i />
              <!-- <span></span> -->
              <input id="cellcolor" type="color">
            </div>
          </div>
          <div class="menu-divider" />
          <div class="menu-item">
            <div class="menu-item__title">
              <i />
              <span class="select" title="切换标题">正文</span>
              <div class="options">
                <ul>
                  <li style="font-size: 16px">正文</li>
                  <li data-level="first" style="font-size: 26px">标题1</li>
                  <li data-level="second" style="font-size: 24px">标题2</li>
                  <li data-level="third" style="font-size: 22px">标题3</li>
                  <li data-level="fourth" style="font-size: 20px">标题4</li>
                  <li data-level="fifth" style="font-size: 18px">标题5</li>
                  <li data-level="sixth" style="font-size: 16px">标题6</li>
                </ul>
              </div>
            </div>
            <div class="menu-item__left">
              <i />
            </div>
            <div class="menu-item__center">
              <i />
            </div>
            <div class="menu-item__right">
              <i />
            </div>
            <div class="menu-item__alignment">
              <i />
            </div>
            <div class="menu-item__row-margin">
              <i title="行间距" />
              <div class="options">
                <ul>
                  <li data-rowmargin="1">1</li>
                  <li data-rowmargin="1.25">1.25</li>
                  <li data-rowmargin="1.5">1.5</li>
                  <li data-rowmargin="1.75">1.75</li>
                  <li data-rowmargin="2">2</li>
                  <li data-rowmargin="2.5">2.5</li>
                  <li data-rowmargin="3">3</li>
                </ul>
              </div>
            </div>
            <div class="menu-item__list">
              <i />
              <div class="options">
                <ul>
                  <li>
                    <label>取消列表</label>
                  </li>
                  <li data-list-type="ol" data-list-style="decimal">
                    <label>有序列表：</label>
                    <ol>
                      <li>________</li>
                    </ol>
                  </li>
                  <!-- <li data-list-type="ul" data-list-style='checkbox'>
                    <label>复选框列表：</label>
                    <ul style="list-style-type: '☑️ ';">
                      <li>________</li>
                    </ul>
                  </li> -->
                  <li data-list-type="ul" data-list-style="disc">
                    <label>实心圆点列表：</label>
                    <ul style="list-style-type: disc">
                      <li>________</li>
                    </ul>
                  </li>
                  <li data-list-type="ul" data-list-style="circle">
                    <label>空心圆点列表：</label>
                    <ul style="list-style-type: circle">
                      <li>________</li>
                    </ul>
                  </li>
                  <li data-list-type="ul" data-list-style="square">
                    <label>空心方块列表：</label>
                    <ul style="list-style-type: square">
                      <li>________</li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="menu-divider" />
          <div class="menu-item">
            <div class="menu-item__chart">
              <i title="图表" />
            </div>
            <div class="menu-item__table">
              <i title="表格" />
            </div>
            <div class="menu-item__table__collapse">
              <div class="table-close">×</div>
              <div class="table-title">
                <span class="table-select">插入</span>
                <span>表格</span>
              </div>
              <div class="table-panel" />
            </div>
            <div class="menu-item__image">
              <i title="图片" />
              <input
                id="image"
                type="file"
                accept=".png, .jpg, .jpeg, .svg, .gif"
              >
            </div>
            <div class="menu-item__barcode">
              <i title="条形码" />
            </div>
            <div class="menu-item__qrcode">
              <i title="二维码" />
            </div>
            <div class="menu-item__hyperlink">
              <i title="超链接" />
            </div>
            <div class="menu-item__separator">
              <i title="分割线" />
              <div class="options">
                <ul>
                  <li data-separator="0,0">
                    <i />
                  </li>
                  <li data-separator="1,1">
                    <i />
                  </li>
                  <li data-separator="3,1">
                    <i />
                  </li>
                  <li data-separator="4,4">
                    <i />
                  </li>
                  <li data-separator="7,3,3,3">
                    <i />
                  </li>
                  <li data-separator="6,2,2,2,2,2">
                    <i />
                  </li>
                </ul>
              </div>
            </div>
            <div class="menu-item__watermark">
              <i title="水印(添加、删除)" />
              <div class="options">
                <ul>
                  <li data-menu="add">添加水印</li>
                  <li data-menu="delete">删除水印</li>
                </ul>
              </div>
            </div>
            <div class="menu-item__page-break" title="分页符">
              <i />
            </div>
            <!-- <div class="menu-item__codeblock" title="代码块">
              <i></i>
            </div>
            <div class="menu-item__page-break" title="分页符">
              <i></i>
            </div> -->
            <!-- <div class="menu-item__control">
              <i title="控件"></i>
              <div class="options">
                <ul>
                  <li data-control='text'>文本</li>
                  <li data-control="select">列举</li>
                  <li data-control="checkbox">复选框</li>
                </ul>
              </div>
            </div> -->
            <!-- <div class="menu-item__checkbox" title="复选框">
              <i></i>
            </div> -->
            <!-- <div class="menu-item__latex" title="LateX">
              <i></i>
            </div>
            <div class="menu-item__date">
              <i title="日期"></i>
              <div class="options">
                <ul>
                  <li data-format="yyyy-MM-dd"></li>
                  <li data-format="yyyy-MM-dd hh:mm:ss"></li>
                </ul>
              </div>
            </div>
            <div class="menu-item__block" title="内容块">
              <i></i>
            </div> -->
          </div>
          <div class="menu-divider" />
          <div class="menu-item">
            <div class="menu-item__search" data-menu="search">
              <i />
            </div>
            <div class="menu-item__search__collapse" data-menu="search">
              <div class="menu-item__search__collapse__search">
                <input type="text">
                <label class="search-result" />
                <div class="arrow-left">
                  <i />
                </div>
                <div class="arrow-right">
                  <i />
                </div>
                <span>×</span>
              </div>
              <div class="menu-item__search__collapse__replace">
                <input type="text">
                <button>替换</button>
              </div>
            </div>
            <div class="menu-item__print" data-menu="print">
              <i />
            </div>
          </div>
        </div>
        <div class="catalog" editor-component="catalog">
          <div class="catalog__header">
            <span>目录</span>
            <div class="catalog__header__close">
              <i />
            </div>
          </div>
          <div class="catalog__main" />
        </div>
        <div class="editor-container">
          <div class="editor" />

        </div>
        <!-- <div class="editor" /> -->
        <div class="comment" editor-component="comment" />
        <div class="footer" editor-component="footer">
          <div>
            <div class="catalog-mode" title="目录">
              <i />
            </div>
            <div class="page-mode">
              <i title="页面模式(分页、连页)" />
              <div class="options">
                <ul>
                  <li data-page-mode="paging" class="active">分页</li>
                  <li data-page-mode="continuity">连页</li>
                </ul>
              </div>
            </div>
            <span>可见页码：<span class="page-no-list">1</span></span>
            <span>页面：<span class="page-no">1</span>/<span
              class="page-size"
            >1</span></span>
            <span>字数：<span class="word-count">0</span></span>
          </div>
          <!-- <div class="editor-mode" title="编辑模式(编辑、清洁、只读、表单)">编辑模式</div> -->
          <div>
            <div class="page-scale-minus" title="缩小(Ctrl+-)">
              <i />
            </div>
            <span
              class="page-scale-percentage"
              title="显示比例(点击可复原Ctrl+0)"
            >100%</span>
            <div class="page-scale-add" title="放大(Ctrl+=)">
              <i />
            </div>
            <div class="paper-size">
              <i title="纸张类型" />
              <div class="options">
                <ul>
                  <li data-paper-size="794*1123" class="active">A4</li>
                  <li data-paper-size="1593*2251">A2</li>
                  <li data-paper-size="1125*1593">A3</li>
                  <li data-paper-size="565*796">A5</li>
                  <li data-paper-size="412*488">5号信封</li>
                  <li data-paper-size="450*866">6号信封</li>
                  <li data-paper-size="609*862">7号信封</li>
                  <li data-paper-size="862*1221">9号信封</li>
                  <li data-paper-size="813*1266">法律用纸</li>
                  <li data-paper-size="813*1054">信纸</li>
                </ul>
              </div>
            </div>
            <div class="paper-direction">
              <i title="纸张方向" />
              <div class="options">
                <ul>
                  <li data-paper-direction="vertical" class="active">纵向</li>
                  <li data-paper-direction="horizontal">横向</li>
                </ul>
              </div>
            </div>
            <div class="paper-margin" title="页边距">
              <i />
            </div>
            <div class="fullscreen" title="全屏显示">
              <i />
            </div>
            <!-- <div class="editor-option" title="编辑器设置">
              <i></i>
            </div> -->
          </div>
        </div>
        <div style="display: none">
          <input id="uploadBtn" type="file" accept="xlsx/*" @change="loadExcel" />
        </div>
        <div style="display: none">
          <input id="uploadAttachmentBtn" type="file" accept="*" @change="changeAttachment" />
        </div>
        <div class="left-action action-icon df-c-b" @click="switchOpenLeftPanel()">
          <img v-if="leftOpen" src="@/assets/img/sheet/left.png" width="8px" height="8px" />
          <img v-else src="@/assets/img/sheet/right.png" width="8px" height="8px" />
        </div>
      </div>
    </div>
    <!-- <el-dialog
      title="数据集"
      v-model="addDatasetsDialogVisiable"
      width="82%"
      top="20px"
      :close-on-click-modal="false"
      @close="closeAddDataSet"
      class="add-dataset-dialog"
    > -->
     <el-dialog
      title="数据集"
      v-model="addDatasetsDialogVisiable"
      width="82%"
      top="20px"
      :close-on-click-modal="false"
      @close="closeAddDataSet"
      class="add-dataset-dialog"
    >
      <el-radio-group v-model="addDatasetType" size="large" style="margin-bottom: 16px">
        <el-radio-button :label="1">sql语句</el-radio-button>
        <el-radio-button :label="2">参数配置</el-radio-button>
      </el-radio-group>
      <div v-show="addDatasetType == 1">
        <el-form :inline="true" :model="sqlForm" class="demo-form-inline" ref="sqlRef">
          <el-form-item
            label="数据集名称"
            prop="datasetName"
            :rules="filter_rules('数据集名称', { required: true })"
          >
            <el-input
              v-model="sqlForm.datasetName"
              placeholder="数据集名称"
              style="width: 192px"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="数据源"
            prop="datasourceId"
            :rules="filter_rules('选择数据源', { required: true })"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.datasourceId"
              placeholder="选择数据源"
              @change="changeDatasource(false)"
            >
              <el-option
                v-for="op in dataSource"
                :label="op.dataSourceName"
                :value="op.datasourceId"
                :key="op.datasourceId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="sql类型"
            prop="sqlType"
            :rules="filter_rules('选择sql类型', { required: true })"
            v-if="datasourceType == 1"
            style="width: 270px"
          >
            <el-select v-model="sqlForm.sqlType" placeholder="选择sql类型">
              <el-option
                v-for="op in selectUtil.sqlType"
                :label="op.label"
                :value="op.value"
                :key="op.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="是否公共数据集"
            prop="isCommon"
            :rules="filter_rules('是否公共数据集', { required: true })"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.isCommon"
              placeholder="是否公共数据集"
            >
              <el-option
                v-for="item in selectUtil.yesNo"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="分组"
            prop="groupId"
            :rules="filter_rules('选择分组', { required: true })"
            style="width: 270px"
            v-if="sqlForm.isCommon == 2"
          >
            <el-select v-model="sqlForm.groupId" placeholder="选择分组">
              <el-option
                v-for="item in groupList"
                :key="item.id"
                :label="item.groupName"
                :value="item.id"
                :disabled="item.id == 0"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="行列转置"
            prop="isConvert"
            :rules="filter_rules('行列转置', { required: true })"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.isConvert"
              placeholder="行列转置"
            >
              <el-option
                v-for="item in selectUtil.yesNo"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="固定列"
            prop="fixedColumn"
            :rules="filter_rules('固定列', { required: false })"
            v-if="sqlForm.isConvert == 1"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.fixedColumn"
              placeholder="固定列"
              multiple
              collapse-tags
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="列转行(表头)"
            prop="headerName"
            :rules="filter_rules('列转行(表头)', { required: false })"
            v-if="sqlForm.isConvert == 1"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.headerName"
              placeholder="列转行(表头)"
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="列转行(数值)"
            prop="valueField"
            :rules="filter_rules('列转行(数值)', { required: false })"
            v-if="sqlForm.isConvert == 1"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.valueField"
              placeholder="列转行(数值)"
            >
            <el-option
                v-for="item in sqlColumnTableData.tableData"
                :key="item.name"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="查询集合(表)"
            prop="mongoTable"
            :rules="filter_rules('查询集合(表)', { required: true })"
            v-if="datasourceType == 3"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.mongoTable"
              placeholder="查询集合(表)"
            >
              <el-option
                v-for="item in dataSourceTables"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="查询方式"
            prop="mongoSearchType"
            :rules="filter_rules('查询方式', { required: true })"
            v-if="datasourceType == 3"
            style="width: 270px"
          >
            <el-select
              v-model="sqlForm.mongoSearchType"
              placeholder="查询方式"
            >
              <el-option
                v-for="item in selectUtil.mongoSearchType"
                :key="item.value"
                :label="item.name"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-form>

        <div class="df" style="width: 100%; border: 1px solid #e8e8e8">
          <div v-show="selectVariableOpen && datasourceType != 3" class="variable-content" >
            <div class="variable-title">选择变量</div>
            <div class="variable-warp">
              <div class="variable-warp-title">系统变量</div>
              <div class="variable-list df">
                <div
                  v-for="(item, index) in commonConstants.systemParam"
                  :key="index"
                  :title="item.label"
                  class="variable-item df-c"
                >
                  <div class="overflow-text" style="flex: 1; margin-right: 8px"
                    >{{ item.label }}({{ item.value }})</div
                  >
                  <icon-copy title="复制" @click="doCopy(item)" />
                  <icon-add-one title="添加" style="margin-left: 4px" @click="doCopy(item, true)" />
                </div>
              </div>
              <div class="variable-warp-title">解析表</div>

              <div class="tablecolumn">
                <el-select
                  v-model="datasourceTableName"
                  placeholder="请选择解析表"
                  filterable
                  style="margin-bottom: 10px; width: 254px"
                  @change="getTableColumns"
                >
                  <el-option
                    v-for="op in dataSourceTables"
                    :key="op.value"
                    :label="op.name"
                    :value="op.value"
                  />
                </el-select>
                <div class="variable-list analysis-list df">
                  <template v-if="tableColumns.length">
                    <div
                      v-for="(column, index) in tableColumns"
                      :key="index"
                      class="variable-item df-c"
                      :title="column.name"
                    >
                      <div class="overflow-text" style="flex: 1; margin-right: 8px">{{
                        column.name
                      }}</div>
                      <el-dropdown>
                        <icon-copy title="复制" style="flex: 1; margin-right: 4px" />
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item v-on:click="getWhereByColumn(1, column)"
                              >仅字段</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(2, column)"
                              >表名.字段</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(3, column)"
                              >查询条件(=)</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(4, column)"
                              >查询条件(in)</el-dropdown-item
                            >
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                      <el-dropdown>
                        <icon-add-one title="添加" />
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item v-on:click="getWhereByColumn(1, column, true)"
                              >仅字段</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(2, column, true)"
                              >表名.字段</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(3, column, true)"
                              >查询条件(=)</el-dropdown-item
                            >
                            <el-dropdown-item v-on:click="getWhereByColumn(4, column, true)"
                              >查询条件(in)</el-dropdown-item
                            >
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </template>
                  <el-empty v-else style="margin: 0 auto" :image-size="60" description="暂无字段" />
                </div>
              </div>
            </div>
          </div>
          <div class="sql-content">
            <div v-if="datasourceType == 1 || datasourceType == 3" style="height: 25px">
              <el-tooltip
                content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中"
                placement="bottom"
                ><el-tag type="success" @click="execSql" style="cursor: pointer"
                  ><icon-play />执行</el-tag
                ></el-tooltip
              >
              <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"
                ><el-tag @click="formatSql" style="cursor: pointer" v-if="datasourceType == 1"
                  ><icon-align-left-one />格式化</el-tag
                >
              </el-tooltip>
              <el-tooltip content="该操作会插入注释标签" placement="right"
                ><el-tag @click="addComment(' <!--  -->')" type="warning" style="cursor: pointer" v-if="datasourceType == 1"
                  ><icon-add-one />添加注释</el-tag
                >
              </el-tooltip>
              <el-dropdown v-if="paramTableData.tableData && paramTableData.tableData.length > 0 && datasourceType == 1">
                <el-tag type="danger" style="cursor: pointer"><icon-add-one />添加参数</el-tag>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-for="(row, index) in paramTableData.tableData"
                      :key="index"
                      v-on:click="getWhereByParam(row)"
                      >{{ row.paramCode }}</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div v-if="datasourceType == 1 || datasourceType == 3" style="height: 300px">
              <div style="height: 100%; width: 100%" v-if="datasourceType == 1">
                <codemirror
                  ref="codeMirror"
                  :options="cmOptions"
                  v-model:value="sqlText"
                ></codemirror>
              </div>
              <div v-if="datasourceType == 3" style="height: 300px">
                <div :style="{height: '100%',width: sqlForm.mongoSearchType == 1?'50%':'100%',float:'left'}" v-if="datasourceType == 3">
                  <codemirror ref="codeMirror" :options="cmOptions" v-model:value="sqlText"/>
                </div>
                <div style="height: 100%; width: 49%;float:right" v-if="datasourceType == 3 && sqlForm.mongoSearchType == 1">
                  <codemirror ref="orderCodeMirror" :options="cmOptions" v-model:value="orderSql"/>
                </div>
              </div>
            </div>
            <div class="table-warp">
              <!--表格 start-->
              <div class="table-title">执行结果</div>
              <el-table
                :data="
                  sqlColumnTableData.tableData.slice(
                    (sqlColumnTableData.tablePage.currentPage - 1) *
                      sqlColumnTableData.tablePage.pageSize,
                    sqlColumnTableData.tablePage.currentPage * sqlColumnTableData.tablePage.pageSize
                  )
                "
                border
                style="width: 100%"
                align="center"
                height="230px"
                :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
              >
                <el-table-column width="72" label="序号" align="center">
                  <template v-slot="scope">
                    <span>{{
                      (sqlColumnTableData.tablePage.currentPage - 1) *
                        sqlColumnTableData.tablePage.pageSize +
                      scope.$index +
                      1
                    }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="columnName" label="列名" align="center" />
                <el-table-column prop="name" label="别名" align="center" />
                <el-table-column prop="dataType" label="数据类型" align="center" />
                <el-table-column prop="width" label="宽度" align="center" />
                <el-table-column prop="remark" label="注释" align="center" />
              </el-table>
              <!--表格 end-->
              <!--分页 start-->
              <el-pagination
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                :current-page="sqlColumnTableData.tablePage.currentPage"
                :page-sizes="sqlColumnTableData.tablePage.pageSizeRange"
                :page-size="sqlColumnTableData.tablePage.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="sqlColumnTableData.tablePage.pageTotal"
              >
              </el-pagination>
              <!--分页 end-->
            </div>
          </div>
        </div>
      </div>

      <div v-show="addDatasetType == 2" class="parameter-content">
        <div v-show="sqlForm.sqlType == 1 || datasourceType == 2" style="margin-bottom: 20px">
          <div class="parameter-warp">
            <div class="warp-title">字段参数</div>

            <el-form label-position="right" :model="paramForm" class="df-form" ref="paramRef">
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input v-model="paramForm.paramName" placeholder="参数名称"></el-input>
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input v-model="paramForm.paramCode" placeholder="参数编码"></el-input>
              </el-form-item>
              <el-form-item
                label="参数前缀"
                v-if="datasourceType == 2"
                prop="paramPrefix"
                :rules="filter_rules('参数前缀', { required: false })"
              >
                <el-input v-model="paramForm.paramPrefix" placeholder="参数前缀"></el-input>
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select v-model="paramForm.paramType" placeholder="参数类型">
                  <el-option label="字符串" value="varchar"></el-option>
                  <el-option label="数值" value="number"></el-option>
                  <el-option label="日期" value="date"></el-option>
                  <el-option label="下拉单选" value="select"></el-option>
                  <el-option label="下拉多选" value="mutiselect"></el-option>
                  <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                  <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select v-model="paramForm.dateFormat" placeholder="日期格式">
                  <el-option label="年" value="YYYY"></el-option>
                  <el-option label="年-月" value="YYYY-MM"></el-option>
                  <el-option label="年-月-日" value="YYYY-MM-DD"></el-option>
                  <el-option label="年-月-日 时:分" value="YYYY-MM-DD HH:mm"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="默认值">
                <el-input v-model="paramForm.paramDefault" placeholder="默认值"></el-input>
              </el-form-item>
              <el-form-item
                label="是否必填"
                prop="paramRequired"
                :rules="filter_rules('是否必填', { required: true })"
              >
                <el-select v-model="paramForm.paramRequired" placeholder="是否必填">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="是否隐藏"
                prop="paramHidden"
                :rules="filter_rules('是否隐藏', { required: true })"
                key="paramHidden"
              >
                <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                label="选择内容来源"
                prop="selectType"
                key="selectType"
                :rules="filter_rules('选择内容来源', { required: true })"
              >
                <el-select
                  v-model="paramForm.selectType"
                  placeholder="选择内容来源"
                  @change="changeSlectType"
                >
                  <el-option label="自定义" value="1"></el-option>
                  <el-option label="sql语句" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  (paramForm.paramType == 'select' && paramForm.selectType == '2') ||
                  (paramForm.paramType == 'mutiselect' && paramForm.selectType == '2') ||
                  paramForm.paramType == 'treeSelect' ||
                  paramForm.paramType == 'multiTreeSelect'
                "
                label="选择数据源"
                prop="datasourceId"
                :rules="filter_rules('选择数据源', { required: true })"
              >
                <el-select v-model="paramForm.datasourceId" placeholder="选择数据源">
                  <el-option
                    v-for="op in dataSource"
                    :label="op.dataSourceName"
                    :value="op.datasourceId"
                    :key="op.datasourceId"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'select' && paramForm.selectType == '2'"
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                key="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select v-model="paramForm.isRelyOnParams" placeholder="是否依赖其他参数">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' &&
                  paramForm.selectType == '2' &&
                  paramForm.isRelyOnParams == '1'
                "
                label="依赖参数代码"
                prop="relyOnParams"
                key="relyOnParams"
                :rules="filter_rules('依赖参数代码', { required: true })"
              >
                <el-input v-model="paramForm.relyOnParams" placeholder="依赖参数代码,多个用,分隔"></el-input>
              </el-form-item>
              <el-form-item
                v-if="paramForm.paramType == 'multiTreeSelect'"
                label="父子联动"
                prop="checkStrictly"
                key="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select v-model="paramForm.checkStrictly" placeholder="选择父子联动">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  paramForm.paramType == 'select' ||
                  paramForm.paramType == 'mutiselect' ||
                  paramForm.paramType == 'treeSelect' ||
                  paramForm.paramType == 'multiTreeSelect'
                "
                label="下拉选择内容"
                key="selectContent"
                prop="selectContent"
                :rules="filter_rules('下拉选择内容', { required: true })"
              >
                <el-input
                  type="textarea"
                  :cols="80"
                  v-model="paramForm.selectContent"
                  placeholder="下拉选择内容"
                ></el-input>
                <div class="sub-title">{{ selectContentSuggestion }}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addParam">添加</el-button>
              </el-form-item>
            </el-form>
            <div >
              <el-link v-if="paramForm.paramType == 'date'" :underline="false"  type="warning" href="https://gitee.com/springreport/springreport/wikis/pages?sort_id=13973093&doc_id=5747656" target="_blank">点击查看日期默认值设置规则</el-link>
              <el-tag
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                type="warning"
                >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
                注意：两个key必须是value 和 name</el-tag
              ><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" />
              <el-tag
                v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'"
                type="warning"
                >sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有
                value 和 name</el-tag
              >
              <el-tag
                v-if="
                  paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'
                "
                type="warning"
                >sql语句格式：select deptId as id, deptName as name,parentId as pid from table
                注意：返回的属性中必须有 id,name和pid</el-tag
              >
            </div>
          </div>
          <div style="height: 50%">
            <!--表格 start-->
            <el-table
              :data="paramTableData.tableData"
              border
              style="width: 98%"
              align="center"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
              <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
              <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
              <el-table-column prop="paramDefault" label="默认值" align="center"></el-table-column>
              <el-table-column
                prop="paramRequired"
                label="是否必填"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              ></el-table-column>
              <el-table-column
                prop="paramHidden"
                label="是否隐藏"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              ></el-table-column>
              <el-table-column
                prop="isRelyOnParams"
                label="是否依赖其他参数"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              ></el-table-column>
              <el-table-column
                prop="relyOnParams"
                label="依赖参数"
                align="center"
              ></el-table-column>
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button @click="editParam(scope.row)" type="primary" text>编辑</el-button>
                  <el-button @click="moveUp(scope.$index, '3')" type="primary" text>上移</el-button>
                  <el-button @click="moveDown(scope.$index, '3')" type="primary" text
                    >下移</el-button
                  >
                  <el-button @click="deleteParam(scope.$index)" type="primary" text>删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
        </div>
        <div v-show="sqlForm.sqlType == 2 && datasourceType == 1" style="margin-bottom: 20px">
          <div class="parameter-warp">
            <div class="warp-title">输入参数</div>

            <el-form
              label-position="right"
              :model="procedureParamForm"
              class="df-form"
              ref="inParamRef"
            >
              <el-form-item
                label="参数名称"
                prop="paramName"
                :rules="filter_rules('参数名称', { required: true })"
              >
                <el-input v-model="procedureParamForm.paramName" placeholder="参数名称"></el-input>
              </el-form-item>
              <el-form-item
                label="参数编码"
                prop="paramCode"
                :rules="filter_rules('参数编码', { required: true })"
              >
                <el-input v-model="procedureParamForm.paramCode" placeholder="参数编码"></el-input>
              </el-form-item>
              <el-form-item
                label="参数类型"
                prop="paramType"
                :rules="filter_rules('参数类型', { required: true })"
              >
                <el-select v-model="procedureParamForm.paramType" placeholder="参数类型">
                  <el-option label="int" value="int"></el-option>
                  <el-option label="String" value="String"></el-option>
                  <el-option label="Long" value="Long"></el-option>
                  <el-option label="BigDecimal" value="BigDecimal"></el-option>
                  <el-option label="Double" value="Double"></el-option>
                  <el-option label="Float" value="Float"></el-option>
                  <el-option label="Date" value="Date"></el-option>
                  <el-option label="DateTime" value="DateTime"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="默认值"
                prop="paramDefault"
                :rules="filter_rules('默认值', { required: false })"
              >
                <el-input v-model="procedureParamForm.paramDefault" placeholder="默认值"></el-input>
              </el-form-item>
              <el-form-item
                label="是否必填"
                prop="paramRequired"
                :rules="filter_rules('是否必填', { required: true })"
              >
                <el-select v-model="procedureParamForm.paramRequired" placeholder="是否必填">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime'
                "
                label="组件类型"
                prop="componentType"
                :rules="filter_rules('组件类型', { required: true })"
              >
                <el-select v-model="procedureParamForm.componentType" placeholder="组件类型">
                  <el-option label="输入框" value="input"></el-option>
                  <el-option label="下拉单选" value="select"></el-option>
                  <el-option label="下拉多选" value="mutiselect"></el-option>
                  <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                  <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime' &&
                  (procedureParamForm.componentType == 'select' ||
                    procedureParamForm.componentType == 'mutiselect')
                "
                label="选择内容来源"
                key="selectType"
                prop="selectType"
                :rules="filter_rules('选择内容来源', { required: true })"
              >
                <el-select v-model="procedureParamForm.selectType" placeholder="选择内容来源">
                  <el-option label="自定义" value="1"></el-option>
                  <el-option label="sql语句" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime' &&
                  ((procedureParamForm.componentType == 'select' &&
                    procedureParamForm.selectType == '2') ||
                    (procedureParamForm.componentType == 'mutiselect' &&
                      procedureParamForm.selectType == '2') ||
                    procedureParamForm.componentType == 'treeSelect' ||
                    procedureParamForm.componentType == 'multiTreeSelect')
                "
                label="选择数据源"
                prop="datasourceId"
                :rules="filter_rules('选择数据源', { required: true })"
              >
                <el-select v-model="procedureParamForm.datasourceId" placeholder="选择数据源">
                  <el-option
                    v-for="op in dataSource"
                    :label="op.dataSourceName"
                    :value="op.datasourceId"
                    :key="op.datasourceId"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime' &&
                  procedureParamForm.componentType == 'select' &&
                  procedureParamForm.selectType == '2'
                "
                label="是否依赖其他参数"
                prop="isRelyOnParams"
                key="isRelyOnParams"
                :rules="filter_rules('是否依赖其他参数', { required: true })"
              >
                <el-select
                  v-model="procedureParamForm.isRelyOnParams"
                  placeholder="是否依赖其他参数"
                >
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime' &&
                  procedureParamForm.componentType == 'multiTreeSelect'
                "
                label="父子联动"
                prop="checkStrictly"
                key="checkStrictly"
                :rules="filter_rules('父子联动', { required: true })"
              >
                <el-select v-model="procedureParamForm.checkStrictly" placeholder="选择父子联动">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="
                  procedureParamForm.paramType != 'Date' &&
                  procedureParamForm.paramType != 'DateTime' &&
                  (procedureParamForm.componentType == 'select' ||
                    procedureParamForm.componentType == 'mutiselect' ||
                    procedureParamForm.componentType == 'treeSelect' ||
                    procedureParamForm.componentType == 'multiTreeSelect')
                "
                key="selectContent"
                label="下拉选择内容"
                prop="selectContent"
                :rules="filter_rules('下拉选择内容', { required: true })"
              >
                <el-input
                  type="textarea"
                  :cols="80"
                  v-model="procedureParamForm.selectContent"
                  placeholder="下拉选择内容"
                ></el-input>
                <div class="sub-title">{{ selectContentSuggestion }}</div>
              </el-form-item>
              <el-form-item
                v-if="procedureParamForm.paramType == 'Date'"
                label="日期格式"
                prop="dateFormat"
                :rules="filter_rules('日期格式', { required: false })"
              >
                <el-select v-model="procedureParamForm.dateFormat" placeholder="日期格式">
                  <el-option label="年" value="YYYY"></el-option>
                  <el-option label="年-月" value="YYYY-MM"></el-option>
                  <el-option label="年-月-日" value="YYYY-MM-DD"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="是否隐藏"
                prop="paramHidden"
                :rules="filter_rules('是否隐藏', { required: true })"
              >
                <el-select v-model="procedureParamForm.paramHidden" placeholder="是否隐藏">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="addInParam">添加</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-tag
            v-if="
              procedureParamForm.paramType == 'Date' || procedureParamForm.paramType == 'DateTime'
            "
            type="warning"
            >注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag
          >
          <el-tag
            v-if="
              procedureParamForm.componentType == 'select' ||
              procedureParamForm.componentType == 'mutiselect'
            "
            type="warning"
            >自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}]
            注意：两个key必须是value 和 name</el-tag
          ><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" />
          <el-tag
            v-if="
              procedureParamForm.componentType == 'select' ||
              procedureParamForm.componentType == 'mutiselect'
            "
            type="warning"
            >sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有
            value 和 name</el-tag
          >
          <el-tag
            v-if="
              procedureParamForm.componentType == 'treeSelect' ||
              procedureParamForm.componentType == 'multiTreeSelect'
            "
            type="warning"
            >sql语句格式：select deptId as id, deptName as name,parentId as pid from table
            注意：返回的属性中必须有 id,name和pid</el-tag
          >
          <div style="height: 40%">
            <!--表格 start-->
            <el-table
              :data="procedureInParamTableData.tableData"
              border
              style="width: 100%"
              align="center"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
              <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
              <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
              <el-table-column prop="paramDefault" label="默认值" align="center"></el-table-column>
              <el-table-column
                prop="paramHidden"
                label="是否隐藏"
                align="center"
                :formatter="commonUtil.formatterTableValue"
              ></el-table-column>
              <el-table-column fixed="right" label="操作" width="180" align="center">
                <template #default="scope">
                  <el-button @click="editInParam(scope.row)" type="primary" text>编辑</el-button>
                  <el-button @click="moveUp(scope.$index, '1')" type="primary" text>上移</el-button>
                  <el-button @click="moveDown(scope.$index, '1')" type="primary" text
                    >下移</el-button
                  >
                  <el-button @click="deleteInParam(scope.$index)" type="primary" text
                    >删除</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
          <el-divider content-position="left">输出参数</el-divider>
          <el-form
            :inline="true"
            :model="procedureOutParamForm"
            class="demo-form-inline"
            ref="outParamRef"
          >
            <el-form-item
              label="参数名称"
              prop="paramName"
              :rules="filter_rules('参数名称', { required: true })"
            >
              <el-input v-model="procedureOutParamForm.paramName" placeholder="参数名称"></el-input>
            </el-form-item>
            <el-form-item
              label="参数编码"
              prop="paramCode"
              :rules="filter_rules('参数编码', { required: true })"
            >
              <el-input v-model="procedureOutParamForm.paramCode" placeholder="参数编码"></el-input>
            </el-form-item>
            <el-form-item
              label="参数类型"
              prop="paramType"
              :rules="filter_rules('参数类型', { required: true })"
            >
              <el-select v-model="procedureOutParamForm.paramType" placeholder="参数类型">
                <el-option label="VARCHAR" value="VARCHAR"></el-option>
                <el-option label="INTEGER" value="INTEGER"></el-option>
                <el-option label="BIGINT" value="BIGINT"></el-option>
                <el-option label="FLOAT" value="FLOAT"></el-option>
                <el-option label="DOUBLE" value="DOUBLE"></el-option>
                <el-option label="DECIMAL" value="DECIMAL"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="addOutParam">添加</el-button>
            </el-form-item>
          </el-form>
          <div style="height: 30%">
            <!--表格 start-->
            <el-table
              :data="procedureOutParamTableData.tableData"
              border
              style="width: 100%"
              align="center"
              height="230px"
              :header-cell-style="{ background: '#eef1f6', color: '#606266' }"
            >
              <el-table-column prop="paramName" label="参数名" align="center"></el-table-column>
              <el-table-column prop="paramCode" label="参数编码" align="center"></el-table-column>
              <el-table-column prop="paramType" label="参数类型" align="center"></el-table-column>
              <el-table-column fixed="right" label="操作" width="180" align="center">
                <template #default="scope">
                  <el-button @click="editOutParam(scope.row)" type="primary" text>编辑</el-button>
                  <el-button @click="moveUp(scope.$index, '2')" type="primary" text>上移</el-button>
                  <el-button @click="moveDown(scope.$index, '2')" type="primary" text
                    >下移</el-button
                  >
                  <el-button @click="deleteOutParam(scope.$index)" type="primary" text
                    >删除</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <!--表格 end-->
          </div>
        </div>
        <div class="parameter-warp">
           <div class="warp-title">字段参数</div>
               <el-select
                  v-model="subParamAttrs"
                  placeholder="主表字段"
                  multiple
                  clearable
                  style="width:220px"
                >
                  <el-option
                    v-for="op in sqlColumnTableData.tableData"
                    :key="op.name"
                    :label="op.name"
                    :value="op.name"
                  />
                </el-select>
            </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeAddDataSet">取 消</el-button>
          <el-button type="primary" @click="addDataSet">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 左侧分组设置 -->
    <el-dialog
      :close-on-click-modal="false"
      title="分组设置"
      v-model="groupSetVisible"
      width="504px"
      @close="closeGroupDialog"
    >
      <div class="group-dialog">
        <div class="df-c-b">
          <div class="title">全部分组（{{ groupList.length }}）</div>
          <el-button size="medium" @click="openGroupHandleDialog()">新增分组</el-button>
        </div>
        <el-table :data="groupList" style="width: 100%; margin-top: 12px">
          <el-table-column prop="groupName" label="选项" />
          <el-table-column label="操作" width="180">
            <template v-slot="scope">
              <el-link
                type="info"
                style="margin-right: 12px"
                @click="openGroupHandleDialog(scope.row)"
                >编辑</el-link
              >
              <el-link type="info" @click="deleteGroup(scope.row)">删除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
    <!-- 新增编辑分组 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="groupForm.id ? '编辑分组' : '新增分组'"
      v-model="groupHandleVisible"
      width="417px"
      @close="closeGroupHandleDialog"
    >
      <el-input v-model="groupForm.groupName" size="medium" placeholder="请输入" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeGroupHandleDialog()">取 消</el-button>
          <el-button type="primary" :loading="groupHandleLoading" @click="addOrEditGroup()"
            >确 定</el-button
          >
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="高亮设置"
      v-model="highlightVisiable"
      width="50%"
      height="80%"
      :close-on-click-modal="false"
      @close="closehighlightDialog"
    >
      <el-form
        ref="highlightRef"
        :inline="true"
        :model="highlightForm"
        class="demo-form-inline"
      >
        <el-form-item
          key="color"
          label="高亮颜色"
          prop="color"
          :rules="filter_rules('高亮颜色', { required: true })"
        >
          <el-select
            v-model="highlightForm.color"
            placeholder="高亮颜色"
            style="width:220px!important"
          >
            <el-option
              v-for="op in selectUtil.highlightcolor"
              :key="op.value"
              :label="op.label"
              :value="op.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button size="small" @click="closehighlightDialog">取 消</el-button>
        <el-button
          type="primary"
          size="small"
          @click="confirmSetHighlight"
        >确 定</el-button>
      </span>
      </template>
    </el-dialog>
    <modal
      ref="commonModal"
      :modal-config="modalConfig"
      :modal-form="modalForm"
      :modal-data="modalData"
      :modal-handles="modalHandles"
      @closeModal="closeModal()"
    />
    <modal
      ref="chartModalRef"
      :modal-config="chartModalConfig"
      :modal-form="chartModalForm"
      :modal-data="chartModalData"
      :modal-handles="chartModalHandles"
      @closeModal="closeChartModal()"
    />
    <modal
      ref="codeModalRef"
      :modal-config="codeModalConfig"
      :modal-form="codeModalForm"
      :modal-data="codeModalData"
      :modal-handles="codeModalHandles"
      @closeModal="closeCodeModal()"
    />
    <modal
      ref="paperMarginModalRef"
      :modal-config="paperMarginModalConfig"
      :modal-form="paperMarginModalForm"
      :modal-data="paperMarginModalData"
      :modal-handles="paperMarginModalHandles"
      @closeModal="closePaperMarginModal()"
    />
    <textarea id="clipboradInput" value="" style="opacity: 0; position: absolute" />
  </div>
</template>

<script src="./docDesign.js"></script>
<style scoped src="./style.css"></style>

<style scoped lang="scss">
  .pagebox {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: #ffffff;
  }

  .left {
    box-sizing: border-box;
    width: 460px;
    height: 99vh;
    background: #ffffff;
    // box-shadow: 0px 4px 8px #D2E3FF;
    overflow-y: auto;
    overflow-x: hidden;
    border-top: 1px solid #e7e7e7;

    .group-list {
      padding: 8px;
      border-radius: 4px;
      background: #f1f2f3;
      height: calc(100% - 170px);
      overflow-y: auto;

      ::v-deep .el-collapse-item__header {
        height: 18px;
        line-height: 18px;
        background-color: transparent;
        color: #979797;
        font-size: 12px;
        margin-bottom: 8px;
      }

      ::v-deep .el-collapse-item__wrap {
        background-color: transparent;
      }

      ::v-deep .el-collapse-item__content {
        padding-bottom: 0;
        background-color: transparent;
      }

      .dataset-item {
        width: 188px;
        box-sizing: border-box;
        border-radius: 4px;
        background: #fff;
        color: #3c3c3c;
        font-size: 12px;
        font-style: normal;
        font-weight: 400;
        line-height: 32px;
        height: 32px;
        margin-bottom: 8px;
        padding: 0 10px;
        cursor: pointer;
        transition: all 0.3s;

        .action-box {
          .action {
            width: 18px;
            height: 18px;
            background-size: 100% 100%;
          }

          .action-edit {
            background-image: url('@/assets/img/sheet/dataset-edit.png');
            margin-right: 4px;
          }

         .action-copy {
          background-image: url("@/assets/img/sheet/dataset-copy.png");
          margin-right: 4px;
        }
        .action-add {
          background-image: url("@/assets/img/sheet/dataset-add.svg");
          margin-right: 4px;
        }
          .action-del {
            background-image: url('@/assets/img/sheet/dataset-del.png');
          }
        }
      }

      .dataset-item:hover,
      .dataset-item-active {
        background: $base-color-primary;
        color: #fff;

        .action-edit {
          background-image: url('@/assets/img/sheet/dataset-edit-active.png') !important;
        }
        .action-add {
          background-image: url("@/assets/img/sheet/dataset-add-active.svg") !important;
        }
        .action-copy {
          background-image: url("@/assets/img/sheet/dataset-copy-active.svg") !important;
        }
        .action-del {
          background-image: url('@/assets/img/sheet/dataset-del-active.png') !important;
        }
      }
    }

    .dataset-fields {
      .group-list {
        padding: 0px;
        border-radius: 0px;
        background: #fff;
        height: calc(100% - 154px);

        .dataset-item {
          width: 100%;
          background: #f1f2f3;
          border: 1px solid #f1f2f3;
          box-sizing: border-box;

          .action-box {
            .action-edit {
              background-image: url('@/assets/img/sheet/dataset-copy.png');
              margin-right: 4px;
            }

            .action-add {
              background-image: url('@/assets/img/sheet/dataset-add.svg');
            }
          }
        }

        .dataset-item:hover,
        .dataset-item-active {
          background: #fff;
          color: $base-color-primary;
          border: 1px solid $base-color-primary;

          .action-edit {
            background-image: url('@/assets/img/sheet/dataset-copy-active.png') !important;
          }
          .action-add {
              background-image: url('@/assets/img/sheet/dataset-add-active2.svg') !important;
            }
          .action-del {
            background-image: url('@/assets/img/sheet/dataset-drag-active.png') !important;
          }
        }
      }
    }
  }

  .left-head {
    box-sizing: border-box;
    position: absolute;
    width: 240px;
    height: 32px;
    left: 0px;
    background: #ffffff;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  .left-head .el-icon-s-fold {
    position: absolute;
    right: 5%;
    top: 18.75%;
    bottom: 18.66%;
  }

  .left-dataset-title {
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 0px 16px;
    width: 460px;
    height: 48px;
    // border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 1;
    flex-grow: 0;
    background: #fff;
    box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.04);

    .add-dataset {
      border-radius: 4px;
      background-color: #fff;

      :deep(span) {
        font-size: 12px;
      }

      &:hover {
        color: $base-color-primary;
      }
    }

    // margin-top: 2px;
  }

  .add-dataset-dialog {
    ::v-deep .el-radio-button:first-child .el-radio-button__inner {
      border-radius: 10px 0 0 10px !important;
    }

    ::v-deep .el-radio-button:last-child .el-radio-button__inner {
      border-radius: 0 10px 10px 0 !important;
    }

    .variable-content {
      border: 1px solid #e4e9ed;
      background: #fafafa;
      width: 42.5%;

      flex-shrink: 0;

      .variable-title {
        height: 46px;
        padding: 0 16px;
        line-height: 46px;
        color: #1a1a1a;
        font-size: 14px;
        font-style: normal;
        font-weight: bold;
        border-bottom: 1px solid #efebeb;
      }

      .variable-warp {
        padding: 9px 17px;

        .variable-warp-title {
          color: #979191;
          font-size: 12px;
          font-style: normal;
          font-weight: bold;
          line-height: 22px;
          /* 183.333% */
          margin-bottom: 12px;
        }

        .variable-list {
          flex-wrap: wrap;

          .variable-item {
            width: calc((100% - 18px) / 3);
            box-sizing: border-box;
            padding: 0 10px;
            border-radius: 4px;
            background: #e1f2f0;
            height: 32px;
            line-height: 32px;
            color: #595959;
            font-size: 12px;
            margin-right: 9px;
            transition: all 0.3s;
            margin-bottom: 12px;
            cursor: pointer;

            &:hover {
              color: #fff;
              background: $base-color-primary;
              .el-dropdown {
                color: #fff;
              }
            }

            &:nth-child(3n) {
              margin-right: 0;
            }
          }
        }

        .analysis-list {
          border-radius: 3px;
          border: 1px solid #c1e0d9;
          background: #fff;
          padding: 8px 10px 0;

          .variable-item {
            border-radius: 4px;
            background: #f1f2f3;
          }
        }
      }
    }

    .sql-content {
      flex: 1;
      position: relative;
      margin-left: 24px;

      .left-action {
        left: -20px;
        border-radius: 0 3px 3px 0;
      }

      .action-icon {
        cursor: pointer;
        transition: all 0.3s;
        position: absolute;
        top: 50%;

        transform: translateY(-50%);
        z-index: 999;
        background-color: $base-color-primary;
        width: 10px;
        height: 48px;

        &:hover {
          opacity: 0.7;
        }
      }
    }

    .table-warp {
      padding: 10px;
      border: 1px solid #eee;

      .table-title {
        color: #1a1a1a;
        font-feature-settings: 'liga' off, 'clig' off;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: bold;
        line-height: normal;
        margin-bottom: 14px;
      }
    }

    .parameter-content {
      .parameter-warp {
        border-radius: 4px;
        background: #f7f9fc;
        padding: 0 14px;

        .warp-title {
          height: 56px;
          line-height: 56px;
          font-size: 14px;
          font-weight: bold;
          color: #1a1a1a;
        }
      }
    }
  }

  .group-dialog {
    ::v-deep .el-table th.el-table__cell {
      background-color: #fafafa;
      color: rgba(0, 0, 0, 0.85);
    }
  }

  .dataset-title {
    width: 80px;
    height: 22px;
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 22px;
    color: rgba(0, 0, 0, 0.9);
    flex: none;
    order: 0;
    flex-grow: 0;
  }

  .left-dataset-content {
    width: 460px;
    height: calc(100% - 48px);

    .dataset-group,
    .dataset-fields {
      flex: 1;
      padding: 0 12px;
      flex-shrink: 0;
      width: 50%;

      .section-name {
        height: 32px;
        color: #666;
        font-family: 'PingFang SC';
        font-size: 14px;
        font-style: normal;
        font-weight: bold;
        line-height: normal;
        line-height: 32px;
        margin-bottom: 4px;

        .set-group {
          cursor: pointer;
        }

        .setting-text {
          color: $base-color-primary;
          font-family: 'PingFang SC';
          font-size: 12px;
          font-style: normal;
          font-weight: 400;
          line-height: normal;
          height: 17px;
          margin-left: 1px;
        }
      }

      .search {
        margin-bottom: 10px;

        ::v-deep .el-input__wrapper {
          height: 36px;
          line-height: 36px;
          border-radius: 6px;
          padding: 0px 11px;
          border-color: rgba(0, 0, 0, 0.1);
        }

        ::v-deep .el-input__icon {
          line-height: 36px;
        }
      }
    }

    .dataset-group {
      border-right: 1px solid #f5f5f5;
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

  .el-icon-arrow-right {
    position: absolute;
    left: 5.25%;
    cursor: pointer;
  }

  .el-icon-edit {
    position: absolute;
    right: 17.3%;
    cursor: pointer;
    width: 14px;
    height: 14px;
    top: 9px;
    background-image: url('@/assets/img/sheet/edit.png');
    background-size: 14px 14px;
    background-repeat: no-repeat;
  }

  .el-icon-delete {
    position: absolute;
    right: 7.3%;
    cursor: pointer;
    width: 14px;
    height: 14px;
    top: 9px;
    background-image: url('@/assets/img/sheet/del.png');
    background-size: 14px 14px;
    background-repeat: no-repeat;
  }

  .right-el-icon-edit {
    position: absolute;
    right: 74px;
    cursor: pointer;
    width: 14px;
    height: 14px;
    top: 9px;
    background-image: url('@/assets/img/sheet/edit.png');
    background-size: 14px 14px;
    background-repeat: no-repeat;
  }

  ::v-deep .el-collapse-item__arrow {
    margin-right: 0;
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

  .right-el-icon-copy-document {
    position: absolute;
    right: 52px;
    cursor: pointer;
    width: 14px;
    height: 14px;
    top: 9px;
    background-image: url('@/assets/img/sheet/copy.png');
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

  .dataset-box-content {
    width: 240px;
    min-height: 150px;
    /* background: #A5C3F5; */
    flex: none;
    order: 4;
    flex-grow: 0;
    max-height: 400px;
    overflow-y: auto;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    padding-left: 5px;
    padding-top: 3px;
  }

  .right-form {
    .column-tag {
      width: 100% !important;
      height: 14px;
      background: transparent;
      color: #595959;
      font-family: 'PingFang SC';
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 14px;
      border: 0;
      padding: 0;
    }
  }

  .column-tag {
    max-width: 150px;
    height: 30px;
    background: #f7bb61;
    border-radius: 2px;
    color: rgba(0, 0, 0, 0.6);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: inline-block;
    padding: 0 10px;
    height: 32px;
    line-height: 30px;
    font-size: 12px;
    border-radius: 4px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    border: 1px solid rgba(64, 158, 255, 0.2);
    font-weight: bold;
  }

  .center {
    flex: 1;
    position: relative;
    margin: 0 10px;

    .left-action {
      left: -10px;
      border-radius: 0 3px 3px 0;
    }

    .right-action {
      border-radius: 3px 0 0 3px;
      right: -13px;
    }

    .action-icon {
      cursor: pointer;
      transition: all 0.3s;
      position: absolute;
      top: 50%;

      transform: translateY(-50%);
      z-index: 999;
      background-color: $base-color-primary;
      width: 10px;
      height: 48px;

      &:hover {
        opacity: 0.7;
      }
    }

    // height: 100vh;
  }

  .right {
    // top:50px;
    width: 306px;
    height: 99vh;
    background: #ffffff;
    // box-shadow: 0px 4px 8px #D2E3FF;
  }

  .right-head {
    // top:50px;
    box-sizing: border-box;
    position: absolute;
    width: 306px;
    height: 32px;
    right: 0px;
    background: #ffffff;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  .right-head .el-icon-s-unfold {
    position: absolute;
    left: 5%;
    top: 18.75%;
    bottom: 18.66%;
  }

  .right-title {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    width: 306px;
    height: 48px;
    background: #e7e7e7;
    flex: none;
    order: 1;
    align-self: stretch;
    flex-grow: 0;
    border-bottom: 1px solid rgba(0, 0, 0, 0.05);
    background: #fff;
    box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.04);
  }

  .cell-property {
    display: inline-block;
    width: 50%;
    height: 48px;
    line-height: 48px;
    font-size: 16px;
    cursor: pointer;
    color: #1a1a1a;
    font-weight: 500;
    position: relative;
    text-align: center;

    &::before {
      position: absolute;
      content: '';
      left: 50%;
      bottom: 0;
      transform: translateX(-50%);
      background: $base-color-primary;
      width: 51px;
      height: 3px;
    }
  }

  .cell-property-noactive {
    color: #666;

    &::before {
      width: 0;
    }
  }

  .cell-label {
    color: #292e33;
    font-family: 'PingFang SC';
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
    line-height: 12px;
    margin-right: 12px;
  }

  ::v-deep .sub-collapse {
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

  .right-form {
    position: absolute;
    width: 306px;
    height: calc(100vh - 64px - 10px - 48px - 10px);
    right: 0px;
    // top: 50px;
    background: #ffffff;
    overflow: auto;

    ::v-deep .el-collapse-item {
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

    .df-form-item {
      display: flex;

      ::v-deep .el-form-item__label {
        margin-right: 12px;
        margin-bottom: 0;
      }
    }

    ::v-deep .el-form-item {
      margin-bottom: 8px;
    }

    ::v-deep .el-form--label-top .el-form-item__label {
      padding-bottom: 0;
      line-height: 32px;
    }
  }

  ::v-deep .demo-form-inline.el-form--label-top .el-form-item__label {
    padding-bottom: 0;
    line-height: 32px;
  }

  ::v-deep .demo-form-inline .el-select,
  ::v-deep .demo-form-inline .el-textarea,
  ::v-deep .demo-form-inline .el-input {
    width: 100% !important;
  }

  .df-form {
    display: flex;
    flex-wrap: wrap;

    ::v-deep .el-form-item {
      width: 20%;
      display: flex;
      box-sizing: border-box;
      padding-right: 12px;

      &:nth-child(5n) {
        margin-right: 0;
      }

      .el-form-item__label {
        flex-shrink: 0;
        width: 80px;
      }

      .el-form-item__content {
        width: calc(100% - 92px);
      }
    }
  }

  .right-form::-webkit-scrollbar {
    width: 14px;
    height: 14px;
  }

  .right-form::-webkit-scrollbar-track,
  ::-webkit-scrollbar-thumb {
    border-radius: 999px;
    border: 5px solid transparent;
  }

  .right-form::-webkit-scrollbar-track {
    box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2) inset;
  }

  .right-form::-webkit-scrollbar-thumb {
    min-height: 20px;
    background-clip: content-box;
    box-shadow: 0 0 0 5px rgba(0, 0, 0, 0.2) inset;
  }

  .right-form::-webkit-scrollbar-corner {
    background: transparent;
  }

  .blockBtn {
    width: 230px;
    height: 30px;
    background: #17b794;
    border-radius: 4px;
    line-height: 5px;
    border-color: #17b794;
  }

  .contentbox {
    display: flex;
  }

  .dataset-box-content::-webkit-scrollbar {
    width: 5px;
  }

  /*修改左侧垂直滚动条的样式*/
  .left::-webkit-scrollbar {
    width: 5px;
  }

  /*修改左侧垂直滚动条的样式*/
  .tplname {
    padding: 0px 16px;
    background-color: rgba(208, 208, 208, 0);
    font-size: 18px;
    line-height: 30px;
    color: #1a1a1a;
    font-weight: bold;
    margin: 5px 0;
  }

  .header-box {
    margin-bottom: 10px;
  }

  ._header {
    height: 64px !important;
    padding: 0px;
    background-color: #fff;

    //   border-bottom: 1px solid #ccc;
    .headerRight {
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

  ::v-deep .el-avatar {
    background: #17b794 !important;
  }

  ::v-deep .el-dialog__wrapper {
    overflow: hidden;
    //    z-index: 2005 !important;
    pointer-events: none !important;
  }

  ::v-deep .el-dialog {
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
  }

  ::v-deep .authdialog {
    margin-top: 50px !important;
    margin-left: 0px !important;
    flex-direction: column !important;
    // overflow: hidden !important;
    max-height: calc(100% - 90px) !important;
    top: 0 !important;
    left: 0px !important;
    bottom: 0;
    pointer-events: auto !important;
    /* background:#d9ebf0 !important; */
  }

  .authdialog ::v-deep .el-dialog__body {
    height: calc(100% - 90px) !important;
    overflow: auto;
  }

  .authdialog ::v-deep .el-dialog-div {
    max-height: 60vh;
    overflow: auto;
    margin-left: 10px;
  }

  .authdialog ::v-deep .el-dialog-div::-webkit-scrollbar {
    display: none;
    /*隐藏滚动条*/
  }

  .authdialog ::v-deep .el-dialog__title {
    font-weight: bold;
  }

  .el-divider--horizontal {
    margin: 10px 0;
  }

  ::v-deep .el-tabs__content .el-tab-pane {
    height: 600px;
    overflow: auto;
  }

  ::v-deep .vue-codemirror .CodeMirror {
    border: 1px solid #eee;
  }

  .config-panel {
    background: #ffffff;
    margin-left: 1px;
    top: -48px;
    position: relative;
    width: 306px;
    // height: 95%;
    height: calc(100vh - 64px - 10px - 10px);

    display: flex;
    flex-direction: column;
    overflow: auto;

    .config-header {
      width: 100%;
      height: 48px;
      // background: #2F343D;
      font-size: 16px;
      font-weight: bold;
      color: #1a1a1a;
      line-height: 48px;
      text-align: center;
    }

    .config-box {
      flex: 1;
      padding: 10px;
      overflow: auto;
    }

    /*定义滚动条的宽度*/
    .config-box::-webkit-scrollbar {
      width: 0;
    }
  }
</style>

<style lang="scss">
  .add-dataset-dialog {
    .el-radio-button:first-child .el-radio-button__inner {
      border-radius: 10px 0 0 10px !important;
    }

    .el-radio-button:last-child .el-radio-button__inner {
      border-radius: 0 10px 10px 0 !important;
    }
  }
</style>
