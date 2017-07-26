package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;


public class AddMembersDialog extends CustomBaseDialog {

    private DialogResultListener listener;

    EditText etMembers;

    public static final String ADD_MEMBERS_RESULT = "ADD_MEMBERS_RESULT";

    public AddMembersDialog(BaseActivity activity, DialogResultListener listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected void onCreateDialog(View dialogView) {
        super.onCreateDialog(dialogView);

        etMembers = (EditText) findViewById(R.id.et_studio_members_add_members);

    }

    @Override
    protected int getResourseLayoutId() {
        return R.layout.studio_members_dialog_add_members;
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
        if (etMembers.getText() == null) {
            return ;
        }

        String emailsOrPhones = etMembers.getText().toString();

        if (emailsOrPhones == null || emailsOrPhones.isEmpty()) {
            this.listener.onDialogResult(BUTTON_POSITIVE, new Bundle());
        } else {
            Bundle result = new Bundle();
            result.putStringArray(ADD_MEMBERS_RESULT, emailsOrPhones.split(";"));
            this.listener.onDialogResult(BUTTON_POSITIVE, result);
        }

        super.onClickPositiveButton(dialogView);
    }

    @Override
    protected void onClickNegativeButton(View dialogView) {
        this.listener.onDialogResult(BUTTON_NEGATIVE, null);
        super.onClickNegativeButton(dialogView);
    }
}
