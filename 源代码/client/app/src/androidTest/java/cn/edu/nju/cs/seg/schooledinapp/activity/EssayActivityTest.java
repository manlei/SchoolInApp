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
public class EssayActivityTest extends BaseActivityTest {

    private static final int ESSAY_ID = 1;

    @Rule
    public ActivityTestRule<EssayActivity> essayActivityTestRule
            = new ActivityTestRule<>(EssayActivity.class, true, false);

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
        i.putExtra(EssayActivity.PARAM_ESSAY_ID, ESSAY_ID);
        essayActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_essay)).check(matches(isDisplayed()));
        onView(withId(R.id.abl_essay_app_bar_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_essay_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_essay_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_essay_praise)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_essay_collect)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_essay_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoEssayComments_isOk() throws Exception {
        this.waitUntilConnectionEnd();
        onView(withId(R.id.ll_essay_comment)).perform(click());
        onView(withId(R.id.activity_comment)).check(matches(isDisplayed()));
    }

}