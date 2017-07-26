package cn.edu.nju.cs.seg.schooledinapp.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import cn.edu.nju.cs.seg.schooledinapp.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.fl_test_container);
        setContentView(frameLayout);
    }

}