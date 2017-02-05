package com.android.tao.xhorizontalprogressbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.tao.xhorizontalprogressbar.view.XHorizontalProgressBar;
import com.android.tao.xhorizontalprogressbar.view.XSectionProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private XHorizontalProgressBar mProgressBar;
    private Button mBtnProgress;
    private XSectionProgressBar mSectionProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* mProgressBar = (XHorizontalProgressBar) findViewById(R.id.xhpb);*/
        mBtnProgress = (Button) findViewById(R.id.btn_progress);
        initListener();
        mSectionProgressBar = (XSectionProgressBar) findViewById(R.id.xspb);
        mSectionProgressBar.setMax(100);
        mSectionProgressBar.setProgress(11);
        mSectionProgressBar.setBgColor(Color.parseColor("#3e9e00"));
        mSectionProgressBar.setProgressColor(Color.parseColor("#ffee16"));
    }


    private void initListener() {
        mBtnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = mSectionProgressBar.getProgress();
                Log.e(TAG, "progress" + progress);
                mSectionProgressBar.setProgress(progress + 2);
            }
        });
    }


}
