package com.springreport.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**  
 * @ClassName: FileUtil
 * @Description: 文件工具类
 * @author caiyang
 * @date 2023-04-03 02:41:22 
*/  
public class FileUtil {

	public static  void createFile(File file) {
        if (file.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File not exists, create it ...");
            //getParentFile() 获取上级目录(包含文件名时无法直接创建目录的)
            if (!file.getParentFile().exists()) {
                System.out.println("not exists");
                //创建上级目录
                file.getParentFile().mkdirs();
            }
            try {
                //在上级目录里创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	 public static ByteArrayOutputStream cloneInputStream(InputStream input) {
	        try {
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = input.read(buffer)) > -1) {
	                baos.write(buffer, 0, len);
	            }
	            baos.flush();
	            return baos;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
