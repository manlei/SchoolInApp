package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.AnswerCommentItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.AnswerCommentItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

/**
 * Created by luping on 2017/5/24.
 */

public class AnswerCommentsFragment extends BaseSwipeRefreshListFragment<AnswerCommentItem> {

    private static final String TAG = "AnswerCommentsFragment";

    public static final String PARAM_ANSWER_ID = "PARAM_ANSWER_ID";

    // 当前回答ID
    private int answerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerId = getArguments().getInt(PARAM_ANSWER_ID, -1);
    }

    @Override
    protected Call<List<AnswerCommentItem>> fetchDataCall() {
        return ApiClient.getAnswersService().fetchComments(answerId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<AnswerCommentItem> itemList) {
        return new AnswerCommentItemAdapter(itemList);
    }

    /**
     * 创建一张新的 AnswerCommentsFragment
     *
     * @param answerId 文章信息
     * @return Fragment
     */
    public static AnswerCommentsFragment newFragment(int answerId) {
        AnswerCommentsFragment fragment = new AnswerCommentsFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_ANSWER_ID, answerId);
        fragment.setArguments(args);
        return fragment;
    }
}
