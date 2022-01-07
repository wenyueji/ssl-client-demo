package com.moshu.ssl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

@Slf4j
@Service
public class HttpsUtil {

    //客户端证书文件名
    private String clientCertPath = "ssl/client.p12";
    //客户端证书生成密码
    private String clientCertPassword = "***";

    private SSLSocketFactory sslFactory;


    //https POST请求返回结果和结果码
    public Map<String, Object> httpsPost(String requestUrl, String json) throws Exception {
        Map<String, Object> map = new HashMap<>();
        OutputStreamWriter wr = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            //start 这一段代码必须加在open之前，即支持ip访问的关键代码
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            //end
            byte[] xmlBytes = json.getBytes();
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("POST");
            //根据自己项目需求设置Content-Type
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(xmlBytes.length));
            ((HttpsURLConnection) conn).setSSLSocketFactory(getSslFactory());
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json);
            wr.close();
            conn.connect();
            String responseBody = getResponseBodyAsString(conn);
            int responseCode = getResponseCode(conn);
            map.put("responseBody", responseBody);
            map.put("responseCode", responseCode);
            if (getResponseCode(conn) == 200) {
                System.out.println("请求成功");
            } else {
                System.out.println("请求失败");
            }
            System.out.println(responseBody);
            conn.disconnect();
        } catch (Exception e) {
            log.error("HTTPS请求出现异常，请求参数为：" + json);
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (wr != null) {
                    wr.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("请求异常", e);
            }
        }
        return map;
    }

    //https POST请求返回结果和结果码
//    public Map<String, Object> httpsFileUpload(String requestUrl, String json, String wasmFile, String abiFile) throws Exception {
    public Map<String, Object> httpsFileUpload(String requestUrl, Map<String, String> textMap, Map<String, String> fileMap) throws Exception {
        Map<String, Object> map = new HashMap<>();
        OutputStreamWriter wr = null;
        HttpURLConnection conn = null;
        String finalSplit = "---------------------------123821742118716";
        try {
            URL url = new URL(requestUrl);
            //start 这一段代码必须加在open之前，即支持ip访问的关键代码
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            //end
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            //根据自己项目需求设置Content-Type
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + finalSplit);

            ((HttpsURLConnection) conn).setSSLSocketFactory(getSslFactory());

            OutputStream out = new DataOutputStream(conn.getOutputStream());


            //文本域
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    inputValue = new String(inputValue.getBytes("GBK"));
                    strBuf.append("\r\n").append("--").append(finalSplit).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes("UTF-8"));
            }

            // 上传文件
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    MagicMatch match = Magic.getMagicMatch(file, false, true);
                    String contentType = match.getMimeType();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(finalSplit).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ( "\r\n--" + finalSplit + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.close();
            conn.connect();
            String responseBody = getResponseBodyAsString(conn);
            int responseCode = getResponseCode(conn);
            map.put("responseBody", responseBody);
            map.put("responseCode", responseCode);
            if (getResponseCode(conn) == 200) {
                System.out.println("请求成功");
            } else {
                System.out.println("请求失败");
            }
            System.out.println(responseBody);
            conn.disconnect();
        } catch (Exception e) {
            log.error("HTTPS请求出现异常，请求参数为：" );
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (wr != null) {
                    wr.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("请求异常", e);
            }
        }
        return map;
    }


    //https POST请求返回结果和结果码
    public Map<String, Object> httpsGet(String requestUrl) throws Exception {
        Map<String, Object> map = new HashMap<>();
        OutputStreamWriter wr = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            //start 这一段代码必须加在open之前，即支持ip访问的关键代码
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            //end
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            //根据自己项目需求设置Content-Type
            ((HttpsURLConnection) conn).setSSLSocketFactory(getSslFactory());
            conn.connect();
            String responseBody = getResponseBodyAsString(conn);
            int responseCode = getResponseCode(conn);
            map.put("responseBody", responseBody);
            map.put("responseCode", responseCode);
            if (getResponseCode(conn) == 200) {
                System.out.println("请求成功");
            } else {
                System.out.println("请求失败");
            }
            System.out.println(responseBody);
            conn.disconnect();
        } catch (Exception e) {
            log.error("请求失败，url= ", requestUrl);

            throw e;
        } finally {
            try {
                if (wr != null) {
                    wr.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                log.error("请求异常", e);
            }
        }
        return map;
    }

    public SSLSocketFactory getSslFactory() throws Exception {
        if (sslFactory == null) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            KeyStore trustStore = KeyStore.getInstance("JKS");
            //加载客户端证书
            InputStream clientInputStream = this.getClass().getClassLoader().getResourceAsStream(clientCertPath);
            trustStore.load(clientInputStream, clientCertPassword.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

            kmf.init(trustStore, clientCertPassword.toCharArray());
            sslContext.init(kmf.getKeyManagers(), tm, new SecureRandom());
            sslFactory = sslContext.getSocketFactory();
        }
        return sslFactory;
    }

    public int getResponseCode(HttpURLConnection connection) throws Exception {
        return connection.getResponseCode();
    }

    public String getResponseBodyAsString(HttpURLConnection connection) throws Exception {
        BufferedReader reader = null;
        if (connection.getResponseCode() == 200) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        return buffer.toString();
    }

    class MyX509TrustManager implements X509TrustManager {
        private X509TrustManager sunJSSEX509TrustManager;

        MyX509TrustManager() throws Exception {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
            tmf.init(ks);
            TrustManager tms[] = tmf.getTrustManagers();
            for (int i = 0; i < tms.length; i++) {
                if (tms[i] instanceof X509TrustManager) {
                    sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                    return;
                }
            }
            throw new Exception("Couldn't not initialize");
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            try {
                sunJSSEX509TrustManager.checkClientTrusted(x509Certificates, s);
            } catch (Exception e) {

            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            try {
                sunJSSEX509TrustManager.checkServerTrusted(x509Certificates, s);
            } catch (Exception e) {

            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return sunJSSEX509TrustManager.getAcceptedIssuers();
        }

    }
}
