package com.cnki.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author min
 * @create 2016-11-30 11:50
 */
public class HttpClientUtils {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static final int maxTotal = 1;
    private static final int maxPerRoute = 1;
    private static CloseableHttpClient client;

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);// 整个连接池最大连接数
        cm.setDefaultMaxPerRoute(maxPerRoute);// 每路由最大连接数，默认值是2
        client = HttpClients.custom().setConnectionManager(cm).build();//通过连接池获取HttpClient
    }

    /**
     * 通过连接池获取HttpClient
     */
    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     *
     * @param url
     * @return
     */
    public static String get(String url) throws URISyntaxException, IOException {
        return get(url, null);
    }

    public static String get(String url, Map<String, Object> params) throws
            URISyntaxException, IOException {
        return get(url, params, null);
    }

    public static String get(String url, Map<String, Object> params,
                                        Map<String, Object> headers) throws URISyntaxException,
            IOException {
        return get(url, params, headers,null);
    }


    public static String get(String url, Map<String, Object> params,
                                        Map<String, Object> headers, Charset responseEncode) throws
            URISyntaxException, IOException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        if (params != null) {
            ub.setParameters(covertParams2NVPS(params));
        }

        HttpGet httpGet = new HttpGet(ub.build());
        if (headers != null) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        return getResult(httpGet, responseEncode);
    }

    public static String post(String url) throws IOException {
        return post(url, null);
    }

    public static String post(String url, Map<String, Object> params) throws
            IOException {
        return post(url, params, null);
    }

    public static String post(String url, Map<String, Object> params,
                                         Map<String, Object> headers) throws IOException {
        return post(url, params, headers, null, null);
    }

    public static String post(String url, Map<String, Object> params,
                                         Map<String, Object> headers, Charset requestEncode,
                                         Charset responseEncode) throws IOException {
        if (requestEncode == null) requestEncode = UTF_8;
        if (responseEncode == null) responseEncode = UTF_8;
        HttpPost httpPost = new HttpPost(url);

        if (headers != null) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        if (params != null) {
            httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), requestEncode));
        }

        return getResult(httpPost, responseEncode);
    }

    private static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        return params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(),
                String.valueOf(entry.getValue()))).collect(Collectors.toList());

    }

    /**
     * 处理Http请求
     */
    private static String getResult(HttpRequestBase request, Charset charset) throws IOException {
        if (charset == null) charset = UTF_8;
        CloseableHttpClient httpClient = client;//getHttpClient();
        try (CloseableHttpResponse response = httpClient.execute(request);) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, charset);
                return result;
            }
        }

        return EMPTY_STR;
    }

}
