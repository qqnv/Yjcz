package com.zzlecheng.yjcz.utils;

import android.content.Context;

import com.zzlecheng.yjcz.base.Commons;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 *
 */

public class OkHttpClientUtil {

    private static final long DEFAULT_TIMEOUT = Commons.POST_DATA_TIMEOUT;


    static  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
        }
    });
    /**
     * 配置Https
     */
    public static OkHttpClient getUnsafeOkHttpClient(final Context context) {
//        final SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
//        final RecordOfOperationHelper mRecordOfOperationHelper = new RecordOfOperationHelper(context);
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .header("token", SpUtil.getInstance().getString(Commons.SP_TOKEN))
//                        .build();
////                LogUtils.e("ok_token--"+SpUtil.getInstance().getString(Commons.SP_TOKEN));
//                return chain.proceed(request);
//            }
//        });
//        if (sslSocketFactory != null) {
//            builder.sslSocketFactory(sslSocketFactory);
//        }
//        builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        OkHttpClient client = builder.build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return client;
    }

//    private static SSLSocketFactory getSSLSocketFactory() {
//
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            // 从assets中加载证书
//            InputStream inStream = MainApplication.getInstance().getAssets().open("su.cer");
//            // 证书工厂
//            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
//            Certificate cer = cerFactory.generateCertificate(inStream);
//            // 密钥库
//            KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            kStore.load(null, null);
//            kStore.setCertificateEntry("mykey", cer);// 加载证书到密钥库中
//            // 密钥管理器
//            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyFactory.init(kStore, null);// 加载密钥库到管理器
//            // 信任管理器
//            TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tFactory.init(kStore);// 加载密钥库到信任管理器
//            // 初始化
//            sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());
//            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
//            return socketFactory;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
