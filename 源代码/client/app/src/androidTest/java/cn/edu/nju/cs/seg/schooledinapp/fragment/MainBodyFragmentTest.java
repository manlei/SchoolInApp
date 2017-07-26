package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.BaseActivityTest;
import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.testutils.FragmentTestRule;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class MainBodyFragmentTest extends BaseActivityTest {

    @Rule
    public FragmentTestRule<MainBodyFragment> mainBodyFragmentTestRule
            = new FragmentTestRule<>(MainBodyFragment.class);

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
        mainBodyFragmentTestRule.launchActivity(null);
    }

    @Test
    public void display_isOk() {
        onView(withId(R.id.fragment_base_recycler_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.srl_base_fragment_recycler_view_swipe_refresh_layout))
                .check(matches(isDisplayed()));
        onView(withId(R.id.rv_base_fragment_recycler_view_items))
                .check(matches(isDisplayed()));
    }

    @Test
    public void gotoSomePage_isOk() throws Exception {
        this.waitUntilConnectionEnd();
        onView(withId(R.id.rv_base_fragment_recycler_view_items))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(anyOf(withId(R.id.activity_question), withId(R.id.activity_essay)))
                .check(matches(isDisplayed()));
    }

}