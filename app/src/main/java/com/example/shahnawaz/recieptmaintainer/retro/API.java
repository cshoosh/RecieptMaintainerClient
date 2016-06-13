package com.example.shahnawaz.recieptmaintainer.retro;

import com.example.shahnawaz.recieptmaintainer.model.Data;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Shahnawaz on 6/13/2016.
 */
public class API {
    private static final String DEV_BASE_URL = "http://api.image1animation.com";
    private Retrofit mRetro;

    public API() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "eu3euQ81z0AwMxeHSb3d78L5TX83vkp4").build();
                return chain.proceed(request);
            }
        }).build();

        mRetro = new Retrofit.Builder()
                .baseUrl(DEV_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public void getAll(final ListResponse res) {
        mRetro.create(APIInterface.class).view()
                .enqueue(new Callback<List<Data>>() {
                    @Override
                    public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                        res.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Data>> call, Throwable t) {

                    }
                });
    }

    public interface APIInterface {
        @GET("receipt/")
        Call<List<Data>> view();
    }

    public interface ListResponse {
        void onResponse(List<Data> data);
    }
}
