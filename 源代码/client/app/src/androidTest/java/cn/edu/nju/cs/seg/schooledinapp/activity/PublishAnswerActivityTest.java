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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PublishAnswerActivityTest extends BaseActivityTest {

    private static final int QUESTION_ID = 1;

    private static final String ANSWER_CONTENT = "ANSWER_CONTENT";

    @Rule
    public ActivityTestRule<PublishAnswerActivity> publishAnswerActivityTestRule
            = new ActivityTestRule<>(PublishAnswerActivity.class, true, false);

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
        i.putExtra(PublishAnswerActivity.PARAM_QUESTION_ID, QUESTION_ID);
        publishAnswerActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_publish_answer)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_ask_question)).check(matches(isDisplayed()));
        onView(withId(R.id.hsv_publish_answer_operations)).check(matches(isDisplayed()));
        onView(withId(R.id.editor)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_release_answer)).check(matches(isDisplayed()));
    }

    @Test
    public void publish_isOk() throws Exception {
        onView(withId(R.id.editor)).perform(typeText(ANSWER_CONTENT));
        onView(withId(R.id.tv_release_answer)).perform(click());
    }

}