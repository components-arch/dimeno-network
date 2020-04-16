package com.dimeno.network.sample;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.network.callback.LoadingCallback;
import com.dimeno.network.sample.entity.PluginVersion;
import com.dimeno.network.sample.task.TestGetTask;
import com.dimeno.network.sample.task.TestPostFormTask;
import com.dimeno.network.sample.task.TestPostJsonTask;
import com.dimeno.network.sample.task.TestUploadTask;
import com.dimeno.permission.PermissionManager;
import com.dimeno.permission.callback.AbsPermissionCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post_json).setOnClickListener(this);
        findViewById(R.id.btn_post_form).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
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
            case R.id.btn_upload:
                PermissionManager.request(this, new AbsPermissionCallback() {
                    @Override
                    public void onGrant(String[] permissions) {
                        upload();
                    }

                    @Override
                    public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
                        Toast.makeText(MainActivity.this, "需要存储权限", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
    }

    private void upload() {
        String path = Environment.getExternalStorageDirectory() + "/Recorder/sample.mp3";
        new TestUploadTask(new LoadingCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).exe(path);
    }

    private void postForm() {
        new TestPostFormTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onStart() {
                Log.e("TAG", "-> onStart");
            }

            @Override
            public void onSuccess(PluginVersion data) {
                Toast.makeText(MainActivity.this, "Post Form -> " + data.version_name + " " + data.version_description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "Post Form -> " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "-> onComplete");
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
