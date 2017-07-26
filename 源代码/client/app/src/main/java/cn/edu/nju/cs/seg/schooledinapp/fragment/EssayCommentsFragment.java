package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.EssayCommentItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.EssayCommentItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

/**
 * Created by luping on 2017/5/24.
 */

public class EssayCommentsFragment extends BaseSwipeRefreshListFragment<EssayCommentItem> {

    private static final String TAG = "EssayCommentsFragment";

    public static final String PARAM_ESSAY_ID = "PARAM_ESSAY_ID";

    // 当前文章 ID
    private int essayId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        essayId = getArguments().getInt(PARAM_ESSAY_ID, -1);
    }

    @Override
    protected Call<List<EssayCommentItem>> fetchDataCall() {
        return ApiClient.getEssaysService().fetchComments(essayId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<EssayCommentItem> itemList) {
        return new EssayCommentItemAdapter(itemList);
    }

    /**
     * 创建一张新的 EssayCommentsFragment
     *
     * @param essayId 文章信息
     * @return Fragment
     */
    public static EssayCommentsFragment newFragment(int essayId) {
        EssayCommentsFragment fragment = new EssayCommentsFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_ESSAY_ID, essayId);
        fragment.setArguments(args);
        return fragment;
    }

}
