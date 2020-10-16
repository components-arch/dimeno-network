package com.dimeno.network.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.network.loading.DefaultLoadingPage;
import com.dimeno.network.sample.task.TestPostFormTask;

/**
 * loading page activity
 * Created by wangzhen on 2020/10/16.
 */
public class LoadingActivity extends AppCompatActivity {
    private View recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        recycler = findViewById(R.id.recycler);

        new TestPostFormTask(null)
                .setLoadingPage(new DefaultLoadingPage(recycler))
                .exe();
    }
}