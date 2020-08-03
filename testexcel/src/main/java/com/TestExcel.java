package com;

import com.pack.PackageGz;
import com.pack.PackageUnzip;
import com.poi.CsvConversionXlsx;
import com.poi.XlsxConversionRequireTxt;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 功能描述: 测试将excel表中的内容取出的基本实现
 *
 * @Author: liuhaili
 * @Date: 2020-07-15, 周三, 16:12
 */
public class TestExcel {

    public static String srcFilePath = "C:\\Users\\lhl03\\Desktop\\qizhi";

    public static String fileTypeOne = ".zip";

    public static String fileTypeTwo = ".csv";

    public static String fileTypeThree = ".xlsx";

    public static void main(String[] args) {
        long current = System.currentTimeMillis();
        //获得.zip file对象
        List<File> listZip = getFile(srcFilePath,fileTypeOne);
        if (!listZip.isEmpty()) {
            //当这个list内部有.zip对象 就解压
            for (File fileZip : listZip) {
                //解压到当前文件夹
                PackageUnzip.unZip(fileZip.getAbsoluteFile(),fileZip.getParent());
            }
        }

        //获得.csv file对象
        List<File> listCsv = getFile(srcFilePath,fileTypeTwo);
        if (!listCsv.isEmpty()) {
            //当这个list内部有.zip对象 就解压
            for (File fileCsv : listCsv) {
                //将文件转换为xlsx的excel
                CsvConversionXlsx.csvToXlsx(fileCsv.getAbsoluteFile(),fileCsv.getParent());
            }
        }

        //获得.xlsx file对象
        List<File> listXlsx = getFile(srcFilePath,fileTypeThree);
        if (!listXlsx.isEmpty()) {
            //当这个list内部有.xlsx对象 就转化
            for (File fileXlsx : listXlsx) {
                XlsxConversionRequireTxt.xlsxToFile(fileXlsx.getAbsoluteFile(),fileXlsx.getParent());
                //直接就单个文件压缩
                String name = fileXlsx.getParent();
                String filePartName = fileXlsx.getName().split("\\.")[0];
                PackageGz.doCompressFile(name,filePartName);
            }
        }
        //删除因为实际操作而生成的不是结果的文件
        Integer delete = delete(srcFilePath);
        System.out.println("已删除文件数" + delete);
        System.out.println("执行完整个操作所花费时间"+(System.currentTimeMillis()-current)+"ms");
    }

    /**
     * 获取当前这个文件夹中符合要求后缀的所有file
     *
     * @param srcFilePath 父路径
     * @param fileType    筛选格式
     * @return list的file对象集合
     */
    private static List<File> getFile(String srcFilePath,String fileType) {
        //获得一个容器 用于存储符合要求的file对象
        List<File> fileList = new ArrayList<>();
        // 获得指定文件对象
        File file = new File(srcFilePath);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        if (array != null && array.length > 0) {
            for (File subFile : array) {
                if (subFile.isFile() && accept(subFile.getName(), fileType)) {
                    fileList.add(subFile);
                }
                /*这里可以递归调用 如果后面是传一个文件夹*/
                /*if (subFile.isDirectory()) {限制这个必须是文件,其他文件夹,不予以查找
                    getFile(subFile.getAbsolutePath(),fileType);
                }*/
            }
        }
        return fileList;
    }

    /**
     * 筛选文件
     *
     * @param name 文件名
     * @return 这个文件是否是我需要的文件
     */
    private static boolean accept(String name,String fileType) {
        boolean flag = false;
        if (name != null && name.toLowerCase().endsWith(fileType)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除这个文件下的所有其他操作出现的文件，保证操作文件的干净
     * @param srcFilePath
     * @return
     */
    private static Integer delete(String srcFilePath) {
        //删除文件数量
        Integer count = 0;
        File file = new File(srcFilePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            //只是删除当前文件下的子文件
            if (files != null && files.length > 0) {
                System.out.println("下面是将要删除的文件内容: \n"+Arrays.toString(files));
                for (File fil : files) {
                    if (!fil.isDirectory()) {
                        fil.delete();
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
