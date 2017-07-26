package cn.edu.nju.cs.seg.schooledinapp.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;

public abstract class BaseRecyclerViewFragment<T> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = "BaseRecyclerViewFragment";

    @BindView(R.id.rv_base_fragment_recycler_view_items)
    RecyclerView rvItems;

    @BindView(R.id.srl_base_fragment_recycler_view_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    // RecyclerView 适配器
    private BaseQuickAdapter adapter;

    // RecyclerView 布局管理器
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // 创建基础 View
        View v = inflater.inflate(R.layout.fragment_base_recycler_view, container, false);
        ButterKnife.bind(this, v);

        // 初始化 View
        initView();

        return v;
    }

    /**
     * 获取项目列表
     *
     * @return 项目列表
     */
    public List<T> getItemList() {
        return adapter.getData();
    }

    /**
     * 获取 RecyclerView
     *
     * @return RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return rvItems;
    }

    /**
     * 获取 RecyclerView 适配器
     *
     * @return
     */
    public BaseQuickAdapter getAdapter() {
        return adapter;
    }

    /**
     * 获取 RecyclerView 布局管理器
     *
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    /**
     * 获取 Adapter 来管理 RecyclerView
     *
     * @return
     */
    protected abstract BaseQuickAdapter onCreateAdapter();

    /**
     * 获取 LayoutManager 来管理 RecyclerView
     *
     * @return
     */
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    /**
     * 下拉刷新事件
     *
     */
    @Override
    public final void onRefresh() {
        onRefresh(adapter, swipeRefreshLayout);
    }

    /**
     * 用户需要重写的 下拉刷新 事件
     *
     */
    protected void onRefresh(BaseQuickAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        refreshEnd();
    }

    /**
     * 刷新结束
     *
     */
    protected void refreshEnd() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载更多事件
     *
     */
    @Override
    public final void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        onLoadMore(adapter, swipeRefreshLayout);
    }

    /**
     * 用户需要重写的 加载更多 事件
     *
     */
    protected void onLoadMore(BaseQuickAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        adapter.loadMoreEnd(true);
    }

    /**
     * 全部数据加载完成，保留底部 No More Data 提示
     *
     */
    protected void loadMoreEnd() {
        adapter.loadMoreEnd();
        swipeRefreshLayout.setEnabled(true);
    }

    /**
     * 全部数据加载完成
     *
     * @param gone true 表示移除底部 No More Data 提示
     */
    protected void loadMoreEnd(boolean gone) {
        adapter.loadMoreEnd(gone);
        swipeRefreshLayout.setEnabled(true);
    }

    /**
     * 此次数据加载完成
     *
     */
    protected void loadMoreComplete() {
        adapter.loadMoreComplete();
        swipeRefreshLayout.setEnabled(true);
    }

    /**
     * 此次数据加载失败
     *
     */
    protected void loadMoreFail() {
        adapter.loadMoreFail();
        swipeRefreshLayout.setEnabled(true);
    }

    /**
     * 设置 RecyclerView
     *
     * @param itemList
     */
    protected int setUpRecyclerView(List<T> itemList) {
        // 设置新数据
        adapter.setNewData(itemList);
        // 为 RecyclerView 设置 Adapter
        rvItems.setAdapter(adapter);
        // 为 RecyclerView 设置 Manager
        rvItems.setLayoutManager(layoutManager);
        return adapter.getData().size();
    }

    /**
     * 扩展 RecyclerView
     *
     * @param itemList
     */
    protected int expandRecyclerView(List<T> itemList) {
        // 添加数据
        adapter.addData(itemList);
        return adapter.getData().size();
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 创建适配器和布局管理器
        createAdapter();
        createLayoutManager();

        // 初始化刷新和加载事件
        initRefreshEvents();
        initLoadMoreEvents();

        // 刷新
        onRefresh();
    }

    /**
     * 创建 RecyclerView 适配器
     *
     */
    private void createAdapter() {
        adapter = onCreateAdapter();
    }

    /**
     * 创建 RecyclerView 布局管理器
     *
     */
    private void createLayoutManager() {
        layoutManager = onCreateLayoutManager();
    }

    /**
     * 初始化 下拉刷新 事件
     *
     */
    private void initRefreshEvents() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化 自动加载 事件
     *
     */
    private void initLoadMoreEvents() {
        adapter.setOnLoadMoreListener(this, rvItems);
    }

}
