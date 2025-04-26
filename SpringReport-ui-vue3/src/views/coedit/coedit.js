/*
 * @Description:
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2022-01-22 15:50:34
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-08 07:01:47
 */
import Axios from 'axios';
import vchart from '../../components/vchart/vchart.vue'
import vchartsetting from '../../components/vchart/vchartsetting.vue'
export default {
    components: {
      vchart,
      vchartsetting
    },
    data() {
        return{
            isThirdParty:2,
            shareUser:"",
            loading:false,
            loadingText:"文件上传中，请耐心等待...",
            hisdialogVisible:false,
            hisDialogTitle:"",
            historyData:[],
            r:0,
            c:0,
            type:2,
            total:0,
            currentPage:1,
            pageSize:5,
            options:{
                container: "luckysheet",
                lang: 'zh',
				        plugins: ['chart'],
                fontList:[
                  {
                  "fontName":"条形码（barCode128）",
                  "url":""
                  },
                  {
                    "fontName":"二维码（qrCode）",
                    "url":""
                  },
                ],
                allowUpdate:true,
                // updateImageUrl: location.origin + "/luckysheet/api/updateImg",
                updateUrl: '',
                gridKey: '',
                isLoadALL:1,
                loadUrl: '',
                loadSheetUrl:'',
                forceCalculation:true,
                showtoolbar: true,//是否显示工具栏
                showinfobar: false,//是否显示顶部信息栏
                showsheetbar: true,//是否显示底部sheet按钮
                showtoolbarConfig:{
                    save:false,//保存
                    preview:false,
                    redo:true,
                    undo:true,
                    upload:true,
                    downloadpdf:false,
                    datasource:false,
                    pdfSetting:false,
                    history:true,
                    saveAs:false,
                    picture:false,
                    chart:true,
                    conditionalFormat: true, // '条件格式'
                    splitColumn: false, // '分列'
                    screenshot: false, // '截图'
                    protection:false, // '工作表保护'
                    print:false, // '打印'
                },
                cellRightClickConfig:{
                    matrix: false,
                    chart: false,
                    cellFormat: false, // 设置单元格格式
                    customs: [{
                        title: '改动记录',
                        onClick: this.showCellHistory
                    }]
                },
                hook: {
                    cellMousedown: this.cellMousedown,
                    cellEditBefore:this.cellEditBefore,
                    shareModeClick:this.shareModeClick,
                    shareModeEditForbidden:this.shareModeEditForbidden,
                    enterShareMode:this.enterShareMode,
                    exitShareMode:this.exitShareMode,
                    showSheetHisDialog:this.showSheetHisDialog,
                    closeSheetHisDialog:this.closeSheetHisDialog,
                    showCellHisDialog:this.showSheetHisDialog,
                    closeCellHisDialog:this.closeSheetHisDialog,
                    initShareModeHook:this.initShareModeHook,
                    userChanged:this.userChanged,
                    addAuthClick:this.addAuthClick,
                    viewAuthClick:this.viewAuthClick,
                    sheetActivateAfter: this.sheetActivateAfter,
                    copyPasteBefore:this.copyPasteBefore,
                    pasteHandlerBefore:this.pasteHandlerBefore,
                    loadDataAfter:this.loadDataAfter,
                    rangeAuthCheck:this.rangeAuthCheck,
                    changeReportAttr:this.changeReportAttr,
                    historyClick:this.historyClick,
                    uploadFileClick:this.uploadFileClick,
                    downloadClick:this.downloadClick,
                    dragEndBefore:this.dragEndBefore,
                    uploadAttachment:this.uploadAttachment,
                    viewAttachment:this.viewAttachment,
                    createVChart:this.createVChart,
                    editVChart:this.editVChart,
                    activeVChart:this.activeVChart,
                }
            },
            users:[],//当前查看文档的用户
            headerUsers:[],//header部分显示的用户，最多展示15个
            addAuthVisiable:false,
            addAuthForm:{
                userIds:[]
            },
            authUsers:[],
            authUsersMap:{},
            authTitle:"",
            sheetRangeAuth:{},
            rangeAxis:null,
            range:null,
            isCreator:false,
            creatorName:"",
            sheetAuthedCells:{},//sheet页已经授权的单元格
            authdialogVisible:false,
            authedRange:[],
            attrDisabled:false,//单元格属性是否禁用，没权限的情况下需要禁用，禁止操作
            authedRangeTitle:"",
            uploadType:"xlsx",
            defaultProps: {
              children: 'children',
              label: 'name'
            },
            defaultCheckedUsers:[],
            vchartShow:false,
            chartOptions:{},
            chartSettingShow:false,
        }
    },
    mounted() {
        this.getOnlineTplInfo();
        this.getUserstree();
        this.getUsers();
    },
    methods: {
        showCellHistory(clickEvent, event, params){
            var cellName = this.commonUtil.getColumnFromCoords(params.rowIndex,params.columnIndex);
            this.hisDialogTitle = "单元格"+cellName+"改动记录"
            this.hisdialogVisible = true;
            this.r = params.rowIndex;
            this.c = params.columnIndex;
            this.type = 2;
            this.getChangeHistory();
        },
        //获取修改记录
        //type=1 文档修改记录 type=2 单元格修改记录
        getChangeHistory(){
            const sheetIndex = luckysheet.getSheet().index;
            var params = {
                // rowCol:this.type==1?"":this.r+"_"+this.c,
                listId:this.$route.query.gridKey,
                sheetIndex:sheetIndex,
                type:this.type,
                currentPage:this.currentPage,
                pageSize:this.pageSize
            }
            if(this.type == 2)
            {
                params.rowNo = this.r;
                params.colNo = this.c
            }
            var obj = {
                params:params,
                removeEmpty:false,
            }
            obj.url = this.apis.luckysheetHis.listApi;
              this.commonUtil.doPost(obj) .then(response=>{
                if (response.code == "200")
                {
                  this.historyData = response.responseData.data;
                  this.total = Number(response.responseData.total);
                  const sheetIndex = luckysheet.getSheet().index;
                  let shareMode = luckysheet.getServerAttr("shareMode");
                  let shareUser = luckysheet.getServerAttr("shareUser");
                  if(shareMode && shareUser)
                  {
                    var obj = {
                        historyData:this.historyData,
                        total:this.total,
                        hisDialogTitle:this.hisDialogTitle,
                        currentPage:this.currentPage,
                        type:this.type
                    }
                    luckysheet.sendServerSocketMsg("shareModeOperation", sheetIndex,obj,{ "k": "showSheetHis"});
                  }
                }
              });
        },
        handleCurrentChange(val){
            this.currentPage = val;
            this.getChangeHistory();
        },
        cellMousedown(cell,postion,sheetFile,ctx){
          this.chartSettingShow = false;
            var r = postion.r;
            var c = postion.c;
            if(!this.hisDialogTitle)
            {
                return;
            }
            if(this.type == 1)
            {
                return;
            }
            if(r == this.r && c == this.c)
            {
                return;
            }
            this.r = r;this.c = c;
            var cellName = this.commonUtil.getColumnFromCoords(r,c);
            this.hisDialogTitle = "单元格"+cellName+"改动记录"
            this.getChangeHistory();
        },
        getSheetHis(){
            const name = luckysheet.getSheet().name;
            this.hisDialogTitle = "【"+name+"】改动记录"
            this.hisdialogVisible = true;
            this.type = 1;
            this.getChangeHistory();
        },
        closeModal(){
            this.hisdialogVisible = false;
            this.hisDialogTitle = "";
            this.historyData = [];
            this.r = 0;
            this.c = 0;
            this.type = 2;
            this.total = 0;
            this.currentPage = 1;
            const sheetIndex = luckysheet.getSheet().index;
            let shareMode = luckysheet.getServerAttr("shareMode");
            let shareUser = luckysheet.getServerAttr("shareUser");
            if(shareMode && shareUser)
            {
                var obj = {
                    historyData:this.historyData,
                    total:this.total,
                    hisDialogTitle:this.hisDialogTitle,
                    type:this.type
                }
                luckysheet.sendServerSocketMsg("shareModeOperation", sheetIndex,null,{ "k": "closeSheetHis"});
            }
        },
        // 引入Excel
        loadExcel(evt) {
            const files = evt.target.files
            if (files == null || files.length == 0) {
                alert('请选择文件')
                return
            }
            // 获取文件名
            const name = files[0].name
            // 获取文件后缀
            const suffixArr = name.split('.')
            const suffix = suffixArr[suffixArr.length - 1]
            if(this.uploadType == "xlsx"){
              if (suffix != 'xlsx') {
                this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.upload.filetype",['xlsx']), type: this.commonConstants.messageType.error })
                return
              }
            }else if(this.uploadType == "csv"){
              if (suffix != 'csv') {
                this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.upload.filetype",['csv']), type: this.commonConstants.messageType.error })
                return
              }
            }else{
              this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.upload.filetype",['xlsx/csv']), type: this.commonConstants.messageType.error })
              return;
            }
            this.loading = true;
            var that = this;
            const formData = new FormData();
            formData.append("file", files[0]);
            formData.append("gridKey", this.$route.query.gridKey);
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': localStorage.getItem(that.commonConstants.sessionItem.authorization),
                    'thirdPartyType':localStorage.getItem(that.commonConstants.sessionItem.thirdPartyType)
                }
            }
            try {
                Axios.post(that.apis.reportTpl.uploadExcelApi, formData, config)
                    .then(res => {
                        if (res.data.code == "200") {
                            let sheetDatas = res.data.responseData;
                            if (sheetDatas && sheetDatas.length > 0) {
                                for (let index = 0; index < sheetDatas.length; index++) {
                                    const element = sheetDatas[index];
                                    luckysheet.appendSheets(element,true);
                                }
                            }
                        } else {
                            that.commonUtil.showMessage({ message: res.data.message, type: res.data.msgLevel })
                        }
                        that.loading = false;
                        evt.target.value = ''
                    })
            } catch (error) {
                evt.target.value = ''
                that.loading = false;
            }
        },
       // 将celldatas转成map
    cellDatasToMap(cellDatas) {
        var result = {}
        for (let index = 0; index < cellDatas.length; index++) {
          const element = cellDatas[index]
          result[element.r + '-' + element.c] = element
        }
        return result
      },
      cellEditBefore(range){
        let shareMode = luckysheet.getServerAttr("shareMode");
        let shareUser = luckysheet.getServerAttr("shareUser");
        if(shareMode && !shareUser)
        {
          this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.sharemode.forbidden",null),type: this.commonConstants.messageType.warning})
          return false;
        }
        let row = range[0].row;
        let column = range[0].column;
        let checkResult = this.checkUserViewAuth(row[0],column[0]);
        if(!checkResult)
        {
          this.commonUtil.showMessage({ message: '暂无查看权限，如需编辑可联系文档创建者【'+this.creatorName+"】。", type: this.commonConstants.messageType.error })
          return false;
        }
        checkResult = this.checkUserEditAuth(row[0],column[0]);
        if(!checkResult)
        {
          this.commonUtil.showMessage({ message: '暂无编辑权限，如需编辑可联系文档创建者【'+this.creatorName+"】。", type: this.commonConstants.messageType.error })
          return false;
        }
      },
      shareModeClick(){
        var gridKey = this.$route.query.gridKey;
        let shareMode = luckysheet.getServerAttr("shareMode");
        let shareUser = luckysheet.getServerAttr("shareUser");
        if(!shareMode)
        {//进入共享模式
            let obj = {
                url:this.apis.coedit.beforeEnterShareModeApi,
                messageContent:this.commonUtil.getMessageFromList("confirm.enter.sharemode",null),
                callback:this.beforeEnterShareMode,
                params:{gridKey:gridKey},
                type:"post",
            }
            //弹出删除确认框
            this.commonUtil.showConfirm(obj)
        }else{
            if(shareUser)
            {
                let obj = {
                    messageContent:this.commonUtil.getMessageFromList("confirm.exit.sharemode",null),
                    callback:this.exitShareModeCallback,
                }
                //弹出删除确认框
                this.commonUtil.showConfirmMsg(obj)
            }else{
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("error.sharemode.enter",[this.shareUser]),type: this.commonConstants.messageType.warning})
            }
        }
      },
      exitShareModeCallback(){
        const sheetIndex = luckysheet.getSheet().index;
        luckysheet.setServerAttr("shareMode",false);
        luckysheet.setServerAttr("shareUser",false);
        luckysheet.sendServerSocketMsg("shareModeOperation", sheetIndex,null,{ "k": "exitShareMode"});
        this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("info.exit.sharemode",null),type: this.commonConstants.messageType.success})
      },
      beforeEnterShareMode(response){
        if(response.code == '200')
        {
            luckysheet.hideUsername();
            luckysheet.setServerAttr("shareMode",true);
            luckysheet.setServerAttr("shareUser",true);
            const sheetIndex = luckysheet.getSheet().index;
            luckysheet.sendServerSocketMsg("shareModeOperation", sheetIndex,null,{ "k": "enterShareMode"});
        }
      },
      shareModeEditForbidden(){
        this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.sharemode.forbidden",null),type: this.commonConstants.messageType.warning})
      },
      //进入共享模式
      enterShareMode(data){
        luckysheet.hideUsername();
        luckysheet.setServerAttr("shareMode",true);
        let userId = data.userId;
        let userName = data.userName;
        let localUserId = localStorage.getItem(this.commonConstants.sessionItem.userId)+"";
        if(userId == localUserId)
        {
            luckysheet.setServerAttr("shareUser",true);
        }else{
            //退出编辑模式
            luckysheet.exitEditMode();
        }
        let index = data.i;
        const sheetIndex = luckysheet.getSheet().index;
        if(index != sheetIndex)
        {//如果不是同一个sheet页，则切换到跟共享用户同一个sheet页
            luckysheet.changeSheetExec(index);
        }
        if(userId != localUserId){
            this.shareUser = userName;
            this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.enter.sharemode",[userName]),type: this.commonConstants.messageType.warning})
        }else{
            this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("info.enter.sharemode",null),type: this.commonConstants.messageType.success})
        }
      },
      //退出共享模式
      exitShareMode(data)
      {
        luckysheet.setServerAttr("shareMode",false);
        luckysheet.setServerAttr("shareUser",false);
        this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("info.exit.sharemode",null),type: this.commonConstants.messageType.success})
      },
      showSheetHisDialog(data)
      {
        const sheetIndex = luckysheet.getSheet().index;
        let index = data.i;
        if(index == sheetIndex)
        {
            this.hisdialogVisible = true;
            this.hisDialogTitle = data.v.hisDialogTitle
            this.historyData = data.v.historyData;
            this.currentPage = data.v.currentPage;
            this.total = Number(data.v.total),
            this.type = 1
        }
      },
      closeSheetHisDialog(data){
        const sheetIndex = luckysheet.getSheet().index;
        let index = data.i;
        if(index == sheetIndex){
            this.hisdialogVisible = false;
            this.hisDialogTitle = "";
            this.historyData = [];
            this.r = 0;
            this.c = 0;
            this.type = 2;
            this.total = 0;
            this.currentPage = 1;
        }
      },
      initShareModeHook(shareMode,shareUser,shareUserName){
        if(shareMode)
        {
            if(shareUser)
            {
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("info.enter.sharemode",null),type: this.commonConstants.messageType.success})
            }else{
                this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.enter.sharemode",[shareUserName]),type: this.commonConstants.messageType.warning})
            }
        }
      },
      userChanged(data){
        this.users = data.v
        if(this.users && this.users.length > 15)
        {
            this.headerUsers = this.users.slice(0,15)
        }else{
            this.headerUsers = this.users;
        }
      },
      //获取协同文档信息
      getOnlineTplInfo(){
        var obj = {
            params:{listId:this.$route.query.gridKey},
            removeEmpty:false,
            url:this.apis.onlineTpl.getOnlineTplInfoApi,
        }
        var that = this;
          this.commonUtil.doPost(obj) .then(response=>{
            if (response.code == "200")
            {
              document.title = response.responseData.tplName;
              that.isCreator = response.responseData.creator;
              that.isThirdParty = response.responseData.isThirdParty;
              that.creatorName = response.responseData.creatorName;
              that.sheetRangeAuth = response.responseData.sheetRangeAuth;
              var gridKey = that.$route.query.gridKey;
              var isLoadALL = that.$route.query.isLoadALL;
              that.options.gridKey = gridKey;
              that.options.updateUrl = location.protocol === 'https:' ? 'wss'+"://"+ location.host +"/SpringReport/api/coedit/websocket/luckysheet" : 'ws'+"://"+ location.host +"/SpringReport/api/coedit/websocket/luckysheet";
              that.options.loadUrl = location.origin + "/SpringReport/api/coedit/load";
              that.options.loadSheetUrl = location.origin + "/SpringReport/api/coedit/loadsheet";
              that.options.uploadImage = that.commonUtil.uploadImage;
              that.options.isLoadALL = isLoadALL;
              if(!that.isCreator)
              {
                that.options.cellRightClickConfig.insertRow = false;
                that.options.cellRightClickConfig.insertColumn = false;
                that.options.cellRightClickConfig.deleteRow = false;
                that.options.cellRightClickConfig.deleteColumn = false;
                that.options.cellRightClickConfig.deleteCell = false;
              }
              luckysheet.setServerAttr("version","vue3");
              luckysheet.create(that.options);
            }
          });
      },
      getUserstree(){
        const obj = {
          url: this.apis.sysUser.getDeptUserTreeApi,
          params: {},
          removeEmpty: false
        }
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            this.authUsers = response.responseData
          }
        })
      },
      getUsers(){
        const obj = {
          url: this.apis.onlineTpl.getRangeUsersApi,
          params: {},
          removeEmpty: false
        }
        var that = this;
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            let authUsers = response.responseData;
            for (let index = 0; index < authUsers.length; index++) {
              let element = authUsers[index];
              that.authUsersMap[element.id] = element;
            }
          }
        })
      },
      addAuthClick(){
        if(this.isThirdParty == 1){
          this.commonUtil.showMessage({ message: '第三方iframe调用暂不支持该功能！', type: this.commonConstants.messageType.error })
          return;
        }
        let rangeAxis = luckysheet.getRangeAxis();
        if(!rangeAxis || rangeAxis.length == 0)
        {
          this.commonUtil.showMessage({ message: '请先选择要设置的区域。', type: this.commonConstants.messageType.error })
          return;
        }else if(rangeAxis.length > 1)
        {
          this.commonUtil.showMessage({ message: '不能对多个选区执行此操作，请选择单个区域进行操作。', type: this.commonConstants.messageType.error })
          return;
        }
        if(!this.isCreator)
        {
          this.commonUtil.showMessage({ message: '您没有权限进行此操作，只有创建者【'+this.creatorName+'】才有权限进行操作。', type: this.commonConstants.messageType.error })
          return;
        }
        this.rangeAxis = rangeAxis[0];
        const sheetIndex = luckysheet.getSheet().index;
        if(!this.sheetRangeAuth[sheetIndex]){
          this.sheetRangeAuth[sheetIndex] = {};
        }
        let rangeAuth = this.sheetRangeAuth[sheetIndex];
        if(rangeAuth[this.rangeAxis])
        {
          this.commonUtil.showMessage({ message: '该选区已经设置权限，请勿重复设置。', type: this.commonConstants.messageType.error })
          return;
        }
        this.range = luckysheet.getRange()[0];
        this.authTitle = "为选区【"+rangeAxis[0]+"】添加保护权限";
        this.addAuthVisiable = true;
      },
      closeAddAuth(){
        this.addAuthVisiable = false;
        this.addAuthForm.userIds = [];
        this.rangeAxis = null;
        this.range = null;
        for(var key in this.authUsersMap) {
          this.authUsersMap[key].authType = 1;
        }
        this.$refs.tree.setCheckedKeys([]);
        this.defaultCheckedUsers = [];
        this.resetUserAuthType(this.authUsers,1);
      },
      confirmAddAuth(){
        let checkedKeys = this.$refs.tree.getCheckedKeys()
        if(checkedKeys && checkedKeys.length > 0)
        {
          const sheetIndex = luckysheet.getSheet().index;
          if(!this.sheetRangeAuth[sheetIndex]){
            this.sheetRangeAuth[sheetIndex] = {};
          }
          let userIds = [];
          for (let index = 0; index < checkedKeys.length; index++) {
            const element = checkedKeys[index];
            if(element.indexOf("_dept")<0){
              userIds.push(element);
            }
          }
          let rangeAuth = this.sheetRangeAuth[sheetIndex];
          let userAuthType = {};
          let rangeAxis = this.rangeAxis;
          rangeAuth[this.rangeAxis] = {
            rangeAxis:this.rangeAxis,
            range:this.range,
            sheetIndex:sheetIndex,
            userIds:userIds,
            userAuthType:userAuthType
          }
          for (let index = 0; index < userIds.length; index++) {
            const element = userIds[index];
            if(this.authUsersMap[element]){
              userAuthType[element] = this.authUsersMap[element].authType
            }
          }
          this.updateRangeAuth(rangeAuth[this.rangeAxis]);
          this.authRangeToArray();
          this.closeAddAuth();
          if(this.isCreator){
            luckysheet.sendServerSocketMsg("reportDesign", sheetIndex,{"rangeAxis":rangeAxis,"type":"add"},{ "k": 'refreshAuth'});
          }
        }else{
          this.commonUtil.showMessage({ message: '请添加授权用户。', type: this.commonConstants.messageType.error })
        }
      },
      authRangeToArray(){
        const sheetIndex = luckysheet.getSheet().index;
        this.authedRange = [];
        if(this.sheetRangeAuth)
        {
          if(this.sheetRangeAuth[sheetIndex]){
            for(var key in this.sheetRangeAuth[sheetIndex]) {
              this.authedRange.push(this.sheetRangeAuth[sheetIndex][key])
            }
          }
        }
      },
      updateRangeAuth(rangeAuth){
        var obj = {
            params:{listId:this.$route.query.gridKey,rangeAuth:rangeAuth},
            removeEmpty:false,
            url:this.apis.onlineTpl.rangeAuthApi,
        }
        var that = this;
          this.commonUtil.doPost(obj) .then(response=>{
            if (response.code == "200")
            {
              
            }
          });
      },
      viewAuthClick(){
        if(this.isThirdParty == 1){
          this.commonUtil.showMessage({ message: '第三方iframe调用暂不支持该功能！', type: this.commonConstants.messageType.error })
          return;
        }
        this.authRangeToArray();
        if(this.isCreator)
        {
          this.authedRangeTitle = "保护范围";
        }else{
          this.authedRangeTitle = "可操作范围";
        }
        this.authdialogVisible = true;
      },
      editRange(range){
        this.rangeAxis = range.rangeAxis;
        this.range = range.range
        this.authTitle = "修改选区【"+range.rangeAxis+"】权限";
        this.addAuthForm.userIds = range.userIds;
        let userAuth = range.userAuth;
        this.defaultCheckedUsers = range.userIds;
        this.addAuthVisiable = true;
        for (let index = 0; index < range.userIds.length; index++) {
          const element = range.userIds[index];
          if(this.authUsersMap[element] && userAuth[element]){
            this.authUsersMap[element].authType = userAuth[element]
          }
        }
        this.resetUserAuthType(this.authUsers,2);
      },
      deleteRange(range,index){
        this.authedRange.splice(index, 1);
        const sheetIndex = luckysheet.getSheet().index;
        if(this.sheetRangeAuth){
          if(this.sheetRangeAuth[sheetIndex]){
            this.submitDeleteRangeAuth(range)
            delete this.sheetRangeAuth[sheetIndex][range.rangeAxis]
          }
        }
      },
      showRange(range){
        luckysheet.addLuckysheetAuthRange([range.range])
      },
      closeAuthDialog(){
        this.authdialogVisible = false;
        this.authedRange = [];
        if(this.isCreator)
        {
          luckysheet.addLuckysheetAuthRange(null)
        }
      },
      submitDeleteRangeAuth(range){
        var obj = {
            params:{listId:this.$route.query.gridKey,rangeAuth:range},
            removeEmpty:false,
            url:this.apis.onlineTpl.deleteRangeAuthApi,
        }
        let sheetIndex = range.sheetIndex;
        let rangeAxis = range.rangeAxis
        this.commonUtil.doPost(obj) .then(response=>{
          if (response.code == "200")
          {
            luckysheet.sendServerSocketMsg("reportDesign", sheetIndex,{"rangeAxis":rangeAxis,"type":"del","range":JSON.parse(JSON.stringify(range.range))},{ "k": 'refreshAuth'});
          }
        });
      },
      sheetActivateAfter(index){
        this.addAuthVisiable = false;
        this.authdialogVisible = false;
        if(!this.isCreator)
        {
          this.showSheetAuthedRanges(index);
        }else{
          luckysheet.addLuckysheetAuthRange(null);
        }
        this.showAuthInfoMsg();
      },
      checkUserEditAuth(r,c){
        let result = false;
        if(this.isCreator)
        {
          return true;
        }else{
          const sheetIndex = luckysheet.getSheet().index;
          if(!this.sheetRangeAuth[sheetIndex]){
            return false;
          }else{
            let rangeAuth = this.sheetRangeAuth[sheetIndex];
            for(var key in rangeAuth) {
              let authType = rangeAuth[key].authType;
              if(authType == 1){
                let range = rangeAuth[key].range;
                let row = range.row;
                let column = range.column;
                let str = row[0]; let edr = row[1];
                let stc = column[0]; let edc = column[1];
                if(r >= str && r <= edr && c >= stc && c <= edc)
                {
                  return true;
                }
              }
            }
          }
          return result;
        }
      },
      checkUserViewAuth(r,c){
        let result = true;
        if(this.isCreator)
        {
          return true;
        }else{
          const sheetIndex = luckysheet.getSheet().index;
          if(!this.sheetRangeAuth[sheetIndex]){
            return true;
          }else{
            let rangeAuth = this.sheetRangeAuth[sheetIndex];
            for(var key in rangeAuth) {
              let authType = rangeAuth[key].authType;
              if(authType == 2){
                let range = rangeAuth[key].range;
                let row = range.row;
                let column = range.column;
                let str = row[0]; let edr = row[1];
                let stc = column[0]; let edc = column[1];
                if(r >= str && r <= edr && c >= stc && c <= edc)
                {
                  return false;
                }
              }
            }
          }
          return result;
        }
      },
      copyPasteBefore(range,copyRange){
        let shareMode = luckysheet.getServerAttr("shareMode");
        let shareUser = luckysheet.getServerAttr("shareUser");
        if(shareMode && !shareUser){
          this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.sharemode.forbidden",null),type: this.commonConstants.messageType.warning})
          return false;
        }
        if(this.isCreator)
        {
          return true;
        }else{
          let r = range[0].row[0]
          let c = range[0].column[0]
          let copyRowColSpan = this.getCopyRangeRowColSpan(copyRange.copyRange);
          let checkResult = this.checkPasteRange(r,c,copyRowColSpan[0],copyRowColSpan[1]);
          if(!checkResult)
          {
            this.commonUtil.showMessage({ message: '粘贴部分包含暂无编辑权限的单元格，如需编辑可联系创建者【'+this.creatorName+"】。", type: this.commonConstants.messageType.error })
            return false;
          }
          return checkResult;
        }
      },
      checkPasteRange(r,c,rowspan,colspan){
          const sheetIndex = luckysheet.getSheet().index;
          if(!this.sheetRangeAuth[sheetIndex]){
            return false;
          }else{
            let rangeAuth = this.sheetRangeAuth[sheetIndex];
            let authedCells = this.getAuthedCells(rangeAuth);
            let pastRangeCells = this.getRangeCells(r,c,rowspan,colspan);
            if(authedCells == null ||  Object.keys(authedCells).length === 0){
              return false;
            }else{
              for(var key in pastRangeCells) {
                if(!(key in authedCells)){
                  return false;
                }
              }
            }
            return true;
          }
      },
      //获取所有已授权的单元格
      getAuthedCells(rangeAuth){
        const sheetIndex = luckysheet.getSheet().index;
        let cells = this.sheetAuthedCells[sheetIndex];
        if(cells == null)
        {
          cells = {};
          for(var key in rangeAuth) {
            let authType = rangeAuth[key].authType;
            if(authType == 1){
              let range = rangeAuth[key].range;
              let row = range.row;
              let column = range.column;
              let str = row[0]; let edr = row[1];
              let stc = column[0]; let edc = column[1];
              for (let i = str; i <= edr; i++) {
                for (let t = stc; t <= edc; t++) {
                  cells[i+"_"+t] = "1";
                }
              }
            }
          }
          this.sheetAuthedCells[sheetIndex] = cells;
        }
        return cells;
      },
      getRangeCells(r,c,rowspan,colspan){
        let cells = {};
        for (let i = 0; i <rowspan; i++) {
          for (let t = 0; t < colspan; t++) {
            cells[(r+i)+"_"+(c+t)] = "1";
          }
        }
        return cells;
      },
      getCopyRangeRowColSpan(copyRange){
        let rowSpan = 0;
        let colSpan = 0;
        if(copyRange.length > 1)
        {
          let isSameRow = true;
          let str_r = copyRange[0].row[0];
          let end_r = copyRange[0].row[1];
          for(let s = 1; s < copyRange.length; s++){
            if(copyRange[s].row[0] != str_r || copyRange[s].row[1] != end_r){
              isSameRow = false;
              break;
            }
          }
          if(isSameRow){
            rowSpan = copyRange[0].row[1] - copyRange[0].row[0] + 1;
          }else{
            colSpan = copyRange[0].column[1] - copyRange[0].column[0] + 1;
          }
          for (let index = 0; index < copyRange.length; index++) {
            const element = copyRange[index];
            let row = element.row;
            let column = element.column;
            let rs = row[1]-row[0]+1;
            let cs = column[1] - column[0] + 1;
            if(isSameRow){
              colSpan = colSpan + cs;
            }else{
              rowSpan = rowSpan + rs;
            }
          }
        }else{
          let row = copyRange[0].row;
          let column = copyRange[0].column;
          rowSpan = row[1]-row[0]+1;
          colSpan = column[1] - column[0] + 1;
        }
        let result = [rowSpan,colSpan]
        return result;
      },
      pasteHandlerBefore(range,data){
        let shareMode = luckysheet.getServerAttr("shareMode");
        let shareUser = luckysheet.getServerAttr("shareUser");
        if(shareMode && !shareUser){
          this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.sharemode.forbidden",null),type: this.commonConstants.messageType.warning})
          return false;
        }
        if(this.isCreator)
        {
          return true;
        }else{
          let r = range[0].row[0]
          let c = range[0].column[0]
          let rs = data.length
          let cs = data[0].length
          let checkResult = this.checkPasteRange(r,c,rs,cs);
          if(!checkResult)
          {
            this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.auth.operate",[this.creatorName]), type: this.commonConstants.messageType.error })
            return false;
          }
          return checkResult;
        }
      },
      rangeAuthCheck(range){
        if(this.isCreator){
          return true;
        }else{
          let checkResult = this.checkRangeAuth(range);
          if(!checkResult)
          {
            this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("error.auth.operate",[this.creatorName]), type: this.commonConstants.messageType.error })
          }
          return checkResult;
        }
      },
      checkRangeAuth(range){
        if(this.isCreator)
        {
         return true;
        }
        const sheetIndex = luckysheet.getSheet().index;
        let rangeAuth = this.sheetRangeAuth[sheetIndex];
        let authedCells = this.getAuthedCells(rangeAuth);
        let rangeCells = {};
        for (let index = 0; index < range.length; index++) {
          const element = range[index];
          let r = element.row[0];
          let c = element.column[0];
          let rs = element.row[1] - element.row[0] + 1;
          let cs = element.column[1] - element.column[0] + 1;
          let cells = this.getRangeCells(r,c,rs,cs);
          rangeCells = {...rangeCells,...cells}
        }
        if(authedCells == null ||  Object.keys(authedCells).length === 0){
          return false;
        }else{
          for(var key in rangeCells) {
            if(!(key in authedCells)){
              return false;
            }
          }
          return true;
        }
    },
    showSheetAuthedRanges(sheetIndex){
      if(this.sheetRangeAuth)
      {
        if(this.sheetRangeAuth[sheetIndex])
        {
          let range = [];
          let noauthrange = [];
          for(var key in this.sheetRangeAuth[sheetIndex]) {
            let authType = this.sheetRangeAuth[sheetIndex][key].authType;
            if(authType == 1){
              range.push(this.sheetRangeAuth[sheetIndex][key].range)
            }else{
              noauthrange.push(this.sheetRangeAuth[sheetIndex][key].range)
            }
          }
          if(noauthrange.length > 0){
            luckysheet.addLuckysheetNoAuthRange(noauthrange);
          }else{
            luckysheet.addLuckysheetNoAuthRange(null);
          }
          if(range.length > 0){
            luckysheet.addLuckysheetAuthRange(range);
          }else{
            luckysheet.addLuckysheetAuthRange(null);
          }
        }else{
          luckysheet.addLuckysheetAuthRange(null);
          luckysheet.addLuckysheetNoAuthRange(null);
        }
      }else{
        luckysheet.addLuckysheetAuthRange(null);
        luckysheet.addLuckysheetNoAuthRange(null);
      }
      },
      loadDataAfter(){
        if(!this.isCreator)
        {
            const sheetIndex = luckysheet.getSheet().index;
            this.showSheetAuthedRanges(sheetIndex);
        }
        this.showAuthInfoMsg();
      },
      showAuthInfoMsg(){
        let shareMode = luckysheet.getServerAttr("shareMode");
        if(!this.isCreator && !shareMode)
        {
            const sheetIndex = luckysheet.getSheet().index;
            if(this.sheetRangeAuth)
            {
              if(this.sheetRangeAuth[sheetIndex])
              {
                let flag = false;
                for(var key in this.sheetRangeAuth[sheetIndex]) {
                  let authType = this.sheetRangeAuth[sheetIndex][key].authType;
                  if(authType == 1){
                    flag = true;
                    break;
                  }
                }
                if(flag){
                  this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("warning.auth.operate",[this.creatorName]), type: this.commonConstants.messageType.warning })
                }else{
                  this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("warning.auth.nooperate",[this.creatorName]), type: this.commonConstants.messageType.warning })  
                }
              }else{
                this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("warning.auth.nooperate",[this.creatorName]), type: this.commonConstants.messageType.warning })
              }
            }else{
              this.commonUtil.showMessage({ message: this.commonUtil.getMessageFromList("warning.auth.nooperate",[this.creatorName]), type: this.commonConstants.messageType.warning })
            }
        }
      },
      changeReportAttr(data){
        let k = data.k;
        let sheetIndex = data.i;
        const currentIndex = luckysheet.getSheet().index;
        let v = data.v;
        let cells = v.cells
        let value = v.value;
        if("refreshAuth" == k){
          //更新权限
          if(!this.isCreator){
            let type = v.type;
            let rangeAxis = v.rangeAxis
            let range = v.range
            if("del" == type){
              //删除授权操作
              if(this.sheetRangeAuth[sheetIndex]){
                let sheetRangeAuth = this.sheetRangeAuth[sheetIndex];
                if(sheetRangeAuth[rangeAxis]){
                  let authType = sheetRangeAuth[rangeAxis].authType;
                  if(authType == 2){
                    let param = {
                      rangeAxis:rangeAxis,
                      sheetIndex:sheetIndex,
                      listId:this.$route.query.gridKey,
                      range:range
                    }
                    this.getRangeValues(param);
                  }
                }
              }
            }
            this.getTplAuth();
          }
        }
      },
      //获取范围内的数据
      getRangeValues(param){
        const obj = {
          url: this.apis.coedit.getRangeValuesApi,
          params: param,
          removeEmpty: false
        }
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            if(response.responseData && response.responseData.length > 0){
              let sheetIndex = param.sheetIndex;
              var luckysheetfiles = luckysheet.getLuckysheetfile();
              let filedata = null;
              if(luckysheetfiles){
                for (let index = 0; index < luckysheetfiles.length; index++) {
                  const element = luckysheetfiles[index];
                  if(element.index == sheetIndex){
                    filedata = element.data;
                    break;
                  }
                }
                if(filedata){
                  try {
                    for (let index = 0; index < response.responseData.length; index++) {
                      const celldata = response.responseData[index];
                      if(celldata != null){
                        let r = celldata.r;
                        let c = celldata.c
                        filedata[r][c] = celldata.v;
                      }
                    }
                  } catch (error) {
                    
                  }
                }
                luckysheet.refresh();
              }
            }
          }
        })
      },
      //获取使用者授权范围
      getTplAuth(){
        const sheetIndex = luckysheet.getSheet().index;
        const listId = this.$route.query.gridKey// reportTplId
        const obj = {
          url: this.apis.onlineTpl.getCoeditAuthApi,
          params: { listId: listId },
          removeEmpty: false
        }
        this.sheetAuthedCells = {};
        this.authedRange = [];
        // luckysheet.addLuckysheetAuthRange();
        var that = this;
        this.commonUtil.doPost(obj).then(response => {
          if (response.code == '200') {
            that.sheetRangeAuth = response.responseData
            that.authRangeToArray();
            that.showSheetAuthedRanges(sheetIndex);
          }
        })
      },
      historyClick(){
        this.getSheetHis();
      },
      uploadFileClick(type){
        this.uploadType = type;
        $("#uploadBtn").click() //触发父容器中的保存模板按钮事件
      },
      downloadClick(){
        //下载excel
        this.commonUtil.downloadFile(this.apis.coedit.downLoadExcelApi,{gridKey:this.$route.query.gridKey},null,{})
      },
      dragEndBefore(rows,cols){
        let shareMode = luckysheet.getServerAttr("shareMode");
        let shareUser = luckysheet.getServerAttr("shareUser");
        if(shareMode && !shareUser){
          this.commonUtil.showMessage({message:this.commonUtil.getMessageFromList("warning.sharemode.forbidden",null),type: this.commonConstants.messageType.warning})
          return false;
        }
        if(this.isCreator)
        {
          return true;
        }else{
          let str = rows[0]
          let edr = rows[1]
          let stc = cols[0]
          let edc = cols[1]
          let checkResult = this.checkPasteRange(str,stc,edr-str+1,edc-stc+1);
          if(!checkResult)
          {
            this.commonUtil.showMessage({ message: '操作部分包含暂无编辑权限的单元格，如需编辑可联系创建者【'+this.creatorName+"】。", type: this.commonConstants.messageType.error })
            return false;
          }
          return checkResult;
        }
      },
      uploadAttachment(){
        let rangeAxis = luckysheet.getRangeAxis();
        if(!rangeAxis || rangeAxis.length == 0)
        {
          this.commonUtil.showMessage({ message: '请先选择单元格。', type: this.commonConstants.messageType.error })
          return;
        }
        $("#uploadAttachmentBtn").click() //触发父容器中的保存模板按钮事件
      },
      viewAttachment(item,r,c){
        let fileType = this.commonUtil.getFileExt(item.linkAddress);
        if(fileType){
          if(this.commonConstants.attachPreviewExt.includes(fileType)){
            let viewReport = this.$router.resolve({ name:"attachment",query: {url:item.linkAddress,name:item.fileName,fileType:fileType,'thirdPartyType':localStorage.getItem(this.commonConstants.sessionItem.thirdPartyType)}});
            window.open(viewReport.href, '_blank');
          }else{
            window.open(item.linkAddress, '_blank');
          }
        }else{
          window.open(item.linkAddress, '_blank');
        }
      },
      changeAttachment(evt){
        this.loading = true;
        let obj = {
          url:this.apis.common.uploadFileApi,
          callback:this.doPostCallback
        }
        const files = evt.target.files
        if (files == null || files.length == 0) {
          alert('请选择文件')
          return
        }
        // 获取文件名
        this.commonUtil.uploadFile(files[0],obj).then(response=>{
          if (response.code == "200")
          {
            let range = luckysheet.getRange()[0];
            let r = range.row[0];
            let c = range.column[0];
            let luckysheetfile = luckysheet.getSheet();
            let cell = luckysheetfile.data[r][c];
            if(cell == null){
                cell = {};
            }
            cell.fc = 'rgb(0, 0, 255)';
            cell.un = 1;
            cell.v = cell.m = "附件:"+response.responseData.fileName;
            let item = {
                linkType: "attachment",
                linkAddress: response.responseData.fileUri,
                linkTooltip: "",
                fileName:response.responseData.fileName
            }
            if(luckysheetfile.hyperlink == null){
              luckysheetfile.hyperlink = {};
            }
            luckysheetfile.hyperlink[r + "_" + c] = item;
            luckysheetfile.data[r][c] = cell;
            luckysheet.refresh({},true,"上传附件："+response.responseData.fileName+"，附件地址："+response.responseData.fileUri);
          }
        });;
        evt.target.value = ''
      },
      doPostCallback(){
        this.loading = false;
      },
      changeAuthType(data){
        this.authUsersMap[data.id]['authType'] = data.authType;
       },
       resetUserAuthType(authUsers,type){
         for (let index = 0; index < authUsers.length; index++) {
           let element = authUsers[index];
           if(element.id.indexOf('dept')>=0){
             if(element.children && element.children.length > 0){
               this.resetUserAuthType(element.children,type);
             }
           }else{
             if(type == 1){
               element.authType = 1;
             }else{
               element.authType = this.authUsersMap[element.id].authType
             }
           }
         }
       },
       createVChart(){
         // luckysheet.createChart("echarts|column|default");
         this.vchartShow = true;
       },
       closeAddChartModal(){
         this.vchartShow = false;
       },
       editVChart(chartOptions){
         this.chartSettingShow = true;
         this.chartOptions = chartOptions;
       },
       activeVChart(chartOptions){
         if(this.chartSettingShow){
           this.chartOptions = chartOptions;
         }
       }
    }
}
