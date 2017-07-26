package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.PublishAudioActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddEssayTitleDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddEssayTitleForAudioDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;

public class PublishEssayByAudioActivity
       extends PublishAudioActivity
        implements CustomBaseDialog.DialogResultWithRequestCodeListener {

    private static final String TAG = "PublishEssayByAudioActiv";

    public static final String PARAM_STUDIO_NAME = "PARAM_STUDIO_NAME";

    private AddEssayTitleForAudioDialog addEssayTitleForAudioDialog;

    private String studioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studioName = getIntent().getStringExtra(PARAM_STUDIO_NAME);
    }

    @Override
    protected void publish(String recordFilePath) {
        addEssayTitleForAudioDialog = new AddEssayTitleForAudioDialog(
                PublishEssayByAudioActivity.this,
                PublishEssayByAudioActivity.this,
                recordFilePath,
                studioName);
        addEssayTitleForAudioDialog.show();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.manifest_label_essay;
    }

    @Override
    public void onDialogResult(int requestCode, int whichbutton, Bundle bundle) {
        switch (requestCode) {
            //提问的dialog
            case AddEssayTitleDialog.ADDESSAYTITLEDIALOGREQUESTCODE: {
                if (whichbutton == AddEssayTitleDialog.BUTTON_POSITIVE) {
                    int updateResult = bundle.getInt(AddEssayTitleDialog.PUBLISH_RESULT);
                    if (updateResult != AddEssayTitleDialog.PUBLISH_SUCCEEDED) {
                        return;
                    }
                    addEssayTitleForAudioDialog.dismiss();
                    addEssayTitleForAudioDialog = null;
                    finish();
                } else {
                    addEssayTitleForAudioDialog.dismiss();
                    addEssayTitleForAudioDialog = null;
                }
            }
            break;
        }
    }

    /**
     * 启动 PublishEssayByAudioActivity
     *
     * @param context 上下文
     * @param studioName 发表文章的工作室的名字，这是必要的
     */
    public static void startActivityWithParameters(Context context, String studioName){
        Intent intent = new Intent(context, PublishEssayByAudioActivity.class);
        intent.putExtra(PARAM_STUDIO_NAME, studioName);
        context.startActivity(intent);
    }


}
