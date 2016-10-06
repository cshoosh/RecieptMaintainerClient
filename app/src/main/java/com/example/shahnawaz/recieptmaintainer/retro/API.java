package com.example.shahnawaz.recieptmaintainer.retro;

import android.view.View;
import android.widget.ProgressBar;

import com.example.shahnawaz.recieptmaintainer.BuildConfig;
import com.example.shahnawaz.recieptmaintainer.MainActivity;
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
                        .addHeader("Auth", BuildConfig.AUTH).build();
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
                .enqueue(new CustomCallback<>(res));
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
        mRetro.create(APIInterface.class).calculate().enqueue(new CustomCallback<>(res));
    }

    public void getDebit(final ListResponse<List<Data>> res) {
        mRetro.create(APIInterface.class).viewDebit()
                .enqueue(new CustomCallback<>(res));
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

    public class CustomCallback<T> implements Callback<T> {
        private ListResponse<T> mResponse;
        private ProgressBar mProgressBar;

        public CustomCallback(ListResponse<T> response) {
            mResponse = response;
            mProgressBar = MainActivity.getProgressBar();

            if (mProgressBar != null)
                mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.body() != null)
                mResponse.onResponse(response.body());
            else
                mResponse.onFailure();

            if (mProgressBar != null)
                mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            mResponse.onFailure();
            if (mProgressBar != null)
                mProgressBar.setVisibility(View.GONE);
        }
    }

    public interface ListResponse<T> {
        void onResponse(T data);

        void onFailure();
    }
}
