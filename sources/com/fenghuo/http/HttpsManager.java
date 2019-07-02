package com.fenghuo.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsManager {
    private HttpsURLConnection connection = null;

    private static class MyHostnameVerifier implements HostnameVerifier {
        private MyHostnameVerifier() {
        }

        public boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    }

    private static class MyTrustManager implements X509TrustManager {
        private MyTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public HttpsManager(String str) {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
            this.connection = (HttpsURLConnection) new URL(str).openConnection();
            this.connection.setDoOutput(true);
            this.connection.setDoInput(true);
            this.connection.setConnectTimeout(30000);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e2) {
            e2.printStackTrace();
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int uploadFile() {
        /*
        r8 = this;
        r0 = 0;
        r1 = r8.connection;
        r1.setUseCaches(r0);
        r1 = r8.connection;	 Catch:{ ProtocolException -> 0x015b }
        r2 = "POST";
        r1.setRequestMethod(r2);	 Catch:{ ProtocolException -> 0x015b }
    L_0x000d:
        r1 = r8.connection;
        r2 = "Charset";
        r3 = "utf-8";
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "connection";
        r3 = "close";
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "esn";
        r3 = com.fenghuo.utils.Global.getGlobal();
        r3 = r3.imei_;
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "imsi";
        r3 = com.fenghuo.utils.Global.getGlobal();
        r3 = r3.imsi_;
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "model";
        r3 = com.fenghuo.utils.Global.getGlobal();
        r3 = r3.loadModel();
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "User-Agent";
        r3 = "";
        r1.setRequestProperty(r2, r3);
        r1 = r8.connection;
        r2 = "Content-Type";
        r3 = "multipart/form-data; boundary=-----------------------7d91e4315304ba";
        r1.setRequestProperty(r2, r3);
        r1 = "UTF-8";
        r2 = new java.io.DataOutputStream;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = r8.connection;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = r3.getOutputStream();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.<init>(r3);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = new java.io.File;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = com.fenghuo.utils.Global.zipEsnPath_;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3.<init>(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r3.exists();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        if (r4 == 0) goto L_0x015a;
    L_0x0074:
        r4 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = "WIFI_";
        r4 = r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = com.fenghuo.utils.Global.getGlobal();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = r5.loadModel();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = ".zip";
        r4 = r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.toString();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = java.lang.System.out;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r7 = "filename.................=";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = r6.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = r6.toString();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5.println(r6);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = "-------------------------7d91e4315304ba\r\n";
        r5 = r5.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.write(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = "Content-Disposition: form-data; name=\"";
        r5 = r5.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = r5.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = "\"; filename=\"";
        r5 = r5.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r5.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = "\"\r\n";
        r4 = r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.toString();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.write(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r3.getAbsolutePath();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = com.fenghuo.utils.Util.getContentTypeByFilepath(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r6 = "Content-Type: ";
        r5 = r5.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r5.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r5 = "\r\n\r\n";
        r4 = r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.toString();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = r4.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.write(r4);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4 = new java.io.FileInputStream;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4.<init>(r3);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r3 = new byte[r3];	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
    L_0x0111:
        r5 = r4.read(r3);	 Catch:{ Exception -> 0x011c, UnsupportedEncodingException -> 0x0161, IOException -> 0x0166 }
        if (r5 < 0) goto L_0x011d;
    L_0x0117:
        r6 = 0;
        r2.write(r3, r6, r5);	 Catch:{ Exception -> 0x011c, UnsupportedEncodingException -> 0x0161, IOException -> 0x0166 }
        goto L_0x0111;
    L_0x011c:
        r3 = move-exception;
    L_0x011d:
        r3 = "\r\n";
        r3 = r3.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.write(r3);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = "-------------------------7d91e4315304ba--\r\n";
        r1 = r3.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.flush();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.close();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r4.close();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r1 = r8.connection;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r0 = r1.getResponseCode();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r1) goto L_0x0142;
    L_0x0142:
        r1 = java.lang.System.out;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r3 = "result=";
        r2 = r2.append(r3);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2 = r2.append(r0);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r2 = r2.toString();	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
        r1.println(r2);	 Catch:{ UnsupportedEncodingException -> 0x0161, IOException -> 0x0166, Exception -> 0x016b }
    L_0x015a:
        return r0;
    L_0x015b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000d;
    L_0x0161:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x015a;
    L_0x0166:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x015a;
    L_0x016b:
        r1 = move-exception;
        goto L_0x015a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fenghuo.http.HttpsManager.uploadFile():int");
    }
}
