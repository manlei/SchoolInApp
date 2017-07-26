package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.UserStudioItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.UserStudioItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;

public class UserStudiosActivity
        extends BaseSwipeRefreshListActivity<UserStudioItem> {

    private static final String TAG = "UserStudiosActivity";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userId = getIntent().getIntExtra(PARAM_USER_ID, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Call<List<UserStudioItem>> fetchDataCall() {
        return ApiClient.getUsersService().fetchStudios(userId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<UserStudioItem> itemList) {
        return new UserStudioItemAdapter(itemList);
    }

    /**
     * 启动 UserStudiosActivity
     *
     * @param context 启动上下文
     * @param userId 用户信息
     */
    public static void startActivityWithParameters(Context context, int userId) {
        Intent intent = new Intent(context, UserStudiosActivity.class);
        intent.putExtra(PARAM_USER_ID, userId);
        context.startActivity(intent);
    }

}
