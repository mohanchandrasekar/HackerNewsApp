package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hackernewsapp.NewsApplication;
import com.hackernewsapp.data.Story;
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
    /* NewsApiRepository is give instance of NewsService*/
    private NewsApiRepository mNewsApiRepository;
    /* mCommentListObservable is a MutableLiveData which responsible for send updated list to subscriber */
    private MutableLiveData<List<Story>> mStoryListObservable = new MutableLiveData<>();
    /*update apifinish liveData to subscriber*/
    private MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();
    /* updated story information in list*/
    private List<Story> mStoryArrayList = new ArrayList<>();

    TopStoriesViewModel(@NonNull Context context, @NonNull NewsApiRepository newsApiRepository) {
        mNewsApiRepository = newsApiRepository;
        getTopStoryList();
    }

    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

    public MutableLiveData<List<Story>> getStoryListObservable() {
        return mStoryListObservable;
    }

    /**
     * Retrofit api call for get the topStoriesId as list
     * Story list which contain the id and id is used to get the story details
     */
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
                mStoryListObservable.setValue(mStoryArrayList);
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {
                //TODO: Need to Handle Error
            }
        });
    }

    /**
     * retrofit api call for get the Story details from the webservice
     *
     * @param storyItem id
     */
    private void getStoryDetails(Long storyItem) {
        mNewsApiRepository.getNewsService().getStoryItem(String.valueOf(storyItem)).
                enqueue(new Callback<Story>() {
                    @Override
                    public void onResponse(Call<Story> call, Response<Story> response) {
                        simulateDelay();
                        Story story = response.body();
                        Log.e("Mohan", "Story Title" + story.getTitle());
                        if (story != null)
                            mStoryArrayList.add(story);
                    }

                    @Override
                    public void onFailure(Call<Story> call, Throwable t) {
                        // TODO better error handling
                    }
                });
    }

    /**
     * delay for network call
     */
    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
