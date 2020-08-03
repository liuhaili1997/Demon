package com.poi;

import com.pack.PackageUnzip;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * 功能描述: 读取csv文件,导出没有后缀名文件
 *
 * 后期的优化:考虑遍历文件夹
 * 1.将所有zip压缩包解压 再查找所有的csv的excel文件,读取内部数据
 * 2.生成一个对应的没有后缀名的文件,压缩 以特定的格式进行压缩
 * 3.调用链接 发送请求 将压缩好的数据包发送出去
 * 实际的功能:这边只需要执行相应的sql语句并且导出文件,然后运行这部分代码完成后续操作
 *
 * @Author: liuhaili
 * @Date: 2020-07-16, 周四, 11:20
 */
public class CsvConversionXlsx {

    /**
     * 将csv文件转化为xmls文件
     *
     * @param srcFile 源文件对象
     * @param targetFile 目标文件路径
     */
    public static void csvToXlsx(File srcFile,String targetFile) {
        long current = System.currentTimeMillis();
        System.out.print("开始将csv文件转化为xlsx文件，当前文件为：");
        try {
            String name = srcFile.getName();
            //输出当前端的文件作为目标文件
            System.out.println(name);
            name = name.split("\\.")[0];
            //xlsx srcFile address
            String xlsxFileAddress = targetFile+"\\"+name+".xlsx";
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet(name);
            String currentLine=null;
            int RowNum=0;
            FileInputStream fis = new FileInputStream(srcFile);
            //指定以UTF-8编码读入
            InputStreamReader isr = new InputStreamReader(fis,"GBK");
            BufferedReader br = new BufferedReader(isr);
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(",");
                RowNum++;
                XSSFRow currentRow=sheet.createRow(RowNum);
                for(int i=0;i<str.length;i++){
                    String trim = trim(str[i], "\"");
                    currentRow.createCell(i).setCellValue(trim);
                }
            }

            FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
            //个人理解:由于sheet是从1开始的,导致生成的excel最上面的一行是空格
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("已完成转换操作，操作时间："+(System.currentTimeMillis()-current)+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去除首尾指定字符
     * @param str 字符串
     * @param element 指定字符
     * @return
     */
    public static String trim(String str, String element){
        if (StringUtils.isBlank(str)) {
            return "";
        }
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do{
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int length = str.length();
            int endIndex = str.lastIndexOf(element) + 1 == length ? str.lastIndexOf(element) : length;
            str = str.substring(beginIndex, endIndex);
            //裁剪了重新获取长度
            length = str.length();
            beginIndexFlag = (str.indexOf(element) == 0);
            if (length > 0) {
                endIndexFlag = (str.lastIndexOf(element) + 1 == length);
            } else {
                endIndexFlag = (str.lastIndexOf(element) == length);
            }
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }
}
