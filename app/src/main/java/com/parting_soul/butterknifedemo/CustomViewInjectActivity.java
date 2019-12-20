package com.parting_soul.butterknifedemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parting_soul.inject.Inject;
import com.parting_soul.jnject_annotation.OnClick;
import com.parting_soul.jnject_annotation.ViewInject;

/**
 * 自定义控件注入框架
 *
 * @author parting_soul
 * @date 2019-12-19
 */
public class CustomViewInjectActivity extends AppCompatActivity {

    @ViewInject(R.id.bt_test)
    Button bt;

    @ViewInject(R.id.tv_text)
    TextView mTv;

//    @ViewInject(R.id.bt_test)
//    String a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_custom);
        Inject.inject(this);
        mTv.setText("注入后设置信息");
    }

    @OnClick(R.id.bt_test)
    void onClick() {
        Toast.makeText(getApplicationContext(), "按钮被点击 " + bt, Toast.LENGTH_SHORT).show();
    }

}
