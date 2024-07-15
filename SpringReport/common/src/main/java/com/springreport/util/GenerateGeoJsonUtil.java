package com.springreport.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class GenerateGeoJsonUtil {

	public static void generate(String areaCode,String areaName,List<String> notFond) {

        URL url = null;
            try {
				url = new URL("https://geo.datav.aliyun.com/areas_v2/bound/"+areaCode+"_full.json");
			} catch (MalformedURLException e1) {
				try {
					url = new URL("https://geo.datav.aliyun.com/areas_v2/bound/"+areaCode+".json");
				} catch (MalformedURLException e) {
					notFond.add(areaCode);
					e.printStackTrace();
				}
			}   
        try {
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String urlString = "";
            String current;
            while ((current = in.readLine()) != null) {
                urlString += current;
            }
            File file =new File("F:/map/json/"+"/");  
            if  (!file .exists()  && !file .isDirectory())      
            {
            	file .mkdir();    
            }
            //写到本地
            File fp = new File("F:/map/json/"+areaCode+".json");
            if(!fp.exists())
            {
            	fp.createNewFile();
            }
            OutputStream os = new FileOutputStream(fp);
            os.write(urlString.getBytes());
            os.close();
        } catch (Exception e) {
        	notFond.add(areaCode);
            e.printStackTrace();
        }
    
	}
}
