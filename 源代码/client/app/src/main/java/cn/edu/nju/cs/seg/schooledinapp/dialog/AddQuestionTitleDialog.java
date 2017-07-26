package cn.edu.nju.cs.seg.schooledinapp.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manlei on 2017/6/10.
 * 说明：
 */

public class AddQuestionTitleDialog extends CustomBaseDialog {

	private static final String TAG = "AddEssayTitleDialog";

	// Result bundle key
	public static final String PUBLISH_RESULT = "PUBLISH_RESULT";

	public static final int ADDQUESTIONTITLEREDIALOGQUESTCODE = 100;

	// Update flag
	public static final int PUBLISH_SUCCEEDED = -1;

	public static final int PUBLISH_FAILED = -2;

	private DialogResultWithRequestCodeListener listener;

	private String questionContent;

	public String questionTitle;

	public String questionDirectedTo;

	// 错误计时器 1
	private CountDownTimer titleErrorTimer;

	// 错误计时器 2
	private CountDownTimer studioErrorTimer;

	public AddQuestionTitleDialog(BaseActivity activity, DialogResultWithRequestCodeListener listener, String content) {
		super(activity);
		this.listener = listener;
		questionContent = content;
	}

	@Override
	protected int getResourseLayoutId() {
		return R.layout.ask_question_dialog_add_title;
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

		questionTitle = ((TextView) findViewById(R.id.et_ask_question_title)).getText().toString();
		questionDirectedTo = ((TextView) findViewById(R.id.et_ask_question_studio)).getText().toString();

		//空值检测
		if (questionTitle == null || questionTitle.equals("")) {
			onTitleInputError();
			return;
		}

		if (questionDirectedTo == null || questionDirectedTo.equals("")) {
			onQuestionStudioError();
			return;
		}

		 //发送请求，拼装相应的请求体
		requestPublishQuestion();

		 //关闭计时器
		if (titleErrorTimer != null) {
			titleErrorTimer = null;
		}

		if (studioErrorTimer != null) {
			studioErrorTimer = null;
		}

	}

	/**
	 * 设置结果
	 *
	 * @param flag 更新标志
	 */
	public void setResult(int flag) {
		Bundle result = new Bundle();
		result.putInt(PUBLISH_RESULT, flag);
		listener.onDialogResult(ADDQUESTIONTITLEREDIALOGQUESTCODE,BUTTON_POSITIVE, result);
	}

	/**
	 * 发送问题请求
	 *
	 */
	public void requestPublishQuestion() {

		//body
		RequestBody body = new JsonRequestBodyBuilder()
				.append("asker_email_or_phone", AppContext.getOnlineUser().getEmail())
				.append("asker_password", AppContext.getOnlineUser().getPassword())
				.append("question_title",questionTitle)
				.append("question_content",questionContent)
				.append("directed_to",questionDirectedTo)
				.append("type", "text")
				.build();

		//photolist
		List<String> srcList = getImageSrc(questionContent);
		List<MultipartBody.Part> fileList = getFileList(srcList);

		ApiClient.getUsersService()
				.addQuestion(body,fileList,null)
				.enqueue(new Callback<Map<String, Object>>() {
					@Override
					public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
						switch(response.code()){
							case 201:{
								Toast.makeText(getContext(),"提问成功",Toast.LENGTH_LONG).show();
								AppContext.updateOnlineUser();
								setResult(PUBLISH_SUCCEEDED);
							}
							break;
							case 401:{
								Toast.makeText(getContext(),"没有权限",Toast.LENGTH_LONG).show();
								setResult(PUBLISH_FAILED);
							}
							break;
							case 404:{
								Toast.makeText(getContext(),"服务器不存在",Toast.LENGTH_LONG).show();
								setResult(PUBLISH_FAILED);
							}
							break;
							case 503:{
								Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();
								setResult(PUBLISH_FAILED);
							}
							break;
							default:{
								Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();
								setResult(PUBLISH_FAILED);
							}
							break;
						}
					}

					@Override
					public void onFailure(Call<Map<String, Object>> call, Throwable t) {
						Toast.makeText(
								getContext(),
								getActivity().getResources().getString(R.string.error_message_network),
								Toast.LENGTH_LONG).show();
						setResult(PUBLISH_FAILED);
					}
				});
	}

	/**
	 * 获取最终富文本中所有图片的src
	 */
	List<String> getImageSrc(String html){
		List<String> imgList = new ArrayList<String>();
		String pattern = "<img src=(.*?)[^>]*?>";
		Matcher matcher = Pattern.compile(pattern).matcher(html);
		while (matcher.find()) {
			String temp = matcher.group();
			String[] src = temp.split("\"");
			if (src.length == 5) {
				Log.e("imgSrc:",src[1]);
				imgList.add(src[1]);
			}
		}
		return imgList;
	}

	/**
	 * 获取图片的multipart list
	 * @param srcList html中图片的地址，还需要转化成另一种格式
	 * @return
	 */
	private List<MultipartBody.Part> getFileList(List<String> srcList) {
		List<MultipartBody.Part> fileList = new ArrayList<MultipartBody.Part>();
		for (String src:srcList) {

			File originFile = new File(src);
			Uri uri = getImageContentUri(getContext(),originFile);
			RequestBody filePart = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)),originFile);
			MultipartBody.Part file = MultipartBody.Part.createFormData("photo",originFile.getName(),filePart);
			fileList.add(file);
		}
		return fileList;
	}

	/**
	 * 图片的地址转换
	 * @param context 上下文
	 * @param imageFile 图片的file
	 * @return
	 */
	private static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * 标题为空
	 *
	 */
	private void onTitleInputError() {
		if (titleErrorTimer != null) {
			titleErrorTimer.cancel();
			titleErrorTimer = null;
		}

		final TextView tvQuestionTitleError =
				(TextView) findViewById(R.id.et_ask_question_error_title);
		tvQuestionTitleError.setVisibility(View.VISIBLE);

		titleErrorTimer = new CountDownTimer(3 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				tvQuestionTitleError.setVisibility(View.GONE);
				titleErrorTimer = null;
			}

		};

		titleErrorTimer.start();
	}

	/**
	 * 工作室为空
	 *
	 */
	private void onQuestionStudioError() {
		if (studioErrorTimer != null) {
			studioErrorTimer.cancel();
			studioErrorTimer = null;
		}

		final TextView tvQuestionStudioError =
				(TextView) findViewById(R.id.et_user_info_error_studio);
		tvQuestionStudioError.setVisibility(View.VISIBLE);

		studioErrorTimer = new CountDownTimer(3 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				tvQuestionStudioError.setVisibility(View.GONE);
				studioErrorTimer = null;
			}

		};

		studioErrorTimer.start();
	}

}
