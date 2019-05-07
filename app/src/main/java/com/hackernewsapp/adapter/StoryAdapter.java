package com.hackernewsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernewsapp.R;
import com.hackernewsapp.model.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter {

    private List<Story> mStoryList;
    private Context mContext;
    public StoryAdapter(Context context, List<Story> storyList){
        this.mContext = context;
        this.mStoryList=storyList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.top_stories_list, null);
        StoryViewHolder storyViewHolder = new StoryViewHolder(view);
        return storyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        StoryViewHolder storyViewHolder =(StoryViewHolder) viewHolder;
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

        storyViewHolder.mStoryPostInfo.setText(new StringBuilder().append(score).append(" points ").append(" by ")
                .append(byName).append(" ").append(hours).append(" hours ago ").toString());
        storyViewHolder.mStoryComments.setText(String.valueOf(mStoryList.get(position).getDescendants()));
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    private String convertSecondsToHours(long seconds) {
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d", h);
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder{
        private TextView mStoryTitle;
        private TextView mStoryUri;
        private TextView mStoryPostInfo;
        private TextView mStoryComments;

         StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mStoryTitle = itemView.findViewById(R.id.story_title);
            mStoryUri = itemView.findViewById(R.id.story_uri);
            mStoryPostInfo = itemView.findViewById(R.id.story_post_info);
            mStoryComments = itemView.findViewById(R.id.story_comments);
        }
    }
}
