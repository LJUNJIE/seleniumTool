package util;

import Bean.TestDetailReport;
import Bean.TestReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DataBaseSource {

    public final  static Logger logger = LoggerFactory.getLogger(DataBaseSource.class);

    private static LoadProperties loadProperties = new LoadProperties();
    private static String user = loadProperties.loadProperties("user");
    private static String pwd = loadProperties.loadProperties("pwd");
    private static String host = loadProperties.loadProperties("host");
    private static String port = loadProperties.loadProperties("port");
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://"+host+":"+port+"/datakreport?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8";

    /**
     * 建立连接
     * @return
     */
    public static Connection getConn () {
        Connection conn = null;
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pwd);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return conn;
    }

//    Connection conn = getConn();
//    Statement state = null;

    /**
     * 关闭连接
     * @param state
     * @param conn
     */
    public static void close (Statement state, Connection conn){
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(),e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    public void update(TestReport testReport){
        String sql = "update result_count set count = '" + testReport.getPass() + "' where result = 'PASS'";
        String sql2 = "update result_count set count = '" + testReport.getFail() + "' where result = 'FAIL'";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            state.executeUpdate(sql);
            state.executeUpdate(sql2);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
    }

    public void insertDetail(TestDetailReport testDetailReport, String reportTableName){
        String sql = "insert into `" +reportTableName +"` values ('" + testDetailReport.getTestName() +"','"+testDetailReport.getBeginTime()+"','"+testDetailReport.getEndTime()+"','"+testDetailReport.getTestComment()+"')";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            state.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
    }

    //获取生成的报告的表名称
    public String getReportTableName(){
        String sql = "SELECT CONCAT('testreport_',nextval('report_id')) as reportName";
        String str = "";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()){
                str = rs.getString("reportName");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
        return str;
    }

    //创建报告的表
    public void createReportTable(String reportTableName){
        String sql = "CREATE TABLE `" + reportTableName + "` (`test_name` VARCHAR (256) DEFAULT NULL COMMENT '用例名称',`begin_time` VARCHAR (256) DEFAULT NULL COMMENT '开始时间',`end_time` VARCHAR (256) DEFAULT NULL COMMENT '结束时间',`test_comment` VARCHAR (256) DEFAULT NULL COMMENT '测试结果') ENGINE=INNODB DEFAULT CHARSET=utf8";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            state.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
    }

    //根据sequence，拼接上次生成的报告表名，用于在报告中将表名替换为新生成的表
    public String getLastReportTableName(){
        String sql = "SELECT CONCAT('testreport_',last_value) as lastReportName from datakreport_base_sequence";
        String str = "";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()){
                str = rs.getString("lastReportName");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
        return str;
    }

    //替换报告中的sql的表名
    public void updateReport(String lastReportTableName,String reportTableName){
        String sql = "UPDATE `datakbase`.`datak_view_record` SET `view_info`=REPLACE (view_info,'"+lastReportTableName+"','"+reportTableName+"') WHERE view_id='1231'";
        Connection conn = getConn();
        Statement state = null;
        try {
            state = conn.createStatement();
            state.executeUpdate(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        } finally {
            close(state, conn);
        }
    }
}
