package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hackernewsapp.repository.NewsApiRepository;

import java.util.ArrayList;

/**
 * ViewModelFactory is responsible for manage the different viewModel lifecycle
 */
public class CommonViewModelFactory implements ViewModelProvider.Factory {
    private Context mContext;
    private ArrayList<Integer> mKidsList;
    private NewsApiRepository mNewsApiRepository;

    public CommonViewModelFactory(@NonNull Context applicationContext, @NonNull ArrayList<Integer> kidsList,
                                  @NonNull NewsApiRepository newsApiRepository) {
        this.mNewsApiRepository = newsApiRepository;
        this.mContext = applicationContext;
        this.mKidsList = kidsList;
    }

    public CommonViewModelFactory(@NonNull Context applicationContext, @NonNull NewsApiRepository newsApiRepository) {
        this.mNewsApiRepository = newsApiRepository;
        this.mContext = applicationContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CommentViewModel.class) {
            return (T) new CommentViewModel(mContext, mKidsList, mNewsApiRepository);
        } else if (modelClass == TopStoriesViewModel.class) {
            return (T) new TopStoriesViewModel(mContext, mNewsApiRepository);
        }
        throw new RuntimeException(
                "ViewModelFactory doesn't know how to create: " + modelClass.toString());
    }
}
