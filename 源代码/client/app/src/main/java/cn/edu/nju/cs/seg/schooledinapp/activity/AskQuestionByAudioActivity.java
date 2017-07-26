package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.PublishAudioActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddQuestionTitleDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddQuestionTitleForAudioDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;

public class AskQuestionByAudioActivity extends PublishAudioActivity
        implements CustomBaseDialog.DialogResultWithRequestCodeListener {

    private static final String TAG = "AskQuestionByAudioActiv";

    private AddQuestionTitleForAudioDialog addQuestionTitleForAudioDialog;

    @Override
    protected void publish(String recordFilePath) {
        addQuestionTitleForAudioDialog = new AddQuestionTitleForAudioDialog(
                AskQuestionByAudioActivity.this,
                AskQuestionByAudioActivity.this,
                recordFilePath);
        addQuestionTitleForAudioDialog.show();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.manifest_label_question;
    }

    @Override
    public void onDialogResult(int requestCode, int whichbutton, Bundle bundle) {
        switch (requestCode) {
            //提问的dialog
            case AddQuestionTitleDialog.ADDQUESTIONTITLEREDIALOGQUESTCODE: {
                if (whichbutton == AddQuestionTitleDialog.BUTTON_POSITIVE) {
                    int updateResult = bundle.getInt(AddQuestionTitleDialog.PUBLISH_RESULT);
                    if (updateResult != AddQuestionTitleDialog.PUBLISH_SUCCEEDED) {
                        return;
                    }
                    addQuestionTitleForAudioDialog.dismiss();
                    addQuestionTitleForAudioDialog = null;
                    finish();
                } else {
                    addQuestionTitleForAudioDialog.dismiss();
                    addQuestionTitleForAudioDialog = null;
                }
            }
            break;
        }
    }

    /**
     * 启动 AskQuestionByAudioActivity
     *
     * @param context
     */
    public static void startActivityWithParameters(Context context) {
        Intent intent = new Intent(context, AskQuestionByAudioActivity.class);
        context.startActivity(intent);
    }

}
