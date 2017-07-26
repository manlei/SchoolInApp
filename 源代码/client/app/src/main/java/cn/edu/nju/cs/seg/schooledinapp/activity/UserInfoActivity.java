package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.BindPhoneDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.UpdatePasswordDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.interf.OnAppBarStateChangedListener;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity
        implements CustomBaseDialog.DialogResultListener {

    private static final String TAG = "UserInfoActivity";

    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    // 当前用户ID
    private int userId;

    // 当前用户
    private User user;

    @BindView(R.id.abl_user_info_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    @BindView(R.id.ctbl_user_info_toolbar_layout)
    CollapsingToolbarLayout ctblToolbarLayout;

    @BindView(R.id.tb_user_info_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.civ_user_info_header_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_user_info_header_name)
    TextView tvHeaderName;

    @BindView(R.id.tv_user_info_name)
    TextView tvName;

    @BindView(R.id.tv_user_info_email)
    TextView tvEmail;

    @BindView(R.id.tv_user_info_phone)
    TextView tvPhone;

    @BindView(R.id.tv_user_info_department)
    TextView tvDepartment;

    @BindView((R.id.tv_user_info_questions))
    TextView tvQuestions;

    @BindView(R.id.tv_user_info_answers)
    TextView tvAnswers;

    @BindView(R.id.ll_user_info_questions)
    LinearLayout llQuestions;

    @BindView(R.id.ll_user_info_answers)
    LinearLayout llAnswers;

    @BindView(R.id.fab_user_info_fab_menu)
    FloatingActionsMenu fabFabMenu;

    @BindView(R.id.fab_user_info_fab_by_text)
    FloatingActionButton fabByText;

    @BindView(R.id.fab_user_info_fab_by_voice)
    FloatingActionButton fabByVoice;


    @BindView(R.id.tv_user_info_edit)
    TextView tvEdit;

    @BindView(R.id.civ_user_info_header_sex)
    ImageView ivSex;

    @BindView(R.id.btn_user_info_change_password)
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        userId = getIntent().getIntExtra(PARAM_USER_ID, -1);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 重新加载更新用户信息
        if (userId == AppContext.getOnlineUser().getId() &&
                AppContext.isOnlineUserUpdated()) {
            user = AppContext.getOnlineUser();
            updateView();
        }

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

    @Override
    public void onDialogResult(int whichbutton, Bundle bundle) {
        switch (whichbutton) {
            case UpdatePasswordDialog.BUTTON_POSITIVE: {
                int updateResult = bundle.getInt(UpdatePasswordDialog.UPDATE_RESULT);
                if (updateResult != UpdatePasswordDialog.UPDATE_SUCCEEDED) {
                    return;
                }
                DialogUtil.showAlertDialog(this, "密码修改成功，请重新登录", "提示",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 注销
                                AppContext.signOut();
                                // 跳回登陆页面
                                SignInActivity.startActivityWithParameters(UserInfoActivity.this);
                            }
                        });
            }
        }
    }

    /**
     * 启动 UserInfoActivity
     *
     * @param context 启动上下文
     * @param userId 用户id
     */
    public static void startActivityWithParameters(Context context, int userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(PARAM_USER_ID, userId);
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

        // 获取用户并初始化
        fetchUserAndInit();
    }

    /**
     * 刷新界面
     *
     */
    private void updateView() {
        if (user == null) {
            return;
        }

        // 设置个人信息
        Glide.with(UserInfoActivity.this)
                .load(user.getAvatarUrl())
                .into(ivAvatar);
        tvHeaderName.setText(user.getName());
        tvQuestions.setText(Integer.toString(user.getQuestions()));
        tvAnswers.setText(Integer.toString(user.getAnswers()));
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            // 清除绑定手机事件
            tvPhone.setOnClickListener(null);
        }
        tvPhone.setText(user.getPhone());
        tvDepartment.setText(user.getDepartment());
        if (user.getSex().equals("male")) {
            ivSex.setVisibility(View.VISIBLE);
            Glide.with(UserInfoActivity.this)
                    .load(R.drawable.icon_sex_male)
                    .into(ivSex);
        } else if (user.getSex().equals("female")) {
            ivSex.setVisibility(View.VISIBLE);
            Glide.with(UserInfoActivity.this)
                    .load(R.drawable.icon_sex_female)
                    .into(ivSex);
        } else {
            ivSex.setVisibility(View.GONE);
        }

    }

    /**
     * 显示登录用户必要的组件
     *
     */
    private void showRequiredComponents() {
        tvEdit.setVisibility(View.VISIBLE);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserInfoActivity.startActivityWithParameters(UserInfoActivity.this);
            }
        });

        btnChangePassword.setVisibility(View.VISIBLE);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePasswordDialog d = new UpdatePasswordDialog(
                        UserInfoActivity.this,
                        UserInfoActivity.this);
                d.show();
            }
        });

        tvPhone.setHint(R.string.tv_user_info_bind_phone);
        tvPhone.setHintTextColor(getResources().getColor(R.color.colorAccent));
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BindPhoneDialog d = new BindPhoneDialog(UserInfoActivity.this);
                d.show();
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAvatarActivity.startActivityWithParameters(UserInfoActivity.this,
                        AppContext.getOnlineUser().getAvatarUrl());
            }
        });
    }

    /**
     * 获取用户并初始化
     *
     */
    private void fetchUserAndInit() {
        // 登录用户直接刷新界面
        if (userId == AppContext.getOnlineUser().getId()) {
            user = AppContext.getOnlineUser();
            showRequiredComponents();
            updateView();
            return ;
        }

        // 不是登录用户
        UsersService usersService = ApiClient.getUsersService();

        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(
                this, "正在加载用户信息...");

        usersService.fetchOne(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();

                switch (response.code()) {
                    case 200: {
                        user = response.body();

                        if (user != null && user.getId() == AppContext.getOnlineUser().getId()) {
                            // 显示 编辑 按钮
                            showRequiredComponents();
                        }

                        // 刷新
                        updateView();
                    } break;

                    case 404: {
                        DialogUtil.showAlertDialog(
                                UserInfoActivity.this, R.string.error_message_404);
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                UserInfoActivity.this, R.string.error_message_503);
                    } break;
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                DialogUtil.showAlertDialog(UserInfoActivity.this,
                        R.string.error_message_network);
            }
        });
    }

    /**
     * 添加监听器
     *
     */
    private void addListeners() {
        ablAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                if (user == null) {
                    return;
                }

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

        llQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    return;
                }

                UserQuestionsActivity.startActivityWithParameters(
                        UserInfoActivity.this, user.getId());
            }
        });

        llAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    return;
                }

                UserAnswersActivity.startActivityWithParameters(
                        UserInfoActivity.this, user.getId());
            }
        });

        fabByText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    return;
                }

                AskQuestionActivity.startAskQuestionActivity(UserInfoActivity.this);
            }
        });

        fabByVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    return;
                }

                AskQuestionByAudioActivity.startActivityWithParameters(UserInfoActivity.this);
            }
        });

    }

}
