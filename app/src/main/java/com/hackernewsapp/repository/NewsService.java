package com.hackernewsapp.repository;

import com.hackernewsapp.model.Comment;
import com.hackernewsapp.model.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsService {

    String HTTPS_API = "https://hacker-news.firebaseio.com";

    /**
     * Return a list of the latest post IDs.
     */
    @GET("/v0/topstories.json")
    Call<List<Long>> getTopStories();

    /**
     * Return story item.
     */
    @GET("/v0/item/{itemId}.json")
    Call<Story> getStoryItem(@Path("itemId") String itemId);

    /**
     * Returns a comment item.
     */
    @GET("/v0/item/{itemId}.json")
    Call<Comment> getCommentItem(@Path("itemId") String itemId);
}

