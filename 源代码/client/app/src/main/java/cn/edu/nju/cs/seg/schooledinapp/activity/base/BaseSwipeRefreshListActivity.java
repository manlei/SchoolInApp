package cn.edu.nju.cs.seg.schooledinapp.activity.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseSwipeRefreshListActivity<T> extends BaseActivity {

    private static final String TAG = "BaseSwipeRefreshListActivity";

    @BindView(R.id.tb_base_activity_swipe_refresh_list_toolbar)
    Toolbar tbToolbar;
    
    @BindView(R.id.srl_base_activity_swipe_refresh_list_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    
    @BindView(R.id.rv_base_activity_swipe_refresh_list_items)
    RecyclerView rvItems;

    // 项目列表
    private List<T> itemList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_swipe_refresh_list);
        ButterKnife.bind(this);
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
     * 获取 Toolbar
     *
     * @return Toolbar
     */
    public Toolbar getTbToolbar() {
        return tbToolbar;
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
        return new LinearLayoutManager(this);
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
                DialogUtil.showAlertDialog(this, R.string.error_message_404);
            } break;

            case 503: {
                DialogUtil.showAlertDialog(this, R.string.error_message_503);
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
        // 设置 toolbar
        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 设置下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFetchData();
            }
        });

        // 获取数据并初始化 RecyclerView
        fetchData();
    }

    /**
     * 初始化数据并设置 RecyclerView
     *
     */
    private void fetchData() {
        // 进度条
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(this);

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
