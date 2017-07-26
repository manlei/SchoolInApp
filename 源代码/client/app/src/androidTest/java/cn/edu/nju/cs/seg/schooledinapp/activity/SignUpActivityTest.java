package cn.edu.nju.cs.seg.schooledinapp.activity;

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
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityTestRule
            = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void display_isOk() {
        onView(withId(R.id.et_sign_up_email)).check(matches(isDisplayed()));
        onView(withId(R.id.et_sign_up_password)).check(matches(isDisplayed()));
        onView(withId(R.id.et_sign_up_validation_code)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_sign_up_sign_up)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_sign_up_sign_in)).check(matches(isDisplayed()));
        onView(withId(R.id.cb_sign_up_protocol)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoSignIn_isOk() {
        onView(withId(R.id.tv_sign_up_sign_in)).perform(click());
        onView(withId(R.id.btn_sign_in_sign_in)).check(matches(isDisplayed()));
    }

}