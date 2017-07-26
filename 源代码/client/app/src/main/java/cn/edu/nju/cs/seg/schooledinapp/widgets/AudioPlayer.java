package cn.edu.nju.cs.seg.schooledinapp.widgets;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.IndexService;
import cn.edu.nju.cs.seg.schooledinapp.util.FileUtils;
import cn.edu.nju.cs.seg.schooledinapp.util.MediaManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioPlayer extends ImageView {

    private static final String TAG = "AudioPlayerButton";

    private static final int NOT_DOWNLOADED = 1;
    private static final int IS_DOWNLOADING = 2;
    private static final int HAS_DOWNLOADED = 3;
    private static final int IS_PLAYING = 4;

    private String audioUrl;

    private String audioLocalFilePath;

    private int state;

    public AudioPlayer(Context context) {
        this(context, null);
    }

    public AudioPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAudioUrl(@NonNull String audioUrl) {
        this.audioUrl = audioUrl;
        this.state = NOT_DOWNLOADED;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPlayer.this.onClick();
            }
        });
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    private void onClick() {
        if (audioUrl == null) {
            return ;
        }

        switch (this.state) {

            case NOT_DOWNLOADED: {
                downloadAndPlay();
            } break;

            case IS_DOWNLOADING: {
                // do nothing
            } break;

            case HAS_DOWNLOADED: {
                play();
            } break;

            case IS_PLAYING: {
                stop();
            } break;

        }

    }

    private void downloadAndPlay() {
        state = IS_DOWNLOADING;

        IndexService indexService = ApiClient.getIndexService();
        indexService.downAudioFrom(audioUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    state = NOT_DOWNLOADED;
                    return ;
                }

                InputStream inputStream = null;
                OutputStream outputStream = null;
                String fileNameString = generalFileName();
                File file = new File(
                        FileUtils.getAppRecordDir(AppContext.getContext()), fileNameString);

                try {
                    inputStream = response.body().byteStream();
                    outputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[4096];
                    int rd = 0;

                    while (true) {
                        rd = inputStream.read(buffer);

                        if (rd == -1) {
                            break;
                        }

                        outputStream.write(buffer, 0, rd);
                    }

                    outputStream.flush();
                } catch (IOException e) {
                    state = NOT_DOWNLOADED;
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }

                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        state = NOT_DOWNLOADED;
                    }
                }

                state = HAS_DOWNLOADED;
                audioLocalFilePath = file.getAbsolutePath();

                play();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                state = NOT_DOWNLOADED;
            }
        });
    }

    private void stop() {
        state = HAS_DOWNLOADED;
        MediaManager.release();
    }

    private void play() {
        state = IS_PLAYING;

        // 播放前重置
        MediaManager.release();

        // 开始实质播放
        MediaManager.playLoacalAudio(audioLocalFilePath,
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // 播放完毕
                        state = HAS_DOWNLOADED;
                    }
                });
    }

    /**
     * 随机生成文件的名称
     *
     * @return
     */
    private String generalFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }


}
