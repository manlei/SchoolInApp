package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = "SignUpActivity";

    @BindView(R.id.tv_sign_up_schooled)
    TextView tvSchooled;

    @BindView(R.id.tv_sign_up_in)
    TextView tvIn;

    @BindView(R.id.et_sign_up_email)
    EditText etEmail;

    @BindView(R.id.et_sign_up_password)
    EditText etPassword;

    @BindView(R.id.et_sign_up_validation_code)
    EditText etValidationCode;

    @BindView(R.id.tv_sign_up_send_validation_code)
    TextView tvSendValidationCode;

    @BindView(R.id.tv_sign_up_remaining_time)
    TextView tvRemainingTime;

    @BindView(R.id.cb_sign_up_protocol)
    AppCompatCheckBox cbProtocol;

    @BindView(R.id.btn_sign_up_sign_up)
    Button btnSignUp;

    @BindView(R.id.tv_sign_up_sign_in)
    TextView tvSignIn;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 启动 SignUpActivity
     *
     * @param context 启动上下文
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化 View
     */
    private void initView() {

        initLogo();
        addListeners();

    }

    /**
     * 初始化 logo
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
     */
    private void addListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String validationCode = etValidationCode.getText().toString();
                String password = etPassword.getText().toString();

                if (cbProtocol.isChecked() &&
                        !"".equals(email) &&
                        !"".equals(validationCode) &&
                        !"".equals(password)) {
                    // 构建请求体
                    RequestBody body = new JsonRequestBodyBuilder()
                            .append("email", email)
                            .append("password", password)
                            .append("verification_code", validationCode)
                            .build();
                    // 进度条
                    final ProgressDialog processDialog = DialogUtil.showProgressDialog(
                            SignUpActivity.this, R.string.pd_sign_in_sign_in_tips);

                    // 发送请求
                    UsersService usersService = ApiClient.getUsersService();
                    usersService.createUser(body).enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            processDialog.dismiss();

                            switch (response.code()) {
                                case 201: {
                                    // 成功注册
                                    DialogUtil.showAlertDialog(
                                            SignUpActivity.this,
                                            "成功注册，请登录",
                                            "注册", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SignInActivity.startActivityWithParameters(SignUpActivity.this);
                                                }
                                            });
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                    // 隐藏剩余时间
                                    tvRemainingTime.setVisibility(View.GONE);
                                    // 显示发送验证码
                                    tvSendValidationCode.setVisibility(View.VISIBLE);
                                } break;

                                case 401: {
                                    DialogUtil.showAlertDialog(
                                            SignUpActivity.this,
                                            "验证码错误",
                                            getResources().getString(R.string.error_message_sign_up_error));
                                } break;

                                case 503: {
                                    DialogUtil.showAlertDialog(
                                            SignUpActivity.this,
                                            R.string.error_message_503,
                                            R.string.error_message_sign_up_error);
                                } break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            processDialog.dismiss();
                            DialogUtil.showAlertDialog(SignUpActivity.this,
                                    R.string.error_message_network,
                                    R.string.error_message_sign_up_error);
                        }
                    });
                } else {
                    Toast.makeText(AppContext.getContext(),
                            "是否同意服务条款?",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvSendValidationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();

                if ("".equals(email)) {
                    Toast.makeText(AppContext.getContext(),
                            "请输入邮箱",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 构建请求体
                    final RequestBody body = new JsonRequestBodyBuilder()
                            .append("email", email)
                            .build();

                    // 发送请求
                    UsersService usersService = ApiClient.getUsersService();
                    usersService.requestVerifyEmail(body).enqueue(new Callback<Map<String, Long>>() {
                        @Override
                        public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                            switch (response.code()) {
                                case 201: {
                                    Map<String, Long> body = response.body();
                                    long retainUntil = body.get("retain_until");
                                    int remainingSeconds = (int)(retainUntil - new Date().getTime()) / 1000;

                                    // 隐藏发送验证码
                                    tvSendValidationCode.setVisibility(View.GONE);
                                    // 显示剩余时间
                                    tvRemainingTime.setText("60");
                                    tvRemainingTime.setVisibility(View.VISIBLE);

                                    timer = new CountDownTimer(60 * 1000, 1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            tvRemainingTime.setText(
                                                    Long.toString(millisUntilFinished / 1000));
                                        }

                                        @Override
                                        public void onFinish() {
                                            // 隐藏剩余时间
                                            tvRemainingTime.setVisibility(View.GONE);
                                            // 显示发送验证码
                                            tvSendValidationCode.setVisibility(View.VISIBLE);
                                            // timer置空
                                            timer = null;
                                        }

                                    };

                                    // 开始计时
                                    timer.start();
                                } break;

                                case 400: {
                                    DialogUtil.showAlertDialog(
                                            SignUpActivity.this,
                                            "请输入正确的南京大学邮箱",
                                            getResources().getString(R.string.error_message_sign_up_error));
                                } break;

                                case 409: {
                                    if (tvRemainingTime.getVisibility() == View.VISIBLE) {
                                        DialogUtil.showAlertDialog(
                                                SignUpActivity.this,
                                                "请于一段时间后再输入",
                                                getResources().getString(R.string.error_message_sign_up_error));
                                    } else {
                                        DialogUtil.showAlertDialog(
                                                SignUpActivity.this,
                                                "该用户已注册",
                                                getResources().getString(R.string.error_message_sign_up_error));
                                    }
                                } break;

                                case 503: {
                                    DialogUtil.showAlertDialog(
                                            SignUpActivity.this,
                                            R.string.error_message_503,
                                            R.string.error_message_sign_in_error);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                            DialogUtil.showAlertDialog(SignUpActivity.this,
                                    R.string.error_message_network,
                                    R.string.error_message_sign_up_error);
                        }
                    });
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInActivity.startActivityWithParameters(SignUpActivity.this);
            }
        });
    }

}
