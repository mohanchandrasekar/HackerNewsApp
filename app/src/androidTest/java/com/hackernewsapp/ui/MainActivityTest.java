package com.hackernewsapp.ui;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.hackernewsapp.R;
import com.hackernewsapp.data.Story;
import com.hackernewsapp.repository.NewsApiRepository;
import com.hackernewsapp.viewmodel.CommonViewModelFactory;
import com.hackernewsapp.viewmodel.TopStoriesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new
            ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {

    }

    @Test
    public void verify_list_item_info_visible() throws InterruptedException {
        ArrayList<Story> mStoryList = new ArrayList<>();
        Story story = new Story();
        story.setTitle("My YC app: Dropbox - Throw away your USB drive");
        story.setBy("dhouston");
        story.setDescendants(71);
        story.setId(8863);
        story.setScore(111);
        story.setTime(1175714200);
        story.setUrl("http://www.getdropbox.com/u/2/screencast.html");
        mStoryList.add(story);
        //Arrange
        MainActivity mainActivity = mMainActivity.launchActivity(new Intent());

        //Act
        TopStoriesViewModel topStoriesViewModel = ViewModelProviders.of(mainActivity,
                new CommonViewModelFactory(new NewsApiRepository())).get(TopStoriesViewModel.class);

        //Update the storyList
        topStoriesViewModel.updateCommentList(mStoryList);
        Thread.sleep(5000);

        onView(withId(R.id.story_title)).check(matches(isDisplayed()));
        onView(withId(R.id.story_post_info)).check(matches(isDisplayed()));
        onView(withId(R.id.story_uri)).check(matches(isDisplayed()));

        // Assert that our selected station is still visible
        //onView(withText("My YC app: Dropbox - Throw away your USB drive")).check(matches(hasDescendant(withText(mStoryList.get(0).getTitle()))));
    }

    @Test
    public void verify_click_action_list_item() throws InterruptedException {
        ArrayList<Story> mStoryList = new ArrayList<>();
        Story story = new Story();
        story.setTitle("My YC app: Dropbox - Throw away your USB drive");
        story.setBy("dhouston");
        story.setDescendants(71);
        story.setId(8863);
        story.setScore(111);
        story.setTime(1175714200);
        story.setUrl("http://www.getdropbox.com/u/2/screencast.html");
        mStoryList.add(story);
        //Arrange
        MainActivity mainActivity = mMainActivity.launchActivity(new Intent());

        //Act
        TopStoriesViewModel topStoriesViewModel = ViewModelProviders.of(mainActivity,
                new CommonViewModelFactory(new NewsApiRepository())).get(TopStoriesViewModel.class);

        //Update the storyList
        topStoriesViewModel.updateCommentList(mStoryList);
        Thread.sleep(5000);

        onView(withId(R.id.story_title)).check(matches(isDisplayed()));
        onView(withId(R.id.story_post_info)).check(matches(isDisplayed()));
        onView(withId(R.id.story_uri)).check(matches(isDisplayed()));

        onView(withId(R.id.recycler_top_stories))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(2000);
        // Assert that our selected station is still visible
        //onView(withText("My YC app: Dropbox - Throw away your USB drive")).check(matches(hasDescendant(withText(mStoryList.get(0).getTitle()))));
    }

    @Test
    public void verify_scroll_to_position(){
    }
}