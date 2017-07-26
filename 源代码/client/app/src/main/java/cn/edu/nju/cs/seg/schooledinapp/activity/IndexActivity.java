package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.util.Set;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.PublishAudioActivity;
import cn.edu.nju.cs.seg.schooledinapp.fragment.MainBodyFragment;
import cn.edu.nju.cs.seg.schooledinapp.fragment.SearchFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.BusEventFilter;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndexActivity extends BaseActivity {

    private static final String TAG = "IndexActivity";

    private static User onlineUser;

    @BindView(R.id.activity_index)
    DrawerLayout indexActivity;

    @BindView(R.id.nv_index_nav)
    NavigationView nvNavigation;

    @BindView(R.id.tb_index_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.fab_index_fab_menu)
    FloatingActionsMenu fabFabMenu;

    @BindView(R.id.fab_index_fab_by_text)
    FloatingActionButton fabByText;

    @BindView(R.id.fab_index_fab_by_voice)
    FloatingActionButton fabByVoice;

    // 主体 fragment
    MainBodyFragment fragmentMainBody;

    // 搜索 fragment
    SearchFragment fragmentSearch;

    // 与搜索相关
    SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        AppContext.getBus().register(this);

        onlineUser = AppContext.getOnlineUser();

        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 登录用户信息已更新
        if (AppContext.isOnlineUserUpdated()) {
            onlineUser = AppContext.getOnlineUser();
            AppContext.clearOnlineUserUpdated();
            updateNavigationView();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_options_menu, menu);

        // 初始化与搜索相关的部分
        initSearchMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                indexActivity.openDrawer(Gravity.LEFT);
                break;
            case R.id.item_index_option_search:
                Toast.makeText(
                        AppContext.getContext(), "搜索", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Subscribe
    public void onReceiveNotification(BusEventFilter.NotificationReceivedEvent e) {
        showNotificationsReminder();
    }

    /**
     * 启动 IndexActivity
     *
     * @param context
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, IndexActivity.class);
        context.startActivity(intent);
    }

    /**
     * 清除别名和标签然后注销
     *
     */
    private void unbindAliasAndTagsThenSignOut() {
        // 清除别名和标签
        AppContext.unbindAliasAndTags(new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                switch (responseCode) {
                    case 0: {
                        signOut();
                    } break;

                    default: {
                        LogUtil.e(TAG, "response code: " + responseCode);
                    }
                }
            }
        });
    }

    /**
     * 注销
     *
     */
    private void signOut() {
        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("user_email_or_phone", AppContext.getOnlineUser().getEmail())
                .append("user_password", AppContext.getOnlineUser().getPassword())
                .build();

        IndexService indexService = ApiClient.getIndexService();
        indexService.signOut(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 204: {
                        // 注销
                        AppContext.signOut();
                        // 返回登录页面
                        SignInActivity.startActivityWithParameters(IndexActivity.this);
                    } break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtil.showAlertDialog(IndexActivity.this,
                        R.string.error_message_network);
            }
        });
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {

        initActionBar();
        initNavigation();

        // 设置 主体
        fragmentMainBody = MainBodyFragment.newFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_index_main_body, fragmentMainBody);
        transaction.commit();

        initFab();
    }

    /**
     * 显示收到新通知提示
     *
     */
    private void showNotificationsReminder() {
        Snackbar.make(indexActivity, R.string.sb_receive_notifications, Snackbar.LENGTH_LONG)
                .setAction("查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationsActivity.startActivityWithParameters(IndexActivity.this);
                    }
                }).show();
    }

    /**
     * 初始化 ActionBar
     */
    private void initActionBar() {
        // 设置 Toolbar
        setSupportActionBar(tbToolbar);

        // 设置“抽屉”按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
    }

    /**
     * 初始化 NavigationView
     *
     */
    private void initNavigation() {
        // 设置侧边栏
        nvNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_index_basic_info:
                        UserInfoActivity.startActivityWithParameters(
                                IndexActivity.this, onlineUser.getId());
                        break;

                    case R.id.item_index_my_notifications:
                        NotificationsActivity.startActivityWithParameters(
                                IndexActivity.this);
                        break;

                    case R.id.item_index_my_favorites:
                        FavoritesActivity.startActivityWithParameters(
                                IndexActivity.this, onlineUser.getId());
                        break;

                    case R.id.item_index_my_studios:
                        UserStudiosActivity.startActivityWithParameters(
                                IndexActivity.this, onlineUser.getId());
                        break;

                    case R.id.item_index_settings:
                        Toast.makeText(
                                AppContext.getContext(), "设置", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.item_index_help:
                        Toast.makeText(
                                AppContext.getContext(), "帮助", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.item_index_sign_out:
                        unbindAliasAndTagsThenSignOut();
                        break;
                }

                return true;
            }
        });

        updateNavigationView();

    }

    /**
     * 初始化搜索相关的部分
     *
     * @param optionsMenu OptionsMenu
     */
    private void initSearchMenu(Menu optionsMenu) {
        svSearch = (SearchView) optionsMenu.findItem(R.id.item_index_option_search).getActionView();

        svSearch.setQueryHint(
                getString(R.string.item_index_options_search_hint));

        // 开启 搜索
        svSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentSearch = SearchFragment.newFragment();

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl_index_main_body, fragmentSearch);
                transaction.commit();
            }
        });

        // 关闭 搜索
        svSearch.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl_index_main_body, fragmentMainBody);
                transaction.commit();
                return false;
            }
        });

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragmentSearch.searchTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragmentSearch.searchTextChange(newText);
                return true;
            }
        });
    }


    /**
     * 初始化 FloatingActionButton
     *
     */
    private void initFab() {
        fabByText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskQuestionActivity.startAskQuestionActivity(IndexActivity.this);
            }
        });

        fabByVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskQuestionByAudioActivity.startActivityWithParameters(IndexActivity.this);
            }
        });
    }

    /**
     * 刷新 NavigationView
     *
     */
    private void updateNavigationView() {
        View header = nvNavigation.getHeaderView(0);
        ImageView ivUserAvatar = (ImageView) header.findViewById(R.id.civ_index_user_avatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.tv_index_user_name);
        TextView tvUserBio = (TextView) header.findViewById(R.id.tv_index_user_bio);

        Glide.with(this)
                .load(onlineUser.getAvatarUrl())
                .into(ivUserAvatar);
        tvUserName.setText(onlineUser.getName());
        tvUserBio.setText(onlineUser.getBio());
    }

}
