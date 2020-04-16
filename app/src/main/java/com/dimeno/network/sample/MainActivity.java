package com.dimeno.network.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.network.callback.LoadingCallback;
import com.dimeno.network.sample.entity.PluginVersion;
import com.dimeno.network.sample.task.TestGetTask;
import com.dimeno.network.sample.task.TestPostFormTask;
import com.dimeno.network.sample.task.TestPostJsonTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post_json).setOnClickListener(this);
        findViewById(R.id.btn_post_form).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                get();
                break;
            case R.id.btn_post_json:
                postJson();
                break;
            case R.id.btn_post_form:
                postForm();
                break;
        }
    }

    private void postForm() {
        new TestPostFormTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onSuccess(PluginVersion data) {
                Toast.makeText(MainActivity.this, "Post Form -> " + data.version_name + " " + data.version_description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "Post Form -> " + message, Toast.LENGTH_SHORT).show();
            }
        }).exe();
    }

    private void postJson() {
        new TestPostJsonTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onSuccess(PluginVersion data) {
                Toast.makeText(MainActivity.this, "Post Json -> " + data.version_name + " " + data.version_description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "Post Json -> " + message, Toast.LENGTH_SHORT).show();
            }
        }).exe();
    }

    private void get() {
        new TestGetTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onSuccess(PluginVersion data) {
                Toast.makeText(MainActivity.this, "Get -> " + data.version_name + " " + data.version_description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "Get -> " + message, Toast.LENGTH_SHORT).show();
            }
        }).exe();
    }
}
