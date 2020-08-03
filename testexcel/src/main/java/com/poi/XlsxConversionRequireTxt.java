package com.poi;

import com.enums.OperationTypeEnum;
import com.enums.PayTypeEnum;
import com.enums.RechargeOperatorEnum;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述:xlsx转换成所要求的文本文档
 *
 * @Rquire 文件编码格式：gbk（由于后台处理程序的限制，只支持GBK编码格式的数据文本文件！）
 *          数据分隔符：|!
 *          数据换行符：\n
 *
 * @Author: liuhaili
 * @Date: 2020-07-17, 周五, 9:23
 */
public class XlsxConversionRequireTxt {

    /**
     * 将xlsx的excel文件改为不带后缀的文件，内部内容以特定格式定义
     *
     * @param srcFile 源文件对象
     * @param targetFile 目标文件
     */
    public static void xlsxToFile(File srcFile,String targetFile) {
        long current = System.currentTimeMillis();
        System.out.print("开始将xlsx文件转化为要求格式文件，当前文件名：");
        try {
            String name = srcFile.getName();
            System.out.println(name);
            name = name.split("\\.")[0];

            //建立输入流获取excle文件对象
            FileInputStream fileInputStream = new FileInputStream(srcFile);
            //获取excel文件的工作簿对象 对应excel的表单结构
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //声明XSSFSheet对象
            XSSFSheet sheet = sheets.getSheet(name);
            //excel表中的行数
            int rows = sheet.getPhysicalNumberOfRows();
            //excel表中的行
            XSSFRow row = sheet.getRow(1);
            //excel表中的列数
            int columns = row.getPhysicalNumberOfCells();
            //拼接数据
            StringBuilder stringBuilder;
            //获得一个数组将这些存入到新的文件夹中
            List<String> arrays = new ArrayList<>();

            //默认的当cell1为null时,自动给这一个字段添加一个"null"的默认值
            String cell = "null";
            Boolean flag = false;
            if (rows >= 2) {
                //用这个字段来区分两个表
                String value= sheet.getRow(2).getCell(6).toString();
                flag = isNumeric(value);
            }

            //因为excel中第一行通常为各个列的名字，故舍去，从第二行开始，索引为1 但是由于处理数据的时候生成的excel的数据是第一行为空
            for (int i = 2; i < rows+1; i++) {
                //得到Excel工作表的行
                row = sheet.getRow(i);
                //用一个stringBuilder
                stringBuilder = new StringBuilder();
                //循环遍历一行中的每一列数据
                for (int j = 0; j < columns; j++) {
                    //获取一列的数据
                    XSSFCell cell1 = row.getCell(j);
                    //这一块优化if else
                    if (null != cell1) {
                        /*针对的第一种类型表中的数据*/
                        if (flag) {
                            switch (j) {
                                case 9:
                                    //cell1.getNumericCellValue()不可用是因为处理数据时,将所有数据转成了String 类型
                                    double parseDouble = Double.parseDouble(cell1.toString());
                                    cell = new DecimalFormat("#0.00").format(parseDouble);
                                    break;
                                case 7:
                                    int code = Integer.parseInt(cell1.toString());
                                    cell = PayTypeEnum.getValueOfCode(code);
                                    break;
                                default:
                                    cell = cell1.toString();
                                    break;
                            }
                        }
                        if (!flag){
                            /*针对的是第二张表中的数据*/
                            switch (j) {
                                case 6:
                                    int code = Integer.parseInt(cell1.toString());
                                    cell = RechargeOperatorEnum.getValueOfCode(code);
                                    break;
                                case 7:
                                    int type = Integer.parseInt(cell1.toString());
                                    cell = OperationTypeEnum.getValueOfCode(type);
                                    break;
                                case 9:
                                    //cell1.getNumericCellValue()不可用是因为处理数据时,将所有数据转成了String 类型
                                    double parseDouble = Double.parseDouble(cell1.toString());
                                    cell = new DecimalFormat("#0.00").format(parseDouble);
                                    break;
                                default:
                                    cell = cell1.toString();
                                    break;
                            }
                        }
                    }
                    //将每一个空格凭借一个字符串
                    if (j == columns - 1) {
                        stringBuilder.append(cell).append("\n");
                    } else {
                        stringBuilder.append(cell).append("|!");
                    }
                }
                //将数据存入内部
                arrays.add(stringBuilder.toString());
            }

            //将数据文件写入到新的文件中
            String dirPath = targetFile + "\\" + name;
            writeFile(dirPath,arrays);
            //保存一份日志文件,防止出错
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            dirPath = targetFile+"\\history\\"+date+"_uploadFile\\"+name;
            writeFile(dirPath,arrays);
            //释放资源
            fileInputStream.close();
            sheets.close();
            System.out.println("已经完成当前文件转换,这个操作一共用时"+(System.currentTimeMillis()-current)+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 区分表格数据类型 如果第六行的数据的长度为1 来区分表格
     *
     * @param s 第六行的数据
     * @return 一个判断的Boolean值
     */
    public final static boolean isNumeric(String s) {
        int length = s.length();
        if (length > 1) {
            return true;
        }
        return false;
    }

    public final static void writeFile(String dirPath,List<String> arrays) {
        //将数据文件写入到新的文件中
        File targetFile = new File(dirPath);
        FileOutputStream outputStream;
        OutputStreamWriter writer;
        try {
            if (!targetFile.getParentFile().exists()) {
                //不存在对应的父路径，就先创建一个父路径下的文件夹
                targetFile.getParentFile().mkdir();
            }
            //createNewFile 目录下没有对应文件，则创建该文件；如果没有父目录不存在，直接抛出异常，如果对应文件已经存在，那么显示文件创建失败
            boolean newFile = targetFile.createNewFile();
            if (!newFile) {
                System.out.println("创建文件" +targetFile.getName()+"失败!!");
            }
            outputStream = new FileOutputStream(targetFile);
            writer = new OutputStreamWriter(outputStream, "GBK");
            for (String array : arrays) {
                writer.write(array);
                writer.flush();
            }
            outputStream.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("写文件阶段出错：" + e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
