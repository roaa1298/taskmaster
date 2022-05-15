package com.roaa.mytasks;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.MenuPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.roaa.mytasks.data.Task;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskAppTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void navigateToDetailsPageTest(){
        onView(withId(R.id.tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.task_details)).check(matches(isDisplayed()));
        onView(withId(R.id.taskTitleView)).check(matches(withText("task1")));
        onView(withId(R.id.desc)).check(matches(withText("task1")));
        onView(withId(R.id.task_state)).check(matches(withText("new")));
    }

    @Test
    public void addTaskTest(){
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.addTaskLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.task_title))
                .perform(typeText("task1"), closeSoftKeyboard());
        onView(withId(R.id.task_desc))
                .perform(typeText("Modify your Add Task form to save the data entered in as a Task in your local database."), closeSoftKeyboard());
        onView(withId(R.id.adding)).perform(click());
        onView(withId(R.id.total)).check(matches(withText("Total Tasks: 1")));

    }
    @Test
    public void settingsPageTest(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onData(CoreMatchers.anything()).inRoot(RootMatchers.isPlatformPopup()).inAdapterView(CoreMatchers.<View>instanceOf(MenuPopupWindow.MenuDropDownListView.class)).atPosition(0).perform(click());
        onView(withId(R.id.settingLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.username))
                .perform(typeText("roaa"), closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        pressBack();
        onView(withId(R.id.userTasks)).check(matches(withText("roaa's Tasks")));
    }

    @Test
    public void allTaskPageTest(){
        onView(withId(R.id.allTasks)).perform(click());
        onView(withId(R.id.allTasksLayout)).check(matches(isDisplayed()));
    }

}
