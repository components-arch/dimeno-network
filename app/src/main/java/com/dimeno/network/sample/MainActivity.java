package com.dimeno.network.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.network.callback.LoadingCallback;
import com.dimeno.network.sample.entity.PluginVersion;
import com.dimeno.network.sample.task.TestGetTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_get).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                get();
                break;
        }
    }

    private void get() {
        new TestGetTask(new LoadingCallback<PluginVersion>() {
            @Override
            public void onSuccess(PluginVersion data) {
                Log.e("TAG", data.version_name);
            }
        }).exe();
    }
}
