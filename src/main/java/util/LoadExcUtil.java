package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class LoadExcUtil {

    public final static Logger logger = LoggerFactory.getLogger(LoadExcUtil.class);
    private Sheet sheet;
    private Workbook workbook;
    private String type;
    private String script;

    private String[] scripts;
    private int countSheet;//Excel文件中sheet个数
    private String sheetName;

    /**
     *
     * @return 将配置项script返回成数组
     */
    public String[] LoadExc(){

        //读取要运行的脚本文件
        try {
            LoadProperties loadProperties = new LoadProperties();
            script = loadProperties.loadProperties("script");
            scripts = script.split(",");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return scripts;
    }

    /**
     *
     * @param exc 要执行的Excel文件
     * @return Excel文件中sheet个数
     */
    public Integer countSheet(String exc){
        try {
            countSheet = loadBook(exc).getNumberOfSheets();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return countSheet;
    }

    /**
     *
     * @param exc
     * @param k 第k个sheet，下标从0开始
     * @return
     */
    public String getSheetName(String exc,int k){
        try {
            sheetName = loadBook(exc).getSheetName(k);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return sheetName;
    }

    /**
     *
     * @param x 列
     * @param y 行
     * @param exc
     * @param k
     * @return 返回第y行的第x列内容
     */
    public String readValue(int x,int y,String exc,int k) {
        try {
            sheet = loadBook(exc).getSheetAt(k);
            Row row = sheet.getRow(y);//获取第y行

            if(row.getCell(x)!=null){
                row.getCell(x).setCellType(Cell.CELL_TYPE_STRING);
                type = row.getCell(x).getStringCellValue();//获取第y行的第x列内容
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return type;
    }

    private Workbook loadBook(String exc){
        try{
            File file = new File("script/" + exc);
            InputStream is = new FileInputStream(file);
            if(exc.endsWith("xls")){
                workbook = new HSSFWorkbook(is);
            }else if(exc.endsWith("xlsx")){
                workbook = new XSSFWorkbook(is);
            }else {
                logger.error("不是excel文件");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return workbook;
    }
}
