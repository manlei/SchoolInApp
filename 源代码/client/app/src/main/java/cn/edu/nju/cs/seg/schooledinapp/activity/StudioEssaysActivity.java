package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.StudioEssayItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioEssayItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import retrofit2.Call;


public class StudioEssaysActivity
        extends BaseSwipeRefreshListActivity<StudioEssayItem> {

    private static final String TAG = "StudioEssaysActivity";

    public static final String PARAM_STUDIO_ID = "PARAM_STUDIO_ID";

    // 当前工作室ID
    private int studioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        studioId = getIntent().getIntExtra(PARAM_STUDIO_ID, -1);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Call<List<StudioEssayItem>> fetchDataCall() {
        return ApiClient.getStudiosService().fetchEssays(studioId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<StudioEssayItem> itemList) {
        return new StudioEssayItemAdapter(itemList);
    }

    /**
     * 启动 StudioEssaysActivity
     *
     * @param context 启动上下文
     * @param studioId 用户信息
     */
    public static void startActivityWithParameters(Context context, int studioId) {
        Intent intent = new Intent(context, StudioEssaysActivity.class);
        intent.putExtra(PARAM_STUDIO_ID, studioId);
        context.startActivity(intent);
    }

}
