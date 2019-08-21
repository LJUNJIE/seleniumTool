package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {

    public final  static Logger logger = LoggerFactory.getLogger(LoadProperties.class);
    Properties p;
    String str;

    /**
     *
     * @param s 读取配置项 s
     * @return
     */
    public String loadProperties(String s){
        p = new Properties();
        try {
            FileInputStream in = new FileInputStream("cfg/config.properties");
            p.load(in);
            in.close();
            str = p.getProperty(s);
        }catch (IOException e){
            logger.error("读取配置文件出错:"+e.getMessage());
        }
        return str;
    }
}