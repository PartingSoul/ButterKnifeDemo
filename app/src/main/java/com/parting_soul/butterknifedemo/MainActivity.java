package com.parting_soul.butterknifedemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

//    @BindView(R.id.cl)
//    ConstraintLayout cl;
//
//    @BindView(R.id.bt)
//    Button bt;

    @BindString(R.string.app_name)
    String appName;

    @BindDrawable(R.drawable.ic_launcher_background)
    Drawable icon;

    @BindColor(R.color.colorAccent)
    int color;

    Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View contentView = findViewById(android.R.id.content);
        new ViewHolder(contentView);
    }

    @OnClick(R.id.bt)
    void onClick() {
        Intent intent = new Intent(this,CustomViewInjectActivity.class);
        startActivity(intent);
    }


//    @OnTouch(R.id.bt)
//    void onTouch() {
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    static class ViewHolder {

        @BindView(R.id.bt)
        Button textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
