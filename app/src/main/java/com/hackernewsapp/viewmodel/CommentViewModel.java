package com.hackernewsapp.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hackernewsapp.data.Comment;
import com.hackernewsapp.repository.NewsApiRepository;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CommentViewModel is resposnible for manage the business logic of api call for Commentlist
 * and update the commentlist and apifinish in live data to activity or fragment
 */
public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    /* NewsApiRepository is give instance of NewsService*/
    private final NewsApiRepository mNewsApiRepository;
    /* updated comment information in list*/
    private final List<Comment> mCommentArrayList = new ArrayList<>();
    /* mCommentListObservable is a MutableLiveData which responsible for send updated list to subscriber */
    private final MutableLiveData<List<Comment>> mCommentListObservable = new MutableLiveData<>();
    /* Handler thread for the api status*/
    private final Handler mHandlerThread = new Handler();
    /*update apifinish liveData to subscriber*/
    private final MutableLiveData<Boolean> isApiCallFinished = new MutableLiveData<>();
    private final ArrayList<Integer> mKidsIdList;

    CommentViewModel(@NonNull ArrayList<Integer> kidsList, @NonNull NewsApiRepository newsApiRepository) {
        mNewsApiRepository = newsApiRepository;
        mKidsIdList = kidsList;
        Flowable<Integer> mFlowableKids = Flowable.create(this::onKidsIdChanged, BackpressureStrategy.LATEST);

        mFlowableKids.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(getCommentList());
    }

    private void onKidsIdChanged(FlowableEmitter<Integer> flowableEmitter) {
        try {
            for (int i = 0; i < mKidsIdList.size(); i++) {
                flowableEmitter.onNext(mKidsIdList.get(i));
            }
            flowableEmitter.onComplete();
        } catch (Exception e) {
            flowableEmitter.onError(e);
        }
    }

    /**
     * Retrofit api call to get the commentDetails from the webservices
     */
    private FlowableSubscriber<? super Integer> getCommentList() {
        return new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer kidsId) {
                getCommentDetails(kidsId);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError : " + t.getMessage());
            }

            @Override
            public void onComplete() {
                mHandlerThread.postDelayed(() -> {
                isApiCallFinished.setValue(true);
                Log.e(TAG, "Comment Size = " + mCommentArrayList.size());
                mCommentListObservable.setValue(mCommentArrayList);
                }, 1000);
            }
        };

    }
    /**
     * Retrofit api call for get the commentDetails from webservice
     *
     * @param commentsItem commentsId
     */
    private void getCommentDetails(@NonNull Integer commentsItem) {
        mNewsApiRepository.getNewsService().getCommentItem(String.valueOf(commentsItem)).
                enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        simulateDelay();
                        Comment comments = response.body();
                        mCommentArrayList.add(comments);
                        Log.e("Vive", "Comment Size = " + mCommentArrayList.size());
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
     *
     * @return mCommentListObservable
     */
    public MutableLiveData<List<Comment>> getmCommentListObservable() {
        return mCommentListObservable;
    }

    /**
     * return updated finishapi call status to subscriber
     *
     * @return isApiCallFinished
     */
    public MutableLiveData<Boolean> getIsApiCallFinished() {
        return isApiCallFinished;
    }

    /**
     * update commentlist
     * @param commentList
     */
    public void updateCommentList(List<Comment> commentList) {
        this.mCommentListObservable.setValue(commentList);
    }
}
