package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.QuickFragmentPageAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.FavoriteAnswersFragment;
import cn.edu.nju.cs.seg.schooledinapp.fragment.FavoriteEssaysFragment;
import cn.edu.nju.cs.seg.schooledinapp.fragment.FavoriteQuestionsFragment;

public class FavoritesActivity extends BaseActivity {

    private static final String TAG = "FavoritesActivity";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @BindView(R.id.tb_favorites_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.tl_favorites_tabs)
    TabLayout tlTabs;

    @BindView(R.id.vp_favorites_views)
    ViewPager vpViews;

    @BindString(R.string.favorites_tab_questions)
    String questionsFragmentTitle;

    @BindString(R.string.favorites_tab_answers)
    String answersFragmentTitle;

    @BindString(R.string.favorites_tab_essays)
    String essaysFragmentTitle;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> fragmentTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        userId = getIntent().getIntExtra(PARAM_USER_ID, -1);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    /**
     * 启动 FavoritesActivity
     *
     * @param context 启动上下文
     * @param userId 用户ID
     */
    public static void startActivityWithParameters(Context context, int userId) {
        Intent intent = new Intent(context, FavoritesActivity.class);
        intent.putExtra(PARAM_USER_ID, userId);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 设置 Toolbar
        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 初始化 Fragment ，并添加
        FavoriteQuestionsFragment questionsFragment = FavoriteQuestionsFragment.newFragment(userId);
        FavoriteAnswersFragment essaysFragment = FavoriteAnswersFragment.newFragment(userId);
        FavoriteEssaysFragment answersFragment = FavoriteEssaysFragment.newFragment(userId);

        fragmentList.add(questionsFragment);
        fragmentList.add(essaysFragment);
        fragmentList.add(answersFragment);

        // 初始化 FragmentTitle ，并添加
        fragmentTitleList.add(questionsFragmentTitle);
        fragmentTitleList.add(answersFragmentTitle);
        fragmentTitleList.add(essaysFragmentTitle);

        QuickFragmentPageAdapter<Fragment> adapter = new QuickFragmentPageAdapter<>(
                getSupportFragmentManager(),
                fragmentList,
                fragmentTitleList
        );

        // 设置 ViewPage 和 TabLayout
        vpViews.setAdapter(adapter);
        tlTabs.setupWithViewPager(vpViews);
    }
}
