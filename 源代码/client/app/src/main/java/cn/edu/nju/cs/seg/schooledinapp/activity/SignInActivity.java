package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.model.OnlineUser;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    private static final String TAG = "SignInActivity";
    
    @BindView(R.id.tv_sign_in_schooled)
    TextView tvSchooled;

    @BindView(R.id.tv_sign_in_in)
    TextView tvIn;

    @BindView(R.id.et_sign_in_email_name_or_phone)
    EditText etEmailNameOrPhone;

    @BindView(R.id.spn_sign_in_email_host)
    Spinner spnEmailHost;

    @BindView(R.id.et_sign_in_password)
    EditText etPassword;

    @BindView(R.id.btn_sign_in_sign_in)
    Button btnSignIn;

    @BindView(R.id.btn_sign_in_forgot_password)
    Button btnForgotPassword;

    @BindView(R.id.tv_sign_in_sign_up)
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 启动 SignInActivity
     *
     * @param context 启动上下文
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        // 清空返回栈，并启动
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {

        initLogo();
        addListeners();

    }

    /**
     * 初始化 logo
     *
     */
    private void initLogo() {
        Typeface font = Typeface.createFromAsset(
                AppContext.getContext().getAssets(),
                AppContext.getContext().getResources().getString(R.string.font_brush_script_std));

        tvSchooled.setTypeface(font);
        tvIn.setTypeface(font);
    }

    /**
     * 添加事件监听器
     *
     */
    private void addListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSignInInfo()) {
                    signIn();
                }
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppContext.getContext(), "忘记密码", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.startActivityWithParameters(SignInActivity.this);
            }
        });
    }

    /**
     * 检查登录信息
     *
     * @return 信息正确返回 true
     */
    private boolean checkSignInInfo() {
        String emailNameOrPhone = etEmailNameOrPhone.getText().toString();
        String password = etPassword.getText().toString();
        if (!"".equals(emailNameOrPhone) && !"".equals(password)) {
            return true;
        } else {
            Toast.makeText(AppContext.getContext(),
                    "请补全信息 ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 执行登录操作
     *
     */
    private void signIn() {
        String emailOrPhone;
        final String password;

        int id = (int)spnEmailHost.getSelectedItemId();
        if (id == 2) {
            // 手机登录
            emailOrPhone = etEmailNameOrPhone.getText().toString();
        } else {
            // 邮箱登录
            emailOrPhone = etEmailNameOrPhone.getText().toString() +
                    getResources().getStringArray(R.array.email_host_of_nju)[id];
        }
        password = etPassword.getText().toString();

        // 进度条
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(
                SignInActivity.this, R.string.pd_sign_in_sign_in_tips);

        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("user_email_or_phone", emailOrPhone)
                .append("user_password", password)
                .build();

        // 发送请求
        IndexService indexService = ApiClient.getIndexService();
        indexService.signIn(body).enqueue(new Callback<OnlineUser>() {
            @Override
            public void onResponse(Call<OnlineUser> call, Response<OnlineUser> response) {
                switch (response.code()) {
                    case 200: {
                        progressDialog.dismiss();
                        // 成功登录
                        OnlineUser u = response.body();
                        u.setPassword(password);
                        bindAliasAndTagsThenConfirm(u);
                    } break;

                    case 400: {
                        progressDialog.dismiss();
                        DialogUtil.showAlertDialog(
                                SignInActivity.this,
                                "请输入正确的南京大学邮箱",
                                getResources().getString(R.string.error_message_sign_in_error));
                    } break;

                    case 401: {
                        progressDialog.dismiss();
                        DialogUtil.showAlertDialog(
                                SignInActivity.this,
                                R.string.error_message_401,
                                R.string.error_message_sign_in_error);
                    } break;

                    case 503: {
                        progressDialog.dismiss();
                        DialogUtil.showAlertDialog(
                                SignInActivity.this,
                                R.string.error_message_503,
                                R.string.error_message_sign_in_error);
                    } break;
                }
            }

            @Override
            public void onFailure(Call<OnlineUser> call, Throwable t) {
                progressDialog.dismiss();
                DialogUtil.showAlertDialog(SignInActivity.this,
                        R.string.error_message_network,
                        R.string.error_message_sign_in_error);
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * 设置别名、标签
     *
     * @param u
     */
    private void bindAliasAndTagsThenConfirm(final OnlineUser u) {
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(
                SignInActivity.this,
                "正在请求服务器...");
        AppContext.bindAliasAndTags(
                u.getAlias(),
                new HashSet<String>(u.getTags()),
                new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                progressDialog.dismiss();
                switch (responseCode) {
                    case 0: {
                        confirmSignIn(u);
                    } break;

                    default: {
                        DialogUtil.showAlertDialog(SignInActivity.this,
                                "请求服务器出错");
                    }
                }
            }
        });
    }

    /**
     * 确认登录
     *
     * @param u
     */
    private void confirmSignIn(final OnlineUser u) {
        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(
                SignInActivity.this,
                "正在验证...");

        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("user_id", u.getId())
                .append("alias", u.getAlias())
                .append("tags", u.getTags())
                .build();

        // 发送请求
        IndexService indexService = ApiClient.getIndexService();
        indexService.signInConfirm(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                switch (response.code()) {
                    case 201: {
                        // 成功确认
                        AppContext.signIn(u);
                        IndexActivity.startActivityWithParameters(SignInActivity.this);
                    } break;

                    case 400: {
                        DialogUtil.showAlertDialog(
                                SignInActivity.this,
                                "验证出错...",
                                getResources().getString(R.string.error_message_sign_in_error));
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                SignInActivity.this,
                                R.string.error_message_503,
                                R.string.error_message_sign_in_error);
                    } break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                DialogUtil.showAlertDialog(SignInActivity.this,
                        R.string.error_message_network,
                        R.string.error_message_sign_in_error);
                LogUtil.e(TAG, t.getMessage());
            }
        });
    }

}
