package com.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 功能描述:
 *
 * @Author: liuhaili
 * @Date: 2020-07-16, 周四, 15:24
 */
public class PackageUnzip {

    /**
     * 解压zip
     *
     * @param srcFile zip源文件
     * @param path  解压后的目标文件夹
     */
    public static void unZip(File srcFile, String path) {
        long current = System.currentTimeMillis();
        String name = srcFile.getName();
        String prefixName = name.split("\\.")[0];
        System.out.println("开始解压文件: "+ name);
        //判断文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + " 此路径不存在");
        }

        /*++++++++++++++++++++++解压开始+++++++++++++++++++++++*/
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String suffixName = entry.getName().split("\\.")[1];
                String zipName =new StringBuilder(prefixName).append(".").append(suffixName).toString();
                System.out.println("解压文件名：" + zipName);
                String dirPath = path + "\\" + zipName;
                if (entry.isDirectory()) {
                    File file = new File(dirPath);
                    //创建文件夹
                    file.mkdir();
                } else {
                    uncompressFile(zipFile, entry, dirPath);
                    //这里添加一个历史文本，用于对数据的检测 放在当前文件目录的history
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    dirPath = path+"\\history\\"+date+"_uploadFile\\"+zipName;
                    uncompressFile(zipFile, entry, dirPath);
                }
            }
            zipFile.close();
            System.out.println("解压消耗时间："+(System.currentTimeMillis()-current)+"ms");
        } catch (Exception e) {
            throw new RuntimeException("zip exception from ZipUtils", e);
        }
        /*++++++++++++++++++++++解压结束+++++++++++++++++++++++*/
    }

    /**
     * 将解压操作提取出来，可以方便生成需要保留的文件
     *
     * @param zipFile 压缩的文件对象
     * @param entry 实体
     * @param dirPath 解压路径
     */
    private static void uncompressFile(ZipFile zipFile, ZipEntry entry, String dirPath) {
        //压缩的是文件，就需要先创建文件，再用io流将文件copy过去
        File targetFile = new File(dirPath);
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        //确保父路径是存在的
        try {
            if (!targetFile.getParentFile().exists()) {
                //不存在对应的父路径，就先创建一个父路径下的文件夹
                targetFile.getParentFile().mkdir();
            }
            boolean newFile = targetFile.createNewFile();
            if (!newFile) {
                System.out.println("创建文件" +targetFile.getName()+"失败!!");
            }
            //将压缩文件解压到当前文件中
            inputStream = zipFile.getInputStream(entry);
            fileOutputStream = new FileOutputStream(targetFile);
            int length;
            byte[] bytes = new byte[1024];
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }

            //释放资源
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println("解压不成功：" + dirPath);
            System.exit(1);
        }
    }
}
