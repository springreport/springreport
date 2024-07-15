/*
 * @Description: 自定义校验方法
 * @Version: 1.0
 * @Autor: caiyang
 * @Date: 2020-06-04 08:36:18
 * @LastEditors: caiyang caiyang90@163.com
 * @LastEditTime: 2022-08-24 17:42:47
 */ 

export default{
  install (app){
      app.config.globalProperties.filter_rules = function(controlName,item){
      let rules = [];
      if(!item)
      {
        return rules;
      }
      if(item.required){
        //必须输入校验
        let message = this.commonUtil.getMessageFromList("error.input.notnull",[controlName]);
        if(item.valueType)
        {
          rules.push({ type:item.valueType,required: true, message: message, trigger: 'change' });
        }else{
          rules.push({ required: true, message: message, trigger: 'change' });
        }
      }
      //字符串长度校验
      if(item.minLength && item.maxLength){
        if(item.maxLength < item.minLength)
        {
          let message = this.commonUtil.getMessageFromList("error.length.range",[controlName]);
          this.commonUtil.showMessage({message:message,type: this.commonConstants.messageType.error})
        }else{
          //最大长度和最小长度校验
          let message = this.commonUtil.getMessageFromList("error.input.length",[controlName,item.minLength,item.maxLength]);
          rules.push({ min: item.minLength,max:item.maxLength, message: message, trigger: 'change' });
        }
        
      }else if(item.minLength && ! item.maxLength)
      {//最小长度校验
        let message = this.commonUtil.getMessageFromList("error.input.min.length",[controlName,item.minLength]);
        rules.push({ min: item.minLength, message: message, trigger: 'change' });
      }else if(!item.minLength && item.maxLength){
        //最大长度
        let message = this.commonUtil.getMessageFromList("error.input.max.length",[controlName,item.maxLength]);
        rules.push({ max: item.maxLength, message: message, trigger: 'change' });
      }
      if(item.length)
      {//固定长度
        let message = this.commonUtil.getMessageFromList("error.input.fixedlength",[controlName,item.length]);
        rules.push({ min:item.length,max: item.length, message: message, trigger: 'change' });
      }
      //浮点类型的数据
      if(item.float)
      {
        let numbermessage =this.commonUtil.getMessageFromList("error.input.format",[controlName,'数字'])
        rules.push({ type: 'string', pattern:/^[+-]?(0|([1-9]\d*))(\.\d+)?$/g,message: numbermessage, trigger: 'change' });
        let message =this.commonUtil.getMessageFromList("error.input.float.format",[controlName,item.float]);
        rules.push({ type: 'string', pattern:eval('/^-?\\d+(\\.\\d{1,'+item.float+'})?$/'),message: message, trigger: 'change' });
      }
      if(item.type){
        let type = item.type;
        let message;
        switch(type) {
          case 'email'://邮箱
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'邮箱'])
              rules.push({ type: 'email', message: message, trigger: 'change' });
              break;
          case 'mobile'://手机号
              message =this.commonUtil.getMessageFromList("error.mobile.format",[controlName])
              rules.push({ type: 'string', pattern:/^(1[0-9])\d{9}$/,message: message, trigger: 'change' });
              break;
          case 'phone'://座机号码
              message =this.commonUtil.getMessageFromList("error.phone.format",[controlName])
              rules.push({ type: 'string', pattern:/^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})$/,message: message, trigger: 'change' });
              break;
          case 'mobilephone'://手机号或者座机号码
              message =this.commonUtil.getMessageFromList("error.mobilephone.format",[controlName])
              rules.push({ type: 'string', pattern:/^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})$|(1[0-9])\d{9}$/,message: message, trigger: 'change' });
              break;
          case 'number'://数字
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'数字'])
              rules.push({ type: 'string', pattern:/^[+-]?(0|([1-9]\d*))(\.\d+)?$/g,message: message, trigger: 'change' });
              break;
          case 'integer'://整数类型
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'整数'])
              rules.push({ type: 'string', pattern:'^-?\\d+$',message: message, trigger: 'change' });
              break;
          case 'url'://超链接
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'超链接'])
              rules.push({ type: 'url', message: message, trigger: 'change' });
              break;
          case 'positiveInt'://>=0整数
              let numbermessage =this.commonUtil.getMessageFromList("error.input.format",[controlName,'数字'])
              rules.push({ type: 'string', pattern:/^[+-]?(0|([1-9]\d*))(\.\d+)?$/g,message: numbermessage, trigger: 'change' });
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'大于等于0的整数'])
              rules.push({ type: 'string', pattern:'^\\+?\\d*$',message: message, trigger: 'change' });
              break;
          case 'idcard'://身份证号
              message =this.commonUtil.getMessageFromList("error.idcard.format")
              rules.push({ type: 'string', pattern:/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/,message: message, trigger: 'change' });
              break;
          case 'accountName'://账号名称格式
              message =this.commonUtil.getMessageFromList("error.accountName.format")
              rules.push({ type: 'string', pattern:/^([a-z]|[A-Z]|[a-zA-Z0-9.@_-]){8,18}$/,message: message, trigger: 'change' });
              break;
          case 'positiveNumber'://账号名称格式
              message =this.commonUtil.getMessageFromList("error.accountName.format")
              rules.push({ type: 'string', pattern:/^([a-z]|[A-Z]|[a-zA-Z0-9.@_-]){8,18}$/,message: message, trigger: 'change' });
              break;
          case 'letter'://字母
              message =this.commonUtil.getMessageFromList("error.input.format",[controlName,'英文字母'])
              rules.push({ type: 'string', pattern:/^[a-zA-Z]+$/,message: message, trigger: 'change' });
              break;
        }
      };
      //自定义正则表达式
      if(item.regexp){
        let message =this.commonUtil.getMessageFromList("error.regexp.format",[controlName]);
        rules.push({ type: 'string', pattern:eval(item.regexp),message: message, trigger: 'change' });
      }
      if(this.commonUtil.isRealNum(item.max) && this.commonUtil.isRealNum(item.min))
      {
        if(item.max<item.min)
        {
          let message = this.commonUtil.getMessageFromList("error.compare.range",[controlName]);
          this.commonUtil.showMessage({message:message,type: this.commonConstants.messageType.error})
        }else{
          let message =this.commonUtil.getMessageFromList("error.number.range",[controlName,item.min,item.max])
          rules.push({ type: 'number', min:item.min,max:item.max,message: message, trigger: 'change', transform:(value) => Number(value)});
        }
        
      }else if(this.commonUtil.isRealNum(item.max) && !this.commonUtil.isRealNum(item.min)){
        let message =this.commonUtil.getMessageFromList("error.number.max",[controlName,item.max])
        rules.push({ type: 'number', max:item.max,message: message, trigger: 'change', transform:(value) => Number(value)});
      }else if(!this.commonUtil.isRealNum(item.max) && this.commonUtil.isRealNum(item.min)){
        let message =this.commonUtil.getMessageFromList("error.number.min",[controlName,item.min])
        rules.push({ type: 'number', min:item.min,message: message, trigger: 'change', transform:(value) => Number(value)});
      }
      if(item.validator)
      {
        rules.push({ validator: item.validator, trigger: 'change' });
      }
      return rules;
        
      }
  }
}
  