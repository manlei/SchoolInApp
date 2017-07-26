package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.edu.nju.cs.seg.schooledinapp.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;



@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    private static final String USER_NAME = "stu1";

    private static final String USER_PASSWORD = "1234";

    @Rule
    public ActivityTestRule<SignInActivity> activityTestRule =
            new ActivityTestRule<SignInActivity>(SignInActivity.class);

    @Test
    public void display_isOk() {
        onView(withId(R.id.et_sign_in_email_name_or_phone)).check(matches(isDisplayed()));
        onView(withId(R.id.et_sign_in_password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_sign_in_sign_in)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_sign_in_forgot_password)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_sign_in_sign_up)).check(matches(isDisplayed()));
        onView(withId(R.id.spn_sign_in_email_host)).check(matches(isDisplayed()));
    }

    @Test
    public void signIn_isOk() {
        onView(withId(R.id.et_sign_in_email_name_or_phone)).perform(typeText(USER_NAME), closeSoftKeyboard());
        onView(withId(R.id.et_sign_in_password)).perform(typeText(USER_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.btn_sign_in_sign_in)).perform(click());
    }

    @Test
    public void spinner_isOk() {
        onView(withId(R.id.spn_sign_in_email_host))
                .perform(click());
        onData(allOf(withText(R.string.email_host_of_faculty)))
                .perform(click());
    }

    @Test
    public void gotoSignUp_isOk() {
        onView(withId(R.id.tv_sign_in_sign_up)).perform(click());
        onView(withId(R.id.iv_sign_up_logo)).check(matches(isDisplayed()));
    }

}