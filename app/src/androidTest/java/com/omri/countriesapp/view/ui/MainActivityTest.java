package com.omri.countriesapp.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.omri.countriesapp.R;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.viewmodel.CountryListViewModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private SimpleIdlingResource idlingRes = new SimpleIdlingResource();

    @Before
    public void idlingResourceSetup() {

        IdlingRegistry.getInstance().register(idlingRes);
        // There's always
        idlingRes.setIdleNow(false);

        CountryListViewModel countryListViewModel = getCountryListViewModel();

        // Subscribe to CountryListViewModel's country list observable to figure out when the
        // app is idle.
        countryListViewModel.getCountries().observeForever(new Observer<List<CountryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CountryEntity> countryEntities) {
                if (countryEntities != null && countryEntities.size() >= 1) {
                    idlingRes.setIdleNow(true);
                }
            }
        });
    }

    @Test
    public void mainActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.country_list), withContentDescription("Country List"), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("China"), withContentDescription("Name"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(not(withText(""))));

    }

    /** Gets the ViewModel for the current fragment */
    private CountryListViewModel getCountryListViewModel() {
        MainActivity activity = mActivityTestRule.getActivity();

        Fragment productListFragment = activity.getSupportFragmentManager()
                .findFragmentByTag(CountryListFragment.TAG);

        return ViewModelProviders.of(productListFragment)
                .get(CountryListViewModel.class);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private static class SimpleIdlingResource implements IdlingResource {

        // written from main thread, read from any thread.
        private volatile ResourceCallback mResourceCallback;

        private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

        public void setIdleNow(boolean idleNow) {
            mIsIdleNow.set(idleNow);
            if (idleNow) {
                mResourceCallback.onTransitionToIdle();
            }
        }

        @Override
        public String getName() {
            return "Simple idling resource";
        }

        @Override
        public boolean isIdleNow() {
            return mIsIdleNow.get();
        }

        @Override
        public void registerIdleTransitionCallback(IdlingResource.ResourceCallback callback) {
            mResourceCallback = callback;
        }
    }
}
