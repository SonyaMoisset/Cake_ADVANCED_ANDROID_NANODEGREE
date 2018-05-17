package com.sonyamoisset.android.cake;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sonyamoisset.android.cake.ui.main.RecipeActivity;
import com.sonyamoisset.android.cake.utils.IdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private final static int MINIMUM_NUMBER_OF_RECIPES = 1;
    private final static int FIRST_INDEX = 0;
    private final static String NUTELLA_PIE = "Nutella Pie";

    private IdlingResource idlingResource;

    @Rule
    final public ActivityTestRule<RecipeActivity> recipeActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = recipeActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void onLaunchRecipeListIsDisplayed() {
        onView(withId(R.id.activity_recipe_list_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onLaunchRecipeListHasMinimumOneRecipe() {
        onView(withId(R.id.activity_recipe_list_recycler_view))
                .check(matches(hasMinimumChildCount(MINIMUM_NUMBER_OF_RECIPES)));
    }

    @Test
    public void onClickFirstEntrySendsCorrectIntent() {

        onView(withId(R.id.activity_recipe_list_recycler_view))
                .perform(actionOnItemAtPosition(FIRST_INDEX, click()));

        onView(withText(NUTELLA_PIE)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickFirstEntryNavigatesToDetailActivityWithMasterFragment() {
        onView(withId(R.id.activity_recipe_list_recycler_view))
                .perform(actionOnItemAtPosition(FIRST_INDEX, click()));

        onView(withId(R.id.fragment_recipe_ingredients_detail_recycler_view))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
