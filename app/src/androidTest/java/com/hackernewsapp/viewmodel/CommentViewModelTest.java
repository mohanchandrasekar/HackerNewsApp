package com.hackernewsapp.viewmodel;

import android.content.Intent;
import android.util.Log;

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
import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * This class is responsible for wheather the viewmodel is properly return the value to subscriber
 */
@RunWith(AndroidJUnit4.class)
public class CommentViewModelTest {

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
    public void verify_comment_list_from_viewmodel() throws InterruptedException {
        //Arrange
        MainActivity mainActivity = mMainActivity.launchActivity(new Intent());
        Thread.sleep(2000);
        AtomicReference<CommentViewModel> commentViewModel = new AtomicReference<>();
        Thread.sleep(2000);
        //Act
        getInstrumentation().runOnMainSync(() -> {
            commentViewModel.set(ViewModelProviders.of(mainActivity,
                    new CommonViewModelFactory(mKidsList, new NewsApiRepository())).get(CommentViewModel.class));

            //Assert
            assertNotNull(commentViewModel.get());
        });

        Thread.sleep(2000);
        commentViewModel.get().getmCommentListObservable().observe(mainActivity,
                comments -> {
                    Log.e("CCC", "comments = " + comments.size());
                    assertEquals(1, comments.size());
        });
    }

}