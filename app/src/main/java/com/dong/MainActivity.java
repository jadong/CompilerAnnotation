package com.dong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dong.lib_annotation.BindView;
import com.dong.library.ViewInjector;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);
        tv_text.setText("Hello Compiler");
    }
}
