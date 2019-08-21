package common;

import util.LoadProperties;
import util.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ExportProject {
    final static Logger logger = LoggerFactory.getLogger(ExportProject.class);

    private RestClient restClient;
    private CloseableHttpResponse response;

    LoadProperties loadProperties = new LoadProperties();

    public String getUrl(){

        String url = "http://192.168.0.140:8888/datak-web/exportProject?viewId=[1231]&needCas=false&needDataSource=true&secretUserId=forUIAutoTest";
        JsonObject responseJson = null;
        String downloadPath = null;

        try {
            logger.info("开始导出独立工程...");
            restClient = new RestClient();
            response = restClient.get(url);
            String responseString = EntityUtils.toString(response.getEntity(),"UTF-8");
            //返回响应内容的JSON格式
            Gson gson = new Gson();

            gson.toJson(responseString);
            responseJson = gson.fromJson(responseString, JsonObject.class);

            String getData = responseJson.get("data").getAsString();
            gson.toJson(getData);
            responseJson = gson.fromJson(getData, JsonObject.class);
            downloadPath = responseJson.get("downloadPath").getAsString();

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return downloadPath;
    }

    public void downloadProject(String downloadPath){

        String urlIp = loadProperties.loadProperties("datakIP");

        String url = "http://192.168.0.140:8888/datak-web/downLoadProject?downloadPath="+downloadPath;

        try {
            restClient = new RestClient();
            response = restClient.get(url);

            HttpEntity entity = response.getEntity();
            InputStream input = entity.getContent();
            File file = new File("report/project.zip");
            OutputStream output = new FileOutputStream(file);
            int len = 0;
            byte[] ch = new byte[1024];
            while ((len = input.read(ch)) != -1) {
                output.write(ch, 0, len);
            }
            logger.info("导出独立工程完成");

        }catch (Exception e){
            logger.error(e.getMessage(),e);

        }
    }
}