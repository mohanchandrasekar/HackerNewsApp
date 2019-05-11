package com.hackernewsapp.repository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NewApiRepositoryClass is used to create instance for NewsService for api call
 */
public class NewsApiRepository {

    private NewsService mNewsService;

    public NewsApiRepository() {

    }

    /**
     * Singleton Instance for api call creation
     *
     * @return NewsService instance
     */
    public NewsService getNewsService() {
        if (mNewsService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NewsService.HTTPS_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mNewsService = retrofit.create(NewsService.class);
        }
        return mNewsService;
    }
}
