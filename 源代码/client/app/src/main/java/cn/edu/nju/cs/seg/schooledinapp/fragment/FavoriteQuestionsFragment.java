package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.FavoriteQuestionItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class FavoriteQuestionsFragment extends BaseSwipeRefreshListFragment<FavoriteQuestionItem> {

    private static final String TAG = "MyFavoriteQuestionsFrag";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt(PARAM_USER_ID, -1);
    }

    @Override
    protected Call<List<FavoriteQuestionItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchFavoriteQuestions(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<FavoriteQuestionItem> itemList) {
        return new FavoriteQuestionItemAdapter(itemList);
    }

    /**
     * 创建一张新的 FavoriteQuestionsFragment
     *
     * @param userId 用户ID
     * @return Fragment
     */
    public static FavoriteQuestionsFragment newFragment(int userId) {
        FavoriteQuestionsFragment fragment = new FavoriteQuestionsFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

}
