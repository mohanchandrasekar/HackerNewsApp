package com.hackernewsapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hackernewsapp.R;
import com.hackernewsapp.adapter.StoryAdapter;
import com.hackernewsapp.data.Comment;
import com.hackernewsapp.repository.NewsApiRepository;
import com.hackernewsapp.viewmodel.CommentViewModel;
import com.hackernewsapp.viewmodel.CommonViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentFragment extends Fragment {

    private static final String KEY_PROJECT_ID = "comments_id";
    /* CommentViewModel is responsible for get the value in observable nammaner*/
    private CommentViewModel mCommentViewModel;
    /* Recycler commentView for show the comment list item */
    private RecyclerView mCommentRecyclerView;
    /* progress bar loading wait until result return from api*/
    private LinearLayout mLoadingBar;
    /* if no comment list is available it will show no comments*/
    private TextView mNoCommentsText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> kidsList = Objects.requireNonNull(getArguments()).getIntegerArrayList(KEY_PROJECT_ID);
        ArrayList<Integer> newkidsList = kidsList != null && kidsList.size() > 0 ? kidsList : new ArrayList<>();
        mCommentViewModel = ViewModelProviders.of(this,
                new CommonViewModelFactory(newkidsList, new NewsApiRepository())).get(CommentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_comments, container, false);

        mCommentRecyclerView = fragmentView.findViewById(R.id.recycler_comments);
        mLoadingBar = fragmentView.findViewById(R.id.progress_indicator);
        mNoCommentsText = fragmentView.findViewById(R.id.text_no_comments);
        mLoadingBar.setVisibility(ProgressBar.VISIBLE);

        mCommentViewModel.getIsApiCallFinished().observe(this, this::onApiFinishChanged);
        mCommentViewModel.getmCommentListObservable().observe(this, this::onCommentListChanged);
        return fragmentView;
    }

    /**
     * Update the comment adapter from updated lsit from ViewModel
     * @param commentList updated commentList which contain comment info
     */
    private void updateCommentAdapter(List<Comment> commentList) {
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        boolean isComment = true;
        StoryAdapter commentAdapter = new StoryAdapter(Objects.requireNonNull(getActivity()), commentList, isComment);
        mCommentRecyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Creates project fragment for specific kids from CommentsList
     *
     * @param commentsList whiech contains kids list
     */
    public static CommentFragment forProject(ArrayList<Integer> commentsList) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();

        args.putIntegerArrayList(KEY_PROJECT_ID, commentsList);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Viewmodel send the updated commentlist
     * @param comments which contain comment info list
     */
    private void onCommentListChanged(List<Comment> comments) {
        if (comments != null && comments.size() > 0) {
            updateCommentAdapter(comments);
        } else {
            mNoCommentsText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Once api finish the call , it will update the finish status
     * @param isFinished status of api call
     */
    private void onApiFinishChanged(Boolean isFinished) {
        if (isFinished) {
            mLoadingBar.setVisibility(ProgressBar.GONE);
        }
    }
}
