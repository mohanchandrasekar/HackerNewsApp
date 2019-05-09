package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hackernewsapp.model.Comment;
import com.hackernewsapp.repository.NewsApiRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel extends ViewModel {

    private NewsApiRepository mNewsApiRepository;
    private List<Comment> mCommentArrayList = new ArrayList<>();
    private MutableLiveData<List<Comment>> mCommentListObservable = new MutableLiveData<>();
    private Handler mHandlerThread = new Handler();

    public MutableLiveData<List<Comment>> getmCommentListObservable() {
        return mCommentListObservable;
    }

    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

    private MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();

    CommentViewModel(@NonNull Context context,
                     @NonNull ArrayList<Integer> kidsList) {
        mNewsApiRepository = new NewsApiRepository();
        if (kidsList.size() > 0) {
            getComments(kidsList);
        }
    }

    private void getComments(@NonNull ArrayList<Integer> mKidsList) {
        for (int i = 0; i < mKidsList.size(); i++) {
            getCommentDetails(mKidsList.get(i));
        }
        mHandlerThread.postDelayed(() -> {
            isApiCallFinished.setValue(true);
            Log.e("Vive", "Comment Size = " + mCommentArrayList.size());
            mCommentListObservable.setValue(mCommentArrayList);
        }, 1000);
    }

    private void getCommentDetails(@NonNull Integer commentsItem) {
        mNewsApiRepository.getNewsService().getCommentItem(String.valueOf(commentsItem)).
                enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        simulateDelay();
                        Comment comments = response.body();
                        //Log.e("Vive", "Comment id = " + comments.getId());
                        mCommentArrayList.add(comments);
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
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
