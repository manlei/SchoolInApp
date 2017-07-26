package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.StudioQuestionItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class StudioQuestionsActivity
        extends BaseSwipeRefreshListActivity<StudioQuestionItem> {

    private static final String TAG = "StudioQuestionsActivity";

    public static final String PARAM_STUDIO_ID = "PARAM_STUDIO_ID";

    // 当前工作室ID
    private int studioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        studioId = getIntent().getIntExtra(PARAM_STUDIO_ID, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Call<List<StudioQuestionItem>> fetchDataCall() {
        return ApiClient.getStudiosService().fetchQuestions(studioId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<StudioQuestionItem> itemList) {
        return new StudioQuestionItemAdapter(itemList);
    }

    /**
     * 启动 StudioQuestionsActivity
     *
     * @param context 启动上下文
     * @param studioId 用户信息
     */
    public static void startActivityWithParameters(Context context, int studioId) {
        Intent intent = new Intent(context, StudioQuestionsActivity.class);
        intent.putExtra(PARAM_STUDIO_ID, studioId);
        context.startActivity(intent);
    }

}
