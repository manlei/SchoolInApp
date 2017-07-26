package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.edu.nju.cs.seg.schooledinapp.AppConfig;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;
import cn.edu.nju.cs.seg.schooledinapp.model.Question;
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
public class QuestionActivityTest extends BaseActivityTest {

    private static final int QUESTION_ID = 1;

    @Rule
    public ActivityTestRule<QuestionActivity> questionActivityTestRule
            = new ActivityTestRule<>(QuestionActivity.class, true, false);

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

    @Override
    public void waitUntilConnectionEnd() throws Exception {
        Thread.sleep((AppConfig.CONNECTION_TIME_OUT_IN_MILLISECONDS + 100) * 2);
    }

    @Before
    public void launchActivity() {
        Intent i = new Intent();
        i.putExtra(QuestionActivity.PARAM_QUESTION_ID, QUESTION_ID);
        questionActivityTestRule.launchActivity(i);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.activity_question)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_question_main_info)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_question_main_operations)).check(matches(isDisplayed()));
        onView(withId(R.id.ll_question_list_header)).check(matches(isDisplayed()));
    }

//    @Test
//    public void gotoDirectedTo_isOK() throws Exception {
//        this.waitUntilConnectionEnd();
//        onView(withId(R.id.tv_question_directed_to)).check(matches(isDisplayed()));
//        onView(withId(R.id.tv_question_directed_to)).perform(click());
//        onView(withId(R.id.activity_studio_info)).check(matches(isDisplayed()));
//    }

}