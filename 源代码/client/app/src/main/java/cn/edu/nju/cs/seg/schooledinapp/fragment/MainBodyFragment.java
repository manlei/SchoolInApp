package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.EssayActivity;
import cn.edu.nju.cs.seg.schooledinapp.activity.QuestionActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.MainItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseRecyclerViewFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.MainItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainBodyFragment
        extends BaseRecyclerViewFragment<MainItem>
        implements BaseQuickAdapter.OnItemClickListener {

    private static final String TAG = "MainBodyFragment";

    // 当前页面偏移量
    private int offset = 0;

    // 页面大小
    private static final int LIMIT = 30;

    @Override
    protected BaseQuickAdapter onCreateAdapter() {
        BaseQuickAdapter adapter = new MainItemAdapter(getActivity(), null);
        adapter.setOnItemClickListener(this);

        return adapter;
    }

    @Override
    protected void onRefresh(BaseQuickAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        offset = 0;

        // 发送请求
        IndexService indexService = ApiClient.getIndexService();

        indexService.fetchIndexItems(offset, LIMIT)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {

                        switch (response.code()) {

                            case 200: {
                                List<Map<String, Object>> body = response.body();

                                List<MainItem> mainItems = new ArrayList<>();
                                MainItem mainItem;

                                for (Map<String, Object> item : body) {
                                    mainItem = MainItem.newMainItem(item);

                                    if (mainItem != null) {
                                        mainItems.add(mainItem);
                                    }
                                }

                                if (mainItems.size() != 0) {
                                    offset = setUpRecyclerView(mainItems);
                                }

                                refreshEnd();
                            } break;

                            case 400: {
                                refreshEnd();
                                DialogUtil.showAlertDialog(getActivity(), "Offset 或 limit 缺失");
                            } break;

                            case 503: {
                                refreshEnd();
                                DialogUtil.showAlertDialog(getActivity(), R.string.error_message_503);
                            } break;

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        refreshEnd();
                        DialogUtil.showAlertDialog(getActivity(), R.string.error_message_network);
                    }
                });

    }

    @Override
    protected void onLoadMore(BaseQuickAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        // 发送请求
        IndexService indexService = ApiClient.getIndexService();

        indexService.fetchIndexItems(offset, LIMIT)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {

                        switch (response.code()) {

                            case 200: {
                                List<Map<String, Object>> body = response.body();

                                List<MainItem> mainItems = new ArrayList<>();
                                MainItem mainItem;

                                for (Map<String, Object> item : body) {
                                    mainItem = MainItem.newMainItem(item);

                                    if (mainItem != null) {
                                        mainItems.add(mainItem);
                                    }
                                }

                                if (mainItems.size() != 0) {
                                    offset = expandRecyclerView(mainItems);
                                    loadMoreComplete();
                                } else {
                                    loadMoreEnd();
                                }
                            } break;

                            case 400: {
                                loadMoreFail();
                                DialogUtil.showAlertDialog(getActivity(), "Offset 或 limit 缺失");
                            } break;

                            case 503: {
                                loadMoreFail();
                                DialogUtil.showAlertDialog(getActivity(), R.string.error_message_503);
                            } break;

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        loadMoreFail();
                        DialogUtil.showAlertDialog(getActivity(), R.string.error_message_network);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<MainItem> itemList = getItemList();
        MainItem item =  itemList.get(position);

        switch (item.getItemType()) {
            case MainItem.QUESTION: {
                QuestionActivity.startActivityWithParameters(getActivity(), item.getId());
            } break;

            case MainItem.ESSAY: {
                EssayActivity.startActivityWithParameters(getActivity(), item.getId());
            } break;
        }


        adapter.notifyDataSetChanged();
    }

    /**
     * 创建一张新的 MainBodyFragment
     *
     * @return MainBodyFragment
     */
    public static MainBodyFragment newFragment() {
        MainBodyFragment fragment = new MainBodyFragment();
        return fragment;
    }

}
