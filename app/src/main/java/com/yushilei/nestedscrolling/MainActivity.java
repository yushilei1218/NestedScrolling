package com.yushilei.nestedscrolling;

import android.content.Intent;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private NestedScrollLayout nested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NestedScrollingChildHelper;
//        NestedScrollingChild;
//        NestedScrollingParent;
//        NestedScrollingParentHelper;
//   RecyclerView;
//        NestedScrollView;
        recycler = (RecyclerView) findViewById(R.id.recycler);
        nested = (NestedScrollLayout) findViewById(R.id.nested);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("item+" + i);
        }
        recycler.setAdapter(new Adapter(this, data));
    }

    public void scrollTo(View view) {
        int x = 0;
        int y = new Random().nextInt(400);

        nested.scrollTo(x, y);
    }

    public void jump(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    String TAG = "ScrollBy";

    public void scrollBy(View view) {
        int y = new Random().nextInt(50);
        Log.d(TAG, "y=" + y);
        nested.scrollBy(0, y);
    }
}
