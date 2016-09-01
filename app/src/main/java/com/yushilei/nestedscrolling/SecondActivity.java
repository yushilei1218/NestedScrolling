package com.yushilei.nestedscrolling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        text = (TextView) findViewById(R.id.aaa);
    }

    public void scroll(View view) {
        Random random = new Random();
        text.scrollTo(random.nextInt(200), random.nextInt(200));
    }
}
