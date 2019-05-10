package com.hackernewsapp.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hackernewsapp.repository.NewsApiRepository;

import java.util.ArrayList;

/**
 * ViewModelFactory is responsible for manage the different viewModel lifecycle
 */
public class CommonViewModelFactory implements ViewModelProvider.Factory {
    private ArrayList<Integer> mKidsList;
    private NewsApiRepository mNewsApiRepository;

    public CommonViewModelFactory(@NonNull ArrayList<Integer> kidsList,
                                  @NonNull NewsApiRepository newsApiRepository) {
        this.mNewsApiRepository = newsApiRepository;
        this.mKidsList = kidsList;
    }

    public CommonViewModelFactory(@NonNull NewsApiRepository newsApiRepository) {
        this.mNewsApiRepository = newsApiRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CommentViewModel.class) {
            return (T) new CommentViewModel(mKidsList, mNewsApiRepository);
        } else if (modelClass == TopStoriesViewModel.class) {
            return (T) new TopStoriesViewModel(mNewsApiRepository);
        }
        throw new RuntimeException(
                "ViewModelFactory doesn't know how to create: " + modelClass.toString());
    }
}
