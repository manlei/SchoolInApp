package cn.edu.nju.cs.seg.schooledinapp.fragment.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseSwipeRefreshListFragment<T> extends BaseFragment {

    private static final String TAG = "BaseSwipeRefreshListFra";
    
    @BindView(R.id.rv_base_fragment_swipe_refresh_list_items)
    RecyclerView rvItems;

    @BindView(R.id.srl_base_fragment_swipe_refresh_list_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    // 项目列表
    private List<T> itemList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_base_swipe_refresh_list, container, false);
        ButterKnife.bind(this, v);
        initView();

        return v;
    }

    /**
     * 获取项目列表
     *
     * @return 项目列表
     */
    public List<T> getItemList() {
        return itemList;
    }

    /**
     * 获取 RecyclerView
     *
     * @return RecyclerView
     */
    public RecyclerView getItemsRecyclerView() {
        return rvItems;
    }

    /**
     * 获取数据
     *
     * @return 数据获取
     */
    protected abstract Call<List<T>> fetchDataCall();

    /**
     * 获取 Adapter 来管理 RecyclerView
     *
     * @return
     */
    protected abstract BaseItemAdapter getAdapter(List<T> itemList);

    /**
     * 获取 LayoutManager 来管理 RecyclerView
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    /**
     * 设置 RecylerView 前的处理
     *
     * @param rv RecyclerView
     * @param itemList 项目列表
     */
    protected void beforeSetupRecyclerView(RecyclerView rv, List<T> itemList) { }

    /**
     * 设置 RecylerView 后的处理
     *
     * @param rv RecyclerView
     * @param itemList 项目列表
     */
    protected void afterSetupRecyclerView(RecyclerView rv, List<T> itemList) { }

    /**
     * 成功返回处理
     *
     * @param call API 调用
     * @param response 回复
     */
    protected void doResponse(Call<List<T>> call, Response<List<T>> response) {
        switch (response.code()) {
            case 200: {
                itemList = response.body();

                beforeSetupRecyclerView(rvItems, itemList);

                // 为 RecyclerView 设置 Adapter
                rvItems.setAdapter(getAdapter(itemList));
                // 为 RecyclerView 设置 Manager
                rvItems.setLayoutManager(getLayoutManager());

                afterSetupRecyclerView(rvItems, itemList);
            } break;

            case 404: {
                DialogUtil.showAlertDialog(getActivity(), R.string.error_message_404);
            } break;

            case 503: {
                DialogUtil.showAlertDialog(getActivity(), R.string.error_message_503);
            } break;
        }
    }

    /**
     * 失败返回处理
     *
     * @param call API 调用
     * @param t t
     */
    protected void doFailure(Call<List<T>> call, Throwable t) { }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 设置下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFetchData();
            }
        });

        fetchData();
    }

    /**
     * 初始化数据并设置 RecyclerView
     *
     */
    private void fetchData() {
        // 进度条
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(getActivity());

        // 获取数据
        fetchDataCall().enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                progressDialog.dismiss();
                doResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                progressDialog.dismiss();
                doFailure(call, t);
            }
        });
    }

    /**
     * 下拉刷新重新获取数据并设置 RecyclerView
     *
     */
    public void reFetchData() {
        // 获取数据
        fetchDataCall().enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                swipeRefreshLayout.setRefreshing(false);
                doResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                doFailure(call, t);
            }
        });
    }

}
