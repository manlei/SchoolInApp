package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupWindow;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.UserQuestionItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.UserQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class UserQuestionsActivity
        extends BaseSwipeRefreshListActivity<UserQuestionItem> {

    private static final String TAG = "UserQuestionsActivity";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    public static PopupWindow popupWindow;

    // 当前用户ID
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userId = getIntent().getIntExtra(PARAM_USER_ID, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Call<List<UserQuestionItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchQuestions(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<UserQuestionItem> itemList) {
        return new UserQuestionItemAdapter(itemList);
    }

    /**
     * 启动 UserQuestionsActivity
     *
     * @param context 启动上下文
     * @param userId 用户信息
     */
    public static void startActivityWithParameters(Context context, int userId) {
        Intent intent = new Intent(context, UserQuestionsActivity.class);
        intent.putExtra(PARAM_USER_ID, userId);
        context.startActivity(intent);
    }

}
