package com.hackernewsapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.hackernewsapp.adapter.StoryAdapter;
import com.hackernewsapp.model.Story;
import com.hackernewsapp.ui.CommentFragment;
import com.hackernewsapp.viewmodel.CommonViewModelFactory;
import com.hackernewsapp.viewmodel.TopStoriesViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.hackernewsapp.ui.CommentFragment.forProject;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    /* Swipe to Referesh the updated story info list from the api  */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    /*TopStoriesViewModel is responsible for return the value based on Observable mannaer*/
    private TopStoriesViewModel mTopStoriesViewModel;
    /* ProgressBar loading wait until result return from Viewmodel */
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupRecyclerView();

        mTopStoriesViewModel = ViewModelProviders.of(this,
                new CommonViewModelFactory(getApplicationContext())).get(TopStoriesViewModel.class);

        mTopStoriesViewModel.getIsApiCallFinished().observe(this, this::onProgressBarChanged);

        mTopStoriesViewModel.getStoryListObservable().observe(this,
                MainActivity.this::updateStoryList);
    }

    /**
     * Initilize the the view from the layout
     */
    private void initView() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mRecyclerView = findViewById(R.id.recycler_top_stories);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    /**
     * Updated the top story info list in recycler adapter
     *
     * @param mStoryList which return the updated story list from api
     */
    private void updateStoryList(List<Story> mStoryList) {
        StoryAdapter mStoryAdapter = new StoryAdapter(
                MainActivity.this, mStoryList);
        mRecyclerView.setAdapter(mStoryAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * View specification in linear layout
     */
    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * Shows the comment list in recyclerview on CommentFragment
     * every click action on list item by user, it will have kids id which contain comment information
     * that is updated in Listview on CommentFragment
     *
     * @param commentList which contain comment list id
     */
    public void show(ArrayList<Integer> commentList) {
        CommentFragment commentFragment = forProject(commentList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, commentFragment, "Comment").addToBackStack("project").commit();
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        mTopStoriesViewModel.getTopStoryList();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * progress bar loading is showing based on api result
     *
     * @param isFinished api finish status is updated based on api call finish from TopStoriesViewModel
     */
    private void onProgressBarChanged(Boolean isFinished) {
        if (isFinished) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}



