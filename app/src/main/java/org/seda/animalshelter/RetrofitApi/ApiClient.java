package org.seda.animalshelter.RetrofitApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final  String BASE_URL = "https://animalshelterkz.000webhostapp.com/users/";

    public static Retrofit retrofit = null;


    public static OkHttpClient getHttp(){

                try {

                    final TrustManager[] trustAllCerts = new TrustManager[] {
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return new java.security.cert.X509Certificate[]{};
                                }
                            }
                    };


                    final SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());


                    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                    OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(true);
                    builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
                    builder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

                    builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

                    OkHttpClient okHttpClient = builder.build();
                    return okHttpClient;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

    }

    public static Retrofit getApiClient(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(getHttp()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }






}
