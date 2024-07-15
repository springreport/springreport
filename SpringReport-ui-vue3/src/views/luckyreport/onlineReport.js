/*
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-01-22 15:50:34
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-08 07:01:47
 */

export default {
    components: {
    },
    data() {
        return{
            pageId:null,
            websocket: null, //建立的连接
            lockReconnect: false, //是否真正建立连接
            timeout: 60 * 1000, //60秒一次心跳
            timeoutObj: null, //心跳心跳倒计时
            serverTimeoutObj: null, //心跳倒计时
            timeoutnum: null, //断开 重连倒计时
            sheetIndexIdMap:{},
            socketInfo:{},//socket通知信息，用于记录单元格更新消息和更新时间
            insertCoordinate:0,
            insertCount:1,
            deleteStart:0,//删除开始标志
            deleteEnd:0,//删除结束标志
            sheetOptions:{
                container: 'luckysheet', //luckysheet为容器id
                title:"", //表 头名
                lang: 'zh', //中文
                // plugins: ['chart'],
                forceCalculation:true,
                index:'0', //工作表索引
                status:'1',//激活状态
                order:'0', //工作表的顺序
                hide:'0',  //是否隐藏
                showtoolbar: true,//是否显示工具栏
                showinfobar: false,//是否显示顶部信息栏
                showsheetbar: true,//是否显示底部sheet按钮
                showsheetbarConfig:{
                    add: true, //新增sheet  
                    menu: false, //sheet管理菜单
                    sheet: true //sheet页显示
                },
                sheetRightClickConfig:{
                    delete: true, // 删除
                    copy: false, // 复制
                    rename: false, //重命名
                    color: false, //更改颜色
                    hide: false, //隐藏，取消隐藏
                    move: false, //向左移，向右移
                },
                allowEdit: true,
                showtoolbarConfig:{
                    save:false,//保存
                    preview:false,
                    upload:false,
                    datasource:false,
                    history:false,
                    saveAs:false,
                    currencyFormat: false, //货币格式
                    percentageFormat: false, //百分比格式
                    numberDecrease: false, // '减少小数位数'
                    numberIncrease: false, // '增加小数位数
                    moreFormats: true, // '更多格式'
                    border: true, // '边框'
                    textWrapMode: false, // '换行方式'
                    textRotateMode: false, // '文本旋转方式'
	                image:false, // '插入图片'
                    link:false,
                    chart: false, // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
                    postil:  false, //'批注'
                    pivotTable: false,  //'数据透视表'
                    function: false, // '公式'
                    frozenMode: false, // '冻结方式'
                    sortAndFilter: false, // '排序和筛选'
                    conditionalFormat: false, // '条件格式'
                    dataVerification: false, // '数据验证'
                    splitColumn: false, // '分列'
                    screenshot: false, // '截图'
                    protection:false, // '工作表保护'
	                print:false, // '打印'
                },
                cellRightClickConfig:{
                    copyAs: false, // 复制为
                    deleteCell: false, // 删除单元格
                    hideRow: false, // 隐藏选中行和显示选中行
                    hideColumn: false, // 隐藏选中列和显示选中列
                    matrix: false, // 矩阵操作选区
                    sort: false, // 排序选区
                    filter: false, // 筛选选区
                    chart: false, // 图表生成
                    image: false, // 插入图片
                    data: false, // 数据验证
	                cellFormat: false, // 设置单元格格式
                    rowHeight: false, // 行高
                    columnWidth: false, // 列宽
                },
                hook:{
                    cellUpdated:this.cellUpdated,
                    rangeClear:this.rangeClear,
                    updateCellFormat:this.updateCellFormat,
                    updateCellOtherFormat:this.updateCellOtherFormat,
                    changeBorder:this.changeBorder,
                    mergeCells:this.mergeCells,
                    mergeCancle:this.mergeCancle,
                    changeRowLen:this.changeRowLen,
                    changeColumnLen:this.changeColumnLen,
                    createSheet:this.createSheet,
                    deleteSheet:this.deleteSheet,
                    sheetEditNameAfter:this.sheetEditName,
                    rangeSelect:this.rangeSelect,
                    rowInsertBefore:this.rowInsertBefore,
                    rowDeleteBefore:this.rowDeleteBefore,
                },
            },
        }
    },
    mounted() {
        this.initTableSettings();
        this.initWebSocket();
        this.pageId = this.commonUtil.getUuid();
    },
    methods: {
        init(){
            let _this = this;
            var options = this.sheetOptions;
            luckysheet.create(options);
        },
        getSheetConfigs(luckysheetfile){
            var result = {};
            // result.title = json.title;
            result.hyperlinks = luckysheetfile.hyperlink
            result.config = luckysheetfile.config;
            result.frozen = luckysheetfile.frozen;
            result.images = luckysheetfile.images;
            result.sheetIndex = luckysheetfile.index;
            result.calcChain = luckysheetfile.calcChain;
            result.sheetName = luckysheetfile.name;
            result.sheetOrder = luckysheetfile.order;
            return result;
        },
        getTplSettings(){
            let reportTplId=this.$route.query.tplId;
            let param = {
                url:this.apis.onlineTpl.getOnlineTplSettingsApi,
                params:{id:reportTplId},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {
                    if(response.responseData && response.responseData.settings)
                    {
                        _this.sheetOptions.data = [];
                        for (let index = 0; index < response.responseData.settings.length; index++) {
                            const element = response.responseData.settings[index];
                            var cellDatas = {
                                celldata:element.cellDatas,
                                hyperlink:element.hyperlinks,
                                config:element.config,
                                frozen:element.frozen,
                                images:element.images,
                                calcChain:element.calcChain,
                                index:element.sheetIndex,
                                name:element.sheetName,
                                order:element.sheetOrder,
                                isPivotTable:false,
                                pivotTable:null
                            }
                            _this.sheetOptions.data.push(cellDatas);
                        }
                        _this.sheetIndexIdMap = response.responseData.sheetIndexIdMap;
                    }
                    _this.init();
                }
            });
        },
          initTableSettings(){
            this.getTplSettings();
            var that = this;
            $(document).click(function (event)
            {
                var content = event.target.innerHTML.trim();
                if(content)
                {
                    if(content.indexOf("插入行") == 0)
                    {
                        that.insertRowOrColumns("row",that.insertCoordinate,1,"lefttop");
                    }else if(content.indexOf("插入列") == 0)
                    {
                        that.insertRowOrColumns("column",that.insertCoordinate,1,"lefttop");
                    }else if(content.indexOf("向") == 0)
                    {
                        if(content.indexOf("列")>=0)
                        {
                            if(content.indexOf("左")>=0)
                            {
                                that.insertRowOrColumns("column",that.insertCoordinate,that.insertCount,"lefttop");
                            }else{
                                that.insertRowOrColumns("column",that.insertCoordinate,that.insertCount,"rightbottom");
                            }
                            
                        }else if(content.indexOf("行")>=0)
                        {
                            if(content.indexOf("上")>=0)
                            {
                                that.insertRowOrColumns("row",that.insertCoordinate,that.insertCount,"lefttop");
                            }else{
                                that.insertRowOrColumns("row",that.insertCoordinate,that.insertCount,"rightbottom");
                            }
                        }
                    }else if(content == "左")
                    {
                        that.insertRowOrColumns("column",that.insertCoordinate,that.insertCount,"lefttop");
                    }else if(content == "右")
                    {
                        that.insertRowOrColumns("column",that.insertCoordinate,that.insertCount,"rightbottom");
                    }else if(content == "上")
                    {
                        that.insertRowOrColumns("row",that.insertCoordinate,that.insertCount,"lefttop");
                    }else if(content == "下")
                    {
                        that.insertRowOrColumns("row",that.insertCoordinate,that.insertCount,"rightbottom");
                    }
                    else if(content.indexOf("删除选中行") == 0)
                    {
                        that.delRowOrColumns("row",that.deleteStart,that.deleteEnd);
                    }else if(content.indexOf("删除选中列") == 0)
                    {
                        that.delRowOrColumns("column",that.deleteStart,that.deleteEnd);
                    }else if(content.indexOf("删除选中") == 0)
                    {
                        if(content.indexOf("行")>=0)
                        {
                            that.delRowOrColumns("row",that.deleteStart,that.deleteEnd);
                        }else if(content.indexOf("列")>=0){
                            that.delRowOrColumns("column",that.deleteStart,that.deleteEnd);
                        }

                    }
                }
            });
          },
        cellUpdated(r,c){
            var data = luckysheet.getCellValue(r, c);
            var cellData = {
                r:r,
                c:c,
                data:data
            }
            var cellDatas = [cellData];
            this.saveCell('updateCell',cellDatas);
        },
        //保存单元格值
        saveCell(type,cellDatas,formatType)
        {
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            var sheetOrder = luckysheet.getSheet().order;
            var _this = this;
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],sheetOrder:sheetOrder,cellDatas:cellDatas,type:type,taskId:this.commonUtil.getUuid(),formatType:formatType},
            }
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        initWebSocket () {
            let tplId = this.$route.query.tplId;
            this.websocket = new WebSocket(import.meta.env.VITE_BASE_SOCKET_URL+'/springReport/api/screenWebsocket/' + tplId)
            // 连接错误
            this.websocket.onerror = this.setErrorMessage
     
            // 连接成功
            this.websocket.onopen = this.setOnopenMessage
     
            // 收到消息的回调
            this.websocket.onmessage = this.setOnMessage
     
            // 连接关闭的回调
            this.websocket.onclose = this.setOncloseMessage
     
            // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
            window.onbeforeunload = this.onbeforeunload
          },
          setErrorMessage () {
            console.log('WebSocket连接发生错误状态码：' + this.websocket.readyState)
            //重连
            this.reconnect();
          },
          setOnopenMessage () {
            console.log('WebSocket连接成功状态码：' + this.websocket.readyState)
            this.start();
          },
          setOnMessage (event) {
            // 根据服务器推送的消息做自己的业务处理
            let obj = JSON.parse(event.data)
            switch(obj.type) {
              case '1'://心跳检测
                //收到服务器信息，心跳重置
                console.log('心跳检测返回消息：' + event.data)
                this.reset();
                break;
              case 'updateCell'://更新单元格数据
                // console.log("接收到页面刷新消息：",event.data)
                this.updateCell(obj);
                break;
              case 'updateCellFormat'://更新格式
                this.updateCellFormatBack(obj,"ct");
                break;
              case 'updateCellFormatOther'://更新字体
                this.updateCellFormatBack(obj,obj.formatType);
                break;
              case 'updateMerge'://更新合并单元格
                this.updateMergeBack(obj);
              case 'updateBorder':
                this.updateBorderBack(obj)
                break;
              case 'updateRowlen':
                this.updateRowlenBack(obj)
                break;
              case 'updateColumnlen':
                this.updateColumnlenBack(obj)
                break;
              case 'createSheet':
                this.createSheetBack(obj)
                break;
              case 'deleteSheet':
                this.deleteSheetBack(obj)
                break;
              case 'changeSheetName':
                this.changeSheetNameBack(obj)
                break;
              case 'rowColumnInsert':
                this.rowColumnInsertBack(obj)
                break;
              case 'rowColumnDel':
                this.rowColumnDelBack(obj)
                break;
            }
          },
          setOncloseMessage () {
            console.log('WebSocket连接关闭状态码：' + this.websocket.readyState)
          },
          onbeforeunload () {
            this.closeWebSocket()
          },
          closeWebSocket () {
            this.websocket.close()
          },
          //重新连接
        reconnect() {
          var that = this;
          if (that.lockReconnect) {
            return;
          }
          that.lockReconnect = true;
          //没连接上会一直重连，设置延迟避免请求过多
          that.timeoutnum && clearTimeout(that.timeoutnum);
          that.timeoutnum = setTimeout(function () {
            //新连接
            that.initWebSocket();
            that.lockReconnect = false;
          }, 5000);
        },
        //重置心跳
        reset() {
          var that = this;
          //清除时间
          clearTimeout(that.timeoutObj);
          clearTimeout(that.serverTimeoutObj);
          //重启心跳
          that.start();
        },
        //开启心跳
        start() {
          var self = this;
          self.timeoutObj && clearTimeout(self.timeoutObj);
          self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj);
          self.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            if (self.websocket.readyState == 1) {
              // console.log("socket心跳检测正常");
              self.websocketsend("socketHeartBeat");
              //如果连接正常
              // self.reset();
            } else {
              //否则重连
              self.reconnect();
            }
            self.serverTimeoutObj = setTimeout(function () {
              //超时关闭
              self.websocket.close();
            }, self.timeout);
          }, self.timeout);
        },
        websocketsend(messsage) {
          this.websocket.send(messsage)
        },
        updateCell(obj){
            var cellDatas = obj.cellDatas;
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            for (let index = 0; index < cellDatas.length; index++) {
                const element = cellDatas[index];
                var key = "updateCellValue_"+sheetIndex+"_"+ element.r + "_"+ element.c
                let localVersion = this.socketInfo[key];
                if(!localVersion || version >= localVersion)
                {
                    luckysheet.setcellvalue(element.r, element.c, luckysheet.flowdata(), element.data);
                    this.socketInfo[key] = version;
                }
            }
            luckysheet.jfrefreshgrid();
        },
        updateCellFormatBack(obj,type)
        {
            var cellDatas = obj.cellDatas;
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            for (let index = 0; index < cellDatas.length; index++) {
                const element = cellDatas[index];
                var key = "updateCellFormat_"+ sheetIndex + "_" + type + "_"+ element.r + "_"+ element.c
                let localVersion = this.socketInfo[key];
                if(!localVersion || version >= localVersion)
                {
                    luckysheet.setCellFormat(element.r,element.c,type,element.data,{order:obj.sheetOrder});
                    if(type == "ct")
                    {
                        let v = luckysheet.getSheet().data[element.r][element.c].v;
                        luckysheet.setCellValue(element.r, element.c, v);
                    }
                    this.socketInfo[key] = version;
                }
            }
            luckysheet.refresh();
        },
        rangeClear(){
            var cells = this.getSelectRangeCells(null);
            if(cells && cells.length > 0){
                var cellDatas = [];
                for (let index = 0; index < cells.length; index++) {
                    var data = luckysheet.getSheet().data[cells[index][0]][cells[index][1]];
                    var cellData = {
                        r:cells[index][0],
                        c:cells[index][1],
                        v:data
                    }
                    cellDatas.push(cellData);
                }
                this.saveCell('updateCell',cellDatas);
            }
        },
        getSelectRangeCells(range,removeFirst){
            var cells = [];
            var selectedRanges;
            if(range)
            {
                selectedRanges = range;
            }else{
                selectedRanges = luckysheet.getRange();
            }
            if(selectedRanges && selectedRanges.length>0)
            {
                var flag = 0;
                for (let index = 0; index < selectedRanges.length; index++) {
                    const range = selectedRanges[index];
                    for (let r = range.row[0]; r <= range.row[1]; r++) {
                        for (let c = range.column[0]; c <= range.column[1]; c++) {
                            var cell = [r,c];
                            if(removeFirst)
                            {
                                if(flag != 0)
                                {
                                    cells.push(cell)
                                }
                            }else{
                                cells.push(cell)
                            }
                            flag ++;
                        }
                    }
                }
            }
            return cells;
        },
        updateCellFormat(r,c,foucsStatus,type){
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            var data = luckysheet.getSheet().data[r][c];
            if(data == null)
            {
                data = {};
                data.ct = {};
            }
            data.ct.t = type;
            data.ct.fa = foucsStatus;
            var cellData = {
                r:r,
                c:c,
                data:data.ct
            }
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],r:r,c:c,cellDatas:[cellData],type:'updateCellFormat',taskId:taskId,formatType:'ct'},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });

        },
        updateCellOtherFormat(row_st, row_ed,col_st,col_ed,foucsStatus,attr){
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let column = [col_st,col_ed];
            let row = [row_st,row_ed]
            var obj = {row:row,column:column}
            let range = [];
            range.push(obj)
            var cells = this.getSelectRangeCells(range);
            var cellDatas = [];
            for (let index = 0; index < cells.length; index++) {
                const element = cells[index];
                var data = luckysheet.getSheet().data[element[0]][element[1]];
                if(!data)
                {
                    data = {};
                }
                data[attr] = foucsStatus;
                var cellData = {
                    r:element[0],
                    c:element[1],
                    data:data[attr]
                }
                cellDatas.push(cellData);
            }
            this.saveCell('updateCellFormatOther',cellDatas,attr);
        },
        mergeCells(range,type){
            var luckysheetfile = luckysheet.getSheet();
            var ranges = [];
            let config = JSON.parse(JSON.stringify(luckysheetfile.config));
            if(!config.merge)
            {
                config.merge = {};
            }
            if(type == "mergeAll")
            {
                var key =  range.row[0]+"_"+range.column[0]
                var newMerge = {
                    r:range.row[0],
                    c:range.column[0],
                    rs:range.row[1]-range.row[0]+1,
                    cs:range.column[1]-range.column[0]+1
                }
                config.merge[key] = newMerge;
                ranges.push([range]);
            }else if(type == "mergeV")
            {
                for (let index = range.column[0]; index <= range.column[1]; index++) {
                    var key = range.row[0]+"_"+index
                    var newMerge = {
                        r:range.row[0],
                        c:index,
                        rs:range.row[1]-range.row[0]+1,
                        cs:1
                    } 
                    config.merge[key] = newMerge;
                    var newRange = {
                        row:[range.row[0],range.row[1]],
                        column:[index,index],
                    }
                    ranges.push([newRange]);
                }
            }else if(type == "mergeH")
            {
                for (let index = range.row[0]; index <= range.row[1]; index++) {
                    var key = index+"_"+range.column[0]
                    var newMerge = {
                        r:index,
                        c:range.column[0],
                        rs:1,
                        cs:range.column[1]-range.column[0]+1
                    } 
                    config.merge[key] = newMerge;
                    var newRange = {
                        row:[index,index],
                        column:[range.column[0],range.column[1]],
                    }
                    ranges.push([newRange]);
                }
            }
            var cells = [];
            if(ranges.length > 0)
            {
                for (let index = 0; index < ranges.length; index++) {
                    const element = ranges[index];
                    var rangeCells = this.getSelectRangeCells(element,true);
                    cells = cells.concat(rangeCells);
                }
            }
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],type:'updateMerge',taskId:taskId,
                        merge:config.merge,cells:cells},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        mergeCancle(range){
            var luckysheetfile = luckysheet.getSheet();
            let config = JSON.parse(JSON.stringify(luckysheetfile.config));
            let key = range.row[0]+"_"+range.column[0];
            this.$delete(config.merge,key);
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],type:'updateMerge',taskId:taskId,
                        merge:config.merge,cells:[]},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        updateMergeBack(obj)
        {
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            var key = "updateMerge_"+ sheetIndex
            let localVersion = this.socketInfo[key];
            let merge = obj.merge;
            let cells = obj.cells;
            if(!localVersion || version >= localVersion)
            {
                this.socketInfo[key] = version;
                this.remerge(merge,sheetIndex,cells);
            }
        },
        remerge(merge,sheetIndex,cells){
            let sheet = luckysheet.getSheet({index:sheetIndex})
            let sheetData = sheet.data;
            for (let index = 0; index < sheetData.length; index++) {
                const rowDatas = sheetData[index];
                for (let t = 0; t < rowDatas.length; t++) {
                    const element = rowDatas[t];
                    if(element != null && element.mc)
                    {
                        this.$delete(element,'mc');
                    }
                }
            }
            if(cells && cells.length > 0)
            {
                for (let index = 0; index < cells.length; index++) {
                    const element = cells[index];
                    if(sheetData[element[0]][element[1]])
                    {
                        sheetData[element[0]][element[1]].v = "";
                        sheetData[element[0]][element[1]].m = "";
                    }
                }
            }
            for(var i in merge)
            {
                var mergeInfo = merge[i];
                var r = mergeInfo.r;
                var c = mergeInfo.c;
                var rs = mergeInfo.rs;
                var cs = mergeInfo.cs;
                for (let index = 0; index < rs; index++) {
                    for (let t = 0; t < cs; t++) {
                        if(sheetData[r+index][c+t] == null)
                         {
                            sheetData[r+index][c+t] = {};
                         }
                        if(index == 0 && t == 0)
                        {
                            sheetData[r][c].mc={
                                r:r,
                                c:c,
                                rs:rs,
                                cs:cs
                            }
                        }else{
                            sheetData[r+index][c+t].mc={
                                r:r,
                                c:c,
                            }
                        }
                    }
                }
            }
            sheet.config.merge = merge;
            luckysheet.refresh();
        },
        changeBorder(borderInfo){
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],type:'updateBorder',taskId:taskId,
                    borderInfo:borderInfo},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        updateBorderBack(obj)
        {
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            var key = "updateBorder_"+ sheetIndex
            let localVersion = this.socketInfo[key];
            if(!localVersion || version >= localVersion)
            {
                this.socketInfo[key] = version;
                let sheet = luckysheet.getSheet({index:sheetIndex})
                sheet.config.borderInfo = obj.borderInfo;
                luckysheet.refresh();
            }
        },
        changeRowLen(rowlen){
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],type:'updateRowlen',taskId:taskId,
                    rowlen:rowlen},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        changeColumnLen(columnlen){
            let tplId = this.$route.query.tplId;
            var sheetIndex = luckysheet.getSheet().index;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetId:this.sheetIndexIdMap[sheetIndex],type:'updateColumnlen',taskId:taskId,
                columnlen:columnlen},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        updateRowlenBack(obj){
            var currentSheet = luckysheet.getSheet().index;
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            var key = "updateRowlen_"+ sheetIndex
            let localVersion = this.socketInfo[key];
            if(sheetIndex == currentSheet && !localVersion || version >= localVersion)
            {
                this.socketInfo[key] = version;
                let sheet = luckysheet.getSheet({index:sheetIndex})
                sheet.config.rowlen = obj.rowlen;
                luckysheet.jfrefreshgrid_rhcw(sheet.data.length,null);
            }
        },
        updateColumnlenBack(obj){
            var currentSheet = luckysheet.getSheet().index;
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            var key = "updateColumnlen_"+ sheetIndex
            let localVersion = this.socketInfo[key];
            if(sheetIndex == currentSheet && !localVersion || version >= localVersion)
            {
                this.socketInfo[key] = version;
                let sheet = luckysheet.getSheet({index:sheetIndex})
                sheet.config.columnlen = obj.columnlen;
                luckysheet.jfrefreshgrid_rhcw(null,sheet.data[0].length);
            }
        },
        createSheet(sheetConfig){
            let tplId = this.$route.query.tplId;
            let taskId = this.commonUtil.getUuid();
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,type:'createSheet',taskId:taskId,
                sheetConfig:sheetConfig},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        createSheetBack(obj)
        {
            if(!luckysheet.getSheet({index:obj.sheetConfig.index}))
            {
                $("#luckysheet-sheet-container-c").append(this.replaceHtml(this.commonConstants.commonVariable.sheetHTML,
                    {"index":obj.sheetConfig.index,"active": "","name": obj.sheetConfig.name,"style": "","colorset": ""}));
                luckysheet.getLuckysheetfile().push(obj.sheetConfig);
            }
            this.sheetIndexIdMap[obj.sheetConfig.index] = obj.newSheetId;
        },
        replaceHtml(temp, dataarry) {
            return temp.replace(/\$\{([\w]+)\}/g, function (s1, s2) { let s = dataarry[s2]; if (typeof (s) != "undefined") { return s; } else { return s1; } });
        },
        deleteSheet(index){
            let tplId = this.$route.query.tplId;
            let taskId = this.commonUtil.getUuid();
            let sheetOrder = luckysheet.getSheet({'index':index}).order;
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:index,sheetOrder:sheetOrder,sheetId:this.sheetIndexIdMap[index],type:'deleteSheet',taskId:taskId},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        deleteSheetBack(obj)
        {
            if(luckysheet.getSheet({index:obj.sheetIndex}))
            {
                luckysheet.setSheetDelete({order:obj.sheetOrder});
            }
        },
        sheetEditName(obj)
        {
            let tplId = this.$route.query.tplId;
            let taskId = this.commonUtil.getUuid();
            let sheetOrder = luckysheet.getSheet({'index':obj.i}).order;
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:obj.i,sheetOrder:sheetOrder,sheetId:this.sheetIndexIdMap[obj.i],sheetName:obj.newName,type:'changeSheetName',taskId:taskId},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        changeSheetNameBack(obj)
        {
            var sheetIndex = obj.sheetIndex;
            let version = obj.version*1;
            var key = "updateSheetName_"+sheetIndex
            let localVersion = this.socketInfo[key];
            if(!localVersion || version >= localVersion)
            {
                
                luckysheet.setSheetName(obj.sheetName,{order:obj.sheetOrder});
            }
        },
        insertRowOrColumns(type, index, value, direction){
            var luckysheetfile = luckysheet.getSheet();
            let sheetIndex = luckysheetfile.index;
            let tplId = this.$route.query.tplId;
            let taskId = this.commonUtil.getUuid();
            let sheetOrder = luckysheet.getSheet({index:sheetIndex}).order;
            var insertParams = {
                type:type,
                index:index,
                value:value,
                direction:direction,
                sheetIndex:sheetIndex,
                merge:luckysheetfile.config.merge?luckysheetfile.config.merge:null,
                borderInfo:luckysheetfile.config.borderInfo?luckysheetfile.config.borderInfo:null,
                rowlen:luckysheetfile.config.rowlen?luckysheetfile.config.rowlen:null,
                columnlen:luckysheetfile.config.columnlen?luckysheetfile.config.columnlen:null,
            }
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetOrder:sheetOrder,sheetId:this.sheetIndexIdMap[sheetIndex],type:'rowColumnInsert',taskId:taskId,
                insertParams:insertParams,pageId:this.pageId},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        delRowOrColumns(type, st, ed){
            var luckysheetfile = luckysheet.getSheet();
            let sheetIndex = luckysheetfile.index;
            let tplId = this.$route.query.tplId;
            let taskId = this.commonUtil.getUuid();
            let sheetOrder = luckysheet.getSheet({index:sheetIndex}).order;
            var delParams = {
                type:type,
                st:st,
                ed:ed,
                merge:luckysheetfile.config.merge?luckysheetfile.config.merge:null,
                borderInfo:luckysheetfile.config.borderInfo?luckysheetfile.config.borderInfo:null,
                rowlen:luckysheetfile.config.rowlen?luckysheetfile.config.rowlen:null,
                columnlen:luckysheetfile.config.columnlen?luckysheetfile.config.columnlen:null,
            }
            let param = {
                url:this.apis.onlineTpl.saveOnlineDocApi,
                params:{tplId:tplId,sheetIndex:sheetIndex,sheetOrder:sheetOrder,sheetId:this.sheetIndexIdMap[sheetIndex],type:'rowColumnDel',taskId:taskId,
                delParams:delParams,pageId:this.pageId},
            }
            var _this = this;
            this.commonUtil.doPost(param) .then(response=>{
                if (response.code == "200")
                {

                }
            });
        },
        rowColumnInsertBack(obj)
        {
            let pageId = obj.pageId;
            let sheetOrder = obj.sheetOrder;
            let insertParams = obj.insertParams;
            let sheetIndex = obj.sheetIndex;
            if(pageId != this.pageId)
            {
               var column = insertParams.index;
               if(insertParams.direction == "rightbottom")
               {
                 column = column + 1
               } 
               if(insertParams.type == "row")
               {
                 luckysheet.insertRow(column,{number:insertParams.value,order:sheetOrder})
               }else{
                 luckysheet.insertColumn(column,{number:insertParams.value,order:sheetOrder})
               }
            }
            let version = obj.version*1;
            this.updateLocalVersion(version,"updateRowlen_"+ sheetIndex);
            this.updateLocalVersion(version,"updateBorder_"+ sheetIndex);
            this.updateLocalVersion(version,"updateMerge_"+ sheetIndex);
            this.updateLocalVersion(version,"updateColumnlen_"+ sheetIndex);
        },
        rowColumnDelBack(obj)
        {
            let pageId = obj.pageId;
            let sheetOrder = obj.sheetOrder;
            let delParams = obj.delParams;
            let sheetIndex = obj.sheetIndex;
            if(pageId != this.pageId)
            {
                if(delParams.type == "row")
                {
                    luckysheet.deleteRow(delParams.st,delParams.ed,{order:sheetOrder});
                }else{
                    luckysheet.deleteColumn(delParams.st,delParams.ed,{order:sheetOrder});
                }
            }
            let version = obj.version*1;
            this.updateLocalVersion(version,"updateRowlen_"+ sheetIndex);
            this.updateLocalVersion(version,"updateBorder_"+ sheetIndex);
            this.updateLocalVersion(version,"updateMerge_"+ sheetIndex);
            this.updateLocalVersion(version,"updateColumnlen_"+ sheetIndex);
        },
        rowInsertBefore(coordinate,count){
            this.insertCoordinate = coordinate;
            this.insertCount = count;
        },
        rowDeleteBefore(start,end){
            this.deleteStart = start;
            this.deleteEnd = end;
        },
        updateLocalVersion(version,key)
        {
            let localVersion = this.socketInfo[key];
            if(version > localVersion)
            {
                this.socketInfo[key] =version;
            }
        }
    }
}
