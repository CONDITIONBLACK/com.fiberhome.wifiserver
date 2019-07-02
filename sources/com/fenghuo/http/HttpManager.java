package com.fenghuo.http;

import com.fenghuo.http.response.ModelResponse;
import com.fenghuo.http.response.RspEvent;
import com.fenghuo.http.response.RspZIPEvent;
import com.fenghuo.utils.Global;
import com.fenghuo.utils.Logger;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public final class HttpManager {
    public static final int CONNECTION_TIMEOUT = 120;
    private static HttpGet ggetMethod = null;
    private static InputStream gis = null;
    private static HttpPost gpostMethod = null;
    private static DefaultHttpClient httpClient;
    public static ArrayList<HttpConnectionQueue> httpqueue;
    private static HttpParams params = new BasicHttpParams();
    private static TrustAllSSLSocketFactory socketFactory = null;
    private boolean canceled = false;

    static {
        httpClient = null;
        httpqueue = null;
        HttpConnectionParams.setConnectionTimeout(params, 120000);
        HttpConnectionParams.setSoTimeout(params, 120000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
        httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
        httpqueue = new ArrayList();
    }

    private HttpManager() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.fenghuo.http.response.RspEvent doGetGcEvent(com.fenghuo.http.request.ReqEvent r10) {
        /*
        r9 = 0;
        r0 = new org.apache.http.impl.client.DefaultHttpClient;
        r1 = params;
        r0.<init>(r1);
        httpClient = r0;
        ggetMethod = r9;
        r0 = r10.getReqUrl();
        r1 = new org.apache.http.client.methods.HttpPost;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1.<init>(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        gpostMethod = r1;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        addHttpHeader();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r10.getCmdType();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = 1;
        if (r0 != r1) goto L_0x002c;
    L_0x0021:
        r0 = "";
        r0 = getHttpFileBody(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = gpostMethod;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1.setEntity(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
    L_0x002c:
        r0 = java.lang.System.currentTimeMillis();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2 = java.lang.System.out;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3.<init>();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r4 = "curTime.......................................=";
        r3 = r3.append(r4);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = r3.append(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = r3.toString();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2.println(r3);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2 = httpClient;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = gpostMethod;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2 = r2.execute(r3);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = r2.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = r3.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r6 = java.lang.System.out;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r7 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r7.<init>();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r8 = "time.......................................=";
        r7 = r7.append(r8);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r4 - r0;
        r0 = r7.append(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r0.toString();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r6.println(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0.<init>();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = "statuscode=";
        r0 = r0.append(r1);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r0.append(r3);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r0.toString();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        com.fenghuo.utils.Logger.m16e(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 != r0) goto L_0x00b7;
    L_0x0090:
        r0 = r2.getEntity();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1.<init>(r2);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0.writeTo(r1);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = r10.getCmdType();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = buildRspEvent(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r2 = new java.lang.String;	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = r1.toByteArray();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r3 = "UTF-8";
        r2.<init>(r1, r3);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0.parserResponse(r2);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
    L_0x00b4:
        ggetMethod = r9;
    L_0x00b6:
        return r0;
    L_0x00b7:
        r0 = r10.getCmdType();	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r0 = buildRspEvent(r0);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = "服务器连接失败，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        r1 = 0;
        r0.setisValid(r1);	 Catch:{ SocketTimeoutException -> 0x00c9, UnknownHostException -> 0x00f8, IOException -> 0x0127, Exception -> 0x0167 }
        goto L_0x00b4;
    L_0x00c9:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01ac }
        r1.<init>();	 Catch:{ all -> 0x01ac }
        r2 = "HttpManager.SocketTimeoutException=";
        r1 = r1.append(r2);	 Catch:{ all -> 0x01ac }
        r0 = r0.getMessage();	 Catch:{ all -> 0x01ac }
        r0 = r1.append(r0);	 Catch:{ all -> 0x01ac }
        r0 = r0.toString();	 Catch:{ all -> 0x01ac }
        com.fenghuo.utils.Logger.m16e(r0);	 Catch:{ all -> 0x01ac }
        r0 = r10.getCmdType();	 Catch:{ all -> 0x01ac }
        r0 = buildRspEvent(r0);	 Catch:{ all -> 0x01ac }
        r1 = "连接服务器超时，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ all -> 0x01ac }
        r1 = 0;
        r0.setisValid(r1);	 Catch:{ all -> 0x01ac }
        ggetMethod = r9;
        goto L_0x00b6;
    L_0x00f8:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01ac }
        r1.<init>();	 Catch:{ all -> 0x01ac }
        r2 = "HttpManager.UnknownHostException=";
        r1 = r1.append(r2);	 Catch:{ all -> 0x01ac }
        r0 = r0.getMessage();	 Catch:{ all -> 0x01ac }
        r0 = r1.append(r0);	 Catch:{ all -> 0x01ac }
        r0 = r0.toString();	 Catch:{ all -> 0x01ac }
        com.fenghuo.utils.Logger.m16e(r0);	 Catch:{ all -> 0x01ac }
        r0 = r10.getCmdType();	 Catch:{ all -> 0x01ac }
        r0 = buildRspEvent(r0);	 Catch:{ all -> 0x01ac }
        r1 = "服务器连接失败，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ all -> 0x01ac }
        r1 = 0;
        r0.setisValid(r1);	 Catch:{ all -> 0x01ac }
        ggetMethod = r9;
        goto L_0x00b6;
    L_0x0127:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01ac }
        r1.<init>();	 Catch:{ all -> 0x01ac }
        r2 = "HttpManager.IOException=";
        r1 = r1.append(r2);	 Catch:{ all -> 0x01ac }
        r0 = r0.getMessage();	 Catch:{ all -> 0x01ac }
        r0 = r1.append(r0);	 Catch:{ all -> 0x01ac }
        r1 = "\n";
        r0 = r0.append(r1);	 Catch:{ all -> 0x01ac }
        r1 = httpClient;	 Catch:{ all -> 0x01ac }
        r1 = r1.toString();	 Catch:{ all -> 0x01ac }
        r0 = r0.append(r1);	 Catch:{ all -> 0x01ac }
        r0 = r0.toString();	 Catch:{ all -> 0x01ac }
        com.fenghuo.utils.Logger.m16e(r0);	 Catch:{ all -> 0x01ac }
        r0 = r10.getCmdType();	 Catch:{ all -> 0x01ac }
        r0 = buildRspEvent(r0);	 Catch:{ all -> 0x01ac }
        r1 = "服务器连接失败，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ all -> 0x01ac }
        r1 = 0;
        r0.setisValid(r1);	 Catch:{ all -> 0x01ac }
        ggetMethod = r9;
        goto L_0x00b6;
    L_0x0167:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01ac }
        r1.<init>();	 Catch:{ all -> 0x01ac }
        r2 = "HttpManager.Exception=";
        r1 = r1.append(r2);	 Catch:{ all -> 0x01ac }
        r2 = r0.getMessage();	 Catch:{ all -> 0x01ac }
        r1 = r1.append(r2);	 Catch:{ all -> 0x01ac }
        r1 = r1.toString();	 Catch:{ all -> 0x01ac }
        com.fenghuo.utils.Logger.m16e(r1);	 Catch:{ all -> 0x01ac }
        r1 = r0.toString();	 Catch:{ all -> 0x01ac }
        r0 = r10.getCmdType();	 Catch:{ all -> 0x01ac }
        r0 = buildRspEvent(r0);	 Catch:{ all -> 0x01ac }
        if (r1 == 0) goto L_0x01a6;
    L_0x0190:
        r2 = "timed out";
        r1 = r1.indexOf(r2);	 Catch:{ all -> 0x01ac }
        r2 = -1;
        if (r1 == r2) goto L_0x01a6;
    L_0x0199:
        r1 = "连接服务器超时，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ all -> 0x01ac }
    L_0x019e:
        r1 = 0;
        r0.setisValid(r1);	 Catch:{ all -> 0x01ac }
        ggetMethod = r9;
        goto L_0x00b6;
    L_0x01a6:
        r1 = "服务器连接失败，请检查网络设置！";
        r0.setDetailMsg(r1);	 Catch:{ all -> 0x01ac }
        goto L_0x019e;
    L_0x01ac:
        r0 = move-exception;
        ggetMethod = r9;
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fenghuo.http.HttpManager.doGetGcEvent(com.fenghuo.http.request.ReqEvent):com.fenghuo.http.response.RspEvent");
    }

    private static String getCmdType(int i) {
        return "";
    }

    public static RspEvent buildRspEvent(int i) {
        switch (i) {
            case 1:
                return new RspZIPEvent();
            case 2:
                return new ModelResponse();
            default:
                return null;
        }
    }

    private static void addHttpHeader() {
        if (gpostMethod != null) {
            gpostMethod.setHeader("esn", Global.getGlobal().imei_);
            gpostMethod.setHeader("imsi", Global.getGlobal().imsi_);
            gpostMethod.setHeader("model", Global.getGlobal().loadModel());
            gpostMethod.setHeader(HTTP.USER_AGENT, "");
            gpostMethod.setHeader(HTTP.CONTENT_TYPE, "multipart/form-data; boundary=-----------------------7d91e4315304ba");
            gpostMethod.setHeader(HTTP.CONN_DIRECTIVE, "close");
        }
    }

    private static HashMap<String, String> getHttpReponseHead(HttpResponse httpResponse) {
        HashMap<String, String> hashMap = new HashMap();
        Header[] allHeaders = httpResponse.getAllHeaders();
        if (allHeaders != null) {
            for (int i = 0; i < allHeaders.length; i++) {
                hashMap.put(allHeaders[i].getName(), allHeaders[i].getValue());
            }
        }
        return hashMap;
    }

    public void cancel() {
        if (!this.canceled) {
            this.canceled = true;
            if (httpClient != null) {
                httpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    public static void cancelConnection(long j) {
        Logger.m18i("HttpManager.cancelConnection=====" + j);
        cancelHttpQueueById(j);
        try {
            if (gis != null) {
                gis.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        if (gpostMethod != null) {
            gpostMethod.abort();
        }
        gpostMethod = null;
    }

    public static void removeHttpQueueById(long j) {
        if (j > 0) {
            try {
                if (httpqueue != null) {
                    int i = 0;
                    while (i < httpqueue.size()) {
                        HttpConnectionQueue httpConnectionQueue = (HttpConnectionQueue) httpqueue.get(i);
                        if (httpConnectionQueue == null || httpConnectionQueue.id_ != j) {
                            i++;
                        } else {
                            httpqueue.remove(i);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.m16e("HttpManager.removeHttpQueueById e=" + e.getMessage());
            }
        }
    }

    public static void cancelHttpQueueById(long j) {
        if (j > 0 && httpqueue != null) {
            int i = 0;
            while (i < httpqueue.size()) {
                HttpConnectionQueue httpConnectionQueue = (HttpConnectionQueue) httpqueue.get(i);
                if (httpConnectionQueue == null || httpConnectionQueue.id_ != j) {
                    i++;
                } else {
                    httpConnectionQueue.canceled_ = true;
                    return;
                }
            }
        }
    }

    public static boolean getCancelHttpQueueById(long j) {
        if (j <= 0 || httpqueue == null) {
            return false;
        }
        for (int i = 0; i < httpqueue.size(); i++) {
            HttpConnectionQueue httpConnectionQueue = (HttpConnectionQueue) httpqueue.get(i);
            if (httpConnectionQueue != null && httpConnectionQueue.id_ == j) {
                return httpConnectionQueue.canceled_;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.apache.http.entity.FileEntity getHttpFileBody(java.lang.String r9) {
        /*
        r0 = 0;
        r1 = com.fenghuo.utils.Global.uploadPath_;
        r2 = new java.io.File;	 Catch:{ Exception -> 0x01a4 }
        r2.<init>(r1);	 Catch:{ Exception -> 0x01a4 }
        if (r2 == 0) goto L_0x0013;
    L_0x000a:
        r3 = r2.exists();	 Catch:{ Exception -> 0x01a4 }
        if (r3 == 0) goto L_0x0013;
    L_0x0010:
        r2.delete();	 Catch:{ Exception -> 0x01a4 }
    L_0x0013:
        r2 = com.fenghuo.utils.Util.createFile(r1);
        r3 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x01a2 }
        r3.<init>(r2);	 Catch:{ FileNotFoundException -> 0x01a2 }
        r4 = "UTF-8";
        r1 = "-------------------------7d91e4315304ba\r\n";
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = "Content-Disposition: form-data; name=\"WIFI_Req_Zip\"\r\n\r\n";
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = "\r\n\r\n";
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r5 = new java.io.File;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = com.fenghuo.utils.Global.zipEsnPath_;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r5.<init>(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = com.fenghuo.utils.Global.getGlobal();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = r1.loadModel();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = com.fenghuo.utils.Global.getGlobal();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.getLocalIP();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        if (r1 == 0) goto L_0x0066;
    L_0x0052:
        r7 = ".";
        r7 = r1.contains(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        if (r7 == 0) goto L_0x0066;
    L_0x005a:
        r7 = 46;
        r7 = r1.lastIndexOf(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = r7 + 1;
        r1 = r1.substring(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
    L_0x0066:
        if (r6 == 0) goto L_0x0178;
    L_0x0068:
        r7 = " ";
        r8 = "_";
        r6 = r6.replace(r7, r8);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "";
        r7 = r6.equals(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        if (r7 != 0) goto L_0x0080;
    L_0x0078:
        r7 = "_";
        r7 = r6.endsWith(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        if (r7 == 0) goto L_0x015f;
    L_0x0080:
        r7 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = r7.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
    L_0x0091:
        r6 = r5.exists();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        if (r6 == 0) goto L_0x0148;
    L_0x0097:
        r1 = com.fenghuo.utils.Util.isContainsInvalidCharacter(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "WIFI_";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = ".zip";
        r1 = r1.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = java.lang.System.out;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r8 = "filename.................=";
        r7 = r7.append(r8);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = r7.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = r7.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6.println(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = "-------------------------7d91e4315304ba\r\n";
        r6 = r6.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "Content-Disposition: form-data; name=\"";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "\"; filename=\"";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = "\"\r\n";
        r1 = r1.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r5.getAbsolutePath();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = com.fenghuo.utils.Util.getContentTypeByFilepath(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "Content-Type: ";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = "\r\n\r\n";
        r1 = r1.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = new java.io.FileInputStream;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1.<init>(r5);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r5 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r5 = new byte[r5];	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
    L_0x0130:
        r6 = r1.read(r5);	 Catch:{ Exception -> 0x013b, UnsupportedEncodingException -> 0x017c, IOException -> 0x0186 }
        if (r6 < 0) goto L_0x013c;
    L_0x0136:
        r7 = 0;
        r3.write(r5, r7, r6);	 Catch:{ Exception -> 0x013b, UnsupportedEncodingException -> 0x017c, IOException -> 0x0186 }
        goto L_0x0130;
    L_0x013b:
        r5 = move-exception;
    L_0x013c:
        r5 = "\r\n";
        r5 = r5.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r5);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1.close();	 Catch:{ Exception -> 0x019c, UnsupportedEncodingException -> 0x017c, IOException -> 0x0186 }
    L_0x0148:
        r1 = "-------------------------7d91e4315304ba--\r\n";
        r1 = r1.getBytes(r4);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.write(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.flush();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r3.close();	 Catch:{ IOException -> 0x019e }
    L_0x0157:
        r0 = new org.apache.http.entity.FileEntity;
        r1 = "multipart/form-data";
        r0.<init>(r2, r1);
    L_0x015e:
        return r0;
    L_0x015f:
        r7 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7.<init>();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r6 = r7.append(r6);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r7 = "_";
        r6 = r6.append(r7);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r6.append(r1);	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        r1 = r1.toString();	 Catch:{ UnsupportedEncodingException -> 0x017c, IOException -> 0x0186, Exception -> 0x0190 }
        goto L_0x0091;
    L_0x0178:
        r1 = "";
        goto L_0x0091;
    L_0x017c:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x0197 }
        r3.close();	 Catch:{ IOException -> 0x0184 }
        goto L_0x015e;
    L_0x0184:
        r1 = move-exception;
        goto L_0x015e;
    L_0x0186:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x0197 }
        r3.close();	 Catch:{ IOException -> 0x018e }
        goto L_0x015e;
    L_0x018e:
        r1 = move-exception;
        goto L_0x015e;
    L_0x0190:
        r1 = move-exception;
        r3.close();	 Catch:{ IOException -> 0x0195 }
        goto L_0x015e;
    L_0x0195:
        r1 = move-exception;
        goto L_0x015e;
    L_0x0197:
        r0 = move-exception;
        r3.close();	 Catch:{ IOException -> 0x01a0 }
    L_0x019b:
        throw r0;
    L_0x019c:
        r1 = move-exception;
        goto L_0x0148;
    L_0x019e:
        r0 = move-exception;
        goto L_0x0157;
    L_0x01a0:
        r1 = move-exception;
        goto L_0x019b;
    L_0x01a2:
        r1 = move-exception;
        goto L_0x015e;
    L_0x01a4:
        r2 = move-exception;
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fenghuo.http.HttpManager.getHttpFileBody(java.lang.String):org.apache.http.entity.FileEntity");
    }
}
