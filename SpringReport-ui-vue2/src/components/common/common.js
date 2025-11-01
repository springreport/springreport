/*
 * @Description: 通用工具类
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-04 08:36:18
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-23 08:08:24
 */
import Vue from 'vue'
import { Message,MessageBox } from 'element-ui'
import MSG_LIST from './message'
import Axios from 'axios'
import moment from 'moment'
import commonConstants from './constants'
import router from '../../router'
import * as echarts from 'echarts';
import $ from 'jquery'
import VChart from '@visactor/vchart';
import screenConstants from './screenConstants'

const commonUtil = {

}
commonUtil.baseUrl="/SpringReport";

// commonUtil.codeValues={};//报错所有数据字典值，数据字典值表的id为key，valueName为值

/**
 * @description:消息展示
 * @param  showClose 是否显示关闭按钮 boolean message 消息内容 type 消息类型 success/warning/info/error duration 显示时间, 毫秒。设为 0 则不会自动关闭
 * @return:
 * @author: caiyang
 */
commonUtil.showMessage = function(obj)
{
    Message({
        showClose: (obj.showClose == undefined)?true:obj.showClose,
        message: obj.message,
        type: (obj.type == undefined)?'success':obj.type,
        duration: (obj.duration == undefined)?3000:obj.duration
    })
}

/**
 * @description: 获取错误消息
 * @param msgid 消息的key
 * @param msgParams 消息参数
 * @return:
 * @author: caiyang
 */
commonUtil.getMessageFromList = function(msgid,msgParams)
{
    let msg = MSG_LIST[msgid];
    if ($.trim(msg) == "") {
        msg = msgid + "[该消息不存在。]";
        return msg;
    }
    if (msgParams != null) {
        for (var i=0; i<msgParams.length; i++) {
            msg = msg.replace("{" + i + "}", msgParams[i]);
        }
    }
    return msg;
}

//表格日期格式化
commonUtil.formatTableDate = function(row, column)
{
    var date = row[column.property];
    if (date == undefined) {
      return "";
    }
    return commonUtil.formatDate(date);
}
//日期格式化
commonUtil.formatDate = function(val)
{
   return moment(val).format("YYYY-MM-DD HH:mm:ss");
}
//日期格式化
commonUtil.formatDate = function(val,format)
{
   return moment(val).format(format);
}

// 0 相等 >0 结束日期大于开始日期 <0 结束日期小于开始日期
commonUtil.compareDate = function(startDate,endDate,dateFormat){
    const startTime= moment(startDate,dateFormat.replace("yyyy","YYYY").replace("dd","DD"));
    const endTime = moment(endDate,dateFormat.replace("yyyy","YYYY").replace("dd","DD"));
    const diff = moment(endTime).diff(moment(startTime), 'seconds')
    return diff;
}

//获取当前时间
commonUtil.getCurrentDate = function(obj)
{

      switch (obj.format) {
          case '1':
            return moment().format("YYYY-MM-DD HH:mm:ss");
          case '2':
            return moment().format("YYYY-MM-DD HH:mm");
          case '3':
            return moment().format("YYYY-MM-DD");
          case '4':
            return moment().format("YYYY-MM");
          case '5':
            return moment().format("HH:mm:ss");
          case '6':
            return moment().format("HH:mm");
          case '7':
            return commonUtil.getWeek();
          case '8':
            return moment().format("YYYY");
          case '9':
                return moment().format("MM");
          case '10':
                return moment().format("DD");
          default:
              break;
      }
}
commonUtil.getWeek = function(){
    let week = moment().day()
      switch (week) {
        case 1:
          return '星期一'
        case 2:
          return '星期二'
        case 3:
          return '星期三'
        case 4:
          return '星期四'
        case 5:
          return '星期五'
        case 6:
          return '星期六'
        case 0:
          return '星期日'
      }
}

/**
 * @description: 获取表格数据
 * @param url:后台请求地址
 * @param param:后台请求参数
 * @return:
 * @author: caiyang
 */
commonUtil.getTableList = function(obj,headers){
    obj.removeEmpty = false;
    return commonUtil.doPost(obj,headers);
}
/**
 * @description: 共通表格赋值
 * @param response:请求返回数据
 * @param tablePage:表格分页信息
 * @param tableData:表格数据
 * @return:
 * @author: caiyang
 */
commonUtil.tableAssignment = function(response,tablePage,tableData){
    if (response.code == "200")
    {
        var responseData = response.responseData;
        tableData.splice(0)
        if(responseData.data&&responseData.data.length > 0)
        {
            for (let index = 0; index < responseData.data.length; index++) {
                tableData.push(responseData.data[index])
            }
        }
        tablePage.pageTotal = Number(responseData.total);
    }
}
/**
 * @description: 显示modal
 * @param show: true or false,弹框是否显示
 * @param type 类型 1新增 2编辑 3详情
 * @return:
 * @author: caiyang
 */
commonUtil.showModal = function(modalConfig,type){
    modalConfig.show = true;
    modalConfig.type = type;
    if(type == commonConstants.modalType.insert)
    {
        modalConfig.title = commonConstants.modalTitle.insert
    }else if(type == commonConstants.modalType.update)
    {
         modalConfig.title = commonConstants.modalTitle.update
    }else if(type == commonConstants.modalType.detail)
    {
        modalConfig.title = commonConstants.modalTitle.detail
    }
}
/**
 * @description: 异步post请求
 * @param url 请求后台的url
 * @param params 参数 map格式
 * @param removeEmpty 空字符串是否作为参数传递到后台 false:空字符串将传递到后台 true:空字符串将不会传递到后台，后台对应的值为null
 * @return:
 * @author: caiyang
 */
commonUtil.doPost = function(obj,headers)
{
    //将删除标志 创建人更新人以及时间移除
    delete obj.params['delFlag']
    delete obj.params['creator']
    delete obj.params['createTime']
    var isSystemMerchant = localStorage.getItem(commonConstants.sessionItem.isSystemMerchant);
    if(isSystemMerchant == 1 && obj.url.indexOf("/api/sysMerchant")<0 && obj.url.indexOf("/api/login/doLogin")<0)
    {
        var merchantNo = localStorage.getItem(commonConstants.sessionItem.merchantNo);
        obj.params['merchantNo'] = merchantNo
    }
    var commonHeader = {
        'Authorization':localStorage.getItem(commonConstants.sessionItem.authorization),
        'reqSource':'1',
        'thirdPartyType':localStorage.getItem(commonConstants.sessionItem.thirdPartyType),
    };
    return new Promise((resolve, reject) => {
        Axios.post(obj.url,obj.params,{headers: Object.assign(commonHeader,headers?headers:{})})
            .then(response => {
                if(response.status == 200)
                {//请求成功
                    var result = response.data;//请求返回结果
                    if(result.newToken)
                    {
                        localStorage.setItem(commonConstants.sessionItem.authorization, result.newToken);
                    }
                    if(result.message)
                    {
                        commonUtil.showMessage({message:result.message,type: result.msgLevel})
                    }
                    if(result.code == "50004")//50004说明token超时，需重新登录
                    {
                        localStorage.removeItem(commonConstants.sessionItem.authorization);
                        router.replace('/login');
                        commonUtil.showMessage({message:result.message,type: result.msgLevel})
                    }
                    if(obj.callback){
                        obj.callback(result)
                    }

                    resolve(result);
                }else{
                    commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error})
                }

            })
            .catch(error => {
                //错误处理
                if (error && error.response)
                {
                    switch (error.response.status) {
                        case 400:commonUtil.showMessage({message:commonConstants.errorCodeMsg[400],type: commonConstants.messageType.error});break;
                        case 401:commonUtil.showMessage({message:commonConstants.errorCodeMsg[401],type: commonConstants.messageType.error});break;
                        case 403:commonUtil.showMessage({message:commonConstants.errorCodeMsg[403],type: commonConstants.messageType.error});break;
                        case 404:commonUtil.showMessage({message:commonConstants.errorCodeMsg[404],type: commonConstants.messageType.error});break;
                        case 408:commonUtil.showMessage({message:commonConstants.errorCodeMsg[408],type: commonConstants.messageType.error});break;
                        case 500:commonUtil.showMessage({message:commonConstants.errorCodeMsg[500],type: commonConstants.messageType.error});break;
                        case 501:commonUtil.showMessage({message:commonConstants.errorCodeMsg[501],type: commonConstants.messageType.error});break;
                        case 502:commonUtil.showMessage({message:commonConstants.errorCodeMsg[502],type: commonConstants.messageType.error});break;
                        case 503:commonUtil.showMessage({message:commonConstants.errorCodeMsg[503],type: commonConstants.messageType.error});break;
                        case 504:commonUtil.showMessage({message:commonConstants.errorCodeMsg[504],type: commonConstants.messageType.error});break;
                        default:commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error});
                    }
                }
                let obj = {
                    code:"50001"
                }
                if(obj.callback){
                    obj.callback(result)
                }
                resolve(obj);
            })
            .finally(() => {
            // loading = false;
            });
    });
}
/**
 * @description: 异步get请求
 * @param url:请求后台的url
 * @param params:参数 map格式
 * @param removeEmpty:空字符串是否作为参数传递到后台 false:空字符串将传递到后台 true:空字符串将不会传递到后台，后台对应的值为null
 * @return:
 * @author: caiyang
 */
commonUtil.doGet = function(obj,headers)
{
    var commonHeader = {
        'Authorization':localStorage.getItem(commonConstants.sessionItem.authorization),
        'reqSource':'1',
        'thirdPartyType':localStorage.getItem(commonConstants.sessionItem.thirdPartyType),
    };
    return new Promise((resolve, reject) => {
        Axios.get(obj.url,{
            params:obj.params,
            headers: Object.assign(commonHeader,headers?headers:{})
        })
          .then((response) => {
            if(response.status == 200)
            {//请求成功
                var result = response.data;//请求返回结果
                if(result.message)
                {
                    commonUtil.showMessage({message:result.message,type: result.msgLevel})
                }
                if(result.code == "50004")//50004说明token超时，需重新登录
                {
                    localStorage.removeItem(commonConstants.sessionItem.authorization);
                    router.replace('/login');
                    commonUtil.showMessage({message:result.message,type: result.msgLevel})
                }
                if(obj.callback){
                    obj.callback(result);
                }
                resolve(result);

            }else{
                commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error})
            }
          })
          .catch((error) => {
            //错误处理
            if (error && error.response)
            {
                switch (error.response.status) {
                    case 400:commonUtil.showMessage({message:commonConstants.errorCodeMsg[400],type: commonConstants.messageType.error});break;
                    case 401:commonUtil.showMessage({message:commonConstants.errorCodeMsg[401],type: commonConstants.messageType.error});break;
                    case 403:commonUtil.showMessage({message:commonConstants.errorCodeMsg[403],type: commonConstants.messageType.error});break;
                    case 404:commonUtil.showMessage({message:commonConstants.errorCodeMsg[404],type: commonConstants.messageType.error});break;
                    case 408:commonUtil.showMessage({message:commonConstants.errorCodeMsg[408],type: commonConstants.messageType.error});break;
                    case 500:commonUtil.showMessage({message:commonConstants.errorCodeMsg[500],type: commonConstants.messageType.error});break;
                    case 501:commonUtil.showMessage({message:commonConstants.errorCodeMsg[501],type: commonConstants.messageType.error});break;
                    case 502:commonUtil.showMessage({message:commonConstants.errorCodeMsg[502],type: commonConstants.messageType.error});break;
                    case 503:commonUtil.showMessage({message:commonConstants.errorCodeMsg[503],type: commonConstants.messageType.error});break;
                    case 504:commonUtil.showMessage({message:commonConstants.errorCodeMsg[504],type: commonConstants.messageType.error});break;
                    default:commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error});
                }
            }
            if(obj.callback){
                obj.callback(result)
            }
          });
    });
}
commonUtil.downloadFile = function(url,params,callback,headers)
{
    var commonHeader = {
        'Authorization':localStorage.getItem(commonConstants.sessionItem.authorization),
        'reqSource':'1',
        'thirdPartyType':localStorage.getItem(commonConstants.sessionItem.thirdPartyType)};
    Axios.post(url,params,{
        responseType: 'blob',
        headers: Object.assign(commonHeader,headers?headers:{})
        },
    ).then((response=>{
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            if(response.headers.filename)
            {
                link.setAttribute('download', decodeURI(response.headers.filename));
            }else{
                link.setAttribute('download', commonUtil.getUuid());
            }
            document.body.appendChild(link);
            link.click();
            if(callback)
            {
                callback(response)
            }
        })
    ).catch((error) => {
        //错误处理
        if (error && error.response)
        {
            const errormsg = error.response.headers.errormsg;
            if(errormsg)
            {
                commonUtil.showMessage({message:decodeURI(errormsg),type: commonConstants.messageType.error});
            }else{
                commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error});
            }

        }
      });
}
//pdf打印
commonUtil.printPdf = function(url,params,headers,callback){
    var commonHeader = {
        'Authorization':localStorage.getItem(commonConstants.sessionItem.authorization),
        'reqSource':'1',
        'thirdPartyType':localStorage.getItem(commonConstants.sessionItem.thirdPartyType)
    };
    Axios.post(url,params,{
        responseType: 'blob',
        headers: Object.assign(commonHeader,headers?headers:{})
        },
    ).then((response=>{
            const content = response.data;
            const url = window.URL.createObjectURL(
                new Blob([content], { type: "application/pdf" })
              );
            var date = new Date().getTime();
            var ifr = document.createElement("iframe");
            ifr.style.frameborder = "no";
            ifr.style.display = "none";
            ifr.style.pageBreakBefore = "always";
            ifr.setAttribute("id", "printPdf" + date);
            ifr.setAttribute("name", "printPdf" + date);
            ifr.src = url;
            document.body.appendChild(ifr);
            commonUtil.doPrint("printPdf" + date);
            window.URL.revokeObjectURL(ifr.src); // 释放URL 对象
            if(callback)
            {
                callback(response)
            }
        })
    ).catch((error) => {
        //错误处理
        if (error && error.response)
        {
            const errormsg = error.response.headers.errormsg;
            if(errormsg)
            {
                commonUtil.showMessage({message:decodeURI(errormsg),type: commonConstants.messageType.error});
            }else{
                commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error});
            }
            if(callback)
            {
                callback(response)
            }
        }
      });
}
commonUtil.doPrint = function(val) {
    var ordonnance = document.getElementById(val).contentWindow;
    setTimeout(() => {
      ordonnance.print();
    }, 100);
  }
//确认消息
//confirmButtonText:确认按钮显示内容 cancelButtonText：取消按钮显示内容，
//api：请求的url requestParam：请求参数 callback：请求成功后的回调函数 type:请求方式，get 或者post，默认为post
commonUtil.showConfirm = function(params)
{
    MessageBox.confirm(params.messageContent,
    {
        confirmButtonText:params.confirmButtonText?params.confirmButtonText:"确认",
        cancelButtonText: params.cancelButtonText?params.cancelButtonText:'取消',
        type:commonConstants.messageType.warning,
    }).then(() => {
        if(params.type && params.type.toLowerCase() == "get")
        {
            var object = {
                url:params.url,
                params:params.params,
                callback:params.callback,
            }
            commonUtil.doGet(object);
        }else{
            var object = {
                url:params.url,
                params:params.params,
                callback:params.callback,
            }
            commonUtil.doPost(object);
        }
    }).catch(() => {
        // commonUtil.showMessage({message:commonUtil.getMessageFromList("error.delete",null),type: commonConstants.messageType.error})
    });
}

commonUtil.showConfirmMsg = function(params){
    MessageBox.confirm(params.messageContent,
        {
            confirmButtonText:params.confirmButtonText?params.confirmButtonText:"确认",
            cancelButtonText: params.cancelButtonText?params.cancelButtonText:'取消',
            type:commonConstants.messageType.warning,
        }).then(() => {
            params.callback()
        }).catch(() => {
            // commonUtil.showMessage({message:commonUtil.getMessageFromList("error.delete",null),type: commonConstants.messageType.error})
        });
}
//清空map对象
commonUtil.clearObj = function(obj)
{
    for(var i in obj) {
       if(obj[i]  instanceof Array)
       {
        obj[i] = [];
       }else if(obj[i]  instanceof Object)
       {
        obj[i] = Object;
       }
       else{
        obj[i] = null;
       }

    }
}

commonUtil.removeEmpty = function (obj)
{
    for(var i in obj)
    {
        if(obj[i] == "")
        {
            delete obj[i]
        }
    }
}

//数据数据字典值对应的name
commonUtil.getDictionaryValueName = function(type,value)
{
    if(commonConstants.dictionary[type])
    {
        if(commonConstants.dictionary[type][value] == undefined){
            return "-";
        }else{
            return commonConstants.dictionary[type][value];
        }
    }else{
        return "-";
    }

}
//获取uuid
commonUtil.getUuid = function()
{
    var len = 32;//32长度
    var radix = 16;//16进制
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;
    if(len) {
      for(i = 0; i < len; i++)uuid[i] = chars[0 | Math.random() * radix];
    } else {
      var r;
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
      uuid[14] = '4';
      for(i = 0; i < 36; i++) {
        if(!uuid[i]) {
          r = 0 | Math.random() * 16;
          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
        }
      }
    }
    return uuid.join('');
}
//关闭页面
commonUtil.closeTag=function(name,that){
    var tag={
        name:name,
      }
      that.GLOBAL.tagsList.splice(that.GLOBAL.tagsList.indexOf(tag), 1);
      that.$router.push("/index");
}
//对象赋值
commonUtil.coperyProperties = function(target,source)
{
    if(source.id)
    {
        target.id = source.id;
    }
    for(var i in target) {
        if(i in source)
        {
            target[i] = source[i];
        }

    }
}

//获取表格数据字典值,数据字典定义在前台js文件中
commonUtil.getTableCodeName = function(prop,row,codeType)
{
    var value = row[prop];
    return commonUtil.getDictionaryValueName(codeType,value)
}

//获取表格数据字典值
commonUtil.formatterTableValue = function(row, column)
{
    var type = column.property;
    var value = row[column.property];
    if(type)
    {
        return commonUtil.getDictionaryValueName(type,value)
    }else{
        return "-"
    }
}
//获取表格数据字典值,数据字典定义在前台js文件中
commonUtil.returnScore = function(prop,row,codeType)
{
    if(row.appealStatus.toString() == '3'){
        return row.score
      }else{
        return " "
      }
}
commonUtil.getSystemCodeName = function(prop,row,codeType)
{
    var value = row[prop];
    let result = commonUtil.getSysCodeValueName(value,codeType);
    return result;
}

/**
 * @description: 获取后台定义的数据字典名称
 * @param {type}
 * @return:
 * @author: caiyang
 */
commonUtil.getSysCodeValueName = function(value,codeType)
{
    var codeValues = JSON.parse(localStorage.getItem("codeValues"));
    return codeValues[codeType+":"+value]?codeValues[[codeType+":"+value]]:"";
}

/**
 * @description: 根据数据字典类型获取数据字典所有的值
 * @param {codeType}  数据字典类型
 * @return:
 * @author: caiyang
 */
commonUtil.getSysCodeValues = function(codeType){
    let result = [];
    let data = commonUtil.doSyncPost("/api/sysCodeValue/selectValByType",{codeType:codeType});
    result = data.responseData;
    return result;
}

/**
 * @description: 根据地区编码获取地区
 * @param {areaCode} 地区编码
 * @return:
 * @author: caiyang
 */
commonUtil.getAreaName = function(areaCode)
{
    let areaName = "";
    let data = commonUtil.doSyncPost("/api/sysArea/getAreaNameByCode",{areaCode:areaCode});
    areaName = data.responseData;
    return areaName;
}
/**
 * @description: 获取下级地区，如果要获取一级地区则传'000000'
 * @param {type}
 * @return:
 * @author: caiyang
 */
commonUtil.getNextArea = function(areaCode)
{
    let areas = [];
    let data = commonUtil.doSyncPost("/api/sysArea/getNextAreasByCode",{areaCode:areaCode});
    areas = data.responseData;
    return areas;
}
/**
 * @description: 初始化所有数据字典的值
 * @param {type}
 * @return:
 * @author: caiyang
 */
commonUtil.initCodeTypeValue = function(){
    let data = commonUtil.doSyncPost("/api/sysCodeValue/getAllVal",{});
    // commonUtil.codeValues = data.responseData;
    localStorage.setItem("codeValues", JSON.stringify(data.responseData));
}
/**
 * @description: 同步请求
 * @param {type}
 * @return:
 * @author: caiyang
 */
commonUtil.doSyncPost=function(url,params){
    let result;
    $.ajax({//ajax
        type : "post",
        url  : commonUtil.baseUrl+url,
        data:JSON.stringify(params),
        async : false,
        dataType : 'json',
        contentType:"application/json;charset=utf-8",
        beforeSend: function(request) {
            request.setRequestHeader('Authorization',localStorage.getItem(commonConstants.sessionItem.authorization));
         },
        success : function(data){
           result = data;
        },
        error : function(data){
           console.log(data)
        }
    });
    return result;
}

commonUtil.formArea = function(prop,row){
    return row.provinceName+row.cityName+row.reginName
}

commonUtil.disabled = function(){
    return true;
}
commonUtil.undisabled = function(){
    return false;
}

//三个连续字符判断
commonUtil.LxStr = function(str){
    var arr = str.split('');
        var flag = true;
        for (var i = 1; i < arr.length-1; i++) {
            var firstIndex = arr[i-1].charCodeAt();
            var secondIndex = arr[i].charCodeAt();
            var thirdIndex = arr[i+1].charCodeAt();
            if((thirdIndex - secondIndex == 1)&&(secondIndex - firstIndex==1)){
                flag =  false;
                return flag;
            }

        }
        for (var i = 1; i < arr.length-1; i++) {
            var firstIndex = arr[i-1].charCodeAt();
            var secondIndex = arr[i].charCodeAt();
            var thirdIndex = arr[i+1].charCodeAt();
            if((firstIndex - secondIndex  == 1)&&(secondIndex - thirdIndex==1)){
                flag =  false;
                return flag
            }
        }
        return flag;
}
//设置cookie
commonUtil.setCookie = function(key,value) {
    //字符串拼接cookie
    window.document.cookie = key+"="+value;
}

//获取cookie
commonUtil.getCookie = function(key){
    if (window.document.cookie.length > 0){
      var arr = document.cookie.split('; ');
      for (var i = 0; i < arr.length; i++) {
        var arr2 = arr[i].split('='); //再次切割
        //判断查找相对应的值
        if (arr2[0] == key) {
          return arr2[1];
        }
      }
    }
}
//校验身份证的合法性
commonUtil.idcardValid = function(code){
    if(!code)
    {
        return true;
    }
    code = code.toUpperCase();
    var city = {11: "北京",12: "天津",13: "河北",14: "山西",15: "内蒙古",21: "辽宁",22: "吉林",23: "黑龙江 ",31: "上海",32: "江苏",33: "浙江",34: "安徽",35: "福建",
    36: "江西",37: "山东",41: "河南",42: "湖北 ",43: "湖南",44: "广东",45: "广西",46: "海南",50: "重庆",51: "四川",52: "贵州",53: "云南",54: "西藏 ",61: "陕西",62: "甘肃",
    63: "青海",64: "宁夏",65: "新疆",71: "台湾",81: "香港",82: "澳门",91: "国外 "};
    if(!city[code.substr(0, 2)])
    {
        return false;
    }else{
        code = code.split('');
        //∑(ai×Wi)(mod 11)
        //加权因子
        var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        //校验位
        var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
        var sum = 0;
        var ai = 0;
        var wi = 0;
        for(var i = 0; i < 17; i++) {
            ai = code[i];
            wi = factor[i];
            sum += ai * wi;
        }
        var last = parity[sum % 11];
        if(parity[sum % 11] != code[17]) {
            return false;
        }
    }
    return true;
}

//form表单校验身份证号是否合法
commonUtil.idcardValidator = function(rule, value, callback){
    if(commonUtil.idcardValid(value))
    {
        callback();
    }else{
        callback(new Error(commonUtil.getMessageFromList("error.idcard.validate")));
    }
}
//启用禁用显示
commonUtil.label = function(row){
    if(row.status == 1)
    {
      return "禁用";
    }else{
      return "启用";
    }
}
/**
*判断是否是数字
*
**/
commonUtil.isRealNum = function (val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
　　if(val === "" || val ==null){
        return false;
　　}
   if(!isNaN(val)){　　　　
　　//对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,   //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
　　　 return true; 　　
    }　else{ 　　　　
        return false; 　　
    }
}

//秒转时分秒
commonUtil.secondToDate = function (result) {
    var h = Math.floor(result / 3600) < 10 ? '0' + Math.floor(result / 3600) : Math.floor(result / 3600);
    var m = Math.floor((result / 60 % 60)) < 10 ? '0' + Math.floor((result / 60 % 60)) : Math.floor((result / 60 % 60));
    var s = Math.floor((result % 60)) < 10 ? '0' + Math.floor((result % 60)) : Math.floor((result % 60));
    result = h + "小时" + m + "分" + s + "秒";
    return result;
}
commonUtil.isNumber = function(val){
    if (isNaN(val)) {
        return false;
    } else {
        return true;
    }
}

//判断值是否是空
commonUtil.isEmpty = function(val)
{
    var result = false;
    if(!val || val == null || val == '')
    {
        result = true;
    }
    return result;
}

//从数组中获取元素和下标
commonUtil.getItemIndexFromList = function (list, attr, val) {
    var result = {
        index:-1,
        item:null
    };
    if (list && list.length > 0) {
        for (let index = 0; index < list.length; index++) {
            const element = list[index];
            if (val == element[attr]) {
                result.index = index;
                result.item = element;
                break;
            }
        }
    }
    return result;
}

//生成随机16进制颜色
commonUtil.color16 = function(){
    var str = "#";
    //一个十六进制的值的数组
    var arr = ["0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"];
    for (var i=0;i<6;i++){
    var num = parseInt(Math.random()*16); //随机产生0-15之间的一个数
    str += arr[num]; //产生的每个随机数都是一个索引,根据索引找到数组中对应的值,拼接到一起
    }
    return str;
}

commonUtil.moveUp = function(data,index){
    if (index > 0) {
        var upData = data[index - 1];
        data.splice(index - 1, 1);
        data.splice(index,0, upData);
    }
}

commonUtil.moveDown = function(data,index){
    if ((index + 1) === data.length){
    
    }else{
        var downData = data[index+1];
        data.splice(index + 1, 1);
        data.splice(index,0, downData);
    }
}
commonUtil.copyObj = function(obj){
    return JSON.parse(JSON.stringify(obj));
}

commonUtil.removePageParams=function(obj){
    var params = {};
    if(obj && obj.length>0)
    {
        params = commonUtil.copyObj(obj);
        for (let index = 0; index < params.length; index++) {
            let element = params[index];
            if(element && element.params && element.params.length>0)
            {
                for (let i = 0; i < element.params.length; i++) {
                    const param = element.params[i];
                    Vue.delete(param,"currentPage");
                    Vue.delete(param,"pageCount");
                }
            }
        }
    }
    return params;
}

commonUtil.reInitChart=function(chartsComponents,component){
    var render = {renderer:"svg"};
    if(commonConstants.componentsType.map3d == component.type)
    {
        render = {devicePixelRatio: 2.5};
    }
    if(component.options.backgroundColor.colorStops[0].color == null)
    {
       component.options.backgroundColor.colorStops[0].color = 'rgba(128, 128, 128, 0.0)';
    }
    if(component.options.backgroundColor.colorStops[1].color == null)
    {
       component.options.backgroundColor.colorStops[1].color = 'rgba(128, 128, 128, 0.0)';
    }
    chartsComponents[component.id].clear();
    var chart = echarts.init(document.getElementById(component.id),render);
    chart.setOption(component.options,true);
    chartsComponents[component.id] = chart;
}

commonUtil.reLoadChart = function(chartsComponents,component,sendRequest,that)
{
    Vue.nextTick(function () {
        if(component.category == "vchart"){
            if(chartsComponents[component.id]){
                chartsComponents[component.id].release();
                chartsComponents[component.id] = null;
            }
            var obj = { dom: component.id}
            if(component.theme){
                obj.theme = component.theme
            }
        }
       
        commonUtil.chartProcess(component);
        if(component.category == "vchart"){
            const vchart = new VChart(component.spec, obj);
            chartsComponents[component.id] = vchart;
            // 绘制
            vchart.renderSync();
            if(component.type == "basicMap" || component.type == "scatterMap"){
                if(component.isDrill){
                    vchart.off('click', null); // 卸载事件
                    vchart.on('click', (params) => {
                        commonUtil.mapDrill(chartsComponents,component,params,sendRequest,that);
                    })
                }else{
                    vchart.off('click', null); // 卸载事件
                }
            }else{
               if(component.isDrill){
                    vchart.off('click', null); // 卸载事件
                    vchart.on('click', (params) => {
                        commonUtil.chartDrill(chartsComponents,component,params);
                    })
                }else{
                    vchart.off('click', null); // 卸载事件
                } 
            }
        }
    })
}

//图表中需要特殊处理的部分
commonUtil.chartProcess = function(component){
    if(component.type.toLowerCase().indexOf("gauge")>=0){
        if(component.spec.data.values && component.spec.data.values.length > 0){
            component.spec.indicator.content.style.text = component.spec.data.values[0][component.spec.valueField];
            if(component.type.toLowerCase().indexOf("seriesgauge")>=0){
                component.spec.gauge.categoryField = component.spec.categoryField;
                component.spec.gauge.valueField = component.spec.valueField;
                component.spec.gauge.seriesField = component.spec.seriesField;
            }else{
                if(component.spec.legends.visible){
                    component.spec.legends.data = items => {
                        for (var index = items.length - 1; index >= 0; index--) {
                            if(items[index].label.indexOf("gaugePointer")>=0 || items[index].label.indexOf("circularProgress")>=0){
                                items.splice(index,1);
                            }
                        }
                        return items.map(item => {
                            return item;
                        });
                    }
                }
            }
        }
        if(component.type == "gauge"){
            if(component.spec.indicator.title){
                component.spec.indicator.title.style.fontSize = component.spec.indicator.content.style.fontSize;
                component.spec.indicator.title.style.fill = component.spec.indicator.content.style.fill;
                component.spec.indicator.title.style.text = component.spec.seriesField?component.spec.data.values[0][component.spec.seriesField]:component.spec.data.values[0][component.spec.categoryField];
            }
        }
    }else if(component.type.toLowerCase().indexOf("circularprogress")>=0){
        component.spec.indicator.content.style.fontSize = component.spec.indicator.title.style.fontSize;
        component.spec.indicator.content.style.fill = component.spec.indicator.title.style.fill;
        if(component.spec.data.values && component.spec.data.values.length > 0){
            if(component.spec.indicator.visible){
                if(component.spec.indicator.trigger == "none"){
                    component.spec.indicator.title.field = null;
                    component.spec.indicator.content.field = null;
                    component.spec.indicator.title.style.text = component.spec.seriesField?component.spec.data.values[0][component.spec.seriesField]:component.spec.data.values[0][component.spec.categoryField];
                    component.spec.indicator.content.style.text = (component.spec.valueTextField?component.spec.data.values[0][component.spec.valueTextField]:component.spec.data.values[0][component.spec.valueField]*100).toFixed(2)+"%";
                }else{
                    component.spec.indicator.title.field = component.spec.valueField;
                    component.spec.indicator.content.field = component.spec.seriesField?component.spec.seriesField:component.spec.categoryField;
                    component.spec.indicator.title.style.text = component.spec.seriesField?component.spec.data.values[0][component.spec.seriesField]:component.spec.data.values[0][component.spec.categoryField];
                    component.spec.indicator.content.style.text = (component.spec.valueTextField?component.spec.data.values[0][component.spec.valueTextField]:component.spec.data.values[0][component.spec.valueField]*100).toFixed(2)+"%";
                }
            }
        }
    }else if(component.type.toLowerCase().indexOf("liquid")>=0){
        component.spec.indicator.content.style.fontSize = component.spec.indicator.title.style.fontSize;
        component.spec.indicator.content.style.fill = component.spec.indicator.title.style.fill;
        if(component.spec.data.values && component.spec.data.values.length > 0){
            if(component.spec.indicator.visible){
                component.spec.indicator.title.style.text = component.spec.categoryField?component.spec.data.values[0][component.spec.categoryField]:"";
                component.spec.indicator.content.style.text = (component.spec.data.values[0][component.spec.valueField]*100).toFixed(2)+"%";
            }
        }
        
    }else if(component.type.toLowerCase().indexOf("text")>=0 || component.type.toLowerCase().indexOf("numberflipper")>=0){
        if(component.spec.valueField && component.spec.data.values && component.spec.data.values.length > 0){
            component.content = component.spec.data.values[0][component.spec.valueField];
        }
       
    }else if(component.type.toLowerCase().indexOf("pie")>=0){
        if(component.spec.isLoop){
            component.spec.animationNormal = screenConstants.pieLoopanimation
        }else{
            component.spec.animationNormal = null
        }
    }else if(component.type.toLowerCase().indexOf("circlepacking")>=0){
        let categoryField = component.spec.categoryField;
        let valueField = component.spec.valueField;
        component.spec.label.style.text = datum => [`${datum[categoryField]}`, `${datum[valueField]}`];
    }
}

commonUtil.dataSourceChange = function(component){
    if(commonConstants.componentsType.text == component.type || commonConstants.componentsType.table == component.type)
      {//文本框和表格
          if(component.columnDataSource == "1")
          {
              component.isRefresh = false;
          }
      }else if(commonConstants.componentsType.histogram == component.type || commonConstants.componentsType.lineChart == component.type)
      {
          if(component.options.categoryDataSource == '1' && component.options.columnDataSource == '1')
          {
              component.isRefresh = false; 
          }
      }else if(commonConstants.componentsType.pieChart == component.type)
      {
          if(component.options.columnDataSource == '1')
          {
              component.isRefresh = false;
          }
      }
}
//校验是否是数字
commonUtil.isNumber = function(num){
    var re = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g;
    if(!re.test(num)){
        return false;
    }else{
        return true;            
    }
}

commonUtil.calculateDate = function(days,format)
{
    let type = "days";
    format = format.replace("yyyy-MM-dd","YYYY-MM-DD").replace("yyyy-MM","YYYY-MM").replace("yyyy","YYYY");
    if(format == "YYYY"){
        type = "years";
    }else if(format == "YYYY-MM"){
        type = "months";
    }
    return moment().add(days, type).format(format) 
}

commonUtil.getDefaultValue = function(paramForm)
{
    var value = "";
    if (paramForm.paramType == "date") {
        if (paramForm.paramDefault != "" && paramForm.paramDefault != null) {
            // if (paramForm.paramDefault.toLowerCase() == "current") {
            //     var now = new Date();
            //     value = commonUtil.formatDate(now, "YYYY-MM-DD")
            // } else {
            //     if (commonUtil.isNumber(paramForm.paramDefault)) {
            //         var days = parseInt(paramForm.paramDefault);
            //         value = commonUtil.calculateDate(days, "YYYY-MM-DD");
            //     } else {
            //         var now = new Date();
            //         value = commonUtil.formatDate(now, "YYYY-MM-DD")
            //     }
            // }
            value = commonUtil.getDefaultDateValue(paramForm);
        }
    }else if(paramForm.paramType == "number"){
       value = paramForm.paramDefault * 1; 
    }
    else {
        value = paramForm.paramDefault;
    }
    return value;
}

commonUtil.getDefaultDateValue = function(paramForm)
{
    var value = "";
    if (paramForm.paramType == "date") {
        let dateFormat = paramForm.dateFormat;
        dateFormat = dateFormat.replace("yyyy-MM-dd","YYYY-MM-DD").replace("yyyy-MM","YYYY-MM").replace("yyyy","YYYY");
        if (paramForm.paramDefault != "" && paramForm.paramDefault != null) {
            if (paramForm.paramDefault.toLowerCase() == "current") {
                var now = new Date();
                value = commonUtil.formatDate(now, paramForm.dateFormat)
            }else if(paramForm.paramDefault.toLowerCase() == "wf"){//本周第一天
                value = moment().startOf('isoWeek').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "wl"){//本周最后一天
                value = moment().endOf('isoWeek').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "mf"){//本月第一天
                value = moment().startOf('month').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "ml"){//本月最后一天
                value = moment().endOf('month').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "sf"){//本季度第一天
                value = moment().startOf('quarter').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "sl"){//本季度最后一天
                value = moment().endOf('quarter').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "yf"){//本年第一天
                value = moment().startOf('year').format(dateFormat);
            }else if(paramForm.paramDefault.toLowerCase() == "yl"){//本年最后一天
                value = moment().endOf('year').format(dateFormat);
            }else {
                if (commonUtil.isNumber(paramForm.paramDefault)) {
                    var days = parseInt(paramForm.paramDefault);
                    value = commonUtil.calculateDate(days, paramForm.dateFormat);
                } else {
                    var now = new Date();
                    value = commonUtil.formatDate(now, paramForm.dateFormat)
                }
            }
        }
    }
    return value;
}
commonUtil.getComponentParams = function(componentParams)
{
    var result = {};
    if(componentParams && componentParams.length>0){
        let prefixMap = {};
        for (let index = 0; index < componentParams.length; index++) {
            const element = componentParams[index];
            let paramType = element.paramType;
            if(paramType == "number"){
                if(element[element.paramCode]){
                    element[element.paramCode] = element[element.paramCode] * 1;
                }
            }
            let prefix = element.paramPrefix;
            let paramObj = null;
            if(prefix){
                if(prefixMap[prefix]){
                    paramObj = prefixMap[prefix];
                }else{
                    paramObj = {};
                    let attrs = prefix.split(".");
                    if(attrs.length > 1) {
                        let temp = null;
                        for (let j = 0; j < attrs.length; j++) {
                            if(j == 0) {
                                if(result[attrs[j]]){
                                    temp = result[attrs[j]];
                                    continue;
                                }
                                let jsonObject = {};
                                result[attrs[j]] = jsonObject;
                                temp = jsonObject;
                            }else if(j == attrs.length - 1) {
                                temp[attrs[j]] = paramObj;
                            }else{
                                if(temp[attrs[j]]){
                                    temp = temp[attrs[j]];
                                    continue;
                                }
                                let jsonObject = {};
                                temp[attrs[j]] = jsonObject;
                                temp = jsonObject;
                            }
                        }
                    }else{
                        result[attrs[0]] = paramObj;
                    }
                    prefixMap[prefix] = paramObj;
                }
            }
            if(element[element.paramCode])
            {
                if(paramObj != null){
                    paramObj[element.paramCode] = element[element.paramCode];
                }else{
                    result[element.paramCode] = element[element.paramCode];
                }
            }else{
                var defaultValue = commonUtil.getDefaultValue(element);
                if(defaultValue)
                {
                    if(paramObj != null){
                        paramObj[element.paramCode] = defaultValue;
                    }else{
                        result[element.paramCode] = defaultValue;
                    }
                }
            }
        }
    }
    return result;
}

commonUtil.getChartsComponentsParams = function(chartsComponents)
{
    var result = {};
    if(chartsComponents){
        for (let index = 0; index < chartsComponents.length; index++) {
            const element = chartsComponents[index];
            let params = commonUtil.getComponentParams(element.params)
            result = Object.assign(result, params)
        }
    }
    return result;
}
commonUtil.mapCodes = {}
//地图下钻
commonUtil.mapDrill = async function(chartsComponents,component,data,sendRequest,that){
    if(data && data.datum && Object.keys(data.datum).length > 0){
        if(component.type == "scatterMap"){
            let type = data.item.type;
            if(type == "symbol"){
                return;
            }
        }
        let adcode = data.datum.properties.adcode+'';
        let name = data.datum.properties.name;
        if(component.drillType == '2'){
            if(component.drillLink){
                let url = commonUtil.buildUrlWithParams(component.drillLink,{mapCode:adcode});
                window.open(url,'_blank')
            }
        }else{
            let parentCode = data.datum.properties.parent.adcode+'';
            let geojson = null;
            if (!VChart.getMap(adcode)) {
                try {
                    geojson = await commonUtil.getMapData(adcode)
                    VChart.registerMap(adcode, geojson)
                } catch (error) {
                    commonUtil.showMessage({message:"未获取到地区【"+name+"】的地图数据",type: commonConstants.messageType.error})
                    return;
                }
            }
            if(component.type == "basicMap"){
                component.spec.map = adcode;
                component.spec.nameMap = screenConstants.nameMap[adcode];
            }else if(component.type == "scatterMap"){
                component.spec.series[0].map = adcode;
            }
            commonUtil.mapCodes[adcode] = parentCode;
            if(!sendRequest ){
                chartsComponents[component.id].updateSpec(component.spec,true);
            } else{
                if(component.type != "picture"){
                    if(component.dataSource == "2"){
                        that.$refs['draggable'].$refs[component.id][0].initData(that);
                    }else{
                        chartsComponents[component.id].updateSpec(component.spec,true);
                    }
                }
            }
        }
    }else if(data && (!data.datum || Object.keys(data.datum).length == 0)){
        if(component.drillType == '2'){
            return;
        }
        let adcode = "";
        if(component.type == "basicMap"){
            adcode = component.spec.map+'';
        }else{
            adcode = component.spec.series[0].map+'';
        }
        let backCode = commonUtil.mapCodes[adcode];
        if(backCode){
            if(component.type == "basicMap"){
                component.spec.map = backCode;
                component.spec.nameMap = screenConstants.nameMap[backCode];
            }else if(component.type == "scatterMap"){
                component.spec.series[0].map = backCode;
            }
            if(!sendRequest ){
                chartsComponents[component.id].updateSpec(component.spec,true);
            } else{
                if(component.type != "picture"){
                    if(component.dataSource == "2"){
                        that.$refs['draggable'].$refs[component.id][0].initData(that);
                    }else{
                        chartsComponents[component.id].updateSpec(component.spec,true);
                    }
                }
            }
        }
    }
}

commonUtil.initClickType = function(chartsComponents,component,that,isPreview){
    if(component.clickType == "1")
    {
        var chart = chartsComponents[component.id];
        if(!chart)
        {
            return;
        }
        chart.off("click");
        chart.getZr().off("dblclick")
        component.bindComponent = null;
    }else if(component.clickType == "2"){
        commonUtil.drill(chartsComponents,component,that,isPreview);
    }else if(component.clickType == "3"){
        var chart = chartsComponents[component.id];
        chart.off("click");
        chart.getZr().off("dblclick")
        component.bindComponent = null;
        commonUtil.jump(chartsComponents,component,that);
    }
}

commonUtil.drill = function(chartsComponents,component,that,isPreview){
    var chart = chartsComponents[component.id];
    chart.on("click", (params) => {
        commonUtil.showDrillComponent(chartsComponents,component,that,isPreview,params.data);
    });
  },
 commonUtil.showDrillComponent = async function(chartsComponents,component,that,isPreview,drillParam){
    if(component.bindComponent)
    {
      if(component.bindComponent.type == commonConstants.componentsType.map || component.bindComponent.type == commonConstants.componentsType.mapScatter)
      {
        var mapCode;
        if(component.bindComponent.type == commonConstants.componentsType.map)
        {
            mapCode = that.component.bindComponent.options.series[0].map;
        }else if(component.bindComponent.type == commonConstants.componentsType.mapScatter)
        {
            mapCode = that.component.bindComponent.options.geo.map;
        }
        const registerMap = that.$echarts.getMap(mapCode);
        if(!registerMap)
        {
            await Axios.get("/geoJson/"+mapCode+".json").then((res)=>{
              that.$echarts.registerMap(mapCode,res.data);
            });
        }
      }
      var chart = chartsComponents[component.id];
      chartsComponents[component.id].setOption(component.bindComponent.options,true);
      chart.off("click");
      that.isBindComponent = true;
      if(!isPreview)
      {
        if(component.bindComponent.type == commonConstants.componentsType.pieChart || component.bindComponent.type == commonConstants.componentsType.gauge
            || component.bindComponent.type == commonConstants.componentsType.map || component.bindComponent.type == commonConstants.componentsType.mapScatter)
        {
            if(component.bindComponent.options.columnDataSource == "2")
            {
                that.getApiData(drillParam);
            }else if(component.bindComponent.options.columnDataSource == "3")
            {
                that.getSqlData(drillParam);
            }
        }else if(component.bindComponent.type == commonConstants.componentsType.histogram || component.bindComponent.type == commonConstants.componentsType.lineChart
            || component.bindComponent.type == commonConstants.componentsType.histogramLineChart)
        {
            if(component.bindComponent.options.categoryDataSource == "2" || component.bindComponent.options.columnDataSource == "2")
            {
                that.getApiData(drillParam);
            }
            if(component.bindComponent.options.categoryDataSource == "3" || component.bindComponent.options.columnDataSource == "3")
            {
                that.getSqlData(drillParam);
            }
        }
      }
      commonUtil.back(chartsComponents,component,that,isPreview);
    }
}

commonUtil.back = function(chartsComponents,component,that,isPreview){
    var chart = chartsComponents[component.id];
    chart.getZr().on("dblclick", (params) => {
        commonUtil.showOriginalComponent(chartsComponents,component,that,isPreview);
    });
}

commonUtil.showOriginalComponent = function(chartsComponents,component,that,isPreview){
    var chart = chartsComponents[component.id];
    chartsComponents[component.id].setOption(component.options,true);
    that.isBindComponent = false;
    chart.getZr().off("dblclick");
    commonUtil.drill(chartsComponents,component,that,isPreview);
    if(!isPreview)
    {
        if(component.type == commonConstants.componentsType.pieChart || component.type == commonConstants.componentsType.gauge)
        {
            if(component.options.columnDataSource == "2")
            {
                that.getApiData();
            }else if(component.options.columnDataSource == "3")
            {
                that.getSqlData();
            }
        }else if(component.type == commonConstants.componentsType.histogram || component.type == commonConstants.componentsType.lineChart
            || component.type == commonConstants.componentsType.histogramLineChart)
        {
            if(component.options.categoryDataSource == "2")
            {
                that.getCategoryApiData();
            }else if(component.options.categoryDataSource == "3")
            {
                that.getCategorySqlData();
            }
            if(component.options.columnDataSource == "2")
            {
                that.getColumnApiData();
            }else if(component.options.columnDataSource == "3"){
                that.getColumnSqlData();
            }
        }
        
    }
}

commonUtil.jump = function(chartsComponents,component,that){
    var chart = chartsComponents[component.id];
    chart.on("click", (params) => {
        var componentParams = commonUtil.getComponentParams(component.params)
        var param = Object.assign({},that.$route.query,componentParams,params.data)
        var url = commonUtil.getUrl(component.thirdUrl,param)
        window.open(url)
    });
}

commonUtil.bindComponent = function(type,component){
    if (type == commonConstants.componentsType.text) {//文本框
        var obj = JSON.parse(JSON.stringify(commonConstants.screenTextInit))
        obj.id = component.id;
        component.bindComponents.push(obj);
    } else if (type == commonConstants.componentsType.picture) {//图片
        var obj = JSON.parse(JSON.stringify(commonConstants.screenPictureInit))
        obj.id = component.id;
        component.bindComponents.push(obj);
    }
    else if (type == commonConstants.componentsType.histogram) {//柱状图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenHistogramInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.lineChart) {//折线图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenLineChartInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.pieChart) {//饼图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenPieChartInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.gauge) {//仪表盘
        var obj = JSON.parse(JSON.stringify(commonConstants.screenGaugeInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.map) {//地图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenMapInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.map3d) {//3d地图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenMap3dInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.pie3dChart) {//3d饼图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenPie3dInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.mapScatter) {
        //散点地图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenMapScatterInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.mapMigrate) {
        //迁徙地图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenMapMigrateInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.circleProgress) {
        //环形百分比
        var obj = JSON.parse(JSON.stringify(commonConstants.screenCircleProgressInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } if (type == commonConstants.componentsType.progressBar) {//进度条
        var obj = JSON.parse(JSON.stringify(commonConstants.screenProcressBarInit))
        obj.id = component.id;
        component.bindComponent = obj;
    } else if (type == commonConstants.componentsType.histogramLineChart) {//折柱图
        var obj = JSON.parse(JSON.stringify(commonConstants.screenHistogramLineInit))
        obj.id = component.id;
        component.bindComponent = obj;
    }
}

commonUtil.getTitle = function(component)
{
   var type = component.type;
   if(type ==commonConstants.componentsType.text)
   {
       return "文本";
   }else if(type ==commonConstants.componentsType.picture)
   {
       return "图片";
   }else if(type ==commonConstants.componentsType.histogram)
   {
       return "柱状图";
   }else if(type ==commonConstants.componentsType.lineChart)
   {
       return "折线图";
   }else if(type ==commonConstants.componentsType.histogramLineChart)
   {
       return "折柱图";
   }else if(type ==commonConstants.componentsType.pieChart)
   {
       return "饼图";
   }else if(type ==commonConstants.componentsType.gauge)
   {
       return "仪表盘";
   }else if(type ==commonConstants.componentsType.table)
   {
       return "表格";
   }else if(type ==commonConstants.componentsType.date)
   {
       return "日期";
   }else if(type ==commonConstants.componentsType.map)
   {
       return "地图";
   }else if(type ==commonConstants.componentsType.map3d)
   {
       return "3D地图";
   }else if(type ==commonConstants.componentsType.mapScatter)
   {
       return "散点地图";
   }else if(type ==commonConstants.componentsType.mapMigrate)
   {
       return "迁徙地图";
   }else if(type ==commonConstants.componentsType.pie3dChart)
   {
       return "3D饼图";
   }else if(type ==commonConstants.componentsType.circleProgress)
   {
       return "环形百分比";
   }else if(type ==commonConstants.componentsType.progressBar)
   {
       return "进度条";
   }
}

commonUtil.getBindComponentApiData = function(that,drillParam){
    var componentParams = commonUtil.getComponentParams(that.component.params)
    if(that.component.bindComponent.type == commonConstants.componentsType.histogram || that.component.bindComponent.type == commonConstants.componentsType.lineChart
        || that.component.bindComponent.type == commonConstants.componentsType.histogramLineChart)
    {
        if(that.component.bindComponent.options.categoryDataSource == "2")
        {
            let obj = {
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
                url:that.apis.screenDesign.getCategoryApiResultApi
            }
            commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    that.component.bindComponent.options.xAxis.data = response.responseData;
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            })
        }
        if(that.component.bindComponent.options.columnDataSource == "2")
        {
            let obj = {
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
                url:that.apis.screenDesign.getApiResultApi
            }
            commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    const series = that.component.bindComponent.options.series;
                    if(series.length == 1)
                    {
                        series[0].data = response.responseData[0];
                    }else{
                        for (let index = 0; index < series.length; index++) {
                            series[index].data = response.responseData[index];
                        }
                    }
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            })
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.pieChart)
    {
        if(that.component.bindComponent.options.columnDataSource == '2')
        {
            let obj = {
                url:that.apis.screenDesign.getPieApiResultApi,
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
             that.commonUtil.doPost(obj) .then(response=> {
                if(response.code == "200")
                {
                    for (let index = 0; index < that.component.bindComponent.options.series[0].data.length; index++) {
                        var element = that.component.bindComponent.options.series[0].data[index];
                        element.value = response.responseData[index].value
                    }
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            });
        }
        if(that.component.bindComponent.options.isAutoHighLight){
            var timer = that.timerMap[that.component.id]
            if(timer != null)
            {
                clearInterval(timer);     
                timer = null;
            }
            var i = 0;
            var chartPie = that.chartsComponents[that.component.id]
            chartPie.dispatchAction({type:'highlight',seriesIndex: 0,dataIndex: i});//设置默认选中高亮部分
            var timer = setInterval(function(){
                var dateLength = that.component.bindComponent.options.series[0].data.length;
                i = i + 1;
                if(i >= dateLength){
                    i=0
                }
                chartPie.dispatchAction({type: 'downplay',seriesIndex: 0,dataIndex: i==0?dateLength-1:i-1});
                chartPie.dispatchAction({type: 'highlight',seriesIndex: 0,dataIndex:i>=dateLength?0:i});//设置默认选中高亮部分
            },1000);
            that.timerMap[that.component.id] = timer;
        }else{
            var timer = that.timerMap[that.component.id]
            if(timer != null)
            {
                var chartPie = that.chartsComponents[that.component.id]
                for (let index = 0; index < that.component.bindComponent.options.series[0].data.length; index++) {
                    chartPie.dispatchAction({type: 'downplay',seriesIndex: 0,dataIndex: index});
                }
                clearInterval(timer);     
                timer = null;
            }
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.gauge){
        if(that.component.bindComponent.options.columnDataSource == '2')
        {
            let obj = {
                url:that.apis.screenDesign.getGaugeApiResult,
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200")
                {
                    that.component.bindComponent.options.series[0].data[0].value = response.responseData.value;
                    if(response.responseData.name)
                    {
                        that.component.bindComponent.options.series[0].data[0].name = response.responseData.name;
                    }
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            });
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.map){
        if(that.component.bindComponent.options.columnDataSource == '3')
        {
            let obj = {
                url:that.apis.screenDesign.getMapApiResult,
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200")
                {
                    that.component.bindComponent.options.series[0].data = response.responseData;
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                        
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
                
            });
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.mapScatter){
        if(that.component.bindComponent.options.columnDataSource == '2')
        {
            let obj = {
                url:that.apis.screenDesign.getMapScatterApiResult,
                params:{urlParams:Object.assign({},that.$route.query,componentParams,drillParam),screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200")
                {
                    that.component.bindComponent.options.series[0].data = response.responseData;
                    if(that.chartsComponents[that.component.id])
                    {
                        that.component.bindComponent.options.tooltip.formatter = function (params) {
                            var result =  params.seriesName + "<br>";
                            if(that.component.bindComponent.options.tooltip.suffix)
                            {
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '') + that.component.bindComponent.options.tooltip.suffix;
                            }else{
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '');
                            }
                            return result ;
                        },
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        that.component.bindComponent.options.tooltip.formatter = function (params) {
                            var result =  params.seriesName + "<br>";
                            if(that.component.bindComponent.options.tooltip.suffix)
                            {
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '') + that.component.bindComponent.options.tooltip.suffix;
                            }else{
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '');
                            }
                            return result ;
                        },
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
                
            });
        }
    }
}

commonUtil.getBindComponentSqlData = function(that,drillParam){
    var componentParams = commonUtil.getComponentParams(that.component.params)
    if(that.component.bindComponent.type == commonConstants.componentsType.histogram || that.component.bindComponent.type == commonConstants.componentsType.lineChart
        || that.component.bindComponent.type == commonConstants.componentsType.histogramLineChart)
    {
        if(that.component.bindComponent.options.categoryDataSource == "3")
        {
            var params = Object.assign({},that.$route.query,componentParams,drillParam)
            let obj = {
                url:that.apis.screenDesign.getScreenDataBySqlApi,
                params:{params:params,screenComponentId:that.component.primaryKey,categoryType:true,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    that.component.bindComponent.options.xAxis.data = response.responseData[0];
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            })
        }
        if(that.component.bindComponent.options.columnDataSource == "3")
        {
            var params = Object.assign({},that.$route.query,componentParams,drillParam)
            let obj = {
                url:that.apis.screenDesign.getScreenDataBySqlApi,
                params:{params:params,screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    const series = that.component.bindComponent.options.series;
                    for (let index = 0; index < series.length; index++) {
                        series[index].data = response.responseData[index];
                    }
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
                
            })
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.pieChart){
        if(that.component.bindComponent.options.columnDataSource == '3')
        {
            var params = Object.assign({},that.$route.query,componentParams,drillParam)
                let obj = {
                    url:that.apis.screenDesign.getScreenPieDataBySqlApi,
                    params:{params:params,screenComponentId:that.component.primaryKey,isBindComponent:1},
                }
                that.commonUtil.doPost(obj) .then(response=>{
                    if(response.code == "200"){
                        that.component.bindComponent.options.series[0].data = response.responseData;
                        if(that.chartsComponents[that.component.id])
                        {
                            that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                        }else{
                            var render = {renderer:"svg"};
                            if(that.commonConstants.componentsType.map3d == that.component.type)
                            {
                                render = {devicePixelRatio: 2.5};
                            }
                            var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                            chart.setOption(that.component.bindComponent.options);
                            that.chartsComponents[that.component.id] = chart;
                            if(that.component.bindComponent.options.isAutoHighLight){
                                var timer = that.timerMap[that.component.id]
                                if(timer != null)
                                {
                                    clearInterval(timer);     
                                    timer = null;
                                }
                                var i = 0;
                                var chartPie = that.chartsComponents[that.component.id]
                                chartPie.dispatchAction({type:'highlight',seriesIndex: 0,dataIndex: i});//设置默认选中高亮部分
                                var timer = setInterval(function(){
                                    var dateLength = that.component.bindComponent.options.series[0].data.length;
                                    i = i + 1;
                                    if(i >= dateLength){
                                        i=0
                                    }
                                    chartPie.dispatchAction({type: 'downplay',seriesIndex: 0,dataIndex: i==0?dateLength-1:i-1});
                                    chartPie.dispatchAction({type: 'highlight',seriesIndex: 0,dataIndex:i>=dateLength?0:i});//设置默认选中高亮部分
                                },1000);
                                that.timerMap[that.component.id] = timer;
                            }else{
                                var timer = that.timerMap[that.component.id]
                                if(timer != null)
                                {
                                    var chartPie = that.chartsComponents[that.component.id]
                                    for (let index = 0; index < that.component.bindComponent.options.series[0].data.length; index++) {
                                        chartPie.dispatchAction({type: 'downplay',seriesIndex: 0,dataIndex: index});
                                    }
                                    clearInterval(timer);     
                                    timer = null;
                                }
                            }
                        }
                    }
                })
        }
        
    }else if(that.component.bindComponent.type == commonConstants.componentsType.gauge){
        if(that.component.bindComponent.options.columnDataSource == '3')
        {
            var params = Object.assign({},that.$route.query,componentParams,drillParam)
            let obj = {
                url:that.apis.screenDesign.getScreenGaugeDataBySqlApi,
                params:{params:params,screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    that.component.bindComponent.options.series[0].data[0].value = response.responseData.value;
                    if(response.responseData.name)
                    {
                        that.component.bindComponent.options.series[0].data[0].name = response.responseData.name;
                    }
                    if(that.chartsComponents[that.component.id])
                    {
                        that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
        })
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.map){
        if(that.component.bindComponent.options.columnDataSource == '3')
        {
            var params = {};
            params.dataSetId = that.component.bindComponent.options.columnDataSetSettings.dataSetId;
            params.props = that.component.bindComponent.options.columnDataSetSettings.column;
            params.params = Object.assign({},that.$route.query,componentParams,drillParam);
            params.isBindComponent = 1;
            let obj = {
                url:that.apis.screenDesign.getScreenMapDataBySqlApi,
                params:params,
            }
            that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    that.component.bindComponent.options.series[0].data = response.responseData;
                    if(that.chartsComponents[that.component.id])
                    {
                        that.commonUtil.reLoadChart(that.chartsComponents,that.component.bindComponent);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            })
        }
    }else if(that.component.bindComponent.type == commonConstants.componentsType.mapScatter){
        if(that.component.bindComponent.options.columnDataSource == '3'){
            var params = Object.assign({},that.$route.query,componentParams,drillParam)
            let obj = {
                url:that.apis.screenDesign.getScreenScatterMapDataBySqlApi,
                params:{params:params,screenComponentId:that.component.primaryKey,isBindComponent:1},
            }
             that.commonUtil.doPost(obj) .then(response=>{
                if(response.code == "200"){
                    that.component.bindComponent.options.series[0].data = response.responseData;
                    if(that.chartsComponents[that.component.id])
                    {
                        that.component.bindComponent.options.tooltip.formatter = function (params) {
                            var result =  params.seriesName + "<br>";
                            if(that.component.bindComponent.options.tooltip.suffix)
                            {
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '') + that.component.bindComponent.options.tooltip.suffix;
                            }else{
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '');
                            }
                            return result ;
                        },
                       that.chartsComponents[that.component.id].setOption(that.component.bindComponent.options,true);
                    }else{
                        var render = {renderer:"svg"};
                        if(that.commonConstants.componentsType.map3d == that.component.type)
                        {
                            render = {devicePixelRatio: 2.5};
                        }
                        var chart = that.$echarts.init(document.getElementById(that.component.id),that.component.bindComponent.theme,render);
                        that.component.bindComponent.options.tooltip.formatter = function (params) {
                            var result =  params.seriesName + "<br>";
                            if(that.component.bindComponent.options.tooltip.suffix)
                            {
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '') + that.component.bindComponent.options.tooltip.suffix;
                            }else{
                                result =  result + params.marker + params.name + "：" + (params.value[2] + '');
                            }
                            return result ;
                        },
                        chart.setOption(that.component.bindComponent.options);
                        that.chartsComponents[that.component.id] = chart;
                    }
                }
            })
        }
    }
}

commonUtil.getParam = function(data)
{
    let url = '';
        for(var k in data){
            let value = data[k] !==undefined ? data[k] : '';
            url += `&${k}=${encodeURIComponent(value)}`
        }
        return url ? url.substring(1) : ''
}

commonUtil.getUrl = function(url,data)
{
    return url += (url.indexOf('?') < 0 ? '?' : '') + this.getParam(data)
}

commonUtil.getCoordsFromColumnName = function(name,isArr){
    var result;
    name = name.toLocaleUpperCase();
    var t = /^[a-zA-Z]+/.exec(name);
    if (t) {
		// Base 26 calculation
		var code = 0;
		for (var i = 0; i < t[0].length; i++) {
			code += parseInt(t[0].charCodeAt(i) - 64) * Math.pow(26, (t[0].length - 1 - i));
		}
		code--;
		// Make sure jexcel starts on zero
		if (code < 0) {
			code = 0;
		}
 
		// Number
		var number = parseInt(/[0-9]+$/.exec(name));
		if (number > 0) {
			number--;
		}
 
		if (isArr == true) {
			result = [ number,code ];
		} else {
			result = number + '_' + code;
		}
        return result;
	}
}

commonUtil.getcFromColumnName = function(name){
    var result;
    name = name.toLocaleUpperCase();
    var t = /^[a-zA-Z]+/.exec(name);
    if (t) {
		// Base 26 calculation
		var code = 0;
		for (var i = 0; i < t[0].length; i++) {
			code += parseInt(t[0].charCodeAt(i) - 64) * Math.pow(26, (t[0].length - 1 - i));
		}
		code--;
		// Make sure jexcel starts on zero
		if (code < 0) {
			code = 0;
		}
 
        return code;
	}
}
commonUtil.getColumnFromCoords = function(r,c){
    var letter = '';
	if (c > 701) {
		letter += String.fromCharCode(64 + parseInt(c / 676));
		letter += String.fromCharCode(64 + parseInt((c % 676) / 26));
	} else if (c > 25) {
		letter += String.fromCharCode(64 + parseInt(c / 26));
	}
	letter += String.fromCharCode(65 + (c % 26));
	return letter + (parseInt(r) + 1);
}

commonUtil.getColumnNameFromC = function(c){
    var letter = '';
	if (c > 701) {
		letter += String.fromCharCode(64 + parseInt(c / 676));
		letter += String.fromCharCode(64 + parseInt((c % 676) / 26));
	} else if (c > 25) {
		letter += String.fromCharCode(64 + parseInt(c / 26));
	}
	letter += String.fromCharCode(65 + (c % 26));
	return letter;
}

commonUtil.isNaN = function(value){
    return !isNaN(parseFloat(value)) && isFinite(value);
}

commonUtil.regexValid = function(value,regex){
   return regex.test(value);
}

commonUtil.uploadImage = function (file) {
    return new Promise(function (resolve, reject) {
        var xhr = new XMLHttpRequest();
        var url = location.protocol + "//" + location.host + commonUtil.baseUrl + "/api/common/upload"
        xhr.open('POST', url);
        // 额外的请求头
        var headers = {
            'Authorization': localStorage.getItem("token")
        };
        if (headers) {
            Object.keys(headers).forEach(function (k) {
                xhr.setRequestHeader(k, headers[k]);
            });
        }
        var data = new FormData();
        // 要上传的图片文件
        data.append('file', file, file.name || '');

        xhr.send(data);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    var res = JSON.parse(xhr.responseText);
                    var url = res.responseData.fileUri;
                    if (url) {
                        resolve(url); // 给上传的后的地址
                    } else {
                        reject('file upload error');
                    }
                } else {
                    reject('file upload error');
                }
            }
        };
    });
}

commonUtil.uploadFile = function (file,obj) {
    const formData = new FormData();
    formData.append("file",file);
    let config = {
        headers: {'Content-Type': 'multipart/form-data',
        'Authorization':localStorage.getItem(commonConstants.sessionItem.authorization),
        'thirdPartyType':localStorage.getItem(commonConstants.sessionItem.thirdPartyType),
      }
    }
    return new Promise((resolve, reject) => {
        Axios.post(obj.url, formData, config).then(response => {
            if(response.status == 200){
                //请求成功
                var result = response.data;//请求返回结果
                if(result.newToken)
                {
                    localStorage.setItem(commonConstants.sessionItem.authorization, result.newToken);
                }
                if(result.message)
                {
                    commonUtil.showMessage({message:result.message,type: result.msgLevel})
                }
                if(obj.callback){
                    obj.callback(result)
                }
                resolve(result);
            }else{
                commonUtil.showMessage({message:commonUtil.getMessageFromList("error.system",null),type: commonConstants.messageType.error})
            }
        })
    })
}
commonUtil.replaceHtml = function (temp, dataarry) {
    return temp.replace(/\$\{([\w]+)\}/g, function (s1, s2) { let s = dataarry[s2]; if (typeof (s) != "undefined") { return s; } else { return s1; } });
}
const SURROGATE_PAIR_REG = /[\uD800-\uDBFF][\uDC00-\uDFFF]/ // unicode代理对（surrogate pair）
const EMOJI_REG =
  /[#*0-9]\uFE0F?\u20E3|[\xA9\xAE\u203C\u2049\u2122\u2139\u2194-\u2199\u21A9\u21AA\u231A\u231B\u2328\u23CF\u23ED-\u23EF\u23F1\u23F2\u23F8-\u23FA\u24C2\u25AA\u25AB\u25B6\u25C0\u25FB\u25FC\u25FE\u2600-\u2604\u260E\u2611\u2614\u2615\u2618\u2620\u2622\u2623\u2626\u262A\u262E\u262F\u2638-\u263A\u2640\u2642\u2648-\u2653\u265F\u2660\u2663\u2665\u2666\u2668\u267B\u267E\u267F\u2692\u2694-\u2697\u2699\u269B\u269C\u26A0\u26A7\u26AA\u26B0\u26B1\u26BD\u26BE\u26C4\u26C8\u26CF\u26D1\u26E9\u26F0-\u26F5\u26F7\u26F8\u26FA\u2702\u2708\u2709\u270F\u2712\u2714\u2716\u271D\u2721\u2733\u2734\u2744\u2747\u2757\u2763\u27A1\u2934\u2935\u2B05-\u2B07\u2B1B\u2B1C\u2B55\u3030\u303D\u3297\u3299]\uFE0F?|[\u261D\u270C\u270D](?:\uFE0F|\uD83C[\uDFFB-\uDFFF])?|[\u270A\u270B](?:\uD83C[\uDFFB-\uDFFF])?|[\u23E9-\u23EC\u23F0\u23F3\u25FD\u2693\u26A1\u26AB\u26C5\u26CE\u26D4\u26EA\u26FD\u2705\u2728\u274C\u274E\u2753-\u2755\u2795-\u2797\u27B0\u27BF\u2B50]|\u26D3\uFE0F?(?:\u200D\uD83D\uDCA5)?|\u26F9(?:\uFE0F|\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|\u2764\uFE0F?(?:\u200D(?:\uD83D\uDD25|\uD83E\uDE79))?|\uD83C(?:[\uDC04\uDD70\uDD71\uDD7E\uDD7F\uDE02\uDE37\uDF21\uDF24-\uDF2C\uDF36\uDF7D\uDF96\uDF97\uDF99-\uDF9B\uDF9E\uDF9F\uDFCD\uDFCE\uDFD4-\uDFDF\uDFF5\uDFF7]\uFE0F?|[\uDF85\uDFC2\uDFC7](?:\uD83C[\uDFFB-\uDFFF])?|[\uDFC4\uDFCA](?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|[\uDFCB\uDFCC](?:\uFE0F|\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|[\uDCCF\uDD8E\uDD91-\uDD9A\uDE01\uDE1A\uDE2F\uDE32-\uDE36\uDE38-\uDE3A\uDE50\uDE51\uDF00-\uDF20\uDF2D-\uDF35\uDF37-\uDF43\uDF45-\uDF4A\uDF4C-\uDF7C\uDF7E-\uDF84\uDF86-\uDF93\uDFA0-\uDFC1\uDFC5\uDFC6\uDFC8\uDFC9\uDFCF-\uDFD3\uDFE0-\uDFF0\uDFF8-\uDFFF]|\uDDE6\uD83C[\uDDE8-\uDDEC\uDDEE\uDDF1\uDDF2\uDDF4\uDDF6-\uDDFA\uDDFC\uDDFD\uDDFF]|\uDDE7\uD83C[\uDDE6\uDDE7\uDDE9-\uDDEF\uDDF1-\uDDF4\uDDF6-\uDDF9\uDDFB\uDDFC\uDDFE\uDDFF]|\uDDE8\uD83C[\uDDE6\uDDE8\uDDE9\uDDEB-\uDDEE\uDDF0-\uDDF5\uDDF7\uDDFA-\uDDFF]|\uDDE9\uD83C[\uDDEA\uDDEC\uDDEF\uDDF0\uDDF2\uDDF4\uDDFF]|\uDDEA\uD83C[\uDDE6\uDDE8\uDDEA\uDDEC\uDDED\uDDF7-\uDDFA]|\uDDEB\uD83C[\uDDEE-\uDDF0\uDDF2\uDDF4\uDDF7]|\uDDEC\uD83C[\uDDE6\uDDE7\uDDE9-\uDDEE\uDDF1-\uDDF3\uDDF5-\uDDFA\uDDFC\uDDFE]|\uDDED\uD83C[\uDDF0\uDDF2\uDDF3\uDDF7\uDDF9\uDDFA]|\uDDEE\uD83C[\uDDE8-\uDDEA\uDDF1-\uDDF4\uDDF6-\uDDF9]|\uDDEF\uD83C[\uDDEA\uDDF2\uDDF4\uDDF5]|\uDDF0\uD83C[\uDDEA\uDDEC-\uDDEE\uDDF2\uDDF3\uDDF5\uDDF7\uDDFC\uDDFE\uDDFF]|\uDDF1\uD83C[\uDDE6-\uDDE8\uDDEE\uDDF0\uDDF7-\uDDFB\uDDFE]|\uDDF2\uD83C[\uDDE6\uDDE8-\uDDED\uDDF0-\uDDFF]|\uDDF3\uD83C[\uDDE6\uDDE8\uDDEA-\uDDEC\uDDEE\uDDF1\uDDF4\uDDF5\uDDF7\uDDFA\uDDFF]|\uDDF4\uD83C\uDDF2|\uDDF5\uD83C[\uDDE6\uDDEA-\uDDED\uDDF0-\uDDF3\uDDF7-\uDDF9\uDDFC\uDDFE]|\uDDF6\uD83C\uDDE6|\uDDF7\uD83C[\uDDEA\uDDF4\uDDF8\uDDFA\uDDFC]|\uDDF8\uD83C[\uDDE6-\uDDEA\uDDEC-\uDDF4\uDDF7-\uDDF9\uDDFB\uDDFD-\uDDFF]|\uDDF9\uD83C[\uDDE6\uDDE8\uDDE9\uDDEB-\uDDED\uDDEF-\uDDF4\uDDF7\uDDF9\uDDFB\uDDFC\uDDFF]|\uDDFA\uD83C[\uDDE6\uDDEC\uDDF2\uDDF3\uDDF8\uDDFE\uDDFF]|\uDDFB\uD83C[\uDDE6\uDDE8\uDDEA\uDDEC\uDDEE\uDDF3\uDDFA]|\uDDFC\uD83C[\uDDEB\uDDF8]|\uDDFD\uD83C\uDDF0|\uDDFE\uD83C[\uDDEA\uDDF9]|\uDDFF\uD83C[\uDDE6\uDDF2\uDDFC]|\uDF44(?:\u200D\uD83D\uDFEB)?|\uDF4B(?:\u200D\uD83D\uDFE9)?|\uDFC3(?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D(?:[\u2640\u2642]\uFE0F?(?:\u200D\u27A1\uFE0F?)?|\u27A1\uFE0F?))?|\uDFF3\uFE0F?(?:\u200D(?:\u26A7\uFE0F?|\uD83C\uDF08))?|\uDFF4(?:\u200D\u2620\uFE0F?|\uDB40\uDC67\uDB40\uDC62\uDB40(?:\uDC65\uDB40\uDC6E\uDB40\uDC67|\uDC73\uDB40\uDC63\uDB40\uDC74|\uDC77\uDB40\uDC6C\uDB40\uDC73)\uDB40\uDC7F)?)|\uD83D(?:[\uDC3F\uDCFD\uDD49\uDD4A\uDD6F\uDD70\uDD73\uDD76-\uDD79\uDD87\uDD8A-\uDD8D\uDDA5\uDDA8\uDDB1\uDDB2\uDDBC\uDDC2-\uDDC4\uDDD1-\uDDD3\uDDDC-\uDDDE\uDDE1\uDDE3\uDDE8\uDDEF\uDDF3\uDDFA\uDECB\uDECD-\uDECF\uDEE0-\uDEE5\uDEE9\uDEF0\uDEF3]\uFE0F?|[\uDC42\uDC43\uDC46-\uDC50\uDC66\uDC67\uDC6B-\uDC6D\uDC72\uDC74-\uDC76\uDC78\uDC7C\uDC83\uDC85\uDC8F\uDC91\uDCAA\uDD7A\uDD95\uDD96\uDE4C\uDE4F\uDEC0\uDECC](?:\uD83C[\uDFFB-\uDFFF])?|[\uDC6E\uDC70\uDC71\uDC73\uDC77\uDC81\uDC82\uDC86\uDC87\uDE45-\uDE47\uDE4B\uDE4D\uDE4E\uDEA3\uDEB4\uDEB5](?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|[\uDD74\uDD90](?:\uFE0F|\uD83C[\uDFFB-\uDFFF])?|[\uDC00-\uDC07\uDC09-\uDC14\uDC16-\uDC25\uDC27-\uDC3A\uDC3C-\uDC3E\uDC40\uDC44\uDC45\uDC51-\uDC65\uDC6A\uDC79-\uDC7B\uDC7D-\uDC80\uDC84\uDC88-\uDC8E\uDC90\uDC92-\uDCA9\uDCAB-\uDCFC\uDCFF-\uDD3D\uDD4B-\uDD4E\uDD50-\uDD67\uDDA4\uDDFB-\uDE2D\uDE2F-\uDE34\uDE37-\uDE41\uDE43\uDE44\uDE48-\uDE4A\uDE80-\uDEA2\uDEA4-\uDEB3\uDEB7-\uDEBF\uDEC1-\uDEC5\uDED0-\uDED2\uDED5-\uDED7\uDEDC-\uDEDF\uDEEB\uDEEC\uDEF4-\uDEFC\uDFE0-\uDFEB\uDFF0]|\uDC08(?:\u200D\u2B1B)?|\uDC15(?:\u200D\uD83E\uDDBA)?|\uDC26(?:\u200D(?:\u2B1B|\uD83D\uDD25))?|\uDC3B(?:\u200D\u2744\uFE0F?)?|\uDC41\uFE0F?(?:\u200D\uD83D\uDDE8\uFE0F?)?|\uDC68(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D(?:[\uDC68\uDC69]\u200D\uD83D(?:\uDC66(?:\u200D\uD83D\uDC66)?|\uDC67(?:\u200D\uD83D[\uDC66\uDC67])?)|[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uDC66(?:\u200D\uD83D\uDC66)?|\uDC67(?:\u200D\uD83D[\uDC66\uDC67])?)|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]))|\uD83C(?:\uDFFB(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D\uDC68\uD83C[\uDFFC-\uDFFF])))?|\uDFFC(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D\uDC68\uD83C[\uDFFB\uDFFD-\uDFFF])))?|\uDFFD(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D\uDC68\uD83C[\uDFFB\uDFFC\uDFFE\uDFFF])))?|\uDFFE(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D\uDC68\uD83C[\uDFFB-\uDFFD\uDFFF])))?|\uDFFF(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?\uDC68\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D\uDC68\uD83C[\uDFFB-\uDFFE])))?))?|\uDC69(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:\uDC8B\u200D\uD83D)?[\uDC68\uDC69]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D(?:[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uDC66(?:\u200D\uD83D\uDC66)?|\uDC67(?:\u200D\uD83D[\uDC66\uDC67])?|\uDC69\u200D\uD83D(?:\uDC66(?:\u200D\uD83D\uDC66)?|\uDC67(?:\u200D\uD83D[\uDC66\uDC67])?))|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]))|\uD83C(?:\uDFFB(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:[\uDC68\uDC69]|\uDC8B\u200D\uD83D[\uDC68\uDC69])\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D[\uDC68\uDC69]\uD83C[\uDFFC-\uDFFF])))?|\uDFFC(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:[\uDC68\uDC69]|\uDC8B\u200D\uD83D[\uDC68\uDC69])\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D[\uDC68\uDC69]\uD83C[\uDFFB\uDFFD-\uDFFF])))?|\uDFFD(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:[\uDC68\uDC69]|\uDC8B\u200D\uD83D[\uDC68\uDC69])\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D[\uDC68\uDC69]\uD83C[\uDFFB\uDFFC\uDFFE\uDFFF])))?|\uDFFE(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:[\uDC68\uDC69]|\uDC8B\u200D\uD83D[\uDC68\uDC69])\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D[\uDC68\uDC69]\uD83C[\uDFFB-\uDFFD\uDFFF])))?|\uDFFF(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D\uD83D(?:[\uDC68\uDC69]|\uDC8B\u200D\uD83D[\uDC68\uDC69])\uD83C[\uDFFB-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83D[\uDC68\uDC69]\uD83C[\uDFFB-\uDFFE])))?))?|\uDC6F(?:\u200D[\u2640\u2642]\uFE0F?)?|\uDD75(?:\uFE0F|\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|\uDE2E(?:\u200D\uD83D\uDCA8)?|\uDE35(?:\u200D\uD83D\uDCAB)?|\uDE36(?:\u200D\uD83C\uDF2B\uFE0F?)?|\uDE42(?:\u200D[\u2194\u2195]\uFE0F?)?|\uDEB6(?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D(?:[\u2640\u2642]\uFE0F?(?:\u200D\u27A1\uFE0F?)?|\u27A1\uFE0F?))?)|\uD83E(?:[\uDD0C\uDD0F\uDD18-\uDD1F\uDD30-\uDD34\uDD36\uDD77\uDDB5\uDDB6\uDDBB\uDDD2\uDDD3\uDDD5\uDEC3-\uDEC5\uDEF0\uDEF2-\uDEF8](?:\uD83C[\uDFFB-\uDFFF])?|[\uDD26\uDD35\uDD37-\uDD39\uDD3D\uDD3E\uDDB8\uDDB9\uDDCD\uDDCF\uDDD4\uDDD6-\uDDDD](?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D[\u2640\u2642]\uFE0F?)?|[\uDDDE\uDDDF](?:\u200D[\u2640\u2642]\uFE0F?)?|[\uDD0D\uDD0E\uDD10-\uDD17\uDD20-\uDD25\uDD27-\uDD2F\uDD3A\uDD3F-\uDD45\uDD47-\uDD76\uDD78-\uDDB4\uDDB7\uDDBA\uDDBC-\uDDCC\uDDD0\uDDE0-\uDDFF\uDE70-\uDE7C\uDE80-\uDE88\uDE90-\uDEBD\uDEBF-\uDEC2\uDECE-\uDEDB\uDEE0-\uDEE8]|\uDD3C(?:\u200D[\u2640\u2642]\uFE0F?|\uD83C[\uDFFB-\uDFFF])?|\uDDCE(?:\uD83C[\uDFFB-\uDFFF])?(?:\u200D(?:[\u2640\u2642]\uFE0F?(?:\u200D\u27A1\uFE0F?)?|\u27A1\uFE0F?))?|\uDDD1(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1|\uDDD1\u200D\uD83E\uDDD2(?:\u200D\uD83E\uDDD2)?|\uDDD2(?:\u200D\uD83E\uDDD2)?))|\uD83C(?:\uDFFB(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D(?:\uD83D\uDC8B\u200D)?\uD83E\uDDD1\uD83C[\uDFFC-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1\uD83C[\uDFFB-\uDFFF])))?|\uDFFC(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D(?:\uD83D\uDC8B\u200D)?\uD83E\uDDD1\uD83C[\uDFFB\uDFFD-\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1\uD83C[\uDFFB-\uDFFF])))?|\uDFFD(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D(?:\uD83D\uDC8B\u200D)?\uD83E\uDDD1\uD83C[\uDFFB\uDFFC\uDFFE\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1\uD83C[\uDFFB-\uDFFF])))?|\uDFFE(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D(?:\uD83D\uDC8B\u200D)?\uD83E\uDDD1\uD83C[\uDFFB-\uDFFD\uDFFF]|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1\uD83C[\uDFFB-\uDFFF])))?|\uDFFF(?:\u200D(?:[\u2695\u2696\u2708]\uFE0F?|\u2764\uFE0F?\u200D(?:\uD83D\uDC8B\u200D)?\uD83E\uDDD1\uD83C[\uDFFB-\uDFFE]|\uD83C[\uDF3E\uDF73\uDF7C\uDF84\uDF93\uDFA4\uDFA8\uDFEB\uDFED]|\uD83D[\uDCBB\uDCBC\uDD27\uDD2C\uDE80\uDE92]|\uD83E(?:[\uDDAF\uDDBC\uDDBD](?:\u200D\u27A1\uFE0F?)?|[\uDDB0-\uDDB3]|\uDD1D\u200D\uD83E\uDDD1\uD83C[\uDFFB-\uDFFF])))?))?|\uDEF1(?:\uD83C(?:\uDFFB(?:\u200D\uD83E\uDEF2\uD83C[\uDFFC-\uDFFF])?|\uDFFC(?:\u200D\uD83E\uDEF2\uD83C[\uDFFB\uDFFD-\uDFFF])?|\uDFFD(?:\u200D\uD83E\uDEF2\uD83C[\uDFFB\uDFFC\uDFFE\uDFFF])?|\uDFFE(?:\u200D\uD83E\uDEF2\uD83C[\uDFFB-\uDFFD\uDFFF])?|\uDFFF(?:\u200D\uD83E\uDEF2\uD83C[\uDFFB-\uDFFE])?))?)/g
commonUtil.splitText = function(text){
    const UNICODE_SYMBOL_REG = new RegExp(
        `${EMOJI_REG.source}|${SURROGATE_PAIR_REG.source}`,
        'g'
      )
    const data = []
    const symbolMap = new Map();
    for (const match of text.matchAll(UNICODE_SYMBOL_REG)) {
        symbolMap.set(match.index, match[0])
    }
    let t = 0
    while (t < text.length) {
        const symbol = symbolMap.get(t)
        if (symbol) {
        data.push(symbol)
        t += symbol.length
        } else {
        data.push(text[t])
        t++
        }
    }
    return data
}

commonUtil.changeHistogramAmination = function(chartsComponents,component){
    if(component.amination == ""){
        component.spec.animationAppear = {};
    }else if(component.amination == "fadeIn"){
        component.spec.animationAppear = {
            type: 'fadeIn',
            oneByOne: true,
        }
    }else if(component.amination == "fadeIn2"){
        component.spec.animationAppear = {
            type: 'fadeIn',
            oneByOne: false,
        }
    }else if(component.amination == "scaleIn"){
        component.spec.animationAppear = {
            type: 'scaleIn',
            oneByOne: true,
        }
    }else if(component.amination == "scaleIn2"){
        component.spec.animationAppear = {
            type: 'scaleIn',
            oneByOne: false,
        }
    }else if(component.amination == "moveIn"){
        component.spec.animationAppear = {
            type: 'moveIn',
            oneByOne: true,
        }
    }else if(component.amination == "moveIn2"){
        component.spec.animationAppear = {
            type: 'moveIn',
            oneByOne: false,
        }
    }else if(component.amination == "growHeightIn"){
        component.spec.animationAppear = {
            type: 'growHeightIn',
            oneByOne: true,
        }
    }else if(component.amination == "growHeightIn2"){
        component.spec.animationAppear = {
            type: 'growHeightIn',
            oneByOne: false,
        }
    }else if(component.amination == "growWidthIn"){
        component.spec.animationAppear = {
            type: 'growWidthIn',
            oneByOne: true,
        }
    }else if(component.amination == "growWidthIn2"){
        component.spec.animationAppear = {
            type: 'growWidthIn',
            oneByOne: false,
        }
    }else if(component.amination == "growCenterIn"){
        component.spec.animationAppear = {
            type: 'growCenterIn',
            oneByOne: true,
        }
    }else if(component.amination == "growCenterIn2"){
        component.spec.animationAppear = {
            type: 'growCenterIn',
            oneByOne: false,
        }
    }
    
    commonUtil.reLoadChart(chartsComponents,component)
}
//获取map数据
commonUtil.getMapData = async function(mapCode){
    const response = await fetch('https://www.springreport.vip/geoJson/'+mapCode+".json");
    const geojson = await response.json();
    return geojson;
}

//获取url文件后缀
commonUtil.getFileExt = function(url){
    let fileType = "";
    try {
        let fileUrl = url.split("?")[0];
        fileType = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        fileType = fileType.toLowerCase();
    } catch (error) {
        console.error("url错误，请检查文件url！")
    }
   
    return fileType;
}

//通过url下载文件
commonUtil.downloadFileByUrl = async function(url,fileName){
    try {
        const res = await fetch(url)
        const blob = await res.blob()
        const a = document.createElement('a')
        const objectURL = URL.createObjectURL(blob)
        a.href = objectURL
        a.download = fileName
        a.click()

        URL.revokeObjectURL(objectURL)
    } catch (error) {
        console.error('文件下载失败', error)
    }
}

commonUtil.processSankeyData = function(component,datas){
    let nodes = [];
    let nodeNames = []
    for (let index = 0; index < datas.length; index++) {
        const element = datas[index];
        let sourceName = element[component.spec.sourceField];
        let targetName = element[component.spec.targetField];
        if(nodeNames.indexOf(sourceName)<0){
          nodeNames.push(sourceName)
          nodes.push({name:sourceName});
        }
        if(nodeNames.indexOf(targetName)<0){
          nodeNames.push(targetName)
          nodes.push({name:targetName});
        }
      }
      component.spec.data.values[0].nodes = nodes;
      component.spec.data.values[0].links = datas;
}

//处理页面参数
commonUtil.processPageParam = function(searchData){
    let newSearchData = JSON.parse(JSON.stringify(searchData))
    if(newSearchData && newSearchData.length > 0){
        for (let index = 0; index < newSearchData.length; index++) {
            let params = newSearchData[index].params;
            if(params && params.length > 0){
                for (let t = 0; t < params.length; t++) {
                    const param = params[t];
                    if(param.paramType == "number"){
                        if(param[param.paramCode] != null && param[param.paramCode] != ""){
                            param[param.paramCode] = param[param.paramCode] * 1;
                        }
                    }
                }
            }
            
        }
    }
    return newSearchData;
}

commonUtil.searchParamMap = function(searchParam){
    let result = {};
    if(searchParam && searchParam.length > 0){
        for (let index = 0; index < searchParam.length; index++) {
            const element = searchParam[index];
            result[element.paramCode] = element[element.paramCode];
        }
    }
    return result;
}

commonUtil.buildUrlWithParams = function(url = "", params = {}) {
  //若参数对象不存在或为空，直接返回url
  if (!params || Object.keys(params).length === 0) {
    return url
  }
  //将参数转化为字符串 eg: {userId: 123,token:'abc'} => 'userId=123&token=abc'
  return url + (url.includes("?") ? "&" : "?") + new URLSearchParams(params).toString()
}

commonUtil.chartDrill = function(chartsComponents,component,data){
    if(component.drillLink && data && data.datum && Object.keys(data.datum).length > 0){
        if(component.type.toLowerCase().indexOf('histogram')>=0 || component.type.toLowerCase().indexOf('line')>=0 || component.type.toLowerCase().indexOf('area')>=0
        || component.type.toLowerCase().indexOf('boxplot')>=0 || component.type.toLowerCase().indexOf('barprogress')>=0 || component.type.toLowerCase().indexOf('combocharthl')>=0
        || component.type.toLowerCase().indexOf('combochartdbbar')>=0){
            let itemName = data.item.name;
            if(itemName == "axis"){
                return;
            }
        }else if(component.type.toLowerCase().indexOf('radar')>=0){
            let itemName = data.item.type;
            if(itemName == "area"){
                return;
            }
        }
        let params = commonUtil.getChartDrillParams(data,component);
        let url = commonUtil.buildUrlWithParams(component.drillLink,params);
        window.open(url,'_blank')
    }
}

commonUtil.getChartDrillParams = function(data,component){
    let result = {};
    if(component.drillParam){
        let params = component.drillParam.split(",");
        for (let index = 0; index < params.length; index++) {
            const element = params[index];
            if(data.datum[element]){
                result[element] = data.datum[element];
            }
        }
    }
    return result;
}

commonUtil.getTableDrillParams = function(d,column){
    let result = {};
    if(column.hyperLinkParam){
        let params = column.hyperLinkParam.split(",");
        for (let index = 0; index < params.length; index++) {
            const element = params[index];
            if(d[element]){
                result[element] = d[element];
            }
        }
    }
    return result;
}

commonUtil.isDate = function(str){
    const date = new Date(str);
    return !isNaN(date.getTime());
}
export default commonUtil;
