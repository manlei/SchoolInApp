package cn.edu.nju.cs.seg.schooledinapp.fragment;


import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.FavoriteAnswerItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class FavoriteAnswersFragment extends BaseSwipeRefreshListFragment<FavoriteAnswerItem> {

    private static final String TAG = "MyFavoriteAnswersFragme";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt(PARAM_USER_ID, -1);
    }

    @Override
    protected Call<List<FavoriteAnswerItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchFavoriteAnswers(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<FavoriteAnswerItem> itemList) {
        return  new FavoriteAnswerItemAdapter(itemList);
    }

    /**
     * 创建一张新的 FavoriteAnswersFragment
     *
     * @param userId 用户信息
     * @return Fragment
     */
    public static FavoriteAnswersFragment newFragment(int userId) {
        FavoriteAnswersFragment fragment = new FavoriteAnswersFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

}
