package cn.edu.nju.cs.seg.schooledinapp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_schooledin_schooled)
    TextView tvSchooledInSchooled;

    @BindView(R.id.tv_schooledin_in)
    TextView tvSchooledInIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initLogo();

        startApp();
    }

    /**
     * 初始化 logo
     */
    private void initLogo() {
        Typeface font = Typeface.createFromAsset(
                AppContext.getContext().getAssets(),
                AppContext.getContext().getResources().getString(R.string.font_brush_script_std));

        tvSchooledInSchooled.setTypeface(font);
        tvSchooledInIn.setTypeface(font);
    }

    /**
     * 启动 App
     */
    private void startApp() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SignInActivity.startActivityWithParameters(MainActivity.this);
            }
        };
        timer.schedule(task, 3000);
    }

}
