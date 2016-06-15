package com.example.shahnawaz.recieptmaintainer.retro;

import com.example.shahnawaz.recieptmaintainer.model.CalculateModel;
import com.example.shahnawaz.recieptmaintainer.model.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Shahnawaz on 6/13/2016.
 */
public class API {
    private static final String DEV_BASE_URL = "http://api.image1animation.com/";
    private Retrofit mRetro;

    private Callback<String> callback = new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> res) {
            if (response != null && res != null && res.body() != null && res.body().equals("1")) {
                response.onResponse(res.body());
            } else {
                if (response != null)
                    response.onFailure();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            if (response != null)
                response.onFailure();
        }
    };

    private ListResponse<String> response;

    public API() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Auth", "eu3euQ81z0AwMxeHSb3d78L5TX83vkp3").build();
                return chain.proceed(request);
            }
        }).build();

        mRetro = new Retrofit.Builder()
                .baseUrl(DEV_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public void getCredit(final ListResponse<List<Data>> res) {
        mRetro.create(APIInterface.class).viewCredit()
                .enqueue(new Callback<List<Data>>() {
                    @Override
                    public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                        if (response.body() != null && !response.body().isEmpty())
                            res.onResponse(response.body());
                        else
                            res.onFailure();
                    }

                    @Override
                    public void onFailure(Call<List<Data>> call, Throwable t) {
                        res.onFailure();
                    }
                });
    }

    public void delete(ListResponse<String> res, int id) {
        response = res;
        mRetro.create(APIInterface.class).delete(id).enqueue(callback);
    }

    public void addUpdate(final ListResponse<String> res, final HashMap<String, String> map, boolean isUpdate) {
        response = res;
        Call<String> call;
        if (isUpdate) {
            call = mRetro.create(APIInterface.class).update(map);
        } else {
            call = mRetro.create(APIInterface.class).insert(map);
        }

        call.enqueue(callback);
    }

    public void getCalculatedData(final ListResponse<CalculateModel> res) {
        mRetro.create(APIInterface.class).calculate().enqueue(new Callback<CalculateModel>() {
            @Override
            public void onResponse(Call<CalculateModel> call, Response<CalculateModel> response) {
                if (response.body() != null)
                    res.onResponse(response.body());
                else
                    res.onFailure();
            }

            @Override
            public void onFailure(Call<CalculateModel> call, Throwable t) {
                res.onFailure();
            }
        });
    }

    public void getDebit(final ListResponse<List<Data>> res) {
        mRetro.create(APIInterface.class).viewDebit()
                .enqueue(new Callback<List<Data>>() {
                    @Override
                    public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                        if (response.body() != null && !response.body().isEmpty())
                            res.onResponse(response.body());
                        else
                            res.onFailure();
                    }

                    @Override
                    public void onFailure(Call<List<Data>> call, Throwable t) {
                        res.onFailure();
                    }
                });
    }

    public interface APIInterface {
        @GET("receipt/json/0")
        Call<List<Data>> viewCredit();

        @GET("receipt/json/1")
        Call<List<Data>> viewDebit();

        @FormUrlEncoded
        @POST("receipt/update")
        Call<String> update(@FieldMap Map<String, String> options);

        @FormUrlEncoded
        @POST("receipt/insert")
        Call<String> insert(@FieldMap Map<String, String> options);

        @GET("receipt/delete/{id}")
        Call<String> delete(@Path("id") int id);

        @GET("receipt/calculate/json")
        Call<CalculateModel> calculate();
    }

    public interface ListResponse<T> {
        void onResponse(T data);

        void onFailure();
    }
}
