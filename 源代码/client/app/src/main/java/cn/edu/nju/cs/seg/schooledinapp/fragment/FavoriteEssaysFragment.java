package cn.edu.nju.cs.seg.schooledinapp.fragment;


import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.FavoriteEssayItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteEssayItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;


public class FavoriteEssaysFragment extends BaseSwipeRefreshListFragment<FavoriteEssayItem> {

    private static final String TAG = "MyFavoriteEssaysFragmen";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt(PARAM_USER_ID, -1);
    }

    @Override
    protected Call<List<FavoriteEssayItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchFavoriteEssays(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<FavoriteEssayItem> itemList) {
        return new FavoriteEssayItemAdapter(itemList);
    }

    /**
     * 创建一张新的 FavoriteEssaysFragment
     *
     * @param userId 用户信息
     * @return Fragment
     */
    public static FavoriteEssaysFragment newFragment(int userId) {
        FavoriteEssaysFragment fragment = new FavoriteEssaysFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

}
