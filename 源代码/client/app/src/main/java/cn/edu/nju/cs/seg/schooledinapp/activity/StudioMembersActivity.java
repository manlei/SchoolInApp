package cn.edu.nju.cs.seg.schooledinapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseSwipeRefreshListActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.StudioMemberItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddMembersDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioMemberItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.StudiosService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudioMembersActivity
        extends BaseSwipeRefreshListActivity<StudioMemberItem>
        implements CustomBaseDialog.DialogResultListener {

    private static final String TAG = "StudioMembersActivity";

    public static final String PARAM_STUDIO_ID = "PARAM_STUDIO_ID";

    public static final String PARAM_MANAGER_ID = "PARAM_MANAGER_ID";

    // 当前工作室ID
    private int studioId;

    // 管理员ID
    private int managerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        studioId = getIntent().getIntExtra(PARAM_STUDIO_ID, -1);
        managerId = getIntent().getIntExtra(PARAM_MANAGER_ID, -1);
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    @Override
    protected Call<List<StudioMemberItem>> fetchDataCall() {
        return ApiClient.getStudiosService().fetchMembers(studioId);
    }

    @Override
    protected BaseItemAdapter getAdapter(List<StudioMemberItem> itemList) {
        return new StudioMemberItemAdapter(itemList,studioId);
    }

    @Override
    public void onDialogResult(int whichbutton, Bundle bundle) {
        switch (whichbutton) {

            case CustomBaseDialog.BUTTON_POSITIVE:
                String[] members = bundle.getStringArray(AddMembersDialog.ADD_MEMBERS_RESULT);
                doAddMembers(members);
                break;

            case CustomBaseDialog.BUTTON_NEGATIVE:
                break;

        }
    }

    /**
     * 启动 StudioMembersActivity
     *
     * @param activity 启动上下文
     * @param studioId 用户信息
     * @param managerId 管理员ID
     */
    public static void startActivityWithParametersForResult(
            Activity activity,
            int requestCode,
            int studioId, int managerId) {
        Intent intent = new Intent(activity, StudioMembersActivity.class);
        intent.putExtra(PARAM_STUDIO_ID, studioId);
        intent.putExtra(PARAM_MANAGER_ID, managerId);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 点击添加成员
     *
     */
    private void onClickAddMembers() {
        AddMembersDialog dialog = new AddMembersDialog(this, this);
        dialog.show();
    }

    /**
     * 发送并添加
     *
     * @param members
     */
    private void doAddMembers(String[] members) {
        if (members == null) return ;

        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("manager_email_or_phone", AppContext.getOnlineUser().getEmail())
                .append("manager_password", AppContext.getOnlineUser().getPassword())
                .append("members", members)
                .build();

        StudiosService studiosService = ApiClient.getStudiosService();
        studiosService.updateStudio(studioId, body).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch (response.code()) {

                    case 204: {
                        Toast.makeText(AppContext.getContext(),
                                "添加成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        StudioMembersActivity.this.reFetchData();
                    } break;

                    case 401: {
                        DialogUtil.showAlertDialog(
                                StudioMembersActivity.this,
                                R.string.error_message_401,
                                R.string.error_message_add_studio_member);
                    } break;

                    case 404: {
                        DialogUtil.showAlertDialog(
                                StudioMembersActivity.this,
                                R.string.error_message_404,
                                R.string.error_message_add_studio_member);
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                StudioMembersActivity.this,
                                R.string.error_message_503,
                                R.string.error_message_add_studio_member);
                    } break;

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                DialogUtil.showAlertDialog(
                        StudioMembersActivity.this,
                        R.string.error_message_network,
                        R.string.error_message_add_studio_member);
            }

        });

    }

    /**
     * 初始化 Toolbar
     *
     */
    private void initToolbar() {
        // 是管理员，添加 "添加成员" 按钮
        if (AppContext.getOnlineUser().getId() == managerId) {
            ImageView ivAddMembers = new ImageView(this);
            ivAddMembers.setImageDrawable(getDrawable(R.drawable.icon_add));
            ivAddMembers.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            innerParams.setMargins(0, 0, (int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0);
            LinearLayout innerLinearLayout = new LinearLayout(this);
            innerLinearLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            innerLinearLayout.setLayoutParams(innerParams);
            innerLinearLayout.addView(ivAddMembers);

            Toolbar toolbar = getTbToolbar();
            toolbar.addView(innerLinearLayout);

            ivAddMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickAddMembers();
                }
            });
        }
    }

}
