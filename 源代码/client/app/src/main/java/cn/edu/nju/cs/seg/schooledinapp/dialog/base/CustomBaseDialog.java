package cn.edu.nju.cs.seg.schooledinapp.dialog.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;

public abstract class CustomBaseDialog extends AlertDialog
        implements View.OnClickListener {

    public interface DialogResultListener {
        void onDialogResult(int whichbutton, Bundle bundle);
    }

    public interface DialogResultWithRequestCodeListener {
        void onDialogResult(int requestCode, int whichbutton, Bundle bundle);
    }

    private static final String TAG = "CustomBaseDialog";

    // 整个 View
    private View overallView;

    // 主体 View
    private View dialogView;

    // 父 activity
    private BaseActivity activity;

    //请求码，判断是谁的dialog
    private int responseCode;

    public CustomBaseDialog(BaseActivity activity) {
        super(activity);
        this.activity = activity;
        createDialog();
    }

    public CustomBaseDialog(int requestCode, BaseActivity activity) {
        super(activity);
        this.activity = activity;
        this.responseCode = requestCode;
        createDialog();
    }

    /**
     * 获取 父 Activity
     *
     * @return 父 Activity
     */
    public BaseActivity getActivity() {
        return activity;
    }

    /**
     * 寻找 View
     *
     * @param id 资源 ID
     * @return View
     */
    public View findViewById(@IdRes int id) {
        return dialogView.findViewById(id);
    }

    @Override
    public void show() {
        super.show();

        // 重新绑定回调函数
        this.getButton(BUTTON_POSITIVE).setOnClickListener(this);
        this.getButton(BUTTON_NEGATIVE).setOnClickListener(this);
    }

    /**
     * 生命周期函数 在创建结束调用
     *
     * @param dialogView 本身的 dialogView
     */
    protected void onCreateDialog(View dialogView) { }

    /**
     * 获取 Layout 资源 ID
     *
     * @return 资源 ID
     */
    protected abstract @LayoutRes int getResourseLayoutId();

    /**
     * PostiveButton 文本
     *
     * @return
     */
    protected abstract String getPositiveButtonText();

    /**
     * NegativeButton 文本
     *
     * @return
     */
    protected abstract String getNegativeButtonText();

    /**
     * PositiveButton 被点击
     *
     * @param dialogView
     */
    protected void onClickPositiveButton(View dialogView) {
        this.dismiss();
    }

    /**
     * NegativeButton 被点击
     *
     * @param dialogView
     */
    protected void onClickNegativeButton(View dialogView) {
        this.dismiss();
    }

    /**
     * DialogView 被点击
     *
     * @param dialogView
     * @param id View ID
     */
    protected void onClickDialogView(View dialogView, @IdRes int id) { }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        int idPositive = this.getButton(BUTTON_POSITIVE).getId();
        int idNegative = this.getButton(BUTTON_NEGATIVE).getId();

        if (id == idPositive) {
            onClickPositiveButton(dialogView);
        } else if (id == idNegative) {
            onClickNegativeButton(dialogView);
        } else {
            onClickDialogView(dialogView, id);
        }

    }

    /**
     * 初始化对话框
     *
     */
    private void createDialog() {
        // 获取填充句柄
        LayoutInflater inflater = activity.getLayoutInflater();

        // 填充
        overallView = inflater.inflate(R.layout.dialog_custom_base, null);
        dialogView = inflater.inflate(getResourseLayoutId(), null);

        // 设置 logo header
        TextView tvSchooledInSchooled =
                (TextView) overallView.findViewById(R.id.tv_dialog_custom_base_schooled);
        TextView tvSchooledInIn =
                (TextView) overallView.findViewById(R.id.tv_dialog_custom_base_in);

        Typeface font = Typeface.createFromAsset(
                AppContext.getContext().getAssets(),
                AppContext.getContext().getResources().getString(R.string.font_brush_script_std));

        tvSchooledInSchooled.setTypeface(font);
        tvSchooledInIn.setTypeface(font);

        // 设置主体
        FrameLayout flMainBody =
                (FrameLayout) overallView.findViewById(R.id.fl_dialog_custom_base_main_body);
        flMainBody.addView(dialogView);

        // 设置给 Dialog
        this.setView(overallView);

        // 设置回调函数，阻止默认关闭对话框的事件
        this.setButton(BUTTON_POSITIVE, getPositiveButtonText(), (OnClickListener) null);
        this.setButton(BUTTON_NEGATIVE, getNegativeButtonText(), (OnClickListener) null);

        // 设置不可取消
        this.setCancelable(false);

        // 生命周期钩子
        onCreateDialog(dialogView);
    }

}