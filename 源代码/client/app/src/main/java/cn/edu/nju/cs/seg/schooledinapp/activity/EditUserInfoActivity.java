package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.interf.OnAppBarStateChangedListener;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserInfoActivity extends BaseActivity {

    private static final String TAG = "EditUserInfoActivity";

    // 当前用户
    private User user;

    @BindView(R.id.abl_edit_user_info_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    @BindView(R.id.ctbl_edit_user_info_toolbar_layout)
    CollapsingToolbarLayout ctblToolbarLayout;

    @BindView(R.id.tb_edit_user_info_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.civ_edit_user_info_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_edit_user_info_header_name)
    TextView tvHeaderName;

    @BindView(R.id.et_edit_user_info_name)
    EditText etName;

    @BindView(R.id.tv_edit_user_info_email)
    TextView tvEmail;

    @BindView(R.id.tv_edit_user_info_phone)
    TextView tvPhone;

    @BindView(R.id.et_edit_user_info_department)
    EditText etDepartment;

    @BindView(R.id.tv_edit_user_info_save)
    TextView tvSave;

    @BindView(R.id.spn_edit_user_info_sex)
    Spinner spnSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    /**
     * 启动 EditUserInfoActivity
     *
     * @param context 启动上下文
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 设置 toolbar
        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        // 添加监听器
        addListeners();

        // 获取用户信息并初始化
        fetchUserAndInit();
;
    }

    /**
     * 获取用户并初始化
     *
     */
    private void fetchUserAndInit() {
        user = AppContext.getOnlineUser();

        // 设置个人信息
        Glide.with(EditUserInfoActivity.this)
                .load(user.getAvatarUrl())
                .into(ivAvatar);
        tvHeaderName.setText(user.getName());
        etName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        etDepartment.setText(user.getDepartment());
        if (user.getSex().equals("female")) {
            spnSex.setSelection(2);
        } else if (user.getSex().equals("male")) {
            spnSex.setSelection(1);
        } else {
            spnSex.setSelection(0);
        }
    }

    /**
     * 添加监听器
     *
     */
    private void addListeners() {
        ablAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                switch (state) {
                    case CLOSE_COLLAPSEC:
                        String title = user.getName();

                        if (title == null || "".equals(title)) {
                            title = tvHeaderName.getHint().toString();
                        }

                        ctblToolbarLayout.setTitle(title);
                        ctblToolbarLayout.setTitleEnabled(true);
                        break;
                    case CLOSE_EXPANDED:
                        ctblToolbarLayout.setTitle("");
                        ctblToolbarLayout.setTitleEnabled(false);
                        break;
                }
            }
        });

        // 保存
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User onlineUser = AppContext.getOnlineUser();

                String name = etName.getText().toString().trim();
                String department = etDepartment.getText().toString().trim();
                String sex = getResources()
                        .getStringArray(R.array.edit_user_info_sex)[spnSex.getSelectedItemPosition()];

                // 构建请求体
                JsonRequestBodyBuilder builder = new JsonRequestBodyBuilder();
                if (name != null &&
                        !"".equals(name) &&
                        !onlineUser.getName().equals(name)) {
                    builder.append("name", name);
                }
                if (department != null &&
                        !"".equals(department) &&
                        !onlineUser.getDepartment().equals(department)) {
                    builder.append("department", department);
                }
                if (!sex.equals(onlineUser.getSex())) {
                    builder.append("sex", sex);
                }
                if (builder.count() != 0) {
                    builder.append("password", onlineUser.getPassword());
                } else {
                    Toast.makeText(AppContext.getContext(),
                            "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                    return ;
                }

                // 发送请求
                UsersService usersService = ApiClient.getUsersService();
                usersService.updateUser(onlineUser.getId(), builder.build()).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        switch (response.code()) {
                            case 204 : {
                                Toast.makeText(AppContext.getContext(),
                                        "更新成功", Toast.LENGTH_SHORT).show();

                                // 更新全局信息
                                AppContext.updateOnlineUser();

                                finish();
                            } break;

                            case 401: {
                                DialogUtil.showAlertDialog(
                                        EditUserInfoActivity.this,
                                        R.string.error_message_401,
                                        R.string.error_message_edit_user_error);
                            } break;

                            case 404: {
                                DialogUtil.showAlertDialog(
                                        EditUserInfoActivity.this,
                                        R.string.error_message_404,
                                        R.string.error_message_edit_user_error);
                            } break;

                            case 503: {
                                DialogUtil.showAlertDialog(
                                        EditUserInfoActivity.this,
                                        R.string.error_message_503,
                                        R.string.error_message_edit_user_error);
                            } break;
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        DialogUtil.showAlertDialog(
                                EditUserInfoActivity.this,
                                R.string.error_message_network,
                                R.string.error_message_edit_user_error);
                    }
                });
            }
        });
    }
    
}
