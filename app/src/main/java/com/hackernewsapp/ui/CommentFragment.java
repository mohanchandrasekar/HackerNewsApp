package com.hackernewsapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackernewsapp.R;
import com.hackernewsapp.adapter.StoryAdapter;
import com.hackernewsapp.model.Comment;
import com.hackernewsapp.repository.NewsApiRepository;
import com.hackernewsapp.viewmodel.CommentViewModel;
import com.hackernewsapp.viewmodel.CommonViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentFragment extends Fragment {

    private static final String KEY_PROJECT_ID = "comments_id";
    private CommentViewModel mCommentViewModel;
    private RecyclerView mCommentRecyclerView;
    private LinearLayout mLoadingBar;
    private TextView mNoCommentsText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Integer> kidsList = Objects.requireNonNull(getArguments()).getIntegerArrayList(KEY_PROJECT_ID);
        mCommentViewModel = ViewModelProviders.of(this,
                new CommonViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext(), kidsList)).get(CommentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_comments, container, false);

        mCommentRecyclerView = fragmentView.findViewById(R.id.recycler_comments);
        mLoadingBar = fragmentView.findViewById(R.id.progress_indicator);
        mNoCommentsText = fragmentView.findViewById(R.id.text_no_comments);

        mLoadingBar.setVisibility(ProgressBar.VISIBLE);
        mCommentViewModel.getIsApiCallFinished().observe(this, isFinished -> {
            if (isFinished) {
                mLoadingBar.setVisibility(ProgressBar.GONE);
            }
        });

        mCommentViewModel.getmCommentListObservable().observe(this, comments -> {
            if (comments != null) {
                updateCommentAdapter(comments);
            } else {
                mNoCommentsText.setVisibility(View.VISIBLE);
            }
        });
        return fragmentView;
    }

    private void updateCommentAdapter(List<Comment> commentList) {
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        boolean isComment = true;
        StoryAdapter commentAdapter = new StoryAdapter(getActivity(), commentList, isComment);
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

}
