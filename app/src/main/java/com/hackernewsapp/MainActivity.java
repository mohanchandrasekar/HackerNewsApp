package com.hackernewsapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hackernewsapp.adapter.StoryAdapter;
import com.hackernewsapp.model.Story;
import com.hackernewsapp.viewmodel.TopStoriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopStoriesViewModel mTopStoriesViewModel;
    private StoryAdapter mStoryAdapter;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mRecyclerView = findViewById(R.id.recycler_top_stories);
        mProgressBar =  findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        setupRecyclerView();

        mTopStoriesViewModel = ViewModelProviders.of(this).get(TopStoriesViewModel.class);

        mTopStoriesViewModel.getIsApiCallFinished().observe(this, isFinished -> {
            if (isFinished) {
                Log.e("vvv", "Visible is gone");
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

        mTopStoriesViewModel.getStoryListObservable().observe(this,
                new Observer<List<Story>>() {
                    @Override
                    public void onChanged(@Nullable List<Story> story) {
                        Log.e("yyy", "Story size = " + story.size());
                        MainActivity.this.updateStoryList(story);
                    }
                });
    }

    private void updateStoryList(List<Story> mStoryList) {
        mStoryAdapter = new StoryAdapter(
                getApplicationContext(), mStoryList);
        mRecyclerView.setAdapter(mStoryAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * LinearListView setup
     */
    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onRefresh() {
        Log.e(TAG, "OnReferesh is Called");
        mTopStoriesViewModel.getTopStoryList();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

