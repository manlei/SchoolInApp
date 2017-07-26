package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.nju.cs.seg.schooledinapp.R;


public class AudioRecordingDialog {

    /**
     * 以下为dialog的初始化控件，包括其中的布局文件
     */

    private Dialog mDialog;

    private RelativeLayout imgBg;

    private TextView tipsTxt;
    private RelativeLayout imgBg2;

    private TextView tipsTxt2;
    private Context mContext;

    public AudioRecordingDialog(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.AudioDialogStyle);
        // 用layoutinflater来引用布局
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_audio_recording, null);
        mDialog.setContentView(view);
        imgBg = (RelativeLayout) view.findViewById(R.id.rl_audio_recording_bg);
        tipsTxt = (TextView) view.findViewById(R.id.tv_audio_recording_txt);
        imgBg2 = (RelativeLayout) view.findViewById(R.id.rl_audio_recording_bg2);
        tipsTxt2 = (TextView) view.findViewById(R.id.tv_audio_recording_txt2);

        mDialog.show();

    }

    /**
     * 设置正在录音时的dialog界面
     */
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            imgBg.setVisibility(View.VISIBLE);
            tipsTxt.setVisibility(View.VISIBLE);
            imgBg2.setVisibility(View.GONE);
            tipsTxt2.setVisibility(View.GONE);

            imgBg.setBackgroundResource(R.drawable.voice_state_1);
            tipsTxt.setText(R.string.tv_audio_recording_up_for_cancel);
        }
    }

    /**
     * 取消界面
     */
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            imgBg.setVisibility(View.GONE);
            tipsTxt.setVisibility(View.GONE);
            imgBg2.setVisibility(View.VISIBLE);
            tipsTxt2.setVisibility(View.VISIBLE);

            imgBg2.setBackgroundResource(R.drawable.voice_cancel);
            tipsTxt2.setText(R.string.tv_audio_recording_want_to_cancle);
            tipsTxt2.setBackgroundColor(mContext.getResources().getColor(R.color.cancel_tips_red));

        }

    }

    // 时间过短
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            imgBg2.setVisibility(View.VISIBLE);
            tipsTxt2.setVisibility(View.VISIBLE);
            imgBg.setVisibility(View.GONE);
            tipsTxt.setVisibility(View.GONE);
            imgBg2.setBackgroundResource(R.drawable.voice_alert);
            tipsTxt2.setText(R.string.tv_audio_recording_time_too_short);
        }

    }

    // 隐藏dialog
    public void dimissDialog() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    public void updateVoiceLevel(int level) {
        if (level > 0 && level < 6) {

        } else {
            level = 5;
        }
        if (mDialog != null && mDialog.isShowing()) {

            //通过level来找到图片的id，也可以用switch来寻址，但是代码可能会比较长
            int resId = mContext.getResources().getIdentifier("voice_state_" + level,
                    "drawable", mContext.getPackageName());
            imgBg.setBackgroundResource(resId);
        }

    }

    public TextView getTipsTxt() {
        return tipsTxt;
    }

    public void setTipsTxt(TextView tipsTxt) {
        this.tipsTxt = tipsTxt;
    }
}
