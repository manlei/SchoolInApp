package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;

/**
 * Created by manlei on 2017/6/16.
 * 说明：
 */

public class AddEssayOrQuestionHypertextDialog extends CustomBaseDialog {

	// Update flag
	public static final int ADDHYPERLINK_SUCCEEDED = -1;

	private static final String TAG = "AddEssayOrQuestionHypertextDialog";

	// Result bundle key
	public static final String ADDHYPERLINK_RESULT = "ADDHYPERLINK_RESULT";

	public static final int HYPERTEXTDIALOGREQUESTCODE = 99;

	public static final String ADDHYPERTEXT = "ADDHYPERTEXT";

	public static final String ADDHYPERLINK = "ADDHYPERLINK";

	private DialogResultWithRequestCodeListener listener;

	private String hyperText;

	private String hyperLink;

	// 错误计时器 1
	private CountDownTimer hyperTextErrorTimer;

	// 错误计时器 2
	private CountDownTimer hyperLinkErrorTimer;

	@Override
	protected int getResourseLayoutId() {
		return R.layout.essay_or_ask_question_dialog_add_hyperlink;
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

		hyperText = ((TextView) findViewById(R.id.et_essay_or_ask_question_hyperlink_text)).getText().toString();
		hyperLink = ((TextView) findViewById(R.id.et_essay_or_ask_question_hyperlink_link)).getText().toString();

		//空值检测
		if (hyperText == null || hyperText.equals("")) {
			onHyperTextInputError();
			return;
		}

		if (hyperLink == null || hyperLink.equals("")) {
			onHyperLinkInputError();
			return;
		}

		//返回结果到activity
		setResult(ADDHYPERLINK_SUCCEEDED);

		//关闭计时器
		if (hyperTextErrorTimer != null) {
			hyperTextErrorTimer = null;
		}

		if (hyperLinkErrorTimer != null) {
			hyperLinkErrorTimer = null;
		}

	}

	public AddEssayOrQuestionHypertextDialog(BaseActivity activity,DialogResultWithRequestCodeListener listener) {
		super(activity);
		this.listener = listener;
	}

	/**
	 * 设置结果,返回text和link
	 *
	 * @param flag 更新标志
	 */
	private void setResult(int flag) {
		Bundle result = new Bundle();
		result.putInt(ADDHYPERLINK_RESULT, flag);
		//传入文本和链接
		result.putString(ADDHYPERTEXT,hyperText);
		result.putString(ADDHYPERLINK,hyperLink);
		listener.onDialogResult(HYPERTEXTDIALOGREQUESTCODE,BUTTON_POSITIVE, result);
	}

	/**
	 * 文本不能为空
	 *
	 */
	private void onHyperTextInputError() {

		if (hyperTextErrorTimer != null) {
			hyperTextErrorTimer.cancel();
			hyperTextErrorTimer = null;
		}

		final TextView tvHyperTextError =
				(TextView) findViewById(R.id.et_essay_or_ask_question_hyperlink_text_error);
		tvHyperTextError.setVisibility(View.VISIBLE);

		hyperTextErrorTimer = new CountDownTimer(3 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				tvHyperTextError.setVisibility(View.GONE);
				hyperTextErrorTimer = null;
			}

		};

		hyperTextErrorTimer.start();
	}

	/**
	 * 链接不能为空
	 *
	 */
	private void onHyperLinkInputError() {
		if (hyperLinkErrorTimer != null) {
			hyperLinkErrorTimer.cancel();
			hyperLinkErrorTimer = null;
		}

		final TextView tvHyperLinkError =
				(TextView) findViewById(R.id.et_essay_or_ask_question_hyperlink_link_error);
		tvHyperLinkError.setVisibility(View.VISIBLE);

		hyperLinkErrorTimer = new CountDownTimer(3 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				tvHyperLinkError.setVisibility(View.GONE);
				hyperLinkErrorTimer = null;
			}

		};

		hyperLinkErrorTimer.start();
	}

}
