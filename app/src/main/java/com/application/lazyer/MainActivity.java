package com.application.lazyer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private ImageView mIVLight1;//灯1图标

    private ImageView mIVLight2;//灯2图标

    private RadioGroup mRGLightOn1;//灯1组

    private RadioGroup mRGLightOn2;//灯2组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        mIVLight1 = (ImageView) findViewById(R.id.imgViewLight1);
        mIVLight2 = (ImageView) findViewById(R.id.imgViewLight2);
        mRGLightOn1 = (RadioGroup) findViewById(R.id.radioGroup1);
        mRGLightOn2 = (RadioGroup) findViewById(R.id.radioGroup2);
        mRGLightOn1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.LightOn1:
                        mIVLight1.setBackgroundResource(R.drawable.lighton);
                        break;
                    case R.id.LightOff1:
                        mIVLight1.setBackgroundResource(R.drawable.lightoff);
                        break;
                }
            }
        });
        mRGLightOn2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.LightOn2:
                        mIVLight2.setBackgroundResource(R.drawable.lighton);
                        break;
                    case R.id.LightOff2:
                        mIVLight2.setBackgroundResource(R.drawable.lightoff);
                        break;
                }
            }
        });
    }
}