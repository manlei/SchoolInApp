package cn.edu.nju.cs.seg.schooledinapp.activity.base;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.fragment.AudioRecordingFragment;

public abstract class PublishAudioActivity
        extends BaseActivity
        implements AudioRecordingFragment.AudioFinishRecorderListener,
                   View.OnClickListener {

    private static final String TAG = "PublishAudioActivity";

    @BindView(R.id.tb_publish_audio_toolbar)
    Toolbar tbToolbar;

    @BindView(R.id.tv_publish_audio_title)
    TextView tvTitle;

    @BindView(R.id.tv_publish_audio_publish)
    TextView tvPublish;

    private AudioRecordingFragment fragmentMainBody;

    private String recordFilePathToBePublished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_audio);
        ButterKnife.bind(this);

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

    @Override
    public void onFinishRecord(float seconds, String filePath) {
        recordFilePathToBePublished = filePath;
    }

    @Override
    public void onDeleteRecordFile() {
        recordFilePathToBePublished = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_publish_audio_publish:
                onPublishButtonClick();
                break;

        }
    }

    protected abstract void publish(String recordFilePath);

    protected abstract @StringRes int getTitleResourceId();

    /**
     * 发送按钮被点击
     *
     */
    private void onPublishButtonClick() {
        if (recordFilePathToBePublished == null) {
            return ;
        }

        publish(recordFilePathToBePublished);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        // 设置 Toolbar
        setSupportActionBar(tbToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        tvTitle.setText(getTitleResourceId());

        // 设置 主体 fragment
        fragmentMainBody = AudioRecordingFragment.newFragment(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_ask_question_by_audio_fragment_container, fragmentMainBody);
        transaction.commit();

        // 设置按钮
        tvPublish.setOnClickListener(this);
    }

}
