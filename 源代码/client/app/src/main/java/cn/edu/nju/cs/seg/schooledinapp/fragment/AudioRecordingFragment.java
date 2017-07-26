package cn.edu.nju.cs.seg.schooledinapp.fragment;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseFragment;
import cn.edu.nju.cs.seg.schooledinapp.util.FileUtils;
import cn.edu.nju.cs.seg.schooledinapp.widgets.AudioRecordingButton;

import cn.edu.nju.cs.seg.schooledinapp.util.PermissionUtil;
import cn.edu.nju.cs.seg.schooledinapp.util.MediaManager;


/**
 * 录音 Fragment
 *
 * Reference: https://github.com/scsfwgy/WeixinRecord
 *
 */
public class AudioRecordingFragment
        extends BaseFragment
        implements View.OnClickListener {

    private static final String TAG = "AudioRecordingFragment";

    public interface AudioFinishRecorderListener {
        void onFinishRecord(float seconds, String filePath);
        void onDeleteRecordFile();
    }

    @BindView(R.id.btn_audio_recording_play)
    Button btnPlay;

    @BindView(R.id.btn_audio_recording_delete)
    Button btnDelete;

    @BindView(R.id.tv_audio_recording_state)
    TextView tvState;

    @BindView(R.id.arbtn_audio_recording_record)
    AudioRecordingButton arbtnRecord;

    // 权限申请
    private PermissionUtil permissionUtil;

    private boolean isPlaying = false;

    private String recordFilePath = null;

    private AudioFinishRecorderListener audioFinishRecorderListener;

    public AudioFinishRecorderListener getAudioFinishRecorderListener() {
        return audioFinishRecorderListener;
    }

    public void setAudioFinishRecorderListener(
            AudioFinishRecorderListener audioFinishRecorderListener) {
        this.audioFinishRecorderListener = audioFinishRecorderListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_audio_recording, container, false);
        ButterKnife.bind(this, v);

        initView();

        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_audio_recording_play:
                onPlayButtonClick();
                break;

            case R.id.btn_audio_recording_delete:
                onDeleteButtonClick();
                break;
        }
    }

    /**
     * 创建一个 AudioRecordingFragment
     *
     * @return
     */
    public static AudioRecordingFragment newFragment(AudioFinishRecorderListener listener) {
        Bundle args = new Bundle();

        AudioRecordingFragment fragment = new AudioRecordingFragment();
        fragment.setArguments(args);
        fragment.setAudioFinishRecorderListener(listener);

        return fragment;
    }

    /**
     * 点击删除
     *
     */
    private void onDeleteButtonClick() {
        if (recordFilePath == null || isPlaying) {
            return ;
        }

        tvState.setText(R.string.tv_audio_recording_is_deleting);
        FileUtils.deleteRecordFile(recordFilePath);
        recordFilePath = null;
        this.audioFinishRecorderListener.onDeleteRecordFile();

        tvState.setText(R.string.tv_audio_recording_state_to_generate);
        btnDelete.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
        arbtnRecord.setVisibility(View.VISIBLE);
    }

    /**
     * 点击播放
     *
     */
    private void onPlayButtonClick() {
        if (recordFilePath == null) {
            return ;
        }

        if (isPlaying) {
            MediaManager.release();
            isPlaying = false;
            tvState.setText(R.string.tv_audio_recording_state_generated);
            return ;
        }

        isPlaying = true;
        tvState.setText(R.string.tv_audio_recording_is_playing);

        // 播放前重置。
        MediaManager.release();

        // 开始实质播放
        MediaManager.playLoacalAudio(recordFilePath,
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //播放完毕
                        isPlaying = false;
                        tvState.setText(R.string.tv_audio_recording_state_generated);
                    }
                });
    }

    private void initAudioRecordingButton() {
        arbtnRecord.setHasRecordPromission(false);

        // 授权处理
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions("请授予[录音]，[读写]权限，否则无法录音",
                new PermissionUtil.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        arbtnRecord.setHasRecordPromission(true);
                        arbtnRecord.setAudioFinishRecorderListener(new AudioRecordingButton.AudioFinishRecorderListener() {
                            @Override
                            public void onFinished(float seconds, String filePath) {
                                recordFilePath = filePath;
                                arbtnRecord.setVisibility(View.GONE);
                                btnDelete.setVisibility(View.VISIBLE);
                                btnPlay.setVisibility(View.VISIBLE);
                                tvState.setText(R.string.tv_audio_recording_state_generated);
                                AudioRecordingFragment.this.audioFinishRecorderListener.onFinishRecord(seconds, filePath);
                            }
                        });
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        arbtnRecord.setHasRecordPromission(false);
                        Toast.makeText(AppContext.getContext(),
                                "请授权，否则无法录音", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initOtherButtons() {
        btnPlay.setVisibility(View.GONE);
        btnPlay.setOnClickListener(this);

        btnDelete.setVisibility(View.GONE);
        btnDelete.setOnClickListener(this);
    }

    /**
     * 初始化 View
     *
     */
    private void initView() {
        tvState.setText(R.string.tv_audio_recording_state_to_generate);
        initAudioRecordingButton();
        initOtherButtons();
    }

}
