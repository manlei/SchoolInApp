package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.model.User;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordDialog extends CustomBaseDialog {

    private static final String TAG = "ChangePasswordDialog";

    // Result bundle key
    public static final String UPDATE_RESULT = "UPDATE_RESULT";

    // Update flag
    public static final int UPDATE_SUCCEEDED = -1;

    public static final int UPDATE_FAILED = -2;

    // 监听器
    private DialogResultListener listener;

    // 错误计时器 1
    private CountDownTimer originPasswordErrorTimer;

    // 错误计时器 2
    private CountDownTimer newPasswordErrorTimer;

    public UpdatePasswordDialog(BaseActivity activity, DialogResultListener listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected int getResourseLayoutId() {
        return R.layout.user_info_dialog_update_password;
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
    protected void onClickPositiveButton(View dialogView) {
        User onlineUser = AppContext.getOnlineUser();

        EditText tvOriginPassword = (EditText) findViewById(R.id.et_user_info_origin_password);
        EditText tvNewPassword = (EditText) findViewById(R.id.et_user_info_new_password);
        EditText tvRenewPassword = (EditText) findViewById(R.id.et_user_info_renew_password);

        String originPassword = tvOriginPassword.getText().toString();
        String newPassword = tvNewPassword.getText().toString();
        String renewPassword = tvRenewPassword.getText().toString();

        if (! onlineUser.getPassword().equals(originPassword)) {
            onOriginPasswordInputError();
            return;
        }

        if (newPassword.isEmpty() || !newPassword.equals(renewPassword)) {
            onNewPasswordInputError();
            return;
        }

        // 发送请求
        requestUpdatePassword(originPassword, newPassword);

        // 关闭计时器
        if (originPasswordErrorTimer != null) {
            originPasswordErrorTimer = null;
        }

        if (newPasswordErrorTimer != null) {
            newPasswordErrorTimer = null;
        }
    }

    /**
     * 设置结果
     *
     * @param flag 更新标志
     */
    private void setResult(int flag) {
        Bundle result = new Bundle();
        result.putInt(UPDATE_RESULT, flag);
        listener.onDialogResult(BUTTON_POSITIVE, result);
    }

    /**
     * 发送修改密码请求
     *
     * @param originPassword 原密码
     * @param newPassword 新密码
     */
    private void requestUpdatePassword(String originPassword, final String newPassword) {

        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("password", originPassword)
                .append("new_password", newPassword)
                .build();

        UsersService usersService = ApiClient.getUsersService();
        usersService.updateUser(AppContext.getOnlineUser().getId(), body).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                UpdatePasswordDialog.this.dismiss();

                switch (response.code()) {
                    case 204: {
                        setResult(UPDATE_SUCCEEDED);
                    } break;

                    case 401: {
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_401,
                                R.string.error_message_edit_user_error);
                        setResult(UPDATE_FAILED);
                    } break;

                    case 404: {
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_404,
                                R.string.error_message_edit_user_error);
                        setResult(UPDATE_FAILED);
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(
                                getActivity(),
                                R.string.error_message_503,
                                R.string.error_message_edit_user_error);
                        setResult(UPDATE_FAILED);
                    } break;
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                UpdatePasswordDialog.this.dismiss();
                DialogUtil.showAlertDialog(getActivity(),
                        "请检查网络连接");
                setResult(UPDATE_FAILED);
            }

        });

    }

    /**
     * 原密码输入错误
     *
     */
    private void onOriginPasswordInputError() {
        if (originPasswordErrorTimer != null) {
            originPasswordErrorTimer.cancel();
            originPasswordErrorTimer = null;
        }

        final TextView tvOriginPasswordError =
                (TextView) findViewById(R.id.et_user_info_error_origin_password);
        tvOriginPasswordError.setVisibility(View.VISIBLE);

        originPasswordErrorTimer = new CountDownTimer(3 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                tvOriginPasswordError.setVisibility(View.GONE);
                originPasswordErrorTimer = null;
            }

        };

        originPasswordErrorTimer.start();
    }

    /**
     * 两次密码输入不一致
     *
     */
    private void onNewPasswordInputError() {
        if (newPasswordErrorTimer != null) {
            newPasswordErrorTimer.cancel();
            newPasswordErrorTimer = null;
        }

        final TextView tvNewPasswordError =
                (TextView) findViewById(R.id.tv_user_info_error_new_password);
        tvNewPasswordError.setVisibility(View.VISIBLE);

        newPasswordErrorTimer = new CountDownTimer(3 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                tvNewPasswordError.setVisibility(View.GONE);
                newPasswordErrorTimer = null;
            }

        };

        newPasswordErrorTimer.start();
    }

}
