package com.hackernewsapp.adapter;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackernewsapp.ui.MainActivity;
import com.hackernewsapp.R;
import com.hackernewsapp.data.Comment;
import com.hackernewsapp.data.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter {

    private final static String TAG ="StoryAdapter";
    /* differenciate the whether its is Commentlist or Stoyllist*/
    private boolean mIsComment;
    /* Storylist which contain story information*/
    private List<Story> mStoryList;
    /* Commentlist which contain comment information*/
    private List<Comment> mCommentList;
    /* Context of the application */
    private Context mContext;

    public StoryAdapter(@NonNull Context context,@NonNull List<Story> storyList) {
        this.mContext = context;
        this.mStoryList = storyList;
    }

    public StoryAdapter(@NonNull Context applicationContext, @NonNull List<Comment> commentList,@NonNull boolean isComment) {
        this.mContext = applicationContext;
        this.mCommentList = commentList;
        this.mIsComment = isComment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (!mIsComment) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.top_stories_list, null);
            return new StoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comments_list, null);
            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (!mIsComment){
            updateStoryView(viewHolder, position);
        }else{
            updateCommentView(viewHolder, position);
        }
    }

    /**
     * update the listitem view from the Viewholder based on the item position
     * @param viewHolder view of the item
     * @param position position index
     */
    private void updateCommentView(RecyclerView.ViewHolder viewHolder, int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder)viewHolder;
        commentViewHolder.mCommentTitle.setText(mCommentList.get(position).getBy());
        commentViewHolder.mCommentComments.setText(mCommentList.get(position).getText());
        int time = mCommentList.get(position).getTime();
        String hoursBy = convertSecondsToHours(time);
        commentViewHolder.mCommentTime.setText(hoursBy + " hours ago ");
    }

    /**
     * update the listitem view from the Viewholder based on the item position
     * @param viewHolder view of the item
     * @param position position index
     */
    private void updateStoryView(RecyclerView.ViewHolder viewHolder, int position) {
        StoryViewHolder storyViewHolder = (StoryViewHolder) viewHolder;
        storyViewHolder.mStoryTitle.setText(mStoryList.get(position).getTitle());
        String uri = mStoryList.get(position).getUrl();
        if (uri != null) {
            String[] url = uri.split("/");
            storyViewHolder.mStoryUri.setText(url[2]);
        }
        int score = mStoryList.get(position).getScore();
        String byName = mStoryList.get(position).getBy();
        int time = mStoryList.get(position).getTime();
        String hours = convertSecondsToHours(time);

        storyViewHolder.mStoryPostInfo.setText(String.valueOf(score) + " points " + " by " +
                byName + " " + hours + " hours ago ");
        storyViewHolder.mStoryComments.setText(String.valueOf(mStoryList.get(position).getDescendants()));

        storyViewHolder.mLinearLayout.setOnClickListener(view -> {
            if (((MainActivity) mContext).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) mContext).show(mStoryList.get(position).getKids());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mIsComment){
            return mCommentList.size();
        }else{
            return mStoryList.size();
        }
    }

    /**
     * Convert from long to hours
     * @param seconds time in long value
     * @return which return the hour string
     */
    @SuppressLint("DefaultLocale")
    private String convertSecondsToHours(long seconds) {
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d", h);
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mStoryTitle;
        private TextView mStoryUri;
        private TextView mStoryPostInfo;
        private TextView mStoryComments;
        private LinearLayout mLinearLayout;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mStoryTitle = itemView.findViewById(R.id.story_title);
            mStoryUri = itemView.findViewById(R.id.story_uri);
            mStoryPostInfo = itemView.findViewById(R.id.story_post_info);
            mStoryComments = itemView.findViewById(R.id.story_comments);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView mCommentTitle;
        private TextView mCommentTime;
        private TextView mCommentComments;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mCommentTitle = itemView.findViewById(R.id.text_post_author);
            mCommentTime = itemView.findViewById(R.id.text_post_date);
            mCommentComments = itemView.findViewById(R.id.text_comment);
        }
    }
}
