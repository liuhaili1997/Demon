package com.pack;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 功能描述:
 *
 * @Author: liuhaili
 * @Date: 2020-07-16, 周四, 18:48
 */
public class PackageGz {

    /**
     * 将文件压缩成.gz的格式
     *
     * @param inFileName
     */
    public static void doCompressFile(String inFileName,String filePartName) {
        //记录这个操作的工作时间
        long current = System.currentTimeMillis();
        try {
            System.out.println("开始以CZIP格式压缩文件："+filePartName);
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            //实际的文件名  WB-CJJS-QZST_sqlResult3029523-1G-20200716.gz
            String outFileName = new StringBuilder(inFileName).append("\\data\\")
                    .append(date).append("\\").append("WB-CJJS-QZST_")
                    .append(filePartName).append("-1G-").append(date).append(".gz").toString();
            //修改逻辑
            inFileName = inFileName+"\\"+filePartName;
            //因为这个是每一天都会创建一个文件夹收藏要传的值，所以要判断是否存在
            File file = new File(outFileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            System.out.println("压缩文件的父路径："+ parentFile);
            GZIPOutputStream out = null;
            try {
                out = new GZIPOutputStream(new FileOutputStream(file));
            } catch(FileNotFoundException e) {
                System.err.println("Could not create file: " + outFileName);
                System.exit(1);
            }

            FileInputStream in = null;
            try {
                in = new FileInputStream(inFileName);
            } catch (FileNotFoundException e) {
                System.err.println("没有找到文件：" + inFileName);
                System.exit(1);
            }

            System.out.println("开始通过byte字节压缩文件------------>");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            //释放资源
            in.close();
            out.finish();
            out.close();
            System.out.println("已经结束了压缩，总耗时"+(System.currentTimeMillis()-current)+"ms");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }


    /**
     * 解压
     *
     * @param inFileName Name of the file to be uncompressed
     */
    private static void doUncompressFile(String inFileName) {

        try {

            if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
                System.err.println("File name must have extension of \".gz\"");
                System.exit(1);
            }

            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            try {
                in = new GZIPInputStream(new FileInputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }

            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                System.err.println("Could not write to file. " + outFileName);
                System.exit(1);
            }

            System.out.println("Transfering bytes from compressed file to the output file.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            System.out.println("Closing the file and stream");
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * 用于提取和返回给定文件的扩展名
     * @param f Incoming file to get the extension of
     * @return <code>String</code> representing the extension of the incoming
     *         file.
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }
        return ext;
    }

    /**
     * Used to extract the filename without its extension.
     * @param f Incoming file to get the filename
     * @return <code>String</code> representing the filename without its
     *         extension.
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }
        return fname;
    }
}
