package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.adapter.SearchFragmentAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseFragment;

public class SearchFragment extends BaseFragment
        implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private static final String TAG = "SearchFragment";

    private  ViewPager viewpagerSearch;

    // tab 数量
    private int nrTabs;

    private  TabHost tabhostSearch;

    private static String queryText;

    private SearchQuestionsFragment searchQuestionsFragment;

    private SearchUsersFragment searchUsersFragment;

    private SearchStudiosFragment searchStudiosFragment;

    // tabhost和viewpager使用的fragments
    private List<Fragment> fragmentLists = new ArrayList<>();

    // fragment返回的view
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_search, container, false);

        initViewPager();
        initTabHost();

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     *
     * @param position 滚动之后显示的页面，同时更新数据，因为每次页面变化都会调用这个方法，所以只需要在这里更新
     */
    @Override
    public void onPageSelected(int position) {
        viewpagerSearch.setCurrentItem(position);
        searchTextChange(queryText);

        for (int i = 0; i < nrTabs; i ++) {
            TextView tv = (TextView) tabhostSearch.getTabWidget()
                    .getChildAt(i).findViewById(android.R.id.title);
            if (i == position) {
                tv.setAlpha(1.0f);
            } else {
                tv.setAlpha(0.6f);
            }
        }
    }

    /**
     * viewPager滚动页面时设置tabhost的位置，使之保持一致
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        int item = viewpagerSearch.getCurrentItem();
        tabhostSearch.setCurrentTab(item);
    }

    /**
     * 改变tabhost的时候要改变viewpager对应的项，同时应该去请求新的搜索内容，更新UI
     *
     * @param tabId 选中某个tab，对应tab的ID
     */
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabhostSearch.getCurrentTab();
        viewpagerSearch.setCurrentItem(selectedItem);
    }

    /**
     * 创建一张新的 SearchFragment
     *
     * @return SearchFragment
     */
    public static SearchFragment newFragment() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    /**
     * 初始化viewPager,添加对应的三个fargment,设置viewPager变化的监听事件
     */
    private void initViewPager() {
        viewpagerSearch = (ViewPager) view.findViewById(R.id.viewpager_search_pager);

        searchStudiosFragment = SearchStudiosFragment.newFragment();
        searchQuestionsFragment =  SearchQuestionsFragment.newFragment();
        searchUsersFragment = SearchUsersFragment.newFragment();

        fragmentLists.add(searchUsersFragment);
        fragmentLists.add(searchQuestionsFragment);
        fragmentLists.add(searchStudiosFragment);

        SearchFragmentAdapter fragmentPagerAdapter = new SearchFragmentAdapter(
                getChildFragmentManager(), fragmentLists);
        viewpagerSearch.setAdapter(fragmentPagerAdapter);
        viewpagerSearch.setOnPageChangeListener(this);
    }


    /**
     * 生成tabhost的view,对应的每一项
     */
    public class FakeContent implements TabHost.TabContentFactory {
        Context context;
        public FakeContent(Context context){
            this.context = context;
        }
        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            return fakeview;
        }
    }


    /**
     * 初始化tabhost，设置tabhost每一项的大小，添加tabhost的监听事件
     */
    private void initTabHost() {
        tabhostSearch = (TabHost) view.findViewById(R.id.tabhost_search_tab);
        tabhostSearch.setup();
        final String[] tabNames = {
                getResources().getString(R.string.search_tab_users),
                getResources().getString(R.string.search_tab_questions),
                getResources().getString(R.string.search_tab_studios)};

        nrTabs = tabNames.length;

        for (int i = 0; i < tabNames.length; i ++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabhostSearch.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(AppContext.getContext()));
            tabhostSearch.addTab(tabSpec);
            tabhostSearch.setTag(tabNames[i]);
            int widgetHeight = tabhostSearch.getTabWidget().getChildAt(i).getLayoutParams().height;
            tabhostSearch.getTabWidget().getChildAt(i).getLayoutParams().height = (int)(0.6*widgetHeight);

            // 设置字体颜色和透明度
            TextView tv = (TextView) tabhostSearch.getTabWidget()
                    .getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            if(i != 0) {
                tv.setAlpha(0.6f);
            } else {
                tv.setAlpha(1.0f);
            }
        }
        tabhostSearch.setOnTabChangedListener(this);
    }

    public void searchTextChange(String queryText) {
        if (queryText == null || "".equals(queryText)) {
            SearchFragment.queryText = queryText;
            searchUsersFragment.clearUsers();
            searchQuestionsFragment.clearQuestions();
            searchStudiosFragment.clearStudios();
            return;
        }

        SearchFragment.queryText = queryText;
        int position = viewpagerSearch.getCurrentItem();
        String currentTag = tabhostSearch.getCurrentTabTag();
        switch (currentTag) {

            case "用户": {
                searchUsersFragment.searchUsers(queryText);
            }
            break;

            case "问题":{
                searchQuestionsFragment.searchQuestions(queryText);
            }
            break;

            case "工作室":{
                searchStudiosFragment.searchStudios(queryText);
            }
            break;
        }
    }

}
