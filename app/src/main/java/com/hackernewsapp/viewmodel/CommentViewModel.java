package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hackernewsapp.NewsApplication;
import com.hackernewsapp.data.Comment;
import com.hackernewsapp.repository.NewsApiRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CommentViewModel is resposnible for manage the business logic of api call for Commentlist
 * and update the commentlist and apifinish in live data to activity or fragment
 */
public class CommentViewModel extends ViewModel {
    /* NewsApiRepository is give instance of NewsService*/
    private NewsApiRepository mNewsApiRepository;
    /* updated comment information in list*/
    private List<Comment> mCommentArrayList = new ArrayList<>();
    /* mCommentListObservable is a MutableLiveData which responsible for send updated list to subscriber */
    private MutableLiveData<List<Comment>> mCommentListObservable = new MutableLiveData<>();
    /* Handler thread for the api status*/
    private Handler mHandlerThread = new Handler();
    /*update apifinish liveData to subscriber*/
    private MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();
    private int mCount = 0;

    CommentViewModel(@NonNull Context context,
                     @NonNull ArrayList<Integer> kidsList, @NonNull  NewsApiRepository newsApiRepository) {
        mNewsApiRepository = newsApiRepository ;
        if (kidsList.size() > 0) {
            getComments(kidsList);
        }
    }

    private void getComments(@NonNull ArrayList<Integer> mKidsList) {
        int size = mKidsList.size();
        for (int i = 0; i < size; i++) {
            getCommentDetails(mKidsList.get(i));
        }
        mHandlerThread.postDelayed(() -> {
            isApiCallFinished.setValue(true);
            Log.e("Vive", "Comment Size = " + mCommentArrayList.size());
            mCommentListObservable.setValue(mCommentArrayList);
        }, 1000);
    }

    /**
     * Retrofit api call for get the commentDetails from webservice
     * @param commentsItem commentsId
     */
    private void getCommentDetails(@NonNull Integer commentsItem) {
        mNewsApiRepository.getNewsService().getCommentItem(String.valueOf(commentsItem)).
                enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        mCount++;
                        simulateDelay();
                        Comment comments = response.body();
                        mCommentArrayList.add(comments);
                        Log.e("Vive", "comments = " + mCommentArrayList.size());
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        // TODO better error handling
                    }
                });
    }

    /**
     * Delay for api call
     */
    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * return the updated Commentlist to subscriber
     * @return
     */
    public MutableLiveData<List<Comment>> getmCommentListObservable() {
        return mCommentListObservable;
    }

    /**
     * return updated finishapi call status to subscriber
     * @return
     */
    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

}
