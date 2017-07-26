package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.testutils.CustomScrollActions;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class UserInfoActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<UserInfoActivity> userinfoActivityTestRule
            = new ActivityTestRule<>(UserInfoActivity.class, true, false);

    @BeforeClass
    public static void initAppContext() throws Exception {
        RequestBody body = new JsonRequestBodyBuilder()
                .append("user_email_or_phone", "stu1@nju.edu.cn")
                .append("user_password", "1234")
                .build();

        IndexService indexService = ApiClient.getIndexService();
        Response<OnlineUser> r = indexService.signIn(body).execute();

        assertEquals(r.code(), 200);

        AppContext.signIn(r.body());
    }

    @Before
    public void launchActivity() {
        Intent i = new Intent();
        i.putExtra(UserInfoActivity.PARAM_USER_ID, AppContext.getOnlineUser().getId());
        userinfoActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_user_info)).check(matches(isDisplayed()));
        onView(withId(R.id.civ_user_info_header_avatar)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_user_info_answers)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_user_info_questions)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_user_info_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_user_info_email)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_user_info_phone)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_user_info_department)).check(matches(isDisplayed()));

        onView(withId(R.id.btn_user_info_change_password)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoUserAnswers_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        // 点击 我的回答
        onView(withId(R.id.tv_user_info_answers)).perform(click());

        // 检测是否成功跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_user_answers)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoUserQuestions_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        // 点击 我的问题
        onView(withId(R.id.tv_user_info_questions)).perform(click());

        // 检测是否成功跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_user_questions)).check(matches(isDisplayed()));
    }

    @Test
    public void changePassword_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        onView(withId(R.id.btn_user_info_change_password))
                .perform(CustomScrollActions.nestedScrollTo(), click());

        onView(withId(R.id.dialog_custom_base)).check(matches(isDisplayed()));
        onView(withId(R.id.user_info_dialog_update_password)).check(matches(isDisplayed()));
    }

}