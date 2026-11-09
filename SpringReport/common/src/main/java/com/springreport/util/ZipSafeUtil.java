package com.springreport.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class ZipSafeUtil {

    private static final int BUFFER_SIZE = 4096;
    // 额外防护：限制单文件大小、总解压大小，防zip炸弹
    private static final long MAX_ENTRY_SIZE = 100 * 1024 * 1024;
    private static final long MAX_TOTAL_SIZE = 500 * 1024 * 1024;

    /**
     * 安全解压，防御Zip Slip
     * @param zipIs zip输入流
     * @param destDirStr 解压目标目录
     */
    public static void unzip(InputStream zipIs) throws IOException {
    	String destDirStr = MessageUtil.getValue("zip.file.check.path")+IdWorker.getIdStr();
        Path destDir = Paths.get(destDirStr).toAbsolutePath().normalize();
        Files.createDirectories(destDir);

        try (ZipInputStream zis = new ZipInputStream(zipIs)) {
            ZipEntry entry;
            long totalSize = 0;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                Path outPath = destDir.resolve(entryName).normalize();
                // 【重点】destDir 也要先 normalize！否则startsWith校验失效（ZipSlip漏洞风险）
                Path destRoot = destDir.normalize();
                if (!outPath.startsWith(destRoot)) {
                    throw new SecurityException("ZipSlip detected, illegal entry: " + entryName);
                }
                // 拦截绝对路径条目
                if (Paths.get(entryName).isAbsolute()) {
                    throw new SecurityException("Absolute path entry not allowed: " + entryName);
                }
                // 拒绝绝对路径的entry（entryName是/xxx这种）
                if (entryName.startsWith("/") || entryName.startsWith("\\") || Paths.get(entryName).isAbsolute()) {
                    throw new SecurityException("Absolute path not allowed: " + entryName);
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(outPath);
                } else {
                    Files.createDirectories(outPath.getParent());
                    long entrySize = entry.getSize();
                    if (entrySize > MAX_ENTRY_SIZE) {
                        throw new IOException("Entry too large: " + entryName);
                    }
                    // ✅ 直接用 zis，不要 try-with-resources 包裹 zis！
                    Files.copy(zis, outPath,StandardCopyOption.REPLACE_EXISTING);
                    totalSize += entrySize;
                    if (totalSize > MAX_TOTAL_SIZE) {
                        throw new IOException("Total decompress size exceed limit");
                    }
                }
                zis.closeEntry(); // 只关闭当前条目，不关闭ZipInputStream
            }
        }finally {
        	FileUtils.deleteDirectory(new File(destDirStr));
        }
    }
    
    /**
     * 递归删除目录，入参为字符串路径
     * @param dirPath 目录字符串
     */
    private static void deleteDir(String dirPath) {
        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir)) {
            return;
        }
        try {
            Files.walk(dir)
                    .sorted((p1, p2) -> -p1.compareTo(p2)) // 先文件，后文件夹
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            // log.error("删除失败 path:{}", path, e);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            // log.error("遍历目录失败 dir:{}", dirPath, e);
            e.printStackTrace();
        }
    }

}

