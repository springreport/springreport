<template>
    <div v-loading="loading" :element-loading-text="loadingText" style="height: 100%;display: flex;flex-direction: column">
      <div style="width: 240px;flex: none;">
            <el-header class="_header df-c-b">
                <div class="headerLeft df-c" style="width:100%">
                <div class="tplname" style="width: 240px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" :title="tplName">
                      {{tplName}}
                    </div>
                </div>
            </el-header>
        </div>
      <input id="clipboradInput" value="" style="opacity:0;position:absolute" />
      <div style="flex: 1;height:100vh;display:flex">
        <div class="left">
            <div class="left-dataset-title">
                <span class="dataset-title">数据集管理</span>
                <el-button class="addBtn" @click="addDataSets">添加<icon-plus theme="outline" size="16" fill="#FFF" class="el-icon--right"/></el-button>
            </div>
            <div v-for="o in datasets" :key="o.id">
                <div :class="o.isActive?'dataset-box-active':'dataset-box'" style="position:relative">
                    <icon-right v-show="!o.isActive" theme="outline" size="16" fill="#999" class="el-icon-arrow-down"/>
                    <icon-down v-show="o.isActive" theme="outline" size="16" fill="#999" class="el-icon-arrow-right"/>
                    <span class="dataset-name" @click="clickDatasets(o)" :title="o.datasetName">{{o.datasetName}}
                       <el-dropdown>
                           <icon-copy style="margin-top:4px"/>
                          <template #dropdown>
                           <el-dropdown-menu>
                            <el-dropdown-item v-on:click="copyAttr(5,o.datasetName)">列表</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(6,o.datasetName)">区块对</el-dropdown-item>
                           </el-dropdown-menu>
                          </template>
                          </el-dropdown>
                    </span>
                    <icon-edit class="el-icon-edit" @click="editDataSet(o)" />
                    <icon-delete class="el-icon-delete" @click="deleteDataSet(o)" />
                </div>
                <div class="dataset-box-content wrapper" v-if="o.isActive">
                        <div class="column-tag" v-for="(column,index) in o.columns" :key="index" :title="column.name" >
                          <el-dropdown>
                          <icon-copy style="margin-top:8px"/>
                           <!-- <i class="icon-copy" title="复制"></i> -->
                           <template #dropdown>
                           <el-dropdown-menu>
                            <el-dropdown-item v-on:click="copyAttr(1,o.datasetName,column.name)">文本</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(2,o.datasetName,column.name)">图片</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(3,o.datasetName,column.name)">列表文本</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(4,o.datasetName,column.name)">列表图片</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(7,o.datasetName,column.name)">区块对文本</el-dropdown-item>
                            <el-dropdown-item v-on:click="copyAttr(8,o.datasetName,column.name)">区块对图片</el-dropdown-item>
                          </el-dropdown-menu>
                          </template>
                          </el-dropdown>
                          {{column.name}}
                        </div>
                    <el-input v-show="o.apiResult" type="textarea" placeholder="" v-model="o.apiResult" rows="6"></el-input>
                </div>
                
            </div>
        </div>
        <div class="center">
          <div class="menu" editor-component="menu">
          <div class="menu-item">
            <div class="menu-item__save" title="保存模板">
              <i></i>
            </div>
            <div class="menu-item__download" title="导出模板">
              <i></i>
            </div>
            <div class="menu-item__preview" title="预览">
              <i></i>
            </div>
            <div class="menu-item__undo">
              <i></i>
            </div>
            <div class="menu-item__redo">
              <i></i>
            </div>
            <div class="menu-item__painter" title="格式刷(双击可连续使用)">
              <i></i>
            </div>
            <div class="menu-item__format" title="清除格式">
              <i></i>
            </div>
          </div>
          <div class="menu-divider"></div>
          <div class="menu-item">
            <div class="menu-item__font">
              <span class="select" title="字体">微软雅黑</span>
              <div class="options">
                <ul>
                  <li data-family="Microsoft YaHei" style="font-family:'Microsoft YaHei';">微软雅黑</li>
                  <li data-family="宋体" style="font-family:'宋体';">宋体</li>
                  <li data-family="黑体" style="font-family:'黑体';">黑体</li>
                  <li data-family="仿宋" style="font-family:'仿宋';">仿宋</li>
                  <li data-family="楷体" style="font-family:'楷体';">楷体</li>
                  <li data-family="等线" style="font-family:'等线';">等线</li>
                  <li data-family="华文琥珀" style="font-family:'华文琥珀';">华文琥珀</li>
                  <li data-family="华文楷体" style="font-family:'华文楷体';">华文楷体</li>
                  <li data-family="华文隶书" style="font-family:'华文隶书';">华文隶书</li>
                  <li data-family="华文新魏" style="font-family:'华文新魏';">华文新魏</li>
                  <li data-family="华文行楷" style="font-family:'华文行楷';">华文行楷</li>
                  <li data-family="华文中宋" style="font-family:'华文中宋';">华文中宋</li>
                  <li data-family="华文彩云" style="font-family:'华文彩云';">华文彩云</li>
                  <li data-family="Arial" style="font-family:'Arial';">Arial</li>
                  <li data-family="Segoe UI" style="font-family:'Segoe UI';">Segoe UI</li>
                  <li data-family="Ink Free" style="font-family:'Ink Free';">Ink Free</li>
                  <li data-family="Fantasy" style="font-family:'Fantasy';">Fantasy</li>
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
              <i></i>
            </div>
            <div class="menu-item__size-minus">
              <i></i>
            </div>
            <div class="menu-item__bold">
              <i></i>
            </div>
            <div class="menu-item__italic">
              <i></i>
            </div>
            <div class="menu-item__underline">
              <i></i>
              <span class="select"></span>
              <div class="options">
                <ul>
                  <li data-decoration-style='solid'>
                    <i></i>
                  </li>
                  <li data-decoration-style='double'>
                    <i></i>
                  </li>
                  <li data-decoration-style='dashed'>
                    <i></i>
                  </li>
                  <li data-decoration-style='dotted'>
                    <i></i>
                  </li>
                  <li data-decoration-style='wavy'>
                    <i></i>
                  </li>
                </ul>
              </div>
            </div>
            <div class="menu-item__strikeout" title="删除线(Ctrl+Shift+X)">
              <i></i>
            </div>
            <div class="menu-item__superscript">
              <i></i>
            </div>
            <div class="menu-item__subscript">
              <i></i>
            </div>
            <div class="menu-item__color" title="字体颜色">
              <i></i>
              <span></span>
              <input type="color" id="color" />
            </div>
            <div class="menu-item__highlight" title="高亮">
              <i></i>
              <span></span>
              <input type="color" id="highlight">
            </div>
          </div>
          <div class="menu-divider"></div>
          <div class="menu-item">
            <div class="menu-item__title">
              <i></i>
              <span class="select" title="切换标题">正文</span>
              <div class="options">
                <ul>
                  <li style="font-size:16px;">正文</li>
                  <li data-level="first" style="font-size:26px;">标题1</li>
                  <li data-level="second" style="font-size:24px;">标题2</li>
                  <li data-level="third" style="font-size:22px;">标题3</li>
                  <li data-level="fourth" style="font-size:20px;">标题4</li>
                  <li data-level="fifth" style="font-size:18px;">标题5</li>
                  <li data-level="sixth" style="font-size:16px;">标题6</li>
                </ul>
              </div>
            </div>
            <div class="menu-item__left">
              <i></i>
            </div>
            <div class="menu-item__center">
              <i></i>
            </div>
            <div class="menu-item__right">
              <i></i>
            </div>
            <div class="menu-item__alignment">
              <i></i>
            </div>
            <div class="menu-item__row-margin">
              <i title="行间距"></i>
              <div class="options">
                <ul>
                  <li data-rowmargin='1'>1</li>
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
              <i></i>
              <div class="options">
                <ul>
                  <li>
                    <label>取消列表</label>
                  </li>
                  <li data-list-type="ol" data-list-style='decimal'>
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
                  <li data-list-type="ul" data-list-style='disc'>
                    <label>实心圆点列表：</label>
                    <ul style="list-style-type: disc;">
                      <li>________</li>
                    </ul>
                  </li>
                  <li data-list-type="ul" data-list-style='circle'>
                    <label>空心圆点列表：</label>
                    <ul style="list-style-type: circle;">
                      <li>________</li>
                    </ul>
                  </li>
                  <li data-list-type="ul" data-list-style='square'>
                    <label>空心方块列表：</label>
                    <ul style="list-style-type: square;">
                      <li>________</li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="menu-divider"></div>
          <div class="menu-item">
            <div class="menu-item__table">
              <i title="表格"></i>
            </div>
            <div class="menu-item__table__collapse">
              <div class="table-close">×</div>
              <div class="table-title">
                <span class="table-select">插入</span>
                <span>表格</span>
              </div>
              <div class="table-panel"></div>
            </div>
            <div class="menu-item__image">
              <i title="图片"></i>
              <input type="file" id="image" accept=".png, .jpg, .jpeg, .svg, .gif">
            </div>
            <div class="menu-item__hyperlink">
              <i title="超链接"></i>
            </div>
            <div class="menu-item__separator">
              <i title="分割线"></i>
              <div class="options">
                <ul>
                  <li data-separator='0,0'>
                    <i></i>
                  </li>
                  <li data-separator="1,1">
                    <i></i>
                  </li>
                  <li data-separator="3,1">
                    <i></i>
                  </li>
                  <li data-separator="4,4">
                    <i></i>
                  </li>
                  <li data-separator="7,3,3,3">
                    <i></i>
                  </li>
                  <li data-separator="6,2,2,2,2,2">
                    <i></i>
                  </li>
                </ul>
              </div>
            </div>
            <div class="menu-item__watermark">
              <i title="水印(添加、删除)"></i>
              <div class="options">
                <ul>
                  <li data-menu="add">添加水印</li>
                  <li data-menu="delete">删除水印</li>
                </ul>
              </div>
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
          <div class="menu-divider"></div>
          <div class="menu-item">
            <div class="menu-item__search" data-menu="search">
              <i></i>
            </div>
            <div class="menu-item__search__collapse" data-menu="search">
              <div class="menu-item__search__collapse__search">
                <input type="text" />
                <label class="search-result"></label>
                <div class="arrow-left">
                  <i></i>
                </div>
                <div class="arrow-right">
                  <i></i>
                </div>
                <span>×</span>
              </div>
              <div class="menu-item__search__collapse__replace">
                <input type="text">
                <button>替换</button>
              </div>
            </div>
            <div class="menu-item__print" data-menu="print">
              <i></i>
            </div>
          </div>
        </div>
        <div class="catalog" editor-component="catalog">
          <div class="catalog__header">
            <span>目录</span>
            <div class="catalog__header__close">
              <i></i>
            </div>
          </div>
          <div class="catalog__main"></div>
        </div>
        <div class="editor"></div>
        <div class="comment" editor-component="comment"></div>
        <div class="footer" editor-component="footer">
          <div>
            <div class="catalog-mode" title="目录">
              <i></i>
            </div>
            <div class="page-mode">
              <i title="页面模式(分页、连页)"></i>
              <div class="options">
                <ul>
                  <li data-page-mode="paging" class="active">分页</li>
                  <li data-page-mode="continuity">连页</li>
                </ul>
              </div>
            </div>
            <span>可见页码：<span class="page-no-list">1</span></span>
            <span>页面：<span class="page-no">1</span>/<span class="page-size">1</span></span>
            <span>字数：<span class="word-count">0</span></span>
          </div>
          <!-- <div class="editor-mode" title="编辑模式(编辑、清洁、只读、表单)">编辑模式</div> -->
          <div>
            <div class="page-scale-minus" title="缩小(Ctrl+-)">
              <i></i>
            </div>
            <span class="page-scale-percentage" title="显示比例(点击可复原Ctrl+0)">100%</span>
            <div class="page-scale-add" title="放大(Ctrl+=)">
              <i></i>
            </div>
            <div class="paper-size">
              <i title="纸张类型"></i>
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
              <i title="纸张方向"></i>
              <div class="options">
                <ul>
                  <li data-paper-direction="vertical" class="active">纵向</li>
                  <li data-paper-direction="horizontal">横向</li>
                </ul>
              </div>
            </div>
            <!-- <div class="paper-margin" title="页边距">
              <i></i>
            </div> -->
            <div class="fullscreen" title="全屏显示">
              <i></i>
            </div>
            <!-- <div class="editor-option" title="编辑器设置">
              <i></i>
            </div> -->
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="数据集" v-model="addDatasetsDialogVisiable" width="80%"  top="20px" :close-on-click-modal='false' @close='closeAddDataSet'>
              <el-tabs type="border-card">
                  <el-tab-pane label="sql语句">
                  <div>
                      <el-form :inline="true" :model="sqlForm" class="demo-form-inline" ref="sqlRef">
                      <el-form-item label="数据集名称"  prop="datasetName" :rules="filter_rules('数据集名称',{required:true})">
                          <el-input v-model="sqlForm.datasetName" placeholder="数据集名称" size="small"></el-input>
                      </el-form-item>
                      <el-form-item  label="选择数据源" prop="datasourceId" :rules="filter_rules('选择数据源',{required:true})">
                          <el-select v-model="sqlForm.datasourceId" placeholder="选择数据源" size="small" @change="changeDatasource">
                              <el-option v-for="op in dataSource" :label="op.dataSourceName" :value="op.datasourceId" :key="op.datasourceId"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item  label="sql类型" prop="sqlType" :rules="filter_rules('选择数据源',{required:true})" v-if="datasourceType == 1">
                          <el-select v-model="sqlForm.sqlType" placeholder="选择sql类型" size="small">
                              <el-option v-for="op in selectUtil.sqlType" :label="op.label" :value="op.value" :key="op.value"></el-option>
                          </el-select>
                          </el-form-item>
                      </el-form>

                  <div style="height:25px;" v-if="datasourceType == 1">
                  <el-tooltip content="该操作将执行sql语句并校验sql语句的正确性，并将查询字段全部显示到下方的表格中" placement="bottom"><el-tag type="success" @click="execSql" size="small" style="cursor:pointer" ><i class="el-icon-caret-right"></i>执行</el-tag></el-tooltip>
                  <el-tooltip content="该操作会将sql语句进行格式化并显示" placement="right"><el-tag @click="formatSql" size="small" style="cursor:pointer"><i class="el-icon-document"></i>格式化</el-tag> </el-tooltip>
                  </div>
                  <div style="height:300px;" v-if="datasourceType == 1">
                  <codemirror ref="codeMirror"  :options="cmOptions" v-model:value="sqlText"></codemirror>
                  </div>
                  <div style="height:1px"></div>
                  <div>
                      <!--表格 start-->
                      <el-table :data="sqlColumnTableData.tableData.slice((sqlColumnTableData.tablePage.currentPage-1)*sqlColumnTableData.tablePage.pageSize,sqlColumnTableData.tablePage.currentPage*sqlColumnTableData.tablePage.pageSize)" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                      <el-table-column prop="columnName" label="列名"  align="center"></el-table-column>
                      <el-table-column prop="name" label="别名"  align="center"></el-table-column>
                      <el-table-column prop="dataType" label="数据类型"  align="center"></el-table-column>
                      <el-table-column prop="width" label="宽度"  align="center"></el-table-column>
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
                          :total="sqlColumnTableData.tablePage.pageTotal">
                          </el-pagination>
                          <!--分页 end-->
                  </div>
                  </div>
                  </el-tab-pane>
                  <el-tab-pane label="参数配置" >
                      <div v-show="sqlForm.sqlType == 1 || datasourceType == 2">
                      <el-divider content-position="left">字段参数</el-divider>
                      <el-form :inline="true" :model="paramForm" class="demo-form-inline" ref="paramRef">
                          <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                          <el-input v-model="paramForm.paramName" placeholder="参数名称" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                          <el-input v-model="paramForm.paramCode" placeholder="参数编码" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
                          <el-select v-model="paramForm.paramType" placeholder="参数类型"  size="small">
                              <el-option label="字符串" value="varchar"></el-option>
                              <el-option label="数值" value="number"></el-option>
                              <el-option label="日期" value="date"></el-option>
                              <el-option label="下拉单选" value="select"></el-option>
                              <el-option label="下拉多选" value="mutiselect"></el-option>
                              <el-option label="下拉树(单选)" value="treeSelect"></el-option>
                              <el-option label="下拉树(多选)" value="multiTreeSelect"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'date'" label="日期格式" prop="dateFormat" :rules="filter_rules('日期格式',{required:false})">
                          <el-select v-model="paramForm.dateFormat" placeholder="日期格式"  size="small">
                              <el-option label="yyyy-MM-dd" value="yyyy-MM-dd"></el-option>
                              <el-option label="yyyy-MM" value="yyyy-MM"></el-option>
                              <el-option label="yyyy-MM-dd HH:mm" value="yyyy-MM-dd HH:mm"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="默认值" prop="paramDefault">
                          <el-input v-model="paramForm.paramDefault" placeholder="默认值" size="small"></el-input>
                          </el-form-item>
                          <el-form-item label="是否必填" prop="paramRequired" :rules="filter_rules('是否必填',{required:true})">
                          <el-select v-model="paramForm.paramRequired" placeholder="是否必填" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item label="是否隐藏" prop="paramHidden" :rules="filter_rules('是否隐藏',{required:true})" key="paramHidden">
                          <el-select v-model="paramForm.paramHidden" placeholder="是否隐藏" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" label="选择内容来源" key="selectType" prop="selectType" :rules="filter_rules('选择内容来源',{required:true})">
                          <el-select v-model="paramForm.selectType" placeholder="选择内容来源"  size="small">
                              <el-option label="自定义" value="1"></el-option>
                              <el-option label="sql语句" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2'" label="是否依赖其他参数" prop="isRelyOnParams" key="isRelyOnParams" :rules="filter_rules('是否依赖其他参数',{required:true})">
                          <el-select v-model="paramForm.isRelyOnParams" placeholder="是否依赖其他参数" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="(paramForm.paramType == 'select') && paramForm.selectType == '2' && paramForm.isRelyOnParams == '1'" label="依赖参数代码" key="relyOnParams" prop="relyOnParams" :rules="filter_rules('依赖参数代码',{required:true})">
                            <el-input v-model="paramForm.relyOnParams" placeholder="依赖参数代码" size="small"></el-input>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'multiTreeSelect'" label="父子联动" prop="checkStrictly" key="checkStrictly" :rules="filter_rules('父子联动',{required:true})">
                          <el-select v-model="paramForm.checkStrictly" placeholder="选择父子联动" size="small">
                              <el-option label="是" value="1"></el-option>
                              <el-option label="否" value="2"></el-option>
                          </el-select>
                          </el-form-item>
                          <el-form-item v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect' || paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" key="selectContent" label="下拉选择内容" prop="selectContent" :rules="filter_rules('下拉选择内容',{required:true})">
                          <el-input type="textarea" :cols="80" v-model="paramForm.selectContent" placeholder="下拉选择内容" size="small"></el-input>
                          <div class="sub-title">{{selectContentSuggestion}}</div>
                          </el-form-item>
                          
                          <el-form-item>
                            <el-button type="primary" @click="addParam" size="small">添加</el-button>
                          </el-form-item>
                          <el-tag v-if="paramForm.paramType == 'date'" type="warning">注：当参数类型选择日期时，如果想让默认日期是当前日期，则默认值填写current或者CURRENT，如果想让默认日期是当前日期的天几天或者后几天，则填天数，例如前七天则填写-7，后七天则填写7。</el-tag>
                          <el-tag v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" type="warning">自定义数据格式：[{"value":"value1","name":"name1"},{"value":"value2","name":"name2"}] 注意：两个key必须是value 和 name</el-tag><br v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'">
                          <el-tag v-if="paramForm.paramType == 'select' || paramForm.paramType == 'mutiselect'" type="warning">sql语句格式：select code as value, name as name from table 注意：返回的属性中必须有 value 和 name</el-tag>
                          <el-tag v-if="paramForm.paramType == 'treeSelect' || paramForm.paramType == 'multiTreeSelect'" type="warning">sql语句格式：select deptId as id, deptName as name,parentId as pid from table 注意：返回的属性中必须有 id,name和pid</el-tag>
                          
                      </el-form>
                      <div style="height:2px"></div>
                      <div style="height:50%">
                      <!--表格 start-->
                      <el-table :data="paramTableData.tableData" border style="width: 98%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                      <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                      <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                      <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                      <el-table-column prop="paramDefault" label="默认值"  align="center"></el-table-column>
                      <el-table-column prop="paramRequired" label="是否必填"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="paramHidden" label="是否隐藏"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="isRelyOnParams" label="是否依赖其他参数"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                      <el-table-column prop="relyOnParams" label="依赖参数"  align="center"></el-table-column>
                      <el-table-column  label="操作"  align="center">
                           <template #default="scope">
                              <el-button @click="editParam(scope.row)" type="text" size="small">编辑</el-button>
                              <el-button @click="deleteParam(scope.$index)" type="text" size="small">删除</el-button>
                          </template>
                      </el-table-column>
                      </el-table>
                          <!--表格 end-->
                      </div>
                      </div>
                      <div v-show="sqlForm.sqlType == 2 && datasourceType == 1">
                          <el-divider content-position="left">输入参数</el-divider>
                          <el-form :inline="true" :model="procedureParamForm" class="demo-form-inline" ref="inParamRef">
                              <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                                  <el-input v-model="procedureParamForm.paramName" placeholder="参数名称" ></el-input>
                              </el-form-item>
                              <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                                  <el-input v-model="procedureParamForm.paramCode" placeholder="参数编码"></el-input>
                              </el-form-item>
                              <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
                              <el-select v-model="procedureParamForm.paramType" placeholder="参数类型"  >
                                  <el-option label="int" value="int"></el-option>
                                  <el-option label="String" value="String"></el-option>
                                  <el-option label="Long" value="Long"></el-option>
                                  <el-option label="BigDecimal" value="BigDecimal"></el-option>
                                  <el-option label="Double" value="Double"></el-option>
                                  <el-option label="Float" value="Float"></el-option>
                                  <el-option label="Date" value="Date"></el-option>
                              </el-select>
                              </el-form-item>
                              <el-form-item label="默认值" prop="paramDefault" :rules="filter_rules('默认值',{required:true})">
                                  <el-input v-model="procedureParamForm.paramDefault" placeholder="默认值"></el-input>
                              </el-form-item>
                              <el-form-item label="是否隐藏" prop="paramHidden" :rules="filter_rules('是否隐藏',{required:true})">
                                <el-select v-model="procedureParamForm.paramHidden" placeholder="是否隐藏" >
                                    <el-option label="是" value="1"></el-option>
                                    <el-option label="否" value="2"></el-option>
                                </el-select>
                                </el-form-item>
                              <el-form-item>
                              <el-button type="primary" @click="addInParam">添加</el-button>
                              </el-form-item>
                          </el-form>
                          <div style="height:40%">
                              <!--表格 start-->
                              <el-table :data="procedureInParamTableData.tableData" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                              <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                              <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                              <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                              <el-table-column prop="paramDefault" label="默认值"  align="center"></el-table-column>
                              <el-table-column prop="paramHidden" label="是否隐藏"  align="center" :formatter="commonUtil.formatterTableValue"></el-table-column>
                              <el-table-column fixed="right" label="操作" width="180" align="center">
                                   <template #default="scope">
                                      <el-button @click="editInParam(scope.row)" type="text" size="small">编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'1')" type="text" size="small">上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'1')" type="text" size="small">下移</el-button>
                                      <el-button @click="deleteInParam(scope.$index)" type="text" size="small">删除</el-button>
                                  </template>
                              </el-table-column>
                              </el-table>
                              <!--表格 end-->
                          </div>
                          <el-divider content-position="left">输出参数</el-divider>
                          <el-form :inline="true" :model="procedureOutParamForm" class="demo-form-inline" ref="outParamRef">
                              <el-form-item label="参数名称"  prop="paramName" :rules="filter_rules('参数名称',{required:true})">
                                  <el-input v-model="procedureOutParamForm.paramName" placeholder="参数名称" ></el-input>
                              </el-form-item>
                              <el-form-item label="参数编码"  prop="paramCode" :rules="filter_rules('参数编码',{required:true})">
                                  <el-input v-model="procedureOutParamForm.paramCode" placeholder="参数编码"></el-input>
                              </el-form-item>
                              <el-form-item label="参数类型" prop="paramType" :rules="filter_rules('参数类型',{required:true})">
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
                          <div style="height:30%">
                              <!--表格 start-->
                              <el-table :data="procedureOutParamTableData.tableData" border style="width: 100%" align="center" size="small" height="230px" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
                              <el-table-column prop="paramName" label="参数名"  align="center"></el-table-column>
                              <el-table-column prop="paramCode" label="参数编码"  align="center"></el-table-column>
                              <el-table-column prop="paramType" label="参数类型"  align="center"></el-table-column>
                              <el-table-column fixed="right" label="操作" width="180" align="center">
                                   <template #default="scope">
                                      <el-button @click="editOutParam(scope.row)" type="text" size="small">编辑</el-button>
                                      <el-button @click="moveUp(scope.$index,'2')" type="text" size="small">上移</el-button>
                                      <el-button @click="moveDown(scope.$index,'2')" type="text" size="small">下移</el-button>
                                      <el-button @click="deleteOutParam(scope.$index)" type="text" size="small">删除</el-button>
                                  </template>
                              </el-table-column>
                              </el-table>
                              <!--表格 end-->
                          </div>
                      </div>
                  </el-tab-pane>
                  </el-tabs>
                  <template #footer>
                  <span class="dialog-footer">
                  <el-button @click="closeAddDataSet" size="small">取 消</el-button>
                  <el-button type="primary" @click="addDataSet" size="small">确 定</el-button>
                  </span>
                  </template>
              </el-dialog>
              <modal
        ref="commonModal"
        :modalConfig="modalConfig"
        :modalForm="modalForm"
        :modalData="modalData"
        :modalHandles="modalHandles"
        @closeModal="closeModal()"
      ></modal>
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
    width: 240px;
    height: 99vh;
    background: #FFFFFF;
    // box-shadow: 0px 4px 8px #D2E3FF;
    overflow-y:auto;
    overflow-x:hidden;
    border-top: 1px solid #E7E7E7;
    border-right: 1px solid #E7E7E7;
}
.left-head{
    box-sizing: border-box;
    position: absolute;
    width: 240px;
    height: 32px;
    left: 0px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.left-head .el-icon-s-fold{
    position: absolute;
    right: 5%;
    top: 18.75%;
    bottom: 18.66%;
}
.left-dataset-title{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 5px 16px;
    gap: 62px;
    width: 240px;
    height: 40px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 1;
    flex-grow: 0;
    // margin-top: 2px;
}
.dataset-title{
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
.right-dataset-title{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 62px;
    width: 240px;
    height: 40px;
    background: #FFFFFF;
    /* border-bottom: 1px solid rgba(0, 0, 0, 0.1); */
    flex: none;
    order: 1;
    flex-grow: 0;
    margin-top: 2px;
}
.attr-dataset-title{
    width: 100px;
    height: 32px;
    font-style: normal;
    font-weight: 500;
    font-size: 14px;
    line-height: 32px;
    color:#606266;
    flex: none;
    order: 0;
    flex-grow: 0;
    font-weight: bold;
}
.addBtn{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 2px 12px;
    gap: 4px;
    width: 66px;
    height: 22px;
    background: #45c5a9;
    border-radius: 4px;
    flex: none;
    order: 1;
    flex-grow: 0;
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    font-size: 12px;
    line-height: 18px;
    color: #FFFFFF;
}
.dataset-box{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    padding: 7px 16px;
    gap: 103px;

    width: 240px;
    height: 36px;

    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 0;
    flex-grow: 0;
}
.el-icon-arrow-right{
    position: absolute;
    left: 5.25%;
    cursor:pointer;
}
.el-icon-arrow-down_dataset{
    position: absolute;
    left: 5.25%;
    cursor:pointer;
}
.el-icon-arrow-down{
    position: absolute;
    left: 5.25%;
    cursor:pointer;
}
.dataset-name{
    width: 140px;
    height: 22px;
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #191919;
    flex: none;
    order: 1;
    flex-grow: 0;
    max-width: 190px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow:ellipsis;
    padding-right:50px;
    cursor:pointer;
}
.el-icon-edit{
    position: absolute;
    right: 17.3%;
    color: #ED7B2F;
    cursor:pointer;
}
.el-icon-delete{
    position: absolute;
    right: 7.3%;
    color: #ED7B2F;
    cursor:pointer;
}

.right-el-icon-edit{
    position: absolute;
    right: 32.3%;
    cursor:pointer;
}
.right-block-el-icon-edit{
    position: absolute;
    right: 20.3%;
    cursor:pointer;
}
.right-el-icon-copy-document{
    position: absolute;
    right: 20.3%;
    cursor:pointer;
}
.right-el-icon-delete{
    position: absolute;
    right: 8.3%;
    cursor:pointer;
}

.dataset-box-active{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    padding: 7px 16px;
    gap: 103px;
    /* background: #A5C3F5; */
    width: 240px;
    height: 36px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    flex: none;
    order: 0;
    flex-grow: 0;
}
.dataset-box-content{
    width: 240px;
    min-height:150px;
    /* background: #A5C3F5; */
    flex: none;
    order: 4;
    flex-grow: 0;
    max-height:400px;
    overflow-y: auto;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    padding-left: 5px;
    padding-top: 3px;
}
.column-tag{
    max-width:150px;
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
    border: 1px solid rgba(64,158,255,.2);
    font-weight: bold;
}
.center{
    flex: 1;
    overflow:auto
    // height: 100vh;
}
.right{
    // top:50px;
    width: 254px;
    height: 99vh;
    background: #FFFFFF;
    // box-shadow: 0px 4px 8px #D2E3FF;
}
.right-head{
    // top:50px;
    box-sizing: border-box;
    position: absolute;
    width: 254px;
    height: 32px;
    right: 0px;
    background: #FFFFFF;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
.right-head .el-icon-s-unfold{
    position: absolute;
    left: 5%;
    top: 18.75%;
    bottom: 18.66%;
}
.right-title{
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    padding: 6px 4px;
    gap: 4px;
    width: 254px;
    height: 30px;
    background: #E7E7E7;
    flex: none;
    order: 1;
    align-self: stretch;
    flex-grow: 0;
    // margin-top: 2px;
}
.cell-property{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 3px 16px;
    gap: 4px;
    width: 50px;
    height: 22px;
    background: #FFFFFF;
    border-radius: 4px;
    flex: none;
    order: 0;
    flex-grow: 1;
    color:#45c5a9;
    font-size: 16px;
    cursor: pointer;
}
.cell-property-noactive{
    background: #E7E7E7;
    cursor: pointer;
    color:rgba(0, 0, 0, 0.6);;

}
.blockBtn{
    width: 230px;
    height: 30px;
    background: #45c5a9;
    border-radius: 4px;
    line-height: 5px;
    border-color: #45c5a9;
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
  padding: 0px 20px;
  background-color: rgba(208, 208, 208, 0);
  font-size: 19px;
  line-height: 30px;
  color: #45c5a9;
  font-weight: bold;
  margin: 5px 0;
}
._header {
  height: 45px !important;
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
:deep(.el-avatar){
    background:#45c5a9 !important
}

.el-divider--horizontal{
    margin: 10px 0
}
</style>
