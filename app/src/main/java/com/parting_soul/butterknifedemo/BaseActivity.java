package com.parting_soul.butterknifedemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindColor;

/**
 * @author parting_soul
 * @date 2019-12-17
 */
public class BaseActivity extends AppCompatActivity {

    @BindColor(R.color.colorAccent)
    int colorA;
//
//    @BindView(R.id.bt)
//    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
