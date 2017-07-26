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
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class AnswerActivityTest extends BaseActivityTest {

    private static final int ANSWER_ID = 1;

    @Rule
    public ActivityTestRule<AnswerActivity> answerActivityTestRule
            = new ActivityTestRule<>(AnswerActivity.class, true, false);

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
        i.putExtra(AnswerActivity.PARAM_ANSWER_ID, ANSWER_ID);
        answerActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_answer)).check(matches(isDisplayed()));
        onView(withId(R.id.abl_answer_app_bar_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_answer_question_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_answer_question_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_answer_collect)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_answer_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void gotoAnswerComments_isOk() throws Exception {
        this.waitUntilConnectionEnd();
        onView(withId(R.id.ll_answer_comment)).perform(click());
        onView(withId(R.id.activity_comment)).check(matches(isDisplayed()));
    }

}