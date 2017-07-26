package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Map;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BindPhoneDialog extends CustomBaseDialog {

    private CountDownTimer timer;

    private TextView tvSendValidationCode;

    private EditText etPhone;

    private EditText etValidationCode;

    private TextView tvRemainingTime;

    public BindPhoneDialog(BaseActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreateDialog(View dialogView) {
        super.onCreateDialog(dialogView);

        // 初始化
        etPhone = (EditText) findViewById(R.id.et_user_info_phone);
        tvSendValidationCode =
                (TextView) findViewById(R.id.tv_user_info_send_validation_code);
        etValidationCode = (EditText) findViewById(R.id.et_user_info_validation_code);
        tvRemainingTime = (TextView) findViewById(R.id.tv_user_info_remaining_time);

        // 为 发送验证码 添加响应事件
        tvSendValidationCode.setOnClickListener(this);

    }

    @Override
    protected int getResourseLayoutId() {
        return R.layout.user_info_dialog_bind_phone;
    }

    @Override
    protected String getPositiveButtonText() {
        return "确定";
    }

    @Override
    protected String getNegativeButtonText() {
        return "取消";
    }

    @Override
    protected void onClickDialogView(View dialogView, @IdRes int id) {
        super.onClickDialogView(dialogView, id);

        switch (id) {
            case R.id.tv_user_info_send_validation_code:
                onClickSendValidationCode();
                break;
        }

    }

    @Override
    protected void onClickPositiveButton(View dialogView) {
        String phone = etPhone.getText().toString();
        String validationCode = etValidationCode.getText().toString();

        if (phone.isEmpty() || validationCode.isEmpty() || phone.length() != 11) {
            Toast.makeText(AppContext.getContext(),
                    "请输入正确的手机号和验证码", Toast.LENGTH_SHORT).show();
            return ;
        }

        RequestBody body = new JsonRequestBodyBuilder()
                .append("password", AppContext.getOnlineUser().getPassword())
                .append("phone", phone)
                .append("verification_code", validationCode)
                .build();

        UsersService usersService = ApiClient.getUsersService();
        usersService.updateUser(AppContext.getOnlineUser().getId(), body).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                switch (response.code()) {

                    case 204 : {
                        Toast.makeText(AppContext.getContext(),
                                "绑定成功", Toast.LENGTH_SHORT).show();

                        // 更新全局信息
                        AppContext.updateOnlineUser();

                        BindPhoneDialog.this.dismiss();
                    } break;

                    case 401: {
                        BindPhoneDialog.this.dismiss();
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                "验证码错误",
                                getActivity().getResources().getString(R.string.error_message_bind_phone_error));
                    } break;

                    case 404: {
                        BindPhoneDialog.this.dismiss();
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_404,
                                R.string.error_message_bind_phone_error);
                    } break;

                    case 503: {
                        BindPhoneDialog.this.dismiss();
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_503,
                                R.string.error_message_bind_phone_error);
                    } break;

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                BindPhoneDialog.this.dismiss();
                DialogUtil.showAlertDialog(getActivity(),
                        R.string.error_message_network,
                        R.string.error_message_bind_phone_error);
            }

        });
    }

    @Override
    protected void onClickNegativeButton(View dialogView) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        this.dismiss();
    }

    /**
     * 发送验证码 被点击
     *
     */
    private void onClickSendValidationCode() {
        EditText etPhone = (EditText) findViewById(R.id.et_user_info_phone);
        String phone = etPhone.getText().toString().trim();

        if ("".equals(phone) || phone.length() != 11) {
            Toast.makeText(AppContext.getContext(),
                    "请输入正确的手机号",
                    Toast.LENGTH_SHORT).show();
            return ;
        }

        RequestBody body = new JsonRequestBodyBuilder()
                .append("phone", phone)
                .build();

        UsersService usersService = ApiClient.getUsersService();
        usersService.requestVerifyPhone(body).enqueue(new Callback<Map<String, Long>>() {
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
                                timer = null;
                            }

                        };

                        // 开始计时
                        timer.start();
                    } break;

                    case 400: {
                        BindPhoneDialog.this.dismiss();
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                "请输入正确的手机号",
                                getActivity().getResources().getString(R.string.error_message_bind_phone_error));
                    } break;

                    case 409: {
                        if (tvRemainingTime.getVisibility() == View.VISIBLE) {
                            BindPhoneDialog.this.dismiss();
                            DialogUtil.showAlertDialog(
                                    getActivity(),
                                    "请于一段时间后再输入",
                                    getActivity().getResources().getString(R.string.error_message_bind_phone_error));
                        } else {
                            BindPhoneDialog.this.dismiss();
                            DialogUtil.showAlertDialog(
                                    getActivity(),
                                    "该手机号已被绑定",
                                    getActivity().getResources().getString(R.string.error_message_bind_phone_error));
                        }
                    } break;

                    case 503: {
                        BindPhoneDialog.this.dismiss();
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_503,
                                R.string.error_message_bind_phone_error);
                    } break;

                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                BindPhoneDialog.this.dismiss();
                DialogUtil.showAlertDialog(
                        getActivity(),
                        R.string.error_message_network,
                        R.string.error_message_bind_phone_error);
            }
        });
    }

}
