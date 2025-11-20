<template>
   <div style="width: 100%;flex: 1;overflow: auto;height:100vh">
            <div id="luckysheet" style="margin:0px;padding:0px;width:100%;height:100%;left: 0px;top: 50px"></div>
        </div>
</template>
<script>
export default {
    components: {
    },
    props:{
        url:{
            type:String,
            default:''
        },
        fileType:{
            type:String,
            default:'xlsx'
        },
        fileName:{
            type:String,
            default:'sheet1'
        }
    },
    data() {
        return {
            options:{
                container: "luckysheet",
                data:[],
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
                allowUpdate:false,
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
                    upload:false,
                    download:false,
                    downloadpdf:false,
                    datasource:false,
                    pdfSetting:false,
                    history:false,
                    attachment:false,
                    shareMode:false,
                    saveAs:false,
                    editAuth:false,
                    xxbt:false,
                    picture:false,
                    chart:true,
                    conditionalFormat: true, // '条件格式'
                    splitColumn: false, // '分列'
                    screenshot: false, // '截图'
                    protection:false, // '工作表保护'
                    print:false, // '打印'
                },
                cellRightClickConfig:{
                },
                hook: {
                }
            },
        }
    },
    mounted(){
        this.parseXlsxByUrl();
    },
    methods: {
        parseXlsxByUrl(){
            var obj = {
                params:{url:this.url,fileName:this.fileName,fileType:this.fileType},
                removeEmpty:false,
                url:this.apis.common.parseXlsxByUrlApi,
            }
            var that = this;
            this.commonUtil.doPost(obj) .then(response=>{
                if (response.code == "200")
                {
                    console.log(response)
                    that.options.data = [];
                    for (let index = 0; index < response.responseData.length; index++) {
                        that.options.data.push(response.responseData[index]);
                    }
                    luckysheet.create(that.options);
                }
            });
        }
    }
}
</script>