package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Intent;
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
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StudioInfoActivityTest extends BaseActivityTest {

    private static final int STUDIO_ID = 1;

    @Rule
    public ActivityTestRule<StudioInfoActivity> studioinfoActivityTestRule
            = new ActivityTestRule<>(StudioInfoActivity.class, true, false);

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
        i.putExtra(StudioInfoActivity.PARAM_STUDIO_ID, STUDIO_ID);
        studioinfoActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_studio_info)).check(matches(isDisplayed()));
        onView(withId(R.id.civ_studio_info_header_avatar)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_studio_info_essays)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_studio_info_questions)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_studio_info_members)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_studio_info_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_studio_info_manager)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoStudioQuestions_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        // 点击 问题
        onView(withId(R.id.tv_studio_info_questions)).perform(click());

        // 检测是否成功跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_studio_questions)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoStudioEssays_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        // 点击 文章
        onView(withId(R.id.tv_studio_info_essays)).perform(click());

        // 检测是否成功跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_studio_essays)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoStudioMembers_isOk() throws Exception {
        this.waitUntilConnectionEnd();

        // 点击 成员列表
        onView(withId(R.id.tv_studio_info_members)).perform(click());

        // 检测是否成功跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
    }

}