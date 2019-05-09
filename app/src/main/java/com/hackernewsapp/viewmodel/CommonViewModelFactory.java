package com.hackernewsapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class CommonViewModelFactory implements ViewModelProvider.Factory {
    private Context mContext;
    private ArrayList<Integer> mKidsList;

    public CommonViewModelFactory(@NonNull Context applicationContext,@NonNull  ArrayList<Integer> kidsList) {
        this.mContext = applicationContext;
        this.mKidsList = kidsList;
    }

    public CommonViewModelFactory(@NonNull Context applicationContext) {
        this.mContext = applicationContext;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CommentViewModel.class) {
            return (T) new CommentViewModel(mContext, mKidsList);
        } else if (modelClass == TopStoriesViewModel.class) {
            return (T) new TopStoriesViewModel(mContext);
        }
        throw new RuntimeException(
                "ViewModelFactory doesn't know how to create: " + modelClass.toString());
    }
}
