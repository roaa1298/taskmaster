package com.roaa.mytasks;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.MenuPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.roaa.mytasks.data.Task;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class TaskAppTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void navigateToDetailsPageTest(){
        // wait during 15 seconds for a view
//        onView(isRoot()).perform(waitId(R.id.tasks, TimeUnit.SECONDS.toMillis(15)));
        onView(withId(R.id.tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.task_details)).check(matches(isDisplayed()));
        onView(withId(R.id.taskTitleView)).check(matches(withText("task3")));
        onView(withId(R.id.desc)).check(matches(withText("testing")));
        onView(withId(R.id.task_state)).check(matches(withText("ASSIGNED")));
    }

    @Test
    public void addTaskTest(){
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.addTaskLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.task_title))
                .perform(typeText("task4"), closeSoftKeyboard());
        onView(withId(R.id.task_desc))
                .perform(typeText("Modify your Add Task form to save the data entered in as a Task in your local database."), closeSoftKeyboard());
        onView(withId(R.id.addTeam)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Team2"))).perform(click());
        onView(withId(R.id.addTeam)).check(matches(withSpinnerText(containsString("Team2"))));
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
        onView(withId(R.id.chooseTeam)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Team3"))).perform(click());
        onView(withId(R.id.chooseTeam)).check(matches(withSpinnerText(containsString("Team3"))));
        onView(withId(R.id.save)).perform(click());
        pressBack();
        onView(withId(R.id.userTasks)).check(matches(withText("Team3's Tasks")));
    }

    @Test
    public void allTaskPageTest(){
        onView(withId(R.id.allTasks)).perform(click());
        onView(withId(R.id.allTasksLayout)).check(matches(isDisplayed()));
    }

    /** Perform action of waiting for a specific view id. */
//    public static ViewAction waitId(final int viewId, final long millis) {
//        return new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return isRoot();
//            }
//
//            @Override
//            public String getDescription() {
//                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
//            }
//
//            @Override
//            public void perform(final UiController uiController, final View view) {
//                uiController.loopMainThreadUntilIdle();
//                final long startTime = System.currentTimeMillis();
//                final long endTime = startTime + millis;
//                final Matcher<View> viewMatcher = withId(viewId);
//
//                do {
//                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
//                        // found view with required ID
//                        if (viewMatcher.matches(child)) {
//                            return;
//                        }
//                    }
//
//                    uiController.loopMainThreadForAtLeast(50);
//                }
//                while (System.currentTimeMillis() < endTime);
//
//                // timeout happens
//                throw new PerformException.Builder()
//                        .withActionDescription(this.getDescription())
//                        .withViewDescription(HumanReadables.describe(view))
//                        .withCause(new TimeoutException())
//                        .build();
//            }
//        };
//    }

}
