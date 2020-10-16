package com.dimeno.network.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.network.callback.LoadingCallback;
import com.dimeno.network.loading.DefaultLoadingPage;
import com.dimeno.network.sample.entity.PluginVersion;
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

        new TestPostFormTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onSuccess(PluginVersion data) {
                Toast.makeText(LoadingActivity.this, data.version_description, Toast.LENGTH_SHORT).show();
            }
        }).setLoadingPage(new DefaultLoadingPage(recycler)).exe();
    }
}