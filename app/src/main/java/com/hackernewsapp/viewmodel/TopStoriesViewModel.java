package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hackernewsapp.model.Story;
import com.hackernewsapp.repository.NewsApiRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for retain the value while rotating the screen and business logic implementation
 */
public class TopStoriesViewModel extends ViewModel {
    private NewsApiRepository mNewsApiRepository;
    private MutableLiveData<List<Story>> mStoryListObservable = new MutableLiveData<>();
    private MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();
    private List<Story> mStoryArrayList = new ArrayList<>();
    private Story mStory;

    TopStoriesViewModel(@NonNull Context context) {
        mNewsApiRepository = new NewsApiRepository();
        getTopStoryList();
    }

    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

    public MutableLiveData<List<Story>> getStoryListObservable() {
        return mStoryListObservable;
    }

    public void getTopStoryList() {
        mNewsApiRepository.getNewsService().getTopStories().enqueue(new Callback<List<Long>>() {
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
        mNewsApiRepository.getNewsService().getStoryItem(String.valueOf(storyItem)).
                enqueue(new Callback<Story>() {
                    @Override
                    public void onResponse(Call<Story> call, Response<Story> response) {
                        simulateDelay();
                        mStory = response.body();
                        Log.e("Mohan", "Story Title" + mStory.getTitle());
                        if (mStory != null)
                            mStoryArrayList.add(mStory);
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
