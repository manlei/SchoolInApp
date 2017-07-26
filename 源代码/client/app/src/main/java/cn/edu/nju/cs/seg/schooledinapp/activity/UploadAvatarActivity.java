package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.DialogUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAvatarActivity extends BaseActivity
        implements View.OnClickListener {

    private static final String TAG = "UploadAvatarActivity";

    public static final String PARAM_AVATAR_URL = "PARAM_AVATAR_URL";

    private static final int REQUEST_GALLARY_CODE = 1010;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @BindView(R.id.iv_upload_avatar_bg)
    ImageView ivBg;

    @BindView(R.id.civ_upload_avatar_avatar)
    ImageView ivAvatar;

    @BindView(R.id.iv_upload_avatar_back)
    ImageView ivBack;

    @BindView(R.id.btn_upload_avatar_change)
    Button btnChangeAvatar;

    @BindView(R.id.btn_upload_avatar_upload)
    Button btnUploadAvatar;

    // 外部传入的 Url
    private String avatarUrl;

    // 修改后的 Uri
    private Uri wantedAvatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);
        ButterKnife.bind(this);

        avatarUrl = getIntent().getStringExtra(PARAM_AVATAR_URL);

        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_upload_avatar_back:
                finish();
                break;

            case R.id.btn_upload_avatar_change:
                doChangeAvatar();
                break;

            case R.id.btn_upload_avatar_upload:
                doUploadAvatar();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLARY_CODE && resultCode == RESULT_OK && data != null) {
            wantedAvatarUri = data.getData();
            Glide.with(UploadAvatarActivity.this)
                    .load(wantedAvatarUri)
                    .into(ivAvatar);
            Glide.with(UploadAvatarActivity.this)
                    .load(wantedAvatarUri)
                    .into(ivBg);
        }

    }

    /**
     * 启动 UploadAvatarActivity
     *
     * @param context 启动上下文
     * @param avatarUrl 头像路径
     */
    public static void startActivityWithParameters(
            Context context, String avatarUrl) {
        Intent intent = new Intent(context, UploadAvatarActivity.class);
        intent.putExtra(PARAM_AVATAR_URL, avatarUrl);
        context.startActivity(intent);
    }

    /**
     * 修改头像
     *
     */
    private void doChangeAvatar() {

        // 打开相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        UploadAvatarActivity.this.startActivityForResult(
                intent,
                REQUEST_GALLARY_CODE);

    }

    /**
     * 上传头像
     *
     */
    private void doUploadAvatar() {

        if (wantedAvatarUri == null) {
            return ;
        }

        final ProgressDialog progressDialog = DialogUtil.showProgressDialog(
                UploadAvatarActivity.this,
                "正在上传中...");

        // 构建请求体
        RequestBody body = new JsonRequestBodyBuilder()
                .append("password", AppContext.getOnlineUser().getPassword())
                .build();

        LogUtil.e(TAG, "RequestBody ... done");

        // 构建 Multipart
        File file = new File(getRealPathFromUri(wantedAvatarUri, UploadAvatarActivity.this));
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), image);

        LogUtil.e(TAG, "MultipartBody ... done");

        // 发送请求
        ApiClient.getUsersService().uploadAvatar(
                AppContext.getOnlineUser().getId(), body, part).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                progressDialog.dismiss();

                LogUtil.e(TAG, "Upload ... response");

                switch (response.code()) {
                    case 204: {
                        DialogUtil.showAlertDialog(UploadAvatarActivity.this, "上传成功", "您已成功修改头像");
                        // 更新全局信息
                        AppContext.updateOnlineUser();
                    } break;

                    case 409: {
                        DialogUtil.showAlertDialog(UploadAvatarActivity.this, "上传失败", "密码错误");
                    } break;

                    case 404: {
                        DialogUtil.showAlertDialog(UploadAvatarActivity.this, "上传失败", "用户未找到");
                    } break;

                    case 503: {
                        DialogUtil.showAlertDialog(UploadAvatarActivity.this, "上传失败", "服务器正忙");
                    } break;
                }

            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                LogUtil.e(TAG, "Upload ... failure: " + t.getLocalizedMessage());

                progressDialog.dismiss();
                DialogUtil.showAlertDialog(UploadAvatarActivity.this, "上传失败", "很抱歉修改失败");
            }
        });

    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        //获取选择图片权限
        getPickPermission();

        Glide.with(UploadAvatarActivity.this)
                .load(avatarUrl)
                .into(ivAvatar);
        Glide.with(UploadAvatarActivity.this)
                .load(avatarUrl)
                .into(ivBg);
        ivBack.setOnClickListener(this);
        btnUploadAvatar.setOnClickListener(this);
        btnChangeAvatar.setOnClickListener(this);
    }

    /**
     * 获取打开 Gallery 的权限
     */
    private void getPickPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }
    }

    /**
     * 从 Uri 获取 Path
     *
     * @param uri
     * @param activity
     * @return
     */
    private String getRealPathFromUri(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
