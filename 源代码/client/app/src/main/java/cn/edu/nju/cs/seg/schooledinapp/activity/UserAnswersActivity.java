package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.UserAnswerItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.UserAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class UserAnswersActivity
        extends BaseSwipeRefreshListActivity<UserAnswerItem> {

    private static final String TAG = "UserAnswersActivity";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userId = getIntent().getIntExtra(PARAM_USER_ID, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Call<List<UserAnswerItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchAnswers(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<UserAnswerItem> itemList) {
        return new UserAnswerItemAdapter(itemList);
    }

    /**
     * 启动 UserAnswersActivity
     *
     * @param context 启动上下文
     * @param userId 用户信息
     */
    public static void startActivityWithParameters(Context context, int userId) {
        Intent intent = new Intent(context, UserAnswersActivity.class);
        intent.putExtra(PARAM_USER_ID, userId);
        context.startActivity(intent);
    }

}
