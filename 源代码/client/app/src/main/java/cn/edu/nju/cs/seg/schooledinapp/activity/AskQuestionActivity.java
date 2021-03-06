package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddEssayOrQuestionHypertextDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddQuestionTitleDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import jp.wasabeef.richeditor.RichEditor;

public class AskQuestionActivity extends BaseActivity implements CustomBaseDialog.DialogResultWithRequestCodeListener {

	private static final String TAG = "AskQuestionActivity";

	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

	private static final String QUESTIONTITLE = "QUESTIONTITLE";

	private static final String QUESTIONSTDUIO = "QUESTIONSTUDIO";

	private static final int GALLERY = 20;

	private static final int TITLE = 30;

	private RichEditor richEditor;

	private Button showHtmlText;

	private Button chooseImage;

	private Button getSrc;

	private Uri imageUri;

	private String abPath;

	private Toolbar toolbarAskQuestion;

	private AddEssayOrQuestionHypertextDialog insertHyperTextLink;

	//根据内容确定显示是发布还是标题

	private String questionTitle;

	private String questionDirectedTo;

	private String questionContent;

	private AddQuestionTitleDialog addQuestionTitleDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);

		toolbarAskQuestion = (Toolbar) findViewById(R.id.toolbar_ask_question);
		setSupportActionBar(toolbarAskQuestion);
		if(getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		//获取选择图片权限
		getPermission();

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ask_question_essay_menu,menu);
		return true;
	}

	/**
	 * 设置菜单按钮的监听事件，提问或者返回
	 *
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.menu_publish:

				String content;
				if (richEditor.getHtml() == null) {
					content = "";
				}
				else
					content = richEditor.getHtml();

				addQuestionTitleDialog = new AddQuestionTitleDialog(
						AskQuestionActivity.this,
						AskQuestionActivity.this,
						content);
				addQuestionTitleDialog.show();

				break;

			case android.R.id.home:
				onBackPressed();
				break;

			default:
				break;
		}
		return true;
	}

	/**
	 * 在相册选择图片返回activity的入口
	 * @param requestCode 请求码，判断是哪个activity启动的
	 * @param resultCode  返回码，是否成功
	 * @param data 获取的image数据
	 */
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if (resultCode == RESULT_OK){
			if(requestCode == GALLERY){
				imageUri = data.getData();
				abPath = getRealPathFromURI(getBaseContext(),imageUri);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						richEditor.insertImage(abPath,"image");
					}
				});
			}

		}
	}

	/**
	 * 提问成功返回的地方
	 * @param whichbutton
	 * @param bundle
	 */
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
					addQuestionTitleDialog.dismiss();
					addQuestionTitleDialog = null;
					finish();
				} else {
					addQuestionTitleDialog.dismiss();
					addQuestionTitleDialog = null;
				}
			}
			break;

			case AddEssayOrQuestionHypertextDialog.HYPERTEXTDIALOGREQUESTCODE: {
				String text = bundle.getString(AddEssayOrQuestionHypertextDialog.ADDHYPERTEXT);
				String link = bundle.getString(AddEssayOrQuestionHypertextDialog.ADDHYPERLINK);
				richEditor.insertLink(link, text);
				insertHyperTextLink.dismiss();
			}
		}
	}

	/**
	 * 启动页面
	 * @param context 上下文
	 */
	public static void startAskQuestionActivity(Context context) {
		Intent intent = new Intent(context,AskQuestionActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 获取图片的绝对地址
	 * @param context 上下文
	 * @param contentUri uri
	 * @return
	 */
	private String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * 获取打开Gallery的权限
	 */
	private void getPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {

				// Should we show an explanation?
				if (shouldShowRequestPermissionRationale(
						Manifest.permission.READ_EXTERNAL_STORAGE)) {
					// Explain to the user why we need to read the contacts
				}

				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

				// MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
				// app-defined int constant that should be quite unique

				return;
			}
		}
	}

	/**
	 * 初始化富文本编辑器的控件并且设置响应的监听事件
	 */
	private void initView() {
		richEditor = (RichEditor) findViewById(R.id.editor);
		richEditor.setEditorHeight(200);
		richEditor.setEditorFontSize(16);
		richEditor.setEditorFontColor(Color.BLACK);
		richEditor.setPadding(10, 10, 10, 10);
		richEditor.setPlaceholder("在这里写问题");

		findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.undo();
			}
		});

		findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.redo();
			}
		});

		findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setBold();
			}
		});

		findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setItalic();
			}
		});

		findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setSubscript();
			}
		});

		findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setSuperscript();
			}
		});

		findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setStrikeThrough();
			}
		});

		findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setUnderline();
			}
		});

		findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(1);
			}
		});

		findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(2);
			}
		});

		findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(3);
			}
		});

		findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(4);
			}
		});

		findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(5);
			}
		});

		findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setHeading(6);
			}
		});

		findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
			private boolean isChanged;

			@Override public void onClick(View v) {
				richEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
				isChanged = !isChanged;
			}
		});

		findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
			private boolean isChanged;

			@Override public void onClick(View v) {
				richEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
				isChanged = !isChanged;
			}
		});

		findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setIndent();
			}
		});

		findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setOutdent();
			}
		});

		findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setAlignLeft();
			}
		});

		findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setAlignCenter();
			}
		});

		findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setAlignRight();
			}
		});

		findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setBlockquote();
			}
		});

		findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setBullets();
			}
		});

		findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.setNumbers();
			}
		});

		findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				getImage();
				//richEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
				//		"dachshund");
			}
		});

		findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				insertHyperTextLink = new AddEssayOrQuestionHypertextDialog(
						AskQuestionActivity.this,
						AskQuestionActivity.this
				);
				insertHyperTextLink.show();

				//richEditor.insertLink("https://github.com/wasabeef", "wasabeef");
			}
		});
		findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				richEditor.insertTodo();
			}
		});

	}

	/**
	 * 打开相册，获取所有种类的图片
	 */
	private void getImage(){
		Intent intent = new Intent(Intent.ACTION_PICK);
		File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		String path = pictureDirectory.getPath();
		Uri data = Uri.parse(path);
		intent.setDataAndType(data,"image/*");
		startActivityForResult(intent,GALLERY);
	}
}
