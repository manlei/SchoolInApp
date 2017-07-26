package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.Manifest;
import android.content.ContentValues;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.dialog.AddHypertextDialog;
import cn.edu.nju.cs.seg.schooledinapp.dialog.base.CustomBaseDialog;
import cn.edu.nju.cs.seg.schooledinapp.service.AnswersService;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cn.edu.nju.cs.seg.schooledinapp.AppContext.getContext;

/**
 * Created by luping on 2017/6/15.
 */

public class PublishAnswerActivity extends BaseActivity implements CustomBaseDialog.DialogResultWithRequestCodeListener{
    private static final String TAG = "PublishAnswerActivity";

    public static final String PARAM_QUESTION_ID = "PARAM_QUESTION_ID";

    @BindView(R.id.tb_publish_answer_toolbar)
    Toolbar toolbarPublishAnswer;

    private int questionId;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private AddHypertextDialog insertHyperTextLink;

    private RichEditor richEditor;

    private TextView releaseAnswer;

    private Uri imageUri;

    private String abPath;

    private String content;

    private static final int GALLERY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_answer);
        ButterKnife.bind(this);

        questionId = getIntent().getIntExtra(PARAM_QUESTION_ID, -1);

        setSupportActionBar(toolbarPublishAnswer);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //获取选择图片权限
        getPermission();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化富文本编辑器的控件并且设置响应的监听事件
     */
    private void initView() {
        releaseAnswer = (TextView) findViewById(R.id.tv_release_answer);
        addListener();
        richEditor = (RichEditor) findViewById(R.id.editor);
        richEditor.setEditorHeight(200);
        richEditor.setEditorFontSize(16);
        richEditor.setEditorFontColor(Color.BLACK);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("在这里写回答...");

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                richEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                richEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHyperTextLink = new AddHypertextDialog(
                        PublishAnswerActivity.this,
                        PublishAnswerActivity.this
                );
                insertHyperTextLink.show();
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.insertTodo();
            }
        });

    }


    /**
     * 启动 PublishAnswerActivity
     *
     * @param activity 启动该页面的上下文
     * @param id       id
     */
    public static void startActivityWithParamForResult(BaseActivity activity, int id, int requestId) {
        Intent intent = new Intent(activity, PublishAnswerActivity.class);
        intent.putExtra(PARAM_QUESTION_ID, id);
        activity.startActivityForResult(intent, requestId);
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
     *
     * @param srcList html中图片的地址，还需要转化成另一种格式
     * @return
     */
    private List<MultipartBody.Part> getFileList(List<String> srcList) {
        List<MultipartBody.Part> fileList = new ArrayList<MultipartBody.Part>();
        for (String src : srcList) {

            File originFile = new File(src);
            Uri uri = getImageContentUri(getContext(), originFile);
            RequestBody filePart = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)), originFile);
            MultipartBody.Part file = MultipartBody.Part.createFormData("photo", originFile.getName(), filePart);
            fileList.add(file);
        }
        return fileList;
    }

    /**
     * 图片的地址转换
     *
     * @param context   上下文
     * @param imageFile 图片的file
     * @return
     */
    private static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
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
     * 打开相册，获取所有种类的图片
     */
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = pictureDirectory.getPath();
        Uri data = Uri.parse(path);
        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, GALLERY);
    }

    /**
     * 在相册选择图片返回activity的入口
     *
     * @param requestCode 请求码，判断是哪个activity启动的
     * @param resultCode  返回码，是否成功
     * @param data        获取的image数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                imageUri = data.getData();
                Log.e("imageUri", imageUri + "");
                abPath = getRealPathFromURI(getBaseContext(), imageUri);
                Log.e("abpath", abPath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        richEditor.insertImage(abPath, "image");
                    }
                });
            }
        }
    }


    /**
     * dialog的返回事件
     * @param requestCode 是哪一个dialog
     * @param whichbutton 是确定或者取消
     * @param bundle 返回参数
     */
    @Override
    public void onDialogResult(int requestCode, int whichbutton, Bundle bundle) {
        switch (requestCode) {
            case AddHypertextDialog.HYPERTEXTDIALOGREQUESTCODE: {
                String text = bundle.getString(AddHypertextDialog.ADDHYPERTEXT);
                String link = bundle.getString(AddHypertextDialog.ADDHYPERLINK);
                richEditor.insertLink(link, text);
                insertHyperTextLink.dismiss();
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

                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return;
            }
        }
    }

    /**
     * 获取图片的绝对地址
     *
     * @param context    上下文
     * @param contentUri uri
     * @return
     */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
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
     * 设置发表答案的事件监听
     */
    private void addListener() {
        releaseAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content = richEditor.getHtml();

                if (content == null || content.isEmpty()) {
                    Toast.makeText(getContext(),
                            "请输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 构建请求体
                RequestBody body = new JsonRequestBodyBuilder()
                        .append("answerer_email_or_phone", AppContext.getOnlineUser().getEmail())
                        .append("answerer_password", AppContext.getOnlineUser().getPassword())
                        .append("question_id", questionId)
                        .append("content", content)
                        .append("type", "text")
                        .build();

                List<String> srcList = getImageSrc(content);
                List<MultipartBody.Part> fileList = getFileList(srcList);

                AnswersService answersService = ApiClient.getAnswersService();
                answersService.addAnswer(body, fileList, null).enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        switch (response.code()) {
                            case 201: {
                                // 成功发布
                                Toast.makeText(AppContext.getContext(),
                                        "发布成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                richEditor.setHtml("");
                            }
                            break;

                            case 401: {
                                DialogUtil.showAlertDialog(PublishAnswerActivity.this, "没有权限！");
                                setResult(RESULT_CANCELED);
                            }
                            break;

                            case 404: {
                                DialogUtil.showAlertDialog(PublishAnswerActivity.this, "没有找到！");
                                setResult(RESULT_CANCELED);
                            }
                            break;

                            case 503: {
                                DialogUtil.showAlertDialog(PublishAnswerActivity.this, "服务器正忙！");
                                setResult(RESULT_CANCELED);
                            }
                            break;
                        }

                        finish();
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        DialogUtil.showAlertDialog(PublishAnswerActivity.this,
                                "请检查网络连接", "发布错误");
                    }
                });
            }
        });
    }

}
