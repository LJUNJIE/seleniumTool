package util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class RestClient {
    final static Logger logger = LoggerFactory.getLogger(RestClient.class);

    BasicCookieStore cookieStore = new BasicCookieStore();

    static RequestConfig requestConfig;
    static {
        //设置http的状态参
        //设置请求和传输超时时间
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
    }

    /**
     *
     * @param url 请求地址
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse get (String url) throws IOException{

        CloseableHttpResponse httpResponse = null;

        try {
            //创建一个可关闭的HttpClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //创建一个HttpGet的请求对象
            HttpGet httpGet = new HttpGet(url);
            //设置http状态参数
//            httpGet.setConfig(requestConfig);
            //执行请求,相当于postman上点击发送按钮，然后赋值给HttpResponse对象接收
//            logger.info("开始发送get请求");
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200){
//                logger.info("请求成功，得到响应对象");
            }else {
                logger.error("请求错误");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return httpResponse;
    }

    /**
     * 带请求头信息的get方法
     * @param url
     * @param headerMap，键值对形式
     * @return 返回响应对象
     */
    public CloseableHttpResponse get (String url,HashMap<String,String> headerMap) throws IOException{
        CloseableHttpResponse httpResponse = null;

        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "0719CFDDBD102373F9D23CBFBC2B83BE");
        cookie.setVersion(0);
//        cookie.setDomain("192.168.0.140:8888");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        try {
            //创建一个可关闭的HttpClient对象
//            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            //创建一个HttpGet的请求对象
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            //设置http状态参数
//            httpGet.setConfig(requestConfig);
            //加载请求头到httpget对象
            for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
            logger.info("发送请求");
            httpResponse = httpClient.execute(httpGet);
            logger.info("请求成功，得到响应对象");
        }catch (Exception e){
            logger.error("请求错误:"+e.getMessage());
        }
//        httpResponse.close();
        return httpResponse;
    }

    /**
     * post方法
     * @param url
     * @param stringEntity，设置请求json参数
     * @param headerMap，带请求头
     * @return 返回响应对象
     */
    public CloseableHttpResponse postString (String url, String stringEntity, HashMap<String,String> headerMap) throws IOException{

        CloseableHttpResponse httpResponse = null;

        try{
            //创建一个可关闭的HttpClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            //设置http状态参数
            httpPost.setConfig(requestConfig);

            //创建一个HttpPost的请求对象
            StringEntity entity = new StringEntity(stringEntity);
            entity.setContentType("application/x-www-form-urlencoded; charset=utf-8\"");
//            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);

            //加载请求头到httpPost对象
            for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            //发送post请求
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            logger.info("开始发送post请求");
            httpResponse = httpClient.execute(httpPost);
            logger.info("请求成功，得到响应对象");
        }catch (Exception e){
            logger.error("请求错误:"+e.getMessage());
        }
//        httpResponse.close();
        return httpResponse;
    }

    /**
     *
     * @param url post请求地址
     * @param list 接口参数，List<NameValuePair>
     * @return
     */
    public CloseableHttpResponse post (String url, List<NameValuePair> list){

        CloseableHttpResponse httpResponse = null;

        try{
            //创建一个可关闭的HttpClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            //设置http状态参数
            httpPost.setConfig(requestConfig);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list);
            httpPost.setEntity(formEntity);

            //设置header信息
            //指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //执行请求操作，并拿到结果
            logger.info("开始发送post请求");
            httpResponse = httpClient.execute(httpPost);
            logger.info("请求成功，得到响应对象");
        }catch (Exception e){
            logger.error("请求错误:"+e.getMessage());
        }
//        httpResponse.close();
        return httpResponse;
    }

    /**
     * put请求方法，参数和post方法一样
     * @param url
     * @param entityString，这个主要是设置payload,一般来说就是json串
     * @param headerMap，带请求的头信息
     * @return 返回响应对象
     * @throws ClientProtocolException
     * @throws IOException
     */
    public CloseableHttpResponse put (String url, String entityString, HashMap<String,String> headerMap) throws ClientProtocolException, IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(entityString));

        for(Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPut.addHeader(entry.getKey(), entry.getValue());
        }
        //发送put请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpPut);
        return httpResponse;
    }

    /**
     * delete请求方法，参数和get方法一样
     * @param url， 接口url完整地址
     * @return 返回一个response对象
     * @throws ClientProtocolException
     * @throws IOException
     */
    public CloseableHttpResponse delete (String url) throws ClientProtocolException, IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDel = new HttpDelete(url);

        //发送delete请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpDel);
        return httpResponse;
    }

    /**
     * 获取响应状态码
     * @param response
     * @return 返回int类型状态码
     */
    public int getStatusCode (CloseableHttpResponse response) {

        int statusCode = response.getStatusLine().getStatusCode();
        logger.info("响应状态:"+ statusCode);
        return statusCode;
    }

    /**
     *
     * @param response, 任何请求返回返回的响应对象
     * @return 返回响应内容的JSON格式
     */
    public JsonObject getResponseJson (CloseableHttpResponse response){
        JsonObject jo = null;
        try {
            //返回响应对象的String格式
            String responseString = EntityUtils.toString(response.getEntity(),"UTF-8");
            //返回响应内容的JSON格式
            Gson gson = new Gson();
            gson.toJson(responseString);
            jo = gson.fromJson(responseString, JsonObject.class);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return jo;
    }

}

