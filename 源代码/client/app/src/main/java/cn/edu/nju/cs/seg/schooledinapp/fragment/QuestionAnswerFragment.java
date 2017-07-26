package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.adapter.QuestionAnswerItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseSwipeRefreshListFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.QuestionAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

/**
 * Created by luping on 2017/5/24.
 */

public class QuestionAnswerFragment extends BaseSwipeRefreshListFragment<QuestionAnswerItem> {

    private static final String TAG = "QuestionAnswerFragment";

    public static final String PARAM_QUESTION_ID = "PARAM_QUESTION_ID";

    private int questionId;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        questionId = getArguments().getInt(PARAM_QUESTION_ID, -1);
    }

    @Override
    protected Call<List<QuestionAnswerItem>> fetchDataCall() {
        return ApiClient.getQuestionsService().fetchAnswers(questionId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<QuestionAnswerItem> itemList) {
        return new QuestionAnswerItemAdapter(itemList);
    }

    /**
     * 创建一张新的 QuestionAnswerFragment
     *
     * @param questionId 问题信息
     * @return Fragment
     */
    public static QuestionAnswerFragment newFragment(int questionId) {
        QuestionAnswerFragment fragment = new QuestionAnswerFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_QUESTION_ID, questionId);
        fragment.setArguments(args);
        return fragment;
    }
}
