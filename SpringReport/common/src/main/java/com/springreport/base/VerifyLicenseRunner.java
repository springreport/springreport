//package com.springreport.base;
//
//import java.util.Date;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import com.springreport.constants.StatusCode;
//import com.springreport.exception.BizException;
//import com.springreport.util.DateUtil;
//import com.springreport.util.StringUtil;
//import com.springreport.util.VerifyLicense;
//
///**  
// * @ClassName: VerifyLicense
// * @Description: 校验证书
// * @author caiyang
// * @date 2021-06-20 12:10:58 
//*/  
//@Component
//public class VerifyLicenseRunner implements ApplicationRunner{
//
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		String netWorkTime  = DateUtil.getNetworkTime();//获取网络时间
//		String dateTime = DateUtil.date2String(new Date(), DateUtil.FORMAT_DATEHOUR);//获取当前时间
//		if(StringUtil.isNullOrEmpty(netWorkTime) || !netWorkTime.equals(dateTime))
//		{
//			throw new BizException(StatusCode.FAILURE, "证书安装失败-服务器时间错误！");
//		}
//		VerifyLicense vLicense = new VerifyLicense();
//        //获取参数
//        vLicense.setParam("licenseverify.properties");
//        //验证证书
//        boolean result = vLicense.verify();
//        if(!result)
//        {
//        	throw new BizException(StatusCode.FAILURE, "证书安装失败，想试用或者购买license请联系作者，qq:986539100，qq群：477055814，邮箱：caiyang90@163.com 或者 cy_report@163.com");
//        }
//	}
//
//}
