package com.raredevz.eventivo.ML;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


//************************************************
public class ServiceGenerator
//************************************************
{
    static Retrofit mRetroFit;

    //**************************************
    public static Retrofit getRetrofitInstance(boolean isMultipartCall, String baseUrl)
    //**************************************
    {
        if (mRetroFit == null || true)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();
            if (isMultipartCall)
            {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .build();
            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            mRetroFit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return mRetroFit;
    }

    //**************************************
    public static <S> S createService(Class<S> serviceClass)
    //**************************************
    {

        return getRetrofitInstance(false,
                                   "https://ussouthcentral.services.azureml.net/workspaces/10f71f37ebcf4094a70993a12ac5fa23/services/63f011e961744bd998d7b7f683e94fdb/").create(
                serviceClass);
    }

    //**************************************
    public static <S> S createService(Class<S> serviceClass, boolean isMultipartCall, String baseUrl)
    //**************************************
    {

        return getRetrofitInstance(isMultipartCall, baseUrl).create(serviceClass);
    }

}
