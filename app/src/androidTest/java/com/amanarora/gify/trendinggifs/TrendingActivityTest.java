package com.amanarora.gify.trendinggifs;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.amanarora.gify.R;
import com.amanarora.gify.randomgifs.GifsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TrendingActivityTest {

    @Rule
    public ActivityTestRule<TrendingActivity> activityRule =
            new ActivityTestRule<>(TrendingActivity.class);

    @Test
    public void onGifClickTest() {
        Intents.init();
        onView(withId(R.id.gif_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));
        intended(hasComponent(hasClassName(GifsActivity.class.getName())));
        Intents.release();
    }

}