package com.fenghuo.http;

import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class TrustAllSSLSocketFactory extends SSLSocketFactory {
    private javax.net.ssl.SSLSocketFactory factory;

    public class TrustAllManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public TrustAllSSLSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        super(null);
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, new TrustManager[]{new TrustAllManager()}, null);
            this.factory = instance.getSocketFactory();
            setHostnameVerifier(new AllowAllHostnameVerifier());
        } catch (Exception e) {
            Log.e("pushmail", "TrustAllSSLSocketFactory.TrustAllSSLSocketFactory:" + e.getMessage());
        }
    }

    public static SocketFactory getDefault() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return new TrustAllSSLSocketFactory();
    }

    public Socket createSocket() throws IOException {
        return this.factory.createSocket();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return this.factory.createSocket(socket, str, i, z);
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return this.factory.createSocket(inetAddress, i, inetAddress2, i2);
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return this.factory.createSocket(inetAddress, i);
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return this.factory.createSocket(str, i, inetAddress, i2);
    }

    public Socket createSocket(String str, int i) throws IOException {
        return this.factory.createSocket(str, i);
    }

    public String[] getDefaultCipherSuites() {
        return this.factory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.factory.getSupportedCipherSuites();
    }
}
