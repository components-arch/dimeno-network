package com.dimeno.network.manager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSLSocketManager trust all https certs
 * Created by wangzhen on 2020/4/15.
 */
public class SSLSocketManager {
    private X509TrustManager trustManager;
    private SSLSocketFactory sslSocketFactory;

    public SSLSocketManager() {
        this.trustManager = new InternalX509TrustManager();
        this.sslSocketFactory = createSSLSocketFactory(trustManager);
    }

    public X509TrustManager getX509TrustManager() {
        return trustManager;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return new InternalHostnameVerifier();
    }

    private static SSLSocketFactory createSSLSocketFactory(TrustManager trustManager) {
        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }

    private static class InternalX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * {@link okhttp3.internal.tls.OkHostnameVerifier}, 校验结果true
     */
    private static class InternalHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
