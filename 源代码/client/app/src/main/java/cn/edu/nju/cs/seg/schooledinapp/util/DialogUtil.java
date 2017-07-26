package cn.edu.nju.cs.seg.schooledinapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;

public class DialogUtil {

    /**
     * 显示警告对话框
     *
     * @param context 上下文
     * @param message 信息
     * @param title 标题
     */
    public static void showAlertDialog(Context context, String message, String title) {
        final AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    /**
     * 显示警告对话框
     *
     * @param context 上下文
     * @param message 信息
     * @param title 标题
     * @param listener 监听器
     */
    public static void showAlertDialog(Context context, String message, String title,
                                       final DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("确定", listener);
        alertDialog.show();
    }

    /**
     * 显示警告对话框
     *
     * @param context 上下文
     * @param message 信息
     */
    public static void showAlertDialog(Context context, String message) {
        showAlertDialog(context, message, "警告");
    }

    /**
     * 显示警告对话框
     *
     * @param context 上下文
     * @param messageId 信息id
     * @param titleId 标题id
     */
    public static void showAlertDialog(Context context, int messageId, int titleId) {
        showAlertDialog(
                context,
                AppContext.getContext().getResources().getString(messageId),
                AppContext.getContext().getResources().getString(titleId));
    }

    /**
     * 显示警告对话框
     *
     * @param messageId 信息id
     */
    public static void showAlertDialog(Context context, int messageId) {
        showAlertDialog(
                context,
                AppContext.getContext().getResources().getString(messageId));
    }

    /**
     * 显示进度对话框
     *
     * @param context 上下文
     * @param title 标题
     * @return 进度对话框
     */
    public static final ProgressDialog showProgressDialog(Context context, String title) {
        final ProgressDialog processDialog = new ProgressDialog(context);
        processDialog.setTitle(title);
        processDialog.setCancelable(false);
        processDialog.show();
        return processDialog;
    }

    /**
     * 显示进度对话框
     *
     * @param context 上下文
     * @param titleId 标题id
     * @return 进度对话框
     */
    public static final ProgressDialog showProgressDialog(Context context, int titleId) {
        final ProgressDialog processDialog = new ProgressDialog(context);
        processDialog.setTitle(titleId);
        processDialog.setCancelable(false);
        processDialog.show();
        return processDialog;
    }

    /**
     * 显示进度对话框
     *
     * @param context 上下文
     * @return 进度对话框
     */
    public static final ProgressDialog showProgressDialog(Context context) {
        final ProgressDialog processDialog = new ProgressDialog(context);
        processDialog.setTitle("正在加载...");
        processDialog.setCancelable(false);
        processDialog.show();
        return processDialog;
    }

}
