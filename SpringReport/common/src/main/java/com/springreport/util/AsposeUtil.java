package com.springreport.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.aspose.words.Document;
import com.aspose.words.EditingLanguage;
import com.aspose.words.License;
import com.aspose.words.LoadOptions;
import com.aspose.words.ParagraphFormat;
import com.aspose.words.SaveFormat;

/**
 * @author : LCheng
 * @date : 2020-12-25 13:47
 * description : Aspose工具类
 */
public class AsposeUtil {

    /**
     * 加载license 用于破解 不生成水印
     *
     * @author LCheng
     * @throws Exception 
     * @date 2020/12/25 13:51
     */
    private static void getLicense() throws Exception {
        try (InputStream is = AsposeUtil.class.getClassLoader().getResourceAsStream("License.xml")) {
            License license = new License();
            license.setLicense(is);
        }
    }

    /**
     * word转pdf
     *
     * @param wordPath word文件保存的路径
     * @param pdfPath  转换后pdf文件保存的路径
     * @author LCheng
     * @date 2020/12/25 13:51
     */
    public static void wordToPdf(String wordPath, String pdfPath) throws Exception{
//        getLicense();
        File file = new File(pdfPath);
        try (FileOutputStream os = new FileOutputStream(file)) {
//        	LoadOptions opt = new LoadOptions();
//        	opt.getLanguagePreferences().setDefaultEditingLanguage(EditingLanguage.CHINESE_PRC);
            Document doc = new Document(wordPath);
            ParagraphFormat pf = doc.getStyles().getDefaultParagraphFormat();
            pf.clearFormatting();
            doc.save(os, SaveFormat.PDF);
        }
    }
}

