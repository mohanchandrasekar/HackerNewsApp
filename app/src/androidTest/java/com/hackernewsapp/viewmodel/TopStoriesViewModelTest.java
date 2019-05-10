package com.hackernewsapp.viewmodel;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.hackernewsapp.repository.NewsApiRepository;
import com.hackernewsapp.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * This class is responsible for wheather the viewmodel is properly return the value to subscriber
 */
@RunWith(AndroidJUnit4.class)
public class TopStoriesViewModelTest {

    private ArrayList<Integer> mKidsList;

    @Before
    public void setup() {
        mKidsList = new ArrayList<>();
        mKidsList.add(2921983);
    }

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new
            ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void verify_top_story_list_size() throws InterruptedException {
        //Arrange
        MainActivity mainActivity = mMainActivity.launchActivity(new Intent());
        Thread.sleep(5000);

        //Act
        TopStoriesViewModel topStoriesViewModel = ViewModelProviders.of(mainActivity,
                new CommonViewModelFactory(new NewsApiRepository())).get(TopStoriesViewModel.class);
        Thread.sleep(5000);

        topStoriesViewModel.getStoryListObservable().observe(mainActivity,
                stories -> assertNotNull(stories.size()));

        //Assert
        assertNotNull(topStoriesViewModel);
    }

    @Test
    public void verify_api_finish_status() throws InterruptedException {
        //Arrange
        MainActivity mainActivity = mMainActivity.launchActivity(new Intent());
        Thread.sleep(1000);

        //Act
        TopStoriesViewModel topStoriesViewModel = ViewModelProviders.of(mainActivity,
                new CommonViewModelFactory(new NewsApiRepository())).get(TopStoriesViewModel.class);
        Thread.sleep(1000);

        topStoriesViewModel.getIsApiCallFinished().observe(mainActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFinish) {
                //Assert
                assertTrue(isFinish);
            }
        });
    }

}