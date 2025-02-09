package com.wallet.api.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * http工具
 */
public class HttpUtil {

    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};
    private static final HostnameVerifier NOT_VERYFY = (s, sslSession) -> true;

    private static HttpURLConnection openConnection(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (url.getProtocol().equals("https")) {
            HttpsURLConnection.setDefaultHostnameVerifier(NOT_VERYFY);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        return conn;
    }

    public static Map<String, Object> sendPost(String urlPath, Object param) {
        String paramStr = JSONObject.toJSONString(param);
        StringBuilder result = new StringBuilder();
        String line;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        Map<String, Object> response = new HashMap<>();
        try {
            URL url = new URL(urlPath);
            conn = openConnection(url);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");

            conn.setConnectTimeout(8000);
            conn.setReadTimeout(30000);

            if (!"".equals(paramStr.trim())) {
                byte[] writebytes = paramStr.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(paramStr.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                // 使用 Fastjson 将 JSON 字符串转换为 Map
                response = JSON.parseObject(result.toString(), Map.class);
                return response;
            }
        } catch (Exception e) {
            response.put("status", 500);
            response.put("msg", e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    response.put("status", 500);
                    response.put("msg", e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    response.put("status", 500);
                    response.put("msg", e.getMessage());
                }
            }
        }
        return response;
    }


}
