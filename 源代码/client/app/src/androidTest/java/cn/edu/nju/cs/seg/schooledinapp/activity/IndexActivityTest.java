package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

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
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class IndexActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<IndexActivity> indexActivityTestRule
            = new ActivityTestRule<>(IndexActivity.class);

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

    @Test
    public void display_isOk() {
        onView(withId(R.id.fl_index_main_body)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_index_fab_menu)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_index_fab_by_text)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_index_fab_by_voice)).check(matches(isDisplayed()));
    }

    @Test
    public void navigationDisplay_isOk() {
        // 打开 抽屉
        onView(withId(R.id.activity_index)).check(matches(isClosed())).perform(open());

        // 检测 抽屉 是否打开
        onView(withId(R.id.activity_index)).check(matches(isOpen(Gravity.LEFT)));

        // 检测是否正常显示
        onView(withId(R.id.civ_index_user_avatar)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_index_user_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_index_user_bio)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToBasicInfo_isOk() {
        // 打开 抽屉
        onView(withId(R.id.activity_index)).check(matches(isClosed())).perform(open());

        // 跳转到 基本信息
        onView(withId(R.id.nv_index_nav)).perform(navigateTo(R.id.item_index_basic_info));

        // 检测是否正常跳转
        onView(withId(R.id.activity_user_info)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToMyFavorites_isOk() {
        // 打开 抽屉
        onView(withId(R.id.activity_index)).check(matches(isClosed())).perform(open());

        // 跳转到 基本信息
        onView(withId(R.id.nv_index_nav)).perform(navigateTo(R.id.item_index_my_favorites));

        // 检测是否正常跳转
        onView(withId(R.id.activity_favorites)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToMyNotifications_isOk() {
        // 打开 抽屉
        onView(withId(R.id.activity_index)).check(matches(isClosed())).perform(open());

        // 跳转到 基本信息
        onView(withId(R.id.nv_index_nav)).perform(navigateTo(R.id.item_index_my_notifications));

        // 检测是否正常跳转
        onView(withId(R.id.activity_notifications)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToMyStudios_isOk() {
        // 打开 抽屉
        onView(withId(R.id.activity_index)).check(matches(isClosed())).perform(open());

        // 跳转到 基本信息
        onView(withId(R.id.nv_index_nav)).perform(navigateTo(R.id.item_index_my_studios));

        // 检测是否正常跳转
        onView(withId(R.id.activity_base_swipe_refresh_list)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_user_studios)).check(matches(isDisplayed()));
    }

    @Test
    public void textFabGoto_isOk() {
        // 点击 文字 -- hack 必须是两次 click，具体原因与 FloatingActionButton 的实现有关
        onView(withId(R.id.fab_index_fab_by_text)).perform(click());
        onView(withId(R.id.fab_index_fab_by_text)).perform(click());

        // 检测是否正常跳转
        onView(withId(R.id.activity_ask_question)).check(matches(isDisplayed()));
    }

    @Test
    public void voiceFabGoto_isOk() {
        // 点击 语音
        onView(withId(R.id.fab_index_fab_by_voice)).perform(click());
        onView(withId(R.id.fab_index_fab_by_voice)).perform(click());

        // 检测是否正常跳转
        onView(withId(R.id.activity_publish_audio)).check(matches(isDisplayed()));
        onView(withText(R.string.manifest_label_question)).check(matches(isDisplayed()));
    }

}