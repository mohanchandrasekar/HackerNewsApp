package com.hackernewsapp.repository;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiRepository {
    private NewsService mNewsService;

    public NewsApiRepository(){

    }

    public NewsService getNewsService() {
        if (mNewsService == null) {
            Log.e("NewsApiRepository", "getNewsService is called");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NewsService.HTTPS_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mNewsService = retrofit.create(NewsService.class);
        }
        return mNewsService;
    }
}
