package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.hackernewsapp.model.Story;
import com.hackernewsapp.repository.NewsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ViewModel for retain the value while rotating the screen and business logic implementation
 */
public class TopStoriesViewModel extends ViewModel {
    private final NewsService mNewsService;
    private MutableLiveData<List<Story>> mStoryListObservable = new MutableLiveData<List<Story>>();
    private MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();
    private List<Story> mStoryArrayList = new ArrayList<>();
    private Story story;

    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

    public MutableLiveData<List<Story>> getStoryListObservable() {
        return mStoryListObservable;
    }

    public TopStoriesViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsService.HTTPS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNewsService = retrofit.create(NewsService.class);
        getTopStoryList();
    }

    public void getTopStoryList() {
        mNewsService.getTopStories().enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                List<Long> storyList = response.body();
                for (int i = 0; i < storyList.size(); i++) {
                    Long storyId = storyList.get(i);
                    getStoryDetails(storyId);
                }
                isApiCallFinished.setValue(true);
                Log.e("Mohan", "Story size = " + mStoryArrayList.size());
                mStoryListObservable.setValue(mStoryArrayList);
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {
                //TODO: Need to Handle Error
            }
        });
    }

    private void getStoryDetails(Long storyItem) {
        mNewsService.getStoryItem(String.valueOf(storyItem)).
                enqueue(new Callback<Story>() {
                    @Override
                    public void onResponse(Call<Story> call, Response<Story> response) {
                        simulateDelay();
                        story = response.body();
                        Log.e("Mohan", "Story Title" + story.getTitle());
                        if (story != null)
                            mStoryArrayList.add(story);
                    }

                    @Override
                    public void onFailure(Call<Story> call, Throwable t) {
                        // TODO better error handling
                        //mStoryListObservable.setValue(null);
                    }
                });
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
